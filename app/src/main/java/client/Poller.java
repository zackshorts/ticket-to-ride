package client;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import models.data.Game;
import models.data.Result;

public class Poller {
    private Thread pollerThread;
    private CountDownLatch threadDoneSignal;
    private PollerState state;
    private static Poller singleton;

    public static Poller instance() {
        return singleton;
    }

    public static void end() {
        singleton.shutdown();
    }

    private static void create() {
        if (singleton == null){
            singleton = new Poller();
        }
    }

    public static void startLobbyPoller() {
        create();
        singleton.shutdown();
        singleton.startPollingLobby();
        singleton.start(0, 500, false);
    }

    public static void startGamePoller() {
        create();
        singleton.shutdown();
        singleton.startPollingGame();
        singleton.start(0, 500, false);
    }

    public ArrayList<Game> pollServerForGames() {
//        String className = PollManager.class.getName();
        String methodName = "getAvailableGames";

        Object[] parameterDataArray = new Object[1];
        parameterDataArray[0] = ClientModel.create().getUser().getUsername();

        Class<?>[] parameterClassArray = new Class<?>[1];
        parameterClassArray[0] = String.class;

        GeneralCommand newCommand = new GeneralCommand(methodName, parameterClassArray, parameterDataArray);

        ClientCommunicator communicator = new ClientCommunicator();

        Result result = communicator.send(newCommand);
        return result.getPollResult().getGamesChanged();
    }

    public Game pollServerForStartedGame() {
        ClientModel client = ClientModel.create();
//        String className = PollManager.class.getName();
        String methodName = "getRunningGame";

        Game game = client.getActiveGame();

        Object[] parameterDataArray = new Object[4];
        parameterDataArray[0] = game.getGameName();
        parameterDataArray[1] = client.getUser().getUsername();
        parameterDataArray[2] = game.getNumPlayerActions();
        parameterDataArray[3] = game.getChatLog().size();

        Class<?>[] parameterClassArray = new Class<?>[4];
        parameterClassArray[0] = String.class;
        parameterClassArray[1] = String.class;
        parameterClassArray[2] = Integer.class;
        parameterClassArray[3] = Integer.class;

        GeneralCommand newCommand = new GeneralCommand(methodName, parameterClassArray, parameterDataArray);

        ClientCommunicator communicator = new ClientCommunicator();

        Result result = communicator.send(newCommand);
        return result.getRunningGame();
    }


    //    We have to use Async tasks here
    private void runThread(int initialDelaySec, int delaySec, boolean fixedRate) {
        System.out.println("poller starting...");
        boolean initiating = true;
        long sleepTime = delaySec;
        while (true) {
            try {
                if (initiating) {
                    if (initialDelaySec > 0) {
                        Thread.sleep(initialDelaySec);
                    }
                    initiating = false;
                }
                else if (sleepTime > 0)
                    Thread.sleep(sleepTime);

                long startMillis = System.currentTimeMillis();
                poll();
                sleepTime = fixedRate ? delaySec - (System.currentTimeMillis() - startMillis) : delaySec;
            }
            catch (InterruptedException e) {
                break;
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error polling" + e.getMessage());
            }
        }
        threadDoneSignal.countDown();
        System.out.println("poller stopping.");
    }

    public void poll() {
        state.poll();
//        ServerProxy server = new ServerProxy();
//        ArrayList<Game> pollResult = pollServerForGames();
//        ClientModel client = ClientModel.create();
//
//        if (true) {
//            /*  Eventually, we will include code to disable this block of code
//                This block updates the lobby list with all games, replacing the
//                list completely every time. This is needlessly intensive if the
//                player is in a game and does not care about the lobby and can be
//                disabled by checking the active game in the if statement above
//            */
//            client.setChangedGameList(pollResult);
//            client.update();
//            client.setLobbyGamesList(pollResult);
//            for (Game game: client.getLobbyGamesList()) {
//                for (String userName: game.getPlayerNames()) {
//                    if (userName.equals(client.getUser().getUsername())) {
//                        client.getUser().setGameJoined(game);
//                    }
//                }
//            }
//        }
//
//        System.out.println("Current Complete Game List: " + client.getLobbyGamesList().toString());
    }

    public  String getThreadName() {
        return getClass().getName();
    }

    public void startPollingLobby() { state = new PSLobby(); }

    public void startPollingGame() {
        state = new PSGame();
    }


    public void start(final int delaySec) {
        start(0, delaySec, false);
    }

    public void start(final int initialDelaySec, final int delaySec, final boolean fixedRate) {
        threadDoneSignal = new CountDownLatch(1);
        pollerThread = new Thread(new Runnable() {
            public  void run() {
                runThread(initialDelaySec, delaySec, fixedRate);
            }
        }, getThreadName());
        pollerThread.start();
        System.out.println("poller thread for " + getThreadName() + " started...");
    }

    public void shutdown() {
        if (pollerThread != null) {
            System.out.println("shutting down... trying to interrupt poller thread...");
            boolean done = false;
            int numTries = 0;
            while (!done) {
                pollerThread.interrupt();
                try {
                    done = threadDoneSignal.await(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    // ingore InterruptedException here
                }
                if (!done) {
                    numTries = numTries + 1;
                    System.out.println("trying to interrupt write thread again " + numTries);
                } else {
                    System.out.println("shutted down successfully.");
                }
            }
        }
    }

    private interface PollerState {
        public void poll();
    }

    private class PSLobby implements PollerState {
        @Override
        public void poll() {
            ArrayList<Game> pollResult = pollServerForGames();
            ClientModel client = ClientModel.create();

            if (true) {
            /*  Eventually, we will include code to disable this block of code
                This block updates the lobby list with all games, replacing the
                list completely every time. This is needlessly intensive if the
                player is in a game and does not care about the lobby and can be
                disabled by checking the active game in the if statement above
            */
                client.setChangedGameList(pollResult);
                client.update();
                client.setLobbyGamesList(pollResult);
                for (Game game: client.getLobbyGamesList()) {
                    for (String userName: game.getPlayerUsernames()) {
                        if (userName.equals(client.getUser().getUsername())) {
                            client.setActiveGame(game);
                        }
                    }
                }
            }

            System.out.println("Current Complete Game List: " + client.getLobbyGamesList().toString());
        }
    }

    private class PSGame implements PollerState {
        @Override
        public void poll() {
//            ServerProxy server = new ServerProxy();
//            Game game = pollServerForStartedGame();
//            if (game != null) {
//                ClientModel client = ClientModel.create();
//                client.setActiveGame(game);
//            }
            Game updatedGame = pollServerForStartedGame();
            if (updatedGame != null) {
                ClientModel client = ClientModel.create();
                client.setActiveGame(updatedGame);
                client.updateGame();

                System.out.println("Current running game: " + ClientModel.create().getActiveGame().toString());
            }
        }
    }
}
