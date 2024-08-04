package com.hms.web.hbd1.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
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

public class HMP0BID_DAO extends GenericDao {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");	
	
	public HMP0BID_DAO(Connection conn) throws SQLException{
		super.setConnection(conn);
	}
	
	/**
	 * 用今天找出當周的預訂單ID
	 * @param compCode
	 * @return
	 */
	public String getCurrentBidmUUID(String compCode){
		
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT BID_UUID                            ");
		sb.append("   FROM HMP0BIDM                            ");
		sb.append("  WHERE BID_COMP = ?             ");
		sb.append("    AND FORMAT(NOW(), 'yyyymmdd')           ");
		sb.append("    BETWEEN BID_ORD_SDATE AND BID_ORD_EDATE ");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(compCode);
		
		String result = "";
		try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
            result = pre.getAString(sb.toString(), parms.toArray());        	               
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }finally{        	
        }		
		return result;
		
	}
	
	/**
	 * 
	 * @param compCode
	 * @param poDate
	 * @return
	 */
	public String getBidmUUIDByPoDate(String compCode, String poDate){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT BID_UUID                 ");
		sb.append("   FROM HMP0BIDM                 ");
		sb.append("  WHERE BID_COMP = ?             ");
		sb.append("    AND ?           				");
		sb.append("    BETWEEN BID_ORD_SDATE AND BID_ORD_EDATE ");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(compCode);
		parms.add(poDate);
		
		String result = "";
		try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
            result = pre.getAString(sb.toString(), parms.toArray());        	               
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }finally{        	
        }		
		return result;
		
	}
	
	public JSONArray getTheWeek(String comp, String beginDate, String endDate){
		
		Calendar c = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		JSONArray ary = new JSONArray();
		JSONObject weekDayJo = new JSONObject();
    	weekDayJo.put("WEEK_DATE", beginDate);
    	weekDayJo.put("WEEK_DAY", UTIL_String.getWeekOfDate(beginDate));   	
    	
    	
    	int n = 0;
    	ary.add(n, weekDayJo);
    	
    	// 星期二到星期日
    	for(int i=2; i < 8; i++){
    		c.add(Calendar.DATE, 1);
    		String weekDate = sdf.format(c.getTime());
    		JSONObject dayJo = new JSONObject();
    		dayJo.put("WEEK_DATE", weekDate);
    		dayJo.put("WEEK_DAY", UTIL_String.getWeekOfDate(weekDate));    		
    		ary.add(++n, dayJo);
    	}
    	
    	
    	
		return ary;
    	
		
		
	}
	
	/**
	 * 依系統現在的日期算出當周的起迄日
	 * @param comp
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public JSONArray getThisWeek(String comp) throws SQLException, ParseException{
		
		// 用今天日期去抓本周預訂單，再找到起迄日區間
		String bidmUUID = this.getCurrentBidmUUID(comp);		
		JSONObject jo = this.getBidmData(comp, bidmUUID);
		String todayStr = UTIL_String.getNowString("yyyyMMdd");
		
		JSONArray ary = new JSONArray();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");   	    	
    	Calendar c = new GregorianCalendar();    	
    	String monday = jo.getString("bidOrdSdate");
    	c.setTime(sdf.parse(monday));
    	
    	String beginDate = sdf.format(c.getTime());
    	// 每天日期跟星期
    	JSONObject weekDayJo = new JSONObject();
    	weekDayJo.put("WEEK_DATE", beginDate);
    	weekDayJo.put("WEEK_DAY", UTIL_String.getWeekOfDate(beginDate));
    	// 判斷今天是不是剛好是預訂單第1天(星期1)
    	if (todayStr.equals(beginDate)){
    		weekDayJo.put("TODAY_MARK", "Y");
    	}else{
    		weekDayJo.put("TODAY_MARK", "N");
    	}
    	int n = 0;
    	ary.add(n, weekDayJo);
    	
    	// 星期二到星期日
    	for(int i=2; i < 8; i++){
    		c.add(Calendar.DATE, 1);
    		String weekDate = sdf.format(c.getTime());
    		JSONObject dayJo = new JSONObject();
    		dayJo.put("WEEK_DATE", weekDate);
    		dayJo.put("WEEK_DAY", UTIL_String.getWeekOfDate(weekDate));
    		if (todayStr.equals(weekDate)){
    			dayJo.put("TODAY_MARK", "Y");
        	}else{
        		dayJo.put("TODAY_MARK", "N");
        	}
    		ary.add(++n, dayJo);
    	}    	
    	/*
    	for(int i=0; i < ary.size(); i++){
    		JSONObject jo1 = ary.getJSONObject(i);
    		System.out.println(jo1.toString());
    	} 
    	*/   	
    	
        return ary;	
		
	}
	
	public JSONArray getThisWeek(String comp, String monday) throws SQLException, ParseException{		
		
		JSONArray ary = new JSONArray();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");   	    	
    	Calendar c = new GregorianCalendar();   	
    	c.setTime(sdf.parse(monday));
    	
    	String beginDate = sdf.format(c.getTime());
    	// 每天日期跟星期
    	JSONObject weekDayJo = new JSONObject();
    	weekDayJo.put("WEEK_DATE", beginDate);
    	weekDayJo.put("WEEK_DAY", UTIL_String.getWeekOfDate(beginDate));
    	
    	// 判斷今天是不是剛好是預訂單第1天(星期1)
    	String todayStr = UTIL_String.getNowString("yyyyMMdd");
    	if (todayStr.equals(beginDate)){
    		weekDayJo.put("TODAY_MARK", "Y");
    	}else{
    		weekDayJo.put("TODAY_MARK", "N");
    	}
    	int n = 0;
    	ary.add(n, weekDayJo);
    	
    	// 星期二到星期日
    	for(int i=2; i < 8; i++){
    		c.add(Calendar.DATE, 1);
    		String weekDate = sdf.format(c.getTime());
    		JSONObject dayJo = new JSONObject();
    		dayJo.put("WEEK_DATE", weekDate);
    		dayJo.put("WEEK_DAY", UTIL_String.getWeekOfDate(weekDate));
    		if (todayStr.equals(weekDate)){
    			dayJo.put("TODAY_MARK", "Y");
        	}else{
        		dayJo.put("TODAY_MARK", "N");
        	}
    		ary.add(++n, dayJo);
    	}    	
    	
        return ary;	
		
	}

	/**
	 * 
	 * @param compCode
	 * @param bidUUID
	 * @return
	 * @throws SQLException
	 */
	public JSONObject getBidmData(String compCode, String bidUUID) throws SQLException{
    	
		StringBuffer sb = new StringBuffer();		
		sb.append(" SELECT bid_uuid, bid_comp, bid_ord_sdate,       		");
		sb.append("        bid_ord_edate, bid_sale_sdate,           		");
		sb.append("		   bid_sale_edate, bid_sale_mark, bid_name  		");		
		sb.append("   FROM hmp0bidm                                 		");
		sb.append("  WHERE bid_comp = ?                   					");
		sb.append("      AND bid_uuid = ?                           ");	        
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(compCode);
		parms.add(bidUUID);
    	        
		//System.out.println("comp: " + compCode);
		//System.out.println("uuid: " + bidUUID);
		
		JSONObject jo = null;
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
            RowData rd = pre.getARowData(sb.toString(), parms.toArray());
        	if (rd == null){
        		jo = new JSONObject();
        		jo.put("bidUuid", "");
        		jo.put("bidComp", "");
        		jo.put("bidOrdSdate", "");
        		jo.put("bidOrdEdate", "");
        		jo.put("bidSaleSdate", "");
        		jo.put("bidSaleEdate", "");
        	}else{
        		jo = rd.toJSONObject();
        	}
        	
        	//System.out.println("getBidmJO: " + jo.toString());
        	return jo;               
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }
	
	//
	public JSONObject getBiddData(String compCode, String bidUUID, String itemCode) throws SQLException{
    	
		StringBuffer sb = new StringBuffer();		
		sb.append(" SELECT 	D.BIDD_UUID, D.BIDD_COMP, D.BIDM_UUID, D.BIDD_ITEM, D.BIDD_PRICE   	");		
		sb.append("   FROM hmp0bidd d                                 		");
		sb.append("  WHERE bidd_comp = ?                   					");
		sb.append("      AND bidm_uuid = ?                           ");	        
		sb.append("      AND bidd_item = ?                           ");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(compCode);
		parms.add(bidUUID);
		parms.add(itemCode);
    	        
		//System.out.println("comp: " + compCode);
		//System.out.println("uuid: " + bidUUID);
		
		JSONObject jo = null;
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
            RowData rd = pre.getARowData(sb.toString(), parms.toArray());
        	if (rd == null){
        		jo = new JSONObject();
        		jo.put("bidmUuid", "");
        		jo.put("biddItem", "");
        		jo.put("itemAbbr", "");
        		jo.put("biddPrice", "0");        		
        	}else{
        		jo = rd.toJSONObject();
        	}
        	return jo;               
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }
    
	/**
	 * �憭拇������
	 * @param compCode
	 * @return
	 */
    public RowDataList queryOnSaleBidList(String compCode){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" SELECT 	BID_UUID, BID_NAME, BID_SALE_MARK,  ");
    	sb.append("			BID_SALE_SDATE, BID_SALE_EDATE, ");
    	sb.append("			BID_ORD_SDATE, BID_ORD_EDATE, ");
    	sb.append("			right(BID_SALE_SDATE,4) sale_sdate, right(BID_SALE_EDATE,4) sale_edate, ");
    	sb.append("			right(BID_ORD_SDATE,4) ord_sdate, right(BID_ORD_EDATE,4) ord_edate ");
		sb.append("   FROM 	HMP0BIDM ");
		sb.append("  WHERE 	BID_COMP = ? ");
		sb.append("    AND 	BID_SALE_MARK = 'Y' ");
		
		// pa
		sb.append("    AND 	FORMAT(NOW(), 'yyyymmdd') BETWEEN BID_SALE_SDATE AND BID_SALE_EDATE ");
		
		// dvp
		/*
		sb.append("    AND 	(");
		sb.append("			FORMAT(NOW(), 'yyyymmdd') BETWEEN BID_SALE_SDATE AND BID_SALE_EDATE ");
		sb.append("         OR ");
		sb.append("			   (FORMAT(NOW()-30, 'yyyymmdd') < BID_SALE_SDATE  ");
		sb.append("				AND ");
		sb.append("			    FORMAT(NOW(), 'yyyymmdd') > BID_SALE_SDATE)  ");
		sb.append("     	)");
		*/
		sb.append(" ORDER BY BID_SALE_SDATE ");        

    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(compCode);        
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
        	rdl = pre.getRowDataList(sb.toString(), parms.toArray());
        	
        	
        	return rdl;               
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }
    
    /**
     * FOR W020: 最近1個月的預訂單資料
     * @param compCode
     * @return
     */
    public RowDataList queryRecentBidList(String compCode){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" SELECT 	BID_UUID, BID_NAME, BID_SALE_MARK,  ");
    	sb.append("			BID_SALE_SDATE, BID_SALE_EDATE, 	");
    	sb.append("			BID_ORD_SDATE, BID_ORD_EDATE, ");
    	sb.append("			right(BID_SALE_SDATE,4) sale_sdate, right(BID_SALE_EDATE,4) sale_edate, ");
    	sb.append("			right(BID_ORD_SDATE,4) ord_sdate, right(BID_ORD_EDATE,4) ord_edate ");
		sb.append("   FROM 	HMP0BIDM ");
		sb.append("  WHERE 	BID_COMP = ? ");
		sb.append("    AND 	BID_SALE_MARK = 'Y' ");
		sb.append("    AND 	(");
		sb.append("			FORMAT(NOW(), 'yyyymmdd') BETWEEN BID_SALE_SDATE AND BID_SALE_EDATE ");
		sb.append("         OR ");
		// 20230403: 調整為最近43天(6周)，原本顯示最近30天(4周)。
		sb.append("			   (FORMAT(NOW()-43, 'yyyymmdd') < BID_SALE_SDATE  ");
		sb.append("				AND ");
		sb.append("			    FORMAT(NOW(), 'yyyymmdd') > BID_SALE_SDATE)  ");
		sb.append("     	)");
		sb.append(" ORDER BY BID_SALE_SDATE ");        

    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(compCode);        
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
        	rdl = pre.getRowDataList(sb.toString(), parms.toArray());      	        	
        	return rdl;               
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }

    
    /**
     * �閰Ｗ恥������
     * @param compCode
     * @return
     */
    public RowDataList queryWeekPOList11(String compCode, String queryMonth){
    	
    	String querySDay = queryMonth + "01";
    	String queryEDay = queryMonth + "31";
    	
    	StringBuffer sb = new StringBuffer();    	
    	sb.append(" SELECT 	BID_UUID, BID_NAME, BID_SALE_MARK,  ");
    	sb.append("			BID_SALE_SDATE, BID_SALE_EDATE, 	");
    	sb.append("			BID_ORD_SDATE, BID_ORD_EDATE, ");
    	sb.append("			right(BID_SALE_SDATE,4) sale_sdate, right(BID_SALE_EDATE,4) sale_edate, ");
    	sb.append("			right(BID_ORD_SDATE,4) ord_sdate, right(BID_ORD_EDATE,4) ord_edate ");
		sb.append("   FROM 	HMP0BIDM ");
		sb.append("  WHERE 	BID_COMP = ? ");
		sb.append("    AND 	BID_SALE_MARK = 'Y' ");
		sb.append("    AND  (BID_ORD_SDATE BETWEEN ? AND ? ");
		sb.append("          OR ");
		sb.append("          BID_ORD_EDATE BETWEEN ? AND ? )");
		sb.append(" ORDER BY BID_ORD_SDATE ");        

    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(compCode);        
    	parms.add(querySDay);
    	parms.add(queryEDay);
    	parms.add(querySDay);
    	parms.add(queryEDay);
    	
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
        	rdl = pre.getRowDataList(sb.toString(), parms.toArray());      	        	
        	return rdl;               
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }	
    	
    	
    }
    
    public RowDataList queryMonthBidList(String compCode, JSONObject jo){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" SELECT 	BID_UUID, BID_NAME, BID_SALE_MARK,  ");
    	sb.append("			BID_ORD_SDATE, BID_ORD_EDATE, 		");
    	sb.append("			BID_SALE_SDATE, BID_SALE_EDATE 		");
		sb.append("   FROM 	HMP0BIDM ");
		sb.append("  WHERE 	BID_COMP = ? ");
		sb.append("    AND 	(BID_ORD_SDATE BETWEEN ? AND ?  ");
		sb.append("          OR								");
		sb.append("      	 BID_ORD_EDATE BETWEEN ? AND ?  )");
		sb.append(" ORDER BY BID_SALE_SDATE ");       
		
		//System.out.println(sb.toString());

    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(compCode);        
    	String qryMon01 = jo.getString("qryYear") + jo.getString("qryMonth") + "01";
    	String qryMon31 = jo.getString("qryYear") + jo.getString("qryMonth") + "31";
    	parms.add(qryMon01);
    	parms.add(qryMon31);
    	parms.add(qryMon01);
    	parms.add(qryMon31);
    	//System.out.println("comp: " + compCode);
    	//System.out.println("qryParm01: " + qryMon01);
    	//System.out.println("qryParm31: " + qryMon31);
    	
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
        	rdl = pre.getRowDataList(sb.toString(), parms.toArray());
        	return rdl;               
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }
    
    public RowDataList queryMonthBidList(String compCode, String queryMonth){
    	
    	//System.out.println("bidDao: " + queryMonth);
    	StringBuffer sb = new StringBuffer();
    	sb.append(" SELECT 	BID_UUID, BID_NAME, BID_SALE_MARK,  ");
    	sb.append("			right(BID_ORD_SDATE,4) BID_ORD_SDATE1, 		");
    	sb.append("			right(BID_ORD_EDATE,4) BID_ORD_EDATE1, ");
    	sb.append("			BID_ORD_SDATE BID_ORD_SDATE2, 		");
    	sb.append("			BID_ORD_EDATE BID_ORD_EDATE2, ");
    	sb.append("			BID_SALE_SDATE, BID_SALE_EDATE 		");
		sb.append("   FROM 	HMP0BIDM ");
		sb.append("  WHERE 	BID_COMP = ? ");
		sb.append("    AND 	(BID_ORD_SDATE BETWEEN ? AND ?  ");
		sb.append("          OR								");
		sb.append("      	 BID_ORD_EDATE BETWEEN ? AND ?  )");
		sb.append(" ORDER BY BID_SALE_SDATE ");       
		
		//System.out.println(sb.toString());

		String qryMon01 = queryMonth + "01";
    	String qryMon31 = queryMonth + "31";
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(compCode);
    	parms.add(qryMon01);
    	parms.add(qryMon31);
    	parms.add(qryMon01);
    	parms.add(qryMon31);
    	
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
        	rdl = pre.getRowDataList(sb.toString(), parms.toArray());
        	return rdl;               
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }

    
    public JSONArray queryBidItemList(String compCode, String bidUuid){
    	
    	StringBuffer sb = new StringBuffer();    	
    	sb.append(" SELECT 	I.ITEM_ABBR, ITEM_NAME, I.ITEM_CODE, BD.BIDD_PRICE,	");
    	sb.append("        	BD.BIDD_COMP, BD.BIDM_UUID, BD.BIDD_UUID, 	");
    	sb.append("			BD.BIDD_ITEM    				");
    	sb.append("   FROM 	HMP0ITEM I, HMP0BIDD BD                 	");
    	sb.append("  WHERE 	BD.BIDD_COMP = ?             				");
    	sb.append("    AND 	BD.BIDM_UUID = ?                        	");
    	sb.append("    AND 	BD.BIDD_COMP = I.ITEM_COMP              	");
    	sb.append("    AND 	BD.BIDD_ITEM = I.ITEM_CODE              	");    	
    	sb.append("ORDER BY I.ITEM_TYPE, I.ITEM_SORT, I.ITEM_CODE		");	    	
    	
    	StringBuffer sb2 = new StringBuffer();
    	sb2.append("");

    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(compCode);           	
    	parms.add(bidUuid);
    	
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
        	rdl = pre.getRowDataList(sb.toString(), parms.toArray());
        	if (null != rdl){
        		return rdl.toJSONArray();
        	}else{
        		return null;
        	}        	          
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }   
    
    
    public Hashtable queryBidItemPrice(String compCode, String bidUuid){
    	
    	StringBuffer sb = new StringBuffer();    	
    	sb.append(" select bidd_item, bidd_price     ");
    	sb.append(" from hmp0bidd                    ");
    	sb.append(" where bidd_comp = ?   ");
    	sb.append("   and bidm_uuid = ?             ");

    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(compCode);           	
    	parms.add(bidUuid);
    	
    	Hashtable priceAry = null;
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
        	rdl = pre.getRowDataList(sb.toString(), parms.toArray());
        	if (null != rdl){
        		JSONArray ary = rdl.toJSONArray();
        		
        		priceAry = new Hashtable();
        		for(int i=0; i < ary.size(); i++){
        			JSONObject priceJo = ary.getJSONObject(i);
        			System.out.println(i + "-iiitem: " + priceJo.getString("biddItem") + ", pprice: " + priceJo.getString("biddPrice"));        			
        			priceAry.put(priceJo.getString("biddItem"), priceJo.getString("biddPrice"));
        		}       	
        		
        		System.out.println("priceAry size: " + priceAry.size());
        		
        		return priceAry;
        	}else{
        		return null;
        	}        	          
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }
    
    public boolean syncItemPriceWithBid(String compCode, JSONArray priceAry){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" UPDATE HBD1ITEMPRICE             ");
    	sb.append("    SET IP_UNIT_PRICE = ?,        ");
    	sb.append("        IP_MOD_DATE = NOW()       ");
    	sb.append("  WHERE IP_COMP_CODE = ?          ");
    	sb.append("    AND IP_DATE BETWEEN ? AND ?   ");
    	sb.append("    AND IP_ITEM_CODE = ?			 ");
    	
    	
    	try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
            
            int cnt = 0;
            for(int i=0; i < priceAry.size(); i++){
    			JSONObject priceJo = priceAry.getJSONObject(i);
    			ArrayList<String> priceParm = new ArrayList<String>();
    			priceParm.add(priceJo.getString("biddPrice"));
    			priceParm.add(compCode);
    			priceParm.add(priceJo.getString("bidOrdSdate"));
    			priceParm.add(priceJo.getString("bidOrdEdate"));
    			priceParm.add(priceJo.getString("biddItem"));
    			cnt += pre.updateSql(sb.toString(), priceParm.toArray());
    		}
            
            return (cnt > 0);
            
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }

    	
    	
    }
    
    public JSONArray queryItemPriceByYear(String compCode, String year){
    	
    	System.out.println("---queryItemPriceByYear----");
    	
    	StringBuffer sb = new StringBuffer();    	
    	sb.append(" SELECT M.BID_COMP as COMP_CODE, D.BIDD_ITEM, M.BID_ORD_SDATE, M.BID_ORD_EDATE, D.BIDD_PRICE      ");
    	sb.append("   FROM HMP0BIDM M, HMP0BIDD D                                           ");
    	sb.append("  WHERE M.BID_COMP = ?                                               	");
    	sb.append("    AND LEFT(M.BID_ORD_SDATE,4) = ?   	");
    	sb.append("    AND M.BID_COMP = D.BIDD_COMP                                         ");
    	sb.append("    AND M.BID_UUID = D.BIDM_UUID                                         ");
    	sb.append(" ORDER BY D.BIDD_ITEM, M.BID_ORD_SDATE                                   ");
    	ArrayList<String> parms1 = new ArrayList<String>();
    	parms1.add(compCode);           	
    	parms1.add(year);    	
    	System.out.println("BID DAO Parms: " + parms1.toString());
    	
    	
    	JSONArray priceAry = null;
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
        	rdl = pre.getRowDataList(sb.toString(), parms1.toArray());
        	
        	//System.out.println("=!=rdl: " + (rdl!=null));
        	if (null != rdl){
        		priceAry = rdl.toJSONArray();       		      	        		
        		//System.out.println("priceAry size: " + priceAry.size());        		        		
        	}        	          
        	return priceAry;
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }
    
    public JSONArray queryItemPriceByYear(String compCode, String itemCode, String year){
    	
    	System.out.println("---queryItemPriceByYear----");
    	
    	StringBuffer sb = new StringBuffer();    	
    	sb.append(" SELECT M.BID_COMP as COMP_CODE, D.BIDD_ITEM, M.BID_ORD_SDATE, M.BID_ORD_EDATE, D.BIDD_PRICE      ");
    	sb.append("   FROM HMP0BIDM M, HMP0BIDD D                                           ");
    	sb.append("  WHERE M.BID_COMP = ?                                               	");
    	sb.append("    AND LEFT(M.BID_ORD_SDATE,4) = ?   	");
    	sb.append("    AND D.BIDD_ITEM  = ? ");
    	sb.append("    AND M.BID_COMP = D.BIDD_COMP                                         ");
    	sb.append("    AND M.BID_UUID = D.BIDM_UUID                                         ");
    	sb.append(" ORDER BY D.BIDD_ITEM, M.BID_ORD_SDATE                                   ");
    	ArrayList<String> parms1 = new ArrayList<String>();
    	parms1.add(compCode);           	
    	parms1.add(year);    	
    	parms1.add(itemCode);
    	System.out.println("queryItemPriceByYear Parms1: " + parms1.toString());
    	
    	
    	JSONArray priceAry = null;
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
        	rdl = pre.getRowDataList(sb.toString(), parms1.toArray());
        	
        	//System.out.println("=!=rdl: " + (rdl!=null));
        	if (null != rdl){
        		priceAry = rdl.toJSONArray();       		      	        		
        		System.out.println("priceAry size: " + priceAry.size());        		        		
        	}        	          
        	return priceAry;
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }
    
    public Hashtable queryItemPriceByPeriod(String comp, String sDate, String eDate){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" SELECT I.IP_UNIT_PRICE unit_price, I.IP_ITEM_CODE item_code, IP_DATE ");
    	sb.append("   FROM HBD1ITEMPRICE I                                        ");
    	sb.append("  WHERE I.IP_COMP_CODE = ?                                  	");    	
    	sb.append("    AND I.IP_DATE between ? and ?                            ");
    	
    	ArrayList<String> parms1 = new ArrayList<String>();
    	parms1.add(comp);           	
    	parms1.add(sDate);
    	parms1.add(eDate);
    	
	
    	Hashtable priceTbl = null;
    	JSONArray priceAry = null;
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
        	rdl = pre.getRowDataList(sb.toString(), parms1.toArray());
        	
        	//System.out.println("=!=rdl: " + (rdl!=null));
        	if (null != rdl){        		
        		priceTbl = new Hashtable();
        		priceAry = rdl.toJSONArray();
        		//System.out.println("priceAry size: " + priceAry.size());
        		for(int i=0; i < priceAry.size(); i++){
        			JSONObject priceJo = priceAry.getJSONObject(i);
        			String priceKey = priceJo.getString("ipDate") + priceJo.getString("ipItemCode");
        			//System.out.println("price JO: " + priceJo.toString());
        			priceTbl.put(priceKey, priceJo.getString("ipUnitPrice"));
        		}        		
        	}        	          
        	return priceTbl;
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
    	
    }
    
    public Hashtable queryItemPriceByDate(String compCode, String date1){
    	
    	//System.out.println("---queryItemPriceByYear----");
    	
    	StringBuffer sb = new StringBuffer();    	
    	sb.append(" SELECT I.IP_UNIT_PRICE unit_price, I.IP_ITEM_CODE item_code	  ");
    	sb.append("   FROM HBD1ITEMPRICE I                                           ");
    	sb.append("  WHERE I.IP_COMP_CODE = ?                                               	");    	
    	sb.append("    AND I.IP_DATE = ?                                          ");    	
    	
    	ArrayList<String> parms1 = new ArrayList<String>();
    	parms1.add(compCode);           	
    	parms1.add(date1);
    	
	
    	Hashtable priceTbl = null;
    	JSONArray priceAry = null;
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
        	rdl = pre.getRowDataList(sb.toString(), parms1.toArray());
        	
        	//System.out.println("=!=rdl: " + (rdl!=null));
        	if (null != rdl){        		
        		priceTbl = new Hashtable();
        		priceAry = rdl.toJSONArray();
        		//System.out.println("priceAry size: " + priceAry.size());
        		for(int i=0; i < priceAry.size(); i++){
        			JSONObject priceJo = priceAry.getJSONObject(i);
        			//System.out.println("price JO: " + priceJo.toString());
        			priceTbl.put(priceJo.getString("ipItemCode"), priceJo.getString("ipUnitPrice"));
        		}
        		
        		
        	}        	          
        	return priceTbl;
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }
    
    /**
     * 當周有設定價格的物料
     * @param compCode
     * @param bidUuid
     * @return
     */
    public JSONArray queryBidValidItemList_bidd(String compCode, String bidUuid){
    	
    	StringBuffer sb = new StringBuffer();    	
    	sb.append(" SELECT 	I.ITEM_NAME, I.ITEM_CODE, BD.BIDD_PRICE,			");
    	sb.append("        	BD.BIDD_COMP, BD.BIDM_UUID, BD.BIDD_UUID, 			");
    	sb.append("			BD.BIDD_ITEM, I.item_Abbr, BD.BIDD_ITEM,			");
    	sb.append("			NZ(I.ITEM_HIDE_PRICE_MK, 'N') AS ITEM_HIDE_PRICE_MK	");
    	sb.append("   FROM 	HMP0ITEM I, HMP0BIDD BD                 			");
    	sb.append("  WHERE 	BD.BIDD_COMP = ?             						");
    	sb.append("    AND 	BD.BIDM_UUID = ?                        			");
    	sb.append("    AND 	BD.BIDD_COMP = I.ITEM_COMP              			");
    	sb.append("    AND 	BD.BIDD_ITEM = I.ITEM_CODE              			");    	
    	sb.append("    AND  NZ(BD.BIDD_PRICE, 0) > 0							");
    	sb.append("ORDER BY I.ITEM_TYPE, I.ITEM_NAME, I.ITEM_SORT, I.ITEM_CODE	");
    	
    	StringBuffer sb2 = new StringBuffer();
    	sb2.append("");

    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(compCode);           	
    	parms.add(bidUuid);
    	
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
        	rdl = pre.getRowDataList(sb.toString(), parms.toArray());
        	if (null != rdl){
        		return rdl.toJSONArray();
        	}else{
        		return null;
        	}        	          
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
    	
    }
    
    public JSONArray queryBidValidItemList(String compCode){
    	
    	StringBuffer sb = new StringBuffer();    	
    	sb.append(" SELECT 	I.ITEM_NAME, 				");    	
    	sb.append("			I.ITEM_CODE, I.item_Abbr, I.ITEM_SORT,  		    ");
    	sb.append("			NZ(I.ITEM_HIDE_PRICE_MK, 'N') AS ITEM_HIDE_PRICE_MK	");
    	sb.append("   FROM 	HMP0ITEM I                 							");
    	sb.append("  WHERE 	I.ITEM_COMP = ?             						");   	    	
    	sb.append("ORDER BY I.ITEM_TYPE, I.ITEM_NAME, I.ITEM_SORT, I.ITEM_CODE	");
    	
    	StringBuffer sb2 = new StringBuffer();
    	sb2.append("");

    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(compCode);
    	
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
        	rdl = pre.getRowDataList(sb.toString(), parms.toArray());
        	
        	if (null != rdl){
        		//System.out.println("sql: " + sb.toString());
        		//System.out.println("rdl: " + rdl.toJSONArray().toString());
        		return rdl.toJSONArray();
        	}else{
        		return null;
        	}        	          
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
    	
    }
    
    public JSONArray queryAppendItemList(String compCode, String buyer, String poDate){
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	StringBuffer sb = new StringBuffer();    	
    	sb.append(" SELECT 	I.ITEM_COMP, I.ITEM_CODE,                  				");
    	sb.append(" 		I.ITEM_NAME, I.ITEM_ABBR,								");
    	sb.append("        	NZ(I.ITEM_HIDE_PRICE_MK, 'N') AS ITEM_HIDE_PRICE_MK,	");
    	sb.append("			P.IP_UNIT_PRICE			");
    	sb.append(" FROM HMP0ITEM I, HBD1ITEMPRICE P                                ");
    	sb.append(" WHERE I.ITEM_COMP = ?                                          	");
    	sb.append("   AND I.ITEM_COMP = P.IP_COMP_CODE                              ");
    	sb.append("   AND I.ITEM_CODE = P.IP_ITEM_CODE                              ");
    	sb.append("   AND P.IP_DATE = ?                                             ");    	
    	sb.append("   AND NZ(P.IP_UNIT_PRICE,0) > 0                                 ");
    	sb.append("   AND I.ITEM_CODE NOT IN 	(									");    	
    	//
    	sb.append(" Select d.podd_item ");		
		sb.append("  From HMP0POMM m, HMP0PODD d                                ");
		sb.append(" Where m.pomm_comp_code = ?                                              ");
		sb.append("   and m.pomm_date = ?                       				");
		sb.append("   and m.pomm_buyer = ?                                         			");		
		sb.append("   and m.pomm_comp_code = d.podd_comp_code                               ");
		sb.append("   and m.pomm_date = d.podd_date                                         ");
		sb.append("   and m.pomm_seq = d.podd_seq                                           ");
    	//   	    	
    	sb.append("   							)									");
    	sb.append("ORDER BY I.ITEM_TYPE, I.ITEM_NAME, I.ITEM_SORT, I.ITEM_CODE		");
    	
    	parms.add(compCode);
    	parms.add(poDate);
    	parms.add(compCode);
    	parms.add(poDate);
    	parms.add(buyer);    	
    	
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
        	rdl = pre.getRowDataList(sb.toString(), parms.toArray());        	
        	if (null != rdl){
        		return rdl.toJSONArray();
        	}else{
        		return null;
        	}        	          
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
    	
    }
    
    public JSONArray queryValidItemList(String compCode, String poDate){
    	
    	StringBuffer sb = new StringBuffer();    	
    	sb.append(" SELECT 	I.ITEM_COMP, I.ITEM_CODE,                  				");
    	sb.append(" 		I.ITEM_NAME, I.ITEM_ABBR,								");
    	sb.append("        	NZ(I.ITEM_HIDE_PRICE_MK, 'N') AS ITEM_HIDE_PRICE_MK,	");
    	sb.append("			P.IP_UNIT_PRICE											");
    	sb.append(" FROM HMP0ITEM I, HBD1ITEMPRICE P                                ");
    	sb.append(" WHERE I.ITEM_COMP = ?                                          	");
    	sb.append("   AND I.ITEM_COMP = P.IP_COMP_CODE                              ");
    	sb.append("   AND I.ITEM_CODE = P.IP_ITEM_CODE                              ");
    	sb.append("   AND P.IP_DATE = ?                                             ");
    	sb.append("   AND NZ(P.IP_UNIT_PRICE,0) > 0                                 ");
    	sb.append("ORDER BY I.ITEM_TYPE, I.ITEM_NAME, I.ITEM_SORT, I.ITEM_CODE		");
    	
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(compCode);
    	parms.add(poDate);
    	
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
        	rdl = pre.getRowDataList(sb.toString(), parms.toArray());
        	
        	if (null != rdl){
        		//System.out.println("sql: " + sb.toString());
        		//System.out.println("rdl: " + rdl.toJSONArray().toString());
        		return rdl.toJSONArray();
        	}else{
        		return null;
        	}        	          
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
    	
    }
    
    public String getBidmUuid(JSONObject jo){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" Select max(bid_uuid) as bidm_uuid  ");
    	sb.append(" From hmp0bidm                      ");
    	sb.append(" Where bid_comp = ?                 ");
    	sb.append("   and bid_ord_Sdate = ?            ");
    	sb.append("   and bid_ord_Edate = ?            ");
    	sb.append("   and bid_sale_Sdate = ?           ");
    	sb.append("   and bid_sale_Edate = ?           ");
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(jo.getString("compCode"));
    	parms.add(jo.getString("txtBidOrdSdate"));
		parms.add(jo.getString("txtBidOrdEdate"));
		parms.add(jo.getString("txtBidSaleSdate"));
		parms.add(jo.getString("txtBidSaleEdate"));
		
		String result = "";
		try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
            result = pre.getAString(sb.toString(), parms.toArray()); 	        	               
        } catch (SQLException e) {
        	result = "";
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
    	
		return result;
    	
    }
    
    /**
     * 霈������
     * @param compCode
     * @param bidUUID
     * @return
     * @throws SQLException
     */
    public RowDataList queryItemList(String compCode, String bidUUID, boolean valid) throws SQLException{
    	
    	//```````````````````123
		StringBuffer sb = new StringBuffer();		
		sb.append(" SELECT 	D.BIDM_UUID, D.BIDD_ITEM, I.ITEM_ABBR,   	");
		sb.append("			D.BIDD_PRICE, I.ITEM_UNIT, I.ITEM_NAME   	");
		sb.append("   FROM 	HMP0BIDD D, HMP0ITEM I                      ");
		sb.append("  WHERE 	D.BIDD_COMP = ?                           	");
		sb.append("    AND 	D.BIDM_UUID = ?                            	");
		sb.append("    AND 	D.BIDD_COMP = I.ITEM_COMP                   ");
		sb.append("    AND 	D.BIDD_ITEM =  I.ITEM_CODE                  ");	       
		if (valid){
			sb.append("AND  NZ(D.BIDD_PRICE,0) > 0						");
		}
		sb.append("ORDER BY I.ITEM_TYPE, I.ITEM_SORT, I.ITEM_CODE		");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(compCode);
		parms.add(bidUUID);
    	        
		//System.out.println("comp: " + compCode);
		//System.out.println("uuid: " + bidUUID);
		
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
        	rdl = pre.getRowDataList(sb.toString(), parms.toArray());
        	return rdl;               
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }
    
    public int addData(JSONObject jo){
    	
    	StringBuffer sb = new StringBuffer();    	
    	sb.append(" Insert into HMP0BIDM(                      	");
		sb.append(" 	BID_COMP, BID_NAME, BID_SALE_MARK ,     ");
		sb.append("		BID_ORD_SDATE, BID_ORD_EDATE,			");
		sb.append(" 	BID_SALE_SDATE, BID_SALE_EDATE)      	");				
		sb.append(" VALUES(?, ?, ?, ?, ?, ?, ?)       			");
    	
		Object[] addParm = new Object[]{
        		jo.getString("compCode"),
        		jo.getString("txtBidName"),        		
        		jo.getString("txtBidSaleMark").replace("[", "").replace("]", "").replace("\"", ""),        		
        		jo.getString("txtBidOrdSdate"),
        		jo.getString("txtBidOrdEdate"),
        		jo.getString("txtBidSaleSdate"),
        		jo.getString("txtBidSaleEdate")
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
    
    public int addBidmData(JSONObject jo){
    	
    	StringBuffer sb = new StringBuffer();    	
    	sb.append(" Insert into HMP0BIDM(                      	");
		sb.append(" 	BID_COMP, BID_NAME, BID_SALE_MARK ,     ");
		sb.append("		BID_ORD_YEAR, BID_ORD_WEEK,				");
		sb.append("		BID_ORD_SDATE, BID_ORD_EDATE,			");
		sb.append(" 	BID_SALE_SDATE, BID_SALE_EDATE)      	");				
		sb.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)       			");
    	
		Object[] addParm = new Object[]{
        		jo.getString("compCode"),
        		jo.getString("txtBidName"),        		
        		jo.getString("txtBidSaleMark").replace("[", "").replace("]", "").replace("\"", ""),
        		jo.getString("BID_ORD_YEAR"),
        		jo.getString("BID_ORD_WEEK"),
        		jo.getString("txtBidOrdSdate"),
        		jo.getString("txtBidOrdEdate"),
        		jo.getString("txtBidSaleSdate"),
        		jo.getString("txtBidSaleEdate")
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
    
    public int addBiddData(String compCode, JSONObject jo){    	
    		
    	StringBuffer isb = new StringBuffer();    	
    	isb.append(" Insert into HMP0BIDD 							");
    	isb.append("  (bidd_comp, bidm_uuid, bidd_item, bidd_price)   ");
    	isb.append(" Values(?, ?, ?, ?)      		");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(compCode);
		parms.add(jo.getString("bidmUuid"));        		
		parms.add(jo.getString("biddItem"));
		parms.add(jo.getString("biddPrice"));
		
		//System.out.println("add: " + jo.toString());
		
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            cnt = pre.updateSql(isb.toString(), parms.toArray());            
        } catch (SQLException e) {
            cnt = 0;
          	throw new DataAccessException(e);
        }    	
    	
    	return cnt;
    	
    }
    
    /**
     * �甈⊥憓����(�靘�)������敦鋆�
     * @param compCode
     * @param jo
     * @return
     */
    public int addBatchBiddData(String compCode, String itemCode, String itemPrice){    	
		
    	StringBuffer sb = new StringBuffer();    	
    	sb.append(" Insert into HMP0BIDD(BIDD_COMP, BIDM_UUID, BIDD_ITEM, BIDD_PRICE) ");
    	sb.append(" SELECT BID_COMP, BID_UUID, ?, ?                              ");
    	sb.append("   FROM HMP0BIDM                                                   ");
    	sb.append("  WHERE BID_COMP = ?                                    ");
    	sb.append("    AND (BID_ORD_SDATE >= FORMAT(NOW(), 'yyyymmdd')                ");
    	sb.append("        OR                                                         ");
    	sb.append("         BID_ORD_EDATE >= FORMAT(NOW(), 'yyyymmdd')                ");
    	sb.append("        )                                                          ");

    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(itemCode);
    	parms.add(itemPrice);
    	parms.add(compCode);		
		
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            cnt = pre.updateSql(sb.toString(), parms.toArray());            
        } catch (SQLException e) {
            cnt = 0;
          	throw new DataAccessException(e);
        }    	
    	
    	return cnt;
    	
    }
    
    public int updateData(JSONObject jo){
    	
    	//System.out.println("execute sql: " + jo.toString());
    	
        StringBuffer sb = new StringBuffer();            
        sb.append(" Update HMP0BIDM              ");
        sb.append("    set BID_NAME = ?,         ");
        sb.append("        BID_ORD_SDATE = ?,    ");
        sb.append("        BID_ORD_EDATE = ?,    ");
        sb.append("        BID_SALE_SDATE = ?,   ");
        sb.append("        BID_SALE_EDATE = ?,   ");
        sb.append("        BID_SALE_MARK = ?     ");
        sb.append(" WHERE  BID_UUID = ?          ");
        sb.append("   AND  BID_COMP = ?          ");

        Object[] updParm = new Object[]{
        		jo.getString("txtBidName"),
        		jo.getString("txtBidOrdSdate"),
        		jo.getString("txtBidOrdEdate"),
        		jo.getString("txtBidSaleSdate"),
        		jo.getString("txtBidSaleEdate"),        		
        		jo.getString("txtBidSaleMark").replace("[", "").replace("]", "").replace("\"", ""),       		        		
        		jo.getString("txtBidUuid"),
        		jo.getString("compCode")
        };
        
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.updateSql(sb.toString(), updParm);            
        } catch (SQLException e) {
            cnt = 0;
            e.toString();
          	throw new DataAccessException(e);
        }
    	
    	return cnt;
    	
    }
    
    /**
     * 
     * @param mastJo
     * @param itemJo
     * @return
     */
    public int updateOtherFuturePOPrice(JSONObject mastJo, JSONObject itemJo){
    	
    	//System.out.println("execute sql: " + jo.toString());
    	
        StringBuffer sb = new StringBuffer();            
        sb.append(" UPDATE HMP0PODD                 ");
        sb.append("    SET PODD_UNIT_PRICE = ?   	");
        sb.append("  WHERE PODD_COMP_CODE  = ?    	");
        sb.append("    AND PODD_ITEM = ?         	");
        sb.append("    AND NZ(PODD_RCVD_QTY,0) = 0  "); // 已有驗收紀錄的也不更新
        sb.append("    AND PODD_DATE >= ?        	"); // today, 過去的歷史資料不更新
        sb.append("    AND PODD_DATE >= ?     		"); // sdate, 從該張預訂單以後的資料開始更新

        ArrayList<String> parms = new ArrayList<String>();
        
        parms.add(itemJo.getString("txtBiddPrice")); // 
        parms.add(mastJo.getString("compCode")); //
        parms.add(itemJo.getString("txtBiddItem")); // 
        parms.add(UTIL_String.getNowString("yyyyMMdd")); // today
        parms.add(mastJo.getString("setBidOrdSdate")); // sdate
        
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.updateSql(sb.toString(), parms.toArray());
            //System.out.println("updateOtherFuturePrice: " + cnt); 
        } catch (SQLException e) {
            cnt = 0;
            e.toString();
            
          	throw new DataAccessException(e);
        }
    	
    	return cnt;
    	
    }
    
    public int updateOtherFutureBiddPrice(JSONObject mastJo, JSONObject itemJo){
    	
    	//System.out.println("execute sql: " + jo.toString());
    	
        StringBuffer sb = new StringBuffer();            
        sb.append(" UPDATE HMP0BIDD               ");
        sb.append("    SET BIDD_PRICE = ?         "); //1
        sb.append("  WHERE BIDD_COMP = ?          "); //2
        sb.append("    AND BIDD_ITEM = ?          "); //3
        sb.append("    AND BIDM_UUID IN (         ");
        sb.append(" SELECT BID_UUID               ");
        sb.append("   FROM HMP0BIDM               ");
        sb.append("  WHERE BID_COMP = ?           "); // 4
        sb.append("    AND BID_ORD_SDATE >= ?     "); // 起日要比今日大
        sb.append("    AND BID_ORD_SDATE >= ?     "); // 起日要比當設定那周的迄日大
        sb.append("    )                          ");

        ArrayList<String> parms = new ArrayList<String>();
        
        parms.add(itemJo.getString("txtBiddPrice")); // 1
        parms.add(mastJo.getString("compCode")); //2
        parms.add(itemJo.getString("txtBiddItem")); //3 
        parms.add(mastJo.getString("compCode")); // 4
        parms.add(UTIL_String.getNowString("yyyyMMdd")); // today
        parms.add(mastJo.getString("setBidOrdEdate")); // edate
        
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.updateSql(sb.toString(), parms.toArray());
            //System.out.println("updateOtherFuturePrice: " + cnt); 
        } catch (SQLException e) {
            cnt = 0;
            e.toString();
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }
    	
    	return cnt;
    	
    }
    
    public int updateBiddItemData(JSONObject jo){
    	
        StringBuffer sb = new StringBuffer();            
        sb.append(" Update HMP0BIDD         ");
        sb.append("    set BIDD_PRICE = ?    ");        
        sb.append(" WHERE  BIDD_COMP = ?     ");
        sb.append("   AND  BIDM_UUID = ?	");
        sb.append("   AND  BIDD_UUID = ?     ");
        sb.append("   AND  BIDD_ITEM = ?     ");
        

        Object[] updParm = new Object[]{
        		jo.getString("txtBiddPrice"),
        		jo.getString("txtBiddComp"),
        		jo.getString("txtBidmUuid"),
        		jo.getString("txtBiddUuid"),        		
        		jo.getString("txtBiddItem")
        };
        
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.updateSql(sb.toString(), updParm);            
        } catch (SQLException e) {
            cnt = 0;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }
    	
    	return cnt;
    	
    }
    
    public int deleteBIDM(String compCode, String bidUUID){
    	
        StringBuffer sb = new StringBuffer();            
        sb.append(" DELETE FROM HMP0BIDM  ");
		sb.append(" WHERE BID_COMP = ?   ");
		sb.append("   AND BID_UUID = ?   ");
		Object[] delParm = new Object[]{
        		compCode,
        		bidUUID
        };           
        
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.updateSql(sb.toString(), delParm);            
        } catch (SQLException e) {
        	e.printStackTrace();
            cnt = 0;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }
    	
    	return cnt;
    	
    }
    
    public int deleteBIDDByBIDM(String compCode, String bidmUUID){
    	
        StringBuffer sb = new StringBuffer();            
        sb.append(" DELETE FROM HMP0BIDD  ");
		sb.append(" WHERE BIDD_COMP = ?   ");
		sb.append("   AND BIDM_UUID = ?   ");
		Object[] delParm = new Object[]{
        		compCode,
        		bidmUUID
        };           
        
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.updateSql(sb.toString(), delParm);            
        } catch (SQLException e) {
        	e.printStackTrace();
            cnt = 0;
            logger.error(e.toString());
          	throw new DataAccessException(e);
        }
    	
    	return cnt;
    	
    }
    
    @SuppressWarnings("unchecked")
	public JSONArray queryAllBidmByComp(String comp){
		
    	
    	// System.out.println("queryAllBidmByComp: " + comp);
    	StringBuffer sb = new StringBuffer();		
		sb.append(" SELECT bid_uuid, bid_comp, bid_ord_sdate,       		");
		sb.append("        bid_ord_edate, bid_sale_sdate,           		");
		sb.append("		   bid_sale_edate, bid_sale_mark, bid_name  		");		
		sb.append("   FROM hmp0bidm                                 		");
		sb.append("  WHERE bid_comp = ?                   					");
		sb.append("  ORDER BY BID_ORD_SDATE                                ");
		
		StringBuffer ub1 = new StringBuffer();
		ub1.append(" UPDATE HMP0BIDM ");
		ub1.append("    SET BID_ORD_SDATE = ? ");
		ub1.append("       ,BID_ORD_EDATE = ? ");
		ub1.append("       ,BID_SALE_SDATE = ? ");
		ub1.append("       ,BID_SALE_EDATE = ? ");
		ub1.append("  Where BID_COMP = ? ");
		ub1.append("    AND BID_UUID = ? ");
		
		StringBuffer ub2 = new StringBuffer();
		ub2.append(" UPDATE HMP0BIDM ");
		ub2.append("    SET BID_NAME = ? ");
		ub2.append("  WHERE BID_COMP = ? ");
		ub2.append("    AND BID_UUID = ? ");
		
		ArrayList<String> parms = new ArrayList<String>();
		parms.add(comp);
		
		
		JSONArray ary = null;
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
            RowDataList rdl = pre.getRowDataList(sb.toString(), parms.toArray());
            
            ary = rdl.toJSONArray();
            
            ary.forEach(obj -> {
                JSONObject item = (JSONObject) obj;                
                //System.out.println("ori Date: " + item.getString("bidOrdSdate"));
                try {
                	String bidUUID = item.getString("bidUuid");
                	String ordDate1 = item.getString("bidOrdSdate");
                	String ordDate2 = item.getString("bidOrdEdate");
                	String weekDay1 = UTIL_String.getWeekOfDate(ordDate1);
                	String weekDay2 = UTIL_String.getWeekOfDate(ordDate2);
                	String bidName = UTIL_String.getDateFormat(UTIL_String.getAddDay(ordDate1, 0, 1), 6);
                	bidName += "(" + weekDay1 + ")~";
                	bidName += UTIL_String.getDateFormat(UTIL_String.getAddDay(ordDate2, 0, 1), 6);
                	bidName += "(" + weekDay2 + ")";
                	
                	// UB1
                	//String newDate1 = UTIL_String.getDateFormat(UTIL_String.getAddDay(oldDate, 1, 1), 1);
                	//String newDate2 = UTIL_String.getDateFormat(UTIL_String.getAddDay(newDate1, 6, 1), 1);
                	//String sDate1 = UTIL_String.getDateFormat(UTIL_String.getAddDay(newDate1, -7, 1), 1);
                	//String sDate2 = UTIL_String.getDateFormat(UTIL_String.getAddDay(newDate1, -3, 1), 1);
                	//String info = String.format("%s(%s)/%s(%s)-%s(%s), s: %s-%s", oldDate, UTIL_String.getWeekOfDate(oldDate), newDate1, UTIL_String.getWeekOfDate(newDate1), newDate2, UTIL_String.getWeekOfDate(newDate2), sDate1, sDate2);
                	
                	
                	//System.out.println(info);
                	
                	// UB1
                	/*
                	ArrayList<String> up1 = new ArrayList<String>();
                	up1.add(newDate1);
                	up1.add(newDate2);
                	up1.add(sDate1);
                	up1.add(sDate2);
                	up1.add(comp);
                	up1.add(bidUUID);
                	
                	int ui = pre.updateSql(ub1.toString(), up.toArray());
                	*/
                	
                	
                	
                	ArrayList<String> up2 = new ArrayList<String>();
                	up2.add(bidName);
                	up2.add(comp);
                	up2.add(bidUUID);
                	
                	int ui = pre.updateSql(ub2.toString(), up2.toArray());
                	
                	//System.out.println(ui + " : " + bidName);
                	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e.toString());
				}                
                		
                
            });
        	               
        } catch (SQLException e) {
        	e.printStackTrace();
        	logger.error(e.toString());
            throw new DataAccessException(e);
        }
        
        return ary;
		
	}

    
}