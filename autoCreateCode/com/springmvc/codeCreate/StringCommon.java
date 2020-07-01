package com.springmvc.codeCreate;

/**
 * Created by lenovo on 2017/7/28.
 * 字符串通用类
 */
public class StringCommon {

    /**
     * 首字母大写
     * @param string 字符串
     * @return 返回首字母大写的字符串
     */
    public static String initialsUpperCase(String string){
        string = string.substring(0,1).toUpperCase() + string.substring(1);
        return string;
    }

    /**
     * 首字母小写
     * @param string 字符串
     * @return 返回首字母小写字符串
     */
    public static String initialsLowerCase(String string){
        string = string.substring(0,1).toLowerCase() + string.substring(1);
        return string;
    }
}
