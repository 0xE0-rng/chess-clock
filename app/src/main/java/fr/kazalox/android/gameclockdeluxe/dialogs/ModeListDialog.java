package fr.kazalox.android.gameclockdeluxe.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import fr.kazalox.android.gameclockdeluxe.C;
import fr.kazalox.android.gameclockdeluxe.R;
import fr.kazalox.android.gameclockdeluxe.data.Mode;
import fr.kazalox.android.gameclockdeluxe.data.ModeAdapter;
import fr.kazalox.android.gameclockdeluxe.fragments.BaseDialogFragment;

/* JADX INFO: loaded from: classes.dex */
public class ModeListDialog extends BaseDialogFragment implements AdapterView.OnItemClickListener, ModeAdapter.ModeAdapterListener {

    public interface ModeListDialogListener extends BaseDialogFragment.BaseFragmentListener {
        void onModeListClick(Mode mode);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ListView lv = new ListView(requireContext());
        lv.setAdapter(new ModeAdapter(getActivity(), this, Mode.sModes));
        lv.setOnItemClickListener(this);

        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.mode_title)
                .setView(lv)
                .create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Mode mode = Mode.sModes.get(position);
        Log.d("ChessClock", "ModeListDialog - onItemClick - " + getString(mode.getName()));
        ((ModeListDialogListener) this.mListener).onModeListClick(mode);
        dismiss();
    }

    @Override
    public void onModeAdapterHelp(Mode mode) {
        SimpleDialog d = SimpleDialog.newInstance(getString(mode.getName()), getString(mode.getDescription()), getString(R.string.dialog_button_ok));
        d.show(getParentFragmentManager(), C.DIALOG_TAG_MODE_HELP);
    }
}
