package fr.kazalox.android.gameclockdeluxe;

import android.content.Context;
import android.media.SoundPool;

/* JADX INFO: loaded from: classes.dex */
public class Sound {
    public static final int CLICK = 2;
    public static final int COUNT_DOWN = 1;
    public static final int END = 3;
    private static int sClick;
    private static Context sContext;
    private static int sCountDown;
    private static int sEnd;
    private static SoundPool sSoundPool;

    public static void init(Context context) {
        sContext = context;
        sSoundPool = new SoundPool(8, 3, 0);
        sCountDown = sSoundPool.load(sContext, R.raw.countdown, 0);
        sClick = sSoundPool.load(sContext, R.raw.click, 0);
        sEnd = sSoundPool.load(sContext, R.raw.end, 0);
    }

    public static synchronized void play(int sound) {
        if (Prefs.getBoolean(R.string.pref_key_sfx, true)) {
            switch (sound) {
                case 1:
                    sSoundPool.play(sCountDown, 1.0f, 1.0f, 1, 0, 1.0f);
                    break;
                case 2:
                    sSoundPool.play(sClick, 1.0f, 1.0f, 1, 0, 1.0f);
                    break;
                case 3:
                    sSoundPool.play(sEnd, 1.0f, 1.0f, 1, 0, 1.0f);
                    break;
            }
        }
    }
}
