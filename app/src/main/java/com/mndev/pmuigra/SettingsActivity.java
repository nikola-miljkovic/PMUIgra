package com.mndev.pmuigra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mndev.pmuigra.controller.PreferenceController;

public class SettingsActivity extends AppCompatActivity {

    PreferenceController preferenceController = PreferenceController.getInstance();

    EditText editMaxFPS;
    EditText editCoefficientOfFriction;
    EditText editCollisionForce;
    EditText editGameSpeed;
    EditText editBoostX;
    EditText editBoostY;

    Button btnSavePreferences;
    Button btnRestoreDefaultPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editMaxFPS = (EditText)findViewById(R.id.max_fps_edit);
        editCoefficientOfFriction = (EditText)findViewById(R.id.cof_edit);
        editCollisionForce = (EditText)findViewById(R.id.collision_force_edit);
        editGameSpeed = (EditText)findViewById(R.id.game_speed_edit);
        editBoostX = (EditText)findViewById(R.id.boost_x_edit);
        editBoostY = (EditText)findViewById(R.id.boost_y_edit);

        btnSavePreferences = (Button)findViewById(R.id.save_pref_btn);
        btnSavePreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferenceController.setMaxFPS(Integer.parseInt(editMaxFPS.getText().toString()));
                preferenceController.setCoefficientOfFriction(Float.parseFloat(editCoefficientOfFriction.getText().toString()));
                preferenceController.setCollisionForce(Float.parseFloat(editCollisionForce.getText().toString()));
                preferenceController.setGameSpeed(Float.parseFloat(editGameSpeed.getText().toString()));
                preferenceController.setBoostX(Float.parseFloat(editBoostX.getText().toString()));
                preferenceController.setBoostY(Float.parseFloat(editBoostY.getText().toString()));

                preferenceController.store(getApplicationContext());
                loadPreferences();
            }
        });

        btnRestoreDefaultPreferences = (Button)findViewById(R.id.restore_pref_btn);
        btnRestoreDefaultPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreDefaults();
                loadPreferences();
            }
        });

        loadPreferences();
    }

    private void loadPreferences() {
        preferenceController.load(getApplicationContext());

        editMaxFPS.setText(String.valueOf(preferenceController.getMaxFPS()), TextView.BufferType.EDITABLE);
        editCoefficientOfFriction.setText(String.valueOf(preferenceController.getCoefficientOfFriction()), TextView.BufferType.EDITABLE);
        editCollisionForce.setText(String.valueOf(preferenceController.getCollisionForce()), TextView.BufferType.EDITABLE);
        editGameSpeed.setText(String.valueOf(preferenceController.getGameSpeed()), TextView.BufferType.EDITABLE);
        editBoostX.setText(String.valueOf(preferenceController.getBoostX()), TextView.BufferType.EDITABLE);
        editBoostY.setText(String.valueOf(preferenceController.getBoostY()), TextView.BufferType.EDITABLE);
    }

    private void restoreDefaults() {
        preferenceController.restoreDefaults(getApplicationContext());
    }
}
