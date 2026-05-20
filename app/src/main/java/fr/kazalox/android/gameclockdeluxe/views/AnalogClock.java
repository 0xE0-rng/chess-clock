package fr.kazalox.android.gameclockdeluxe.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import fr.kazalox.android.gameclockdeluxe.R;
import fr.kazalox.android.gameclockdeluxe.models.Timer;

public class AnalogClock extends ViewGroup {
    private BitmapDrawable mBackgroundLeft;
    private BitmapDrawable mBackgroundRight;
    private BitmapDrawable mBtDown;
    private BitmapDrawable mBtUp;
    private int mButtonDownHeight;
    private int mButtonMargin;
    private int mButtonUpHeight;
    private int mButtonWidth;
    private int mClockMargin;
    private int mClockOffset;
    private AnalogClockView mClockView;
    private Context mContext;
    private boolean mIsLeft;
    private boolean mIsUp;
    private float mScale;

    public AnalogClock(Context context) {
        this(context, null);
    }

    public AnalogClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnalogClock(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    public void init() {
        this.mClockView = new AnalogClockView(this.mContext);
        addView(this.mClockView);
    }

    public void onCreateView() {
        Resources r = this.mContext.getResources();
        this.mBackgroundLeft = new BitmapDrawable(r, BitmapFactory.decodeResource(r, R.drawable.background_left));
        this.mBackgroundRight = new BitmapDrawable(r, BitmapFactory.decodeResource(r, R.drawable.background_right));
        this.mBtUp = new BitmapDrawable(r, BitmapFactory.decodeResource(r, R.drawable.button_up));
        this.mBtDown = new BitmapDrawable(r, BitmapFactory.decodeResource(r, R.drawable.button_down));
        this.mClockView.onCreateView();
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = View.MeasureSpec.getSize(widthMeasureSpec);
        int h = View.MeasureSpec.getSize(heightMeasureSpec);
        int size = this.mBackgroundLeft.getIntrinsicWidth();
        this.mScale = size / w;
        this.mClockMargin = w / 8;
        this.mClockOffset = (int) ((this.mIsLeft ? 0.4f : -0.4f) * this.mClockMargin);
        this.mButtonMargin = (int) (w / 3.5f);
        this.mButtonWidth = (int) (this.mBtDown.getIntrinsicWidth() / this.mScale);
        this.mButtonUpHeight = (int) (this.mBtUp.getIntrinsicHeight() / this.mScale);
        this.mButtonDownHeight = (int) (this.mBtDown.getIntrinsicHeight() / this.mScale);
        this.mClockView.measure(View.MeasureSpec.makeMeasureSpec(w - (this.mClockMargin * 2), 1073741824), View.MeasureSpec.makeMeasureSpec(w - (this.mClockMargin * 2), 1073741824));
        setMeasuredDimension(w, h);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int h = getMeasuredHeight();
        int w = getMeasuredWidth();
        setChildLayout(this.mClockMargin + this.mClockOffset, (h - w) + this.mClockMargin, this.mClockView);
    }

    public void setChildLayout(int l, int t, View child) {
        this.mClockView.layout(l, t, child.getMeasuredWidth() + l, child.getMeasuredHeight() + t);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mIsUp) {
            int l = this.mIsLeft ? this.mButtonMargin : (getWidth() - this.mButtonMargin) - this.mButtonWidth;
            int t = (getHeight() - getWidth()) - this.mButtonUpHeight;
            int r = l + this.mButtonWidth;
            int b = t + this.mButtonUpHeight;
            this.mBtUp.setBounds(l, t, r, b);
            this.mBtUp.draw(canvas);
        } else {
            int l2 = this.mIsLeft ? this.mButtonMargin : (getWidth() - this.mButtonMargin) - this.mButtonWidth;
            int t2 = (getHeight() - getWidth()) - this.mButtonDownHeight;
            int r2 = l2 + this.mButtonWidth;
            int b2 = t2 + this.mButtonDownHeight;
            this.mBtDown.setBounds(l2, t2, r2, b2);
            this.mBtDown.draw(canvas);
        }
        if (this.mIsLeft) {
            this.mBackgroundLeft.setBounds(0, getHeight() - getWidth(), getWidth(), getHeight());
            this.mBackgroundLeft.draw(canvas);
        } else {
            this.mBackgroundRight.setBounds(0, getHeight() - getWidth(), getWidth(), getHeight());
            this.mBackgroundRight.draw(canvas);
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void update(long remaining, Timer.State state) {
        this.mClockView.update(remaining, state);
    }

    public void updateNbMoves(String textNbMoves, float fontSize) {
        this.mClockView.updateNbMoves(textNbMoves, fontSize);
    }

    public void setIsUp(boolean mIsUp) {
        this.mIsUp = mIsUp;
        invalidate();
    }

    public void setIsLeft(boolean isLeft) {
        this.mIsLeft = isLeft;
    }

    public void onDestroyView() {
        if (this.mBackgroundLeft != null) {
            this.mBackgroundLeft.getBitmap().recycle();
            this.mBackgroundRight.getBitmap().recycle();
            this.mBtDown.getBitmap().recycle();
            this.mBtUp.getBitmap().recycle();
            this.mClockView.onDestroyView();
        }
    }
}
