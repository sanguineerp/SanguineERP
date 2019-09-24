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
	function funValidateFields()
	{
		var flg=true;
		var businessSrcName =  $("#txtBusinessSourceName").val();
		var businessSrcPer =  $("#txtBusinessSourcePercent").val();
		
		if ( $("#txtBusinessSourceName").val() == "")
	    {
	        alert("Please enter business source name");
	        flg=false;
	    }
		
		if ($("#txtBusinessSourcePercent").val()=="")
	    {
	        alert("Please enter business source percent");
	        flg=false;
	    }
		return flg;
	}
	
	
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
	<label>Business Source</label>
	</div>

<br/>
<br/>

	<s:form name="WebClubBusinessSourceMaster" method="POST" action="saveWebClubBusinessSourceMaster.html">

		<table class="masterTable">
			<tr>
			
				<td><label>Business Source Code</label></td>
				<td><s:input id="txtBusinessSrcCode" path="strBusinessSrcCode" cssClass="searchTextBox" ondblclick="funHelp('webClubBusinessSrcCode')" /></td>				
				
			</tr>
			<tr>
			    <td><label>Business Source Name</label></td>
				<td><s:input id="txtBusinessSourceName" path="strBusinessSrcName" cssClass="longTextBox" /></td>				
			</tr>
			
			<tr>
			    <td><label>Business Source Percent</label></td>
				<td><s:input id="txtBusinessSourcePercent" path="dblPercent" style = "text-align:right;" cssClass="longTextBox" /></td>				
			</tr>
			
		</table>

		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button"
				onclick="return funValidateFields()" /> 
				<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>

	</s:form>
</body>
</html>
