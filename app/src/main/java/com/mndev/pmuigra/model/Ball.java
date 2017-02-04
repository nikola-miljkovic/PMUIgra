package com.mndev.pmuigra.model;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball implements IDrawable {

    private float x;
    private float y;
    private float radius;
    private float smallRadius;
    private Paint paint;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        this.smallRadius = radius / 10;
    }

    public float getSmallRadius() {
        return smallRadius;
    }

    public void setSmallRadius(float smallRadius) {
        this.smallRadius = smallRadius;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, paint);
        canvas.drawCircle(x, y, smallRadius, GamePolygon.getBlackPaint());
    }
}
