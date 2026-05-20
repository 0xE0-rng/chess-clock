package fr.kazalox.android.gameclockdeluxe.data;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.kazalox.android.gameclockdeluxe.R;
import java.util.List;

public class ModeAdapter extends ArrayAdapter<Mode> implements View.OnClickListener {
    private final Activity mContext;
    private ModeAdapterListener mListener;
    private final List<Mode> mModes;

    public interface ModeAdapterListener {
        void onModeAdapterHelp(Mode mode);
    }

    static class ViewHolder {
        public ImageView ivHelp;
        public TextView tv;

        ViewHolder() {
        }
    }

    public ModeAdapter(Activity context, ModeAdapterListener listener, List<Mode> modes) {
        super(context, R.layout.layout_mode, modes);
        this.mContext = context;
        this.mListener = listener;
        this.mModes = modes;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = this.mContext.getLayoutInflater();
            rowView = inflater.inflate(R.layout.layout_mode, (ViewGroup) null);
            ViewHolder holder = new ViewHolder();
            holder.tv = (TextView) rowView.findViewById(R.id.tv);
            holder.ivHelp = (ImageView) rowView.findViewById(R.id.ivHelp);
            rowView.setTag(holder);
        }
        ViewHolder holder2 = (ViewHolder) rowView.getTag();
        Mode mode = this.mModes.get(position);
        holder2.tv.setText(this.mContext.getString(mode.getName()));
        holder2.ivHelp.setImageResource(R.drawable.ic_action_help);
        holder2.ivHelp.setTag(mode);
        holder2.ivHelp.setOnClickListener(this);
        return rowView;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        this.mListener.onModeAdapterHelp((Mode) v.getTag());
    }
}
