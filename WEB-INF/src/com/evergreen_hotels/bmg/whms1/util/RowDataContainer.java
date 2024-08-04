package com.evergreen_hotels.bmg.whms1.util;

import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class RowDataContainer
{
  protected int max_rows = 9999;
  protected String space = "";
  
  protected RowColumn[] columns;
  private RowDataListImpl dataSet = null;
  
  public void setMaxRows(int max_rows) {
    this.max_rows = max_rows;
  }
  
  public void setSpace(String space) {
    this.space = space;
  }
  
  public RowDataList getRowDataList() {
    return dataSet;
  }
  
  public void fetchData(ResultSet rs) throws java.sql.SQLException {
	  
    dataSet = new RowDataListImpl();    
    ResultSetMetaData md = rs.getMetaData();
    columns = getColumnsInfo(md);
    
    Object data_value = null;
    for (int j = 0; j < max_rows; j++){
    	
      if (!rs.next())
        break;
      RowDataImpl data = new RowDataImpl(columns);      

      for (int i = 1; i < columns.length + 1; i++){
    	  
        RowColumn column = columns[(i - 1)];        
        data_value = rs.getObject(i);        

        if (data_value != null){        	
        	
        	if (column.getType() == 2005) {
        		Clob clob = rs.getClob(i);
        		data.put(column, clob.getSubString(1L, (int)clob.length()));
        	} else if (column.getType() == 2004) {
        		Blob blob = rs.getBlob(i);
        		byte[] bdata = blob.getBytes(1L, (int)blob.length());
        		try {
        			data.put(column, new String(bdata, "UTF-8"));
        		} catch (UnsupportedEncodingException e) {
        			data.put(column, space);
        			e.printStackTrace();
        		}
        	} else if (column.getType() == -8) {
        		//String rowid = ((oracle.sql.ROWID)data_value).stringValue();
        		String rowid = data_value.toString();
        		data.put(column, rowid);
        		dataSet.addIndex(rowid, data);
        	} else {
        		data.put(column, data_value.toString());
        	}
        }else {
        	data.put(column, space);
        	//System.out.println(column.getName() + " : null value");
        }
        
      }
      dataSet.add(data);
    }
  }
  


  public static RowColumn[] getColumnsInfo(ResultSetMetaData rsmd)
    throws java.sql.SQLException
  {
    int numColumns = rsmd.getColumnCount();
    RowColumn[] columns = new RowColumn[numColumns];
    RowColumn column = null;
    for (int i = 1; i <= numColumns; i++) {
      column = new RowColumn(rsmd.getColumnName(i), rsmd.getColumnType(i));
      columns[(i - 1)] = column;
    }
    return columns;
  }
}