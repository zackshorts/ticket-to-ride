package view.presenterInterface;

import models.data.Result;

public interface ICardDeckPresenter {
    Integer getTrainCardAtPosition(int num);
    Result drawTrainCard(int num);
    Result drawDestinationCard();
    Integer getDestinationCardsLeft();
    Integer getTrainCardsLeft();
}
