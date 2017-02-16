package com.mndev.pmuigra.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.Serializable;

public class GameHole implements GameObject, Serializable {

    private float x;
    private float y;
    private float radius;
    private float smallRadius;
    private transient Paint paint;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        this.smallRadius = radius / 6;
    }

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

    public void setPaint(Paint paint) {
        this.paint = paint;
        this.paint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, GamePolygon.getBlackPaint());
        canvas.drawCircle(x, y, radius - smallRadius, paint);
    }

    @Override
    public boolean HasColided(float x, float y, float radius) {
        float dx = x - this.x;
        float dy = y - this.y;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);

        if (distance < radius + this.radius) {
            return true;
        }
        return false;
    }
}
