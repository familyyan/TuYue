package com.ywb.tuyue.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.Movie;
import com.ywb.tuyue.utils.DensityUtil;
import com.ywb.tuyue.widget.CardMovie;

import java.util.List;

/**
 * Created by mhdt on 2017/12/16.
 * 电影浏览适配器
 */
public class MovieScanAdapter extends BaseRecycleViewAdapter<Movie.ResultBean> {
    int itemHeight;

    public MovieScanAdapter(Context c, List<Movie.ResultBean> mList, int itemHeight) {
        super(c, mList);
        this.itemHeight = itemHeight - DensityUtil.dip2px(30);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = resIdToView(parent, R.layout.item_movie);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = itemHeight;
        view.setLayoutParams(params);
        return new MovieScanViewHolder(view);
    }

    private class MovieScanViewHolder extends BaseViewHolder<CardMovie> {
        CardMovie cardMovie;

        public MovieScanViewHolder(View itemView) {
            super(itemView);
            cardMovie = itemView.findViewById(R.id.carMovie);
        }

        @Override
        public void onBind() {
            Log.d("LoadImage","onBind_____"+position+"____"+mList.get(position).toString());
            cardMovie.setMovieBean(mList.get(position));
        }
    }
}
