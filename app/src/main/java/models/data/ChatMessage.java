package models.data;

import java.util.Date;

public class ChatMessage {
    private String authorUserName;
    private String messageContents;
    private String timeStamp;
    private boolean containsAnAction = false;

    public ChatMessage() {
    }

    public String getAuthorUserName() {
        return authorUserName;
    }

    public void setAuthorUserName(String authorUserName) {
        this.authorUserName = authorUserName;
    }

    public String getMessageContents() {
        return messageContents;
    }

    public void setMessageContents(String messageContents) {
        this.messageContents = messageContents;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    //constructor
    public void ChatMessage(String authorUserName, String messageContents, Date timeStamp) {

    }

    public boolean isContainsAnAction() {
        return containsAnAction;
    }

    public void setContainsAnAction() {
        this.containsAnAction = true;
    }
}
