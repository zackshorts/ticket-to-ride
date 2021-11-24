package models.data;

public class DestinationCard {
    private String[] locations;
    private Integer points;

    public DestinationCard() {
    }

    public String[] getLocations() {
        return locations;
    }

    public Integer getPoints() {
        return points;
    }

    public DestinationCard(String[] locations, Integer points) {
        this.locations = locations;
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        DestinationCard d = (DestinationCard) o;
        return (points.equals(d.points) && locations[0].equals(d.locations[0]) && locations[1].equals(d.locations[1]));
    }
}
