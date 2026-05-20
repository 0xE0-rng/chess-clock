package fr.kazalox.android.gameclockdeluxe.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import fr.kazalox.android.gameclockdeluxe.App;
import fr.kazalox.android.gameclockdeluxe.C;
import fr.kazalox.android.gameclockdeluxe.Prefs;
import fr.kazalox.android.gameclockdeluxe.R;
import fr.kazalox.android.gameclockdeluxe.dialogs.SimpleDialog;
import fr.kazalox.android.gameclockdeluxe.fragments.AnalogClockFragment;
import fr.kazalox.android.gameclockdeluxe.fragments.ClockFragment;
import fr.kazalox.android.gameclockdeluxe.fragments.DigitalClockFragment;
import fr.kazalox.android.gameclockdeluxe.helpers.FragmentHelper;
import fr.kazalox.android.gameclockdeluxe.inapp.IabHelper;
import fr.kazalox.android.gameclockdeluxe.inapp.IabResult;
import fr.kazalox.android.gameclockdeluxe.inapp.Inventory;
import fr.kazalox.android.gameclockdeluxe.inapp.Purchase;
import fr.kazalox.android.gameclockdeluxe.managers.ClockManager;
import fr.kazalox.android.gameclockdeluxe.models.Timer;
import fr.kazalox.android.gameclockdeluxe.utils.MetricUtils;

/* JADX INFO: loaded from: classes.dex */
@TargetApi(11)
public class ChessClockActivity extends BaseActivity implements DigitalClockFragment.DigitalClockFragmentListener, SimpleDialog.SimpleDialogListener, ClockManager.ClockManagerListener, AnalogClockFragment.AnalogClockFragmentListener, IabHelper.OnIabSetupFinishedListener, IabHelper.QueryInventoryFinishedListener {
    private static final String KEY_CHECK_UPGRADE_DONE = "KEY_CHECK_UPGRADE_DONE";
    private static final String KEY_CLOCK_MANAGER = "KEY_CLOCK_MANAGER";
    private static final String KEY_THEME_ID = "KEY_THEME_ID";
    private static final String TAG_CLOCK_1 = "TAG_CLOCK_1";
    private static final String TAG_CLOCK_2 = "TAG_CLOCK_2";
    private boolean mCheckFullUpgradeDone;
    private ClockManager mClockManager;
    private View mDecorView;
    private float mDistance;
    private boolean mFullScreen;
    private IabHelper mHelper;
    private boolean mShowUpgradeDialogLater;
    private int mSwipeThreshold;
    private PointF mOldPoint = new PointF();
    private PointF mNewPoint = new PointF();
    private int mCurrentTheme = -1;

