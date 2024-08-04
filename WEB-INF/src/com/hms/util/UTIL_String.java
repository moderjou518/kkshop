/**
 * 
 */

package com.hms.util;

import java.io.UnsupportedEncodingException;
/**
 * @author 290098
 *
 */
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;

import com.hms.entity.HMS_TBLE;
import com.hms.entity.HMS_TROW;
import com.hms.util.hms2.Msg;

/**
 * 處理字串的公用程式
 * @author Moder Jou
 *
 *<pre>
 *CREATE DATE : 2009/06/05
 *<b>NOTES</b>       :
 *     A:  EXTRA LIBRARY
 *
 *     B:  REMARK
 *         20090605    
 * </pre>
 */

public class UTIL_String {

	private static String dateStyle = "yyyy/MM/dd HH:mm:ss:SSS 'GMT'Z";//"yyyyMMdd";    	

	/**
	 * 取代原本為null的字串
	 * @param source 原本的字串
	 * @param replace 被置換的字串
	 * @return String
	 */
	public static String nvl( String source , String replace ) {
		if ( source == null) {
			source = replace;
		}else{
			if (source.length() == 0){
				source = replace;
			}
		}
		return source;
	}

	public static BigDecimal nvl( BigDecimal source , BigDecimal replace ) {
		if ( source == null ) {
			source = replace;
		}
		return source;
	}

	public static String commaNumber( BigDecimal s ) {
		DecimalFormat df = new DecimalFormat( "##,###" );
		return df.format( s );
	}

	public static String commaString( String s ) {
		DecimalFormat df = new DecimalFormat( "##,###" );
		return df.format( new BigDecimal( s ) );
	}

	public static String getNowString() {
		return getNowString( 0 , "yyyyMMddHHmm" );
	}

	public static String getNowString( String dFormat ) {
		if ( "".equals( dFormat ) ) {
			dFormat = "yyyyMMddHHmm";
		}
		return getNowString( 0 , dFormat );
	}
	
	public static String getClinicID(String ecID){

		ecID = UTIL_String.nvl(ecID, "99999999");
		
		String cID = String.valueOf(99999999 - Integer.parseInt(ecID));
		
		if (cID.length() != 8){
			for(int i=cID.length(); i <= 8; i++){
				cID = "0" + cID;
			}
		}
		
		return cID;
		
	}
	
	/**
     * 每月有幾天
     * yyyyMMdd, weekDay
     * @param yyyyMM
     * @return
     * @throws ParseException 
     */
    public static JSONArray getMonthDays(String yyyyMM) throws ParseException{
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        //進行轉換
        Date qryDate = sdf.parse(yyyyMM + "01");        
        
        // 取得特定月份的天數
        Calendar cal = Calendar.getInstance();
        cal.setTime(qryDate);        
        int cntDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);        
        
