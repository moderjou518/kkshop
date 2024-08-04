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
import com.hms.util.UTIL_Encrypt_Base64;
import com.hms.util.UTIL_GlobalConfig;
import com.hms.util.UTIL_String;
import com.hms.web.HMS_WEBACTION;

import net.sf.json.JSONObject;

/**
 * HRS1W010 / HRS1W020 JSP 不同但共用同1個ACTION
 * 
 * @author Window10
 * 
 */
public class HRS1W010_ACTION implements HMS_WEBACTION {

	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	@Override
	public void doAction(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			Exception {

	
		logger.info("HRS1W010 doAction:#");

		/* 第一次進 來載入診所資料 */
		String cID = UTIL_String.nvl(request.getParameter("cID"), "99999999");
		String clinicID = UTIL_String.getClinicID(cID);
		String clinicName = UTIL_String.nvl(request.getParameter("cname"),
				"查無資料");

		// 讀取診所資料
		StringBuffer cb = new StringBuffer();
		cb.append(" Select misc_kind, misc_code, misc_desc ");
		cb.append("  From  Hms0misc                        ");
		cb.append(" Where  misc_comp = '00000000'            ");
		cb.append("   and  misc_kind = 'CLINIC'                 ");
		cb.append("   and  misc_num1 = 'Y'                 ");
		cb.append("   and  misc_code = ?                   ");
		Object[] cp = new Object[] { clinicID };

		JSONObject rtnJo = new JSONObject();
		HMS_PrepareExecSQL hmsDB = new HMS_PrepareExecSQL();

		try {
			hmsDB.openConnection("HMS");
			HMS_TROW dr = hmsDB.queryDataRow(cb.toString(), cp);
			clinicID = dr.GetField("misc_code");
			clinicName = dr.GetField("misc_desc");
			rtnJo.put("success", true);

		} catch (Exception ex) {
			String msg = ex.toString();
			rtnJo.put("msg", msg);
			rtnJo.put("success", false);			
			logger.error(msg);
			ex.printStackTrace();
		} finally {
			hmsDB.closeConnection();
		}

		request.setAttribute("CLINIC_ID", cID);
		request.setAttribute("CLINIC_NAME", clinicName);
	}

	/**
	 * jc: 簽到退時檢查帳密，需要檢核信任裝置的所有參數os+browser+解析度
	 */
	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		// 帳密驗證		
		//logger.info("jc:#");
		
		//String info = new String(UTIL_String.nvl(request.getParameter("info"), "").getBytes("ISO-8859-1"), "utf-8");
		String info = UTIL_String.nvl(request.getParameter("info"), "");
		JSONObject jo = JSONObject.fromObject(info);
		String clinicID = UTIL_String.getClinicID(jo.getString("txtClinicID")); 				
		
		String method = UTIL_String.nvl(request.getParameter("methodName"), "");
		logger.debug("info: " + info);
		logger.debug("method: " + method);
		HRS1W000_ACTION w000 = new HRS1W000_ACTION();			
		
		if (w000.localSignCheck(clinicID, jo.getString("txtUserName"), jo.getString("txtUserPasswd"))){
			
			// 通過驗證後，產生 QRCODE 所需的加密字串
			//String sName = new String(UTIL_String.nvl(request.getParameter("txtUserName"), "").getBytes("ISO-8859-1"),"utf-8");
			
			// Client 要透過 URL 丟中文參數給 Server時，要在 client 端先用 encodeURIComponent 加密後再傳遞過去
			// Server 接收 client url 中文參數後，要先轉成 utf-8
			// 再用  URLDecoder.decode 作反解密
			// String signName = URLDecoder.decode(UTIL_String.nvl(request.getParameter("txtUserName"), ""), "UTF-8");
			
			//String sType = new String(UTIL_String.nvl(request.getParameter("txtSignType"), "").getBytes("ISO-8859-1"),"utf-8");
			//String sShift = new String(UTIL_String.nvl(request.getParameter("txtSignShift"), "").getBytes("ISO-8859-1"),"utf-8");
			
			//logger.debug("name11: " + sName);
			//logger.debug("type: " + sType);
			//logger.debug("shift: " + sShift);		
			JSONObject qrJo = new JSONObject();		
			qrJo.put("clinicID", clinicID);
			qrJo.put("signType", jo.getString("txtSignType").replace("[", "").replace("]", "").replace("\"", ""));
			qrJo.put("signShift", jo.getString("txtSignShift").replace("[", "").replace("]", "").replace("\"", ""));
			qrJo.put("requestTime", UTIL_String.getNowString("yyyyMMddhhmm"));
			String eText = UTIL_Encrypt_Base64.encrypt(qrJo.toString());
			
			//logger.debug("info: " + qrJo.toString());					
			//logger.debug("pText: " + pText);
			
			JSONObject rtnJo = new JSONObject();
			rtnJo.put("eText", eText);
			rtnJo.put("success", true);
			response.getWriter().print(rtnJo.toString());
		}else{
			JSONObject rtnJo = new JSONObject();			
			rtnJo.put("success", false);
			response.getWriter().print(rtnJo.toString());
		}
		
		
		
		
		
		
		//http://kkshop:8888/do.sign?webactionID=HRS1W011&cID=77427332&actionMethod=jc


	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, Exception {

		
		logger.info("jr:#");

		String info = UTIL_String.nvl(request.getParameter("info"), null);
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);
		String clinicID = UTIL_String.getClinicID(request.getParameter("cID"));
		JSONObject jo = JSONObject.fromObject(info);

		logger.debug("inf: " + info);
		logger.debug("mtd: " + method);

		/**
		 * 載入網路版的使用者名單
		 */
		if ("loadUserList".equals(method)) {

			// 載入診所員工帳號
			StringBuffer ub = new StringBuffer();
			ub.append("Select USER_ID, USER_NAME from HMS0_USER ");
			ub.append("Where user_comp = ? ");
			ub.append("  and user_role = 'NUS' ");
			ub.append("  and user_status = 'Y' ");			
			ub.append("Order by user_sort ");
			Object[] up = new Object[] { clinicID };

			JSONObject rtnJo = new JSONObject();
			HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();
			try {
				dbt01.openConnection("HRS_DMZ");
				HMS_TBLE dt = dbt01.queryDataTable(ub.toString(), up);
				rtnJo.put("success", true);
				rtnJo.put("data", dt.dataTable2Json());
			} catch (Exception ex) {							
				rtnJo.put("success", false);
				logger.error(ex.toString());
				ex.printStackTrace();				
			} finally {
				dbt01.closeConnection();
			}
			response.getWriter().print(rtnJo.toString());

		}
		
		/**
		 * 載入本機的使用者名單
		 */
		if ("loadLocalUserList".equals(method)) {

			// 載入診所員工帳號
			StringBuffer ub = new StringBuffer();
			ub.append("Select USER_ID, USER_NAME from HMS0_USER ");
			ub.append("Where user_comp = ? ");
			ub.append("  and user_role = 'NUS' ");
			ub.append("  and user_status = 'Y' ");
			ub.append("Order by user_sort ");
			Object[] up = new Object[] { clinicID };

			JSONObject rtnJo = new JSONObject();
			HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();
			try {
				dbt01.openConnection("HRS_LOCAL");
				HMS_TBLE dt = dbt01.queryDataTable(ub.toString(), up);
				rtnJo.put("success", true);
				rtnJo.put("data", dt.dataTable2Json());
			} catch (Exception ex) {
				String msg = ex.toString();
				rtnJo.put("msg", msg);
				// rtnJo.put("connStr", connStr);
				rtnJo.put("success", false);
				ex.printStackTrace();

				logger.error(msg);
			} finally {
				dbt01.closeConnection();
			}
			response.getWriter().print(rtnJo.toString());
		}
		
		

		/**
		 * 簽到退作業
		 */
		/*
		if ("SignIn".equals(method) || "SignOff".equals(method)) {
			clinicID = UTIL_String.getClinicID(jo.getString("txtClinicID"));
			JSONObject rtnJo = doSign(clinicID, jo.getString("txtUserName"),
					jo.getString("txtUserPasswd"), method);
			response.getWriter().print(rtnJo.toString());
		}
		*/

		/**
		 * 查詢紀錄
		 */
		if ("Query".equals(method)) {

			JSONObject rtnJo = new JSONObject();
			clinicID = UTIL_String.getClinicID(jo.getString("txtClinicID"));
			HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();

			String qType = jo.getString("txtSignType").replace("[", "")
					.replace("]", "").replace("\"", "");
			String sDate = "";
			String eDate = "";

			if ("TODAY".equals(qType)) {
				sDate = UTIL_String.getNowString("yyyyMMdd");
				eDate = UTIL_String.getNowString("yyyyMMdd");
			}
			if ("YESTERDAY".equals(qType)) {
				sDate = UTIL_String.getNowString(-1, "yyyyMMdd");
				eDate = UTIL_String.getNowString(-1, "yyyyMMdd");

			}
			if ("THIS_MONTH".equals(qType)) {
				sDate = UTIL_String.getNowString("yyyyMM01");
				eDate = UTIL_String.getNowString("yyyyMM31");

			}
			if ("LAST_MONTH".equals(qType)) {
				sDate = UTIL_String.getNowString(-30, "yyyyMM01");
				eDate = UTIL_String.getNowString(-30, "yyyyMM31");
			}

			try {
				dbt01.openConnection("HRS_DMZ");
				StringBuffer sb = new StringBuffer();
				sb.append("	Select	SLOG_COMP, SLOG_USER_NAME, SLOG_ACT_ON_TIME, SLOG_ACT_OFF_TIME,	");
				sb.append("			SLOG_DATE, RIGHT(SLOG_DATE,4) AS SLOG_DAY ");
				sb.append("	  From 	HRS0SLOG						");
				sb.append("  Where	SLOG_COMP = ? ");
				if (!"".equals(jo.getString("qryUserName"))) {
					sb.append("    and  SLOG_USER_NAME = ? ");
				}
				sb.append("    and  SLOG_DATE BETWEEN ? AND ?  ");
				sb.append("Order by SLOG_USER_NAME, SLOG_DATE ");
				Object[] parm = null;
				if (!"".equals(jo.getString("qryUserName"))) {
					parm = new Object[] { clinicID,
							jo.getString("qryUserName"), sDate, eDate };
				} else {
					parm = new Object[] { clinicID, sDate, eDate };
				}

				HMS_TBLE dt = dbt01.queryDataTable(sb.toString(), parm);

				rtnJo.put("success", true);
				rtnJo.put("data", dt.dataTable2Json());

			} catch (Exception ex) {
				String msg = ex.toString();
				rtnJo.put("msg", msg);
				// rtnJo.put("connStr", connStr);
				rtnJo.put("success", false);
				ex.printStackTrace();
				logger.debug(msg);
			} finally {
				dbt01.closeConnection();
			}

			response.getWriter().print(rtnJo.toString());

		}

	}
	



	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		
		logger.info("doUpdate:#");
		
		// setCookie(有encode就要decode)
		Cookie url = new Cookie("cid", URLEncoder.encode("12345678", "UTF-8"));

		url.setMaxAge(60 * 60 * 24);
		response.addCookie(url);
		response.setContentType("text/html;charset=UTF-8");
	}


	/**
	 * 信任裝置檢查
	 */
	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
				
		 
		// 檢查全部的參數
		HRS1W000_ACTION wa = new HRS1W000_ACTION();
		boolean result = wa.dosSignTrustCheck(request, response, true);
		JSONObject jo = new JSONObject();
		jo.put("success", result);
		
		response.getWriter().print(jo.toString());      

	}
}
