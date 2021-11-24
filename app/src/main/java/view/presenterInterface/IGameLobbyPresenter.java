package view.presenterInterface;

import java.util.ArrayList;

import models.data.Game;
import models.data.Result;
import models.data.User;

public interface IGameLobbyPresenter {
    public Result addPlayer(Game gameId);

    public void startGame();

    public ArrayList getGameList();
    public Result createGame(Game game);

    public User getPlayer();
    public boolean onCreate();
}
