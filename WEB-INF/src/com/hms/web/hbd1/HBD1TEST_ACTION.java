package com.hms.web.hbd1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.evergreen_hotels.bmg.whms1.exception.DataAccessException;
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
public class HBD1TEST_ACTION implements HMS_WEBACTION {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");

	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		logger.info(" !! HBD1TEST_ACTION doAction:#");
		
	
		
		
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
		
	
		
	}

	

	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		logger.info("doUpdate:#");
		
		logger.info("ddddddddddddddddddddd");
		
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		UserInfo ui = (UserInfo) session.getAttribute("UserInfo");
		
		boolean result = false;
		JSONObject rtn = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			
			logger.debug("open connection ");;
			pes.openConnection("HBD_DMZ");
		
			
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			
			bidDao.queryAllBidmByComp("1122334455");
			
			
			String msg = MsgUtils.toString(true, "", rtn); 
			System.out.println("rtn msg: " + msg);
			out.print(msg);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			
		}
		

		
		
			
		
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
				
		// logger.info("jd#");		
		
		
		
		 

	}
	
	
}
