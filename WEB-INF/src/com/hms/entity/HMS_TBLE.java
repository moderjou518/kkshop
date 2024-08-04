package com.hms.entity;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class HMS_TBLE {
    
    private ArrayList cols = null;
    private int colsCount = 0;
    private List<HMS_TROW> rows = null;
    private int rowsCount = 0;

    public HMS_TBLE() {
        this.colsCount = 0;
        this.rowsCount = 0;
        this.cols = new ArrayList();
        this.rows = new ArrayList();
    }

    public HMS_TBLE(
    		String[] colAry , ArrayList rowList ) {
        this.colsCount = 0;
        this.rowsCount = 0;
        this.cols = new ArrayList();
        this.rows = new ArrayList();
        this.SetDataRow( colAry , rowList );
    }    
    

    /**
     * 設定資料表欄位及資料
     * @param parmAry [0]:String[] colAry, [1]: ArrayList rowList
     * @return void
     */
    public void SetDataRow( String[] colAry, ArrayList dataList ) {
        try {
            this.SetColumns( colAry );
            this.SetRows( dataList );
        } catch( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void CreateDataRow( HMS_TROW tr ) {
        try {
            this.rows.add(tr);
            this.rowsCount++;
        } catch( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 設定資料表欄位
     * @param parmAry [0]:String[] colAry
     * @return void
     */
    private void SetColumns( String[] colAry ) throws Exception {
        if ( null != colAry ) {
            this.colsCount = colAry.length;
            for( int i = 0 ; i < this.colsCount ; i++ ) {            	
                this.cols.add( i , colAry[i].toString()) ;                
            }
        } else {
            this.colsCount = 0;
        }
    }

    /**
     * 取得欄位名稱
     * @param parmAry [0]:String[] colAry, [1]: ArrayList rowList
     * @return String
     */
    private String GetColumnName( int j ) {
        return (String)this.cols.get( j );
    }

    /**
     * 設定資料表的資料
     * @param parmAry [0]: ArrayList rowList 
     * @return String
     */
    public void SetRows( ArrayList rowsList ) {

        HMS_TROW datarow = null;

        if ( null != rowsList && rowsList.size() > 0 ) {

            this.rowsCount = rowsList.size();
            for( int i = 0 ; i < this.rowsCount ; i++ ) {
                String[] tmpArr = (String[])rowsList.get( i );
                datarow = new HMS_TROW();
                for( int j = 0 ; j < tmpArr.length ; j++ ) {
                	datarow.SetCell( this.GetColumnName( j ) , tmpArr[j] );
                }
                this.rows.add( i , datarow);
            }
            // obj = null 資料存入, obj != null 資料取代    
        } else {
            this.rowsCount = 0;
        }
    }

    /**
     * 取得資料表資料筆數
     * @param parmAry  
     * @return int
     */
    public int GetRowsCount() {
        return this.rowsCount;
    }

    /**
     * 取得資料表欄位數
     * @param parmAry  
     * @return int
     */
    public int GetColsCount() {
        return this.colsCount;
    }

    /**
     * 取得所有資料列
     * @param parmAry  
     * @return int
     */
    private List Rows() {
        return this.rows;
    }

    /**
     * 取得第n筆資料列
     * @param parmAry [0]: int i  
     * @return DataRow
     */
    public HMS_TROW getDataRow( int i ) {
        return (HMS_TROW)this.Rows().get( i );
    }    
    
    public String dataTable2Json(){
		
		JSONObject json = null;
		JSONArray jsonAry = new JSONArray();
		if (null!=this.rows){
			for(int i=0; i < this.GetRowsCount(); i++){
				HMS_TROW dr = this.getDataRow(i);			
				Enumeration enu = dr.getAllColumns();
				json = new JSONObject();
				while(enu.hasMoreElements()){
					String colName = (String) enu.nextElement();
					String colVal = dr.GetField(colName);
					json.put(colName, colVal);
				}			
				jsonAry.add(json);
			}	
		}
		
		return jsonAry.toString();
	}

}