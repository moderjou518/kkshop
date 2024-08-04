package com.hms.web;

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

import net.sf.json.JSONObject;

public class HMS1W002_ACTION implements HMS_WEBACTION {

	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	@Override
	public void doAction(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			Exception {
		// TODO Auto-generated method stub	
		logger.debug("doAction()");
	}

	@Override
	public void doCreate(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			Exception {


	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		
		logger.debug("do read");				
		
		String info = UTIL_String.nvl(request.getParameter("info"), null);		
		JSONObject jo = JSONObject.fromObject(info);		
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);		
		String clinicID = UTIL_GlobalConfig.getConfig("CLINIC_ID");
		
		logger.debug("info: " + info);
		logger.debug("mthd: " + method);
		
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
				
				logger.debug("data: " + dt.dataTable2Json());

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
		
		// 查詢
		if ("querySignList".equals(method)){			
			
			JSONObject rtnJo = new JSONObject();
			HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();
			try {
				//System.out.println("open connection .. ");
				dbt01.openConnection();
				StringBuffer sb = new StringBuffer();
				sb.append("	Select	SLOG_COMP, SLOG_USER_NAME, SLOG_ACT_ON_TIME, SLOG_ACT_OFF_TIME,	");
				sb.append("			SLOG_DATE, SLOG_STD_ON_TIME, SLOG_STD_OFF_TIME ");
				sb.append("	  From 	HRS0SLOG						");
				sb.append("  Where	SLOG_COMP = ? ");
				sb.append("    and  SLOG_USER_NAME = ? ");
				sb.append("    and  SLOG_DATE = ? ");
				sb.append("Order by SLOG_DATE, SLOG_USER_NAME ");
				Object[] parm = new Object[]{clinicID, jo.getString("qryUserName"), jo.getString("qrySignDate")};
								
				HMS_TBLE dt = dbt01.queryDataTable(sb.toString(), parm);				
	            
				rtnJo.put("success", true);
				rtnJo.put("data", dt.dataTable2Json());

			} catch (Exception ex) {
				String msg = ex.toString();
				rtnJo.put("msg", msg);
				//rtnJo.put("connStr", connStr);
				rtnJo.put("success", false);
				ex.printStackTrace();
				logger.debug(msg);
			} finally {
				dbt01.closeConnection();
			}
			
			//System.out.println(rtnJo.toString());
			response.getWriter().print(rtnJo.toString());
			
		}
		
		
		
		

	}

	@Override
	public void doUpdate(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			Exception {
		
		logger.debug("do update");
	}

	@Override
	public void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			Exception {		
		logger.debug("do delete");		
	}
}
