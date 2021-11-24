package client.PlayerStates;

import java.util.Map;

import client.ClientModel;
import client.ServerProxy;
import models.TTR_Constants;
import models.data.Result;
import models.data.TrainCard;
import models.data.User;

public class YourTurnSecondDraw extends PlayerState {
    private static final YourTurnSecondDraw ourInstance = new YourTurnSecondDraw();

    public static YourTurnSecondDraw getInstance() {
        return ourInstance;
    }

    private YourTurnSecondDraw() {
    }
    public Result requestTicketCard(ClientModel clientModel, int cardNum){
        if(cardNum != 0) {
            if(clientModel.getUser().getGameJoined().getFaceUpTrainCards()[cardNum-1].CardColor.equals(TTR_Constants.getInstance().EMPTY)) {
                for (TrainCard card :clientModel.getUser().getGameJoined().getFaceUpTrainCards()) {
                    if(!card.CardColor.equals(TTR_Constants.getInstance().EMPTY)){
                        return null;
                    }
                }

                Map<Integer,Integer> deck = clientModel.getUser().getGameJoined().getTicketCardDeck();
                for (Integer cardColor : deck.keySet()) {
                    if(deck.get(cardColor) != 0) {
                        return null;
                    }
                }
                clientModel.setState(NotYourTurn.getInstance());
                return null;
            }

            if (!clientModel.getUser().getGameJoined().getFaceUpTrainCards()[cardNum-1].CardColor.equals(TTR_Constants.getInstance().WILD)) {
                ServerProxy serverProxy = new ServerProxy();
                User user = clientModel.getUser();
                Result result = serverProxy.requestTicketCard(user.getUsername(), user.getGameJoined().getGameName(), cardNum, true);

                if (!result.isSuccessful()) {
                    return null;
                }
                clientModel.setState(NotYourTurn.getInstance());

                return result;
            } else {
                return null;
            }
        } else {
            ServerProxy serverProxy = new ServerProxy();
            User user = clientModel.getUser();
            Result result = new Result();
            result = serverProxy.requestTicketCard(user.getUsername(), user.getGameJoined().getGameName(), cardNum, true);

            if (!result.isSuccessful()) {
                return null;
            }

            clientModel.setState(NotYourTurn.getInstance());
            return result;
        }

    }
    public Result acceptPlayerAction(ClientModel clientModel){
        Result result = new Result();

        if(clientModel.getUser().getGameJoined().isLastTurn()) {
            clientModel.setState(GameFinished.getInstance());
            result.setSuccessful(true);
        } else {
            result.setSuccessful(true);

            for (TrainCard card :clientModel.getUser().getGameJoined().getFaceUpTrainCards()) {
                if(!card.CardColor.equals(TTR_Constants.getInstance().EMPTY) && !card.CardColor.equals(TTR_Constants.getInstance().WILD )){
                    return result;
                }
            }

            Map<Integer,Integer> deck = clientModel.getUser().getGameJoined().getTicketCardDeck();
            for (Integer cardColor : deck.keySet()) {
                if(deck.get(cardColor) != 0) {
                    return result;
                }
            }
            clientModel.setState(NotYourTurn.getInstance());
        }
        return result;
    }
}
