package com.evergreen_hotels.bmg.whms1.util;

import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.apache.commons.collections.bidimap.DualHashBidiMap;

public abstract interface RowDataList
{
  public abstract <T> List<T> toBeanList(Class<T> paramClass);
  
  public abstract List<Map<String, String>> toMapList();
  
  public abstract List<RowData> toRowDataList();
  
  public abstract JSONArray toJSONArray(DualHashBidiMap paramDualHashBidiMap);
  
  public abstract JSONArray toJSONArray();
  
  public abstract RowData getRowData(String paramString);
  
  public abstract int size();
}