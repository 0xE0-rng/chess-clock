package fr.kazalox.android.gameclockdeluxe.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class MetricUtils {
    public static float dp2px(Context context, float dpValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(1, dpValue, metrics);
    }

    public static float pixels2mm(Context context, float pixels) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (25.4f * pixels) / metrics.xdpi;
    }
}
