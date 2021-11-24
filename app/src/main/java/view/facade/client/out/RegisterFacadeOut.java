package view.facade.client.out;

import client.ServerProxy;
import models.data.Result;
import models.data.User;

public class RegisterFacadeOut {
    private ServerProxy server;

    public RegisterFacadeOut () {
        this.server = new ServerProxy();
    }

    public Result register(User newUser) {
        return this.server.register(newUser);
    }
}
