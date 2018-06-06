package com.ywb.tuyue.utils;

import java.math.BigDecimal;

/**
 * Created by mhdt on 2018/2/7.
 */

public class MathUtils {
    public static float fromatFloat(double data, int scale) {
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        BigDecimal bd = new BigDecimal(data);
        bd = bd.setScale(scale, roundingMode);
        return bd.floatValue();
    }

    public static float fromatFloat2(double data) {
        return fromatFloat(data, 2);
    }
}
