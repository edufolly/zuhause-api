package zuhause;

import zuhause.util.Config;
import zuhause.util.ServerLog;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import zuhause.sunrise.SunriseSunset;
import zuhause.ws.ApiArduino;

/**
 *
 * @author Eduardo Folly
 */
public class Main {

    private final static ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(4);

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        int porta = -1;

        ServerLog serverlog = ServerLog.getInstance();

        try {
            Config config = Config.getInstance();

            for (String arg : args) {
                if (arg.equals("--debug")) {
                    config.setDebug(true);
                    serverlog.msg(0, "Debug");
                    serverlog.msg(0, "Debug");
                    serverlog.msg(0, "Debug");
                }
            }

            porta = config.getTcpPort();

            EndpointCache.init();

            try {
                new ApiArduino().getTempInterna();
            } catch (Exception ex) {

            }

            // TODO - Implementar configurável.
            SCHEDULER.scheduleAtFixedRate(new TempScheduler(),
                    0, 5, TimeUnit.MINUTES);

            SCHEDULER.scheduleWithFixedDelay(Config.getTelegramBot("zuhause_iot_bot"),
                    10, 30, TimeUnit.SECONDS);

            new Thread(new SunriseSunset()).start();

//            Desativado
//            SCHEDULER.scheduleWithFixedDelay(new RouterFullScheduler(),
//                    1, 10, TimeUnit.MINUTES);
//
//            SCHEDULER.scheduleAtFixedRate(new RouterScheduler(),
//                    0, 1, TimeUnit.MINUTES);
//
            ServerSocket server = new ServerSocket(porta, config.getMaxConnections());
            serverlog.msg(0, "API Server aguardando conexões na porta " + porta + ".");

            if (!config.isDebug()) {
                Config.getTelegramBot("zuhause_iot_bot").sendMessage("Zuhause iniciada.");
            }

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
