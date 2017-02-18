package com.mndev.pmuigra.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mndev.pmuigra.R;

public class PreferenceController {
    private static PreferenceController ourInstance = new PreferenceController();

    public static PreferenceController getInstance() {
        return ourInstance;
    }

    private PreferenceController() {
    }

    public void load(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file), Context.MODE_PRIVATE);
    }

    int mMaxFPS = 60;
    float mCoefficientOfFriction = 0.25f;
    float mCollisionForce = 0.85f;
    float mGameSpeed = 0.60f;
    float mBoostX = 1.6f;
    float mBoostY = 1.1f;

    public int getRenderSpeed() {
        return 1000 / mMaxFPS;
    }

    public void setMaxFPS(int maxFPS) {
        this.mMaxFPS = maxFPS;
    }

    public float getCoefficientOfFriction() {
        return mCoefficientOfFriction;
    }

    public void setCoefficientOfFriction(float mCoefficientOfFriction) {
        this.mCoefficientOfFriction = mCoefficientOfFriction;
    }

    public float getCollisionForce() {
        return mCollisionForce;
    }

    public void setCollisionForce(float mCollisionForce) {
        this.mCollisionForce = mCollisionForce;
    }

    public float getGameSpeed() {
        return mGameSpeed;
    }

    public void setGameSpeed(float mGameSpeed) {
        this.mGameSpeed = mGameSpeed;
    }

    public float getBoostX() {
        return mBoostX;
    }

    public void setBoostX(float mBoostX) {
        this.mBoostX = mBoostX;
    }

    public float getBoostY() {
        return mBoostY;
    }

    public void setBoostY(float mBoostY) {
        this.mBoostY = mBoostY;
    }
}
