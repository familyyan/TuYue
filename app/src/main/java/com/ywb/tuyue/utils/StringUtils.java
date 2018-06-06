package com.ywb.tuyue.utils;

import android.net.Uri;
import android.util.Log;

import com.micro.player.service.DrmService;
import com.ywb.tuyue.net.Urls;

/**
 * Created by penghao on 2018/4/25.
 * description：
 */

public class StringUtils {


    /**
     * 是否是以http开头的链接
     *
     * @param url
     * @return
     */
    public static boolean isHttpUrl(String url) {
        if (url != null && url.startsWith("http://")) return true;
        return false;
    }

    /**
     * 添加默认ip，拼接成一个可下载的链接。
     *
     * @param url
     * @return
     */
    public static String addHttpIP(String url) {
        String str;
        str = Urls.HEAD_URL_IMAGE + url;
        return str;
    }

    /**
     * 如果不是以http开头，就拼接
     *
     * @param url
     * @return
     */
    public static String dealStr(String url) {
        String newUrl;

        if (isHttpUrl(url)) {
            Uri uri = StringUtils.add1905Url(url, DeviceUtils.getMacAddressFromIp(U.getAppContext()), Urls.HEAD_URL_IMAGE.substring(7, Urls.HEAD_URL_IMAGE.length()), "");
            if (uri != null) {
                newUrl = uri.toString();
            } else {
                newUrl = null;
            }
        } else {
            newUrl = Urls.HEAD_URL_IMAGE + url;
        }

        return newUrl;
    }

    /**
     * 如果不是以http开头，就拼接
     *
     * @param url
     * @return
     */
    public static String dealStrAPP(String url) {
        String newUrl;

        if (isHttpUrl(url)) {
            newUrl = url;
        } else {
            newUrl = Urls.HEAD_URL_IMAGE + url;
        }

        return newUrl;
    }


    public static Uri add1905Url(String url, String clientId, String ip, String sn) {
        try {
            if (DrmService.getService().startService()) {

           /*if (!DrmService.getService().initDrmdecoder()){  // 初始化
               ToastUtil.showShort(this,"解密模块启动失败，请重启电视盒子或联系管理员");
           }*/

                //url,
                //"B0:89:00:A3:AE:4C", //传入一个房间或者是盒子的唯一标示
                // "192.168.1.6",//服务器端的ip
                //"" //订单标识
                Uri uri = DrmService.getService().getUrl(url, clientId, ip, sn);

                Log.d("##1905Url##", "add1905Url: " + uri.toString());

                return uri;

            }
        } catch (Exception e) {
            Log.e("##1905出错##", "add1905Url: " + e.getMessage());
            return null;
        }

        return null;

    }

    public static int playLocalPath(String localUrl) {

        int port = DrmService.getService().startDecoder(localUrl.getBytes());

        Log.d("##1905Url##", "port: " + port);

        return port;

    }

}
