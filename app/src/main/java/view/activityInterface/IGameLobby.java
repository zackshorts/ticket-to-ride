package view.activityInterface;

import java.util.ArrayList;

import models.data.Game;
import models.data.User;


public interface IGameLobby {
    //static Void updateGameList(ArrayList<Game> lobbyGames, User user);
    void onGameCreated();
    void onCreateGameFailed(String errorMessage);
}
