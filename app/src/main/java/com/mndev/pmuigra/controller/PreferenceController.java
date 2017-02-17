package com.mndev.pmuigra.controller;

public class PreferenceController {
    private static PreferenceController ourInstance = new PreferenceController();

    public static PreferenceController getInstance() {
        return ourInstance;
    }

    private PreferenceController() {
    }

    public int getRenderSpeed() {
        return 1000 / 60;
    }

    public float getCoefficientOfFriction() {
        return 0.25f;
    }

    public float getCollisionForce() {
        return 0.85f;
    }

    public float getGameSpeed() {
        return 0.60f;
    }

    public float getBoostX() {
        return 1.6f;
    }

    public float getBoostY() {
        return 1.1f;
    }
}
