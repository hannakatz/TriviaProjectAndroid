package com.example.triviaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

public class ScoresPlayerActivity extends AppCompatActivity {
    private ListView listView;
    private Player player;

    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores_player);
        listView = findViewById(R.id.listView);

        SharedPreferences credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = credentials.getString("Player","");
        player = gson.fromJson(json,Player.class);

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.activity_listview, player.playerScores);

        listView.setAdapter(adapter);
    }
}