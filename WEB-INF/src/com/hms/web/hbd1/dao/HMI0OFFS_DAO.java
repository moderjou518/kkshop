package com.hms.web.hbd1.dao;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.Logger;

import com.evergreen_hotels.bmg.whms1.exception.DataAccessException;
import com.evergreen_hotels.bmg.whms1.util.GenericDao;
import com.evergreen_hotels.bmg.whms1.util.PrepareExecuteSql;
import com.evergreen_hotels.bmg.whms1.util.RowData;
import com.evergreen_hotels.bmg.whms1.util.RowDataList;
//import com.evergreen_hotels.wlh.util.UTIL_ExceptionHandle;
//import com.evergreen.common.dao.GenericDao;
//import com.evergreen.common.dao.PrepareExecuteSql;
//import com.evergreen.common.exception.DataAccessException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HMI0OFFS_DAO extends GenericDao {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	public HMI0OFFS_DAO(Connection conn) throws SQLException{
		super.setConnection(conn);
	}
    
       /**
     * 新增匯款紀錄
     * @param tketType
     * @return
     */
    public boolean insertOffs(JSONObject offsJo){
    	
    	StringBuffer sb = new StringBuffer();    	   
    	sb.append(" Insert into HMI0OFFS                          ");
    	sb.append(" (OFFS_COMP_CODE, OFFS_BUYER, OFFS_BIDM_UUID,  ");
    	sb.append("  OFFS_PAY_DATE, OFFS_PAY_AMT, OFFS_PAY_NOTE,  ");
    	sb.append("  OFFS_MOD_TIME, OFFS_DOC_MARK, OFFS_DOC_TIME) ");
    	sb.append(" VALUES                                        ");
    	sb.append(" (?, ?, ?, ?, ?, ?, NOW(), ?, NOW())           ");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(offsJo.getString("OFFS_COMP_CODE"));
    	parms.add(offsJo.getString("OFFS_BUYER"));
    	parms.add(offsJo.getString("OFFS_BIDM_UUID"));   	
    	parms.add(offsJo.getString("OFFS_PAY_DATE"));
    	parms.add(offsJo.getString("OFFS_PAY_AMT"));
    	parms.add(offsJo.getString("OFFS_PAY_NOTE"));    	
    	parms.add(offsJo.getString("OFFS_DOC_MARK"));
    	
    	
    	boolean result = false;
    	int cnt = 0;
    	try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.updateSql(sb.toString(), parms.toArray());
            result = (cnt > 0);
        } catch (SQLException e) {
            result = false;
          	throw new DataAccessException(e);
        }
    	
    	return result;	
    }
    

    
    public JSONObject getOneOffs(String comp, String buyer, String bidmUUID){
    	
    	StringBuffer sb = new StringBuffer();
    	
    	sb.append(" Select OFFS_BUYER, OFFS_BIDM_UUID,            	");
    	sb.append("        OFFS_PAY_DATE, OFFS_PAY_AMT, OFFS_PAY_NOTE  	");
    	sb.append("   From HMI0OFFS                                    	");
    	sb.append("  Where OFFS_COMP_CODE = ?                          	");
    	sb.append("    and OFFS_BUYER = ?                         	");
    	sb.append("    and OFFS_BIDM_UUID = ?                          	");
    	sb.append("    AND OFFS_DOC_MARK = 'Y'     						");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(comp);
    	parms.add(buyer);
    	parms.add(bidmUUID);
    	
    	JSONObject rtn = null;
    	try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            RowData rd = pre.getARowData(sb.toString(), parms.toArray());
            
            if (rd != null){
            	rtn = rd.toJSONObject();
            }else{
            	rtn = new JSONObject();
            	rtn.put("offsPayDate", "");
            	rtn.put("offsPayAmt", "0");
            }
        } catch (SQLException e) {
        	String msg = String.format("Get Ooffs Error-Comp:%s, Buyer:%s, BidUUID:%s", comp, buyer, bidmUUID);
        	logger.error(msg);
          	throw new DataAccessException(e);
        }
    	
    	return rtn;
    	
    }
    
    /**
     * 作廢之前的匯款紀錄
     * @param comp
     * @param buyer
     * @param bidUUID
     */
    public void resetDocMark(String comp, String buyer, String bidUUID){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" UPDATE HMI0OFFS                ");
    	sb.append("    SET OFFS_DOC_MARK = 'V',    ");
    	sb.append("        OFFS_DOC_TIME = NOW()   ");
    	sb.append("  WHERE OFFS_COMP_CODE = ?     ");
    	sb.append("    AND OFFS_BUYER = ?         ");
    	sb.append("    AND OFFS_BIDM_UUID = ?     ");
    	sb.append("    AND OFFS_DOC_MARK = 'Y'     ");
    	
    	ArrayList<String> parms = new ArrayList<String>();    	
    	parms.add(comp);
    	parms.add(buyer);
    	parms.add(bidUUID);    	
    	
    	try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            pre.updateSql(sb.toString(), parms.toArray());
        } catch (SQLException e) {
        	String msg = String.format("Update Error-Comp:%s, Buyer:%s, BidUUID:%s", comp, buyer, bidUUID);
        	logger.error(msg);
          	throw new DataAccessException(e);
        }
    	
    }
    
    /**
     * 查預訂單那周的匯款紀錄
     * @param comp
     * @param bidUUID
     * @return
     */
    public JSONArray queryOffsList(String comp, String bidUUID){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("Select o.offs_bidm_uuid, o.offs_buyer, a.hac0_basc_name, ");
    	sb.append("       o.offs_pay_date,  o.offs_pay_amt,                 ");
    	sb.append("o.offs_mod_time, o.offs_doc_time  ");
    	sb.append("  From HMI0OFFS o, HAC0BASC a                            ");
    	sb.append(" Where o.offs_comp_code = ?                             ");
    	sb.append("   and o.offs_bidm_uuid = ?                             ");
    	sb.append("   and o.offs_doc_mark = 'Y'                             ");
    	sb.append("   and o.offs_comp_code = a.hac0_basc_comp_code               ");
    	sb.append("   and o.offs_buyer = a.hac0_basc_login_id               ");
    	sb.append("Order by A.HAC0_BASC_NOTE, o.offs_doc_time desc				");
    	
    	ArrayList<String> parms = new ArrayList<String>();    	
    	parms.add(comp);    	
    	parms.add(bidUUID);    	
    	
    	//System.out.println("****SQL:   " + sb.toString());
    	//System.out.println("****IDID:   " + bidUUID);
    	
    	RowDataList rdl = null;
    	JSONArray ary = null;
    	try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            rdl = pre.getRowDataList(sb.toString(), parms.toArray());
            if (rdl != null){
            	ary = rdl.toJSONArray();
            }
        } catch (SQLException e) {
        	String msg = String.format("Update Error-Comp:%s, BidUUID:%s", comp, bidUUID);
        	logger.error(msg);
          	throw new DataAccessException(e);
        }
    	
    	return ary;
    	
    }
    
}