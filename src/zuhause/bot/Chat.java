package zuhause.bot;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Eduardo Folly
 *
 * https://core.telegram.org/bots/api#chat
 */
public class Chat {

    private int id;
    //--
    private String type;
    //--
    private String title;
    //--
    private String username;
    //--
    @SerializedName("first_name")
    private String firstName;
    //--
    @SerializedName("last_name")
    private String lastName;
    //--
    @SerializedName("all_members_are_administrators")
    private boolean allAdmin;
    //--
    private ChatPhoto photo;
    //--
    private String description;
    //--
    private String invite_link;

    public Chat() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean isAllAdmin() {
        return allAdmin;
    }

    public void setAllAdmin(boolean allAdmin) {
        this.allAdmin = allAdmin;
    }

    public ChatPhoto getPhoto() {
        return photo;
    }

    public void setPhoto(ChatPhoto photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvite_link() {
        return invite_link;
    }

    public void setInvite_link(String invite_link) {
        this.invite_link = invite_link;
    }

}
