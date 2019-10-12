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
		if($("#txtBanquetTypeName").val().trim().length==0)
			{
			alert("Please Enter Name !!");
			 flg=false;
			 $("#txtBanquetTypeName").focus();
			}
		
		if($("#cmbTaxIndicator").val().trim().length==0)
		{
		alert("Please Select Tax Indicator !!");
		 flg=false;
		 $("#cmbTaxIndicator").focus();
		}
		
		
		return flg;
	}
	
	function funSetData(code){

		switch(fieldName){
		
		case 'banquetTypeCode' : 
			funSetBanquetTypeName(code);
			break;

		}
	}


	function funSetBanquetTypeName(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadBanquetType.html?docCode=" + code,
			dataType : "json",
			success : function(response){ 

				if(response.strEquipmentCode=='Invalid Code')
	        	{
	        		alert("Invalid Equipment No");
	        		$("#txtBanquetTypeCode").val('');
	        	}
	        	else
	        	{
	        		
	        		$("#txtBanquetTypeName").val(response.strBanquetTypeName);
	        		$("#txtBanquetTypeCode").val(response.strBanquetTypeCode);
	        		$("#txtRate").val(response.dblRate);
	        		$("#cmbTaxIndicator").val(response.strTaxIndicator);
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
	<label>Banquet Type Master</label>
	</div>

<br/>
<br/>

	<s:form name="Banquet Type Master" method="POST" action="saveBanquetTypeMaster.html">

		<table class="masterTable">
			<tr>
				<td>
					<label>Banquet Type Code</label>
				</td>
				<td>
					<s:input colspan="3" type="text" id="txtBanquetTypeCode" path="strBanquetTypeCode" ondblclick="funHelp('banquetTypeCode')" cssClass="searchTextBox jQKeyboard form-control" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Banquet Type Name</label>
				</td>
				<td>
					<s:input colspan="3" style="width:60%;" type="text" id="txtBanquetTypeName" path="strBanquetTypeName" cssClass="longTextBox" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Rate</label>
				</td>
				<td>
					<s:input colspan="3" type="number" style="text-align:right;width:23%;" step="0.01" id="txtRate" path="dblRate" cssClass="longTextBox" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Tax Indicator</label>
				</td>
				
				<td><s:select id="cmbTaxIndicator" name="taxIndicator" path="strTaxIndicator" items="${taxIndicatorList}"  cssClass="BoxW48px" /></td>
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
