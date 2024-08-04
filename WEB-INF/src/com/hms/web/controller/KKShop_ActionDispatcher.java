/**
 * 
 */

package com.hms.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


import com.hms.entity.Actions.Action;
import com.hms.entity.HMS_TBLE;
import com.hms.entity.HMS_TROW;
import com.hms.util.HMS_Util;
import com.hms.util.UTIL_Encrypt_Base64;
import com.hms.util.UTIL_GlobalConfig;
import com.hms.util.UTIL_String;
import com.hms.util.UserInfo;
import com.hms.web.HMS_WEBACTION;

import net.sf.json.JSONObject;

//import com.hms.web.entity.Webaction;

public class KKShop_ActionDispatcher extends HttpServlet {		

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
	 * 時間差(秒)
	 * @param startDate
	 * @return
	 */
	/***
	 * yyyy-MM-dd HH:mm:ss
	 */
	private static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
	/***
	 * 两个日期相差多少秒
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getTimeDelta(Date date1,Date date2){
		long timeDelta=(date1.getTime()-date2.getTime())/1000;//单位是秒
		int secondsDelta=timeDelta>0?(int)timeDelta:(int)Math.abs(timeDelta);
		return secondsDelta;
	}
	
	/***
	 * 两个日期相差多少秒
	 * @param dateStr1  :yyyy-MM-dd HH:mm:ss
	 * @param dateStr2 :yyyy-MM-dd HH:mm:ss
	 */
	public static int getTimeDelta(String dateStr1,String dateStr2){
		Date date1=parseDateByPattern(dateStr1, yyyyMMddHHmmss);
		Date date2=parseDateByPattern(dateStr2, yyyyMMddHHmmss);
		return getTimeDelta(date1, date2);
	}
	
