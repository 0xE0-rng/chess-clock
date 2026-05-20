package fr.kazalox.android.gameclockdeluxe.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import fr.kazalox.android.gameclockdeluxe.C;
import fr.kazalox.android.gameclockdeluxe.Prefs;
import fr.kazalox.android.gameclockdeluxe.R;
import fr.kazalox.android.gameclockdeluxe.fragments.BaseFragment;
import fr.kazalox.android.gameclockdeluxe.models.Timer;
import fr.kazalox.android.gameclockdeluxe.utils.MetricUtils;
import fr.kazalox.android.gameclockdeluxe.views.AnalogClock;
import java.util.Observable;

public class AnalogClockFragment extends ClockFragment implements View.OnTouchListener, View.OnLongClickListener, ViewTreeObserver.OnPreDrawListener {
    private AnalogClock mAnalogClock;
    private boolean mFirstDisplay;
    private float mFontScale;
    private boolean mOnLongClick;
    private boolean mShowNbMoves;
    private int mStyle;
    private TextView mTvMovesCount;
    private boolean mViewAvailable;

    public interface AnalogClockFragmentListener extends BaseFragment.BaseFragmentListener {
        void onClockClick(Timer timer);

        boolean onClockLongClick();
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mClockId = getArguments().getInt(C.ARG_CLOCK_ID);
        this.mStyle = getArguments().getInt(C.ARG_CLOCK_STYLE);
        this.mTimer = (Timer) getArguments().getSerializable(C.ARG_TIMER);
        updateTimer(this.mTimer);
        setRetainInstance(false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float cm = (2.54f * Math.min(dm.widthPixels, dm.heightPixels)) / dm.xdpi;
        this.mFontScale = cm / 6.0f;
        this.mFirstDisplay = true;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(this.mClockId == 1 ? R.layout.fragment_analog_clock_left : R.layout.fragment_analog_clock_right, container, false);
        this.mAnalogClock = (AnalogClock) layout.findViewById(R.id.analogClock);
        this.mAnalogClock.setIsLeft(this.mClockId == 1);
        this.mAnalogClock.setOnTouchListener(this);
        this.mAnalogClock.setOnLongClickListener(this);
        this.mAnalogClock.onCreateView();
        this.mTvMovesCount = (TextView) layout.findViewById(R.id.tvMovesCount);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
        this.mTvMovesCount.setTypeface(face);
        this.mViewAvailable = true;
        layout.getViewTreeObserver().addOnPreDrawListener(this);
        return layout;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        this.mShowNbMoves = Prefs.getBoolean(R.string.pref_key_moves_counter);
    }

    @Override // fr.kazalox.android.gameclockdeluxe.fragments.ClockFragment, java.util.Observer
    public void update(Observable observable, Object data) {
        if (this.mViewAvailable) {
            Timer.State state = (Timer.State) data;
            if (state != null) {
                switch (state) {
                    case RUNNING:
                    case PAUSED:
                        this.mAnalogClock.setIsUp(true);
                        break;
                    case STOPPED:
                    case GAME_OVER:
                        this.mAnalogClock.setIsUp(false);
                        break;
                }
            }
            this.mAnalogClock.update(this.mTimer.getRemaining(), state);
            updateMovesCount();
        }
    }

    private void updateMovesCount() {
        if (getActivity() != null) {
            float fontSize = this.mFontScale * MetricUtils.dp2px(getActivity(), 30.0f);
            String textNbMoves = this.mShowNbMoves ? String.valueOf(this.mTimer.getMovesCount()) : null;
            this.mAnalogClock.updateNbMoves(textNbMoves, fontSize);
        }
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public boolean onPreDraw() {
        if (this.mFirstDisplay) {
            updateMovesCount();
            this.mFirstDisplay = false;
            getView().getViewTreeObserver().removeOnPreDrawListener(this);
            getView().requestLayout();
            return true;
        }
        return true;
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View v) {
        this.mOnLongClick = ((AnalogClockFragmentListener) this.mListener).onClockLongClick();
        return true;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == 1) {
            if (!this.mOnLongClick) {
                ((AnalogClockFragmentListener) this.mListener).onClockClick(this.mTimer);
            } else {
                this.mOnLongClick = false;
            }
        }
        return false;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        this.mAnalogClock.onDestroyView();
        super.onDestroyView();
    }
}
