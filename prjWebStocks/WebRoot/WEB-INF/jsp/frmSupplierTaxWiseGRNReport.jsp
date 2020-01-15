<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="sp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Supplier Tax Wise GRN Report</title>
</head>

<script type="text/javascript">

function funResetFields()
{
	$("#txtSuppCode").focus();
}
$(document).ready(function() 
		{		
		
			var startDate="${startDate}";
			var startDateOfMonth="${startDateOfMonth}";
			var arr = startDate.split("/");
			Date1=arr[0]+"-"+arr[1]+"-"+arr[2];
			$("#txtFromDate").datepicker({ dateFormat: 'dd-mm-yy' });
			$("#txtFromDate" ).datepicker('setDate', startDateOfMonth);
			$("#txtFromDate").datepicker();	
			
			 $("#txtToDate").datepicker({ dateFormat: 'dd-mm-yy' });
				$("#txtToDate" ).datepicker('setDate', 'today');
				$("#txtToDate").datepicker();	
				
			
				
		});
		
function formSubmit()
{
    var spFromDate=$("#txtFromDate").val().split('-');
	var spToDate=$("#txtToDate").val().split('-');
	var FromDate= new Date(spFromDate[2],spFromDate[1]-1,spFromDate[0]);
	var ToDate = new Date(spToDate[2],spToDate[1]-1,spToDate[0]);
	
	if(!fun_isDate($("#txtFromDate").val())) 
    {
		 alert('Invalid From Date');
		 $("#txtFromDate").focus();
		 return false;  
    }
    if(!fun_isDate($("#txtToDate").val())) 
    {
		 alert('Invalid To Date');
		 $("#txtToDate").focus();
		 return false;  
    }
	if(ToDate<FromDate)
	{
		 alert("To Date Should Not Be Less Than Form Date");
		 $("#txtToDate").focus();
		 return false;		    	
	}
    else
    {
    	document.forms["frmSupplierTaxWiseGRNReport"].submit();
    }
} 

function funHelp(transactionName)
{
	fieldName = transactionName;
//	window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
	window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
	
}


function funSetData(code)
    {
	$.ajax({
		type : "GET",
		url : getContextPath()+ "/loadSupplierMasterData.html?partyCode=" + code,
		dataType : "json",
		success : function(response){ 
			if(response.strDeptCode=='Invalid Code')
        	{
        		alert("Invalid Supplier Code");
        		$("#txtSuppCode").val('');
        	}
        	else
        	{
        		$("#txtSuppCode").val(response.strPCode);
        		$("#lblSuppCode").text(response.strPName);
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
</script>

<body>
<div id="formHeading">
		<label>Supplier Tax Wise GRN Report</label>
	</div>
		<s:form name="frmSupplierTaxWiseGRNReport" method="POST" action="rptfrmSupplierTaxWiseGRNReport.html" >
			<input type="hidden" value="${urlHits}" name="saddr">
            <br />
	   		<table class="transTable">
			    <tr>
					<td width="10%"><label>From Date :</label></td>
					<td width="10%" colspan="1"><s:input id="txtFromDate" path="dtFromDate" required="true" readonly="readonly" cssClass="calenderTextBox"/></td>
					<td width="20%"><label>To Date :</label></td>
					<td><s:input id="txtToDate" path="dtToDate" required="true" readonly="readonly" cssClass="calenderTextBox"/>
					</td>	
				</tr>
				
				<br>
				
				<tr>
				<td width="10%"><label >Supplier</label></td>
				   <td width="10%"><s:input id="txtSuppCode"  path="strSuppCode"  readonly="true" ondblclick="funHelp('suppcodeActive')" cssClass="searchTextBox jQKeyboard form-control"/></td>
				   <td> <label id="lblSuppCode" style="font-size: 12px;"></label> </td>
				</tr>
				
				
				
		     <tr>
					<td width="10%"><label>Report Type :</label></td>
					<td >
						<s:select id="cmbDocType" path="strDocType" cssClass="BoxW124px">
				    	
				    		<s:option value="XLS">EXCEL</s:option>
				    		<%-- <s:option value="HTML">HTML</s:option> --%>
				    		<%-- <s:option value="CSV">CSV</s:option> --%>
				    	</s:select>
					</td>
				</tr>
			</table>
			<br>
			<p align="center">
				 <input type="button" value="Submit" onclick="return formSubmit();" class="form_button" />
				 <input type="button" value="Reset" class="form_button" onclick="funResetFields()"/>			     
			</p>
			
			
		</s:form>
</body>
</html>