package view.presenter;

import com.example.cs340.tickettoride.LobbyViewActivity;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import client.ClientModel;
import client.Poller;
import models.data.Game;
import models.data.Result;
import models.data.User;
import view.facade.client.ClientFacade;
import view.facade.client.out.LobbyFacadeOut;
import view.presenterInterface.IGameLobbyPresenter;

/**
 * The GameLobbyPresenter inherits from IGameLobbyPresenter and Observer.
 * It is created in the LobbyViewActivity and its methods are called from the buttons in the activity.
 */
public class    GameLobbyPresenter implements IGameLobbyPresenter, Observer {

    /**
     * The gameList data member is the list of games that are in the lobby.
     *
     */
    private ArrayList<Game> gameList = new ArrayList<>();
    /**
     * The gameLobby data member is the reference to the lobbyViewActivity(view)
     */
    LobbyViewActivity gameLobby;

    /**
     * The getPlayer method gets and returns the user that is logged in and in the lobby.
     * @return Returns the user that is logged in and in the lobby.
     */
    @Override
    public User getPlayer() {
        ClientFacade client = new ClientFacade();
        return client.getPlayer();
    }


    /**
     * This is the GameLobbyPresenter constructor. This presenter requires a lobbyViewActivity to be
     * created. That way there is a way for the activity data members to be changed with data from the presenter.
     * This constructor makes a single connection between the activity, presenter, and model.
     *
     * @param lobbyViewActivity the lobby view activity
     */
    public GameLobbyPresenter(LobbyViewActivity lobbyViewActivity) {
        this.gameLobby = lobbyViewActivity;
        ClientModel.create().setGameLobbyPresenter(this);
    }


    /**
     * The addPlayer method will get the current user that is logged in and will add that user to
     * the game that is selected by that user. It is called by clicking on one of the games that is
     * the lobby's list of games.
     * @param game
     * @return Returns the result of the add player call to the server.
     *
     * @pre game != null
     * @post game has user added
     */
    @Override
    public Result addPlayer(Game game) {
        ClientModel.create().initializeGame();
        LobbyFacadeOut lobbyFacadeOut = new LobbyFacadeOut();
        User user = ClientModel.create().getUser();
        Result joinResult = lobbyFacadeOut.joinGame(game, user);
        //gameLobby.updateGamePlayers(gameId);

        return joinResult;
    }

    /**
     * This method is called when the logged in host of the game clicks the start game button from
     * the LobbyViewActivity. The method will start the game for the host that clicks the button. It
     * will call a method in the gameStartFacade which will in turn make the call to the server.
     *
     * @pre there is a user
     * @post game is started
     */
    @Override
    public void startGame() {
//        User user = ClientModel.create().getUser();
        ClientModel.create().initializeGame();
//        GameStartFacadeOut gameStartFacadeOut = new GameStartFacadeOut();
//        gameStartFacadeOut.startGame(user.getGame().getGameName());
    }

    /**
     * This function will get and return the games that are currently in the lobby.
     * @return The list of games that are currently in the lobby is returned
     * @pre none
     * @post none
     */
    @Override
    public ArrayList getGameList() {
        ClientFacade client = new ClientFacade();
        return client.getGames();
    }


    /**
     * This method will add the user to the game as the host and call the . This method is called when a logged in user clicks the create game button, types a name of
     * the game, and clicks the create button. The Game object parameter is made in the activity
     * and passed to this method.
     * @param game
     * @return Returns the result of the create game call to the server. If the game was created successfully
     * then it will return a Result object that is successful.
     *
     * @pre game
     */
    @Override
    public Result createGame(Game game) {
        Result result = new Result();
        if (game.getGameName().equals("")) {
            result.setErrorMessage("name invalid...");
            result.setSuccessful(false);
            return result;
        }

        ClientFacade client = new ClientFacade();
        User player = client.getPlayer();
        LobbyFacadeOut lobbyFacadeOut = new LobbyFacadeOut();
        game.addPlayer(player.getUsername());
        result = lobbyFacadeOut.createGame(game, player.getUsername());
        if (result.isSuccessful()) {
            player.setHost(true);
            player.setGameJoined(game);
        }

        return result;

    }

    /**
     * The onCreate method will try to start the poller. The poller will make calls to the server
     *  and get an updated list of the games for the lobby list of games.
     *
     * @return Returns a boolean depending on if the poller starting was successful or not
     * @pre none
     * @post none
     */
    @Override
    public boolean onCreate() {
        try {
            Poller.startLobbyPoller();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This method is called whenever the observed object(client model) is changed. An application calls an
     * Observable object's notifyObservers method to have all the object's observers
     * notified of the change. The updated game list will be grabbed and then given to the
     * Lobby view activity and then update in the client.
     *
     * @param o the observable object.
     * @param arg an argument passed to the notifyObservers method.
     * @pre gameLobby
     * @post gameLobby matches client model game list
     */
    @Override
    public void update(Observable o, Object arg) {
        ClientModel client = (ClientModel) o;
        System.out.println("Server Polled by User: " + client.getUser().getUsername());

        this.gameList = client.getChangedGameList();
        gameLobby.new UpdateGameListAsyncTask(client.getUser(), gameLobby).execute(this.gameList);
    }
}