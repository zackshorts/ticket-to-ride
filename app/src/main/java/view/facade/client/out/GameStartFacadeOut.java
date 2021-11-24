package view.facade.client.out;

import client.ServerProxy;
import models.data.Result;

public class GameStartFacadeOut {

    private ServerProxy server;

    public GameStartFacadeOut() {
        this.server = new ServerProxy();
    }

    public Result startGame(String gameName) {
        return this.server.startGame(gameName);
    }

}
