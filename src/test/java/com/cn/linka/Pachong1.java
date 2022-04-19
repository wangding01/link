package com.cn.linka;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Pachong1 {
    public static void main(String[] args) throws IOException {
        // 创建httpclients对象，可以理解为打开浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 输入网址 发送get请求
        HttpGet get = new HttpGet("http://www.zhaozongjie.com/gzzj/grgzzj/111802.html");
        // 发送并接收响应结果
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(get);
            // 解析响应，获取数据
            if (response.getStatusLine().getStatusCode() == 200) {
                // 响应体数据
                HttpEntity entity = response.getEntity();
                String conut = EntityUtils.toString(entity, "utf-8");

                System.out.println(conut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
