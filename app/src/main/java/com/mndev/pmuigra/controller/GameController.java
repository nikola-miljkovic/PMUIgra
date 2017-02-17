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
import com.mndev.pmuigra.util.VelocityVec;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class GameController {
    private static GameController ourInstance = new GameController();

    private GameRenderThread gameRenderThread;
    private GamePolygon gamePolygon;

    private Ball gameBall;

    // TODO: changme
    private VelocityVec ballVect = new VelocityVec();

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

                Paint paint = new Paint();
                paint.setColor(Color.GREEN);
                paint.setFlags(Paint.ANTI_ALIAS_FLAG);

                gameBall = new Ball();
                gameBall.setX(gameHole.getX());
                gameBall.setY(gameHole.getY());
                gameBall.setRadius(gameHole.getRadius());
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
        ballVect.updateX(x * 0.6f);
        ballVect.updateY(y * 0.1f);
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

        public void run() {
            super.run();

            Canvas canvas;
            long deltaTime = MaxFpsSpeed;
            long renderTime = 0;

            while(true) {
                if (deltaTime >= MaxFpsSpeed) {
                    renderTime = System.currentTimeMillis();
                    gameController.update(deltaTime);

                    canvas = surfaceHolder.lockCanvas();
                    gameController.draw(canvas);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }

                deltaTime = (System.currentTimeMillis() - renderTime);
            }
        }
    }

    private void update(long deltaTime) {
        // apply friction

        ballVect.updateX(- ballVect.getX() * CoefOfFriction * deltaTime / 1000);
        ballVect.updateY(- ballVect.getY() * CoefOfFriction * deltaTime / 1000);

        gameBall.setX(gameBall.getX() + ballVect.getX() * deltaTime / 1000);
        gameBall.setY(gameBall.getY() + ballVect.getY() * deltaTime / 1000);

        if (gameBall.getX() - gameBall.getRadius() <= 0.0f
                || gameBall.getX() + gameBall.getRadius() >= width) {
            ballVect.setX(- ballVect.getX());
        }

        if (gameBall.getY() - gameBall.getRadius() <= 0.0f
                || gameBall.getY() + gameBall.getRadius() >= height) {
            ballVect.setY(- ballVect.getY());
        }
    }

    private void draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        gamePolygon.draw(canvas);
        gameBall.draw(canvas);
    }
}
