package zuhause.router.intelbras;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import zuhause.router.DhcpClient;
import zuhause.router.Router;
import zuhause.router.Rule;

/**
 *
 * @author Eduardo Folly
 */
public class WRN240 extends Router {

    private transient static final ScriptEngine ENGINE = new ScriptEngineManager()
            .getEngineByName("JavaScript");

    private transient static final Pattern PATTERN = Pattern
            .compile("([0-9A-F]{2}-){5}([0-9A-F]{2})");

    private transient static OkHttpClient client;

    /**
     *
     * @param url
     * @return String
     * @throws IOException
     */
    private String get(HttpUrl url) throws IOException {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .authenticator(new Authenticator() {
                        @Override
                        public Request authenticate(Route route, Response response) throws IOException {
                            if (response.request().header("Authorization") != null) {
                                return null;
                            }

                            String credential = Credentials.basic(getUser(), getPassword());
                            return response.request().newBuilder()
                                    .header("Authorization", credential)
                                    .build();
                        }
                    })
                    .build();
        }

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("cache-control", "no-cache")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    /**
     *
     * @param html
     * @param arrayName
     * @return ScriptObjectMirror
     * @throws ScriptException
     */
    private ScriptObjectMirror scriptArray(String html, String arrayName)
            throws ScriptException {

        int ini = html.indexOf("var " + arrayName + " = new Array");
        int fim = html.indexOf("</SCRIPT>", ini);

        String resp = html.substring(ini, fim);

        ENGINE.eval(resp);

        return (ScriptObjectMirror) ENGINE.get(arrayName);
    }

    /**
     *
     * @return Lista de DhcpClient
     * @throws Exception
     */
    @Override
    public List<DhcpClient> dhcpList() throws Exception {
        List<DhcpClient> lista = new ArrayList();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(getHost())
                .addPathSegments("userRpm/AssignedIpAddrListRpm.htm")
                .build();

        String html = get(url);

        ScriptObjectMirror scriptObj = scriptArray(html, "DHCPDynList");

        int total = scriptObj.size() / 4;

        for (int i = 0; i < total; i++) {
            int j = 4 * i;
            DhcpClient dhcpClient = new DhcpClient();
            dhcpClient.setClient(scriptObj.get(String.valueOf(j)).toString());
            dhcpClient.setMac(scriptObj.get(String.valueOf(j + 1)).toString());
            dhcpClient.setIp(scriptObj.get(String.valueOf(j + 2)).toString());
            dhcpClient.setExtra(scriptObj.get(String.valueOf(j + 3)).toString());

            lista.add(dhcpClient);
        }

        return lista;
    }

    /**
     *
     * @return JSON
     * @throws Exception
     */
    @Override
    public Map<String, Object> arpList() throws Exception {

        Map<String, Object> mapa = new HashMap();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(getHost())
                .addPathSegments("userRpm/LanArpBindingListRpm.htm")
                .build();

        String html = get(url);

        ScriptObjectMirror scriptObj = scriptArray(html, "arpClientListDyn");

        int total = scriptObj.size() / 3;

        for (int i = 0; i < total; i++) {
            int j = 3 * i;
            mapa.put(scriptObj.get(String.valueOf(j + 1)).toString(),
                    scriptObj.get(String.valueOf(j + 2)).toString());
        }

        return mapa;
    }

    /**
     *
     * @return Mac Address List
     * @throws Exception
     */
    @Override
    public Set<String> getConnected() throws Exception {

        Set<String> connected = new HashSet();

        int pages;

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(getHost())
                .addPathSegments("userRpm/WlanStationRpm.htm")
                .build();

        String html = get(url);

        ScriptObjectMirror scriptObj = scriptArray(html, "wlanHostPara");

        int total = Integer.parseInt(scriptObj.get("0").toString());

        pages = (int) Math.ceil(total / 8.0);

        connected.addAll(getMacFromString(html));

        for (int page = 2; page <= pages; page++) {
            url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(getHost())
                    .addPathSegments("userRpm/WlanStationRpm.htm")
                    .addQueryParameter("Page", String.valueOf(page))
                    .build();

            connected.addAll(getMacFromString(get(url)));
        }

        return connected;
    }

    /**
     *
     * @param string
     * @return List
     */
    private Set<String> getMacFromString(String string) {
        Set<String> macs = new HashSet();
        Matcher matcher = PATTERN.matcher(string);
        while (matcher.find()) {
            macs.add(matcher.group());
        }
        return macs;
    }

    /**
     *
     * @return Lista de host
     * @throws Exception
     */
    @Override
    public List<String> hostList() throws Exception {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(getHost())
                .addPathSegments("userRpm/AccessCtrlHostsListsRpm.htm")
                .build();

        return hostProc(get(url));
    }

