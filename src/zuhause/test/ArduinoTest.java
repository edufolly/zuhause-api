package zuhause.test;

import java.util.Map;
import jssc.SerialPortException;
import org.apache.logging.log4j.Logger;
import zuhause.serial.Serial;
import zuhause.util.Config;

/**
 *
 * @author Eduardo Folly
 */
public class ArduinoTest extends AbstractTest {

    public ArduinoTest(Logger logger) {
        super(logger);
    }

    @Override
    public void run() {
        Config config = Config.getInstance();
        Map<String, Serial> serialConfigs = config.getSerialConfigs();
        for (String name : serialConfigs.keySet()) {
            logger.info("Serial: {}", name);
            Serial serial = serialConfigs.get(name);
            serial.open();
            executaTeste(serial, "$T00#");
            serial.close();
        }
    }

    private void executaTeste(Serial serial, String mensagem) {
        try {
            String ret = executaComando(serial, mensagem, "}");
            logger.info("Teste OK!");
        } catch (Exception ex) {
            logger.error("Erro ao executar teste.", ex);
        }

    }

    private String executaComando(Serial serial, String cmd, String waitFor)
            throws SerialPortException {
        serial.safeWrite(cmd);
        return serial.waitFor(waitFor);
    }

}
