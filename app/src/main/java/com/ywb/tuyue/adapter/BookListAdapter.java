package com.ywb.tuyue.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ywb.tuyue.R;
import com.ywb.tuyue.bean.BookBean;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.utils.LoadImage;

import java.util.List;

/**
 * Created by penghao on 2018/4/24.
 * description：
 */

public class BookListAdapter extends BaseRecycleViewAdapter<BookBean.ResultBean.BooksBeanX.BooksBean> {

    float height;

    public BookListAdapter(Context c, List<BookBean.ResultBean.BooksBeanX.BooksBean> mList) {
        super(c, mList);
//        this.height = height;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = resIdToView(parent, R.layout.item_book_list);
//        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
//        layoutParams.height = (int) height;
//        contentView.setLayoutParams(layoutParams);
        return new BookViewHolder(contentView);
    }

    private class BookViewHolder extends BaseViewHolder {
        ImageView pic;
        TextView title, sTitle;

        public BookViewHolder(View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.book_pic);
            title = itemView.findViewById(R.id.book_text);
            sTitle = itemView.findViewById(R.id.book_text_small);
        }

        @Override
        public void onBind() {
            BookBean.ResultBean.BooksBeanX.BooksBean booksBean = mList.get(position);
            if (booksBean != null) {
                String localUrl = MyDownLoadManager.getLocalUrl(booksBean.getImg());
                LoadImage.loadImageNormal(localUrl, pic);
                title.setText(booksBean.getName());
                sTitle.setText(booksBean.getAuthor());
            }

        }
    }
}
