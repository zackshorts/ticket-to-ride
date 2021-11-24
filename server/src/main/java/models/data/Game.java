package models.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import models.TTR_Constants;

public class Game {
    private boolean isStarted;
    private String gameName;
    private Integer numPlayerActions;
    private Integer currentLongestRouteValue;
    private Integer currentTurnPlayer;
    private boolean lastRound = false;
    private boolean lastTurn = false;
    private Integer lastPlayerColor = null;

    private ArrayList<String> playerUsernames = new ArrayList<>();

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<ChatMessage> chatLog = new ArrayList<>();
    private Map<Integer, Integer> ticketCardDeck = new HashMap<>();
    private Map<Integer, Integer> ticketCardDiscard = new HashMap<>();
    private TrainCard[] faceUpTrainCards = new TrainCard[5];
    private ArrayList<DestinationCard> destinationDeck = new ArrayList<>();
    private Set<Route> availableRoutes = new HashSet<>();

    private TTR_Constants constants = TTR_Constants.getInstance();

    ////////////

    public Game(String gameName) {
        this.gameName = gameName;
        isStarted = false;
        numPlayerActions = 0;
        currentLongestRouteValue = 0;
        currentTurnPlayer = constants.BLACK_PLAYER;
        faceUpTrainCards = new TrainCard[]{new TrainCard(0),
                                           new TrainCard(0),
                                           new TrainCard(0),
                                           new TrainCard(0),
                                           new TrainCard(0)};
        initDiscard();
    }

    public Game() {

    }


    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }
    
    public String getGameName() {
        return gameName;
    }

    public Player findPlayerByColor(Integer color) {
        for (Player player: players) {
            if (player.getPlayerColor().equals(color)) {
                return player;
            }
        }
        return null;
    }

    public void addPlayer(String userName) { this.playerUsernames.add(userName); }

    public ArrayList<String> getPlayerUsernames() {
        return playerUsernames;
    }
    public void addPlayerUsername(String userName) { this.playerUsernames.add(userName); }
//////////////////////////////////////////////////////////////////////////////

    public Map<Integer, Integer> getTicketCardDeck() {
        return ticketCardDeck;
    }

    public Integer countTickets() {
        int total = 0;
        for (Integer color : ticketCardDeck.keySet()) {
            total += ticketCardDeck.get(color);
        }
        return total;
    }

    public void setTicketCardDeck(Map<Integer, Integer> ticketCardDeck) {
        this.ticketCardDeck = ticketCardDeck;
    }

    public TrainCard dealTicketCard(int position) {
        TrainCard dealtCard = new TrainCard(0);
        if (position == 0) {
            int remainingCardCount = 0;
            Random generator = new Random();
            Integer dealtCardColor = generator.nextInt(9) + 1;
            Integer originalDealtCardColor = null;

            do {
                if (originalDealtCardColor == null) {
                    originalDealtCardColor = dealtCardColor;
                }
                else if (originalDealtCardColor.equals(dealtCardColor)) {
                    dealtCard.setCardColor(0);
                    TrainCard newCard = new TrainCard();
                    newCard.setCardColor(0);
                    return newCard;
                }

                if (dealtCardColor == 9) {
                    dealtCardColor = 1;
                } else {
                    dealtCardColor++;
                }

                dealtCard.setCardColor(dealtCardColor);
                remainingCardCount = this.ticketCardDeck.get(dealtCardColor);
            } while (remainingCardCount == 0);

            this.ticketCardDeck.put(dealtCardColor,remainingCardCount - 1);
        }
        else if (position < 6) {
            dealtCard = this.faceUpTrainCards[position - 1];
            TrainCard newCard = dealTicketCard(0);

            this.faceUpTrainCards[position - 1] = newCard;

            int wildCardCount = 0;
            for (TrainCard card :this.faceUpTrainCards) {
                if (card.getCardColor().equals(constants.WILD)) {
                    wildCardCount++;
                }
            }

            if (wildCardCount >= 3) {
                reshuffleTicketDecks();
            }

            for(TrainCard card : faceUpTrainCards){
                if (card.getCardColor().equals(constants.EMPTY)) {
                    reshuffleTicketDecks();
                }
            }
        }

        boolean deckEmpty = true;
        for (Integer cardColor : this.ticketCardDeck.keySet()) {
            if(ticketCardDeck.get(cardColor) != 0) {
                deckEmpty = false;
                break;
            }
        }
        if (deckEmpty) {
            boolean discardEmpty = true;
            for (Integer cardColor : this.ticketCardDiscard.keySet()) {

                if(ticketCardDiscard.get(cardColor) != 0) {
                    discardEmpty = false;
                    break;
                }
            }
            if (!discardEmpty) {
                reshuffleTicketDecks();
            }
        }

        return dealtCard;
    }
