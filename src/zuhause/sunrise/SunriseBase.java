package zuhause.sunrise;

import java.io.Serializable;

/**
 *
 * @author Eduardo Folly
 */
public class SunriseBase implements Serializable {

    private String status;
    private SunriseResult results;

    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public SunriseResult getResults() {
        return results;
    }

    /**
     *
     * @param results
     */
    public void setResults(SunriseResult results) {
        this.results = results;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "SunriseBase{" + status + " " + results + '}';
    }

}
