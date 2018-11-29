package zuhause.util;

import com.google.common.hash.Hashing;
import com.google.common.net.HttpHeaders;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author Eduardo Folly
 */
public class Request {

    private String httpMethod;
    private String path;
    private String protocol;
    private String rawqs = "";
    private Map<String, String> queryString;
    private Map<String, String> headers;
    private String body;

    /**
     *
     * @param inputStream
     * @throws MalformedRequestException
     * @throws IOException
     * @throws TimeoutException
     */
    public Request(InputStream inputStream)
            throws MalformedRequestException, IOException, TimeoutException {

        this(inputStream, 20000);
    }

    /**
     *
     * @param inputStream
     * @param timeout
     * @throws MalformedRequestException
     * @throws IOException
     * @throws TimeoutException
     */
    public Request(InputStream inputStream, long timeout)
            throws MalformedRequestException, IOException, TimeoutException {

        BufferedReader inFromClient = new BufferedReader(
                new InputStreamReader(inputStream));

        headers = new HashMap();

        List<String> requestArray = new ArrayList();

        String line;
        while ((line = inFromClient.readLine()) != null) {
            if (line.trim().equals("")) {
                break;
            }

            requestArray.add(line);
            String[] p = line.split(":", 2);

            if (p.length > 1) {
                headers.put(p[0].toLowerCase(), p[1].trim());
            }
        }

        if (requestArray.isEmpty()) {
            throw new MalformedRequestException("Request String empty. Connection refused.");
        }

        String[] stline = requestArray.get(0).split(" ");

        if (stline.length < 3) {
            throw new MalformedRequestException("HTTP protocol standards not applied.");
        }

        httpMethod = stline[0];
        path = stline[1];
        protocol = stline[2];

        String[] split = path.split("\\?", 2);

        if (split.length == 2) {
            rawqs = split[1];
        }

        path = split[0];

        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        if (!path.endsWith("/")) {
            path += "/";
        }

        queryString = new HashMap();
        for (String s : rawqs.split("&")) {
            String[] splt = s.split("=", 2);
            String key = URLDecoder.decode(splt[0], "UTF-8");
            String value = null;
            if (splt.length == 2) {
                value = URLDecoder.decode(splt[1], "UTF-8");
            }
            queryString.put(key, value);
        }

        if (headers.containsKey(HttpHeaders.CONTENT_LENGTH.toLowerCase())) {
            int length = Integer.parseInt(headers
                    .get(HttpHeaders.CONTENT_LENGTH.toLowerCase()));
            long start = System.currentTimeMillis();
            char[] raw = new char[length];
            int readed = 0;
            while (readed < length) {
                if (System.currentTimeMillis() - start > timeout) {
                    throw new TimeoutException("Content length incomplete ("
                            + readed + "/" + length + ").");
                }
                if (inFromClient.ready()) {
                    int read = inFromClient.read(raw, readed, length - readed);
                    if (read < 1) {
                        break;
                    }
                    readed += read;
                } else {
                    break;
                }
            }
            body = String.valueOf(raw).trim();
        }
    }

    /**
     *
     * @return
     */
    public String getHttpMethod() {
        return httpMethod;
    }

    /**
     *
     * @return
     */
    public String getPath() {
        return path;
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
     * @return
     */
    public Map<String, String> getQueryString() {
        return queryString;
    }

    /**
     *
     * @return
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     *
     * @return
     */
    public String getBody() {
        return body;
    }

    /**
     *
     * @param header
     * @return
     */
    public String getHeader(String header) {
        return headers.get(header);
    }

    /**
     *
     * @param key
     * @return
     */
    public String getQueryString(String key) {
        return queryString.get(key);
    }

    /**
     *
     * @param key
     * @return
     */
    public boolean containsQueryString(String key) {
        return queryString.containsKey(key);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("===== REQUEST =====\n");

        sb.append("Protocol: ");
        sb.append(getProtocol());
        sb.append("\n");

        sb.append("Path: ");
        sb.append(getPath());
        sb.append("\n");

        sb.append("Method: ");
        sb.append(getHttpMethod());
        sb.append("\n");

        sb.append("Headers: \n");

        for (String key : headers.keySet()) {
            sb.append("\t");
            sb.append(key);
            sb.append(" => ");
            sb.append(headers.get(key));
            sb.append("\n");
        }

        sb.append("Query String: \n");

        for (String key : queryString.keySet()) {
            sb.append("\t");
            sb.append(key);
            sb.append(" => ");
            sb.append(queryString.get(key));
            sb.append("\n");
        }

        if (body != null) {
            sb.append("Body:\n");
            if (body.isEmpty()) {
                sb.append("<EMPTY>");
            } else {
                sb.append(body);
            }
        }

        return sb.toString();

    }

    /**
     *
     * @return
     */
    public String getHash() {
        StringBuilder base = new StringBuilder();
        base.append(getProtocol());
        base.append(getHttpMethod());
        base.append(getPath());
        base.append(rawqs);
        base.append(body);

        return Hashing.goodFastHash(16)
                .hashString(base.toString(), StandardCharsets.UTF_8)
                .toString();
    }

}
