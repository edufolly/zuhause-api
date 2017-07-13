package zuhause.ws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import zuhause.annotations.GET;
import zuhause.annotations.Path;
import zuhause.annotations.PathParam;
import zuhause.db.DbConfig;
import zuhause.db.Pair;
import zuhause.db.PairDao;
import zuhause.router.DhcpClient;
import zuhause.router.Router;
import zuhause.util.Config;

/**
 *
 * @author Eduardo Folly
 */
@Path("/api/router")
public class ApiRouter {

    private static final DbConfig DB_CONFIG = Config.getInstance()
            .getDbConfigs().get("localhost");

    /**
     * 
     */
    class RouterConfig {
        public String ip;
        public String mac;
        public String name;
        public boolean online;
    }

    /**
     *
     * @return @throws Exception
     */
    @Path("/full_config")
    @GET
    public Map<String, RouterConfig> getFullConfig() throws Exception {

        PairDao dao = new PairDao(DB_CONFIG);

        Router router = Config.getRouter("WRN240");

        Map<String, RouterConfig> lista = new HashMap<>();

        /**
         * Cadastro no banco de dados.
         */
        List<Pair> pairs = dao.selectTab("resolve_mac");

        for (Pair pair : pairs) {
            RouterConfig rc = new RouterConfig();
            rc.name = pair.getValue();
            rc.mac = pair.getKey();
            lista.put(rc.mac, rc);
        }

        /**
         * Lista dos clientes DHCP do roteador.
         */
        List<DhcpClient> dhcpList = router.dhcpList();

        for (DhcpClient client : dhcpList) {
            if (lista.containsKey(client.getMac())) {
                RouterConfig rc = lista.get(client.getMac());
                rc.ip = client.getIp();
            } else {
                RouterConfig rc = new RouterConfig();
                rc.name = client.getName().isEmpty() ? "?" : client.getName();
                rc.mac = client.getMac();
                rc.ip = client.getIp();

                dao.Insert("resolve_mac", rc.mac, rc.name);

                lista.put(rc.mac, rc);
            }
        }

        /**
         * Verificação ARP no Raspi.
         */
        Map<String, String> arp = new ApiRaspiStats().getArp();

        /**
         * Verificação ARP no roteador.
         */
        arp.putAll(router.arpList());

        for (String mac : arp.keySet()) {
            if (lista.containsKey(mac)) {
                RouterConfig rc = lista.get(mac);
                rc.ip = arp.get(mac);
                rc.online = true;
            } else {
                RouterConfig rc = new RouterConfig();
                rc.name = "?";
                rc.mac = mac;
                rc.ip = arp.get(mac);
                rc.online = true;

                dao.Insert("resolve_mac", rc.mac, rc.name);

                lista.put(rc.mac, rc);
            }
        }

        /**
         * Conectados no roteador.
         */
        List<String> macs = router.getConnected();

        for (String mac : macs) {
            if (lista.containsKey(mac)) {
                RouterConfig rc = lista.get(mac);
                rc.online = true;
            }
        }

        return lista;
    }

    /**
     *
     * @return Lista de sessões DHCP ativas.
     * @throws Exception
     */
    @Path("/dhcp/list")
    @GET
    public List<DhcpClient> getDhcpList() throws Exception {
        return Config.getRouter("WRN240").dhcpList();
    }

    /**
     *
     * @return @throws Exception
     */
    @Path("/arp/list")
    @GET
    public Map<String, String> getArpList() throws Exception {
        return Config.getRouter("WRN240").arpList();
    }

    /**
     *
     * @return Lista de mac address conectados.
     * @throws Exception
     */
    @Path("/connected")
    @GET
    public List<String> getConnected() throws Exception {
        return Config.getRouter("WRN240").getConnected();
    }

    /**
     *
     * @param mac
     * @return
     * @throws Exception
     */
    @Path("/device/pause/:mac")
    @GET
    @PathParam({"mac"})
    public Map<String, Boolean> getDevicePause(String mac) throws Exception {
        return Config.getRouter("WRN240").devicePause(mac);
    }
}
