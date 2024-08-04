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
import com.hms.web.hbd1.dao.HFS0CACP_DAO;
import com.hms.web.hbd1.dao.HMI0TRNS_DAO;
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
public class HBD1W670_ACTION implements HMS_WEBACTION {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");

	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("HBD1W670 doAction:#");
		
		/* 第一次進 來載入廠商資料 */
		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);

		
		
	}

	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		//logger.info("jc:#");
	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("jr:#");

		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);				
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);		
		
		System.out.println("#### 670 query data list");
		
		if ("QUERY_DATA_LIST".equals(method)){			
			this.QUERY_DATA_LIST(compCode, request, out);			
		}
	}

	

	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("doUpdate:#");
		
		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		
		String method = UTIL_String.nvl(request.getParameter("methodName"), "");	
		String addInfo = UTIL_String.nvl(request.getParameter("addInfo"), "{}");
		String updInfo = UTIL_String.nvl(request.getParameter("updInfo"), "{}");
	
		String rtn = "";
		String msg = "";
		boolean success = false;
		PrepareExecuteSql pes = new PrepareExecuteSql();
		
		try {								
			pes.openConnection("HBD_DMZ");
			HMP0ITEM_DAO itemDAO = new HMP0ITEM_DAO(pes.getConnection());
			
			// update
            int updRow = 0;
            int updCnt = 0;
            JSONArray updAry = JSONArray.fromObject(updInfo);
            updRow = updAry.size();
            for(int i=0; i<updAry.size(); i++) {                    
                JSONObject jo = updAry.getJSONObject(i);
                jo.put("compCode", compCode);                
                int c = itemDAO.updateData(jo);
                if (c == 1){
                    updCnt += c;    
                }else{
                	// 用 msg 保留 exception 當下的 code號
                    msg = "Upd[" + jo.getString("txtItemName") + "]";
                    break;
                }
            }
            
            // insert
            int addRow = 0;
            int addCnt = 0;            
            JSONArray addAry = JSONArray.fromObject(addInfo);
            addRow = addAry.size();
            for(int i=0; i < addAry.size(); i++) {
                JSONObject jo = addAry.getJSONObject(i);
                jo.put("compCode", compCode);
                int c = itemDAO.addData(jo);                
                if (c == 1){
                	addCnt += c;
                }else{
                	// 用 msg 保留 exception 當下的 code號
                	msg = "Add[" + jo.getString("txtItemName") + "]";
                	break;
                }
            }
            
            if ((updRow+addRow) == (updCnt+addCnt)){
            	pes.getConnection().commit();
                msg = "";
                success = true;
            }else{
            	pes.getConnection().rollback();
                msg += " ! Data save error!";
                success = false;
            }
            
            JSONObject jo = new JSONObject();            
            jo.put("count", (updCnt+addCnt));			  
            rtn = MsgUtils.toString(success, msg, jo);
			out.print(rtn);
            
		}catch(Exception e){
			logger.error("Action Method: " + method);
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
		
		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		
		String method = UTIL_String.nvl(request.getParameter("methodName"), "");
		String delID = UTIL_String.nvl(request.getParameter("delID"), "");		
		String rtn = "";
		String msg = "";
		boolean success = false;
		
		PrepareExecuteSql pes = new PrepareExecuteSql();		
		try {						
			pes.openConnection("HBD_DMZ");
			HMP0ITEM_DAO itemDAO = new HMP0ITEM_DAO(pes.getConnection());
			
			// delete            
            int delCnt = 0;                                           
            delCnt = itemDAO.deleteThisData(compCode, delID);
            if (delCnt == 1){
            	msg = "";
                success = true;    
            }else{
                msg = "Del[" + delID + "]";
                success = false;
            }
            
            JSONObject jo = new JSONObject();
            rtn = MsgUtils.toString(success, msg, jo);
			out.print(rtn);
            
		}catch(Exception e){
			logger.error("Action Method: " + method);
			logger.error(e.toString());
			e.printStackTrace();			
			rtn = MsgUtils.toString(false, e.toString(), null);
          	out.print(rtn);
		}finally{
			pes.closeConnection();
		}
		
		// setCookie(有encode就要decode)
		/*
		Cookie url = new Cookie("cid", URLEncoder.encode("12345678", "UTF-8"));

		url.setMaxAge(60 * 60 * 24);
		response.addCookie(url);
		response.setContentType("text/html;charset=UTF-8");
		*/
		
		 

	}

	private void QUERY_DATA_LIST(String compCode, HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {
		
		String qryInfo = UTIL_String.nvl(request.getParameter("qryInfo"), "{}");
		JSONObject qryJo = JSONObject.fromObject(qryInfo);	
		
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();				
		try {
			pes.openConnection("HBD_DMZ");
			HMP0POMM_DAO pommDAO = new HMP0POMM_DAO(pes.getConnection());	
			HMI0TRNS_DAO trnsDao = new HMI0TRNS_DAO(pes.getConnection());
			HFS0CACP_DAO cacpDao = new HFS0CACP_DAO(pes.getConnection());
			JSONArray itemList = pommDAO.query670Report(compCode, qryJo.getString("qryPoDate1"), qryJo.getString("qryPoDate2"));
			for(int i=0; i < itemList.size(); i++){
				JSONObject jo = itemList.getJSONObject(i);				
				
				String inQty = trnsDao.getTrnsQtySum(compCode, jo.getString("poddItem"),qryJo.getString("qryPoDate1"),qryJo.getString("qryPoDate2"));
				if (null == inQty || "".equals(inQty)){
					inQty = "0";
				}
				jo.put("trnsQtySum", inQty);
				
				String caQty = cacpDao.getCacpQtySum(compCode, jo.getString("poddItem"),qryJo.getString("qryPoDate1"),qryJo.getString("qryPoDate2"));
				if (null == caQty || "".equals(caQty)){
					caQty = "0";
				}
				jo.put("cacpQtySum", caQty);
				
				
			}
			rtnJo.put("success", true);
			rtnJo.put("POSTAT", itemList.toString());				
			out.print(MsgUtils.toString(true, "", rtnJo));	
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: 670 QUERY_DATA_LIST");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}
		
	}

}
