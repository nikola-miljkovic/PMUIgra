package com.mndev.pmuigra;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
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

public class PolygonGameActivity extends Activity implements SurfaceHolder.Callback {

    GameController gameController = GameController.getInstance();
    SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygon_game);

        String polygonName = getIntent().getStringExtra("NAME");
        if (polygonName != null && !polygonName.isEmpty()) {
            boolean status = gameController.load(polygonName, getApplicationContext());
        } else {
            Toast.makeText(this, R.string.toast_new_polygon, Toast.LENGTH_SHORT).show();
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(
                new SensorEventHandler(),
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME
        );

        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.game_surface);
        surfaceView.getHolder().addCallback(this);
        //surfaceView.setOnTouchListener(this);
    }

    private class SensorEventHandler implements SensorEventListener {

        final float alpha = 0.8f;
        float[] gravity = { 0.0f, 0.0f, 0.0f };
        float[] linear_acceleration = { 0.0f, 0.0f, 0.0f };

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            for (int i = 0; i < 3; i++) {
                gravity[i] = alpha * gravity[0] + (1 - alpha) * sensorEvent.values[i];
                linear_acceleration[i] = sensorEvent.values[i] - gravity[i];
            }

            Log.d("ACCELERATION", " X: " + String.valueOf(linear_acceleration[0])
                    + " Y: " + String.valueOf(linear_acceleration[1])
                    + " Z: " + String.valueOf(linear_acceleration[2]));

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //gameController.setMovementValues(linear_acceleration[0], linear_acceleration[1], linear_acceleration[2]);
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
