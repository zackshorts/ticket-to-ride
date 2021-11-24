package com.example.cs340.tickettoride;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import client.ClientModel;
import client.PlayerStates.NotInGame;
import client.ServerProxy;
import models.TTR_Constants;
import models.data.Player;
import view.presenter.GameOverPresenter;
import view.presenterInterface.IGameOverPresenter;

public class GameOverActivity extends AppCompatActivity {
    ImageView winnerTop, winnerBottom, p2Top, p2Bottom, p3Top, p3Bottom, p4Top, p4Bottom, p5Top, p5Bottom;
    TextView winnerName, winnerScore, winnerGainedDestinationCards, winnerLostDestinationCards, winnerLongestRoute,
            p2Name, p2Score, p2gainedDestinationCards, p2LostDestinationCards, p2LongestRoute,
            p3Name, p3Score, p3gainedDestinationCards, p3LostDestinationCards, p3LongestRoute,
            p4Name, p4Score, p4gainedDestinationCards, p4LostDestinationCards, p4LongestRoute,
            p5Name, p5Score, p5gainedDestinationCards, p5LostDestinationCards, p5LongestRoute;
    Button backToGameLobbyButton;
    private Map playerColorValues;
    private IGameOverPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        playerColorValues = new HashMap();
        presenter = new GameOverPresenter();
        playerColorValues.put(TTR_Constants.getInstance().BLACK_PLAYER, R.drawable.black_background);
        playerColorValues.put(TTR_Constants.getInstance().BLUE_PLAYER, R.drawable.blue_background);
        playerColorValues.put(TTR_Constants.getInstance().GREEN_PLAYER, R.drawable.green_background);
        playerColorValues.put(TTR_Constants.getInstance().RED_PLAYER, R.drawable.red_background);
        playerColorValues.put(TTR_Constants.getInstance().YELLOW_PLAYER, R.drawable.yellow_background);

        // presenter.getPlayersInWinningOrder() returns an arrayList with the winner in the 0th element
        ArrayList<Player> playersInWinningOrder = presenter.getPlayersInWinningOrder();
        presenter.getLongestRoute();

        if (playersInWinningOrder.size() > 0) {
            winnerTop = findViewById(R.id.winner_color_top);
            winnerTop.setImageResource((int) playerColorValues.get(playersInWinningOrder.get(0).getPlayerColor()));
            winnerBottom = findViewById(R.id.winner_color_bottom);
            winnerBottom.setImageResource((int) playerColorValues.get(playersInWinningOrder.get(0).getPlayerColor()));
            winnerName = findViewById(R.id.winner_name_text_view);
            winnerName.setText("Winner: " + playersInWinningOrder.get(0).getUsername());
            winnerGainedDestinationCards = findViewById(R.id.winner_gained_destination_cards_text_view);
            Pair<Integer, Integer> pair = presenter.getDestinationPoints(playersInWinningOrder.get(0));
            winnerGainedDestinationCards.append(pair.first.toString());
            winnerLostDestinationCards = findViewById(R.id.winner_lost_destination_cards_text_view);
            winnerLostDestinationCards.append(pair.second.toString());
            winnerScore = findViewById(R.id.winner_score_text_view);
            winnerScore.append((playersInWinningOrder.get(0).getScore() + pair.first - pair.second) + "");

            // If winner has longest route, then make the 'Longest Route' text visible
            if (playersInWinningOrder.get(0).getHasLongestRoute()) {
                winnerLongestRoute = findViewById(R.id.winner_longest_path_text_view);
                winnerLongestRoute.setAlpha(1);
                winnerScore.setText("Score: " +(playersInWinningOrder.get(0).getScore() + pair.first - pair.second+10));
            }
        }

        if (playersInWinningOrder.size() > 1) {
            p2Top = findViewById(R.id.p2_color_top);
            p2Top.setImageResource((int) playerColorValues.get(playersInWinningOrder.get(1).getPlayerColor()));
            p2Bottom = findViewById(R.id.p2_color_bottom);
            p2Bottom.setImageResource((int) playerColorValues.get(playersInWinningOrder.get(1).getPlayerColor()));
            p2Name = findViewById(R.id.p2_name_text_view);
            p2Name.setText("2nd place: " + playersInWinningOrder.get(1).getUsername());
            p2Score = findViewById(R.id.p2_score_text_view);
            p2gainedDestinationCards = findViewById(R.id.p2_gained_destination_cards_text_view);
            Pair<Integer, Integer> pair = presenter.getDestinationPoints(playersInWinningOrder.get(1));
            p2gainedDestinationCards.append(pair.first.toString());
            p2LostDestinationCards = findViewById(R.id.p2_lost_destination_cards_text_view);
            p2LostDestinationCards.append(pair.second.toString());
            p2Score.append((playersInWinningOrder.get(1).getScore() + pair.first - pair.second) + "");

            // If p2 has longest route, then make the 'Longest Route' text visible
            if (playersInWinningOrder.get(1).getHasLongestRoute()) {
                p2LongestRoute = findViewById(R.id.p2_longest_path_text_view);
                p2LongestRoute.setAlpha(1);
                p2Score.setText("Score: " + (playersInWinningOrder.get(1).getScore() + pair.first - pair.second+10));
            }
        }

