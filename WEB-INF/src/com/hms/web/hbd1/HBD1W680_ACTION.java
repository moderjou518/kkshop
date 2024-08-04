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
public class HBD1W680_ACTION implements HMS_WEBACTION {
		
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");

	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("HBD1W620 doAction:#");
		
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
		
		//System.out.println("qryInfo: " + qryInfo);		
		
		// 查詢這個月的周預訂單
		if ("LOAD_WEEK".equals(method)){					
			this.LOAD_WEEK(compCode, request, out);
		}
		
		// 查詢那周的周結資料
		if ("LOAD_OFFS".equals(method)){			
			this.LOAD_OFFS(compCode, request, out);			
		}
		
		//
		
		if ("LOAD_PODD_BY_WEEK_BUYER".equals(method)){						
			this.LOAD_PODD_BY_WEEK_BUYER(compCode, request, out);			
		}
		
		if ("LOAD_ONE_OFFS".equals(method)){
			
			String qryUUID = UTIL_String.nvl(request.getParameter("qryBidUUID"), "");
			String qryBuyer = UTIL_String.nvl(request.getParameter("qryBuyer"), "");
			this.LOAD_ONE_OFFS(compCode, qryBuyer, qryUUID, out);
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
		// 送出匯款資料
		if ("SAVE_BIDM_MONEY".equals(method)){			
			this.SAVE_BIDM_MONEY(compCode, request, out);			
		}		
			
		
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
				
		//// logger.info("jd#");		
		
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
			HMP0BID_DAO bidDAO = new HMP0BID_DAO(pes.getConnection());
			
			// delete            
            int delCnt = 0;                                           
            delCnt = bidDAO.deleteBIDM(compCode, delID);
            if (delCnt == 1){
            	delCnt = bidDAO.deleteBIDDByBIDM(compCode, delID);            	
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
	
	private void LOAD_WEEK(String compCode, HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {
		
		
		//System.out.println("LOAD_WEEK");
		JSONObject rtnJo = new JSONObject();
		
		String qryInfo = UTIL_String.nvl(request.getParameter("qryInfo"), "{}");
		JSONObject qryJo = JSONObject.fromObject(qryInfo);		
		
		PrepareExecuteSql pes = new PrepareExecuteSql();			
		try {
			pes.openConnection("HBD_DMZ");
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			RowDataList list = bidDao.queryMonthBidList(compCode, qryJo.getString("qryMonth"));
			
			
			JSONObject weekTotal = new JSONObject();
			//weekTotal.put("TotalOrdQty", weekOrdTotQty);
			//weekTotal.put("TotalRcvdQty", weekRcvdTotQty);
			//weekTotal.put("TotalAmt", weekTotAmt);
			
			rtnJo.put("WEEK_TOTAL", weekTotal);
			
			rtnJo.put("WEEK_LIST", list.toJSONArray());
			
			
			String msg = MsgUtils.toString(true, "", rtnJo);
			//System.out.println("msg: " + msg);
			out.print(msg);	
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: LOAD_WEEK");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}
		
	}
	
	private void LOAD_OFFS(String compCode, HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {
		
		//System.out.println("LOAD_OFFS");
		
		JSONObject rtnJo = new JSONObject();
		String bidUUID = UTIL_String.nvl(request.getParameter("qryUUID"), "{}");
		
		PrepareExecuteSql pes = new PrepareExecuteSql();			
		try {
			pes.openConnection("HBD_DMZ");
			HMI0OFFS_DAO offsDao = new HMI0OFFS_DAO(pes.getConnection());
			HMP0POMM_DAO poDao = new HMP0POMM_DAO(pes.getConnection());
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			
			
			
			JSONArray ary = offsDao.queryOffsList(compCode, bidUUID);
			
			JSONObject bidJo = bidDao.getBidmData(compCode, bidUUID);
			
			// 找出貨金額
			for(int i=0; i < ary.size(); i++){
				JSONObject offsJo = ary.getJSONObject(i);
				JSONObject weekPoJo = poDao.queryWeekPOAmt(compCode, offsJo.getString("offsBuyer"), bidJo.getString("bidOrdSdate"), bidJo.getString("bidOrdEdate"));
				offsJo.put("poddRcvdAmtSum", weekPoJo.getString("poddRcvdAmtSum"));
				
			}
			
			
			rtnJo.put("OFFS_LIST", ary);			
			String msg = MsgUtils.toString(true, "", rtnJo);
			//System.out.println("msg: " + msg);
			out.print(msg);	
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: LOAD_OFFS");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}
		
	}
	
	private void LOAD_PODD_BY_WEEK_BUYER(String compCode, HttpServletRequest request, PrintWriter out) throws Exception{				
		
		//System.out.println(String.format("c/b/u: %s/%s/%s", compCode, buyer, qryUUID));
		
		String qryUUID = UTIL_String.nvl(request.getParameter("qryBidUUID"), "");
		String buyer = UTIL_String.nvl(request.getParameter("qryBuyer"), "");
		
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		
		try {
		
			pes.openConnection("HBD_DMZ");
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			HMP0POMM_DAO poDao = new HMP0POMM_DAO(pes.getConnection());			
			
			// 用 uuid 找出當周起迄日
			JSONObject bidm = bidDao.getBidmData(compCode, qryUUID);
			String sDate = bidm.getString("bidOrdSdate");
			String eDate = bidm.getString("bidOrdEdate");					
			
			// 當周每天訂單
			JSONArray weekDayPOItemAry = poDao.queryWeekPOList(compCode, buyer, sDate, eDate);
			
			// 周統計
			int weekOrdTotQty = 0;
			int weekRcvdTotQty = 0;
			int weekTotAmt = 0;
			
			// 日統計
			String dayType = "0";
			int dayOrdTotQty = 0;
			int dayRcvdTotQty = 0;
			int dayTotAmt = 0;
			
			JSONArray newWeekDayPoItemAry = new JSONArray();
			
			
			for(int i=0; i < weekDayPOItemAry.size(); i++){
				JSONObject itemJo = weekDayPOItemAry.getJSONObject(i);
				
				
				// total cal
				weekOrdTotQty = weekOrdTotQty + itemJo.getInt("poddOrdQty");
				weekRcvdTotQty = weekRcvdTotQty + itemJo.getInt("poddRcvdQty");
				weekTotAmt = weekTotAmt + itemJo.getInt("poddAmt");				
				// day type
				if (i == 0){
					dayType = "0";
					itemJo.put("dayType", dayType);		
					newWeekDayPoItemAry.add(itemJo);
					
					dayOrdTotQty = itemJo.getInt("poddOrdQty");
					dayRcvdTotQty = itemJo.getInt("poddRcvdQty");
					dayTotAmt = itemJo.getInt("poddAmt");
					
				}else{
					
					JSONObject preItem = weekDayPOItemAry.getJSONObject(i-1);
					
					// 最後1天的資料 
					if (i == weekDayPOItemAry.size()-1){
						
						// 最後1天是同1天資料
						if (preItem.getString("pommDate").equals(itemJo.getString("pommDate"))){
							
							// 同1天的資料
							itemJo.put("poDate", "");
							itemJo.put("weekDay", "");							
							itemJo.put("dayType", dayType);
							newWeekDayPoItemAry.add(itemJo);
							
							dayOrdTotQty = dayOrdTotQty + itemJo.getInt("poddOrdQty");
							dayRcvdTotQty = dayRcvdTotQty + itemJo.getInt("poddRcvdQty");
							dayTotAmt = dayTotAmt + itemJo.getInt("poddAmt");							
							
							// 最後1天的統計
							JSONObject dayTotJo = new JSONObject();						
							dayTotJo.put("dayType", "9");
							//dayTotJo.put("poDate", preItem.getString("poDate1"));														
							dayTotJo.put("itemAbbr", "");						
							dayTotJo.put("poddOrdQty", "");
							dayTotJo.put("poddRcvdQty", "小計");
							dayTotJo.put("poddAmt", dayTotAmt);
							newWeekDayPoItemAry.add(dayTotJo);
							
						}else{
							dayOrdTotQty = itemJo.getInt("poddOrdQty");
							dayRcvdTotQty = itemJo.getInt("poddRcvdQty");
							dayTotAmt = itemJo.getInt("poddAmt");
							if ("0".equals(dayType)){							
								dayType ="1";
							}else{
								dayType ="0";
							}
							itemJo.put("dayType", dayType);
							newWeekDayPoItemAry.add(itemJo);
							
							// 最後1筆是不同天資料
							JSONObject dayTotJo = new JSONObject();						
							dayTotJo.put("dayType", "9");
							//dayTotJo.put("poDate", preItem.getString("poDate1"));							
							dayTotJo.put("itemAbbr", "");						
							dayTotJo.put("poddOrdQty", "");
							dayTotJo.put("poddRcvdQty", "小計");
							dayTotJo.put("poddAmt", dayTotAmt);
							newWeekDayPoItemAry.add(dayTotJo);
						}
						
					}else{
						
						if (preItem.getString("pommDate").equals(itemJo.getString("pommDate"))){
							// 同1天的資料
							itemJo.put("poDate", "");
							itemJo.put("weekDay", "");
							itemJo.put("dayType", dayType);
							newWeekDayPoItemAry.add(itemJo);
							
							dayOrdTotQty = dayOrdTotQty + itemJo.getInt("poddOrdQty");
							dayRcvdTotQty = dayRcvdTotQty + itemJo.getInt("poddRcvdQty");
							dayTotAmt = dayTotAmt + itemJo.getInt("poddAmt");
							
							
							
						}else{
							// 不同天資料, 
							JSONObject dayTotJo = new JSONObject();						
							dayTotJo.put("dayType", "9");
							//dayTotJo.put("poDate", preItem.getString("poDate1"));
							//dayTotJo.put("weekDay", preItem.getString("weekDay1"));							
							dayTotJo.put("itemAbbr", "");						
							dayTotJo.put("poddOrdQty", "");
							dayTotJo.put("poddRcvdQty", "小計");
							dayTotJo.put("poddAmt", dayTotAmt);
							newWeekDayPoItemAry.add(dayTotJo);					
							
							
							// 新1天的資料
							dayOrdTotQty = itemJo.getInt("poddOrdQty");
							dayRcvdTotQty = itemJo.getInt("poddRcvdQty");
							dayTotAmt = itemJo.getInt("poddAmt");
							if ("0".equals(dayType)){							
								dayType ="1";
							}else{
								dayType ="0";
							}
							itemJo.put("dayType", dayType);
							newWeekDayPoItemAry.add(itemJo);
						}
						
						
					}
					
					
					
				}				
				//System.out.println(i + ":::::" + itemJo.toString());				
			}
			
			JSONObject weekTotal = new JSONObject();
			weekTotal.put("TotalOrdQty", weekOrdTotQty);
			weekTotal.put("TotalRcvdQty", weekRcvdTotQty);
			weekTotal.put("TotalAmt", weekTotAmt);
			
			// 訂單總金額											
			rtnJo.put("success", true);				
			rtnJo.put("BIDM", bidm);		
			rtnJo.put("WEEK_PO", newWeekDayPoItemAry);
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
	private void LOAD_ONE_OFFS(String compCode, String buyer, String qryUUID, PrintWriter out) throws Exception{				
		
		//System.out.println(String.format("c/b/u: %s/%s/%s", compCode, buyer, qryUUID));
		
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		
		try {
		
			pes.openConnection("HBD_DMZ");
			HMI0OFFS_DAO offDao = new HMI0OFFS_DAO(pes.getConnection());			
			
			// 找出當周起迄日
			JSONObject offs = offDao.getOneOffs(compCode, buyer, qryUUID);
										

			String rtn = MsgUtils.toString(true, "", offs);		
			//System.out.println(" LOAD_ONE_OFFS rtn: " + rtn);
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
	
	
	private void SAVE_BIDM_MONEY(String compCode, HttpServletRequest request, PrintWriter out) throws Exception{
	
		String info = UTIL_String.nvl(request.getParameter("info"), "{}");
		JSONObject jo = JSONObject.fromObject(info);
		//System.out.println("money: " + info);
		
		String payAmt = "0";
		
		if (!"".equals(jo.getString("txtAmount").trim())){
			payAmt = jo.getString("txtAmount").trim();
		}
		
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");
			HMI0OFFS_DAO offDao = new HMI0OFFS_DAO(pes.getConnection());					
			
			JSONObject parmJo = new JSONObject();
			parmJo.put("OFFS_COMP_CODE", compCode);
			parmJo.put("OFFS_BUYER", jo.getString("txtAccount"));
			parmJo.put("OFFS_BIDM_UUID", jo.getString("txtBidmUUID"));   	
			parmJo.put("OFFS_PAY_DATE", jo.getString("txtDate"));
			parmJo.put("OFFS_PAY_AMT", payAmt);
			parmJo.put("OFFS_PAY_NOTE", "");    	
			parmJo.put("OFFS_DOC_MARK", "Y");			
			offDao.resetDocMark(compCode, jo.getString("txtAccount"), jo.getString("txtBidmUUID"));
			
			
			//System.out.println(" money: " + parmJo.toString());
			//System.out.println(" save money: " + rtnJo.toString());
			
			boolean insOk = false;
			insOk = offDao.insertOffs(parmJo);	    												
			rtnJo.put("success", insOk);
			//System.out.println(" insOK: " + insOk);
			
			out.print(MsgUtils.toString(insOk, "", rtnJo));				
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
