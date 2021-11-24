package models.data;

public class TrainCard {
    public Integer CardColor;

    public TrainCard(Integer cardColor) {
        this.CardColor = cardColor;
    }

    public TrainCard() {
    }

    public Integer getCardColor() {
        return CardColor;
    }

    public void setCardColor(Integer cardColor) {
        CardColor = cardColor;
    }

}
