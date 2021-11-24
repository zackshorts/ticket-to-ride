package view.facade.client.out;

import client.ServerProxy;
import models.data.Game;
import models.data.Result;
import models.data.User;

public class LobbyFacadeOut {
    private ServerProxy server;

    public LobbyFacadeOut() {
        this.server = new ServerProxy();
    }

    public Result createGame(Game game, String username) {
        Result result = server.createGame(game.getGameName(), username, game.getPlayerUsernames().size());
        return result;
    }

    public Result joinGame(Game game, User user) {
        Result result = server.joinGame(user.getUsername(), game.getGameName(), game.getPlayerUsernames().size());
        user.setGameJoined(game);
        return result;

    }
}
