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
import com.hms.util.HMS_PrepareExecSQL;
import com.hms.util.UTIL_GlobalConfig;
import com.hms.util.UTIL_String;
import com.hms.web.HMS_WEBACTION;

import net.sf.json.JSONObject;

public class HRS_SIGN_ACTION implements HMS_WEBACTION {

	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	@Override
	public void doAction(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			Exception {
		// TODO Auto-generated method stub

		
	
		logger.debug("doAction:#");
	}

	@Override
	public void doCreate(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			Exception {


	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, Exception {
		// TODO Auto-generated method stub

				
		
		logger.debug("doRead:#");
		
		String info = UTIL_String.nvl(request.getParameter("info"), null);
		logger.debug("info: " + info);
		JSONObject jo = JSONObject.fromObject(info);		
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);
		logger.debug("do method: " + method);
		
		String clinicID = UTIL_GlobalConfig.getConfig("CLINIC_ID");
		
		// 載入系統使用者
		if ("loadUserList".equals(method)){
			
			
			
			JSONObject rtnJo = new JSONObject();
			HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();
			try {
				//System.out.println("open connection .. ");
				dbt01.openConnection();
				StringBuffer sb = new StringBuffer();
				sb.append("Select USER_NAME from HMS0_USER ");	
				sb.append("Where user_comp = ? ");
				sb.append("  and user_role = 'NUS' ");
				sb.append("Order by user_sort ");				
				HMS_TBLE dt = dbt01.queryDataTable(sb.toString(), new Object[]{clinicID});				
	            
				rtnJo.put("success", true);
				rtnJo.put("data", dt.dataTable2Json());

			} catch (Exception ex) {
				String msg = ex.toString();
				rtnJo.put("msg", msg);
				//rtnJo.put("connStr", connStr);
				rtnJo.put("success", false);
				ex.printStackTrace();
				
				logger.error(msg);
			} finally {
				dbt01.closeConnection();
			}
			
			//System.out.println(rtnJo.toString());
			response.getWriter().print(rtnJo.toString());
			
		}
		
		
		// 登入檢查
		if ("loginCheck".equals(method)){
			
			JSONObject rtnJo = new JSONObject();
			String signType = jo.getString("txtSignType").replace("[", "").replace("]", "").replace("\"", "");
			String signDate = UTIL_String.getNowString("yyyyMMdd");
			String signTime = UTIL_String.getNowString("HH:mm");
			logger.debug("login Check --> signType: " + signType);
			
			StringBuffer chk = new StringBuffer();
			chk.append("Select 'OK' from HMS0_USER 	");
			chk.append("Where user_name = ? 			");
			chk.append("  and user_passwd = ? 		");
			chk.append("  and user_comp = ? ");
			Object[] chkParm = new Object[]{jo.getString("txtUserName"), jo.getString("txtUserPasswd"), clinicID};
			
			/*
			 *  先update當天資料，沒udpate到就新增資料
			 *  有資料就看type決定要更新那一個欄位
			 */
			
			// 簽到
			StringBuffer upd = new StringBuffer();
			upd.append("Update HRS0SLOG ");					
			if ("CI".equals(signType)){
				upd.append("   Set SLOG_ACT_ON_TIME = ? ");				
			}else{
				upd.append("   Set SLOG_ACT_OFF_TIME = ? ");				
			}
			upd.append("Where SLOG_COMP = ? ");
			upd.append("  and SLOG_USER_NAME = ? ");
			upd.append("  and SLOG_DATE = ? ");
			Object[] updParm = new Object[]{signTime, clinicID, jo.getString("txtUserName"), signDate};			
			
			// 新增簽到
			StringBuffer ins = new StringBuffer();
			ins.append("Insert into HRS0SLOG");
			ins.append("(SLOG_COMP, SLOG_USER_NAME, SLOG_DATE, SLOG_STD_ON_TIME, SLOG_STD_OFF_TIME) ");
			ins.append("Values(?, ?, ?, ?, ?)");
			Object[] insParm = new Object[]{clinicID, jo.getString("txtUserName"), signDate, "09:00", "17:00"};	
			
			HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();
			try {				
				dbt01.openConnection();								
				String chkStr = dbt01.queryAString(chk.toString(), chkParm);			
				
				// 密碼ok
				if ("OK".equals(chkStr)){
					
					logger.debug("1-login oK");
					
				
					int updCnt = dbt01.executeNonquery(upd.toString(), updParm);
					if (updCnt ==1){
						logger.debug("2-update ok");
						rtnJo.put("success", true);	
					}else{
						logger.debug("3-do insert");
						int insCnt = dbt01.executeNonquery(ins.toString(), insParm);
						if (insCnt ==1){
							logger.debug("4-insert ok");
							updCnt = dbt01.executeNonquery(upd.toString(), updParm);
							if (updCnt ==1){
								logger.debug("5-update ok");
								rtnJo.put("success", true);
							}else{
								logger.debug("6-update fail");
								rtnJo.put("msg", "Cannot find record to update");
								rtnJo.put("success", false);
							}
							
							rtnJo.put("success", true);
							
						}else{
							logger.debug("7-insert fail");
							rtnJo.put("msg", "Insert records fail");
							rtnJo.put("success", false);
							
						}
					}
						
				}else{
					logger.debug("8-password correct");
					rtnJo.put("msg", "簽到退失敗：帳號/密碼錯誤");
					rtnJo.put("success", false);
				}

			} catch (Exception ex) {
				String msg = ex.toString();
				rtnJo.put("msg", msg);
				rtnJo.put("success", false);
				ex.printStackTrace();				
				logger.error(msg);				
			} finally {
				dbt01.closeConnection();
			}
			
						
			response.getWriter().print(rtnJo.toString());
			
		}
		
		
		
		
		

	}

	@Override
	public void doUpdate(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			Exception {
		
		
		logger.debug("doUpdate:#");

	}

	@Override
	public void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			Exception {

		
		logger.debug("doDelete:#");
	}
}
