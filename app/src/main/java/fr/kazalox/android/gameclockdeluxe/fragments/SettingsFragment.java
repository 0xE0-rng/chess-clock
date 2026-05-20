package fr.kazalox.android.gameclockdeluxe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import fr.kazalox.android.gameclockdeluxe.C;
import fr.kazalox.android.gameclockdeluxe.R;
import fr.kazalox.android.gameclockdeluxe.data.Mode;
import fr.kazalox.android.gameclockdeluxe.fragments.BaseFragment;
import fr.kazalox.android.gameclockdeluxe.utils.TimeUtils;

/* JADX INFO: loaded from: classes.dex */
public class SettingsFragment extends BaseFragment implements View.OnClickListener {
    private Button mBtIncrement;
    private Button mBtIncrement2;
    private Button mBtMode;
    private Button mBtNbMoves;
    private Button mBtTimeClock1;
    private Button mBtTimeClock2;
    private Button mBtTimePeriod2;
    private View mLayoutClock2;
    private View mLayoutIncrement;
    private View mLayoutIncrement2;
    private View mLayoutNbMoves;
    private View mLayoutPeriod2;
    private Mode mMode;
    private TextView mTvClock1;

    public interface SettingsFragmentListener extends BaseFragment.BaseFragmentListener {
        void onSettingsClock1Click();

        void onSettingsClock2Click();

        void onSettingsIncrement();

        void onSettingsIncrement2();

        void onSettingsModeClick();

        void onSettingsNbMoves();

        void onSettingsPeriod2();
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mMode = (Mode) getArguments().getSerializable(C.ARG_MODE);
        setRetainInstance(true);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_time_settings, container, false);
        this.mBtMode = (Button) layout.findViewById(R.id.btMode);
        this.mBtMode.setText(this.mMode.getName());
        this.mBtMode.setOnClickListener(this);
        this.mTvClock1 = (TextView) layout.findViewById(R.id.tvClock1);
        this.mBtTimeClock1 = (Button) layout.findViewById(R.id.btTimeClock1);
        this.mBtTimeClock1.setOnClickListener(this);
        this.mLayoutClock2 = layout.findViewById(R.id.layoutClock2);
        this.mBtTimeClock2 = (Button) layout.findViewById(R.id.btTimeClock2);
        this.mBtTimeClock2.setOnClickListener(this);
        this.mLayoutIncrement = layout.findViewById(R.id.layoutIncrement);
        this.mBtIncrement = (Button) layout.findViewById(R.id.btIncrement);
        this.mBtIncrement.setOnClickListener(this);
        this.mLayoutIncrement2 = layout.findViewById(R.id.layoutIncrement2);
        this.mBtIncrement2 = (Button) layout.findViewById(R.id.btIncrement2);
        this.mBtIncrement2.setOnClickListener(this);
        this.mLayoutNbMoves = layout.findViewById(R.id.layoutNbMoves);
        this.mBtNbMoves = (Button) layout.findViewById(R.id.btNbMoves);
        this.mBtNbMoves.setOnClickListener(this);
        this.mLayoutPeriod2 = layout.findViewById(R.id.layoutPeriod2);
        this.mBtTimePeriod2 = (Button) layout.findViewById(R.id.btTimePeriod2);
        this.mBtTimePeriod2.setOnClickListener(this);
        update();
        return layout;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        if (v == this.mBtMode) {
            ((SettingsFragmentListener) this.mListener).onSettingsModeClick();
            return;
        }
        if (v == this.mBtTimeClock1) {
            ((SettingsFragmentListener) this.mListener).onSettingsClock1Click();
            return;
        }
        if (v == this.mBtTimeClock2) {
            ((SettingsFragmentListener) this.mListener).onSettingsClock2Click();
            return;
        }
        if (v == this.mBtIncrement) {
            ((SettingsFragmentListener) this.mListener).onSettingsIncrement();
            return;
        }
        if (v == this.mBtNbMoves) {
            ((SettingsFragmentListener) this.mListener).onSettingsNbMoves();
        } else if (v == this.mBtTimePeriod2) {
            ((SettingsFragmentListener) this.mListener).onSettingsPeriod2();
        } else if (v == this.mBtIncrement2) {
            ((SettingsFragmentListener) this.mListener).onSettingsIncrement2();
        }
    }

    public void updateMode(Mode mode) {
        this.mMode = mode;
        update();
    }

    private void update() {
        this.mLayoutIncrement.setVisibility((!this.mMode.hasDelay() || this.mMode.getId() == 6) ? 8 : 0);
        this.mTvClock1.setText(this.mMode.getId() == 6 ? R.string.settings_period1 : R.string.settings_clock1);
        this.mLayoutClock2.setVisibility(this.mMode.getId() != 6 ? 0 : 8);
        this.mLayoutNbMoves.setVisibility(this.mMode.getId() == 6 ? 0 : 8);
        this.mLayoutPeriod2.setVisibility(this.mMode.getId() == 6 ? 0 : 8);
        this.mLayoutIncrement2.setVisibility(this.mMode.getId() != 6 ? 8 : 0);
        updateViews();
    }

    public void updateViews() {
        this.mBtMode.setText(this.mMode.getName());
        this.mBtTimeClock1.setText(TimeUtils.timeToString(this.mMode.getTime1()));
        this.mBtTimeClock2.setText(TimeUtils.timeToString(this.mMode.getTime2()));
        this.mBtIncrement.setText(TimeUtils.timeToString(this.mMode.getDelay()));
        this.mBtNbMoves.setText(String.valueOf(this.mMode.getNbMoves()));
        this.mBtTimePeriod2.setText(TimeUtils.timeToString(this.mMode.getPeriod2()));
        this.mBtIncrement2.setText(TimeUtils.timeToString(this.mMode.getDelay()));
    }
}
