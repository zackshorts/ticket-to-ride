package client;

import java.util.ArrayList;
import java.util.List;

import models.data.ChatMessage;
import models.data.DestinationCard;
import models.data.Result;
import models.data.Route;
import models.data.User;


public interface IServer {
    public Result requestGame(String gameName);
    public Result register(User newUser);
    public Result login(User returnUser);
    public Result startGame(String gameName);
    public Result joinGame(String userName, String gameName, Integer numPlayers);
    public Result createGame(String gameName, String username, Integer numPlayers);
    public Result endGame(String gameName);

    public Result returnDestinationCards(String userName, String gameName, DestinationCard[] returnedCards);
    public Result purchaseRoute(String userName, String gameName, Route purchasedRoute, Integer numberOfWilds, Integer colorUsed);
    public Result requestDestinationCards(String userName, String gameName);
    public Result requestTicketCard(String userName, String gameName, Integer selectedCard, Boolean secondPick);
    public Result postChatMessage(String gameName, ChatMessage chatMessage);
}
