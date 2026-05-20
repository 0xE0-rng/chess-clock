package fr.kazalox.android.gameclockdeluxe.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

/* JADX INFO: loaded from: classes.dex */
public class SimpleDialog extends DialogFragment {
    public static final String ARG_BUTTON_1 = "ARG_BUTTON_1";
    public static final String ARG_BUTTON_2 = "ARG_BUTTON_2";
    public static final String ARG_BUTTON_3 = "ARG_BUTTON_3";
    public static final String ARG_MESSAGE = "ARG_MESSAGE";
    public static final String ARG_TITLE = "ARG_TITLE";
    SimpleDialogListener mListener;

    public interface SimpleDialogListener {
        void onButton1Click(DialogFragment dialogFragment);

        void onButton2Click(DialogFragment dialogFragment);

        void onButton3Click(DialogFragment dialogFragment);
    }

    public static SimpleDialog newInstance(String title, String msg, String button1) {
        return newInstance(title, msg, button1, null);
    }

    public static SimpleDialog newInstance(String title, String msg, String button1, String button2) {
        return newInstance(title, msg, button1, button2, null);
    }

    public static SimpleDialog newInstance(String title, String msg, String button1, String button2, String button3) {
        Bundle b = new Bundle();
        b.putString("ARG_TITLE", title);
        b.putString(ARG_MESSAGE, msg);
        b.putString(ARG_BUTTON_1, button1);
        b.putString(ARG_BUTTON_2, button2);
        b.putString(ARG_BUTTON_3, button3);
        SimpleDialog d = new SimpleDialog();
        d.setArguments(b);
        return d;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (SimpleDialogListener) activity;
        } catch (ClassCastException e) {
        }
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("ARG_TITLE");
        String message = getArguments().getString(ARG_MESSAGE);
        String button1 = getArguments().getString(ARG_BUTTON_1);
        String button2 = getArguments().getString(ARG_BUTTON_2);
        String button3 = getArguments().getString(ARG_BUTTON_3);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message).setPositiveButton(button1, new DialogInterface.OnClickListener() { // from class: fr.kazalox.android.gameclockdeluxe.dialogs.SimpleDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
                if (SimpleDialog.this.mListener != null) {
                    SimpleDialog.this.mListener.onButton1Click(SimpleDialog.this);
                }
            }
        });
        if (button2 != null) {
            builder.setNegativeButton(button2, new DialogInterface.OnClickListener() { // from class: fr.kazalox.android.gameclockdeluxe.dialogs.SimpleDialog.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int id) {
                    if (SimpleDialog.this.mListener != null) {
                        SimpleDialog.this.mListener.onButton2Click(SimpleDialog.this);
                    }
                }
            });
        }
        if (button3 != null) {
            builder.setNeutralButton(button3, new DialogInterface.OnClickListener() { // from class: fr.kazalox.android.gameclockdeluxe.dialogs.SimpleDialog.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int id) {
                    if (SimpleDialog.this.mListener != null) {
                        SimpleDialog.this.mListener.onButton3Click(SimpleDialog.this);
                    }
                }
            });
        }
        return builder.create();
    }
}
