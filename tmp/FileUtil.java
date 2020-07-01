package com.east.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by rui on 14-8-7.
 */
public class FileUtil {
    public static void updateTokenFile(String token,String filePath) {
        try {
            File tokenFile = new File(filePath);
            // 文件不存在则创建
            if (!tokenFile.exists()) {
                tokenFile.createNewFile();
            }
            BufferedWriter out = new BufferedWriter(new FileWriter(tokenFile));
            out.write(token);
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
