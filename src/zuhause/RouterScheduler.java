package zuhause;

import java.util.List;
import zuhause.db.DbConfig;
import zuhause.db.Pair;
import zuhause.db.PairDao;
import zuhause.util.Config;
import zuhause.util.ServerLog;
import zuhause.ws.ApiRouter;

/**
 *
 * @author Eduardo Folly
 */
public class RouterScheduler implements Runnable {

    private static final DbConfig DB_CONFIG = Config.getInstance().getDbConfigs().get("localhost");
    private static final ServerLog LOG = ServerLog.getInstance();

    @Override
    public void run() {
        try {
            PairDao dao = new PairDao(DB_CONFIG);

            List<Pair> pairs = dao.selectTab("monitora_mac");

            if (!pairs.isEmpty()) {
                ApiRouter router = new ApiRouter();

                List<String> macs = router.getConnected();

                for (Pair pair : pairs) {

                    String mac = pair.getKey();

                    List<Pair> p = dao.select("`tab` = ? AND `key` = ?",
                            new String[]{"mac_status", mac},
                            "`when` DESC", "1");

                    boolean save;
                    boolean ativo;

                    if (p.isEmpty()) {
                        save = true;
                    } else {
                        ativo = stringToBoolean(p.get(0).getValue());
                        save = macs.contains(mac) != ativo;
                    }

                    if (save) {
                        ativo = macs.contains(mac);
                        dao.Insert("mac_status", mac, booleanToString(ativo));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        LOG.msg(0, "RouterScheduler");
    }

    private boolean stringToBoolean(String s) {
        return s.equals("1");
    }

    private String booleanToString(boolean b) {
        return b ? "1" : "0";
    }
}
