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

	

	function funResetFields()
	{
		$("#txtFunctionName").focus();
    }
	
	$(function() 
	{
		
		
	          /**
				* On Blur Event on TextField
				**/
				$('#txtFunctionCode').blur(function() 
				{
						var code = $('#txtFunctionCode').val();
						if (code.trim().length > 0 && code !="?" && code !="/")
						{				
							funSetData(code);							
						}
				});
				
				$('#txtFunctionName').blur(function () {
					 var strFunctionName=$('#txtFunctionName').val();
				      var st = strFunctionName.replace(/\s{2,}/g, ' ');
				      $('#txtFunctionName').val(st);
					});
				
	});
 function funSetData(code){

		switch(fieldName){

			case 'FunctionName' : 
				funSetFunctionCode(code);
				break;
		}
	}



	function funSetData(code)
	{
		$("#txtFunctionCode").val(code);
		var searchurl=getContextPath()+ "/loadFunctionMasterData.html?functionCode=" + code;
		$.ajax({
			type : "GET",
			url : searchurl,
			dataType : "json",
			success : function(response)
			{ 
				if(response.strFunctionCode=='Invalid Code')
	        	{
	        		alert("Invalid Function Code");
	        		$("#txtFunctionCode").val('');
	        	}
				else
				{
					$("#txtFunctionCode").val(response.strFunctionCode);
	        		$("#txtFunctionName").val(response.strFunctionName);
	        		$("#txtFunctionName").focus();
	        		$("#txtOperational").val(response.strOperationalYN);
	        		
				}

			},
			error : function(jqXHR, exception){
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





	function funHelp(transactionName)
	{
		fieldName=transactionName;
		//window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
		 window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
	}
</script>

</head>
<body>

	<div id="formHeading">
	<label>FunctionMaster</label>
	</div>

<br/>
<br/>

	<s:form name="FunctionMaster" method="POST" action="saveFunctionMaster.html">

		<table class="masterTable">
			<tr>
				<td>
					<label>Function Code</label>
				</td>
				<td>
					<s:input colspan="2" type="text" id="txtFunctionCode" path="strFunctionCode" value="" cssClass="searchTextBox" ondblclick="funHelp('functionMaster')" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Function Name</label>
				</td>
				<td>
					<s:input colspan="2" type="text" id="txtFunctionName" path="strFunctionName" value="" cssClass="longTextBox" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Operational Y/N</label>
				</td>
				
					<!-- <input colspan="3" type="text" id="txtOperationalYN" cssClass="longTextBox" /> -->
					<td colspan="2"><s:select path="strOperationalYN"  cssClass="BoxW62px" id="txtOperational">
						<s:option value="Y">YES</s:option>
						<s:option value="N">NO</s:option></s:select></td>
				
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
