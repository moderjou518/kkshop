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

import com.hms.util.HMS_PrepareExecSQL;
import com.hms.util.UTIL_GlobalConfig;
import com.hms.util.UTIL_String;

import net.sf.json.JSONObject;

public class HMS_SIGNIN implements HMS_WEBACTION {

	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	@Override
	public void doAction(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			Exception {
		// TODO Auto-generated method stub

		
		
		logger.info("doAction:#");
	}

	@Override
	public void doCreate(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			Exception {
		// TODO Auto-generated method stub
		
		
		logger.info("doCreate:#");					
		

		
		
        // getCookie
		String cookieName = "mobileNo";
        String cookVal = "";
        Cookie[] cookies = request.getCookies();
        for(Cookie c: cookies){
            if(cookieName.equals(c.getName())){
                cookVal = URLDecoder.decode(c.getValue());      
            }
        }
        //logger.debug("Get Cookie: " + cookVal);

		// String info = GenericWebAction.getRequestParameter(req, "info",
		// null);
		String info = UTIL_String.nvl(request.getParameter("info"), null);
		JSONObject jo = JSONObject.fromObject(info);
		logger.debug("jo:" + jo.toString());

		String msg = "";
		StringBuffer ins = new StringBuffer();
		ins.append(" INSERT INTO HMP0SIGN                                                  ");
		ins.append(" (SIGN_DATE, SIGN_ROOM, SIGN_CLASS, SIGN_NO,                     ");
		ins.append("  SIGN_NAME, SIGN_MOBILE, SIGN_TIME, SIGN_DOCTOR, SIGN_MARK)   ");
		ins.append(" VALUES 																	");
		ins.append(" (?, '01', 'A', ?,            		");
		ins.append("  ?, ?, ?, 'DOCTOR', 'Y')                 					");

		StringBuffer no = new StringBuffer();
		//no.append("SELECT IFNULL(MAX(SIGN_NO),0) +1  "); mysql
		no.append("SELECT MAX(SIGN_NO) + 1 ");
		no.append("FROM HMP0SIGN ");
		no.append("WHERE SIGN_DATE = ? ");

		StringBuffer chk = new StringBuffer();
		chk.append("SELECT COUNT(*) ");
		chk.append("  FROM HMP0SIGN  ");
		chk.append(" WHERE SIGN_DATE = ? ");
		chk.append("   AND SIGN_MOBILE = ? ");
		
		//create 資料時要處 no

		String currDay = UTIL_String.getNowString("yyyyMMdd");
		String currNo = "0";
		JSONObject rtnJo = new JSONObject();
		HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();
		try {
			dbt01.openConnection();

			// 重複報到檢查
			Object[] chkParm = new Object[] { currDay, jo.getString("txtMobileNo") };
			String exist = dbt01.queryAString(chk.toString(), chkParm);

			int i = 0;
			if ("0".equals(exist)) {

				
				String currTime = UTIL_String.getNowString("HH:mm");
				
				// 取號
				logger.debug("currDay: " + currDay);
				Object[] noParm = new Object[]{currDay};
				String newNo = dbt01.queryAString(no.toString(), noParm);
				
				logger.debug("111");
				if (null == newNo){
					logger.debug("222");
					newNo = "1";
				}
				
				logger.debug("333:  " + newNo);

				Object[] insParm = new Object[] { 
						currDay, newNo, jo.getString("txtName"), 
						jo.getString("txtMobileNo"), currTime };
				i = dbt01.executeNonquery(ins.toString(), insParm);
				if (i > 0) {
					rtnJo.put("errCode", "0");
				} else {
					rtnJo.put("errCode", "2");
					// 資料新增失敗
				}

			} else {

				// 重複報到
				rtnJo.put("errCode", "1");
			}
			
			// todo: for test(setcookie)

            
            String mobileNo = jo.getString("txtMobileNo");           

            // setCookie(有encode就要decode)
            Cookie url = new Cookie(cookieName, URLEncoder.encode(mobileNo, "UTF-8"));

            url.setMaxAge(60*60*24);
            response.addCookie(url);
            response.setContentType("text/html;charset=UTF-8");
            logger.debug("Set Cookie: " + mobileNo);

			rtnJo.put("success", true);

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

		response.getWriter().print(rtnJo.toString());

	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, Exception {
		// TODO Auto-generated method stub


		
		logger.info("doRead:#");

	}

	@Override
	public void doUpdate(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			Exception {
		// TODO Auto-generated method stub

		
		logger.info("doUpdate:#");

	}

	@Override
	public void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			Exception {
		// TODO Auto-generated method stub		
		logger.info("doDelete:#");
	}
}
