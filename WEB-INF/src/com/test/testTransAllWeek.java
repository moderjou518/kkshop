package com.test;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.evergreen_hotels.bmg.whms1.util.PrepareExecuteSql;
import com.evergreen_hotels.bmg.whms1.util.RowData;
import com.hms.web.hbd1.dao.HMP0BID_DAO;
import com.hms.web.hbd1.dao.HMP0ITEM_DAO;
import com.hms.web.hbd1.dao.HMS0MISC_DAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.hms.web.HMS_WEBACTION;
import com.hms.web.hbd1.dao.HMP0BID_DAO;
import com.hms.web.hbd1.dao.HMP0POMM_DAO;
import com.hms.web.hbd1.dao.HMS0MISC_DAO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 
 * 產生年度預訂單主檔
 * 訂單起迄：1到日
 * 銷售起迄：上周1到5
 * @author Window10
 *
 */
public class testTransAllWeek {


    public static void main(String[] args) throws Exception {   	    	
   
    	// 轉檔年度
        int parmYear = 2024;
        String parmComp = "1122334455";
        
        // 當年度有幾周
        int weekCnt = getWeekCnt(parmYear);        
        for(int i=1; i <= weekCnt; i++){
        	// 產生當周預訂單主檔
            //getBidmObject(parmYear, i);
        }        
        
		PrepareExecuteSql pes = new PrepareExecuteSql();
		try {
			pes.setConnection(UTIL_GlobalConfig.getTestConnection());			
			HMP0BID_DAO bidDao = new HMP0BID_DAO(pes.getConnection());
			
			// 目前全部商品
			//HMP0ITEM_DAO itemDao = new HMP0ITEM_DAO(pes.getConnection());		
			//JSONArray itmAry = itemDao.queryItemList(parmComp, "*");
			
			// 產生預訂單主檔/明細
			for(int i=1; i <= weekCnt; i++){
				// 計算預訂單主檔
	            JSONObject jo = getBidmObject(parmYear, i);
	            jo.put("compCode", parmComp);
	            jo.put("BID_ORD_YEAR", parmYear);
	            jo.put("BID_ORD_WEEK", i);
	            bidDao.addBidmData(jo);	            
	            System.out.println(i + ":" + jo.toString());
	            
	            /*
	             * String bidmID = bidDao.getBidmUuid(jo);
	             * 預訂單明細
	            int ic = 0;	            
            	for(int j=0; j < itmAry.size(); j ++){
            		JSONObject itm = itmAry.getJSONObject(j);	                		
            		itm.put("bidmUuid", bidmID);
            		itm.put("biddItem", itm.getString("itemCode"));
            		itm.put("biddPrice", UTIL_String.nvl(itm.getString("itemPrice"), "0"));           		            		
            		ic += bidDao.addBiddData(parmComp, itm);
            	}
            	System.out.println("ic: " + ic);
            	*/
	        }
    		
		} catch (Exception ex) {			
			ex.printStackTrace();
		} finally {
			pes.closeConnection();
		}	
        
    }


    /**
     * 計算當周預訂單主檔相關欄位(日期、標題)
     * @param year 年
     * @param week 周
     * @return
     */
    public static JSONObject getBidmObject(int year, int week){
        
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR,  week);
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // 星期日        
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        
        DateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        DateFormat sf = new SimpleDateFormat("M/d", Locale.getDefault());
        String bdDate1 = "", bdDate2 = "", osDate1 = "", osDate2 = "";
        String subject = "";

        // 訂單日期起日
        c.add(Calendar.DATE, 1); // 星期一
        bdDate1 = df.format(c.getTime());
        subject = sf.format(c.getTime()) + "(一)";
        
        // 訂單日期迄日
        c.add(Calendar.DATE, 6); // 星期日
        bdDate2 = df.format(c.getTime());
        subject = subject + "-" + sf.format(c.getTime()) + "(日)";
        
        // 上架日期起迄日 1228~1231 -> 26~29
        c.add(Calendar.DATE, -10); // 上周四
        osDate2 = df.format(c.getTime());
        c.add(Calendar.DATE, -3); // 上周一
        osDate1 = df.format(c.getTime());
        
       
        
        JSONObject jo = new JSONObject();               
		jo.put("txtBidName", String.format("%-11s", subject));        		
		jo.put("txtBidSaleMark", "Y");        		
		jo.put("txtBidOrdSdate", bdDate1);
		jo.put("txtBidOrdEdate", bdDate2);
		jo.put("txtBidSaleSdate", osDate1);
		jo.put("txtBidSaleEdate", osDate2);
		
		System.out.println(String.format("%d#%2d: %-11s , ordDate: %s-%s, saleDate: %s-%s", year, week, subject, bdDate1, bdDate2, osDate1, osDate2));
		
		
        
        //Insert into HMP0BIDM(BIDM_COMP, BIDM_YEAR, BIDM_WEEK, BIDM_ORD_DATE1, BIDM_ORD_DATE2, BIDM_SDATE1, BIDM_SDATE2, BIDM)

        return jo;
        
    }
    
    /**
     * 获取某周得第一天
     * @param year 年
     * @param week 周
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week){
        Calendar c = new GregorianCalendar();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR,  week);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c.getTime();
    }

    /**
     * 获取某周得最后一天
     * @param year 年
     * @param week 周
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week){
        Calendar c = new GregorianCalendar();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR,  week);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 5);
        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);

        return c.getTime();
    }
    
    /**
     * 一年有幾周
     * @param parmYear
     * @return
     */
    public static int getWeekCnt(int parmYear) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, parmYear);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);

        int ordinalDay = cal.get(Calendar.DAY_OF_YEAR);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1; // Sunday = 0
        int numberOfWeeks = (ordinalDay - weekDay + 10) / 7;
        System.out.println(parmYear + " : " + numberOfWeeks);
        
        return numberOfWeeks;        
    }

}