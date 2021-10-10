package com.example.triviaproject;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private TextView username, textScore;
    private Button btnStartGame;
    private ImageView profileImage;
    private Button btnSettings;
    private String ifMusicPlay;
    private Player player;
    private ImageButton imageButtonScores;


    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.name_display);
        btnStartGame = findViewById(R.id.btn_start_game);
        profileImage = findViewById(R.id.profile_image);
        btnSettings = findViewById(R.id.btn_settings);
        imageButtonScores = findViewById(R.id.score_table);
        textScore = findViewById(R.id.show_score);

        SharedPreferences credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = credentials.getString("Player","");
        player = gson.fromJson(json,Player.class);

        String highScoreString = Integer.toString(player.getHighScore());
        textScore.setText(highScoreString);

        username.setText(player.userName);
        profileImage.setImageBitmap(decodeBase64(player.image));
        ifMusicPlay = player.musicPlay;

        if(ifMusicPlay.equals("true")){
            startService(new Intent(this, MusicService.class));
        }

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameTypeActivity.class);
                startActivity(intent);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        imageButtonScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScoresPlayerActivity.class);
                startActivity(intent);
            }
        });
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopService(new Intent(this, MusicService.class));
    }
}

