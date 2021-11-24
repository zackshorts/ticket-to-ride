package Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.data.Game;
import models.data.User;
import server.GeneralCommand;

public interface DBFacade {
    public void clearDB() throws Exception;
    public void backupGame(Game game) throws Exception;
    public void addCommand(Game game, GeneralCommand command) throws Exception;
    public void addUser(User user) throws Exception;
    public void createGame(Game game) throws Exception;
    public void endGame(Game game) throws Exception;
    public void joinGame(User user, Game game);

    //    public Map<String, Game> getLobby();
    public List<User> getUsers() throws Exception;
    public Map<String, ArrayList<GeneralCommand>> getCommands() throws Exception;
    public Map<String, Game> getGames() throws Exception;
}

//This should be pretty simple?
