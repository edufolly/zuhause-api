package zuhause.bot;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Eduardo Folly
 *
 * https://core.telegram.org/bots/api#update
 */
public class Update {

    @SerializedName("update_id")
    private int id;
    //--
    private Message message;

    public Update() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
