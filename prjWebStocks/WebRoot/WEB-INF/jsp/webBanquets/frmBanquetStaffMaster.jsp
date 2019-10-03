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
		alert("\nStaff Data Save successfully");
	<%
	}}%>

});
	

	
	//validation
	function funValidate(data)
	{
		var flg=true;
		if($("#txtStaffName").val().trim().length==0)
			{
			alert("Please Enter Staff Name !!");
			 flg=false;
			}
		else if($("#txtDeptCode").val().trim().length==0)
			{
			
			alert("Please Select Department Code !!");
			 flg=false;
			
			}
		return flg;
	}
	
	
	function funSetData(code){

		switch(fieldName)
		{		
		   case 'staffCode':
		    	funSetStaffCode(code);
		        break;
		   
		   case 'deptCode':
		    	funSetDeptCode(code);
		        break;
		
		
		}
	}

	
	function funSetStaffCode(code)
	{
			var searchUrl="";
			searchUrl=getContextPath()+"/loadStaffMasterData.html?staffCode="+code;			
			$.ajax({
			        type: "GET",
			        url: searchUrl,
				    dataType: "json",
				    success: function(response)
				    {
				    	$("#txtStaffCode").val(code);
				    	$("#txtStaffName").val(response.strStaffName);
				    	$("#txtDeptCode").val(response.strDeptCode);
				    	$("#cmbOperationalYN").val(response.strOperationalYN);
		        		
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
	
	
	function funSetDeptCode(code)
	{
		$("#txtDeptCode").val(code);
		
	}
	
	
	function funHelp(transactionName)
	{		
		fieldName=transactionName;
		window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px,dialogWidth:1100px,top=500,left=500")
		
	}
</script>

</head>
<body>

	<div id="formHeading">
	<label>Staff Master</label>
	</div>

<br/>
<br/>
																		
	<s:form name="BanquetStaffMaster" method="POST" action="saveBanquetStaffMaster.html">

		<table class="masterTable">				
			 <tr>
				<td>
					<label>Staff Code</label>
				</td>
				<td><s:input id="txtStaffCode" name="txtreqCode" path="strStaffCode" cssClass="searchTextBox" ondblclick="funHelp('staffCode')" readonly="true"/>
				</td>
			</tr> 
			<tr>
				<td>
					<label>Staff Name</label>
				</td>				
					<td><s:input id="txtStaffName" name="txtStaffName" path="strStaffName" class="BoxW124px" />
					</td>				
			</tr>
			<tr>
				<td>
					<label>Department Code</label>
				</td>								
					<td><s:input id="txtDeptCode" name="txtDeptCode" path="strDeptCode" class="searchTextBox" ondblclick="funHelp('deptCode')" readonly="true"/>
					</td>					
			</tr>
			
			<tr>			
			<td><label>Operational</label></td>
				<td><s:select id="cmbOperationalYN" name="strOperational" path="strOperationalYN" cssClass="BoxW124px"  >
 				 <option value="Yes">Yes</option>
 				 <option value="No">No</option>
				</s:select></td>
				
			</tr>
		
		</table>

		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3"  class="form_button" onclick="return funValidate(this)"/>
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>

	</s:form>
</body>
</html>
