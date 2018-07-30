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

            listNotEmpty(list);

            // TODO - Realizar mais teste.
            // print(list);
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

            listSizeEqual(list, 1);

            Map map = list.get(0);

            mapHasKey(map, "shared");
            mapHasKey(map, "total");
            mapHasKey(map, "available");
            mapHasKey(map, "used");
            mapHasKey(map, "free");
            mapHasKey(map, "buff/cache");

        } catch (Exception ex) {
            error(ex, list);
        }
    }

}
