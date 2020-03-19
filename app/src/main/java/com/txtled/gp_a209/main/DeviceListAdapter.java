package com.txtled.gp_a209.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.bean.DeviceInfo;
import com.txtled.gp_a209.widget.ArialRoundTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr.Quan on 2020/3/16.
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListHolder> {
    private Context context;
    private List<DeviceInfo> data;
    private OnDeviceClickListener listener;

    public DeviceListAdapter(Context context,OnDeviceClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(List<DeviceInfo> data){
        this.data = null;
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceListAdapter.DeviceListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_device, parent, false);
        return new DeviceListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceListAdapter.DeviceListHolder holder, int position) {
        if (data != null){
            holder.atvDeviceName.setText(data.get(position).getDeviceName());
            holder.itemView.setOnClickListener(v ->
                    listener.onDeviceClick(data.get(position).getEndpoint(),data.get(position).getDeviceName()));
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class DeviceListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.atv_device_name)
        ArialRoundTextView atvDeviceName;
        @BindView(R.id.img_setting)
        ImageView imgSetting;
        @BindView(R.id.img_delete)
        ImageView imgDelete;

        public DeviceListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnDeviceClickListener{
        void onDeviceClick(String endpoint, String name);
    }
}
