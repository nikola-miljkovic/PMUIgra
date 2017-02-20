package com.mndev.pmuigra;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mndev.pmuigra.controller.PolygonController;
import com.mndev.pmuigra.model.GamePolygon;
import com.mndev.pmuigra.model.LineComponent;

public class NewPolygonActivity extends Activity implements SurfaceHolder.Callback, View.OnTouchListener, SavePolygonDialog.Listener {

    PolygonController polygonController;
    private SurfaceHolder surfaceHolder;
    private String name;

    float contextX;
    float contextY;

    LineComponent drawingRectangle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_polygon);

        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.polygonView);
        surfaceView.getHolder().addCallback(this);
        surfaceView.setOnTouchListener(this);

        polygonController = PolygonController.getInstance();
        String polygonName = getIntent().getStringExtra("NAME");
        if (polygonName != null && !polygonName.isEmpty()) {
            name = polygonName;

            boolean status = polygonController.load(polygonName, getApplicationContext());
            if (status) {
                Toast.makeText(this, getString(R.string.toast_edit_polygon, polygonName), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.toast_edit_error_polygon, polygonName), Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, R.string.toast_new_polygon, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;

        Canvas canvas = surfaceHolder.lockCanvas();
        polygonController.draw(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);

        registerForContextMenu(findViewById(R.id.polygonView));
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        this.surfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        polygonController = null;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.polygonView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.polygon_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.polygon_set_start:
                polygonController.createStartPoint(contextX, contextY);
                redraw();
                return true;
            case R.id.polygon_set_end:
                polygonController.createEndPoint(contextX, contextY);
                redraw();
                return true;
            case R.id.polygon_set_hole:
                polygonController.createHole(contextX, contextY);
                redraw();
                return true;
            case R.id.polygon_create_rectangle:
                if (drawingRectangle == null) {
                    drawingRectangle = new LineComponent(contextX, contextX + 200.0f, contextY, contextY + 15.0f);

                } else {
                    polygonController.createRectangle(drawingRectangle.getX1(), drawingRectangle.getY1(),
                            drawingRectangle.getX2(), drawingRectangle.getY2());
                    drawingRectangle = null;
                }

                redraw();
                return true;
            case R.id.polygon_save:
                // Call save dialog if we are not in edit mode
                // if confirmed save is done in onClick function
                if (name == null) {
                    SavePolygonDialog savePolygonDialog = new SavePolygonDialog();
                    savePolygonDialog.setListener(this);
                    savePolygonDialog.show(getFragmentManager(), "Save Polygon");
                } else {
                    save(name);
                }

                return true;
            case R.id.polygon_cancel:
                polygonController.cancel();
                Toast.makeText(this, R.string.toast_polygon_cancel, Toast.LENGTH_SHORT).show();
                finish();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = MotionEventCompat.getActionMasked(motionEvent);
        switch(action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                contextX = motionEvent.getX();
                contextY = motionEvent.getY();
                checkAndUpdateDrawingRectangle();
        }
        return super.onTouchEvent(motionEvent);
    }

    private void redraw() {
        Canvas canvas = surfaceHolder.lockCanvas();

        polygonController.draw(canvas);
        if (drawingRectangle != null) {
            drawingRectangle.draw(canvas);
        }

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void checkAndUpdateDrawingRectangle() {
        if (drawingRectangle != null) {
            drawingRectangle.setX2(contextX);
            drawingRectangle.setY2(contextY);
            redraw();
        }
    }

    @Override
    public void save(String name) {
         boolean status = polygonController.save(name, getApplicationContext());

        if (status) {
            Toast.makeText(this, getString(R.string.toast_polygon_created, name), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.toast_polygon_creation_error, name), Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    @Override
    public void cancel() {
        // Nothing here, no finish
    }
}
