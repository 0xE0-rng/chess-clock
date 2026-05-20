package fr.kazalox.android.gameclockdeluxe.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import fr.kazalox.android.gameclockdeluxe.activities.BaseActivity;

/* JADX INFO: loaded from: classes.dex */
public class ActivityHelper {
    public static <T extends BaseActivity> void start(FragmentActivity src, Class<T> target) {
        start(src, target, false);
    }

    public static <T extends BaseActivity> void start(FragmentActivity src, Class<T> target, boolean clearTop) {
        Intent intent = new Intent((Context) src, (Class<?>) target);
        if (clearTop) {
            intent.addFlags(67108864);
        }
        src.startActivity(intent);
    }

    public static <T extends BaseActivity> void goToErrorGeneric(FragmentActivity src, String message) {
    }

    public static <T extends BaseActivity> void goToError(FragmentActivity src, int errorId, String message) {
    }

    public static <T extends BaseActivity> void goToError(FragmentActivity src, int errorId) {
        goToError(src, errorId, null);
    }

    public static void goToUrl(BaseActivity src, String url) {
        Intent i = new Intent("android.intent.action.VIEW");
        i.setData(Uri.parse(url));
        src.startActivity(i);
    }

    public static void removeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService("input_method");
        if (imm != null && activity.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Activity activity, View v) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService("input_method");
        if (imm != null && activity.getCurrentFocus() != null) {
            imm.showSoftInput(v, 0);
        }
    }
}
