package my.edu.utar.noiseapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class RecordingDAO {
    private SQLiteDatabase database;
    private static final String TABLE_RECORDINGS = "recordings";
    private static final String KEY_FILE_PATH = "file_path";
    private static final String KEY_TIMESTAMP = "timestamp";

    public RecordingDAO(SQLiteDatabase database) {
        this.database = database;
    }

    public long insertRecording(RecordingData recordingData) {
        ContentValues values = new ContentValues();

        // Add the recording data to the ContentValues
        values.put(KEY_FILE_PATH, recordingData.getFilePath());
        values.put(KEY_TIMESTAMP, recordingData.getTimestamp());

        // Insert the data into the table
        long result = database.insert(TABLE_RECORDINGS, null, values);

        // Return the result of the insertion (the row ID)
        return result;
    }

    // Add methods for retrieving recordings if needed
}


