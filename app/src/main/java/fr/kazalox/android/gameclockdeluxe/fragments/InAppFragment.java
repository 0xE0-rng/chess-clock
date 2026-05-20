package fr.kazalox.android.gameclockdeluxe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import fr.kazalox.android.gameclockdeluxe.C;
import fr.kazalox.android.gameclockdeluxe.R;
import fr.kazalox.android.gameclockdeluxe.fragments.BaseFragment;

/* JADX INFO: loaded from: classes.dex */
public class InAppFragment extends BaseFragment implements View.OnClickListener {
    private Button mBtCancel;
    private Button mBtOk;
    private String mDescInApp;
    private TextView mTvInAppDesc;

    public interface InAppFragmentListener extends BaseFragment.BaseFragmentListener {
        void onInAppCancel();

        void onInAppOk();
    }

    public static InAppFragment newInstance(String desc) {
        InAppFragment f = new InAppFragment();
        Bundle b = new Bundle();
        b.putString(C.ARG_TEXT, desc);
        f.setArguments(b);
        return f;
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mDescInApp = getArguments().getString(C.ARG_TEXT);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_in_app, container, false);
        this.mTvInAppDesc = (TextView) layout.findViewById(R.id.tvInAppDesc);
        this.mTvInAppDesc.setText(this.mDescInApp);
        this.mBtCancel = (Button) layout.findViewById(R.id.btCancel);
        this.mBtCancel.setOnClickListener(this);
        this.mBtOk = (Button) layout.findViewById(R.id.btOk);
        this.mBtOk.setOnClickListener(this);
        return layout;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        if (v == this.mBtOk) {
            ((InAppFragmentListener) this.mListener).onInAppOk();
        } else if (v == this.mBtCancel) {
            ((InAppFragmentListener) this.mListener).onInAppCancel();
        }
    }
}
