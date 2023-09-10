package my.edu.utar.noiseapp;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class MyApplication extends Application {
    private int currentTheme;

    @Override
    public void onCreate() {
        super.onCreate();
        // Set the default night mode based on the current theme
        int currentTheme = getCurrentTheme();
        if (currentTheme == R.style.AppTheme_Light) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    public int getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(int theme) {
        currentTheme = theme;
    }
}
