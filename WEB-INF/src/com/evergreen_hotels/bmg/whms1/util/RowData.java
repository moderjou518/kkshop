package com.evergreen_hotels.bmg.whms1.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import net.sf.json.JSONObject;
//import oracle.sql.ROWID;
import org.apache.commons.collections.bidimap.DualHashBidiMap;

public abstract interface RowData extends Serializable
{
  public abstract String getValue(String paramString);
  
  public abstract JSONObject toJSONObject(DualHashBidiMap paramDualHashBidiMap);
  
  public abstract JSONObject toJSONObject();
  
  public abstract Map<String, String> toMap();
  
  public abstract void toBean(Object paramObject);
  
  public abstract String[] toStringArray();
  
  public RowData fetchOne(ResultSet rs, RowColumn[] columns, String space) throws SQLException;
}