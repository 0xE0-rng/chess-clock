package fr.kazalox.android.gameclockdeluxe.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;
import fr.kazalox.android.gameclockdeluxe.C;
import fr.kazalox.android.gameclockdeluxe.Prefs;
import fr.kazalox.android.gameclockdeluxe.R;
import fr.kazalox.android.gameclockdeluxe.fragments.BaseFragment;
import fr.kazalox.android.gameclockdeluxe.models.Timer;
import fr.kazalox.android.gameclockdeluxe.utils.TimeUtils;
import java.util.Observable;
import java.util.Observer;

public class DigitalClockFragment extends ClockFragment implements View.OnTouchListener, View.OnLongClickListener, Observer, ViewTreeObserver.OnPreDrawListener {
    private static final float PADDING = 0.68f;
    private boolean mAlwaysShowColon;
    private Drawable mBtDown;
    private Drawable mBtUp;
    private int mColonCounter;
    private boolean mFirstDisplay;
    private float mFontScale;
    private int mFontSizeLandscape;
    private int mFontSizePortrait;
    private float mLeftPaddingLandscapeDown;
    private float mLeftPaddingLandscapeUp;
    private float mLeftPaddingPortraitDown;
    private float mLeftPaddingPortraitUp;
    private int mModeId;
    private boolean mOnLongClick;
    private boolean mPortrait;
    private boolean mShowNbMoves;
    private int mStyle;
    private int mTextColorDown;
    private int mTextColorUp;
    private float mTopPaddingLandscapeDown;
    private float mTopPaddingLandscapeUp;
    private float mTopPaddingPortraitDown;
    private float mTopPaddingPortraitUp;
    private TextView mTvDelayCountdown;
    private TextView mTvMovesCount;
    private TextView mTvTimer;
    private boolean mViewAvailable;

    public interface DigitalClockFragmentListener extends BaseFragment.BaseFragmentListener {
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
        setRetainInstance(true);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = R.layout.fragment_digital_clock;
        this.mPortrait = getResources().getConfiguration().orientation == 1;
        if (this.mClockId == 1 && this.mPortrait) {
            layoutId = R.layout.fragment_digital_clock_reversed;
        }
        View layout = inflater.inflate(layoutId, container, false);
        this.mTvTimer = (TextView) layout.findViewById(R.id.tvTimer);
        this.mTvTimer.setTag(this.mClockId == 1 ? "#1" : "#2");
        this.mTvMovesCount = (TextView) layout.findViewById(R.id.tvMovesCount);
        this.mTvDelayCountdown = (TextView) layout.findViewById(R.id.tvDelayCountdown);
        Resources r = getResources();
        String style = "light";
        Typeface typeface = null;
        switch (this.mStyle) {
            case 0:
                style = "digital_light";
                this.mTextColorUp = ViewCompat.MEASURED_STATE_MASK;
                this.mTextColorDown = -11184811;
                this.mFontSizePortrait = 25;
                this.mFontSizeLandscape = 22;
                this.mLeftPaddingPortraitUp = 0.09f;
                this.mLeftPaddingPortraitDown = 0.1f;
                this.mTopPaddingPortraitUp = 0.09f;
                this.mTopPaddingPortraitDown = 0.1f;
                this.mLeftPaddingLandscapeUp = 0.09f;
                this.mLeftPaddingLandscapeDown = 0.1f;
                this.mTopPaddingLandscapeUp = 0.09f;
                this.mTopPaddingLandscapeDown = 0.1f;
                typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/digital.ttf");
                this.mAlwaysShowColon = false;
                break;
            case 1:
                style = "digital_dark";
                this.mTextColorUp = -1;
                this.mTextColorDown = -5592406;
                this.mFontSizePortrait = 25;
                this.mFontSizeLandscape = 22;
                this.mLeftPaddingPortraitUp = 0.09f;
                this.mLeftPaddingPortraitDown = 0.1f;
                this.mTopPaddingPortraitUp = 0.09f;
                this.mTopPaddingPortraitDown = 0.1f;
                this.mLeftPaddingLandscapeUp = 0.09f;
                this.mLeftPaddingLandscapeDown = 0.1f;
                this.mTopPaddingLandscapeUp = 0.09f;
                this.mTopPaddingLandscapeDown = 0.1f;
                typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/digital.ttf");
                this.mAlwaysShowColon = false;
                break;
            case 2:
                style = "modern_light";
                this.mTextColorUp = ViewCompat.MEASURED_STATE_MASK;
                this.mTextColorDown = -8947849;
                this.mFontSizePortrait = 20;
                this.mFontSizeLandscape = 18;
                this.mLeftPaddingPortraitUp = 0.07f;
                this.mLeftPaddingPortraitDown = 0.09f;
                this.mLeftPaddingPortraitUp = 0.07f;
                this.mTopPaddingPortraitUp = 0.08f;
                this.mLeftPaddingPortraitDown = 0.09f;
                this.mTopPaddingPortraitDown = 0.09f;
                this.mLeftPaddingLandscapeUp = 0.09f;
                this.mTopPaddingLandscapeUp = 0.07f;
                this.mLeftPaddingLandscapeDown = 0.1f;
                this.mTopPaddingLandscapeDown = 0.08f;
                typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
                this.mAlwaysShowColon = true;
                break;
            case 3:
                style = "modern_dark";
                this.mTextColorUp = -1;
                this.mTextColorDown = -7829368;
                this.mFontSizePortrait = 20;
                this.mFontSizeLandscape = 18;
                this.mLeftPaddingPortraitUp = 0.07f;
                this.mTopPaddingPortraitUp = 0.08f;
                this.mLeftPaddingPortraitDown = 0.09f;
                this.mTopPaddingPortraitDown = 0.1f;
                this.mLeftPaddingLandscapeUp = 0.09f;
                this.mTopPaddingLandscapeUp = 0.07f;
                this.mLeftPaddingLandscapeDown = 0.11f;
                this.mTopPaddingLandscapeDown = 0.09f;
                typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
                this.mAlwaysShowColon = true;
                break;
        }
        int btUpId = r.getIdentifier("bt_" + style + "_up", "drawable", getActivity().getPackageName());
        int btDownId = r.getIdentifier("bt_" + style + "_down", "drawable", getActivity().getPackageName());
        this.mBtUp = getResources().getDrawable(btUpId);
        this.mBtDown = getResources().getDrawable(btDownId);
        this.mTvMovesCount.setTypeface(typeface);
        this.mTvDelayCountdown.setTypeface(typeface);
        this.mTvTimer.setTypeface(typeface);
        this.mTvTimer.setBackgroundDrawable(this.mBtUp);
        this.mTvTimer.setOnTouchListener(this);
        this.mTvTimer.setOnLongClickListener(this);
        updateLayout();
        this.mViewAvailable = true;
        layout.getViewTreeObserver().addOnPreDrawListener(this);
        return layout;
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
    public void onResume() {
        super.onResume();
        this.mModeId = this.mTimer.getMode().getId();
        this.mShowNbMoves = Prefs.getBoolean(R.string.pref_key_moves_counter);
        this.mTvMovesCount.setVisibility(this.mShowNbMoves ? 0 : 8);
        this.mTvDelayCountdown.setVisibility(8);
        if ((this.mModeId == 3 || this.mModeId == 2) && Prefs.getBoolean(R.string.pref_key_show_delay_elapsed)) {
            this.mTvDelayCountdown.setVisibility(0);
        }
    }

