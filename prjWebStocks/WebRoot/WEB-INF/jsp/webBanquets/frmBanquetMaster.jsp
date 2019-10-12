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
		
		$("#strOperational").attr('checked', true);
	});

	function funValidate(data)
	{
		var flg=true;
		if($("#txtBanquetName").val().trim().length==0)
			{
			alert("Please Enter Name !!");
			 flg=false;
			 $("#txtBanquetName").focus();
			}
		
		
		return flg;
	}
	
	function funSetData(code){

		switch(fieldName){

		case 'banquetCode' : 
			funSetBanquetName(code);
			break;
		
		
		}
	}


	function funSetBanquetName(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadBanquetName.html?docCode=" + code,
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
	        		$("#txtBanquetCode").val(response.strBanquetCode);
	        		$("#txtBanquetName").val(response.strBanquetName);
	        		if(response.strOperational=='Y')
		        	{
		        		document.getElementById("strOperational").checked = response.strOperational == 'Y' ? true: false;
		        	}
	        		else
		        	{
		        		$("#strOperational").attr('checked', false);
		        		
		        	}
	        		
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
	<label>Banquet Master</label>
	</div>

<br/>
<br/>

	<s:form name="BanquetMaster" method="POST" action="saveBanquetMaster.html">

		<table class="masterTable">
			<tr>
				<td>
					<label>Banquet Code</label>
				</td>
				<td>
					<s:input colspan="3" type="text" id="txtBanquetCode" path="strBanquetCode" ondblclick="funHelp('banquetCode')" cssClass="searchTextBox jQKeyboard form-control" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Banquet Name</label>
				</td>
				<td>
					<s:input colspan="3" type="text" id="txtBanquetName" path="strBanquetName" cssClass="longTextBox" />
				</td>
			</tr>
			
			<tr>
			
			<td><label>Operational</label></td>
				<td colspan="3"><s:checkbox id="strOperational" path="strOperational" value="Y"/></td>
				
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
