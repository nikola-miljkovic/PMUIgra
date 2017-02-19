package com.mndev.pmuigra.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class ScoresDB extends SQLiteOpenHelper {

    /**
     * METADATA
     */

    public abstract class ScoresTableEntry implements BaseColumns {
        public static final String TABLE_NAME = "score";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_POLYGON_NAME = "polygon_name";
        public static final String COLUMN_NAME_SCORE = "score";
    }

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + ScoresTableEntry.TABLE_NAME + " (" + ScoresTableEntry._ID
            + " INTEGER PRIMARY KEY,"
            + ScoresTableEntry.COLUMN_NAME_USERNAME + " TEXT, "
            + ScoresTableEntry.COLUMN_NAME_POLYGON_NAME + " TEXT, "
            + ScoresTableEntry.COLUMN_NAME_SCORE + " TEXT);";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + ScoresTableEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "default.db";

    public ScoresDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
