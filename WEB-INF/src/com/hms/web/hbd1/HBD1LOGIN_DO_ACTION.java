package com.hms.web.hbd1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.evergreen_hotels.bmg.whms1.util.MsgUtils;
import com.evergreen_hotels.bmg.whms1.util.PrepareExecuteSql;
import com.evergreen_hotels.bmg.whms1.util.RowData;
import com.evergreen_hotels.bmg.whms1.util.RowDataList;
import com.hms.entity.HMS_TBLE;
import com.hms.entity.HMS_TROW;
import com.hms.util.HMS_PrepareExecSQL;
import com.hms.util.UTIL_Encrypt_Base64;
import com.hms.util.UTIL_GlobalConfig;
import com.hms.util.UTIL_String;
import com.hms.util.UserInfo;
import com.hms.web.HMS_WEBACTION;
import com.hms.web.hbd1.dao.HAC0BASC_DAO;
import com.hms.web.hbd1.dao.HMI0OFFS_DAO;
import com.hms.web.hbd1.dao.HMP0BID_DAO;
import com.hms.web.hbd1.dao.HMP0ITEM_DAO;
import com.hms.web.hbd1.dao.HMP0POMM_DAO;
import com.hms.web.hbd1.dao.HMS0MISC_DAO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * HBD1W020 
 * 
 * @author Window10
 * 
 */
public class HBD1LOGIN_DO_ACTION implements HMS_WEBACTION {
	
	Logger logger = Logger.getLogger("KKSHOP_LOGIN_LOG");

	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info(" !! DO_HBD1LOGIN_ACTION doAction:#");
		
	
		
		
		
		
		
