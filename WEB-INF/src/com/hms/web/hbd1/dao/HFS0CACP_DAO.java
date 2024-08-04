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

public class HFS0CACP_DAO extends GenericDao {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	public HFS0CACP_DAO(Connection conn) throws SQLException{
		super.setConnection(conn);
	}
	
	public String getNewNo(String comp, String wkDate){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" Select max(cacp_no) + 1 as NEW_NO 	");
    	sb.append("   From HFS0CACPD                	");
    	sb.append("  WHERE COMP_CODE = ?         		");
    	sb.append("    AND CACP_DATE = ?           		");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(comp);
    	parms.add(wkDate);				
		String result = "";
		
		try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
            result = pre.getAString(sb.toString(), parms.toArray());
            
            if(null == result || "".equals(result)){
            	result = "1";
            }
            
        } catch (SQLException e) {
        	result = "";
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
    	
		return result;
    	
    }
    
	public JSONArray queryCacpList(String compCode, String qryDate1, String qryDate2){
    	
    	StringBuffer sb = new StringBuffer();    	
    	sb.append(" SELECT 	I.ITEM_NAME, I.ITEM_CODE, I.ITEM_ABBR, 	");
    	sb.append("        	BD.CACP_QTY, I.ITEM_UNIT, BD.CACP_DATE, ");
    	sb.append("         BD.CACP_NO, BD.CACPD_UUID,BD.CACP_NOTES ");
    	sb.append("   FROM 	HMP0ITEM I, HFS0CACPD BD                ");
    	sb.append("  WHERE 	BD.COMP_CODE = ?             			");
    	sb.append("    AND 	BD.CACP_DATE BETWEEN ? AND ?            ");
    	sb.append("    AND 	BD.COMP_CODE = I.ITEM_COMP              ");
    	sb.append("    AND 	BD.CACP_RECP_CODE = I.ITEM_CODE         ");
    	sb.append(" ORDER BY BD.CACP_DATE, BD.CACP_NO              	");		

    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(compCode);           	
    	parms.add(qryDate1);
    	parms.add(qryDate2);
    	
    	System.out.println("parms: " + parms.toString());
    	
    	JSONArray ary = null;
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
            RowDataList rdl = pre.getRowDataList(sb.toString(), parms.toArray());
        	
        	if (null != rdl){
        		ary = rdl.toJSONArray();
        	}        	
        	return ary;
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }   

	public boolean addOneData(JSONObject jo){
    	
		System.out.println("hfs0 addOneData: " + jo.toString());
		
    	StringBuffer sb = new StringBuffer();
    	sb.append(" INSERT INTO HFS0CACPD(			");
    	sb.append(" COMP_CODE, CACP_DATE, CACP_NO, 	");
    	sb.append(" CACP_RECP_CODE, CACP_QTY, CACP_NOTES,     	");
    	sb.append(" MOD_USER, MOD_DATE) 			");
    	sb.append(" VALUES(?, ?, ?, ?, ?, ?, ?, now()) ");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(jo.getString("COMP_CODE"));
    	parms.add(jo.getString("CACP_DATE"));
    	parms.add(jo.getString("CACP_NO"));
    	parms.add(jo.getString("CACP_RECP_CODE"));
    	parms.add(jo.getString("CACP_QTY"));
    	parms.add(jo.getString("CACP_NOTES"));
    	parms.add(jo.getString("CACP_USER"));
    	
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
    	
		System.out.println("saveOneData: " + jo.toString());
		
    	StringBuffer sb = new StringBuffer();
    	sb.append(" UPDATE HFS0CACPD             ");
    	sb.append("    SET CACP_RECP_CODE = ?,   ");    	
    	sb.append(" 	   CACP_QTY = ?,         ");    	
    	sb.append(" 	   CACP_NOTES = ?,         ");
    	sb.append("        MOD_USER = ?,         ");
    	sb.append(" 	   MOD_DATE = NOW()      ");
    	sb.append("  WHERE COMP_CODE = ?    ");
    	sb.append("    AND CACPD_UUID = ?         ");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(jo.getString("dlItemCode"));
    	parms.add(jo.getString("txtTrnsQty"));
    	parms.add(jo.getString("txtCacpNotes"));    	
    	parms.add(jo.getString("loginID"));
    	parms.add(jo.getString("COMP_CODE"));
    	parms.add(jo.getString("txtCacpdUuid"));
    	
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
    	
		System.out.println("deleteOneData: " + trnsUuid);
		
    	StringBuffer sb = new StringBuffer();
    	sb.append(" DELETE FROM HFS0CACPD	");    	
    	sb.append("  WHERE COMP_CODE =  ?   ");
    	sb.append("    AND CACPD_UUID = ?   ");
    	
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
	
	public String getCacpQtySum(String comp, String itemCode, String qryDate1, String qryDate2){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" Select sum(cacp_qty) as cacp_sum_qty ");
		sb.append("   From HFS0CACPD                     ");
		sb.append("  Where COMP_CODE = ?                 ");
		sb.append("    and CACP_RECP_CODE = ?            ");
		sb.append("    and CACP_DATE BETWEEN ? AND ?     ");
		
		
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