package com.mndev.pmuigra.controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.mndev.pmuigra.model.GameHole;
import com.mndev.pmuigra.model.GamePolygon;
import com.mndev.pmuigra.model.GameObject;
import com.mndev.pmuigra.model.LineComponent;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PolygonController implements GameObject {

    /* SINGLETON */
    private static PolygonController polygonController;

    public static PolygonController getInstance() {
        if (polygonController == null) {
            polygonController = new PolygonController();
        }
        return polygonController;
    }

    private GamePolygon gamePolygon;

    private PolygonController() {
        gamePolygon = new GamePolygon();
    }

    public void createStartPoint(float x, float y) {
        GameHole hole = createGameHole(x, y, getStartPointPaint());
        gamePolygon.setStartHole(hole);
    }

    public void createEndPoint(float x, float y) {
        GameHole hole = createGameHole(x, y, getEndPointPaint());
        gamePolygon.setEndHole(hole);
    }

    public void createHole(float x, float y) {
        GameHole hole = createGameHole(x, y, getHolePaint());
        gamePolygon.getGameObjects().add(hole);
    }

    public void createRectangle(float left, float top, float right, float bottom) {
        gamePolygon.getGameObjects().add(new LineComponent(left, right, top, bottom));
    }

    public boolean save(String name, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(name, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(gamePolygon);
            os.close();
            fos.close();
            polygonController = null;

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean load(String name, Context context) {
        try {
            FileInputStream fis = context.openFileInput(name);
            ObjectInputStream is = new ObjectInputStream(fis);
            gamePolygon = (GamePolygon) is.readObject();

            if (gamePolygon.getStartHole() != null) {
                gamePolygon.getStartHole().setPaint(getStartPointPaint());
            }

            if (gamePolygon.getEndHole() != null) {
                gamePolygon.getEndHole().setPaint(getEndPointPaint());
            }

            for (GameObject drawable : gamePolygon.getGameObjects()) {
                if (drawable.getClass() == GameHole.class) {
                    ((GameHole)drawable).setPaint(getHolePaint());
                }
            }

            is.close();
            fis.close();
            return true;
        } catch (Exception e) {

        }

        return false;
    }

    public void cancel() {
        polygonController = null;
    }

    private GameHole createGameHole(float x, float y, Paint paint) {
        GameHole gameHole = new GameHole();
        gameHole.setX(x);
        gameHole.setY(y);
        gameHole.setRadius(getDefaultRadius());
        gameHole.setPaint(paint);
        return gameHole;
    }

    private float getDefaultRadius() {
        return 15.0f;
    }

    public static Paint getStartPointPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        return paint;
    }

    public static Paint getEndPointPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        return paint;
    }

    public static Paint getHolePaint() {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        return paint;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        gamePolygon.draw(canvas);
    }

    @Override
    public boolean HasColided(float x, float y, float radius) {
        return false;
    }
}
