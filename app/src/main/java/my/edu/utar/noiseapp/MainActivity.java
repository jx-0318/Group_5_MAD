package my.edu.utar.noiseapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private List<NoiseLevelData> noiseLevelList = new ArrayList<>();


    private Button buttonToMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Retrieve the selected language from SharedPreferences
        String selectedLanguage = getUserSelectedLanguage();

        // Use LocaleHelper to set the locale based on the selected language
        Context updatedContext = LocaleHelper.setLocale(this, selectedLanguage);

        // Set the updated context for this activity
        setContentView(R.layout.activity_main);
        Log.d("LanguageDebug", "Selected Language: " + selectedLanguage);


        FirebaseApp.initializeApp(this);

        // Check if the permission is not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            // Permission is already granted; you can proceed with recording.
            // Add your noise detection or audio recording logic here.
        }

        // Find the button in your layout
        Button buttonToInputNoise = findViewById(R.id.buttonToInputNoise);

// Set a click listener for the button
        buttonToInputNoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to InputNoiseActivity
                Intent intent = new Intent(MainActivity.this, InputNoiseActivity.class);
                startActivity(intent);
            }
        });

        buttonToMap = findViewById(R.id.buttonRecordVoice);

        buttonToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the RecordVoiceActivity
                Intent intent = new Intent(MainActivity.this, RecordVoiceActivity.class);
                startActivity(intent);
            }
        });

        Button buttonToMap = findViewById(R.id.buttonToMap);

        // Set a click listener for the button
        buttonToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the MapActivity
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });


        // Find the language selection button by its ID
        Button languageSelectionButton = findViewById(R.id.languageSelectionButton);

        // Set a click listener for the button
        languageSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the LanguageSelectionActivity when the button is clicked
                Intent intent = new Intent(MainActivity.this, LanguageSelectionActivity.class);
                startActivity(intent);
            }
        });

        Button buttonToggleTheme = findViewById(R.id.buttonToggleTheme);

        buttonToggleTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTheme(); // Toggle the theme when the button is clicked
            }
        });

        //Historical data button click listener
        findViewById(R.id.historicalDataButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openHistoricalDataActivity();
            }
        });

        // Exit button click listener
        findViewById(R.id.exitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showExitDialog();
            }
        });


    }



    //Open HistoricalDataActivity
    private void openHistoricalDataActivity() {
        Intent intent = new Intent(this, HistoricalDataActivity.class);
        startActivity(intent);
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.exit_confirmation)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // Close the app
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted; you can proceed with recording.
                // Add your noise detection or audio recording logic here.
            } else {
                // Permission denied; handle the case where the user doesn't grant the permission.
                // You can show a message or take appropriate action.
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Call super method
    }

    private String getUserSelectedLanguage() {
        // Retrieve the selected language from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        return preferences.getString("selectedLanguage", "en"); // Default to English if not found
    }

    private int currentTheme = R.style.Theme_NoiseApp; // Default theme


    protected void setAppTheme() {
        int currentTheme = ThemeManager.getCurrentTheme();
        setTheme(currentTheme);
    }

    private void toggleTheme() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
            // Switch to dark theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            // Switch to light theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        recreate(); // Restart the activity to apply the new theme
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_theme) {

            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

            if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            recreate(); // Restart the activity to apply the new theme
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_theme, menu);
        return true;
    }

}


