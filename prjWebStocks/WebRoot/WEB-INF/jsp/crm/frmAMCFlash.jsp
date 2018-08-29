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
			$("#divValueTotal").hide();
			
			$("#btnExecute").click(function( event )
			{
				funFillAMC();					
			});
		});
		
		function funFillAMC()
		{			
			var fromDate=$("#txtFromDate").val();
			var toDate=$("#txtToDate").val();
	    	var searchUrl=getContextPath()+"/loadAMCReport.html?fDate="+fromDate+"&tDate="+toDate;
			$.ajax({
			        type: "GET",
			        url: searchUrl,
				    dataType: "json",
				    success: function(response)
				    {
				    	StkFlashData=response[0];
				    	showTable();		       	

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
					<td colspan="4"><input id="btnExecute" type="button" class="form_button1" value="EXECUTE"/></td>
				</tr>
			</table>
			
			<dl id="Searchresult" style="width: 95%; margin-left: 26px; overflow:auto;"></dl>
		<div id="Pagination" class="pagination" style="width: 80%;margin-left: 26px;">
		
		</div>
		
		<br>
		<br>
		
		
		<div id="divValueTotal"
			style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 40px; margin: auto; overflow-x: hidden; overflow-y: hidden; width: 95%;">
			<table id="tblTotalFlash" class="transTablex"
				style="width: 100%; font-size: 11px; font-weight: bold;">
				
				<tr style="margin-left: 28px">
				
					<td id="labld26" style="width:20%; text-align:right">Total</td>
					
					<td id="tdTotValue" style="width:10%; align:right">
						<input id="txtTotValue" style="width: 100%; text-align: right;" class="Box"></input>
					</td>

				</tr>
			</table>
			</div>
	</s:form>
</body>
</html>