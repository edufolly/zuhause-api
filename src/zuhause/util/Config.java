package zuhause.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Map;
import zuhause.db.DbConfig;
import zuhause.serial.Serial;

/**
 *
 * @author Eduardo Folly
 */
public class Config {

    private int tcpPort;
    private int wsPort;
    private int maxConnections;
    private String appPackage;
    private Map<String, DbConfig> dbConfigs;
    private Map<String, Serial> serialConfigs;

    private static transient Config INSTANCE = null;

    /**
     *
     * @return
     */
    public static Config getInstance() {
        if (INSTANCE == null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try {
                INSTANCE = gson.fromJson(new FileReader("config.json"), Config.class);
            } catch (FileNotFoundException ex) {
                INSTANCE = new Config();
                INSTANCE.setTcpPort(8088);
                INSTANCE.setMaxConnections(5);
                INSTANCE.setAppPackage("zuhause.ws");
                try {
                    String json = gson.toJson(INSTANCE);
                    FileOutputStream out = new FileOutputStream("config.json");
                    out.write(json.getBytes());
                    out.close();
                } catch (Exception exx) {
                    ex.printStackTrace();
                }
            }
        }
        return INSTANCE;
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

    public int getWsPort() {
        return wsPort;
    }

    public void setWsPort(int wsPort) {
        this.wsPort = wsPort;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public Map<String, DbConfig> getDbConfigs() {
        return dbConfigs;
    }

    public void setDbConfigs(Map<String, DbConfig> dbConfigs) {
        this.dbConfigs = dbConfigs;
    }

    public Map<String, Serial> getSerialConfigs() {
        return serialConfigs;
    }

    public void setSerialConfigs(Map<String, Serial> serialConfigs) {
        this.serialConfigs = serialConfigs;
    }

}
