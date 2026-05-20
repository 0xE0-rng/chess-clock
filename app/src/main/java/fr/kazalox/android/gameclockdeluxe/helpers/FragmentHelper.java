package fr.kazalox.android.gameclockdeluxe.helpers;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import fr.kazalox.android.gameclockdeluxe.R;

/* JADX INFO: loaded from: classes.dex */
public class FragmentHelper {
    public static void add(FragmentActivity activity, Class fragmentClass, int containerId) {
        add(activity, fragmentClass, containerId, null, null);
    }

    public static void add(FragmentActivity activity, Class fragmentClass, int containerId, String tag) {
        add(activity, fragmentClass, containerId, tag, null);
    }

    public static void add(FragmentActivity activity, Class fragmentClass, int containerId, Bundle b) {
        add(activity, fragmentClass, containerId, null, b);
    }

    public static void add(FragmentActivity activity, Class fragmentClass, int containerId, String tag, Bundle b) {
        Fragment fragment = Fragment.instantiate(activity, fragmentClass.getName(), b);
        String fragmentTag = tag != null ? tag : fragment.getClass().getSimpleName();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction().add(containerId, fragment, fragmentTag);
        ft.commit();
    }

    public static void replace(FragmentActivity activity, Class fragmentClass) {
        replace(activity, fragmentClass, (Bundle) null, (String) null);
    }

    public static void replace(FragmentActivity activity, Class fragmentClass, Bundle b) {
        replace(activity, fragmentClass, b, (String) null);
    }

    public static void replace(FragmentActivity activity, Class fragmentClass, Bundle b, String transactionTag) {
        try {
            Fragment fragment = Fragment.instantiate(activity, fragmentClass.getName(), b);
            String fragmentTag = fragment.getClass().getSimpleName();
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction().addToBackStack(transactionTag).replace(R.id.layout_container, fragment, fragmentTag);
            ft.commit();
        } catch (Fragment.InstantiationException e) {
            throw new RuntimeException("Cannot instantiate " + fragmentClass.getSimpleName());
        }
    }

    public static void replace(FragmentActivity activity, Class fragmentClass, int containerId, Bundle b) {
        replace(activity, fragmentClass, containerId, b, null);
    }

    public static void replace(FragmentActivity activity, Class fragmentClass, int containerId, Bundle b, String tag) {
        Fragment fragment = Fragment.instantiate(activity, fragmentClass.getName(), b);
        String fragmentTag = tag != null ? tag : fragment.getClass().getSimpleName();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(containerId, fragment, fragmentTag);
        ft.commit();
    }

    public static boolean isVisible(FragmentActivity activity, Class fragmentClass) {
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(fragmentClass.getSimpleName());
        if (fragment == null || !fragment.isVisible()) {
            return false;
        }
        return true;
    }

    public static Fragment get(FragmentActivity activity, Class fragmentClass) {
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(fragmentClass.getSimpleName());
        return fragment;
    }
}
