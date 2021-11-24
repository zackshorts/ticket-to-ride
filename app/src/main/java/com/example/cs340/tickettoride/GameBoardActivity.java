package com.example.cs340.tickettoride;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import client.ClientModel;
import models.TTR_Constants;
import models.data.ChatMessage;
import models.data.Player;
import models.data.Route;
import view.presenter.CardDeckPresenter;
import view.presenter.ChatPresenter;
import view.presenter.PlayerInfoPresenter;
import view.presenter.PlayersHandPresenter;
import view.presenter.RoutePresenter;
import view.presenterInterface.ICardDeckPresenter;
import view.presenterInterface.IChatPresenter;
import view.presenterInterface.IPlayerInfoPresenter;
import view.presenterInterface.IPlayersHandPresenter;
import view.presenterInterface.IRoutePresenter;

//import view.presenter.DemoPresenter;

public class GameBoardActivity extends AppCompatActivity {

    private ArrayList<ChatMessage> chatMessages;
    private ArrayList<String> newDestinationCardList;
    private ArrayList<String> currentDestinationCardList;
    private IChatPresenter chatPresenter;
    private IPlayersHandPresenter playersHandPresenter;
    private IPlayerInfoPresenter playerInfoPresenter;
    private ICardDeckPresenter cardDeckPresenter;
    private IRoutePresenter routePresenter;
    private RecyclerViewAdapterChat adapter;
    private RecyclerViewAdapterDestinationCards destinationCardsAdapter;
    private EditText inputChatEditText;
    private Button sendMessageButton, playerInfoButton, doneButton;
    private Button mGreenTrainCard, mRedTrainCard, mPinkTrainCard, mYellowTrainCard,
            mWhiteTrainCard, mBlackTrainCard, mWildTrainCard, mBlueTrainCard, mOrangeTrainCard;
    private Button destinationCardDeck, trainCardDeck, cardOne, cardTwo, cardThree, cardFour, cardFive;
    private Button redDeck, blueDeck, greenDeck, yellowDeck, purpleDeck, orangeDeck, whiteDeck, blackDeck, rainbowDeck;
    private ImageView gameBoard;
    private Map playerColorValues;
    private Map trainCardImages;
    private PopupWindow mPopupWindow;
    private TextView one_destinationCards, one_trainCards, one_score, one_trainsLeft;
    private TextView two_destinationCards, two_trainCards, two_score, two_trainsLeft;
    private TextView three_destinationCards, three_trainCards, three_score, three_trainsLeft;
    private TextView four_destinationCards, four_trainCards, four_score, four_trainsLeft;
    private TextView five_destinationCards, five_trainCards, five_score, five_trainsLeft;
    private ImageView blueTurn, redTurn, blackTurn, yellowTurn, greenTurn;
    private TextView player1_username, player2_username, player3_username, player4_username, player5_username;
    private DrawerLayout activityLayout;
    private ClientModel clientModel;
    private ArrayList<Button> discardButtons;
    private int lengthOfNewDestinationCards;
    private Integer selectedTicketColor = TTR_Constants.getInstance().EMPTY;


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You committed to this game. No turning back.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables sticky immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        clientModel = ClientModel.create();
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_game_board);

        playerColorValues = new HashMap();
        trainCardImages = new HashMap<>();
        playerColorValues.put(TTR_Constants.getInstance().BLACK_PLAYER, R.drawable.black_background);
        playerColorValues.put(TTR_Constants.getInstance().BLUE_PLAYER, R.drawable.blue_background);
        playerColorValues.put(TTR_Constants.getInstance().GREEN_PLAYER, R.drawable.green_background);
        playerColorValues.put(TTR_Constants.getInstance().RED_PLAYER, R.drawable.red_background);
        playerColorValues.put(TTR_Constants.getInstance().YELLOW_PLAYER, R.drawable.yellow_background);

        trainCardImages.put(TTR_Constants.getInstance().EMPTY, R.drawable.back_of_train_card);
        trainCardImages.put(TTR_Constants.getInstance().BLACK, R.drawable.train_card_black);
        trainCardImages.put(TTR_Constants.getInstance().BLUE, R.drawable.train_card_blue);
        trainCardImages.put(TTR_Constants.getInstance().GREEN, R.drawable.train_card_green);
        trainCardImages.put(TTR_Constants.getInstance().PURPLE, R.drawable.train_card_purple);
        trainCardImages.put(TTR_Constants.getInstance().ORANGE, R.drawable.train_card_orange);
        trainCardImages.put(TTR_Constants.getInstance().RED, R.drawable.train_card_red);
        trainCardImages.put(TTR_Constants.getInstance().WHITE, R.drawable.train_card_white);
        trainCardImages.put(TTR_Constants.getInstance().WILD, R.drawable.train_card_wild);
        trainCardImages.put(TTR_Constants.getInstance().YELLOW, R.drawable.train_card_yellow);

        final View decorView = getWindow().getDecorView();


        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            // TODO: The system bars are visible. Make any desired
                            // adjustments to your UI, such as showing the action bar or
                            // other navigational controls.
                            System.out.print("faj");
                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                            System.out.print("faj");
                        }
                    }
                });

        chatPresenter = new ChatPresenter(this);
        cardDeckPresenter = new CardDeckPresenter(this);
        playerInfoPresenter = new PlayerInfoPresenter(this);
        playersHandPresenter = new PlayersHandPresenter(this);
        routePresenter = new RoutePresenter(this);
        initRecyclerView();
        initDestinationCardsRecyclerView();
        blueTurn = findViewById(R.id.blue_turn_color);
        redTurn = findViewById(R.id.red_turn_color);
        blackTurn = findViewById(R.id.black_turn_color);
        yellowTurn = findViewById(R.id.yellow_turn_color);
        greenTurn = findViewById(R.id.green_turn_color);
        inputChatEditText = findViewById(R.id.input_edit_text);
        sendMessageButton = findViewById(R.id.send_message_button);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            // When the sendMessage button is clicked, send the text to the chatPresenter.addMessage function
            @Override
            public void onClick(View v) {
                String newMessage = inputChatEditText.getText().toString();
                chatPresenter.addMessage(newMessage);
                inputChatEditText.setText("");
            }
        });

