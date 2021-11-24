package server;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Database.DBFacade;
import Database.DBFactory;
import models.TTR_Constants;
import models.data.Game;
import models.data.Player;
import models.data.Result;
import models.data.TrainCard;
import models.data.User;

public class ServerData {
    private static ServerData sServerData;
    private Map<String, Game> availableGames;
    private Map<String, Integer> commandCounts = new HashMap<>();
    private List<User> users;

    private TTR_Constants constants = TTR_Constants.getInstance();
    private DBFacade dbFacade;

    private int delta;

    public static String string1;
    public static String string2;
    public static String string3;

    public ServerData() throws Exception {
        this.availableGames = new HashMap<>();
        this.users = new ArrayList<User>();

        boolean test = true;
        if (test) {
            this.users.add(new User("t", "t"));
            this.users.add(new User("a", "a"));
            this.users.add(new User("s", "s"));
            this.users.add(new User("d", "d"));
            this.users.add(new User("f", "f"));
            this.users.add(new User("g", "g"));
            this.users.add(new User("o", "o"));

        }

        try {
            dbFacade = getDBFacadeInstance();
        } catch (Exception e) {
            dbFacade = null;
            e.printStackTrace();
        }

    }

    public void loadDB() throws Exception {
        if (dbFacade != null) {
            users = dbFacade.getUsers();
            availableGames = dbFacade.getGames();
            for (String gameName : availableGames.keySet()) {
                commandCounts.put(gameName, 0);
            }
            Map<String, ArrayList<GeneralCommand>> commands = dbFacade.getCommands();
            for (Map.Entry<String, ArrayList<GeneralCommand>> entry : commands.entrySet()) {
                commandCounts.put(entry.getKey(), entry.getValue().size());
                for (GeneralCommand command : entry.getValue()) {
                    command.exec();
                }
            }
        }
    }

    public Map<String, Game> getAvailableGames() {
        return availableGames;
    }

    public Game getGame(String gameId) {
        return availableGames.get(gameId);
    }

    public void setDelta(int d) {
        this.delta = d;
    }

    public Result setGame(Game newGame) throws Exception {
        Result result = new Result();
        result.setGame(newGame.getGameName());
        if (availableGames.containsKey(newGame.getGameName())) {
            result.setErrorMessage("ERROR: " + newGame.getGameName() + " is taken, cannot create game.");
            result.setSuccessful(false);

        } else {
            availableGames.put(newGame.getGameName(), newGame);
            commandCounts.put(newGame.getGameName(), 0);
            if (dbFacade != null) {
                dbFacade.createGame(newGame);
            }
            result.setSuccessful(true);
        }
        return result;
    }

    public Result joinGame(User u, Game newGame) {
        Result result = new Result();
        if (dbFacade != null) {
            try {
                dbFacade.joinGame(u, newGame);
            } catch (Exception e) {
                System.out.print("exception: " + e);
            }
        }
        result.setSuccessful(true);
        return result;
    }

    public Result removeGame(Game closedGame) throws Exception {
        Result result = new Result();
        if (availableGames.containsKey(closedGame.getGameName())) {
            Game removedGame = availableGames.remove(closedGame.getGameName());
            if (availableGames.get(removedGame.getGameName()) == null) {
                commandCounts.remove(closedGame.getGameName());
                if (dbFacade != null) {
                    dbFacade.endGame(removedGame);
                }
                result.setSuccessful(true);
            } else {
                result.setErrorMessage("ERROR: Game Removal Failed");
                result.setSuccessful(false);
            }

        } else {
            result.setErrorMessage("ERROR: " + closedGame.getGameName() + " Game Does Not Exist.");
            result.setSuccessful(true);
        }
        return result;
    }

    public ArrayList<User> getUsers() {
        return new ArrayList<>(users);
    }

    public void addUsers(User user) throws Exception {
        this.users.add(user);
        if (dbFacade != null) {
            dbFacade.addUser(user);
        }
    }

