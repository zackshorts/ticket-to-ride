package view.facade.client.out;

import client.ServerProxy;
import models.data.Result;
import models.data.User;

public class LoginFacadeOut {
    private ServerProxy server;

    public LoginFacadeOut () {
        this.server = new ServerProxy();
    }

    public Result login(User returningUser) {
        return this.server.login(returningUser);
    }
}
