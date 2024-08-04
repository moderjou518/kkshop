package com.evergreen_hotels.bmg.whms1.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.apache.commons.collections.bidimap.DualHashBidiMap;

public class RowDataListImpl implements RowDataList
{
  private List<RowData> dataSet;
  private Map<String, RowData> rowDataSet = new java.util.LinkedHashMap();
  
  public RowDataListImpl() {
    dataSet = new ArrayList();
  }
  
  public RowDataListImpl(List<RowData> list) {
    dataSet = list;
  }
  
  public void addIndex(String rowid, RowData data) {
    rowDataSet.put(rowid, data);
  }
  
  public void add(RowData data) {
    dataSet.add(data);
  }
  
  public void addAll(List<RowData> list) {
    for (RowData data : list) {
      dataSet.add(data);
    }
  }
  
  public RowData getRowData(String rowid) {
    return (RowData)rowDataSet.get(rowid);
  }
  
  public List<RowData> toRowDataList() {
    return dataSet;
  }
  
  public <T> List<T> toBeanList(Class<T> t) {
    try {
      List<T> list = new ArrayList();
      for (RowData data : dataSet) {
        T obj = t.newInstance();
        data.toBean(obj);
        list.add(obj);
      }
      return list;
    } catch (Exception e) {
      throw new BeanConvertException(e);
    }
  }
  
  public List<Map<String, String>> toMapList() {
    try {
      List<Map<String, String>> list = new ArrayList();
      for (RowData data : dataSet) {
        list.add(data.toMap());
      }
      return list;
    } catch (Exception e) {
      throw new BeanConvertException(e);
    }
  }
  
  public JSONArray toJSONArray(DualHashBidiMap keymapping) {
    JSONArray jsonArr = new JSONArray();
    for (RowData data : dataSet) {
      jsonArr.add(data.toJSONObject(keymapping));
    }
    return jsonArr;
  }
  
  public JSONArray toJSONArray() {
    return toJSONArray(null);
  }
  
  public int size() {
    return dataSet.size();
  }
}