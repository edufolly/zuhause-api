package zuhause.router;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * @return Router
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Router parse()
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {

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
     * @return JSON
     * @throws Exception
     */
    public List<DhcpClient> dhcpList() throws Exception {
        return null;
    }

    /**
     *
     * @return JSON
     * @throws Exception
     */
    public Map<String, Object> arpList() throws Exception {
        return null;
    }

    /**
     *
     * @return JSON
     * @throws Exception
     */
    public Set<String> getConnected() throws Exception {
        return null;
    }

    /**
     *
     * @return JSON
     * @throws Exception
     */
    public List<String> hostList() throws Exception {
        return null;
    }

    /**
     *
     * @param mac
     * @return JSON
     * @throws Exception
     */
    public List<String> hostCreate(String mac) throws Exception {
        return null;
    }

    /**
     *
     * @return JSON
     * @throws Exception
     */
    public List<Rule> ruleList() throws Exception {
        return null;
    }

    /**
     *
     * @param mac
     * @return JSON
     * @throws Exception
     */
    public List<Rule> ruleCreate(String mac) throws Exception {
        return null;
    }

    /**
     *
     * @param idRule
     * @return JSON
     * @throws Exception
     */
    public List<Rule> rulePause(int idRule) throws Exception {
        return null;
    }

    /**
     *
     * @param idRule
     * @return JSON
     * @throws Exception
     */
    public List<Rule> rulePlay(int idRule) throws Exception {
        return null;
    }

}
