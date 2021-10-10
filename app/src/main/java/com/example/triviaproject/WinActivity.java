package com.example.triviaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class WinActivity extends AppCompatActivity {
    ImageView view;
    Animation animationSmallToBig;
    Button btnReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        view = findViewById(R.id.win_image);
        btnReturn = findViewById(R.id.btn_return);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WinActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        animationSmallToBig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        view.startAnimation(animationSmallToBig);
    }
}