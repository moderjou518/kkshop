package com.evergreen_hotels.bmg.whms1.whti1.dao;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.List;

import com.evergreen_hotels.bmg.whms1.exception.DataAccessException;
import com.evergreen_hotels.bmg.whms1.util.GenericDao;
import com.evergreen_hotels.bmg.whms1.util.PrepareExecuteSql;
import com.evergreen_hotels.bmg.whms1.util.RowData;
import com.evergreen_hotels.bmg.whms1.util.RowDataList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Hti0miscDAO extends GenericDao {
	
	public Hti0miscDAO(Connection conn) throws SQLException{
		super.setConnection(conn);
	}
    
	/**
	 * 取回指定類別下的系統參數
	 * @param comp
	 * @param miscKind
	 * @return
	 */
    public RowDataList getDataListByKind(String comp, String miscKind){
        
    	RowDataList rdl = null;
        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            StringBuffer sb = new StringBuffer();
            sb.append("   SELECT MISC_KIND,            	");
            sb.append("          MISC_CODE,            	");
            sb.append("          MISC_TYPE,            	");
            sb.append("          MISC_LOC_DESC,        	");
            sb.append("          MISC_ENG_DESC,        	");
            sb.append("          MISC_DATA1,           	");
            sb.append("          MISC_NUM1,            	");
            sb.append("          MISC_DATA2,           	");
            sb.append("          MISC_NUM2,            	");
            sb.append("          MISC_NOTE,            	");
            sb.append("          MISC_MOD_USER,        	");
            sb.append("          MISC_MOD_DATE         	");
            sb.append("     FROM HTI0MISC              	");
            sb.append("    WHERE MISC_COMP 	= :01  		");
            sb.append("      AND MISC_KIND 	= :02  		");
            sb.append(" ORDER BY MISC_CODE             	");
            Object[] parm = new Object[]{comp, miscKind};
        	
        	rdl = pre.getRowDataList(sb.toString(), parm);
        	return rdl;               
        } catch (SQLException e) {            
            throw new DataAccessException(e);
        }
        
    }   
    /**
     *  用KEY值取得系統參數
     * @param comp
     * @param miscKind
     * @param miscCode
     * @return
     */
    public RowData getDataByKindCode(String comp, String miscKind, String miscCode){
        
    	RowData rd = null;
        
        try {       
            PrepareExecuteSql pre = getPrepareExecuteSql();
            StringBuffer sb = new StringBuffer();
            sb.append("   SELECT MISC_KIND,            	");
            sb.append("          MISC_CODE,            	");
            sb.append("          MISC_TYPE,            	");
            sb.append("          MISC_LOC_DESC,        	");
            sb.append("          MISC_ENG_DESC,        	");
            sb.append("          MISC_DATA1,           	");
            sb.append("          MISC_NUM1,            	");
            sb.append("          MISC_DATA2,           	");
            sb.append("          MISC_NUM2,            	");
            sb.append("          MISC_NOTE,            	");
            sb.append("          MISC_MOD_USER,        	");
            sb.append("          MISC_MOD_DATE         	");
            sb.append("     FROM HTI0MISC              	");
            sb.append("    WHERE MISC_COMP 	= :01  		");
            sb.append("      AND MISC_KIND 	= :02  		");
            sb.append("      AND MISC_CODE 	= :03  		");            
            Object[] parm = new Object[]{comp, miscKind, miscCode};
        	
        	rd = pre.getARowData(sb.toString(), parm);        			
        	return rd;               
        } catch (SQLException e) {            
            throw new DataAccessException(e);
        }
        
    }
    
    
    
}