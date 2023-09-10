package my.edu.utar.noiseapp;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.IOException;


public class RecordVoiceActivity extends BaseActivity {

    private Button StartRecording, StopRecording, StartPlaying, StopPlaying;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String AudioSavaPath = null;
    private boolean isRecording = false;

    // Define a member variable for the database helper
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeManager.getCurrentTheme());
        super.onCreate(savedInstanceState);
        // Retrieve the selected language from SharedPreferences
        String selectedLanguage = getUserSelectedLanguage();

        // Use LocaleHelper to set the locale based on the selected language
        Context updatedContext = LocaleHelper.setLocale(this, selectedLanguage);

        // Set the updated context for this activity
        setContentView(R.layout.activity_record_voice);
        Log.d("LanguageDebug", "Selected Language: " + selectedLanguage);


        // Initialize the database helper
        dbHelper = new MyDatabaseHelper(this);

        StartRecording = findViewById(R.id.startRecord);
        StopRecording = findViewById(R.id.stopRecord);
        StartPlaying = findViewById(R.id.startPlaying);
        StopPlaying = findViewById(R.id.stopPlaying);

        StartRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording && checkPermissions()) {
                    AudioSavaPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/" + "recordingAudio.mp4";

                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    mediaRecorder.setOutputFile(AudioSavaPath);

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        isRecording = true; // Set recording state to true
                        Toast.makeText(RecordVoiceActivity.this, R.string.recording_started_message, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(RecordVoiceActivity.this, R.string.failed_to_start_recording_message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ActivityCompat.requestPermissions(RecordVoiceActivity.this, new String[]{
                            Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 1);
                }
            }
        });

        StopRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    try {
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        isRecording = false; // Set recording state to false
                        Toast.makeText(RecordVoiceActivity.this, R.string.recording_stopped_message, Toast.LENGTH_SHORT).show();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                        Toast.makeText(RecordVoiceActivity.this, R.string.failed_to_stop_recording_message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        StartPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavaPath);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(RecordVoiceActivity.this, R.string.start_playing_message, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(RecordVoiceActivity.this, R.string.failed_to_start_playing_message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        StopPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    Toast.makeText(RecordVoiceActivity.this, R.string.stopped_playing_message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getUserSelectedLanguage() {
        // Retrieve the selected language from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        return preferences.getString("selectedLanguage", "en"); // Default to English if not found
    }

    private boolean checkPermissions() {
        int first = ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO);
        int second = ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return first == PackageManager.PERMISSION_GRANTED &&
                second == PackageManager.PERMISSION_GRANTED;
    }

    // Method to store recording data in the database
    private void storeRecordingData(String audioPath) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        RecordingDAO recordingDAO = new RecordingDAO(database);

        long timestamp = System.currentTimeMillis();

        RecordingData recordingData = new RecordingData(audioPath, timestamp);

        long result = recordingDAO.insertRecording(recordingData);

        if (result != -1) {
            // Recording data was successfully inserted into the database
            Toast.makeText(RecordVoiceActivity.this, R.string.recording_data_saved, Toast.LENGTH_SHORT).show();
        } else {
            // Failed to insert recording data
            Toast.makeText(RecordVoiceActivity.this, R.string.failed_to_save_recording_data, Toast.LENGTH_SHORT).show();
        }

        // Close the database
        database.close();
    }
}



