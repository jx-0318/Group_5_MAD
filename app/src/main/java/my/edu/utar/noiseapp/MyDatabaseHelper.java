package my.edu.utar.noiseapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "recordings.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RECORDINGS = "recordings";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FILE_PATH = "file_path";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_RECORDINGS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_FILE_PATH + " TEXT," +
                COLUMN_TIMESTAMP + " INTEGER);";
        db.execSQL(createTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema upgrades here
        // You can modify the table structure if needed
    }
}
