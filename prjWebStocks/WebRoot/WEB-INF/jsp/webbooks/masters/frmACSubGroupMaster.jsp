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
	
	function funSetAccountsSubGroupData(subGroupCode)
	{
	    $("#txtSubGroupCode").val(subGroupCode);
		var searchurl=getContextPath()+"/loadACSubGroupMasterData.html?acSubGroupCode="+subGroupCode;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(fieldName=="acSubGroupCode")
			        	{
			        		
			        		if(response.strGroupCode=='Invalid Code')
				        	{
				        		alert("Invalid Group Code");
				        		$("#txtSubGroupCode").val('');
				        		$("#txtSubGroupName").val('');
				        	}
				        	else
				        	{
					        	$("#txtSubGroupName").val(response.strSubGroupName);
					        	$("#txtSubGroupName").focus();
					        	$("#txtGroupCode").val(response.strGroupCode);
					        	$("#lblGroupName").text(response.strGroupName);
					        	$("#txtUnderSubGroup").val(response.strUnderSubGroup);
				        	}	
			        	}else{
			        		
			        		if(response.strGroupCode=='Invalid Code')
				        	{
				        		alert("Invalid Group Code");
				        		$("#txtUnderSubGroup").val('');
				        	}
				        	else
				        	{
				        		 $("#lblUnderSubGroup").val(response.strSubGroupName);
					        	
				        	}
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


	function funSetUnderSubGroupData(subGroupCode)
	{
	    $("#txtUnderSubGroup").val(subGroupCode);
		var searchurl=getContextPath()+"/loadUnderSubGroupMasterData.html?acSubGroupCode="+subGroupCode;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "text",
			        success: function(response)
			        {
			        	 $("#lblUnderSubGroup").text(response);
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
	function funSetAccountsGroupData(groupCode)
	{
	    $("#txtGroupCode").val(groupCode);
		var searchurl=getContextPath()+"/loadACGroupMasterData.html?acGroupCode="+groupCode;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(response.strGroupCode=='Invalid Code')
			        	{
			        		alert("Invalid Group Code");
			        		$("#txtGroupCode").val('');
			        	}
			        	else
			        	{
				        	$("#lblGroupName").text(response.strGroupName);
				        	
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
	
	function funSetData(code){

		switch(fieldName)
		{		
			
			case "acGroupCode":
			       funSetAccountsGroupData(code);
				 break;
			case "acSubGroupCode":
				funSetAccountsSubGroupData(code);
				 break;
				 
			case "underSubGroupCode":
				funSetUnderSubGroupData(code);
				 break;
				 
			
		}
	}
	

	function funHelp(transactionName)
	{
		fieldName=transactionName;
		
		if(transactionName=="underSubGroupCode"){
			var strGroupCode=$("#txtGroupCode").val();
			var strSubGroupCode=$("#txtSubGroupCode").val();
			window.open("searchform.html?formname="+transactionName+"&strGroupCode="+strGroupCode+"&strSubGroupCode="+strSubGroupCode+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
		}else{
			window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");	
		}
	
		
	}
	function funResetFields(){
		
		$("#lblUnderSubGroup").text('');
		$("#lblGroupName").text('');
		
		$("#txtSubGroupCode").val('');
		$("#txtSubGroupName").val('');
		$("#txtGroupCode").val('');
		$("#txtUnderSubGroup").val('');
	}
</script>

</head>
<body>

	<div id="formHeading">
	<label>Group Master</label>
	</div>

<br/>
<br/>

	<s:form name="BankMaster" method="POST" action="saveACSubGroupMaster.html">

		<table class="masterTable">
			<tr>
			    <td><label >Sub Group Code</label></td>
			    <td><s:input id="txtSubGroupCode" path="strSubGroupCode"  ondblclick="funHelp('acSubGroupCode')" cssClass="searchTextBox"/></td>			        			        
			    <td><s:input id="txtSubGroupName" path="strSubGroupName" required="true" cssClass="longTextBox"/></td>			    			    			    		        			 
			</tr>
			<tr>
				<td><label>Group Code</label> </td>
				<td><s:input id="txtGroupCode" path="strGroupCode"  ondblclick="funHelp('acGroupCode')" cssClass="searchTextBox"/></td>
				<td><label id="lblGroupName"></label></td>					
			</tr>
			<tr>
				<td><label>Under SubGroup Code</label> </td>
				<td><s:input id="txtUnderSubGroup" path="strUnderSubGroup"  ondblclick="funHelp('underSubGroupCode')" cssClass="searchTextBox"/></td>
				<td><label id="lblUnderSubGroup"></label></td>					
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
