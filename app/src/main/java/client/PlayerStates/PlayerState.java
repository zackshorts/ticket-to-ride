package client.PlayerStates;

import client.ClientModel;
import models.data.DestinationCard;
import models.data.Result;
import models.data.Route;

public class PlayerState {
    public Result initializeGame(ClientModel clientModel){
        return null;
    };
    public Result requestTicketCard(ClientModel clientModel, int cardNum){
        return null;
    };
    public Result requestDestinationCards(ClientModel clientModel){
        return null;
    };
    public Result returnDestinationCards(ClientModel clientModel, DestinationCard[] destinationCards){
        return null;
    };
    public Result purchaseRoute(ClientModel clientModel, Route route, int numberOfWilds, int colorUsed){
        return null;
    };
    public Result acceptPlayerAction(ClientModel clientModel){
        return null;
    };
    public Result leaveGame(ClientModel clientModel){
        return null;
    };
}
