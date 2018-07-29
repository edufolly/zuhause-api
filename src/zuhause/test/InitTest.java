package zuhause.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Eduardo Folly
 */
public class InitTest {

    public static void main(String[] args) {

        boolean test = false;

        for (String arg : args) {
            if ("--test".equals(arg)) {
                test = true;
            }
        }

        if (!test) {
            return;
        }

        System.setProperty("log4j.configurationFile", "log4j2-debug.xml");

        Logger logger = LogManager.getRootLogger();

        logger.info("Iniciando Testes...");

        new ArduinoTest(logger).run();

        logger.info("Testes finalizados.");
    }

}
