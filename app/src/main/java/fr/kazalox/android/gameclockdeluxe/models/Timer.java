package fr.kazalox.android.gameclockdeluxe.models;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import fr.kazalox.android.gameclockdeluxe.Prefs;
import fr.kazalox.android.gameclockdeluxe.R;
import fr.kazalox.android.gameclockdeluxe.Sound;
import fr.kazalox.android.gameclockdeluxe.Vibe;
import fr.kazalox.android.gameclockdeluxe.data.Mode;
import java.io.Serializable;
import java.util.Observable;

/* JADX INFO: loaded from: classes.dex */
public class Timer extends Observable implements Serializable {
    public static final int DELAY_BETWEEN_UPDATES = 100;
    private static int sSerial = 0;
    private static final long serialVersionUID = -3062398709382874814L;
    private long mBronsteinElapsed;
    private transient ClockUpdater mClockUpdater;
    private boolean mCountdownStarted;
    private transient CountdownUpdater mCountdownUpdater;
    private long mDelay;
    private transient Handler mHandler;
    private int mId;
    private long mLastMoveDuration;
    private long mLastTs;
    private TimerListener mListener;
    private Mode mMode;
    private int mMovesCount;
    private int mNbMovesFide;
    private boolean mNegativeCountdown;
    private long mOpponentLastMoveDuration;
    private boolean mPeriod2Added;
    private long mRemaining;
    private boolean mRemainingNegative;
    private boolean mRunning;
    private int mSerial;
    private long mSimpleDelayRemaining;
    private State mState;
    private long mTimeInit;

    public enum State {
        PAUSED,
        RUNNING,
        STOPPED,
        GAME_OVER
    }

    public interface TimerListener {
        void onTimerExpired(Timer timer);
    }

    static /* synthetic */ long access$314(Timer x0, long x1) {
        long j = x0.mRemaining + x1;
        x0.mRemaining = j;
        return j;
    }

    static /* synthetic */ long access$322(Timer x0, long x1) {
        long j = x0.mRemaining - x1;
        x0.mRemaining = j;
        return j;
    }

    static /* synthetic */ long access$414(Timer x0, long x1) {
        long j = x0.mLastMoveDuration + x1;
        x0.mLastMoveDuration = j;
        return j;
    }

    static /* synthetic */ long access$622(Timer x0, long x1) {
        long j = x0.mSimpleDelayRemaining - x1;
        x0.mSimpleDelayRemaining = j;
        return j;
    }

    public Timer(TimerListener listener, Context context, int id) {
        Log.d("ChessClock", "Timer - Timer - ");
        this.mListener = listener;
        this.mId = id;
        int i = sSerial;
        sSerial = i + 1;
        this.mSerial = i;
        updateConfig();
        init(context);
        reset();
    }

    public long getBronsteinElapsed() {
        return this.mBronsteinElapsed;
    }

    public long getSimpleDelayRemaining() {
        return this.mSimpleDelayRemaining;
    }

    public Mode getMode() {
        return this.mMode;
    }

    public void setMode(Mode mode) {
        this.mMode = mode;
    }

    public long getLastMoveDuration() {
        return this.mLastMoveDuration;
    }

    public void setOpponentLastMoveDuration(long opponentLastMoveDuration) {
        this.mOpponentLastMoveDuration = opponentLastMoveDuration;
    }

    public void init(Context context) {
        this.mHandler = new Handler();
        this.mClockUpdater = new ClockUpdater();
        this.mCountdownUpdater = new CountdownUpdater();
    }

    public void updateConfig() {
        this.mMode = Mode.getById(Prefs.getInt(R.string.pref_key_mode_id));
        int time1 = this.mMode.getTime1();
        int time2 = this.mMode.getTime2();
        if (this.mId != 1) {
            time1 = time2;
        }
        this.mTimeInit = time1 * 1000;
        this.mDelay = this.mMode.getDelay() * 1000;
        this.mNegativeCountdown = Prefs.getBoolean(R.string.pref_key_negative_countdown);
        if (this.mMode.getId() == 6) {
            this.mNbMovesFide = this.mMode.getNbMoves();
        }
    }

    public void reset() {
        this.mRemaining = this.mTimeInit;
        this.mCountdownStarted = false;
        this.mLastTs = 0L;
        this.mMovesCount = 0;
        this.mRunning = false;
        this.mSimpleDelayRemaining = this.mDelay;
        this.mBronsteinElapsed = 0L;
        this.mLastMoveDuration = 0L;
        this.mRemainingNegative = false;
        this.mOpponentLastMoveDuration = 0L;
        this.mPeriod2Added = false;
        switchState(State.PAUSED);
    }

    public void start() {
        switchState(State.RUNNING);
    }

