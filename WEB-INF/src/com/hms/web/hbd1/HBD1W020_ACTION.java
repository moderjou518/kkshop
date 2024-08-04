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
 * HBD1W020 
 * 
 * @author Window10
 * 
 */
public class HBD1W020_ACTION implements HMS_WEBACTION {

	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		
		logger.info("HBD1W020 doAction:#");
		
		/* 固定寫法 */
		
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		/* 固定寫法 */		
		String compName = "";	

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
				compName = jo.getString("miscDesc");
			}else{
				rtnJo.put("success", false);
				compCode = "";
				compName = compCode + " not found!";				
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
		PrintWriter out = response.getWriter();
		out.print(MsgUtils.toString(true, "", null));
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
		// logger.info("jr method: " + method);
		
		// 載入客戶當月的預訂單列表
		if ("LOAD_BID_LIST".equals(method)){
			this.LOAD_BID_LIST(compCode, ui.getLoginID(request), out);
		}
		
		// 載入客戶該周每1天預訂單的明細
		if ("LOAD_PODD_BY_WEEK_BUYER".equals(method)){
			String buyer = ui.getLoginID(request);
			String qryUUID = UTIL_String.nvl(request.getParameter("qryBidUUID"), "#");		
			this.LOAD_PODD_BY_WEEK_BUYER(compCode, buyer, qryUUID, out);
		}
		
		if ("LOAD_ONE_OFFS".equals(method)){
			String buyer = ui.getLoginID(request);
			String qryUUID = UTIL_String.nvl(request.getParameter("qryBidUUID"), "#");			
			this.LOAD_ONE_OFFS(compCode, buyer, qryUUID, out);
		}

	}

	

	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
				
		//logger.info("doUpdate:#");
		
		/* 固定寫法 */
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		String buyer = ui.getLoginID(request);
		/* 固定寫法 */
		String method = UTIL_String.nvl(request.getParameter("methodName"), "#");
		
		// 送出匯款資料
		if ("SAVE_BIDM_MONEY".equals(method)){			
			this.SAVE_BIDM_MONEY(compCode, buyer, request, out);			
		}		

	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		// logger.info("jd#");		
	}
	
	/**
	 * 
	 * @param compCode
	 * @param loginID
	 * @param out
	 * @throws Exception
	 */
	private void LOAD_BID_LIST(String compCode, String loginID, PrintWriter out) throws Exception{
	
		String todayStr = UTIL_String.getNowString("yyyyMMdd");
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			HMP0POMM_DAO poDao = new HMP0POMM_DAO(pes.getConnection());			
			HMI0OFFS_DAO offDao = new HMI0OFFS_DAO(pes.getConnection());
			
			RowDataList rdl = bidDao.queryRecentBidList(compCode);	
			
			JSONArray ary = null;
			if (rdl.size() > 0){
				
				ary = rdl.toJSONArray();				
				for(int i=0; i < ary.size(); i++){
					JSONObject jo = ary.getJSONObject(i);
					//System.out.println(String.format("ordDate:%s-%s", jo.getString("bidOrdSdate"), jo.getString("bidOrdEdate")));
					// 
					JSONObject poJo = poDao.queryWeekPOAmt(compCode, loginID, jo.getString("bidOrdSdate"), jo.getString("bidOrdEdate"));
					jo.put("weekOrdAmt", poJo.getString("poddRcvdAmtSum"));
					
					
					// 20211126: 找出已匯出的金額
					JSONObject offs = offDao.getOneOffs(compCode, loginID, jo.getString("bidUuid"));
					jo.put("offsPayAmt", offs.getString("offsPayAmt"));
					jo.put("offsPayDate", offs.getString("offsPayDate"));
					
					
					
					// 是否已滿一周(迄日小於今日)
					int pastDays = (int) UTIL_String.getDateDiff(jo.getString("bidOrdEdate"), todayStr);
					if (pastDays >0){
						jo.put("PAST_WEEK", "Y");
					}else{
						jo.put("PAST_WEEK", "N");
					}
					//System.out.println(jo.getString("bidOrdEdate") + "-" + todayStr + "-" + jo.getString("PAST_WEEK"));
					
					
				}
				
			}
			
			rtnJo.put("success", true);
			rtnJo.put("BIDLIST", ary.toString());				
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
	
	private void LOAD_PODD_BY_WEEK_BUYER(String compCode, String buyer, String qryUUID, PrintWriter out) throws Exception{				
		
		// System.out.println(String.format("c/b/u: %s/%s/%s", compCode, buyer, qryUUID));
		
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		
		try {
		
			pes.openConnection("HBD_DMZ");
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			HMP0POMM_DAO poDao = new HMP0POMM_DAO(pes.getConnection());
			HAC0BASC_DAO hac0Dao = new HAC0BASC_DAO(pes.getConnection());
			
			// 用 uuid 找出當周起迄日
			JSONObject bidm = bidDao.getBidmData(compCode, qryUUID);
			String sDate = bidm.getString("bidOrdSdate");
			String eDate = bidm.getString("bidOrdEdate");
			
			//System.out.println(String.format("s:%s, e:%s", sDate, eDate));
			JSONObject buyerJo = hac0Dao.getProfile(compCode, buyer);
			
			
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
				
				//System.out.println(i + ":" + itemJo.toString());
				
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
							dayTotJo.put("poddOrdQty", "小");
							dayTotJo.put("poddRcvdQty", "計");
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
						
						// 不是最後1天(一般資料)
						
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
			rtnJo.put("WEEK_TOTAL", weekTotal);
			
			//rtnJo.put("WEEK_PO", weekDayPOItemAry);
			//System.out.println("weekPO: " + newWeekDayPoItemAry);
			
			// 報表時間
			rtnJo.put("REPORT_TIME", UTIL_String.getNowString("yyyy/MM/dd HH:mm"));
			rtnJo.put("PO_DATE", sDate + "-" + eDate);
			rtnJo.put("PO_BUYER", buyerJo.getString("hac0BascName"));
			
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
		
		// System.out.println(String.format("c/b/u: %s/%s/%s", compCode, buyer, qryUUID));
		
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		
		try {
		
			pes.openConnection("HBD_DMZ");
			HMI0OFFS_DAO offDao = new HMI0OFFS_DAO(pes.getConnection());			
			
			// 找出當周起迄日
			JSONObject offs = offDao.getOneOffs(compCode, buyer, qryUUID);
										

			String rtn = MsgUtils.toString(true, "", offs);		
			// System.out.println("rtn: " + rtn);
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
		// System.out.println("money: " + info);
		String bidUUID = jo.getString("txtBidmUUID");
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
			parmJo.put("OFFS_BUYER", buyer);
			parmJo.put("OFFS_BIDM_UUID", bidUUID);   	
			parmJo.put("OFFS_PAY_DATE", jo.getString("txtDate"));
			parmJo.put("OFFS_PAY_AMT", payAmt);
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
