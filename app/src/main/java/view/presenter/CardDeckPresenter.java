package view.presenter;

import com.example.cs340.tickettoride.GameBoardActivity;
import java.util.Observable;
import java.util.Observer;
import client.ClientModel;
import client.ServerProxy;
import models.data.Result;
import view.presenterInterface.ICardDeckPresenter;

public class CardDeckPresenter implements ICardDeckPresenter, Observer {
    ClientModel clientModel = ClientModel.create();
    ServerProxy serverProxy = new ServerProxy();
    Boolean secondTurn = false;
    private GameBoardActivity boardActivity;

    public CardDeckPresenter(GameBoardActivity activity) {
        boardActivity = activity;
        clientModel.setmCardDeckPresenter(this);
    }

    @Override
    public Integer getTrainCardAtPosition(int num) {
        return clientModel.getUser().getGameJoined().getFaceUpTrainCards()[num-1].getCardColor();
    }

    @Override
    public Result drawTrainCard(int cardNum) {
        return ClientModel.create().requestTicketCard(cardNum);
    }

    @Override
    public Result drawDestinationCard() {
        return ClientModel.create().requestDestinationCards();
    }

    @Override
    public Integer getDestinationCardsLeft() {
        return clientModel.getUser().getGameJoined().getDestinationDeck().size();
    }

    @Override
    public Integer getTrainCardsLeft() {
        return clientModel.getUser().getGameJoined().countTickets();
    }

    @Override
    public void update(Observable o, Object arg) {
        boardActivity.new UpdateAsyncTask(boardActivity).execute();
    }
}
