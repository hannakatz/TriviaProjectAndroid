package com.example.triviaproject;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Player {
    String userName;
    String password;
    String image;
    String musicPlay;
    ArrayList<PlayerScore> playerScores;
    int highScore;

    public Player(String name, String setPassword, String setImage, String setMusicPlay){
        this.userName = name;
        this.password = setPassword;
        this.image = setImage;
        this.musicPlay = setMusicPlay;
        playerScores = new ArrayList<PlayerScore>();
        highScore = 0;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMusicPlay() {
        return musicPlay;
    }

    public void setMusicPlay(String password) {
        this.musicPlay = password;
    }

    public ArrayList<PlayerScore> getPlayerScores() {
        return playerScores;
    }

    public void setPlayerScores(ArrayList<PlayerScore> playerScores) {
        this.playerScores = playerScores;
    }

    public int getHighScore() {
        return highScore;
    }


    public void setHighScore(int highScore) {
        if(highScore > this.highScore){
            this.highScore = highScore;
        }

    }
}
