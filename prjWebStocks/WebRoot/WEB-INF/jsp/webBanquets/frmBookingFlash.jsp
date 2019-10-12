<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
var activeClick='';
$(document).ready(function() 
		{		
		
			$("#dteFromDate").datepicker({ dateFormat: 'dd-mm-yy' });
			$("#dteFromDate").datepicker('setDate','today');
			$("dteFromDate").datepicker();
			$("#dteToDate").datepicker({ dateFormat: 'dd-mm-yy' });
			$("#dteToDate" ).datepicker('setDate', 'today');
			$("#dteToDate").datepicker();
			
			var oldFrmDate=$('#dteFromDate').val().split('-');
			var oldToDate=$('#dteToDate').val().split('-');
			frmDte1=oldFrmDate[2]+"-"+oldFrmDate[1]+"-"+oldFrmDate[0];
			toDte1=oldToDate[2]+"-"+oldToDate[1]+"-"+oldToDate[0];
			
		   
			
			$(document).ajaxStart(function()
					{
					    $("#wait").css("display","block");
					});
					$(document).ajaxComplete(function()
					{
						$("#wait").css("display","none");
					});
           });
           
           
function funShowTableGUI(divID)
{
	hidReportName=divID;
	// for hide Table GUI
	document.all["divBookingDtl"].style.display = 'none';
    
	
    // for show Table GUI
	document.all[divID].style.display = 'block';
}
           
           
function funOnClckWaitingBtn( divId)
{
	
	activeClick=$('#btnWaitingList').val();
	funShowTableGUI(divId)
	var report=$('#btnWaitingList').val();
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var searchUrl=getContextPath()+"/loadBookingDetail.html?frmDte="+frmDte1+"&toDte="+toDte1+"&reportType="+report;
	$.ajax({
	        type: "GET",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funBookingDetail(response[0]);
		    	$("#txtTotValue").val("");
		    	$("#txtTaxTotValue").val(parseFloat(response[1]).toFixed(maxQuantityDecimalPlaceLimit));
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

function funOnClckBookingBtn( divId)
{
	activeClick=$('#btnConfirmBooking').val();
	funShowTableGUI(divId)
	var report=$('#btnConfirmBooking').val();
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var searchUrl=getContextPath()+"/loadBookingDetail.html?frmDte="+frmDte1+"&toDte="+toDte1+"&reportType="+report;
	$.ajax({
	        type: "GET",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funBookingDetail(response[0]);
		    	$("#txtTotValue").val("");
		    	$("#txtTaxTotValue").val(parseFloat(response[1]).toFixed(maxQuantityDecimalPlaceLimit));
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


function funOnClckProvisionalBtn( divId)
{
	activeClick=$('#btnProvisionalList').val();
	funShowTableGUI(divId)
	var report=$('#btnProvisionalList').val();
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var searchUrl=getContextPath()+"/loadBookingDetail.html?frmDte="+frmDte1+"&toDte="+toDte1+"&reportType="+report;
	$.ajax({
	        type: "GET",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funBookingDetail(response[0]);
		    	$("#txtTotValue").val("");
		    	$("#txtTaxTotValue").val(parseFloat(response[1]).toFixed(maxQuantityDecimalPlaceLimit));
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

function funOnClckCancelBtn( divId)
{
	activeClick=$('#btnCancelList').val();
	funShowTableGUI(divId)
	var report=$('#btnCancelList').val();
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var searchUrl=getContextPath()+"/loadBookingDetail.html?frmDte="+frmDte1+"&toDte="+toDte1+"&reportType="+report;
	$.ajax({
	        type: "GET",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funBookingDetail(response[0]);
		    	$("#txtTotValue").val("");
		    	$("#txtTaxTotValue").val(parseFloat(response[1]).toFixed(maxQuantityDecimalPlaceLimit));
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


function funBookingDetail(ProdDtl)
{
	$('#tblBookingDet tbody').empty();
	for(var i=0;i<ProdDtl.length;i++)
	{
	 var data=ProdDtl[i];
	 
    var table = document.getElementById("tblBookingDet");
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);
    var bookingNo=data.strBookingNo;
    
   // row.insertCell(0).innerHTML= "<input name=\"strBookingNo["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"50%\" id=\"strBookingNo."+(rowCount)+"\" value='"+data.strBookingNo+"'>";
   
    row.insertCell(0).innerHTML= "<input name=\"strBookingNo["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"50%\" id=\"strBookingNo."+(rowCount)+"\" value='"+bookingNo+"' onclick=\"funClick(this)\">";
   
    row.insertCell(1).innerHTML= "<input name=\"strBookingStatus["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" style=\"text-align: center;\" size=\"15%\" id=\"strBookingStatus."+(rowCount)+"\" value='"+data.strBookingStatus+"'>";
    row.insertCell(2).innerHTML= "<input name=\"dteBookingDate["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"50%\" id=\"strRoomDesc."+(rowCount)+"\" value='"+data.dteBookingDate+"'>";
    row.insertCell(3).innerHTML= "<input name=\"tmeFromTime["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"15%\" id=\"tmeFromTime."+(rowCount)+"\" value='"+data.tmeFromTime+"'>";
    row.insertCell(4).innerHTML= "<input name=\"tmeToTime["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\"  size=\"15%\" id=\"tmeToTime."+(rowCount)+"\" value='"+data.tmeToTime+"'>";
    row.insertCell(5).innerHTML= "<input name=\"strAreaCode["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\"  size=\"15%\" id=\"strAreaCode."+(rowCount)+"\" value='"+data.strAreaCode+"' >";
    row.insertCell(6).innerHTML= "<input name=\"strFunctionCode["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\"  size=\"15%\" id=\"strFunctionCode."+(rowCount)+"\" value='"+data.strFunctionCode+"'>";
    row.insertCell(7).innerHTML= "<input name=\"dblAmt["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" style=\"text-align: right;\" size=\"15%\" id=\"dblAmt."+(rowCount)+"\" value='"+data.dblAmt+"'>";
	} 
}


function funClick(obj)
{
	//var bookingNo=document.getElementById(""+obj.id+"").innerHTML;
	var bookingNo=obj.value;
	var dteFromDate=document.getElementById("dteFromDate").value;
	var dteToDate=document.getElementById("dteToDate").value;
	
	window.open(getContextPath()+"/rptOpenFunctionProspectus.html?bookingNo="+bookingNo);
	
}	

function funExportReport()
{
	var click=activeClick;
	var frmDte1=$('#dteFromDate').val();
	var toDte1=$('#dteToDate').val();
	if(hidReportName=='divBookingDtl')
	{
		window.location.href = getContextPath()+"/exportBookingFlashDtl.html?frmDte="+frmDte1+"&toDte="+toDte1+"&reportType="+click;
	}
}	




</script>


</head>
<body>
<div id="formHeading">
		<label>Banquet Flash </label>
	</div>
	
<s:form name="frmBookingFlash" method="GET" action="">
	<br />
	<table class="transTable">
	       <tr>
				<td><label>From Date</label></td>
				<td><s:input type="text" id="dteFromDate" path="dteFromDate" required="true" class="calenderTextBox" /></td>
				<td><label>To Date</label></td>
				<td><s:input type="text" id="dteToDate" path="dteToDate" required="true" class="calenderTextBox" /></td>
		        
		        <td colspan="9"><input id="btnExport" type="button" value="EXPORT"  class="form_button1" onclick="funExportReport()"/>
				<s:input type="hidden" id="hidReportName" path=""></s:input>	
				</td>
		   
		   </tr>
	</table>		
		<br/>	
			
	
	<div id="divBookingDtl" class="dynamicTableContainer"
			style="height: 400px;">
			<table style="width: 100%; border: #0F0; table-layout: fixed;"
				class="transTablex col15-center">
				<tr bgcolor="#72BEFC">
					<td width="2%">Booking No </td>
					<td width="2%">Booking Status</td>
					<td width="2%">Booking Date</td>
					<td width="2%">From Time</td>
					<td width="2%">To Time</td>
					<td width="2%">Area Code</td>
					<td width="2%">Function Code</td>
					<td width="2%">Amount</td>
					
					
					
				</tr>
			</table>
			<div
				style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 330px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblBookingDet"
					style="width: 100%; border: #0F0; table-layout: fixed;"
					class="transTablex col15-center">
					<tbody>
					<col style="width: 2%">
					<col style="width: 2%">
					<col style="width: 2.1%">
					<col style="width: 2%">
					<col style="width: 2%">
					<col style="width: 2%">
					<col style="width: 2%">
					<col style="width: 1.8%">
					
					</tbody>

				</table>
			</div>

		</div>
		
		
		<div id="divValueTotal"
			style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 25px; margin: auto; overflow-x: hidden; overflow-y: hidden; width: 94%;">
			<table id="tblTotalFlash" class="transTablex"
				style="width: 100%; font-size: 11px; font-weight: bold;">
				<tr style="margin-left: 28px">
					<td id="labld26" width="50%" align="right">Total Value</td>
					<td id="tdTotValue" width="20%" align="right"><input
						id="txtTotValue" style="width: 100%; text-align: right;"
						class="Box"></input></td>
					<td id="tdTaxTotValue" width="20%" align="right"><input
						id="txtTaxTotValue" style="width: 100%; text-align: right;"
						class="Box"></input></td>
					

				</tr>
			</table>
		</div>
		
									
	    
	 <div style=" border: 1px solid #ccc; display: block; height: 30px; margin: auto;  width: 95%;">
	 <table class="transTable" style="width:100%;height:30px; ">
		       <tr>
		       <td ><input id="btnConfirmBooking" type="button" value="Confirm Booking"  class="form_button"
		       style="background-size: 140px 24px; width: 140px;" onclick="funOnClckBookingBtn('divBookingDtl')" />
			   </td>
					
			   <td ><input colspan="3" id="btnWaitingList" type="button" value="Waiting List"  class="form_button" 
			   style="background-size: 140px 24px; width: 140px;" onclick="funOnClckWaitingBtn('divBookingDtl')" />
			   </td>
			   
			   <td ><input colspan="3" id="btnProvisionalList" type="button" value="Provisional List"  class="form_button" 
			   style="background-size: 140px 24px; width: 140px;" onclick="funOnClckProvisionalBtn('divBookingDtl')" />
			   </td>
			   
			   <td ><input colspan="3" id="btnCancelList" type="button" value="Cancel List"  class="form_button" 
			   style="background-size: 140px 24px; width: 140px;" onclick="funOnClckCancelBtn('divBookingDtl')" />
			   </td>	
	    </tr>
	 </table>
	</div>
		
</s:form>
</body>
</html>