package fr.kazalox.android.gameclockdeluxe;

import android.app.Application;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class App extends Application {
    private static boolean sFullUpgrade;
    private static App sInstance;

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sInstance.init();
    }

    public void init() {
        Prefs.init(this);
        Sound.init(this);
        Vibe.init(this);
    }

    public static App get() {
        return sInstance;
    }

    public static boolean hasFullUpgrade() {
        return sFullUpgrade;
    }

    public static boolean isPaidVersion() {
        return get().getResources().getBoolean(R.bool.is_paid_version);
    }

    public static boolean hasProVersion() {
        return isPaidVersion() || hasFullUpgrade();
    }

    public static void setFullUpgrade(boolean bFullUpgrade) {
        Log.d("ChessClock", "App - setFullUpgrade -  bFullUpgrade " + bFullUpgrade);
        sFullUpgrade = bFullUpgrade;
    }
}
