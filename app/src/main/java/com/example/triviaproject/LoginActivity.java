package com.example.triviaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    private EditText edUsername;
    private EditText edPassword;
    private Button btnLogin;
    private Button btnSingUp;
    private Player player;

    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edUsername = findViewById(R.id.ed_username);
        edPassword = findViewById(R.id.ed_password);
        btnLogin = findViewById(R.id.btn_login);
        btnSingUp = findViewById(R.id.btn_signup);


        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = credentials.getString("Player","");
                player = gson.fromJson(json,Player.class);

                String strUsername = player.userName;
                String strPassword = player.password;

                String username_from_ed = edUsername.getText().toString();
                String password_from_ed = edPassword.getText().toString();

                if(strUsername != null && username_from_ed != null && strUsername.equalsIgnoreCase(username_from_ed)) {
                    if (strPassword != null && password_from_ed != null && strPassword.equalsIgnoreCase(password_from_ed)) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
        });
    }
}

