import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Java Unix时间戳转换
 */
public class TimeUtil {
    private static final String TEN_STRING_DATE = "yyyy-MM-dd HH:mm:ss";
    private static final String THIRTEEN_STRING_DATE = "yyyy-MM-dd HH:mm:ss.SSS";

    //获取当前时间，精确到毫秒 --> 13位
    public static String getNowTimeStamp() {
        long time = System.currentTimeMillis();
        String nowTimeStamp = String.valueOf(time);
        return nowTimeStamp;
    }

    /**
     * 10或13位时间戳转 String 格式(yyyy-MM-dd HH:mm:ss)日期
     *
     * @param timestamp
     * @return
     */
    public static String unix2String(String timestamp) {
        String date;
        if (timestamp.length() == 13) {
            date = new SimpleDateFormat(THIRTEEN_STRING_DATE).format(Long.parseLong(timestamp));
        } else {
            date = new SimpleDateFormat(TEN_STRING_DATE).format(Long.parseLong(timestamp) * 1000);
        }
        return date;
    }

    /**
     * String 格式转10或13位时间戳
     *
     * @param dateString
     * @param ten
     * @return
     */
    public static String string2Unix(String dateString, boolean ten) {
        long timestamp;
        if (ten) {
            timestamp = (Timestamp.valueOf(dateString).getTime()) / 1000;
        } else {
            timestamp = (Timestamp.valueOf(dateString).getTime());
        }
        return String.valueOf(timestamp);
    }

    /**
     * 10或13位时间戳转 Date
     *
     * @param timestamp 参数时间戳
     * @return
     */
    public static Date unix2Date(String timestamp) {
        Date date = null;
        try {
            if (timestamp.length() == 13) {
                SimpleDateFormat sdf = new SimpleDateFormat(THIRTEEN_STRING_DATE);
                date = sdf.parse(sdf.format(Long.parseLong(timestamp)));
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat(TEN_STRING_DATE);
                date = sdf.parse(sdf.format(Long.parseLong(timestamp) * 1000));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Date转10位13位时间戳
     *
     * @param date 参数date
     * @param ten  需要转换成几位时间戳
     * @return
     */
    public static String date2Unix(Date date, boolean ten) {
        String result = null;
        if (!ten) { //13 位
            result = String.valueOf(date.getTime());
        } else { //10位
            result = String.valueOf(date.getTime() / 1000);
        }
        return result;
    }

    /**
     * String 格式转 Date
     *
     * @param dateString
     * @return
     */
    public static Date string2Date(String dateString) {
        Date date = new Date();
        try {
            if (dateString.length() == 13) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                date = sdf.parse(dateString);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = sdf.parse(dateString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Date 转 String 格式
     *
     * @param date
     * @param ten
     * @return
     */
    public static String date2String(Date date, Boolean ten) {
        String dateString;
        if (ten) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateString = sdf.format(date);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            dateString = sdf.format(date);
        }
        return dateString;
    }

    public static void main(String[] args) {
        //获取当前时间的13位时间戳
        String timeStamp = TimeUtil.getNowTimeStamp();
        System.out.println(timeStamp);
        //unix时间戳转String
        String dateString = TimeUtil.unix2String(timeStamp);
        System.out.println(dateString);
        //String转unix时间戳
        String string2Unix = TimeUtil.string2Unix(dateString, false);
        System.out.println(string2Unix);
        //unix转Date
        Date unix2Date = TimeUtil.unix2Date(timeStamp);
        System.out.println(unix2Date);
        //Date转unix
        String unix = TimeUtil.date2Unix(unix2Date, false);
        System.out.println(unix);
        //String转Date
        Date string2Date = TimeUtil.string2Date(dateString);
        System.out.println(string2Date);
        //Date转String
        String date2String = TimeUtil.date2String(unix2Date, false);
        System.out.println(date2String);
    }
}