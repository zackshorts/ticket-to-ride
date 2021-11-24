package client;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import client.PlayerStates.NotInGame;
import client.PlayerStates.PlayerState;
import models.TTR_Constants;
import models.data.ChatMessage;
import models.data.DestinationCard;
import models.data.Game;
import models.data.Player;
import models.data.Route;
import models.data.Result;
import models.data.User;
import view.presenter.CardDeckPresenter;
import view.presenter.ChatPresenter;
import view.presenter.GameLobbyPresenter;
import view.presenter.PlayerInfoPresenter;
import view.presenter.PlayersHandPresenter;
import view.presenter.RoutePresenter;

public class ClientModel extends Observable {
    private User userPlayer;
    private Game gameActive;
    private ArrayList<Game> lobbyGameList = new ArrayList<>();
    private ArrayList<Game> newGameList = new ArrayList<>();
    private GameLobbyPresenter mGameLobbyPresenter;
    private ChatPresenter mChatPresenter;
    private CardDeckPresenter mCardDeckPresenter;
    private PlayerInfoPresenter mPlayerInfoPresenter;
    private PlayersHandPresenter mPlayersHandPresenter;
    private RoutePresenter mRoutePresenter;
    public PlayerState state = NotInGame.getInstance();

    //    TEMPORARY DEMO THINGS
    //new stuff for phase 2
    private Map<Integer, Integer> ticketCardHand;
    private ArrayList<DestinationCard> destinationCardHand;
    private Player player;
    private ArrayList<Object> changedObjects;
    private ArrayList<ChatMessage> chatMessages;