    public void stop() {
        switchState(State.STOPPED);
    }

    public void pause() {
        switchState(State.PAUSED);
    }

    public void gameOver() {
        switchState(State.GAME_OVER);
    }

    public void resume() {
        updateObservers(this.mState);
    }

    public void switchState(State newState) {
        if (newState == State.RUNNING) {
            Sound.play(2);
            Vibe.play(1);
            this.mLastTs = SystemClock.elapsedRealtime();
            this.mHandler.postDelayed(this.mClockUpdater, 100L);
            this.mCountdownStarted = false;
            if (!this.mRunning && this.mMode.getId() == 4) {
                this.mRemaining += this.mOpponentLastMoveDuration;
            }
            this.mRunning = true;
        }
        if (newState == State.STOPPED) {
            if (this.mRunning) {
                this.mLastMoveDuration = 0L;
                if (this.mMode.getId() == 1 || this.mMode.getId() == 6) {
                    this.mRemaining += this.mDelay;
                } else if (this.mMode.getId() == 2) {
                    this.mRemaining += Math.min(this.mBronsteinElapsed, this.mDelay);
                    this.mBronsteinElapsed = 0L;
                } else if (this.mMode.getId() == 3) {
                    this.mSimpleDelayRemaining = this.mDelay;
                } else if (this.mMode.getId() == 5) {
                    this.mRemaining = this.mTimeInit;
                }
                this.mMovesCount++;
                if (this.mMovesCount >= this.mNbMovesFide && !this.mPeriod2Added) {
                    this.mRemaining += (long) (this.mMode.getPeriod2() * 1000);
                    this.mPeriod2Added = true;
                }
            }
            this.mRunning = false;
        }
        if (newState == State.STOPPED || newState == State.PAUSED) {
            this.mHandler.removeCallbacks(this.mClockUpdater);
            this.mHandler.removeCallbacks(this.mCountdownUpdater);
        }
        this.mState = newState;
        updateObservers(this.mState);
    }

    public boolean isRunning() {
        return this.mState == State.RUNNING;
    }

    public int getMovesCount() {
        return this.mMovesCount;
    }

    public long getRemaining() {
        return this.mRemaining;
    }

    public void updateObservers() {
        updateObservers(null);
    }

    public void updateObservers(Object data) {
        setChanged();
        notifyObservers(data);
    }

    public State getState() {
        return this.mState;
    }

    private class ClockUpdater implements Runnable {
        private ClockUpdater() {
        }

        @Override // java.lang.Runnable
        public void run() {
            long now = SystemClock.elapsedRealtime();
            long delta = now - Timer.this.mLastTs;
            Timer.this.mLastTs = now;
            Timer.access$322(Timer.this, delta);
            Timer.access$414(Timer.this, delta);
            if (Timer.this.mMode.getId() == 3 && Timer.this.mSimpleDelayRemaining > 0) {
                Timer.access$314(Timer.this, delta);
                Timer.access$622(Timer.this, delta);
            }
            if (Timer.this.mMode.getId() == 2) {
                Timer.this.mBronsteinElapsed = Math.min(Timer.this.mBronsteinElapsed + delta, Timer.this.mDelay);
            }
            if (Timer.this.mRemaining <= 0 && !Timer.this.mNegativeCountdown) {
                Timer.this.mRemaining = 0L;
                Timer.this.mListener.onTimerExpired(Timer.this);
                Sound.play(3);
                Vibe.play(2);
                Timer.this.updateObservers();
                return;
            }
            Timer.this.updateObservers();
            Timer.this.mHandler.postDelayed(this, 100L);
            if (Timer.this.mRemaining < 11000 && Timer.this.mRemaining >= 1000 && !Timer.this.mCountdownStarted) {
                Timer.this.mCountdownStarted = true;
                long millisToSecond = Timer.this.mRemaining % 1000;
                Timer.this.mHandler.postDelayed(Timer.this.mCountdownUpdater, millisToSecond);
            }
            if (!Timer.this.mRemainingNegative && Timer.this.mRemaining < 0) {
                Sound.play(3);
                Vibe.play(2);
                Timer.this.mRemainingNegative = true;
            }
            if (Timer.this.mRemaining > 0) {
                Timer.this.mRemainingNegative = false;
            }
        }
    }

    private class CountdownUpdater implements Runnable {
        private CountdownUpdater() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (Timer.this.mRemaining <= 900 || Timer.this.mRemaining >= 11000) {
                Timer.this.mHandler.removeCallbacks(Timer.this.mCountdownUpdater);
                Timer.this.mCountdownStarted = false;
            } else {
                Sound.play(1);
                Timer.this.mHandler.postDelayed(this, 1000L);
            }
        }
    }
}
