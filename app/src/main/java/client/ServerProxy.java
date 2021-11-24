package client;

import java.util.ArrayList;
import java.util.List;

import models.data.ChatMessage;
import models.data.DestinationCard;
import models.data.Game;
import models.data.Result;
import models.data.Route;
import models.data.User;

//TODO: Factor out server side facades and remove then from both app and server module.
public class ServerProxy implements IServer {

    ClientCommunicator client = new ClientCommunicator();


    @Override
    public Result joinGame(String username, String gameName, Integer numPlayers) {
//        String className = LobbyFacade.class.getName();
        String methodName = "joinGame";

        Object[] parameterDataArray = new Object[3];
        Class<?>[] parameterClassArray = new Class<?>[3];

        parameterClassArray[0] = String.class;
        parameterClassArray[1] = String.class;
        parameterClassArray[2] = Integer.class;
        parameterDataArray[0] = username;
        parameterDataArray[1] = gameName;
        parameterDataArray[2] = numPlayers;

        GeneralCommand newCommand = new GeneralCommand(methodName, parameterClassArray, parameterDataArray);

        ClientCommunicator communicator = new ClientCommunicator();

        return communicator.send(newCommand);
    }


    @Override
    public Result createGame(String gameName, String username, Integer numPlayers) {


//        String className = LobbyFacade.class.getName();
        String methodName = "createGame";

        Object[] parameterDataArray = new Object[3];
        Class<?>[] parameterClassArray = new Class<?>[3];

        parameterClassArray[0] = String.class;
        parameterClassArray[1] = String.class;
        parameterClassArray[2] = Integer.class;
        parameterDataArray[0] = gameName;
        parameterDataArray[1] = username;
        parameterDataArray[2] = numPlayers;

        GeneralCommand newCommand = new GeneralCommand(methodName, parameterClassArray, parameterDataArray);

        ClientCommunicator communicator = new ClientCommunicator();

        Result result = communicator.send(newCommand);
        if (result.isSuccessful()) {
            ClientModel.create().addLobbyGamesList(new Game(result.getGame()));
        }
        return result;
    }

    @Override
    public Result startGame(String gameName) {

//        String className = GameStartFacade.class.getName();
        String methodName = "startGame";

        Object[] parameterDataArray = new Object[1];
        Class<?>[] parameterClassArray = new Class<?>[1];

        parameterClassArray[0] = String.class;
        parameterDataArray[0] = gameName;

        GeneralCommand newCommand = new GeneralCommand(methodName, parameterClassArray, parameterDataArray);

        ClientCommunicator communicator = new ClientCommunicator();

        Result result = communicator.send(newCommand);

        return result;
    }

    @Override
    public Result register(User newUser) {
//        String className = RegisterFacade.class.getName();
        String methodName = "register";

        Object[] params = new Object[1];
        params[0] = newUser;

        Object[] parameterDataArray = new Object[1];
        Class<?>[] parameterClassArray = new Class<?>[1];

        parameterClassArray[0] = User.class;
        parameterDataArray[0] = newUser;

        GeneralCommand newCommand = new GeneralCommand(methodName, parameterClassArray, parameterDataArray);

        ClientCommunicator communicator = new ClientCommunicator();
        Result result = communicator.send(newCommand);
        if (result.isSuccessful()) {
            ClientModel.create().setUserPlayer(newUser);
        }
        return result;
    }
    @Override
    public Result login(User returnUser) {
//        String className = LoginFacade.class.getName();
        String methodName = "login";

        Object[] params = new Object[1];
        params[0] = returnUser;

        Object[] parameterDataArray = new Object[1];
        Class<?>[] parameterClassArray = new Class<?>[1];

        parameterClassArray[0] = User.class;
        parameterDataArray[0] = returnUser;

        GeneralCommand newCommand = new GeneralCommand(methodName, parameterClassArray, parameterDataArray);

        ClientCommunicator communicator = new ClientCommunicator();
        Result result = communicator.send(newCommand);
        if (result.isSuccessful()) {
            returnUser.setHost(result.isHost());
            if (result.getGameJoined() != null) {
                for (Game game : ClientModel.create().getLobbyGamesList()) {
                    if (game.getGameName().equals(result.getGameJoined())) {
                        returnUser.setGameJoined(game);
                        break;
                    }
                }
            }
            ClientModel.create().setUserPlayer(returnUser);
        }
        return result;
    }

    @Override
    public Result returnDestinationCards(String userName, String gameName, DestinationCard[] returnedCards) {
//        String className = RunGameFacade.class.getName();
        String methodName = "returnDestinationCards";

//        int additionalArraySize = 0;
//        if (returnedCards != null) {
//            additionalArraySize = (returnedCards.length * 3);
//        }

        Object[] parameterDataArray = new Object[3];
        Class<?>[] parameterClassArray = new Class<?>[3];

        parameterClassArray[0] = String.class;
        parameterClassArray[1] = String.class;
        parameterClassArray[2] = DestinationCard[].class;
        System.out.println("------------------" + DestinationCard.class + "----------------------");
        parameterDataArray[0] = userName;
        parameterDataArray[1] = gameName;
        parameterDataArray[2] = returnedCards;

//        if (returnedCards != null) {
//            int position = 0;
//            for (DestinationCard currentCard : returnedCards) {
//                String[] location = returnedCards[position].getLocations();
//                String first_location = location[0];
//                String second_location = location[1];
//                Integer points = returnedCards[position].getPoints();
//
//                parameterClassArray[2 + position * 3] = String.class;
//                parameterClassArray[3 + position * 3] = String.class;
//                parameterClassArray[4 + position * 3] = Integer.class;
//                parameterDataArray[2 + position * 3] = first_location;
//                parameterDataArray[3 + position * 3] = second_location;
//                parameterDataArray[4 + position * 3] = points;
//                position++;
//            }
//        }

        GeneralCommand newCommand = new GeneralCommand(methodName, parameterClassArray, parameterDataArray);

        ClientCommunicator communicator = new ClientCommunicator();

        return communicator.send(newCommand);
    }

