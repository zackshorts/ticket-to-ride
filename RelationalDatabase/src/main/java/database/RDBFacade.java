package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.data.Game;
import models.data.User;
import server.GeneralCommand;
import Database.DBFacade;

class RDBFacade implements DBFacade {

    private CommandDao commandDao;
    private UserDao userDao;
    private GameDao gameDao;

    public RDBFacade() throws Exception{
        this.commandDao = new CommandDao();
        this.userDao = new UserDao();
        this.gameDao = new GameDao();
    }

    @Override
    public void clearDB() throws Exception{
        this.commandDao.clear();
        this.userDao.clear();
        this.gameDao.clear();
    }

    @Override
    public void backupGame(Game game) throws Exception{
        this.gameDao.delete(game);
        this.gameDao.add(game);
        this.commandDao.deleteAllFromGame(game.getGameName());
    }

    @Override
    public void addCommand(Game game, GeneralCommand command) throws Exception{
        this.commandDao.add(command);
    }

    @Override
    public void addUser(User user) throws Exception{
        this.userDao.add(user);
    }

    @Override
    public void createGame(Game game) throws Exception{
        this.gameDao.add(game);
    }

    @Override
    public void endGame(Game game) throws Exception{
        this.gameDao.delete(game);
    }

    @Override
    public void joinGame(User user, Game game) {
        userDao.joinGame(user, game.getGameName());
    }

    @Override
    public List<User> getUsers()throws Exception {
        return this.userDao.getAll();
    }

    @Override
    public Map<String, ArrayList<GeneralCommand>> getCommands()throws Exception {
        ArrayList<Game> games = this.gameDao.getAllGames();
        Map<String, ArrayList<GeneralCommand>> outputMap = new HashMap<>();
        for (Game currGame: games) {
            outputMap.put(currGame.getGameName(),this.commandDao.getAll(currGame));
        }
        return outputMap;
    }

    @Override
    public Map<String, Game> getGames()throws Exception {
        Map<String, Game> allGames = new HashMap<>();
        for(Game game:this.gameDao.getAllGames()) {
            allGames.put(game.getGameName(),game);
        }
        return allGames;
    }
}
