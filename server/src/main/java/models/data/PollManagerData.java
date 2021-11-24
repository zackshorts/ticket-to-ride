package models.data;

import java.util.ArrayList;

public class PollManagerData {
    public PollManagerData() {
    }

    public ArrayList<Game> gamesChanged;
    public ArrayList<User> usersChanged;

    public void setGamesChanged(ArrayList<Game> games) { this.gamesChanged = games;}
    public ArrayList<Game> getGamesChanged() {
        return gamesChanged;
    }

    public void addGamesChanged(Game gamesChanged) {
        this.gamesChanged.add(gamesChanged);
    }

    public ArrayList<User> getUsersChanged() {
        return usersChanged;
    }

    public void addUsersChanged(User usersChanged) {
        this.usersChanged.add(usersChanged);
    }


}
