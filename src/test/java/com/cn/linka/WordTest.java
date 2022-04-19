package com.cn.linka;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordTest {

    private Configuration configuration = null;

    public WordTest() {
        configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8");
    }

    public static void main(String[] args) {
        WordTest test = new WordTest();
        test.createWord();
    }

    public void createWord() {

        Map<String, Object> dataMap = new HashMap<String, Object>();
        getData(dataMap);
        configuration.setClassForTemplateLoading(this.getClass(), "/");//模板文件所在路径,此处我是存放在resource目录下
        try {
            Template t = configuration.getTemplate("example.ftl"); //获取模板文件
            File outFile = new File("D:\\JAVA\\project\\freemaker\\" + Math.random() * 10000 + ".doc"); //导出文件
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
            t.process(dataMap, out); //将填充数据填入模板文件并输出到目标文件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> getData(Map<String, Object> dataMap) {
        dataMap.put("title", "标题");
        dataMap.put("detail", "2020");
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        for (int i = 0; i < 10; i++) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("xuehao", i);
//            map.put("neirong", "内容" + i);
//            list.add(map);
//        }
//        dataMap.put("list", list);
        return dataMap;
    }
}

