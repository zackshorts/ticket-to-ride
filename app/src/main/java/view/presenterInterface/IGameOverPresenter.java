package view.presenterInterface;

import android.util.Pair;

import java.util.ArrayList;

import models.data.Player;

public interface IGameOverPresenter {
    public void getLongestRoute();
    public ArrayList<Player> getPlayersInWinningOrder();
    public Pair<Integer, Integer> getDestinationPoints(Player player);
}
