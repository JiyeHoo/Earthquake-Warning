package com.jiyehoo.informationentry.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jiyehoo.informationentry.R;
import com.qweather.sdk.bean.WarningBean;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author JiyeHoo
 * @description:
 * @date :2021/5/17 下午12:44
 */

public class DisasterAdapter extends RecyclerView.Adapter<DisasterAdapter.ViewHolder> {

    private Context context;
    private List<WarningBean.WarningBeanBase> baseList;

    public DisasterAdapter(List<WarningBean.WarningBeanBase> baseList) {
        this.baseList = baseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.disaster_list_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 加载数据到控件
        WarningBean.WarningBeanBase beanBase = baseList.get(position);

        // 标题，去除最后方括号内的内容
        String title = beanBase.getTitle();
        title = title.replaceAll("\\[.*?]", "");
        holder.mTvTitle.setText(title);

        // 发布时间，删除 T 和 最后的时区
        String pubTime = beanBase.getPubTime().substring(0, 10) +
                " " +
                beanBase.getPubTime().substring(11, 16);
        holder.mTvTime.setText(pubTime);

        // 预警等级，将字体颜色修改
        holder.mTvLevel.setText(beanBase.getLevel());
        switch (beanBase.getLevel()) {
            case "蓝色":
                holder.mTvLevel.setTextColor(Color.BLUE);
                break;
            case "黄色":
                holder.mTvLevel.setTextColor(Color.YELLOW);
                break;
            case "橙色":
                holder.mTvLevel.setTextColor(Color.parseColor("#FF5722"));
                break;
            case "红色":
                holder.mTvLevel.setTextColor(Color.RED);
                break;
            default:
                break;
        }

        // 预警内容，将末尾可能有的 $EOF 删除
        holder.mTvText.setText(beanBase.getText().replace("$EOF", ""));
    }

    @Override
    public int getItemCount() {
        return baseList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        // 控件声明
        View item;
        TextView mTvTitle;
        TextView mTvTime;
        TextView mTvLevel;
        TextView mTvText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 控件绑定
            item = itemView;
            mTvTitle = itemView.findViewById(R.id.tv_disaster_title);
            mTvTime = itemView.findViewById(R.id.tv_disaster_time);
            mTvLevel = itemView.findViewById(R.id.tv_disaster_level);
            mTvText = itemView.findViewById(R.id.tv_disaster_text);
        }
    }


}