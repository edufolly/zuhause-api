package zuhause.router;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Eduardo Folly
 */
public class Router implements Serializable {

    private String host;
    private String user;
    private String password;
    private String internalType;

    public Router() {

    }

    public String getHost() {
        return host;
    }

    public Router setHost(String host) {
        this.host = host;
        return this;
    }

    public String getUser() {
        return user;
    }

    public Router setUser(String user) {
        this.user = user;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Router setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getInternalType() {
        return internalType;
    }

    public Router build() {
        this.internalType = this.getClass().getName();
        return this;
    }

    public Router parse() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName(this.internalType);

        Router newRouter = (Router) clazz.newInstance();

        newRouter
                .setHost(this.host)
                .setUser(this.user)
                .setPassword(this.password);

        return newRouter;
    }

    public List<DhcpClient> dhcpList() throws IOException {
        return null;
    }

}
