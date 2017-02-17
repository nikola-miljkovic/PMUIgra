package com.mndev.pmuigra.controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.mndev.pmuigra.model.Ball;
import com.mndev.pmuigra.model.GameHole;
import com.mndev.pmuigra.model.GameObject;
import com.mndev.pmuigra.model.GamePolygon;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class GameController {
    private static GameController ourInstance = new GameController();

    private GameRenderThread gameRenderThread;
    private GamePolygon gamePolygon;

    private Ball gameBall;

    private int width;
    private int height;

    private final int MaxFpsSpeed = PreferenceController.getInstance().getRenderSpeed();
    private final float CoefOfFriction = PreferenceController.getInstance().getCoefficientOfFriction();
    private final float CollisionForce = PreferenceController.getInstance().getCollisionForce();
    
    public static GameController getInstance() {
        return ourInstance;
    }

    private GameController() {
    }

    public boolean load(String name, Context context) {
        try {
            FileInputStream fis = context.openFileInput(name);
            ObjectInputStream is = new ObjectInputStream(fis);
            gamePolygon = (GamePolygon) is.readObject();

            if (gamePolygon.getStartHole() != null) {
                GameHole gameHole = gamePolygon.getStartHole();

                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.GREEN);

                gameBall = new Ball();
                gameBall.setX(gameHole.getX());
                gameBall.setY(gameHole.getY());
                gameBall.setPaint(paint);

                gamePolygon.setGameMode(true);
            }

            if (gamePolygon.getEndHole() != null) {
                gamePolygon.getEndHole().setPaint(PolygonController.getEndPointPaint());
            }

            for (GameObject drawable : gamePolygon.getGameObjects()) {
                if (drawable.getClass() == GameHole.class) {
                    ((GameHole)drawable).setPaint(PolygonController.getHolePaint());
                }
            }

            is.close();
            fis.close();
            return true;
        } catch (Exception e) {

        }

        return false;
    }

    public void setDimensions(int w, int h) {
        this.width = w;
        this.height = h;
    }

    public void setMovementValues(float x, float y, float z) {
    }

    public void start(SurfaceHolder surfaceHolder) {
        gameRenderThread = new GameRenderThread(this, surfaceHolder);
        gameRenderThread.start();
    }

    private class GameRenderThread extends Thread {
        GameController gameController;
        SurfaceHolder surfaceHolder;

        GameRenderThread(GameController gameController, SurfaceHolder surfaceHolder) {
            this.gameController = gameController;
            this.surfaceHolder = surfaceHolder;
        }

        @Override
        public void run() {
            super.run();

            Canvas canvas;
            long deltaTime = MaxFpsSpeed;
            long currentTime;

            while(true) {
                currentTime = System.currentTimeMillis();

                if (deltaTime >= MaxFpsSpeed) {
                    gameController.update(deltaTime);

                    canvas = surfaceHolder.lockCanvas();
                    gameController.draw(canvas);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }

                deltaTime = (System.currentTimeMillis() - currentTime);
            }
        }
    }

    private void update(long deltaTime) {
        gameBall.setX((gameBall.getX() - 15.0f) / deltaTime);
        gameBall.setY((gameBall.getY() - 15.0f) / deltaTime);

        if (gameBall.getX() - gameBall.getRadius() <= 0.0f
                || gameBall.getX() + gameBall.getRadius() >= width) {
            gameBall.setX(width / 2);
        }
    }

    private void draw(Canvas canvas) {
        gameBall.draw(canvas);

        gamePolygon.draw(canvas);
    }
}
