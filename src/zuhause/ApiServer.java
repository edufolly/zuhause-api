package zuhause;

import com.google.common.net.HttpHeaders;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import zuhause.util.HttpStatus;
import zuhause.util.InvokableException;
import zuhause.util.Request;
import zuhause.util.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zuhause.util.Config;

/**
 *
 * @author Eduardo Folly
 */
public class ApiServer extends Thread {

    private static final Logger LOGGER = LogManager.getRootLogger();
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
        Request request;

        Response response = null;
        assert response != null;

        try {
            LOGGER.info("Conectado: {}:{}",
                    connectedClient.getInetAddress(),
                    connectedClient.getPort());

            response = new Response(connectedClient.getOutputStream());

            request = new Request(connectedClient.getInputStream());

            Invokable invokable = EndpointCache.find(request);

            if (invokable == null) {
                throw new InvokableException("Endpoint não encontrado.\n"
                        + "HTTP Method: " + request.getHttpMethod()
                        + " - Path: " + request.getPath());
            }

            response = invokable.invoke(request, response);

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            try {
                response.setHttpStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                Erro erro = new Erro(ex.getMessage(), sw.toString());
                response.setBody(Config.getGson().toJson(erro));
            } catch (Exception exx) {
                LOGGER.error(exx.getMessage(), exx);
            }
        } finally {
            try {
                response.addHeader(HttpHeaders.SERVER, "Zuhause API Server");
                response.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
                response.flush();
            } catch (Exception exx) {
                LOGGER.error(exx.getMessage(), exx);
            }

            LOGGER.info("Desconectado.");
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
