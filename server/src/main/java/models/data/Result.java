package models.data;

public class Result {
    /*
        This class will need to be more general. Perhaps a member to hold any throwable
        a member to hold an array of any object, a member to hold the class of the return
        function. Essentially a Generic response class.
     */


    private String errorMessage = "";
    private String authenticationToken;
    private String gameName;
    private boolean isSuccessful;
    private PollManagerData pollResult;
    private boolean isHost;
    private String gameJoined;
    private Game runningGame;

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public String getGameJoined() {
        return gameJoined;
    }

    public void setGameJoined(String gameJoined) {
        this.gameJoined = gameJoined;
    }

    public Result(String error, String auth, String gameName, boolean isSuccess) {
        this.authenticationToken = auth;
        this.gameName = gameName;
        this.errorMessage = error;
        this.isSuccessful = isSuccess;
        this.isHost = false;
        this.gameJoined = null;
    }

    public Result() {
    }

    public void setPollResult (PollManagerData data) {
        this.pollResult = data;
    }

    public PollManagerData getPollResult () {
        return this.pollResult;
    }

    public boolean isSuccessful() {
        return this.isSuccessful;
    }

    public void setSuccessful(boolean success) {
        this.isSuccessful = success;
    }

    public String getGame() {
        return gameName;
    }

    public void setGame(String gameName) {
        this.gameName = gameName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public void setRunningGame(Game game) {
        runningGame = game;
    }

    public Game getRunningGame() {
        return runningGame;
    }
}
