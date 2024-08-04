<%@page language="java" import="com.evergreen_hotels.bmg.whms1.util.*"%>
<%
	String clrType = WSUtils.getRequestParameter(request, "clr", "y");
    String useHmsStyle = WSUtils.getRequestParameter(request, "hmsStyle", "y");    
    String agent = request.getHeader("User-Agent").toLowerCase();
    String ver = WSUtils.getRequestParameter(request, "ver", "1.0.0");
    String path = "";
    if("2.0.0".equals(ver)){
        path = "2.0.0/";
    }
    /*String browser = "";
    boolean isOldIE = false;
    if(agent.indexOf("msie 7")>0){
        browser = "ie7";
        isOldIE = true;
    }
    else if(agent.indexOf("msie 8")>0){
        browser =  "ie8";
        isOldIE = true;
    }
    else if(agent.indexOf("msie 9")>0){
        browser =  "ie9";
        isOldIE = true;
    }
    else if(agent.indexOf("msie 10")>0){
        browser =  "ie10";
    }
    else if(agent.indexOf("msie")>0){
        browser =  "ie";
    }
    else if(agent.indexOf("opera")>0){
        browser =  "opera";
    }
    else if(agent.indexOf("opera")>0){
        browser =  "opera";
    }
    else if(agent.indexOf("firefox")>0){
        browser =  "firefox";
    }
    else if(agent.indexOf("webkit")>0){
        browser =  "webkit";
    }
    else if(agent.indexOf("gecko")>0 && agent.indexOf("rv:11")>0){
        browser =  "ie11";
    }
    else{
        browser =  "Others";
    }    */
%>
<!-- <link rel="icon" href="/_ui/images/favicon.ico" type="image/x-icon"> -->

<%if("y".equals(useHmsStyle)){%>
    <%if("2.0.0".equals(ver)){ %>
    <link rel="stylesheet" href="/_comm/css/<%=path%>hmsStyle2.css?<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
    <%}
    else{%>
    <link rel="stylesheet" href="/_comm/css/hmsStyle.css?<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
    <%}%>
<%}%>
<link rel="stylesheet" href="/_comm/css/<%=path%>hmsDataGrid.css?<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
<link rel="stylesheet" href="/_comm/css/<%=path%>hmsDataGrid_thin.css?<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
<%if("y".equals(clrType)){%>
    <%if("2.0.0".equals(ver)){ %>
    <link rel="stylesheet" href="/_comm/css/<%=path%>hmsColorSet2.css?<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
    <%}
    else{%>
    <link rel="stylesheet" href="/_comm/css/hmsColorSet.css?<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
    <%}%>
<%}%>
<%if("2.0.0".equals(ver)){ %>
    <link rel="stylesheet" href="/_comm/css/<%=path%>hmsStyle_FIT.css?<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
<%} %>


       
    
    
    