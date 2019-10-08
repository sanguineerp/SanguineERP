
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    
   	<%-- Started Default Script For Page  --%>
    
		<script type="text/javascript" src="<spring:url value="/resources/js/jQuery.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery-ui.min.js"/>"></script>	
		<script type="text/javascript" src="<spring:url value="/resources/js/validations.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/TreeMenu.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/main.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery.fancytree.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery.numeric.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery.ui-jalert.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/pagination.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery-ui.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery.excelexport.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/hindiTyping.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/checkNetworkConnection.js"/>"></script>
	
	<%-- End Default Script For Page  --%>
	
	<%-- Started Default CSS For Page  --%>

	    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/favicon.ico" type="image/x-icon" sizes="16x16">
	 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/design.css"/>" />
	    <link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/tree.css"/>" /> 
	 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/jquery-ui.css"/>" />
	 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/main.css"/>" />
	 	<link rel="stylesheet"  href="<spring:url value="/resources/css/pagination.css"/>" />
	 	
 	
 	<%-- End Default CSS For Page  --%>
 	
 	<%--  Started Script and CSS For Select Time in textBox  --%>
	
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery.timepicker.min.js"/>"></script>
	  	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/jquery.timepicker.css"/>" />
	
	<%-- End Script and CSS For Select Time in textBox  --%>
	
 	  
  	<title>Web Stocks</title>
	
	<script type="text/javascript">
	var maxQuantityDecimalPlaceLimit=parseInt('<%=session.getAttribute("qtyDecPlace").toString()%>');
	var maxAmountDecimalPlaceLimit=parseInt('<%=session.getAttribute("amtDecPlace").toString()%>');
   	function getContextPath() 
   	{
	  	return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
	}
   
   	
   	var debugFlag = false;
   	function debug(value)
   	{
   		if(debugFlag)
   		{
   			alert(value);
   		}   		
   	} 
   	
    $(document).ready(function(){
    
    	var strIndustryType='<%=session.getAttribute("strIndustryType").toString()%>';
   		//alert("Upper");
   		switch(strIndustryType) 
   		{
   		
   		case 'Manufacture' :
   						document.getElementById("pageTop").className = "pagetopMenufactureing";
   			break;
   			
   		case 'Hospitality' :	
   						document.getElementById("pageTop").className = "pagetop";
   			break;

   		case 'Retailing' :
   						document.getElementById("pageTop").className = "pagetopMenufactureing";
   				break;
   			
   		case 'MilkFederation' :
   						document.getElementById("pageTop").className = "pagetopMenufactureing";
				break;//headerimageMilkFederation
   			
   		default :
   			document.getElementById("pageTop").className = "pagetop";
   			break;
   		}
   		
   		
   			var strModule='<%=session.getAttribute("selectedModuleName").toString()%>';
   			if(strModule!=null){
   			
   				switch(strModule){
   	   			case '1-WebStocks' :
   	   				document.getElementById("pageTop").className = "pagetopMenufactureing";
   						//	$("#pageTop").css("background-image", 'url(.../resources/images/headerimagewebPMS.jpg)');
   						break;
   	   			case '2-WebExcise' :
   	   				document.getElementById("pageTop").className = "pagetopMenufactureing";
   				break;
   	   			case '3-WebPMS' :
   	   				document.getElementById("pageTop").className = "pagetopMenuWebPMS";
   				break;
   	   			case '6-WebCRM' :
   	   				document.getElementById("pageTop").className = "pagetopMenuWebCRM";
   				break;
   	   			case '4-WebClub' :
   	   				document.getElementById("pageTop").className = "pagetopMenuWebClub";
   				break;
   	   			case '5-WebBook' :
   	   				document.getElementById("pageTop").className = "pagetopMenuWebBook";
   				break;
   				
   	   			case '5-WebBookAR' :
	   				document.getElementById("pageTop").className = "pagetopMenuWebBook";
				break;
   				
   	   			case '7-WebBanquet' :
   	   				document.getElementById("pageTop").className = "pagetopMenuWebBanquet";
   				break;
   						
   				default :
   					document.getElementById("pageTop").className = "pagetopMenufactureing";
   	   			}

   			}
       
   });
   	
   	
	</script>
<script  type="text/JavaScript">
document.onkeypress = stopRKey;
function stopRKey(evt) {
              var evt = (evt) ? evt : ((event) ? event : null);
              var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
              if (evt.keyCode == 13)  {
                           //disable form submission
                           return false;
              }
}
</script>
	
  	</head>
		<body>
		 <div id="pageTop"   >		
		
		</div>
		</body>
</html>