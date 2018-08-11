<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

	<script>
	
	
			$(function(){
					var startDate="${startDate}";
					var arr = startDate.split("/");
					Dat=arr[0]+"-"+arr[1]+"-"+arr[2];
					$( "#txtFromDate" ).datepicker({ dateFormat: 'dd-mm-yy' });
					$("#txtFromDate" ).datepicker('setDate', Dat);
					$("#txtToDate").datepicker({ dateFormat: 'dd-mm-yy' });
					$("#txtToDate").datepicker('setDate', 'today');
					var glCode = $("#txtGLCode").val();
					
					$('#txtReportCode').blur(function() {
						var code = $('#txtReportCode').val();
						if(code.trim().length > 0 && code !="?" && code !="/")
						{
							funSetReportName(code);
						}
					});
					
					});
			
	
	function funSetData(code)
	{
		switch (fieldName) 
		{
		   case 'userDefinedReportCode':
			   funSetReportName(code);
		        break;
		        
		}
	}
	
	function funHelp(transactionName)
	{
		fieldName=transactionName;
		window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
    }
	
	
	function funSetReportName(code)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadUserDefinedReportMasterData.html?userDefinedCode=" + code,
			dataType : "json",
			success : function(response)
			{
				if(response.strReportId=="Invalid Code")
				{
				alert("Invlid Report Id");
				$("#txtReportCode").val("");
				$("#lblUserName").text("");
				}
				else
				{	
				$("#txtReportCode").val(response.strReportId);
				$("#lblUserName").text(response.strReportName);
				}
			},
			error : function(e){
			}
		});
	}
	
	</script>
</head>
	<body>
	<div id="formHeading">
		<label>User Defined Report Master</label>
	</div>
	<br/>
    <br/>
		<s:form id="frmUserDefinedReportProcess" method="POST" action="getUserDefinedReportProcess.html?saddr=${urlHits}">
			
		    <table class="masterTable" style="width: 95%;">
			    <tr>
			        <td ><label>Report ID</label></td>
			        <td><s:input id="txtReportCode" path="strReportId" ondblclick="funHelp('userDefinedReportCode');" class="searchTextBox"/></td>
			        <td><label id="lblUserName"></label></td>			    			    			    		    
			    </tr>
			    
			    <tr>
				<td>
					<label>From Date</label>
				</td>
				<td>
					<s:input colspan="3" type="text" id="txtFromDate" path="dteFDate" cssClass="calenderTextBox" />
				</td>
				
				<td>
					<label>To Date</label>
				</td>
				<td>
					<s:input type="text" id="txtToDate" path="dteTDate" cssClass="calenderTextBox" />
				</td>
			</tr>
			
			</table>
			<p align="center">
			<input id="btnSubmit" type="submit" value="Execute" class="form_button" />
			</p>
			</s:form>
			
			

</body>
</html>