    @Override
    public Result purchaseRoute(String userName, String gameName, Route purchasedRoute, Integer numberOfWilds, Integer colorUsed) {
//        String className = RunGameFacade.class.getName();
        String methodName = "purchaseRoute";

        String[] location = purchasedRoute.getLocation();
        String first_location = location[0];
        String second_location = location[1];

        Object[] parameterDataArray = new Object[5];
        Class<?>[] parameterClassArray = new Class<?>[5];

        parameterClassArray[0] = String.class;
        parameterClassArray[1] = String.class;
        parameterClassArray[2] = Route.class;
        parameterClassArray[3] = Integer.class;
        parameterClassArray[4] = Integer.class;
        parameterDataArray[0] = userName;
        parameterDataArray[1] = gameName;
        parameterDataArray[2] = purchasedRoute;
        parameterDataArray[3] = numberOfWilds;
        parameterDataArray[4] = colorUsed;

        GeneralCommand newCommand = new GeneralCommand(methodName, parameterClassArray, parameterDataArray);

        ClientCommunicator communicator = new ClientCommunicator();

        return communicator.send(newCommand);
    }

    @Override
    public Result requestDestinationCards(String userName, String gameName) {
//        String className = RunGameFacade.class.getName();
        String methodName = "requestDestinationCards";

        Object[] parameterDataArray = new Object[2];
        Class<?>[] parameterClassArray = new Class<?>[2];

        parameterClassArray[0] = String.class;
        parameterClassArray[1] = String.class;
        parameterDataArray[0] = userName;
        parameterDataArray[1] = gameName;

        GeneralCommand newCommand = new GeneralCommand(methodName, parameterClassArray, parameterDataArray);

        ClientCommunicator communicator = new ClientCommunicator();

        return communicator.send(newCommand);
    }

    @Override
    public Result requestTicketCard(String userName, String gameName, Integer selectedCard, Boolean secondPick) {
//        String className = RunGameFacade.class.getName();
        String methodName = "requestTicketCard";

        Object[] parameterDataArray = new Object[4];
        Class<?>[] parameterClassArray = new Class<?>[4];

        parameterClassArray[0] = String.class;
        parameterClassArray[1] = String.class;
        parameterClassArray[2] = Integer.class;
        parameterClassArray[3] = Boolean.class;
        parameterDataArray[0] = userName;
        parameterDataArray[1] = gameName;
        parameterDataArray[2] = selectedCard;
        parameterDataArray[3] = secondPick;


        GeneralCommand newCommand = new GeneralCommand(methodName, parameterClassArray, parameterDataArray);

        ClientCommunicator communicator = new ClientCommunicator();

        return communicator.send(newCommand);
    }

    @Override
    public Result postChatMessage(String gameName, ChatMessage chatMessage) {
//        String className = RunGameFacade.class.getName();
        String methodName = "postChatMessage";

        Object[] parameterDataArray = new Object[2];
        Class<?>[] parameterClassArray = new Class<?>[2];

        parameterClassArray[0] = String.class;
        parameterClassArray[1] = ChatMessage.class;
        parameterDataArray[0] = gameName;
        parameterDataArray[1] = chatMessage;

        GeneralCommand newCommand = new GeneralCommand(methodName, parameterClassArray, parameterDataArray);

        ClientCommunicator communicator = new ClientCommunicator();

        return communicator.send(newCommand);
    }

//    public ArrayList<Game> getGames() {
//        String className = PollManager.class.getName();
//        String methodName = "getAvailableGames";
//
//        Object[] parameterDataArray = new Object[0];
//        Class<?>[] parameterClassArray = new Class<?>[0];
//
//
//        GeneralCommand newCommand = new GeneralCommand(className, methodName, parameterClassArray, parameterDataArray);
//
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            String json = mapper.writeValueAsString(newCommand);
//
//            ClientCommunicator communicator = new ClientCommunicator();
//
//            json = communicator.send(json);
//
//            Result result = mapper.readValue(json, Result.class);
//
//            return result.getPollResult().getGamesChanged();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    @Override
    public Result requestGame(String gameName) {
//        String className = GameStartFacade.class.getName();
        String methodName = "requestGame";

        Class<?>[] parameterClassArray = new Class<?>[1];
        parameterClassArray[0] = String.class;
        Object[] parameterDataArray = new Object[1];
        parameterDataArray[0] = gameName;

        GeneralCommand newCommand = new GeneralCommand(methodName, parameterClassArray, parameterDataArray);

        ClientCommunicator communicator = new ClientCommunicator();

        Result result = communicator.send(newCommand);
        return result;
    }

    @Override
    public Result endGame(String gameName) {
        String methodName = "endGame";

        Object[] parameterDataArray = new Object[1];
        Class<?>[] parameterClassArray = new Class<?>[1];

        parameterClassArray[0] = String.class;
        parameterDataArray[0] = gameName;

        GeneralCommand newCommand = new GeneralCommand(methodName, parameterClassArray, parameterDataArray);

        ClientCommunicator communicator = new ClientCommunicator();

        Result result = communicator.send(newCommand);

        return result;
    }


}