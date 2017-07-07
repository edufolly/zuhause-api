package zuhause.router;

import java.io.Serializable;

/**
 *
 * @author Eduardo Folly
 */
public class DhcpClient implements Serializable {

    private String client;
    private String mac;
    private String ip;
    private String time;

    public DhcpClient() {
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
