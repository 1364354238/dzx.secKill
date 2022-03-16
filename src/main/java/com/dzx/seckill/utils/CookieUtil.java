package com.dzx.seckill.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @description: cookie工具
 * @author: dzx
 * @date: 2022/3/7
 */


public class CookieUtil {

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, false);
    }

    /**
     * 得到cookie的值
     * @param request
     * @param cookieName
     * @param b 是否编码,true则需要工具解码
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean b) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies | null == cookieName) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                if (b) {
                    return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                }
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 得到cookie的值
     * @param request
     * @param cookieName
     * @param encodeString 编码格式
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies | null == cookieName) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                try {
                    return URLDecoder.decode(cookie.getValue(), encodeString);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 设置cookie的值，不设置生效时间(浏览器关闭即失效)
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue) {
       setCookie(request,response,cookieName,cookieValue,-1);
    }

    /**
     * 设置Cookie的值 在指定时间内生效,但不编码
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response,
                                 String cookieName, String cookieValue,int cookieLife) {
        setCookie(request,response,cookieName,cookieValue,cookieLife,false);
    }

    /**
     * 设置Cookie的值 不设置生效时间,但编码
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response,
                                 String cookieName, String cookieValue,boolean isEncode) {
       setCookie(request,response,cookieName,cookieValue,-1,isEncode);
    }

    /**
     * 设置Cookie的值 在指定时间内生效, 编码参数
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response,
                                 String cookieName, String cookieValue,int cookieLife,boolean isEncode) {
        doSetCookie(request,response,cookieName,cookieValue,cookieLife,isEncode);
    }

    /**
     * 删除Cookie带cookie域名
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName) {
        doSetCookie(request, response, cookieName, "", -1, false);
    }

    /**
     * 设置Cookie的值，并使其在指定时间内生效
     */
    public static void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                   String cookieName, String cookieValue,
                                   int cookieLife, boolean isEncode) {
        if(null!=cookieValue&&isEncode){
            cookieValue = URLEncoder.encode(cookieValue, StandardCharsets.UTF_8);
        }
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(cookieLife);
//        设置域名
        if(null!=request){
            String domainName = getDomainName(request);
            if("localhost"!=domainName){
                cookie.setDomain(domainName);
            }
        }
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 得到cookie的域名
     */
    private static  String getDomainName(HttpServletRequest request) {
        String domainName = null;
        // 通过request对象获取访问的url地址
        String serverName = request.getRequestURL().toString();
        if (serverName.equals("")) {
            domainName = "";
        } else {
            // 将url地下转换为小写
            serverName = serverName.toLowerCase();
            // 如果url地址是以http://开头  将http://截取
            if (serverName.startsWith("http://")) {
                serverName = serverName.substring(7);
            }
            int end = serverName.length();
            // 判断url地址是否包含"/"
            if (serverName.contains("/")) {
                //得到第一个"/"出现的位置
                end = serverName.indexOf("/");
            }
            // 截取
            serverName = serverName.substring(0, end);
            // 根据"."进行分割
            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3) {
                // www.xxx.com.cn
                domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len > 1) {
                // xxx.com or xxx.cn
                domainName = domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }
        if (domainName.indexOf(":") > 0) {
            String[] ary = domainName.split("\\:");
            domainName = ary[0];
        }
        return domainName;
    }
}