    @Override // fr.kazalox.android.gameclockdeluxe.fragments.ClockFragment, java.util.Observer
    public void update(Observable observable, Object data) {
        if (this.mViewAvailable) {
            if (data != null) {
                Timer.State state = (Timer.State) data;
                this.mColonCounter = 0;
                switch (state) {
                    case RUNNING:
                    case PAUSED:
                        this.mTvTimer.setBackgroundDrawable(this.mBtUp);
                        break;
                    case STOPPED:
                    case GAME_OVER:
                        this.mTvTimer.setBackgroundDrawable(this.mBtDown);
                        break;
                }
            }
            updateLayout();
        }
    }

    private void updateLayout() {
        if (getActivity() != null) {
            updateTimeUi();
            updateDelayCountdown();
            updateMovesCount();
        }
    }

    private void updateTimeUi() {
        long remaining = this.mTimer.getRemaining();
        boolean showColon = this.mColonCounter < 5 || this.mAlwaysShowColon;
        this.mColonCounter = (this.mColonCounter + 1) % 10;
        this.mTvTimer.setText(TimeUtils.toClockString(remaining, showColon));
        adjustTextSize(this.mTvTimer);
        if (remaining < 0) {
            this.mTvTimer.setTextColor(0xffff0000);
        } else {
            this.mTvTimer.setTextColor(isUp() ? this.mTextColorUp : this.mTextColorDown);
        }
    }

    private void adjustTextSize(TextView tv) {
        String text = tv.getText().toString();
        int length = text.length();
        int w = tv.getWidth();
        float upCoef = isUp() ? 1.0f : 0.9f;
        float size = (((2.2f * upCoef) * w) * PADDING) / length;
        tv.setTextSize(0, size);
    }

