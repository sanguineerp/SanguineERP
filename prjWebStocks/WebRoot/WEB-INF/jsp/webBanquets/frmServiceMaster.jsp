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
	
	

function funSetData(code){

		switch(fieldName){

			case 'ServiceMaster' :{
				funSetServiceName(code);
				break;
		     }
			case 'deptCode' : {
				funSetDepartmentData(code);
				break;
			}
			
		}
	}



	function funSetServiceName(code)
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
	        		alert("Invalid Service Code");
	        		$("#txtServiceCode").val('');
	        	}
				else
				{
					$("#txtServiceCode").val(response.strServiceCode);
	        		$("#txtServiceName").val(response.strServiceName);
	        		//$("#txtOperational").val(response.strOperationalYN);
	        		$("#txtDeptCode").val(response.strDeptCode);
	        		$("#txtRate").val(response.dblRate);
	        		document.getElementById("txtOperational").checked = response.strOperationalYN == 'Yes' ? true
							: false;
	        		
	        		if (response.strOperationalYN == 'Y') {
						$("#txtOperational").prop('checked',
								true);
					} else
						$("#txtOperational").prop('checked',
								false);
	        		
	        		$("#cmbTaxIndicator").val(response.strTaxIndicator);
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
		        		$("#txtDeptCode").val('');
		        	}
		        	else
		        	{
		        		$("#txtDeptCode").val(response.strDeptCode);
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
	<label>Service Master</label>
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
					<s:input colspan="3" type="text" id="txtServiceCode" path="strServiceCode" cssClass="searchTextBox jQKeyboard form-control" ondblclick="funHelp('ServiceMaster')" />
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
				<td ><s:checkbox path="strOperationalYN" id="txtOperational" value="Y" />
				
				<%-- <td ><s:checkbox value="true" path="strOperationalYN"  id="txtOperational"  /> --%>
					
			</tr>
			<tr>
				<td>
				   <label>Department Code</label>
				</td>
				<td ><s:input colspan="3" type="text" id="txtDeptCode" path="strDeptCode"  readonly="true" cssClass="searchTextBox jQKeyboard form-control" ondblclick="funHelp('deptCode')"/>
				&nbsp&nbsp&nbsp&nbsp<label id="lblDepartmentName"></label>		
			</tr>
			
			<tr>
				<td>
					<label>Rate</label>
				</td>
				<td>
					<s:input colspan="3" type="text" id="txtRate" path="dblRate" cssClass="decimal-places numberField" />
				</td>
				<!-- <td><label >Weight</label></td> -->
				<%--  <td><s:input id="txtWeight" name="weight" path="dblWeight" cssClass="decimal-places numberField"/></td> --%>
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
			<input type="submit" value="Submit" tabindex="3" class="form_button" />
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>

	</s:form>
</body>
</html>