    public ArrayList<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void addChatMessages(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);
    }

    public Map<Integer, Integer> getTicketCardHand() {
        return ticketCardHand;
    }

    public void setTicketCardHand(Map<Integer, Integer> ticketCardHand) {
        this.ticketCardHand = ticketCardHand;
    }
    public void incrementTicketCardHand(Integer color) {
        ticketCardHand.put(color, ticketCardHand.get(color) + 1);
    }

    public ArrayList<DestinationCard> getDestinationCardHand() {
        return destinationCardHand;
    }

    public void setDestinationCardHand(ArrayList<DestinationCard> destinationCardHand) {
        this.destinationCardHand = destinationCardHand;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ArrayList<Object> getChangedObjects() {
        return changedObjects;
    }

    public void setChangedObjects(ArrayList<Object> changedObjects) {
        this.changedObjects = changedObjects;
    }

/////////////

    private static ClientModel singleton;

    private ClientModel() {}

    public static ClientModel create() {
        if (singleton == null) {
            singleton = new ClientModel();
        }
        return singleton;
    }

    public User getUser() {
        return this.userPlayer;
    }

    public void setUserPlayer(User userPlayer) {
        this.userPlayer = userPlayer;
    }

    public Game getActiveGame() {
        return this.gameActive;
    }

    public void setActiveGame(Game gamePlaying) {
        System.out.print("Setting active game: ");
        if (gamePlaying == null) {
            this.gameActive = gamePlaying;
            this.userPlayer.setGameJoined(gamePlaying);
            this.player = null;
        }
        else if (gamePlaying.getPlayerUsernames().contains(userPlayer.getUsername())) { // change this back to checking player?
            System.out.print("Game is updating");
            this.gameActive = gamePlaying;
            this.userPlayer.setGameJoined(gamePlaying);
            this.player = gamePlaying.getPlayer(userPlayer.getUsername());
            if (this.player != null) {
                this.ticketCardHand = player.getTickets();
                this.destinationCardHand = player.getDestinationCardHand();
            } else {
                System.out.println("Active Game Waiting To Start");
            }
            if (gamePlaying.isLastTurn()) {
                this.leaveGame();
            }
        }
        System.out.println();
    }

    public ArrayList<Game> getLobbyGamesList() {
        return this.lobbyGameList;
    }

    public void setLobbyGamesList(ArrayList<Game> newGameList) { this.lobbyGameList = new ArrayList<>(newGameList);}

    public void addLobbyGamesList(Game game) {
        this.lobbyGameList.add(game);
    }

    public void setChangedGameList(ArrayList<Game> newGameList) { this.newGameList = new ArrayList<>(newGameList);}

    public ArrayList<Game> getChangedGameList() {
        return this.newGameList;
    }

    public void addChange(Game changedGame) {
        this.newGameList.add(changedGame);
    }

    public void clearChangeList() {
        this.newGameList.clear();
    }

    public GameLobbyPresenter getGameLobbyPresenter() {
        return mGameLobbyPresenter;
    }

    public void setGameLobbyPresenter(GameLobbyPresenter gameLobbyPresenter) {
        mGameLobbyPresenter = gameLobbyPresenter;
    }

    public void setmCardDeckPresenter(CardDeckPresenter mCardDeckPresenter) {
        this.mCardDeckPresenter = mCardDeckPresenter;
    }

    public void setmPlayerInfoPresenter(PlayerInfoPresenter mPlayerInfoPresenter) {
        this.mPlayerInfoPresenter = mPlayerInfoPresenter;
    }

    public void setmPlayersHandPresenter(PlayersHandPresenter mPlayersHandPresenter) {
        this.mPlayersHandPresenter = mPlayersHandPresenter;
    }

    public ChatPresenter getmChatPresenter() { return mChatPresenter; }

    public void setChatPresenter(ChatPresenter chatPresenter) { mChatPresenter = chatPresenter; }

    public void setRoutePresenter(RoutePresenter routePresenter) { mRoutePresenter = routePresenter; }

    public RoutePresenter getmRoutePresenter() { return mRoutePresenter; }

    public void update() {
        if (mGameLobbyPresenter != null) {
            addObserver(this.mGameLobbyPresenter);

            setChanged();
            notifyObservers();
            deleteObservers();
            //clearChangeList();
        }
    }

    public void updateGame() {
        this.acceptPlayerAction();
        if (mChatPresenter != null) {
            addObserver(this.mChatPresenter);
        }
        if (mCardDeckPresenter != null) {
            addObserver(this.mCardDeckPresenter);
        }
        if (mPlayerInfoPresenter != null) {
            addObserver(this.mPlayerInfoPresenter);
        }
        if (mPlayersHandPresenter != null) {
            addObserver(this.mPlayersHandPresenter);
        }
        if (mRoutePresenter != null) {
            addObserver(this.mRoutePresenter);
        }
        setChanged();
        notifyObservers();
        deleteObservers();
    }

    public Result initializeGame(){
        System.out.println("Trying to initialize game in state " + state.getClass().getName());
        return state.initializeGame(this);
    };
    public Result requestTicketCard(int cardNum){
        System.out.println("Trying to request ticket card in state " + state.getClass().getName());
        return state.requestTicketCard(this, cardNum);
    };
    public Result requestDestinationCards(){
        System.out.println("Trying to request destination cards in state " + state.getClass().getName());
        return state.requestDestinationCards(this);
    };
    public Result returnDestinationCards(DestinationCard[] destinationCards){
        System.out.println("Trying to return destination cards in state " + state.getClass().getName());
        return state.returnDestinationCards(this, destinationCards);
    };
    public Result purchaseRoute(Route route, int numberOfWilds, int colorUsed){
        System.out.println("Trying to purchase a route in state " + state.getClass().getName());
        return state.purchaseRoute(this, route, numberOfWilds, colorUsed);
    };
    public Result leaveGame(){

        System.out.println("Trying to leave game in state " + state.getClass().getName());
        return state.leaveGame(this);
    };
    public Result acceptPlayerAction(){
        System.out.println("Trying to accept player action in state " + state.getClass().getName());
        return state.acceptPlayerAction(this);
    };

    public void setState(PlayerState state) {
        this.state = state;
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers();
    }

    @Override
    public synchronized void deleteObservers() {
        super.deleteObservers();
    }

    ///Observable Functions


}
