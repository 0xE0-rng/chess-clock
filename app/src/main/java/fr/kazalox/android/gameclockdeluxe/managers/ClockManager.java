package fr.kazalox.android.gameclockdeluxe.managers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import fr.kazalox.android.gameclockdeluxe.Prefs;
import fr.kazalox.android.gameclockdeluxe.R;
import fr.kazalox.android.gameclockdeluxe.data.Mode;
import fr.kazalox.android.gameclockdeluxe.models.Pair;
import fr.kazalox.android.gameclockdeluxe.models.Timer;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class ClockManager implements Timer.TimerListener, Serializable, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final long serialVersionUID = -1800988266794412338L;
    private transient Context mContext;
    private boolean mGameStarted;
    private transient ClockManagerListener mListener;
    private Mode mMode;
    private State mState;
    private Pair<Timer> mTimers;

    public interface ClockManagerListener {
        void onClockManagerGameOver();

        void onClockManagerPause();

        void onClockManagerReminder();

        void onClockManagerStart();
    }

    public enum State {
        PAUSED,
        RUNNING,
        STOPPED,
        GAME_OVER
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ClockManager(Activity activity) {
        this.mContext = activity.getApplicationContext();
        this.mListener = (ClockManagerListener) activity;
        Prefs.setListener(this);
        init();
    }

    private void init() {
        this.mTimers = new Pair<>(new Timer(this, this.mContext, 1), new Timer(this, this.mContext, 2));
        pause();
        updateMode();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void reInit(Activity activity) {
        this.mContext = activity.getApplicationContext();
        this.mListener = (ClockManagerListener) activity;
        Prefs.setListener(this);
        this.mTimers.first.init(this.mContext);
        this.mTimers.second.init(this.mContext);
        if (this.mState != State.PAUSED && this.mState != State.GAME_OVER) {
            pause();
        }
        updateMode();
    }

    private void updateMode() {
        this.mMode = Mode.getById(Prefs.getInt(R.string.pref_key_mode_id));
    }

    public void quitClock() {
        if (this.mState != State.PAUSED && this.mState != State.GAME_OVER) {
            pause();
        }
    }

    public void pause() {
        if (this.mState != State.PAUSED) {
            this.mTimers.first.pause();
            this.mTimers.second.pause();
            this.mState = State.PAUSED;
            this.mListener.onClockManagerPause();
        }
    }

    public void reset() {
        if (this.mGameStarted) {
            checkNewUse();
        }
        pause();
        this.mTimers.first.reset();
        this.mTimers.second.reset();
        this.mGameStarted = false;
    }

    public void checkNewUse() {
        int nbUses = Prefs.getInt(R.string.pref_key_nb_uses, 0);
        Log.w("ChessClock", "nbUses: " + nbUses);
        int nbUses2 = nbUses + 1;
        Prefs.putInt(R.string.pref_key_nb_uses, nbUses2);
        Log.w("ChessClock", "nbUses after: " + nbUses2);
        if (nbUses2 % 5 == 0) {
            this.mListener.onClockManagerReminder();
        }
    }

    public void clickTimer(Timer timer) {
        switch (this.mState) {
            case PAUSED:
                this.mGameStarted = true;
                this.mTimers.other(timer).setOpponentLastMoveDuration(timer.getLastMoveDuration());
                this.mTimers.other(timer).start();
                timer.stop();
                start();
                break;
            case RUNNING:
                if (timer.isRunning()) {
                    this.mTimers.other(timer).setOpponentLastMoveDuration(timer.getLastMoveDuration());
                    this.mTimers.other(timer).start();
                    timer.stop();
                }
                break;
        }
    }

    public void start() {
        this.mState = State.RUNNING;
        this.mListener.onClockManagerStart();
    }

    public void resume() {
        this.mTimers.first.resume();
        this.mTimers.second.resume();
    }

    public boolean pauseRequested() {
        if (this.mState == State.PAUSED || this.mState == State.GAME_OVER) {
            return false;
        }
        pause();
        return true;
    }

    public Timer getTimer1() {
        return this.mTimers.first;
    }

    public Timer getTimer2() {
        return this.mTimers.second;
    }

    @Override // fr.kazalox.android.gameclockdeluxe.models.Timer.TimerListener
    public void onTimerExpired(Timer timer) {
        this.mState = State.GAME_OVER;
        this.mTimers.first.gameOver();
        this.mTimers.second.gameOver();
        this.mListener.onClockManagerGameOver();
    }

    @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(this.mContext.getString(this.mMode.getPrefTime1())) || key.equals(this.mContext.getString(this.mMode.getPrefTime2())) || key.equals(this.mContext.getString(R.string.pref_key_mode_id)) || ((this.mMode.getPrefDelay() != 0 && key.equals(this.mContext.getString(this.mMode.getPrefDelay()))) || ((this.mMode.getPrefPeriod2() != 0 && key.equals(this.mContext.getString(this.mMode.getPrefPeriod2()))) || (this.mMode.getPrefNbMoves() != 0 && key.equals(this.mContext.getString(this.mMode.getPrefNbMoves())))))) {
            pause();
            this.mMode.fetchPreferences();
            updateTimers();
            reset();
        }
        if (key.equals(this.mContext.getString(R.string.pref_key_mode_id))) {
            updateMode();
        }
    }

    private void updateTimers() {
        Log.d("ChessClock", "ClockManager - updateTimers - ");
        this.mTimers.first.updateConfig();
        this.mTimers.second.updateConfig();
    }
}
