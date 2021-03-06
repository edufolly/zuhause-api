package zuhause.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zuhause.bot.TelegramBot;
import zuhause.db.DbConfig;
import zuhause.router.Router;
import zuhause.serial.Serial;
import zuhause.sunrise.SunriseSunset;

/**
 *
 * @author Eduardo Folly
 */
public class Config implements Serializable {

    private static final Map<String, String> OS = new HashMap();
    private static final Logger LOGGER = LogManager.getRootLogger();
    private static boolean DEBUG = false;
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private static final OkHttpClient CLIENT
            = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
    //--
    private String cache;
    private int tcpPort;
    private int maxConnections;
    private String appPackage;
    private Map<String, DbConfig> dbConfigs;
    private Map<String, Serial> serialConfigs;
    private Map<String, Router> routerConfigs;
    private Map<String, TelegramBot> telegramBotConfigs;
    private Map<String, SunriseSunset> sunriseSunsetConfigs;
    //--
    private static final Config INSTANCE;

    /**
     *
     */
    static {
        try {
            File file = new File("/etc/os-release");
            BufferedReader b = new BufferedReader(new FileReader(file));

            String readLine;

            while ((readLine = b.readLine()) != null) {
                String[] p = readLine.split("=");
                OS.put(p[0], p[1].replaceAll("\"", ""));
            }
        } catch (Exception ex) {
            LOGGER.error("Não foi possível obter informações do sistema "
                    + "operacional.", ex);
        }

        FileReader fileReader = null;
        try {
            fileReader = new FileReader("config.json");
        } catch (Exception ex) {
            LOGGER.fatal("Arquivo config.json não encontrado.", ex);
        }
        INSTANCE = GSON.fromJson(fileReader, Config.class);
    }

    /**
     *
     * @return
     */
    public static OkHttpClient getHttpClient() {
        return CLIENT;
    }

    /**
     *
     * @return
     */
    public static Gson getGson() {
        return GSON;
    }

    /**
     *
     * @return
     */
    public static int getOsVersionId() {
        return Integer.parseInt(OS.getOrDefault("VERSION_ID", "-1"));
    }

    /**
     *
     * @return
     */
    public static Config getInstance() {
        return INSTANCE;
    }

    /**
     *
     * @param config
     * @return
     */
    public static DbConfig getDbConfig(String config) {
        return INSTANCE.getDbConfigs().get(config);
    }

    /**
     *
     * @param serial
     * @return
     */
    public static Serial getSerial(String serial) {
        return INSTANCE.getSerialConfigs().get(serial);
    }

    /**
     *
     * @param router
     * @return Router
     * @throws Exception
     */
    public static Router getRouter(String router) throws Exception {
        return INSTANCE.getRouterConfigs().get(router).parse();
    }

    /**
     *
     * @param bot
     * @return
     */
    public static TelegramBot getTelegramBot(String bot) {
        return INSTANCE.getTelegramBotConfigs().get(bot);
    }

    /**
     *
     * @return
     */
    public static boolean isDebug() {
        return DEBUG;
    }

    /**
     *
     * @param dbg
     */
    public static void setDebug(boolean dbg) {
        DEBUG = dbg;
    }

    /**
     *
     */
    private Config() {

    }

    /**
     *
     * @return
     */
    public String getCache() {
        return cache;
    }

    /**
     *
     * @param cache
     */
    public void setCache(String cache) {
        this.cache = cache;
    }

    /**
     *
     * @return
     */
    public int getTcpPort() {
        return tcpPort;
    }

    /**
     *
     * @param tcpPort
     */
    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    /**
     *
     * @return
     */
    public int getMaxConnections() {
        return maxConnections;
    }

    /**
     *
     * @param maxConnections
     */
    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    /**
     *
     * @return
     */
    public String getAppPackage() {
        return appPackage;
    }

    /**
     *
     * @param appPackage
     */
    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    /**
     *
     * @return
     */
    public Map<String, DbConfig> getDbConfigs() {
        return dbConfigs;
    }

    /**
     *
     * @param dbConfigs
     */
    public void setDbConfigs(Map<String, DbConfig> dbConfigs) {
        this.dbConfigs = dbConfigs;
    }

    /**
     *
     * @return
     */
    public Map<String, Serial> getSerialConfigs() {
        return serialConfigs;
    }

    /**
     *
     * @param serialConfigs
     */
    public void setSerialConfigs(Map<String, Serial> serialConfigs) {
        this.serialConfigs = serialConfigs;
    }

    /**
     *
     * @return
     */
    public Map<String, Router> getRouterConfigs() {
        return routerConfigs;
    }

    /**
     *
     * @param routerConfigs
     */
    public void setRouterConfigs(Map<String, Router> routerConfigs) {
        this.routerConfigs = routerConfigs;
    }

    /**
     *
     * @return
     */
    public Map<String, TelegramBot> getTelegramBotConfigs() {
        return telegramBotConfigs;
    }

    /**
     *
     * @param telegramBotConfigs
     */
    public void setTelegramBotConfigs(
            Map<String, TelegramBot> telegramBotConfigs) {

        this.telegramBotConfigs = telegramBotConfigs;
    }

    /**
     *
     * @return
     */
    public Map<String, SunriseSunset> getSunriseSunsetConfigs() {
        return sunriseSunsetConfigs;
    }

    /**
     *
     * @param sunriseSunsetConfigs
     */
    public void setSunriseSunsetConfigs(Map<String, SunriseSunset> sunriseSunsetConfigs) {
        this.sunriseSunsetConfigs = sunriseSunsetConfigs;
    }

}
