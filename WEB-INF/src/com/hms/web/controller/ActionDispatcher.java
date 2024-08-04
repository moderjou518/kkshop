/**
 * 
 */

package com.hms.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


import com.hms.entity.Actions.Action;
import com.hms.entity.HMS_TBLE;
import com.hms.entity.HMS_TROW;
import com.hms.util.HMS_Util;
import com.hms.util.UTIL_GlobalConfig;
import com.hms.util.UTIL_String;
import com.hms.web.HMS_WEBACTION;

//import com.hms.web.entity.Webaction;

public class ActionDispatcher extends HttpServlet {		

	/**
	 * 在Servlet初始化時，建立部份對應檔的內容
	 */
	@Override public void init() throws ServletException {		
		
		try {
			// load misc & webaction & item & trustRange from DB
			UTIL_GlobalConfig.preLoad();								
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * 處理GET REQUEST
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override public void doGet( HttpServletRequest request , HttpServletResponse response ) throws ServletException ,
		IOException {
		try {
			this.processRequest( request , response );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 處理POST REQUEST
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override public void doPost( HttpServletRequest request , HttpServletResponse response ) throws ServletException ,
		IOException {
		try {
			this.processRequest( request , response );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 處理請求的主程式 待新增邏輯： 1.當tomcat開啟時，db無法連線，設定統一導頁的頁面
	 * 2.當tomcat運作後，突然db無法連線，則各頁面如何控制
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	
	
	public void processRequest(HttpServletRequest request , HttpServletResponse response) throws Exception {
		
		Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
		
		request.setCharacterEncoding( "utf-8" );
		response.setHeader( "Pragma" , "no-cache" );
		response.setHeader( "Cache-Control" , "no-cache" );
		response.setDateHeader( "Expires" , 0 );
		response.setContentType( "text/html;charset=utf-8" );		
		
		Properties prop = new Properties();
		try {
			prop.load(UTIL_GlobalConfig.class.getClassLoader().getResourceAsStream("config.properties"));
			logger.debug("DOMAIN:" + prop.getProperty("SYSTEM_DOMAIN"));
			logger.debug("HMSDB:" + prop.getProperty("HMS_DB"));			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};

		
		
		String currWAID 	= UTIL_String.nvl(request.getParameter("webactionID"), "");
		logger.debug("webactionID-" + currWAID);
		
		String actionMethod	= UTIL_String.nvl(request.getParameter("actionMethod"), "#");		
		logger.debug("actionMethod-" + actionMethod);
		
		Action act 	= UTIL_GlobalConfig.getAction(currWAID);
		
		logger.debug("webAction-" + currWAID );
		
		if (null != act){
			logger.debug("webAction- not null, get loginUser" );
			HMS_TROW trUser = HMS_Util.getOne().getDRB("LOGIN_USER");			
			doMethod(request, response, act, actionMethod);
			
			if (null != act.getPageName()){
				if (act.getPageName().length() > 0){
					this.dispatchUrl(act.getPageName(), request, response);			
				}
			}	
			
			
		}else{
			// Invalid webaction
			this.dispatchUrl("/noAuth.jsp?msgCode=00005&w=" + currWAID, request, response);
		}		

	}

	
	private void doMethod(HttpServletRequest request , HttpServletResponse response, Action act, String method){
		
		method = method.toLowerCase();
		//UTIL_GlobalConfig.postLog("misc_data1 == null " + (null == currWA));
		//UTIL_GlobalConfig.postLog("misc_data1 == " + currWA.GetField( "misc_data1" ));
		// todo: Group 權限控管 & 瀏覽 log
		
		System.out.println("wa: " + act.getActionID() + "-" + act.getClassName() + "-" + method);
		
		if (act.getClassName().length() > 1){
			
			try {							
				Class Loadedclass = Class.forName(act.getClassName());
				HMS_WEBACTION wa = (HMS_WEBACTION)Loadedclass.newInstance();			
				
				
				// default
				if ("#".equals(method)){
					wa.doAction(request, response);	
				}
				// create
				if ("jc".equals(method)){
					wa.doCreate(request, response);	
				}
				// read
				if ("jr".equals(method)){
					wa.doRead(request, response);	
				}
				// update
				if ("ju".equals(method)){
					wa.doUpdate(request, response);	
				}
				// delete
				if ("jd".equals(method)){
					wa.doDelete(request, response);	
				}
			} catch(Exception e) {
				//UTIL_GlobalConfig.postIssue("doMethod", e);
				e.printStackTrace();
			}
		}	
		
	}
	
		
	
	
	
	
	
	
	
	
	/**
	 * 導頁
	 * 
	 * @param url
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void dispatchUrl( String url , HttpServletRequest request , HttpServletResponse response )
		throws ServletException , IOException {
		

		// 用來確保透過proxy 也是拿到最新的資料.
		url = url + ( ( url.indexOf( "?" ) != -1 )
			? "&"
			: "?" ) + UTIL_String.getDateFormat( 4 );
		RequestDispatcher view = request.getRequestDispatcher( url );
		view.forward( request , response );
	}

	private void showEntity( HMS_TROW tr ) {
		System.out.println( "code:" + tr.GetField( "MISC_CODE" ) );
		System.out.println( "dat1" + tr.GetField( "misc_data1" ) );
		System.out.println( "dat2" + tr.GetField( "misc_data2" ) );
		System.out.println( "num1:" + tr.GetField( "MISC_NUM1" ) );
		System.out.println( "" );

	}

}
