package zuhause.bot;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Eduardo Folly
 *
 * https://core.telegram.org/bots/api#user
 */
public class User {

    private int id;
    //--
    @SerializedName("first_name")
    private String firstName;
    //--
    @SerializedName("last_name")
    private String lastName;
    //--
    private String username;
    //--
    @SerializedName("language_code")
    private String languageCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

}
