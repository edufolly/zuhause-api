package zuhause.router;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Eduardo Folly
 */
public class Router implements Serializable {

    private String host;
    private String user;
    private String password;
    private String internalType;

    /**
     *
     */
    public Router() {

    }

    /**
     *
     * @return
     */
    public String getHost() {
        return host;
    }

    /**
     *
     * @param host
     * @return
     */
    public Router setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     *
     * @return
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @param user
     * @return
     */
    public Router setUser(String user) {
        this.user = user;
        return this;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * @return
     */
    public Router setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     *
     * @return
     */
    public String getInternalType() {
        return internalType;
    }

    /**
     *
     * @return
     */
    public Router build() {
        this.internalType = this.getClass().getName();
        return this;
    }

    /**
     *
     * @return @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Router parse() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName(this.internalType);

        Router newRouter = (Router) clazz.newInstance();

        newRouter
                .setHost(this.host)
                .setUser(this.user)
                .setPassword(this.password);

        return newRouter;
    }

    /**
     *
     * @return @throws Exception
     */
    public List<DhcpClient> dhcpList() throws Exception {
        return null;
    }

    /**
     *
     * @return @throws Exception
     */
    public Map<String, String> arpList() throws Exception {
        return null;
    }

    /**
     *
     * @return @throws Exception
     */
    public List<String> getConnected() throws Exception {
        return null;
    }

    /**
     *
     * @param mac
     * @return
     * @throws Exception
     */
    public Map<String, Boolean> devicePause(String mac) throws Exception {
        return null;
    }

    /**
     *
     * @param mac
     * @return
     * @throws Exception
     */
    public Map<String, Boolean> devicePlay(String mac) throws Exception {
        return null;
    }
}
