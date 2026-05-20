package fr.kazalox.android.gameclockdeluxe;

import android.app.Application;

public class App extends Application {
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
}
