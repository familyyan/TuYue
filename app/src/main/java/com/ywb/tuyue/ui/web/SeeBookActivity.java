package com.ywb.tuyue.ui.web;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bifan.txtreaderlib.main.TxtReaderView;
import com.bifan.txtreaderlib.ui.HwTxtPlayActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.shockwave.pdfium.PdfDocument;
import com.ywb.tuyue.R;
import com.ywb.tuyue.db.DataBase;
import com.ywb.tuyue.downLoad.MyDownLoadManager;
import com.ywb.tuyue.ui.BaseActivity;
import com.ywb.tuyue.widget.HeaderView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by penghao on 2018/4/24.
 * description：
 */

public class SeeBookActivity extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener {
    private static final String TAG = "SeeBookActivityc";
    @BindView(R.id.title)
    HeaderView title;
    @BindView(R.id.pdfView)
    PDFView pdfView;
    //    @BindView(R.id.scrollView)
//    ScrollView scrollView;
//    @BindView(R.id.content)
//    TextView content;
    @BindView(R.id.last_page)
    TextView last_page;
    @BindView(R.id.next_page)
    TextView next_page;
    @BindView(R.id.show_qq)
    TextView show_qq;
    @BindView(R.id.activity_hwtxtplay_readerView)
    TxtReaderView mTxtReaderView;

    private static final String URI = "/storage/emulated/0/Android/data/com.ywb.tuyue/files/Download/";
    int defaultPage;//pdf 上次阅读位置
    String resourceUrl, pdfTitle;

    int lastPage;//txt 上次阅读位置
    private Handler handler = new Handler();
    //    private ProcessText processText;
    private int currentPage = 1;

    @Override
    protected int getViewId() {
        return R.layout.activity_see_book;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView(Bundle savedInstanceState) {
        resourceUrl = getIntent().getExtras().getString("resourceUrl");
        pdfTitle = getIntent().getExtras().getString("title");
        if (!TextUtils.isEmpty(resourceUrl)) {
            if (resourceUrl.endsWith("txt")) {
                initTxT();
            } else {
                initPDF();
            }
        }

    }

    private void initPDF() {
        pdfView.setVisibility(View.VISIBLE);
//        scrollView.setVisibility(View.GONE);
        next_page.setVisibility(View.GONE);
        last_page.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(pdfTitle)) {
            title.setTitle(pdfTitle);
        }
        if (!TextUtils.isEmpty(resourceUrl)) {
            String localUrl = MyDownLoadManager.getLocalUrl(resourceUrl);
            if (!TextUtils.isEmpty(localUrl)) {
                File file = new File(localUrl);
                if (file.exists()) {
                    setPDFView(file);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initTxT() {
        pdfView.setVisibility(View.GONE);
//        scrollView.setVisibility(View.VISIBLE);
        lastPage = DataBase.getInt(pdfTitle);
        next_page.setVisibility(View.VISIBLE);
        last_page.setVisibility(View.VISIBLE);
        show_qq.setText(R.string.ad_qq);
        if (lastPage != 0) {
            currentPage = lastPage;
        }
        Log.d(TAG, "initTxT: " + lastPage);
//        mTxtReaderView.setTextSize(14);
//        mTxtReaderView.setStyle(R.color.white, R.color.my_black);
//        TxtConfig.saveIsShowSpecialChar(this, false);
        try {
            if (!TextUtils.isEmpty(resourceUrl)) {
                String localUrl = MyDownLoadManager.getLocalUrl(resourceUrl);
                if (!TextUtils.isEmpty(localUrl)) {
                    File file = new File(localUrl);
                    if (file.exists()) {
//                        mTxtReaderView.loadTxtFile(file.getPath(), new ILoadListener() {
//                            @Override
//                            public void onSuccess() {
//                                //加载成功回调
//                                Toast.makeText(mContext, "加载成功", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onFail(TxtMsg txtMsg) {
//                                Toast.makeText(mContext, "加载失败：" + txtMsg, Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onMessage(String s) {
//
//                            }
//                        });
                        HwTxtPlayActivity.loadTxtFile(this, file.getPath(), pdfTitle);
                        finish();
//                        processText = new ProcessText(file);
//                        lastPage();
//                        long pages = processText.getPages();
//                        if (pages <= 1) {
//                            pages = 1;
//                        }
//                        setName(pdfTitle + "(" + currentPage + "/" + pages + ")");
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        ScrollBindHelper.bind(seekBar, scrollView);
    }

    private void setName(String name) {
        if (!TextUtils.isEmpty(name)) {
            title.setTitle(name);
        }
    }

    @OnClick({R.id.last_page, R.id.next_page})
    public void onViewClicked(View view) {
        int mode = 0;
        switch (view.getId()) {
            case R.id.last_page:
                mode = ScrollView.FOCUS_DOWN;//滚动到底部
                currentPage--;
//                lastPage();
                break;
            case R.id.next_page:
                mode = ScrollView.FOCUS_UP;//滚动到顶部
                currentPage++;
//                nextPage();
                break;
        }
//        int finalMode = mode;
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                scrollView.fullScroll(finalMode);
//            }
//        });
//        long totalPage = processText.getPages();
//        if (currentPage <= 1) {
//            currentPage = 1;
//        }
//        if (currentPage > totalPage) {
//            if (totalPage <= 1) {
//                totalPage = 1;
//            }
//            currentPage = (int) totalPage;
//        }
//        setName(pdfTitle + "(" + currentPage + "/" + totalPage + ")");
    }

//    private void lastPage() {
//        String pre = processText.getPre(currentPage);
//        if (!TextUtils.isEmpty(pre)) {
//            content.setText(pre);
//        }
//    }
//
//    private void nextPage() {
//        String next = processText.getNext(currentPage);
//        if (!TextUtils.isEmpty(next)) {
//            content.setText(next);
//        }
//    }


    private void setPDFView(File file) {
        defaultPage = DataBase.getInt(resourceUrl);
        pdfView.fromFile(file)//
                .defaultPage(defaultPage)//
                .enableAnnotationRendering(true)//
                .spacing(10)//
                .onLoad(this)//
                .onPageChange(this)//
                .load();//

    }

    @Override
    protected void setHeader() {
        super.setHeader();
        title.setRightBtnVisiable(View.GONE);
        title.setLeftBtnClickListsner(onBackClickListener);
    }

    @Override
    protected void onDestroy() {
        title.unRegister();
        DataBase.saveInt(resourceUrl, defaultPage);
        DataBase.saveInt(pdfTitle, currentPage);
        super.onDestroy();
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.e(TAG, "modDate = " + meta.getModDate());

        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        defaultPage = page;
        title.setTitle(String.format("%s %s / %s", pdfTitle, page + 1, pageCount));
    }

}
