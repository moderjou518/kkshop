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
import com.hms.web.hbd1.dao.HMS0MISC_DAO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * HBD1W020 
 * 
 * @author Window10
 * 
 */
public class HBD1W610_ACTION implements HMS_WEBACTION {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");

	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("HBD1W610 doAction:#");
		
		/* 蝚砌�甈⊿�� 靘�撱���� */
		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);							

	}

	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		//logger.info("jc:#");
		
		
		
		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);
		
		
		
		if ("ADD_DATA".equals(method)){
			String info = UTIL_String.nvl(request.getParameter("info"), "{}");
			JSONObject jo = JSONObject.fromObject(info);		
			
			jo.put("compCode", compCode);
			jo.put("loginID", ui.getLoginID(request));
			
			this.ADD_DATA(jo, out);
		}
		
	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		
		
		

		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);		
		
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);					
		JSONObject rtnJo = new JSONObject();
		
		if ("PAGE_LOAD".equals(method)){					
			this.PAGE_LOAD(compCode, "ITEM_CLAS1", out);			
		}
		
		if ("QUERY_DATA_LIST".equals(method)){		
			String qryInfo = UTIL_String.nvl(request.getParameter("qryInfo"), "{}");
			JSONObject qryJo = JSONObject.fromObject(qryInfo);
			String qryItemType = qryJo.getString("qryItemType");
			this.QUERY_DATA_LIST(compCode, qryItemType, out);
		}
	}

	/**
	 * 20220713: 新 增價格隱藏欄欄位(item_hide_price_mk)
	 */
	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("doUpdate:#");		
				
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
                	// � msg 靽�� exception �銝�� code���
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
                jo.put("loginID", ui.getLoginID(request));
                String newSeq = itemDAO.getMaxSeq(compCode);
                jo.put("newSeq", newSeq);
                //System.out.println("item Jo: " + jo.toString());
                int c = itemDAO.addData(jo);                
                if (c == 1){
                	addCnt += c;
                }else{
                	// � msg 靽�� exception �銝�� code���
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

	}
	
	private void PAGE_LOAD(String compCode, String qryKind, PrintWriter out) throws FileNotFoundException, Exception {
		
		
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();			
		try {
			pes.openConnection("HBD_DMZ");
			HMS0MISC_DAO miscDAO = new HMS0MISC_DAO(pes.getConnection());
			JSONArray typeList = miscDAO.queryDataList(compCode, qryKind);
			rtnJo.put("ITEMCLASS", typeList);
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
	
	private void QUERY_DATA_LIST(String compCode, String qryItemType, PrintWriter out) throws FileNotFoundException, Exception {		
		
		JSONObject rtnJo = new JSONObject();
		
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");
			HMP0ITEM_DAO itemDAO = new HMP0ITEM_DAO(pes.getConnection());
			JSONArray itemList = itemDAO.queryItemList(compCode, qryItemType);				
			rtnJo.put("success", true);
			rtnJo.put("ITEMLIST", itemList);				
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
	
	
	private void ADD_DATA(JSONObject itemJo, PrintWriter out) throws Exception{
		
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.openConnection("HBD_DMZ");
			HMP0ITEM_DAO itemDao = new HMP0ITEM_DAO(pes.getConnection());
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			String newSeq = itemDao.getMaxSeq(itemJo.getString("compCode"));
			itemJo.put("newSeq", newSeq);
			//System.out.println("newSeq: " + newSeq);
			int i = itemDao.addData(itemJo);		
			
			//System.out.println("i: " + i);
			
			boolean result = false;
			if (i >0){
				i = bidDao.addBatchBiddData(itemJo.getString("compCode"), newSeq, itemJo.getString("txtItemPrice"));
				//System.out.println("isi: " + i);
				result = (i >0);				
			}else{
				result = false;
			}			
			rtnJo.put("success", result);
							
			String msg = MsgUtils.toString(result, "", rtnJo); 
			out.print(msg);	
		} catch (Exception ex) {							
			rtnJo.put("success", false);
			logger.error("Action Method: ADD_DATA");
			logger.error(ex.toString());
			ex.printStackTrace();				
		} finally {
			pes.closeConnection();
		}
		
	}
	
}
