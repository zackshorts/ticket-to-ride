package com.example.cs340.tickettoride;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import models.data.ChatMessage;

public class RecyclerViewAdapterChat  extends RecyclerView.Adapter<RecyclerViewAdapterChat.ViewHolder> {


    private ArrayList<ChatMessage> listOfMessages = new ArrayList<>();

    public RecyclerViewAdapterChat(ArrayList<ChatMessage> listOfMessages) {
        this.listOfMessages = listOfMessages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layoutlist_item_chat, viewGroup, false);
        RecyclerViewAdapterChat.ViewHolder holder = new RecyclerViewAdapterChat.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if (!listOfMessages.get(position).isContainsAnAction()) {
            holder.currentMessage.setText(listOfMessages.get(position).getMessageContents());
            holder.timestamp.setText(listOfMessages.get(position).getTimeStamp());
            holder.user.setText(listOfMessages.get(position).getAuthorUserName());
        }
        else {
            System.out.println("Action printed in chat");
            holder.currentMessage.setTextColor(Color.RED);
            holder.currentMessage.setAllCaps(true);
            holder.currentMessage.setText(listOfMessages.get(position).getMessageContents());
            holder.timestamp.setTextColor(Color.RED);
            holder.timestamp.setAllCaps(true);
            holder.timestamp.setText(listOfMessages.get(position).getTimeStamp());
            holder.user.setTextColor(Color.RED);
            holder.user.setAllCaps(true);
            holder.user.setText(listOfMessages.get(position).getAuthorUserName());
        }

        //
//        holder.currentMessage.setText(listOfMessages.get(position).getMessageContents());
//        holder.timestamp.setText(listOfMessages.get(position).getTimeStamp());
//        holder.user.setText(listOfMessages.get(position).getAuthorUserName());
    }

    public void setListOfMessages(ArrayList<ChatMessage> newArrayList) {
        this.listOfMessages = newArrayList;
    }

    @Override
    public int getItemCount() {
        return listOfMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView timestamp, user, currentMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timestamp = itemView.findViewById(R.id.textViewTime);
            user = itemView.findViewById(R.id.textViewUser);
            currentMessage = itemView.findViewById(R.id.textViewMessage);
        }
    }
}