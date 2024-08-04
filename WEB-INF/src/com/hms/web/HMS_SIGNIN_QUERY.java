
package com.hms.web;

import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.hms.entity.HMS_TBLE;
import com.hms.entity.HMS_TROW;
import com.hms.util.HMS_PrepareExecSQL;
import com.hms.util.UTIL_GlobalConfig;
import com.hms.util.UTIL_String;


public class HMS_SIGNIN_QUERY implements HMS_WEBACTION {

	@Override public void doAction( HttpServletRequest request , HttpServletResponse response )
		throws FileNotFoundException , Exception {
		// TODO Auto-generated method stub
		
		// LOAD MOBILE COOKIE NO
		System.out.println("SIGNIN_QUERY");
		
	}

	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		// TODO Auto-generated method stub
		
		
		System.out.println("SIGNIN QUERY: doRead");	
		String today = UTIL_String.getNowString("yyyyMMdd");
		String info = UTIL_String.nvl(request.getParameter("info"), null);
		System.out.println("info: " + info);
		JSONObject jo = JSONObject.fromObject(info);
		

		// 查詢者的號碼(尚未報到、不是當天的人、或是取消報到)
		StringBuffer qb = new StringBuffer();
		qb.append("Select 	SIGN_NO, SIGN_MARK ");
		qb.append("  From  HMP0SIGN ");
		qb.append(" Where  SIGN_DATE = ? ");
		qb.append("   and  SIGM_MOBILE = ?");				
		//Object[] qp = new Object[]{today, jo.getString("txtMobileNo")};
		HMS_TROW qdr = null;
		
		// 目前看診號碼
		StringBuffer cb = new StringBuffer();
		cb.append("SELECT	SIGN_NO 		");
		cb.append("  FROM	HMP0SIGN 		");
		cb.append(" WHERE   SIGN_DATE = ? 	");
		cb.append("   AND   SIGN_MARK = 'A' ");
		Object[] cp = new Object[]{today};
		HMS_TROW cdr = null;
		
		// 2者之間還有幾個人
		StringBuffer bb = new StringBuffer();
		bb.append("SELECT	COUNT(*) ");
		bb.append("  FROM   HMP0SIGN ");
		bb.append(" WHERE	SIGN_DATE = ? ");
		bb.append("   AND   SIGN_NO BETWEEN ? AND ? ");
		
		
		String errCode = "0";
		HMS_TBLE dt = null;
		HMS_TROW bdr = null;
		JSONObject rtnJo = new JSONObject();
		// 算2者之間等待的患者
		HMS_PrepareExecSQL db01 = new HMS_PrepareExecSQL();
		try {
			db01.openConnection();			
			
			//qdr = db01.queryDataRow(qb.toString(), qp);
			cdr = db01.queryDataRow(cb.toString(), cp);
			
			//System.out.println("q:" + qdr.GetField("SIGN_NO"));
			//System.out.println("c:" + cdr.GetField("SIGN_NO"));
			
			Object[] bp = new Object[]{today};
			
			
			//rtnJo.put("cnt", dt.GetRowsCount());
			rtnJo.put("data", dt.dataTable2Json());
			//rtnJo.put(key, value)
			rtnJo.put("errCode", errCode);
			rtnJo.put("success", true);
			
			System.out.println("new: " + UTIL_String.toString(true, "", dt.dataTable2Json()));
			//UTIL_GlobalConfig.postLog(2, "rtnJo: " + rtnJo.toString());			
			System.out.println("rtnJO - " + rtnJo.toString());
			
			response.getWriter().write(rtnJo.toString());
			
		}catch(Exception ex){			
			//UTIL_GlobalConfig.postIssue("Query SignIN", ex);
			ex.printStackTrace();
		}finally{
			db01.closeConnection();
		}
		
		System.out.println("End query");
	}

	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		// TODO Auto-generated method stub
		
		System.out.println("SIGNIN QUERY: update");
		JSONObject rtnJo = new JSONObject();
		String today = UTIL_String.getNowString("yyyyMMdd");
		String selUUID = UTIL_String.nvl(request.getParameter("setUUID"), "#");
		
		// 清空 sign_mark = 'A'
		// 設定 sign_mark = 'A'
		
		StringBuffer rb = new StringBuffer();
		rb.append("Update 	HMP0SIGN		");
		rb.append("	  Set 	SIgn_mark = 'Y'	");
		rb.append(" Where 	SIGN_DATE = ?	");
		rb.append("   and 	SIGN_MARK = 'A' ");
		Object[] rbObj = new Object[]{today};
		
		StringBuffer sb = new StringBuffer();
		sb.append("Update 	HMP0SIGN		");
		sb.append("	  Set 	SIgn_mark = 'A'	");
		sb.append(" Where 	SIGN_UUID = ?	");
		Object[] sbObj = new Object[]{selUUID};
		
		HMS_PrepareExecSQL db01 = new HMS_PrepareExecSQL();
		try {
			
			db01.openConnection();
			
			int i = 0;
			int j = 0;
			
			System.out.println("111");
			i = db01.executeNonquery(rb.toString(), rbObj);
			System.out.println("2222");
			j = db01.executeNonquery(sb.toString(), sbObj);
			if (j==1){
				System.out.println("33333");
				rtnJo.put("success", true);
			}else{
				System.out.println("44444");
				rtnJo.put("success", false);
			}

			
			response.getWriter().write(rtnJo.toString());
			
		}catch(Exception ex){			
			//UTIL_GlobalConfig.postIssue("Query SignIN setReg", ex);
			ex.printStackTrace();
		}finally{
			db01.closeConnection();
		}
		
		
		
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		// TODO Auto-generated method stub
		System.out.println("SIGNIN QUERY: delete");
		JSONObject rtnJo = new JSONObject();
		String today = UTIL_String.getNowString("yyyyMMdd");
		String selUUID = UTIL_String.nvl(request.getParameter("setUUID"), "#");
		
		// 清空 sign_mark = 'A'
		// 設定 sign_mark = 'A'
		
		StringBuffer rb = new StringBuffer();
		rb.append("Update 	HMP0SIGN		");
		rb.append("	  Set 	SIgn_mark = 'N'	");
		rb.append(" Where 	SIGN_DATE = ?	");
		rb.append("   and 	SIGN_UUID = ? 	");
		Object[] rbObj = new Object[]{today, selUUID};		
		
		HMS_PrepareExecSQL db01 = new HMS_PrepareExecSQL();
		try {
			
			db01.openConnection();
			
			int i = 0;			
			
			System.out.println("111");
			i = db01.executeNonquery(rb.toString(), rbObj);			
			if (i==1){
				System.out.println("33333");
				rtnJo.put("success", true);
			}else{
				System.out.println("44444");
				rtnJo.put("success", false);
			}

			
			
			response.getWriter().write(rtnJo.toString());
			
		}catch(Exception ex){			
			//UTIL_GlobalConfig.postIssue("Query SignIN cancel", ex);
			ex.printStackTrace();
		}finally{
			db01.closeConnection();
		}
		
	}
}
