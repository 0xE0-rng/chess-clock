package fr.kazalox.android.gameclockdeluxe.activities;

import android.content.Intent;
import android.os.Bundle;
import fr.kazalox.android.gameclockdeluxe.App;
import fr.kazalox.android.gameclockdeluxe.C;
import fr.kazalox.android.gameclockdeluxe.Prefs;
import fr.kazalox.android.gameclockdeluxe.R;
import fr.kazalox.android.gameclockdeluxe.data.Mode;
import fr.kazalox.android.gameclockdeluxe.dialogs.ModeListDialog;
import fr.kazalox.android.gameclockdeluxe.dialogs.NbMovesDialog;
import fr.kazalox.android.gameclockdeluxe.dialogs.SimpleDialog;
import fr.kazalox.android.gameclockdeluxe.dialogs.TimePickerDialog;
import fr.kazalox.android.gameclockdeluxe.fragments.SettingsFragment;
import fr.kazalox.android.gameclockdeluxe.helpers.FragmentHelper;

/* JADX INFO: loaded from: classes.dex */
public class SettingsActivity extends BaseActivity implements SettingsFragment.SettingsFragmentListener, ModeListDialog.ModeListDialogListener, TimePickerDialog.TimePickerDialogListener, NbMovesDialog.NbMovesDialogListener {
    private Mode mMode;

