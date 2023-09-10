package my.edu.utar.noiseapp;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import java.util.Locale;

public class LocaleHelper {
    public static Context setLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        // Add this in LocaleHelper
        Log.d("LocaleDebug", "Selected Locale: " + locale.toString());

        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLocale(locale);

        return context.createConfigurationContext(configuration);

    }
}
