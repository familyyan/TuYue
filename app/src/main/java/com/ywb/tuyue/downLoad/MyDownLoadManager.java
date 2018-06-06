package com.ywb.tuyue.downLoad;

import android.app.Activity;
import android.app.Application;
import android.app.DownloadManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.ywb.tuyue.db.SharedPreferenceHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by mhdt on 2018/2/7.
 */

public class MyDownLoadManager {
    private static Application app;
    public static DownloadManager.Query query;
    public static DownloadManager downloadManager;

    /**
     * 获取查询器
     *
     * @return
     */
    public static DownloadManager.Query getQuery() {
        return query;
    }

    /**
     * 获取下载管理器
     *
     * @return
     */
    public static DownloadManager getDownloadManager() {
        return downloadManager;
    }


    public static void init(Application application) {
        app = application;
        downloadManager = (DownloadManager) application.getSystemService(Activity.DOWNLOAD_SERVICE);
        query = new DownloadManager.Query();
    }

    /**
     * 获取连接的下载id
     *
     * @param downLoadUrl
     * @return
     */
    public static long getDownLoadId(String downLoadUrl) {
        return SharedPreferenceHelper.getLong(downLoadUrl);
    }


    /**
     * 任务是否已存在
     *
     * @param taskId
     * @return
     */
    public static boolean isDownLoadTaskExit(long taskId) {
        DownLoadEntity downLoadEntity = getDownLoadEntity(taskId);
        if (downLoadEntity != null) {
            return true;
        }
        return false;
    }


    /**
     * 通过id获取下载信息
     *
     * @param downLoadId
     * @return
     */
    public static DownLoadEntity getDownLoadEntity(long downLoadId) {
        DownLoadEntity downLoadEntity = null;

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downLoadId);
        Cursor cursor = downloadManager.query(query);

        if (cursor != null && cursor.moveToFirst() && downLoadId != 0) {
            long COLUMN_ID = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
            int COLUMN_STATUS = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            int COLUMN_BYTES_DOWNLOADED_SO_FAR = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int COLUMN_TOTAL_SIZE_BYTES = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            String COLUMN_LOCAL_URI = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            String COLUMN_TITLE = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
            String COLUMN_REASON = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
            String COLUMN_DESCRIPTION = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));
            String COLUMN_MEDIA_TYPE = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));

            downLoadEntity = new DownLoadEntity();
            downLoadEntity.setCOLUMN_ID(COLUMN_ID);
            downLoadEntity.setCOLUMN_STATUS(COLUMN_STATUS);
            downLoadEntity.setCOLUMN_BYTES_DOWNLOADED_SO_FAR(COLUMN_BYTES_DOWNLOADED_SO_FAR);
            downLoadEntity.setCOLUMN_TOTAL_SIZE_BYTES(COLUMN_TOTAL_SIZE_BYTES);
            downLoadEntity.setCOLUMN_LOCAL_URI(COLUMN_LOCAL_URI);
            downLoadEntity.setCOLUMN_TITLE(COLUMN_TITLE);
            downLoadEntity.setCOLUMN_REASON(COLUMN_REASON);
            downLoadEntity.setCOLUMN_DESCRIPTION(COLUMN_DESCRIPTION);
            downLoadEntity.setCOLUMN_MEDIA_TYPE(COLUMN_MEDIA_TYPE);
            cursor.close();
        }

        return downLoadEntity;
    }

    /**
     * 通过下载地址获取下载信息
     *
     * @param downLoadUrl
     * @return
     */
    public static DownLoadEntity getDownLoadEntity(String downLoadUrl) {
        long downLoadId = getDownLoadId(downLoadUrl);
        if (downLoadId != 0) {
            DownLoadEntity downLoadEntity = getDownLoadEntity(downLoadId);
            if (downLoadEntity != null) {
                return downLoadEntity;
            }
        }
        return null;
    }

    /**
     * 通过网络地址获取本地地址
     *
     * @param downLoadUrl
     * @return
     */
    public static String getLocalUrl(String downLoadUrl) {
        DownLoadEntity downLoadEntity = getDownLoadEntity(downLoadUrl);
        if (downLoadEntity != null && downLoadEntity.getCOLUMN_STATUS() == DownloadManager.STATUS_SUCCESSFUL) {
            String localUrl = downLoadEntity.getCOLUMN_LOCAL_URI();
            if (!TextUtils.isEmpty(localUrl)) {
                try {
                    localUrl = URLDecoder.decode(localUrl, "utf-8");
                    localUrl = localUrl.replace("file://", "");
                    return localUrl;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return downLoadUrl;
    }

    /**
     * 移除任务(取消任务，删除文件)
     *
     * @param taskId 任务id
     * @return
     */
    public static int removeTask(long... taskId) {
        return downloadManager.remove(taskId);
    }


    /**
     * 移除任务(取消任务，删除文件)
     *
     * @param url 下载地址
     * @return
     */
    public static int removeTask(String... url) {
        int count = 0;
        for (String r : url) {
            long id = getDownLoadId(r);
            if (id != 0) {
                count += downloadManager.remove(id);
            }
        }
        return count;
    }


    public static DownloadManager.Request buildRequest(DownLoadTask downLoadTask) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downLoadTask.getDownLoadUrl()));

        request.setDestinationInExternalFilesDir(app, Environment.DIRECTORY_DOWNLOADS, downLoadTask.getFileName());

        //net
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);

        // TODO: 2018/2/6 通知栏
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        return request;
    }


    public static String getStatuDesc(int statu) {
        String desc = "NONE";
        switch (statu) {
            case DownloadManager.STATUS_FAILED:
                desc = "STATUS_FAILED";
                break;
            case DownloadManager.STATUS_PAUSED:
                desc = "STATUS_PAUSED";
                break;
            case DownloadManager.STATUS_PENDING:
                desc = "STATUS_PENDING";
                break;
            case DownloadManager.STATUS_RUNNING:
                desc = "STATUS_RUNNING";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                desc = "STATUS_SUCCESSFUL";
                break;
        }
        return desc;
    }

}
