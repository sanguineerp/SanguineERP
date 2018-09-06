<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@	taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
	<script type="text/javascript">

$(document).ready(function() 
		{
			var startDate="${startDate}";
			var arr = startDate.split("/");
			Dat=arr[0]+"-"+arr[1]+"-"+arr[2];
			$("#txtFromDate").datepicker({ dateFormat: 'dd-mm-yy' });
			$("#txtFromDate").datepicker('setDate',Dat);
			$("#txtToDate").datepicker({ dateFormat: 'dd-mm-yy' });
			$("#txtToDate").datepicker('setDate', 'today');

			
				
			$(document).ajaxStart(function()
			{
			    $("#wait").css("display","block");
			});
			$(document).ajaxComplete(function()
			{
				$("#wait").css("display","none");
			});
			
			
			$("#btnExecute").click(function( event )
			{
				funLoadAMCList();					
			});
		});
		
		
		function funLoadAMCList()
		{
			var fromDate=$("#txtFromDate").val();
			var toDate=$("#txtToDate").val();
			var searchUrl="";
	    	var searchUrl=getContextPath()+"/loadAMCReport.html?fromDate="+fromDate+"&toDate="+toDate;
			$.ajax({
			        type: "GET",
			        url: searchUrl,
				    dataType: "json",
				    success: function(response)
				    {
				    	funFillTable(response);		       	

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
		
		function funFillTable(resp)
		{
			for(var i=0;i<resp.length;i++)
			{
			 	var data=resp[i];
				var table = document.getElementById("tblTranList");
			    var rowCount = table.rows.length;
			    var row = table.insertRow(rowCount);
			
			    row.insertCell(0).innerHTML= "<input id=\"cbSel."+(rowCount)+"\" size=\"3%\" type=\"checkbox\"  />";
			    row.insertCell(1).innerHTML= "<input name=\"StrInvCode["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"13%\" id=\"StrInvCode."+(rowCount)+"\" value='"+data.strCustomerName+"'>";
			    row.insertCell(2).innerHTML= "<input name=\"DteInvDate["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\"  style=\"text-align: right;\" size=\"34%\" id=\"DteInvDate."+(rowCount)+"\" value='"+data.dblLicenceAmt+"'>";
			    row.insertCell(3).innerHTML= "<input name=\"DteInvDate1["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"13%\" id=\"DteInvDate1."+(rowCount)+"\" value='"+data.dteInstallation+"'>";
			    row.insertCell(4).innerHTML= "<input name=\"DblSubTotalAmt["+(rowCount)+"]\" id=\"DblSubTotalAmt."+(rowCount)+"\" readonly=\"readonly\" style=\"text-align: right;\" size=\"13%\" class=\"Box\" value="+data.dteExpiry+">"; 
			    row.insertCell(5).innerHTML= "<input name=\"strSerialNo["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\"  style=\"text-align: right;\" size=\"6%\" id=\"strSerialNo."+(rowCount)+"\" value='"+data.dblAMCAmt+"'>";
		   }
		}
		
		
</script>
<body>
	<s:form name="AMCFlash" method="GET" action="" >
	<table class="transTable">
			<tr><th colspan="6"></th></tr>
				<tr>
				    <td><label id="lblFromDate">AMC From Date</label></td>
			        <td>
			            <s:input id="txtFromDate" name="fromDate" path="dteFromDate" cssClass="calenderTextBox"/>
			        	<s:errors path="dteFromDate"></s:errors>
			        </td>
			        <td><label id="lblToDate">AMC To Date</label></td>
			        <td>
			            <s:input id="txtToDate" name="toDate" path="dteToDate" cssClass="calenderTextBox"/>
			        	<s:errors path="dteToDate"></s:errors>
			        </td>
				</tr>
				<tr>
					<td colspan="4"><input id="btnExecute" type="button" class="form_button1"   value="EXECUTE"/></td>
				</tr>
			</table>
			
		<div id="divDocList" class="dynamicTableContainer"
			style="height: 400px;">
			<table style="width: 100%; border: #0F0;   overflow-x: scroll; overflow-y: scroll;"
				class="transTablex col15-center">
				<tr bgcolor="#72BEFC">
					<td width="3%">Select<input type="checkbox" id="chkALL" onclick="funCheckUncheck()" /></td>
					<td width="8%">Customer Name</td>
					<td width="9%"> Licence Amount</td>
					<td width="9%">Insatlation Date</td>
					<td width="8%">Expiry Date</td>
					<td width="6%">AMC Amount</td>
				</tr>
			</table>
			<div
				style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 330px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
					<table id="tblTranList"
					style="width: 100%; border: #0F0;  overflow-x: scroll; overflow-y: scroll;"
					class="transTablex col15-center">
					<tbody>
					<col style="width: 3%">
					<col style="width: 8%">
					<!--  COl1   -->
					<col style="width: 9%">
					<!--  COl2   -->
					<col style="width: 9%">
					<!--  COl3   -->
					<col style="width: 8%">
					<!--  COl4   -->
					<col style="width: 6%">
				
										
					</tbody>

				</table>
			</div>

		</div>
		
		<div id="wait" style="display:none;width:60px;height:60px;border:0px solid black;position:absolute;top:60%;left:55%;padding:2px;">
				<img src="../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif" width="60px" height="60px" />
			</div>
	</s:form>
</body>
</html>