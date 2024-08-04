package com.hms.web.hbd1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
import com.hms.web.hbd1.dao.HMP0POMM_DAO;
import com.hms.web.hbd1.dao.HMS0MISC_DAO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * HBD1W030 
 * 
 * @author Window10
 * 
 */
public class HBD1W030_ACTION implements HMS_WEBACTION {

	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		
		logger.info("HBD1W030 doAction:#");
		
		/* 固定寫法 */
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		/* 固定寫法 */				

		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");
			HMS0MISC_DAO miscDAO = new HMS0MISC_DAO(pes.getConnection());
			
			RowData rd = miscDAO.getCompData(compCode);
			JSONObject jo = null;
			if (null != rd){
				rtnJo.put("success", true);				
				jo = rd.toJSONObject();
				
			}else{
				rtnJo.put("success", false);
				compCode = "";
								
			}
		} catch (Exception ex) {
			String msg = ex.toString();
			rtnJo.put("msg", msg);
			rtnJo.put("success", false);			
			logger.error(msg);
			logger.error("compCode: " + compCode);
			ex.printStackTrace();
		} finally {
			pes.closeConnection();
		}
		
	}

	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		//#logger.info("jc:#");	
	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		/* 固定寫法 */
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		/* 固定寫法 */				
		
		String method = UTIL_String.nvl(request.getParameter("methodName"), "#");		
		//// logger.info("jr method: " + method);
		
		// 載入客戶當月的預訂單列表
		if ("LOAD_PROFILE".equals(method)){
			this.LOAD_PROFILE(compCode, ui.getLoginID(request), out);
		}
		
		// 載入客戶該周的預訂單明細
		if ("LOAD_PODD_BY_WEEK_BUYER".equals(method)){
			String buyer = ui.getLoginID(request);
			String qryUUID = UTIL_String.nvl(request.getParameter("qryBidUUID"), "#");		
			this.LOAD_PODD_BY_WEEK_BUYER(compCode, buyer, qryUUID, out);
		}

	}

	

	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
				
		//logger.info("doUpdate:#");
		
		/* 固定寫法 */		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);		
		/* 固定寫法 */
		
		String info =  UTIL_String.nvl(request.getParameter("info"), "{}");
		JSONObject jo = JSONObject.fromObject(info);
		
		String rtn = "";
		String msg = "";		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		
		try {								
			pes.openConnection("HBD_DMZ");			
			HAC0BASC_DAO hac0Dao = new HAC0BASC_DAO(pes.getConnection());			
			hac0Dao.saveProfile(compCode, ui.getLoginID(request), jo);		
			JSONObject empJo = hac0Dao.getProfile(compCode, ui.getLoginID(request));
            rtn = MsgUtils.toString(true, msg, empJo);
            //System.out.println("saveProfile: " + rtn);
			out.print(rtn);            
		}catch(Exception e){
			logger.error("Action Method: LOAD_PROFILE");
			logger.error(e.toString());
			e.printStackTrace();			
			rtn = MsgUtils.toString(false, e.toString(), null);
          	out.print(rtn);
		}finally{
			pes.closeConnection();
		}		

	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		// logger.info("jd#");		
	}
	
	private void LOAD_PROFILE(String compCode, String loginID, PrintWriter out) throws Exception{
					
		String rtn = "";
		String msg = "";		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		
		try {								
			pes.openConnection("HBD_DMZ");			
			HAC0BASC_DAO hac0Dao = new HAC0BASC_DAO(pes.getConnection());			
			JSONObject jo = hac0Dao.getProfile(compCode, loginID);						  
            rtn = MsgUtils.toString(true, msg, jo);
            //System.out.println("loadProfile: " + rtn);
			out.print(rtn);            
		}catch(Exception e){
			logger.error("Action Method: LOAD_PROFILE");
			logger.error(e.toString());
			e.printStackTrace();			
			rtn = MsgUtils.toString(false, e.toString(), null);
          	out.print(rtn);
		}finally{
			pes.closeConnection();
		}
		
	}
	
	private void LOAD_PODD_BY_WEEK_BUYER(String compCode, String buyer, String qryUUID, PrintWriter out) throws Exception{				
		
		//System.out.println(String.format("c/b/u: %s/%s/%s", compCode, buyer, qryUUID));
		
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		
		try {
		
			pes.openConnection("HBD_DMZ");
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			HMP0POMM_DAO poDao = new HMP0POMM_DAO(pes.getConnection());			
			
			// 找出當周起迄日
			JSONObject bidm = bidDao.getBidmData(compCode, qryUUID);
										
			// 用起迄日去找出預訂單明細
			String sDate = bidm.getString("bidOrdSdate");
			String eDate = bidm.getString("bidOrdEdate");					
			
			// 當周每天訂單
			JSONArray ary = poDao.queryWeekPOList(compCode, buyer, sDate, eDate);
			
			// 訂單總金額											
			rtnJo.put("success", true);				
			rtnJo.put("BIDM", bidm);		
			rtnJo.put("WEEK_PO", ary);
			String rtn = MsgUtils.toString(true, "", rtnJo);			
			out.print(rtn);				
		} catch (Exception ex) {
			String msg = ex.toString();
			rtnJo.put("msg", msg);
			rtnJo.put("success", false);			
			logger.error(msg);				
			ex.printStackTrace();
		} finally {
			pes.closeConnection();
		}	
	
	}
	
	private void SAVE_BIDM_MONEY(String compCode, String buyer, HttpServletRequest request, PrintWriter out) throws Exception{
	
		String info = UTIL_String.nvl(request.getParameter("info"), "{}");
		JSONObject jo = JSONObject.fromObject(info);
		String bidUUID = jo.getString("txtBidmUUID");
		
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");
			HMI0OFFS_DAO offDao = new HMI0OFFS_DAO(pes.getConnection());					
			
			JSONObject parmJo = new JSONObject();
			parmJo.put("OFFS_COMP_CODE", compCode);
			parmJo.put("OFFS_BUYER", buyer);
			parmJo.put("OFFS_BIDM_UUID", bidUUID);   	
			parmJo.put("OFFS_PAY_DATE", jo.getString("txtDate"));
			parmJo.put("OFFS_PAY_AMT", jo.getString("txtAmount"));
			parmJo.put("OFFS_PAY_NOTE", "");    	
			parmJo.put("OFFS_DOC_MARK", "Y");			
			offDao.resetDocMark(compCode, buyer, bidUUID);
			
			boolean insOk = false;
			insOk = offDao.insertOffs(parmJo);	    												
			rtnJo.put("success", insOk);
			out.print(MsgUtils.toString(true, "", rtnJo));				
		} catch (Exception ex) {
			String msg = ex.toString();
			rtnJo.put("msg", msg);
			rtnJo.put("success", false);			
			logger.error(msg);				
			ex.printStackTrace();
		} finally {
			pes.closeConnection();
		}
		
	}
	
	
}
