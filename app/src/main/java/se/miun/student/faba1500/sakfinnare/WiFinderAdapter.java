package se.miun.student.faba1500.sakfinnare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.lang.ref.WeakReference;
import java.util.List;

class WiFinderAdapter extends RecyclerView.Adapter<WiFinderAdapter.ViewHolder> {
    private final WeakReference<Context> context;
    List<WiFinder> wiFinders;

    public WiFinderAdapter(List<WiFinder> wiFinders, WeakReference<Context> context) {
        this.wiFinders = wiFinders;
        this.context = context;
    }

    @Override
    public WiFinderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wifinder_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WiFinderAdapter.ViewHolder holder, int position) {
        holder.bssid.setText(wiFinders.get(position).getBssid());
        holder.key.setText(wiFinders.get(position).getKey());

        holder.connect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("ConnectAction", "Connect to " + holder.bssid.getText());

                WiFinder wiFinder = new WiFinder();
                wiFinder.setBssid(holder.bssid.getText().toString());
                wiFinder.setKey(holder.key.getText().toString());

                if (isChecked) {
                    wiFinder.setState(true, context);
                } else {
                    wiFinder.setState(false, context);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return wiFinders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bssid;
        public TextView key;
        public ToggleButton connect;

        public ViewHolder(View itemView) {
            super(itemView);
            bssid = itemView.findViewById(R.id.bssid);
            key = itemView.findViewById(R.id.key);
            connect = itemView.findViewById(R.id.connect);
        }
    }
}
