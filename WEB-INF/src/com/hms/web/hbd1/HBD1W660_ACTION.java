package com.hms.web.hbd1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.evergreen_hotels.bmg.whms1.util.MsgUtils;
import com.evergreen_hotels.bmg.whms1.util.PrepareExecuteSql;
import com.hms.util.UTIL_String;
import com.hms.util.UserInfo;
import com.hms.web.HMS_WEBACTION;
import com.hms.web.hbd1.dao.HMP0BID_DAO;
import com.hms.web.hbd1.dao.HMP0ITEM_DAO;
import com.hms.web.hbd1.dao.HMP0POMM_DAO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * HBD1W660
 * 
 * @author Window10
 * 
 */
public class HBD1W660_ACTION implements HMS_WEBACTION {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");

	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("HBD1W660 doAction:#");
		
		/* 第一次進 來載入廠商資料 */
		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);

		
		
	}

	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		//#logger.info("jc:#");
	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("action jr:#");

		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);				
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);		
		
		//System.out.println("#### 660 query data list");
		
		if ("QUERY_DATA_LIST".equals(method)){			
			this.QUERY_DATA_LIST(compCode, request, out);			
		}
		
		
		if ("LOAD_BUYER_DAILY_PO".equals(method)){
			this.LOAD_BUYER_DAILY_PO(compCode, request, out);
		}
		
	}

	

	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		logger.info("660 doUpdate:#");
		
		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		String loginID = ui.getLoginID(request);
		
		String method = UTIL_String.nvl(request.getParameter("methodName"), "");
		String poMastInfo = UTIL_String.nvl(request.getParameter("poMastData"), "{}");
		String addInfo = UTIL_String.nvl(request.getParameter("addInfo"), "{}");
		String updInfo = UTIL_String.nvl(request.getParameter("updInfo"), "{}");			
		JSONObject poMastJo = JSONObject.fromObject(poMastInfo);
		
		//System.out.println("mastInfo: " + poMastJo.toString());
		//System.out.println("addInfo:" + addInfo);
		//System.out.println("updInfo:" + updInfo);
	
		String rtn = "";
		String msg = "";
		boolean success = false;
		PrepareExecuteSql pes = new PrepareExecuteSql();
		
		try {								
			pes.openConnection("HBD_DMZ");
			HMP0POMM_DAO poDao = new HMP0POMM_DAO(pes.getConnection());
			
			
			
			// update
            int updRow = 0;
            int updCnt = 0;
            JSONArray updAry = JSONArray.fromObject(updInfo);
            updRow = updAry.size();
            for(int i=0; i<updAry.size(); i++) {                    
                JSONObject jo = updAry.getJSONObject(i);                                
                int c = poDao.updateRcvPO(compCode, jo.getString("txtPoddUuid"), jo.getString("txtPoddRcvdQty"), jo.getString("txtPoddUnitPrice"), loginID);
                if (c == 1){
                    updCnt += c;    
                }else{
                	// 用 msg 保留 exception 當下的 code號
                    msg = "Upd PoddUUID Error[" + jo.getString("txtPoddUuid") + "]";
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
                jo.put("poddDate", poMastJo.getString("txtPoDate"));
                jo.put("poddSeq",  poMastJo.getString("txtPoSeq"));
                jo.put("poddItem", jo.getString("txtPoddItem"));                
                jo.put("poddRcvdQty", jo.getString("txtPoddRcvdQty"));
                jo.put("poddPrice", jo.getString("txtPoddUnitPrice"));         
                
                int c = poDao.appendPODDData(jo);
                //System.out.println("Add " + jo.getString("poddItem") + ": " + c);
                if (c == 1){
                	addCnt += c;
                }else{
                	// 用 msg 保留 exception 當下的 code號
                	msg = "Append[" + jo.getString("txtPoddItem") + "]";
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
		String delID = UTIL_String.nvl(request.getParameter("delUUID"), "");		
		String rtn = "";
		String msg = "";
		boolean success = false;
		
		PrepareExecuteSql pes = new PrepareExecuteSql();		
		try {						
			pes.openConnection("HBD_DMZ");			
			
			HMP0POMM_DAO poDao = new HMP0POMM_DAO(pes.getConnection());
			
			// delete            
            int delCnt = 0;
            delCnt = poDao.delAPODD(compCode, delID);            
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
			HMP0POMM_DAO poDao = new HMP0POMM_DAO(pes.getConnection());
			
			// 當天統計
			JSONObject jo = poDao.queryDailyPOStatistic(compCode, qryJo.getString("qryPoDate1"));
						
			// 當天清單
			JSONArray poList = poDao.queryDailyPO(compCode, qryJo.getString("qryPoDate1"), qryJo.getString("qryPoDate1"));
			for(int i = 0; i < poList.size(); i++){
				JSONObject row = poList.getJSONObject(i);
				String weekDay = "";
				weekDay = UTIL_String.getWeekOfDate(row.getString("pommDate"));
				row.put("weekDay", weekDay);
			}
									
			rtnJo.put("success", true);			
			rtnJo.put("DAILY_STAT", jo);
			rtnJo.put("POSTAT", poList.toString());
			//System.out.println("queryDataList:  " + rtnJo.toString());
			out.print(MsgUtils.toString(true, "", rtnJo));	
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: 660 QUERY_DATA_LIST");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}
		
	}
	
	
	
	private void LOAD_BUYER_DAILY_PO(String compCode, HttpServletRequest request, PrintWriter out) throws Exception{				
		
		String wkBuyer = UTIL_String.nvl(request.getParameter("parmBuyer"), "");
		String wkPoDate = UTIL_String.nvl(request.getParameter("parmPoDate"), "");
		String wkPoSeq = UTIL_String.nvl(request.getParameter("parmPoSeq"), "");
		
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		
		try {
		
			pes.openConnection("HBD_DMZ");
			HMP0POMM_DAO poDao = new HMP0POMM_DAO(pes.getConnection());	
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			
			
			// 每天訂單
			// JSONArray poAry = poDao.queryWeekPOList(compCode, wkBuyer, wkPoDate, wkPoDate);
			
			// 20231202: 
			JSONArray poAry = poDao.queryDailyPOList(compCode, wkBuyer, wkPoDate, wkPoSeq);
			
			
			
			// 追加訂單
			JSONArray addItemAry = bidDao.queryAppendItemList(compCode, wkBuyer, wkPoDate);
			
			
			
			// 訂單總金額											
			rtnJo.put("success", true);								
			rtnJo.put("DAILY_PO", poAry);
			rtnJo.put("ADD_PO", addItemAry);
			
			String rtn = MsgUtils.toString(true, "", rtnJo);		
			
			//System.out.println("660 daily po: " + rtn);
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

}
