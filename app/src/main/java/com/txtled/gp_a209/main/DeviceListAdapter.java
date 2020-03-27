package com.txtled.gp_a209.main;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.bean.DeviceInfo;
import com.txtled.gp_a209.bean.WWADeviceInfo;
import com.txtled.gp_a209.login.LoginActivity;
import com.txtled.gp_a209.widget.ArialRoundTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr.Quan on 2020/3/16.
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListHolder> {
    private Context context;
    private List<WWADeviceInfo> data;
    private OnDeviceClickListener listener;
    private String userId;
    private List<String> nameList;

    public DeviceListAdapter(Context context,OnDeviceClickListener listener, String userId) {
        this.context = context;
        this.listener = listener;
        this.userId = userId;
        nameList = new ArrayList<>();
    }

    public void setData(List<WWADeviceInfo> data){
        if (this.data == null || this.data.isEmpty()){
            this.data = data;
        }else {
            for (int i = 0; i < this.data.size(); i++) {
                for (int j = 0; j < data.size(); j++) {
                    if (this.data.get(i).getThing().equals(data.get(j).getThing())){
                        data.set(j,this.data.get(i));
                        continue;
                    }
                }
            }
            this.data = data;
        }
        
        notifyDataSetChanged();
    }

    public void setDiscoveryData(List<WWADeviceInfo> discovery){
        if (data == null || data.isEmpty()){
            data = discovery;
            notifyDataSetChanged();
        }else {
            for (int i = 0; i < discovery.size(); i++) {
                for (int j = 0; j < data.size(); j++) {
                    if (discovery.get(i).getThing().equals(data.get(j).getThing())){
                        data.set(j,discovery.get(i));
                        notifyItemChanged(j);
                        continue;
                    }
                }
            }
        }
    }

    public void deleteItem(String name){
        int position = nameList.indexOf(name);
        nameList.remove(position);
        data.remove(position);
        notifyItemRemoved(position);
        if (position != data.size())
            notifyItemRangeChanged(position,data.size() - position);
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
            String names = data.get(position).getFriendlyNames();
            String[] name = names.split(",");
            for (int i = 0; i < name.length; i++) {
                if (name[i].contains(userId)){
                    nameList.add(name[i].split("_")[1]);
                    String same = "";
                    if (data.get(position).getIp() != null){
                        same = " (in same network)";
                    }
                    holder.atvDeviceName.setText(name[i].split("_")[1] + same);
                    SpannableStringBuilder stringBuilder =
                            new SpannableStringBuilder(holder.atvDeviceName.getText());
                    stringBuilder.setSpan(new ForegroundColorSpan(context.getResources()
                            .getColor(R.color.yellow)), name[i].split("_")[1].length(),
                            holder.atvDeviceName.getText().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    holder.atvDeviceName.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.atvDeviceName.setText(stringBuilder);
                }
            }
            holder.itemView.setOnClickListener(v ->
                    listener.onDeviceClick(data.get(position).getThing(),nameList.get(position)));
            holder.imgSetting.setOnClickListener(v ->
                    listener.onSettingClick(data.get(position), nameList.get(position)));
            holder.imgDelete.setOnClickListener(v ->
                    listener.onDeleteClick(data.get(position), nameList.get(position)));
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
        void onSettingClick(WWADeviceInfo data, String name);
        void onDeleteClick(WWADeviceInfo data,String name);
    }
}
