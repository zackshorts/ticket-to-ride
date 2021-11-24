package client.PlayerStates;

import client.ClientModel;
import models.TTR_Constants;
import models.data.Result;
import view.facade.client.out.GameStartFacadeOut;

public class NotInGame extends PlayerState {
    private static final NotInGame ourInstance = new NotInGame();

    public static NotInGame getInstance() {
        return ourInstance;
    }

    private NotInGame() {
    }

    public Result initializeGame(ClientModel clientModel){
        Result result = new Result();
        TTR_Constants.getInstance().createGraph();
        result.setSuccessful(false);
        if(clientModel.getUser().isHost()) {
            GameStartFacadeOut gameStartFacadeOut = new GameStartFacadeOut();
            result = gameStartFacadeOut.startGame(clientModel.getUser().getGameJoined().getGameName());
        }
        clientModel.setState(YourTurnAwaitingDestinations.getInstance());
        return result;
    }
};

