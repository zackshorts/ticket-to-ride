package database;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Database.DBFacade;
import models.data.Game;
import models.data.User;
import server.GeneralCommand;

class NRDBFacade implements DBFacade {

    private FileAccess dao;

    public NRDBFacade() {
        this.dao = new FileAccess();
    }
    @Override
    public void clearDB() throws Exception {
        this.dao.deleteFilesInDirectory("games");
        this.dao.deleteFilesInDirectory("commands");
        this.dao.deleteFilesInDirectory("users");
    }

    @Override
    public void backupGame(Game game) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        String serializedGame = mapper.writeValueAsString(game);
        this.dao.checkpointUpdate(serializedGame,game.getGameName());
        this.dao.removeFile("commands",game.getGameName());
    }

    @Override
    public void addCommand(Game game, GeneralCommand command) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        String serializedCommand = mapper.writeValueAsString(command);
        this.dao.addUpdate(serializedCommand, game.getGameName());
        if(command.get_methodName().equals("joinGame")) {
            dao.joinGame(command._paramValues[0].toString(), command._paramValues[1].toString());
        }
    }

    @Override
    public void addUser(User user) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        String serializedUser = mapper.writeValueAsString(user);
        this.dao.addUser(serializedUser);
    }

    @Override
    public void createGame(Game game) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        String serializedGame = mapper.writeValueAsString(game);
        this.dao.checkpointUpdate(serializedGame,game.getGameName());
        this.dao.addUpdate("", game.getGameName());
    }

    @Override
    public void endGame(Game game) throws Exception {
        this.dao.removeFile("commands",game.getGameName());
        this.dao.removeFile("games",game.getGameName());
    }

    @Override
    public void joinGame(User user, Game game) {

    }

//    @Override
//    public Map<String, Game> getLobby() {
//        return null;
//    }

    @Override
    public List<User> getUsers() throws Exception {
        List<String> output = this.dao.getUsers();
        List<User> finalList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (String currString: output) {
            finalList.add(mapper.readValue(currString, User.class));
        }
        return  finalList;
    }

    @Override
    public Map<String, ArrayList<GeneralCommand>> getCommands() throws Exception {
        Map<String, ArrayList<String>> output = this.dao.getAllCommands();
        Map<String, ArrayList<GeneralCommand>> outputMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        for (String currKey: output.keySet()) {
            ArrayList<GeneralCommand> commands = new ArrayList<>();
            for (String currLine: output.get(currKey)) {
                GeneralCommand currentCommand = mapper.readValue(currLine, GeneralCommand.class);
                commands.add(currentCommand);
            }
            outputMap.put(currKey,commands);
        }
        return outputMap;
    }

    @Override
    public Map<String, Game> getGames() throws Exception{
        List<String> output = this.dao.getGames();
        Map<String, Game> outputMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        for (String currString: output) {
            Game currentGame = mapper.readValue(currString, Game.class);
            outputMap.put(currentGame.getGameName(),currentGame);
        }
        return outputMap;
    }
}
