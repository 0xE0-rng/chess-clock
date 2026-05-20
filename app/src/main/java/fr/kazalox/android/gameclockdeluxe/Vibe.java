package fr.kazalox.android.gameclockdeluxe;

import android.content.Context;
import android.os.Vibrator;

public class Vibe {
    public static final int CLICK = 1;
    public static final int END = 2;
    private static Context sContext;
    private static Vibrator sVibrator;

    public static void init(Context context) {
        sContext = context;
        sVibrator = (Vibrator) sContext.getSystemService("vibrator");
    }

    public static synchronized void play(int event) {
        if (Prefs.getBoolean(R.string.pref_key_vibe, true)) {
            switch (event) {
                case 1:
                    sVibrator.vibrate(200L);
                    break;
                case 2:
                    sVibrator.vibrate(new long[]{0, 100, 50, 100, 50, 100, 50, 1000}, -1);
                    break;
            }
        }
    }
}
