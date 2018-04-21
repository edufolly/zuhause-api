package zuhause.bot;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author Eduardo Folly
 */
public class Result {

    private boolean ok;
    //--
    @SerializedName("result")
    private List<Update> updates;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<Update> getUpdates() {
        return updates;
    }

    public void setUpdates(List<Update> updates) {
        this.updates = updates;
    }

}
