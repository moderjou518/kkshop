package com.hms.web.hbd1.dao;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

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



public class HAC0BASC_DAO extends GenericDao {
	
	Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");
	
	public HAC0BASC_DAO(Connection conn) throws SQLException{
		super.setConnection(conn);
	}
    
       /**
     * 帳密驗證
     * @param tketType
     * @return
     */
    public boolean loginCheck(String comp, String loginID, String password){
    	
    	StringBuffer sb = new StringBuffer();    	   
    	sb.append(" Select count(*)                              ");
    	sb.append("   From HAC0BASC                              ");
    	sb.append("  Where hac0_basc_comp_code = ?    ");
    	sb.append("    and hac0_basc_login_id = ?     ");
    	sb.append("    and hac0_basc_pwd = ?           ");
    	sb.append("    and nz(hac0_void_mark, '#') <> 'Y'        ");
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(comp);
    	parms.add(loginID);
    	parms.add(password);   	
    	
    	boolean chk = false;
    	int cnt = 0;
    	try {       
    		
    		//logger.debug(String.format("  @ c:%s, id/pw:%s/%s", comp, loginID, password));
    		
    		
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.getAInt(sb.toString(), parms.toArray());
            
            //System.out.println("Cnt: " + cnt);
            chk = (cnt > 0);
            //logger.debug("  @ dao.loginCheck: " + chk);
        } catch (SQLException e) {
            cnt = 0;
          	throw new DataAccessException(e);
        }
    	
    	return chk;	
    }
    
    public JSONObject getProfile(String comp, String loginID){
    	
    	StringBuffer sb = new StringBuffer();    	   
    	sb.append(" SELECT HAC0_BASC_AC_CODE, HAC0_BASC_COMP_CODE, HAC0_BASC_LOGIN_ID, HAC0_BASC_NAME,  ");
    	sb.append("        HAC0_BASC_PWD, HAC0_BASC_GROUP, HAC0_BASC_SEQ, HAC0_BASC_NOTE,               ");
    	sb.append(" 	   HAC0_VOID_MARK, HAC0_MOD_USER, HAC0_MOD_TIME                                 ");
    	sb.append(" FROM HAC0BASC                                                                       ");
    	sb.append("  Where hac0_basc_comp_code = ?                                                      ");
    	sb.append("    and hac0_basc_login_id = ?                                                       ");    	
    	
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(comp);
    	parms.add(loginID);  	   	    	
    	
    	RowData rd = null;
    	JSONObject jo = null;
    	try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            rd = pre.getARowData(sb.toString(), parms.toArray());            
            if(null != rd){
            	jo = rd.toJSONObject();
            }else{
            	jo = null;
            }
            
        } catch (SQLException e) {
            rd = null;
          	throw new DataAccessException(e);
        }
    	
    	System.out.println("");
    	
