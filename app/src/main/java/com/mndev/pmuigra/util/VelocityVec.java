package com.mndev.pmuigra.util;

/**
 * Created by milja on 2/16/2017.
 */

public class VelocityVec {

    private float x = 0.0f;
    private float y = 0.0f;

    public VelocityVec() {
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void updateX(float diffX) {
        x += diffX;
    }

    public void updateY(float diffY) {
        y += diffY;
    }
}
