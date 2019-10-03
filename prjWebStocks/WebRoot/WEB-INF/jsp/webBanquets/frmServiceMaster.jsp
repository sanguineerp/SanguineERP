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
		$("#txtServiceName").focus();
    }
	
	$(function() 
	{
		 /**
		* On Blur Event on TextField
		**/
		$('#txtServiceCode').blur(function() 
		{
				var code = $('#txtServiceCode').val();
				if (code.trim().length > 0 && code !="?" && code !="/")
				{				
					funSetData(code);							
				}
		});
		
		$('#txtServiceName').blur(function () {
			 var strFunctionName=$('#txtServiceName').val();
		      var st = strFunctionName.replace(/\s{2,}/g, ' ');
		      $('#txtServiceName').val(st);
			});
		
	});

	function funSetData(code){

		switch(fieldName){

			case 'ServiceName' : 
				funSetServiceName(code);
				break;
		}
	}



	function funSetData(code)
	{
		$("#txtServiceCode").val(code);
		var searchurl=getContextPath()+ "/loadServiceMasterData.html?serviceCode=" + code;
		$.ajax({
			type : "GET",
			url : searchurl,
			dataType : "json",
			success : function(response)
			{ 
				if(response.strServiceCode=='Invalid Code')
	        	{
	        		alert("Invalid Function Code");
	        		$("#txtServiceCode").val('');
	        	}
				else
				{
					$("#txtServiceCode").val(response.strServiceCode);
	        		$("#txtServiceName").val(response.strServiceName);
	        		$("#txtServiceName").focus();
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
	<label>ServiceMaster</label>
	</div>

<br/>
<br/>

	<s:form name="ServiceMaster" method="POST" action="saveServiceMaster.html">

		<table class="masterTable">
			<tr>
				<td>
					<label>Service Code</label>
				</td>
				<td>
					<s:input colspan="3" type="text" id="txtServiceCode" path="strServiceCode" cssClass="searchTextBox" ondblclick="funHelp('ServiceMaster')" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Service Name</label>
				</td>
				<td>
					<s:input colspan="3" type="text" id="txtServiceName" path="strServiceName" cssClass="longTextBox" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Operational Y/N</label>
				</td>
				<!-- <td>
					<input colspan="3" type="text" id="txtOperationalYN" cssClass="longTextBox" />
				</td> -->
				<td colspan="2"><s:select width="140px" path="strOperationalYN"  cssClass="BoxW62px" id="txtOperational">
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
