package models.data;

public class Route {
    private Integer points;
    private Integer cardColor;
    private String[] location;

    public Route(Integer points, Integer cardColor, String[] location) {
        this.points = points;
        this.cardColor = cardColor;
        this.location = location;
    }

    public Route() {
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoint(Integer points) {
        this.points = points;
    }

    public int findLength() {
        switch (points.intValue()) {
            case (1): return 1;
            case (2): return 2;
            case (4): return 3;
            case (7): return 4;
            case (10): return 5;
            case (15): return 6;
            default:
                return 0;
        }
    }

    public Integer getCardColor() {
        return cardColor;
    }

    public void setCardColor(Integer cardColor) {
        this.cardColor = cardColor;
    }

    public String[] getLocation() {
        return location;
    }

    public void setLocation(String[] locations) {
        this.location = locations;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        Route r = (Route) o;

        return (location[0].equals(r.location[0]) && location[1].equals(r.location[1]) &&
                points.equals(r.points) && cardColor.equals(r.cardColor));
    }

    @Override
    public int hashCode() {
        return location[0].hashCode() + location[1].hashCode();
    }
}
