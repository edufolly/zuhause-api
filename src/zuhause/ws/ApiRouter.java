package zuhause.ws;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import zuhause.annotations.GET;
import zuhause.annotations.Path;
import zuhause.annotations.PathParam;
import zuhause.bot.TelegramBot;
import zuhause.db.DbConfig;
import zuhause.db.Pair;
import zuhause.db.PairDao;
import zuhause.router.DhcpClient;
import zuhause.router.Router;
import zuhause.router.Rule;
import zuhause.util.Config;

/**
 *
 * @author Eduardo Folly
 */
@Path("/api/router")
public class ApiRouter {

    private static final DbConfig DB_CONFIG = Config.getDbConfig("localhost");
    private static final TelegramBot BOT = Config.getTelegramBot("zuhause_iot_bot");

    /**
     *
     */
    class RouterConfig {

        public String ip;
        public String mac;
        public String name;
        public boolean online = false;
        public int idHost = -1;
        public int idRule = -1;
        public boolean paused = false;

        @Override
        public String toString() {
            return "{" + "ip " + ip + ", mac " + mac + ", name " + name + "}";
        }
    }

    /**
     *
     * @return JSON
     * @throws Exception
     */
    @Path("/full_config")
    @GET
    public List<RouterConfig> getFullConfig() throws Exception {

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
                rc.name = client.getName().isEmpty() ? "DHCP Find" : client.getName();
                rc.mac = client.getMac();
                rc.ip = client.getIp();

                dao.Insert("resolve_mac", rc.mac, rc.name);
                BOT.sendMessage("Novo dispositivo encontrado: " + rc.toString());
                lista.put(rc.mac, rc);
            }
        }

        /**
         * Verificação ARP no Raspi.
         */
        Map<String, Object> arp = new ApiRaspiStats().getArp();

        /**
         * Verificação ARP no roteador.
         */
        arp.putAll(router.arpList());

        for (String mac : arp.keySet()) {
            if (lista.containsKey(mac)) {
                RouterConfig rc = lista.get(mac);
                rc.ip = arp.get(mac).toString();
                rc.online = true;
            } else {
                RouterConfig rc = new RouterConfig();
                rc.name = "ARP Find";
                rc.mac = mac;
                rc.ip = arp.get(mac).toString();
                rc.online = true;

                dao.Insert("resolve_mac", rc.mac, rc.name);
                BOT.sendMessage("Novo dispositivo encontrado: " + rc.toString());
                lista.put(rc.mac, rc);
            }
        }

        /**
         * Conectados no roteador.
         */
        Set<String> macs = router.getConnected();
        for (String mac : macs) {
            if (lista.containsKey(mac)) {
                RouterConfig rc = lista.get(mac);
                rc.online = true;
            } else {
                RouterConfig rc = new RouterConfig();
                rc.name = "Router Connected";
                rc.mac = mac;
                rc.ip = "?";
                rc.online = true;

                dao.Insert("resolve_mac", rc.mac, rc.name);
                BOT.sendMessage("Novo dispositivo encontrado: " + rc.toString());
                lista.put(rc.mac, rc);
            }
        }

        /**
         * Hosts cadastrados no roteador.
         */
        List<String> hosts = getHostList();
        for (int i = 0; i < hosts.size(); i++) {
            String host = hosts.get(i);
            if (lista.containsKey(host)) {
                RouterConfig rc = lista.get(host);
                rc.idHost = i;
            }
        }

        /**
         * Regras cadastradas no roteador.
         */
        List<Rule> rules = getRuleList();
        for (Rule rule : rules) {
            if (lista.containsKey(rule.getHost())) {
                RouterConfig rc = lista.get(rule.getHost());
                rc.idRule = rule.getId();
                if (rule.getStatus() == 1) {
                    rc.paused = true;
                }
            }
        }

        /**
         *
         */
        List<RouterConfig> retorno = new ArrayList<>(lista.values());

        Collections.sort(retorno, new Comparator<RouterConfig>() {
            @Override
            public int compare(RouterConfig o1, RouterConfig o2) {
                if (o1.ip == null && o2.ip == null) {
                    return (o1.online + o1.mac).compareTo(o2.online + o2.mac);
                }

                if (o1.ip == null && o2.ip != null) {
                    return 1;
                }

                if (o1.ip != null && o2.ip == null) {
                    return -1;
                }

                String[] ip1 = o1.ip.split("\\.");
                Long i1 = o1.online ? 0 : 10000000000l;
                i1 += Long.parseLong(ip1[0]) * 1000000000l + 1000000000l;
                i1 += Long.parseLong(ip1[1]) * 1000000l + 1000000l;
                i1 += Long.parseLong(ip1[2]) * 1000l + 100l;
                i1 += Long.parseLong(ip1[3]) + 1l;

                String[] ip2 = o2.ip.split("\\.");
                Long i2 = o2.online ? 0 : 10000000000l;
                i2 += Long.parseLong(ip2[0]) * 1000000000l + 1000000000l;
                i2 += Long.parseLong(ip2[1]) * 1000000l + 1000000l;
                i2 += Long.parseLong(ip2[2]) * 1000l + 100l;
                i2 += Long.parseLong(ip2[3]) + 1l;

                return i1.compareTo(i2);
            }
        });

        return retorno;
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
     * @return JSON
     * @throws Exception
     */
    @Path("/arp/list")
    @GET
    public Map<String, Object> getArpList() throws Exception {
        return Config.getRouter("WRN240").arpList();
    }

    /**
     *
     * @return JSON
     * @throws Exception
     */
    @Path("/connected")
    @GET
    public Set<String> getConnected() throws Exception {
        return Config.getRouter("WRN240").getConnected();
    }

    /**
     *
     * @return JSON
     * @throws Exception
     */
    @Path("/host/list")
    @GET
    public List<String> getHostList() throws Exception {
        return Config.getRouter("WRN240").hostList();
    }

    /**
     *
     * @param mac
     * @return JSON
     * @throws Exception
     */
    @Path("/host/create/:mac")
    @GET
    @PathParam({"mac"})
    public List<String> getHostCreate(String mac) throws Exception {
        return Config.getRouter("WRN240").hostCreate(mac);
    }

    /**
     *
     * @return JSON
     * @throws Exception
     */
    @Path("/rule/list")
    @GET
    public List<Rule> getRuleList() throws Exception {
        return Config.getRouter("WRN240").ruleList();
    }

    /**
     *
     * @param mac
     * @return JSON
     * @throws Exception
     */
    @Path("/rule/create/:mac")
    @GET
    @PathParam({"mac"})
    public List<Rule> getRuleCreate(String mac) throws Exception {
        return Config.getRouter("WRN240").ruleCreate(mac);
    }

    /**
     *
     * @param idRule
     * @return JSON
     * @throws Exception
     */
    @Path("/rule/pause/:idRule")
    @GET
    @PathParam({"idRule"})
    public List<Rule> getRulePause(int idRule) throws Exception {
        return Config.getRouter("WRN240").rulePause(idRule);
    }

    /**
     *
     * @param idRule
     * @return JSON
     * @throws Exception
     */
    @Path("/rule/play/:idRule")
    @GET
    @PathParam({"idRule"})
    public List<Rule> getRulePplay(int idRule) throws Exception {
        return Config.getRouter("WRN240").rulePlay(idRule);
    }

}
