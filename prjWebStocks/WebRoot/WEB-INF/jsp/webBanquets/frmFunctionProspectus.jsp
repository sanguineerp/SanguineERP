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

	function funSetData(code){

		switch(fieldName){

		case 'BillForBanquet' : 
			funSetBillForBanquetCode(code);
			break;
		}
	}
	
	function funSetBillForBanquetCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadBookingCode.html?docCode=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strMenuHeadCode=='Invalid Code')
	        	{
	        		alert("Invalid Cost Center Code");
	        		$("#txtMenuHeadCode").val('');
	        	}
	        	else
	        	{
	        		$("#txtMenuHeadName").val(response.strMenuHeadName);
	        		$("#txtMenuHeadCode").val(response.strMenuHeadCode);
	        	}
			},
			error: function(jqXHR, exception) {
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
	
	function funValidateFields()
	{
		var flag=false;
		if($("#txtBookingNo").val().trim().length==0)
		{
			alert("Please Select Booking No.");
		}
		else
		{
			flag=true;
		}
		return flag;
	}

	function funHelp(transactionName)
	{
		fieldName=transactionName;
		window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
	}
</script>

</head>
<body>

	<div id="formHeading">
	<label>Function Prospectus</label>
	</div>

<br/>
<br/>

	<s:form name="FunctionProspectus" method="POST" action="rptFunctionProspectus.html">

		<table class="masterTable">
			<tr>
				<td style="width:15%;">
					<label>Booking No</label>
				</td>
				<td>
					<s:input type="text" path="strBookingNo" id="txtBookingNo" ondblclick="funHelp('BillForBanquet')" cssClass="searchTextBox jQKeyboard form-control" />
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
