package zuhause.test;

import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import zuhause.ws.ApiRaspiStats;

/**
 *
 * @author Eduardo Folly
 */
public class RaspiStatsTest extends AbstractTest {

    private final ApiRaspiStats api = new ApiRaspiStats();

    /**
     *
     * @param logger
     */
    public RaspiStatsTest(Logger logger) {
        super(logger);
    }

    /**
     *
     */
    @Override
    public void run() {
        diskTest();
        ramTest();
    }

    /**
     *
     */
    private void diskTest() {
        List<Map<String, Object>> list = null;
        try {
            list = api.diskGet();

            notEmptyList(list);

            // TODO - Realizar mais teste.
            // MapUtil.print(logger, list);
        } catch (Exception ex) {
            error(ex, list);
        }
    }

    /**
     *
     */
    private void ramTest() {
        List<Map<String, Object>> list = null;
        try {
            list = api.ramGet();

            notEmptyList(list);

            // TODO - Realizar mais teste.
            // MapUtil.print(logger, list);
        } catch (Exception ex) {
            error(ex, list);
        }
    }

}
