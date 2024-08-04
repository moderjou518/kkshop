
package com.hms.web;

import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.hms.entity.HMS_TBLE;
import com.hms.util.HMS_PrepareExecSQL;
import com.hms.util.UTIL_GlobalConfig;
import com.hms.util.UTIL_String;


public class HMS_SIGNIN_LIST implements HMS_WEBACTION {

	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	@Override public void doAction( HttpServletRequest request , HttpServletResponse response )
		throws FileNotFoundException , Exception {
		// TODO Auto-generated method stub
		
		

		
	}

	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		// TODO Auto-generated method stub
		
		
		logger.debug("SIGNIN LIST: doRead");		
		
		//String info = UTIL_String.nvl(request.getParameter("info"), null);
		//JSONObject jo = JSONObject.fromObject(info);
		//UTIL_GlobalConfig.postLog(2, info);
		String today = UTIL_String.getNowString("yyyyMMdd");		

		// 抓資料
		StringBuffer sb3 = new StringBuffer();
		sb3.append("Select * from HMP0SIGN ");
		sb3.append(" Where SIGN_DATE = ? ");
		sb3.append("Order by sign_time asc");		
		Object[] parm = new Object[]{today};
		
		String errCode = "0";
		HMS_TBLE dt = null;
		JSONObject rtnJo = new JSONObject();
		// 算2者之間等待的患者
		HMS_PrepareExecSQL db01 = new HMS_PrepareExecSQL();
		try {
			db01.openConnection();
			
			dt = db01.queryDataTable(sb3.toString(), parm);
			
			System.out.println("sql: " + sb3.toString());
			System.out.println("today: " + today);
			
			
			//rtnJo.put("cnt", dt.GetRowsCount());
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

	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		// TODO Auto-generated method stub
		
	}
}
