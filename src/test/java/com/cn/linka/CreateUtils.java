package com.cn.linka;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.Map;

public class CreateUtils {
    public void createWord(Map<String, Object> dataMap){
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8");
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
}
