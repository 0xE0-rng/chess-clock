package fr.kazalox.android.gameclockdeluxe.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import fr.kazalox.android.gameclockdeluxe.C;
import fr.kazalox.android.gameclockdeluxe.R;
import fr.kazalox.android.gameclockdeluxe.fragments.BaseDialogFragment;

/* JADX INFO: loaded from: classes.dex */
public class NbMovesDialog extends BaseDialogFragment {
    private int mNbMoves;

    public interface NbMovesDialogListener extends BaseDialogFragment.BaseFragmentListener {
        void onNbMovesOk(int i);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mNbMoves = getArguments().getInt(C.ARG_NB_MOVES);
        setRetainInstance(true);
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_nb_moves, (ViewGroup) null);
        final EditText input = (EditText) layout.findViewById(R.id.et);
        input.setText(String.valueOf(this.mNbMoves));
        input.setSelection(input.getText().length());
        Dialog dialog = new AlertDialog.Builder(getActivity()).setIcon(R.drawable.ic_launcher).setTitle(R.string.settings_number_of_moves).setView(layout).setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: fr.kazalox.android.gameclockdeluxe.dialogs.NbMovesDialog.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog2, int whichButton) {
                ((NbMovesDialogListener) NbMovesDialog.this.mListener).onNbMovesOk(Integer.parseInt(input.getText().toString()));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: fr.kazalox.android.gameclockdeluxe.dialogs.NbMovesDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog2, int whichButton) {
                NbMovesDialog.this.dismiss();
            }
        }).create();
        dialog.getWindow().setSoftInputMode(5);
        return dialog;
    }
}
