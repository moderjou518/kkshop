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
import com.hms.web.hbd1.dao.HFS0CACP_DAO;
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
public class HBD1W330_ACTION implements HMS_WEBACTION {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");

	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		logger.info("HBD1W330 doAction:#");
		
		/* 第一次進 來載入廠商資料 */
		HttpSession session = request.getSession();		
		
		
	
	}

	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		String method = UTIL_String.nvl(request.getParameter("methodName"), null);		
		
		if ("ADD_DATA_ONE".equals(method)){		
			this.ADD_DATA_ONE(request, response);			
		}
		
		if ("ADD_DATA_LIST".equals(method)){			
			this.ADD_DATA_LIST(request, response);			
		}		
	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		logger.info("jr:#");
				
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
		
		logger.info("doUpdate:#");
		
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);
		
		if ("SAVE_DATA_ONE".equals(method)){			
			this.SAVE_DATA_ONE(request, response);			
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
			HFS0CACP_DAO hfsDao =  new HFS0CACP_DAO(pes.getConnection());
			boolean result = hfsDao.deleteOneData(compCode, delID);
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
			JSONArray itemList = itemDAO.queryItemList(compCode, "*");						
			rtnJo.put("ITEM_LIST", itemList);			
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
	
	private void ADD_DATA_ONE(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		
				
		String info = UTIL_String.nvl(request.getParameter("info"), "{}");
		JSONObject parmJo = JSONObject.fromObject(info);		
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");			
			HFS0CACP_DAO hfsDao =  new HFS0CACP_DAO(pes.getConnection());
			String cacpDate = UTIL_String.getNowString("yyyyMMdd");
			String newNo = hfsDao.getNewNo(compCode, cacpDate);
			parmJo.put("COMP_CODE", compCode);
			parmJo.put("CACP_DATE", cacpDate);
			parmJo.put("CACP_NO", newNo);
			parmJo.put("CACP_NOTES", parmJo.getString("txtCacpNotes"));
			parmJo.put("CACP_RECP_CODE", parmJo.getString("dlItemCode"));
			parmJo.put("CACP_QTY", parmJo.getString("txtTrnsQty"));
			parmJo.put("CACP_USER", ui.getLoginID(request));

			boolean result = hfsDao.addOneData(parmJo);
			String msg = MsgUtils.toString(result, "", rtnJo);
							
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
	
	private void ADD_DATA_LIST(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
	
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);	
		String updInfo = UTIL_String.nvl(request.getParameter("updInfo"), "{}");
				
		String loginID = ui.getLoginID(request);			
		
		
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");			
			HFS0CACP_DAO hfsDao =  new HFS0CACP_DAO(pes.getConnection());						
		
			int updRow = 0;
			int updCnt = 0;
			String cacpDate = UTIL_String.getNowString("yyyyMMdd");
			String newNo = hfsDao.getNewNo(compCode, cacpDate);
			JSONArray updAry = JSONArray.fromObject(updInfo);
			updRow = updAry.size();			
			for(int i=0; i<updAry.size(); i++) {				
				JSONObject jo = updAry.getJSONObject(i);
				JSONObject parmJo = new JSONObject();
				parmJo.put("COMP_CODE", compCode);
				parmJo.put("CACP_DATE", cacpDate);
				parmJo.put("CACP_NO", newNo);
				parmJo.put("CACP_NOTES", jo.getString("txtCacpNotes"));
				parmJo.put("CACP_RECP_CODE", jo.getString("dlItemCode"));
				parmJo.put("CACP_QTY", jo.getString("txtTrnsQty"));
				parmJo.put("CACP_USER", loginID);
				if(hfsDao.addOneData(parmJo)){
					updCnt += 1;
				}				
			}
			
			boolean result = (updRow == updRow);			
			 
			String msg = MsgUtils.toString(result, "", rtnJo);
			System.out.println("AddDataList: " + msg);
							
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
	
	private void SAVE_DATA_ONE(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);						
		String loginID = ui.getLoginID(request);
		String info = UTIL_String.nvl(request.getParameter("info"), "{}");
		JSONObject parmJo = JSONObject.fromObject(info);
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");			
			HFS0CACP_DAO hfsDao =  new HFS0CACP_DAO(pes.getConnection());
			parmJo.put("COMP_CODE", compCode);					
			parmJo.put("loginID", ui.getLoginID(request));
			//
			boolean result = hfsDao.saveOneData(parmJo);
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
	
	private void QUERY_DATA_LIST(String compCode, HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {		
		
		String qryInfo = UTIL_String.nvl(request.getParameter("qryInfo"), "{}");
		JSONObject qryJo = JSONObject.fromObject(qryInfo);		
		
		//System.out.println("qryInfo: " + qryJo.toString());
		//System.out.println("qryInfo: " + qryInfo);
		
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");			
			HFS0CACP_DAO hfsDao =  new HFS0CACP_DAO(pes.getConnection());
			JSONArray trnsList = hfsDao.queryCacpList(compCode, qryJo.getString("qryTrnsDate1"), qryJo.getString("qryTrnsDate2"));			
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
