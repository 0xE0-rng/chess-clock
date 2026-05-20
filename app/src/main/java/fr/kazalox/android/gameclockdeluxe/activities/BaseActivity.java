package fr.kazalox.android.gameclockdeluxe.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.Toast;
import fr.kazalox.android.gameclockdeluxe.views.SwipeDetector;

public class BaseActivity extends AppCompatActivity implements SwipeDetector.SwipeListener {
    private boolean mCommitable;
    private GestureDetector mGestureDetector;

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ChessClock", "activity - onCreate()");
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override // fr.kazalox.android.gameclockdeluxe.views.SwipeDetector.SwipeListener
    public void onSwipeLeft() {
    }

    @Override // fr.kazalox.android.gameclockdeluxe.views.SwipeDetector.SwipeListener
    public void onSwipeRight() {
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        this.mCommitable = false;
        super.onStop();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPostResume() {
        this.mCommitable = true;
        super.onPostResume();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        Log.d("ChessClock", "activity - onDestroy()");
        super.onDestroy();
    }

    public void showToast(String text) {
        Toast.makeText(this, text, 0).show();
    }

    protected boolean isCommitable() {
        return this.mCommitable;
    }
}
