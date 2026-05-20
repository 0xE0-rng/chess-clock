package fr.kazalox.android.gameclockdeluxe;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/* JADX INFO: loaded from: classes.dex */
public class Prefs {
    public static final String FIRST_TIME = "FIRST_TIME";
    private static Context sContext;
    private static boolean sFirstTime;
    private static SharedPreferences.OnSharedPreferenceChangeListener sListener;
    private static SharedPreferences sPrefs;

    public static void init(Context context) {
        sContext = context;
        sPrefs = PreferenceManager.getDefaultSharedPreferences(sContext);
        sFirstTime = getBoolean(R.string.pref_key_first_use, true);
        if (sFirstTime) {
            SharedPreferences.Editor editor = sPrefs.edit();
            putInt(R.string.pref_key_mode_id, 0);
            putIntRes(R.string.pref_key_classic_time_1, R.integer.pref_key_classic_time_default);
            putIntRes(R.string.pref_key_classic_time_2, R.integer.pref_key_classic_time_default);
            putIntRes(R.string.pref_key_fischer_time_1, R.integer.pref_key_fischer_time_default);
            putIntRes(R.string.pref_key_fischer_time_2, R.integer.pref_key_fischer_time_default);
            putIntRes(R.string.pref_key_fischer_delay, R.integer.pref_key_fischer_delay_default);
            putIntRes(R.string.pref_key_bronstein_time_1, R.integer.pref_key_bronstein_time_default);
            putIntRes(R.string.pref_key_bronstein_time_2, R.integer.pref_key_bronstein_time_default);
            putIntRes(R.string.pref_key_bronstein_delay, R.integer.pref_key_bronstein_delay_default);
            putIntRes(R.string.pref_key_simple_delay_time_1, R.integer.pref_key_simple_delay_time_default);
            putIntRes(R.string.pref_key_simple_delay_time_2, R.integer.pref_key_simple_delay_time_default);
            putIntRes(R.string.pref_key_simple_delay_delay, R.integer.pref_key_simple_delay_delay_default);
            putIntRes(R.string.pref_key_hour_glass_time_1, R.integer.pref_key_hour_glass_time_default);
            putIntRes(R.string.pref_key_hour_glass_time_2, R.integer.pref_key_hour_glass_time_default);
            putIntRes(R.string.pref_key_time_per_move_time_1, R.integer.pref_key_time_per_move_time_default);
            putIntRes(R.string.pref_key_time_per_move_time_2, R.integer.pref_key_time_per_move_time_default);
            putIntRes(R.string.pref_key_fide_period_1, R.integer.pref_key_fide_period_1_default);
            putIntRes(R.string.pref_key_fide_period_2, R.integer.pref_key_fide_period_2_default);
            putIntRes(R.string.pref_key_fide_nb_moves, R.integer.pref_key_fide_nb_moves_default);
            putIntRes(R.string.pref_key_fide_delay, R.integer.pref_key_fide_delay_default);
            putBoolean(R.string.pref_key_negative_countdown, R.bool.pref_default_negative_countdown);
            putBoolean(R.string.pref_key_moves_counter, R.bool.pref_default_moves_counter);
            putString(R.string.pref_key_theme, "3");
            putBoolean(R.string.pref_key_show_delay_elapsed, R.bool.pref_default_show_increment_elapsed);
            putBoolean(R.string.pref_key_sfx, R.bool.pref_default_sfx);
            putBoolean(R.string.pref_key_vibe, R.bool.pref_default_vibe);
            putBoolean(R.string.pref_key_first_use, false);
            editor.commit();
        }
    }

    public static void setListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sListener = listener;
        sPrefs.registerOnSharedPreferenceChangeListener(sListener);
    }

    public static boolean getBoolean(String key) {
        return sPrefs.getBoolean(key, false);
    }

    public static boolean getBoolean(int keyId) {
        return sPrefs.getBoolean(sContext.getString(keyId), false);
    }

    public static boolean getBoolean(int keyId, boolean def) {
        return sPrefs.getBoolean(sContext.getString(keyId), def);
    }

    public static boolean getBoolean(String key, boolean def) {
        return sPrefs.getBoolean(key, def);
    }

    public static void putBoolean(int keyId, int resId) {
        putBoolean(keyId, sContext.getResources().getBoolean(resId));
    }

    public static void putBoolean(int keyId, boolean value) {
        putBoolean(sContext.getString(keyId), value);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sPrefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static int getInt(String key) {
        return sPrefs.getInt(key, -1);
    }

    public static int getInt(int keyId) {
        return sPrefs.getInt(sContext.getString(keyId), -1);
    }

    public static int getInt(int keyId, int def) {
        return sPrefs.getInt(sContext.getString(keyId), def);
    }

    public static void putIntRes(int keyId, int resId) {
        putInt(keyId, sContext.getResources().getInteger(resId));
    }

    public static void putInt(int keyId, int value) {
        putInt(sContext.getString(keyId), value);
    }

    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = sPrefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static long getLong(String key) {
        return sPrefs.getLong(key, 0L);
    }

    public static long getLong(String key, long def) {
        return sPrefs.getLong(key, def);
    }

    public static long getLong(int keyId, long def) {
        return sPrefs.getLong(sContext.getString(keyId), def);
    }

    public static void putLong(int keyId, long value) {
        putLong(sContext.getString(keyId), value);
    }

    public static void putLong(String key, long value) {
        SharedPreferences.Editor editor = sPrefs.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static String getString(String key) {
        return sPrefs.getString(key, "null");
    }

    public static String getString(int keyId, String def) {
        return getString(sContext.getString(keyId), def);
    }

    public static String getString(int keyId, int resId) {
        return getString(keyId, sContext.getString(resId));
    }

    public static String getString(String key, String def) {
        return sPrefs.getString(key, def);
    }

    public static void putString(int keyId, String value) {
        putString(sContext.getString(keyId), value);
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = sPrefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static SharedPreferences getPrefs() {
        return sPrefs;
    }
}
