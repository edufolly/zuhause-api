package zuhause;

import zuhause.util.Config;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import zuhause.sunrise.SunriseSunset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zuhause.serial.Serial;

/**
 *
 * @author Eduardo Folly
 */
public class Main {

    private final static ScheduledExecutorService SCHEDULER
            = Executors.newScheduledThreadPool(2);

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        boolean debug = false;

        for (String arg : args) {
            if ("--debug".equals(arg)) {
                debug = true;
            }
        }

        if (debug) {
            System.setProperty("log4j.configurationFile", "log4j2-debug.xml");
        } else {
            System.setProperty("log4j.configurationFile", "log4j2.xml");
        }

        Logger logger = LogManager.getRootLogger();

        Config.setDebug(debug);

        int porta = -1;

        try {
            Config config = Config.getInstance();

            Map<String, Serial> serialConfigs = config.getSerialConfigs();
            for (String name : serialConfigs.keySet()) {
                Serial serial = serialConfigs.get(name);
                logger.info("Abrindo serial {} em {}.", name, serial.getDev());
                serial.open();
            }

            porta = config.getTcpPort();

            ServerSocket server = new ServerSocket(porta,
                    config.getMaxConnections());

            logger.info("API Server aguardando conexões na porta {}.", porta);

            EndpointCache.init();

            // TODO - Implementar configurável.
            SCHEDULER.scheduleAtFixedRate(new TempScheduler(),
                    1, 5, TimeUnit.MINUTES);

            SCHEDULER.scheduleWithFixedDelay(Config
                    .getTelegramBot("default"),
                    30, 30, TimeUnit.SECONDS);

            if (!Config.isDebug()) {
                Config.getTelegramBot("default")
                        .sendMessage("Zuhause iniciada.");
            }

            for (SunriseSunset sunriseSunset : config
                    .getSunriseSunsetConfigs().values()) {

                new Thread(sunriseSunset).start();
            }

//            Desativado
//            SCHEDULER.scheduleWithFixedDelay(new RouterFullScheduler(),
//                    1, 10, TimeUnit.MINUTES);
//
//            SCHEDULER.scheduleAtFixedRate(new RouterScheduler(),
//                    0, 1, TimeUnit.MINUTES);
//
            while (true) {
                Socket connection = server.accept();
                (new ApiServer(connection)).start();
            }
        } catch (java.net.BindException ex) {
            logger.fatal("A porta {} está em uso por outro programa. ({})",
                    porta, ex.getMessage());
            System.exit(0);
        } catch (Exception ex) {
            logger.fatal(ex.getMessage(), ex);
            System.exit(0);
        }

    }

}
