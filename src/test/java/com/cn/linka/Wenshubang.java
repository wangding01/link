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
 * @description:
 * @author: wangding
 * @create: 2022-04-20 16:37
 */
public class Wenshubang {
    public static void main(String[] args) {
        runDocThread();
    }
    public static void createProcess(Integer startId,Integer endId){
        // 解析Url地址 参数1：访问的url，参数2：访问的超时时间
        Integer numTag = 0;
//        for (int i = startId; i < endId; i++) {
//            numTag = i;
            String url = "http://www.wenshubang.com/gongzuozongjie/"+"477657"+".html";
            try {
                Document doc = Jsoup.parse(new URL(url), 10000);
                // 使用选择器，获取想要的内容
                String title = doc.getElementsByTag("title").first().text();
                StringBuilder sb = new StringBuilder();
                List<String> details = new ArrayList<>();
                Elements contentTexts = doc.getElementsByClass("content").select("p");
                for (Element contentText : contentTexts) {
                    details.add(contentText.text());
                }
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("title", title);
                dataMap.put("detail", sb);
                RunPoiUtils.createFileList(numTag+"-"+title,title,details);
//                RunPoiUtils.createFile(numTag+"-"+title,dataMap);
                System.out.println("标号"+numTag+"  文章名："+title + "生成完成");
            } catch (Exception e) {
                System.out.println(numTag+"失败");
            }
//        }
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
//        thread2.start();
//        thread3.start();
//        thread4.start();
//        thread5.start();
    }
}