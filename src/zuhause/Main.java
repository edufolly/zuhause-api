package zuhause;

import zuhause.util.Config;
import zuhause.util.ServerLog;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Eduardo Folly
 */
public class Main {

    private final static ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        int porta = -1;

        ServerLog serverlog = ServerLog.getInstance();

        try {
            Config config = Config.getInstance();

            porta = config.getTcpPort();

            EndpointCache.init();
/*
            // TODO - Implementar configurável.
            SCHEDULER.scheduleAtFixedRate(new TempScheduler(),
                    0, 5, TimeUnit.MINUTES);
*/
            ServerSocket server = new ServerSocket(porta, config.getMaxConnections());
            serverlog.msg(0, "Start");
            serverlog.msg(0, "Start");
            serverlog.msg(0, "Start");
            serverlog.msg(0, "API Server aguardando conexões na porta " + porta + ".");

            while (true) {
                Socket connection = server.accept();
                (new ApiServer(connection)).start();
            }
        } catch (java.net.BindException ex) {
            serverlog.fatal(0, "A porta " + porta
                    + " está em uso por outro programa. ("
                    + ex.getMessage() + ")");
        } catch (Exception ex) {
            serverlog.fatal(0, ex);
        }

    }

}
