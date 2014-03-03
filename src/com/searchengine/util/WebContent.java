package com.searchengine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.searchengine.entity.ContentObject;

public class WebContent {
	
	/**
     * 读取一个txt
     */
    public String getTxt(final String dir) throws IOException {
        String temp;
        File file = new File(dir);
        final StringBuffer sb = new StringBuffer();
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(
            		new FileInputStream(file), "gbk"));// 读取网页全部内容
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
        } catch (final MalformedURLException me) {
            me.getMessage();
            throw me;
        } catch (final IOException e) {
            e.printStackTrace();
            throw e;
        }
        return sb.toString();
    }
    /**
     * 读取一个网页全部内容
     */
    public String getOneHtml(final String htmlurl) throws IOException {
        URL url;
        String temp;
        final StringBuffer sb = new StringBuffer();
        try {
            url = new URL(htmlurl);
            final BufferedReader in = new BufferedReader(new InputStreamReader(
                    url.openStream(), "gbk"));// 读取网页全部内容
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
        } catch (final MalformedURLException me) {
            me.getMessage();
            throw me;
        } catch (final IOException e) {
            e.printStackTrace();
            throw e;
        }
        return sb.toString();
    }

    /**
     * 
     * @param s
     * @return 获得网页标题
     */
    public String getTitle(final String s) {
        String regex;
        String title = "";
        final List<String> list = new ArrayList<String>();
        regex = "<title>.*?</title>";
        final Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
        final Matcher ma = pa.matcher(s);
        while (ma.find()) {
            list.add(ma.group());
        }
        for (int i = 0; i < list.size(); i++) {
            title = title + list.get(i);
        }
        return outTag(title);
    }

    /**
     * 
     * @param s
     * @return 获得链接
     */
    public List<String> getLink(final String s) {
        String regex;
        final List<String> list = new ArrayList<String>();
        regex = "<a[^>]*href=(\"([^\"]*)\"|\'([^\']*)\'|([^\\s>]*))[^>]*>(.*?)</a>";
        final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
        final Matcher ma = pa.matcher(s);
        while (ma.find()) {
            list.add(ma.group());
        }
        return list;
    }

    /**
     * 
     * @param s
     * @return 获得脚本代码
     */
    public List<String> getScript(final String s) {
        String regex;
        final List<String> list = new ArrayList<String>();
        regex = "<script.*?</script>";
        final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
        final Matcher ma = pa.matcher(s);
        while (ma.find()) {
            list.add(ma.group());
        }
        return list;
    }

    /**
     * 
     * @param s
     * @return 获得CSS
     */
    public List<String> getCSS(final String s) {
        String regex;
        final List<String> list = new ArrayList<String>();
        regex = "<style.*?</style>";
        final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
        final Matcher ma = pa.matcher(s);
        while (ma.find()) {
            list.add(ma.group());
        }
        return list;
    }

    /**
     * 
     * @param s
     * @return 去掉标记
     */
    public String outTag( String s) {
    	  s=s.replace("<.*?>.*?<.*?>", "");
        return s.replaceAll("<.*?>", "");
    }

    /**
     *  Function:抓取网页内容
     *  @author XC
     *  @param url
     *  @return
     */
    public ContentObject getContentFromSite(String url){
        ContentObject cobj= new ContentObject();
        String html = "";
        try {
            html = getOneHtml(url);
            String title = outTag(getTitle(html));
         //   html = html.replaceAll("(<br>)+?", "\n");// 转化换行
         //   html = html.replaceAll("<p><em>.*?</em></p>", "");
            cobj.setTitle(title);
            			System.out.println(title);
            cobj.setContent(html.trim());
        } catch (final Exception e) {}
        return cobj;
    }
    
    /**
     *  Function:抓取txt内容
     *  @author XC
     *  @param url
     *  @return
     */
    public ContentObject getContentFromTxt(String dir){
        ContentObject cobj= new ContentObject();
        String content = "";
        try {
        	content = getTxt(dir);
            String title = new File(dir).getName();
         //   html = html.replaceAll("(<br>)+?", "\n");// 转化换行
         //   html = html.replaceAll("<p><em>.*?</em></p>", "");
            cobj.setTitle(title.trim());
            			System.out.println(title);
            cobj.setContent(content.trim());
        } catch (final Exception e) {}
        return cobj;
    }
}