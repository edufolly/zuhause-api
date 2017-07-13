package zuhause;

import java.util.Map;
import zuhause.db.DbConfig;
import zuhause.db.PairDao;
import zuhause.util.Config;
import zuhause.util.ServerLog;
import zuhause.ws.ApiArduino;

/**
 *
 * @author Eduardo Folly
 */
public class TempScheduler implements Runnable {

    private static final DbConfig DB_CONFIG = Config.getDbConfig("localhost");
    private static final ServerLog LOG = ServerLog.getInstance();

    /**
     *
     */
    @Override
    public void run() {
        try {
            ApiArduino apiArduino = new ApiArduino();

            Map<String, Object> map = apiArduino.getTempInterna();

            String temp = map.get("t").toString();

            new PairDao(DB_CONFIG).Insert("temp", "interna", temp);

            LOG.msg(0, "TempScheduler");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
