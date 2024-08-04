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
import com.hms.web.hbd1.dao.HMI0TRNS_DAO;
import com.hms.web.hbd1.dao.HMP0ITEM_DAO;
import com.hms.web.hbd1.dao.HMS0MISC_DAO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * HBD1W020 
 * 
 * @author Window10
 * 
 */
public class HBD1W510_ACTION implements HMS_WEBACTION {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");

	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("HBD1W510 doAction:#");
		
		/* 第一次進 來載入廠商資料 */
		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		JSONObject parmJo = new JSONObject();
		JSONObject rtnJo = new JSONObject();
		
		
		
	
	}

	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		//logger.info("jc:#");
		
		
		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);
					
		
		
		
		if ("ADD_DATA_ONE".equals(method)){		
			this.ADD_DATA_ONE(compCode, request, out);			
		}
	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("jr:#");
				
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);
					
		
		
		
		if ("PAGE_LOAD".equals(method)){		
			this.PAGE_LOAD(compCode, out);			
		}
		
		if ("QUERY_DATA_LIST".equals(method)){			
			this.QUERY_DATA_LIST(compCode, request, out);			
		}
	}

	

	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("doUpdate:#");		
				
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		String info = UTIL_String.nvl(request.getParameter("info"), "{}");
		JSONObject parmJo = JSONObject.fromObject(info);		
		
		System.out.println("Info: " + info);
		
		
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");			
			HMI0TRNS_DAO hmiDao =  new HMI0TRNS_DAO(pes.getConnection());
			parmJo.put("COMP_CODE", compCode);					
			parmJo.put("loginID", ui.getLoginID(request));
			
			
			
			//
			boolean result = hmiDao.saveOneData(parmJo);
			String msg = MsgUtils.toString(result, "", rtnJo);
			System.out.println("saveOne: " + msg);
							
			out.print(msg);	
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: SAVE ONE DATA");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}			
		
		
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
				
		// 新增資料
		// logger.info("jd#");		
						
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		String delID = UTIL_String.nvl(request.getParameter("delID"), "");		
		
		//System.out.println("DELETE Info: " + delID);	
		
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");			
			HMI0TRNS_DAO hmiDao =  new HMI0TRNS_DAO(pes.getConnection());
			boolean result = hmiDao.deleteOneData(compCode, delID);
			String msg = MsgUtils.toString(result, "", rtnJo);
			System.out.println("deleteOne: " + msg);							
			out.print(msg);	
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: DELETE ONE DATA");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}

		 

	}
	
	private void PAGE_LOAD(String compCode, PrintWriter out) throws FileNotFoundException, Exception {
		
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");
			HMP0ITEM_DAO itemDAO = new HMP0ITEM_DAO(pes.getConnection());
			HMS0MISC_DAO miscDao = new HMS0MISC_DAO(pes.getConnection());
			
			JSONArray itemList = itemDAO.queryItemList(compCode, "*");						
			rtnJo.put("ITEM_LIST", itemList);					
						
			JSONArray vendAry = miscDao.queryDataList(compCode, "VENDOR");
			rtnJo.put("VENDOR_LIST", vendAry);			
			
			String msg = MsgUtils.toString(true, "", rtnJo);
			
			out.print(msg);	
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: QUERY_DATA_LIST");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}	
		
	}
	
	private void ADD_DATA_ONE(String compCode, HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {
		
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		
		
		String info = UTIL_String.nvl(request.getParameter("info"), "{}");
		JSONObject parmJo = JSONObject.fromObject(info);		
		
		System.out.println("Info: " + info);
		
		
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");			
			HMI0TRNS_DAO hmiDao =  new HMI0TRNS_DAO(pes.getConnection());
			parmJo.put("COMP_CODE", compCode);			
			parmJo.put("parmTrnsSeq", "001");
			parmJo.put("txtStore", "GST");
			parmJo.put("txtTrnsUnitPrice", "0");
			parmJo.put("txtInvoiceNo", "");
			parmJo.put("loginID", ui.getLoginID(request));
			
			//
			boolean result = hmiDao.addOneData(parmJo);
			String msg = MsgUtils.toString(result, "", rtnJo);
			System.out.println("addOne: " + msg);
							
			out.print(msg);	
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: QUERY_DATA_LIST");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}			
		
	}
	
	private void QUERY_DATA_LIST(String compCode, HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {		
		
		String qryInfo = UTIL_String.nvl(request.getParameter("qryInfo"), "{}");
		JSONObject qryJo = JSONObject.fromObject(qryInfo);		
		
		//System.out.println("qryInfo: " + qryJo.toString());
		//System.out.println("qryInfo: " + qryInfo);
		
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");			
			HMI0TRNS_DAO hmiDao =  new HMI0TRNS_DAO(pes.getConnection());
			JSONArray trnsList = hmiDao.queryTransList(compCode, qryJo.getString("qryTrnsDate"));			
			rtnJo.put("success", true);
			rtnJo.put("TRNS_LIST", trnsList.toString());				
			out.print(MsgUtils.toString(true, "", rtnJo));	
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: QUERY_DATA_LIST");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}			

		
	}
}
