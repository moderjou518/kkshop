package com.evergreen_hotels.bmg.whms1.whti1.dao;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.evergreen_hotels.bmg.whms1.exception.DataAccessException;
import com.evergreen_hotels.bmg.whms1.util.GenericDao;
import com.evergreen_hotels.bmg.whms1.util.PrepareExecuteSql;
import com.evergreen_hotels.bmg.whms1.util.RowDataList;
//import com.evergreen_hotels.wlh.util.UTIL_ExceptionHandle;
//import com.evergreen.common.dao.GenericDao;
//import com.evergreen.common.dao.PrepareExecuteSql;
//import com.evergreen.common.exception.DataAccessException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Hti0tketDAO extends GenericDao {
	
	public Hti0tketDAO(Connection conn) throws SQLException{
		super.setConnection(conn);
	}
    
    public RowDataList queryDataList(String COMP, JSONObject jo){
        
    	ArrayList<String> parms = new ArrayList<String>();        
    	RowDataList rdl = null;        
        try {
            PrepareExecuteSql pre = getPrepareExecuteSql();
            StringBuffer sb = new StringBuffer();
            sb.append(" SELECT 	TKET_COMP,         ");
            sb.append("        	TKET_CODE,           ");
            sb.append("        	TKET_NAME,           ");
            sb.append("        	TKET_DESC,           ");
            sb.append("        	TKET_TYPE,           ");
            sb.append("        	TKET_ET_MARK,        ");
            sb.append("        	TKET_MP_MARK,        ");
            sb.append("        	TKET_ORI_PRICE,      ");
            sb.append("        	TKET_LMT_PRICE,      ");
            sb.append("        	TKET_LMT_MON,        ");
            sb.append("        	TKET_USE_DESC,       ");
            sb.append("        	TKET_VOID_MK,        ");
            sb.append("        	TKET_MOD_USER,       ");
            sb.append("		   	TO_CHAR(TKET_MOD_TIME, 'yyyy/MM/dd hh:mi') ");
            sb.append("        	TKET_MOD_TIME,       ");
            sb.append("			ROWIDTOCHAR (ROWID) tket_row_id ");
            sb.append("   FROM  HTI0TKET             ");
            sb.append("  WHERE  TKET_COMP = :01 	");
            parms.add(COMP);
            
            if (!"".equals(jo.getString("qryTketName"))){
            	sb.append("AND  TKET_NAME LIKE '%' || :QRYNAME || '%' ");
            	parms.add(jo.getString("qryTketName"));
            }
            
            if (!"*".equals(jo.getString("qryTketType"))){
            	sb.append("AND  TKET_TYPE = :QRYTYPE ");
            	parms.add(jo.getString("qryTketType"));
            	
            }            
        	sb.append("  ORDER BY TKET_CODE 		");
        	
        	rdl = pre.getRowDataList(sb.toString(), parms.toArray());
        	return rdl;               
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataAccessException(e);
        }
        
    }   
    
    public int addData(JSONObject jo){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" INSERT INTO HTI0TKET (                     			");
    	sb.append("    TKET_COMP, TKET_CODE, TKET_NAME,            		");
    	sb.append("    TKET_DESC, TKET_TYPE, TKET_ET_MARK, TKET_MP_MARK,");
    	sb.append("    TKET_ORI_PRICE, TKET_LMT_PRICE, TKET_LMT_MON, 	");
    	sb.append("    TKET_USE_DESC, TKET_VOID_MK, TKET_MOD_USER,   	");
    	sb.append("    TKET_MOD_TIME)                                	");
    	sb.append(" VALUES (                                         	");
    	sb.append("   :TKET_COMP,                                  		");
    	sb.append("   :TKET_CODE,                                    	");
    	sb.append("   :TKET_NAME,                                    	");
    	sb.append("   :TKET_DESC,                                    	");
    	sb.append("   :TKET_TYPE,                                    	");
    	sb.append("   :TKET_ET_MARK,                                 	");
    	sb.append("   :TKET_MP_MARK,                                 	");    	
    	sb.append("   :TKET_ORI_PRICE,                               	");
    	sb.append("   :TKET_LMT_PRICE,                               	");
    	sb.append("   :TKET_LMT_MON,                                 	");
    	sb.append("   :TKET_USE_DESC,                                	");
    	sb.append("   :TKET_VOID_MK,                                 	");
    	sb.append("   :TKET_MOD_USER,                                	");
    	sb.append("   GET_LOC_TIME())                                	");
    	
        Object[] parm = new Object[]{
        		jo.getString("txtComp"),
        		jo.getString("txtTketCode"),        		
                jo.getString("txtTketName"),
                jo.getString("txtTketDesc"),
                jo.getString("txtTketType"),
                jo.getString("txtTketEtMark"),
                jo.getString("txtTketMpMark"),                
                jo.getString("txtTketOriPrice"),
                jo.getString("txtTketLmtPrice"),
                jo.getString("txtTketLmtMon"),
                jo.getString("txtTketUseDesc"),
                jo.getString("txtTketVoidMk").replace("[", "").replace("]", "").replace("\"", ""),
                jo.getString("tketModUser")                                
        };

        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();            
            cnt = pre.updateSql(sb.toString(), parm);            
        } catch (SQLException e) {
            cnt = 0;
          	throw new DataAccessException(e);
        }    	
    	
    	return cnt;
    	
    }
    
    public int updateData(JSONObject jo){
    	
        StringBuffer sb = new StringBuffer();            
        sb.append(" UPDATE HTI0TKET                      		");
        sb.append(" SET                                        	");
        sb.append("     TKET_NAME        = :in_TKET_NAME       	");
        sb.append("    ,TKET_DESC        = :in_TKET_DESC       	");
        //sb.append("    ,TKET_TYPE        = :in_TKET_TYPE      ");
        //sb.append("    ,TKET_ET_MARK     = :in_TKET_ET_MARK   ");
      //sb.append("    ,TKET_MP_MARK     = :in_TKET_MP_MARK   ");
        sb.append("    ,TKET_ORI_PRICE   = :in_TKET_ORI_PRICE  	");
        sb.append("    ,TKET_LMT_PRICE   = :in_TKET_LMT_PRICE  	");
        sb.append("    ,TKET_LMT_MON     = :in_TKET_LMT_MON    	");
        sb.append("    ,TKET_USE_DESC    = :in_TKET_USE_DESC   	");
        sb.append("    ,TKET_VOID_MK     = :in_TKET_VOID_MK    	");
        sb.append("    ,TKET_MOD_USER    = :in_TKET_MOD_USER   	");
        sb.append("    ,TKET_MOD_TIME    = GET_LOC_TIME()   	");
        sb.append(" WHERE                                      	");
        sb.append("       ROWIDTOCHAR (ROWID) = :RID			");
        //sb.append("     TKET_COMP      = :in_TKET_COMP     	");
        //sb.append(" AND TKET_CODE        = :in_TKET_CODE       	");
        
        //System.out.println("VMK: " + jo.getString("txtTketVoidMk"));

        Object[] parm = new Object[]{
                jo.getString("txtTketName"),
                jo.getString("txtTketDesc"),
                jo.getString("txtTketOriPrice"),
                jo.getString("txtTketLmtPrice"),
                jo.getString("txtTketLmtMon"),
                jo.getString("txtTketUseDesc"),
                jo.getString("txtTketVoidMk").replace("[", "").replace("]", "").replace("\"", ""),
                jo.getString("tketModUser"),
                //jo.getString("comp"),                                
                //jo.getString("txtTketCode")
                jo.getString("txtTketRowId")
        };
        
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.updateSql(sb.toString(), parm);            
        } catch (SQLException e) {
            cnt = 0;
          	throw new DataAccessException(e);
        }
    	
    	return cnt;
    	
    }
    
    public int deleteThisData(String rowID){
    	
        StringBuffer sb = new StringBuffer();            
        sb.append(" DELETE FROM HTI0TKET                      		");        
        sb.append(" WHERE                                      	");
        sb.append("       ROWIDTOCHAR (ROWID) = :RID			");
        //sb.append("     TKET_COMP      = :in_TKET_COMP     	");
        //sb.append(" AND TKET_CODE        = :in_TKET_CODE       	");

        Object[] parm = new Object[]{
                //jo.getString("comp"),                                
                //jo.getString("txtTketCode")
                //jo.getString("txtTketRowId")
        		rowID
        };
        
        int cnt = 0;        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.updateSql(sb.toString(), parm);            
        } catch (SQLException e) {
            cnt = 0;
          	throw new DataAccessException(e);
        }
    	
    	return cnt;
    	
    } 
    
    /**
     * 券種是否有被商品使用中
     * @param tketType
     * @return
     */
    public boolean isInUse(String comp, String tketType){
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(" SELECT	COUNT(*) ");
    	sb.append("   FROM  HTI0ITDD ");
    	sb.append("  WHERE	COMP = :comp ");
    	sb.append("    AND  ITDD_TKET_TYPE = :TYPE ");
    	Object[] parm = new Object[]{comp, tketType};
    	
    	boolean inUse = false;
    	int cnt = 0;
    	try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            cnt = pre.getAInt(sb.toString(), parm);
            inUse = (cnt > 0);
        } catch (SQLException e) {
            cnt = 0;
          	throw new DataAccessException(e);
        }
    	
    	return false;	
    }
    
}