package com.example.triviaproject;

import androidx.annotation.NonNull;

public class PlayerScore {
    private String gameName;
    private int score;
    private String name;

    public PlayerScore(int score, String gameName, String name){
        this.score = score;
        this.gameName = gameName;
        this.name = name;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @NonNull
    @Override
    public String toString() {
        return name + ":\nGame : " + gameName + "\n" + "The Score : " + score;
    }
}
