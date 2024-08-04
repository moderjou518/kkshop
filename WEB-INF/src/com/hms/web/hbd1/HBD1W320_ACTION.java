package com.hms.web.hbd1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
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
import com.hms.web.hbd1.dao.HMP0BID_DAO;
import com.hms.web.hbd1.dao.HMP0ITEM_DAO;
import com.hms.web.hbd1.dao.HMP0POMM_DAO;
import com.hms.web.hbd1.dao.HMS0MISC_DAO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * HBD1W320 
 * 
 * @author Window10
 * 
 */
public class HBD1W320_ACTION implements HMS_WEBACTION {

	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		
		logger.info("HBD1W320 doAction:#");
		
		/* 第一次進 來載入廠商資料 */
		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) session.getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		
		
		
		/*
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
			logger.error("compID: " + compID);
			ex.printStackTrace();
		} finally {
			pes.closeConnection();
		}						
		
		request.getSession().setAttribute("COMP_ID", compID);
		request.getSession().setAttribute("COMP_CODE", compCode);
		request.getSession().setAttribute("COMP_NAME", compName);
		
		*/

		
	}

	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		//logger.info("jc:#");
		//System.out.println("2222222222222222320 action ::: ");
		PrintWriter out = response.getWriter();
		out.print(MsgUtils.toString(true, "", null));
		
	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) session.getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		
		
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);
		
		//System.out.println("320 action ::: " + compCode);
		
		// logger.info("jr method: " + method);
		
		if ("PAGE_LOAD".equals(method)){
			this.PAGE_LOAD(compCode, "", out);
		}

		
		// 抓當周預訂單起迄日(標頭)
		if ("LOAD_WEEKDAY_LIST".equals(method)){			
			this.LOAD_WEEKDAY_LIST(compCode, request, out);
		}
		
		// 載入某一天訂單資料
		if ("LOAD_TODAY_PO_LIST".equals(method)){
			String date1 = UTIL_String.nvl(request.getParameter("selDate1"), UTIL_String.getNowString("yyyyMMdd"));
			String date2 = UTIL_String.nvl(request.getParameter("selDate2"), UTIL_String.getNowString("yyyyMMdd"));
			this.LOAD_TODAY_PO_LIST(compCode, date1, date2, out);
		}
		
		// 載入: 出貨維護
		if ("LOAD_PODD_LIST".equals(method)){
			String selBidUUID = UTIL_String.nvl(request.getParameter("selBidUUID"), "");
			String selBuyer = UTIL_String.nvl(request.getParameter("selBuyer"), "");
			String selDay = UTIL_String.nvl(request.getParameter("selDate"), "");
			String selSeq = UTIL_String.nvl(request.getParameter("selSeq"), "");			
			
			this.LOAD_PODD_LIST(compCode, selBidUUID, selBuyer, selDay, selSeq, out);			
		}
		
		// 選擇其他周
		// 載入客戶當月的預訂單列表
		if ("LOAD_BID_LIST".equals(method)){
			// System.out.println("320 LOAD BID LIST");
			this.LOAD_BID_LIST(compCode, request, out);
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


	@SuppressWarnings("unchecked")
	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("doUpdate:#");
		
		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) session.getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		String loginID = ui.getLoginID(request);
		
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);		
		String msg = "";
				
		if ("SAVE_RCV_INFO".equals(method)){			
			
			String wkBidmUUID = UTIL_String.nvl(request.getParameter("parmBidmUUID"), "");
			String wkBuyer = UTIL_String.nvl(request.getParameter("parmBuyer"), "");
			String wkPoDate = UTIL_String.nvl(request.getParameter("parmPoDate"), "");
			String wkPoSeq = UTIL_String.nvl(request.getParameter("parmPoSeq"), "");
			
			// 有下單的品項
			String rcvInfo = UTIL_String.nvl(request.getParameter("revInfo"), "{}");
			JSONObject rcvJo = JSONObject.fromObject(rcvInfo);
			String[] rcvItemAry = null;
			String[] rcvQtyAry = null;
			
			JSONObject poJo = new JSONObject();
			poJo.put("compCode", compCode);
			poJo.put("pommDate", wkPoDate);
			poJo.put("pommSeq", wkPoSeq);
			poJo.put("buyer", wkBuyer);
			poJo.put("hac0BascLoginId", wkBuyer);			
			poJo.put("bidUUID", wkBidmUUID);
			
			
			// 出貨
			int updateRow = 0;
			int updateCnt = 0;
			
			if (rcvJo.containsKey("txtPoddItem")){
				rcvItemAry = rcvJo.getString("txtPoddItem").split(",");
				rcvQtyAry = rcvJo.getString("txtPoddRcvdQty").split(",");
				updateRow = rcvItemAry.length;
			}
			
			// 臨時追加的品項
			String addInfo = UTIL_String.nvl(request.getParameter("addInfo"), "{}");
			JSONObject addJo = JSONObject.fromObject(addInfo);			
			String[] addItemAry = null;
			String[] addQtyAry = null;
			int addRow = 0;
			int addCnt = 0;
			
			// 有可能每種品項都有預訂，導致加購品項是空的
			if (addJo.containsKey("txtPoddItem")){
				addItemAry = addJo.getString("txtPoddItem").split(",");
				addQtyAry = addJo.getString("txtPoddRcvdQty").split(",");	
			}
			
			JSONObject rtnJo = new JSONObject(); 
			PrepareExecuteSql pes = new PrepareExecuteSql();
			Connection myConn = null;
			try {
				pes.openConnection("HBD_DMZ");
				myConn = pes.getConnection();
				myConn.setAutoCommit(false);
				HMP0POMM_DAO poDao = new HMP0POMM_DAO(myConn);			
				HMP0BID_DAO bidDao = new HMP0BID_DAO(myConn);
				HAC0BASC_DAO hacDao = new HAC0BASC_DAO(myConn);
				JSONObject profileJo = hacDao.getProfile(compCode, loginID);				
				poJo.put("hac0BascName", profileJo.getString("hac0BascName"));
								
				// 當天所有有效商品品項
				JSONArray bidItemAry = bidDao.queryValidItemList(compCode, wkPoDate);
				rtnJo.put("BID_ITEM_LIST", bidItemAry.toString());
				
				// 抓最新的單價
				Hashtable priceTbl = bidDao.queryItemPriceByDate(compCode, wkPoDate);
				
				// 有出貨資料
				if (null != rcvItemAry){
					
					// 出貨資料
					for(int i=0; i<rcvItemAry.length; i++){					 
						
						String poItemCode = rcvItemAry[i].replace("\"", "").replace("]", "").replace("[", "");
						String poRcvQty = rcvQtyAry[i].replace("\"", "").replace("]", "").replace("[", "");
						String itemPrice = "0";
						
						// 抓最新的單價
						//JSONObject biddItem = bidDao.getBiddData(compCode, wkBidmUUID, poItem);
						if(priceTbl.get(poItemCode) == null){
							itemPrice = "0";
						}else{
							itemPrice = priceTbl.get(poItemCode).toString();								
						}
												
						int c = poDao.updateRcvPO(compCode, wkPoDate, wkPoSeq, poItemCode, poRcvQty, itemPrice, loginID);									
						//int c = poDao.updateRcvPO(compCode, wkPoDate, wkPoSeq, poItem, poQty, "0");			
						if (c!=1){
							msg = String.format(" Item rcv upd cnt(%d): %s-%s-%s, Item:%s-%s-%s", c, compCode, wkPoDate, wkPoSeq, poItemCode, "0", poRcvQty);
							logger.error(msg);						
						}else{
							System.out.println("updItem: " + poItemCode);
							updateCnt ++;
						}								
					}	
				}						

				// 有可能每種品項都有預訂，導致加購品項是空的
				if (null != addItemAry){
					
					// 當天沒有下任何單, 新增po單&取號
					if("".equals(wkPoSeq)){
						
						wkPoSeq = poDao.getMaxSeq(compCode, wkPoDate);
						poJo.put("pommSeq", wkPoSeq);
						
						// 新增pomm																	
						int poCnt = poDao.addPOMMData(poJo);
						
						if (poCnt == 0){
							
						}
						
					}							
					
					for(int i=0; i<addItemAry.length; i++){			
						
						String addItem = addItemAry[i].replace("\"", "").replace("]", "").replace("[", "");
						String poRcvQty = addQtyAry[i].replace("\"", "").replace("]", "").replace("[", "");
						String itemPrice = "0";

						if(!"0".equals(poRcvQty)){
							addRow +=1;
							
							if(priceTbl.get(addItem) == null){
								itemPrice = "0";
							}else{
								itemPrice = priceTbl.get(addItem).toString();								
							}
							
							int c = poDao.addRcvPO(compCode, wkPoDate, wkPoSeq, addItem, poRcvQty, itemPrice, loginID);							
							if (c==0){
								msg = String.format(" Item rcv add fail(%d): %s-%s-%s, Item:%s-%s-%s", addCnt, compCode, wkPoDate, wkPoSeq, addItem, "0", poRcvQty);
								logger.error(msg);						
							}else{
								System.out.println("addItem: " + addItem);
								addCnt +=1;
							}	
						}													
					}
					
				}
				
				//System.out.println(String.format("Cnt- u:%d, a:%d ", updateCnt, addCnt));
				//System.out.println(String.format("Row- u:%d, a:%d ", updateRow, addRow));				
				
				if ((updateCnt+addCnt) == (updateRow + addRow)){
					myConn.commit();					
					msg = MsgUtils.toString(true, "", rtnJo);						
				}else{										
					logger.error(String.format(" PO# Add %s-%s", addRow, addCnt));
					logger.error(String.format(" PO# Rcv %s-%s", updateRow, updateCnt));					
					myConn.rollback();
					msg = MsgUtils.toString(false, "", rtnJo);					
				}	
				
				/*
				 * 20240617: 單獨抓出該筆資料丟回去
				 */
				// 找出該名客人訂單資料
				//JSONArray poCustsAry = poDao.queryDayCustomer(compCode, wkPoDate, wkPoDate, wkBuyer);
				//JSONArray custPOItemsAry = poDao.queryWeekPOList(compCode, wkBuyer, wkPoDate, wkPoDate);
				
				//JSONArray allCustsAry = poDao.queryAllCustomer(compCode);
				
				//JSONObject custJo = new JSONObject();
				
				// 將下單的清單轉換成當期的完整物料清單
				//JSONArray custBidItemsAry = new JSONArray();				

				// 整理
				/*
				bidItemAry.forEach(obj -> {
					JSONObject bidItem = (JSONObject) obj;		    
					bidItem.put("itemQty", "0");
					bidItem.put("itemRcvdQty", "0");				    
					
					custPOItemsAry.forEach(itmObj->{				    	
						JSONObject poItem = (JSONObject) itmObj;
						if (bidItem.getString("itemCode").equals(poItem.getString("poddItem"))){
							bidItem.put("itemQty", poItem.getString("poddOrdQty"));
							if (!bidItem.getString("itemQty").equals("0")){
								// 有預訂資料
								custJo.put("poOrdQtySum", "1");
							}
							
							bidItem.put("itemRcvdQty", poItem.getString("poddRcvdQty"));
							if (!bidItem.getString("itemRcvdQty").equals("0")){
								// 有出貨資料
								custJo.put("poddSumRcvdQty", "1");
							}
						}				    	
					});
					
					custBidItemsAry.add(bidItem);				    			    
				});
				
				rtnJo.put("CUST_PO", poJo.toString());
				rtnJo.put("CUST_ITEM_ARY", custBidItemsAry);
				*/
				//System.out.println("cust_item_ary: " + custBidItemsAry.toString());
				msg = MsgUtils.toString(true, "", rtnJo);							
				
			} catch (Exception ex) {
				out.print(MsgUtils.toString(false, "", rtnJo));
				logger.error("Action Method: " + method);
				logger.error(ex.toString());
				ex.printStackTrace();				
			} finally {
				//pes.closeConnection();
				myConn.close();
			}
			
			//System.out.println("msg:" + msg);
			out.print(msg);
			
			
		}

	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		// logger.info("jd#");
	}
	
	// 抓當周日期
	private void LOAD_WEEKDAY_LIST(String compCode, HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {		

		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		String ordSDate = UTIL_String.nvl(request.getParameter("selOrdSdate"), "");		
		
		try {
			pes.openConnection("HBD_DMZ");
			
			// 抓當周預訂單的起迄日、星期			
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			String bidmUUID = bidDao.getCurrentBidmUUID(compCode);
			if ("".equals(ordSDate)){
				JSONObject jo = bidDao.getBidmData(compCode, bidmUUID);
				ordSDate = jo.getString("bidOrdSdate");	
			}
			
			JSONArray weekDayAry = bidDao.getThisWeek(compCode, ordSDate);

			rtnJo.put("WEEK_DAY_LIST", weekDayAry);
			rtnJo.put("OrdSDate", ordSDate);
			String msg = MsgUtils.toString(true, "", rtnJo); 
			out.print(msg);
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
	
	
	@SuppressWarnings("unchecked")
	private void LOAD_PODD_LIST(String compCode, String selBidUUID, String selBuyer, String selDate, String selSeq, PrintWriter out) throws FileNotFoundException, Exception {
			
		JSONObject rtnJo = new JSONObject(); 
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			HMP0POMM_DAO poDao = new HMP0POMM_DAO(pes.getConnection());				
			//JSONArray custList = poDAO.queryPOList(compCode, selBuyer, selDate, selSeq);
			//String bidUUID = bidDao.getBidmUUIDByPoDate(compCode, selDate);
			// 完整的bid item清單
			//JSONArray bidItemAry = bidDao.queryBidValidItemList(compCode, selBidUUID);			
			JSONArray bidItemAry = bidDao.queryValidItemList(compCode, selDate);
			// 當天的 PO ITEM清單
			JSONArray poItemAry = poDao.queryPOList(compCode, selBuyer, selDate);
			
			//System.out.println("bidUUID: " + selBidUUID);
			//System.out.println("bidItemAry: " + bidItemAry.toString());
			
			// 找出沒有在預購清單中的物料
			JSONArray poAddItemAry = new JSONArray();
			String today = UTIL_String.getFutureString(0);
			// 20220421: 當天才需要另外show出其他未訂購的商品
			if(today.equals(selDate)){
				
				// 有效的商品
				bidItemAry.forEach(obj -> {
				    
					JSONObject bidItem = (JSONObject) obj;				    
				    String ordMk = "N";
				    String poddDate = "";
				    String poddSeq = "";
				    
				    // 有預訂的商品
				    for (int i=0; i < poItemAry.size(); i++){
				    	JSONObject poItem = poItemAry.getJSONObject(i);
				    	poddDate = poItem.getString("poddDate");
				    	poddSeq = poItem.getString("poddSeq");
				    	if(poItem.getString("poddItem").equals(bidItem.getString("itemCode"))){
				    		ordMk = "Y";
				    	}
				    }
				    
				    // 沒有預訂的商品
				    if ("N".equals(ordMk)){
				    	JSONObject addItem = new JSONObject();			    	
				    	addItem.put("poddDate", poddDate);
				    	addItem.put("poddSeq", poddSeq);
				    	addItem.put("itemAbbr", bidItem.getString("itemAbbr"));
				    	addItem.put("poddItem", bidItem.getString("itemCode"));
				    	addItem.put("poddOrdQty", "0");
				    	addItem.put("poddRcvdQty", "0");			    	
				    	poAddItemAry.add(addItem);
				    }   
				});	
			}
			
			/*
			System.out.println("poItemAry Size: " + poItemAry.size());
			System.out.println("poAddItemAry Size: " + poAddItemAry.size());
			System.out.println("poAddItemAry: " + poAddItemAry.toString());
			*/
			
			rtnJo.put("PO_ADD_ITEM_LIST", poAddItemAry.toString());
			rtnJo.put("PO_ITEM_LIST", poItemAry.toString());
			
			//System.out.println("rtnJo: " + rtnJo.toString());
			out.print(MsgUtils.toString(true, "", rtnJo));	
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: LOAD_PODD_LIST");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}
		
	}	
		
	
	@SuppressWarnings("unchecked")
	private void LOAD_TODAY_PO_LIST(String compCode, String poDate1, String poDate2, PrintWriter out) throws FileNotFoundException, Exception {
		
				
		//System.out.println("320 load po list: " + poDate1 + "-" + poDate2);
		
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		
		try {
			pes.openConnection("HBD_DMZ");
			//HAC0BASC_DAO hacDao = new HAC0BASC_DAO(pes.getConnection());
			HMP0POMM_DAO poDao = new HMP0POMM_DAO(pes.getConnection());			
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());			
			String bidUUID = bidDao.getBidmUUIDByPoDate(compCode, poDate1);
			JSONObject bidJo = bidDao.getBidmData(compCode, bidUUID);
			
			// 表頭：物料清單(全部清單)，該周有設定價格的物料
			//JSONArray bidItemAry = bidDao.queryBidValidItemList(compCode, bidUUID);			
			JSONArray bidItemAry = bidDao.queryValidItemList(compCode, poDate1);
			
			// 當天各種商品的訂購總和
			/*
			bidItemAry.forEach(obj -> {
			    JSONObject itm = (JSONObject) obj;			    
			    JSONObject sumQty = poDao.queryDailyItemQtySum(compCode, itm.getString("itemCode"), poDate1, poDate2);			    
			    itm.put("poOrdQtySum", sumQty.getString("poOrdQtySum"));
			    itm.put("poRcvdQtySum", sumQty.getString("poRcvdQtySum"));			    
			});
			*/
			
			// 20210725: 找出 所有客人
			JSONArray allCustsAry = poDao.queryAllCustomer(compCode);
			JSONArray poCustsAry = poDao.queryDayCustomer(compCode, poDate1, poDate2, "*");
		
			
			// 整理 allcustary
			for(int i=0; i < allCustsAry.size(); i++){
				
				JSONObject cust = allCustsAry.getJSONObject(i);
				// 1開始預設沒有出貨資料								
				cust.put("poddSumRcvdQty", "0");
				cust.put("poOrdQtySum", "0");
				
				// 20220413: po 日期
				cust.put("bidUUID", bidUUID);
				cust.put("pommDate", poDate1);
				cust.put("pommSeq", "");
				
				// 紀錄po#
				for(int j=0; j < poCustsAry.size(); j++){					
					JSONObject poCust = poCustsAry.getJSONObject(j);
					if (poCust.getString("pommBuyer").equals(cust.getString("hac0BascLoginId"))){
						cust.put("pommDate", poCust.getString("pommDate"));
						cust.put("pommSeq", poCust.getString("pommSeq"));
						break;
					}
				}
				
				// 客人的下單資料
				JSONArray custPOItemsAry = poDao.queryWeekPOList(compCode, cust.getString("hac0BascLoginId"), poDate1, poDate1);
				// 將下單的清單轉換成當期的完整物料清單
				JSONArray custBidItemsAry = new JSONArray();				
				
				// 整理
				bidItemAry.forEach(obj -> {
				    JSONObject bidItem = (JSONObject) obj;		    
				    bidItem.put("itemQty", "0");
				    bidItem.put("itemRcvdQty", "0");				    
				    custPOItemsAry.forEach(itmObj->{				    	
				    	JSONObject poItem = (JSONObject) itmObj;
				    	if (bidItem.getString("itemCode").equals(poItem.getString("poddItem"))){
				    		bidItem.put("itemQty", poItem.getString("poddOrdQty"));
				    		if (!bidItem.getString("itemQty").equals("0")){
				    			// 有預訂資料
				    			cust.put("poOrdQtySum", "1");
				    		}
				    		
				    		bidItem.put("itemRcvdQty", poItem.getString("poddRcvdQty"));
				    		if (!bidItem.getString("itemRcvdQty").equals("0")){
				    			// 有出貨資料
				    			cust.put("poddSumRcvdQty", "1");
				    		}
				    	}				    	
				    });
				    custBidItemsAry.add(bidItem);				    			    
				});				
				cust.put("CUST_ITEM_ARY", custBidItemsAry);				
			}	
			
			// 從客戶的訂單資料統計出各物料的訂購及出貨加總數量
			for(int i=0; i < bidItemAry.size(); i++){
				int bidOrdQtySum = 0;
				int bidRcvdQtySum = 0;				
				JSONObject bidItem = bidItemAry.getJSONObject(i);
				// 從每個客戶中找出該品項				
				for(int j=0; j < allCustsAry.size(); j++){
					JSONObject cust = allCustsAry.getJSONObject(j);
					JSONArray custItemAry = cust.getJSONArray("CUST_ITEM_ARY");
					JSONObject custPoItem = custItemAry.getJSONObject(i);
					bidOrdQtySum += custPoItem.getInt("itemQty");
					bidRcvdQtySum += custPoItem.getInt("itemRcvdQty");					
				}				
				bidItem.put("poOrdQtySum", String.valueOf(bidOrdQtySum));
				bidItem.put("poRcvdQtySum", String.valueOf(bidRcvdQtySum));				
			}	
			
			rtnJo.put("REPORT_TIME", UTIL_String.getNowString("yyyy/MM/dd HH:mm"));
			rtnJo.put("DATA_TIME", UTIL_String.getNowString("yyyy/MM/dd HH:mm"));
							
			rtnJo.put("BID_DATA", bidJo.toString());
			rtnJo.put("BID_ITEM_LIST", bidItemAry.toString());		
			rtnJo.put("CUST_LIST", allCustsAry.toArray());
			
			rtnJo.put("PODATE", poDate1);
			rtnJo.put("SHORT_DATE", poDate1.substring(4, 6) + "/" + poDate1.substring(6, 8));
			rtnJo.put("PODATEOFWEEK", "(" + UTIL_String.getWeekOfDate(poDate1) + ")");
			
			String msg = MsgUtils.toString(true, "", rtnJo);	
			//System.out.println("320 msg: " + msg);
			out.print(msg);				
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
	 * 選擇其他周
	 * @param compCode
	 * @param out
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	private void LOAD_BID_LIST(String compCode, HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {		

		
		String queryMonth = UTIL_String.nvl(request.getParameter("queryMonth"), "");
		
		//System.out.println("queryMonth: " + queryMonth);
		
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			
			RowDataList rdl = bidDao.queryMonthBidList(compCode, queryMonth);	
			
			JSONArray ary = null;
			if (rdl.size() > 0){				
				ary = rdl.toJSONArray();
			}else{
				ary = new JSONArray();
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

	
	
}