//////////////////////////////////////////////////////////////////////////////

    public Map<Integer, Integer> getTicketCardDiscard() {
        return ticketCardDiscard;
    }
    public void setTicketCardDiscard(Map<Integer, Integer> ticketCardDiscard) {
        this.ticketCardDiscard = ticketCardDiscard;
    }
    public void reshuffleTicketDecks() {
        for (TrainCard card:this.faceUpTrainCards) {
            if (card.getCardColor() != 0) {
                this.ticketCardDiscard.put(card.getCardColor(), ticketCardDiscard.get(card.getCardColor()) + 1 );
            }
        }
        for (Integer color: this.ticketCardDiscard.keySet()){
            this.ticketCardDeck.put(color,this.ticketCardDeck.get(color) + this.ticketCardDiscard.get(color));
            this.ticketCardDiscard.put(color, 0);
        }

        dealFaceUpTicketCards();
    }
//////////////////////////////////////////////////////////////////////////////

    public TrainCard[] getFaceUpTrainCards() {
        return faceUpTrainCards;
    }
    public void setFaceUpTrainCard(TrainCard card, int position) {
        this.faceUpTrainCards[position] = card;
    }
    public void dealFaceUpTicketCards() {
        for(int i  = 0; i < 5; i++) {
            TrainCard card = this.dealTicketCard(0);
            this.setFaceUpTrainCard(card, i);
        }
    }
//////////////////////////////////////////////////////////////////////////////

    public ArrayList<DestinationCard> getDestinationDeck() {
        return destinationDeck;
    }
    public void setDestinationDeck(ArrayList<DestinationCard> destinationDeck) {
        this.destinationDeck = destinationDeck;
    }
    public void returnDestinationCards (DestinationCard[] returnedCards) {
        if (returnedCards != null) {
            destinationDeck.addAll(Arrays.asList(returnedCards));
        }
    }
    public DestinationCard dealDestinationCard() {
        Collections.shuffle(this.destinationDeck);
        DestinationCard card = this.destinationDeck.get(0);
        this.destinationDeck.remove(card);
        return card;
    }
    //////////////////////////////////////////////////////////////////////////////

    public Set<Route> getAvailableRoutes() {
        return availableRoutes;
    }

    public void setAvailableRoutes(Set<Route> availableRoutes) {
        this.availableRoutes = availableRoutes;
    }
    public boolean purchaseRoute(String playerName, Route purchasedRoute, Integer numberOfWilds, Integer colorUsed) {
        if (!this.availableRoutes.contains(purchasedRoute)) {
            return false;
        }

        for (Player player : this.players){
            if (player.getUsername().equals(playerName)) {
                if (player.getTrainsRemaining() >= purchasedRoute.findLength()) {
                    //check if the player has the cards and the train markers to make this purchase
                    player.decrementTrainsRemaining(purchasedRoute.findLength());
                    Integer color = purchasedRoute.getCardColor();
                    if (color == TTR_Constants.getInstance().WILD) {
                        color = colorUsed;
                    }
                    Integer numberOfNonWilds = purchasedRoute.findLength() - numberOfWilds;
                    this.ticketCardDiscard.put(color, this.ticketCardDiscard.get(color) + numberOfNonWilds);
                    player.removeTicketsFromHand(color, numberOfNonWilds);
//                for (int i = 0; i < purchasedRoute.findLength() - numberOfWilds; i++) {
//                    this.ticketCardDiscard.put(color,this.ticketCardDiscard.get(color)+1);
//                    player.removeTicketFromHand(color);
//                }
                    this.ticketCardDiscard.put(constants.WILD, this.ticketCardDiscard.get(constants.WILD) + numberOfWilds);
                    player.removeTicketsFromHand(constants.WILD, numberOfWilds);
//                for (int i = 0; i < numberOfWilds; i++) {
//                    this.ticketCardDiscard.put(constants.WILD,this.ticketCardDiscard.get(constants.WILD)+1);
//                    player.removeTicketFromHand(constants.WILD);
//                }
                    player.incrementScore(purchasedRoute.getPoints());
                    player.addRoute(purchasedRoute);
                    this.availableRoutes.remove(purchasedRoute);
                    if (player.getTrainsRemaining() < 3 && !lastRound) {
                        this.lastRound = true;
                        player.setDoneWithTurns(true);
                    }
                    //TODO: calculate the longest route here and give player longest route if they have it.
                    return true;
                }
            }
        }
        return false;
    }

//////////////////////////////////////////////////////////////////////////////

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(String username) {
        for (Player p: players) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }
        return null;
    }
    public void addPlayer(Player newPlayer) {
        this.players.add(newPlayer);
    }
    public Player findPlayer(String username){
        for (Player foundPlayer: this.players) {
            if (foundPlayer.getUsername().equals(username)) {
                return foundPlayer;
            }
        }
        return null;
    }
