package com.jiyehoo.informationentry.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jiyehoo.informationentry.R;
import com.qweather.sdk.bean.WarningBean;
import com.tuya.smart.sdk.bean.message.MessageBean;

import java.util.List;

/**
 * @author JiyeHoo
 * @description:
 * @date :2021/5/17 下午12:44
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private Context context;
    private final List<MessageBean> beanList;
    private OnNoticeItemClickListener onNoticeItemClickListener;


    public NoticeAdapter(List<MessageBean> beanList) {
        this.beanList = beanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.notice_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 加载数据到控件
        MessageBean messageBean = beanList.get(position);

        String type = messageBean.getMsgTypeContent();
        if ("家庭消息".equals(type)) {
            type = "设备消息";
        }
        holder.mTvType.setText(type);

        String time = messageBean.getDateTime();
        holder.mTvTime.setText(time);

        String text = messageBean.getMsgContent();
        holder.mTvText.setText(text);

        // 设置点击、长按
        holder.item.setOnClickListener(v -> {
           if (onNoticeItemClickListener != null) {
               onNoticeItemClickListener.onItemClick(position);
           }
        });

        holder.item.setOnLongClickListener(v -> {
            if (onNoticeItemClickListener != null) {
                onNoticeItemClickListener.onItemLongClick(position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        // 控件声明
        View item;
        TextView mTvType;
        TextView mTvTime;
        TextView mTvText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 控件绑定
            item = itemView;
            mTvType = itemView.findViewById(R.id.tv_notice_title);
            mTvTime = itemView.findViewById(R.id.tv_notice_time);
            mTvText = itemView.findViewById(R.id.tv_notice_text);
        }
    }

    public void setOnNoticeItemClickListener(OnNoticeItemClickListener onNoticeItemClickListener) {
        this.onNoticeItemClickListener = onNoticeItemClickListener;
    }
}