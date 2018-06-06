package com.ywb.tuyue.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

/**
 * Created by penghao on 2018/5/3.
 * description：
 */

public class ProcessText {

    private long pages;//总页数
    private final int SIZE = 800;//每一页的字节数 字节数固定
    private long bytescount;//字节总数
    private RandomAccessFile readFile;
    private String charSet;

    //构造方法 传入当前页 为了实现书签的功能 记录用户读取的文章位置
    public ProcessText(File file) {
        try {
            readFile = new RandomAccessFile(file, "r");
            bytescount = readFile.length();//获得字节总数
            pages = bytescount / SIZE;//计算得出文本的页数
            charSet = FileUtils.getCharset(file);//编码
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getPages() {
        return pages;
    }


    //定位字节位置 根据页数定位文本的位置
    public void seek(long page) {
        try {
            //if(pages)
            readFile.seek(page * SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//固定读取 SIZE+30个字节的内容 为什么+30 ？

//读取的为字节 需要进行转码 转码不可能刚好转的就是文本内容,

//一页的末尾 和开始出有可能乱码 每一次多读30个字节 是为了第一页乱码位置

//在第二页就可以正常显示出内容 不影响阅读


    public String read() {
        //内容重叠防止 末尾字节乱码
        byte[] chs = new byte[SIZE + 30];

        try {

            readFile.read(chs);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(chs, Charset.forName(charSet));
    }

    //上一页功能的实现
    public String getPre(int currentpage) {
        String content = null;
        //第一页 的情况 定位在0字节处 读取内容 当前页数不改变
        if (currentpage <= 1) {
            seek(0);
            content = read();
        } else {
            //其它页 则定位到当前页-2 处 在读取指定字节内容 例如当前定位到第二页的末尾 2*SIZE  上一页应该是第一页 也就是从0位置 开始读取SIZE个字节
            seek(currentpage);
            content = read();
        }

        return content;
    }

    //下一页功能的实现
    public String getNext(int currentpage) {

        String content = null;
        if (currentpage >= pages) {//当前页为最后一页时候,显示的还是 最后一页
            seek(pages);
            content = read();
        } else {
            seek(currentpage);
            content = read();
        }

        return content;
    }


}
