package com.ywb.tuyue.ui.web;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.ywb.tuyue.R;
import com.ywb.tuyue.adapter.BookListAdapter;
import com.ywb.tuyue.adapter.OnItemClickListener;
import com.ywb.tuyue.bean.BookBean;
import com.ywb.tuyue.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by penghao on 2018/4/24.
 * description： 书吧浏览
 */

public class BookListFrag extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;

    BookListAdapter adapter;
    List<BookBean.ResultBean.BooksBeanX.BooksBean> bookBeans = new ArrayList<>();
    BookBean.ResultBean.BooksBeanX beanX;

    @Override
    protected int getViewId() {
        return R.layout.frag_book_list;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        beanX = (BookBean.ResultBean.BooksBeanX) getArguments().getSerializable("bookList");
        bookBeans.clear();
        if (beanX != null) {
            List<BookBean.ResultBean.BooksBeanX.BooksBean> books = beanX.getBooks();
            bookBeans.addAll(books);
            setEmptyViewShow(false);
        } else {
            setEmptyViewShow(true);
        }
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                int height = recyclerView.getHeight();
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 7));
//                recyclerView.addItemDecoration(new GridSpacingItemDecoration(7, 30, false));
                recyclerView.setAdapter(adapter = new BookListAdapter(mActivity, bookBeans));
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int pos) {
                        Intent intent = new Intent();
                        intent.putExtra("resourceUrl", bookBeans.get(pos).getBook_url());
                        intent.putExtra("title", bookBeans.get(pos).getName());
                        SeeBookActivity.Go(mActivity, SeeBookActivity.class, intent);
                    }
                });
            }
        });

    }

    private void setEmptyViewShow(boolean isShow) {
        emptyView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

}
