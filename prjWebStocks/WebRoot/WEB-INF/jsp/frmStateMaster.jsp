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
	
	var clickCount =0.0;
	function funCallFormAction(actionName,object) 
		{
				
			if ($("#txtStateName").val()=="") 
			    {
				 alert('Enter State Name');
				 $("#txtStateName").focus();
				 return false;  
			   
			}
		if(clickCount==0){
		clickCount=clickCount+1;
		}
			else
			{
				return false;
			}
			return true; 
		}

	/**
	* Success Message After Saving Record
	**/
	 $(document).ready(function()
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
	
	

	$(function() 
	{
	});

	function funSetData(code){

		switch(fieldName){

			case 'StateCode' : 
				funSetStateCode(code);
				break;
			case 'CountryCode' : 
				funSetCountryCode(code);
				break;
		}
	}


	function funSetStateCode(code){

		$.ajax({
			type : "POST",
			url : getContextPath()+ "/loadWSStateCode.html?docCode=" + code,
			dataType : "json",
			success : function(response)
			{
	        	if(response.strStateCode=='Invalid Code')
	        	{
	        		alert("Invalid State Code");
	        		$("#txtStateCode").val('');
	        	}
	        	else
	        	{
		        	$("#txtStateCode").val(response.strStateCode);
		        	$("#txtStateName").val(response.strStateName);
		        	$("#txtStateDesc").val(response.strStateDesc);
		        	funSetCountryCode(response.strCountryCode);
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



	function funSetCountryCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadWSCountryCode.html?docCode=" + code,
			dataType : "json",
			success : function(response)
			{
	        	if(response.strCountryCode=='Invalid Code')
	        	{
	        		alert("Invalid Country Code");
	        		$("#txtCountryCode").val('');
	        	}
	        	else
	        	{
		        	$("#txtCountryCode").val(response.strCountryCode);
		        	$("#lblCountryName").text(response.strCountryName);
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







	function funHelp(transactionName)
	{
		fieldName=transactionName;
	//	window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
		window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
	}
</script>

</head>
<body>

	<div id="formHeading">
	<label>State Master</label>
	</div>

<br/>
<br/>

	<s:form name="frmWSStateMaster" method="POST" action="saveWSStateMaster.html">

		<table class="masterTable">
			<tr>
				<td>
					<label>State Code</label>
				</td>
				<td>
					<s:input colspan="3" type="text" id="txtStateCode" path="strStateCode" cssClass="searchTextBox" ondblclick="funHelp('StateCode');"/>
				</td>
			</tr>
			<tr>
				<td>
					<label>State Name</label>
				</td>
				<td>
					<s:input colspan="3" type="text" id="txtStateName" path="strStateName" cssClass="longTextBox" />
				</td>
			</tr>
			<tr>
				<td>
					<label>State Desc</label>
				</td>
				<td>
					<s:input colspan="3" type="text" id="txtStateDesc" path="strStateDesc" cssClass="longTextBox" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Country Code</label>
				</td>
				<td>
					<s:input  type="text" id="txtCountryCode" path="strCountryCode" cssClass="searchTextBox" ondblclick="funHelp('CountryCode');"/>
				</td>
				<td colspan="1"><label id="lblCountryName"></label></td>
			</tr>
		</table>

		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button" onclick="return funCallFormAction('submit',this);"/>
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>

	</s:form>
</body>
</html>
