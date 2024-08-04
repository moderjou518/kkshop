package com.hms.web.hcr1;

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
import com.hms.web.hrs1.HRS1W000_ACTION;

import net.sf.json.JSONObject;

/**
 * HRS1W010 / HRS1W020 JSP 不同但共用同1個ACTION
 * 
 * @author Window10
 * 
 */
public class DO_CHECKIN_ACTION implements HMS_WEBACTION {

	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		
		logger.info("MANAGE CHECKIN doAction:#");

		
	}

	/**
	 * jc: 簽到退時檢查帳密，需要檢核信任裝置的所有參數os+browser+解析度
	 */
	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		// 帳密驗證		
		//logger.info("jc:#");
		

	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		
		logger.info("jr:#");
		
		String today = UTIL_String.getNowString("yyyyMMdd");
		String info = UTIL_String.nvl(request.getParameter("info"), null);			
		JSONObject jo = JSONObject.fromObject(info);
		
		logger.debug("info: " + info);
		String clinicID = UTIL_String.getClinicID(request.getParameter("cID"));		
		String method = UTIL_String.nvl(request.getParameter("methodName"), "");		
		
		logger.debug("method: " + method);
		
		// setCookie(有encode就要decode)
		Cookie url = new Cookie("cid", URLEncoder.encode("12345678", "UTF-8"));
		url.setMaxAge(60 * 60 * 24);
		response.addCookie(url);
		response.setContentType("text/html;charset=UTF-8");			
		
		// 抓資料
		StringBuffer sb3 = new StringBuffer();
		sb3.append("Select * From HMP0SIGN 	");
		sb3.append(" Where Clinic_ID= ? 		");
		sb3.append("   and SIGN_DATE = ? 	");
		sb3.append("   AND SIGN_STATUS = 'Y' ");
		sb3.append("Order by SIGN_UUID asc");		
		Object[] parm = new Object[]{clinicID, today};
		
		String errCode = "0";
		HMS_TBLE dt = null;
		JSONObject rtnJo = new JSONObject();
		HMS_PrepareExecSQL db01 = new HMS_PrepareExecSQL();
		
		try {
			db01.openConnection("HCR_DMZ");			
			dt = db01.queryDataTable(sb3.toString(), parm);
			
			rtnJo.put("data", dt.dataTable2Json());
			rtnJo.put("errCode", errCode);
			rtnJo.put("success", true);
			
			//UTIL_GlobalConfig.postLog(2, "rtnJo: " + rtnJo.toString());
			
			System.out.println("new: " + UTIL_String.toString(true, "", dt.dataTable2Json()));
			System.out.println("rtnJO - " + rtnJo.toString());
			
			response.getWriter().write(rtnJo.toString());
			
		}catch(Exception ex){			
			//UTIL_GlobalConfig.postIssue("Query SignIN List", ex);
			ex.printStackTrace();
		}finally{
			db01.closeConnection();
		}
		
		System.out.println("End List");
		
		

		
	}
	

	/**
	 * 看診管理: 看診中/取消報到
	 */
	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		// SET REG NO
		
		logger.info("doUpdate:#");
		
		String cID = UTIL_String.nvl(request.getParameter("cID"), "99999999");
		String clinicID = UTIL_String.getClinicID(cID);		
		String setUUID = UTIL_String.nvl(request.getParameter("setUUID"), "99999999");
		String method = UTIL_String.nvl(request.getParameter("method"), "99999999");
		String today = UTIL_String.getNowString("yyyyMMdd");
		
		logger.debug("cID: " + cID);
		logger.debug("setID: " + setUUID);
		logger.debug("today: " + today);
		logger.debug("method: " + method);
		
		String msg = "";
		JSONObject rtnJo = new JSONObject();
		HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();
		try {
			dbt01.openConnection("HCR_DMZ");

			
			
			

			if ("setRegNo".equals(method)){
				
				/*
				 * 看診中
				 * */
				StringBuffer reset = new StringBuffer();
				reset.append("");
				reset.append("Update HMP0SIGN 		");			
				reset.append("   Set SIGN_MARK = 'Y' 	");
				reset.append(" Where Clinic_ID= ? 	");
				reset.append("   and SIGN_DATE = ? 	");
				reset.append("   and SIGN_MARK = 'A' 	");
				reset.append("   AND SIGN_STATUS = 'Y' ");
				Object[] resParm = new Object[]{clinicID, today};			
				
				StringBuffer upd = new StringBuffer();
				upd.append("Update HMP0SIGN 		");			
				upd.append("   Set SIGN_MARK = 'A' 	");
				upd.append(" Where Clinic_ID= ? 	");
				upd.append("   and SIGN_DATE = ? 	");
				upd.append("   and SIGN_UUID = ? 	");
				upd.append("   AND SIGN_STATUS = 'Y' ");
				Object[] updParm = new Object[]{clinicID, today, setUUID};
				
				int i = 0;
				// 清掉目前看診號碼
				i = dbt01.executeNonquery(reset.toString(), resParm);
				
				logger.debug("i:" + i);
				
				// 設定目前看診號碼
				int c = dbt01.executeNonquery(upd.toString(), updParm);
				logger.debug("C:" + c);
				if (c == 1){
					rtnJo.put("errCode", "0");
				}else{
					//
					rtnJo.put("errCode", "1");
				}
				
				rtnJo.put("success", true);	
			}
			
			if ("cancelRegNo".equals(method)){
				
				/**
				 * 取消看診
				 */
				StringBuffer del = new StringBuffer();
				del.append("Update HMP0SIGN 		");
				del.append("   Set SIGN_STATUS = 'D'");
				del.append(" Where SIGN_UUID= ? 	");				
				Object[] delParm = new Object[]{setUUID};
				
				int i = 0;
				// 清掉目前看診號碼
				i = dbt01.executeNonquery(del.toString(), delParm);
				
				if (i > 0){
					rtnJo.put("success", true);	
				}				
				
			}
			
			
		} catch (Exception ex) {
			msg = ex.toString();
			rtnJo.put("msg", msg);
			//rtnJo.put("connStr", connStr);
			rtnJo.put("success", false);
			ex.printStackTrace();
			logger.error(msg);
		} finally {
			dbt01.closeConnection();
		}
			
		response.getWriter().write(rtnJo.toString());
		
	}


	/**
	 * 信任裝置檢查
	 */
	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		
		logger.info("jd: ");		
		 
	

	}
}
