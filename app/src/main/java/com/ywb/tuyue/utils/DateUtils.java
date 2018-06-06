package com.ywb.tuyue.utils;

import java.util.Calendar;

/**
 * Created by penghao on 2018/1/23.
 * description：
 */

public class DateUtils {
    /**
     * 是否在某一时间段
     *
     * @return
     */
    public static boolean isBetweenTime() {
        Calendar cal = Calendar.getInstance();// 当前日期
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
        final int start = 9 * 60;// 起始时间 00:20的分钟数
        final int end = 18 * 60;// 结束时间 8:00的分钟数
        if (minuteOfDay >= start && minuteOfDay <= end) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取2个时间点的差值 单位s
     *
     * @param lastMill
     * @param currentMill
     * @return
     */
    public static int getStaySecond(long lastMill, long currentMill) {
        int second;
        long l = currentMill - lastMill;
        second = (int) (l / 1000);
        return second;
    }
}
