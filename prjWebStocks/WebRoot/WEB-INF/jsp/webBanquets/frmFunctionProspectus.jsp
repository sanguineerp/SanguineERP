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
			funSetFunctionProspectusCode(code);
			break;
		}
	}
	
	function funSetFunctionProspectusCode(code)
	{
		$("#txtBookingNo").val(code);	
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
			<input type="submit" value="Submit" tabindex="3" class="form_button" onclick="return funValidateFields(this)" />
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>

	</s:form>
</body>
</html>
