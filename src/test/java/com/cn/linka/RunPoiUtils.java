package com.cn.linka;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * @program: linka
 * @description:
 * @author: wangding
 * @create: 2022-04-20 16:29
 */
public class RunPoiUtils {
    public static void createFile(String fileName, Map<String,Object> map){
        //创建文本对象
        XWPFDocument docxDocument = new XWPFDocument();
        //创建第一段落
        XWPFParagraph firstParagraphX = docxDocument.createParagraph();
        firstParagraphX.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun runTitle = firstParagraphX.createRun();
        runTitle.setText(map.get("title").toString());
        runTitle.setBold(true);
        runTitle.setFontSize(20);
        runTitle.setFontFamily("宋体");
        runTitle.addCarriageReturn();//回车键
        runTitle.setKerning(30);
        XWPFParagraph paragraphX = docxDocument.createParagraph();
        paragraphX.setAlignment(ParagraphAlignment.LEFT);//对齐方式
        paragraphX.setFirstLineIndent(400);//首行缩进
        //创建段落中的run
        XWPFRun run = paragraphX.createRun();
        run.setText(map.get("detail").toString());
        String path="D://work//other//freemaker//"+fileName+".docx";
        FileOutputStream stream=null;
        try {
            File file = new File(path);
            stream = new FileOutputStream(file);
            docxDocument.write(stream);
            stream.close();
        }catch (Exception e){
            System.out.println("生成失败");
        }
        System.out.println("文件生成完成!");
    }
    public static void createFileZuowen(String fileName, Map<String,Object> map){
        //创建文本对象
        XWPFDocument docxDocument = new XWPFDocument();
        //创建第一段落
        XWPFParagraph firstParagraphX = docxDocument.createParagraph();
        firstParagraphX.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun runTitle = firstParagraphX.createRun();
        runTitle.setText(map.get("title").toString());
        runTitle.setBold(true);
        runTitle.setFontSize(20);
        runTitle.setFontFamily("宋体");
        runTitle.addCarriageReturn();//回车键
        runTitle.setKerning(30);
        XWPFParagraph paragraphX = docxDocument.createParagraph();
        paragraphX.setAlignment(ParagraphAlignment.LEFT);//对齐方式
        paragraphX.setFirstLineIndent(400);//首行缩进
        //创建段落中的run
        XWPFRun run = paragraphX.createRun();
        run.setText(map.get("detail").toString());
        String path="D://work//other//zuowen-jtcl//"+fileName+".docx";
        FileOutputStream stream=null;
        try {
            File file = new File(path);
            stream = new FileOutputStream(file);
            docxDocument.write(stream);
            stream.close();
        }catch (Exception e){
            System.out.println("生成失败");
        }
        System.out.println("文件生成完成!");
    }
}