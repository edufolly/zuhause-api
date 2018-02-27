package zuhause.util;

import com.google.common.net.HttpHeaders;
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
    private final Map<String, String> headers = new HashMap();
    private StringBuilder body = new StringBuilder();
    private final OutputStream outToClient;

    /**
     *
     * @param outToClient
     */
    public Response(OutputStream outToClient) {
        this.outToClient = outToClient;
    }

    /**
     *
     * @return
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     *
     * @param protocol
     * @return
     */
    public Response setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    /**
     *
     * @return
     */
    public String getHttpStatus() {
        return httpStatus;
    }

    /**
     *
     * @param httpStatus
     * @return
     */
    public Response setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    /**
     *
     * @param body
     * @return
     */
    public Response setBody(String body) {
        this.body = new StringBuilder(body);
        return this;
    }

    /**
     *
     * @param string
     * @return
     */
    public Response append(String string) {
        this.body.append(string);
        return this;
    }

    /**
     *
     * @return
     */
    public Response clearBody() {
        body = new StringBuilder();
        return this;
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public String addHeader(String key, String value) {
        return headers.put(key, value);
    }

    /**
     *
     */
    public void clearHeaders() {
        headers.clear();
    }

    /**
     *
     * @throws IOException
     */
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
