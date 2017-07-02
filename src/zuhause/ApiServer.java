package zuhause;

import com.google.gson.Gson;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import zuhause.util.HttpHeaders;
import zuhause.util.HttpStatus;
import zuhause.util.ServerLog;
import zuhause.util.Request;
import zuhause.util.Response;

/**
 *
 * @author Eduardo Folly
 */
public class ApiServer extends Thread {

    private static final Locale locale = new Locale("en", "US");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", locale);
    //--
    private Socket connectedClient = null;

    static {
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
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
            serverlog.conectado(this.hashCode(),
                    connectedClient.getInetAddress(), connectedClient.getPort());

            request = new Request(connectedClient.getInputStream());

            response = new Response(connectedClient.getOutputStream());

            Invokable invokable = EndpointCache.find(request);

            if (invokable == null) {
                throw new Exception("Endpoint não encontrado.\n"
                        + "HTTP Method: " + request.getHttpMethod()
                        + " - Path: " + request.getPath());
            }

            String body = invokable.invoke(request);

            if (body == null) {
                response.setHttpStatus(HttpStatus.SC_NO_CONTENT);
            } else {
                response.setHttpStatus(HttpStatus.SC_OK);
                response.setBody(body);
            }

            response.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");

            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                    "origin, content-type, accept, authorization");

            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
                    "true");

            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                    "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        } catch (Exception ex) {
            serverlog.erro(this.hashCode(), ex);
            try {
                response.setHttpStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                Erro erro = new Erro(ex.getMessage(), sw.toString());
                Gson gson = new Gson();
                response.setBody(gson.toJson(erro));
            } catch (Exception exx) {
            }
        } finally {
            try {
                response.addHeader(HttpHeaders.SERVER, "Zuhause API Server");
                response.flush();
            } catch (Exception exx) {
            }
            serverlog.desconectado(this.hashCode());
        }
    }

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
