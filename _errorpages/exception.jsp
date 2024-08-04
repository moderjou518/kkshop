<%@ page contentType="text/html; charset=UTF-8" isErrorPage="true" %>
<%@page language="java" import="java.util.HashMap,com.hms.util.HMS_Util,com.hms.util.UTIL_String" %>
<%@page language="java" import="com.hms.web.*, com.hms.util.*, com.hms.entity.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%	
	String ip1 = UTIL_String.getIpAddr(request);
	String ip2 = UTIL_String.getRemortIP(request);
	String msgCode = UTIL_String.nvl(request.getParameter("msgCode"), "x");
	 
	// remoteAddress
	String rip = UTIL_String.getIpAddr(request) + "," + UTIL_String.getRemortIP(request);	
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<title>系統訊息</title> 
<SCRIPT language=JavaScript>
<!-- begin
	document.onmousedown=click;
	document.onkeydown=click;
	if (document.layers) window.captureEvents(Event.MOUSEDOWN); window.onmousedown=click;
	if (document.layers) window.captureEvents(Event.KEYDOWN); window.onkeydown=click;
	function click(e){
		if (navigator.appName == 'Netscape'){
			if (e.which != 1){
				//alert("是不是連鍵盤都無法使用啦？");
				return false;
			}
		}
		if (navigator.appName == "Microsoft Internet Explorer"){
			if (event.button != 1){
				//alert("是不是連鍵盤都無法使用啦？");
				return false;
			}
		}
	}
// end -->
</SCRIPT>
<style type="text/css"> 
<!-- 
body{margin:0;font-size:.9em;font-family:Verdana,Arial,Helvetica,sans-serif;background:#CBE1EF;} 
code{margin:0;color:#006600;font-size:1.1em;font-weight:bold;} 
.config_source code{font-size:.8em;color:#000000;} 
pre{margin:0;font-size:1.4em;word-wrap:break-word;} 
ul,ol{margin:10px 0 10px 40px;} 
ul.first,ol.first{margin-top:5px;} 
fieldset{padding:0 15px 10px 15px;} 
.summary-container fieldset{padding-bottom:5px;margin-top:4px;} 
legend.no-expand-all{padding:2px 15px 4px 10px;margin:0 0 0 -12px;} 
legend{color:#333333;padding:4px 15px 4px 10px;margin:4px 0 8px -12px;_margin-top:0px; 
 border-top:1px solid #EDEDED;border-left:1px solid #EDEDED;border-right:1px solid #969696; 
 border-bottom:1px solid #969696;background:#E7ECF0;font-weight:bold;font-size:1em;} 
a:link,a:visited{color:#007EFF;font-weight:bold;} 
a:hover{text-decoration:none;} 
h1{font-size:2.4em;margin:0;color:#FFF;} 
h2{font-size:1.7em;margin:0;color:#CC0000;} 
h3{font-size:1.4em;margin:10px 0 0 0;color:#CC0000;} 
h4{font-size:1.2em;margin:10px 0 5px 0;}
#header{width:96%;margin:0 0 0 0;padding:6px 2% 6px 2%;font-family:"trebuchet MS",Verdana,sans-serif; 
 color:#FFF;background-color:#5C87B2; }
 #content{margin:0 0 0 2%;position:relative;} 
.summary-container,.content-container{background:#FFF;width:96%;margin-top:8px;padding:10px;position:relative;} 
.config_source{background:#fff5c4;} 
.content-container p{margin:0 0 10px 0; }
#details-left{width:35%;float:left;margin-right:2%; }
#details-right{width:63%;float:left;overflow:hidden; }
#server_version{width:96%;_height:1px;min-height:1px;margin:0 0 5px 0;padding:11px 2% 8px 2%;color:#FFFFFF; 
 background-color:#5A7FA5;border-bottom:1px solid #C1CFDD;border-top:1px solid #4A6C8E;font-weight:normal; 
 font-size:1em;color:#FFF;text-align:right; }
 #server_version p{margin:5px 0;} 
table{margin:4px 0 4px 0;width:100%;border:none;} 
td,th{vertical-align:top;padding:3px 0;text-align:left;font-weight:bold;border:none;} 
th{width:30%;text-align:right;padding-right:2%;font-weight:normal;} 
thead th{background-color:#ebebeb;width:25%; }
#details-right th{width:20%;} 
table tr.alt td,table tr.alt th{background-color:#ebebeb;} 
.highlight-code{color:#CC0000;font-weight:bold;font-style:italic;} 
.clear{clear:both;} 
.preferred{padding:0 5px 2px 5px;font-weight:normal;background:#006633;color:#FFF;font-size:.8em;} 
--> 
</style> 
 
</head> 
<body onselectstart="return false;" ondragstart="return false;" oncontextmenu="return false;">
<div id="header"><h1>系統資訊</h1></div> 
<div id="server_version"><p>牙語工作室</p></div> 
<div id="content"> 
	<div class="content-container"> 
	 <fieldset><legend>系統資訊</legend>   
	  <h4 style="color: white"><%=ip1%></h4>
	  <h3>請稍候再試</h3>   
	  <h4 style="color: white"><%=ip2%></h4>
	 </fieldset> 
	</div> 

	<div class="content-container"> 
	 <fieldset><legend>最有可能的原因:</legend> 
	  <ul> 	<li>系統忙錄中</li> </ul> 
	 </fieldset> 
	</div>
	<pre>
<%
response.getWriter().println("Exception: " + exception);
 
if(exception != null)
{
  response.getWriter().println("<pre>");
  exception.printStackTrace(response.getWriter());
  response.getWriter().println("</pre>");
}
 
response.getWriter().println("<hr/>");
%>
</pre>

</div> 
</body> 
</html> 
