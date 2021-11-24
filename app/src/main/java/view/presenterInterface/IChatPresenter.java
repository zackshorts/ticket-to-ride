package view.presenterInterface;

import java.util.ArrayList;

import models.data.ChatMessage;

public interface IChatPresenter {
    ArrayList<ChatMessage> getMessages();
    void addMessage(String message);
    String getSenderName();
}
