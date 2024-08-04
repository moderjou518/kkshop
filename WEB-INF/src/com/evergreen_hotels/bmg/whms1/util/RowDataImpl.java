package com.evergreen_hotels.bmg.whms1.util;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.commons.collections.bidimap.DualHashBidiMap;

public class RowDataImpl
  implements RowData
{
  private static final long serialVersionUID = 93385222300492337L;
  private RowColumn[] columns;
  private Map<String, String> record = new HashMap();
  
  public RowDataImpl(RowColumn[] columns) {
    this.columns = columns;
  }
  
  public void put(RowColumn column, String fieldValue) {
    record.put(column.getAttributeName(), fieldValue);
  }
  
  public Map<String, String> toMap() {
    return record;
  }
  
  public JSONObject toJSONObject(DualHashBidiMap keymapping) {
    if (keymapping != null) {
      Map<String, String> newMap = BeanConvert.swapMap(record, keymapping);
      return BeanConvert.transMap2JSONObject(newMap);
    }
    return BeanConvert.transMap2JSONObject(toMap());
  }
  
  public void toBean(Object obj)
  {
    BeanConvert.transMap2Bean(toMap(), obj);
  }
  
  public String[] toStringArray() {
    String[] strArr = new String[columns.length];
    for (int i = 0; i < strArr.length; i++) {
      strArr[i] = ((String)record.get(columns[i].getAttributeName()));
    }
    return strArr;
  }
  
  public String getValue(String fieldName) {
    return (String)record.get(fieldName);
  }
  
  public JSONObject toJSONObject()
  {
    return toJSONObject(null);
  }

@Override
public RowData fetchOne(ResultSet rs, RowColumn[] columns, String space) throws SQLException {
	// TODO Auto-generated method stub
	return null;
}
}
