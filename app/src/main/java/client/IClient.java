package client;

import models.data.User;

public interface IClient {
    public void updateClient();
    public void join();
    public void create();
    public void updateAuthToken(String newAuthToken);
    public String passAuthToken();
    public void setUserValues(User newUser);
}
