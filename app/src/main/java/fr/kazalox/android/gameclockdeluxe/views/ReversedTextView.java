package fr.kazalox.android.gameclockdeluxe.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class ReversedTextView extends TextView {
    public ReversedTextView(Context context) {
        super(context);
    }

    public ReversedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ReversedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override // android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        canvas.save();
        float y = getHeight() / 2.0f;
        float x = getWidth() / 2.0f;
        canvas.rotate(180.0f, x, y);
        super.onDraw(canvas);
        canvas.restore();
    }
}
