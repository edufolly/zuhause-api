package zuhause.util;

import java.net.InetAddress;

/**
 *
 * @author Eduardo Folly
 */
public class ServerLog {

    private static final ServerLog INSTANCE;

    static {
        INSTANCE = new ServerLog();
    }

    public static ServerLog getInstance() {
        return INSTANCE;
    }

    public void conectado(int id, InetAddress address, int port) {
        System.out.println("[" + id + "] - Conectado: " + address + ":" + port);
    }

    public void desconectado(int id) {
        System.out.println("[" + id + "] - Desconectado.");
    }

    public void erro(int id, Throwable t) {
        System.err.println("[" + id + "] - Erro: " + t.getMessage());
        t.printStackTrace();
    }

    public void erro(int id, String msg) {
        System.err.println("[" + id + "] - Erro: " + msg);
    }

//    public void request(int id, HttpRequest request) {
//        System.out.println("[" + id + "] - Request: " + request.getHeaderLine());
//    }
    public void msg(int id, String msg) {
        System.out.println("[" + id + "] - " + msg);
    }

    public void fatal(int id, Throwable t) {
        erro(id, t);
        System.exit(0);
    }

    public void fatal(int id, String msg) {
        erro(id, msg);
        System.exit(0);
    }
}
