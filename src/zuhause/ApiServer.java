package zuhause;

import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import zuhause.util.Config;
import zuhause.util.HttpStatus;
import zuhause.util.ServerLog;
import zuhause.util.Request;
import zuhause.util.Response;

/**
 *
 * @author Eduardo Folly
 */
public class ApiServer extends Thread {

    private static final boolean DEBUG = Config.getInstance().isDebug();
    private static final Locale LOCALE = new Locale("en", "US");
    private static final SimpleDateFormat SDF
            = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", LOCALE);
    //--
    private Socket connectedClient = null;

    static {
        SDF.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     *
     * @param connectedClient
     */
    public ApiServer(Socket connectedClient) {
        this.connectedClient = connectedClient;
    }

    /**
     *
     */
    @Override
    public void run() {
        ServerLog serverlog = ServerLog.getInstance();

        Request request;

        Response response = null;
        assert response != null;

        try {
            if (DEBUG) {
                serverlog.conectado(this.hashCode(),
                        connectedClient.getInetAddress(),
                        connectedClient.getPort());
            }

            request = new Request(connectedClient.getInputStream());

            response = new Response(connectedClient.getOutputStream());

            Invokable invokable = EndpointCache.find(request);

            if (invokable == null) {
                throw new Exception("Endpoint não encontrado.\n"
                        + "HTTP Method: " + request.getHttpMethod()
                        + " - Path: " + request.getPath());
            }

            response = invokable.invoke(request, response);

        } catch (Exception ex) {
            serverlog.erro(this.hashCode(), ex);
            try {
                response.setHttpStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                Erro erro = new Erro(ex.getMessage(), sw.toString());
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                response.setBody(gson.toJson(erro));
            } catch (Exception exx) {
                exx.printStackTrace();
            }
        } finally {
            try {
                response.addHeader(HttpHeaders.SERVER, "Zuhause API Server");
                response.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
                response.flush();
            } catch (Exception exx) {
                exx.printStackTrace();
            }
            
            if (DEBUG) {
                serverlog.desconectado(this.hashCode());
            }
        }
    }

    /**
     *
     */
    class Erro {

        private String mensagem;
        private String stacktrace;

        public Erro() {
        }

        public Erro(String mensagem, String stacktrace) {
            this.mensagem = mensagem;
            this.stacktrace = stacktrace;
        }

        public String getMensagem() {
            return mensagem;
        }

        public void setMensagem(String mensagem) {
            this.mensagem = mensagem;
        }

        public String getStacktrace() {
            return stacktrace;
        }

        public void setStacktrace(String stacktrace) {
            this.stacktrace = stacktrace;
        }
    }
}
