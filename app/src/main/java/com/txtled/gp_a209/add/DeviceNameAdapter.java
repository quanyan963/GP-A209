package com.txtled.gp_a209.add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.widget.ArialRoundTextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr.Quan on 2020/3/18.
 */
public class DeviceNameAdapter extends RecyclerView.Adapter<DeviceNameAdapter.DeviceNameHolder> {
    private Context context;
    private List<String> data;
    private OnNameClickListener listener;
    private int mPosition = -1;

    public DeviceNameAdapter(Context context, OnNameClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(String[] d) {
        data = Arrays.asList(context.getResources().getStringArray(R.array.friendlyName));
        if (d != null){
            for (int i = 0; i < d.length; i++) {
                for (int j = 0; j < data.size(); j++) {
                    if (d[i].equals(data.get(j))){
                        data.remove(j);
                        return;
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceNameAdapter.DeviceNameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_name, parent, false);
        return new DeviceNameHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceNameAdapter.DeviceNameHolder holder, int position) {
        if (data != null){
            holder.atvDeviceName.setText(data.get(position));
            if (mPosition == position){
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            }else if (mPosition != -1){
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
            holder.itemView.setOnClickListener(v -> {
                listener.onNameClick(data.get(position));
                if (mPosition != -1)
                    notifyItemChanged(mPosition);
                mPosition = position;
                notifyItemChanged(position);

            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class DeviceNameHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.atv_device_name)
        ArialRoundTextView atvDeviceName;
        public DeviceNameHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnNameClickListener{
        void onNameClick(String name);
    }
}
