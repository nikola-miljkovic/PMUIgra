package com.mndev.pmuigra.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GamePolygon implements Serializable, IDrawable {

    private static Paint BlackPaint;

    public static Paint getBlackPaint() {
        return BlackPaint;
    }

    static {
        BlackPaint = new Paint();
        BlackPaint.setColor(Color.BLACK);
        BlackPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    private List<IDrawable> components;
    private GameHole startHole;
    private GameHole endHole;
    private boolean isGameMode = false;

    public GamePolygon() {
        components = new ArrayList<>();
    }

    public List<IDrawable> getComponents() {
        return components;
    }

    public void setComponents(List<IDrawable> components) {
        this.components = components;
    }

    public GameHole getStartHole() {
        return startHole;
    }

    public void setStartHole(GameHole startHole) {
        this.startHole = startHole;
    }

    public GameHole getEndHole() {
        return endHole;
    }

    public void setEndHole(GameHole endHole) {
        this.endHole = endHole;
    }

    public boolean isGameMode() {
        return isGameMode;
    }

    public void setGameMode(boolean gameMode) {
        isGameMode = gameMode;
    }

    @Override
    public void draw(Canvas canvas) {
        if (startHole != null) {
            startHole.draw(canvas);
        }

        if (endHole != null) {
            endHole.draw(canvas);
        }

        for (IDrawable component : components) {
            component.draw(canvas);
        }
    }
}
