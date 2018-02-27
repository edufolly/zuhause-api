package zuhause.util;

import com.google.common.net.HttpHeaders;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
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
    private Map<String, String> queryString;
    private Map<String, String> headers;
    private char[] body;

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
                headers.put(p[0], p[1].trim());
            }
        }

        if (requestArray.isEmpty()) {
            throw new MalformedRequestException("Request String vazia. Conexão recusada.");
        }

        String[] stline = requestArray.get(0).split(" ");

        if (stline.length < 3) {
            throw new MalformedRequestException("Padrões do protocolo HTTP não contemplados.");
        }

        httpMethod = stline[0];
        path = stline[1];
        protocol = stline[2];

        String[] split = path.split("\\?", 2);

        String qs = "";
        if (split.length == 2) {
            qs = split[1];
        }

        // path = URLDecoder.decode(split[0], "UTF-8");
        path = split[0];

        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        if (!path.endsWith("/")) {
            path += "/";
        }

        queryString = new HashMap();
        for (String s : qs.split("&")) {
            String[] splt = s.split("=", 2);
            String key = URLDecoder.decode(splt[0], "UTF-8");
            String value = null;
            if (splt.length == 2) {
                value = URLDecoder.decode(splt[1], "UTF-8");
            }
            queryString.put(key, value);
        }

        if (headers.containsKey(HttpHeaders.CONTENT_LENGTH)) {
            int length = Integer.parseInt(headers.get(HttpHeaders.CONTENT_LENGTH));
            long start = System.currentTimeMillis();
            body = new char[length];
            int readed = 0;
            while (readed < length) {
                if (System.currentTimeMillis() - start > timeout) {
                    throw new TimeoutException("Content length incomplete (" + readed + "/" + length + ").");
                }
                int read = inFromClient.read(body, readed, length - readed);
                readed += read;
            }
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
    public char[] getBody() {
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
}
