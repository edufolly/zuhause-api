package zuhause.ws;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import jssc.SerialPortException;
import zuhause.annotations.GET;
import zuhause.annotations.Path;
import zuhause.serial.Serial;
import zuhause.util.BooleanUtil;
import zuhause.util.Config;
import zuhause.util.HttpClient;

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

    private static final String REMOTO = "http://10.0.0.25/";

    private static final Gson GSON = new Gson();

    private static final Type TYPE = new TypeToken< Map<String, Object>>() {
    }.getType();

    /**
     *
     * @return Temperatura
     * @throws SerialPortException
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
     * @return Temperatura
     * @throws IOException
     */
    @Path("/temp/externa")
    @GET
    public Map<String, Object> getTempExterna() throws IOException {
        return GSON.fromJson(HttpClient.get(REMOTO + "temperature"), TYPE);
    }

    /**
     *
     * @return A0 : 0 - 1023
     * @throws IOException
     */
    @Path("/ldr")
    @GET
    public Map<String, Object> getLdrRemoto() throws IOException {
        return GSON.fromJson(HttpClient.get(REMOTO + "analog?0"), TYPE);
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

    /**
     *
     * @return @throws SerialPortException
     */
    @Path("/luz/sala")
    @GET
    public Map<String, Object> getLuzSala() throws SerialPortException {
        return acionarDigital("sala", "5");
    }

    /**
     *
     * @return @throws SerialPortException
     */
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
        int toInt = BooleanUtil.toInt(bool);
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
    public Map<String, Object> acionarDigital(String chave, String pino) throws SerialPortException {
        Boolean status = !STATUS.getOrDefault(chave, Boolean.FALSE);
        return acionarDigital(chave, pino, status);
    }

    /**
     *
     * @param chave
     * @param pino
     * @param status
     * @return
     * @throws SerialPortException
     */
    public Map<String, Object> acionarDigital(String chave, String pino, Boolean status) throws SerialPortException {
        ARDUINO.write("$D" + pino + BooleanUtil.toInt(status) + "#");
        String temp = ARDUINO.waitFor("}");
        STATUS.put(chave, status);
        Map<String, Object> map = GSON.fromJson(temp, TYPE);
        return map;
    }

}
