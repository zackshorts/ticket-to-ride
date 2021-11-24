package client.PlayerStates;

import client.ClientModel;
import client.ServerProxy;
import models.data.Result;

public class GameFinished extends PlayerState{
    private static final GameFinished ourInstance = new GameFinished();

    public static GameFinished getInstance() {
        return ourInstance;
    }

    private GameFinished() {

    }

    public Result leaveGame(ClientModel clientModel){
//        Result result = null;
//        ServerProxy serverProxy = new ServerProxy();
//        result = serverProxy.endGame(clientModel.getUser().getGameJoined().getGameName());
//        clientModel.getUser().setGameJoined(null);
//        clientModel.setState(NotInGame.getInstance());
//        return result;
        return null;
    };
}
