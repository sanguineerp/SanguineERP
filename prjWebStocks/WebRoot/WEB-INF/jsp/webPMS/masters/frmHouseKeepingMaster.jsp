<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<script type="text/javascript">
	var fieldName;

	$(function() 
	{
		
		var message='';
		<%if (session.getAttribute("success") != null) {
			            if(session.getAttribute("successMessage") != null){%>
			            message='<%=session.getAttribute("successMessage").toString()%>';
			            <%
			            session.removeAttribute("successMessage");
			            }
						boolean test = ((Boolean) session.getAttribute("success")).booleanValue();
						session.removeAttribute("success");
						if (test) {
						%>	
			alert("Data Save successfully\n\n"+message);
		<%
		}}%>
		
	});

	function funSetData(code){

		switch(fieldName){
		
		case 'houseKeepCode' : 
			funSetHouseKeepData(code);
			break;
			

		}
	}
	
	
	function funSetHouseKeepData(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadHouseKeepData.html?houseKeepCode=" + code,
			dataType : "json",
			success: function(response)
	        {
				
	        	if(response.strBookingTypeCode=='Invalid Code')
	        	{
	        		alert("Invalid Agent Code");
	        		$("#txtBookingTypeCode").val('');
	        	}
	        	else
	        	{					        	    	        		
	        		$("#txtHouseKeepCode").val(response.strHouseKeepCode);
	        	    $("#txtHouseKeepName").val(response.strHouseKeepName);
	        	    $("#txtRemarks").val(response.strRemarks);
	        
	        	}
			},
			error: function(jqXHR, exception) 
			{
	            if (jqXHR.status === 0) {
	                alert('Not connect.n Verify Network.');
	            } else if (jqXHR.status == 404) {
	                alert('Requested page not found. [404]');
	            } else if (jqXHR.status == 500) {
	                alert('Internal Server Error [500].');
	            } else if (exception === 'parsererror') {
	                alert('Requested JSON parse failed.');
	            } else if (exception === 'timeout') {
	                alert('Time out error.');
	            } else if (exception === 'abort') {
	                alert('Ajax request aborted.');
	            } else {
	                alert('Uncaught Error.n' + jqXHR.responseText);
	            }		            
	        }			
		});
	}










	function funHelp(transactionName)
	{
		fieldName=transactionName;
		window.open("searchform.html?formname="+transactionName+"&searchText=","mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
	}
</script>

</head>
<body>

	<div id="formHeading">
	<label>Housekeeping Master</label>
	</div>

<br/>
<br/>

	<s:form name="HouseKeepingMaster" method="POST" action="saveHouseKeepingMaster.html">

		<table class="masterTable">
			<tr>
				<td>
					<label>Housekeeping Code</label>
				</td>
				<td>
					<s:input colspan="3" type="text" id="txtHouseKeepCode" path="strHouseKeepCode" cssClass="searchTextBox" ondblclick="funHelp('houseKeepCode')" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Housekeeping Name</label>
				</td>
				<td>
					<s:input colspan="3" type="text" id="txtHouseKeepName" path="strHouseKeepName" cssClass="longTextBox" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Remarks</label>
				</td>
				<td>
					<s:input colspan="3" type="text" id="txtRemarks" path="strRemarks" cssClass="longTextBox" />
				</td>
			</tr>
		</table>

		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button" />
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>

	</s:form>
</body>
</html>
