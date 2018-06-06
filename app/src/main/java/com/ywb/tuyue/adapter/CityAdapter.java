package com.ywb.tuyue.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.City;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.utils.LoadImage;

import java.util.List;

/**
 * Created by penghao on 2017/12/22.
 * description：
 */

public class CityAdapter extends BaseRecycleViewAdapter<City.ResultBean> {
    float height;

    public CityAdapter(Context c, List<City.ResultBean> mList, float height) {
        super(c, mList);
        this.height = height;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = resIdToView(parent, R.layout.item_city);
        ViewGroup.LayoutParams params = contentView.getLayoutParams();
        params.height = (int) height;
        contentView.setLayoutParams(params);
        return new CityViewHolder(contentView);
    }

    private class CityViewHolder extends BaseViewHolder {
        ImageView cityPic;
        TextView cityName, cityEnglish;

        public CityViewHolder(View itemView) {
            super(itemView);
            cityPic = itemView.findViewById(R.id.city_pic);
            cityName = itemView.findViewById(R.id.city_name);
            cityEnglish = itemView.findViewById(R.id.city_E_name);

        }

        @Override
        public void onBind() {
            cityName.setText(mList.get(position).getName());
            cityEnglish.setText(mList.get(position).getPinyin());
            String localUrl = MyDownLoadManager.getLocalUrl(mList.get(position).getImg_url());
            Log.d("onBind__", "本地: " + localUrl + "url--->" + mList.get(position).getImg_url());
            LoadImage.loadImageNormal(localUrl, cityPic);

//            cityPic.setImageURI(Uri.fromFile(new File(DataBase.getString(mList.get(position).getImg_url()))));
        }
    }
}
