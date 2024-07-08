package com.example.finalproject;

public class GlobalLeaderboardData {
    private int rank;
    private String firstName;
    private String lastName;
    private double finalScore;

    public GlobalLeaderboardData(int rank, String firstName, String lastName, double finalScore) {
        this.rank = rank;
        this.firstName = firstName;
        this.lastName = lastName;
        this.finalScore = finalScore;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }
}
