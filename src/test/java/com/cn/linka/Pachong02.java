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
 * @description:
 * @author: wangding
 * @create: 2022-04-20 16:37
 */
public class Pachong02 {
    public static void main(String[] args) {
        runDocThread();
    }
    public static void createProcess(Integer startId,Integer endId){
        // 解析Url地址 参数1：访问的url，参数2：访问的超时时间
        Integer numTag = 0;
        for (int i = startId; i < endId; i++) {
            numTag = i;
            String url = "http://www.zhaozongjie.com/nzzj/others/"+numTag+".html";
            try {
                Document doc = Jsoup.parse(new URL(url), 10000);
                // 使用选择器，获取想要的内容
                String title = doc.getElementsByTag("title").first().text();
                StringBuilder sb = new StringBuilder();
                Elements contentTexts = doc.getElementById("contentText").select("p");
                for (Element contentText : contentTexts) {
                    if (!contentText.text().contains("★") && !contentText.text().contains("▼")) {
                        sb.append(contentText.text()).append("\r");
                    }
                }
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("title", title);
                dataMap.put("detail", sb);
                RunPoiUtils.createFile(numTag+"-"+title,dataMap);
                System.out.println("标号"+numTag+"  文章名："+title + "生成完成");
            } catch (Exception e) {
                System.out.println(numTag+"失败");
            }
        }
    }
    public static void runDocThread(){
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                createProcess(60000,80000);
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                createProcess(80000,100000);
            }
        });
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                createProcess(100000,120000);
            }
        });
        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                createProcess(120000,140000);
            }
        });
        Thread thread5 = new Thread(new Runnable() {
            @Override
            public void run() {
                createProcess(140000,160000);
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
    }
}