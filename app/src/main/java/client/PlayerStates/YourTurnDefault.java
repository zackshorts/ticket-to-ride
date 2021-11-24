package client.PlayerStates;

import java.util.Map;

import client.ClientModel;
import client.ServerProxy;
import models.TTR_Constants;
import models.data.Result;
import models.data.Route;
import models.data.TrainCard;
import models.data.User;

public class YourTurnDefault extends PlayerState {
    private static final YourTurnDefault ourInstance = new YourTurnDefault();
    ServerProxy serverProxy = new ServerProxy();
    public static YourTurnDefault getInstance() {
        return ourInstance;
    }

    private YourTurnDefault() {
    }

    public Result requestTicketCard(ClientModel clientModel, int cardNum) {

        if(cardNum != 0) {
            if(clientModel.getUser().getGameJoined().getFaceUpTrainCards()[cardNum-1].CardColor.equals(TTR_Constants.getInstance().EMPTY)) {
                return null;
            }

            if(clientModel.getUser().getGameJoined().getFaceUpTrainCards()[cardNum-1].CardColor.equals(TTR_Constants.getInstance().WILD)) {
                Result result = new Result();
                result = serverProxy.requestTicketCard(clientModel.getUser().getUsername(), clientModel.getUser().getGameJoined().getGameName(), cardNum, false);
                if (!result.isSuccessful()) {
                    return null;
                }
                clientModel.setState(NotYourTurn.getInstance());

                return result;
            }
        }

        Result result = serverProxy.requestTicketCard(clientModel.getUser().getUsername(), clientModel.getUser().getGameJoined().getGameName(), cardNum, false);

        if (!result.isSuccessful()) {
            return null;
        }

        for (TrainCard card :clientModel.getUser().getGameJoined().getFaceUpTrainCards()) {
            if(!card.CardColor.equals(TTR_Constants.getInstance().EMPTY) &&
                    !card.CardColor.equals(TTR_Constants.getInstance().WILD)){
                clientModel.setState(YourTurnSecondDraw.getInstance());
                return result;
            }
        }

        Map<Integer,Integer> deck = clientModel.getUser().getGameJoined().getTicketCardDeck();
        for (Integer cardColor : deck.keySet()) {
            if(deck.get(cardColor) != 0) {
                clientModel.setState(YourTurnSecondDraw.getInstance());
                return result;
            }
        }

        clientModel.setState(NotYourTurn.getInstance());
        return result;
    }

    public Result requestDestinationCards(ClientModel clientModel) {
        Result result = new Result();
        result = serverProxy.requestDestinationCards(clientModel.getUser().getUsername(), clientModel.getUser().getGameJoined().getGameName());
        clientModel.setState(YourTurnAwaitingDestinations.getInstance());
        return result;
    }

    public Result purchaseRoute(ClientModel clientModel, Route route, int numberOfWilds, int colorUsed) {
        Result result = new Result();
        result = serverProxy.purchaseRoute(clientModel.getUser().getUsername(), clientModel.getUser().getGameJoined().getGameName(), route, numberOfWilds, colorUsed);
        if (result.isSuccessful()) {
            clientModel.setState(NotYourTurn.getInstance());
        }

        return result;
    }

//    public Result acceptPlayerAction(ClientModel clientModel){}
//
//    public Result leaveGame(ClientModel clientModel){}
}
