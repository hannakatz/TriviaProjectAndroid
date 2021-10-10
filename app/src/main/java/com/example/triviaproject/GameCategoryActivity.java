package com.example.triviaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class GameCategoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String[] items;
    Button btnSelect;
    String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_category);

        btnSelect = findViewById(R.id.startgamebtn);

        Spinner dropdown = findViewById(R.id.spinner);

        items = new String[]{"Sports & Leisure", "General", "Food & Drink", "Toys & Games", " Geography", " History & Holidays", "Music", "Art & Literature"
        , "Science & Nature", "People & Places", " Language", "Religion & Mythology", "Entertainment", "Technology & Video Games","Tech & Video Games",
        "Mathematics","Mathematics & Geometry"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, items);

        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(this);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameCategoryActivity.this, GameByCategoryActivity.class);
                intent.putExtra("Category",category);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = items[i] ;
    }
}
