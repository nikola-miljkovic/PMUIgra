package com.mndev.pmuigra.model;

public class StatisticsEntry {
    private String polygonName;
    private String username;
    private float score;

    public StatisticsEntry(String polygonName, String username, float score) {
        this.polygonName = polygonName;
        this.username = username;
        this.score = score;
    }

    public String getPolygonName() {
        return polygonName;
    }

    public void setPolygonName(String polygonName) {
        this.polygonName = polygonName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
