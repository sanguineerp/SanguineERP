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

	function funSetData(code){

		switch(fieldName){
		
			case 'MenuHeadCode' : {
				funSetMenuHeadData(code);
				break;
			}
			
			case 'subgroup' : {
				funSetSubGroupData(code);
				break;
			}
			
			case 'deptCode' : {
				funSetDepartmentData(code);
				break;
			}
			
			case 'ItemCode' : {
				funSetItemData(code);
				break;
			}

		}
	}
	
	function funSetDepartmentData(code)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadDeptMasterData.html?deptCode=" + code,
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
	        		$("#lblDepartmentName").text(response.strDeptDesc);
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
	
	function funSetMenuHeadData(code)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadMenuHeadCode.html?docCode=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strMenuHeadCode=='Invalid Code')
	        	{
	        		alert("Invalid Menu Head Code");
	        		$("#txtMenuHeadCode").val('');
	        	}
	        	else
	        	{
	        		$("#txtMenuHeadCode").val(response.strMenuHeadCode);
	        		$("#lblMenuHeadName").text(response.strMenuHeadName);
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
	
	function funSetSubGroupData(code)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadSubGroupMasterData.html?subGroupCode=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strSGCode=='Invalid Code')
	        	{
	        		alert("Invalid Sub-Group Code");
	        		$("#txtSubGroupCode").val('');
	        	}
	        	else
	        	{
	        		$("#txtSubGroupCode").val(response.strSGCode);
	        		$("#lblSubGroupName").text(response.strSGName);
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
	
	function funSetItemData(code)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadItemCode.html?docCode=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strItemCode=='Invalid Code')
	        	{
	        		alert("Invalid Item Code");
	        		$("#txtItemCode").val('');
	        	}
	        	else
	        	{
	        		$("#txtItemName").val(response.strItemName);
	        		$("#txtItemCode").val(response.strItemCode);
	        		$("#txtMenuHeadCode").val(response.strMenuHeadCode);
	        		$("#txtSubGroupCode").val(response.strSubGroupCode);
	        		$("#txtDepartmentCode").val(response.strDepartmentCode);
	        		$("#txtUnit").val(response.strUnit);
	        		$("#txtAmount").val(response.dblAmount);
	        		$("#txtPercent").val(response.dblPercent);
	        		document.getElementById("chkOperational").checked = response.strOperational == 'Yes' ? true
							: false;
	        		$("#cmbTaxIndicator").val(response.strTaxIndicator);
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

	function funValidate(data)
	{
		var flg=true;
		if($("#txtItemName").val().trim().length==0)
		{
			alert("Please Enter Item Name !!");
			flg=false;
		}
		else if($("#txtMenuHeadCode").val().trim().length==0)
		{
			alert("Please Select Menu Head !!");
			flg=false;
		}
		else if($("#txtSubGroupCode").val().trim().length==0)
		{
			alert("Please Select Sub Group !!");
			flg=false;
		}
		else if($("#txtDepartmentCode").val().trim().length==0)
		{
			alert("Please Select Department !!");
			flg=false;
		}
		return flg;
	}


	function funHelp(transactionName)
	{
		fieldName=transactionName;
		window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
	}
	
	function funResetFields()
	{
		$("#lblMenuHeadName").text("");
		$("#lblSubGroupName").text("");
		$("#lblDepartmentName").text("");
	}
	function funUOMChange(object)
	{
		 var index=object.parentNode.parentNode.rowIndex;
		 var strUOM=object.value;
		 $("#txtUnit").val(strUOM.toUpperCase());
	}
</script>

</head>
<body>

	<div id="formHeading">
	<label>Item Master</label>
	</div>

<br/>
<br/>

	<s:form name="frmItemMaster" method="POST" action="saveItemMaster.html">

		<table class="masterTable">
			<tr>
				<td style="width:20%;">
					<label>Item Code</label>
				</td>
				<td>
					<s:input colspan="3" path="strItemCode" type="text" id="txtItemCode" cssClass="searchTextBox jQKeyboard form-control" ondblclick="funHelp('ItemCode')" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Item Name</label>
				</td>
				<td>
					<s:input colspan="3" path="strItemName" type="text" id="txtItemName" cssClass="longTextBox" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Menu Head Code</label>
				</td>
				<td>
					<s:input colspan="3" path="strMenuHeadCode" type="text" id="txtMenuHeadCode" cssClass="searchTextBox jQKeyboard form-control" ondblclick="funHelp('MenuHeadCode')" />
					&nbsp&nbsp&nbsp&nbsp<label id="lblMenuHeadName"></label>
				</td>
			</tr>
			<tr>
				<td>
					<label>Sub-Group Code</label>
				</td>
				<td>
					<s:input colspan="3" path="strSubGroupCode" type="text" id="txtSubGroupCode" cssClass="searchTextBox jQKeyboard form-control" ondblclick="funHelp('subgroup')" />
					&nbsp&nbsp&nbsp&nbsp<label id="lblSubGroupName"></label>
				</td>
			</tr>
			<tr>
				<td>
					<label>Department Code</label>
				</td>
				<td>
					<s:input colspan="3" path="strDepartmentCode" type="text" id="txtDepartmentCode" cssClass="searchTextBox jQKeyboard form-control" ondblclick="funHelp('deptCode')"/>
					&nbsp&nbsp&nbsp&nbsp<label id="lblDepartmentName"></label>
				</td>
			</tr>
			<tr>
				<td>
					<label>Unit</label>
				</td>
				<td>
					<s:select colspan="3" path="strUnit" type="text" id="txtUnit" cssClass="BoxW124px"
						items="${uomList}" onchange="funUOMChange(this)" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Amount</label>
				</td>
				<td>
					<s:input colspan="3" path="dblAmount" type="number" step="0.01" id="txtAmount" cssClass="longTextBox" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Percent</label>
				</td>
				<td>
					<s:input colspan="3" path="dblPercent" type="number" step="0.01" id="txtPercent" cssClass="longTextBox" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Operational</label>
				</td>
				<td>
					<s:checkbox value="true" element="li" id="chkOperational" path="strOperational"  checked="true"/>
				</td>
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
			<input type="submit" value="Submit" tabindex="3" class="form_button" onclick="return funValidate(this)" />
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>

	</s:form>
</body>
</html>
