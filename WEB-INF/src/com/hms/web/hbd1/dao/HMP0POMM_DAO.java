package com.hms.web.hbd1.dao;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.evergreen_hotels.bmg.whms1.exception.DataAccessException;
import com.evergreen_hotels.bmg.whms1.util.GenericDao;
import com.evergreen_hotels.bmg.whms1.util.PrepareExecuteSql;
import com.evergreen_hotels.bmg.whms1.util.RowData;
import com.evergreen_hotels.bmg.whms1.util.RowDataList;
//import com.evergreen_hotels.wlh.util.UTIL_ExceptionHandle;
//import com.evergreen.common.dao.GenericDao;
//import com.evergreen.common.dao.PrepareExecuteSql;
//import com.evergreen.common.exception.DataAccessException;
import com.hms.util.UTIL_String;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HMP0POMM_DAO extends GenericDao {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	public HMP0POMM_DAO(Connection conn) throws SQLException{
		super.setConnection(conn);
	}
	
	/**
	 * 取流水號
	 * @param comp
	 * @param poDate
	 * @return
	 */
	public String getMaxSeq(String comp, String poDate){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT Nz(Max(POMM_SEQ), 0) + 1   ");
		sb.append("   FROM HMP0POMM              ");
		sb.append("  WHERE POMM_COMP_CODE = ?   ");
		sb.append("    AND POMM_DATE= ?  			");
		
		//System.out.println(String.format("get max sesq poDate: %s", poDate));
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);
		parms.add(poDate);
		
		String result = "";        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            result = pre.getAString(sb.toString(), parms.toArray());
        } catch (SQLException e) {
        	logger.error(e.toString());
            result = "";
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
		
	}
    
	/**
	 * 新增1張po單
	 * @param jo
	 * @return
	 */
	public int addPOMMData(JSONObject jo){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" INSERT INTO HMP0POMM(                        		");
    	sb.append(" 		POMM_COMP_CODE, POMM_DATE, POMM_SEQ,        ");
    	sb.append(" 		POMM_REQ_DATE, POMM_BUYER, POMM_CLOS_MARK)  ");
    	sb.append(" VALUES(?, ?, ?, 									");
    	sb.append("			FORMAT(NOW(), 'yyyymmdd'), ?, 'N')          ");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	
    	parms.add(jo.getString("compCode"));
    	parms.add(jo.getString("pommDate"));
    	parms.add(jo.getString("pommSeq"));
    	parms.add(jo.getString("buyer"));
		
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            cnt = pre.updateSql(sb.toString(), parms.toArray());            
        } catch (SQLException e) {
            cnt = 0;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return cnt;
    	
    }
	
	public int delPOMMData(JSONObject jo){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" DELETE FROM HMP0POMM		");
    	sb.append("	 WHERE POMM_COMP_CODE = ? 	");
    	sb.append("    AND POMM_DATE = ?		");
    	sb.append("    AND POMM_BUYER = ?		");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	
    	parms.add(jo.getString("compCode"));
    	parms.add(jo.getString("pommDate"));    	
    	parms.add(jo.getString("buyer"));
		
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            cnt = pre.updateSql(sb.toString(), parms.toArray());            
        } catch (SQLException e) {
            cnt = 0;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return cnt;
    	
    }
	
	public int delPODDData(JSONObject jo){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" DELETE FROM HMP0PODD                 ");
    	sb.append("  WHERE PODD_COMP_CODE = ?            ");
    	sb.append("    AND NZ(PODD_RCVD_QTY, 0) = 0      ");
    	sb.append("    AND (PODD_DATE & PODD_SEQ) IN     ");
    	sb.append(" (                                    ");
    	sb.append(" 	SELECT POMM_DATE & POMM_SEQ      ");
    	sb.append("           FROM HMP0POMM              ");
    	sb.append("          WHERE POMM_COMP_CODE = ?    ");
    	sb.append("            AND POMM_DATE = ?         ");
    	sb.append("            AND POMM_BUYER = ?        ");
    	sb.append(" )                                    ");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	
    	parms.add(jo.getString("compCode"));
    	parms.add(jo.getString("compCode"));
    	parms.add(jo.getString("pommDate"));    	
    	parms.add(jo.getString("buyer"));
		
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            cnt = pre.updateSql(sb.toString(), parms.toArray());            
        } catch (SQLException e) {
            cnt = 0;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return cnt;
    	
    }
	
	
	
	public int delAPODD(String comp, String poddUuid){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" DELETE FROM HMP0PODD                 ");
    	sb.append("  WHERE PODD_COMP_CODE = ?            ");
    	sb.append("    AND PODD_UUID = ?                 ");
    	
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	
    	parms.add(comp);
    	parms.add(poddUuid);
    	
		
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            cnt = pre.updateSql(sb.toString(), parms.toArray());            
        } catch (SQLException e) {
            cnt = 0;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return cnt;
    	
    }
	
	/**
	 * 新增一筆podd
	 * @param jo
	 * @return
	 */
	public int addPODDData(JSONObject jo){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" INSERT INTO HMP0PODD(                                ");
    	sb.append(" 	PODD_COMP_CODE, PODD_DATE, PODD_SEQ, PODD_ITEM,  ");
    	sb.append(" 	PODD_ORD_QTY, PODD_RCVD_QTY, PODD_UNIT_PRICE, PODD_MOD_DATE)    ");
    	sb.append(" VALUES(?, ?, ?, ?, ?, 0, ?, FORMAT(NOW(), 'yyyymmdd'))                             ");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	
    	parms.add(jo.getString("compCode"));
    	parms.add(jo.getString("poddDate"));
    	parms.add(jo.getString("poddSeq"));
    	parms.add(jo.getString("poddItem"));
    	parms.add(jo.getString("poddQty"));
    	// 20210811: 物料單價出貨時會再抓1次
    	parms.add(jo.getString("poddPrice"));   	    	
		
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            cnt = pre.updateSql(sb.toString(), parms.toArray());            
        } catch (SQLException e) {
            cnt = 0;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return cnt;
    	
    }
	
	public int appendPODDData(JSONObject jo){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" INSERT INTO HMP0PODD(                                ");
    	sb.append(" 	PODD_COMP_CODE, PODD_DATE, PODD_SEQ, PODD_ITEM,  ");
    	sb.append(" 	PODD_ORD_QTY, PODD_RCVD_QTY, PODD_UNIT_PRICE, PODD_MOD_DATE)    ");
    	sb.append(" VALUES(?, ?, ?, ?, 0, ?, ?, FORMAT(NOW(), 'yyyymmdd'))                             ");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	
    	parms.add(jo.getString("compCode"));
    	parms.add(jo.getString("poddDate"));
    	parms.add(jo.getString("poddSeq"));
    	parms.add(jo.getString("poddItem"));
    	parms.add(jo.getString("poddRcvdQty"));
    	parms.add(jo.getString("poddPrice"));   
    	
    	System.out.println("append jo:" + jo.toString());
		
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            cnt = pre.updateSql(sb.toString(), parms.toArray());            
        } catch (SQLException e) {
            cnt = 0;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return cnt;
    	
    }
	
	public int delAPODDData(JSONObject jo){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" DELETE FROM HMP0PODD                 ");
    	sb.append("  WHERE PODD_COMP_CODE = ?            ");    	
    	sb.append("    AND PODD_DATE = ?     		");
    	sb.append("    AND PODD_SEQ = ?     		");
    	sb.append("    AND PODD_ITEM = ?     		");
    	sb.append("    AND NZ(PODD_RCVD_QTY, 0) = 0      ");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	
    	parms.add(jo.getString("compCode"));
    	parms.add(jo.getString("poddDate"));
    	parms.add(jo.getString("poddSeq"));
    	parms.add(jo.getString("poddItem"));    	   	    	
		
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            cnt = pre.updateSql(sb.toString(), parms.toArray());            
        } catch (SQLException e) {
            cnt = 0;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return cnt;
    	
    }
	
	/**
	 * 這個po單有幾個這item的品項
	 * @param jo
	 * @return
	 */
	public String countAPODDData(JSONObject jo){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" SELECT COUNT(*) FROM HMP0PODD                 ");
    	sb.append("  WHERE PODD_COMP_CODE = ?            ");    	
    	sb.append("    AND PODD_DATE = ?     		");
    	sb.append("    AND PODD_SEQ = ?     		");
    	sb.append("    AND PODD_ITEM = ?     		");    	
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	
    	parms.add(jo.getString("compCode"));
    	parms.add(jo.getString("poddDate"));
    	parms.add(jo.getString("poddSeq"));
    	parms.add(jo.getString("poddItem"));    	   	    	
		
        String cnt = "0";
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            cnt = pre.getAString(sb.toString(), parms.toArray());       
            if(null == cnt){
            	cnt = "0";
            }
        } catch (SQLException e) {
            cnt = "0";
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return cnt;
    	
    }
	
	/**
	 * 20210725: 按會員帳號(手機號碼)排序資料
	 * @param comp
	 * @return
	 */
	public JSONArray queryAllCustomer(String comp){
		
		StringBuffer sb = new StringBuffer();			
		sb.append(" SELECT 	HAC0_BASC_LOGIN_ID as POMM_BUYER,    ");
		sb.append("         HAC0_BASC_NAME ");
		sb.append("   FROM 	HAC0BASC                        	");
		sb.append("  WHERE 	HAC0_BASC_COMP_CODE = ?     		");
		sb.append("    AND  HAC0_BASC_GROUP = 'C'					");		
		sb.append(" ORDER BY HAC0_BASC_NOTE, HAC0_BASC_LOGIN_ID					");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);			
		
		JSONArray result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            result = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();    
            //System.out.println("cust cnt: " + result.size());
            //System.out.println("cust : " + result.toString());
        } catch (SQLException e) {
            result = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
	}
	
	/**
	 * 找出當天客戶
	 * @param comp
	 * @param day
	 * @return
	 */
	public JSONArray queryDayCustomer(String comp, String selDay1, String selDay2, String pommBuyer){
		
		ArrayList<String> parms = new ArrayList<String>();
		// 依備註欄位(排序用)進行資料排序 
		StringBuffer sb = new StringBuffer();
		sb.append("  SELECT 	POMM_DATE, POMM_SEQ, POMM_BUYER, HAC0_BASC_NOTE, HAC0_BASC_NAME,    ");
		sb.append("          SUM(PODD_RCVD_QTY)    as PODD_SUM_RCVD_QTY                             ");
		sb.append("    FROM 	HMP0POMM, HAC0BASC, HMP0PODD                                        ");
		sb.append("   WHERE 	POMM_COMP_CODE = ?                                                  ");
		parms.add(comp);
		
		if (selDay1.equals(selDay2)){
			sb.append("     AND 	POMM_DATE = ?                                                       ");
			parms.add(selDay1);
		}else{
			sb.append("     AND 	POMM_DATE >= ?                                                       ");
			sb.append("     AND 	POMM_DATE <= ?                                                       ");
			parms.add(selDay1);
			parms.add(selDay2);			
		}
		
		if(!"*".equals(pommBuyer)){
			sb.append("     AND 	POMM_BUYER = ?                                                       ");
			parms.add(pommBuyer);
		}
		
		sb.append("     AND  POMM_COMP_CODE = PODD_COMP_CODE                                        ");
		sb.append("     AND  POMM_DATE 		= PODD_DATE                                                  ");
		sb.append("     AND  POMM_SEQ 		= PODD_SEQ                                                    ");
		sb.append("     AND  POMM_COMP_CODE = HAC0_BASC_COMP_CODE                                ");
		sb.append("     AND  POMM_BUYER 	= HAC0_BASC_LOGIN_ID                                     ");
		sb.append("   GROUP BY POMM_DATE, POMM_SEQ, HAC0_BASC_NOTE, POMM_BUYER, HAC0_BASC_NAME      ");
		sb.append(" ORDER BY HAC0_BASC_NOTE, POMM_BUYER, POMM_DATE							");		
		
		
		
				
		
		JSONArray result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            result = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();           
        } catch (SQLException e) {
            result = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
	}
	
	public JSONArray queryAllCustomer1111111(String comp){
		
		// 依備註欄位(排序用)進行資料排序 
		StringBuffer sb = new StringBuffer();
		sb.append("  SELECT 	HAC0_BASC_LOGIN_ID as POMM_BUYER, 	");
		sb.append("				HAC0_BASC_NOTE, HAC0_BASC_NAME 		");
		sb.append("    FROM 	HAC0BASC                            ");
		sb.append("   WHERE 	HAC0_BASC_COMP_CODE = ?             ");		
		sb.append(" ORDER BY HAC0_BASC_NOTE, POMM_BUYER				");		
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);				
		
		JSONArray result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            result = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();           
        } catch (SQLException e) {
            result = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
	}

	/**
	 * 找出某段時間訂購的客人
	 * @param comp
	 * @param monday
	 * @param sunday
	 * @return
	 */
	public JSONArray queryPeriodCustomer(String comp, String monday, String sunday){
		
		StringBuffer sb = new StringBuffer();
		/*
		sb.append(" SELECT 	POMM_DATE, POMM_SEQ,     			");
		sb.append("			POMM_BUYER, HAC0_BASC_NAME			");
		sb.append("   FROM 	HMP0POMM, HAC0BASC                       ");
		sb.append("  WHERE 	POMM_COMP_CODE = ?                       ");
		sb.append("    AND 	POMM_DATE = ?                            ");
		sb.append("    AND 	POMM_COMP_CODE = HAC0_BASC_COMP_CODE     ");
		sb.append("    AND 	POMM_BUYER = HAC0_BASC_LOGIN_ID          ");
		sb.append(" ORDER BY HAC0_BASC_NAME                         ");
		*/
		
		// 20210725: 按會員帳號(手機號碼)排序資料
		sb.append(" SELECT 	POMM_DATE, POMM_SEQ, POMM_BUYER, HAC0_BASC_NAME,    ");
		sb.append("         SUM(PODD_RCVD_QTY)    PODD_SUM_RCVD_QTY                 ");
		sb.append("   FROM 	HMP0POMM, HAC0BASC, HMP0PODD                        ");
		sb.append("  WHERE 	POMM_COMP_CODE = ?                       ");
		sb.append("    AND 	POMM_DATE between ? and ?                              ");
		sb.append("    AND  POMM_COMP_CODE = PODD_COMP_CODE                     ");
		sb.append("    AND  POMM_DATE = PODD_DATE                               ");
		sb.append("    AND  POMM_SEQ = PODD_SEQ                                 ");
		sb.append("    AND 	POMM_COMP_CODE = HAC0_BASC_COMP_CODE                ");
		sb.append("    AND 	POMM_BUYER = HAC0_BASC_LOGIN_ID                     ");		
		sb.append(" GROUP BY POMM_DATE, POMM_SEQ, POMM_BUYER, HAC0_BASC_NAME    ");
		sb.append(" ORDER BY POMM_BUYER, POMM_DATE                                     ");
		
		
		
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);
		parms.add(monday);
		parms.add(sunday);
		
		JSONArray result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            result = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();           
        } catch (SQLException e) {
            result = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
	}
	
