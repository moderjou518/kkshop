package com.evergreen_hotels.bmg.whms1.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;

//import org.bouncycastle.asn1.ocsp.Request;


public class URLUtils{	
	
  public static long getJsNoCacheParameter(ServletContext context){
	  
	  //return ((Long)context.getAttribute("WarLastModifiedDateTime")).longValue();	  
	  Random ran = new Random();
	  return ran.nextInt(999)+1;
  }
  

  public static String getEntryContext() {
	  // return ContextUtils.getInstance().getEntryContext();
	  return "123";
  }

}