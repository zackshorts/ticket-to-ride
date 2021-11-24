package models.command;

import client.IClient;
import models.data.User;

//TODO: this could be the command queue that dr. woodfield mentioned
// that stores an arrayList of Commands that have been run.
public class CommandManager implements IClient {
    @Override
    public void updateClient() {

    }

    @Override
    public void join() {

    }

    @Override
    public void create() {

    }

    @Override
    public void updateAuthToken(String newAuthToken) {

    }

    @Override
    public String passAuthToken() {

        return null;
    }
    @Override
    public void setUserValues(User newUser) {

    }
}
