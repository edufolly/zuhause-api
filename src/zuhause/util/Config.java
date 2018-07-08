package zuhause.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zuhause.bot.TelegramBot;
import zuhause.db.DbConfig;
import zuhause.router.Router;
import zuhause.serial.Serial;

/**
 *
 * @author Eduardo Folly
 */
public class Config {

    private static final Logger LOGGER = LogManager.getRootLogger();
    private transient boolean debug;
    private int tcpPort;
    private int maxConnections;
    private String appPackage;
    private Map<String, DbConfig> dbConfigs;
    private Map<String, Serial> serialConfigs;
    private Map<String, Router> routerConfigs;
    private Map<String, TelegramBot> telegramBotConfigs;
    //--
    private static final transient OkHttpClient CLIENT
            = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
    //--
    private static transient Config INSTANCE = null;

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
    public static Config getInstance() {
        if (INSTANCE == null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try {
                INSTANCE = gson.fromJson(new FileReader("config.json"),
                        Config.class);
            } catch (FileNotFoundException ex) {
                INSTANCE = new Config();
                INSTANCE.setTcpPort(8088);
                INSTANCE.setMaxConnections(5);
                INSTANCE.setAppPackage("zuhause.ws");
                try {
                    String json = gson.toJson(INSTANCE);
                    try (FileOutputStream out
                            = new FileOutputStream("config.json")) {
                        out.write(json.getBytes());
                    }
                } catch (Exception exx) {
                    LOGGER.error(exx.getMessage(), exx);
                }
            }
        }
        return INSTANCE;
    }

    /**
     *
     * @param config
     * @return
     */
    public static DbConfig getDbConfig(String config) {
        return Config.getInstance().getDbConfigs().get(config);
    }

    /**
     *
     * @param serial
     * @return
     */
    public static Serial getSerial(String serial) {
        return Config.getInstance().getSerialConfigs().get(serial);
    }

    /**
     *
     * @param router
     * @return Router
     * @throws Exception
     */
    public static Router getRouter(String router) throws Exception {
        return Config.getInstance().getRouterConfigs().get(router).parse();
    }

    /**
     *
     * @param bot
     * @return
     */
    public static TelegramBot getTelegramBot(String bot) {
        return Config.getInstance().getTelegramBotConfigs().get(bot);
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
    public boolean isDebug() {
        return debug;
    }

    /**
     *
     * @param debug
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
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
}
