package zuhause.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
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

    private static final Type TYPE = new TypeToken< Map<String, Object>>() {
    }.getType();
    private final Gson gson = Config.getGson();

    /**
     *
     * @param logger
     */
    public ArduinoTest(Logger logger) {
        super(logger);
    }

    /**
     *
     */
    @Override
    public void run() {
        Config config = Config.getInstance();
        Map<String, Serial> serialConfigs = config.getSerialConfigs();
        for (String name : serialConfigs.keySet()) {
            Serial serial = serialConfigs.get(name);
            serial.open();
            executaTesteTemperatura(serial);
            executaTestePino(serial, "3");
            executaTestePino(serial, "4");
            executaTestePino(serial, "5");
            executaTestePino(serial, "6");
            executaTestePino(serial, "7");
            executaTestePino(serial, "8");
            executaTestePino(serial, "9");
            executaTestePino(serial, "A");
            executaTestePino(serial, "B");
            executaTestePino(serial, "C");
            executaTestePino(serial, "D");
            serial.close();
        }
    }

    /**
     *
     * @param serial
     * @param pino
     */
    private void executaTestePino(Serial serial, String pino) {

        Map<String, Object> map = null;

        String comando = "$D" + pino + "1#";

        try {
            String ret = executaComando(serial, comando, "}");
            map = gson.fromJson(ret, TYPE);
            mapNotEmpty(map);
            mapEqual(map, "s", 200.0);
            mapEqual(map, "m", "D");
            mapEqual(map, "p", pino);
            mapEqual(map, "v", 1.0);
        } catch (Exception ex) {
            error(ex, map);
        }

        comando = "$D" + pino + "0#";

        try {
            String ret = executaComando(serial, comando, "}");
            map = gson.fromJson(ret, TYPE);
            mapNotEmpty(map);
            mapEqual(map, "s", 200.0);
            mapEqual(map, "m", "D");
            mapEqual(map, "p", pino);
            mapEqual(map, "v", 0.0);
        } catch (Exception ex) {
            error(ex, map);
        }
    }

    /**
     *
     * @param serial
     */
    private void executaTesteTemperatura(Serial serial) {
        Map<String, Object> map = null;
        try {
            String ret = executaComando(serial, "$T00#", "}");
            map = gson.fromJson(ret, TYPE);

            mapNotEmpty(map);
            mapEqual(map, "s", 200.0);
            mapBetweenExclusive(map, "t", 5.0, 45.0);
            mapBetweenExclusive(map, "h", 20.0, 70.0);
        } catch (Exception ex) {
            error(ex, map);
        }
    }

    /**
     *
     * @param serial
     * @param cmd
     * @param waitFor
     * @return
     * @throws SerialPortException
     */
    private String executaComando(Serial serial, String cmd, String waitFor)
            throws SerialPortException {
        serial.safeWrite(cmd);
        return serial.waitFor(waitFor);
    }

}
