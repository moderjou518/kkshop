package com.hms.web.hbd1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
 * HBD1W020 
 * 
 * @author Window10
 * 
 */
public class HBD1W690_ACTION implements HMS_WEBACTION {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");

	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("HBD1W690 doAction:#");
		
		/* 第一次進 來載入廠商資料 */
		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);		
		
	}

	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		PrintWriter out = response.getWriter();		
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		
		//logger.info("jc:#");		
		String info = UTIL_String.nvl(request.getParameter("info"), "{}");
		JSONObject itemJo = JSONObject.fromObject(info);
		
		String pInfo = UTIL_String.nvl(request.getParameter("pInfo"), "{}");
		JSONObject priceJo = JSONObject.fromObject(pInfo);
		
		//System.out.println("CREATE PRICE!!!!!!" + itemJo.toString());
		
		
		String fromYearStr = itemJo.getString("qryYear");
		String addItem = itemJo.getString("qryItem");
		
		int toYear = Integer.parseInt(fromYearStr) + 1;
		String toYearStr = String.valueOf(toYear);
		
        LocalDate localDate1 = LocalDate.parse(fromYearStr + "-01-01");
        LocalDate localDate2 = LocalDate.parse(toYearStr + "-01-01");        
        
        // 組合出當年度的日期資料, 用20230101~20240101產生出 20230101~20231231的日期區間資料
        List<LocalDate> dayPeriodList = getDatesBetweenUsingJava8(localDate1, localDate2);

        PrepareExecuteSql pes = new PrepareExecuteSql();
        
		try {
			
			pes.openConnection("HBD_DMZ");
			HMP0ITEM_DAO itemDao = new HMP0ITEM_DAO(pes.getConnection());
			
			String fromDate = fromYearStr + "0101";
			String toDate = toYearStr + "1231";
			
			// 先清空舊資料(yyyy0101~yyyy1231)
			itemDao.clearDatePrice(compCode, addItem, fromDate, toDate);
			
			// 新增新價格
			for(int i=0; i < dayPeriodList.size(); i++) {            
	            LocalDate day1 = dayPeriodList.get(i);
	            String addDate = day1.toString().replace("-", "");
	            boolean addResult = itemDao.createDatePrice(compCode, addItem, addDate, priceJo.getString("txtSetUnitPrice"));
	            //System.out.println(sql1);
	            System.out.println(addDate + ":" + addResult);
	        }
			
			String msg = MsgUtils.toString(true, "", null);
			out.print(msg);
			
			
		}catch (Exception ex){
				ex.printStackTrace();
		}finally{
			pes.closeConnection();
		}
		
	}
	
	public void submitCreatePrice(String compCode, HttpServletRequest request, PrintWriter out){
		
		String info = UTIL_String.nvl(request.getParameter("info"), "{}");
		JSONObject parmJo = JSONObject.fromObject(info);		
		
		System.out.println("690_SYNC_PRICE: " + parmJo.toString());
		
		try {
			PrepareExecuteSql pes = new PrepareExecuteSql();			
			pes.openConnection("HBD_DMZ");			
			pes.getConnection().setAutoCommit(false);
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			
			JSONArray priceAry = bidDao.queryItemPriceByYear(compCode, parmJo.getString("qryItem"), parmJo.getString("qryYear"));
			
			bidDao.syncItemPriceWithBid(compCode, priceAry);			
			String msg = MsgUtils.toString(true, "", priceAry);
			
			System.out.println("price msg: " + msg);
			pes.getConnection().commit();
			out.print(msg);
			
			
		}catch (Exception ex){
				ex.printStackTrace();
		}finally{
				
		}
		
	}
	
    public List<LocalDate> getDatesBetweenUsingJava8(LocalDate startDate, LocalDate endDate) {        
        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return IntStream.iterate(0, i -> i + 1).limit(numOfDaysBetween)
                .mapToObj(i -> startDate.plusDays(i)).collect(Collectors.toList());
    }



	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("jr:#");
		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);				
		
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);
		
		//System.out.println("qryInfo: " + qryInfo);
		
		JSONObject rtnJo = new JSONObject();
		
		if ("PAGE_LOAD".equals(method)){
			this.PAGE_LOAD(compCode, "", out);
		}
		
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
		String updInfo = UTIL_String.nvl(request.getParameter("updInfo"), "{}");
		
		//System.out.println("addInfo: " + addInfo);
		//System.out.println("updInfo: " + updInfo);		
		
		if ("SAVE_DATA_LIST".equals(method)){
			//System.out.println("690 SAVE_DATA_LIST");
			this.SAVE_DATA_LIST(compCode, updInfo, out);
		}
		
		
		
			
		
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		System.out.println("jd111111111111111:#");
		logger.info("jd111111111111111:#");		
		
		PrintWriter out = response.getWriter();		
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		
		String qryInfo = UTIL_String.nvl(request.getParameter("qryInfo"), "{}");
		JSONObject qryJo = JSONObject.fromObject(qryInfo);
		
		//System.out.println("690: " + qryInfo);
		String qryYear = qryJo.getString("qryYear");
		String qryItem = qryJo.getString("qryItem");
		String qryMonth = qryJo.getString("qryMonth");		
		String queryMonth = qryYear + qryMonth;
		String fromDate = qryYear + "0101";
		String toDate = qryYear + "1231";
		
		
		System.out.println("qryYear: " + qryYear);
		System.out.println("qryItem: " + qryItem);
		System.out.println("qryMonth: " + qryMonth);
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {			
			pes.openConnection("HBD_DMZ");
			pes.getConnection().setAutoCommit(false);						
			HMP0ITEM_DAO itemDao = new HMP0ITEM_DAO(pes.getConnection());			
			
			
			
			// 先清空舊資料(yyyy0101~yyyy1231)
			itemDao.clearDatePrice(compCode, qryItem, fromDate, toDate);
					
			
			
			pes.getConnection().commit();			
			
           
			JSONObject jo = new JSONObject();
			
			String rtn = MsgUtils.toString(true, "", jo);
			out.print(rtn);
			
		}catch(Exception e){
			logger.error("Action Method: SAVE_DATA_LIST");
			logger.error(e.toString());
			e.printStackTrace();			
			String rtn = MsgUtils.toString(false, e.toString(), null);
			out.print(rtn);
		}finally{
			pes.closeConnection();
		}
		
		
		 

	}
	
	private void PAGE_LOAD(String compCode, String qryKind, PrintWriter out) throws FileNotFoundException, Exception {
		
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();			
		try {
			pes.openConnection("HBD_DMZ");
			HMP0ITEM_DAO itemDao = new HMP0ITEM_DAO(pes.getConnection());
			HMS0MISC_DAO miscDao = new HMS0MISC_DAO(pes.getConnection());
			JSONArray itemAry = itemDao.queryItemList(compCode, "*");
			JSONArray yearAry =  miscDao.queryValidYear(compCode, "DATA_YEAR");
			rtnJo.put("ITEM_LIST", itemAry);			
			rtnJo.put("YEAR_LIST", yearAry);
			String msg = MsgUtils.toString(true, "", rtnJo);			
			out.print(msg);	
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: PAGE_LOAD");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}
	
	}



	/**
	 * 
	 * @param compCode
	 * @param request
	 * @param out
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	private void QUERY_DATA_LIST(String compCode, HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {	
	
		
		String qryInfo = UTIL_String.nvl(request.getParameter("qryInfo"), "{}");
		JSONObject qryJo = JSONObject.fromObject(qryInfo);
		
		//System.out.println("690: " + qryInfo);
		String qryYear = qryJo.getString("qryYear");
		String qryItem = qryJo.getString("qryItem");
		String qryMonth = qryJo.getString("qryMonth");		
		String queryMonth = qryYear + qryMonth;
		
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			System.out.println("690_QUERY_DATA_LIST: " + qryJo.toString());
			
			pes.openConnection("HBD_DMZ");
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			HMP0ITEM_DAO itmDao = new HMP0ITEM_DAO(pes.getConnection());
			//RowDataList itemList = bidDao.queryMonthBidList(compCode, qryJo);	
			JSONArray ipList = itmDao.queryItemPriceList(compCode, qryItem, queryMonth);			
			rtnJo.put("ITEM_PRICE_LIST", ipList);	
			String msg = MsgUtils.toString(true, "", rtnJo);
			//System.out.println("itemPrice: " + msg);
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
	
	
	private void SAVE_DATA_LIST(String compCode, String updInfo, PrintWriter out) throws FileNotFoundException, Exception {	
		
		String msg = "";
		boolean success = false;
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {			
			pes.openConnection("HBD_DMZ");
			pes.getConnection().setAutoCommit(false);						
			HMP0ITEM_DAO itemDao = new HMP0ITEM_DAO(pes.getConnection());			
			
			// update
			int updRow = 0;
			int updCnt = 0;
			JSONArray updAry = JSONArray.fromObject(updInfo);
			updRow = updAry.size();			
			for(int i=0; i<updAry.size(); i++) {             
				//System.out.println("upd index: " + i);				
				JSONObject jo = updAry.getJSONObject(i);				
				jo.put("compCode", compCode);				   
				System.out.println(jo.toString());
				updCnt += itemDao.updateItemPrice(jo);
			}			
			
			pes.getConnection().commit();
			msg = "";
			success = true;
			
			JSONObject jo = new JSONObject();            
			jo.put("count", updCnt);			  
			String rtn = MsgUtils.toString(success, msg, jo);
			out.print(rtn);
			
		}catch(Exception e){
			logger.error("Action Method: SAVE_DATA_LIST");
			logger.error(e.toString());
			e.printStackTrace();			
			String rtn = MsgUtils.toString(false, e.toString(), null);
			out.print(rtn);
		}finally{
			pes.closeConnection();
		}
	}
	
	/**
	 * 設定物料價格
	 * @param compCode
	 * @param updInfo
	 * @param out
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	private void SAVE_SUB_DATA_LIST(String compCode, HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {	
		
		String msg = "";
		boolean success = false;
		
		String mastInfo = UTIL_String.nvl(request.getParameter("mastInfo"), "");
		JSONObject mastJo  = JSONObject.fromObject(mastInfo);
		mastJo.put("compCode", compCode);
		
		String updInfo = UTIL_String.nvl(request.getParameter("updInfo"), "{}");
		JSONArray bidItemAry = JSONArray.fromObject(updInfo);
		
				
		System.out.println("mast: " + mastInfo);
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		
		try {
			
			pes.openConnection("HBD_DMZ");
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			//pes.getConnection().setAutoCommit(false);			
			
			// update
			int updRow = bidItemAry.size();
			int updCnt = 0;			
			for(int i=0; i < bidItemAry.size(); i++) {                    
				JSONObject itemJo = bidItemAry.getJSONObject(i);	                                
				int c = bidDao.updateBiddItemData(itemJo);
				System.out.println("i: " + itemJo.toString());				
				if (c == 1){						
					updCnt += c;
					// 1．還沒出貨的訂單價格也要改成1致					
					c = bidDao.updateOtherFuturePOPrice(mastJo, itemJo);
					System.out.println("updateOtherPO: " + c);
					// 2．這個日期之後的預訂單的價格也要設成一樣，以免管理者還要逐1設定
					c = bidDao.updateOtherFutureBiddPrice(mastJo, itemJo);
					System.out.println("updateOtherBidd: " + c);
				}else{
					// 用 msg 保留 exception 當下的 code號
					msg = "Upd[" + itemJo.getString("txtBiddItem") + "]";
					break;
				}
			}		   
			System.out.println("upd: " + updRow + "/" + updCnt);
			
			if (updRow == updCnt){
				System.out.println("commit()");
				pes.getConnection().commit();
				msg = "";
				success = true;
			}else{
				System.out.println("rollBack()");
				pes.getConnection().rollback();
				msg += " ! Data save error!";
				success = false;
			}
			
			JSONObject jo = new JSONObject();            
			jo.put("count", (updCnt));			  
			String rtn = MsgUtils.toString(success, msg, jo);
			out.print(rtn);
			
		}catch(Exception e){
			logger.error("Action Method: SAVE_SUB_DATA_LIST");
			logger.error(e.toString());
			e.printStackTrace();			
			String rtn = MsgUtils.toString(false, e.toString(), null);
			out.print(rtn);
		}finally{
			pes.closeConnection();
		}
	}
	
}