    /**
     *
     * @param html
     * @return Lista de host
     * @throws IOException
     */
    private List<String> hostProc(String html) throws Exception {

        int pages;

        List<String> hosts = new ArrayList<>();

        ScriptObjectMirror scriptObj = scriptArray(html, "hosts_lists_page_param");

        int total = Integer.parseInt(scriptObj.get("4").toString());

        pages = (int) Math.ceil(total / 8.0);

        hosts.addAll(hostProcPage(html));

        for (int page = 2; page <= pages; page++) {
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(getHost())
                    .addPathSegments("userRpm/AccessCtrlHostsListsRpm.htm")
                    .addQueryParameter("Page", String.valueOf(page))
                    .build();

            hosts.addAll(hostProcPage(get(url)));
        }

        return hosts;
    }

    /**
     *
     * @param html
     * @return Lista de host
     * @throws Exception
     */
    private List<String> hostProcPage(String html) throws Exception {

        List<String> hosts = new ArrayList<>();

        ScriptObjectMirror scriptObj = scriptArray(html, "hosts_lists_data_param");

        int total = scriptObj.size() / 5;

        for (int i = 0; i < total; i++) {
            int j = 5 * i;
            hosts.add(scriptObj.get(String.valueOf(j + 4)).toString());
        }

        return hosts;
    }

    /**
     *
     * @param mac
     * @return Lista de host
     * @throws Exception
     */
    @Override
    public List<String> hostCreate(String mac) throws Exception {

        // TODO - Validar mac
        mac = mac.toUpperCase();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(getHost())
                .addPathSegments("userRpm/AccessCtrlHostsListsRpm.htm")
                .encodedQuery("addr_type=0&hosts_lists_name=" + mac)
                .addQueryParameter("src_ip_start", "")
                .addQueryParameter("src_ip_end", "")
                .addQueryParameter("mac_addr", mac)
                .addQueryParameter("Changed", "0")
                .addQueryParameter("SelIndex", "0")
                .addQueryParameter("fromAdd", "0")
                .addQueryParameter("Page", "1")
                .addQueryParameter("Save", "Salvar")
                .build();

        return hostProc(get(url));
    }

    /**
     *
     * @return Lista de regras
     * @throws Exception
     */
    @Override
    public List<Rule> ruleList() throws Exception {

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(getHost())
                .addPathSegments("userRpm/AccessCtrlAccessRulesRpm.htm")
                .build();

        return ruleProc(get(url));
    }

    /**
     *
     * @param html
     * @return Lista de regras
     * @throws Exception
     */
    private List<Rule> ruleProc(String html) throws Exception {

        int pages;

        List<Rule> rules = new ArrayList();

        ScriptObjectMirror scriptObj = scriptArray(html, "access_rules_page_param");

        int total = Integer.parseInt(scriptObj.get("4").toString());

        pages = (int) Math.ceil(total / 8.0);

        rules.addAll(ruleProcPage(html));

        for (int page = 2; page <= pages; page++) {
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(getHost())
                    .addPathSegments("userRpm/AccessCtrlAccessRulesRpm.htm")
                    .addQueryParameter("Page", String.valueOf(page))
                    .build();

            rules.addAll(ruleProcPage(get(url)));
        }

        return rules;
    }

    /**
     *
     * @param html
     * @return
     */
    private List<Rule> ruleProcPage(String html) throws Exception {
        List<Rule> rules = new ArrayList();

        ScriptObjectMirror scriptObj = scriptArray(html, "access_rules_data_param");

        int total = scriptObj.size() / 8;

        for (int i = 0; i < total; i++) {
            int j = 8 * i;

            Rule rule = new Rule();
            rule.setName(scriptObj.get(String.valueOf(j)).toString());
            rule.setIdHost(Integer.parseInt(scriptObj.get(String.valueOf(j + 1)).toString()));
            rule.setIdDestination(Integer.parseInt(scriptObj.get(String.valueOf(j + 2)).toString()));
            rule.setIdSchedule(Integer.parseInt(scriptObj.get(String.valueOf(j + 3)).toString()));
            rule.setHost(scriptObj.get(String.valueOf(j + 4)).toString());
            rule.setDestination(scriptObj.get(String.valueOf(j + 5)).toString());
            rule.setSchedule(scriptObj.get(String.valueOf(j + 6)).toString());
            rule.setStatus(Integer.parseInt(scriptObj.get(String.valueOf(j + 7)).toString()));

            rules.add(rule);
        }

        return rules;
    }

}
