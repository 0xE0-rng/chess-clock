package fr.kazalox.android.gameclockdeluxe.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class AutoSizeTextView extends TextView {
    public AutoSizeTextView(Context context) {
        super(context);
    }

    public AutoSizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoSizeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init() {
    }

    @Override // android.widget.TextView, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        adjust();
    }

    @Override // android.widget.TextView
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        adjust();
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        adjust();
    }

    private void adjust() {
        int length = getText().length();
        if (length == 0) {
            length = 1;
        }
        float size = Math.min(getHeight() * 1.0f, (2.0f * getWidth()) / length) * 1.0f;
        setTextSize(0, size);
    }
}
