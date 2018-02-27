package zuhause;

import zuhause.util.ServerLog;
import zuhause.ws.ApiRouter;

/**
 *
 * @author Eduardo Folly
 */
public class RouterFullScheduler implements Runnable {

    private static final ServerLog LOG = ServerLog.getInstance();

    /**
     *
     */
    @Override
    public void run() {

        try {
            new ApiRouter().getFullConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        LOG.msg(0, "RouterFullScheduler");
    }
}
