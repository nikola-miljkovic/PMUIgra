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

        mMaxFPS = sharedPref.getInt(context.getString(R.string.pref_max_fps), mMaxFPSDef);
        mCoefficientOfFriction = sharedPref.getFloat(context.getString(R.string.pref_cof), mCoefficientOfFrictionDef);
        mCollisionForce = sharedPref.getFloat(context.getString(R.string.pref_collision_force), mCollisionForceDef);
        mGameSpeed = sharedPref.getFloat(context.getString(R.string.pref_game_speed), mGameSpeedDef);
        mBoostX = sharedPref.getFloat(context.getString(R.string.pref_boost_x), mBoostXDef);
        mBoostY = sharedPref.getFloat(context.getString(R.string.pref_boost_y), mBoostYDef);
    }

    public void store(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(context.getString(R.string.pref_max_fps), mMaxFPS);
        editor.putFloat(context.getString(R.string.pref_cof), mCoefficientOfFriction);
        editor.putFloat(context.getString(R.string.pref_collision_force), mCollisionForce);
        editor.putFloat(context.getString(R.string.pref_game_speed), mGameSpeed);
        editor.putFloat(context.getString(R.string.pref_boost_x), mBoostX);
        editor.putFloat(context.getString(R.string.pref_boost_y), mBoostY);

        editor.apply();
    }

    // TODO: restoreDefaults
    public void restoreDefaults(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(context.getString(R.string.pref_max_fps), mMaxFPSDef);
        editor.putFloat(context.getString(R.string.pref_cof), mCoefficientOfFrictionDef);
        editor.putFloat(context.getString(R.string.pref_collision_force), mCollisionForceDef);
        editor.putFloat(context.getString(R.string.pref_game_speed), mGameSpeedDef);
        editor.putFloat(context.getString(R.string.pref_boost_x), mBoostXDef);
        editor.putFloat(context.getString(R.string.pref_boost_y), mBoostYDef);

        editor.apply();
    }

    private static int mMaxFPSDef = 60;
    private static float mCoefficientOfFrictionDef = 0.25f;
    private static float mCollisionForceDef = 0.85f;
    private static float mGameSpeedDef = 0.60f;
    private static float mBoostXDef = 1.6f;
    private static float mBoostYDef = 1.1f;

    int mMaxFPS;
    float mCoefficientOfFriction;
    float mCollisionForce;
    float mGameSpeed;
    float mBoostX;
    float mBoostY;

    public int getRenderSpeed() {
        return 1000 / mMaxFPS;
    }

    public int getMaxFPS() {
        return mMaxFPS;
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
