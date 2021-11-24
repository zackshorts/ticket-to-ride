package view.presenterInterface;

import java.util.ArrayList;

import models.data.DestinationCard;
import models.data.Player;

public interface IPlayersHandPresenter {
    int getCurrentPlayerColor();
    Player getCurrentPlayer();
    Integer getTrainCardAmount(Integer color);
}
