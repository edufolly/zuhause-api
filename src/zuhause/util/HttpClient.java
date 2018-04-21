package zuhause.util;

import java.io.IOException;
import okhttp3.OkHttpClient;

/**
 *
 * @author Eduardo Folly
 */
public class HttpClient {

    private static final OkHttpClient CLIENT = Config.getHttpClient();

    /**
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        return get(url, 3);
    }

    /**
     *
     * @param url
     * @param tentativas
     * @return
     * @throws IOException
     */
    public static String get(String url, int tentativas) throws IOException {
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();

        Exception e = null;

        int tent = tentativas;

        while (tent > 0) {
            try (okhttp3.Response response = CLIENT.newCall(request)
                    .execute()) {

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                return response.body().string();
            } catch (Exception ex) {
                e = ex;
                tent--;
            }
        }

        throw new IOException("Número máximo de tentativas excedidas.", e);
    }

}
