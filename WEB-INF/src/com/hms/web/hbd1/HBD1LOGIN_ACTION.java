package com.hms.web.hbd1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
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
public class HBD1LOGIN_ACTION implements HMS_WEBACTION {
	
	Logger logger = Logger.getLogger("KKSHOP_LOGIN_LOG");

	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info(" !! HBD1LOGIN_ACTION doAction:#");
		
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
		
		JSONObject jo = new JSONObject();
		JSONObject rtn = new JSONObject();
		
		if (ui == null){
			jo.put("hac0BascLoginId", "");
			jo.put("hac0BascCompCode", "");
		}else{
			jo.put("hac0BascLoginId", ui.getLoginID(request));
			jo.put("hac0BascCompCode", ui.getCompCode(request));	
		}
		
					
		
		String msg = "";
		msg = MsgUtils.toString(true, "", jo);
		
		logger.debug("login: " + msg);
		
		out.print(msg);
		
		
		
	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		logger.info(" !! do login: jr");	
		
		logger.info(" !! loginID: " + request.getParameter("txtLoginID"));
		
		PrintWriter out = response.getWriter();		
		
    	// loop cartItem & transfer to order item
		String loginInfo = UTIL_String.nvl(request.getParameter("loginInfo"), "{}");
		JSONObject loginJo = JSONObject.fromObject(loginInfo);
		HttpSession session = request.getSession();
		
		String compCode = loginJo.getString("txtCompCode");
		
		logger.debug("loginInfo: " + loginInfo);
		
    	JSONObject userJo = null;
    	UserInfo ui = null;
    	String LoginMark = "N";
    	boolean result = false;
    	String msg = "";
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			
			logger.debug("open connection ");;
			pes.openConnection("HBD_DMZ");				
		
			HAC0BASC_DAO hac0Dao = new HAC0BASC_DAO(pes.getConnection());				
			
	    	// 帳密檢查
			logger.debug(String.format(" !! check login: %s-%s-%s ", compCode, loginJo.getString("txtLoginID"), loginJo.getString("txtPassword")));
			boolean chkLogin = hac0Dao.loginCheck(compCode, loginJo.getString("txtLoginID"), loginJo.getString("txtPassword"));
			
			logger.debug(" !! chkLogin: " + chkLogin);
			
			if (chkLogin == true){
				
				result = true;
				logger.debug(" !! get profile");;
				userJo = hac0Dao.getProfile(compCode, loginJo.getString("txtLoginID"));
				
				if (userJo == null){
					logger.debug(" !! userJo null");;
					LoginMark = "N";
					ui = new UserInfo();
					ui.setCompCode(request, "1122334455");
					ui.setLoginID(request, "0000000000");
					ui.setLoginRole(request, "C");
					result = false;
				}else{
					logger.debug(" !! userJo not null");;
					ui = new UserInfo();
					ui.setCompCode(request, userJo.getString("hac0BascCompCode"));
					ui.setLoginID(request, userJo.getString("hac0BascLoginId"));
					ui.setLoginRole(request, userJo.getString("hac0BascGroup"));
					LoginMark = "Y";					
				}				
									
			}else{
				result = false;
				userJo = new JSONObject();
				LoginMark = "N";
				msg = "id/pwd error";
				logger.debug(msg);										
			}
			
			
			session.setAttribute("UserInfo", ui);
			logger.debug("seet userinfo ok");
			//session.setAttribute("LoginMark", LoginMark);
			
			// 




			
			
			
			//UserInfo uuii = (UserInfo) session.getAttribute("UserInfo");
			//String lgnMark = (String) session.getAttribute("LoginMark");
			
			logger.debug(String.format(" !! valid login userInfo: ", ui.getCompCode(request), ui.getLoginID(request), ui.getLoginRole(request)));
			//logger.debug(" !! loginMark: " + lgnMark);;
						
			msg = MsgUtils.toString(result, msg, userJo);
			logger.debug(" !! msg: " + msg);
			JSONObject jj = JSONObject.fromObject(msg);
			logger.debug(" !! success: " + jj.getString("success"));
			logger.debug(" !! msg: " + jj.getString("msg"));
			out.println(msg);					
			
		}catch(Exception ex){			
			ex.printStackTrace();
		}finally{
			pes.getConnection().close();
		}
		
		logger.debug(" !! end process");;
		
	}

	

	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		logger.info("doUpdate:#");
		
		
		
			
		
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
				
		// logger.info("jd#");		
		
		
		
		 

	}
	
	
}
