package com.ywb.tuyue.downLoad;

/**
 * Created by mhdt on 2018/2/7.
 */

public class DownLoadTask {
    private int id;
    private int MIMETYPE = UNKNOW;

    public static final int IMG = 1;
    public static final int VIDEO = 2;
    public static final int UNKNOW = 3;
    public static final int ZIP = 4;


    private String downLoadUrl;
    private String fileName;

    public DownLoadTask(String downLoadUrl, String fileName, int MIMETYPE, int id) {
        this.downLoadUrl = downLoadUrl;
        this.fileName = fileName;
        this.MIMETYPE = MIMETYPE;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMIMETYPE() {
        return MIMETYPE;
    }

    public void setMIMETYPE(int MIMETYPE) {
        this.MIMETYPE = MIMETYPE;
    }

    public boolean isVideo() {
        return this.MIMETYPE == VIDEO;
    }

    public boolean isImg() {
        return this.MIMETYPE == IMG;
    }

    public boolean isZIP() {
        return this.MIMETYPE == ZIP;
    }

    @Override
    public String toString() {
        return "DownLoadTask{" +
                "id=" + id +
                ", downLoadUrl='" + downLoadUrl + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
