<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <!-- charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> -->
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>

<script type="text/javascript">

/**
* Open Help
**/
	var fieldName;

function funHelp(transactionName)
{	       
   // window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
   fieldName = transactionName;
   window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
}

function funSetData(code)
{

	switch(fieldName){

	case 'GLCode' : 
		funSetGLCode(code);
		break;
		
	case 'expense' : 
		funSetExpCode(code);
		break;
		

}
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
function funSetGLCode(code){

	$.ajax({
		type : "GET",
		url : getContextPath()+ "/loadAccontCodeAndName.html?accountCode=" + code,
		dataType : "json",
		success : function(response){ 
			if(response.strAccountCode!="Invalid Code")
	    	{
				$("#txtGLCode").val(response.strAccountCode);
				$("#lblGLName").text(response.strAccountName);
	    	}
	    	else
		    {
		    	alert("Invalid Account No");
		    	$("#txtGLCode").val("");
		    
		    	return false;
		    }
		},
		error : function(e){
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

function funSetExpCode(code){

	$.ajax({
		type : "GET",
		url : getContextPath()+ "/loadExpenseCode.html?expCode=" + code,
		
		dataType : "json",
		success : function(response){ 
			if(response.strExpCode!="Invalid Code")
	    	{
				$("#txtExpCode").val(response.strExpCode);
				$("#txtExpenseName").val(response.stnExpName);
				$("#txtExpSortName").val(response.strExpShortName);

				funSetGLCode(response.strGLCode);
				
	    	}
	    	else
		    {
		    	alert("Invalid Account No");
		    	$("#txtGLCode").val("");
		    
		    	return false;
		    }
		},
		error : function(e){
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





</script>

<body onload="funOnLoad();">
	<div id="formHeading">
		<label>Expense Master</label>
	</div>
	<s:form name="expform" method="POST" action="saveExpenseMaster.html?saddr=${urlHits}">

		<br />
		<br />
		<table class="masterTable">

			<tr>
				<td width="140px">Expense Code</td>
				<td><s:input id="txtExpCode" path="strExpCode"
						cssClass="searchTextBox" ondblclick="funHelp('expense')" /></td>
			</tr>
			<tr>
				<td><label>Expense Name</label></td>
				<td><s:input  type="text" id="txtExpenseName" 
						name="txtExpenseName" path="stnExpName" required="true"
						cssStyle="text-transform: uppercase;" cssClass="longTextBox"  /> <s:errors path="stnExpName"></s:errors></td>
			</tr>
			<tr>
				<td><label>Short Name </label></td>
				<td><s:input id="txtExpSortName" name="txtExpSortName"
						cssStyle="text-transform: uppercase;" path="strExpShortName" cssClass="longTextBox" autocomplete="off" /> </td>
			</tr>
			
				<tr>
				<td><label>GL Code </label></td>
				<td><s:input id="txtGLCode" path="strGLCode"
						cssClass="searchTextBox" ondblclick="funHelp('GLCode')" />
						<label id = "lblGLName"></label> </td>
			</tr>

		</table>
		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button" />
			 <input type="reset"
				value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>
	</s:form>

</body>
</html>