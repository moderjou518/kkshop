package com.hms.web.hrs1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.hms.entity.HMS_TBLE;
import com.hms.entity.HMS_TROW;
import com.hms.util.HMS_PrepareExecSQL;
import com.hms.util.UTIL_GlobalConfig;
import com.hms.util.UTIL_String;
import com.hms.web.HMS_WEBACTION;

import net.sf.json.JSONObject;

/**
 * HRS1W010 / HRS1W020 JSP 不同但共用同1個ACTION 
 * @author Window10
 *
 */
public class HRS1W000_ACTION implements HMS_WEBACTION {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	public boolean dosSignTrustCheck(HttpServletRequest request, HttpServletResponse response, boolean chkAll) throws FileNotFoundException, Exception {
		
		
		
		String os = "";
        String browser = "";
       
        String  browserDetails  =   request.getHeader("User-Agent");
        String  userAgent       =   browserDetails;
        String  user            =   userAgent.toLowerCase();

        //System.out.println("User Agent for the request is===>" + browserDetails);

        //=================OS==============================
        if(userAgent.toLowerCase().indexOf("windows") >= 0 ){
            os = "Windows";
        }else if(userAgent.toLowerCase().indexOf("mac") >= 0){
            os = "Mac";
        }else if(userAgent.toLowerCase().indexOf("x11") >= 0){
            os = "Unix";
        }else if(userAgent.toLowerCase().indexOf("android") >= 0){
            os = "Android";
        }else if(userAgent.toLowerCase().indexOf("iphone") >= 0){
            os = "IPhone";
        }else{
            os = "UnKnown, More-Info: " + userAgent;
        }
         
