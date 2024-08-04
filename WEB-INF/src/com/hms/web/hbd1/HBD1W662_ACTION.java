package com.hms.web.hbd1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.synth.SynthSeparatorUI;

import org.apache.log4j.Logger;

import com.evergreen_hotels.bmg.whms1.util.MsgUtils;
import com.evergreen_hotels.bmg.whms1.util.PrepareExecuteSql;
import com.hms.util.UTIL_String;
import com.hms.util.UserInfo;
import com.hms.web.HMS_WEBACTION;
import com.hms.web.hbd1.dao.HMP0BID_DAO;
import com.hms.web.hbd1.dao.HMP0ITEM_DAO;
import com.hms.web.hbd1.dao.HMP0POMM_DAO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * HBD1W662
 * 
 * @author Window10
 * 
 */
public class HBD1W662_ACTION implements HMS_WEBACTION {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");

	/**
	 * DEFAULT ACTION
	 */
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		//logger.info("HBD1W662 doAction:#");
		
		/* 第一次進 來載入廠商資料 */
		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);

		
		
	}

	@Override
	public void doCreate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {

		//logger.info("jc:#");
	}

	@Override
	public void doRead(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		logger.warn("action jr:#");

		HttpSession session = request.getSession();		
		PrintWriter out = response.getWriter();
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");
		String compCode = ui.getCompCode(request);				
		String method = UTIL_String.nvl(request.getParameter("methodName"), null);		
		
		System.out.println("#### 662 query data list !!!!!");
		
		
		
		
		if ("QUERY_DATA_LIST".equals(method)){
			this.QUERY_DATA_LIST(compCode, request, out);
		}
		
	}

	

	@Override
	public void doUpdate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		logger.info("doUpdate:#");
		
		
		
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, Exception {
		
		

		 

	}	
	
	
	private void QUERY_DATA_LIST(String compCode, HttpServletRequest request, PrintWriter out) throws Exception{				
		
		String qryInfo = UTIL_String.nvl(request.getParameter("qryInfo"), "");
		JSONObject qryJo = JSONObject.fromObject(qryInfo);
		
		JSONObject rtnJo = new JSONObject();
		PrepareExecuteSql pes = new PrepareExecuteSql();
		
		UserInfo ui = (UserInfo) request.getSession().getAttribute("UserInfo");		
		
		//System.out.println("qry: " + qryJo.toString());
		
		
		
		pes.openConnection("HBD_DMZ");
		
		HMP0ITEM_DAO itemDao = new HMP0ITEM_DAO(pes.getConnection());
				
		/**
		 * 662 待改
		 */
        String qryYear = qryJo.getString("qryYear");
        JSONArray poAry = itemDao.queryYearTable(compCode, qryYear);
        pes.getConnection().close();
        JSONArray custAry = new JSONArray();
        int custCnt = 0;
        
        String curBuyer = "";
        String preBuyer = "";
        String curName = "";
        String initMonth = "";
        String monthAmount = "";
        JSONObject custYearMonthJo = null;
        StringBuilder msgSb = new StringBuilder();
        
        msgSb.append("poAry: " + poAry.size() + "\n");
        
        for(int i=0; i < poAry.size(); i++) {
        	
            JSONObject monthJo = poAry.getJSONObject(i);
            
            //  01,02,03
            String poMonth = monthJo.getString("pommMonth").substring(4,6);
            // M01, M02, M03
            poMonth = "M" + poMonth;
            curBuyer = monthJo.getString("pommBuyer");
            curName = monthJo.getString("hac0BascName");
            monthAmount = monthJo.getString("monthAmt").replace(".0", "");
            
            if(i==0) {
                // 第1筆
                preBuyer = curBuyer;                                        
                custYearMonthJo = new JSONObject();
                custYearMonthJo.put("buyer", curBuyer);
                custYearMonthJo.put("buyerName", curName);
                for(int j=1; j <= 12; j++) {
                    if (j < 10) {
                        initMonth = "M0" + String.valueOf(j);
                    }else {
                        initMonth = "M" + String.valueOf(j);
                    }
                    custYearMonthJo.put(initMonth, "");
                }
            }
            
            
            // 同1個人
            if(curBuyer.equals(preBuyer)) {
                custYearMonthJo.put(poMonth, monthAmount);
            }else {
                // 不同人，先放到CUSTARY，再開新的YEARJO
                custAry.add(custCnt, custYearMonthJo);
                custCnt +=1;
                preBuyer = curBuyer;                    
                custYearMonthJo = new JSONObject();
                custYearMonthJo.put("buyer", curBuyer);
                custYearMonthJo.put("buyerName", curName);
                for(int j=1; j <= 12; j++) {
                    if (j < 10) {
                        initMonth = "M0" + String.valueOf(j);
                    }else {
                        initMonth = "M" + String.valueOf(j);
                    }
                    custYearMonthJo.put(initMonth, "");
                }                
                custYearMonthJo.put(poMonth, monthAmount);                
            }
            
            // 最後1筆資料，把最後1個CUST放進去
            if (i == poAry.size()-1) {
                custAry.add(custCnt, custYearMonthJo);
            }
        }              
        
        rtnJo.put("CUST_YEAR_REPORT", custAry);
        
        
        
        /**
         * testing code2
         * month report
         */
        
        //JSONArray monthItemAry = itemDao.queryMonthItem(compCode, "201701", "53172712");
        //JSONArray monthAry = itemDao.queryCustMonthTable(compCode, "201701", "53172712"); 
        
        //System.out.println("monthItemAry: " + monthItemAry);
        //System.out.println("monthAry: " + monthAry);                     
        
        
        String msg = MsgUtils.toString(true, "", rtnJo);
        System.out.println("return po");
        System.out.println(msg);
        out.print(msg);
        //System.out.println(msg);
	
	}

}
