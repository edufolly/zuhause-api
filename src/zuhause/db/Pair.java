package zuhause.db;

import java.sql.Timestamp;

/**
 *
 * @author Eduardo Folly
 */
public class Pair {

    private long id;
    private String tab;
    private String key;
    private String value;
    private Timestamp when;

    /**
     *
     */
    public Pair() {
    }

    /**
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getTab() {
        return tab;
    }

    /**
     *
     * @param tab
     */
    public void setTab(String tab) {
        this.tab = tab;
    }

    /**
     *
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     *
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    public Timestamp getWhen() {
        return when;
    }

    /**
     *
     * @param when
     */
    public void setWhen(Timestamp when) {
        this.when = when;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Pair{" + "id=" + id + ", tab=" + tab + ", key=" + key
                + ", value=" + value + ", when=" + when + '}';
    }

}
