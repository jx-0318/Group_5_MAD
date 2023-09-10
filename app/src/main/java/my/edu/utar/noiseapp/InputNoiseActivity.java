package my.edu.utar.noiseapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InputNoiseActivity extends BaseActivity {

    private EditText editTextNoiseLevel;
    private DatabaseReference databaseReference;
    private TextView textViewNoiseCategory;
    private static final String CHANNEL_ID = "noise_alert_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeManager.getCurrentTheme());
        super.onCreate(savedInstanceState);

        // Retrieve the selected language from SharedPreferences
        String selectedLanguage = getUserSelectedLanguage();

        // Use LocaleHelper to set the locale based on the selected language
        Context updatedContext = LocaleHelper.setLocale(this, selectedLanguage);

        // Set the updated context for this activity
        setContentView(R.layout.activity_input_noise);

        // Create the notification channel (call this before creating any notifications)
        createNotificationChannel();

        editTextNoiseLevel = findViewById(R.id.editTextNoiseLevel);
        databaseReference = FirebaseDatabase.getInstance().getReference("noiseData");
        textViewNoiseCategory = findViewById(R.id.textViewNoiseCategory);

        Button buttonSubmitNoise = findViewById(R.id.buttonSubmitNoise);
        buttonSubmitNoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNoiseData();
            }
        });
    }

    private String getUserSelectedLanguage() {
        // Retrieve the selected language from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        return preferences.getString("selectedLanguage", "en"); // Default to English if not found
    }

    private void saveNoiseData() {
        String noiseLevelText = editTextNoiseLevel.getText().toString().trim();
        if (!noiseLevelText.isEmpty()) {
            int noiseLevel = Integer.parseInt(noiseLevelText);

            if (noiseLevel >= 1 && noiseLevel <= 100) {
                // Determine noise category based on noise level
                String category;
                String alertMessage;

                if (noiseLevel <= 25) {
                    category = "Low (0-25 dB)";
                    alertMessage = getString(R.string.low_noise_alert);
                } else if (noiseLevel <= 50) {
                    category = "Medium (26-50 dB)";
                    alertMessage = getString(R.string.medium_noise_alert);
                } else {
                    category = "High (51-100 dB)";
                    alertMessage = getString(R.string.high_noise_alert);
                }

                // Display the noise category message
                textViewNoiseCategory.setText("Noise Category: " + category);
                textViewNoiseCategory.setVisibility(View.VISIBLE); // Make the TextView visible

                // Show the notification with the alert message
                createNotification(alertMessage);

                NoiseLevelData noiseData = new NoiseLevelData(noiseLevel, category);
                String dataId = databaseReference.push().getKey();
                if (dataId != null) {
                    databaseReference.child(dataId).setValue(noiseData);

                    // Optionally, you can clear the input field.
                    editTextNoiseLevel.setText("");
                }
            } else {
                // Notify the user that the input is outside the valid range
                Toast.makeText(this, R.string.invalid_noise_level_message, Toast.LENGTH_SHORT).show();
            }
        } else {
            // Notify the user that the input is empty
            Toast.makeText(this, R.string.enter_noise_level_message, Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotification(String alertMessage) {
        // Create an intent to open the app when the user taps the notification
        Intent intent = new Intent(this, InputNoiseActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE); // Specify FLAG_IMMUTABLE

        // Build the notification using the custom icon and the provided alert message
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_custom_notification) // Use the custom icon here
                .setContentTitle("Noise Alert")
                .setContentText(alertMessage) // Use the provided alert message
                .setPriority(NotificationCompat.PRIORITY_HIGH) // High priority for noisy alerts
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_name);
            String description = getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH; // High importance for noisy alerts
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}