		//logger.debug("111: " + UTIL_GlobalConfig.getDvpConfig("LOGIN_ID"));
		//String val = UTIL_GlobalConfig.getDvpConfig("LO1231232");
		//logger.debug("222: " + val);
		
		
		
	}

	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		//#logger.info("jc:#");		
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		UserInfo ui = (UserInfo) session.getAttribute("UserInfo");	
		String msg = "";	
		//logger.debug("login: " + msg);
		out.print(msg);	
	}

	/**
	 * 執行登入
	 */
	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		
		String loginInfo = UTIL_String.nvl(request.getParameter("loginInfo"), "{}");
		JSONObject loginJo = JSONObject.fromObject(loginInfo);
		String compCode = loginJo.getString("txtCompCode");
		
		boolean result = false;
		JSONObject rtn = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			
			//logger.debug("10/28 do read open connection: " + compCode);;
			pes.openConnection("HBD_DMZ");
		
			HAC0BASC_DAO hac0Dao = new HAC0BASC_DAO(pes.getConnection());				
			
	    				
			JSONObject bascAcct = hac0Dao.getProfile(compCode, loginJo.getString("txtLoginID"));
			// 帳密檢查
			
			
			String loginMark = "";
			String loginComp = "";
			String loginID = "";
			String loginGroup = "";
			
			//System.out.println("basc:" + bascAcct.getString("hac0BascPwd"));
			//System.out.println(" txt:" + loginJo.getString("txtPassword"));
			//System.out.println(bascAcct.getString("hac0BascPwd").equals(loginJo.getString("txtPassword")));

			// TODO: 錯誤嘗試次數
			int errTryCnt = 0;			
			//String strTry = (String)session.getAttribute("");
			
			if (null == bascAcct){
				logger.info(String.format(" Login Fail: %s-%s-%s", compCode, loginJo.getString("txtLoginID"), loginJo.getString("txtPassword")));
				result = false;		
				loginMark = "N";
			}else{
				
				if (!bascAcct.getString("hac0BascPwd").equals(loginJo.getString("txtPassword"))){					
					logger.info(String.format(" Login Fail: %s-%s-%s", compCode, loginJo.getString("txtLoginID"), loginJo.getString("txtPassword")));
					result = false;					
					loginMark = "N";					
				}else{			
					// 登入成功					
					result = true;
					loginMark = "Y";
					loginComp = bascAcct.getString("hac0BascCompCode");
					loginID = bascAcct.getString("hac0BascLoginId");
					//loginGroup = bascAcct.getString("hac0BascGroup");
					loginGroup = bascAcct.getString("hac0BascGroup");					
					logger.info(String.format(" Login OK. (%s-%s)", loginGroup, loginJo.getString("txtLoginID")));
					
					// 設定cookie
			        // setting cookie
					
					Cookie compCookie = new Cookie("CompCode", compCode);
			        response.addCookie(compCookie);
			        compCookie.setMaxAge(60*60);
			        
			        Cookie loginCookie = new Cookie("LoginID", loginJo.getString("txtLoginID"));
			        response.addCookie(loginCookie);
			        loginCookie.setMaxAge(60*60);
			        
			        Cookie passwdCookie = new Cookie("Password", bascAcct.getString("hac0BascPwd"));
			        response.addCookie(passwdCookie);
			        passwdCookie.setMaxAge(60*60);
			        
			        Cookie roleCookie = new Cookie("UserRole", loginGroup);
			        response.addCookie(roleCookie);
			        roleCookie.setMaxAge(60*60);			        
			        
				}
				
			}
			
			rtn.put("LoginMark", loginMark);
			rtn.put("LoginComp", loginComp);
			rtn.put("LoginID", loginID);
			rtn.put("LoginGroup", loginGroup);
			rtn.put("LoginDate", UTIL_String.getNowString("yyyyMMdd"));
			
			
			String pText =  String.format("{\"LoginMark\":\"%s\",\"LoginComp\":\"%s\",\"LoginID\":\"%s\",\"LoginGroup\":\"%s\", \"LoginDate\":\"%s\"}", loginMark, loginComp, loginID, loginGroup, UTIL_String.getNowString("yyyyMMdd"));
			
			//logger.debug(String.format("{\"LoginMark\":\"%s\",\"LoginComp\":\"%s\",\"LoginID\":\"%s\",\"LoginGroup\":\"%s\", \"LoginDate\":\"%s\"}", loginMark, loginComp, loginID, loginGroup, UTIL_String.getNowString("yyyyMMdd")));
			
			
			String tabKey = UTIL_Encrypt_Base64.encrypt(pText);
			
			//System.out.println("tabKey: " + tabKey);
			
			rtn.put("tabKey", tabKey);
			
			//System.out.println("rtnJo: " + rtn.toString());
			
			Iterator<String> keys = rtn.keys();

			while(keys.hasNext()) {
			    String key = keys.next();
			    //System.out.println("key: " + rtn.get(key));
			    String val = (String) rtn.get(key);
			    if (rtn.get(key) instanceof JSONObject) {
			          session.setAttribute(key, val);      
			    }
			}
			
			
			String msg = MsgUtils.toString(true, "", rtn);
			//System.out.println("msg: " + msg);
			out.print(msg);
			
		}catch(Exception ex){
			ex.toString();
			logger.error(ex.toString());
			throw ex;
		}finally{
			
		}
		
		/*
		
		
		
		
		
		
		String strLoginMark = "LoginMark";
		String loginMark = (String) session.getAttribute(strLoginMark);
		
		if (null == loginMark){
			logger.debug(" !! loginMark is null, init loginMark -> NN");
			loginMark = "NN";
		}else{
			loginMark = "Y";
			logger.debug(" !! loginMark not nul, plus Y --> " + loginMark);					
		}	
		
		session.setAttribute(strLoginMark, "Y");
		
		JSONObject jo = new JSONObject();
		
		jo.put("LoginMark", loginMark);
		
		response.getWriter().print(MsgUtils.toString(true, "", jo));
		
		*/
		
		



		
	}

	

	/**
	 * page_load, 讀登入過的loginid
	 */
	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("doUpdate:#");
		
		PrintWriter out = response.getWriter();
		
		// 讀取成功登入過的 COOKIE
        JSONObject cookieJo = new JSONObject();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if("LoginID".equals(cookie.getName())) {
                //cookie.setValue("");
                cookieJo.put("LoginID", cookie.getValue());                
            }
            if("Password".equals(cookie.getName())) {
                cookieJo.put("Password", cookie.getValue());                
            }
            //System.out.println("Name: " + cookie.getName() + "; Value: " + cookie.getValue());            
        }
		
		
        String msg = MsgUtils.toString(true, "", cookieJo);			
		out.print(msg);
		
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
				
		// logger.info("jd#");		
		
		
		
		 

	}
	
	
}
