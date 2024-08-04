package com.hms.web.hbd1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
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
 * HBD1W010 
 * 
 * @author Window10
 * 
 */
public class HBD1W010_ACTION implements HMS_WEBACTION {

	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		//logger.info(" !! HBD1W010 doAction:#");
		
		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		//String compName = "";

		/* 來載入廠商資料		
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
				//compName = jo.getString("miscDesc");
			}else{
				rtnJo.put("success", false);
				compCode = "";
				//compName = compCode + " not found!";				
			}
		} catch (Exception ex) {
			String msg = ex.toString();
			rtnJo.put("msg", msg);
			rtnJo.put("success", false);			
			logger.error(msg);
			logger.error(" !! compCode: " + compCode);
			ex.printStackTrace();
		} finally {
			pes.closeConnection();
		}	
		 */
		
	}

	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		// logger.info(" !! jc:#");	
	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();		
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);
		
		//logger.info(" !! jr method: " + method);
		
		
		// 目前有開放的預訂單
		if ("LOAD_BID_LIST".equals(method)){
			this.LOAD_BID_LIST(request, out);
		} 
		
		// 載入某張預訂單的日期及產品
		if ("LOAD_BID_ITEM_LIST".equals(method)){								
			this.LOAD_BID_ITEM_LIST(request, out);									
		}
		
		// 
		if ("BID_ITEM_TO_CART_ITEM".equals(method)){			                  
			this.BID_ITEM_TO_CART_ITEM(request, out);			
		}

	}	

	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info(" !! doUpdate:#");		
		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");			
		String compCode = ui.getCompCode(request);
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);
		
		// 把購物車的品項寫到po
		if ("CART_ITEM_TO_ORDER_ITEM".equals(method)){			
			this.CART_ITEM_TO_ORDER_ITEM(request, out);			
		}		

	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {		
		// logger.info("jd#");
	}
	
	private void LOAD_BID_LIST(HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {
	
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());					
			RowDataList rdl = bidDao.queryOnSaleBidList(compCode);			
			rtnJo.put("success", true);
			rtnJo.put("BIDLIST", rdl.toJSONArray().toString());				
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
	 * 某張預訂單的物料清單
	 * @param compCode
	 * @param bidUUID
	 * @param out
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void LOAD_BID_ITEM_LIST(HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {
	
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		String loginID = ui.getLoginID(request);
		String bidUUID = UTIL_String.nvl(request.getParameter("queryBidUUID"), "");		
		
		
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");
			
			HMP0POMM_DAO poDao = new HMP0POMM_DAO (pes.getConnection()); 
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());	
			JSONObject currBidmJo = bidDao.getBidmData(compCode, bidUUID);				
			
			String prevUuid = bidDao.getCurrentBidmUUID(compCode);
			JSONObject prevBidmJo = bidDao.getBidmData(compCode, prevUuid);
			//logger.info(" prevBidmJo: " + prevBidmJo.toString());
			
			
			// 取得有效的商品(有設定價格的)
			//RowDataList bidItemList1 = bidDao.queryItemList(compCode, bidUUID, true);
			JSONArray bidItemList = bidDao.queryValidItemList(compCode, currBidmJo.getString("bidOrdSdate"));
			rtnJo.put("BID_DAY_COUNT", bidItemList.size());
			
			
			
			if (null != currBidmJo){
				
				// 當周所有日期
				List<LocalDate> bidDates = UTIL_String.getValidDatesBetweenDate(currBidmJo.getString("bidOrdSdate"), currBidmJo.getString("bidOrdEdate"));
				
				// 看當周有沒有訂購資料
				JSONArray currPoAry = poDao.queryWeekPOList(compCode, loginID, currBidmJo.getString("bidOrdSdate"), currBidmJo.getString("bidOrdEdate"));
				//logger.info(" currPoAry size: " + currPoAry.size());;
				
				// 若當周沒有訂購資料，就抓本周的訂購資料
				JSONArray prevPoAry = poDao.queryWeekPOList(compCode, loginID, prevBidmJo.getString("bidOrdSdate"), prevBidmJo.getString("bidOrdEdate"));
				//logger.info(" prevPoAry size: " + prevPoAry.size());;

				JSONArray bidAry = new JSONArray();
				for(int i=0; i < bidDates.size(); i++){
					LocalDate orderDate = bidDates.get(i);										
					JSONObject bid = new JSONObject();
					bid.put("ORDER_DATE", orderDate.toString());
					String[] orderDateStrAry = orderDate.toString().split("-");
					bid.put("PO_DATE", orderDateStrAry[1] + "/" + orderDateStrAry[2]);
					bid.put("WEEKDAY", UTIL_String.getWeekOfDate(orderDate.toString()));
					JSONArray bidItemAry = bidItemList;
					String currPoDate = orderDateStrAry[0] + orderDateStrAry[1] + orderDateStrAry[2];
					
					// 把之前的預訂資料放進來
					for(int j=0; j < bidItemAry.size(); j++){
						JSONObject bidItemJo = bidItemAry.getJSONObject(j);
						bidItemJo.put("poddQty", "0");
						
						// 載入目前預訂單的資料
						if (currPoAry.size() > 0){
							currPoAry.forEach(obj->{							
								JSONObject poItemJo = (JSONObject) obj;
								if (poItemJo.getString("pommDate").equals(currPoDate)){
									if (poItemJo.getString("poddItem").equals(bidItemJo.getString("itemCode"))){
										bidItemJo.put("poddQty", poItemJo.getString("poddOrdQty"));
									}
								}
								
							});							
						}else{
							// 載入前1次預訂單的資料
							SimpleDateFormat dateFormat = new SimpleDateFormat();
							dateFormat.applyPattern( "yyyyMMdd" );							
							
							prevPoAry.forEach(obj->{
								JSONObject poItemJo = (JSONObject) obj;
								
								// 把歷史訂單的日期轉成當周的日期(+7天)
								Date prevPoDate = null;
								try {
									prevPoDate = UTIL_String.getAddDay(poItemJo.getString("pommDate"), 7, 1);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								String prevPoDateString = dateFormat.format( prevPoDate.getTime() ).toString();								
								
								if (prevPoDateString.equals(currPoDate)){
									if (poItemJo.getString("poddItem").equals(bidItemJo.getString("itemCode"))){
										//bidItemJo.put("poddQty", poItemJo.getString("poddRcvdQty"));
										bidItemJo.put("poddQty", poItemJo.getString("poddOrdQty"));
									}
								}
							});
							
						}
					//System.out.println("bidItemJo: " + bidItemJo.toString());	
					}
										
					bid.put("ITEM_LIST", bidItemAry);					
					bidAry.add(bid);
				}					
				rtnJo.put("WEEK_LIST", bidAry);
			}
			
			String msg = MsgUtils.toString(true, "", rtnJo);
			//System.out.println("rtnJo: " + msg);
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
	
	private void BID_ITEM_TO_CART_ITEM(HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {
		
		String dayInfo = UTIL_String.nvl(request.getParameter("dayInfo"), "{}");
		String dataInfo = UTIL_String.nvl(request.getParameter("dataInfo"), "{}");
		JSONArray dataAry = JSONArray.fromObject("[" + dataInfo + "]");
		
		//System.out.println(" !! bidItem to CartItem data info: " + dataInfo);
		
		String[] dayAry = dayInfo.split(",");    	    	
		JSONArray bidAry = new JSONArray();
		
		for(int i=0; i < dayAry.length ;i++){
			
			JSONObject dayItmJo = dataAry.getJSONObject(i);
			JSONObject dayJo = new JSONObject();
			
			// 日期 & 物料
			dayJo.put("ORDER_DATE", dayAry[i]); // 預訂單日期						
			dayJo.put("ORDER_WEEKDAY", UTIL_String.getWeekOfDate(dayAry[i]));			
			
			if(dayItmJo.has("txtBiddItem")){
				
				JSONArray dayItemAry = new JSONArray();    			
				String[] itemAry = dayItmJo.getString("txtBiddItem").replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", "").split(",");
				String[] nameAry = dayItmJo.getString("txtItemAbbr").replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", "").split(",");
				String[] qtyAry = dayItmJo.getString("txtPoddQty").replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", "").split(",");
				String[] priceAry = dayItmJo.getString("txtBiddPrice").replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", "").split(",");
				
				for(int c=0; c < itemAry.length; c++){
					
					JSONObject itemJo = new JSONObject();
					itemJo.put("itemCode", itemAry[c]);
					itemJo.put("itemAbbr", nameAry[c]);
					itemJo.put("poddQty", qtyAry[c]);
					itemJo.put("biddPrice", priceAry[c]);
					dayItemAry.add(itemJo);	
					
					
					if (!qtyAry[c].equals("0")){
						// 訂購數量不是0						
					}else{
						// 訂購數量為0						
					}    				
				}
				
				if(dayItemAry.size() > 0){	    				
					dayJo.put("ITEM_LIST", dayItemAry);
					bidAry.add(dayJo);
				}	    			
			}    		
			
		}
		JSONObject json = new JSONObject();
		json.put("WEEK_LIST", bidAry.toString());
		String rtn = MsgUtils.toString(true, "", json);
		//logger.debug(" !! BID_ITEM_TO_CART_ITEM: " + rtn);		
		out.print(rtn);	    	
		
	}
	
	/**
	 * 最後1步，成立訂單
	 * @param request
	 * @param out
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	private void CART_ITEM_TO_ORDER_ITEM(HttpServletRequest request, PrintWriter out) throws FileNotFoundException, Exception {
	
		HttpSession session = request.getSession();			
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");		
		String compCode = ui.getCompCode(request);
	
		//String loginInfo = UTIL_String.nvl(request.getParameter("loginInfo"), "{}");
		String dayInfo = UTIL_String.nvl(request.getParameter("dayInfo"), "{}");
		String dataInfo = UTIL_String.nvl(request.getParameter("dataInfo"), "{}");
		
		//JSONObject loginJo = JSONObject.fromObject(loginInfo);
		JSONArray dataAry = JSONArray.fromObject("[" + dataInfo + "]");
		
		// 
		String bidDate = "";
		
		boolean result = false;

		// loop cartItem & transfer to order item
		PrepareExecuteSql pes = new PrepareExecuteSql();
		JSONArray poDayAry = new JSONArray();	    	
		try {		
				
			// 預訂單起迄日區間
			String[] dayAry = dayInfo.split(",");			
			for(int i=0; i < dayAry.length ;i++){
				
				JSONObject dayItmJo = dataAry.getJSONObject(i);
				JSONObject dayJo = new JSONObject();
				if(i==0){ bidDate = dayAry[i].replace("-", "");}
				
				// 日期 & 物料
				dayJo.put("ORDER_DATE", dayAry[i]); 
				if(dayItmJo.has("txtBiddItem")){	    			
					
					// 拆解每日訂單中的訂購品項、名稱及數量到陣列裡面
					JSONArray dayItemAry = new JSONArray();    			
					String[] itemAry = dayItmJo.getString("txtBiddItem").replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", "").split(",");
					//String[] abbrAry = dayItmJo.getString("txtItemAbbr").replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", "").split(",");
					String[] qtyAry = dayItmJo.getString("txtPoddQty").replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", "").split(",");
					//String[] priceAry = dayItmJo.getString("txtBiddPrice").replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", "").split(",");
					
					//System.out.println("itemAry size: " + itemAry.length);
					//System.out.println("priceAry size: " + priceAry.length);
					//System.out.println("priceAry: " + priceAry.toString());
					
					// 購的品項、名稱及數量
					for(int c=0; c < itemAry.length; c++){
						JSONObject itemJo = new JSONObject();
						//itemJo.put("biddItem", itemAry[c]);
						itemJo.put("itemCode", itemAry[c]);
						//itemJo.put("itemAbbr", abbrAry[c]);
						itemJo.put("poddQty", qtyAry[c]);
						//itemJo.put("biddPrice", priceAry[c]);
						dayItemAry.add(itemJo);
						
						// 有訂購就放進每日訂單的陣列中(dayItemAry)
						if (!qtyAry[c].equals("0")){
								
						}else{
							
						}
					}
					
					if(dayItemAry.size() > 0){	    				
						dayJo.put("ITEM_LIST", dayItemAry);
						poDayAry.add(dayJo);
					}
				}
			}

			// 把整理好的資料寫到po單
			pes.openConnection("HBD_DMZ");				
			pes.getConnection().setAutoCommit(false);
			HMP0POMM_DAO pommDao = new HMP0POMM_DAO(pes.getConnection());
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			//HMP0ITEM_DAO itemDao = new HMP0ITEM_DAO(pes.getConnection());	
			
			String bidmUUID = bidDao.getBidmUUIDByPoDate(compCode, bidDate);
			JSONObject bidmJo = bidDao.getBidmData(compCode, bidmUUID);
			//System.out.println("bidmJo: " + bidmJo.toString());
			//System.out.println("bidmUUID: " + bidmUUID);			
			Hashtable priceTbl = bidDao.queryItemPriceByPeriod(compCode, bidmJo.getString("bidOrdSdate"), bidmJo.getString("bidOrdEdate"));			
			
			int poCnt = 0;
			for(int i=0; i < poDayAry.size(); i++){	    		
				
				JSONObject poJo = poDayAry.getJSONObject(i);	
				//logger.debug("--i: " + i);
				//logger.debug("--poJo: " + poJo.toString());
				//logger.debug("--getMaxSeq- ");
				JSONArray dayItemAry = poJo.getJSONArray("ITEM_LIST");
				String pommDate = poJo.getString("ORDER_DATE").replaceAll("-", "");				
				String pommSeq = pommDao.getMaxSeq(compCode, pommDate);
				
				//Hashtable priceTbl = bidDao.queryItemPriceByDate(compCode, pommDate);				
				poJo.put("compCode", compCode);
				poJo.put("pommDate", pommDate);
				poJo.put("pommSeq", pommSeq);
				poJo.put("buyer", ui.getLoginID(request));	
				
				//logger.debug("--delPODD/POMM-- ");
				// ** 新增訂單前，清空原來的訂單->每個人1天只有1張訂單，第2次訂購就等於重下訂單 **
				pommDao.delPODDData(poJo);
				pommDao.delPOMMData(poJo);				

				// 重新產生 pomm/podd
				int cnt1 = pommDao.addPOMMData(poJo);
				
				poCnt += cnt1;
				
				if (cnt1 ==1){					
					
					for(int j=0; j < dayItemAry.size(); j++){
						
						JSONObject poddItem = dayItemAry.getJSONObject(j);
						String poddQty = UTIL_String.nvl(poddItem.getString("poddQty").trim(), "0");
						poddItem.put("compCode", poJo.getString("compCode"));
						poddItem.put("poddDate", poJo.getString("pommDate"));
						poddItem.put("poddSeq", poJo.getString("pommSeq"));
						poddItem.put("poddItem", poddItem.getString("itemCode"));
						poddItem.put("poddQty", poddQty);						
						
						if ("0".equals(poddQty) || "".equals(poddQty)){
							// 沒有輸入 qty, 刪除品項							
							//logger.warn(" ! item qty is empty: " + poddItem.toString());
						}else{
							// 有輸入 qty
							String priceKey = poJo.getString("pommDate") + poddItem.getString("itemCode");
							if(priceTbl.get(priceKey) == null){
								poddItem.put("poddPrice", "0");
								logger.warn(" ! no item price found: " + priceKey);
							}else{
								//priceJo.getString("ipItemCode") + "-" + priceJo.getString("ipDate");								 
								poddItem.put("poddPrice", priceTbl.get(priceKey));	
							}
							String cntItem = pommDao.countAPODDData(poddItem);
							if(cntItem.equals("0")){
								int cnt2 = pommDao.addPODDData(poddItem);
								if (cnt2 != 1){
									//logger.warn(" ! insert item error: " + poJo.toString());
									logger.warn(" ! insert item error: " + poddItem.toString());								
									throw new Exception("insert item error");
								}	
							}else{
								logger.warn(" ! count item before insert dayItemAry: " + dayItemAry.toString());
								logger.warn(" ! count item before insert poddItem: " + poddItem.toString());
								throw new Exception(" dupicated item");
							}							
							//logger.debug("--addPODD--");
						}
						
					}	
				}else{
					logger.error("insert pomm error! " + poJo.toString());
					break;
				}		    		
			}
			
			if(poCnt == poDayAry.size()){
				result = true;
				pes.getConnection().commit();	
			}else{
				result = false;				
				logger.warn(" !! create po error ! dayInfo: " + dayInfo);
				logger.warn(" !! create po error ! dataInfo: " + dataInfo);
				logger.warn(" !! rollback create po !");
				pes.getConnection().rollback();
			}
			
			JSONObject json = new JSONObject();
			json.put("WEEK_LIST", poDayAry.toString());
			String rtn = MsgUtils.toString(result, "", json);			
			out.print(rtn);			
			
		}catch(Exception ex){
			logger.error("CART_ITEM_TO_ORDER_ITEM");
			logger.error(ex.toString());
			logger.error(poDayAry);
			ex.printStackTrace();
		}finally{
			pes.getConnection().close();
		}
	
	}
	
	
	
}
