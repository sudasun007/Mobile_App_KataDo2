package com.example.finalproject;

public class LeaderboardData {
    private String videoname;
    private double score;

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }



    public LeaderboardData(String videoname, double score) {
        this.videoname = videoname;
        this.score = score;
    }


}