    @Override // fr.kazalox.android.gameclockdeluxe.activities.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_one_fragment);
        int modeId = Prefs.getInt(R.string.pref_key_mode_id);
        this.mMode = Mode.getById(modeId);
        this.mMode.fetchPreferences();
        if (savedInstanceState == null) {
            Bundle b = new Bundle();
            b.putSerializable(C.ARG_MODE, this.mMode);
            FragmentHelper.add(this, SettingsFragment.class, R.id.layout_container, b);
        }
    }

    @Override // fr.kazalox.android.gameclockdeluxe.fragments.SettingsFragment.SettingsFragmentListener
    public void onSettingsClock1Click() {
        if (Prefs.getBoolean(R.string.pref_key_dialog_show_clocks_setting_tip, true)) {
            Prefs.putBoolean(R.string.pref_key_dialog_show_clocks_setting_tip, false);
            SimpleDialog d = SimpleDialog.newInstance(getString(R.string.dialog_title_set_both_clock_at_the_same_time), getString(R.string.dialog_message_set_both_clock_at_the_same_time), getString(R.string.dialog_button_ok));
            d.show(getSupportFragmentManager(), (String) null);
        }
        Bundle b = new Bundle();
        b.putInt(C.ARG_NB_DIGITS, 5);
        b.putInt(C.ARG_TIME, this.mMode.getTime1());
        b.putString("ARG_TITLE", getString(R.string.settings_clock1));
        FragmentHelper.replace(this, TimePickerDialog.class, R.id.layout_container, b);
        b.putInt(C.ARG_PREF_KEY, this.mMode.getPrefTime1());
    }

    @Override // fr.kazalox.android.gameclockdeluxe.fragments.SettingsFragment.SettingsFragmentListener
    public void onSettingsClock2Click() {
        Bundle b = new Bundle();
        b.putInt(C.ARG_NB_DIGITS, 5);
        b.putInt(C.ARG_TIME, this.mMode.getTime2());
        b.putInt(C.ARG_PREF_KEY, this.mMode.getPrefTime2());
        b.putString("ARG_TITLE", getString(R.string.settings_clock2));
        FragmentHelper.replace(this, TimePickerDialog.class, R.id.layout_container, b);
    }

    @Override // fr.kazalox.android.gameclockdeluxe.fragments.SettingsFragment.SettingsFragmentListener
    public void onSettingsIncrement() {
        Bundle b = new Bundle();
        b.putInt(C.ARG_NB_DIGITS, 4);
        b.putInt(C.ARG_TIME, this.mMode.getDelay());
        b.putInt(C.ARG_PREF_KEY, this.mMode.getPrefDelay());
        b.putString("ARG_TITLE", getString(R.string.settings_delay));
        FragmentHelper.replace(this, TimePickerDialog.class, R.id.layout_container, b);
    }

    @Override // fr.kazalox.android.gameclockdeluxe.fragments.SettingsFragment.SettingsFragmentListener
    public void onSettingsNbMoves() {
        NbMovesDialog f = new NbMovesDialog();
        Bundle b = new Bundle();
        b.putInt(C.ARG_NB_MOVES, this.mMode.getNbMoves());
        f.setArguments(b);
        f.show(getSupportFragmentManager(), NbMovesDialog.class.getSimpleName());
    }

    @Override // fr.kazalox.android.gameclockdeluxe.fragments.SettingsFragment.SettingsFragmentListener
    public void onSettingsPeriod2() {
        Bundle b = new Bundle();
        b.putInt(C.ARG_NB_DIGITS, 5);
        b.putInt(C.ARG_TIME, this.mMode.getPeriod2());
        b.putInt(C.ARG_PREF_KEY, this.mMode.getPrefPeriod2());
        b.putString("ARG_TITLE", getString(R.string.settings_period2));
        FragmentHelper.replace(this, TimePickerDialog.class, R.id.layout_container, b);
    }

    @Override // fr.kazalox.android.gameclockdeluxe.fragments.SettingsFragment.SettingsFragmentListener
    public void onSettingsIncrement2() {
        Bundle b = new Bundle();
        b.putInt(C.ARG_NB_DIGITS, 4);
        b.putInt(C.ARG_TIME, this.mMode.getDelay());
        b.putInt(C.ARG_PREF_KEY, this.mMode.getPrefDelay());
        b.putString("ARG_TITLE", getString(R.string.settings_delay));
        FragmentHelper.replace(this, TimePickerDialog.class, R.id.layout_container, b);
    }

    @Override // fr.kazalox.android.gameclockdeluxe.fragments.SettingsFragment.SettingsFragmentListener
    public void onSettingsModeClick() {
        ModeListDialog f = new ModeListDialog();
        f.show(getSupportFragmentManager(), ModeListDialog.class.getSimpleName());
    }

    @Override // fr.kazalox.android.gameclockdeluxe.dialogs.ModeListDialog.ModeListDialogListener
    public void onModeListClick(Mode mode) {
        mode.fetchPreferences();
        this.mMode = mode;
        if (!mode.isFree() && !App.hasProVersion()) {
            Intent i = new Intent(this, (Class<?>) InAppActivity.class);
            i.putExtra(C.ARG_FEATURE_NOT_AVAILABLE, true);
            startActivity(i);
        } else {
            Prefs.putInt(R.string.pref_key_mode_id, this.mMode.getId());
            SettingsFragment f = (SettingsFragment) getSupportFragmentManager().findFragmentByTag(SettingsFragment.class.getSimpleName());
            if (f != null) {
                f.updateMode(this.mMode);
            }
        }
    }

    @Override // fr.kazalox.android.gameclockdeluxe.dialogs.TimePickerDialog.TimePickerDialogListener
    public void onTimePickerOk(int time, int prefKey) {
        getSupportFragmentManager().popBackStack();
        Prefs.putInt(prefKey, time);
        if (prefKey == this.mMode.getPrefTime1()) {
            Prefs.putInt(this.mMode.getPrefTime2(), time);
        }
        this.mMode.fetchPreferences();
        SettingsFragment f = (SettingsFragment) getSupportFragmentManager().findFragmentByTag(SettingsFragment.class.getSimpleName());
        if (f != null) {
            f.updateViews();
        }
    }

    @Override // fr.kazalox.android.gameclockdeluxe.dialogs.NbMovesDialog.NbMovesDialogListener
    public void onNbMovesOk(int nbMoves) {
        Prefs.putInt(R.string.pref_key_fide_nb_moves, nbMoves);
        this.mMode.fetchPreferences();
        SettingsFragment f = (SettingsFragment) getSupportFragmentManager().findFragmentByTag(SettingsFragment.class.getSimpleName());
        if (f != null) {
            f.updateViews();
        }
    }
}
