package fr.kazalox.android.gameclockdeluxe.views;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import fr.kazalox.android.gameclockdeluxe.R;
import fr.kazalox.android.gameclockdeluxe.models.Timer;
import fr.kazalox.android.gameclockdeluxe.utils.TimeUtils;

public class AnalogClockView extends View {
    private static final float PI = 3.1415f;
    private int mAngle;
    private boolean mChanged;
    private Context mContext;
    private BitmapDrawable mDial;
    private int mDialHeight;
    private int mDialWidth;
    private BitmapDrawable mFlag;
    private long mFlagAnimationElapsed;
    private boolean mFlagAnimationOn;
    private final Handler mHandler;
    private float mHour;
    private BitmapDrawable mHourHand;
    private BitmapDrawable mMinuteHand;
    private float mMinutes;
    private boolean mNegativeRemaining;
    private Paint mPaintText;
    private float mScale;
    private BitmapDrawable mSecondHand;
    private float mSeconds;
    private String mTextNbMoves;
    private Updater mUpdater;

    static /* synthetic */ long access$114(AnalogClockView x0, long x1) {
        long j = x0.mFlagAnimationElapsed + x1;
        x0.mFlagAnimationElapsed = j;
        return j;
    }

    public AnalogClockView(Context context) {
        this(context, null);
    }

