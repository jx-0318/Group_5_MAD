package my.edu.utar.noiseapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String selectedLanguage = preferences.getString("selectedLanguage", "en"); // Default to English if not found

        super.attachBaseContext(LocaleHelper.setLocale(newBase, selectedLanguage));
    }

    protected void setAppTheme() {
        int currentTheme = ((MyApplication) getApplication()).getCurrentTheme();
        setTheme(currentTheme);
    }

}

