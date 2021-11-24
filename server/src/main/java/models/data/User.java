package models.data;

public class User {
    private String username;
    private String password;
    private Boolean host;
    private Game gameJoined;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.host = false;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public void setGameJoined(Game gameJoined) {
        this.gameJoined = gameJoined;
    }



    public Game getGameJoined() {
        return gameJoined;
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(Boolean host) {
        this.host = host;
    }
}
