<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Staff Category</title>
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
		alert("Data Save successfully :\t"+message);
	<%
	}}%>

});
	
	
	
	//validation
	function funValidate(data)
	{
		var flg=true;
		if($("#txtStaffCategeoryName").val().trim().length==0)
			{
			alert("Please Enter Staff Categeory Name !!");
			 flg=false;
			}
		else if($("#txtStaffCount").val().trim().length==0)
			{
			
			alert("Please Enter Staff Count!!");
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

		switch(fieldName){

		case 'StaffCatCode':
			funSetStaffMasterCode(code);
	        break;
		
		
		case 'deptCode':
			funSetDeptCode(code);
	        break;
		}
	}

	function funSetDeptCode(code)
	{
		$("#txtDeptCode").val(code);
	}
	
	
	function funSetStaffMasterCode(code)
	{
			var searchUrl="";
			searchUrl=getContextPath()+"/loadStaffCategeoryMasterData.html?staffCatCode="+code;			
			$.ajax({
			        type: "GET",
			        url: searchUrl,
				    dataType: "json",
				    success: function(response)
				    {
				    	$("#txtStaffCategeoryCode").val(code);
				    	$("#txtStaffCategeoryName").val(response.strStaffCategeoryName);
				    	$("#txtStaffCount").val(response.strStaffCount);
				    	$("#txtDeptCode").val(response.strDeptCode);
				    	//$("#txtOperationalYN").val(response.strOperationalYN);
		        				        		
		        		if(response.strOperationalYN=="Y")
			        	{
			        		/* $("#txtOperational").attr('checked', true); */
			        		document.getElementById("txtOperationalYN").checked = response.strOperationalYN == 'Y' ? true: false;
			        	}
		        		else
			        	{
			        		$("#txtOperationalYN").attr('checked', false);
			        		
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
		window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px,dialogWidth:1100px,top=500,left=500")
		
		//window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
	}
</script>

</head>
<body>

	<div id="formHeading">
	<label>Staff Categeory Master</label>
	</div>

<br/>
<br/>

	<s:form name="BanquetStaffCategeoryMaster" method="POST" action="saveBanquetStaffCategeoryMaster.html">

		<table class="masterTable">
			<tr>
				<td>
					<label>Staff Categeory Code</label>
				</td>
								
				<td><s:input id="txtStaffCategeoryCode" name="txtStaffCategeoryCode" path="strStaffCategeoryCode" cssClass="searchTextBox" ondblclick="funHelp('StaffCatCode')" readonly="true"/>
				</td>
			</tr>
			<tr>
				<td>
					<label>Staff Categeory Name</label>
				</td>				
				<td><s:input id="txtStaffCategeoryName" name="txtStaffCategeoryName" path="strStaffCategeoryName" class="BoxW124px" />
					</td>
				
			</tr>
			<tr>
				<td>
					<label>Staff Count</label>
				</td>				
				<td><s:input id="txtStaffCount" name="txtStaffCount" path="strStaffCount" class="BoxW124px" />
					</td>
				
			</tr>
			<tr>
				<td>
					<label>Department Code</label>
				</td>				
				<td><s:input id="txtDeptCode" name="txtDeptCode" path="strDeptCode" cssClass="searchTextBox" ondblclick="funHelp('deptCode')" readonly="true"/>
				</td>			
			</tr>
			<tr>
				<td>
					<label>Operational Y/N</label>
				</td>	
									
				 <td><s:checkbox id="txtOperationalYN" name="txtOperationalYN" path="strOperationalYN" value="Y" checked="true"/></td> 
				
			</tr>
			
		</table>

		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button" onclick="return funValidate(this)"/>
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>

	</s:form>
</body>
</html>