	public static Date parseDateByPattern(String dateStr,String dateFormat){
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
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
		
		Date startDate = new Date(System.currentTimeMillis());
		
		request.setCharacterEncoding( "utf-8" );
		response.setHeader( "Pragma" , "no-cache" );
		response.setHeader( "Cache-Control" , "no-cache" );
		response.setDateHeader( "Expires" , 0 );
		response.setContentType( "text/html;charset=utf-8" );
		
		Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
		
		HttpSession session = request.getSession();
		int chk = 0;
		//logger.debug("shop.go-->processRequest");
		
		/*
		Properties prop1 = new Properties();
		try {
			// classes/config.properties
			prop1.load(UTIL_GlobalConfig.class.getClassLoader().getResourceAsStream(UTIL_GlobalConfig.getConfigFile()));
			//logger.debug("DOMAIN:" + prop.getProperty("SYSTEM_DOMAIN"));
			//logger.debug("HMSDB:" + prop.getProperty("HMS_DB"));			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		*/
	
		String actionID 	= UTIL_String.nvl(request.getParameter("webactionID"), "");		
		String actionMethod	= UTIL_String.nvl(request.getParameter("actionMethod"), "#");
		String methodName = UTIL_String.nvl(request.getParameter("methodName"), "#");
		String loginID = "";
		
		int r = 0;
		r = (int)(Math.random()*100000)+10000;
		
		//logger.info(String.format(" %d:s-%s-%s", r, actionID, methodName));		
		Action action 	= UTIL_GlobalConfig.getAction(actionID);		
		
		
		
		
		/**
		 * test code end .......
		 */		
		/*
		HMS_TROW trUser = HMS_Util.getOne().getDRB("UI_LOGIN");			
		if ( null == trUser ) {					
			logger.debug("tr user is null####");			
			trUser = new HMS_TROW();
			trUser.SetCell("COMP_CODE", "290098");
			trUser.SetCell("LOGIN_ID", "290098");
			trUser.SetCell("LOGIN_ROLE", "290098");
		} else {
			logger.debug("!!!!!tr user something !!!!");;			
		}		
		HMS_Util.getOne().setDRB("USER_INFO", trUser);		
		logger.debug("Print TR USer");;
		logger.debug(trUser.toJSONString());
		
		String group = (String) session.getAttribute("LOGIN_GROUP");
		if (group == null || group.length() ==0){
			//logger.debug("------ Group null && init Group -----");
			session.setAttribute("LOGIN_GROUP", "EMP");			
			group = (String) session.getAttribute("LOGIN_GROUP");
		}				
		*/
		/**
		 * test code end .......
		 */
		
		
		
		/**
		 * test code start .......
		 * # 有沒有LoginMark 	
		 * 	N: 	
		 * 		LoginMark = N
		 * 		Login_Group = Cust
		 * 		AuthPgmID = LOGIN_IDX, HBD1W010, HBD1W020, HBD1W030, 
		 * 
		 *  ActionID 需不需要登入
		 *  	Y: 	
		 *  		ActionID 有沒有在  AuthPgmID(授權程式)裡面		   
		 *  			Y: 執行 process
		 *  			N: 權限不足
		 *  	N:  直接執行 process  
		 *   
		 */
		
		//logger.debug("### action(" + actionID + ") null ? ");
		//System.out.println("### action(" + actionID + ") null ? ");
		
		if (null != action){

			//logger.debug("### action is ok ");
			//System.out.println("### action is ok ");
			// 是不是需要登入的頁面
			String actionType = action.getDesc();						
			String uiLoginMark = (String) session.getAttribute("LoginMark");
			
			//logger.debug("### LoginMark: " + uiLoginMark);
			//System.out.println("### LoginMark: " + uiLoginMark);
			
			
			
			/**
			 * login Mark Test
			 */
			
			
			/*
			if (null == loginMark){
				logger.debug("@@@ loginMark is null");				
			}else{				
				logger.debug("@@@ loginMark not nul, plus Y --> " + loginMark);					
			}
			*/
			
			/*
			// 有沒有login mark
			String uiLoginMark = (String) session.getAttribute("LoginMark");
			if (null == uiLoginMark || !"Y".equals(uiLoginMark)){
				// 預設給Cust權限
				session.setAttribute("UI_LoginMark",  "N");
				session.setAttribute("UI_LoginGroup", "Cust");
				session.setAttribute("UI_AuthPgmID", "LOGIN_IDX, HBD1W010, HBD1W020, HBD1W030,");
			}else{
				// 看原本的權限還在不在，不在就先給CUST權限
				String uiGroup = (String) session.getAttribute("UI_LoginGroup");
				String uiAuthPgmID = (String) session.getAttribute("UI_AuthPgmID");
				String custAuthPgmID = "LOGIN_IDX, HBD1W010, HBD1W020, HBD1W030,"; 
				if (null == uiGroup || uiGroup.length() == 0){
					// 預設給cust權限
					session.setAttribute("UI_LoginGroup", "Cust");
					session.setAttribute("UI_AuthPgmID", custAuthPgmID);
				}else{
					if (null == uiAuthPgmID || uiAuthPgmID.length() == 0){
						// 預設給cust權限
						session.setAttribute("UI_LoginGroup", "Cust");
						session.setAttribute("UI_AuthPgmID", custAuthPgmID);
					}					
				}
			}
			*/
			
			// 免登入		
			if ("N".equals(actionType) || "LOGIN_IDX".equals(actionID)){
				//logger.debug(String.format("### no need to login: %s ", actionID));
				//System.out.println("### no need to login ");
				// 免登入
				chk = 0;
			}else{
				// 要登入
				String tabKey = UTIL_String.nvl(request.getParameter("tabKey"), "#");
				//System.out.println("!!!!!tabKey: " + tabKey);
				//logger.debug("20201012 : new !!!! tabKey: " + tabKey);
				String jsonInfo = UTIL_Encrypt_Base64.decrypt(tabKey);
				//System.out.println("@@@@@@2jsonInfo: " + jsonInfo);
				JSONObject userJo = JSONObject.fromObject(jsonInfo);
				
				if (UTIL_String.getNowString("yyyyMMdd").equals(userJo.getString("LoginDate"))){
					//System.out.println("LoginInfo: " + userJo.toString());
					
					UserInfo ui = new UserInfo();
					ui.setCompCode(request, userJo.getString("LoginComp"));
					ui.setLoginID(request, userJo.getString("LoginID"));
					ui.setLoginRole(request, userJo.getString("LoginGroup"));
					
					loginID = userJo.getString("LoginID");
					//ui.setLoginMark("Y");
					session.setAttribute("UserInfo", ui);
					
					//System.out.println("### UserInfo: " + ui);
					
					
					//logger.debug("### uiLoginMark = Y ?");
					//System.out.println("### uiLoginMark = Y ?");
					if("Y".equals(userJo.getString("LoginMark"))){
						// System.out.println("### yMark");
						// 找被授權的程式清單					
						String AuthPgmID = UTIL_GlobalConfig.getAuthPgmID(ui.getCompCode(request), ui.getLoginRole(request));			
						//logger.debug("### AuthPgmID: " + AuthPgmID);
						//System.out.println("### AuthPgmID: " + AuthPgmID);
						
						String aID = actionID + ","; // 結案符號
						int chkIdx = AuthPgmID.indexOf(aID);	
						// logger.debug("### actionID: " + actionID);
						// logger.debug("### AuthPgmID: " + AuthPgmID);
						//System.out.println("### actionID: " + actionID);
						//System.out.println("### AuthPgmID: " + AuthPgmID);
						
						//System.out.println("CHKIDX: " + chkIdx);
						
						if (chkIdx < 0){
							// 找不到授權程式
							chk = 3; 
						}else{
							// 有授權，執行do process
							chk = 0;
						}
						//System.out.println("### CheckIdx: " + chkIdx);
					}else{
						// 沒有登入過
						// 需要登入
						chk = 2; // 重導				
						//System.out.println("### CheckIdx: " + chk);
					}
				}else{
					// tabkey 過期，需要重登
					chk = 2;
				}
				
							
			}
			
			//logger.debug("### chk: " + chk);
			// System.out.println("### chk: " + chk);
			
			// 依檢核結果決定執行功能
			if (chk == 0){
				// 有權限執行, doProcess
				// logger.debug("### Do Request Process ### actionID: " + actionID + ", method: " + actionMethod);
				// System.out.println("### Do Request Process ### actionID: " + actionID + ", method: " + actionMethod);
				
				doMethod(request, response, action, actionMethod);
				if (null != action.getPageName()){
					if (action.getPageName().length() > 0){		
						this.dispatchUrl(action.getPageName(), request, response);
					}
				}
			}else if (chk == 2){
				// 沒有登入過，導到登入頁
				// logger.debug("### GOTO LOGIN ###");
				this.dispatchUrl("/noAuth.jsp?msgCode=10001&w=" + actionID, request, response);
			}else if (chk == 3){
				// 沒權限執行 & 第1次登入抓到 tabkey 時
				// logger.debug("### No Authority ###" + actionID);
				this.dispatchUrl("/_errorpages/error.jsp", request, response);
				//this.dispatchUrl("/noAuth.jsp?msgCode=00003&w=" + actionID, request, response);
			}
			
			
		}else{
			// Invalid webaction
			logger.error("!!! action null: " + actionID + " !!!");
			chk = 1; 
			this.dispatchUrl("/noAuth.jsp?msgCode=00005&w=" + actionID, request, response);
		}	
		
		//logger.info(String.format(" %d:e-%s-%s", r, actionID, methodName));
		
		Date endDate = new Date(System.currentTimeMillis());
		
		String ipAddress = request.getHeader("X-FORWARDED-FOR");  
		if (ipAddress == null) {  
		    ipAddress = request.getRemoteAddr();  
		}
		int diffS = getTimeDelta(endDate, startDate);
		logger.info(String.format(" %s,%s,%s,%s,%ds", loginID, ipAddress, actionID, methodName, diffS));

	}

	
	private void doMethod(HttpServletRequest request , HttpServletResponse response, Action act, String method){
		
		method = method.toLowerCase();
		//UTIL_GlobalConfig.postLog("misc_data1 == null " + (null == currWA));
		//UTIL_GlobalConfig.postLog("misc_data1 == " + currWA.GetField( "misc_data1" ));
		// todo: Group 權限控管 & 瀏覽 log
		
		Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
		//logger.debug("wa: " + act.getActionID() + "-" + act.getClassName() + "-" + method);
		
		
		
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
				e.printStackTrace();							
				logger.error(e.toString());
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
		
		//logger.debug("aaaurl: " + url);
		// 用來確保透過proxy 也是拿到最新的資料.
		url = url + ( ( url.indexOf( "?" ) != -1 )
			? "&"
			: "?" ) + UTIL_String.getDateFormat( 4 );
		//logger.debug("bbburl: " + url);
		RequestDispatcher view = request.getRequestDispatcher( url );
		view.forward( request , response );
	}

	

}
