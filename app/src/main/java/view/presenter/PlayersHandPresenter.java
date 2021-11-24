package view.presenter;

import com.example.cs340.tickettoride.GameBoardActivity;

import java.util.Observable;
import java.util.Observer;

import client.ClientModel;
import models.data.Player;
import view.presenterInterface.IPlayersHandPresenter;

public class PlayersHandPresenter implements IPlayersHandPresenter, Observer {
    ClientModel clientModel = ClientModel.create();
    private GameBoardActivity boardActivity;

    public PlayersHandPresenter(GameBoardActivity activity) {
        boardActivity = activity;
        clientModel.setmPlayersHandPresenter(this);
    }

    @Override
    public Player getCurrentPlayer() {
        return clientModel.getPlayer();
    }

    @Override
    public int getCurrentPlayerColor() {
        return clientModel.getUser().getGameJoined().getCurrentTurnPlayer();
    }

    @Override
    public Integer getTrainCardAmount(Integer color) {
        return clientModel.getUser().getGameJoined().findPlayer(
                clientModel.getUser().getUsername()).getTickets().get(color);
    }

    @Override
    public void update(Observable o, Object arg) {
        boardActivity.new UpdateAsyncTask(boardActivity).execute();
    }
}