        if (playersInWinningOrder.size() > 2) {
            p3Top = findViewById(R.id.p3_color_top);
            p3Top.setImageResource((int) playerColorValues.get(playersInWinningOrder.get(2).getPlayerColor()));
            p3Bottom = findViewById(R.id.p3_color_bottom);
            p3Bottom.setImageResource((int) playerColorValues.get(playersInWinningOrder.get(2).getPlayerColor()));
            p3Name = findViewById(R.id.p3_name_text_view);
            p3Name.setText("3rd place: " + playersInWinningOrder.get(2).getUsername());
            p3Score = findViewById(R.id.p3_score_text_view);
            p3gainedDestinationCards = findViewById(R.id.p3_gained_destination_cards_text_view);
            Pair<Integer, Integer> pair = presenter.getDestinationPoints(playersInWinningOrder.get(2));
            p3gainedDestinationCards.append(pair.first.toString());
            p3LostDestinationCards = findViewById(R.id.p3_lost_destination_cards_text_view);
            p3LostDestinationCards.append(pair.second.toString());
            p3Score.append((playersInWinningOrder.get(2).getScore() + pair.first - pair.second) + "");

            // If p3 has longest route, then make the 'Longest Route' text visible
            if (playersInWinningOrder.get(2).getHasLongestRoute()) {
                p3LongestRoute = findViewById(R.id.p3_longest_path_text_view);
                p3LongestRoute.setAlpha(1);
                p3Score.setText("Score: " + (playersInWinningOrder.get(2).getScore() + pair.first - pair.second+10));
            }
        }

        if (playersInWinningOrder.size() > 3) {
            p4Top = findViewById(R.id.p4_color_top);
            p4Top.setImageResource((int) playerColorValues.get(playersInWinningOrder.get(3).getPlayerColor()));
            p4Bottom = findViewById(R.id.p4_color_bottom);
            p4Bottom.setImageResource((int) playerColorValues.get(playersInWinningOrder.get(3).getPlayerColor()));
            p4Name = findViewById(R.id.p4_name_text_view);
            p4Name.setText("4th place: " + playersInWinningOrder.get(3).getUsername());
            p4Score = findViewById(R.id.p4_score_text_view);
            p4Score.append(playersInWinningOrder.get(3).getScore().toString());
            p4gainedDestinationCards = findViewById(R.id.p4_gained_destination_cards_text_view);
            Pair<Integer, Integer> pair = presenter.getDestinationPoints(playersInWinningOrder.get(3));
            p4gainedDestinationCards.append(pair.first.toString());
            p4LostDestinationCards = findViewById(R.id.p4_lost_destination_cards_text_view);
            p4LostDestinationCards.append(pair.second.toString());
            p4Score.append((playersInWinningOrder.get(3).getScore() + pair.first - pair.second) + "");

            // If p4 has longest route, then make the 'Longest Route' text visible
            if (playersInWinningOrder.get(3).getHasLongestRoute()) {
                p4LongestRoute = findViewById(R.id.p4_longest_path_text_view);
                p4LongestRoute.setAlpha(1);
                p4Score.setText("Score: " + (playersInWinningOrder.get(3).getScore() + pair.first - pair.second+10));
            }
        }

        if (playersInWinningOrder.size() > 4) {
            p5Top = findViewById(R.id.p5_color_top);
            p5Top.setImageResource((int) playerColorValues.get(playersInWinningOrder.get(4).getPlayerColor()));
            p5Bottom = findViewById(R.id.p5_color_bottom);
            p5Bottom.setImageResource((int) playerColorValues.get(playersInWinningOrder.get(4).getPlayerColor()));
            p5Name = findViewById(R.id.p5_name_text_view);
            p5Name.setText("5th place: " + playersInWinningOrder.get(4).getUsername());
            p5Score = findViewById(R.id.p5_score_text_view);
            p5gainedDestinationCards = findViewById(R.id.p5_gained_destination_cards_text_view);
            Pair<Integer, Integer> pair = presenter.getDestinationPoints(playersInWinningOrder.get(4));
            p5gainedDestinationCards.append(pair.first.toString());
            p5LostDestinationCards = findViewById(R.id.p5_lost_destination_cards_text_view);
            p5LostDestinationCards.append(pair.second.toString());
            p5Score.append((playersInWinningOrder.get(4).getScore() + pair.first - pair.second) + "");

            // If p5 has longest route, then make the 'Longest Route' text visible
            if (playersInWinningOrder.get(4).getHasLongestRoute()) {
                p5LongestRoute = findViewById(R.id.p5_longest_path_text_view);
                p5LongestRoute.setAlpha(1);
                p5Score.setText("Score: " + (playersInWinningOrder.get(4).getScore() + pair.first - pair.second+10));
            }
        }

        backToGameLobbyButton = findViewById(R.id.back_to_game_lobby_btn);
        backToGameLobbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ClientModel.create().leaveGame();
                ClientModel clientModel = ClientModel.create();
                ServerProxy serverProxy = new ServerProxy();
                serverProxy.endGame(clientModel.getUser().getGameJoined().getGameName());
                clientModel.setActiveGame(null);
                clientModel.setState(NotInGame.getInstance());
                Intent intent = new Intent(GameOverActivity.this, LobbyViewActivity.class);
                startActivity(intent);
            }
        });


    }
}