public JSONArray queryWeekCustomer(String comp, String sDate, String eDate){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT 	POMM_BUYER, HAC0_BASC_NAME			");
		sb.append("   FROM 	HMP0POMM, HAC0BASC                       ");
		sb.append("  WHERE 	POMM_COMP_CODE = ?                       ");
		sb.append("    AND 	POMM_DATE between ?  and ?                     ");
		sb.append("    AND 	POMM_COMP_CODE = HAC0_BASC_COMP_CODE     ");
		sb.append("    AND 	POMM_BUYER = HAC0_BASC_LOGIN_ID          ");
		sb.append("GROUP BY POMM_BUYER, HAC0_BASC_NAME ");
		sb.append(" ORDER BY POMM_BUYER, HAC0_BASC_NAME                         ");
		
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);
		parms.add(sDate);
		parms.add(eDate);
		

		
		JSONArray result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            result = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();
            
        } catch (SQLException e) {
            result = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
	}
	
	/**
	 * 查詢客戶當天的採購品項
	 * @param comp
	 * @param day
	 * @param custCode
	 * @return
	 */
	public JSONArray queryPODayList(String comp, String day, String buyer){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT D.PODD_ITEM, D.PODD_ORD_QTY, D.PODD_RCVD_QTY ");
		sb.append("   FROM HMP0PODD D, HMP0POMM M                       ");
		sb.append("  WHERE M.POMM_COMP_CODE = ?              		"); // 1122334455		
		sb.append("    AND M.POMM_DATE = ?                     "); // '20200607'
		sb.append("    AND M.POMM_BUYER = ?                  "); // '0900555555'
		sb.append("    AND M.POMM_COMP_CODE = D.PODD_COMP_CODE          ");
		sb.append("    AND M.POMM_DATE = D.PODD_DATE                    ");
		sb.append("    AND M.POMM_SEQ = D.PODD_SEQ                      ");
		sb.append(" ORDER BY D.PODD_ITEM                                ");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);
		parms.add(day);
		parms.add(buyer);
		
		
		JSONArray ary = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            ary = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();            
        } catch (SQLException e) {
            ary = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return ary;
		
	}
	
	public JSONArray queryPODayList(String comp, String day){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT M.POMM_BUYER, D.PODD_ITEM, D.PODD_ORD_QTY, D.PODD_RCVD_QTY ");
		sb.append("   FROM HMP0PODD D, HMP0POMM M                       ");
		sb.append("  WHERE M.POMM_COMP_CODE = ?              		"); // 1122334455		
		sb.append("    AND M.POMM_DATE = ?                     "); // '20200607'		
		sb.append("    AND M.POMM_COMP_CODE = D.PODD_COMP_CODE          ");
		sb.append("    AND M.POMM_DATE = D.PODD_DATE                    ");
		sb.append("    AND M.POMM_SEQ = D.PODD_SEQ                      ");
		sb.append(" ORDER BY M.POMM_BUYER, D.PODD_ITEM                                ");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);
		parms.add(day);				
		
		JSONArray ary = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            ary = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();            
        } catch (SQLException e) {
            ary = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return ary;
		
	}
	
	/**
	 * 客人當初的訂單數量
	 * @param comp
	 * @param sDate
	 * @param eDate
	 * @param buyer
	 * @return
	 */
	public JSONArray queryWeekQty(String comp, String sDate, String eDate, String buyer){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT D.PODD_ITEM, SUM(D.PODD_ORD_QTY) D.PODD_ORD_QTY,  ");
		sb.append("        sum(D.PODD_RCVD_QTY) D.PODD_RCVD_QTY 		");
		sb.append("   FROM HMP0PODD D, HMP0POMM M                       ");
		sb.append("  WHERE M.POMM_COMP_CODE = ?              "); // 1122334455		
		sb.append("    AND M.POMM_DATE between ? and ?                     "); // '20200607'
		sb.append("    AND M.POMM_BUYER = ?                  "); // '0900555555'
		sb.append("    AND M.POMM_COMP_CODE = D.PODD_COMP_CODE          ");
		sb.append("    AND M.POMM_DATE = D.PODD_DATE                    ");
		sb.append("    AND M.POMM_SEQ = D.PODD_SEQ                      ");
		sb.append(" GROUP BY D.PODD_ITEM");
		sb.append(" ORDER BY D.PODD_ITEM                                ");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);
		parms.add(sDate);
		parms.add(eDate);
		parms.add(buyer);
		
		
		JSONArray ary = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            ary = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();            
        } catch (SQLException e) {
            ary = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return ary;
		
	}
	
	
	
	
	/**
	 * 查詢某天po單(by使用者)
	 * @param comp
	 * @param poBuyer
	 * @param poDate
	 * @param poSeq
	 * @return
	 */
	public JSONArray queryPOList(String comp, String poBuyer, String poDate, String poSeq){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT POMM_REQ_DATE, PODD_DATE, PODD_SEQ, PODD_ITEM, ITEM_NAME, ITEM_ABBR, ");
		sb.append("       PODD_ORD_QTY, PODD_RCVD_QTY, PODD_NOTE, PODD_CLOS_MARK     ");
		sb.append(" FROM HMP0POMM, HMP0PODD, HMP0ITEM                                ");
		sb.append(" WHERE POMM_COMP_CODE = ?                              ");		
		sb.append("   AND POMM_BUYER = ?                                  ");
		sb.append("   AND POMM_DATE = ?                                     ");
		sb.append("   AND POMM_SEQ = ?                                     ");
		sb.append("   AND POMM_COMP_CODE = PODD_COMP_CODE                            ");
		sb.append("   AND POMM_DATE = PODD_DATE                                      ");
		sb.append("   AND POMM_SEQ = PODD_SEQ                                        ");
		sb.append("   AND PODD_COMP_CODE = ITEM_COMP                                 ");
		sb.append("   AND PODD_ITEM = ITEM_CODE                                      ");
		sb.append(" ORDER BY ITEM_TYPE, ITEM_SORT, PODD_ITEM, POMM_REQ_DATE, PODD_SEQ                      ");
		
		
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);		
		parms.add(poBuyer);
		parms.add(poDate);
		parms.add(poSeq);
		
		//System.out.println("Comp: " + comp);		
		//System.out.println("buyer: " + poBuyer);
		//System.out.println("poDate: " + poDate);
		//System.out.println("poDate: " + poSeq);
		
		JSONArray result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            result = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();
            
        } catch (SQLException e) {
            result = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
	}
	
	public JSONArray queryPOList(String comp, String poBuyer, String poDate){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT POMM_REQ_DATE, PODD_DATE, PODD_SEQ, PODD_ITEM, ITEM_NAME, ITEM_ABBR, ");
		sb.append("       PODD_ORD_QTY, PODD_RCVD_QTY, PODD_NOTE, PODD_CLOS_MARK     ");
		sb.append(" FROM HMP0POMM, HMP0PODD, HMP0ITEM                                ");
		sb.append(" WHERE POMM_COMP_CODE = ?                              ");		
		sb.append("   AND POMM_BUYER = ?                                  ");
		sb.append("   AND POMM_DATE = ?                                     ");		
		sb.append("   AND POMM_COMP_CODE = PODD_COMP_CODE                            ");
		sb.append("   AND POMM_DATE = PODD_DATE                                      ");
		sb.append("   AND POMM_SEQ = PODD_SEQ                                        ");
		sb.append("   AND PODD_COMP_CODE = ITEM_COMP                                 ");
		sb.append("   AND PODD_ITEM = ITEM_CODE                                      ");
		sb.append(" ORDER BY ITEM_TYPE, ITEM_SORT, PODD_ITEM, POMM_REQ_DATE, PODD_SEQ                      ");
		
		
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);		
		parms.add(poBuyer);
		parms.add(poDate);
		
		
		
		JSONArray result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            result = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();
            
        } catch (SQLException e) {
            result = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
	}
	
	/**
	 * 查買家某段日期區間的每種物料的採購數量及金額
	 * @param comp
	 * @param poBuyer
	 * @param sDate
	 * @param eDate
	 * @return``````````````
	 */
	@SuppressWarnings("unchecked")
	public JSONArray queryWeekPOList(String comp, String poBuyer, String sDate, String eDate){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" Select m.pomm_date, m.pomm_seq, d.podd_item, i.item_name, i.item_abbr,  ");
		sb.append("        d.podd_ord_qty, d.podd_rcvd_qty, d.podd_unit, d.podd_unit_price,	");
		sb.append("        d.podd_rcvd_qty * nz(d.podd_unit_price,1) podd_Amt,				");
		sb.append("		   right(POMM_DATE,4) AS PO_DATE,									");
		sb.append("		   right(POMM_DATE,4) AS PO_DATE1,									");
		sb.append("        d.podd_uuid, d.podd_mod_user, d.podd_mod_date 				    ");
		//sb.append("		   IIf(IsNull(d.podd_mod_date), '', FORMAT(d.podd_mod_date, 'hh:nn mm/dd')) podd_mod_date_str		");
		/*
		sb.append("		   IIf(IsNull(d.podd_mod_date), '', FORMAT(d.podd_mod_date, 'yyyy-mm-dd hh:nn')) podd_mod_date		");
		sb.append("		   IIf(IsNull(d.podd_mod_date), '', Format(d.podd_mod_date, 'yyyy-mm-dd')) podd_mod_date ");
		*/
		sb.append("  From HMP0POMM m, HMP0PODD d, HMP0ITEM i                                ");
		sb.append(" Where m.pomm_comp_code = ?                                              ");
		sb.append("   and m.pomm_buyer = ?                                         			");
		sb.append("   and m.pomm_date between ? and ?                       				");
		sb.append("   and m.pomm_comp_code = d.podd_comp_code                               ");
		sb.append("   and m.pomm_date = d.podd_date                                         ");
		sb.append("   and m.pomm_seq = d.podd_seq                                           ");
		sb.append("   and d.podd_comp_code = i.item_comp                                    ");
		sb.append("   and d.podd_item = i.item_code                                         ");
		sb.append(" order by m.pomm_date, i.item_type, i.item_sort, I.ITEM_CODE             ");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);		
		parms.add(poBuyer);
		parms.add(sDate);
		parms.add(eDate);
		

		/*
		System.out.println("!!Comp: " + comp);		
		System.out.println("!!buyer: " + poBuyer);
		System.out.println("!!sDate: " + sDate);
		System.out.println("!!eDate: " + eDate);		
		System.out.println("SQL: " + sb.toString());
		*/

		
		JSONArray result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            result = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();
            result.forEach(item -> {
                JSONObject jo = (JSONObject) item;                
                jo.put("weekDay", UTIL_String.getWeekOfDate(jo.getString("pommDate")));
                jo.put("weekDay1", UTIL_String.getWeekOfDate(jo.getString("pommDate")));
            });            
            
            // System.out.println("count: " + result.size());
        } catch (SQLException e) {
            result = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
	}
	
	public JSONArray queryDailyPOList(String comp, String poBuyer, String poDate, String poSeq){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" Select m.pomm_date, m.pomm_seq, d.podd_item, i.item_name, i.item_abbr,  ");
		sb.append("        d.podd_ord_qty, d.podd_rcvd_qty, d.podd_unit, d.podd_unit_price,	");
		sb.append("        d.podd_rcvd_qty * nz(d.podd_unit_price,1) podd_Amt,				");
		sb.append("		   right(POMM_DATE,4) AS PO_DATE,									");
		sb.append("		   right(POMM_DATE,4) AS PO_DATE1,									");
		sb.append("        d.podd_uuid, d.podd_mod_user, d.podd_mod_date 				    ");
		//sb.append("		   IIf(IsNull(d.podd_mod_date), '', FORMAT(d.podd_mod_date, 'hh:nn mm/dd')) podd_mod_date_str		");
		/*
		sb.append("		   IIf(IsNull(d.podd_mod_date), '', FORMAT(d.podd_mod_date, 'yyyy-mm-dd hh:nn')) podd_mod_date		");
		sb.append("		   IIf(IsNull(d.podd_mod_date), '', Format(d.podd_mod_date, 'yyyy-mm-dd')) podd_mod_date ");
		*/
		sb.append("  From HMP0POMM m, HMP0PODD d, HMP0ITEM i                                ");
		sb.append(" Where m.pomm_comp_code = ?                                              ");
		sb.append("   and m.pomm_buyer = ?                                         			");
		sb.append("   and m.pomm_date = ? 	                       							");
		sb.append("   and m.pomm_seq = ? 	                       							");
		sb.append("   and m.pomm_comp_code = d.podd_comp_code                               ");
		sb.append("   and m.pomm_date = d.podd_date                                         ");
		sb.append("   and m.pomm_seq = d.podd_seq                                           ");
		sb.append("   and d.podd_comp_code = i.item_comp                                    ");
		sb.append("   and d.podd_item = i.item_code                                         ");
		sb.append(" order by m.pomm_date, i.item_type, i.item_sort, I.ITEM_CODE             ");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);		
		parms.add(poBuyer);
		parms.add(poDate);
		parms.add(poSeq);
		
		JSONArray result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            result = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();
            result.forEach(item -> {
                JSONObject jo = (JSONObject) item;                
                jo.put("weekDay", UTIL_String.getWeekOfDate(jo.getString("pommDate")));
                jo.put("weekDay1", UTIL_String.getWeekOfDate(jo.getString("pommDate")));
            });            
            
            // System.out.println("count: " + result.size());
        } catch (SQLException e) {
            result = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
	}
	
	/**
	 * 20230531: 追加訂單
	 */	
	@SuppressWarnings("unchecked")
	public JSONArray queryAppendPOList(String comp, String poBuyer, String poDate){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" Select m.pomm_date, m.pomm_seq, d.podd_item, i.item_name, i.item_abbr,  ");
		sb.append("        d.podd_ord_qty, d.podd_rcvd_qty, d.podd_unit, d.podd_unit_price,	");
		sb.append("        d.podd_rcvd_qty * nz(d.podd_unit_price,1) podd_Amt,				");
		sb.append("		   right(POMM_DATE,4) AS PO_DATE,									");
		sb.append("		   right(POMM_DATE,4) AS PO_DATE1,									");
		sb.append("        d.podd_uuid,  				    ");				
		sb.append("  From HMP0POMM m, HMP0PODD d, HMP0ITEM i                                ");
		sb.append(" Where m.pomm_comp_code = ?                                              ");
		sb.append("   and m.pomm_buyer = ?                                         			");
		sb.append("   and m.pomm_date between ? and ?                       				");
		sb.append("   and m.pomm_comp_code = d.podd_comp_code                               ");
		sb.append("   and m.pomm_date = d.podd_date                                         ");
		sb.append("   and m.pomm_seq = d.podd_seq                                           ");
		sb.append("   and d.podd_comp_code = i.item_comp                                    ");
		sb.append("   and d.podd_item = i.item_code                                         ");
		sb.append(" order by m.pomm_date, i.item_type, i.item_sort, I.ITEM_CODE             ");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);		
		parms.add(poBuyer);
		parms.add(poDate);		
		

		/*
		System.out.println("!!Comp: " + comp);		
		System.out.println("!!buyer: " + poBuyer);
		System.out.println("!!sDate: " + sDate);
		System.out.println("!!eDate: " + eDate);		
		System.out.println("SQL: " + sb.toString());
		*/

		
		JSONArray result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            result = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();
            result.forEach(item -> {
                JSONObject jo = (JSONObject) item;                
                jo.put("weekDay", UTIL_String.getWeekOfDate(jo.getString("pommDate")));
                jo.put("weekDay1", UTIL_String.getWeekOfDate(jo.getString("pommDate")));
            });            
            
            // System.out.println("count: " + result.size());
        } catch (SQLException e) {
            result = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
	}
	
	/**
	 * 預訂及出貨統計
	 * @param comp
	 * @param poDate1
	 * @param poDate2
	 * @return
	 */
	public JSONArray query670Report(String comp, String poDate1, String poDate2){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT  I.ITEM_NAME, P.PODD_ITEM, I.ITEM_ABBR,                            ");
		sb.append("         SUM(P.PODD_ORD_QTY) as PODD_ORD_QTY_SUM,                          ");
		sb.append("         SUM(P.PODD_RCVD_QTY) as PODD_RCVD_QTY_SUM,                        ");
		sb.append("         (SUM(P.PODD_RCVD_QTY) - SUM(P.PODD_ORD_QTY)) as PODD_DIFF_QTY     ");
		sb.append("   FROM  HMP0PODD as P, HMP0ITEM as I                                      ");
		sb.append("  WHERE  P.PODD_COMP_CODE = ?                                   ");
		sb.append("    AND  P.PODD_DATE BETWEEN ? AND ?                     ");
		sb.append("    AND  P.PODD_COMP_CODE = I.ITEM_COMP                                    ");
		sb.append("    AND  P.PODD_ITEM = I.ITEM_CODE                                         ");
		sb.append("  GROUP  BY I.ITEM_NAME, P.PODD_ITEM, I.ITEM_ABBR                                       ");
		//sb.append(" HAVING  (SUM(P.PODD_ORD_QTY) - SUM(P.PODD_RCVD_QTY)) <> 0                 ");
		sb.append(" ORDER BY I.ITEM_NAME, P.PODD_ITEM, I.ITEM_ABBR                                                       		");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);
		parms.add(poDate1);
		parms.add(poDate2);
		
		
		//System.out.println("Comp: " + comp);
		//System.out.println("poDate1: " + poDate1);
		//System.out.println("poDate2: " + poDate2);
		
		
		JSONArray result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            result = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();
            
        } catch (SQLException e) {
        	logger.error(String.format("parms: %s-%s-%s", comp, poDate1, poDate2));
        	logger.error(sb.toString());
            result = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
	}
	
	
	/**
	 * 
	 * @param comp
	 * @param loginID
	 * @param poDate1
	 * @param poDate2
	 * @return
	 */
	public JSONArray queryWeekPODetailsAmt(String comp, String loginID, String poDate1, String poDate2){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT  D.PODD_ITEM, I.ITEM_NAME, I.ITEM_ABBR,                                        ");
		sb.append("         SUM(D.PODD_ORD_QTY) as PODD_ORD_QTY_SUM,                          ");
		sb.append("         SUM(D.PODD_RCVD_QTY) as PODD_RCVD_QTY_SUM,                        ");
		sb.append("         (SUM(D.PODD_RCVD_QTY) - SUM(D.PODD_ORD_QTY)) as PODD_DIFF_QTY    ");		
		sb.append("  From HMP0POMM m, HMP0PODD d, HMP0ITEM i                                ");
		sb.append(" Where m.pomm_comp_code = ?                                              ");
		sb.append("   and m.pomm_buyer = ?                                         			");
		sb.append("   and m.pomm_date between ? and ?                       				");
		sb.append("   and m.pomm_comp_code = d.podd_comp_code                               ");
		sb.append("   and m.pomm_date = d.podd_date                                         ");
		sb.append("   and m.pomm_seq = d.podd_seq                                           ");
		sb.append("   and d.podd_comp_code = i.item_comp                                    ");
		sb.append("   and d.podd_item = i.item_code                                         ");
		sb.append("  GROUP  BY P.PODD_ITEM, I.ITEM_NAME                                       ");
		//sb.append(" HAVING  (SUM(P.PODD_ORD_QTY) - SUM(P.PODD_RCVD_QTY)) <> 0                 ");
		sb.append(" ORDER BY 1                                                        		");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);
		parms.add(loginID);
		parms.add(poDate1);
		parms.add(poDate2);
		
		
		//System.out.println("Comp: " + comp);
		//System.out.println("poDate1: " + poDate1);
		//System.out.println("poDate2: " + poDate2);
		
		
		JSONArray result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            result = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();
            
        } catch (SQLException e) {
            result = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
	}
	
	/**
	 * 查詢每月訂購報表
	 * @param comp
	 * @param qryMonth
	 * @return
	 */
	public JSONArray queryMonthPO(String comp, String qryMonth){
		
		
		StringBuffer sb = new StringBuffer();		
		sb.append(" SELECT 	M.POMM_BUYER, M.POMM_DATE            ");
		sb.append(" 		,SUM(NZ(PODD_RCVD_QTY,0) * NZ(PODD_UNIT_PRICE,0))  ");
		sb.append(" 		as 		POMM_TOTAL_AMT                            ");
		sb.append("   FROM 	HMP0POMM M, HMP0PODD D                      ");  
		sb.append("  WHERE 	M.POMM_COMP_CODE = ?                     ");
		sb.append("    AND 	LEFT(M.POMM_DATE,6) =  ?                            ");
		sb.append("    AND  M.POMM_COMP_CODE = D.PODD_COMP_CODE                   ");  
		sb.append("    AND  M.POMM_DATE = D.PODD_DATE                             ");  
		sb.append("    AND  M.POMM_SEQ = D.PODD_SEQ                               "); 		  
		sb.append(" GROUP BY M.POMM_BUYER, M.POMM_DATE            ");
		sb.append(" ORDER BY M.POMM_BUYER, M.POMM_DATE            ");
		
		ArrayList<String> parms = new ArrayList<String>();
		//qryMonth = qryMonth + "*";
		parms.add(comp);
		parms.add(qryMonth);
		
		//System.out.println("qqqComp/QryMonth: " + comp + "/" + qryMonth);
		
		
		
		JSONArray result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            System.out.println("SQL: " + sb.toString());
            RowDataList rdl = pre.getRowDataList(sb.toString(), parms.toArray());
            System.out.println("queryMonthPO: " + rdl.size());
            result = rdl.toJSONArray();         
        } catch (SQLException e) {
            result = null;
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
		
	}
	
	/**
	 * 20220713: 依照會員排序(note)排列資料
	 * @param comp
	 * @param qryMonth
	 * @return
	 */
	public JSONArray queryMonthCust(String comp, String qryMonth){
		
		
		StringBuffer sb = new StringBuffer();		
		sb.append(" SELECT 	nz(HAC0_BASC_NOTE, 'zzzzzz') as hac0_basc_note, POMM_BUYER, HAC0_BASC_NAME 			");
		sb.append("   FROM 	HMP0POMM, HAC0BASC, HMP0PODD            ");  
		sb.append("  WHERE 	POMM_COMP_CODE = ?                      ");
		sb.append("    AND 	LEFT(POMM_DATE,6) =  ?                            ");
		sb.append("    AND  POMM_COMP_CODE = PODD_COMP_CODE                   ");  
		sb.append("    AND  POMM_DATE = PODD_DATE                             ");  
		sb.append("    AND  POMM_SEQ = PODD_SEQ                               ");  
		sb.append("    AND 	POMM_COMP_CODE = HAC0_BASC_COMP_CODE              ");  
		sb.append("    AND 	POMM_BUYER = HAC0_BASC_LOGIN_ID                   ");  
		sb.append(" GROUP BY nz(HAC0_BASC_NOTE, 'zzzzzz'), POMM_BUYER, HAC0_BASC_NAME                       ");
		sb.append(" ORDER BY nz(HAC0_BASC_NOTE, 'zzzzzz'), POMM_BUYER, HAC0_BASC_NAME                        ");
		
		ArrayList<String> parms = new ArrayList<String>();
		//qryMonth = qryMonth + "*";
		parms.add(comp);
		parms.add(qryMonth);
		
		JSONArray result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            RowDataList rdl = pre.getRowDataList(sb.toString(), parms.toArray());
            if (null != rdl){
            	result = rdl.toJSONArray();
            }else{
            	result = new JSONArray();
            }            
        } catch (SQLException e) {
            result = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
		
	}
	
	/**
	 * 每日訂單管理的列表
	 * @param comp
	 * @param loginID
	 * @param poDate1
	 * @param poDate2
	 * @return
	 */
	public JSONArray queryDailyPO(String comp, String poDate1, String poDate2){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT  M.POMM_BUYER, M.POMM_DATE, M.POMM_SEQ, A.HAC0_BASC_NAME, M.POMM_CLOS_MARK,  A.HAC0_BASC_NOTE,      ");
		sb.append("         SUM(D.PODD_ORD_QTY * D.PODD_UNIT_PRICE) AS PO_ORD_AMT,   ");
		sb.append("         SUM(D.PODD_RCVD_QTY * D.PODD_UNIT_PRICE) AS PO_RCVD_AMT  ");
		sb.append("   FROM  HMP0PODD D, HMP0POMM M, HAC0BASC A                       ");
		sb.append(" WHERE M.POMM_COMP_CODE = ?                                       ");
		//sb.append("   AND M.POMM_DATE BETWEEN ? AND ?                                ");
		sb.append("   AND M.POMM_DATE = ?                                 ");
		sb.append("   AND M.POMM_COMP_CODE = A.HAC0_BASC_COMP_CODE ");
		sb.append("   AND M.POMM_BUYER = A.HAC0_BASC_LOGIN_ID                        ");
		sb.append("   AND M.POMM_COMP_CODE = D.PODD_COMP_CODE                        ");
		sb.append("   AND M.POMM_DATE = D.PODD_DATE                                  ");
		sb.append("   AND M.POMM_SEQ = D.PODD_SEQ                                    ");
		sb.append(" GROUP BY M.POMM_BUYER, M.POMM_DATE, M.POMM_SEQ, A.HAC0_BASC_NAME, M.POMM_CLOS_MARK, A.HAC0_BASC_NOTE        ");
		sb.append(" ORDER BY A.HAC0_BASC_NOTE ");		
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);		
		parms.add(poDate1);
		//parms.add(poDate2);
		
		JSONArray result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            result = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();
            
        } catch (SQLException e) {
            result = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
	}
	
	public JSONObject queryDailyItemQtySum(String comp, String itemCode, String poDate1, String poDate2){
		
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT  SUM(D.PODD_ORD_QTY) AS PO_ORD_QTY_SUM,   ");		         
		sb.append("         SUM(D.PODD_RCVD_QTY) AS PO_RCVD_QTY_SUM  ");
		sb.append("   FROM  HMP0PODD D, HMP0POMM M                       ");
		sb.append(" WHERE M.POMM_COMP_CODE = ?                                       ");
		sb.append("   AND M.POMM_DATE between ? and ?                                 ");				
		sb.append("   AND M.POMM_COMP_CODE = D.PODD_COMP_CODE                        ");
		sb.append("   AND M.POMM_DATE = D.PODD_DATE                                  ");
		sb.append("   AND M.POMM_SEQ = D.PODD_SEQ                                    ");
		sb.append("   AND D.PODD_ITEM = ? ");
		sb.append(" GROUP BY D.PODD_ITEM        ");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);			
		parms.add(poDate1);
		parms.add(poDate2);
		parms.add(itemCode);
		
		JSONObject sumQty = null;
		try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();          
            RowData rd = pre.getARowData(sb.toString(), parms.toArray());
            if (null != rd){
            	sumQty = rd.toJSONObject();
            }else{
            	sumQty = new JSONObject();
            	sumQty.put("poOrdQtySum", "0");
            	sumQty.put("poRcvdQtySum", "0");
            }
             
            
        } catch (SQLException e) {
        	sumQty = new JSONObject();
        	sumQty.put("poOrdQtySum", "0");
        	sumQty.put("poRcvdQtySum", "0");
        	logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
		
		return sumQty;
	}
	
