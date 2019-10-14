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

	$("#strOperational").attr('checked', true);
});

	function funValidate(data)
	{
		var flg=true;
		if($("#txtEquipmentName").val().trim().length==0)
			{
			alert("Please Select Name !!");
			 flg=false;
			 $("#txtEquipmentName").focus();
			}
		
		if($("#txtDepartmentCode").val().trim().length==0)
		{
		alert("Please Select Department Code !!");
		 flg=false;
		 $("#txtDepartmentCode").focus();
		}
		return flg;
	}
	
	function funSetData(code){

		switch(fieldName){

			case 'equipmentCode' : 
				funSetEquipmentName(code);
				break;
				
			case 'deptCode' : 
				funSetDepartmentCode(code);
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
	        		if(response.strOperational=='Y')
		        	{
		        		document.getElementById("strOperational").checked = response.strOperational == 'Y' ? true: false;
		        	}
	        		else
		        	{
		        		$("#strOperational").attr('checked', false);
		        		
		        	}
	        		$("#txtEquipmentName").val(response.strEquipmentName);
	        		$("#txtEquipmentCode").val(response.strEquipmentCode);
	        		$("#txtDepartmentCode").val(response.strDeptCode);
	        		$("#dblEquipmentRate").val(response.dblEquipmentRate);
	        		$("#cmbTaxIndicator").val(response.strTaxIndicator);
	        	}
			},
			error : function(e){

			}
		});
	}
	function funSetDepartmentCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadDepartmentCode.html?deptCode=" + code,
			dataType : "json",
			success : function(response){ 

				if(response.strDeptCode=='Invalid Code')
	        	{
	        		alert("Invalid Department Code");
	        		$("#txtDepartmentCode").val('');
	        	}
	        	else
	        	{
	        	
	        		$("#txtDepartmentCode").val(response.strDeptCode);
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
	
	function isNumber(evt) {
        var iKeyCode = (evt.which) ? evt.which : evt.keyCode
        if (iKeyCode != 46 && iKeyCode > 31 && (iKeyCode < 48 || iKeyCode > 57))
            return false;

        return true;
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
				<td style="width:20%;">
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
					<label>Equipment Rate</label>
				</td>
				<td colspan="4">
				
					<s:input colspan="3" type="text" value="0" path="dblEquipmentRate" id="dblEquipmentRate" style="width:17%;text-align:right;" onkeypress="javascript:return isNumber(event)" cssClass="longTextBox" /> 
					
				</td>
				</tr>
				
				<tr>
				<td>
					<label>Department Code</label>
				</td>
				
				
				<td>
					<s:input colspan="3" type="text" id="txtDepartmentCode" path="strDeptCode" ondblclick="funHelp('deptCode')" cssClass="searchTextBox jQKeyboard form-control"  />
				</td>
			</tr>
				
				<tr>
			
			<td><label>Operational</label></td>
				<td colspan="3"><s:checkbox id="strOperational" path="strOperational" value="Y"/></td>
				
			</tr>
			
			<tr>
				<td><label>Tax Indicator</label></td>
				<td><s:select id="cmbTaxIndicator" name="taxIndicator"
				path="strTaxIndicator" items="${taxIndicatorList}"  cssClass="BoxW48px"/></td>
			
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
