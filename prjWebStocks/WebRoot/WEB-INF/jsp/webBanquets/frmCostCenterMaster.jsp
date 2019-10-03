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
	
	$("#txtOperational").attr('checked', true);

});
	
	function funValidate(data)
	{
		var flg=true;
		if($("#txtCostCenterName").val().trim().length==0)
			{
			alert("Please Select Name !!");
			 flg=false;
			}
		return flg;
	}
	
	function funSetData(code){

		switch(fieldName){

			case 'CostCenterCode' : 
				funSetCostCenterCode(code);
				break;
		}
	}


	function funSetCostCenterCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadCostCenterCode.html?docCode=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strCostCenterCode=='Invalid Code')
	        	{
	        		alert("Invalid Cost Center Code");
	        		$("#txtCostCenterCode").val('');
	        	}
	        	else
	        	{
	        		
	        		if(response.strOperational=='Y')
		        	{
		        		/* $("#txtOperational").attr('checked', true); */
		        		document.getElementById("txtOperational").checked = response.strOperational == 'Y' ? true: false;
		        	}
	        		else
		        	{
		        		$("#txtOperational").attr('checked', false);
		        		
		        	}
	        		$("#txtCostCenterName").val(response.strCostCenterName);
	        		$("#txtCostCenterCode").val(response.strCostCenterCode);
	        	}
			},
			error : function(e){

			}
		});
	}








	function funHelp(transactionName)
	{
		fieldName=transactionName;
		window.open("searchform.html?formname="+fieldName+"&searchText=", 'window', 'width=600,height=600');
	}
</script>

</head>
<body>

	<div id="formHeading">
	<label>Cost Center Master</label>
	</div>

<br/>
<br/>

	<s:form name="CostCenterMaster" method="POST" action="saveCostCenterMaster.html">

		<table class="masterTable">
			<tr>
				<td style="width:20%;">
					<label>Cost Center Code</label>
				</td>
				<td>
					<s:input colspan="3" type="text" path="strCostCenterCode" id="txtCostCenterCode" ondblclick="funHelp('CostCenterCode')" cssClass="searchTextBox jQKeyboard form-control" />
				</td>
				
				
			</tr>
			<tr>
				<td>
					<label>Cost Center Name</label>
				</td>
				<td>
					<s:input colspan="3" type="text" path="strCostCenterName" id="txtCostCenterName" cssClass="longTextBox" />
				</td>
			</tr>
			<tr>
			
			<td><label>Operational</label></td>
				<td colspan="3"><s:checkbox id="txtOperational" path="strOperational" value="Y"/></td>
				
			</tr>
		</table>

		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" onclick="return funValidate(this)" class="form_button" />
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>

	</s:form>
</body>
</html>
