package com.evergreen_hotels.bmg.whms1.util;

import javax.servlet.ServletContext;

public class ContextUtils {
  public static String ACTION_DISPATCHER = "ActionDispatcher";
  private static ContextUtils contextUtils;
  private String entryContext;
  
  private ContextUtils(String entryContext) {
    this.entryContext = entryContext;
  }
  


  public static ContextUtils getInstance()
  {
    return contextUtils;
  }
  
  public static void newInstance(ServletContext context) {
    if ((context != null) && (contextUtils == null)) {
      contextUtils = new ContextUtils(context.getContextPath());
    }
  }
  
  
  





  public String getEntryContext()
  {
    return entryContext;
  }
  






  public boolean isEntryContext(ServletContext context)
  {
    if (context != null) {
      return context.getContextPath().equals(entryContext);
    }
    return false;
  }
  






  public boolean isURIStartWithEntryContext(String uri)
  {
    if (uri == null) return false;
    return uri.startsWith(entryContext + "/");
  }
  
  public boolean isEntryContextRoot(String uri) {
    if (uri == null) return false;
    return uri.equals(entryContext + "/");
  }
  






  public boolean isDefaultEntryURIRequest(javax.servlet.http.HttpServletRequest request)
  {
    return request.getRequestURI().equals(entryContext + "/" + ACTION_DISPATCHER);
  }
}