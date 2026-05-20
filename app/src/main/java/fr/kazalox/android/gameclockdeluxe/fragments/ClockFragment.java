package fr.kazalox.android.gameclockdeluxe.fragments;

import fr.kazalox.android.gameclockdeluxe.models.Timer;
import java.util.Observable;
import java.util.Observer;

public class ClockFragment extends BaseFragment implements Observer {
    protected int mClockId;
    protected Timer mTimer;

    public void updateTimer(Timer timer) {
        if (this.mTimer != null) {
            this.mTimer.deleteObserver(this);
        }
        this.mTimer = timer;
        this.mTimer.addObserver(this);
    }

    public void update(Observable observable, Object data) {
    }
}
