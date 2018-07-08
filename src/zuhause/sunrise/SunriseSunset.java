package zuhause.sunrise;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zuhause.bot.TelegramBot;
import zuhause.db.DbConfig;
import zuhause.db.PairDao;
import zuhause.util.Config;
import zuhause.ws.ApiArduino;

/**
 *
 * @author Eduardo Folly
 */
public class SunriseSunset implements Runnable {

    private static final boolean DEBUG = Config.getInstance().isDebug();

    private static final DbConfig DB_CONFIG = Config.getDbConfig("localhost");

    private static final SimpleDateFormat LOC
            = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private static final Logger LOGGER = LogManager.getRootLogger();

    private static final TelegramBot BOT
            = Config.getTelegramBot("zuhause_iot_bot");

    private final PairDao dao = new PairDao(DB_CONFIG);

    /**
     *
     * @return Lat
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private double getLat() throws ClassNotFoundException, SQLException {
        return Double.parseDouble(dao.getValue("sunrise_sunset", "lat"));
    }

    /**
     *
     * @return Lng
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private double getLng() throws ClassNotFoundException, SQLException {
        return Double.parseDouble(dao.getValue("sunrise_sunset", "lng"));
    }

    /**
     *
     * @return Name
     * @throws ClassNotFoundException
     * @throws SQLException
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
        String name;
        String pin;
        double lat;
        double lng;

        try {
            name = getName();
            pin = getPin();
            lat = getLat();
            lng = getLng();
        } catch (ClassNotFoundException | SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return;
        }

        while (true) {
            try {

                Date[] dates = new Date[4];

                /**
                 * Today
                 */
                GregorianCalendar today = new GregorianCalendar();

                GregorianCalendar[] riseSet = getRiseSet(today, lat, lng);

                dates[0] = riseSet[0].getTime();

                dates[1] = riseSet[1].getTime();

                /**
                 * Tomorrow
                 */
                GregorianCalendar tomorrow = new GregorianCalendar();
                tomorrow.add(Calendar.DAY_OF_MONTH, 1);

                riseSet = getRiseSet(tomorrow, lat, lng);

                dates[2] = riseSet[0].getTime();

                dates[3] = riseSet[1].getTime();

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

                LOGGER.info(msg);

                if (!DEBUG) {
                    BOT.sendMessage(msg);
                }

                new ApiArduino().acionarDigital(name, pin, (useDate % 2) == 0);

                Thread.sleep(dates[useDate].getTime() - now);

                new ApiArduino().acionarDigital(name, pin, (useDate % 2) == 1);

                msg = "A luz " + name + " foi "
                        + (useDate % 2 == 1 ? "acesa." : "apagada.");

                LOGGER.info(msg);

                if (!DEBUG) {
                    BOT.sendMessage(msg);
                }

            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
                try {
                    Thread.sleep(300000); // 5 min
                } catch (Exception exx) {
                    LOGGER.error(exx.getMessage(), exx);
                    break;
                }
            }
        }
    }

    /**
     *
     * @param calendar
     * @param lat
     * @param lng
     * @return
     */
    private GregorianCalendar[] getRiseSet(GregorianCalendar calendar,
            double lat, double lng) {

        double n = calendar.get(Calendar.DAY_OF_YEAR);

        double f = calendar.getTimeZone().getOffset(calendar.getTimeInMillis())
                / 1000.0 / 60.0 / 60.0;

        double sigma = 23.45 * Math.sin(Math.toRadians(360.0 / 365.0
                * (284.0 + n)));

        double T = 2.0 / 15.0 * Math.toDegrees(Math.acos(
                -(Math.tan(Math.toRadians(lat))
                * Math.tan(Math.toRadians(sigma)))));

        double halfT = T / 2.0;

        double clng = (Math.abs(lng) - Math.abs(f * 15.0)) / 15.0;

        double rise = 12.0 - halfT + clng;

        GregorianCalendar sunrise = new GregorianCalendar();
        sunrise.setTime(calendar.getTime());

        double floor = Math.floor(rise);
        sunrise.set(Calendar.HOUR_OF_DAY, (int) floor);

        rise = (rise - floor) * 60.0;
        floor = Math.floor(rise);

        sunrise.set(Calendar.MINUTE, (int) floor);

        rise = (rise - floor) * 60.0;
        floor = Math.floor(rise);

        sunrise.set(Calendar.SECOND, (int) floor);

        double set = 12.0 + halfT + clng;

        GregorianCalendar sunset = new GregorianCalendar();
        sunset.setTime(calendar.getTime());

        floor = Math.floor(set);
        sunset.set(Calendar.HOUR_OF_DAY, (int) floor);

        set = (set - floor) * 60.0;
        floor = Math.floor(set);

        sunset.set(Calendar.MINUTE, (int) floor);

        // Sunset delay.
        sunset.add(Calendar.MINUTE, 15);

        set = (set - floor) * 60.0;
        floor = Math.floor(set);

        sunset.set(Calendar.SECOND, (int) floor);

        return new GregorianCalendar[]{sunrise, sunset};
    }

}
