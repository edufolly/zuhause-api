package zuhause.util;

/**
 *
 * @author Eduardo Folly
 */
public class Triple {

    public final String key;
    public final int begin;
    public final int end;

    /**
     *
     * @param key
     * @param begin
     * @param end
     */
    public Triple(String key, int begin, int end) {
        this.key = key;
        this.begin = begin;
        this.end = end;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "(" + key + ", [" + begin + ", " + end + "])";
    }

}
