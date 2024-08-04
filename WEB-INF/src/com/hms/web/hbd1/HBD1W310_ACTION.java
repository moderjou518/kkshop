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
import com.hms.web.hbd1.dao.HMP0BID_DAO;
import com.hms.web.hbd1.dao.HMP0ITEM_DAO;
import com.hms.web.hbd1.dao.HMP0POMM_DAO;
import com.hms.web.hbd1.dao.HMS0MISC_DAO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * HBD1W31
 * 
 * @author Window10
 * 
 */
public class HBD1W310_ACTION implements HMS_WEBACTION {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");

	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		logger.info("HBD1W310 doAction:#");
		
		/* 第一次進 來載入廠商資料 */
		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		
					

	}

	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		logger.info("310 jc:#");	
	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		logger.info("310 jr:#");
		
		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) session.getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);					
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);
		
		if ("LOAD_CUSTOMER".equals(method)){						
			String selDay = UTIL_String.nvl(request.getParameter("selectDay"), "");			
			this.LOAD_CUSTOMER(compCode, selDay, out);								
		}
		
		if ("LOAD_CUST_POS_LIST".equals(method)){
			String selBuyer = UTIL_String.nvl(request.getParameter("selBuyer"), "");
			String selDay = UTIL_String.nvl(request.getParameter("selDate"), "");
			String selSeq = UTIL_String.nvl(request.getParameter("selSeq"), "");			
			this.LOAD_CUST_POS_LIST(compCode, selBuyer, selDay, selSeq, out);					
		}
		
	}
	

	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		logger.info("310 doUpdate:#");
		
		PrintWriter out = response.getWriter();
		String compCode = (String) request.getSession().getAttribute("COMP_CODE");		
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);
		
		System.out.println("method: " + method);
		
		if ("SAVE_RCV_INFO".equals(method)){			
			this.SAVE_RCV_INFO(request, out);
		}			
		
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {				
		logger.info("310 jd#");
	}
	
	private void LOAD_CUSTOMER(String compCode, String selDay, PrintWriter out) throws FileNotFoundException, Exception {
		
		JSONObject rtnJo = new JSONObject(); 
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");
			HMP0POMM_DAO poDAO = new HMP0POMM_DAO(pes.getConnection());				
			JSONArray custList = poDAO.queryPeriodCustomer(compCode, selDay, selDay);
			rtnJo.put("CUST_LIST", custList.toString());
			rtnJo.put("WEEKDAY", UTIL_String.getWeekOfDate(selDay));
			//System.out.println(MsgUtils.toString(true, "", rtnJo));
			out.print(MsgUtils.toString(true, "", rtnJo));	
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: Load_Customer");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}
		
	}
	
	private void LOAD_CUST_POS_LIST(String compCode, String selBuyer, String selDay, String selSeq, PrintWriter out) throws FileNotFoundException, Exception {
		
		JSONObject rtnJo = new JSONObject(); 
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");
			HMP0POMM_DAO poDAO = new HMP0POMM_DAO(pes.getConnection());				
			JSONArray custList = poDAO.queryPOList(compCode, selBuyer, selDay, selSeq);
			rtnJo.put("PO_ITEM_LIST", custList.toString());		
			System.out.println("PO ITEM LIST: " + MsgUtils.toString(true, "", rtnJo));
			out.print(MsgUtils.toString(true, "", rtnJo));	
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: LOAD_CUST_POS_LIST");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}
		
	}
	
	private void SAVE_RCV_INFO(HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {
		
		HttpSession session = request.getSession();
		UserInfo ui = (UserInfo) session.getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);				
		
		String info = UTIL_String.nvl(request.getParameter("revInfo"), "{}");
		JSONObject jo = JSONObject.fromObject(info);	
		System.out.println("info: " + info);
		String[] poDateAry = jo.getString("txtPoddDate").split(",");
		String[] poSeqAry = jo.getString("txtPoddSeq").split(",");
		String[] itemAry = jo.getString("txtPoddItem").split(",");
		String[] qtyAry = jo.getString("txtPoddRcvdQty").split(",");		
		
		JSONObject rtnJo = new JSONObject(); 
		PrepareExecuteSql pes = new PrepareExecuteSql();
		
		try {
			pes.openConnection("HBD_DMZ");
			
			HMP0POMM_DAO poDAO = new HMP0POMM_DAO(pes.getConnection());
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			
			
			
			int cnt = 0;
			for(int i=0; i<itemAry.length; i++){
				
				String poDate = poDateAry[i].replace("\"", "").replace("]", "").replace("[", "");
				String poSeq = poSeqAry[i].replace("\"", "").replace("]", "").replace("[", "");
				String poItem = itemAry[i].replace("\"", "").replace("]", "").replace("[", "");
				String poQty = qtyAry[i].replace("\"", "").replace("]", "").replace("[", "");
				String bidmUUID = bidDao.getBidmUUIDByPoDate(compCode, poDate);
				JSONObject bidItemJo = bidDao.getBiddData(compCode, bidmUUID, poItem);
				
				int c = poDAO.updateRcvPO(compCode, poDate, poSeq, poItem, poQty, bidItemJo.getString("biddPrice"), ui.getLoginID(request));
				if (c!=1){
					String msg = String.format("無法更新此筆訂單的物料發貨數量:C:%s, PO:%s-%s, Q:%s-%s", compCode, poDate, poSeq, poItem, poQty);
					logger.error(msg);;						
				}else{
					cnt ++;
				}								
			}
			
			
			if (cnt != itemAry.length){
				out.print(MsgUtils.toString(false, "", rtnJo));	
			}else{
				out.print(MsgUtils.toString(true, "", rtnJo));
			}
			
				
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: SAVE_RCV_INFO");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}
	}
	
}
