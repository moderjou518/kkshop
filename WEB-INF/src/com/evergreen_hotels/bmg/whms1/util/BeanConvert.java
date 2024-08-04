package com.evergreen_hotels.bmg.whms1.util;


import com.evergreen.web.security.AntiXss;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.bidimap.DualHashBidiMap;



public class BeanConvert
{
  public static <T> T request2Bean(HttpServletRequest request, Class<T> t)
    throws BeanConvertException
  {
    try
    {
      Map<String, Object> newmap = new HashMap();
      for (Object key : request.getParameterMap().keySet()) {
        String paramName = key.toString();
        if (!paramName.contains(".")) {
          newmap.put(paramName, AntiXss.getSafeHtmlFragment(request.getParameter(paramName)));
        }
      }
      T bean = t.newInstance();
      BeanUtils.populate(bean, newmap);
      return bean;
    } catch (Exception e) {
      throw new BeanConvertException(e);
    }
  }
  







  public static <T> T request2Bean(HttpServletRequest request, Class<T> t, DualHashBidiMap keymapping)
    throws BeanConvertException
  {
    try
    {
      if (keymapping != null) {
        Map<String, Object> newmap = new HashMap();
        for (Object key : request.getParameterMap().keySet()) {
          String paramName = keymapping.getKey(key.toString()).toString();
          if ((paramName != null) && (!paramName.contains("."))) {
            newmap.put(paramName, AntiXss.getSafeHtmlFragment(request.getParameter(key.toString())));
          }
        }
        T bean = t.newInstance();
        BeanUtils.populate(bean, newmap);
        return bean;
      }
      return (T)request2Bean(request, t);
    }
    catch (Exception e) {
      throw new BeanConvertException(e);
    }
  }
  




  public static JSONObject request2JSONObject(HttpServletRequest request)
  {
    JSONObject jsonObject = new JSONObject();
    Map<String, String[]> map = request.getParameterMap();
    Set<Map.Entry<String, String[]>> s = map.entrySet();
    Iterator<Map.Entry<String, String[]>> it = s.iterator();
    while (it.hasNext()) {
      Map.Entry<String, String[]> entry = (Map.Entry)it.next();
      String key = (String)entry.getKey();
      String[] value = (String[])entry.getValue();
      if (value.length > 1) {
        jsonObject.put(key, value);
      } else {
        jsonObject.put(key, value[0].toString());
      }
    }
    return jsonObject;
  }
  
  public static <T> T transJSONString2Bean(String json, Class<T> t, Map<String, Class<?>> classMap, DualHashBidiMap keymapping) {
    JSONObject jsonObject = JSONObject.fromObject(json);
    Map<?, ?> paramMap = null;
    if (classMap == null) {
      paramMap = (Map)JSONObject.toBean(jsonObject, Map.class);
    } else {
      paramMap = (Map)JSONObject.toBean(jsonObject, Map.class, classMap);
    }
    
    if (keymapping != null) {
      Map<String, Object> newmap = new HashMap();
      for (Object key : paramMap.keySet()) {
        String paramName = keymapping.getKey(key.toString()).toString();
        if (paramName != null) {
          newmap.put(paramName, paramMap.get(key.toString()));
        }
      }
      try {
        T bean = t.newInstance();
        BeanUtils.populate(bean, newmap);
        return bean;
      } catch (Exception e) {
        throw new BeanConvertException(e);
      }
    }
    try {
      T bean = t.newInstance();
      BeanUtils.populate(bean, paramMap);
      return bean;
    } catch (Exception e) {
      throw new BeanConvertException(e);
    }
  }
  









  public static <T> T transJSONString2Bean(String json, Class<T> t, Map<String, Class<?>> classMap)
  {
    JSONObject jsonObject = JSONObject.fromObject(json);
    if (classMap == null) {
      return (T)JSONObject.toBean(jsonObject, t);
    }
    return (T)JSONObject.toBean(jsonObject, t, classMap);
  }
  






  public static <T> T transJSONString2Bean(String json, Class<T> t)
  {
    return (T)transJSONString2Bean(json, t, null);
  }
  




  public static String transBean2JSONString(Object bean)
  {
    JSONObject jsonObj = JSONObject.fromObject(bean);
    return jsonObj.toString();
  }
  




  public static void transMap2Bean(Map<String, ?> map, Object obj)
    throws BeanConvertException
  {
    if ((map == null) || (obj == null)) {
      return;
    }
    try {
      BeanUtils.populate(obj, map);
    } catch (Exception e) {
      throw new BeanConvertException(e);
    }
  }
  







  public static Map<String, Object> transBean2Map(Object obj)
  {
    if (obj == null) {
      return null;
    }
    Map<String, Object> map = new HashMap();
    try {
      BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
      PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
      PropertyDescriptor[] arrayOfPropertyDescriptor1; int j = (arrayOfPropertyDescriptor1 = propertyDescriptors).length; for (int i = 0; i < j; i++) { PropertyDescriptor property = arrayOfPropertyDescriptor1[i];
        String key = property.getName();
        
        if (!key.equals("class"))
        {
          Method getter = property.getReadMethod();
          Object value = getter.invoke(obj, new Object[0]);
          map.put(key, value);
        }
      }
    } catch (Exception e) {
      throw new BeanConvertException(e);
    }
    return map;
  }
  
  public static JSONObject transMap2JSONObject(Map<String, ?> data) {
    return JSONObject.fromObject(data);
  }
  
  public static JSONArray transRowDataList2JSONArray(List<RowData> dataList) {
    JSONArray jsonArray = new JSONArray();
    if ((dataList == null) || (dataList.isEmpty())) return jsonArray;
    for (RowData data : dataList) {
      jsonArray.add(data.toJSONObject());
    }
    return jsonArray;
  }
  
  public static JSONArray transRowDataList2JSONArray(List<RowData> dataList, DualHashBidiMap keymapping) {
    JSONArray jsonArray = new JSONArray();
    if ((dataList == null) || (dataList.isEmpty())) return jsonArray;
    for (RowData data : dataList) {
      jsonArray.add(data.toJSONObject(keymapping));
    }
    return jsonArray;
  }
  
  public static Map<String, String> swapMap(Map<String, String> map, DualHashBidiMap keymapping) {
    Map<String, String> newMap = new HashMap();
    for (String key : map.keySet()) {
      String newKey = (String)keymapping.get(key);
      if (newKey != null) {
        newMap.put(newKey, (String)map.get(key));
      }
    }
    return newMap;
  }
  
  public static void main(String[] args) {
    Map<?, ?> map = (Map)transJSONString2Bean("{ \"key\": \"values\" }", Map.class);
    System.out.println(map.get("key"));
  }
}