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
public class CHECKIN_ACTION implements HMS_WEBACTION {

	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	@Override
	public void doAction(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			Exception {

		
		logger.info("CHECKIN doAction:#");

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
			logger.info("CHECKIN doAction:3");
			hmsDB.openConnection("HMS");
			HMS_TROW dr = hmsDB.queryDataRow(cb.toString(), cp);
			
			logger.info("CHECKIN doAction:4");
			clinicID = dr.GetField("misc_code");
			clinicName = dr.GetField("misc_desc");
			rtnJo.put("success", true);

			logger.info("CHECKIN doAction:5");
		} catch (Exception ex) {
			rtnJo.put("success", false);
			logger.error(ex.toString());
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
								
		String method = UTIL_String.nvl(request.getParameter("methodName"), "");	
		String info = UTIL_String.nvl(request.getParameter("info"), null);
		JSONObject jo = JSONObject.fromObject(info);
		String clinicID = UTIL_String.getClinicID(jo.getString("txtClinicID"));
		
		logger.debug("method: " + method);
		logger.debug("jo:" + jo.toString());

		String msg = "";
		
		// 新增掛號資料
		StringBuffer ins = new StringBuffer();
		ins.append(" INSERT INTO HMP0SIGN                                                  ");
		ins.append(" (CLINIC_ID, SIGN_DATE, SIGN_ROOM, SIGN_CLASS, SIGN_NO, SIGN_STATUS,                    ");
		ins.append("  SIGN_NAME, SIGN_MOBILE, SIGN_TIME, SIGN_DOCTOR, SIGN_MARK)   ");
		ins.append(" VALUES 																	");
		ins.append(" (?, ?, '01', 'A', ?, 'Y',            		");
		ins.append("  ?, ?, ?, 'DOCTOR', 'Y')                 					");

		
		// 取號
		StringBuffer no = new StringBuffer();
		no.append("SELECT MAX(SIGN_NO) + 1 ");
		no.append("FROM HMP0SIGN ");
		no.append("WHERE SIGN_DATE = ? ");
		

		// 有沒有重複報到
		StringBuffer chk = new StringBuffer();
		chk.append("SELECT COUNT(*) ");
		chk.append("  FROM HMP0SIGN  ");
		chk.append(" WHERE SIGN_DATE = ? ");
		chk.append("   AND SIGN_MOBILE = ? ");
		chk.append("   AND SIGN_STATUS = 'Y' ");
		
		
		//create 資料時要處 no
		String currDay = UTIL_String.getNowString("yyyyMMdd");		
		JSONObject rtnJo = new JSONObject();
		HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();
		try {
			dbt01.openConnection("HCR_DMZ");

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
						clinicID, currDay, newNo, jo.getString("txtName"), 
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
            Cookie url = new Cookie("mobileNo", URLEncoder.encode(mobileNo, "UTF-8"));
            logger.debug("Set Cookie: " + mobileNo);
            url.setMaxAge(60*60*24);
            response.addCookie(url);
            response.setContentType("text/html;charset=UTF-8");
            

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

		logger.debug(rtnJo.toString());
		response.getWriter().print(rtnJo.toString());

	}

	/**
	 * 查詢看診進度
	 */
	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		
		logger.info("jr:#");
		
		System.out.println("SIGNIN QUERY: doRead");	
		String today = UTIL_String.getNowString("yyyyMMdd");
		String info = UTIL_String.nvl(request.getParameter("info"), null);			
		JSONObject jo = JSONObject.fromObject(info);
		String clinicID = UTIL_String.getClinicID(jo.getString("txtClinicID"));

		// 查詢者的號碼(尚未報到、不是當天的人、或是取消報到)
		StringBuffer qb = new StringBuffer();
		qb.append("Select 	SIGN_NO, SIGN_MARK, SIGN_TIME ");
		qb.append("  From  HMP0SIGN ");
		qb.append(" Where  clinic_ID = ? ");
		qb.append("   and  SIGN_DATE = ? ");
		qb.append("   and  SIGN_MOBILE = ?");				
		qb.append("   and  SIGN_STATUS = 'Y' ");
		Object[] qp = new Object[]{clinicID, today, jo.getString("txtMobileNo")};
		HMS_TROW qdr = null;
		
		// 目前看診號碼
		StringBuffer cb = new StringBuffer();
		cb.append("SELECT	SIGN_NO, SIGN_TIME 	");
		cb.append("  FROM	HMP0SIGN 			");
		cb.append(" WHERE   clinic_ID = ? 		");
		cb.append("   and   SIGN_DATE = ? 		");
		cb.append("   AND   SIGN_MARK = 'A' 	");
		Object[] cp = new Object[]{clinicID, today};
		HMS_TROW cdr = null;
		
		// 2者之間還有幾個人
		StringBuffer bb = new StringBuffer();
		bb.append("SELECT	COUNT(*) as CNT ");
		bb.append("  FROM   HMP0SIGN ");
		bb.append(" WHERE	CLINIC_ID = ? ");
		bb.append("   AND 	SIGN_DATE = ? ");
		bb.append("   AND   SIGN_NO BETWEEN ? AND ? ");
		bb.append("   AND   SIGN_STATUS = 'Y' ");
		
		
		HMS_TROW bdr = null;
		JSONObject rtnJo = new JSONObject();
		// 算2者之間等待的患者
		HMS_PrepareExecSQL db01 = new HMS_PrepareExecSQL();
		try {
			db01.openConnection("HCR_DMZ");			
			
			qdr = db01.queryDataRow(qb.toString(), qp);
			// 當天沒有掛號報到的資料
			if (null == qdr){
				rtnJo.put("errCode", "1");
				rtnJo.put("success", true);
			}else{				
				logger.debug("qdr:" + qdr.toJSONString());
				cdr = db01.queryDataRow(cb.toString(), cp);
				if (null == cdr){
					// 還沒有開始看診
					rtnJo.put("errCode", "2");					
				}else{
					logger.debug("dr:" + cdr.toJSONString());
					// 2 者之間還有幾個人
					logger.debug("clinicID: " + clinicID);
					logger.debug("   today: " + today);
					logger.debug(" curr no: " + cdr.GetField("SIGN_NO"));
					logger.debug("query no: " + qdr.GetField("SIGN_NO"));
					Object[] bp = new Object[]{clinicID, today, cdr.GetField("SIGN_NO"), qdr.GetField("SIGN_NO")};
					bdr = db01.queryDataRow(bb.toString(), bp);
					
					if (null == bdr){
						logger.debug("null bdr");
					}else{
						logger.debug("bdr:" + bdr.toJSONString());
						rtnJo.put("COUNT", bdr.GetField("CNT"));						
					}
				}
			}			
			
			rtnJo.put("success", true);			
			//System.out.println("rtnJO - " + rtnJo.toString());			
			response.getWriter().write(rtnJo.toString());
			
		}catch(Exception ex){
			logger.error("看診進度(by電話號碼): " + this.getClass().getName() + ":doRead");
			logger.error(ex.toString());
			ex.printStackTrace();
		}finally{
			db01.closeConnection();
		}
		
	}
	



	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		
		logger.info("doUpdate:checkInAction");
		
		
		
		
		
	}


	/**
	 * 信任裝置檢查
	 */
	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		
		logger.info("jd: Check all sign trust parameter.");		
		 
		// 檢查全部的參數
		HRS1W000_ACTION wa = new HRS1W000_ACTION();
		boolean result = wa.dosSignTrustCheck(request, response, true);
		JSONObject jo = new JSONObject();
		jo.put("success", result);
		
		response.getWriter().print(jo.toString());      

	}
}
