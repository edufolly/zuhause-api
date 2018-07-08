package zuhause.bot;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zuhause.db.DbConfig;
import zuhause.db.Pair;
import zuhause.db.PairDao;
import zuhause.util.Config;
import zuhause.ws.ApiArduino;

/**
 *
 * @author Eduardo Folly
 */
public class TelegramBot implements Serializable, Runnable {

    private String scheme;
    private String host;
    private String token;
    private String name;

    private static final Logger LOGGER = LogManager.getRootLogger();

    private static final transient OkHttpClient CLIENT
            = Config.getHttpClient();

    private transient static final DbConfig DB_CONFIG
            = Config.getDbConfig("localhost");

    private transient static final Gson GSON = new Gson();

    /**
     *
     * @param url
     * @return String
     * @throws IOException
     */
    private String get(HttpUrl url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("cache-control", "no-cache")
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    /**
     *
     * @param text
     * @return boolean
     * @throws Exception
     */
    public Boolean sendMessage(String text) throws Exception {
        PairDao dao = new PairDao(DB_CONFIG);

        List<Pair> p = dao.select("`tab` = ? AND `key` = ?",
                new String[]{"telegram_bot", "send_to"});

        String resp = "";

        for (Pair pair : p) {

            HttpUrl url = new HttpUrl.Builder()
                    .scheme(scheme)
                    .host(host)
                    .addPathSegment("bot" + token)
                    .addPathSegment("sendMessage")
                    .addQueryParameter("chat_id", pair.getValue())
                    .addQueryParameter("text", text)
                    .build();

            resp = get(url);

            LOGGER.info(resp);
        }

        return !resp.isEmpty();
    }

    /**
     *
     */
    @Override
    public void run() {
        PairDao dao = new PairDao(DB_CONFIG);

        ApiArduino apiArduino = new ApiArduino();

        try {
            List<Pair> p = dao.select("`tab` = ? AND `key` = ?",
                    new String[]{"telegram_bot", name},
                    "`when` DESC", "1");

            Pair pair;

            if (p.isEmpty()) {
                pair = new Pair();
                pair.setTab("telegram_bot");
                pair.setKey(name);
                pair.setValue("0");
            } else {
                pair = p.get(0);
            }

            int offset = Integer.parseInt(pair.getValue());

            HttpUrl url = new HttpUrl.Builder()
                    .scheme(scheme)
                    .host(host)
                    .addPathSegment("bot" + token)
                    .addPathSegment("getUpdates")
                    .addQueryParameter("offset", String.valueOf(offset + 1))
                    .build();

            String ret = get(url);

            Result result = GSON.fromJson(ret, Result.class);

            if (result.isOk()) {

                List<Update> updates = result.getUpdates();

                /**
                 * Se houver mensagens.
                 */
                if (!updates.isEmpty()) {
                    for (Update update : updates) {
                        LOGGER.info(update.getMessage().getText());

                        if (update.getMessage().getText()
                                .equalsIgnoreCase("temperatura")) {

                            Map<String, Object> tempInt
                                    = apiArduino.getTempInterna();

                            sendMessage("Temperatura interna: "
                                    + tempInt.get("t") + "ºC");
                        }
                    }
                    pair.setValue(String.valueOf(updates.get(updates.size() - 1)
                            .getId()));

                    dao.saveOrUpdate(pair);
                }
            } else {
                LOGGER.error("TelegramBot: {}", ret);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
