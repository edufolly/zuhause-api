package zuhause.bot;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Eduardo Folly
 *
 * https://core.telegram.org/bots/api#message
 */
public class Message {

    @SerializedName("message_id")
    private int id;
    //--
    private User from;
    //--
    private int date; // Unix time
    //--
    private Chat chat;
    //--
    @SerializedName("forward_from")
    private User forwardFrom;
    //--
    @SerializedName("forward_from_chat")
    private Chat forwardFromChat;
    //--
    @SerializedName("forward_from_message_id")
    private int forwardFromMessageId;
    //--
    @SerializedName("forward_date")
    private int forwardDate; // Unix time
    //--
    @SerializedName("reply_to_message")
    private Message replyToMessage;
    //--
    @SerializedName("edit_date")
    private int editDate; // unix time
    //--
    private String text;

    // TODO - Implementações não realizadas.
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getForwardFrom() {
        return forwardFrom;
    }

    public void setForwardFrom(User forwardFrom) {
        this.forwardFrom = forwardFrom;
    }

    public Chat getForwardFromChat() {
        return forwardFromChat;
    }

    public void setForwardFromChat(Chat forwardFromChat) {
        this.forwardFromChat = forwardFromChat;
    }

    public int getForwardFromMessageId() {
        return forwardFromMessageId;
    }

    public void setForwardFromMessageId(int forwardFromMessageId) {
        this.forwardFromMessageId = forwardFromMessageId;
    }

    public int getForwardDate() {
        return forwardDate;
    }

    public void setForwardDate(int forwardDate) {
        this.forwardDate = forwardDate;
    }

    public Message getReplyToMessage() {
        return replyToMessage;
    }

    public void setReplyToMessage(Message replyToMessage) {
        this.replyToMessage = replyToMessage;
    }

    public int getEditDate() {
        return editDate;
    }

    public void setEditDate(int editDate) {
        this.editDate = editDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