        //===============Browser===========================
        if(user.contains("msie")){
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        }else if (user.contains("safari") && user.contains("version")){
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        }else if ( user.contains("opr") || user.contains("opera")){
            if(user.contains("opera")){
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            }else if(user.contains("opr")){
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
            }
        }else if (user.contains("chrome")){
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        }else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) ){
            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
            browser = "Netscape-?";
        }else if (user.contains("firefox")){
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        }else if(user.contains("rv")){
            browser="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
        }else{
            browser = "UnKnown, More-Info: " + userAgent;
        }
       
        String bw = UTIL_String.nvl(request.getParameter("bw"), "1");
        String bh = UTIL_String.nvl(request.getParameter("bh"), "1");
        String cw = UTIL_String.nvl(request.getParameter("cw"), "1");
        String ch = UTIL_String.nvl(request.getParameter("ch"), "1");                
        
        logger.info("OS======> " + os);
        logger.info("BS======> " + browser);
        if (chkAll == true){
        	//logger.info("BW======> " + bw);
            //logger.info("BH======> " + bh);
            logger.info("CW======> " + cw);
            logger.info("CH======> " + ch);	
        }                
                
        boolean result = false;        
		HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();
		String clinicID = UTIL_String.getClinicID(UTIL_String.nvl(request.getParameter("cID"), "99999999"));
		
		//logger.debug("open connection ...");
        try {
			dbt01.openConnection("HMS");
			StringBuffer sb = new StringBuffer();
			sb.append("	Select	'x'	");			
			sb.append("	  From 	HMS0MISC				");
			sb.append("  Where	MISC_COMP = ? ");			
			sb.append("    and  MISC_KIND = 'SIGN_TRUST'  ");
			sb.append("    and  MISC_VAL =  ?  "); // os
			sb.append("    and  MISC_DESC = ?  "); // browser
			Object[] parm = null;
			
			//logger.debug("chk: " + chkAll);
			if (chkAll == true){
				//sb.append("    and  MISC_DATA1 = ?  "); // bw
				//sb.append("    and  MISC_DATA2 = ?  "); // bh
				sb.append("    and  MISC_NUM1 = ?  "); // cw
				sb.append("    and  MISC_NUM2 = ?  "); // ch
				parm = new Object[] {clinicID, os, browser, cw, ch};				
			}else{
				parm = new Object[] {clinicID, os, browser};				
			}				
						
			//logger.debug("query datatable");
			HMS_TBLE dt = dbt01.queryDataTable(sb.toString(), parm);
			
			//logger.debug("dt count: " + dt.GetRowsCount());
			if (dt != null && dt.GetRowsCount() > 0){
				result = true;				
			}else{
				result = false;
			}				
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			logger.error(ex.toString());
			ex.printStackTrace();
		} finally {
			dbt01.closeConnection();
		}
        
        // todo: 暫時關閉信任裝置
        result = true;
        
        return result;
        
	}
	
	/**
	 * 簽到退作業
	 * 
	 * @param clinicID
	 * @param userName
	 * @param passwd
	 * @param signType
	 * @return
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	public JSONObject doSign(String clinicID, String userName, String passwd,
			String signType, String signShift) throws FileNotFoundException, Exception {

		
		logger.info("doRead:#");

		JSONObject json = new JSONObject();

		String signDate = UTIL_String.getNowString("yyyyMMdd");
		String signTime = UTIL_String.getNowString("HH:mm");
		logger.debug(String.format("cid:%s name:%s type:%s", clinicID,
				userName, signType));
		/*
		 * 先update當天資料，沒udpate到就新增資料, 有資料就看type決定要更新那一個欄位
		 */

		// 簽到
		StringBuffer upd = new StringBuffer();
		upd.append("Update HRS0SLOG ");
		if ("SignIn".equals(signType)) {
			upd.append("   Set SLOG_ACT_ON_TIME = ? ");
		}
		if ("SignOff".equals(signType)) {
			upd.append("   Set SLOG_ACT_OFF_TIME = ? ");
		}
		upd.append(" Where SLOG_COMP = ? ");
		upd.append("   and SLOG_USER_NAME = ? ");
		upd.append("   and SLOG_DATE = ? ");

		Object[] updParm = new Object[] { signTime, clinicID, userName,
				signDate };

		// 新增 RECORDS
		StringBuffer ins = new StringBuffer();
		ins.append("Insert into HRS0SLOG");
		ins.append("(SLOG_COMP, SLOG_USER_NAME, SLOG_DATE) ");
		ins.append("Values(?, ?, ?, ?, ?)");
		Object[] insParm = new Object[] { clinicID, userName, signDate};

		/**
		 * Start-process
		 */
		HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();
		try {
			dbt01.openConnection("HRS_DMZ");
			boolean chk = signCheck(clinicID, userName, passwd);

			// 密碼ok
			if (chk) {

				//logger.debug("1-login oK");
				int updCnt = dbt01.executeNonquery(upd.toString(), updParm);
				if (updCnt == 1) {
					//logger.debug("2-update ok");
					json.put("success", true);
				} else {
					//logger.debug("3-do insert");
					int insCnt = dbt01.executeNonquery(ins.toString(), insParm);
					if (insCnt == 1) {
						logger.debug("4-insert ok");
						updCnt = dbt01.executeNonquery(upd.toString(), updParm);
						if (updCnt == 1) {
							//logger.debug("5-update ok");
							json.put("success", true);
						} else {
							//logger.debug("6-update fail");
							json.put("msg", "Cannot find record to update");
							json.put("success", false);
						}
						json.put("success", true);

					} else {
						//logger.debug("7-insert fail");
						json.put("msg", "Insert records fail");
						json.put("success", false);
					}
				}

			} else {
				//logger.debug("8-password correct");
				json.put("success", false);
			}

		} catch (Exception ex) {
			String msg = ex.toString();
			json.put("msg", msg);
			json.put("success", false);
			ex.printStackTrace();
			logger.error(msg);
		} finally {
			dbt01.closeConnection();
		}

		return json;

	}
	
	/**
	 * request time 檢核: 1 分鐘以內完成 qrcode掃描簽到退
	 * 是否重複簽到退: 同日同診同卡資料可重複簽到退，後蓋前
	 * 完成後要回傳真正的簽到退時間(oo簽到成功 [12/12 17:00]
	 * 紀錄成功時的 client os/browser
	 */
	public String doSign(String clinicID, String userName, String signType, String signShift) throws FileNotFoundException, Exception {

		

		String result = "";
		String signDate = UTIL_String.getNowString("yyyyMMdd");
		String signTime = UTIL_String.getNowString("HH:mm");
		logger.debug(String.format("cid:%s name:%s type:%s", clinicID, userName, signType));
		
		/*
		 * 先update當天資料，沒udpate到就新增資料, 有資料就看type決定要更新那一個欄位
		 */

		// 簽到
		StringBuffer upd = new StringBuffer();
		upd.append("Update HRS0SLOG ");
		
		// 上班
		if ("On".equals(signType)) {
			upd.append("   Set SLOG_ACT_ON_TIME = ? ");
		}
		// 下班
		if ("Off".equals(signType)) {
			upd.append("   Set SLOG_ACT_OFF_TIME = ? ");					
		}
		// 加下
		if ("Ot".equals(signType)) {
			upd.append("   Set SLOG_ACT_OFF_TIME = ? ");			
			if ("Ot".equals(signType)) {
				upd.append("   , SLOG_OT_MARK = 'Y' ");
			}
		}		
		upd.append(" Where SLOG_COMP = ? 		");
		upd.append("   and SLOG_USER_NAME = ? 	");
		upd.append("   and SLOG_DATE = ? 		");
		upd.append("   and SLOG_SHIFT= ?		");

		Object[] updParm = new Object[] { signTime, clinicID, userName,
				signDate, signShift };

		// 新增 簽到退
		StringBuffer ins = new StringBuffer();
		ins.append("Insert into HRS0SLOG");
		ins.append("(SLOG_COMP, SLOG_USER_NAME, SLOG_DATE, SLOG_SHIFT) ");
		ins.append("Values(?, ?, ?, ?)");
		Object[] insParm = new Object[] { clinicID, userName, signDate, signShift};

		/**
		 * Start-process
		 */
		HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();
		try {
			dbt01.openConnection("HRS_DMZ");

			//logger.debug("1-login oK");
			int updCnt = dbt01.executeNonquery(upd.toString(), updParm);
			if (updCnt == 1) {
				//logger.debug("2-update ok");
				result = signTime;
			} else {
				//logger.debug("3-do insert");
				int insCnt = dbt01.executeNonquery(ins.toString(), insParm);
				if (insCnt == 1) {
					logger.debug("4-insert ok");
					updCnt = dbt01.executeNonquery(upd.toString(), updParm);
					if (updCnt == 1) {
						//logger.debug("5-update ok");
						result = signTime;
					} else {
						//logger.debug("6-update fail");					
						result = "";
					}
				} else {
					//logger.debug("7-insert fail");
					result = "";
				}
			}

		} catch (Exception ex) {			
			result = "";
			logger.error(ex.toString());
			ex.printStackTrace();			
		} finally {
			dbt01.closeConnection();
		}
		
		return result;		

	}
	
	public boolean signCheck(String clinicID, String userName, String passwd){
		
		boolean result = false;
				
		// 密碼驗證
		StringBuffer chk = new StringBuffer();
		chk.append("Select 'OK' from HRS0USER 	");
		chk.append("Where user_name = ? 			");
		chk.append("  and user_passwd = ? 		");
		chk.append("  and user_comp = ? ");
		Object[] chkParm = new Object[] { userName, passwd, clinicID };
		HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();
		try {
			dbt01.openConnection("HRS_LOCAL");
			String chkStr = dbt01.queryAString(chk.toString(), chkParm);
			result = "OK".equals(chkStr);
		}catch (Exception ex) {
			result = false;						
			ex.printStackTrace();
			logger.error("ID/PWD cannot check!");
		} finally {
			dbt01.closeConnection();
		}		
		
		
		return result;
		
	}
	
	public boolean localSignCheck(String clinicID, String userID, String passwd){
		
		boolean result = false;		
		
		// 密碼驗證
		StringBuffer chk = new StringBuffer();
		chk.append("Select 'OK' from HMS0_USER 	");
		chk.append("Where user_ID = ? 			");
		chk.append("  and user_passwd = ? 		");
		chk.append("  and user_comp = ? ");
		Object[] chkParm = new Object[] { userID, passwd, clinicID };
		HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();
		try {
			dbt01.openConnection("HRS_LOCAL");
			String chkStr = dbt01.queryAString(chk.toString(), chkParm);
			result = "OK".equals(chkStr);
		}catch (Exception ex) {
			result = false;						
			ex.printStackTrace();
			logger.error("ID/PWD cannot check!");
		} finally {
			dbt01.closeConnection();
		}		
		
		
		return result;
		
	}


	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		
		logger.info("HRS1W000 doAction:#");
		
		
		
	}

	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
	    // setCookie(有encode就要decode)
        Cookie url = new Cookie("cid", URLEncoder.encode("12345678", "UTF-8"));

        url.setMaxAge(60*60*24);
        response.addCookie(url);
        response.setContentType("text/html;charset=UTF-8");		       


	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
	
		
		logger.info("jr:#");		

	

	}

	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		logger.info("doUpdate:#");
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		logger.info("doDelete:#");	
		
	}
}
