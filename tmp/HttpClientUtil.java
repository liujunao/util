package com.east.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wangyan on 14-3-18.
 * httpClient请求封装
 */
public class HttpClientUtil {
    protected static final Logger LOG = LoggerFactory.getLogger(HttpClientUtil.class);

    public String getHttpClientResponse(HashMap<String, String> map, String reqUrl, String contentTimeOut, String soTimeOut) {
        org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(reqUrl);
        //请求超时
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Integer.parseInt(contentTimeOut));
        //读取超时
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Integer.parseInt(soTimeOut));
        HttpResponse response = null;
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (map != null && map.size() > 0) {
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                nvps.add(new BasicNameValuePair(key, map.get(key)));
            }
        }
        String resStr = "";
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            response = httpclient.execute(httppost);
            InputStreamReader reader = new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8");
            char[] buff = new char[1024];
            int length = 0;
            while ((length = reader.read(buff)) != -1) {
                resStr += new String(buff, 0, length);
            }
        } catch (IOException e) {
            LOG.error("[SYS] httpclient have a Exception", e);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return resStr;
    }

    public static String getURLConnection(String reqUrl, String xml, String soapAction) {
        URL url;
        String result = "";
        URLConnection connection = null;
        try {
            url = new URL(reqUrl);
            connection = url.openConnection();
            HttpURLConnection httpconn = (HttpURLConnection) connection;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bout.write(xml.getBytes());
            byte[] b = bout.toByteArray();
            httpconn.setRequestProperty("Content-Length", String
                    .valueOf(b.length));
            httpconn.setRequestProperty("Content-Type",
                    "text/xml; charset=gb2312");
            httpconn.setRequestProperty("SOAPAction", soapAction);
            httpconn.setRequestMethod("POST");
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);

            OutputStream out = httpconn.getOutputStream();
            out.write(b);
            out.close();

            InputStreamReader isr = new InputStreamReader(httpconn
                    .getInputStream());
            BufferedReader in = new BufferedReader(isr);
            int length = 0;
            char[] buff = new char[1024];
            while ((length = in.read(buff)) != -1) {
                result += new String(buff, 0, length);
            }
            in.close();
            return new String(result.getBytes(), "utf-8");
        } catch (Exception e) {
            LOG.error("[SYS] getURLConnection have a Exception", e);
        }
        return "";
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
//            TrustManager[] tm = { new MyX509TrustManager() }; //TODO
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
//            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr && !"".equals(outputStr)) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {

        } catch (Exception e) {

        }
        return jsonObject;
    }
}
