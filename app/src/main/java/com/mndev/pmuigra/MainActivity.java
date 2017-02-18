package com.mndev.pmuigra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mndev.pmuigra.controller.PreferenceController;

import java.io.File;
import java.util.ArrayList;
import java.util.prefs.PreferenceChangeEvent;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> polygonNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load preferences
        PreferenceController.getInstance().load(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();

        File dir = getFilesDir();
        polygonNames = new ArrayList<>();
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                polygonNames.add(file.getName());
            }
        }

        listView = (ListView)findViewById(R.id.list_view);
        registerForContextMenu(listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, polygonNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String listItem = (String)listView.getItemAtPosition(position);
                openPolygonGameActivity(listItem);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new_polygon:
                openNewPolygonActivity();
                return true;
            case R.id.menu_statistics:
                openStatisticsActivity();
                return true;
            case R.id.menu_settings:
                openSettingsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openNewPolygonActivity() {
        Intent intent = new Intent(this, NewPolygonActivity.class);
        startActivity(intent);
    }

    private void openEditPolygonActivity(String name) {
        Intent intent = new Intent(this, NewPolygonActivity.class);
        intent.putExtra("NAME", name);
        startActivity(intent);
    }

    private void openPolygonGameActivity(String name) {
        Intent intent = new Intent(this, PolygonGameActivity.class);
        intent.putExtra("NAME", name);
        startActivity(intent);
    }

    private void openStatisticsActivity() {
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.list_view) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.polygon_item_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemPosition = info.position;
        if (listView != null && polygonNames != null && !polygonNames.isEmpty()) {
            String name = polygonNames.get(itemPosition);
            switch(item.getItemId()) {
                case R.id.item_edit:
                    openEditPolygonActivity(name);
                    return true;
                case R.id.item_delete:
                    deleteFile(name);
                    polygonNames.remove(itemPosition);
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>)listView.getAdapter();
                    adapter.notifyDataSetChanged();
                    return true;
            }
        }
        return super.onContextItemSelected(item);
    }
}
