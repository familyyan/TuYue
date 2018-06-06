package com.ywb.tuyue.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by penghao on 2018/4/8.
 * description：
 */

public class HTMLFormat {

    public static String getNewContent(String htmltext) {

        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }
//attr("-ms-interpolation-mode", "bicubic")
        return doc.toString();
    }

}
