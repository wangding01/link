package com.cn.linka;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Pachong {
    public static void main(String[] args) throws IOException {
        // 解析Url地址 参数1：访问的url，参数2：访问的超时时间
        Document doc = Jsoup.parse(new URL("http://www.zhaozongjie.com/gzzj/grgzzj/111802.html"), 10000);
        // 使用选择器，获取想要的内容
        String title = doc.getElementsByTag("title").first().text();
        System.out.println(title);
//        System.out.println(doc.getElementById("contentText").text());
        StringBuilder sb = new StringBuilder();
        Elements contentTexts = doc.getElementById("contentText").select("p");
        for (Element contentText : contentTexts) {
            if (!contentText.text().contains("★") && !contentText.text().contains("▼")) {
                sb.append(contentText.text()).append("<w:br/>");
//                System.out.println(sb);
            }
        }
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("title",title);
        dataMap.put("detail",sb);
        CreateUtils createUtils = new CreateUtils();
        createUtils.createWord(dataMap);


    }
}
