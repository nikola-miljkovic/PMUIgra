package com.mndev.pmuigra.model;

import android.graphics.Canvas;

public interface GameObject {
    void draw(Canvas canvas);
    boolean HasColided(float x, float y, float radius);
}