//                 //FIXME: Break up game demo into multiple button presses. Remove waits?
// //                mDemoPresenter.runNextDemo();
//             }
//         });
        doneButton = mPopupWindow.getContentView().findViewById(R.id.done_button);

        // Open up a popup window when 'Player destination Cards' button is pressed
        playerInfoButton = findViewById(R.id.get_player_info_button);
        playerInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.showAtLocation(activityLayout, Gravity.CENTER,0,0);
                /*
                // Re-enable the done button if there are cards to discard
                if (playerInfoPresenter.getNewDestinationCardStrings().size() > 0) {
                    doneButton.getBackground().setColorFilter(null);
                    doneButton.setAlpha(1);
                    doneButton.setEnabled(true);
                }
                else {
                    doneButton.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
                    doneButton.setAlpha(.5f);
                    doneButton.setEnabled(false);
                }
                */
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
                        v.setAlpha(.5f);
                        v.setEnabled(false);
                        if (playerInfoPresenter.returnDestinationCards() != null)
                            Toast.makeText(GameBoardActivity.this, "Successfully returned destination cards.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(GameBoardActivity.this, "Cannot return destination cards", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        player1_username = findViewById(R.id.player1_name_text_view);
        player2_username = findViewById(R.id.player2_name_text_view);
        player3_username = findViewById(R.id.player3_name_text_view);
        player4_username = findViewById(R.id.player4_name_text_view);
        player5_username = findViewById(R.id.player5_name_text_view);
        mGreenTrainCard = findViewById(R.id.greenCard);
        mGreenTrainCard.setShadowLayer(5,0,0,Color.BLACK);
        mRedTrainCard = findViewById(R.id.redCard);
        mRedTrainCard.setShadowLayer(5,0,0,Color.BLACK);
        mPinkTrainCard = findViewById(R.id.pinkCard);
        mPinkTrainCard.setShadowLayer(5,0,0,Color.BLACK);
        mYellowTrainCard = findViewById(R.id.yellowCard);
        mYellowTrainCard.setShadowLayer(5,0,0,Color.BLACK);
        mWhiteTrainCard = findViewById(R.id.whiteCard);
        mWhiteTrainCard.setShadowLayer(5,0,0,Color.BLACK);
        mBlackTrainCard = findViewById(R.id.blackCard);
        mBlackTrainCard.setShadowLayer(5,0,0,Color.BLACK);
        mWildTrainCard = findViewById(R.id.wildCard);
        mWildTrainCard.setShadowLayer(5,0,0,Color.BLACK);
        mBlueTrainCard = findViewById(R.id.blueCard);
        mBlueTrainCard.setShadowLayer(5,0,0,Color.BLACK);
        mOrangeTrainCard = findViewById(R.id.orangeCard);
        mOrangeTrainCard.setShadowLayer(5,0,0,Color.BLACK);
        one_destinationCards = findViewById(R.id.player1_destination_cards_text_view);
        one_score = findViewById(R.id.player1_score_text_view);
        one_trainCards = findViewById(R.id.player1_train_cards_text_view);
        one_trainsLeft = findViewById(R.id.player1_trains_left_text_view);
        two_destinationCards = findViewById(R.id.player2_destination_cards_text_view);
        two_score = findViewById(R.id.player2_score_text_view);
        two_trainCards = findViewById(R.id.player2_train_cards_text_view);
        two_trainsLeft = findViewById(R.id.player2_trains_left_text_view);
        three_destinationCards = findViewById(R.id.player3_destination_cards_text_view);
        three_score = findViewById(R.id.player3_score_text_view);
        three_trainCards = findViewById(R.id.player3_train_cards_text_view);
        three_trainsLeft = findViewById(R.id.player3_trains_left_text_view);
        four_destinationCards = findViewById(R.id.player4_destination_cards_text_view);
        four_score = findViewById(R.id.player4_score_text_view);
        four_trainCards = findViewById(R.id.player4_train_cards_text_view);
        four_trainsLeft = findViewById(R.id.player4_trains_left_text_view);
        five_destinationCards = findViewById(R.id.player5_destination_cards_text_view);
        five_score = findViewById(R.id.player5_score_text_view);
        five_trainCards = findViewById(R.id.player5_train_cards_text_view);
        five_trainsLeft = findViewById(R.id.player5_trains_left_text_view);

        chatMessages = chatPresenter.getMessages();
        newDestinationCardList = playerInfoPresenter.getNewDestinationCardStrings();
        currentDestinationCardList = playerInfoPresenter.getDestinationCardStrings();
        mGreenTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(1));
        mRedTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(2));
        mPinkTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(6));
        mYellowTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(3));
        mWhiteTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(7));
        mBlackTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(8));
        mWildTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(9));
        mBlueTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(4));
        mOrangeTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(5));
        for (int i = 0; i < playerInfoPresenter.getPlayers().size(); i++) {
            if (i == 0) {
                player1_username.setText("" + playerInfoPresenter.getPlayerByOrder(i).getUsername());
            }
            else if (i == 1) {
                player2_username.setText("" + playerInfoPresenter.getPlayerByOrder(i).getUsername());
            }
            else if (i == 2) {
                player3_username.setText("" + playerInfoPresenter.getPlayerByOrder(i).getUsername());
            }
            else if (i == 3) {
                player4_username.setText("" + playerInfoPresenter.getPlayerByOrder(i).getUsername());
            }
            else if (i == 4) {
                player5_username.setText("" + playerInfoPresenter.getPlayerByOrder(i).getUsername());
            }
        }
        blackTurn.setVisibility(View.VISIBLE);
        redTurn.setVisibility(View.INVISIBLE);
        blueTurn.setVisibility(View.INVISIBLE);
        greenTurn.setVisibility(View.INVISIBLE);
        yellowTurn.setVisibility(View.INVISIBLE);


        destinationCardDeck = findViewById(R.id.destination_card_deck);
        destinationCardDeck.setShadowLayer(5,0,0,Color.BLACK);
        destinationCardDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardDeckPresenter.drawDestinationCard() != null) {
                    Toast.makeText(GameBoardActivity.this, "Drew destination card successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(GameBoardActivity.this, "Cannot draw destination card!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        trainCardDeck = findViewById(R.id.train_card_deck);
        trainCardDeck.setShadowLayer(5,0,0,Color.BLACK);
        trainCardDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardDeckPresenter.drawTrainCard(0) != null) {
                    Toast.makeText(GameBoardActivity.this, "Successfully drew train card!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(GameBoardActivity.this, "Cannot draw train card!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cardOne = findViewById(R.id.card_index_zero);
        cardOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardDeckPresenter.drawTrainCard(1) != null) {
                    Toast.makeText(GameBoardActivity.this, "Successfully drew train card!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(GameBoardActivity.this, "Cannot draw train card!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cardTwo = findViewById(R.id.card_index_one);
        cardTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardDeckPresenter.drawTrainCard(2) != null) {
                    Toast.makeText(GameBoardActivity.this, "Successfully drew train card!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(GameBoardActivity.this, "Cannot draw train card!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cardThree = findViewById(R.id.card_index_two);
        cardThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardDeckPresenter.drawTrainCard(3) != null) {
                    Toast.makeText(GameBoardActivity.this, "Successfully drew train card!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(GameBoardActivity.this, "Cannot draw train card!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cardFour = findViewById(R.id.card_index_three);
        cardFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardDeckPresenter.drawTrainCard(4) != null) {
                    Toast.makeText(GameBoardActivity.this, "Successfully drew train card!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(GameBoardActivity.this, "Cannot draw train card!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cardFive = findViewById(R.id.card_index_four);
        cardFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardDeckPresenter.drawTrainCard(5) != null) {
                    Toast.makeText(GameBoardActivity.this, "Successfully drew train card!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(GameBoardActivity.this, "Cannot draw train card!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cardFive.setBackgroundResource((int) trainCardImages.get(cardDeckPresenter.getTrainCardAtPosition(5)));
        cardFour.setBackgroundResource((int) trainCardImages.get(cardDeckPresenter.getTrainCardAtPosition(4)));
        cardThree.setBackgroundResource((int) trainCardImages.get(cardDeckPresenter.getTrainCardAtPosition(3)));
        cardTwo.setBackgroundResource((int) trainCardImages.get(cardDeckPresenter.getTrainCardAtPosition(2)));
        cardOne.setBackgroundResource((int) trainCardImages.get(cardDeckPresenter.getTrainCardAtPosition(1)));

        mWhiteTrainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSelectedTicketColor() == TTR_Constants.getInstance().WHITE) {
                    Toast.makeText(GameBoardActivity.this, "This color is already selected as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
                else {
                    setSelectedTicketColor(TTR_Constants.getInstance().WHITE);
                    Toast.makeText(GameBoardActivity.this, "You have selected WHITE as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBlackTrainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSelectedTicketColor() == TTR_Constants.getInstance().BLACK) {
                    Toast.makeText(GameBoardActivity.this, "This color is already selected as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
                else {
                    setSelectedTicketColor(TTR_Constants.getInstance().BLACK);
                    Toast.makeText(GameBoardActivity.this, "You have selected BLACK as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBlueTrainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((getSelectedTicketColor()) == TTR_Constants.getInstance().BLUE) {
                    Toast.makeText(GameBoardActivity.this, "This color is already selected as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
                else {
                    setSelectedTicketColor(TTR_Constants.getInstance().BLUE);
                    Toast.makeText(GameBoardActivity.this, "You have selected BLUE as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mGreenTrainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSelectedTicketColor() == TTR_Constants.getInstance().GREEN) {
                    Toast.makeText(GameBoardActivity.this, "This color is already selected as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
                else {
                    setSelectedTicketColor(TTR_Constants.getInstance().GREEN);
                    Toast.makeText(GameBoardActivity.this, "You have selected GREEN as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRedTrainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSelectedTicketColor() ==TTR_Constants.getInstance().RED) {
                    Toast.makeText(GameBoardActivity.this, "This color is already selected as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
                else {
                    setSelectedTicketColor(TTR_Constants.getInstance().RED);
                    Toast.makeText(GameBoardActivity.this, "You have selected RED as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mYellowTrainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSelectedTicketColor() ==TTR_Constants.getInstance().YELLOW) {
                    Toast.makeText(GameBoardActivity.this, "This color is already selected as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
                else {
                    setSelectedTicketColor(TTR_Constants.getInstance().YELLOW);
                    Toast.makeText(GameBoardActivity.this, "You have selected YELLOW as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPinkTrainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSelectedTicketColor() ==TTR_Constants.getInstance().PURPLE) {
                    Toast.makeText(GameBoardActivity.this, "This color is already selected as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
                else {
                    setSelectedTicketColor(TTR_Constants.getInstance().PURPLE);
                    Toast.makeText(GameBoardActivity.this, "You have selected PURPLE as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mOrangeTrainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSelectedTicketColor() ==TTR_Constants.getInstance().ORANGE) {
                    Toast.makeText(GameBoardActivity.this, "This color is already selected as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
                else {
                    setSelectedTicketColor(TTR_Constants.getInstance().ORANGE);
                    Toast.makeText(GameBoardActivity.this, "You have selected ORANGE as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mWildTrainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSelectedTicketColor() ==TTR_Constants.getInstance().WILD) {
                    Toast.makeText(GameBoardActivity.this, "This color is already selected as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
                else {
                    setSelectedTicketColor(TTR_Constants.getInstance().WILD);
                    Toast.makeText(GameBoardActivity.this, "You have selected WILD as your designated purchasing color", Toast.LENGTH_SHORT).show();
                }
            }
        });

        destinationCardDeck.setText("" + cardDeckPresenter.getDestinationCardsLeft());
        trainCardDeck.setText("" + cardDeckPresenter.getTrainCardsLeft());
        gameBoard = findViewById(R.id.game_board_pic);
        new UpdateAsyncTask(this).execute();
    }

    public void popToast(String output, boolean longMessage){
        if (longMessage)    {
            Toast.makeText(GameBoardActivity.this, output, Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(GameBoardActivity.this, output, Toast.LENGTH_SHORT).show();
        }
    }

    private void initDestinationCardsRecyclerView() {
        newDestinationCardList = playerInfoPresenter.getNewDestinationCardStrings();
        currentDestinationCardList = playerInfoPresenter.getDestinationCardStrings();

        // Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.player_info_popup_window,null);

        // Initialize a new instance of popup window
        mPopupWindow = new PopupWindow(customView, 900,
                800, true);

        RecyclerView destinationCardsRecyclerView = mPopupWindow.getContentView().findViewById(R.id.recycler_view_destination_cards);
        newDestinationCardList = playerInfoPresenter.getNewDestinationCardStrings();
        currentDestinationCardList = playerInfoPresenter.getDestinationCardStrings();
        ArrayList<String> allCards = new ArrayList<>();
        allCards.addAll(newDestinationCardList);
        allCards.addAll(currentDestinationCardList);
        // currentDestinationCardList.addAll(newDestinationCardList);
        discardButtons = new ArrayList<>();
        for (String s : newDestinationCardList) {
            discardButtons.add(new Button(this));
        }
        lengthOfNewDestinationCards = newDestinationCardList.size();
        destinationCardsAdapter = new RecyclerViewAdapterDestinationCards(currentDestinationCardList, discardButtons, lengthOfNewDestinationCards, mPopupWindow.getContentView().getContext(), playerInfoPresenter);
        destinationCardsRecyclerView.setHasFixedSize(true);
        destinationCardsRecyclerView.setAdapter(destinationCardsAdapter);
        destinationCardsRecyclerView.setLayoutManager(new LinearLayoutManager(mPopupWindow.getContentView().getContext()));


        // Set an elevation value for popup window
        if(Build.VERSION.SDK_INT>=21)
            mPopupWindow.setElevation(10);

        activityLayout = findViewById(R.id.game_board_activity);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.chat_recycler_view);
        chatMessages = chatPresenter.getMessages();
        adapter = new RecyclerViewAdapterChat(chatMessages);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public Void updateChatList(ArrayList<ChatMessage> newChatMessages) {
        chatMessages = newChatMessages;
        return null;
    }

    public void change_color_nashville_littlerock_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_LIT_TO_NAS);
    }

    public void change_color_neworleans_houston_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_HOU_TO_ORI);
    }

    public void change_color_littlerock_neworleans_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_ORI_TO_LIT);
    }

    public void change_color_dallas_houston_g2(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_HOU_TO_DAL_2);

    }

    public void change_color_littlerock_dallas_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_DAL_TO_LIT);
    }

    public void change_color_oklahomacity_dallas_g2(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_DAL_TO_OKL_2);
    }

    public void change_color_oklahomacity_littlerock_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_OKL_TO_LIT);
    }

    public void change_color_saintlouis_nashville_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_SAI_TO_NAS);
    }

    public void change_color_saintlouis_littlerock_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_LIT_TO_SAI);
    }

    public void change_color_kansascity_saintlouis_g2(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_KAN_TO_SAI_2);
    }

    public void change_color_kansascity_saintlouis_g1(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_KAN_TO_SAI_1);
    }

    public void change_color_kansascity_oklahomacity_g2(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_OKL_TO_KAN_2);
    }

    public void change_color_omaha_kansascity_g2(View view) {//1
        routePresenter.purchase(TTR_Constants.getInstance().R_KAN_TO_OMA_2);
    }

    public void change_color_neworleans_miami_g1(View view) {//6
        routePresenter.purchase(TTR_Constants.getInstance().R_ORI_TO_MIA);
//        ServerProxy proxy = new ServerProxy();
//        ClientModel client = ClientModel.create();
//        proxy.purchaseRoute(client.getUser().getUsername(), client.getUser().getGame().getGameName(), TTR_Constants.getInstance().R_ORI_TO_MIA, 0);
    }

    public void change_color_charleston_miami_g1(View view) {//4
        routePresenter.purchase(TTR_Constants.getInstance().R_MIA_TO_CHA);
    }

    public void change_color_atlanta_charleston_g1(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_ATL_TO_CHA);
    }

    public void change_color_atlanta_miami_g1(View view) {//5
        routePresenter.purchase(TTR_Constants.getInstance().R_MIA_TO_ATL);

    }

    public void change_color_atlanta_neworleans_g2(View view) {//4
        routePresenter.purchase(TTR_Constants.getInstance().R_ORI_TO_ATL_2);

    }

    public void change_color_atlanta_neworleans_g1(View view) {//4
        routePresenter.purchase(TTR_Constants.getInstance().R_ORI_TO_ATL_1);
    }

    public void change_color_nashville_atlanta_g1(View view) {//1
        routePresenter.purchase(TTR_Constants.getInstance().R_ATL_TO_NAS);
    }

    public void change_color_raleigh_nashville_g1(View view) {//3
        routePresenter.purchase(TTR_Constants.getInstance().R_NAS_TO_RAL);
    }

    public void change_color_pittsburgh_raleigh_g1(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_RAL_TO_PIT);
    }

    public void change_color_pittsburgh_nashville_g1(View view) {//4
        routePresenter.purchase(TTR_Constants.getInstance().R_NAS_TO_PIT);
    }

    public void change_color_pittsburgh_saintlouis_g1(View view) {//5
        routePresenter.purchase(TTR_Constants.getInstance().R_SAI_TO_PIT);

    }

    public void change_color_pittsburgh_chicago_g2(View view) {//3
        routePresenter.purchase(TTR_Constants.getInstance().R_CHI_TO_PIT_1);
    }

    public void change_color_pittsburgh_chicago_g1(View view) {//3
        routePresenter.purchase(TTR_Constants.getInstance().R_CHI_TO_PIT_2);
    }

    public void change_color_chicago_saintlouis_g2(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_SAI_TO_CHI_1);
    }

    public void change_color_chicago_saintlouis_g1(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_SAI_TO_CHI_2);
    }

    public void change_color_chicago_omaha_g1(View view) {//4
        routePresenter.purchase(TTR_Constants.getInstance().R_OMA_TO_CHI);

    }

    public void change_color_duluth_chicago_g1(View view) {//3
        routePresenter.purchase(TTR_Constants.getInstance().R_DUL_TO_CHI);
    }

    public void change_color_duluth_omaha_g2(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_OMA_TO_DUL_2);
    }

    public void change_color_saulstmarie_duluth_g1(View view) {//3
        routePresenter.purchase(TTR_Constants.getInstance().R_DUL_TO_SSM);

    }

    public void change_color_winnipeg_saulstmarie_g1(View view) {//6
        routePresenter.purchase(TTR_Constants.getInstance().R_WIN_TO_SSM);
    }

    public void change_color_toronto_duluth_g1(View view) {//6
        routePresenter.purchase(TTR_Constants.getInstance().R_DUL_TO_TOR);
//        ServerProxy proxy = new ServerProxy();
//        ClientModel client = ClientModel.create();
//        proxy.purchaseRoute(client.getUser().getUsername(), client.getUser().getGame().getGameName(), TTR_Constants.getInstance().R_DUL_TO_TOR, 0);
    }

    public void change_color_toronto_pittsburgh_g1(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_PIT_TO_TOR);
    }

    public void change_color_toronto_chicago_g1(View view) {//4
        routePresenter.purchase(TTR_Constants.getInstance().R_CHI_TO_TOR);
    }

    public void change_color_saulstmarie_toronto_g1(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_SSM_TO_TOR);
    }

    public void change_color_montreal_saulstmarie_g1(View view) {//5
        routePresenter.purchase(TTR_Constants.getInstance().R_SSM_TO_MON);

    }

    public void change_color_montreal_toronto_g1(View view) {//3
        routePresenter.purchase(TTR_Constants.getInstance().R_TOR_TO_MON);
    }

    public void change_color_raleigh_atlanta_g2(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_ATL_TO_RAL_2);
    }

    public void change_color_raleigh_atlanta_g1(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_ATL_TO_RAL_1);
    }

    public void change_color_raleigh_charleston_g1(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_CHA_TO_RAL);
    }

    public void change_color_washington_raleigh_g2(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_RAL_TO_WAS_2);
    }

    public void change_color_washington_raleigh_g1(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_RAL_TO_WAS_1);
    }

    public void change_color_pittsburgh_washington_g1(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_WAS_TO_PIT);
    }

    public void change_color_newyork_washington_g2(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_WAS_TO_NYC_2);
    }

    public void change_color_newyork_washington_g1(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_WAS_TO_NYC_1);
    }

    public void change_color_newyork_pittsburgh_g2(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_PIT_TO_NYC_2);
    }

    public void change_color_newyork_pittsburgh_g1(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_PIT_TO_NYC_1);
    }

    public void change_color_boston_newyork_g2(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_NYC_TO_BOS_1);
    }

    public void change_color_boston_newyork_g1(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_NYC_TO_BOS_2);
    }

    public void change_color_montreal_boston_g2(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_MON_TO_BOS_2);
    }

    public void change_color_montreal_boston_g1(View view) {//2
        routePresenter.purchase(TTR_Constants.getInstance().R_MON_TO_BOS_1);
    }

    public void change_color_montreal_newyork_g1(View view) {//3
        routePresenter.purchase(TTR_Constants.getInstance().R_NYC_TO_MON);
    }

    public void change_color_portland_sanfransisco_g2(View v) {
        routePresenter.purchase(TTR_Constants.getInstance().R_POR_TO_SAN_2);
    }

    public void change_color_vancouver_seattle_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_VAN_TO_SEA_1);
    }

    public void change_color_vancouver_seattle_g2(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_VAN_TO_SEA_2);
    }

    public void change_color_seattle_portland_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_SEA_TO_POR_1);
    }

    public void change_color_seattle_portland_g2(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_SEA_TO_POR_2);
    }

    public void change_color_portland_sanfransisco_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_POR_TO_SAN_1);
    }

    public void change_color_sanfransisco_losangeles_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_SAN_TO_LA_1);
    }

    public void change_color_sanfransisco_losangeles_g2(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_SAN_TO_LA_2);
    }

    public void change_color_losangeles_elpaso_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_LA_TO_EL);
    }

    public void change_color_vancouver_calgary_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_VAN_TO_CAL);
    }

    public void change_color_seattle_calgary_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_SEA_TO_CAL);
    }

    public void change_color_seattle_helena_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_SEA_TO_HEL);
    }

    public void change_color_calgary_winnipeg_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_CAL_TO_WIN);
    }

    public void change_color_calgary_helena_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_CAL_TO_HEL);
    }

    public void change_color_helena_winnipeg_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_HEL_TO_WIN);
    }

    public void change_color_portland_saltlakecity_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_POR_TO_SLC);
    }

    public void change_color_sanfransisco_saltlakecity_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_SAN_TO_SLC_1);
    }

    public void change_color_sanfransisco_saltlakecity_g2(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_SAN_TO_SLC_2);
    }

    public void change_color_losangeles_lasvegas_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_LA_TO_LAS);
    }

    public void change_color_lasvegas_saltlakecity_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_LAS_TO_SLC);
    }

    public void change_color_losangeles_phoenix_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_LA_TO_PHO);
    }

    public void change_color_phoenix_elpaso_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_PHO_TO_EL);
    }

    public void change_color_phoenix_santafe_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_PHO_TO_FE);
    }

    public void change_color_phoenix_denver_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_PHO_TO_DEN);
    }

    public void change_color_saltlakecity_denver_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_SLC_TO_DEN_1);
    }

    public void change_color_saltlakecity_denver_g2(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_SLC_TO_DEN_2);
    }

    public void change_color_saltlakecity_helena_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_SLC_TO_HEL);
    }

    public void change_color_helena_denver_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_DEN_TO_HEL);
    }

    public void change_color_helena_duluth_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_HEL_TO_DUL);
    }

    public void change_color_helena_omaha_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_HEL_TO_OMA);
    }

    public void change_color_omaha_duluth_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_OMA_TO_DUL_1);
    }

    public void change_color_denver_omaha_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_DEN_TO_OMA);
    }

    public void change_color_denver_kansascity_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_DEN_TO_KAN_1);
    }

    public void change_color_denver_kansascity_g2(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_DEN_TO_KAN_2);
    }

    public void change_color_denver_oklahomacity_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_DEN_TO_OKL);
    }

    public void change_color_denver_santafe_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_FE_TO_DEN);
    }

    public void change_color_santafe_elpaso_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_EL_TO_FE);
    }

    public void change_color_santafe_oklahomacity_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_FE_TO_OKL);
    }

    public void change_color_elpaso_oklahomacity_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_EL_TO_OKL);
    }

    public void change_color_elpaso_dallas_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_EL_TO_DAL);
    }

    public void change_color_elpaso_houston_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_EL_TO_HOU);
