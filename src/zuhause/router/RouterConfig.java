package zuhause.router;

/**
 *
 * @author Eduardo Folly
 */
public class RouterConfig implements Comparable {

    private String ip;
    private String mac;
    private String name;
    private boolean online = false;
    private int idHost = -1;
    private int idRule = -1;
    private boolean paused = false;

    /**
     *
     */
    public RouterConfig() {
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
    public RouterConfig setIp(String ip) {
        this.ip = ip;
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
    public RouterConfig setMac(String mac) {
        this.mac = mac;
        return this;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * @return
     */
    public RouterConfig setName(String name) {
        this.name = name;
        return this;
    }

    /**
     *
     * @return
     */
    public boolean isOnline() {
        return online;
    }

    /**
     *
     * @param online
     * @return
     */
    public RouterConfig setOnline(boolean online) {
        this.online = online;
        return this;
    }

    /**
     *
     * @return
     */
    public int getIdHost() {
        return idHost;
    }

    /**
     *
     * @param idHost
     * @return
     */
    public RouterConfig setIdHost(int idHost) {
        this.idHost = idHost;
        return this;
    }

    /**
     *
     * @return
     */
    public int getIdRule() {
        return idRule;
    }

    /**
     *
     * @param idRule
     * @return
     */
    public RouterConfig setIdRule(int idRule) {
        this.idRule = idRule;
        return this;
    }

    /**
     *
     * @return
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     *
     * @param paused
     * @return
     */
    public RouterConfig setPaused(boolean paused) {
        this.paused = paused;
        return this;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "{" + "ip " + ip + ", mac " + mac + ", name " + name + "}";
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {

        RouterConfig o2 = (RouterConfig) o;

        if (this.getIp() == null && o2.getIp() == null) {
            return (this.isOnline() + this.getMac())
                    .compareTo(o2.isOnline() + o2.getMac());
        }

        if (this.getIp() == null && o2.getIp() != null) {
            return 1;
        }

        if (this.getIp() != null && o2.getIp() == null) {
            return -1;
        }

        String[] ip1 = this.getIp().split("\\.");
        Long i1 = toLong(this.isOnline() ? 0 : 10000000000l, ip1);

        String[] ip2 = o2.getIp().split("\\.");
        Long i2 = toLong(o2.isOnline() ? 0 : 10000000000l, ip2);

        return i1.compareTo(i2);
    }

    /**
     *
     * @param i
     * @param ip
     * @return
     */
    private Long toLong(Long i, String[] ip) {
        Long ret = i;
        ret += Long.parseLong(ip[0]) * 1000000000l + 1000000000l;
        ret += Long.parseLong(ip[1]) * 1000000l + 1000000l;
        ret += Long.parseLong(ip[2]) * 1000l + 100l;
        ret += Long.parseLong(ip[3]) + 1l;
        return ret;
    }

}
