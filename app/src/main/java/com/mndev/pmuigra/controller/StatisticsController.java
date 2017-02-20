package com.mndev.pmuigra.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mndev.pmuigra.model.StatisticsEntry;
import com.mndev.pmuigra.util.ScoresDB;

import java.util.ArrayList;
import java.util.List;

public class StatisticsController {
    private static StatisticsController ourInstance = new StatisticsController();

    public static StatisticsController getInstance() {
        return ourInstance;
    }

    ScoresDB scoresDB;
    private StatisticsController() {
    }

    public StatisticsController loadContext(Context context) {
        scoresDB = new ScoresDB(context);
        return this;
    }

    public void writeStatistic(String polygonName, String userName, float score) throws DatabaseNoLoadedException {
        if (scoresDB == null) {
            throw new DatabaseNoLoadedException();
        }

        SQLiteDatabase db = scoresDB.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ScoresDB.ScoresTableEntry.COLUMN_NAME_POLYGON_NAME, polygonName);
        contentValues.put(ScoresDB.ScoresTableEntry.COLUMN_NAME_USERNAME, userName);
        contentValues.put(ScoresDB.ScoresTableEntry.COLUMN_NAME_SCORE, score);
        db.insert(ScoresDB.ScoresTableEntry.TABLE_NAME, null, contentValues);
    }

    public List<StatisticsEntry> getStatisticsForPolygon(String polygonName) throws DatabaseNoLoadedException {
        if (scoresDB == null) {
            throw new DatabaseNoLoadedException();
        }

        SQLiteDatabase db = scoresDB.getReadableDatabase();
        String[] columns = {
                ScoresDB.ScoresTableEntry.COLUMN_NAME_POLYGON_NAME,
                ScoresDB.ScoresTableEntry.COLUMN_NAME_USERNAME,
                ScoresDB.ScoresTableEntry.COLUMN_NAME_SCORE
        };
        String whereSelection = ScoresDB.ScoresTableEntry.COLUMN_NAME_POLYGON_NAME + " = ?";
        String[] whereValues = { polygonName };

        Cursor c = db.query(
                ScoresDB.ScoresTableEntry.TABLE_NAME,
                columns,
                whereSelection,
                whereValues,
                null,
                null,
                ScoresDB.ScoresTableEntry.COLUMN_NAME_SCORE + " DESC"
        );

        List<StatisticsEntry> entries = new ArrayList<>();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                entries.add(new StatisticsEntry(
                        c.getString(c.getColumnIndex(ScoresDB.ScoresTableEntry.COLUMN_NAME_POLYGON_NAME)),
                        c.getString(c.getColumnIndex(ScoresDB.ScoresTableEntry.COLUMN_NAME_USERNAME)),
                        c.getFloat(c.getColumnIndex(ScoresDB.ScoresTableEntry.COLUMN_NAME_SCORE))
                ));
            }
        }

        return entries;
    }

    public class DatabaseNoLoadedException extends Exception {
    }
}
