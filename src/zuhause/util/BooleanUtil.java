package zuhause.util;

/**
 *
 * @author Eduardo Folly
 */
public class BooleanUtil {

    /**
     *
     * @param bool
     * @return
     */
    public static int toInt(Boolean bool) {
        return bool ? 1 : 0;
    }

    /**
     *
     * @param s
     * @return
     */
    public static boolean fromString(String s) {
        return s.equals("1");
    }

    /**
     *
     * @param b
     * @return
     */
    public static String toString(boolean b) {
        return b ? "1" : "0";
    }

}
