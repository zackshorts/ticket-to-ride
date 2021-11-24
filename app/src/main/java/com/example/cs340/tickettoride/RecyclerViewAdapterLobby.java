package com.example.cs340.tickettoride;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import models.data.Game;
import models.data.Result;
import view.presenterInterface.IGameLobbyPresenter;


// This class will perform all the logic for anything within the recyclerView

public class RecyclerViewAdapterLobby extends RecyclerView.Adapter<RecyclerViewAdapterLobby.ViewHolder> {

    private ArrayList<Game> listOfGames = new ArrayList<>();
    private Context mContext;
    private IGameLobbyPresenter presenter; // = new GameLobbyPresenter();


    public RecyclerViewAdapterLobby(ArrayList<Game> listOfGames, Context mContext) {
        this.listOfGames = listOfGames;
        this.mContext = mContext;
        presenter = ((LobbyViewActivity)mContext).getPresenter();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layoutlist_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void setListOfGames(ArrayList<Game> listOfGames) {
        this.listOfGames = listOfGames;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.currentNumOfPlayers.setText("Current players: " +
                listOfGames.get(position).getPlayerUsernames().size());
        holder.gameName.setText(listOfGames.get(position).getGameName());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // Check if user is already in a game. If not, add them to game
                // Don't allow more than 5 people join a game
                if (presenter.getPlayer().getGameJoined() != null)
                    Toast.makeText(mContext, "Already part of a game", Toast.LENGTH_SHORT).show();
                else {
                    if (listOfGames.get(position).getPlayerUsernames().size() > 4)
                        Toast.makeText(mContext, "Too many players", Toast.LENGTH_SHORT).show();
                    else {
                        Result result = presenter.addPlayer(listOfGames.get(position));
                        if (result.isSuccessful()) {


                            Toast.makeText(mContext, "You've been added to " +
                                    listOfGames.get(position).getGameName(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfGames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView gameName, currentNumOfPlayers;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gameName = itemView.findViewById(R.id.gameName);
            currentNumOfPlayers = itemView.findViewById(R.id.currentNumOfPlayers);
            parentLayout = itemView.findViewById(R.id.parent_layout_lobby);
        }
    }
}