public JSONObject queryWeekItemQtySum(String comp, String itemCode, String sDate, String eDate){
		
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT  SUM(D.PODD_ORD_QTY) AS PO_ORD_QTY_SUM,   ");		         
		sb.append("         SUM(D.PODD_RCVD_QTY) AS PO_RCVD_QTY_SUM  ");
		sb.append("   FROM  HMP0PODD D, HMP0POMM M                       ");
		sb.append(" WHERE M.POMM_COMP_CODE = ?                                       ");
		sb.append("   AND M.POMM_DATE between ? and ?                                ");				
		sb.append("   AND M.POMM_COMP_CODE = D.PODD_COMP_CODE                        ");
		sb.append("   AND M.POMM_DATE = D.PODD_DATE                                  ");
		sb.append("   AND M.POMM_SEQ = D.PODD_SEQ                                    ");
		sb.append("   AND D.PODD_ITEM = ? ");
		sb.append(" GROUP BY D.PODD_ITEM        ");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);			
		parms.add(sDate);
		parms.add(eDate);
		parms.add(itemCode);
		
		JSONObject sumQty = null;
		try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();          
            RowData rd = pre.getARowData(sb.toString(), parms.toArray());
            if (null != rd){
            	sumQty = rd.toJSONObject();
            }else{
            	sumQty = new JSONObject();
            	sumQty.put("poOrdQtySum", "0");
            	sumQty.put("poRcvdQtySum", "0");
            }
             
            
        } catch (SQLException e) {
        	sumQty = new JSONObject();
        	sumQty.put("poOrdQtySum", "0");
        	sumQty.put("poRcvdQtySum", "0");
          	throw new DataAccessException(e);
        }    	
		
		return sumQty;
	}
	

	/**
	 * 
	 * @param comp
	 * @param loginID
	 * @param poDate1
	 * @param poDate2
	 * @return
	 */
	public JSONObject queryWeekPOAmt(String comp, String loginID, String poDate1, String poDate2){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT  SUM(d.PODD_ORD_QTY) as PODD_ORD_QTY_SUM,                          ");
		sb.append("         SUM(d.PODD_RCVD_QTY) as PODD_RCVD_QTY_SUM,                        ");
		sb.append("         SUM(d.PODD_RCVD_QTY * d.PODD_UNIT_PRICE) as PODD_RCVD_AMT_SUM   ");		
		sb.append("  From HMP0POMM m, HMP0PODD d, HMP0ITEM i                                ");
		sb.append(" Where m.pomm_comp_code = ?                                              ");
		sb.append("   and m.pomm_buyer = ?                                         			");
		sb.append("   and m.pomm_date between ? and ?                       				");
		sb.append("   and m.pomm_comp_code = d.podd_comp_code                               ");
		sb.append("   and m.pomm_date = d.podd_date                                         ");
		sb.append("   and m.pomm_seq = d.podd_seq                                           ");
		sb.append("   and d.podd_comp_code = i.item_comp                                    ");
		sb.append("   and d.podd_item = i.item_code                                         ");		
		//sb.append(" HAVING  (SUM(P.PODD_ORD_QTY) - SUM(P.PODD_RCVD_QTY)) <> 0                 ");
		sb.append(" ORDER BY 1                                                        		");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);
		parms.add(loginID);
		parms.add(poDate1);
		parms.add(poDate2);
		

		
		JSONObject result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            RowData rd = pre.getARowData(sb.toString(), parms.toArray());
            if (null != rd){
            	result = rd.toJSONObject();
            }else{
            	result = new JSONObject();
            	result.put("poddRcvdAmtSum", "0");
            }
        } catch (SQLException e) {
            result = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
	}
	
	
	/**
	 * 660
	 * 查詢某天訂單訂購、出貨總金額
	 * @param comp
	 * @param poDate
	 * @return
	 */
	public JSONObject queryDailyPOStatistic(String comp, String poDate){
		
		StringBuffer sb = new StringBuffer();
		/*
		 * 若是沒勾 pomm，預訂單總金額跟出貨總金額對不起來
		sb.append(" SELECT SUM(NZ(PODD_ORD_QTY,0)  * PODD_UNIT_PRICE) AS ORD_AMT_SUM,          "); // 預訂單總金額
		sb.append("        SUM(NZ(PODD_RCVD_QTY,0) * PODD_UNIT_PRICE) AS RCVD_AMT_SUM,  "); // 出貨金總額
		sb.append("        ROUND((SUM(NZ(PODD_RCVD_QTY,0) * PODD_UNIT_PRICE) / SUM(PODD_ORD_QTY * PODD_UNIT_PRICE))* 100, 0) as DONE_PER,	");
		sb.append("        (SUM(PODD_ORD_QTY * PODD_UNIT_PRICE) - SUM(NZ(PODD_RCVD_QTY,0) * PODD_UNIT_PRICE)) as DIFF_AMT,	");
		sb.append("        PODD_DATE                                                    ");
		sb.append("   FROM HMP0PODD                                                     ");
		sb.append("  WHERE PODD_COMP_CODE = ?                                			");
		sb.append("    AND PODD_DATE = ?                                       			");
		sb.append(" GROUP BY PODD_DATE                                                  ");
		*/
		sb.append("  SELECT SUM(NZ(PODD_ORD_QTY,0)  * PODD_UNIT_PRICE) AS ORD_AMT_SUM,                                                     ");
		sb.append("         SUM(NZ(PODD_RCVD_QTY,0) * PODD_UNIT_PRICE) AS RCVD_AMT_SUM,                                                    ");
		sb.append("         ROUND((SUM(NZ(PODD_RCVD_QTY,0) * PODD_UNIT_PRICE) / SUM(PODD_ORD_QTY * PODD_UNIT_PRICE))* 100, 0) as DONE_PER, ");
		sb.append("         (SUM(PODD_ORD_QTY * PODD_UNIT_PRICE) - SUM(NZ(PODD_RCVD_QTY,0) * PODD_UNIT_PRICE)) as DIFF_AMT,	               ");
		sb.append("         PODD_DATE                                                                                                      ");
		sb.append("    FROM HMP0PODD, HMP0POMM                                                                                             ");
		sb.append("   WHERE PODD_COMP_CODE = ?                                                                                             ");
		sb.append("     AND PODD_DATE = ?                                                                                                  ");
		sb.append("     AND POMM_COMP_CODE = PODD_COMP_CODE                                                                                ");
		sb.append("     AND POMM_DATE = PODD_DATE                                                                                          ");
		sb.append("     AND POMM_SEQ = PODD_SEQ                                                                                            ");
		sb.append(" GROUP BY PODD_DATE                                  	                                                               ");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);		
		parms.add(poDate);
		
		JSONObject result = null;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            RowData rd = pre.getARowData(sb.toString(), parms.toArray());
            if (null != rd){
            	result = rd.toJSONObject();
            }else{
            	result = new JSONObject();
            	result.put("poDate", poDate);
            	result.put("ordAmtSum", "0");
            	result.put("rcvdAmtSum", "0");
            	result.put("diffAmt", "0");            	
            	result.put("donePer", "0");
            }
        } catch (SQLException e) {
            result = null;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
	}
	
    /**
     * 
     * @param jo
     * @return
     */
    public int updateRcvPO(String comp, String poDate, String poSeq, String poItem, String rcvQty, String itemPrice, String modUser){
    	
    	//System.out.println("updateRcvPO");
    	
        StringBuffer sb = new StringBuffer();            
        sb.append(" UPDATE HMP0PODD               	");
        sb.append("        SET PODD_RCVD_QTY = ?,   ");
        sb.append("            PODD_UNIT_PRICE = ?,	");
        sb.append("            PODD_MOD_USER = ?,	");
        sb.append("            PODD_MOD_DATE = NOW()");
        sb.append(" WHERE PODD_COMP_CODE = ?       	");
        sb.append("   AND PODD_DATE = ?            	");
        sb.append("   AND PODD_SEQ  = ?            	");
        sb.append("   AND PODD_ITEM = ?            	");
        
        ArrayList<String> parms = new ArrayList<String>();
        parms.add(rcvQty);
        parms.add(itemPrice);
        parms.add(modUser);
		parms.add(comp);
		parms.add(poDate);
		parms.add(poSeq);
		parms.add(poItem);				
        
        int cnt = 0;        
        try {      
        	//System.out.println(sb.toString());
        	        	
            PrepareExecuteSql pre = getPrepareExecuteSql();
            System.out.println(String.format("UpdateRcvPO-PO#: %s-%s, Item-Price: %s-%s, Qty:%s, Cnt: %d", poDate, poSeq, poItem, itemPrice, rcvQty, cnt));
            cnt = pre.updateSql(sb.toString(), parms.toArray());            
        } catch (SQLException e) {        	
            cnt = 0;
            logger.error(e.toString());
          	//throw new DataAccessException(e);
        }
    	
    	return cnt;
    	
    }
    
    public int updateRcvPO(String comp, String poddUUID, String rcvQty, String unitPrice, String modUser){
    	
    	//System.out.println("updateRcvPO");
    	
        StringBuffer sb = new StringBuffer();            
        sb.append(" UPDATE HMP0PODD               	");
        sb.append("        SET PODD_RCVD_QTY = ?,   ");
        sb.append("            PODD_UNIT_PRICE = ?,	");
        sb.append("            PODD_MOD_USER = ?,	");
        sb.append("            PODD_MOD_DATE = NOW()");
        sb.append(" WHERE PODD_COMP_CODE = ?       	");
        sb.append("   AND PODD_UUID = ?            	");        
        
        ArrayList<String> parms = new ArrayList<String>();
        parms.add(rcvQty);        
        parms.add(unitPrice);
        parms.add(modUser);
		parms.add(comp);		
		parms.add(poddUUID);
		
        
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.updateSql(sb.toString(), parms.toArray());            
        } catch (SQLException e) {        	
            cnt = 0;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }
    	
    	return cnt;
    	
    }
    
    public int addRcvPO(String comp, String poDate, String poSeq, String poItem, String rcvQty, String itemPrice, String loginID){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" INSERT INTO HMP0PODD(                                ");
    	sb.append(" 	PODD_COMP_CODE, PODD_DATE, PODD_SEQ, PODD_ITEM,  ");
    	sb.append(" 	PODD_ORD_QTY, PODD_RCVD_QTY, PODD_UNIT_PRICE, PODD_MOD_USER, PODD_MOD_DATE)    ");
    	sb.append(" VALUES(?, ?, ?, ?, 0, ?, ?, ?, now())                             ");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(comp);
    	parms.add(poDate);
    	parms.add(poSeq);
    	parms.add(poItem);
    	parms.add(rcvQty);
    	parms.add(itemPrice);
    	parms.add(loginID);
    	
    	//System.out.println(String.format("%s-%s-%s-%s-%s-%s: ", comp, poDate, poSeq, poItem, rcvQty, itemPrice));
        
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.updateSql(sb.toString(), parms.toArray());    
            
        } catch (SQLException e) {        	
            cnt = 0;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }
        
        System.out.println("PODD INSERT COUNT: " + cnt);
    	
    	return cnt;
    	
    }
    
   
    
}