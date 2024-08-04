/**
 * 
 */

package com.hms.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.log4j.Logger;

import com.hms.entity.Actions.Action;
import com.hms.entity.HMS_TBLE;
import com.hms.entity.HMS_TROW;




/**
 * @author 290098 採 singleton
 */
public class UserInfo {

	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");	

	private String loginMark;

	public String getCompCode(HttpServletRequest request) {
		
		//HttpSession session = request.getSession();			
		//return this.getCookie(request, "CompCode");
		return this.getSession(request, "CompCode");
	}

	public void setCompCode(HttpServletRequest request, String compCode) throws UnsupportedEncodingException {		
		//this.setCookie(response, "CompCode", compCode);
		//this.compCode = compCode;
		this.setSession(request, "CompCode", compCode);
	}

	public String getLoginID(HttpServletRequest request) {
		//return this.getCookie(request, "LoginID");
		return this.getSession(request, "LoginID");
	}

	public void setLoginID(HttpServletRequest request, String loginID) throws UnsupportedEncodingException {				
		//this.setCookie(response, "LoginID", loginID);
		//this.loginID = loginID;
		this.setSession(request, "LoginID", loginID);
	}

	public String getLoginRole(HttpServletRequest request) {
		//return this.getCookie(request, "LoginRole");
		return this.getSession(request, "LoginRole");
	}

	public void setLoginRole(HttpServletRequest request, String loginRole) throws UnsupportedEncodingException {
		//this.setCookie(response, "LoginRole", loginRole);
		//this.loginRole = loginRole;
		this.setSession(request, "LoginRole", loginRole);
	}

	// 策略．將預設建構子封閉起來，讓外界無法 new() 建立 Instance。
	public UserInfo() {
	}
	
	public void setLoginMark(String loginMark){
		this.loginMark = loginMark;
	}
	
	public String getLoginMark(){
		return this.loginMark;
	}
	
	public void setCookie(HttpServletResponse response, String cookieName, String cookieVal) throws UnsupportedEncodingException{
		
		Cookie cookie = new Cookie(cookieName, URLEncoder.encode(cookieVal, "UTF-8"));

		cookie.setMaxAge(60*60*24);
        response.addCookie(cookie);
        response.setContentType("text/html;charset=UTF-8");
        //logger.debug(String.format("  @ Set Cookie: %s-%s", cookieName, cookieVal));

		
	}
	
	public String getCookie(HttpServletRequest request, String cookieName){
		
        String cookVal = "";
        Cookie[] cookies = request.getCookies();
        for(Cookie c: cookies){
            if(cookieName.equals(c.getName())){
                cookVal = URLDecoder.decode(c.getValue());      
            }
        }        
        return cookVal;
	}
	
	public void setSession(HttpServletRequest request, String attrName, String attrVal){
		HttpSession session = request.getSession();		
		session.setAttribute(attrName, attrVal);
	}
	
	public String getSession(HttpServletRequest request, String attrName){
		
		String attrVal = "";
		
		HttpSession session = request.getSession();
		
		attrVal = (String) session.getAttribute(attrName);
		
		if (null == attrVal){
			attrVal = "";
		}
		
		return attrVal;
		
		
	}
	
	
	

	



	

}
