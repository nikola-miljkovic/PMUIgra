package com.mndev.pmuigra.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GamePolygon implements Serializable, GameObject {

    private static Paint BlackPaint;

    static Paint getBlackPaint() {
        return BlackPaint;
    }

    static {
        BlackPaint = new Paint();
        BlackPaint.setColor(Color.BLACK);
        BlackPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    private List<GameObject> gameObjects;
    private GameHole startHole;
    private GameHole endHole;
    private transient boolean isGameMode = false;

    public GamePolygon() {
        gameObjects = new ArrayList<>();
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
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
        if (startHole != null && !isGameMode) {
            startHole.draw(canvas);
        }

        if (endHole != null) {
            endHole.draw(canvas);
        }

        for (GameObject component : gameObjects) {
            component.draw(canvas);
        }
    }

    @Override
    public boolean HasColided(float x, float y, float radius) {
        return false;
    }
}
