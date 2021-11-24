package client.PlayerStates;

import java.util.ArrayList;

import client.ClientModel;
import client.ServerProxy;
import models.data.DestinationCard;
import models.data.Result;

public class YourTurnAwaitingDestinations extends PlayerState {
    private static final YourTurnAwaitingDestinations ourInstance = new YourTurnAwaitingDestinations();

    public static YourTurnAwaitingDestinations getInstance() {
        return ourInstance;
    }

    private YourTurnAwaitingDestinations() {

    }
    public Result returnDestinationCards(ClientModel clientModel, DestinationCard[] destinationCards){
        ServerProxy serverProxy = new ServerProxy();

        Result result = new Result();
        result = serverProxy.returnDestinationCards(clientModel.getUser().getUsername(), clientModel.getUser().getGameJoined().getGameName(), destinationCards);

//      Handle whose turn it is.
        if (clientModel.getUser().getGameJoined().getCurrentTurnPlayer().equals(clientModel.getPlayer().getPlayerColor())) {
            clientModel.setState(YourTurnDefault.getInstance());
        } else {
            clientModel.setState(NotYourTurn.getInstance());
        }
        return result;
    }
    public Result acceptPlayerAction(ClientModel clientModel){
        Result result = new Result();
        if(clientModel.getUser().getGameJoined().isLastTurn()) {
            clientModel.setState(GameFinished.getInstance());
            result.setSuccessful(true);
        }
        else {
//            TODO: Ask Christian about this. It is effecting the stats of other emulators.
//            clientModel.setState(YourTurnDefault.getInstance());
            result.setSuccessful(true);
        }
        return result;
    }
}
