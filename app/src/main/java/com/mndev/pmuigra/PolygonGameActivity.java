package com.mndev.pmuigra;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.mndev.pmuigra.controller.GameController;
import com.mndev.pmuigra.controller.StatisticsController;

public class PolygonGameActivity extends Activity implements SurfaceHolder.Callback, SaveScoreDialog.Listener {

    GameController gameController = GameController.getInstance();
    StatisticsController statisticsController = StatisticsController.getInstance();

    SensorManager sensorManager;
    SensorEventHandler sensorEventHandler;

    String polygonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygon_game);

        polygonName = getIntent().getStringExtra("NAME");
        if (polygonName != null && !polygonName.isEmpty()) {
            boolean status = gameController.load(polygonName, this);
        } else {
            Toast.makeText(this, R.string.toast_new_polygon, Toast.LENGTH_SHORT).show();
        }

        sensorEventHandler = new SensorEventHandler();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(
                sensorEventHandler,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME
        );

        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.game_surface);
        surfaceView.getHolder().addCallback(this);
    }

    @Override
    public void onBackPressed() {
        gameController.finishGame();

        super.onBackPressed();
    }

    public void onGameFinished(int gameStatus, String totalGameTime) throws InterruptedException {
        switch (gameStatus) {
            case GameController.GameStatus.IN_PROGRESS:
                // just exit
                Toast.makeText(this, R.string.toast_game_exit, Toast.LENGTH_SHORT).show();
                break;
            case GameController.GameStatus.LOST:
                Toast.makeText(this, R.string.toast_game_lost, Toast.LENGTH_SHORT).show();
                Thread.sleep(1000);
                finish();
                break;
            case GameController.GameStatus.WON:
                Toast.makeText(this, getString(R.string.toast_game_won, totalGameTime), Toast.LENGTH_SHORT).show();
                Bundle arguments = new Bundle();
                arguments.putString(SaveScoreDialog.ARGUMENT_TOTAL_GAME_TIME, totalGameTime);

                SaveScoreDialog saveScoreDialog = new SaveScoreDialog();
                saveScoreDialog.setListener(this);
                saveScoreDialog.setArguments(arguments);
                saveScoreDialog.show(getFragmentManager(), "Save Polygon");

                break;
        }

        sensorManager.unregisterListener(sensorEventHandler);
    }

    @Override
    public void save(String name, String totalGameTime) {
        statisticsController.loadContext(getApplicationContext());
        try {
            statisticsController.writeStatistic(polygonName, "Nikolica", Float.parseFloat(totalGameTime));
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void cancel() {
        finish();
    }

    private class SensorEventHandler implements SensorEventListener {

        final float alpha = 0.8f;
        float[] accelerationValues = { 0.0f, 0.0f };
        //float direction =

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            for (int i = 0; i < 2; i++) {
                accelerationValues[i] = alpha * accelerationValues[i] + (1 - alpha) * sensorEvent.values[i] ; //- gravity[i];
            }

            Log.d("ACCELERATION", " X: " + String.valueOf(accelerationValues[0])
                    + " Y: " + String.valueOf(accelerationValues[1]));

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameController.setMovementValues(accelerationValues[0], accelerationValues[1]);
                }
            });
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        gameController.start(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int w, int h) {
        gameController.setDimensions(w, h);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
