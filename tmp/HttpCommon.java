package common;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by lenovo on 2017/9/24.
 */
public class HttpCommon {

    /**
     * 发送 http 请求
     * @param url url 地址
     * @param param post 参数
     * @param type 请求的方式（主要是 GET/POST）
     * @param encode 编码
     * @param paramType 请求的数据类型（application/json）
     * @param json json传输
     * @return 返回请求数据的结果
     */
    private String submitUrlContent(String url, Map<String,String>param,String type,String encode,String paramType,String json){
        String result = "";
        String sparam = getUrl(url,param);
        if (!paramType.isEmpty() && paramType.toUpperCase() == "JSON"){
            sparam = json;
        }
        BufferedReader bufferedReader = null;
        HttpURLConnection connection = null;

        try {
            URL realUrl = new URL(url);
            //打开和 URL 之间的连接
            connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestProperty("accept","image/gif,image/x-xbitmap,image/jpeg,image/pjpeg,application/x-shockwave-flash,application/vnd.ms-excel," +
                    "application/vnd.ms-powerpoint,application/msword,*/*");
            connection.setRequestProperty("connection","Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0(compatible;MSIE 6.0;Windows NT 5.1;SV1)");
            connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
            connection.setRequestProperty("contentType",encode.toUpperCase());
            //如果是 post 请求要设置这两个属性
            connection.setDoInput(true);
            connection.setDoOutput(true);
            switch (paramType.toUpperCase()){
                case "HTML":connection.setRequestProperty("content-Type","application/x-www-form-urlencoded;charset=" + encode.toUpperCase());break;
                case "JSON":connection.setRequestProperty("content-Type","application/json;charset=" + encode.toUpperCase());break;
            }
            connection.setRequestMethod(type.toUpperCase());
            //建立实际的连接
            connection.connect();
            //获取所有的响应头字段
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream(),encode.toLowerCase());
            outputStreamWriter.write(sparam);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            if (connection.getResponseCode() == 200){
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),encode.toLowerCase()));
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null){
                    result += inputLine;
                }
                bufferedReader.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getUrl(String url, Map<String, String> param) {
        String result = "";
        if (param == null || param.size() == 0){
            result = "";
        }else {
            StringBuffer stringBuffer = new StringBuffer();
            for (Map.Entry<String,String> entry : param.entrySet()){
                stringBuffer.append("&" + entry.getKey() + "=" + entry.getValue());
            }
            result = stringBuffer.toString();
        }
        return result;
    }

    //发送 get 请求
    public String getContentUrl(String url,String encode){
        return submitUrlContent(url,null,"GET",encode,"HTML","");
    }

    //发送 post 请求
    public String postContentUrl(String url,String encode,Map<String,String>param){
        return submitUrlContent(url,param,"POST",encode,"HTML","");
    }

    //发送 post json 请求
    public String jsonContentUrl(String url,String encode,String json){
        return submitUrlContent(url,null,"POST",encode,"JSON",json);
    }
}
