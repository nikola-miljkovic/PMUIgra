package com.mndev.pmuigra.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Hud implements GameObject {

    private long time = 0;
    private long fps = 0;
    private Paint defaultPaint;

    public Hud() {
        defaultPaint = new Paint();
        defaultPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        defaultPaint.setColor(Color.DKGRAY);
        defaultPaint.setTextSize(24.0f);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(
                "Time: " + String.valueOf(time / 1000)
                + "." + String.valueOf((time % 1000) / 10),
                20.0f,
                44.0f,
                defaultPaint
        );

        canvas.drawText(
                "FPS: " + String.valueOf(fps),
                20.0f,
                72.0f,
                defaultPaint
        );
    }

    @Override
    public boolean HasColided(float x, float y, float radius) {
        return false;
    }

    public void updateTime(long deltaTime) {
        time += deltaTime;
    }

    public void setFps(long fps) {
        this.fps = fps;
    }
}
