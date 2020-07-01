package Common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2017/8/1.
 */
public class HtmlCommon {

    //region -- 获取请求参数
    /**
     * 获取请求的参数
     * @param request HttpServletRequest 对象
     * @param name 参数的键
     * @return 返回结果的字符串
     */
    public static String getParameter(HttpServletRequest request,String name){
        String result="";
        result=request.getParameter(name);
        return result;
    }
    //endregion


    //region --弹框
    /**
     * 弹框
     * @param response HttpServletResponse response
     * @param msg 弹框信息
     * @throws IOException
     */
    public static void showMsg(HttpServletResponse response,String msg) throws IOException {
        response.setContentType("text/html; charset=UTF-8"); //转码
        PrintWriter out = response.getWriter();
        out.flush();
        out.println("<script type='text/javascript'>");
        out.println("alert('"+msg+"');history.back(-1);");
        out.println("</script>");
    }


    /**
     * 弹框并跳转
     * @param response HttpServletResponse response
     * @param msg 弹框信息
     * @param url 跳转地址
     * @throws IOException
     */
    public static void showMsg(HttpServletResponse response,String msg,String url) throws IOException {
        response.setContentType("text/html; charset=UTF-8"); //转码
        PrintWriter out = response.getWriter();
        out.flush();
        out.println("<script type='text/javascript'>");
        out.println("alert('"+msg+"');window.location.href='"+url+"'");
        out.println("</script>");
    }

    //endregion

    //region --跳转
    /**
     * 跳转
     * @param response HttpServletRequest 对象
     * @param url 跳转的URL
     * @throws IOException
     */
    public static void redirect(HttpServletResponse response,String url) throws IOException {
        response.setContentType("text/html; charset=UTF-8"); //转码
        PrintWriter out = response.getWriter();
        out.flush();
        out.println("<script type='text/javascript'>");
        out.println("window.location='"+url+"'");
        out.println("</script>");
    }

    //endregion


    //region --session
    /**
     * 设置session
     * @param request HttpServletRequest 对象
     * @param sessionKey session的键
     * @param sessionValue session 的值
     */
    public static void setSession(HttpServletRequest request,String sessionKey,String sessionValue){
        HttpSession session = request.getSession();
        session.setAttribute(sessionKey, sessionValue);
    }


    /**
     * 获取Session
     * @param request HttpServletRequest 对象
     * @param sessionKey session的键
     * @return 返回session的值
     */
    public static String getSession(HttpServletRequest request,String sessionKey){
        String result="";
        //获得session
        HttpSession session = request.getSession();
        //获得session中保留的信息
        result = session.getAttribute(sessionKey).toString();
        return result;
    }

    /**
     * 移除session的指定值
     * @param request HttpServletRequest 对象
     * @param sessionKey session的键
     */
    public static  void removeSession(HttpServletRequest request,String sessionKey){
        HttpSession session = request.getSession();
        session.removeAttribute(sessionKey);

    }

    //endregion


    //region --cookie
    /**
     * 将cookie封装到Map里面
     * @param request
     * @return
     */
    private static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){
        Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
        Cookie[] cookies = request.getCookies();
        if(null!=cookies){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }


    /**
     * 设置cookie
     * @param response
     * @param name  cookie名字
     * @param value cookie值
     * @param maxAge cookie生命周期  小时
     */
    public static void setCookie(HttpServletResponse response,String name,String value,int maxAge){
        Cookie cookie = new Cookie(name,value);
        maxAge=maxAge*3600;
        cookie.setPath("/");
        if(maxAge>0)  cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }


    /**
     * 获取cookie
     * @param request
     * @param name cookie的键
     * @return 返回结果的字符串
     */
    public static String getCookie(HttpServletRequest request,String name){
        String result="";
        Map<String,Cookie> cookieMap = ReadCookieMap(request);
        if(cookieMap.containsKey(name)){
            Cookie cookie = (Cookie)cookieMap.get(name);
            result=cookie.getValue();
        }
        return result;
    }

    /**
     * 移除cookie
     * @param request
     * @param name cookie的键
     */
    public static void removeCookie(HttpServletRequest request,String name){
        Map<String,Cookie> cookieMap = ReadCookieMap(request);
        cookieMap.remove(name);
    }

    //endregion

}
