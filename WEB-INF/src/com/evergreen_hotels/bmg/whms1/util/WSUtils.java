package com.evergreen_hotels.bmg.whms1.util;

//package com.evergreen.common.http;

import com.evergreen.web.security.AntiXss;
import javax.servlet.http.HttpServletRequest;

public class WSUtils
{
	public static <T> T getRequestAttribute(HttpServletRequest request, String attr_name, T null_replace)
	{
		return (T) (request.getAttribute(attr_name) != null ? request.getAttribute(attr_name) : null_replace);
	}

	public static String getRequestParameter(HttpServletRequest request, String parm_name, String null_replace)
	{
		return request.getParameter(parm_name) != null ?
				AntiXss.getSafeHtmlFragment(request.getParameter(parm_name)) : null_replace;
	}
}
