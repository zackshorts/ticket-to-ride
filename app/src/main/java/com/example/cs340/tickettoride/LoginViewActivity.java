package com.example.cs340.tickettoride;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import client.ClientModel;
import models.data.Result;
import view.activityInterface.ILoginView;
import view.activityInterface.IRegisterView;
import view.presenter.LoginPresenter;

public class LoginViewActivity extends AppCompatActivity implements ILoginView, IRegisterView {

    private EditText username, password;
    private Button registerButton, loginButton, startDemo;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        username = findViewById(R.id.username);
        password = findViewById(R.id.game_name);
        registerButton = findViewById(R.id.register_button);
        loginButton = findViewById(R.id.create_game);
        forgotPassword = findViewById(R.id.forgot_password);
        startDemo = findViewById(R.id.startDemo);
        final LoginPresenter loginPresenter = new LoginPresenter();


        loginPresenter.onCreate();

        startDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginViewActivity.this, "Demo Initializing \n Players initialized at Zero Points, 48 trains, Color Set\n Game Initialized: Starting Player Set, Game Decks Filled\n Initial Actions: Players Handed 3 Dest Cards and 4 Tickets\n", Toast.LENGTH_SHORT).show();
            }
        });


        // Login button is pushed
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LoginPresenter loginPresenter = new LoginPresenter();

                Result result = loginPresenter.loginUser(username.getText().toString(), password.getText().toString());
                if (result.isSuccessful()){
                    Intent intent = new Intent(LoginViewActivity.this, LobbyViewActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginViewActivity.this, "Successfully logged in " + username.getText().toString(), Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(LoginViewActivity.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Register button is pushed
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginViewActivity.this, RegisterViewActivity.class);
                startActivity(intent);
            }
        });

        //  Initialize the 'Forgot Password?' onClickListener
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword.setText("Sorry bud, you should have written it down");
            }
        });

    }


    @Override
    public void updateServerStatus(String status) {

    }

    @Override
    public void sendResponse(String message) {

    }

    @Override
    public void notifyRegisterStarted() {

    }

    @Override
    public void notifyLoginStarted() {

    }
}
