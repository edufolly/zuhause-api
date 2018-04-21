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
         * Nome dos dispositivos.
         */
        List<Pair> pairs = dao.selectTab("resolve_mac");

        /**
         * Dispositivos ignorados.
         */
        List<Pair> ignored = dao.selectTab("ignore_device");

        for (Pair pair : pairs) {
            lista.put(pair.getKey(),
                    new RouterConfig()
                            .setName(pair.getValue())
                            .setMac(pair.getKey()));
        }

        /**
         * Lista dos clientes DHCP do roteador.
         */
        List<DhcpClient> dhcpList = router.dhcpList();

        for (DhcpClient client : dhcpList) {
            if (lista.containsKey(client.getMac())) {
                lista.get(client.getMac()).setIp(client.getIp());
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
                    lista.put(rc.getMac(), rc);
                }
            }
        }

        /**
         * Verificação ARP no Raspi.
         */
        Map<String, Object> arp = new ApiRaspiStats().arpGet();

        /**
         * Verificação ARP no roteador.
         */
        arp.putAll(router.arpList());

        for (String mac : arp.keySet()) {
            if (lista.containsKey(mac)) {
                lista.get(mac)
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
                    lista.put(rc.getMac(), rc);
                }
            }
        }

        /**
         * Conectados no roteador.
         */
        Set<String> macs = router.getConnected();
        for (String mac : macs) {
            if (lista.containsKey(mac)) {
                lista.get(mac).setOnline(true);
            } else {
                RouterConfig rc = new RouterConfig()
                        .setName("Desconhecido")
                        .setMac(mac)
                        .setIp("?")
                        .setOnline(true);

                if (!isIgnored(ignored, rc)) {
                    dao.insert("resolve_mac", rc.getMac(), rc.getName());
                    BOT.sendMessage("Novo dispositivo encontrado: "
                            + rc.toString());
                    lista.put(rc.getMac(), rc);
                }
            }
        }

        /**
         * Hosts cadastrados no roteador.
         */
        List<String> hosts = getHostList();
        for (int i = 0; i < hosts.size(); i++) {
            String host = hosts.get(i);
            if (lista.containsKey(host)) {
                lista.get(host).setIdHost(i);
            }
        }

        /**
         * Regras cadastradas no roteador.
         */
        List<Rule> rules = getRuleList();
        for (Rule rule : rules) {
            if (lista.containsKey(rule.getHost())) {
                RouterConfig rc = lista.get(rule.getHost());
                rc.setIdRule(rule.getId());
                if (rule.getStatus() == 1) {
                    rc.setPaused(true);
                }
            }
        }

        /**
         *
         */
        List<RouterConfig> retorno = new ArrayList<>(lista.values());

        Collections.sort(retorno);

        return retorno;
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
