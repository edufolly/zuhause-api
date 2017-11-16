package zuhause.sunrise;

import java.io.Serializable;

/**
 *
 * @author Eduardo Folly
 */
public class SunriseBase implements Serializable {

    private String status;
    private SunriseResult results;

    public SunriseBase() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SunriseResult getResults() {
        return results;
    }

    public void setResults(SunriseResult results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "SunriseBase{" + status + " " + results + '}';
    }

}