//        ServerProxy proxy = new ServerProxy();
//        ClientModel client = ClientModel.create();
//        proxy.purchaseRoute(client.getUser().getUsername(), client.getUser().getGame().getGameName(), TTR_Constants.getInstance().R_EL_TO_HOU, 0);
    }

    public void change_color_winnipeg_duluth_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_WIN_TO_DUL);
    }

    public void change_color_omaha_kansascity_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_KAN_TO_OMA_1);
    }

    public void change_color_oklahomacity_kansascity_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_OKL_TO_KAN_1);
    }

    public void change_color_oklahomacity_dallas_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_DAL_TO_OKL_1);
//        ServerProxy proxy = new ServerProxy();
//        ClientModel client = ClientModel.create();
//        proxy.purchaseRoute(client.getUser().getUsername(), client.getUser().getGame().getGameName(), TTR_Constants.getInstance().R_DAL_TO_OKL_1, 0);
    }

    public void change_color_dallas_houston_g1(View view) {
        routePresenter.purchase(TTR_Constants.getInstance().R_HOU_TO_DAL_1);
    }

    public void draw(Set<Integer> ids, Integer playerColor) {
        for (Integer id: ids) {
            View view = findViewById(id);
            int color = (int)playerColorValues.get(playerColor);
            System.out.println("Color: " + color);
            view.setBackgroundResource(color);
            view.setAlpha(1);
//            findViewById(id).setBackgroundResource((int)playerColorValues.get(playerColor));
//            findViewById(id).setAlpha(1);
        }
    }

    public void initiateGameOver() {
        Intent intent = new Intent(GameBoardActivity.this, GameOverActivity.class);
        startActivity(intent);
    }

    public Integer getSelectedTicketColor() {
        return selectedTicketColor;
    }

    public void setSelectedTicketColor(Integer selectedTicketColor) {
        this.selectedTicketColor = selectedTicketColor;
    }

    public Integer getPurchaseNumberWilds() {
        return  playersHandPresenter.getTrainCardAmount(9);
    }


    public class UpdateAsyncTask extends AsyncTask<Void, Void, Void> {
        private GameBoardActivity activity;

        //Empty constructor
        public UpdateAsyncTask(GameBoardActivity activity) {
            this.activity = activity;
        }

        /**
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         */

        @Override
        protected Void doInBackground(Void... result) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter.setListOfMessages(chatMessages);
            adapter.notifyDataSetChanged();
            chatMessages = chatPresenter.getMessages();
            ArrayList<Player> players = playerInfoPresenter.getPlayers();

            newDestinationCardList = playerInfoPresenter.getNewDestinationCardStrings();
            currentDestinationCardList = playerInfoPresenter.getDestinationCardStrings();
            ArrayList<String> allCards = new ArrayList<>();
            allCards.addAll(newDestinationCardList);
            allCards.addAll(currentDestinationCardList);
            discardButtons.clear();
            for (String s : allCards) {
                discardButtons.add(new Button(getApplicationContext()));
            }
            lengthOfNewDestinationCards = newDestinationCardList.size();
            destinationCardsAdapter.setListOfDestinationCards(allCards, discardButtons, lengthOfNewDestinationCards);
            destinationCardsAdapter.notifyDataSetChanged();

            // Re-enable the done button if there are cards to discard
            if (playerInfoPresenter.getNewDestinationCardStrings().size() > 0) {
                doneButton.getBackground().setColorFilter(null);
                doneButton.setAlpha(1);
                doneButton.setEnabled(true);
            }
            else {
                doneButton.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
                doneButton.setAlpha(.5f);
                doneButton.setEnabled(false);
            }

            Integer num = 0;
            for (Player player: players) {
                if (player.getDoneWithTurns()) {
                    num++;
                    continue;
                }
                else {
                    break;
                }
            }
            if (num == players.size()) {
                initiateGameOver();
            }


            mGreenTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(1));
            mRedTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(2));
            mPinkTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(6));
            mYellowTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(3));
            mWhiteTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(7));
            mBlackTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(8));
            mWildTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(9));
            mBlueTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(4));
            mOrangeTrainCard.setText("" + playersHandPresenter.getTrainCardAmount(5));
            destinationCardDeck.setText("" + cardDeckPresenter.getDestinationCardsLeft());
            trainCardDeck.setText("" + cardDeckPresenter.getTrainCardsLeft());
            cardFive.setBackgroundResource((int) trainCardImages.get(cardDeckPresenter.getTrainCardAtPosition(5)));
            cardFour.setBackgroundResource((int) trainCardImages.get(cardDeckPresenter.getTrainCardAtPosition(4)));
            cardThree.setBackgroundResource((int) trainCardImages.get(cardDeckPresenter.getTrainCardAtPosition(3)));
            cardTwo.setBackgroundResource((int) trainCardImages.get(cardDeckPresenter.getTrainCardAtPosition(2)));
            cardOne.setBackgroundResource((int) trainCardImages.get(cardDeckPresenter.getTrainCardAtPosition(1)));
            TTR_Constants constants = TTR_Constants.getInstance();

            if (playerInfoPresenter.getCurrentTurn().getPlayerColor().equals(constants.BLACK_PLAYER)) {
                blackTurn.setVisibility(View.VISIBLE);
                redTurn.setVisibility(View.INVISIBLE);
                blueTurn.setVisibility(View.INVISIBLE);
                greenTurn.setVisibility(View.INVISIBLE);
                yellowTurn.setVisibility(View.INVISIBLE);
            }
            else if (playerInfoPresenter.getCurrentTurn().getPlayerColor().equals(constants.RED_PLAYER)) {
                redTurn.setVisibility(View.VISIBLE);
                blackTurn.setVisibility(View.INVISIBLE);
                blueTurn.setVisibility(View.INVISIBLE);
                greenTurn.setVisibility(View.INVISIBLE);
                yellowTurn.setVisibility(View.INVISIBLE);
            }
            else if (playerInfoPresenter.getCurrentTurn().getPlayerColor().equals(constants.BLUE_PLAYER)) {
                blueTurn.setVisibility(View.VISIBLE);
                blackTurn.setVisibility(View.INVISIBLE);
                redTurn.setVisibility(View.INVISIBLE);
                greenTurn.setVisibility(View.INVISIBLE);
                yellowTurn.setVisibility(View.INVISIBLE);
            }
            else if (playerInfoPresenter.getCurrentTurn().getPlayerColor().equals(constants.GREEN_PLAYER)) {
                greenTurn.setVisibility(View.VISIBLE);
                blackTurn.setVisibility(View.INVISIBLE);
                redTurn.setVisibility(View.INVISIBLE);
                blueTurn.setVisibility(View.INVISIBLE);
                yellowTurn.setVisibility(View.INVISIBLE);
            }
            else if (playerInfoPresenter.getCurrentTurn().getPlayerColor().equals(constants.YELLOW_PLAYER)) {
                yellowTurn.setVisibility(View.VISIBLE);
                blackTurn.setVisibility(View.INVISIBLE);
                redTurn.setVisibility(View.INVISIBLE);
                blueTurn.setVisibility(View.INVISIBLE);
                greenTurn.setVisibility(View.INVISIBLE);
            }

            for (Player player : players) {
                Set<Route> routes = playerInfoPresenter.getPurchasedRoutesFromPlayer(player.getPlayerColor());
                System.out.println("player: " + player.getUsername() + "\nroutes: " + player.getRoutesOwned());
                for (Route route: routes) {
                    if (constants.R_DAL_TO_OKL_1.equals(route)) {
                        activity.findViewById(R.id.oklahomacity_dallas_g1b1).setBackgroundResource((int)playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.oklahomacity_dallas_g1b1).setAlpha(1);
                        activity.findViewById(R.id.oklahomacity_dallas_g1b2).setBackgroundResource((int)playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.oklahomacity_dallas_g1b2).setAlpha(1);

                    }
                    else if (constants.R_ORI_TO_MIA.equals(route)) {
                        activity.findViewById(R.id.neworleans_miami_g1b1).setBackgroundResource((int) playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.neworleans_miami_g1b1).setAlpha(1);
                        activity.findViewById(R.id.neworleans_miami_g1b2).setBackgroundResource((int) playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.neworleans_miami_g1b2).setAlpha(1);
                        activity.findViewById(R.id.neworleans_miami_g1b3).setBackgroundResource((int) playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.neworleans_miami_g1b3).setAlpha(1);
                        activity.findViewById(R.id.neworleans_miami_g1b4).setBackgroundResource((int) playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.neworleans_miami_g1b4).setAlpha(1);
                        activity.findViewById(R.id.neworleans_miami_g1b5).setBackgroundResource((int) playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.neworleans_miami_g1b5).setAlpha(1);
                        activity.findViewById(R.id.neworleans_miami_g1b6).setBackgroundResource((int) playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.neworleans_miami_g1b6).setAlpha(1);
                    }
                    else if (constants.R_DUL_TO_TOR.equals(route)) {
                        activity.findViewById(R.id.toronto_duluth_g1b1).setBackgroundResource((int)playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.toronto_duluth_g1b1).setAlpha(1);
                        activity.findViewById(R.id.toronto_duluth_g1b2).setBackgroundResource((int)playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.toronto_duluth_g1b2).setAlpha(1);
                        activity.findViewById(R.id.toronto_duluth_g1b3).setBackgroundResource((int)playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.toronto_duluth_g1b3).setAlpha(1);
                        activity.findViewById(R.id.toronto_duluth_g1b4).setBackgroundResource((int)playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.toronto_duluth_g1b4).setAlpha(1);
                        activity.findViewById(R.id.toronto_duluth_g1b5).setBackgroundResource((int)playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.toronto_duluth_g1b5).setAlpha(1);
                        activity.findViewById(R.id.toronto_duluth_g1b6).setBackgroundResource((int)playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.toronto_duluth_g1b6).setAlpha(1);
                    }
                    else if (constants.R_EL_TO_HOU.equals(route)) {
                        activity.findViewById(R.id.elpaso_houston_g1b1).setBackgroundResource((int)playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.elpaso_houston_g1b1).setAlpha(1);
                        activity.findViewById(R.id.elpaso_houston_g1b2).setBackgroundResource((int)playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.elpaso_houston_g1b2).setAlpha(1);
                        activity.findViewById(R.id.elpaso_houston_g1b3).setBackgroundResource((int)playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.elpaso_houston_g1b3).setAlpha(1);
                        activity.findViewById(R.id.elpaso_houston_g1b4).setBackgroundResource((int)playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.elpaso_houston_g1b4).setAlpha(1);
                        activity.findViewById(R.id.elpaso_houston_g1b5).setBackgroundResource((int)playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.elpaso_houston_g1b5).setAlpha(1);
                        activity.findViewById(R.id.elpaso_houston_g1b6).setBackgroundResource((int)playerColorValues.get(player.getPlayerColor()));
                        activity.findViewById(R.id.elpaso_houston_g1b6).setAlpha(1);
                    }
                }
            }




            for (int i = 0; i < playerInfoPresenter.getNumOfPlayers(); i++) {
                Player player = playerInfoPresenter.getPlayerByOrder(i);
                if (player == null) {
                    break;
                }
                int destCardSize;
                int ticketSize;
                if (player.getDestinationCardHand() == null) {
                    destCardSize = 0;
                }
                else {
                    destCardSize = player.getDestinationCardHand().size();
                }
                if (player.getTickets() == null) {
                    ticketSize = 0;
                }
                else {
                    ticketSize = player.countTickets();
                }
                if (i == 0) {
                    one_destinationCards.setText("Destination Cards: " + destCardSize);
                    one_score.setText("Score: " + player.getScore());
                    one_trainCards.setText("Train Cards: " + ticketSize);
                    one_trainsLeft.setText("Trains Left: " + player.getTrainsRemaining());
                }
                else if (i == 1) {
                    two_destinationCards.setText("Destination Cards: " + destCardSize);
                    two_score.setText("Score: " + player.getScore());
                    two_trainCards.setText("Train Cards: " + ticketSize);
                    two_trainsLeft.setText("Trains Left: " + player.getTrainsRemaining());
                }
                else if (i == 2) {
                    three_destinationCards.setText("Destination Cards: " + destCardSize);
                    three_score.setText("Score: " + player.getScore());
                    three_trainCards.setText("Train Cards: " + ticketSize);
                    three_trainsLeft.setText("Trains Left: " + player.getTrainsRemaining());
                }
                else if (i == 3) {
                    four_destinationCards.setText("Destination Cards: " + destCardSize);
                    four_score.setText("Score: " + player.getScore());
                    four_trainCards.setText("Train Cards: " + ticketSize);
                    four_trainsLeft.setText("Trains Left: " + player.getTrainsRemaining());
                }
                else if (i == 4) {
                    five_destinationCards.setText("Destination Cards: " + destCardSize);
                    five_score.setText("Score: " + player.getScore());
                    five_trainCards.setText("Train Cards: " + ticketSize);
                    five_trainsLeft.setText("Trains Left: " + player.getTrainsRemaining());
                }
            }

        }
    }

    public class updateRouteAsyncTask extends AsyncTask<Void, Void, Map<Integer, Set<Integer>>> {
        GameBoardActivity activity;
        Map<Route, Boolean> hasBeenDrawn;

        public updateRouteAsyncTask(GameBoardActivity activity, Map<Route, Boolean> hasBeenDrawn) {
            this.activity = activity;
            this.hasBeenDrawn = hasBeenDrawn;
        }

        @Override
        protected Map<Integer, Set<Integer>> doInBackground(Void... voids) {
            System.out.println("Updating routes");
            ClientModel model = ClientModel.create();
            List<Player> players = model.getUser().getGameJoined().getPlayers();
            Map<Integer, Set<Integer>> colorToIds = new HashMap<>();
            if (players != null) {
                for (Player p: players) {
                    for (Route r: p.getRoutesOwned()) {
                        Boolean rDrawn = hasBeenDrawn.get(r);
                        if (rDrawn == null) {
                            System.out.println("Not finding r in hasBeenDrawn...");
                        }
                        else if (!rDrawn) {
                            Set<Integer> ids = TTR_Constants.routeToIdMap.get(r);
//                            activity.draw(ids, p.getPlayerColor());
                            if (colorToIds.get(p.getPlayerColor()) == null) {
                                colorToIds.put(p.getPlayerColor(), new HashSet<Integer>());
                            }
                            colorToIds.get(p.getPlayerColor()).addAll(ids);
                            hasBeenDrawn.put(r, true);
                        }
                    }
                }
            }

            return colorToIds;
        }

        @Override
        protected void onPostExecute(Map<Integer, Set<Integer>> colorToIds) {
            for (Integer color: colorToIds.keySet()) {
                draw(colorToIds.get(color), color);
            }
        }
    }
}
