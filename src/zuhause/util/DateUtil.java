package zuhause.util;

import java.util.Date;

/**
 *
 * @author Eduardo Folly
 */
public class DateUtil {

    public static String humanDateDifference(Date startDate, Date endDate) {

        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return String.format("%d dias, %d horas, %d minutos, %d segundos", elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);
    }
}
