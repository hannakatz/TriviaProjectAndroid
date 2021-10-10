package com.example.triviaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameTypeActivity extends AppCompatActivity {
    Button allCategoriesMode, categoryMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_type);
        allCategoriesMode = findViewById(R.id.button_all_categories);
        categoryMode = findViewById(R.id.button_category);

        allCategoriesMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameTypeActivity.this, GameAllCategoryActivity.class);
                startActivity(intent);
            }
        });

        categoryMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameTypeActivity.this, GameCategoryActivity.class);
                startActivity(intent);
            }
        });
    }
}