    public boolean isHost(User user) {
        for (String s : availableGames.keySet()) {
            if (availableGames.get(s).getPlayers().get(0).equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public void removeFromGames(User user) {
        for (String s : availableGames.keySet()) {
            availableGames.get(s).getPlayers().remove(user.getUsername());
        }
    }

    public static ServerData getInstance() throws Exception {
        if (sServerData == null) {
            sServerData = new ServerData();
        }
        return sServerData;
    }

    public Game findGame(String gameName) {
        return availableGames.get(gameName);
    }

    public void initializeGamePlayers(Game targetGame) {
        ArrayList<String> usernameList = targetGame.getPlayerUsernames();
        Collections.shuffle(usernameList);
        int playerCount = 0;
        for (String username : usernameList) {
            playerCount++;
            Player userPlayer = null;
            switch (playerCount) {
                case (1):
                    userPlayer = new Player(username, constants.BLACK_PLAYER);
                    break;
                case (2):
                    userPlayer = new Player(username, constants.BLUE_PLAYER);
                    break;
                case (3):
                    userPlayer = new Player(username, constants.RED_PLAYER);
                    break;
                case (4):
                    userPlayer = new Player(username, constants.GREEN_PLAYER);
                    break;
                case (5):
                    userPlayer = new Player(username, constants.YELLOW_PLAYER);
                    break;
                default:
                    System.out.println("Initializing more than 5 players!");
            }
            if (userPlayer != null) {
                targetGame.addPlayer(userPlayer);
            }
        }
    }

    public void initializeContainers(Game targetGame) {
        TTR_Constants constants = TTR_Constants.getInstance();
        HashMap<Integer, Integer> ticketCards = new HashMap<Integer, Integer>();
        for (Map.Entry<Integer, Integer> entry : constants.getStartingTicketDeck().entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            ticketCards.put(key, value);
        }
        targetGame.setTicketCardDeck(ticketCards);
        targetGame.setAvailableRoutes(constants.getStartingRouteSet());
        targetGame.setDestinationDeck(constants.getStartingDestinationDeck());
        targetGame.dealFaceUpTicketCards();
    }

    public void dealHands(Game targetGame) {
        for (Player targetPlayer : targetGame.getPlayers()) {
            for (int i = 0; i < 4; i++) {
                TrainCard card = targetGame.dealTicketCard(0);
                targetPlayer.addTicketToHand(card.getCardColor());
            }
            targetPlayer.addToNewDestinationCardHand(targetGame.dealDestinationCard());
            targetPlayer.addToNewDestinationCardHand(targetGame.dealDestinationCard());
            targetPlayer.addToNewDestinationCardHand(targetGame.dealDestinationCard());
        }
    }

    public void registerCommand(String gameName, GeneralCommand command) throws Exception {
        if (dbFacade != null && availableGames.keySet().contains(gameName)) {
            Game game = availableGames.get(gameName);
            Integer count = commandCounts.get(gameName);
            if (count != null && count + 1 >= delta) {
                dbFacade.backupGame(game);
                commandCounts.put(gameName, 0);
            } else {
                dbFacade.addCommand(game, command);
                commandCounts.put(gameName, count + 1);
            }
        }
    }

    private DBFacade getDBFacadeInstance() throws Exception {
        // Get the plugin information from the plugin information file

        String pluginDirectory = string1;
        String pluginJarName = string2;
        String pluginClassName = string3;

        // Get a class loader and set it up to load the jar file
        File pluginJarFile = new File(pluginDirectory, pluginJarName);
        URL pluginURL = pluginJarFile.toURI().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[]{pluginURL});

        // Load the jar file's plugin class, create and return an instance
        Class<? extends DBFactory> FactoryClass = (Class<DBFactory>) loader.loadClass(pluginClassName);
        return FactoryClass.getDeclaredConstructor(null).newInstance().getFacade();
    }

    private void clear() throws Exception {
        if (dbFacade != null) {
            dbFacade.clearDB();
        }
    }

    public static void main(String[] args) throws Exception {
        ServerData data = ServerData.getInstance();
        data.clear();
    }
}
