package org.aqua.string.regex.url;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Seper {

    public static void main(String[] args) {
        String url = "taskId=420&sceneId=401&testcaseId=%E9%9B%86%E5%9B%A2%E7%9B%B4%E4%BE%9B%E4%B8%B2%E5%8F%B7%E5%88%A0%E9%99%A4_%E5%BC%82%E5%B8%B8_prod_instance%E4%B8%ADsale_mode%21%3D15";
        url = "taskId=449&sceneId=400&testcaseId=%E4%BA%B2%E6%83%851%2B1%E8%B4%AD%E6%9C%BA%E4%BF%9D%E5%BA%95%E9%80%81%E7%BC%B4%E8%B4%B9%E5%8D%A1_%E6%AD%A3%E5%B8%B8_%28%E6%B8%A0%E9%81%93%29%E4%BA%B2%E6%83%85%E8%B4%AD%E6%9C%BA%E6%94%AF%E4%BB%98299%E5%85%83%E9%80%812%E5%85%83%EF%BC%88%E8%80%81%E5%8F%B7%E4%BF%9D%E5%BA%9557%E5%85%8324%E4%B8%AA%E6%9C%88%EF%BC%89_576";
        String regex = "((?!&)\\S)+[=][^&]*";
        try {
            url = URLDecoder.decode(url, "UTF-8");
//            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(url);
        
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(url);
        while (m.find()) {
            System.out.println(m.group());
        }
        System.out.println(url);
        
        url = "—";
        try {
            url = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(url);
        url = "%E5%A4%A9%E6%B0%94%E9%A2%84%E6%8A%A5_%E6%AD%A3%E5%B8%B8_%E5%A4%A9%E6%B0%94%E9%A2%84%E6%8A%A5%EF%BF%BD%EF%BF%BD%E6%BC%AB%E6%B8%B8%E5%A4%A9%E6%B0%94%E9%A2%84%E6%8A%A5_0";
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(url);
        
    }

}
