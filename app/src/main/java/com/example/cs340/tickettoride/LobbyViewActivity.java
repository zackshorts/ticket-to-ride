package com.example.cs340.tickettoride;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import client.Poller;
import models.data.Game;
import models.data.Result;
import models.data.User;
import view.presenter.GameLobbyPresenter;
import view.presenterInterface.IGameLobbyPresenter;

public class LobbyViewActivity extends AppCompatActivity /*implements IGameLobby*/ {

    // Member variables
    private ArrayList<Game> listOfGames;
    private Button startGameButton, createGameButton;
    private boolean createGameOpen = false;
    private String create_game_text = "";
    private RecyclerViewAdapterLobby adapter;
    private IGameLobbyPresenter presenter;
    private LobbyViewActivity singleton;
    private Activity a = LobbyViewActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_lobby_view);
        presenter = new GameLobbyPresenter(LobbyViewActivity.this);

        presenter.onCreate();
        initRecyclerView();


        // Initialize startGameButton and set onClickListener
        // If the user is not a host, disable the 'Start Game' button
        startGameButton = findViewById(R.id.startGameButton);
        if (!presenter.getPlayer().isHost()) {
            disableStartGameButton();
        } else {
            enableStartGameButton();
        }

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the user is a host, the button will be enabled
//              // When clicked, the GameBoardActivity will be started
                presenter.startGame();

            }
        });


        // Initialize createGameButton and set onClickListener
        // This is the button at the bottom of the recycler view
        createGameButton = findViewById(R.id.createGameButton);
        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LobbyViewActivity.this);
                builder.setTitle("Create Game");
                builder.setMessage("Choose a name for your game");

                final EditText input = new EditText(LobbyViewActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        // Enable startGameButton and disable createGameButton
                        Game game = new Game(input.getText().toString());
                        Result result = presenter.createGame(game);
                        //enableStartGameButton();
                        //disableCreateGameButton();

                        if (result.isSuccessful()) {
                            //enableStartGameButton();
                            disableCreateGameButton();
                            Toast.makeText(LobbyViewActivity.this, "Succesfully created game: " + game.getGameName(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LobbyViewActivity.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        // This is the button to create game in the dialogue box
        final Button createGameButtonDialog;
        createGameButtonDialog = findViewById(R.id.create_game_button);
        if (createGameOpen) {
            createGameButtonDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(LobbyViewActivity.this, "Game Created", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public IGameLobbyPresenter getPresenter() {
        return presenter;
    }

    // This initializes the recyclerView
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        listOfGames = presenter.getGameList();
        adapter = new RecyclerViewAdapterLobby(listOfGames, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    public Void updateGameList(ArrayList<Game> lobbyGames, User user) {
        listOfGames = lobbyGames;
        return null;
    }


    public void updateGameListAfterClickingOnGame(ArrayList<Game> lobbyGames) {
        // Disable 'create game' button
        disableCreateGameButton();

        listOfGames = lobbyGames;
        if (listOfGames.size() > 1) {
            // Enable 'start game' button
            enableStartGameButton();
        }
    }


    public void disableStartGameButton() {
        startGameButton.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
        startGameButton.setAlpha(.5f);
        startGameButton.setEnabled(false);
    }

    public void enableStartGameButton() {
        startGameButton.getBackground().setColorFilter(null);
        startGameButton.setAlpha(1);
        startGameButton.setEnabled(true);
    }

    public void disableCreateGameButton(){
        createGameButton.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
        createGameButton.setAlpha(.5f);
        createGameButton.setEnabled(false);
    }

    public void enableCreateGameButton() {
        createGameButton.getBackground().setColorFilter(null);
        createGameButton.setAlpha(1);
        createGameButton.setEnabled(true);
    }



    // AsyncTask class
    public class UpdateGameListAsyncTask extends AsyncTask<ArrayList<Game>, Void, Void> {
        //private IGameLobby gameLobby = new LobbyViewActivity();
        private User user;
        private Context context;
        

        //Constructor to make
        public UpdateGameListAsyncTask(User user, Context context) {
            this.user = user;
            this.context = context.getApplicationContext();
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         */

        // It's possible that having this return 'void' could cause a problem
        @Override
        protected Void doInBackground(ArrayList<Game>... arrayLists) {
            return updateGameList(arrayLists[0], user);
        }

        @Override
        protected void onPostExecute(Void result) {

            adapter.setListOfGames(listOfGames);
            adapter.notifyDataSetChanged();

            // If user is part of a game, disable the createGameButton
            if (user.getGameJoined() != null)
                disableCreateGameButton();

                // If user isn't part of a game, disable the startGameButton
            else
                disableStartGameButton();

            System.out.println(user);
            // If user is a host and the amount of players in the game is greater than 1, then
            // enable the startGamebutton
            if (user.getGameJoined() != null) {
                if (user.isHost() && user.getGameJoined().getPlayerUsernames().size() > 1) {
                    enableStartGameButton();
                }
                // If not a host or if there aren't enough players, disable the 'Start game' button
                else {
                    disableStartGameButton();
                }
//                if (!listOfGames.contains(user.getGame())) {
//                    user.getGame().setStarted(true);
//                    // client.setActiveGame(user.getGame()) // Not sure, Aliasing should handle this...
//                }
                if (user.getGameJoined().isStarted()) {
                    Poller.instance().startPollingGame();
                    Intent intent = new Intent(context, GameBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }


        }


    }

}
