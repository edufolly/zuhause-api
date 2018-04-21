package zuhause.ws;

import java.util.ArrayList;
import java.util.Collections;
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
import zuhause.router.RouterConfig;
import zuhause.router.Rule;
import zuhause.util.Config;

/**
 *
 * @author Eduardo Folly
 */
@Path("/api/router")
public class ApiRouter {

    private static final DbConfig DB_CONFIG = Config.getDbConfig("localhost");

    private static final TelegramBot BOT
            = Config.getTelegramBot("zuhause_iot_bot");

    private final PairDao dao = new PairDao(DB_CONFIG);

    /**
     *
     * @return JSON
     * @throws Exception
     */
    @Path("/full_config")
    @GET
    public List<RouterConfig> getFullConfig() throws Exception {

        Router router = Config.getRouter("WRN240");

        Map<String, RouterConfig> list = new HashMap<>();

        /**
         * Nome dos dispositivos.
         */
        List<Pair> pairs = dao.selectTab("resolve_mac");

        for (Pair pair : pairs) {
            list.put(pair.getKey(),
                    new RouterConfig()
                            .setName(pair.getValue())
                            .setMac(pair.getKey()));
        }

        /**
         * Dispositivos ignorados.
         */
        List<Pair> ignored = dao.selectTab("ignore_device");

        /**
         * Lista dos clientes DHCP do roteador.
         */
        dhcpList(router, list, ignored);

        /**
         * Verificação ARP no roteador.
         */
        verifyArp(router, list, ignored);

        /**
         * Conectados no roteador.
         */
        getConnected(router, list, ignored);

        /**
         * Hosts cadastrados no roteador.
         */
        registredHost(list);

        /**
         * Regras cadastradas no roteador.
         */
        rules(list);

        /**
         *
         */
        List<RouterConfig> retorno = new ArrayList<>(list.values());

        Collections.sort(retorno);

        return retorno;
    }

    /**
     *
     * @param router
     * @param list
     * @param ignored
     * @throws Exception
     */
    private void dhcpList(Router router,
            Map<String, RouterConfig> list, List<Pair> ignored)
            throws Exception {

        List<DhcpClient> dhcpList = router.dhcpList();

        for (DhcpClient client : dhcpList) {
            if (list.containsKey(client.getMac())) {
                list.get(client.getMac()).setIp(client.getIp());
            } else {
                RouterConfig rc = new RouterConfig()
                        .setName(client.getName()
                                .isEmpty() ? "DHCP Find" : client.getName())
                        .setMac(client.getMac())
                        .setIp(client.getIp());
                if (!isIgnored(ignored, rc)) {
                    dao.insert("resolve_mac", rc.getMac(), rc.getName());
                    BOT.sendMessage("Novo dispositivo encontrado: "
                            + rc.toString());
                    list.put(rc.getMac(), rc);
                }
            }
        }
    }

    /**
     *
     * @param router
     * @param list
     * @param ignored
     * @throws Exception
     */
    private void verifyArp(Router router,
            Map<String, RouterConfig> list, List<Pair> ignored)
            throws Exception {

        Map<String, Object> arp = new ApiRaspiStats().arpGet();

        arp.putAll(router.arpList());

        for (String mac : arp.keySet()) {
            if (list.containsKey(mac)) {
                list.get(mac)
                        .setIp(arp.get(mac).toString())
                        .setOnline(true);
            } else {
                RouterConfig rc = new RouterConfig()
                        .setName("ARP Find")
                        .setMac(mac)
                        .setIp(arp.get(mac).toString())
                        .setOnline(true);

                if (!isIgnored(ignored, rc)) {
                    dao.insert("resolve_mac", rc.getMac(), rc.getName());
                    BOT.sendMessage("Novo dispositivo encontrado: "
                            + rc.toString());
                    list.put(rc.getMac(), rc);
                }
            }
        }
    }

    /**
     *
     * @param router
     * @param list
     * @param ignored
     * @throws Exception
     */
    private void getConnected(Router router,
            Map<String, RouterConfig> list, List<Pair> ignored)
            throws Exception {

        Set<String> macs = router.getConnected();

        for (String mac : macs) {
            if (list.containsKey(mac)) {
                list.get(mac).setOnline(true);
            } else {
                RouterConfig rc = new RouterConfig()
                        .setName("Desconhecido")
                        .setMac(mac)
                        .setIp("?")
                        .setOnline(true);

                if (!isIgnored(ignored, rc)) {
                    dao.insert("resolve_mac", rc.getMac(), rc.getName());
                    BOT.sendMessage("Novo dispositivo encontrado: " + rc.toString());
                    list.put(rc.getMac(), rc);
                }
            }
        }
    }

    /**
     *
     * @param ignored
     * @param rc
     * @return
     */
    private boolean isIgnored(List<Pair> ignored, RouterConfig rc) {
        for (Pair ignore : ignored) {
            if (ignore.getKey().equalsIgnoreCase("name")) {
                if (rc.getName().equals(ignore.getValue())) {
                    return true;
                }
            } else {
                if (rc.getMac().equals(ignore.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param list
     * @throws Exception
     */
    private void registredHost(Map<String, RouterConfig> list)
            throws Exception {

        List<String> hosts = getHostList();
        for (int i = 0; i < hosts.size(); i++) {
            String host = hosts.get(i);
            if (list.containsKey(host)) {
                list.get(host).setIdHost(i);
            }
        }
    }

    /**
     *
     * @param list
     * @throws Exception
     */
    private void rules(Map<String, RouterConfig> list) throws Exception {
        List<Rule> rules = getRuleList();
        for (Rule rule : rules) {
            if (list.containsKey(rule.getHost())) {
                RouterConfig rc = list.get(rule.getHost());
                rc.setIdRule(rule.getId());
                if (rule.getStatus() == 1) {
                    rc.setPaused(true);
                }
            }
        }
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
