package com.example.cs340.tickettoride;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import models.data.Result;
import view.presenter.RegisterPresenter;

public class RegisterViewActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText repeatedPassword;
    private Button registerButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repeatedPassword = findViewById(R.id.password_checker);
        registerButton = findViewById(R.id.register_button);
        cancelButton = findViewById(R.id.cancel_button);


        // Login button is pushed
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegisterPresenter registerPresenter = new RegisterPresenter();
                Result result = registerPresenter.registerUser(username.getText().toString(), password.getText().toString(), repeatedPassword.getText().toString());
                if (result.isSuccessful()){
                    Intent intent = new Intent(RegisterViewActivity.this, LobbyViewActivity.class);
                    startActivity(intent);
                    Toast.makeText(RegisterViewActivity.this, "Successfully registered " + username.getText().toString(), Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(RegisterViewActivity.this, result.getErrorMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

        // Register button is pushed
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterViewActivity.this, LoginViewActivity.class);
                startActivity(intent);
            }
        });
    }
}
