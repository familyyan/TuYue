package com.ywb.tuyue.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.MusicBean;

import java.util.List;

/**
 * Created by penghao on 2018/4/26.
 * description：
 */

public class MusicAdapter extends BaseRecycleViewAdapter<MusicBean.ResultBean> {

    int currentIndex = -1;

    public MusicAdapter(Context c, List<MusicBean.ResultBean> mList) {
        super(c, mList);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void updateIndex(int index) {
        currentIndex = index;
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = resIdToView(parent, R.layout.item_music);
        return new MyViewHolder(contentView);
    }

    public class MyViewHolder extends BaseViewHolder {

        TextView name, actor, time;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.music_name);
            actor = itemView.findViewById(R.id.music_actor);
            time = itemView.findViewById(R.id.music_time);
        }

        @Override
        public void onBind() {
            MusicBean.ResultBean resultBean = mList.get(position);
            if (resultBean != null) {
                name.setText(resultBean.getName() + "");
                actor.setText(resultBean.getSinger() + "");
                time.setText(resultBean.getDuration() + "");

                int myRed = c.getResources().getColor(R.color.my_red);
                int myBlack = c.getResources().getColor(R.color.little_black);

                if (currentIndex == position) {
                    name.setTextColor(myRed);
                    actor.setTextColor(myRed);
                    time.setTextColor(myRed);
                } else {
                    name.setTextColor(myBlack);
                    actor.setTextColor(myBlack);
                    time.setTextColor(myBlack);
                }
            }

        }

    }
}
