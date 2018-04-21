package zuhause.sunrise;

import com.google.gson.Gson;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import zuhause.bot.TelegramBot;
import zuhause.db.DbConfig;
import zuhause.db.PairDao;
import zuhause.util.Config;
import zuhause.util.HttpClient;
import zuhause.util.ServerLog;
import zuhause.ws.ApiArduino;

/**
 *
 * @author Eduardo Folly
 */
public class SunriseSunset implements Runnable {

    private static final boolean DEBUG = Config.getInstance().isDebug();

    private static final DbConfig DB_CONFIG = Config.getDbConfig("localhost");

    private static final Gson GSON = new Gson();

    private static final SimpleDateFormat SDF
            = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

    private static final SimpleDateFormat LOC
            = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private static final ServerLog SERVERLOG = ServerLog.getInstance();

    private static final TelegramBot BOT
            = Config.getTelegramBot("zuhause_iot_bot");

    private final PairDao dao = new PairDao(DB_CONFIG);

    /**
     *
     * @return
     */
    private String getUrl() throws ClassNotFoundException, SQLException {
        String url = "http://api.sunrise-sunset.org/json?formatted=0";
        url += "&lat=" + dao.getValue("sunrise_sunset", "lat");
        url += "&lng=" + dao.getValue("sunrise_sunset", "lng");
        return url;
    }

    /**
     *
     * @return
     */
    private String getName() throws ClassNotFoundException, SQLException {
        return dao.getValue("sunrise_sunset", "name");
    }

    /**
     *
     * @return
     */
    private String getPin() throws ClassNotFoundException, SQLException {
        return dao.getValue("sunrise_sunset", "pin");
    }

    /**
     *
     */
    @Override
    public void run() {
        String url;
        String name;
        String pin;

        try {
            url = getUrl();
            name = getName();
            pin = getPin();
        } catch (ClassNotFoundException | SQLException ex) {
            return;
        }

        while (true) {
            try {

                Date[] dates = new Date[4];

                /**
                 * Today
                 */
                String responseToday = HttpClient
                        .get(url + "&date=today");

                SunriseBase base = GSON.fromJson(responseToday,
                        SunriseBase.class);

                dates[0] = SDF.parse(base.getResults().getSunrise());

                dates[1] = SDF.parse(base.getResults().getCivilTwilightEnd());

                /**
                 * Tomorrow
                 */
                String responseTomorrow = HttpClient
                        .get(url + "&date=tomorrow");

                base = GSON.fromJson(responseTomorrow, SunriseBase.class);

                dates[2] = SDF.parse(base.getResults().getSunrise());

                dates[3] = SDF.parse(base.getResults().getCivilTwilightEnd());

                /**
                 * Calc
                 */
                long now = System.currentTimeMillis();

                int useDate = -1;

                for (int i = 0; i < dates.length; i++) {
                    if (dates[i].getTime() - now > 0) {
                        useDate = i;
                        break;
                    }
                }

                if (useDate < 0) {
                    System.out.println("Erro na utilização de datas.");
                    break;
                }

                String msg = "Luz " + name + " programada para "
                        + (useDate % 2 == 1 ? "acender" : "apagar")
                        + " às " + LOC.format(dates[useDate]) + ".";

                SERVERLOG.msg(-1, msg);

                if (!DEBUG) {
                    BOT.sendMessage(msg);
                }

                new ApiArduino().acionarDigital(name, pin, (useDate % 2) == 0);

                Thread.sleep(dates[useDate].getTime() - now);

                new ApiArduino().acionarDigital(name, pin, (useDate % 2) == 1);

                msg = "A luz " + name + " foi "
                        + (useDate % 2 == 1 ? "acesa." : "apagada.");

                SERVERLOG.msg(-1, msg);

                if (!DEBUG) {
                    BOT.sendMessage(msg);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    Thread.sleep(300000); // 5 min
                } catch (Exception ex1) {
                    ex1.printStackTrace();
                    break;
                }
            }
        }
    }
}
