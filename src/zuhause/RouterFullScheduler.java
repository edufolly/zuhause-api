package zuhause;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zuhause.ws.ApiRouter;

/**
 *
 * @author Eduardo Folly
 */
public class RouterFullScheduler implements Runnable {

    private static final Logger LOGGER = LogManager.getRootLogger();

    /**
     *
     */
    @Override
    public void run() {

        try {
            new ApiRouter().getFullConfig();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        LOGGER.info("RouterFullScheduler");
    }
}
