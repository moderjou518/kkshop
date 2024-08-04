package com.hms.web.hrs1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
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
import com.hms.util.UTIL_Encrypt_Base64;
import com.hms.util.UTIL_GlobalConfig;
import com.hms.util.UTIL_String;
import com.hms.web.HMS_WEBACTION;

import net.sf.json.JSONObject;

/**
 * HRS1W011 
 * 
 * @author Window10
 * 
 */
public class HRS1W011_ACTION implements HMS_WEBACTION {

	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

			logger.info("HRS1W011 doAction:#");

		// Client 要透過 URL 丟中文參數給 Server時，要在 client 端先用 encodeURIComponent 加密後再傳遞過去
		// Server 接收 client url 中文參數後，要先轉成 utf-8
		// 再用  URLDecoder.decode 作反解密
		String eText = UTIL_String.nvl(request.getParameter("eText"), "99999999");
		String sn = UTIL_String.nvl(request.getParameter("sn"), "");
		String signID = URLDecoder.decode(new String(sn.getBytes("iso-8859-1"), "utf-8"));					
		//logger.debug("eText: " + eText);			
		String pText = UTIL_Encrypt_Base64.decrypt(eText);
		//logger.debug("PText: " + pText);
		JSONObject jo = JSONObject.fromObject(pText);						
		
		logger.debug("sname: " + signID);
		logger.debug("jo: " + jo.toString());
		
		/*
		BigDecimal nowTime = new BigDecimal(UTIL_String.getNowTime(0));
		BigDecimal reqTime = new BigDecimal(jo.getString("requestTime"));
		BigDecimal lmtTime = new BigDecimal(jo.getString("requestTime")).add(new BigDecimal("2"));
		*/
		
		
		
		
		request.setAttribute("SIGN_NAME", signID);
		request.setAttribute("CLINIC_ID", jo.getString("clinicID"));
		request.setAttribute("SIGN_TYPE", jo.getString("signType"));
		
		HRS1W000_ACTION w000 = new HRS1W000_ACTION();			
		String signTime = w000.doSign(jo.getString("clinicID"), signID, jo.getString("signType"), jo.getString("signShift"));
		request.setAttribute("SIGN_TIME", signTime);
		
		
		
				
		
		
		
	}

	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {


		//logger.info("jc:#");	


	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, Exception {


		//logger.info("jr:#");

		String info = UTIL_String.nvl(request.getParameter("info"), null);
		JSONObject jo = JSONObject.fromObject(info);
		String method = UTIL_String.nvl(request.getParameter("methodName"),
				null);
		String clinicID = UTIL_String.getClinicID(request.getParameter("cID"));



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
		ins.append("(SLOG_COMP, SLOG_USER_NAME, SLOG_DATE, SLOG_STD_ON_TIME, SLOG_STD_OFF_TIME) ");
		ins.append("Values(?, ?, ?, ?, ?)");
		Object[] insParm = new Object[] { clinicID, userName, signDate,
				"09:00", "17:00" };

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

	@Override
	public void doUpdate(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			Exception {
		
		//logger.info("doUpdate:#");
		
		// setCookie(有encode就要decode)
		Cookie url = new Cookie("cid", URLEncoder.encode("12345678", "UTF-8"));

		url.setMaxAge(60 * 60 * 24);
		response.addCookie(url);
		response.setContentType("text/html;charset=UTF-8");
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		
		// logger.info("jd#");		
		 

	}
}
