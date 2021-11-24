package view.presenter;

import com.example.cs340.tickettoride.GameBoardActivity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import client.ClientModel;
import client.ServerProxy;
import models.data.ChatMessage;
import view.presenterInterface.IChatPresenter;

/**
 * This class is used by the gameBoardActivity and defines the methods in IChatPresenter. It is used to interact with the
 * model and updates the activity when any changes occur.
 */
public class ChatPresenter implements IChatPresenter, Observer {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");
    private final ServerProxy serverProxy = new ServerProxy();
    private ClientModel clientModel = ClientModel.create();
    private GameBoardActivity boardActivity;

    /**
     * pre-condition - You must pass in a valid non null GameBoardActivity.
     *
     * post-condition - the clientModel will have the valid ChatPresenter in it. The ChatPresenter will have the
     * GameBoardActivity stored in it as well.
     *
     * description - this is the constructor for the chat presenter. It sets the boardActivity and sets the chat
     * presenter in the client model.
     *
     * @param activity this is the instance of the gameBoardActivity that sets it in the class so that it can
     *                 call the async task in the gameBoardActivity when anything changes in the activity.
     *
     * exception - There are no exceptions that can occur here.
     */
    public ChatPresenter(GameBoardActivity activity) {
        boardActivity = activity;
        clientModel.setChatPresenter(this);
    }

    /**
     * pre-condition - there is no pre condition
     *
     * post-condition - the caller will recieve all of the messages in the chat log.
     *
     * @return - returns all of the ChatMessage objects in an arrayList.
     *
     * exception - there can be an exception if for some reason the poller is not running and does not fill the clientModel
     * with ChatMessages and returns null.
     */
    @Override
    public ArrayList<ChatMessage> getMessages() {
        return clientModel.getUser().getGameJoined().getChatLog();
    }

    /**
     * description - This creates a new ChatMessage with its appropriate fields. It then calls the postChatMessage
     * in the serverProxy which puts that created message in the server.
     *
     * pre-condition - you must pass in a message, it can be any valid string including empty string.
     *
     * post-condition - This will put the message in the server data and it will be polled so that it is present in the
     * clientModel.
     *
     * @param message - this must be any valid string. Can be empty string.
     *
     * exception - there is no exception that could occur here.
     *
     */
    @Override
    public void addMessage(String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setAuthorUserName(clientModel.getUser().getUsername());
        chatMessage.setMessageContents(message);
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        chatMessage.setTimeStamp(sdf.format(timestamp));
        //clientModel.getUser().getGame().addChat(chatMessage);
        serverProxy.postChatMessage(clientModel.getUser().getGameJoined().getGameName(), chatMessage);
    }

    /**
     * description - This will get the username of the person who posted the message.
     *
     * pre-condition - none
     *
     * post-condition - it will return the username of the user that is currently logged in.
     *
     * @return will return the username of the current user.
     *
     * exception - there could be an exception if you called this method before you logged in. But this is never called
     * before then.
     *
     */
    @Override
    public String getSenderName() {
        return clientModel.getUser().getUsername();
    }

    /**
     * description - This function will call a method on an async task in the GameBoardActivity when there are changes
     * to the presenter.
     *
     * pre-condition - must pass in the observable and can pass in another argument but we do not use either in this function.
     *
     * post-condition - the updateAsyncTask excute function will be called.
     *
     * @param o - the object that extends observable
     * @param arg - optional parameter that can be passed to notifyObservers.
     *
     * exception - no exceptions can occur.
     */
    @Override
    public void update(Observable o, Object arg) {
        boardActivity.new UpdateAsyncTask(boardActivity).execute();
    }
}
