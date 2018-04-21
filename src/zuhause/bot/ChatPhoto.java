package zuhause.bot;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Eduardo Folly
 *
 * https://core.telegram.org/bots/api#chatphoto
 */
public class ChatPhoto {

    @SerializedName("small_file_id")
    private String smallFileId;
    //--
    @SerializedName("big_file_id")
    private String bigFileId;

    public String getSmallFileId() {
        return smallFileId;
    }

    public void setSmallFileId(String smallFileId) {
        this.smallFileId = smallFileId;
    }

    public String getBigFileId() {
        return bigFileId;
    }

    public void setBigFileId(String bigFileId) {
        this.bigFileId = bigFileId;
    }

}
