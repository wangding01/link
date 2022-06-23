package com.cn.linka;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: linka
 * @description:https://www.jtcl.org.cn/a/130395.html
 * @author: wangding
 * @create: 2022-04-20 17:31
 */
public class zuowenPachong {
    public static void main(String[] args) throws Exception {
        runDocThread();
    }

    public static void runDocThread() {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                createProcess(100000, 120000);
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                createProcess(120000, 130000);
            }
        });
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                createProcess(20000, 50000);
            }
        });
        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                createProcess(50000, 80000);
            }
        });
        Thread thread5 = new Thread(new Runnable() {
            @Override
            public void run() {
                createProcess(80000, 100000);
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
    }

    public static void createProcess(Integer startId, Integer endId) {
        Integer numTag = 0;
        for (int i = startId; i < endId; i++) {
            try {
                numTag = i;
                String url = "https://www.jtcl.org.cn/a/" + numTag + ".html";
                Document doc = Jsoup.parse(new URL(url), 10000);
                // 使用选择器，获取想要的内容
                String title = doc.getElementsByTag("title").first().text().replace("作文网","").replace("观后感网","");
                System.out.println(title);
                Elements contentTexts = doc.getElementsByClass("content").select("p");
                List<String> details = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                for (Element contentText : contentTexts) {
                    if (!contentText.text().contains("jtcl.org.cn") && !contentText.text().contains("作文网")) {
                        details.add( sb.append(contentText.text()).append("\r").toString());
                    }
                }
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("title", title);
                dataMap.put("detail", sb);
                RunPoiUtils.createFileList(numTag + "-" + title,title, details);
                System.out.println("标号" + numTag + "  文章名：" + title + "生成完成");
            } catch (Exception e) {
                System.out.println(numTag + "失败");
            }
        }

    }
}