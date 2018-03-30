package zuhause.util;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Eduardo Folly
 */
public class ServerLog {

    private static final SimpleDateFormat SDF
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final ServerLog INSTANCE;

    /**
     *
     */
    static {
        INSTANCE = new ServerLog();
    }

    /**
     *
     * @return
     */
    public static ServerLog getInstance() {
        return INSTANCE;
    }

    /**
     *
     * @param id
     * @param address
     * @param port
     */
    public void conectado(int id, InetAddress address, int port) {
        System.out.println("[" + SDF.format(new Date()) + "] ["
                + id + "] - Conectado: " + address + ":" + port);
    }

    /**
     *
     * @param id
     */
    public void desconectado(int id) {
        System.out.println("[" + SDF.format(new Date()) + "] ["
                + id + "] - Desconectado.");
    }

    /**
     *
     * @param id
     * @param t
     */
    public void erro(int id, Throwable t) {
        System.out.println("[" + SDF.format(new Date()) + "] ["
                + id + "] - Erro: " + t.getMessage());
        t.printStackTrace();
    }

    /**
     *
     * @param id
     * @param msg
     */
    public void erro(int id, String msg) {
        System.out.println("[" + SDF.format(new Date()) + "] ["
                + id + "] - Erro: " + msg);
    }

    /**
     *
     * @param id
     * @param msg
     */
    public void msg(int id, String msg) {
        System.out.println("[" + SDF.format(new Date()) + "] ["
                + id + "] - " + msg);
    }

    /**
     *
     * @param id
     * @param t
     */
    public void fatal(int id, Throwable t) {
        erro(id, t);
        System.exit(0);
    }

    /**
     *
     * @param id
     * @param msg
     */
    public void fatal(int id, String msg) {
        erro(id, msg);
        System.exit(0);
    }
}
