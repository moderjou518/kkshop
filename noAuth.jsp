<%@page contentType="text/html;charset=utf-8"%>
<%@page language="java" import="java.util.HashMap,com.hms.util.HMS_Util,com.hms.util.UTIL_String" %>
<%@page language="java" import="com.hms.web.*, com.hms.util.*, com.hms.entity.*" %>
<%	
	String ip1 = UTIL_String.getIpAddr(request);
	String ip2 = UTIL_String.getRemortIP(request);
	String msgCode = UTIL_String.nvl(request.getParameter("msgCode"), "x");
	 
	// remoteAddress
	String rip = UTIL_String.getIpAddr(request) + "," + UTIL_String.getRemortIP(request);	
	
	// loginID
	//String loginID = UTIL_String.nvl(request.getParameter("l"), "#");
	//String loginID = new String(UTIL_String.nvl(request.getParameter("l"), "#").getBytes("ISO-8859-1"), "utf-8");
	
	//String loginMark = (String) request.getSession().getAttribute("LoginMark");
	
	//System.out.println("noAuth.jsp LoginMark: " + loginMark);
	
	// webAction ID
	String waID = UTIL_String.nvl(request.getParameter("w"), "#");	
	HMS_TROW faWeb 	= UTIL_GlobalConfig.getWebAction(waID);
	String waName = "";
	if (null != faWeb){
		waName = faWeb.GetField("MISC_DESC");
	}
	
	HashMap titleMap	= new HashMap();
	HashMap infoMap		= new HashMap();
	HashMap reasonMap 	= new HashMap();
	
	// 00000
	titleMap.put("00000", 	"非信任裝置");
	infoMap.put("00000", 	"瀏覽端裝置未被授權使用。");
	reasonMap.put("00000", 	"請確定此裝置已被授權");
	
	// 00001
	titleMap.put("00001", 	"主機未被認證");
	infoMap.put("00001", 	"電腦主機未通過認證。");
	reasonMap.put("00001", 	"電腦主機未取得合法認證。");
	
	// 00002
	titleMap.put("00002", 	"權限不足");
	infoMap.put("00002", 	"用戶端不在授權名單。");
	reasonMap.put("00002", 	"此用戶端電腦未被授權使用系統(" + rip + ")。");
	
	// 00003
	titleMap.put("00003", 	"權限不足");
	infoMap.put("00003", 	"登入帳號未被授權，<a href=\"#\" onclick=\"window.location.href='shop.go?webactionID=HBD1LOGIN'\">登入系統</a>");
	reasonMap.put("00003", 	"登入帳號未被授權使用此功能<br/><br/>帳號: " +  "<br/>程式: " + waName + " " + waID);
	
	// 00005
	titleMap.put("00005", 	"無效程式");
	infoMap.put("00005", 	"程式不存在");
	reasonMap.put("00005", 	"系統尚未安裝此程式，請聯絡廠商進行安裝設定<br/><br/>程式: " + waID + "");
	
	// 00009
	titleMap.put("00009", 	"請進行整檔");
	infoMap.put("00009", 	"資料庫空間碎片化");
	reasonMap.put("00009", 	"請聯絡廠商進行整檔作業");	
	
	// 00010
	titleMap.put("00010", 	"參數資料不完整");
	infoMap.put("00010", 	"參數資料不完整");
	reasonMap.put("00010", 	"參數資料不完整(c)");
	
	
	// 
	titleMap.put("10001", 	"請先登入系統");
	infoMap.put("10001", 	"請先<a href=\"#\" onclick=\"window.location.href='shop.go?webactionID=HBD1LOGIN'\">登入系統</a><br/>");
	reasonMap.put("10001", 	"請先登入系統");
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<title><%=titleMap.get(msgCode)%> - MsgCode</title> 
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
<div id="header"><h1><%=titleMap.get(msgCode)%></h1></div> 
<div id="server_version"><p>牙語工作室</p></div> 
<div id="content"> 
	<div class="content-container"> 
	 <fieldset><legend>系統資訊</legend>   
	  <h4 style="color: white"><%=ip1%></h4>
	  <h3><%=infoMap.get(msgCode)%></h3>   
	  <h4 style="color: white"><%=ip2%></h4>
	 </fieldset> 
	</div> 

	<div class="content-container"> 
	 <fieldset><legend>最有可能的原因:</legend> 
	  <ul> 	<li><%=reasonMap.get(msgCode)%></li> </ul> 
	 </fieldset> 
	</div>

</div> 
</body> 
</html> 
