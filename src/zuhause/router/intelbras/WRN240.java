package zuhause.router.intelbras;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import zuhause.router.DhcpClient;
import zuhause.router.Router;

/**
 *
 * @author Eduardo Folly
 */
public class WRN240 extends Router {

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
     * @return @throws java.io.IOException
     */
    @Override
    public List<DhcpClient> dhcpList() throws IOException {
        if (client == null) {
            create();
        }

        List<DhcpClient> lista = new ArrayList();

        // TODO - Implementar HttpUrl
        Request request = new Request.Builder()
                .url("http://" + getHost() + "/userRpm/AssignedIpAddrListRpm.htm")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String resp = response.body().string();

            int ini = resp.indexOf("var DHCPDynList = new Array");
            int fim = resp.indexOf("</SCRIPT>", ini);

            resp = resp.substring(ini, fim);

            try {
                ScriptEngineManager factory = new ScriptEngineManager();
                ScriptEngine engine = factory.getEngineByName("JavaScript");
                engine.eval(resp);
                ScriptObjectMirror scriptObj = (ScriptObjectMirror) engine.get("DHCPDynList");

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

}
