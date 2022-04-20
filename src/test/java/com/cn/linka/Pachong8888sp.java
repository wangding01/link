package com.cn.linka;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: linka
 * @description:http://www.8888ps.com/8183.html
 * @author: wangding
 * @create: 2022-04-20 17:31
 */
public class Pachong8888sp {
    public static void main(String[] args) throws Exception {
        runDocThread();
//        Integer numTag = 0;
//        String url = "https://www.51test.net/show/105119.html";
//        Document doc = Jsoup.parse(new URL(url), 10000);
//        // 使用选择器，获取想要的内容
//        String title = doc.getElementsByTag("title").first().text();
//        System.out.println(title);
//        Elements contentTexts = doc.getElementsByClass("content-txt").select("p");
//        if (contentTexts.size() < 10) {
//            return;
//        }
//        StringBuilder sb = new StringBuilder();
//        for (Element contentText : contentTexts) {
//            System.out.println(contentText.text());
//        }
////        Map<String, Object> dataMap = new HashMap<>();
////        dataMap.put("title", title);
////        dataMap.put("detail", sb);
////        RunPoiUtils.createFileZuowen(numTag + "-" + title, dataMap);
////        System.out.println("标号" + numTag + "  文章名：" + title + "生成完成");
    }

    public static void runDocThread() {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                createProcess(0, 1000);
            }
        });
//        Thread thread2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                createProcess(120000, 130000);
//            }
//        });
//        Thread thread3 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                createProcess(20000, 50000);
//            }
//        });
//        Thread thread4 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                createProcess(50000, 80000);
//            }
//        });
//        Thread thread5 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                createProcess(80000, 100000);
//            }
//        });
        thread1.start();
//        thread2.start();
//        thread3.start();
//        thread4.start();
//        thread5.start();
    }

    public static void createProcess(Integer startId, Integer endId) {
        Integer numTag = 0;
        for (int i = startId; i < endId; i++) {
            try {
                numTag = i;
                String url = "https://www.51test.net/show/" + numTag + ".html";
                Document doc = Jsoup.parse(new URL(url), 10000);
                // 使用选择器，获取想要的内容
                String title = doc.getElementsByTag("title").first().text();
                Elements contentTexts = doc.getElementsByClass("content-txt").select("p");
                if (contentTexts.size() < 10) {
                    break;
                }
                StringBuilder sb = new StringBuilder();
                for (Element contentText : contentTexts) {
                        sb.append(contentText.text()).append("\r");
                }
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("title", title);
                dataMap.put("detail", sb);
                RunPoiUtils.createFileZuowen(numTag + "-" + title, dataMap);
                System.out.println("标号" + numTag + "  文章名：" + title + "生成完成");
            } catch (Exception e) {
                System.out.println(numTag + "失败");
            }
        }

    }
}