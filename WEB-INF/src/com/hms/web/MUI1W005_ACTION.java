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
import com.hms.entity.HMS_TROW;
import com.hms.util.HMS_PrepareExecSQL;
import com.hms.util.UTIL_GlobalConfig;
import com.hms.util.UTIL_String;

import net.sf.json.JSONObject;

/**
 * HRS1W010 / HRS1W020 JSP 不同但共用同1個ACTION 
 * @author Window10
 *
 */
public class MUI1W005_ACTION implements HMS_WEBACTION {
	
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		
		logger.info("MUI1W005 doAction:#");
		
		
		
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
		
		String info = UTIL_String.nvl(request.getParameter("info"), null);
		JSONObject jo = JSONObject.fromObject(info);
		String method = UTIL_String.nvl(request.getParameter("methodName"),
				null);
		
		/**
		 * 查詢紀錄
		 */
		if ("Query".equals(method)) {

			JSONObject rtnJo = new JSONObject();			
			HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();
			
			String qryComp = jo.getString("qryComp");
			String qryKind = jo.getString("qryKind");

			try {
				dbt01.openConnection("HMS");
				StringBuffer sb = new StringBuffer();
				sb.append("	Select	*");				
				sb.append("	  From 	HMS0MISC						");
				sb.append("  Where	MISC_COMP = ? ");
				sb.append("    and  MISC_KIND = ? ");				
				sb.append("Order by MISC_CODE ");
				Object[] parm = new Object[] {qryComp, qryKind};				

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
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		logger.info("doDelete:#");	
		
	}
}
