package my.edu.utar.noiseapp;

public class ThemeManager {
    private static int currentTheme = R.style.Theme_NoiseApp; // Default theme

    public static int getCurrentTheme() {
        return currentTheme;
    }

    public static void setCurrentTheme(int theme) {
        currentTheme = theme;
    }
}