//////////////////////////////////////////////////////////////////////////////

    public ArrayList<ChatMessage> getChatLog() {
        return chatLog;
    }

    public void addChat(ChatMessage chat) {
        this.chatLog.add(chat);
    }
//////////////////////////////////////////////////////////////////////////////

    public Integer getNumPlayerActions() {
        return numPlayerActions;
    }

    public void incrementNumPlayerActions() {
        ++numPlayerActions;
    }
//////////////////////////////////////////////////////////////////////////////

    public Integer getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }
    public void incrementCurrentTurnPlayer() {
        if (this.currentTurnPlayer.equals(constants.BLACK_PLAYER)) {
            this.currentTurnPlayer = constants.BLUE_PLAYER;
        }
        else if (this.currentTurnPlayer.equals(constants.BLUE_PLAYER)) {
            if (this.players.size() > 2) {
                this.currentTurnPlayer = constants.RED_PLAYER;
            }
            else {
                this.currentTurnPlayer = constants.BLACK_PLAYER;
            }
        }
        else if (this.currentTurnPlayer.equals(constants.RED_PLAYER)) {
            if (this.players.size() > 3) {
                this.currentTurnPlayer = constants.GREEN_PLAYER;
            }
            else {
                this.currentTurnPlayer = constants.BLACK_PLAYER;
            }
        }
        else if (this.currentTurnPlayer.equals(constants.GREEN_PLAYER)) {
            if (this.players.size() > 4) {
                this.currentTurnPlayer = constants.YELLOW_PLAYER;
            }
            else {
                this.currentTurnPlayer = constants.BLACK_PLAYER;
            }
        }
        else if (this.currentTurnPlayer.equals(constants.YELLOW_PLAYER)) {
            this.currentTurnPlayer = constants.BLACK_PLAYER;
        }
    }
//////////////////////////////////////////////////////////////////////////////

    public Integer getCurrentLongestRouteValue() {
        return currentLongestRouteValue;
    }

    public void setCurrentLongestRouteValue(Integer currentLongestRouteValue) {
        this.currentLongestRouteValue = currentLongestRouteValue;
    }

    public Game copy() {
//        Game clone = new Game();
//        /*
//        public String status;
//        private boolean isStarted;
//        private String gameName;
//        private ArrayList<String> playerUsernames = new ArrayList<>();
//
//        //new stuff for phase 2
//        private Map<Enums.Color, Integer> ticketCardDeck;
//        private Map<Enums.Color, Integer> ticketCardDiscard;
//        private ArrayList<TrainCard> faceUpTrainCards = new ArrayList<>(5);
//        private Queue<DestinationCard> destinationCards;
//        private Set<Route> availableRoutes;
//        private ArrayList<Player> players;
//        private ArrayList<ChatMessage> chatLog;
//        private Enums.Color currentTurnPlayer;
//        private Integer numPlayerActions;
//        private Integer currentLongestRouteValue;
//        */
//
//        clone.status = new String(status);
//        clone.isStarted = new Boolean(isStarted);
//        clone.gameName = new String(gameName);
//        clone.playerUsernames = new ArrayList<>(playerUsernames);
//        clone.ticketCardDeck = new HashMap<>(ticketCardDeck);
//        clone.ticketCardDiscard = new HashMap<>(ticketCardDiscard);
//        clone.faceUpTrainCards = new ArrayList<>(faceUpTrainCards);
//        clone.destinationCards = new LinkedList<>(destinationCards);
//        clone.availableRoutes = new HashSet<>(availableRoutes);
//        clone.players = new ArrayList<>(players);
//        clone.chatLog = new ArrayList<>(chatLog);
//        clone.currentTurnPlayer = currentTurnPlayer;
//        clone.numPlayerActions = new Integer(numPlayerActions);
//        clone.currentLongestRouteValue = new Integer(currentLongestRouteValue);
        ObjectMapper om = new ObjectMapper();
        String info;
        Game clone = null;
        try {
            info = om.writeValueAsString(this);
            clone = om.readValue(info, Game.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return clone;
    }

    public void hideSecrets(String viewingUser) {
        if (players != null) {
            for (Player p : players) {
                if (!p.getUsername().equals(viewingUser)) {
                    p.hideCards();
                }
            }
        }
    }

    private void initDiscard() {
        for (int i = 1; i < 10; i++ ) {
            this.ticketCardDiscard.put(i, 0);
        }
    }

    public boolean isLastRound() {
        return lastRound;
    }

    public void setLastRound(boolean lastRound) {
        this.lastRound = lastRound;
    }

    public boolean isLastTurn() {
        return lastTurn;
    }

    public void setLastTurn(boolean lastTurn) {
        this.lastTurn = lastTurn;
    }
}