        JSONArray ary = new JSONArray();
        for(int i=1; i <= cntDay; i++){
            String dayStr = "";
            if (i < 10){
                dayStr = "0" + String.valueOf(i);
            }else{
                dayStr = String.valueOf(i);
            }
            String qryDay = yyyyMM + dayStr;            
            JSONObject dayJo = new JSONObject();
            dayJo.put("shortMonthDay", dayStr);
            dayJo.put("monthDay", qryDay);
            ary.add(i-1, dayJo);
        }        
        
        
        return ary;
        
    }
	
	public static String getCompID(String ecID){

		ecID = UTIL_String.nvl(ecID, "9999999999");				
		String cID = String.valueOf(9999999999L - Long.parseLong(ecID));	
		
		//System.out.println("ecID: " + ecID);
		//System.out.println("cID1: " + cID);
		
		if (cID.length() != 10){
			for(int i=cID.length(); i <= 10; i++){
				cID = "0" + cID;
			}
		}		
		
		//System.out.println("cID2: " + cID);
		return cID;		
	}

	public static String getNowString( int addDays , String dFormat ) {

		Date dt = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime( dt );
		cal.add( Calendar.DATE , addDays );

		// dFormat: yyyyMMdd, yyyyMMddHHmm, yyyyMMddHHmmss
		SimpleDateFormat format = new SimpleDateFormat( dFormat );
		String newDate = format.format( cal.getTime() ).toString();

		return newDate;

	}
	
	// 取得現在時間，格式:yyyyMMddHHmm
	public static String getNowTime(int addMinutes){		
		
		Date targetTime = new Date(); //now
		targetTime = DateUtils.addMinutes(targetTime, addMinutes); //add minute
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		String newTime = format.format( targetTime.getTime() ).toString();
		
		return newTime;
		
	}

	public static String getCurrentDate1( String formats ) {

		Date dt = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime( dt );
		SimpleDateFormat format = new SimpleDateFormat( formats );
		String newDate = format.format( cal.getTime() ).toString();

		return newDate;
	}

	/**
	 * 取得未來的時間，可指定格式
	*/
	public static String getFutureString( int addDay ) {

		String s = "";
		// get a calendar instance, which defaults to "now"
		Calendar calendar = Calendar.getInstance();

		// get a date to represent "today"
		Date today = calendar.getTime();

		// add one day to the date/calendar
		calendar.add( Calendar.DAY_OF_YEAR , addDay );

		// now get "tomorrow"
		Date futureDate = calendar.getTime();

		SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMdd" );
		s = formatter.format( futureDate );

		return s;
	}
	
	/**
     * 獲取兩個日期之間的所有日期
     * 
     * @param startTime
     *            開始日期
     * @param endTime
     *            結束日期
     * @return
     */
    public static List<String> getDays(String startTime, String endTime) {

        // 返回的日期集合
        List<String> days = new ArrayList<String>();

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含結束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }


	/**
	 * 將傳入的javascript字串中特殊符號字元跳脫，如單引號'
	 * @param s
	 * @return String
	 */
	public static String jScriptEscape( String s ) {
		return StringEscapeUtils.escapeJavaScript( s );
	}

	/**
	 * 將傳入的html字串中特殊符號字元跳脫，如<,>,&.....
	 * @param s
	 * @return String
	 */
	public static String htmlEscape( String s ) {
		return StringEscapeUtils.escapeHtml( s );
	}

	/**
	 * 切割出字串的某一段，從第frmIndex到第endIndex前一個字
	 * @param
	 *  allStr       要被切割的字串
	 *  frmIndex     從這個字串的那個位置開始切
	 *  endIndex     切到第幾個字
	 *
	 */
	public static String subString( String allStr , int frmIndex , int endIndex ) {
		return StringUtils.substring( allStr , frmIndex , endIndex );
	}

	public static String subString( String allStr , int frmIndex ) {
		return StringUtils.substring( allStr , frmIndex );
	}

	/**
	 * 依分割字元，分割字串為array
	 * @param allStr
	 * @param splitChars
	 * @return
	 */
	public static String[] split( String allStr , String splitChars ) {
		return StringUtils.split( allStr , splitChars );
	}

	/**
	 * 將字串左邊補上要填入的字元
	 * @param
	 *  str       要轉的字串
	 *  size      補完後的長度
	 *  padStr    要補的字元
	 */
	public static String leftPad( String str , int size , String padStr ) {
		return StringUtils.leftPad( str , size , padStr );
	}

	/**
	 * 將字串轉整數，不為字串時會回傳0
	 * @param
	 *  str       要轉的字串
	 */
	public static int stringToInt( String str ) {
		return NumberUtils.toInt( str );
	}

	/*
	 * 設定輸出日期格式
	 * @param
	 *    i    0:2007/01/15 14:48:32:184
	 *         1:20070115
	 *         2:20070115235959
	 *         3:200701152359
	 */
	private static SimpleDateFormat setDateStyle( int i ) {
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		if ( i == 0 ) { // default
			dateFormat.applyPattern( dateStyle );
		} else if ( i == 1 ) { // 年月日
			dateFormat.applyPattern( "yyyyMMdd" );
		} else if ( i == 2 ) {
			dateFormat.applyPattern( "yyyyMMddHHmmss" );
		} else if ( i == 3 ) {
			dateFormat.applyPattern( "yyyyMMddHHmm" );
		} else if ( i == 4 ) {
			dateFormat.applyPattern( "yyyyMMddHHmmssSSS" );
		} else if ( i == 5 ) {
			dateFormat.applyPattern( "yyyy" );
		} else if ( i == 6 ) {
			dateFormat.applyPattern("MM/dd");
		}
		return dateFormat;
	}

	/*
	 * 回傳輸入時間，轉為指定的格式
	 */
	public static String getDateFormat( Date dd , int i ) {
		if ( dd == null ) {
			dd = new Date();
		}
		return setDateStyle( i ).format( dd ).toString();
	}

	/*
	 * 回傳輸入時間，轉為預設的格式
	 */
	public static String getDateFormat() {
		return getDateFormat( null , 0 );
	}

	/*
	 * 依現在時間，轉為指定系統提供的格式
	 */
	public static String getDateFormat( int i ) {
		return getDateFormat( new Date() , i );
	}

	/*
	 * 將數字時間格式化成時間字串yyyy/MM/dd hh24:mi
	 * 
	 */

	public static String getDateFormat( BigDecimal dateNumber ) {

		String val = "";

		if ( dateNumber != null && !"0".equals( dateNumber.toString() ) ) {

			String dateNumberStr = dateNumber.toString();
			val = dateNumberStr.substring( 0 , 4 );
			val = val + "/" + dateNumberStr.substring( 4 , 6 );
			val = val + "/" + dateNumberStr.substring( 6 , 8 );
			val = val + " " + dateNumberStr.substring( 8 , 10 );
			val = val + ":" + dateNumberStr.substring( 10 , 12 );
		}

		return val;

	}

	// 只要顯示時間
	public static String getDateFormat( BigDecimal dateNumber , String style ) {

		String val = "";

		if ( dateNumber != null && !"0".equals( dateNumber.toString() ) ) {

			String dateNumberStr = dateNumber.toString();
			if ( "HHmm".equals( style ) ) {
				val = val + " " + dateNumberStr.substring( 8 , 10 );
				val = val + ":" + dateNumberStr.substring( 10 , 12 );
			}

		}

		return val;

	}
	
	/**
	 * 計算2個日期差幾天
	 * @param sDateStr
	 * @param eDateStr
	 * @return
	 */
	public static long getDateDiff(String sDateStr, String eDateStr){
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		java.util.Date sDate = null;
		java.util.Date eDate = null;
		try {
			sDate = df.parse(sDateStr);
			eDate = df.parse(eDateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long dayDiff = (eDate.getTime() - sDate.getTime()) / (24 * 60 * 60 * 1000);
		
		return dayDiff;
		
	}
	
	/**
	 * 將日期字串轉成星期
	 * @param srcDate
	 * @return
	 */
	public static String getWeekOfDate(String srcDate) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");// 定義日期格式  
        Date date = null;  
        try {  
            date = format.parse(srcDate.replace("-", "").replace("/", ""));// 將字串轉換為日期  
        } catch (ParseException e) {  
            System.out.println("輸入的日期格式不合理!");  
        }  
        
		String[] weekDays = {"日", "一", "二", "三", "四", "五", "六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/*
	 * 依指定時間字串，回傳Date Object
	 */
	public static Date getAsignDate( String dateStr , int i ) throws Exception {
		return setDateStyle( i ).parse( dateStr );
	}

	/*
	 * 依日期8碼字串，增加天數
	 * @param
	 *  dd          ex:20060101
	 *  interval    間隔天數
	 *  i           要顯示的日期格式
	 */
	public static Date getAddDay( String dateStr , int addDays , int styleIdx ) throws Exception {
		SimpleDateFormat dateFormat = setDateStyle(styleIdx);
		Calendar cal = Calendar.getInstance();
		cal.setTime( dateFormat.parse( dateStr ) );
		cal.add( Calendar.DATE , addDays);
		return cal.getTime();
	}
	
	public static String HTMLFilter(String message) {

        if (message == null)
            return (null);

        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuffer result = new StringBuffer(content.length + 50);
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
            case '<':
                result.append("&lt;");
                break;
            case '>':
                result.append("&gt;");
                break;
            case '&':
                result.append("&amp;");
                break;
            case '"':
                result.append("&quot;");
                break;
            default:
                result.append(content[i]);
            }
        }
        return (result.toString());

    }
	
	
	
	public static String getRemortIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");

	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	public static String toString(boolean success, String msg, JSON data) {
	    Msg rm = new Msg(success, msg, data);
	    return rm.getJSONString();
	}
	
	public static String toString(boolean success, String msg, String jsonStr) {
	    Msg rm = new Msg(success, msg, jsonStr);
	    return rm.getJSONString();
	}
	
	public static List<LocalDate> getDatesBetweenDate(String s, String e) {
		//s = "2014-05-01";
		//e = "2014-05-10";
		String sDate = s.substring(0,4) + "-" + s.substring(4,6) + "-" + s.substring(6,8);
		String eDate = e.substring(0,4) + "-" + e.substring(4,6) + "-" + e.substring(6,8);
		
		//System.out.println("sDate: " + sDate);
		//System.out.println("eDate: " + eDate);
		
		
		LocalDate start = LocalDate.parse(sDate);
		LocalDate end = LocalDate.parse(eDate);
		//System.out.println("1");
		List<LocalDate> totalDates = new ArrayList<LocalDate>();
		while (!start.isAfter(end)) {			
			//System.out.println(start);
		    totalDates.add(start);
		    start = start.plusDays(1);    
		}
		
		
		
		//System.out.println("2");
		
		return totalDates;
	}
	
	public static List<LocalDate> getValidDatesBetweenDate(String s, String e) {
		//s = "2014-05-01";
		//e = "2014-05-10";
		String sDate = s.substring(0,4) + "-" + s.substring(4,6) + "-" + s.substring(6,8);
		String eDate = e.substring(0,4) + "-" + e.substring(4,6) + "-" + e.substring(6,8);	
		
		LocalDate today = LocalDate.now();
		LocalDate start = LocalDate.parse(sDate);
		LocalDate end = LocalDate.parse(eDate);
		
		List<LocalDate> totalDates = new ArrayList<LocalDate>();
		while (!start.isAfter(end)) {
			totalDates.add(start);
			if (start.isAfter(today)) { // 判断当前日期是否大于今天的日期
		         // 如果是，则添加到列表中		        
		    }else{		    	
		    }
		    start = start.plusDays(1);    
		}
		
		
		
		//System.out.println("2");
		
		return totalDates;
	}

	
	public void setCookie(HttpServletResponse response, String cookieName, String cookieVal) throws UnsupportedEncodingException {		
		Cookie url = new Cookie(cookieName, URLEncoder.encode(cookieVal, "UTF-8"));
		url.setMaxAge(60*60*24);		  
		response.addCookie(url);
		response.setContentType("text/html;charset=UTF-8");		
	}
	

	public static String getCookie(HttpServletRequest request, String cookieName) {		
		String cookieVal = "";			
		Cookie[] cookies = request.getCookies();
		for(Cookie c: cookies){
			if(cookieName.equals(c.getName())){
				cookieVal = URLDecoder.decode(c.getValue());      
			}
		}		
		return cookieVal;		
	}



}
