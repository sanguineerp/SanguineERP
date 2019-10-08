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
	
	function funResetFields()
	{
		location.reload(true); 
	}
	
		function funSetData(code){

		switch(fieldName){

		case 'WCFacilityMaster' :
			funSetFacilityData(code);
			break;
		}
	}
 

function funSetFacilityData(code){

	$.ajax({
		type : "GET",
		url : getContextPath()+ "/loadWebClubFacilityMasterData.html?docCode=" + code,
		dataType : "json",
		success : function(response){ 

			if(response.strFacilityCode=='Invalid Code')
        	{
        		alert("Invalid Facility Code");
        		$("#txtFacilityCode").val('');
        	}
        	else
        	{      
	        	$("#txtFacilityCode").val(code);
	        	$("#txtFacilityName").val(response.strFacilityName);
	        
	        	if(response.strOperationalNY=='Y')
	        	{
	        		$("#chkOperationalNY").attr('checked', true);
	        	}
	        	else
	        	{
	        		$("#chkOperationalNY").attr('checked', false);
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
	<label>Facility Master</label>
	</div>

<br/>
<br/>

	<s:form name="WebClubFacilityMaster" method="POST" action="saveWebClubFacilityMaster.html">

		<table class="masterTable">
		
			<tr>    
		        <td width="10px">
		        <label>Facility Code</label></td>
		        <td width="20px"><s:input id="txtFacilityCode" path="strFacilityCode" required=""
				              cssClass="searchTextBox" type="text" ondblclick="funHelp('WCFacilityMaster')" ></s:input></td>
		       
		</tr>
		<tr>
		        <td width="10px">
		        <label>Facility Name</label>
		        <td colspan="2"><s:input id="txtFacilityName" path="strFacilityName" required="true"
				            cssStyle="width:50% ;" cssClass="longTextBox" type="text" ></s:input></td>
	</tr>
	<tr>		        
		         <td width="20px">
		        <label>Operational</label>		       
		        <td colspan="2"><s:checkbox id="chkOperationalNY" path="strOperationalNY" value="Y" checked="true" /></td>
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
