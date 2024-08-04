package com.hms.web.hbd1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.synth.SynthSeparatorUI;

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
 * HBD1W661
 * 
 * @author Window10
 * 
 */
public class HBD1W661_ACTION implements HMS_WEBACTION {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");

	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("HBD1W661 doAction:#");
		
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
		
		//logger.info("action jr:#");

		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);				
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);		
		
		//System.out.println("#### 661 query data list");
		
		if ("QUERY_DATA_LIST".equals(method)){			
			this.QUERY_DATA_LIST(compCode, request, out);			
		}
		
		
		if ("LOAD_BUYER_DAILY_PO".equals(method)){
			this.LOAD_BUYER_DAILY_PO(compCode, request, out);
		}
		
	}

	

	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		logger.info("doUpdate:#");
		
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

	@SuppressWarnings("unchecked")
	private void QUERY_DATA_LIST(String compCode, HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {
		
		String qryInfo = UTIL_String.nvl(request.getParameter("qryInfo"), "{}");
		JSONObject qryJo = JSONObject.fromObject(qryInfo);
		String qryMonth = qryJo.getString("qryMonth");
		JSONArray dayList = UTIL_String.getMonthDays(qryMonth);			
		
		// 當月的所有天(monthDay, weekDay)
		dayList.forEach(obj->{
			JSONObject dayJo = (JSONObject) obj;
			String weekDay = UTIL_String.getWeekOfDate(dayJo.getString("monthDay"));
			dayJo.put("weekDay", weekDay);
		});
		
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");
			HMP0POMM_DAO poDao = new HMP0POMM_DAO(pes.getConnection());
			
			// 當月訂單、客戶
			//System.out.println("Comp/QryMonth: " + compCode + "/" + qryMonth);
			JSONArray monthPoList = poDao.queryMonthPO(compCode, qryMonth);
			JSONArray monthCustList = poDao.queryMonthCust(compCode, qryMonth);			
			Double monthTotalAmt = new Double("0");
			
			// 每1個客人的當月統計
			for(int i=0; i < monthCustList.size(); i++){
				JSONObject custTr = (JSONObject) monthCustList.get(i);
				JSONArray custMonthPo = new JSONArray();
				dayList.forEach(dayObj->{
					// monthDay, weekDay, pommBuyer, dayAmt
					JSONObject dayJo = (JSONObject) dayObj;
					String currDay = dayJo.getString("monthDay");
					dayJo.put("pommBuyer", custTr.getString("pommBuyer"));					
					dayJo.put("dayAmt", "0");
					
					// 從月訂單中找到客戶當天的金額
					monthPoList.forEach(poObj->{
						JSONObject poJo = (JSONObject) poObj;
						String poBuyer = poJo.getString("pommBuyer");
						String poDate = poJo.getString("pommDate");
						
						if (poBuyer.equals(custTr.getString("pommBuyer"))){
							if (poDate.equals(currDay)){
								//custJo.put(currDayStr, poJo.getString("pommTotalAmt"));
								dayJo.put("dayAmt", poJo.getString("pommTotalAmt"));
							}
						}
					});
					custMonthPo.add(dayJo);					
				});	// 1~31 金額陣列
				custTr.put("monthAmts", custMonthPo);
				//BigDecimal monthTotalAmt = new BigDecimal("0");		
				
				// 該客戶當月的加總金額
				Double custMonthTotalAmt = new Double("0"); 
				for(int c=0; c < custMonthPo.size(); c++){
					JSONObject dayJo = custMonthPo.getJSONObject(c);
					custMonthTotalAmt += Double.parseDouble(dayJo.getString("dayAmt"));					
				}
				custTr.put("custMonthTotalAmt", custMonthTotalAmt);
				monthTotalAmt += custMonthTotalAmt;
				
			}
			monthCustList.forEach(custObj->{
				JSONObject custTr = (JSONObject) custObj;			
			});
			
			//
			
			
			
			// 頁尾每一天的金額小計
			JSONArray dayTotalAry = new JSONArray();			
			for(int i=0; i < dayList.size(); i++){
				JSONObject dayJo = dayList.getJSONObject(i);				
				String dayStr = dayJo.getString("shortMonthDay");				
	            
				
				Double dayTotalAmt = new Double("0");				
				for(int c=0; c < monthCustList.size(); c++){
					JSONObject custTr = monthCustList.getJSONObject(c);
					JSONArray custMonthAmtsAry = custTr.getJSONArray("monthAmts");					
					for(int j=0; j < custMonthAmtsAry.size(); j++){
						JSONObject custDayJo = custMonthAmtsAry.getJSONObject(j);
						String custDay = custDayJo.getString("shortMonthDay");						
						if (custDay.equals(dayStr)){
							System.out.println(custDay + "/" + dayStr + ":" + custDayJo.toString());
							//System.out.println("i: " + custMonthAmtsAry.getString(i));							
							dayTotalAmt += Double.parseDouble(custDayJo.getString("dayAmt"));		
						}
					}
										
				}							
				JSONObject dayAmt = new JSONObject();
				dayAmt.put("dayTotalAmt", dayTotalAmt);
				dayTotalAry.add(dayAmt);								
			}
			rtnJo.put("monthTotalAmt", monthTotalAmt);
					
			rtnJo.put("dayTotalList", dayTotalAry);			
			rtnJo.put("monthCustList", monthCustList);
			rtnJo.put("monthDayList", dayList);
			
			
			System.out.println(rtnJo.toString());
			out.print(MsgUtils.toString(true, "", rtnJo));	
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: 661 QUERY_DATA_LIST");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}
		
	}
	
	
	
	private void LOAD_BUYER_DAILY_PO(String compCode, HttpServletRequest request, PrintWriter out) throws Exception{				
		
		String wkBuyer = UTIL_String.nvl(request.getParameter("parmBuyer"), "");
		String wkPoDate = UTIL_String.nvl(request.getParameter("parmPoDate"), "");
		
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		
		try {
		
			pes.openConnection("HBD_DMZ");
			HMP0POMM_DAO poDao = new HMP0POMM_DAO(pes.getConnection());	
			
			// 每天訂單
			JSONArray ary = poDao.queryWeekPOList(compCode, wkBuyer, wkPoDate, wkPoDate);
			
			
			
			// 訂單總金額											
			rtnJo.put("success", true);								
			rtnJo.put("DAILY_PO", ary);
			
			String rtn = MsgUtils.toString(true, "", rtnJo);		
			
			System.out.println("661 daily po: " + rtn);
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
