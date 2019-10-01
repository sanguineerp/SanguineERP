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

	function funValidate(data)
	{
		var flg=true;
		if($("#txtEquipmentName").val().trim().length==0)
			{
			alert("Please Select Name !!");
			 flg=false;
			}
		return flg;
	}
	
	function funSetData(code){

		switch(fieldName){

			case 'equipmentCode' : 
				funSetEquipmentName(code);
				break;
		}
	}



	function funSetEquipmentName(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadEquipmentName.html?docCode=" + code,
			dataType : "json",
			success : function(response){ 

				if(response.strEquipmentCode=='Invalid Code')
	        	{
	        		alert("Invalid Equipment No");
	        		$("#txtEquipmentCode").val('');
	        	}
	        	else
	        	{
	        		$("#strOperational").val(response.strOperational);
	        		$("#txtEquipmentName").val(response.strEquipmentName);
	        		$("#txtEquipmentCode").val(response.strEquipmentCode);
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
	<label>Equipment Master</label>
	</div>

<br/>
<br/>

	<s:form name="Equipment" method="POST" action="saveEquipment.html">

		<table class="masterTable">
			<tr>
				<td>
					<label>Equipment Code</label>
				</td>
				
				
				<td>
					<s:input colspan="3" type="text" id="txtEquipmentCode" path="strEquipmentCode" ondblclick="funHelp('equipmentCode')" cssClass="searchTextBox jQKeyboard form-control"  />
				</td>
			</tr>
			<tr>
				<td>
					<label>Equipment Name</label>
				</td>
				<td colspan="4">
				
					<s:input colspan="3" type="text" path="strEquipmentName" id="txtEquipmentName" cssClass="longTextBox" /> 
					
				</td>
				</tr>
				<tr>
				<td>
					<label>Operational</label>
				</td>
				<td>
				<%-- 	<s:input colspan="3" type="text" path="strOperational" id="strOperational" cssClass="BoxW124px" /> --%>
					<s:select id="strOperational" path="strOperational" cssClass="BoxW124px">
				    		<s:option value="Yes">Yes</s:option>
				    		<s:option value="No">No</s:option>
				    		</s:select>
				</td>
			</tr>
		</table>

		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" onclick="return funValidate(this)" class="form_button" />
			<input type="reset" value="Reset" class="form_button" onclick="return funResetFields()"/>
		</p>

	</s:form>
</body>
</html>
