package zuhause.util;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Eduardo Folly
 */
public class ServerLog {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final ServerLog INSTANCE;

    static {
        INSTANCE = new ServerLog();
    }

    public static ServerLog getInstance() {
        return INSTANCE;
    }

    public void conectado(int id, InetAddress address, int port) {
        System.out.println("[" + SDF.format(new Date()) + "] ["
                + id + "] - Conectado: " + address + ":" + port);
    }

    public void desconectado(int id) {
        System.out.println("[" + SDF.format(new Date()) + "] ["
                + id + "] - Desconectado.");
    }

    public void erro(int id, Throwable t) {
        System.out.println("[" + SDF.format(new Date()) + "] ["
                + id + "] - Erro: " + t.getMessage());
        t.printStackTrace();
    }

    public void erro(int id, String msg) {
        System.out.println("[" + SDF.format(new Date()) + "] ["
                + id + "] - Erro: " + msg);
    }

    public void msg(int id, String msg) {
        System.out.println("[" + SDF.format(new Date()) + "] ["
                + id + "] - " + msg);
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
