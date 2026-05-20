package fr.kazalox.android.gameclockdeluxe.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.Log;

public class BaseDialogFragment extends DialogFragment {
    protected BaseFragmentListener mListener;

    public interface BaseFragmentListener {
    }

    public static BaseDialogFragment newInstance(Bundle b) {
        BaseDialogFragment f = new BaseDialogFragment();
        f.setArguments(b);
        return f;
    }

    public static <T extends BaseDialogFragment> T newInstance(Class<T> fragmentClass, Bundle b) {
        try {
            T f = fragmentClass.getDeclaredConstructor().newInstance();
            f.setArguments(b);
            return f;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        String listenerName = getClass().getName() + '$' + getClass().getSimpleName() + "Listener";
        try {
            Class<?> cls = Class.forName(listenerName);
            if (!cls.isAssignableFrom(activity.getClass())) {
                throw new ClassCastException(activity.toString() + " must implement " + cls.getSimpleName());
            }
            this.mListener = (BaseFragmentListener) activity;
        } catch (ClassNotFoundException e) {
            Log.e("ChessClock", e.getMessage());
        }
    }
}
