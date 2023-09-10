package my.edu.utar.noiseapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

public class LanguageSelectionActivity extends BaseActivity {

    private RadioGroup languageRadioGroup;
    private Button applyLanguageButton;
    private RadioButton englishRadioButton;
    private RadioButton frenchRadioButton;
    private RadioButton spanishRadioButton;
    private RadioButton chineseRadioButton;
    private RadioButton japaneseRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeManager.getCurrentTheme());
        super.onCreate(savedInstanceState);
        // Retrieve the selected language from SharedPreferences
        String selectedLanguage = getUserSelectedLanguage();

        // Use LocaleHelper to set the locale based on the selected language
        Context updatedContext = LocaleHelper.setLocale(this, selectedLanguage);

        // Set the updated context for this activity
        setContentView(R.layout.activity_language_selection);
        Log.d("LanguageDebug", "Selected Language: " + selectedLanguage);

        // Initialize the RadioButtons
        englishRadioButton = findViewById(R.id.englishRadioButton);
        frenchRadioButton = findViewById(R.id.frenchRadioButton);
        spanishRadioButton = findViewById(R.id.spanishRadioButton);
        chineseRadioButton = findViewById(R.id.chineseRadioButton);
        japaneseRadioButton = findViewById(R.id.japaneseRadioButton);



        languageRadioGroup = findViewById(R.id.languageRadioGroup);
        applyLanguageButton = findViewById(R.id.applyLanguageButton);

        // Set text for UI elements based on the selected language
        applyLanguageButton.setText(R.string.apply_language);
        englishRadioButton.setText(R.string.english_language);
        frenchRadioButton.setText(R.string.french_language);
        spanishRadioButton.setText(R.string.spanish_language);
        chineseRadioButton.setText(R.string.chinese_language);
        japaneseRadioButton.setText(R.string.japanese_language);


        applyLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedRadioButtonId = languageRadioGroup.getCheckedRadioButtonId();

                if (selectedRadioButtonId == R.id.englishRadioButton) {
                    setAppLanguage("en"); // English
                } else if (selectedRadioButtonId == R.id.frenchRadioButton) {
                    setAppLanguage("fr"); // French
                } else if (selectedRadioButtonId == R.id.spanishRadioButton) {
                    setAppLanguage("es"); // Spanish
                } else if (selectedRadioButtonId == R.id.chineseRadioButton) {
                    setAppLanguage("cn"); // Chinese
                } else if (selectedRadioButtonId == R.id.japaneseRadioButton) {
                    setAppLanguage("jp"); // Japanese
                }

                // Restart the app to apply the new language
                restartApp();
            }
        });
    }
    private String getUserSelectedLanguage() {
        // Retrieve the selected language from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        return preferences.getString("selectedLanguage", "en"); // Default to English if not found
    }

    private void setAppLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        // Save the selected language for future use
        SharedPreferences preferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedLanguage", languageCode);
        editor.apply();
    }

    private void restartApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

