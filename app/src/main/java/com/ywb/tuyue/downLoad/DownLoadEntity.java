package com.ywb.tuyue.downLoad;

import com.ywb.tuyue.utils.MathUtils;

/**
 * Created by mhdt on 2018/2/7.
 */

public class DownLoadEntity {
    private long COLUMN_ID;//long
    private int COLUMN_STATUS;//int
    private int COLUMN_BYTES_DOWNLOADED_SO_FAR;//int
    private int COLUMN_TOTAL_SIZE_BYTES;//int
    private String COLUMN_LOCAL_URI;//String
    private String COLUMN_TITLE;//String
    private String COLUMN_REASON;//String
    private String COLUMN_DESCRIPTION;//String
    private String COLUMN_MEDIA_TYPE;
    private String COLUMN_LAST_MODIFIED_TIMESTAMP;

    public long getCOLUMN_ID() {
        return COLUMN_ID;
    }

    public void setCOLUMN_ID(long COLUMN_ID) {
        this.COLUMN_ID = COLUMN_ID;
    }

    public int getCOLUMN_STATUS() {
        return COLUMN_STATUS;
    }

    public void setCOLUMN_STATUS(int COLUMN_STATUS) {
        this.COLUMN_STATUS = COLUMN_STATUS;
    }

    public int getCOLUMN_BYTES_DOWNLOADED_SO_FAR() {
        return COLUMN_BYTES_DOWNLOADED_SO_FAR;
    }

    public void setCOLUMN_BYTES_DOWNLOADED_SO_FAR(int COLUMN_BYTES_DOWNLOADED_SO_FAR) {
        this.COLUMN_BYTES_DOWNLOADED_SO_FAR = COLUMN_BYTES_DOWNLOADED_SO_FAR;
    }

    public int getCOLUMN_TOTAL_SIZE_BYTES() {
        return COLUMN_TOTAL_SIZE_BYTES;
    }

    public void setCOLUMN_TOTAL_SIZE_BYTES(int COLUMN_TOTAL_SIZE_BYTES) {
        this.COLUMN_TOTAL_SIZE_BYTES = COLUMN_TOTAL_SIZE_BYTES;
    }

    public String getCOLUMN_LOCAL_URI() {
        return COLUMN_LOCAL_URI;
    }

    public void setCOLUMN_LOCAL_URI(String COLUMN_LOCAL_URI) {
        this.COLUMN_LOCAL_URI = COLUMN_LOCAL_URI;
    }

    public String getCOLUMN_TITLE() {
        return COLUMN_TITLE;
    }

    public void setCOLUMN_TITLE(String COLUMN_TITLE) {
        this.COLUMN_TITLE = COLUMN_TITLE;
    }

    public String getCOLUMN_REASON() {
        return COLUMN_REASON;
    }

    public void setCOLUMN_REASON(String COLUMN_REASON) {
        this.COLUMN_REASON = COLUMN_REASON;
    }

    public String getCOLUMN_DESCRIPTION() {
        return COLUMN_DESCRIPTION;
    }

    public void setCOLUMN_DESCRIPTION(String COLUMN_DESCRIPTION) {
        this.COLUMN_DESCRIPTION = COLUMN_DESCRIPTION;
    }

    public String getCOLUMN_MEDIA_TYPE() {
        return COLUMN_MEDIA_TYPE;
    }

    public void setCOLUMN_MEDIA_TYPE(String COLUMN_MEDIA_TYPE) {
        this.COLUMN_MEDIA_TYPE = COLUMN_MEDIA_TYPE;
    }

    public String getCOLUMN_LAST_MODIFIED_TIMESTAMP() {
        return COLUMN_LAST_MODIFIED_TIMESTAMP;
    }

    public void setCOLUMN_LAST_MODIFIED_TIMESTAMP(String COLUMN_LAST_MODIFIED_TIMESTAMP) {
        this.COLUMN_LAST_MODIFIED_TIMESTAMP = COLUMN_LAST_MODIFIED_TIMESTAMP;
    }


    /**
     * 获取下载进度
     *
     * @return
     */
    public float getDownLoadPregress() {
        return COLUMN_BYTES_DOWNLOADED_SO_FAR * 100.0f / COLUMN_TOTAL_SIZE_BYTES;
    }

    /**
     * 获取下载进度(格式化，保留两位小数)
     *
     * @return
     */
    public float getFormatDownLoadPregress() {
        return MathUtils.fromatFloat2(getDownLoadPregress());
    }

}
