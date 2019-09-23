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
	});

	function funSetData(code){

		switch(fieldName){

		case 'webClubBusinessSrcCode' : 
			funSetBusinessSrcCode(code);
			break;
		
		}
	}



	function funSetBusinessSrcCode(code)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadWebClubBusinessSourceData.html?docCode="+code,
			dataType : "json",
			success : function(response){ 

				if(response.strSCCode=='Invalid Code')
	        	{
	        		alert("Invalid Business Source Code");
	        		$("#txtBusinessSrcCode").val('');
	        	}
	        	else
	        	{      
		        	$("#txtBusinessSrcCode").val(code);
		        	$("#txtBusinessSourceName").val(response[0][1]);
		        	$("#txtBusinessSourcePercent").val(response[0][2]);
		      
		        	
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
		window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:1000px;dialogLeft:200px;")
	}
</script>

</head>
<body>

	<div id="formHeading">
	<label>WebClubBusinessSourceMaster</label>
	</div>

<br/>
<br/>

	<s:form name="WebClubBusinessSourceMaster" method="POST" action="saveWebClubBusinessSourceMaster.html">

		<table class="masterTable">
			<tr>
			
				<td><label>Business SourceCode</label></td>
				<td><s:input id="txtBusinessSrcCode" path="strBusinessSrcCode" cssClass="searchTextBox" ondblclick="funHelp('webClubBusinessSrcCode')" /></td>				
				
			</tr>
			<tr>
			    <td><label>Business Source Name</label></td>
				<td><s:input id="txtBusinessSourceName" path="strBusinessSrcName" cssClass="longTextBox" /></td>				
			</tr>
			
			<tr>
			    <td><label>Business Source Percent</label></td>
				<td><s:input id="txtBusinessSourcePercent" path="dblPercent" cssClass="longTextBox" /></td>				
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
