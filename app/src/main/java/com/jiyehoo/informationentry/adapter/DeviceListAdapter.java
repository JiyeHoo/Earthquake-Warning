package com.jiyehoo.informationentry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jiyehoo.informationentry.R;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {

    private Context context;
    private List<DeviceBean> deviceBeanList;

    public DeviceListAdapter(List<DeviceBean> deviceBeanList) {
        this.deviceBeanList = deviceBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.device_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 加载数据到控件
        DeviceBean deviceBean = deviceBeanList.get(position);
        holder.mTvDeviceName.setText(deviceBean.getName());
        Glide.with(context).load(deviceBean.getIconUrl()).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return deviceBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // 控件声明
        TextView mTvDeviceName;
        ImageView mImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 控件绑定
            mTvDeviceName = itemView.findViewById(R.id.tv_item_device_name);
            mImageView = itemView.findViewById(R.id.iv_item_device_icon);
        }
    }
}
