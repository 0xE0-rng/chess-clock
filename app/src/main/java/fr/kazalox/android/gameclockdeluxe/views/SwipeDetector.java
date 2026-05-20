package fr.kazalox.android.gameclockdeluxe.views;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

/* JADX INFO: loaded from: classes.dex */
public class SwipeDetector implements GestureDetector.OnGestureListener {
    private float mDpi;
    private SwipeListener mSwipeListener;

    public interface SwipeListener {
        void onSwipeLeft();

        void onSwipeRight();
    }

    public SwipeDetector(Context context, SwipeListener swipeListener) {
        this.mSwipeListener = swipeListener;
        WindowManager window = (WindowManager) context.getSystemService("window");
        DisplayMetrics metrics = new DisplayMetrics();
        window.getDefaultDisplay().getMetrics(metrics);
        this.mDpi = metrics.densityDpi;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float vX = (25.4f * velocityX) / this.mDpi;
        float vY = (25.4f * velocityY) / this.mDpi;
        if (Math.abs(vX) <= 50.0f || Math.abs(vX) <= 1.5f * Math.abs(vY)) {
            return false;
        }
        if (vX > 0.0f) {
            this.mSwipeListener.onSwipeRight();
            return false;
        }
        this.mSwipeListener.onSwipeLeft();
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onLongPress(MotionEvent e) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onShowPress(MotionEvent e) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
}
