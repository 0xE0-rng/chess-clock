package fr.kazalox.android.gameclockdeluxe.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.kazalox.android.gameclockdeluxe.R;

/* JADX INFO: loaded from: classes.dex */
public class DigitPad extends ViewGroup implements View.OnClickListener {
    private DigitPadListener mListener;

    public interface DigitPadListener {
        void onDigitPadTouch(int i);
    }

    public DigitPad(Context context) {
        this(context, null, 0);
    }

    public DigitPad(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DigitPad(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setListener(DigitPadListener listener) {
        this.mListener = listener;
    }

    public void init() {
        Typeface face = null;
        if (!isInEditMode()) {
            face = Typeface.createFromAsset(getContext().getAssets(), "fonts/digital.ttf");
        }
        for (int i = 0; i < 10; i++) {
            TextView tv = new TextView(getContext());
            tv.setTag(Integer.valueOf(i));
            tv.setText(String.valueOf(i));
            tv.setGravity(17);
            tv.setOnClickListener(this);
            tv.setBackgroundResource(R.drawable.digit_text_view);
            if (!isInEditMode()) {
                tv.setTypeface(face);
            }
            addView(tv);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        int wspec = View.MeasureSpec.makeMeasureSpec(w / 3, 1073741824);
        int hspec = View.MeasureSpec.makeMeasureSpec(h / 4, 1073741824);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(wspec, hspec);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int w = (r - l) / 3;
        int h = (b - t) / 4;
        setChildrenAt(1, 0, 0, w, h);
        setChildrenAt(2, 1, 0, w, h);
        setChildrenAt(3, 2, 0, w, h);
        setChildrenAt(4, 0, 1, w, h);
        setChildrenAt(5, 1, 1, w, h);
        setChildrenAt(6, 2, 1, w, h);
        setChildrenAt(7, 0, 2, w, h);
        setChildrenAt(8, 1, 2, w, h);
        setChildrenAt(9, 2, 2, w, h);
        setChildrenAt(0, 1, 3, w, h);
    }

    private void setChildrenAt(int i, int x, int y, int w, int h) {
        TextView tv = (TextView) getChildAt(i);
        int left = x * w;
        int right = (x + 1) * w;
        int top = y * h;
        int bottom = (y + 1) * h;
        tv.setTextSize(0, (float) (Math.min(2.2f * w, 1.14d * ((double) h)) * 0.699999988079071d));
        tv.layout(left, top, right, bottom);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        if (this.mListener != null) {
            this.mListener.onDigitPadTouch(((Integer) v.getTag()).intValue());
        }
    }
}
