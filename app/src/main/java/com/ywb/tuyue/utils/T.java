package com.ywb.tuyue.utils;

import com.ywb.tuyue.bean.Menu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mhdt on 2017/12/16.
 * 相关转化
 */

public class T {
    public static <T> List<T> arrayTList(T[] array) {
        List<T> tList = new ArrayList<>();
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                tList.add(array[i]);
            }
        }
        return tList;
    }

    public static List<Menu> getMenu(List<String> stringList) {
        List<Menu> menuList = new ArrayList<>();
        for (String s : stringList) {
            menuList.add(new Menu(s));
        }
        return menuList;
    }

    public static String getDate(long currentTime) {  // 得到的是一个日期：格式为：yyyy-MM-dd HH:mm:ss.SSS
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(currentTime));// 将当前日期进行格式化操作
    }
}
