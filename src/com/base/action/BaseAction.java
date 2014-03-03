package com.base.action;



import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;


/**
 *  Class Name: BaseAction.java  
 *  @author XC
 *  @version 1.0
 */
public class BaseAction extends ActionSupport {

    private static final long serialVersionUID = -2983580695806850367L;
    /**
     * 
     */
    protected ServletContext getServletContext(){
        return ServletActionContext.getServletContext();
    }
    /**
     * 
     */
    protected HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }
    /**
     * 
     */
    protected HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }
    /**
     * 
     */
    protected HttpSession getSession() {
        return getRequest().getSession();
    }
    /**
     * 取得访问域名
     * @return
     */
    public String getURL(){
        return getRequest().getServerName();
    }
    /**
     * 存放Cookie
     * @param name
     * @param value
     */
    public void addCookie(String name,String value){
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(60*60*24);
        cookie.setPath("/");
        getResponse().addCookie(cookie);
    }
    /**
     * 删除Cookie
     * @param name
     * @param value
     */
    public void removeCookie(String name){
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        getResponse().addCookie(cookie);
    }
    /**
     * Cookie取得
     * @return
     * @throws Exception
     */
    public String getCookie(String name){
      try{
          Cookie[] cookies = getRequest().getCookies();
          for(Cookie cookie : cookies){
              cookie.setPath("/");
            if(cookie.getName().equals(name)){
                return cookie.getValue();
            }
          }
      }catch(Exception e){  }
      return "";
    }
    /**
     *  获取IP地址
     */
    public String getIpAddress(){
        HttpServletRequest request= getRequest();
        String ip = request.getHeader("x-forwarded-for");    
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("Proxy-Client-IP");    
        }    
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("WL-Proxy-Client-IP");    
        }    
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
           ip = request.getRemoteAddr();    
        } 
        return ip;
    }
}