    public AnalogClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnalogClockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mHandler = new Handler();
        this.mContext = context;
    }

    public void onCreateView() {
        this.mDial = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.dial));
        this.mDial.setAntiAlias(true);
        this.mDial.setDither(true);
        this.mHourHand = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.hand_hour_cropped));
        this.mMinuteHand = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.hand_minute_cropped));
        this.mSecondHand = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.hand_seconds_cropped));
        this.mFlag = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.flag));
        this.mDialWidth = this.mDial.getIntrinsicWidth();
        this.mDialHeight = this.mDial.getIntrinsicHeight();
        this.mNegativeRemaining = false;
        this.mPaintText = new Paint();
        Typeface face = Typeface.createFromAsset(this.mContext.getAssets(), "fonts/Roboto-Thin.ttf");
        this.mPaintText.setColor(-3401704);
        this.mPaintText.setAntiAlias(true);
        this.mPaintText.setTypeface(face);
        this.mPaintText.setTextAlign(Paint.Align.CENTER);
        this.mPaintText.setShadowLayer(4.0f, 2.0f, 2.0f, ViewCompat.MEASURED_STATE_MASK);
        this.mPaintText.setTextSize(40.0f);
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        float hScale = 1.0f;
        float vScale = 1.0f;
        if (widthMode != 0) {
            hScale = widthSize / this.mDialWidth;
        }
        if (heightMode != 0) {
            vScale = heightSize / this.mDialHeight;
        }
        this.mScale = Math.min(hScale, vScale);
        setMeasuredDimension(resolveSize((int) (this.mDialWidth * this.mScale), widthMeasureSpec), resolveSize((int) (this.mDialHeight * this.mScale), heightMeasureSpec));
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mChanged = true;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean changed = this.mChanged;
        if (changed) {
            this.mChanged = false;
        }
        int availableWidth = getRight() - getLeft();
        int availableHeight = getBottom() - getTop();
        int oX = availableWidth / 2;
        int oY = availableHeight / 2;
        Drawable dial = this.mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();
        float scale = Math.min(availableWidth / w, availableHeight / h);
        canvas.save();
        canvas.scale(scale, scale, oX, oY);
        if (changed) {
            dial.setBounds(oX - (w / 2), oY - (h / 2), (w / 2) + oX, (h / 2) + oY);
        }
        dial.draw(canvas);
        if (this.mTextNbMoves != null) {
            canvas.drawText(this.mTextNbMoves, oX + (dial.getIntrinsicWidth() / 5.5f), oY + (dial.getIntrinsicHeight() / 5.5f), this.mPaintText);
        }
        Drawable hourHand = this.mHourHand;
        canvas.save();
        canvas.rotate(360.0f - (((this.mHour / 12.0f) * 360.0f) + ((this.mMinutes / 60.0f) * 30.0f)), oX, oY);
        if (changed) {
            int w2 = hourHand.getIntrinsicWidth();
            int h2 = hourHand.getIntrinsicHeight();
            Rect rect = new Rect(oX - (w2 / 2), oY - h2, (w2 / 2) + oX, oY);
            rect.offset(0, (int) (h2 * 0.072f));
            hourHand.setBounds(rect);
        }
        hourHand.draw(canvas);
        canvas.restore();
        canvas.save();
        float angle = ((this.mMinutes / 60.0f) * 360.0f) + ((this.mSeconds / 60.0f) * 6.0f);
        canvas.rotate(360.0f - angle, oX, oY);
        Drawable minuteHand = this.mMinuteHand;
        if (changed) {
            int w3 = minuteHand.getIntrinsicWidth();
            int h3 = minuteHand.getIntrinsicHeight();
            Rect rect2 = new Rect(oX - (w3 / 2), oY - h3, (w3 / 2) + oX, oY);
            rect2.offset(0, (int) (h3 * 0.065f));
            minuteHand.setBounds(rect2);
        }
        minuteHand.draw(canvas);
        canvas.restore();
        canvas.save();
        int flagHeight = this.mFlag.getIntrinsicHeight();
        int flagWidth = this.mFlag.getIntrinsicWidth();
        int minuteHandRadius = (int) (0.93f * this.mMinuteHand.getIntrinsicHeight());
        int flagTop = (int) ((oY - minuteHandRadius) - (0.75f * flagWidth));
        int flagLeft = oX - flagHeight;
        int flagX = flagLeft + (flagWidth / 2);
        int flagY = flagTop + (flagWidth / 2);
        float r = minuteHandRadius;
        float d = flagHeight - (flagWidth / 2);
        float alphaContact = (float) ((Math.atan2(r - d, d) * 180.0d) / 3.1414999961853027d);
        float alphaColin = (float) ((Math.atan2(r, d) * 180.0d) / 3.1414999961853027d);
        float alpha = (360.0f - angle) - 270.0f;
        if (this.mFlagAnimationOn) {
            this.mAngle = (int) (((double) ((-90.0f) * ((float)Math.cos((12.566f * this.mFlagAnimationElapsed) / 1000.0f))) * Math.exp(((-3.1415f) * this.mFlagAnimationElapsed) / 1000.0f)));
            canvas.rotate(this.mAngle, flagX, flagY);
        } else if (alpha > alphaColin) {
            float cosAlpha = ((float)Math.cos(((PI * alpha) * 2.0f) / 360.0f));
            float sinAlpha = ((float)Math.sin(((PI * alpha) * 2.0f) / 360.0f));
            float bc = r - (r * sinAlpha);
            float dc = d - (r * cosAlpha);
            canvas.rotate(((((float) Math.atan2(bc, dc)) * 180.0f) / PI) - 90.0f, flagX, flagY);
        } else if (alpha > alphaContact) {
            float tanA = (float) Math.tan((PI * alpha) / 180.0f);
            float angleRad = (float) (((double) (1.57075f - ((PI * alpha) / 180.0f))) + Math.asin((tanA - (r / d)) / ((float)Math.sqrt((tanA * tanA) + 1.0f))));
            canvas.rotate(-((180.0f * angleRad) / PI), flagX, flagY);
        }
        Drawable flag = this.mFlag;
        flag.setBounds(flagLeft, flagTop, flagLeft + flagWidth, flagTop + flagHeight);
        flag.draw(canvas);
        canvas.restore();
        canvas.save();
        canvas.rotate(360.0f - ((this.mSeconds / 60.0f) * 360.0f), oX, oY);
        Drawable secondHand = this.mSecondHand;
        if (changed) {
            int w4 = secondHand.getIntrinsicWidth();
            int h4 = secondHand.getIntrinsicHeight();
            Rect rect3 = new Rect(oX - (w4 / 2), oY - h4, (w4 / 2) + oX, oY);
            rect3.offset(0, (int) (h4 * 0.069f));
            secondHand.setBounds(rect3);
        }
        secondHand.draw(canvas);
        canvas.restore();
        if (1 != 0) {
            canvas.restore();
        }
    }

    public void updateNbMoves(String textNbMoves, float fontSize) {
        this.mTextNbMoves = textNbMoves;
        this.mPaintText.setTextSize(1.3f * fontSize);
        this.mChanged = true;
    }

    public void update(long remaining, Timer.State state) {
        if ((remaining == 0 || (remaining < 0 && !this.mNegativeRemaining)) && state == null) {
            launchAnimation();
            this.mNegativeRemaining = true;
        } else if (!this.mFlagAnimationOn) {
            invalidate();
        }
        if (remaining > 0) {
            this.mNegativeRemaining = false;
        }
        if (remaining < 0) {
            remaining += 43200000;
        }
        int[] t = TimeUtils.splitTimeMillis(remaining);
        this.mHour = t[0];
        this.mMinutes = t[1];
        this.mSeconds = t[2];
        long millis = t[3];
        if (millis != 0) {
            this.mSeconds += 1.0f;
        }
        this.mChanged = true;
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        Log.d("ChessClock", "onDetachedFromWindow");
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.mUpdater);
            this.mFlagAnimationOn = false;
        }
        super.onDetachedFromWindow();
    }

    private void launchAnimation() {
        this.mFlagAnimationOn = true;
        this.mFlagAnimationElapsed = 0L;
        this.mAngle = 0;
        this.mUpdater = new Updater();
        this.mHandler.postDelayed(this.mUpdater, 50L);
    }

    private class Updater implements Runnable {
        private Updater() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AnalogClockView.access$114(AnalogClockView.this, 50L);
            if (AnalogClockView.this.mFlagAnimationElapsed >= 1500) {
                AnalogClockView.this.mHandler.removeCallbacks(AnalogClockView.this.mUpdater);
                AnalogClockView.this.mFlagAnimationOn = false;
            } else {
                AnalogClockView.this.invalidate();
                AnalogClockView.this.mHandler.postDelayed(this, 50L);
            }
        }
    }

    public void onDestroyView() {
        this.mDial.getBitmap().recycle();
        this.mHourHand.getBitmap().recycle();
        this.mMinuteHand.getBitmap().recycle();
        this.mSecondHand.getBitmap().recycle();
        this.mFlag.getBitmap().recycle();
    }
}
