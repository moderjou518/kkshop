package com.hms.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Set;

public class HMS_TROW {
    
    private Hashtable rowData = null;

    public HMS_TROW() {
        this.rowData = new Hashtable();
    }
    
    public HMS_TROW( ArrayList colList , ArrayList valList ) {
        this.rowData = new Hashtable();
        this.SetCells( colList , valList );
    }

    /**
     * 以陣列設定所有的欄位及資料
     * @param parmAry [0]:colArray, valArray
     * @return String
     */
    public void SetCells( ArrayList colList , ArrayList valList ) {
        for( int i = 0 ; i < valList.size() ; i++ ) {
            this.SetCell( colList.get(i) , valList.get(i) );            
        }
    }
    
    

    /**
     * 設定的欄位及資料
     * @param parmAry [0]: String columnName, [1]: String columnVal
     * @return String
     */
    public void SetCell( Object colNam , Object colVal ) {        
        this.rowData.put( colNam.toString().toUpperCase() , colVal.toString() );        
    }
    
    /**
     * 複製欄位及資料
     * @param src
     * @param colName
     */
    public void CopySell(HMS_TROW src, String colName){
    	this.rowData.put(colName, src.GetField(colName));
    }

    /**
     * 取得該欄位資料
     * @param parmAry [0]:colName
     * @return String
     */
    public String GetField( String columnName ) {
    	String val = "";
    	
    	if(this.rowData.containsKey(columnName.toUpperCase())){
    		val = (String)this.rowData.get( columnName.toUpperCase());
    	}else{ 		   		
    		System.out.println("Warnning: no column found [" + columnName + "]");
    	}
        return val;
    }
    
    public BigDecimal GetFieldNumber(String columnName){
    	
    	BigDecimal val;
    	
    	try{
    		val = new BigDecimal(this.GetField(columnName));
    	}catch(Exception ex){
    		ex.printStackTrace();
    		System.out.println("Error: [" + columnName + "] value: " + this.GetField(columnName));;
    		val = new BigDecimal("0");    		
    	}    	
    	
    	return val;
    	
    }
    
    public String toJSONString(){
    	
    	StringBuffer sb = new StringBuffer();
    	Set<String> keys = this.rowData.keySet();
        for(String key: keys){
            sb.append(key);
            sb.append(": ");
            sb.append(this.GetField(key));
            sb.append(", ");
        }
        
        return sb.toString();
    	
    }
    
    /**
     * 取得所有欄位集合
     * @param parmAry [0]:colName
     * @return String
     */
    public Enumeration getAllColumns(){
        return this.rowData.keys();
    }
    
    /**
     * 取得欄位數量
     * @param parmAry [0]:colName
     * @return String
     */
    public int getColCount(){
        return this.rowData.size();    
    }       
}