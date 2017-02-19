package com.mndev.pmuigra.controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;

import com.mndev.pmuigra.PolygonGameActivity;
import com.mndev.pmuigra.R;
import com.mndev.pmuigra.model.Ball;
import com.mndev.pmuigra.model.GameHole;
import com.mndev.pmuigra.model.GameObject;
import com.mndev.pmuigra.model.GamePolygon;
import com.mndev.pmuigra.model.Hud;
import com.mndev.pmuigra.model.LineComponent;
import com.mndev.pmuigra.util.VelocityVec;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class GameController {
    public abstract class GameStatus {
        public static final int IN_PROGRESS = 0;
        public static final int WON = 1;
        public static final int LOST = 2;
    }

    private static GameController ourInstance = new GameController();

    private GameRenderThread gameRenderThread;
    private GamePolygon gamePolygon;
    private Hud hud;

    private MediaPlayer mediaPlayer;
    private PolygonGameActivity gameActivity;
    private Ball gameBall;
    private VelocityVec ballVect = new VelocityVec();
    private int gameStatus = GameStatus.IN_PROGRESS;

    private int width;
    private int height;

    private long totalGameTime = 0;

    private final int MaxFpsSpeed = PreferenceController.getInstance().getRenderSpeed();
    private final float CoefOfFriction = PreferenceController.getInstance().getCoefficientOfFriction();
    private final float CollisionForce = PreferenceController.getInstance().getCollisionForce();
    private final float GameSpeed = PreferenceController.getInstance().getGameSpeed();
    private final float BoostX = PreferenceController.getInstance().getBoostX();
    private final float BoostY = PreferenceController.getInstance().getBoostY();
    
    public static GameController getInstance() {
        if (ourInstance == null) {
            ourInstance = new GameController();
        }
        return ourInstance;
    }

    private GameController() {
        hud = new Hud();
    }

    public boolean load(String name, PolygonGameActivity gameActivity) {
        try {
            this.gameActivity = gameActivity;
            Context context = gameActivity.getApplicationContext();
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

    public void setMovementValues(float x, float y) {
        ballVect.updateX(- x * BoostX * GameSpeed);
        ballVect.updateY(y * BoostY * GameSpeed);
    }

    public void start(SurfaceHolder surfaceHolder) {
        gameRenderThread = new GameRenderThread(this, surfaceHolder);
        gameRenderThread.start();
    }

    // TODO: kill game
    public void finishGame() {
        gameRenderThread.interrupt();

        if (gameActivity != null) {
            gameActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (gameStatus != GameStatus.IN_PROGRESS &&
                                (mediaPlayer == null || !mediaPlayer.isPlaying())) {
                            if (gameStatus == GameStatus.WON) {
                                mediaPlayer = MediaPlayer.create(gameActivity.getApplicationContext(), R.raw.win);
                            } else {
                                mediaPlayer = MediaPlayer.create(gameActivity.getApplicationContext(), R.raw.loss);
                            }

                            mediaPlayer.start();
                        }

                        gameActivity.onGameFinished(gameStatus, Hud.produceTimeString(totalGameTime));
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            });
        }
        ourInstance = null;
    }

    public int getGameStatus() {
        return gameStatus;
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

            while(!isInterrupted()) {
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
        float speed = deltaTime / 1000.0f;
        ballVect.updateX(- ballVect.getX() * CoefOfFriction * speed);
        ballVect.updateY(- ballVect.getY() * CoefOfFriction * speed);

        gameBall.setX(gameBall.getX() + ballVect.getX() * speed);
        gameBall.setY(gameBall.getY() + ballVect.getY() * speed);

        if (gameBall.getX() - gameBall.getRadius() <= 0.0f
                || gameBall.getX() + gameBall.getRadius() >= width) {
            ballVect.setX(- ballVect.getX() * CollisionForce);
        }

        if (gameBall.getY() - gameBall.getRadius() <= 0.0f
                || gameBall.getY() + gameBall.getRadius() >= height) {
            ballVect.setY(- ballVect.getY() * CollisionForce);
        }

        for (GameObject object : gamePolygon.getGameObjects()) {
            if (object instanceof LineComponent) {
                // bounce
                LineComponent lineComponent = (LineComponent)object;
                if (gameBall.getX() - gameBall.getRadius() < lineComponent.getX2()
                        && gameBall.getX() + gameBall.getRadius() > lineComponent.getX1()
                        && gameBall.getY() > lineComponent.getY1()
                        && gameBall.getY() < lineComponent.getY2())  {
                    ballVect.setX(- ballVect.getX() * CollisionForce);
                }

                if (gameBall.getX() < lineComponent.getX2()
                        && gameBall.getX() > lineComponent.getX1()
                        && gameBall.getY() + gameBall.getRadius()  >= lineComponent.getY1()
                        && gameBall.getY() - gameBall.getRadius()  <= lineComponent.getY2())  {
                    ballVect.setY(- ballVect.getY() * CollisionForce);
                }
            } else if (object.HasColided(gameBall.getX(), gameBall.getY(), gameBall.getRadius())) {
                gameStatus = GameStatus.LOST;
                finishGame();
            }
        }

        if (gamePolygon.getEndHole().HasColided(gameBall.getX(), gameBall.getY(), gameBall.getRadius())) {
            gameStatus = GameStatus.WON;
            finishGame();
        }

        hud.updateTime(deltaTime);
        hud.setFps(1000 / deltaTime);

        totalGameTime += deltaTime;
    }

    private void draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        gamePolygon.draw(canvas);
        gameBall.draw(canvas);
        hud.draw(canvas);
    }
}
