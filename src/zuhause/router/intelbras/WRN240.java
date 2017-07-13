package zuhause.router.intelbras;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import zuhause.router.DhcpClient;
import zuhause.router.Router;

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
     */
    private void create() {
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

    /**
     *
     * @return @throws Exception
     */
    @Override
    public List<DhcpClient> dhcpList() throws Exception {
        if (client == null) {
            create();
        }

        List<DhcpClient> lista = new ArrayList();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(getHost())
                .addPathSegments("userRpm/AssignedIpAddrListRpm.htm")
                .build();

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String resp = response.body().string();

            int ini = resp.indexOf("var DHCPDynList = new Array");
            int fim = resp.indexOf("</SCRIPT>", ini);

            resp = resp.substring(ini, fim);

            try {
                ENGINE.eval(resp);
                ScriptObjectMirror scriptObj = (ScriptObjectMirror) ENGINE.get("DHCPDynList");

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
            } catch (Exception ex) {
                throw new IOException("WRN240", ex);
            }
        }

        return lista;
    }

    /**
     *
     * @return @throws Exception
     */
    @Override
    public Map<String, String> arpList() throws Exception {
        if (client == null) {
            create();
        }

        Map<String, String> mapa = new HashMap();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(getHost())
                .addPathSegments("userRpm/LanArpBindingListRpm.htm")
                .build();

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String resp = response.body().string();

            int ini = resp.indexOf("var arpClientListDyn = new Array");
            int fim = resp.indexOf("</SCRIPT>", ini);

            resp = resp.substring(ini, fim);

            try {
                ENGINE.eval(resp);
                ScriptObjectMirror scriptObj = (ScriptObjectMirror) ENGINE.get("arpClientListDyn");

                int total = scriptObj.size() / 3;

                for (int i = 0; i < total; i++) {
                    int j = 3 * i;
                    mapa.put(scriptObj.get(String.valueOf(j + 1)).toString(),
                            scriptObj.get(String.valueOf(j + 2)).toString());
                }
            } catch (Exception ex) {
                throw new IOException("WRN240", ex);
            }
        }

        return mapa;
    }

    /**
     *
     * @return @throws Exception
     */
    @Override
    public List<String> getConnected() throws Exception {
        if (client == null) {
            create();
        }

        List<String> connected = new ArrayList();

        int pages = 0;

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(getHost())
                .addPathSegments("userRpm/WlanStationRpm.htm")
                .build();

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String resp = response.body().string();

            Document doc = Jsoup.parse(resp);

            Elements scriptElements = doc.getElementsByTag("script");

            Element element = scriptElements.get(0);

            ENGINE.eval(scriptToString(element));

            ScriptObjectMirror scriptObj = (ScriptObjectMirror) ENGINE.get("wlanHostPara");

            int total = Integer.parseInt(scriptObj.get("0").toString());

            pages = (int) Math.ceil(total / 8.0);

            connected.addAll(getMacFromString(resp));
        }

        for (int page = 2; page <= pages; page++) {
            url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(getHost())
                    .addPathSegments("userRpm/WlanStationRpm.htm")
                    .addQueryParameter("Page", String.valueOf(page))
                    .build();

            request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                connected.addAll(getMacFromString(response.body().string()));
            }
        }

        return connected;
    }

    /**
     *
     * @param script
     * @return
     */
    private String scriptToString(Element script) {
        StringBuilder sb = new StringBuilder();
        for (DataNode node : script.dataNodes()) {
            sb.append(node.getWholeData());
        }
        return sb.toString();
    }

    /**
     *
     * @param string
     * @return
     */
    private List<String> getMacFromString(String string) {
        List<String> macs = new ArrayList();
        Matcher matcher = PATTERN.matcher(string);
        while (matcher.find()) {
            macs.add(matcher.group());
        }
        return macs;
    }
}