    private void updateDelayCountdown() {
        long remaining;
        int resId;
        float f;
        float f2;
        if (this.mModeId == 2 || this.mModeId == 3) {
            if (this.mModeId == 2) {
                remaining = this.mTimer.getBronsteinElapsed() / 1000;
                resId = R.string.clock_bonus;
            } else {
                remaining = this.mTimer.getSimpleDelayRemaining() / 1000;
                resId = R.string.clock_delay;
            }
            this.mTvDelayCountdown.setTextColor(this.mTextColorDown);
            this.mTvDelayCountdown.setText(getActivity().getString(resId, new Object[]{Long.valueOf(remaining)}));
            float fontSize = this.mFontScale * (isUp() ? 1.05f : 1.0f) * (this.mPortrait ? this.mFontSizePortrait : this.mFontSizeLandscape);
            this.mTvDelayCountdown.setTextSize(1, fontSize);
            int w = this.mTvTimer.getWidth();
            int h = this.mTvTimer.getHeight();
            float f3 = h;
            if (isUp()) {
                f = this.mPortrait ? this.mTopPaddingPortraitUp : this.mTopPaddingLandscapeUp;
            } else {
                f = this.mPortrait ? this.mTopPaddingPortraitDown : this.mTopPaddingLandscapeDown;
            }
            int topPadding = (int) (f * f3);
            float f4 = w;
            if (isUp()) {
                f2 = this.mPortrait ? this.mLeftPaddingPortraitUp : this.mLeftPaddingLandscapeUp;
            } else {
                f2 = this.mPortrait ? this.mLeftPaddingPortraitDown : this.mLeftPaddingLandscapeDown;
            }
            int leftPadding = (int) (f2 * f4);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) this.mTvDelayCountdown.getLayoutParams();
            if (this.mPortrait) {
                lp.gravity = this.mClockId == 2 ? 83 : 53;
            } else {
                lp.gravity = this.mClockId == 1 ? 85 : 83;
            }
            ((FrameLayout) this.mTvDelayCountdown.getParent()).updateViewLayout(this.mTvDelayCountdown, lp);
            this.mTvDelayCountdown.setPadding(leftPadding, topPadding, leftPadding, topPadding);
        }
    }

    private void updateMovesCount() {
        float f;
        float f2;
        this.mTvMovesCount.setTextColor(this.mTextColorDown);
        this.mTvMovesCount.setText(getString(R.string.clock_moves_counter, Integer.valueOf(this.mTimer.getMovesCount())));
        float fontSize = this.mFontScale * (isUp() ? 1.05f : 1.0f) * (this.mPortrait ? this.mFontSizePortrait : this.mFontSizeLandscape);
        this.mTvMovesCount.setTextSize(1, fontSize);
        int w = this.mTvTimer.getWidth();
        int h = this.mTvTimer.getHeight();
        float f3 = h;
        if (isUp()) {
            f = this.mPortrait ? this.mTopPaddingPortraitUp : this.mTopPaddingLandscapeUp;
        } else {
            f = this.mPortrait ? this.mTopPaddingPortraitDown : this.mTopPaddingLandscapeDown;
        }
        int topPadding = (int) (f * f3);
        float f4 = w;
        if (isUp()) {
            f2 = this.mPortrait ? this.mLeftPaddingPortraitUp : this.mLeftPaddingLandscapeUp;
        } else {
            f2 = this.mPortrait ? this.mLeftPaddingPortraitDown : this.mLeftPaddingLandscapeDown;
        }
        int leftPadding = (int) (f2 * f4);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) this.mTvMovesCount.getLayoutParams();
        if (this.mPortrait) {
            lp.gravity = this.mClockId == 2 ? 85 : 51;
        } else {
            lp.gravity = this.mClockId == 1 ? 83 : 85;
        }
        ((FrameLayout) this.mTvMovesCount.getParent()).updateViewLayout(this.mTvMovesCount, lp);
        this.mTvMovesCount.setPadding(leftPadding, topPadding, leftPadding, topPadding);
    }

    public boolean isUp() {
        return this.mTvTimer.getBackground() == this.mBtUp;
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View v) {
        this.mOnLongClick = ((DigitalClockFragmentListener) this.mListener).onClockLongClick();
        return true;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == 1) {
            if (!this.mOnLongClick) {
                ((DigitalClockFragmentListener) this.mListener).onClockClick(this.mTimer);
            } else {
                this.mOnLongClick = false;
            }
        }
        return false;
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public boolean onPreDraw() {
        if (this.mFirstDisplay) {
            updateLayout();
            this.mFirstDisplay = false;
            getView().requestLayout();
            return true;
        }
        return true;
    }

    private void freeBitmap(Drawable d) {
        if (d instanceof BitmapDrawable) {
            BitmapDrawable b = (BitmapDrawable) d;
            Bitmap bitmap = b.getBitmap();
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        this.mTimer = null;
        super.onDestroy();
    }
}