    @Override // fr.kazalox.android.gameclockdeluxe.activities.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        supportRequestWindowFeature(9);
        this.mDecorView = getWindow().getDecorView();
        hideSystemUI();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#88000000")));
        computeSwipeThreshold();
        setContentView(R.layout.activity_two_clocks);
        if (savedInstanceState == null) {
            this.mCurrentTheme = -1;
            this.mClockManager = new ClockManager(this);
        } else {
            this.mCheckFullUpgradeDone = savedInstanceState.getBoolean(KEY_CHECK_UPGRADE_DONE);
            this.mCurrentTheme = savedInstanceState.getInt(KEY_THEME_ID);
            this.mClockManager = (ClockManager) savedInstanceState.getSerializable(KEY_CLOCK_MANAGER);
            this.mClockManager.reInit(this);
        }
        if (!this.mCheckFullUpgradeDone && !App.isPaidVersion()) {
            initInApp();
        }
        checkDialogToShow();
        setVolumeControlStream(3);
    }

    private void hideSystemUI() {
        if (!this.mFullScreen) {
            this.mFullScreen = true;
            if (Build.VERSION.SDK_INT >= 19) {
                this.mDecorView.setSystemUiVisibility(5894);
            } else {
                getSupportActionBar().hide();
            }
        }
    }

    private void showSystemUI() {
        if (this.mFullScreen) {
            this.mFullScreen = false;
            if (Build.VERSION.SDK_INT >= 19) {
                this.mDecorView.setSystemUiVisibility(1792);
            } else {
                getSupportActionBar().show();
            }
        }
    }

    @Override // fr.kazalox.android.gameclockdeluxe.activities.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        int themeId = Integer.parseInt(Prefs.getString(R.string.pref_key_theme, "0"));
        if (this.mCurrentTheme == -1 || themeId != this.mCurrentTheme) {
            int current = getRequestedOrientation();
            int next = -1;
            if (themeId == 4) {
                next = 0;
            }
            if (current != next) {
                setRequestedOrientation(next);
                int orientation = getResources().getConfiguration().orientation;
                if (orientation != 2) {
                    return;
                }
            }
            this.mCurrentTheme = themeId;
            Class fragmentClass = themeId == 4 ? AnalogClockFragment.class : DigitalClockFragment.class;
            Bundle b1 = new Bundle();
            b1.putInt(C.ARG_CLOCK_ID, 1);
            b1.putInt(C.ARG_CLOCK_STYLE, themeId);
            b1.putSerializable(C.ARG_TIMER, this.mClockManager.getTimer1());
            FragmentHelper.replace(this, fragmentClass, R.id.layout_fragment_container_1, b1, TAG_CLOCK_1);
            Bundle b2 = new Bundle();
            b2.putInt(C.ARG_CLOCK_ID, 2);
            b2.putInt(C.ARG_CLOCK_STYLE, themeId);
            b2.putSerializable(C.ARG_TIMER, this.mClockManager.getTimer2());
            FragmentHelper.replace(this, fragmentClass, R.id.layout_fragment_container_2, b2, TAG_CLOCK_2);
            return;
        }
        ClockFragment clockFragment1 = (ClockFragment) getSupportFragmentManager().findFragmentByTag(TAG_CLOCK_1);
        clockFragment1.updateTimer(this.mClockManager.getTimer1());
        ClockFragment clockFragment2 = (ClockFragment) getSupportFragmentManager().findFragmentByTag(TAG_CLOCK_2);
        clockFragment2.updateTimer(this.mClockManager.getTimer2());
    }

    private void computeSwipeThreshold() {
        this.mSwipeThreshold = (int) MetricUtils.dp2px(this, 150.0f);
    }

    @Override // fr.kazalox.android.gameclockdeluxe.fragments.DigitalClockFragment.DigitalClockFragmentListener, fr.kazalox.android.gameclockdeluxe.fragments.AnalogClockFragment.AnalogClockFragmentListener
    public void onClockClick(Timer timer) {
        this.mClockManager.clickTimer(timer);
    }

    @Override // fr.kazalox.android.gameclockdeluxe.fragments.DigitalClockFragment.DigitalClockFragmentListener, fr.kazalox.android.gameclockdeluxe.fragments.AnalogClockFragment.AnalogClockFragmentListener
    public boolean onClockLongClick() {
        return this.mClockManager.pauseRequested();
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clocks, menu);
        return true;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (e.getAction() == 0) {
            this.mDistance = 0.0f;
            this.mOldPoint.set(e.getRawX(), e.getRawY());
            this.mNewPoint.set(e.getRawX(), e.getRawY());
        } else if (e.getAction() == 2) {
            this.mNewPoint.set(e.getRawX(), e.getRawY());
            this.mDistance += (float) Math.hypot(this.mNewPoint.x - this.mOldPoint.x, this.mNewPoint.y - this.mOldPoint.y);
            this.mOldPoint.set(this.mNewPoint);
            if (this.mDistance > this.mSwipeThreshold) {
                showSystemUI();
                this.mClockManager.pauseRequested();
                return true;
            }
        } else if (e.getAction() == 1 && this.mDistance > this.mSwipeThreshold) {
            return true;
        }
        return super.dispatchTouchEvent(e);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(getClass().getSimpleName(), "onOptionsItemSelected");
        int id = item.getItemId();
        if (id == R.id.action_time) {
            Intent i = new Intent(this, (Class<?>) SettingsActivity.class);
            startActivityForResult(i, 1);
            return true;
        } else if (id == R.id.action_reset) {
            this.mClockManager.reset();
            showToast(getString(R.string.toast_reset));
            return true;
        } else if (id == R.id.action_settings) {
            Intent i2 = new Intent(this, (Class<?>) PrefActivity.class);
            startActivityForResult(i2, 2);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override // fr.kazalox.android.gameclockdeluxe.activities.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        this.mClockManager.quitClock();
        super.onPause();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_CHECK_UPGRADE_DONE, this.mCheckFullUpgradeDone);
        outState.putSerializable(KEY_CLOCK_MANAGER, this.mClockManager);
        outState.putInt(KEY_THEME_ID, this.mCurrentTheme);
    }

    @Override // fr.kazalox.android.gameclockdeluxe.managers.ClockManager.ClockManagerListener
    public void onClockManagerGameOver() {
        showSystemUI();
    }

    @Override // fr.kazalox.android.gameclockdeluxe.managers.ClockManager.ClockManagerListener
    public void onClockManagerPause() {
        showSystemUI();
    }

    @Override // fr.kazalox.android.gameclockdeluxe.managers.ClockManager.ClockManagerListener
    public void onClockManagerStart() {
        hideSystemUI();
    }

    @Override // fr.kazalox.android.gameclockdeluxe.managers.ClockManager.ClockManagerListener
    public void onClockManagerReminder() {
        if (isCommitable()) {
            showUpgradeDialog();
        } else {
            this.mShowUpgradeDialogLater = true;
        }
    }

    private void checkDialogToShow() {
        if (Prefs.getBoolean(R.string.pref_key_dialog_show_tips_and_tricks, true)) {
            Prefs.putBoolean(R.string.pref_key_dialog_show_tips_and_tricks, false);
            showTipsAndTricksDialog();
        }
        if (Prefs.getBoolean(R.string.pref_key_dialog_show_whats_new_1, true)) {
            Prefs.putBoolean(R.string.pref_key_dialog_show_whats_new_1, false);
            showWhatsNewDialog();
        }
        if (Prefs.getBoolean(R.string.pref_key_dialog_show_please_rate, true)) {
            long now = System.currentTimeMillis();
            long lastTs = Prefs.getLong(R.string.pref_key_dialog_please_rate_last_ts, 0L);
            if (lastTs == 0) {
                Prefs.putLong(R.string.pref_key_dialog_please_rate_last_ts, now);
                return;
            }
            long delta = now - lastTs;
            if (delta > 345600000) {
                Prefs.putLong(R.string.pref_key_dialog_please_rate_last_ts, now);
                showRateMeDialog();
            }
        }
    }

    @Override // fr.kazalox.android.gameclockdeluxe.activities.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPostResume() {
        super.onPostResume();
        this.mClockManager.resume();
        if (this.mShowUpgradeDialogLater) {
            showUpgradeDialog();
            this.mShowUpgradeDialogLater = false;
        }
    }

    private void showRateMeDialog() {
        SimpleDialog d = SimpleDialog.newInstance(getString(R.string.dialog_title_rate_me), getString(R.string.dialog_message_rate_me), getString(R.string.dialog_button_rate_me), getString(R.string.dialog_button_never), getString(R.string.dialog_button_later));
        d.show(getSupportFragmentManager(), C.DIALOG_TAG_RATE_ME);
    }

    public void showUpgradeDialog() {
        if (!App.hasProVersion()) {
            Intent i = new Intent(this, (Class<?>) InAppActivity.class);
            startActivityForResult(i, 1);
        }
    }

    public void showQuitDialog() {
        SimpleDialog d = SimpleDialog.newInstance(getString(R.string.dialog_title_exit), getString(R.string.dialog_message_exit), getString(R.string.dialog_button_ok), getString(R.string.dialog_button_cancel));
        d.show(getSupportFragmentManager(), C.DIALOG_TAG_QUIT);
    }

    public void showTipsAndTricksDialog() {
        SimpleDialog d = SimpleDialog.newInstance(getString(R.string.dialog_title_tips_and_tricks), getString(R.string.dialog_message_tips_and_tricks), getString(R.string.dialog_button_ok));
        d.show(getSupportFragmentManager(), C.DIALOG_TAG_TIPS_AND_TRICKS);
    }

    public void showWhatsNewDialog() {
        SimpleDialog d = SimpleDialog.newInstance(getString(R.string.dialog_title_whats_new), getString(R.string.dialog_message_whats_new), getString(R.string.dialog_button_ok));
        d.show(getSupportFragmentManager(), C.DIALOG_TAG_WHATS_NEW);
    }

    @Override // fr.kazalox.android.gameclockdeluxe.dialogs.SimpleDialog.SimpleDialogListener
    public void onButton1Click(DialogFragment dialog) {
        if (dialog.getTag().equals(C.DIALOG_TAG_QUIT)) {
            finish();
        } else if (!dialog.getTag().equals(C.DIALOG_TAG_UPGRADE) && dialog.getTag().equals(C.DIALOG_TAG_RATE_ME)) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.url_market))));
            Prefs.putBoolean(R.string.pref_key_dialog_show_please_rate, false);
        }
    }

    @Override // fr.kazalox.android.gameclockdeluxe.activities.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        this.mClockManager.quitClock();
        showQuitDialog();
    }

    @Override // fr.kazalox.android.gameclockdeluxe.dialogs.SimpleDialog.SimpleDialogListener
    public void onButton2Click(DialogFragment dialog) {
        if (dialog.getTag().equals(C.DIALOG_TAG_RATE_ME)) {
            Prefs.putBoolean(R.string.pref_key_dialog_show_please_rate, false);
        }
    }

    @Override // fr.kazalox.android.gameclockdeluxe.dialogs.SimpleDialog.SimpleDialogListener
    public void onButton3Click(DialogFragment dialog) {
    }

    private void initInApp() {
        Log.d("ChessClock", "Creating IAB helper.");
        this.mHelper = new IabHelper(this, C.SETTINGS_ACTIVITY + C.LOG_PREFIX + C.TEST + C.TEST_2);
        this.mHelper.startSetup(this);
    }

    @Override // fr.kazalox.android.gameclockdeluxe.inapp.IabHelper.OnIabSetupFinishedListener
    public void onIabSetupFinished(IabResult result) {
        Log.d("ChessClock", "Setup finished.");
        if (!result.isSuccess()) {
            Log.d("ChessClock", "SplashActivity - onIabSetupFinished - setup error " + result);
        } else if (this.mHelper != null) {
            Log.d("ChessClock", "Setup successful. Querying inventory.");
            this.mHelper.queryInventoryAsync(this);
        }
    }

    @Override // fr.kazalox.android.gameclockdeluxe.inapp.IabHelper.QueryInventoryFinishedListener
    public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
        Log.d("ChessClock", "Query inventory finished.");
        if (this.mHelper == null || result.isFailure()) {
            Log.d("ChessClock", "Failed to query inventory: " + result);
            return;
        }
        Log.d("ChessClock", "Query inventory was successful.");
        Purchase fullUpgradePurchase = inventory.getPurchase(C.SKU_FULL_UPGRADE);
        Log.d("ChessClock", "Query inventory was successful. fullUpgradePurchase : " + fullUpgradePurchase);
        App.setFullUpgrade(fullUpgradePurchase != null);
        this.mCheckFullUpgradeDone = true;
    }

    @Override // fr.kazalox.android.gameclockdeluxe.activities.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        Log.d("ChessClock", "Destroying helper.");
        if (this.mHelper != null) {
            this.mHelper.dispose();
            this.mHelper = null;
        }
    }
}
