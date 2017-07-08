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
    private String extra;

    /**
     *
     */
    public DhcpClient() {
    }

    /**
     *
     * @return
     */
    public String getClient() {
        return client;
    }

    /**
     *
     * @param client
     * @return
     */
    public DhcpClient setClient(String client) {
        this.client = client;
        return this;
    }

    /**
     *
     * @return
     */
    public String getMac() {
        return mac;
    }

    /**
     *
     * @param mac
     * @return
     */
    public DhcpClient setMac(String mac) {
        this.mac = mac;
        return this;
    }

    /**
     *
     * @return
     */
    public String getIp() {
        return ip;
    }

    /**
     *
     * @param ip
     * @return
     */
    public DhcpClient setIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     *
     * @return
     */
    public String getExtra() {
        return extra;
    }

    /**
     *
     * @param extra
     * @return
     */
    public DhcpClient setExtra(String extra) {
        this.extra = extra;
        return this;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "DhcpClient{" + "client=" + client + ", mac=" + mac + ", ip=" + ip + ", extra=" + extra + '}';
    }

}
