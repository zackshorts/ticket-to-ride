package com.example.cs340.tickettoride;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import models.data.Result;
import view.presenterInterface.IPlayerInfoPresenter;

public class RecyclerViewAdapterDestinationCards extends RecyclerView.Adapter<RecyclerViewAdapterDestinationCards.ViewHolder> {

    private ArrayList<String> mDestinationRoutes;
    private ArrayList<Button> mDiscardButtons;
    private Context mContext;
    private IPlayerInfoPresenter playerInfoPresenter;
    private int mLengthOfNewDestinationCards;

    public RecyclerViewAdapterDestinationCards(ArrayList<String> mDestinationRoutes, ArrayList<Button> mDiscardButtons, int lengthOfNewDestinationCards, Context mContext, IPlayerInfoPresenter playerInfoPresenter) {
        this.mDestinationRoutes = mDestinationRoutes;
        this.mDiscardButtons = mDiscardButtons;
        this.mContext = mContext;
        this.playerInfoPresenter = playerInfoPresenter;
        this.mLengthOfNewDestinationCards = lengthOfNewDestinationCards;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_destination_card, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.destinationRoute.setText(mDestinationRoutes.get(position));
        // This if statement will make sure the discard button doesn't show on old destination cards
        if (position >= mLengthOfNewDestinationCards)
            holder.discardButton.setVisibility(View.GONE);
        else
            holder.discardButton.setVisibility(View.VISIBLE);

        if (mDiscardButtons.get(position).isEnabled()) {
            holder.discardButton.getBackground().setColorFilter(null);
            holder.discardButton.setAlpha(1);
            holder.discardButton.setEnabled(true);
        }

        holder.discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerInfoPresenter.addToListOfDestinationCardsToDiscard(mDestinationRoutes.get(position))) {
                    mDiscardButtons.get(position).getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
                    mDiscardButtons.get(position).setAlpha(.5f);
                    mDiscardButtons.get(position).setEnabled(false);

                    v.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
                    v.setAlpha(.5f);
                    v.setEnabled(false);
                    Toast.makeText(mContext, mDestinationRoutes.get(position) +
                            "\nThis card will be discarded", Toast.LENGTH_SHORT).show();
                }

                else
                    Toast.makeText(mContext, "You cannot discard more cards", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDestinationRoutes.size();
    }

    public void setListOfDestinationCards(ArrayList<String> destinationCardList, ArrayList<Button> discardButtons, int lengthOfNewDestinationCards) {
        mDestinationRoutes = destinationCardList;
        mDiscardButtons = discardButtons;
        mLengthOfNewDestinationCards = lengthOfNewDestinationCards;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView destinationRoute;
        Button discardButton;

        public ViewHolder(View itemView) {
            super(itemView);
            destinationRoute = itemView.findViewById(R.id.textview_for_destination_card);
            discardButton = itemView.findViewById(R.id.discard_button);
        }
    }

}
