package fr.kazalox.android.gameclockdeluxe.dialogs;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import fr.kazalox.android.gameclockdeluxe.C;
import fr.kazalox.android.gameclockdeluxe.R;
import fr.kazalox.android.gameclockdeluxe.fragments.BaseDialogFragment;
import fr.kazalox.android.gameclockdeluxe.utils.TimeUtils;
import fr.kazalox.android.gameclockdeluxe.views.DigitPad;
import java.util.LinkedList;

public class TimePickerDialog extends BaseDialogFragment implements View.OnClickListener, DigitPad.DigitPadListener {
    private Button mBtOk;
    private int mCallbackId;
    private DigitPad mDigitPad;
    private boolean mFirstTime;
    private ImageView mIvBackspace;
    private int mNbDigits;
    private LinkedList<Integer> mNumberList = new LinkedList<>();
    private int mTime;
    private String mTitle;
    private TextView mTvTime;

    public interface TimePickerDialogListener {
        void onTimePickerOk(int i, int i2);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mNbDigits = getArguments().getInt(C.ARG_NB_DIGITS);
        this.mTime = getArguments().getInt(C.ARG_TIME);
        this.mCallbackId = getArguments().getInt(C.ARG_PREF_KEY);
        this.mTitle = getArguments().getString("ARG_TITLE");
        int[] tab = TimeUtils.splitTime(this.mTime);
        if (this.mNbDigits == 5) {
            this.mNumberList.add(Integer.valueOf(tab[0]));
        }
        this.mNumberList.add(Integer.valueOf(tab[1] / 10));
        this.mNumberList.add(Integer.valueOf(tab[1] % 10));
        this.mNumberList.add(Integer.valueOf(tab[2] / 10));
        this.mNumberList.add(Integer.valueOf(tab[2] % 10));
        this.mFirstTime = true;
        setRetainInstance(true);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_time_picker, container, false);
        getActivity().setTitle(this.mTitle);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/digital.ttf");
        this.mTvTime = (TextView) layout.findViewById(R.id.tvTime);
        this.mTvTime.setTypeface(face);
        this.mDigitPad = (DigitPad) layout.findViewById(R.id.digitPad);
        this.mDigitPad.setListener(this);
        this.mIvBackspace = (ImageView) layout.findViewById(R.id.ivBackspace);
        this.mIvBackspace.setOnClickListener(this);
        this.mBtOk = (Button) layout.findViewById(R.id.btOk);
        this.mBtOk.setOnClickListener(this);
        updateTime();
        return layout;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        if (v.getId() == R.id.ivBackspace) {
            clear();
            updateTime();
        } else {
            if (v.getId() == R.id.btOk) {
                ((TimePickerDialogListener) this.mListener).onTimePickerOk(TimeUtils.stringToTime(getStringFromNumbers()), this.mCallbackId);
                return;
            }
            this.mNumberList.addLast((Integer) v.getTag());
            this.mNumberList.removeFirst();
            updateTime();
        }
    }

    private void updateTime() {
        this.mTvTime.setText(getStringFromNumbers());
    }

    private String getStringFromNumbers() {
        StringBuilder sb = new StringBuilder();
        if (this.mNbDigits == 4) {
            sb.append(this.mNumberList.get(0)).append(this.mNumberList.get(1)).append(':').append(this.mNumberList.get(2)).append(this.mNumberList.get(3));
        } else if (this.mNbDigits == 5) {
            sb.append(this.mNumberList.get(0)).append(':').append(this.mNumberList.get(1)).append(this.mNumberList.get(2)).append(':').append(this.mNumberList.get(3)).append(this.mNumberList.get(4));
        }
        return sb.toString();
    }

    private void clear() {
        this.mNumberList.clear();
        for (int i = 0; i < this.mNbDigits; i++) {
            this.mNumberList.add(0);
        }
        updateTime();
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(1);
        return dialog;
    }

    @Override // fr.kazalox.android.gameclockdeluxe.views.DigitPad.DigitPadListener
    public void onDigitPadTouch(int number) {
        if (this.mFirstTime) {
            Log.d("ChessClock", "TimePickerDialog - onClick - clearingd");
            this.mFirstTime = false;
            clear();
        }
        this.mNumberList.addLast(Integer.valueOf(number));
        this.mNumberList.removeFirst();
        updateTime();
    }
}
