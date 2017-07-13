package zuhause.ws;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import jssc.SerialPortException;
import zuhause.annotations.GET;
import zuhause.annotations.Path;
import zuhause.serial.Serial;
import zuhause.util.Config;

/**
 *
 * @author Eduardo Folly
 */
@Path("/api")
public class ApiArduino {

    private static final Map<String, Boolean> STATUS = new HashMap();

    private static final String[] PINOS = new String[]{"3", "4", "5", "6", "7",
        "8", "9", "A", "B", "C", "D"};

    private static final Serial ARDUINO = Config.getSerial("arduino");

    private static final Gson GSON = new Gson();

    private static final Type TYPE = new TypeToken< Map<String, Object>>() {
    }.getType();

    /**
     *
     * @return @throws SerialPortException
     */
    @Path("/temp/interna")
    @GET
    public Map<String, Object> getTempInterna() throws SerialPortException {
        ARDUINO.write("$T00#");
        String temp = ARDUINO.waitFor("}");
        Map<String, Object> map = GSON.fromJson(temp, TYPE);
        return map;
    }

    /**
     *
     * @return @throws SerialPortException
     */
    @Path("/luz/suite")
    @GET
    public Map<String, Object> getLuzSuite() throws SerialPortException {
        return acionarDigital("suite", "A");
    }

    /**
     *
     * @return @throws SerialPortException
     */
    @Path("/luz/escritorio")
    @GET
    public Map<String, Object> getLuzEscritorio() throws SerialPortException {
        return acionarDigital("escritorio", "9");
    }

    /**
     *
     * @return @throws SerialPortException
     */
    @Path("/luz/escada_parede")
    @GET
    public Map<String, Object> getLuzEscadaParede() throws SerialPortException {
        return acionarDigital("escada_parede", "8");
    }

    /**
     *
     * @return @throws SerialPortException
     */
    @Path("/luz/escada_teto")
    @GET
    public Map<String, Object> getLuzEscadaTeto() throws SerialPortException {
        return acionarDigital("escada_teto", "7");
    }

    /**
     *
     * @return @throws SerialPortException
     */
    @Path("/luz/cozinha")
    @GET
    public Map<String, Object> getLuzCozinha() throws SerialPortException {
        return acionarDigital("cozinha", "6");
    }

    @Path("/luz/sala")
    @GET
    public Map<String, Object> getLuzSala() throws SerialPortException {
        return acionarDigital("sala", "5");
    }

    @Path("/luz/frente")
    @GET
    public Map<String, Object> getLuzFrente() throws SerialPortException {
        return acionarDigital("frente", "4");
    }

    /**
     *
     * @return @throws SerialPortException
     */
    @Path("/rele/off")
    @GET
    public String getReleOff() throws SerialPortException {
        return todosReles(Boolean.FALSE);
    }

    /**
     *
     * @return @throws SerialPortException
     */
    @Path("/rele/on")
    @GET
    public String getReleOn() throws SerialPortException {
        return todosReles(Boolean.TRUE);
    }

    /**
     *
     * @param bool
     * @return
     * @throws SerialPortException
     */
    private String todosReles(Boolean bool) throws SerialPortException {
        STATUS.clear();
        int toInt = toInt(bool);
        for (String pino : PINOS) {
            try {
                ARDUINO.write("$D" + pino + toInt + "#");
                ARDUINO.waitFor("}");
            } catch (Exception ex) {

            }
        }
        return "OK!";

    }

    /**
     *
     * @param chave
     * @param pino
     * @return
     * @throws SerialPortException
     */
    private Map<String, Object> acionarDigital(String chave, String pino) throws SerialPortException {
        Boolean sts = !STATUS.getOrDefault(chave, Boolean.FALSE);
        ARDUINO.write("$D" + pino + toInt(sts) + "#");
        String temp = ARDUINO.waitFor("}");
        STATUS.put(chave, sts);
        Map<String, Object> map = GSON.fromJson(temp, TYPE);
        return map;
    }

    /**
     *
     * @param bool
     * @return
     */
    private int toInt(Boolean bool) {
        return bool ? 1 : 0;
    }

}
