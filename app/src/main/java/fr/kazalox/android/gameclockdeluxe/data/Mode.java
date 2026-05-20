package fr.kazalox.android.gameclockdeluxe.data;

import fr.kazalox.android.gameclockdeluxe.Prefs;
import fr.kazalox.android.gameclockdeluxe.R;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class Mode implements Serializable {
    public static final int BRONSTEIN = 2;
    public static final int CLASSIC = 0;
    public static final int FIDE = 6;
    public static final int FISCHER = 1;
    public static final int HOUR_GLASS = 4;
    public static final int SIMPLE_DELAY = 3;
    public static final int TIME_PER_MOVE = 5;
    public static List<Mode> sModes = new ArrayList();
    private int mDelay;
    private int mDescription;
    private boolean mFree;
    private int mId;
    private int mName;
    private int mNbMoves;
    private int mPeriod2;
    private int mPrefDelay;
    private int mPrefNbMoves;
    private int mPrefPeriod2;
    private int mPrefTime1;
    private int mPrefTime2;
    private int mTime1;
    private int mTime2;

    static {
        sModes.add(new Mode(0, R.string.mode_name_classic, R.string.mode_help_classic, true));
        sModes.add(new Mode(1, R.string.mode_name_fischer, R.string.mode_help_fischer, true));
        sModes.add(new Mode(2, R.string.mode_name_bronstein, R.string.mode_help_bronstein, true));
        sModes.add(new Mode(3, R.string.mode_name_simple_delay, R.string.mode_help_simple_delay, true));
        sModes.add(new Mode(4, R.string.mode_name_hour_glass, R.string.mode_help_hour_glass, false));
        sModes.add(new Mode(5, R.string.mode_name_time_per_move, R.string.mode_help_time_per_move, false));
        sModes.add(new Mode(6, R.string.mode_name_meta_mode, R.string.mode_help_fide_mode, false));
    }

    public Mode(int id, int name, int description, boolean free) {
        this.mId = id;
        this.mName = name;
        this.mDescription = description;
        this.mFree = free;
    }

    public static Mode getById(int id) {
        Mode mode = null;
        Iterator<Mode> it = sModes.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Mode m = it.next();
            if (m.getId() == id) {
                mode = m;
                break;
            }
        }
        mode.fetchPreferences();
        return mode;
    }

    public int getPrefTime1() {
        return this.mPrefTime1;
    }

    public int getPrefTime2() {
        return this.mPrefTime2;
    }

    public int getPrefDelay() {
        return this.mPrefDelay;
    }

    public int getPrefPeriod2() {
        return this.mPrefPeriod2;
    }

    public int getPrefNbMoves() {
        return this.mPrefNbMoves;
    }

    public int getNbMoves() {
        return this.mNbMoves;
    }

    public void fetchPreferences() {
        this.mPrefDelay = 0;
        this.mPrefNbMoves = 0;
        this.mPrefPeriod2 = 0;
        switch (this.mId) {
            case 0:
                this.mPrefTime1 = R.string.pref_key_classic_time_1;
                this.mPrefTime2 = R.string.pref_key_classic_time_2;
                break;
            case 1:
                this.mPrefTime1 = R.string.pref_key_fischer_time_1;
                this.mPrefTime2 = R.string.pref_key_fischer_time_2;
                this.mPrefDelay = R.string.pref_key_fischer_delay;
                break;
            case 2:
                this.mPrefTime1 = R.string.pref_key_bronstein_time_1;
                this.mPrefTime2 = R.string.pref_key_bronstein_time_2;
                this.mPrefDelay = R.string.pref_key_bronstein_delay;
                break;
            case 3:
                this.mPrefTime1 = R.string.pref_key_simple_delay_time_1;
                this.mPrefTime2 = R.string.pref_key_simple_delay_time_2;
                this.mPrefDelay = R.string.pref_key_simple_delay_delay;
                break;
            case 4:
                this.mPrefTime1 = R.string.pref_key_hour_glass_time_1;
                this.mPrefTime2 = R.string.pref_key_hour_glass_time_2;
                break;
            case 5:
                this.mPrefTime1 = R.string.pref_key_time_per_move_time_1;
                this.mPrefTime2 = R.string.pref_key_time_per_move_time_2;
                break;
            case 6:
                this.mPrefTime1 = R.string.pref_key_fide_period_1;
                this.mPrefTime2 = R.string.pref_key_fide_period_1;
                this.mPrefDelay = R.string.pref_key_fide_delay;
                this.mPrefNbMoves = R.string.pref_key_fide_nb_moves;
                this.mPrefPeriod2 = R.string.pref_key_fide_period_2;
                break;
        }
        this.mTime1 = Prefs.getInt(this.mPrefTime1);
        this.mTime2 = Prefs.getInt(this.mPrefTime2);
        this.mNbMoves = this.mPrefNbMoves != 0 ? Prefs.getInt(this.mPrefNbMoves) : 0;
        this.mPeriod2 = this.mPrefPeriod2 != 0 ? Prefs.getInt(this.mPrefPeriod2) : 0;
        this.mDelay = this.mPrefDelay != 0 ? Prefs.getInt(this.mPrefDelay) : 0;
    }

    public int getTime1() {
        return this.mTime1;
    }

    public int getTime2() {
        return this.mTime2;
    }

    public int getDelay() {
        return this.mDelay;
    }

    public int getPeriod2() {
        return this.mPeriod2;
    }

    public boolean hasDelay() {
        return this.mId == 2 || this.mId == 1 || this.mId == 3 || this.mId == 6;
    }

    public int getId() {
        return this.mId;
    }

    public int getName() {
        return this.mName;
    }

    public int getDescription() {
        return this.mDescription;
    }

    public boolean isFree() {
        return this.mFree;
    }

    public String toString() {
        return "Mode{mId=" + this.mId + ", mName=" + this.mName + ", mFree=" + this.mFree + ", mTime1=" + this.mTime1 + ", mTime2=" + this.mTime2 + ", mDelay=" + this.mDelay + ", mPeriod2=" + this.mPeriod2 + ", mNbMoves=" + this.mNbMoves + '}';
    }
}
