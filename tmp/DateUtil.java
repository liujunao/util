package com.east.common.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: EastDayWD
 * Date: 13-9-23
 * Time: 下午3:14
 * 处理时间工具类
 */
public class DateUtil {
    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentTime() {
        return df.format(System.currentTimeMillis());
    }

    public static String getCurrentTime(Date date) {
        return df.format(date);
    }

    public static Date fromString(String value) throws java.text.ParseException {
        return df.parse(value);
    }

    public static String getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, -1);    //得到前一天
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    public static byte[] IntToBytes(int aint) {
        byte[] b = new byte[4];
        int part1 = 0;
        int part2 = 0;
        int part3 = 0;
        int part4 = 0;

        part1 = 0xFF000000 & aint;
        part1 >>= 24;
        b[0] = (byte) part1;

        part2 = 0x00FF0000 & aint;
        part2 >>= 16;
        b[1] = (byte) part2;

        part3 = 0x0000FF00 & aint;
        part3 >>= 8;
        b[2] = (byte) part3;

        part4 = 0x000000FF & aint;
        b[3] = (byte) part4;

        return b;
    }

    // 从网络上接到的字节数组转换为int类型
    public static int BytesToInt(byte[] b, int start) {
        int part1 = 0;
        int part2 = 0;
        int part3 = 0;
        int part4 = 0;

        part1 = b[start];
        if (part1 < 0)
            part1 += 256;
        part1 <<= 24;

        part2 = b[start + 1];
        if (part2 < 0)
            part2 += 256;
        part2 <<= 16;

        part3 = b[start + 2];
        if (part3 < 0)
            part3 += 256;
        part3 <<= 8;

        part4 = b[start + 3];
        if (part4 < 0)
            part4 += 256;

        return part1 + part2 + part3 + part4;

    }

    public static long BytesToLong(byte[] b, int start) {
        long part1 = 0;
        long part2 = 0;
        long part3 = 0;
        long part4 = 0;
        long part5 = 0;
        long part6 = 0;
        long part7 = 0;
        long part8 = 0;

        part1 = b[start];
        if (part1 < 0)
            part1 += 256;
        part1 <<= 56;

        part2 = b[start + 1];
        if (part2 < 0)
            part2 += 256;
        part2 <<= 48;

        part3 = b[start + 2];
        if (part3 < 0)
            part3 += 256;
        part3 <<= 40;

        part4 = b[start + 3];
        if (part4 < 0)
            part4 += 256;
        part4 <<= 32;

        part5 = b[start + 4];
        if (part5 < 0)
            part5 += 256;
        part5 <<= 24;

        part6 = b[start + 5];
        if (part6 < 0)
            part6 += 256;
        part6 <<= 16;

        part7 = b[start + 6];
        if (part7 < 0)
            part7 += 256;
        part7 <<= 8;

        part8 = b[start + 7];
        if (part8 < 0)
            part8 += 256;

        return part1 + part2 + part3 + part4 + part5 + part6 + part7 + part8;
    }

    public static byte[] LongToBytes(long along) {
        byte[] b = new byte[8];
        long part1 = 0;
        long part2 = 0;
        long part3 = 0;
        long part4 = 0;
        long part5 = 0;
        long part6 = 0;
        long part7 = 0;
        long part8 = 0;

        part1 = 0xFF00000000000000L & along;
        part1 >>= 56;
        b[0] = (byte) part1;

        part2 = 0x00FF000000000000L & along;
        part2 >>= 48;
        b[1] = (byte) part2;

        part3 = 0x0000FF0000000000L & along;
        part3 >>= 40;
        b[2] = (byte) part3;

        part4 = 0x000000FF00000000L & along;
        part4 >>= 32;
        b[3] = (byte) part4;

        part5 = 0x00000000FF000000L & along;
        part5 >>= 24;
        b[4] = (byte) part5;

        part6 = 0x0000000000FF0000L & along;
        part6 >>= 16;
        b[5] = (byte) part6;

        part7 = 0x000000000000FF00L & along;
        part7 >>= 8;
        b[6] = (byte) part7;

        part8 = 0x00000000000000FFL & along;
        b[7] = (byte) part8;

        return b;
    }

    /**
     * byte转String，自动截断0
     */
    public static String bytesToString(byte[] data) {
        if (data == null || data.length == 0)
            return null;

        for (int n = 0; n < data.length; ++n) {
            if (data[n] == 0) {
                return new String(data, 0, n);
            }
        }

        return new String();
    }

    public static String bytesToString(byte[] data, String mark) {

        if (data == null || data.length == 0)
            return null;

        try {
            return new String(data, mark);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
	 * 计算时间差
	 * @param begin
	 * @param end
	 */
	public static long timeInterval(Date begin, Date end) {
        long between = 0;
        try {
            between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                - min * 60 * 1000 - s * 1000);
        System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒" + ms + "毫秒");
        return between;
	}
    
    public static void main(String arg[]) throws Exception {
        String mobile = "13918511830\n" +
                "13918511830\n" +
                "18721368759\n" +
                "18721368759\n" +
                "18721129721\n" +
                "18721129721\n" +
                "18717963502\n" +
                "18717963502\n" +
                "18717916810\n" +
                "18717916810\n" +
                "18217556971\n" +
                "18217556971\n" +
                "18217558690\n" +
                "18217558690\n" +
                "18201987587\n" +
                "18201987587\n" +
                "18221550767\n" +
                "18221550767\n" +
                "15921691087\n" +
                "15921691087\n" +
                "15000196729\n" +
                "15000196729\n" +
                "15000924087\n" +
                "15000924087\n" +
                "18217180734\n" +
                "18217180734\n" +
                "13636422124\n" +
                "13636422124\n" +
                "13795254347\n" +
                "13795254347\n" +
                "13761795873\n" +
                "13761795873\n" +
                "18201967553\n" +
                "18201967553\n" +
                "15921009101\n" +
                "15721535727\n" +
                "15921009101\n" +
                "15721535727\n" +
                "18721088201\n" +
                "18721088201\n" +
                "18701759529\n" +
                "18721522720\n" +
                "18701759529\n" +
                "18721522720\n" +
                "18721565283\n" +
                "18721565283\n" +
                "15921078953\n" +
                "15921078953\n" +
                "18717971365\n" +
                "18321739703\n" +
                "18717971365\n" +
                "18321739703\n" +
                "18721790581\n" +
                "13795320751\n" +
                "13795320751\n" +
                "18217510390\n" +
                "18217510390\n" +
                "18721790581\n" +
                "18221725576\n" +
                "18202126521\n" +
                "18221725576\n" +
                "18321701817\n" +
                "18321701817\n" +
                "15900861782\n" +
                "15900861782\n" +
                "18202126521\n" +
                "18721558579\n" +
                "18721558579\n" +
                "18201839175\n" +
                "13761153275\n" +
                "18321883521\n" +
                "15901671612\n" +
                "15901671612\n" +
                "18201839175\n" +
                "18321883521\n" +
                "13761123730\n" +
                "13761153275\n" +
                "13761123730\n" +
                "18721255276\n" +
                "18721255276\n" +
                "18701738712\n" +
                "18701738712\n" +
                "18721216973\n" +
                "18202129631\n" +
                "18721216973\n" +
                "18202129631\n" +
                "18701762581\n" +
                "18701762581\n" +
                "18321822571\n" +
                "18321822571\n" +
                "18721562393\n" +
                "18721562393\n" +
                "13761821539\n" +
                "13761322721\n" +
                "18201882597\n" +
                "13761821539\n" +
                "13761322721\n" +
                "18201882597\n" +
                "18702123752\n" +
                "18702123752\n" +
                "18721198751\n" +
                "18721198751\n" +
                "18321826320\n" +
                "18321826320\n" +
                "18217250761\n" +
                "18217250761\n" +
                "13761591781\n" +
                "13761591781\n" +
                "13761137209\n" +
                "13761137209\n" +
                "18701702067\n" +
                "18321796860\n" +
                "18701702067\n" +
                "18321796860\n" +
                "18702182797\n" +
                "18702182797\n" +
                "18721857261\n" +
                "18721857261\n" +
                "18321701910\n" +
                "18321701910\n" +
                "18321750865\n" +
                "18321750865\n";
        String[] mobileArray = mobile.split("\\n");
        StringBuffer sb = new StringBuffer();
        for (String mo : mobileArray) {
            if (!sb.toString().contains(mo)) {
                sb.append(mo + ",");
            }
        }
        System.out.println(sb.toString());
        System.out.println(sb.toString().split(",").length);
    }
}
