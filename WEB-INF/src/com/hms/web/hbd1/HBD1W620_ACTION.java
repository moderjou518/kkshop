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
 * HBD1W020 
 * 
 * @author Window10
 * 
 */
public class HBD1W620_ACTION implements HMS_WEBACTION {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");

	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("HBD1W620 doAction:#");
		
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
		
		
		/**
		 * 載入指定周的訂單日期
		 */
		if ("LOAD_WEEK_PO_LIST".equals(method)){
			this.LOAD_WEEK_PO_LIST(compCode, request, out);
		}
		
		/**
		 * 查當周的有效商品
		 */
		if ("QUERY_SUB_DATA_LIST".equals(method)){
			String qryBidUuid = UTIL_String.nvl(request.getParameter("qryBidUuid"), "");			
			this.QUERY_SUB_DATA_LIST(compCode, qryBidUuid, out);			
		}
	}

	

	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("doUpdate:#");
		
		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		String loginID = ui.getLoginID(request);		
		
		String method = UTIL_String.nvl(request.getParameter("methodName"), "");	
		String addInfo = UTIL_String.nvl(request.getParameter("addInfo"), "{}");
		String updInfo = UTIL_String.nvl(request.getParameter("updInfo"), "{}");
		
		//System.out.println("addInfo: " + addInfo);
		//System.out.println("updInfo: " + updInfo);		
		
		if ("SAVE_DATA_LIST".equals(method)){
			//System.out.println("620 SAVE_DATA_LIST");
			this.SAVE_DATA_LIST(compCode, loginID, addInfo, updInfo, out);
		}
		
		// 設定物料價格
		if ("SAVE_SUB_DATA_LIST".equals(method)){
			// System.out.println("620 SAVE_SUB_DATA_LIST");
			// this.SAVE_SUB_DATA_LIST(compCode, updInfo, out);
			this.SAVE_SUB_DATA_LIST(compCode, request, out);
			
			
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
			logger.error("Action Method: delete");
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
	
	private void PAGE_LOAD(String compCode, String qryKind, PrintWriter out) throws FileNotFoundException, Exception {
		
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();			
		try {
			pes.openConnection("HBD_DMZ");
			//HMS0MISC_DAO miscDAO = new HMS0MISC_DAO(pes.getConnection());
			//RowDataList typeList = miscDAO.queryDataList(compCode, "ITEM_CLAS1");							
			//rtnJo.put("ITEMCLASS", typeList.toJSONArray().toString());
			
			HMS0MISC_DAO miscDao = new HMS0MISC_DAO(pes.getConnection());			
			JSONArray yearAry =  miscDao.queryValidYear(compCode, "DATA_YEAR");			
			rtnJo.put("YEAR_LIST", yearAry);
			out.print(MsgUtils.toString(true, "", rtnJo));	
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
	 * 依訂單起迄日期載入日期區間的訂單統計(訂購)
	 * @param compCode
	 * @param request
	 * @param out
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	private void LOAD_WEEK_PO_LIST(String compCode,HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {
		
		String sDate = UTIL_String.nvl(request.getParameter("wkBeginDate"), "");
		String eDate = UTIL_String.nvl(request.getParameter("wkEndDate"), "");
		String bidUuid = UTIL_String.nvl(request.getParameter("uuid"), "");
		
		//System.out.println("beginDate: " + sDate);
		//System.out.println("endDate: " + eDate);
		//System.out.println("bidUuid: " + bidUuid);
		

			JSONObject rtnJo = new JSONObject();
			PrepareExecuteSql pes = new PrepareExecuteSql();
			try {
				pes.openConnection("HBD_DMZ");
				HMP0ITEM_DAO itemDao = new HMP0ITEM_DAO(pes.getConnection());
				HMP0POMM_DAO poDao = new HMP0POMM_DAO(pes.getConnection());
				HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
				
				// 表頭物料清單(全部清單)
				JSONArray allItemAry = itemDao.queryItemList(compCode, "*");
							
				// 該周有設定價格的物料
				//JSONArray bidItemAry = bidDao.queryBidValidItemList(compCode, bidUuid);			
				JSONArray bidItemAry = bidDao.queryBidValidItemList(compCode);
				
				// 當天各種商品的訂購總和
				bidItemAry.forEach(obj -> {
				    JSONObject itm = (JSONObject) obj;			    
				    JSONObject sumQty = poDao.queryWeekItemQtySum(compCode, itm.getString("itemCode"), sDate, eDate);			    
				    itm.put("poOrdQtySum", sumQty.getString("poOrdQtySum"));
				    itm.put("poRcvdQtySum", sumQty.getString("poRcvdQtySum"));			    
				});
				
				// 那周有那些客人(有下訂的才會顯示)
				JSONArray custAry = poDao.queryWeekCustomer(compCode, sDate, eDate);
				for(int i=0; i < custAry.size(); i++){
					
					// 客人
					JSONObject cust = custAry.getJSONObject(i);
					//
					// 客人訂購及驗收了那些品項及數量
					JSONArray custPoItemList = poDao.queryWeekQty(compCode, sDate, eDate, cust.getString("pommBuyer"));
					
					// 將採購品項整理成1陣列
					JSONArray custItmAry = new JSONArray();
					for(int idx=0; idx < bidItemAry.size(); idx++){
						// 預設品項採購數量為0										
						String ordQty = "0";
						String rcvQty = "0";
						JSONObject bidItem = bidItemAry.getJSONObject(idx);					
						for(int pi = 0; pi < custPoItemList.size(); pi ++){
							JSONObject poItem = custPoItemList.getJSONObject(pi);
							if (bidItem.getString("itemCode").equals(poItem.getString("poddItem"))){
								ordQty = poItem.getString("poddOrdQty");
								rcvQty = poItem.getString("poddRcvdQty");
								if ("".equals(rcvQty) || rcvQty.length() == 0){
									rcvQty = "0";
								}
							}						
						}
						JSONObject bidItemJo = new JSONObject();
						bidItemJo.put("itemCode", bidItem.getString("itemCode"));
						bidItemJo.put("itemQty", ordQty);
						bidItemJo.put("itemRcvdQty", rcvQty);
						custItmAry.add(bidItemJo);
					}
					
					cust.put("CUST_ITEM_ARY", custItmAry);
					
					
					
					
					
				}
				// 所有的物料
				rtnJo.put("ITEMLIST", bidItemAry.toString());
				// 客戶該周採購統計
				rtnJo.put("POITEMLIST", custAry.toArray());
				rtnJo.put("PO_SDATE", sDate);			
				rtnJo.put("PO_EDATE", eDate);
				//System.out.println("cust_item_ary: " + custAry.toString());
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
		//System.out.println("620: " + qryInfo);
		String qryYear = qryJo.getString("qryYear");
		String qryMonth = qryJo.getString("qryMonth");
		String queryMonth = qryYear + qryMonth;
		
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());				
			//RowDataList itemList = bidDao.queryMonthBidList(compCode, qryJo);				
			RowDataList itemList = bidDao.queryMonthBidList(compCode, queryMonth);
			rtnJo.put("BIDMLIST", itemList.toJSONArray().toString());		
			//System.out.println(MsgUtils.toString(true, "", rtnJo));
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
	
	/**
	 * 查當周的有效商品
	 * @param compCode
	 * @param qryBidUuid
	 * @param out
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	private void QUERY_SUB_DATA_LIST(String compCode, String qryBidUuid, PrintWriter out) throws FileNotFoundException, Exception {	
					
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());				
			JSONArray itemList = bidDao.queryBidItemList(compCode, qryBidUuid);
			rtnJo.put("BIDDLIST", itemList.toString());
			out.print(MsgUtils.toString(true, "", rtnJo));	
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: QUERY_SUB_DATA_LIST");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}			
		
	}
	
	private void SAVE_DATA_LIST(String compCode, String loginID, String addInfo, String updInfo, PrintWriter out) throws FileNotFoundException, Exception {	
		
		String msg = "";
		boolean success = false;
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {								
			pes.openConnection("HBD_DMZ");
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			//pes.getConnection().setAutoCommit(false);
			
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
				jo.put("loginID", loginID);
				int c = bidDao.updateData(jo);
				if (c == 1){
					//System.out.println("update okk: " + c);
					updCnt += c;    
				}else{
					//System.out.println("no update: " + c);
					// 用 msg 保留 exception 當下的 code號
					msg = "Upd[" + jo.getString("txtBidName") + "]";
					break;
				}
			}
			
			int addRow = 0;
			int addCnt = 0;
			
			// insert	                        
			JSONArray addAry = JSONArray.fromObject(addInfo);
			addRow = addAry.size();			
			for(int i=0; i < addAry.size(); i++) {
			
				//System.out.println("ADD data: " + i);
				JSONObject jo = addAry.getJSONObject(i);
				jo.put("compCode", compCode);
				int c = bidDao.addData(jo);                
				if (c == 1){
					addCnt += c;      	  
					// todo: 取得 bidmUuid
					String bidmID = bidDao.getBidmUuid(jo);
					//System.out.println("bidmID: " + bidmID);
					
					JSONArray itmAry = itemDao.queryItemList(compCode, "*");
					//System.out.println("itemAry count: " + itmAry.size());
					
					int ic = 0;
					for(int j=0; j < itmAry.size(); j ++){
						JSONObject itm = itmAry.getJSONObject(j);	                		
						itm.put("bidmUuid", bidmID);
						itm.put("biddItem", itm.getString("itemCode"));
						itm.put("biddPrice", itm.getString("itemPrice"));	                		
						/*
						if (bc ==0){
							logger.error("bidmID: " + bidmID);
							logger.error("itemCo: " + itm.getString("itemCode"));
						}else{
							logger.debug("bc count: " + bc);
						}
						System.out.println(itm.toString());
						*/
						ic += bidDao.addBiddData(compCode, itm);
					}					
					//System.out.println("insert count: " + ic);					
					//int ic = bidDao.batchInsertBidItem(compCode, bidmID);											
				}else{
					// 用 msg 保留 exception 當下的 code號
					msg = "Add[" + jo.getString("txtBidName") + "]";
					break;
				}
			}
			
			//System.out.println("add: " + addRow + "/" + addCnt);
			//System.out.println("upd: " + updRow + "/" + updCnt);
			
			if ((updRow+addRow) == (updCnt+addCnt)){
				// 批次新增物料
				//System.out.println("commit save data");
				pes.getConnection().commit();
				msg = "";
				success = true;
			}else{
				//System.out.println("roll back save data");
				pes.getConnection().rollback();
				msg += " ! Data save error!";
				success = false;
			}
			
			JSONObject jo = new JSONObject();            
			jo.put("count", (updCnt+addCnt));			  
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
