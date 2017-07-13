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

    public Pair() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Timestamp getWhen() {
        return when;
    }

    public void setWhen(Timestamp when) {
        this.when = when;
    }

    @Override
    public String toString() {
        return "Pair{" + "id=" + id + ", tab=" + tab + ", key=" + key + ", value=" + value + ", when=" + when + '}';
    }

}
