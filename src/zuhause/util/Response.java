package zuhause.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Eduardo Folly
 */
public class Response {

    private static final byte[] RN = "\r\n".getBytes();
    //--
    private String protocol = "HTTP/1.1";
    private String httpStatus = HttpStatus.SC_OK;
    private Map<String, String> headers = new HashMap();
    private StringBuilder body = new StringBuilder();
    private final OutputStream outToClient;

    public Response(OutputStream outToClient) {
        this.outToClient = outToClient;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void append(String string) {
        body.append(string);
    }

    public void setBody(String string) {
        body = new StringBuilder(string);
    }

    public String addHeader(String key, String value) {
        return headers.put(key, value);
    }

    public void flush() throws IOException {

        if (!body.toString().isEmpty()) {
            headers.put(HttpHeaders.CONTENT_LENGTH,
                    String.valueOf(body.toString().getBytes().length));
        }

        outToClient.write(protocol.getBytes());
        outToClient.write(" ".getBytes());
        outToClient.write(httpStatus.getBytes());
        outToClient.write(RN);

        for (String key : headers.keySet()) {
            outToClient.write(key.getBytes());
            outToClient.write(": ".getBytes());
            outToClient.write(headers.get(key).getBytes());
            outToClient.write(RN);
        }

        outToClient.write(RN);

        if (!body.toString().isEmpty()) {
            outToClient.write(body.toString().getBytes());
        }

        outToClient.flush();
        outToClient.close();
    }
}
