<%@page contentType="text/html;charset=utf-8"%>
<%@page language="java" import="com.evergreen_hotels.bmg.whms1.util.*"%>
<%
    String step = WSUtils.getRequestParameter(request, "step", null);
    String fa = WSUtils.getRequestParameter(request, "fa", null);
    String basic = WSUtils.getRequestParameter(request, "basic", "3.0.0");
    String color = WSUtils.getRequestParameter(request, "color", "Y");
    String fit = WSUtils.getRequestParameter(request, "fit", "Y");    
%>
<%if (basic.equals("1.0.0")) { %>    
    <link rel="stylesheet" href="/_ui/css/1.0.0/Div.css?v=<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
    <link rel="stylesheet" href="/_ui/css/1.0.0/Basic.css?v=<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
    <link rel="stylesheet" href="/_ui/css/1.0.0/Table.css?v=<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
    <link rel="stylesheet" href="/_ui/css/1.0.0/Accordion.css?v=<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
<%} else if (basic.startsWith("2")) { %>    
    <link rel="stylesheet" href="/_ui/css/<%=basic%>/Basic.css?v=<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
<%} else if (basic.equals("3.0.0")) { %>    
    <link rel="stylesheet" href="/_ui/css/3.0.0/Basic.css?v=<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
    <%if (color.toUpperCase().equals("Y")) { %>   
        <link rel="stylesheet" href="/_ui/css/3.0.0/Color.css?v=<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
    <%} %>
    <%if (fit.toUpperCase().equals("Y")) { %>   
        <link rel="stylesheet" href="/_ui/css/3.0.0/Fit.css?v=<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
    <%} else{ %>   
        <link rel="stylesheet" href="/_ui/css/3.0.0/Original.css?v=<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
    <%} %>
<%} %>

<%if (step!=null) { %>
    <link rel="stylesheet" href="/_ui/css/1.0.0/WizardSteps.css?v=<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
<%} %>

<%if (fa!=null) { %>
    <%if (fa.startsWith("4")) { %>
        <link rel="stylesheet" href="/_util_lib/fontawesome/<%=fa%>/css/font-awesome.css" type="text/css" />
        <link rel="stylesheet" href="/_ui/css/2.0.0/icon_f4.css?v=<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
    <%}else if (fa.startsWith("5")) { %>
        <%if (fa.equals("5.0.2")) { %>   
        <link rel="stylesheet" href="/_util_lib/fontawesome/<%=fa%>/css/fontawesome-all.min.css" type="text/css" />
        <%}else{ %>
        <link rel="stylesheet" href="/_util_lib/fontawesome/<%=fa%>/css/all.min.css" type="text/css" />
        <%} %>
        
        <%if (basic.startsWith("2")) { %>   
        <link rel="stylesheet" href="/_ui/css/<%=basic%>/icon_f5.css?v=<%=URLUtils.getJsNoCacheParameter(application)%>" type="text/css" />
        <%} %>
    <%} %>
    
<%} %>

<!-- <link rel="icon" href="/_ui/images/favicon.ico" type="image/x-icon"> -->
