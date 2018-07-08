package zuhause;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zuhause.db.DbConfig;
import zuhause.db.PairDao;
import zuhause.util.Config;
import zuhause.ws.ApiArduino;

/**
 *
 * @author Eduardo Folly
 */
public class TempScheduler implements Runnable {

    private static final DbConfig DB_CONFIG = Config.getDbConfig("localhost");
    private static final Logger LOGGER = LogManager.getRootLogger();

    /**
     *
     */
    @Override
    public void run() {
        PairDao dao = new PairDao(DB_CONFIG);

        ApiArduino apiArduino = new ApiArduino();

        // Interna
        try {
            Map<String, Object> map = apiArduino.getTempInterna();
            dao.insert("temp", "interna", map.get("t").toString());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        LOGGER.info("TempScheduler");
    }
}
