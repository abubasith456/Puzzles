package com.example.puzzle.model;

public class Score {

    String userId, playedTime, score, playedDate;

    public Score() {

    }

    public Score(String userId, String playedTime, String score, String playedDate) {
        this.userId = userId;
        this.playedTime = playedTime;
        this.score = score;
        this.playedDate = playedDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlayedTime() {
        return playedTime;
    }

    public void setPlayedTime(String playedTime) {
        this.playedTime = playedTime;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPlayedDate() {
        return playedDate;
    }

    public void setPlayedDate(String playedDate) {
        this.playedDate = playedDate;
    }
}
