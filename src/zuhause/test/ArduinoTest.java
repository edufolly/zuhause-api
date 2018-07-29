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
            serial.close();
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

            notEmptyMap(map);

            Object s = map.get("s");
            if (s instanceof Double) {
                Double d = (Double) s;
                if (d != 200.0) {
                    throw new TestErrorException("s diferente de 200.");
                }
            } else {
                throw new TestErrorException("s não é double.");
            }

            Object t = map.get("t");
            if (t instanceof Double) {
                Double d = (Double) t;
                if (d < 5 || d > 45) {
                    throw new TestErrorException("t < 5 ou t > 45.");
                }
            } else {
                throw new TestErrorException("t não é double.");
            }

            Object h = map.get("h");
            if (h instanceof Double) {
                Double d = (Double) h;
                if (d < 20 || d > 50) {
                    throw new TestErrorException("h < 20 ou h > 50.");
                }
            } else {
                throw new TestErrorException("h não é double.");
            }
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
