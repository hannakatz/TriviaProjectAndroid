package com.example.triviaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button musicPlayerBtn;
    private String ifMusicPlay;
    private Player player;
    private Gson gson = new Gson();
    private Button btnResetQuestion;
    final String TABLE_NAME = "Trivia_Questions";
    private SQLiteDatabase database;

    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        btnResetQuestion = findViewById(R.id.btn_reset_questions);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");

        SharedPreferences credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
        String json = credentials.getString("Player","");
        player = gson.fromJson(json,Player.class);

        musicPlayerBtn = (Button) findViewById(R.id.btn_sound);
        ifMusicPlay = player.musicPlay;

        if (ifMusicPlay.equals("false")) {
            musicPlayerBtn.setBackgroundResource(R.drawable.ic_sound_off);
        } else {
            musicPlayerBtn.setBackgroundResource(R.drawable.ic_sound_play);
        }

        musicPlayerBtn.setOnClickListener(this);

        btnResetQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetQuestionsDB();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == musicPlayerBtn) {

            SharedPreferences credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = credentials.edit();

            ifMusicPlay = player.musicPlay;

            if (ifMusicPlay.equals("false")) {
                player.musicPlay = "true";
                musicPlayerBtn.setBackgroundResource(R.drawable.ic_sound_play);
                startService(new Intent(this, MusicService.class));

            } else {
                player.musicPlay = "false";
                musicPlayerBtn.setBackgroundResource(R.drawable.ic_sound_off);
                stopService(new Intent(this, MusicService.class));
            }
            String json = gson.toJson(player);
            editor.putString("Player",json);
            editor.commit();
        }
    }
    //Reset all questions that were asked, so they could be asked again.
    private void ResetQuestionsDB() {
        database = openOrCreateDatabase("TriviaQ", MODE_PRIVATE,null);
        ContentValues contentVal = new ContentValues();
        contentVal.put("asked", "n");
        database.update(TABLE_NAME,contentVal,null , null);
    }
}





