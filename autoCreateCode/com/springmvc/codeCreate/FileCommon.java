package com.springmvc.codeCreate;

import java.io.*;

/**
 * Created by lenovo on 2017/7/28.
 * 文件和文件夹帮助类
 */
public class FileCommon {

    //判断是否存在文件夹
    public static boolean isExitsDirectory(String path){
        boolean flag = false;
        File file = new File(path);
        if (file.isDirectory()){//识别指定文件夹
            if (file.exists()){
                flag = true;
            }
        }
        return flag;
    }

    //判断文件是否存在
    public static boolean isExitsFile(String path){
        boolean flag = false;
        File file = new File(path);
        if (file.isFile()){
            if (file.exists()){
                flag = true;
            }
        }
        return flag;
    }

    //创建文件夹
    public static boolean createDirectory(String path) throws IOException {
        boolean flag = false;
        if (isExitsDirectory(path)){
            flag = true;
        }else {
            File file = new File(path);
            if (file.mkdirs()){
                flag = true;
            }
        }
        return flag;
    }

    //创建文件
    public static boolean createFile(String path) throws IOException {
        boolean flag = false;
        if (isExitsFile(path)){
            flag = true;
        }else {
            File file = new File(path);
            if (file.createNewFile()){
                flag = true;
            }
        }
        return flag;
    }

    //向 txt 写入数据
    public static void writeText(String path,String txt){
        FileWriter fileWriter = null;
        try {
            if (createFile(path)){
                fileWriter = new FileWriter(path);
                fileWriter.write(txt);
                fileWriter.flush();
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //向文本文件的尾部追加内容
    public static void appendText(String path,String txt){
        BufferedWriter bufferedWriter = null;
        try {
            if (createFile(path)){
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path,true)));
                bufferedWriter.write(txt + "\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}