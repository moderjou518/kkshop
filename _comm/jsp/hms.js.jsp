<%@page language="java" import="com.evergreen_hotels.bmg.whms1.util.*"%>
<%
	String lightpick = WSUtils.getRequestParameter(request, "lightpick", "Y");
    String slideMenu = WSUtils.getRequestParameter(request, "slideMenu", "Y");
%>
<%if("Y".equals(lightpick)){%>
  <link rel="stylesheet" href="/_comm/js/lightpicker/lightpick.css?<%=URLUtils.getJsNoCacheParameter(application)%>"  type="text/css">
  <link rel="stylesheet" href="/_comm/js/lightpicker/lightpick_hmsClr.css?<%=URLUtils.getJsNoCacheParameter(application)%>"  type="text/css">  
  <script type="text/javascript" src="/_comm/js/lightpicker/moment.min.js?<%=URLUtils.getJsNoCacheParameter(application)%>"></script>
  <script type="text/javascript" src="/_comm/js/lightpicker/hms_lightpick.js?<%=URLUtils.getJsNoCacheParameter(application)%>"></script>  
<%}%>
<%if("Y".equals(slideMenu)){%>
  <link rel="stylesheet" href="/_comm/js/slideMenu/HMS2_SlideMenuSylte.css?<%=URLUtils.getJsNoCacheParameter(application)%>"  type="text/css">  
  <script type="text/javascript" src="/_comm/js/slideMenu/HMS2_SlideMenu.js?<%=URLUtils.getJsNoCacheParameter(application)%>"></script>  
<%}%> 
<script type="text/javascript" src="/_comm/js/HMS2_Util.js?<%=URLUtils.getJsNoCacheParameter(application)%>"></script>
<script>
$(function(){
  //hambuger menu animation
    $('.hamburger-icon').click(function() {
        $(this).toggleClass('showAct');        
    });    
})

</script>