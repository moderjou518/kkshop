package com.hms.web.hbd1.dao;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.impl.Log4JLogger;
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

public class HMI0TRNS_DAO extends GenericDao {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	public HMI0TRNS_DAO(Connection conn) throws SQLException{
		super.setConnection(conn);
	}
    
	public JSONArray queryTransList(String compCode, String trnsDate){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" SELECT D.TRNS_UUID, D.TRNS_COMP_CODE, D.TRNS_DATE, D.TRNS_SEQ, D.CARD_ID, ");
    	sb.append("        D.TRNS_ITEM, D.TRNS_QTY, D.TRNS_UNIT_PRICE, D.TRNS_AMT,  D.STORE,  ");
    	sb.append("        D.INVOICE_NO, D.MOD_USER, D.MOD_TIME, I.ITEM_NAME, I.ITEM_ABBR,  	");
    	sb.append("        D.VEND_NO, D.TRNS_NOTE   											");
    	sb.append("   FROM HMI0TRNSD D, HMP0ITEM I                                            	");
    	sb.append("  WHERE D.TRNS_COMP_CODE = ?                                               	");
    	sb.append("    AND D.TRNS_DATE = ?                                                    	");
    	sb.append("    AND D.TRNS_COMP_CODE = I.ITEM_COMP                                     	");
    	sb.append("    AND D.TRNS_ITEM = I.ITEM_CODE                                          	");
    	sb.append("ORDER BY D.TRNS_UUID	");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(compCode);        
    	parms.add(trnsDate);
    	JSONArray ary = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
            RowDataList rdl = pre.getRowDataList(sb.toString(), parms.toArray());
        	if (rdl != null){
        		ary = rdl.toJSONArray();
        		ary.forEach(item -> {
                    JSONObject jo = (JSONObject) item;                
                    jo.put("weekDay", UTIL_String.getWeekOfDate(jo.getString("trnsDate")));
                });  
        	}
        	
        	
        	return ary;               
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }
	
	public boolean addOneData(JSONObject jo){
    	
		//System.out.println("addOneData: " + jo.toString());
		
    	StringBuffer sb = new StringBuffer();
    	sb.append(" INSERT INTO HMI0TRNSD(                                    ");
    	sb.append(" TRNS_COMP_CODE, TRNS_DATE, CARD_ID,  ");
    	sb.append(" TRNS_ITEM, TRNS_QTY, TRNS_AMT,    ");
    	sb.append(" VEND_NO, TRNS_NOTE, MOD_USER, MOD_TIME)                           ");
    	sb.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, now())             ");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(jo.getString("COMP_CODE"));
    	parms.add(jo.getString("txtTrnsDate"));
    	//parms.add(jo.getString("parmTrnsSeq"));
    	parms.add(jo.getString("dlCard_ID"));
    	//parms.add(jo.getString("txtStore"));
    	parms.add(jo.getString("dlItemCode"));
    	parms.add(jo.getString("txtTrnsQty"));
    	//parms.add(jo.getString("txtTrnsUnitPrice"));
    	parms.add(jo.getString("txtTrnsAmt"));
    	parms.add(jo.getString("txtVendNo"));
    	parms.add(jo.getString("txtTrnsNote"));
    	parms.add(jo.getString("loginID"));
    	
    	
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
            int i = pre.updateSql(sb.toString(), parms.toArray());
        	return (i > 0); 
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }
	
	public boolean saveOneData(JSONObject jo){
    	
		//System.out.println("saveOneData: " + jo.toString());
		
    	StringBuffer sb = new StringBuffer();
    	sb.append(" UPDATE HMI0TRNSD             ");
    	sb.append("    SET TRNS_DATE = ?,        ");
    	sb.append("        CARD_ID = ?,          ");
    	sb.append(" 	   TRNS_ITEM = ?,        ");
    	sb.append(" 	   TRNS_QTY = ?,         ");
    	sb.append(" 	   TRNS_AMT = ?,         ");
    	sb.append(" 	   VEND_NO = ?,         ");
    	sb.append(" 	   TRNS_NOTE = ?,         ");
    	sb.append("        MOD_USER = ?,         ");
    	sb.append(" 	   MOD_TIME = NOW()      ");
    	sb.append("  WHERE TRNS_COMP_CODE = ?    ");
    	sb.append("    AND TRNS_UUID = ?         ");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(jo.getString("txtTrnsDate"));
    	parms.add(jo.getString("dlCard_ID"));
    	parms.add(jo.getString("dlItemCode"));
    	parms.add(jo.getString("txtTrnsQty"));
    	parms.add(jo.getString("txtTrnsAmt"));
    	parms.add(jo.getString("txtVendNo"));
    	parms.add(jo.getString("txtTrnsNote"));
    	parms.add(jo.getString("loginID"));    	
    	parms.add(jo.getString("COMP_CODE"));
    	parms.add(jo.getString("txtTrnsUuid"));
    	
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
            int i = pre.updateSql(sb.toString(), parms.toArray());
        	return (i > 0); 
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }
	
	public boolean deleteOneData(String compCode, String trnsUuid){
    	
		//System.out.println("deleteOneData: " + trnsUuid);
		
    	StringBuffer sb = new StringBuffer();
    	sb.append(" DELETE FROM HMI0TRNSD        ");    	
    	sb.append("  WHERE TRNS_COMP_CODE = ?    ");
    	sb.append("    AND TRNS_UUID = ?         ");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(compCode);
    	parms.add(trnsUuid);
    	
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
            int i = pre.updateSql(sb.toString(), parms.toArray());
        	return (i > 0); 
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }
	
	public String getTrnsQtySum(String comp, String itemCode, String qryDate1, String qryDate2){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT SUM(TRNS_QTY)            ");
		sb.append(" FROM HMI0TRNSD                  ");
		sb.append(" WHERE TRNS_COMP_CODE = ?        ");
		sb.append("   AND TRNS_ITEM = ?             ");
		sb.append("   AND TRNS_DATE BETWEEN ? AND ? ");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);
		parms.add(itemCode);
		parms.add(qryDate1);
		parms.add(qryDate2);
		
		try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
            String qtySum = pre.getAString(sb.toString(), parms.toArray());
        	return qtySum; 
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
		
	}
    
}