package models.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import models.TTR_Constants;

public class Player {
    private Boolean hasLongestRoute;

    public Player() {
    }

    private Integer score;
    private Integer trainsRemaining;
    private Integer individualLongestRouteValue;
    private Integer numTickets;
    private String username;
    private Integer playerColor;
    private Boolean doneWithTurns = false;

    public Boolean getDoneWithTurns() {
        return doneWithTurns;
    }

    public void setDoneWithTurns(Boolean doneWithTurns) {
        this.doneWithTurns = doneWithTurns;
    }

    private Set<Route> routesOwned = new HashSet<>(0);
    private Map<Integer, Integer> tickets = new HashMap<>();
    private ArrayList<DestinationCard> destinationCardHand = new ArrayList<>(0);

    public Set<Route> getRoutesOwned() {
        return routesOwned;
    }

    private ArrayList<DestinationCard> newDestinationCards = new ArrayList<>(0);


    public Player(String username, Integer playerColor) {
        this.username = username;
        this.playerColor = playerColor;
        this.score = 0;
        TTR_Constants constants = TTR_Constants.getInstance();
        this.trainsRemaining = constants.TRAIN_STARTING_COUNT;
//        this.trainsRemaining = 15;
        this.individualLongestRouteValue = 0;
        this.hasLongestRoute = false;
        this.doneWithTurns = false;
        initTickets();
    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<DestinationCard> getDestinationCardHand() {
        return destinationCardHand;
    }
    public void addToDestinationCardHand(DestinationCard destinationCard) {
        this.destinationCardHand.add(destinationCard);
    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    public void resetNewDestinationCards() {
        this.newDestinationCards = new ArrayList<>();
    }
    public ArrayList<DestinationCard> getNewDestinationCards() {
        return newDestinationCards;
    }
    public void addToNewDestinationCardHand(DestinationCard destinationCard) {
        this.newDestinationCards.add(destinationCard);
    }
    public void removeFromNewDestinationCards(DestinationCard[] returnedCards) {
        for (DestinationCard card: returnedCards) {
            this.newDestinationCards.remove(card);
        }
    }
    public void hideCards() {
//        tickets = null;
//        this.destinationCardHand = null;
//        this.newDestinationCards = null;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    public String getUsername() {
        return username;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    public Integer getPlayerColor() {
        return playerColor;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    public Integer getScore() {
        return score;
    }

    public void incrementScore(Integer numToIncrementBy) {
        this.score += numToIncrementBy;
    }
    public void decrementScore(Integer numToDecrementBy) {
        this.score -= numToDecrementBy;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    public Integer getTrainsRemaining() {
        return trainsRemaining;
    }

    public void decrementTrainsRemaining(Integer numToDecrementBy) {
        this.trainsRemaining -= numToDecrementBy;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    public void addRoute(Route route) {
        this.routesOwned.add(route);
    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    public Integer getIndividualLongestRouteValue() {
        return individualLongestRouteValue;
    }

    public void incrementIndividualLongestRouteValue(Integer numToIncrementBy) {
        this.individualLongestRouteValue += numToIncrementBy;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    public Boolean getHasLongestRoute() {
        return hasLongestRoute;
    }

    public void setHasLongestRoute(Boolean hasLongestRoute) {
        this.hasLongestRoute = hasLongestRoute;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    public Map<Integer, Integer> getTickets() {
        return tickets;
    }

    public void addTicketToHand(Integer color) {
        this.tickets.put(color,tickets.get(color)+1);
    }

    public void removeTicketFromHand(Integer color) {
        this.tickets.put(color,this.tickets.get(color)-1);

    }
    public Integer countTickets() {
        int total = 0;
        for (Integer color : tickets.keySet()) {
            total += tickets.get(color);
        }
        return total;
    }


    public void removeTicketsFromHand(Integer color, Integer numTickets) {
        this.tickets.put(color, this.tickets.get(color) - numTickets);
    }

    private void initTickets() {
        for (int i = 1; i < 10; i++ ) {
            tickets.put(i, 0);
        }
        numTickets = 0;
    }

//    public Player copy() {
//        Player clone = new Player(username, playerColor);
//        clone.score = score;
//        clone.trainsRemaining = trainsRemaining;
//        clone.routesOwned = new HashSet<>(routesOwned);
//        clone.individualLongestRouteValue = individualLongestRouteValue;
//        clone.hasLongestRoute = hasLongestRoute;
//        clone.destinationCards = new ArrayList<>(destinationCards);
//        clone.numDestinationCards = numDestinationCards;
//        clone.tickets = new HashMap<>(tickets);
//        clone.numTickets = numTickets;
//        return clone;
//    }
}
