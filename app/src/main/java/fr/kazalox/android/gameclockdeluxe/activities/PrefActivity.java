package fr.kazalox.android.gameclockdeluxe.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import fr.kazalox.android.gameclockdeluxe.App;
import fr.kazalox.android.gameclockdeluxe.C;
import fr.kazalox.android.gameclockdeluxe.Prefs;
import fr.kazalox.android.gameclockdeluxe.R;

/* JADX INFO: loaded from: classes.dex */
public class PrefActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener {
    ListPreference mClockTheme;
    String[] mClockThemeArray;
    CheckBoxPreference mMovesCounter;
    CheckBoxPreference mShowDelayElapsed;

    @Override // android.preference.PreferenceActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        Log.d(getClass().getSimpleName(), "onCreate");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        addPreferencesFromResource(R.xml.preferences);
        this.mClockThemeArray = getResources().getStringArray(R.array.themes);
        this.mClockTheme = (ListPreference) findPreference(getString(R.string.pref_key_theme));
        this.mClockTheme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: fr.kazalox.android.gameclockdeluxe.activities.PrefActivity.1
            @Override // android.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object o) {
                PrefActivity.this.updateClockTheme(Integer.parseInt(o.toString()));
                return true;
            }
        });
        this.mMovesCounter = (CheckBoxPreference) findPreference(getString(R.string.pref_key_moves_counter));
        this.mMovesCounter.setOnPreferenceClickListener(this);
        this.mShowDelayElapsed = (CheckBoxPreference) findPreference(getString(R.string.pref_key_show_delay_elapsed));
        this.mShowDelayElapsed.setOnPreferenceClickListener(this);
        checkVibrator();
        updateClockTheme(-1);
    }

    private void checkVibrator() {
        PreferenceScreen screen = getPreferenceScreen();
        Vibrator mVibrator = (Vibrator) getSystemService("vibrator");
        CheckBoxPreference mVibe = (CheckBoxPreference) findPreference(getString(R.string.pref_key_vibe));
        if (Build.VERSION.SDK_INT >= 11) {
            if (mVibrator == null || !mVibrator.hasVibrator()) {
                screen.removePreference(mVibe);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateClockTheme(int themeId) {
        if (themeId < 0) {
            String sThemeId = Prefs.getString(R.string.pref_key_theme, "0");
            themeId = Integer.parseInt(sThemeId);
        }
        String theme = this.mClockThemeArray[themeId];
        Log.d(getClass().getSimpleName(), "onPreferenceChange - new theme : " + theme);
        this.mClockTheme.setSummary(theme);
    }

    @Override // android.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        if (!App.hasProVersion()) {
            ((CheckBoxPreference) preference).setChecked(false);
            Intent i = new Intent(this, (Class<?>) InAppActivity.class);
            i.putExtra(C.ARG_FEATURE_NOT_AVAILABLE, true);
            startActivityForResult(i, 1);
        }
        return true;
    }
}
