package com.ywb.tuyue.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ywb.tuyue.AppConfig;
import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.WifiBean;
import com.ywb.tuyue.utils.WifiSupport;

import java.util.List;


/**
 * Created by ${GuoZhaoHui} on 2017/11/7.
 * Email:guozhaohui628@gmail.com
 */

public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.MyViewHolder> {

    private Context mContext;
    private List<WifiBean> resultList;
    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(WifiListAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public WifiListAdapter(Context mContext, List<WifiBean> resultList) {
        this.mContext = mContext;
        this.resultList = resultList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_wifi_list, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WifiBean bean = resultList.get(position);
        String capabilities = bean.getCapabilities();//加密方式
        holder.tvItemWifiName.setText(bean.getWifiName());
        holder.tvItemWifiStatus.setText(bean.getState());
        int level = Integer.parseInt(bean.getLevel());
        Log.d("onBindViewHolder", "加密方式" + capabilities);
        String capabi = "";
        if (capabilities.contains("WPA-")) {
            capabi = "WPA";
        }
        if (capabilities.contains("WPA2-")) {
            if (!TextUtils.isEmpty(capabi)) {
                capabi = capabi + "/";
            }
            capabi = capabi + "WPA2";
        }
        holder.tv_item_wifi_safe.setText(capabi);
        //无需密码
        if (WifiSupport.getWifiCipher(bean.getCapabilities()) == WifiSupport.WifiCipherType.WIFICIPHER_NOPASS) {
            holder.wifi_level.setImageResource(getWifiIcon(level, 0));
        } else {
            holder.wifi_level.setImageResource(getWifiIcon(level, 1));
        }
        //可以传递给adapter的数据都是经过处理的，已连接或者正在连接状态的wifi都是处于集合中的首位，所以可以写出如下判断
        if (position == 0 && (AppConfig.WIFI_STATE_ON_CONNECTING.equals(bean.getState()) || AppConfig.WIFI_STATE_CONNECT.equals(bean.getState()))) {
            holder.llshow.setVisibility(View.VISIBLE);
            if (AppConfig.WIFI_STATE_CONNECT.equals(bean.getState())) {
                holder.choose.setVisibility(View.VISIBLE);
            } else {
                holder.choose.setVisibility(View.GONE);
            }
        } else {
            holder.llshow.setVisibility(View.GONE);
            holder.choose.setVisibility(View.GONE);
        }

        holder.itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position, bean);
            }
        });
    }

    public void replaceAll(List<WifiBean> datas) {
        if (resultList.size() > 0) {
            resultList.clear();
        }
        resultList.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {

        View itemview;
        TextView tvItemWifiName, tvItemWifiStatus, tv_item_wifi_safe;
        LinearLayout llshow;
        ImageView wifi_level, choose;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            tvItemWifiName = itemView.findViewById(R.id.tv_item_wifi_name);
            tvItemWifiStatus = itemView.findViewById(R.id.tv_item_wifi_status);
            llshow = itemView.findViewById(R.id.ll_show_collection);
            wifi_level = itemView.findViewById(R.id.wifi_level);
            choose = itemView.findViewById(R.id.iv_choose);
            tv_item_wifi_safe = itemView.findViewById(R.id.tv_item_wifi_safe);
        }

    }


    private int getWifiIcon(int level, int type) {
        int resId = 0;
        switch (level) {//信号等级
            case 1://强
                resId = type == 0 ? R.drawable.icon_wifi1 : R.drawable.icon_swifi1;
                break;
            case 2://较强
                resId = type == 0 ? R.drawable.icon_wifi3 : R.drawable.icon_swifi2;
                break;
            case 3://较弱
            case 4://弱
                resId = type == 0 ? R.drawable.icon_wifi2 : R.drawable.icon_swifi3;
                break;
        }
        return resId;
    }

    public interface onItemClickListener {
        void onItemClick(View view, int postion, Object o);
    }

}
