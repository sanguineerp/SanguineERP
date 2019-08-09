<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>

<script>


	


//set date
		$(document).ready(function(){
			
			var pmsDate='<%=session.getAttribute("PMSDate").toString()%>';
			
			$("#dteFromDate").datepicker({
				dateFormat : 'dd-mm-yy'
			});
			$("#dteFromDate").datepicker('setDate', pmsDate);	
			
			
			$("#dteToDate").datepicker({
				dateFormat : 'dd-mm-yy'
			});
			$("#dteToDate").datepicker('setDate', pmsDate);	
		});
		
		
		
		
</script>
<body onload="funOnLoad();">
	<div id="formHeading">
		<label>Check-In Check-Out List </label>
	</div>
	<s:form name="frmCheckInCheckOutList" method="GET" action="CheckInCheckOutList.html" >
		
	<table class="masterTable">
		<br/><br/>
			<tr>
				<td><label>From Date</label></td>
				<td><s:input type="text" id="dteFromDate" path="strArrivedDate" required="true" class="calenderTextBox" /></td>
				<td><label>To Date</label></td>
				<td><s:input type="text" id="dteToDate" path="dteDepartureDate" required="true" class="calenderTextBox" /></td>				
			</tr>
		</table>
		<br>
	<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button" />
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>	
	</s:form>

</body>
</html>