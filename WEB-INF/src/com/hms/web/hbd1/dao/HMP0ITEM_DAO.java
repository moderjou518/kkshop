package com.hms.web.hbd1.dao;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.evergreen_hotels.bmg.whms1.exception.DataAccessException;
import com.evergreen_hotels.bmg.whms1.util.GenericDao;
import com.evergreen_hotels.bmg.whms1.util.PrepareExecuteSql;
import com.evergreen_hotels.bmg.whms1.util.RowDataList;
//import com.evergreen_hotels.wlh.util.UTIL_ExceptionHandle;
//import com.evergreen.common.dao.GenericDao;
//import com.evergreen.common.dao.PrepareExecuteSql;
//import com.evergreen.common.exception.DataAccessException;
import com.hms.util.UTIL_String;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HMP0ITEM_DAO extends GenericDao {
	
	public HMP0ITEM_DAO(Connection conn) throws SQLException{
		super.setConnection(conn);
	}
    
    public JSONArray queryItemList(String comp, String itemType){
        
    	//System.out.println("DAO1: querydataList");
    	// 全部，有使用中的
    	ArrayList<String> parms = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        sb.append(" Select	item_uuid, ");
        sb.append(" 		item_comp,        	");
        sb.append(" 		item_code,        	");
        sb.append(" 		item_name,        	");
        sb.append(" 		item_abbr,        	");
        sb.append(" 		item_unit,        	");
        sb.append(" 		item_price,       	");
        sb.append(" 		item_type,        	");
        sb.append(" 		item_sort,        	");
        sb.append(" 		item_hide_price_mk, ");
        sb.append(" 		item_void_mk      	");        
		sb.append("   FROM HMP0ITEM 			");
		sb.append("  WHERE ITEM_COMP = ? 		");		    		
        parms.add(comp);        
        
        if(!"*".equals(itemType)){
        	sb.append("AND ITEM_TYPE = ? ");
        	parms.add(itemType);
        }
        sb.append(" ORDER BY ITEM_TYPE, ITEM_SORT, ITEM_UUID		");    	        
    	 
        JSONArray ary = null;
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();                           	
            RowDataList rdl = pre.getRowDataList(sb.toString(), parms.toArray());
            if(rdl != null){
            	ary = rdl.toJSONArray();
            }
        	return ary;
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }   
    
    public String getMaxSeq(String comp){
		
		StringBuffer sb = new StringBuffer();
		//sb.append(" SELECT Format(Nz(Max(RIGHT(ITEM_CODE,4)), 0) + 1, '0000')   ");
		sb.append(" SELECT Nz(Max(item_uuid), 0) + 1   ");
		sb.append("   FROM HMP0ITEM                                             ");
		sb.append("  WHERE ITEM_COMP = ?                                		");
		
		
		
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);
		
		
		String result = "";        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            result = pre.getAString(sb.toString(), parms.toArray());
            if (result.length() == 1){
            	result = "000" + result;
            }
            if (result.length() == 2){
            	result = "00" + result;
            }
            if (result.length() == 3){
            	result = "0" + result;
            }
            
            
        } catch (SQLException e) {
            result = "";
          	throw new DataAccessException(e);
        }    	
    	
    	return result;
		
		
	}   
    
    
    public int addData(JSONObject jo){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" Insert into HMP0ITEM(                      ");
		sb.append(" 	ITEM_CODE, ITEM_NAME, ITEM_ABBR, ITEM_UNIT,        ");
		sb.append(" 	ITEM_PRICE,ITEM_TYPE, ITEM_SORT, ITEM_HIDE_PRICE_MK, ITEM_VOID_MK,     ");
		sb.append(" 	ITEM_MOD_USER, ITEM_MOD_TIME, ITEM_COMP)  ");
		sb.append(" VALUES(?, ?, ?, ?, 		");
		sb.append("        ?, ?, ?, ?, ?,	");
		sb.append(" 	   ?, now(), ?)	");
    	
		// System.out.println("Item: " + jo.toString());
		Object[] addParm = new Object[]{
				jo.getString("newSeq"),
        		jo.getString("txtItemName"),
        		jo.getString("txtItemAbbr"),
        		jo.getString("txtItemUnit"),
        		jo.getString("txtItemPrice"),
        		jo.getString("txtItemType"),
        		jo.getString("txtItemSort"),
        		"",
        		jo.getString("txtItemVoidMk").replace("[", "").replace("]", "").replace("\"", ""),
        		jo.getString("loginID"),
        		jo.getString("compCode")                		
        };
		
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            cnt = pre.updateSql(sb.toString(), addParm);            
        } catch (SQLException e) {
            cnt = 0;
          	throw new DataAccessException(e);
        }    	
    	
    	return cnt;
    	
    }
    
    public int updateData(JSONObject jo){
    	
        StringBuffer sb = new StringBuffer();            
        sb.append(" Update HMP0ITEM                 ");
		sb.append("    Set ITEM_NAME = ?,           ");
		sb.append("        ITEM_ABBR = ?,           ");
		sb.append("        ITEM_UNIT = ?,           ");
		sb.append("        ITEM_PRICE = ?,          ");
		sb.append("        ITEM_TYPE = ?,           ");
		sb.append("        ITEM_SORT = ?,           ");
		sb.append("		   ITEM_HIDE_PRICE_MK = ?,  ");
		sb.append("        ITEM_VOID_MK = ?,        ");
		sb.append("        ITEM_MOD_USER = ?,       ");
		sb.append("        ITEM_MOD_TIME = NOW()    ");
		sb.append("  WHERE ITEM_COMP = ?            ");
		sb.append("    AND ITEM_UUID = ?            ");

        Object[] updParm = new Object[]{
        		jo.getString("txtItemName"),
        		jo.getString("txtItemAbbr"),
        		jo.getString("txtItemUnit"),
        		jo.getString("txtItemPrice"),
        		jo.getString("txtItemType"),
        		jo.getString("txtItemSort"),        		
        		jo.getString("txtItemHidePriceMk").replace("[", "").replace("]", "").replace("\"", ""),
        		jo.getString("txtItemVoidMk").replace("[", "").replace("]", "").replace("\"", ""),
        		"sys",
        		jo.getString("compCode"),
        		jo.getString("txtItemUuid")
        };
        
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.updateSql(sb.toString(), updParm);            
        } catch (SQLException e) {
            cnt = 0;
          	throw new DataAccessException(e);
        }
    	
    	return cnt;
    	
    }
    
    public int deleteThisData(String compCode, String itemUuid){
    	
        StringBuffer sb = new StringBuffer();            
        sb.append(" DELETE FROM HMP0ITEM  ");
		sb.append(" WHERE ITEM_COMP = ?   ");
		sb.append("   AND ITEM_UUID = ?   ");
		Object[] delParm = new Object[]{
        		compCode,
        		itemUuid
        };           
        
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.updateSql(sb.toString(), delParm);            
        } catch (SQLException e) {
        	e.printStackTrace();
            cnt = 0;
          	throw new DataAccessException(e);
        }
    	
    	return cnt;
    	
    } 
    
    /**
     * 券種是否有被商品使用中
     * @param tketType
     * @return
     */
    public boolean isInUse(String comp, String tketType){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" SELECT	COUNT(*) ");
    	sb.append("   FROM  HTI0ITDD ");
    	sb.append("  WHERE	COMP = :comp ");
    	sb.append("    AND  ITDD_TKET_TYPE = :TYPE ");
    	Object[] parm = new Object[]{comp, tketType};
    	
    	boolean inUse = false;
    	int cnt = 0;
    	try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.getAInt(sb.toString(), parm);
            inUse = (cnt > 0);
        } catch (SQLException e) {
            cnt = 0;
          	throw new DataAccessException(e);
        }
    	
    	return false;	
    }
    
    public int updateItemPrice(JSONObject priceJo){
    	    	    	
    	System.out.println("priceJo: " + priceJo.toString());
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" UPDATE HBD1ITEMPRICE      	");
    	sb.append("    SET IP_UNIT_PRICE = ?  	");
    	sb.append("        ,IP_MOD_DATE = NOW()	");
    	sb.append("  WHERE IP_COMP_CODE = ?   	");
    	sb.append("    AND IP_ITEM_CODE = ?		");
    	sb.append("    AND IP_DATE >= ?     	");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(priceJo.getString("txtIpUnitPrice"));    	
    	parms.add(priceJo.getString("compCode"));
    	parms.add(priceJo.getString("txtIpItemCode"));
    	parms.add(priceJo.getString("txtIpDate"));       
    	
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
            int i = pre.updateSql(sb.toString(), parms.toArray());                      
        	return i;
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
    	
    	
    	
    	
    }
    
    public JSONArray queryItemPriceList(String comp, String itemCode, String qryMonth){
        
    	// 全部，有使用中的
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT 	IP_UUID, IP_COMP_CODE, IP_ITEM_CODE,  	");
        sb.append("			IP_DATE, IP_UNIT_PRICE, IP_WEEKDAY		");
        sb.append("   FROM HBD1ITEMPRICE                            ");
        sb.append("  WHERE IP_COMP_CODE = ?                         ");
        sb.append("    AND IP_ITEM_CODE = ?                         ");
        sb.append("    AND LEFT(IP_DATE,6)  = ?                     ");
        
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(comp);
    	parms.add(itemCode);
    	parms.add(qryMonth);
    	        
    	JSONArray ary = null;
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();                                 
        	
            RowDataList rdl = pre.getRowDataList(sb.toString(), parms.toArray());
            if(null != rdl){
            	ary = rdl.toJSONArray();
            	
            	for(int i=0; i < ary.size(); i++){
            		JSONObject jo = ary.getJSONObject(i);
            		String weekDay = UTIL_String.getWeekOfDate(jo.getString("ipDate"));
            		jo.put("WEEK_DAY", weekDay);
            	}
            }
        	return ary;
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }
    
    public boolean createDatePrice(String comp, String itemCode, String addDate, String unitPrice){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("INSERT INTO HBD1ITEMPRICE	");
    	sb.append("	(IP_COMP_CODE, IP_DATE, IP_ITEM_CODE, IP_UNIT_PRICE) ");
    	sb.append("	VALUES(?, ?, ?, ?)			");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(comp);
    	parms.add(addDate);
    	parms.add(itemCode);
    	parms.add(unitPrice);
    	
    	boolean result = false;
    	try {
            PrepareExecuteSql pre = getPrepareExecuteSql();                                 
        	int i = pre.updateSql(sb.toString(), parms.toArray());
        	
        	if(i > 0){
        		result = true;
        	}else{
        		result = false;
        	}
            
            
        	return result;
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
    }
    
    public boolean clearDatePrice(String comp, String itemCode, String fromDate, String toDate){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("DELETE FROM HBD1ITEMPRICE	");
    	sb.append("	WHERE IP_COMP_CODE = ?      ");
    	sb.append("   AND IP_ITEM_CODE = ?		");
    	sb.append("   AND IP_DATE >=  ?			");
    	sb.append("   AND IP_DATE <=  ?			");
    	
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(comp);
    	parms.add(itemCode);
    	parms.add(fromDate);
    	parms.add(toDate);
    	
    	//parms.add(unitPrice);
    	
    	boolean result = false;
    	try {
            PrepareExecuteSql pre = getPrepareExecuteSql();                                 
        	int i = pre.updateSql(sb.toString(), parms.toArray());
        	
        	if(i > 0){
        		result = true;
        	}else{
        		result = false;
        	}
            
            
        	return result;
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
    }
    
    /**
     *  for 662 年度報表使用
     */    
    public JSONArray queryYearTable(String comp, String year) {
        
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT  A.HAC0_BASC_NOTE, A.HAC0_BASC_LOGIN_ID, M.POMM_BUYER, A.HAC0_BASC_NAME, A.HAC0_BASC_NOTE, LEFT(D.PODD_DATE, 6) AS POMM_MONTH,              ");
        sb.append(" SUM(round(NZ(D.PODD_RCVD_QTY, 0) * D.PODD_UNIT_PRICE,0)) AS MONTH_AMT  ");
        sb.append("   FROM  HMP0POMM M, HMP0PODD D, HAC0BASC A                                        ");     
        sb.append("  WHERE  M.POMM_COMP_CODE = ?                                ");
        sb.append("    AND  LEFT(M.POMM_DATE,4) = ?                                   ");
        sb.append("    AND  M.POMM_COMP_CODE = D.PODD_COMP_CODE                            ");
        sb.append("    AND  M.POMM_DATE = D.PODD_DATE                                      ");
        sb.append("    AND  M.POMM_SEQ = D.PODD_SEQ                                        ");
        sb.append("    AND  M.POMM_BUYER 	= A.HAC0_BASC_LOGIN_ID	");
        sb.append("  GROUP  BY A.HAC0_BASC_NOTE, A.HAC0_BASC_LOGIN_ID, M.POMM_BUYER, A.HAC0_BASC_NAME,  A.HAC0_BASC_NOTE, LEFT(D.PODD_DATE, 6)                          ");
        sb.append("  ORDER BY A.HAC0_BASC_NOTE, A.HAC0_BASC_LOGIN_ID, 1, 2, 3, 4                                                        ");
        //sb.append(" HAVING  NVL(SUM(round(NVL(D.PODD_RCVD_QTY, 0) * D.PODD_UNIT_PRICE,0)),0) > 0 ");
        
        
        ArrayList<String> parms = new ArrayList<String>();
        parms.add(comp);
        parms.add(year);
        
        JSONArray ary = null;
        try {            
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            ary = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();
            System.out.println("Year PO size: " + ary.toString());            
        } catch (SQLException e) {            
            throw new DataAccessException(e);
        }
        
        return ary;
    }
    
    /**
     *  for 662 年度報表使用
     */
    public JSONArray queryMonthItem(String comp, String month, String vendCode) {
        
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT DISTINCT D.PODD_ITEM, I.ITEM_NAME ");
        sb.append(" FROM HMP0POMM M, HMP0PODD D, HMP0ITEM I      ");
        sb.append(" WHERE M.POMM_COMP_CODE = :comp               ");
        sb.append("   AND LEFT(M.POMM_DATE,4) = ?        ");
        sb.append("   AND M.POMM_BUYER = ?                ");        
        sb.append("   AND M.POMM_COMP_CODE = D.PODD_COMP_CODE    ");
        sb.append("   AND M.POMM_DATE = D.PODD_DATE              ");
        sb.append("   AND M.POMM_SEQ =  D.PODD_SEQ               ");
        sb.append("   AND I.ITEM_COMP = D.PODD_COMP_CODE    ");
        sb.append("   AND I.ITEM_CODE = D.PODD_ITEM              ");
        sb.append(" ORDER BY D.PODD_ITEM                         ");
        
        ArrayList<String> parms = new ArrayList<String>();
        parms.add(comp);
        parms.add(month);
        parms.add(vendCode);
        
        JSONArray ary = null;
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            ary = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();
            System.out.println("Month item size: " + ary.size());            
        } catch (SQLException e) {            
            throw new DataAccessException(e);
        }
        
        return ary;
    }
    
    /**
     *  for 662 年度報表使用
     */
    public JSONArray queryCustMonthTable(String comp, String month, String buyer) {
        
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT D.PODD_ITEM, D.PODD_ITEM, D.PODD_ORD_QTY, D.PODD_RCVD_QTY    ");
        sb.append("   FROM HMP0POMM M, HMP0PODD D                                       ");
        sb.append(" WHERE M.POMM_COMP_CODE = :comp                                      ");
        sb.append("   AND LEFT(M.POMM_DATE,6) = ?                                ");
        sb.append("   AND M.POMM_BUYER = ?                                       ");        
        sb.append("    AND M.POMM_COMP_CODE = D.PODD_COMP_CODE                       ");
        sb.append("    AND M.POMM_DATE = D.PODD_DATE                                 ");
        sb.append("    AND M.POMM_SEQ =  D.PODD_SEQ                                  ");
        sb.append(" ORDER BY M.POMM_DATE, D.PODD_ITEM                                ");
        
        ArrayList<String> parms = new ArrayList<String>();
        parms.add(comp);
        parms.add(month);
        parms.add(buyer);
        
        JSONArray ary = null;
        try {            
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            ary = pre.getRowDataList(sb.toString(), parms.toArray()).toJSONArray();
            System.out.println("Year PO size: " + ary.size());            
        } catch (SQLException e) {            
            throw new DataAccessException(e);
        }
        
        return ary;
    }

    
}