    	return jo;	
    }
    
    public JSONArray getHacList(String comp, String group){
    	
    	StringBuffer sb = new StringBuffer();    	   
    	sb.append(" SELECT HAC0_BASC_AC_CODE, HAC0_BASC_COMP_CODE, HAC0_BASC_LOGIN_ID, HAC0_BASC_NAME,  ");
    	sb.append("        HAC0_BASC_PWD, HAC0_BASC_GROUP, HAC0_BASC_SEQ, HAC0_BASC_NOTE,               ");
    	sb.append(" 	   HAC0_VOID_MARK, HAC0_MOD_USER, HAC0_MOD_TIME                                 ");
    	sb.append(" FROM HAC0BASC                                                                       ");
    	sb.append("  Where hac0_basc_comp_code = ?                                                      ");
    	sb.append("    and HAC0_BASC_GROUP = ?                                                       	");
    	sb.append(" ORDER BY nz(HAC0_BASC_NOTE, 'ZZZZZZ'), HAC0_BASC_LOGIN_ID											");
    	
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(comp);
    	parms.add(group);  	   	    	
    	
    	RowDataList rd = null;
    	JSONArray ary = null;
    	try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            rd = pre.getRowDataList(sb.toString(), parms.toArray());
            if(null != rd){
            	ary = rd.toJSONArray();
            }            
        } catch (SQLException e) {
            rd = null;
          	throw new DataAccessException(e);
        }
    	
    	return ary;	
    }
    
    public boolean saveProfile(String comp, String modUser, JSONObject jo){
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" UPDATE HAC0BASC 				");
    	sb.append("    SET HAC0_BASC_NOTE = ? 		");
    	sb.append("       ,HAC0_MOD_USER = ? 		");
    	
    	parms.add(jo.getString("txtHac0BascNote"));
    	parms.add(modUser);
    	
    	if(!jo.getString("txtNewPassword").equals("")){
    		sb.append("       ,HAC0_BASC_PWD = ? 	");
    		parms.add(jo.getString("txtNewPassword"));
    	}
    	    	
    	sb.append("  Where hac0_basc_comp_code = ?  ");
    	sb.append("    and HAC0_BASC_AC_CODE = ?    ");    	
    	parms.add(comp);
    	parms.add(jo.getString("txtHac0BascAcCode"));  	
    	    	
    	
    	int cnt = 0;
    	try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.updateSql(sb.toString(), parms.toArray());            
        } catch (SQLException e) {
            cnt = 0;
          	throw new DataAccessException(e);
        }
    	
    	return (cnt > 0);
    }
    
    public boolean updProfile(String comp, String modUser, JSONObject jo){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" UPDATE HAC0BASC ");
    	sb.append("    SET HAC0_BASC_LOGIN_ID = ? 	");
    	sb.append("       ,HAC0_BASC_NAME = ? 		");
    	sb.append("       ,HAC0_BASC_PWD = ? 		");
    	sb.append("       ,HAC0_BASC_GROUP = ? 		");
    	sb.append("       ,HAC0_BASC_NOTE = ? 		");
    	sb.append("       ,HAC0_VOID_MARK = ? 		");
    	sb.append("       ,HAC0_MOD_USER = ? 		");
    	//sb.append("       ,HAC0_MOD_TIME = ? 		");    	
    	sb.append("  Where hac0_basc_comp_code = ?  ");
    	sb.append("    and HAC0_BASC_AC_CODE = ?    ");    	
    	
    	
    	ArrayList<String> parms = new ArrayList<String>();    	
    	parms.add(jo.getString("txtHascBascLoginId"));
    	parms.add(jo.getString("txtHac0BascName"));
    	parms.add(jo.getString("txtHac0BascPwd"));
    	parms.add(jo.getString("dlHac0BascGroup"));
    	parms.add(jo.getString("txtHac0BascNote"));
    	parms.add(jo.getString("txtVoidMark").replace("[", "").replace("]", "").replace("\"", ""));
    	parms.add(modUser);
    	parms.add(comp);
    	parms.add(jo.getString("txtHac0BascAcCode"));    	
    	
    	int cnt = 0;
    	try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.updateSql(sb.toString(), parms.toArray());            
        } catch (SQLException e) {
            cnt = 0;
          	throw new DataAccessException(e);
        }
    	
    	return (cnt > 0);
    }
    
    public boolean addProfile(String comp, String modUser, JSONObject jo){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" INSERT INTO HAC0BASC(hac0_basc_comp_code  ");   	    	
    	sb.append("       ,HAC0_BASC_LOGIN_ID		");
    	sb.append("       ,HAC0_BASC_NAME 			");
    	sb.append("       ,HAC0_BASC_PWD 			");
    	sb.append("       ,HAC0_BASC_GROUP 			");
    	sb.append("       ,HAC0_BASC_NOTE 			");
    	sb.append("       ,HAC0_VOID_MARK 			");
    	sb.append("       ,HAC0_MOD_USER) 			"); 	    	
    	sb.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?)  ");    	    	
    	
    	ArrayList<String> parms = new ArrayList<String>();
    	parms.add(comp);
    	parms.add(jo.getString("txtHascBascLoginId"));
    	parms.add(jo.getString("txtHac0BascName"));
    	parms.add(jo.getString("txtHac0BascPwd"));
    	parms.add(jo.getString("dlHac0BascGroup"));
    	parms.add(jo.getString("txtHac0BascNote"));
    	parms.add(jo.getString("txtVoidMark").replace("[", "").replace("]", "").replace("\"", ""));
    	parms.add(modUser);
    	
    	int cnt = 0;
    	try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.updateSql(sb.toString(), parms.toArray());            
        } catch (SQLException e) {
            cnt = 0;
          	throw new DataAccessException(e);
        }
    	
    	return (cnt > 0);
    }
}