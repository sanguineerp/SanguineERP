<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
  String appletWidth = "750";
  String appletHeight = "600";
%>

<html>
<head>
<title>
JasperReports - Web Application Sample
</title>
<link rel="stylesheet" type="text/css" href="stylesheet.css" title="Style">
</head>
<body BGCOLOR="#ffffff" LINK="#000099">

<span class="title">Embedded Viewer Applet</span>
<c:out value='${PAGE_URL}'/>
<br>
<br>

<!--"CONVERTED_APPLET"-->
<!-- HTML CONVERTER -->
<SCRIPT LANGUAGE="JavaScript"><!--
    var _info = navigator.userAgent; 
    var _ns = false; 
    var _ns6 = false;
    var _ie = (_info.indexOf("MSIE") > 0 && _info.indexOf("Win") > 0 && _info.indexOf("Windows 3.1") < 0);
//--></SCRIPT>
    <COMMENT>
        <SCRIPT LANGUAGE="JavaScript1.1"><!--
        var _ns = (navigator.appName.indexOf("Netscape") >= 0 && ((_info.indexOf("Win") > 0 && _info.indexOf("Win16") < 0 && java.lang.System.getProperty("os.version").indexOf("3.5") < 0) || (_info.indexOf("Sun") > 0) || (_info.indexOf("Linux") > 0) || (_info.indexOf("AIX") > 0) || (_info.indexOf("OS/2") > 0)));
        var _ns6 = ((_ns == true) && (_info.indexOf("Mozilla/5") >= 0));
//--></SCRIPT>
    </COMMENT>

<SCRIPT LANGUAGE="JavaScript"><!--
    if (_ie == true) document.writeln('<OBJECT classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" WIDTH = "600" HEIGHT = "400"  codebase="http://java.sun.com/products/plugin/1.1.2/jinstall-112-win32.cab#Version=1,1,2,0"><NOEMBED><XMP>');
    else if (_ns == true && _ns6 == false) document.writeln('<EMBED type="application/x-java-applet;version=1.1.2" CODE = "EmbeddedApplet.class" CODEBASE = "applets" ARCHIVE = "jasperreports-applet-5.6.0.jar,jasperreports-applet-5.6.0.jar,commons-logging-1.2.jar,commons-collections-3.2.1.jar" WIDTH = "600" HEIGHT = "400" REPORT_URL = "${PAGE_URL}" scriptable=false pluginspage="http://java.sun.com/products/plugin/1.1.2/plugin-install.html"><NOEMBED><XMP>');
//--></SCRIPT>
<APPLET  CODE = "EmbeddedApplet.class" CODEBASE = "applets" ARCHIVE = "jasperreports-applet-5.6.0.jar,jasperreports-applet-5.6.0.jar,commons-logging-1.2.jar,commons-collections-3.2.1.jar" WIDTH = "600" HEIGHT = "400"></XMP>
    <PARAM NAME = CODE VALUE = "EmbeddedApplet.class" >
<PARAM NAME = CODEBASE VALUE = "applets" >
<PARAM NAME = ARCHIVE VALUE = "jasperreports-applet-5.6.0.jar,jasperreports-applet-5.6.0.jar,commons-logging-1.2.jar,commons-collections-3.2.1.jar" >

    <PARAM NAME="type" VALUE="application/x-java-applet;version=1.2.2">
    <PARAM NAME="scriptable" VALUE="false">
    <PARAM NAME = "REPORT_URL" VALUE ="http://localhost:8080/prjWebStocks/reports/showViewer.html">
    
    <!--<PARAM NAME = "REPORT_URL" VALUE ="${reportname}"> -->

</APPLET>
</NOEMBED>
</EMBED>
</OBJECT>

<!--
<APPLET CODE = "EmbeddedApplet.class" CODEBASE = "applets" ARCHIVE = "jasperreports-applet-5.6.0.jar,jasperreports-applet-5.6.0.jar,commons-logging-1.2.jar,commons-collections-3.2.1.jar" WIDTH = "600" HEIGHT = "400">
<PARAM NAME = "REPORT_URL" VALUE ="<c:out value="${PAGE_URL}"/>">


</APPLET>
-->
<!--"END_CONVERTED_APPLET"-->
</body>
</html>