package com.mndev.pmuigra.controller;

public class PreferenceController {
    private static PreferenceController ourInstance = new PreferenceController();

    public static PreferenceController getInstance() {
        return ourInstance;
    }

    private PreferenceController() {
    }

    public int getRenderSpeed() {
        return 60;
    }

    public float getCoefficientOfFriction() {
        return 0.25f;
    }

    public float getCollisionForce() {
        return 0.35f;
    }
}
