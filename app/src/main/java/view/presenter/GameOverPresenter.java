package view.presenter;


import android.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import client.ClientModel;
import models.TTR_Constants;
import models.data.DestinationCard;
import models.data.Player;
import models.data.Route;
import view.presenterInterface.IGameOverPresenter;

public class GameOverPresenter implements IGameOverPresenter {

    ClientModel clientModel = ClientModel.create();
    @Override
    public ArrayList<Player> getPlayersInWinningOrder() {
        ArrayList<Player> winningOrder = new ArrayList<>();
        ArrayList<Player> players = new ArrayList<Player>(clientModel.getUser().getGameJoined().getPlayers());
        while (players.size() > 0) {
            Player highestPlayer = players.get(0);
            Pair<Integer, Integer> highestPlayerPair = getDestinationPoints(highestPlayer);
            for (Player otherPlayer : players) {
                Pair<Integer, Integer> otherPlayerPair = getDestinationPoints(otherPlayer);
                if (highestPlayer.getScore()+highestPlayerPair.first-highestPlayerPair.second < otherPlayer.getScore()+otherPlayerPair.first-otherPlayerPair.second) {
                    highestPlayer = otherPlayer;
                    highestPlayerPair = otherPlayerPair;
                }
            }
            winningOrder.add(highestPlayer);
            players.remove(highestPlayer);
        }
        return winningOrder;
    }

    @Override
    public void getLongestRoute() {
        ArrayList<Player> players = clientModel.getUser().getGameJoined().getPlayers();
        Player mostRoutesPlayer = players.get(0);
        for (Player player : players) {
            if (player.getRoutesOwned().size() > mostRoutesPlayer.getRoutesOwned().size()) {
                mostRoutesPlayer = player;
            }
        }
        mostRoutesPlayer.setHasLongestRoute(true);
    }
    // first object in pair are the points gained from destination cards second is points lost from destination cards.
    @Override
    public Pair<Integer, Integer> getDestinationPoints(Player player) {
        Integer pointsGained = 0;
        Integer pointsLost = 0;
        for (DestinationCard destinationCard: player.getDestinationCardHand()) {
            String location1 = destinationCard.getLocations()[0];
            String location2 = destinationCard.getLocations()[1];
            if (TTR_Constants.getInstance().graph.pathExists(location1,location2,player)) {
                pointsGained += destinationCard.getPoints();
            }
            else {
                pointsLost += destinationCard.getPoints();
            }
        }
        return new Pair<>(pointsGained,pointsLost);
    }
}
