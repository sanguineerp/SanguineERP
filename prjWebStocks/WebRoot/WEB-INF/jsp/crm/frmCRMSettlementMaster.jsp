<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<style>
.ui-autocomplete {
    max-height: 200px;
    overflow-y: auto;
    /* prevent horizontal scrollbar */
    overflow-x: hidden;
    /* add padding to account for vertical scrollbar */
    padding-right: 20px;
}
/* IE 6 doesn't support max-height
 * we use height instead, but this forces the menu to always be this tall
 */
* html .ui-autocomplete {
    height: 200px;
}
</style>
<script type="text/javascript">

	var settlemantWiseSeries='No';
	var clickCount =0.0;

	//Textfiled On blur geting data
	$(function() {
		
		$('#txtSettlementCode').blur(function() {
			var code = $('#txtSettlementCode').val();
			if(code.trim().length > 0 && code !="?" && code !="/")
			{
				funSetData(code);
			}
		});
		
		settlemantWiseSeries="${selltemetInv}";
		
		if( "${moduleNo}" == 1)
		{
			
		 	$('#txtInvSeriesChar').css('visibility','hidden');
		    $('#lblsettleMaster').css('visibility','hidden');
		
		}
	});



	/**
	* Reset The Group Name TextField
	**/
	function funResetFields()
	{
		$("#txtFactoryName").focus();
		
    }
	
	
		/**
		* Open Help
		**/
		function funHelp(transactionName)
		{	       
	       // window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
	       window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
	    }
		
		/**
		* Get and Set data from help file and load data Based on Selection Passing Value(Group Code)
		**/
		function funSetData(code)
		{
			$("#txtSettlementCode").val(code);
			var searchurl=getContextPath()+"/loadSettlementMasterData.html?code="+code;
			 $.ajax({
				        type: "GET",
				        url: searchurl,
				        dataType: "json",
				        success: function(response)
				        {
				        	if(response.strSettlementCode=='Invalid Code')
				        	{
				        		alert("Invalid Settlement Code");
				        		$("#txtSettlementCode").val('');
				        	}
				        	else
				        	{
					        	$("#txtSettlementDesc").val(response.strSettlementDesc);
					        	$("#cmbSettlementType").val(response.strSettlementType);
					        	$("#txtInvSeriesChar").val(response.strInvSeriesChar);
					        	
					        	if(response.strApplicable=='true')
				        		{
					        		$("#chkApplicable").prop('checked',true);
				        		}
					        	else
					        		$("#chkApplicable").prop('checked',false);
					        	$("#txtSettlementDesc").focus();
					        	
					        	
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
		
	
	
		/**
		* Success Message After Saving Record
		**/
		$(document).ready(function()
		{
			var message='';
			<%if (session.getAttribute("success") != null) 
			{
				if(session.getAttribute("successMessage") != null)
				{%>
					message='<%=session.getAttribute("successMessage").toString()%>';
				    <%
				    session.removeAttribute("successMessage");
				}
				boolean test = ((Boolean) session.getAttribute("success")).booleanValue();
				session.removeAttribute("success");
				if (test) 
				{
					%>alert("Data Saved \n\n"+message);<%
				}
			}%>
		});
		
		function funValidateFields()
		{
			if(clickCount==0){
				clickCount=clickCount+1;
			if(settlemantWiseSeries=='Yes')
			{
				
				if($("#txtInvSeriesChar").val().trim()=="")
				{
					alert("Please Fill Settlement Wise Series");
					return false;
				}
			}
			return true;
			}
			else
			{
				
				return false;
				
			}
	    }
</script>


</head>

<body >
	<div id="formHeading">
		<label>Settlement Master</label>
	</div>
	<s:form name="manufactureForm" method="POST" action="saveCRMSettlementMaster.html?saddr=${urlHits}">

		<br />
		<br />
		<table class="masterTable">

			<tr>
				<td width="150px">Settlement Code</td>
				<td><s:input id="txtSettlementCode" path="strSettlementCode"
						cssClass="searchTextBox jQKeyboard form-control"  ondblclick="funHelp('settlementMaster')" /></td>
			</tr>
			<tr>
				<td><label>Settlement Desc</label></td>
				<td><s:input colspan="3" type="text" id="txtSettlementDesc" 
						path="strSettlementDesc" required="true"
						cssStyle="text-transform: uppercase;" cssClass="longTextBox jQKeyboard form-control"  /> 
				</td>
			</tr>
			
			<tr>
				<td><label>Settlement Type</label></td>
				<td>
					<s:select id="cmbSettlementType" path="strSettlementType" cssClass="BoxW124px">
				    	<option selected="selected" value="Cash">Cash</option>
				    	<option value="Credit Card">Credit Card</option>
				    	<option value="Credit">Credit</option>
				    	<option value="Online Payment">Online Payment</option>
		         	</s:select>					
				</td>				
			</tr>
			
			<tr>
				<td><label>Applicable</label></td>
				<td><s:input colspan="1" type="checkbox" id="chkApplicable" 
						name="chkApplicable" path="strApplicable" value ="true" style="width:8%" />
			
			<td><label id="lblsettleMaster">Settlement Wise Invoice Series </label></td>
				 <td > 
				<s:select id="txtInvSeriesChar" path="strInvSeriesChar" items="${alphabetList}"   cssClass="BoxW124px">
				    
		         	</s:select>	
				</td>	
			</tr>
		</table>
		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3"  onclick="return funValidateFields()" class="form_button"/> 
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>
	</s:form>

</body>
</html>
