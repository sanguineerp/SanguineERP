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
var frmDte1="",toDte1="",maxQuantityDecimalPlaceLimit=2;

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
			
		    funOnClckSettlementWiseBtn('divSettlementWise');
			
			$(document).ajaxStart(function()
					{
					    $("#wait").css("display","block");
					});
					$(document).ajaxComplete(function()
					{
						$("#wait").css("display","none");
					});
           });
		

$(document).ready(function() 
		{
			$("#btnExecute").click(function( event )
			{
				
			});
				
			$(document).ajaxStart(function()
			{
			    $("#wait").css("display","block");
			});
			$(document).ajaxComplete(function()
			{
				$("#wait").css("display","none");
			});
		});
		
		
$(document).ready(function(){
	
	var pmsDate='<%=session.getAttribute("PMSDate").toString()%>';
	
	$("#dteFromDate").datepicker({
		dateFormat : 'dd-mm-yy'
	});
	$("#dteFromDate").datepicker('setDate', pmsDate);	
	
	
	$("#dteToDate").datepicker({
		dateFormat : 'dd-mm-yy'
	});
	$("#dteToDate").datepicker('setDate', pmsDate);	
});


function funShowTableGUI(divID)
{
	hidReportName=divID;
	// for hide Table GUI
	document.all["divSettlementWise"].style.display = 'none';
    document.all["divRevenueHeadWise"].style.display = 'none';
	document.all["divTaxWise"].style.display = 'none';
	document.all["divExpectedArrList"].style.display = 'none';
	document.all["divExpectedDeptList"].style.display = 'none';
	document.all["divCheckInList"].style.display = 'none';
	document.all["divCheckOutList"].style.display = 'none';
	document.all["divCancelationList"].style.display = 'none';
	document.all["divNoShowList"].style.display = 'none';
	document.all["divVoidBillList"].style.display = 'none';
	document.all["divPayment"].style.display = 'none';
	document.all["divBillPrinting"].style.display = 'none';
	
	
	// for show Table GUI
	document.all[divID].style.display = 'block';
}

function funOnClckSettlementWiseBtn( divId)
{
	funShowTableGUI(divId)
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var searchUrl=getContextPath()+"/loadSettlementWiseDtl.html?frmDte="+frmDte1+"&toDte="+toDte1;
	$.ajax({
	        type: "GET",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	
		    	funSettlementWiseDetail(response[0]);
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
function funSettlementWiseDetail(ProdDtl)
{
	$('#tblSettlementDet tbody').empty();
	for(var i=0;i<ProdDtl.length;i++)
	{
	 var data=ProdDtl[i];
	 
    var table = document.getElementById("tblSettlementDet");
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);
   
    row.insertCell(0).innerHTML= "<input name=\"strSettlementDesc["+(rowCount)+"]\" readonly=\"readonly\"  class=\"Box\" size=\"50%\" id=\"strSettlementDesc."+(rowCount)+"\" value='"+data.strSettlementDesc+"'>";		    
    row.insertCell(1).innerHTML= "<input name=\"dblSettlementAmt["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" style=\"text-align: right;\" size=\"49%\" id=\"dblSettlementAmt."+(rowCount)+"\" value='"+data.dblSettlementAmt+"'>";
    
     funApplyNumberValidation();
	}
}

function funApplyNumberValidation(){
	$(".numeric").numeric();
	$(".integer").numeric(false, function() { alert("Integers only"); this.value = ""; this.focus(); });
	$(".positive").numeric({ negative: false }, function() { alert("No negative values"); this.value = ""; this.focus(); });
	$(".positive-integer").numeric({ decimal: false, negative: false }, function() { alert("Positive integers only"); this.value = ""; this.focus(); });
    $(".decimal-places").numeric({ decimalPlaces: maxQuantityDecimalPlaceLimit, negative: false });
    $(".decimal-places-amt").numeric({ decimalPlaces: maxAmountDecimalPlaceLimit, negative: false });
}


function funOnClckRevenueHeadWiseBtn( divId)
{
	funShowTableGUI(divId)
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var searchUrl=getContextPath()+"/loadRevenueHeadWiseDtl.html?frmDte="+frmDte1+"&toDte="+toDte1;
	$.ajax({
	        type: "GET",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funRevenueHeadWiseDetail(response[0]);
		    	$("#txtTotValue").val(parseFloat(response[1]).toFixed(maxQuantityDecimalPlaceLimit));
		        $("#txtTaxTotValue").val(parseFloat(response[2]).toFixed(maxQuantityDecimalPlaceLimit));
		    	
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

function funRevenueHeadWiseDetail(ProdDtl)
{
	$('#tblRevenueHeadDet tbody').empty();
	for(var i=0;i<ProdDtl.length;i++)
	{
	 var data=ProdDtl[i];
	 data.dblAmount=parseFloat(data.dblAmount).toFixed(maxQuantityDecimalPlaceLimit);
	 data.dblTaxAmount=parseFloat(data.dblTaxAmount).toFixed(maxQuantityDecimalPlaceLimit);
    var table = document.getElementById("tblRevenueHeadDet");
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);
   
    row.insertCell(0).innerHTML= "<input name=\"strRevenueType["+(rowCount)+"]\" readonly=\"readonly\"  class=\"Box\" size=\"25%\" id=\"strRevenueType."+(rowCount)+"\" value='"+data.strRevenueType+"'>";		    
    row.insertCell(1).innerHTML= "<input name=\"dblAmount["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" style=\"text-align: right;\" size=\"57%\" id=\"dblAmount."+(rowCount)+"\" value='"+data.dblAmount+"'>";
    row.insertCell(2).innerHTML= "<input name=\"dblTaxAmountt["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\"  style=\"text-align: right;\" size=\"27%\" id=\"dblTaxAmount."+(rowCount)+"\" value='"+data.dblTaxAmount+"'>";
     funApplyNumberValidation();
	}	
}
function funOnClckTaxWiseBtn( divId)
{
	funShowTableGUI(divId)
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var searchUrl=getContextPath()+"/loadTaxWiseDtl.html?frmDte="+frmDte1+"&toDte="+toDte1;
	$.ajax({
	        type: "GET",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funTaxWiseDetail(response[0]);
		    	$("#txtTotValue").val(parseFloat(response[1]).toFixed(maxQuantityDecimalPlaceLimit));
		        $("#txtTaxTotValue").val(parseFloat(response[2]).toFixed(maxQuantityDecimalPlaceLimit));
		    	
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
function funTaxWiseDetail(ProdDtl)
{
	$('#tblTaxDet tbody').empty();
	for(var i=0;i<ProdDtl.length;i++)
	{
	 var data=ProdDtl[i];
	 
    var table = document.getElementById("tblTaxDet");
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);
   
    row.insertCell(0).innerHTML= "<input name=\"strTaxDesc["+(rowCount)+"]\" readonly=\"readonly\"  class=\"Box\" size=\"25%\" id=\"strTaxDesc."+(rowCount)+"\" value='"+data.strTaxDesc+"'>";		    
    row.insertCell(1).innerHTML= "<input name=\"dblTaxableAmt["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" style=\"text-align: right;\" size=\"57%\" id=\"dblTaxableAmt."+(rowCount)+"\" value='"+data.dblTaxableAmt+"'>";
    row.insertCell(2).innerHTML= "<input name=\"dblTaxAmt["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\"  style=\"text-align: right;\" size=\"27%\" id=\"dblTaxAmt."+(rowCount)+"\" value='"+data.dblTaxAmt+"'>";
    funApplyNumberValidation();
	}	
}

function funOnClckExpectedArrWiseBtn( divId)
{
	funShowTableGUI(divId)
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var searchUrl=getContextPath()+"/loadExpectedArrWiseDtl.html?frmDte="+frmDte1+"&toDte="+toDte1;
	$.ajax({
	        type: "GET",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funExpectedArrDetail(response[0]);
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
function funExpectedArrDetail(ProdDtl)
{
	$('#tblExpectedArrDet tbody').empty();
	for(var i=0;i<ProdDtl.length;i++)
	{
	 var data=ProdDtl[i];
	 data.dblTotalAmt=parseFloat(data.dblTotalAmt).toFixed(maxQuantityDecimalPlaceLimit);
     var table = document.getElementById("tblExpectedArrDet");
     var rowCount = table.rows.length;
     var row = table.insertRow(rowCount);
    
	    row.insertCell(0).innerHTML= "<input name=\"strReservationNo["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"25%\" id=\"strReservationNo."+(rowCount)+"\" value='"+data.strReservationNo+"'>";		    
	    row.insertCell(1).innerHTML= "<input name=\"dteReservationDate["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"20%\" id=\"dteReservationDate."+(rowCount)+"\" value='"+data.dteReservationDate+"'>";
	    row.insertCell(2).innerHTML= "<input name=\"strGuestName["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"15%\" id=\"strGuestName."+(rowCount)+"\" value='"+data.strGuestName+"'>";
	    row.insertCell(3).innerHTML= "<input name=\"dteDepartureDate["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"12%\" id=\"dteDepartureDatet."+(rowCount)+"\" value='"+data.dteDepartureDate+"'>";
	    row.insertCell(4).innerHTML= "<input name=\"dteArrivalDate["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"15%\" id=\"dteArrivalDate."+(rowCount)+"\" value='"+data.dteArrivalDate+"'>";
	    row.insertCell(5).innerHTML= "<input name=\"dblReceiptAmt["+(rowCount)+"]\" id=\"dblReceiptAmt."+(rowCount)+"\" readonly=\"readonly\"   size=\"14%\" class=\"Box\" value="+data.dblReceiptAmt+">";
	    
	funApplyNumberValidation();
	}
}



function funOnClckExpectedDeptWiseBtn( divId)
{
	funShowTableGUI(divId)
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var searchUrl=getContextPath()+"/loadExpectedDeptWiseDtl.html?frmDte="+frmDte1+"&toDte="+toDte1;
	$.ajax({
	        type: "GET",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funExpectedDeptDetail(response);
		    	$("#txtTotValue").val("");
		    	$("#txtTaxTotValue").val("");
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

function funExpectedDeptDetail(ProdDtl)
{
	$('#tblExpectedDeptDet tbody').empty();
	for(var i=0;i<ProdDtl.length;i++)
	{
	 var data=ProdDtl[i];
	 
    var table = document.getElementById("tblExpectedDeptDet");
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);
    var bookingType=data.strBookingType;
    var roomDesc=data.strRoomDesc;
    
    if(roomDesc.includes(" ") || bookingType.includes(" "))
	{
	roomDesc=roomDesc.replace(" ",'_');
	bookingType=bookingType.replace(" ",'_');

	}
	row.insertCell(0).innerHTML= "<input name=\"strCheckInNo["+(rowCount)+"]\" readonly=\"readonly\"  class=\"Box\" size=\"25%\" id=\"strCheckInNo."+(rowCount)+"\" value='"+data.strCheckInNo+"'>";		    
    row.insertCell(1).innerHTML= "<input name=\"strGuestName["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"25%\" id=\"strGuestName."+(rowCount)+"\" value='"+data.strGuestName+"'>";
    row.insertCell(2).innerHTML= "<input name=\"dteDepartureDate["+(rowCount)+"]\" id=\"dteDepartureDate."+(rowCount)+"\" readonly=\"readonly\"   size=\"14%\" class=\"Box\" value="+data.dteDepartureDate+">";
    row.insertCell(3).innerHTML= "<input name=\"strRoomDesc["+(rowCount)+"]\" id=\"strRoomDesc."+(rowCount)+"\" readonly=\"readonly\"   size=\"14%\" class=\"Box\" value="+roomDesc+">";
    row.insertCell(4).innerHTML= "<input name=\"strRoomType["+(rowCount)+"]\" readonly=\"readonly\"  class=\"Box\" size=\"25%\" id=\"strRoomType."+(rowCount)+"\" value='"+data.strRoomType+"'>";
    row.insertCell(5).innerHTML= "<input name=\"strBookingType["+(rowCount)+"]\" id=\"strBookingType."+(rowCount)+"\" readonly=\"readonly\"   size=\"14%\" class=\"Box\" value="+bookingType+">";		    	   
   
	} 
}


function funOnClckCheckInBtn( divId)
{
	funShowTableGUI(divId)
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var searchUrl=getContextPath()+"/loadCheckInDtl.html?frmDte="+frmDte1+"&toDte="+toDte1;
	$.ajax({
	        type: "GET",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funCheckInDetail(response);
		    	$("#txtTotValue").val("");
		    	$("#txtTaxTotValue").val("");
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
function funCheckInDetail(ProdDtl)
{
	$('#tblCheckInDet tbody').empty();
	for(var i=0;i<ProdDtl.length;i++)
	{
	 var data=ProdDtl[i];
	 
    var table = document.getElementById("tblCheckInDet");
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);
    var bookingType=data.strBookingType;
    var roomDesc=data.strRoomDesc;
    
    if(roomDesc.includes(" ") || bookingType.includes(" "))
	{
	roomDesc=roomDesc.replace(" ",'_');
	bookingType=bookingType.replace(" ",'_');

	}
	row.insertCell(0).innerHTML= "<input name=\"strCheckInNo["+(rowCount)+"]\" readonly=\"readonly\"  class=\"Box\" size=\"25%\" id=\"strCheckInNo."+(rowCount)+"\" value='"+data.strCheckInNo+"' onclick=\"funCheckInOpenSlip(this)\">";		    
    row.insertCell(1).innerHTML= "<input name=\"strGuestName["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"25%\" id=\"strGuestName."+(rowCount)+"\" value='"+data.strGuestName+"'>";
    row.insertCell(2).innerHTML= "<input name=\"dteCheckInDate["+(rowCount)+"]\" id=\"dteCheckInDate."+(rowCount)+"\" readonly=\"readonly\"   size=\"14%\" class=\"Box\" value="+data.dteCheckInDate+">";
    row.insertCell(3).innerHTML= "<input name=\"strRoomDesc["+(rowCount)+"]\" id=\"strRoomDesc."+(rowCount)+"\" readonly=\"readonly\"   size=\"14%\" class=\"Box\" value="+roomDesc+">";
    row.insertCell(4).innerHTML= "<input name=\"strRoomType["+(rowCount)+"]\" readonly=\"readonly\"  class=\"Box\" size=\"25%\" id=\"strRoomType."+(rowCount)+"\" value='"+data.strRoomType+"'>";
    row.insertCell(5).innerHTML= "<input name=\"strBookingType["+(rowCount)+"]\" id=\"strBookingType."+(rowCount)+"\" readonly=\"readonly\"   size=\"14%\" class=\"Box\" value="+bookingType+">";		    	   
    row.insertCell(6).innerHTML= "<input name=\"strArrivalTime["+(rowCount)+"]\" id=\"strArrivalTime."+(rowCount)+"\" readonly=\"readonly\" style=\"text-align: right;\" size=\"8%\" class=\"Box\" value="+data.strArrivalTime+">";
 
	} 
}

function funCheckInOpenSlip(data,against)
{
	
	var checkInNo = data.value;
	window.open(getContextPath()+"/rptCheckInSlip.html?checkInNo="+checkInNo+"&cmbAgainst="+against+ "");
}

function funOnClckCheckOutBtn( divId)
{
	funShowTableGUI(divId)
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var searchUrl=getContextPath()+"/loadCheckOutDtl.html?frmDte="+frmDte1+"&toDte="+toDte1;
	$.ajax({
	        type: "GET",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funCheckOutDetail(response[0]);
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

function funCheckOutDetail(ProdDtl)
{
	$('#tblCheckOutDet tbody').empty();
	for(var i=0;i<ProdDtl.length;i++)
	{
	 var data=ProdDtl[i];
	 
    var table = document.getElementById("tblCheckOutDet");
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);
    var roomDesc=data.strRoomDesc;
    var bookingType=data.strBookingType;
    if(roomDesc.includes(" ") || bookingType.includes(" "))
    	{
    	roomDesc=roomDesc=roomDesc.replace(" ",'_');
    	bookingType=bookingType.replace(" ",'_');
    	}
    
    row.insertCell(0).innerHTML='<input name=\"strBillNo['+(rowCount)+']\" readonly=\"readonly\" class=\"Box\" size=\"15%\" id=\"strBillNo.'+(rowCount)+'\" value='+data.strBillNo+'> </a> ';
	row.insertCell(1).innerHTML= "<input name=\"strGuestName["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"25%\" id=\"strGuestName."+(rowCount)+"\" value='"+data.strGuestName+"'>";
    row.insertCell(2).innerHTML= "<input name=\"dteDepartureDate["+(rowCount)+"]\" id=\"dteDepartureDate."+(rowCount)+"\" readonly=\"readonly\"   size=\"14%\" class=\"Box\" value="+data.dteDepartureDate+">";
    row.insertCell(3).innerHTML= "<input name=\"strRoomDesc["+(rowCount)+"]\" id=\"strRoomDesc."+(rowCount)+"\" readonly=\"readonly\"   size=\"14%\" class=\"Box\" value="+roomDesc+">";
    row.insertCell(4).innerHTML= "<input name=\"strRoomType["+(rowCount)+"]\" readonly=\"readonly\"  class=\"Box\" size=\"25%\" id=\"strRoomType."+(rowCount)+"\" value='"+data.strRoomType+"'>";
    row.insertCell(5).innerHTML= "<input name=\"strBookingType["+(rowCount)+"]\" id=\"strBookingType."+(rowCount)+"\" readonly=\"readonly\"   size=\"14%\" class=\"Box\" value="+data.strBookingType+">";		    	   
    row.insertCell(6).innerHTML= "<input name=\"dblGrandTotal["+(rowCount)+"]\" id=\"dblGrandTotal."+(rowCount)+"\" readonly=\"readonly\" style=\"text-align: right;\" size=\"14%\" class=\"Box\" value="+data.dblGrandTotal+">";
	} 
}




function funOnClckCancelationWiseBtn( divId)
{
	funShowTableGUI(divId)
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var searchUrl=getContextPath()+"/loadCancelationWiseDtl.html?frmDte="+frmDte1+"&toDte="+toDte1;
	$.ajax({
	        type: "GET",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funCancelationDetail(response);
		    	$("#txtTotValue").val("");
		    	$("#txtTaxTotValue").val("");
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
function funCancelationDetail(ProdDtl)
{
	$('#tblCancelationDet tbody').empty();
	for(var i=0;i<ProdDtl.length;i++)
	{
	 var data=ProdDtl[i];
	 
    var table = document.getElementById("tblCancelationDet");
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);
    var roomDesc=data.strRoomDesc;
    var reasonDesc=data.strReasonDesc;
    
    if(roomDesc.includes(" ") || reasonDesc.includes(" "))
    	{
    	roomDesc=roomDesc.replace(" ",'_');
    	reasonDesc=reasonDesc.replace(/ /ig,'_');
    	}
   
    row.insertCell(0).innerHTML= "<input name=\"strReservationNo["+(rowCount)+"]\" readonly=\"readonly\"  class=\"Box\" size=\"25%\" id=\"strReservationNo."+(rowCount)+"\" value='"+data.strReservationNo+"'>";		    
    row.insertCell(1).innerHTML= "<input name=\"strGuestName["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"25%\" id=\"strGuestName."+(rowCount)+"\" value='"+data.strGuestName+"'>";
    row.insertCell(2).innerHTML= "<input name=\"strBookingType["+(rowCount)+"]\" id=\"dteSRDate."+(rowCount)+"\" readonly=\"readonly\"   size=\"14%\" class=\"Box\" value="+data.strBookingType+">";
    row.insertCell(3).innerHTML= "<input name=\"strRoomType["+(rowCount)+"]\" id=\"strDCCode."+(rowCount)+"\" readonly=\"readonly\"   size=\"14%\" class=\"Box\" value="+data.strRoomType+">";
    row.insertCell(4).innerHTML= "<input name=\"dteReservationDate["+(rowCount)+"]\" readonly=\"readonly\"  class=\"Box\" size=\"25%\" id=\"dteReservationDate."+(rowCount)+"\" value='"+data.dteReservationDate+"'>";
    row.insertCell(5).innerHTML= "<input name=\"dteCancelDate["+(rowCount)+"]\" id=\"dteCancelDate."+(rowCount)+"\" readonly=\"readonly\"   size=\"14%\" class=\"Box\" value="+data.dteCancelDate+">";		    	   
    row.insertCell(6).innerHTML= "<input name=\"strRoomDesc["+(rowCount)+"]\" id=\"strRoomDesc."+(rowCount)+"\" readonly=\"readonly\" style=\"text-align: right;\" size=\"8%\" class=\"Box\" value="+roomDesc+">";
    row.insertCell(7).innerHTML= "<input name=\"strReasonDesc["+(rowCount)+"]\" id=\"strReasonDesc."+(rowCount)+"\" readonly=\"readonly\" style=\"text-align: right;\" size=\"12%\" class=\"Box\" value="+reasonDesc+">";
    row.insertCell(8).innerHTML= "<input name=\"strRemark["+(rowCount)+"]\" id=\"strRemark."+(rowCount)+"\" readonly=\"readonly\" style=\"text-align: right;\" size=\"12%\" class=\"Box\" value="+data.strRemark+">";
   
    
    funApplyNumberValidation();
	}
}
function funOnClckNoShowWiseBtn( divId)
{
	funShowTableGUI(divId)
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var searchUrl=getContextPath()+"/loadNoShowDtl.html?frmDte="+frmDte1+"&toDte="+toDte1;
	$.ajax({
	        type: "GET",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funNoShowDetail(response);
		    	$("#txtTotValue").val("");
		    	$("#txtTaxTotValue").val("");
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
function funNoShowDetail(ProdDtl)

{
	$('#tblNoShowDet tbody').empty();
	for(var i=0;i<ProdDtl.length;i++)
	{
	 var data=ProdDtl[i];
	 
    var table = document.getElementById("tblNoShowDet");
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);
   
    row.insertCell(0).innerHTML= "<input name=\"strGuestName["+(rowCount)+"]\" readonly=\"readonly\"  class=\"Box\" size=\"25%\" id=\"strGuestName."+(rowCount)+"\" value='"+data.strGuestName+"'>";		    
    row.insertCell(1).innerHTML= "<input name=\"strReservationNo["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"25%\" id=\"strSRCode."+(rowCount)+"\" value='"+data.strReservationNo+"'>";
    row.insertCell(2).innerHTML= "<input name=\"strNoOfRooms["+(rowCount)+"]\" id=\"dteSRDate."+(rowCount)+"\" readonly=\"readonly\"   size=\"14%\" class=\"Box\" value="+data.strNoOfRooms+">";
    row.insertCell(3).innerHTML= "<input name=\"dblReceiptAmt["+(rowCount)+"]\" id=\"strDCCode."+(rowCount)+"\" readonly=\"readonly\"  style=\"text-align: right;\"  size=\"24%\" class=\"Box\" value="+data.dblReceiptAmt+">";
}
}

function funOnClckVoidBillWiseBtn( divId)
{
	funShowTableGUI(divId)
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var searchUrl=getContextPath()+"/loadVoidBillDtl.html?frmDte="+frmDte1+"&toDte="+toDte1;
	$.ajax({
	        type: "GET",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funVoidBillDetail(response);
		    	$("#txtTotValue").val("");
		    	$("#txtTaxTotValue").val("");
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
function funVoidBillDetail(ProdDtl)
{
	$('#tblVoidBillDet tbody').empty();
	for(var i=0;i<ProdDtl.length;i++)
	{
	 var data=ProdDtl[i];
	 data.dblTotalAmt=parseFloat(data.dblTotalAmt).toFixed(maxQuantityDecimalPlaceLimit);
     var table = document.getElementById("tblVoidBillDet");
     var rowCount = table.rows.length;
     var row = table.insertRow(rowCount);
     var  reasonDesc=data.strReasonDesc;
     
     if(reasonDesc.includes(" "))
 	 {
     reasonDesc=reasonDesc.replace(/ /ig,'_');
 	 }
        row.insertCell(0).innerHTML= "<input name=\"strBillNo["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"12%\" id=\"strBillNo."+(rowCount)+"\" value='"+data.strBillNo+"'>";		    
	    row.insertCell(1).innerHTML= "<input name=\"dteBillDate["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"9%\" id=\"dteBillDate."+(rowCount)+"\" value='"+data.dteBillDate+"'>";
	    row.insertCell(2).innerHTML= "<input name=\"strGuestName["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"40%\" id=\"strAgainst."+(rowCount)+"\" value='"+data.strGuestName+"'>";
	    row.insertCell(3).innerHTML= "<input name=\"strRoomDesc["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"05%\" id=\"strGuestName."+(rowCount)+"\" value='"+data.strRoomDesc+"'>";
	    row.insertCell(4).innerHTML= "<input name=\"strPerticular["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"50%\" id=\"strRoomDesc."+(rowCount)+"\" value='"+data.strPerticular+"'>";
	    row.insertCell(5).innerHTML= "<input name=\"dblVoidDebitAmt["+(rowCount)+"]\" readonly=\"readonly\"  style=\"text-align: right;\" size=\"8%\" class=\"Box\" value="+data.dblVoidDebitAmt+">";
	    row.insertCell(6).innerHTML= "<input name=\"strReasonDesc["+(rowCount)+"]\" readonly=\"readonly\"  style=\"text-align: left;\" size=\"18%\" class=\"Box\" value="+data.strReasonDesc+">";
	    row.insertCell(7).innerHTML= "<input name=\"strRemark["+(rowCount)+"]\" readonly=\"readonly\"  style=\"text-align: left;\" size=\"15%\" class=\"Box\" value="+data.strRemark+">";
	    row.insertCell(8).innerHTML= "<input name=\"strVoidType["+(rowCount)+"]\" readonly=\"readonly\"  style=\"text-align: left;\" size=\"7%\" class=\"Box\" value="+data.strVoidType+">";
	    row.insertCell(9).innerHTML= "<input name=\"strVoidUser["+(rowCount)+"]\" readonly=\"readonly\"  style=\"text-align: left;\" size=\"10%\" class=\"Box\" value="+data.strVoidUser+">";
	    
	   
	    
	    
	    funApplyNumberValidation();
	}
}
function funSetPaymentDetail(ProdDtl)
{
	$('#tblPayment tbody').empty();
	for(var i=0;i<ProdDtl.length;i++)
	{
	 var data=ProdDtl[i];
	 data.dblTotalAmt=parseFloat(data.dblTotalAmt).toFixed(maxQuantityDecimalPlaceLimit);
     var table = document.getElementById("tblPayment");
     var rowCount = table.rows.length;
     var row = table.insertRow(rowCount);
     
     var receiptNo = data.strReceiptNo;
     var date = data.dteReceiptDate;
     var guestName = data.strGuestName;
     var against = data.strType;
     var settlement = data.strSettlement;
     var amt = data.dblAmount;
     
    
        row.insertCell(0).innerHTML= "<input name=\"strReceiptNo["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"15%\" id=\"strBillNo."+(rowCount)+"\" value='"+receiptNo+"' onclick=\"funOpenSlip(this,'"+against+"')\" >";		    
	    row.insertCell(1).innerHTML= "<input name=\"dteReceiptDate["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"10%\" id=\"dteBillDate."+(rowCount)+"\" value='"+date+"'>";
	    row.insertCell(2).innerHTML= "<input name=\"strGuestName["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"40%\" id=\"strAgainst."+(rowCount)+"\" value='"+guestName+"'>";
	    row.insertCell(3).innerHTML= "<input name=\"strType["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"07%\" id=\"strGuestName."+(rowCount)+"\" value='"+against+"'>";
	    row.insertCell(4).innerHTML= "<input name=\"strSettlement["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"50%\" id=\"strRoomDesc."+(rowCount)+"\" value='"+settlement+"'>";
	    row.insertCell(5).innerHTML= "<input name=\"dblAmount["+(rowCount)+"]\" readonly=\"readonly\"  style=\"text-align: right;\" size=\"17%\" class=\"Box\" value="+amt+">";
	    
	    
	    
	    funApplyNumberValidation();
	}
}

function funOpenSlip(data,against)
{
	var receiptNumber = data.value;
	window.open(getContextPath()+"/rptReservationPaymentRecipt.html?reciptNo="+receiptNumber+"&checkAgainst="+against+ "");
}

function funSetBillPrintingDetail(ProdDtl)
{
	$('#tblBillPrinting tbody').empty();
	for(var i=0;i<ProdDtl.length;i++)
	{
	 var data=ProdDtl[i];
	 data.dblTotalAmt=parseFloat(data.dblTotalAmt).toFixed(maxQuantityDecimalPlaceLimit);
     var table = document.getElementById("tblBillPrinting");
     var rowCount = table.rows.length;
     var row = table.insertRow(rowCount);
     
     var dblDiscount = data.dblDiscount;
     dblDiscount= dblDiscount.toFixed(2);
     var dblGrndTotal = data.dblGrndTotal;
     dblGrndTotal=dblGrndTotal.toFixed(2);
     var dblTaxAmount = data.dblTaxAmount;
     dblTaxAmount=dblTaxAmount.toFixed(2);
     var dteBillDate = data.dteBillDate;
     var strBillNo = data.strBillNo;
     var strCheckInNo = data.strCheckInNo;
     var strGuestName = data.strGuestName;
     var strRoomDesc = data.strRoomDesc;
     var dblAdvanceAmount = data.dblAdvanceAmount
     dblAdvanceAmount=dblAdvanceAmount.toFixed(2);
     var perticular = data.strPerticular;
     
     	row.insertCell(0).innerHTML= "<input name=\"strBillNo["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"50%\" id=\"strBillNo."+(rowCount)+"\" value='"+strBillNo+"' onclick=\"funOpenBillPrintingSlip(this,'"+perticular+"')\">";
	    row.insertCell(1).innerHTML= "<input name=\"dteBillDate["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" style=\"text-align: center;\" size=\"15%\" id=\"dteBillDate."+(rowCount)+"\" value='"+dteBillDate+"'>";
	    row.insertCell(2).innerHTML= "<input name=\"strRoomDesc["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"50%\" id=\"strRoomDesc."+(rowCount)+"\" value='"+strRoomDesc+"'>";
	    row.insertCell(3).innerHTML= "<input name=\"strGuestName["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" size=\"50%\" id=\"strGuestName."+(rowCount)+"\" value='"+strGuestName+"'>";
	    row.insertCell(4).innerHTML= "<input name=\"dblGrndTotal["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" style=\"text-align: right;\" size=\"15%\" id=\"dblGrndTotal."+(rowCount)+"\" value='"+dblGrndTotal+"'>";
	    row.insertCell(5).innerHTML= "<input name=\"dblDiscount["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" style=\"text-align: right;\" size=\"15%\" id=\"dblDiscount."+(rowCount)+"\" value='"+dblDiscount+"' >";
	    row.insertCell(6).innerHTML= "<input name=\"dblTaxAmount["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" style=\"text-align: right;\" size=\"15%\" id=\"dblTaxAmount."+(rowCount)+"\" value='"+dblTaxAmount+"'>";
	    row.insertCell(7).innerHTML= "<input name=\"dblAdvanceAmount["+(rowCount)+"]\" readonly=\"readonly\" class=\"Box\" style=\"text-align: right;\" size=\"15%\" id=\"dblAdvanceAmount."+(rowCount)+"\" value='"+dblAdvanceAmount+"'>";
	   
	    
	    
	    
	    
	    
	    
	    funApplyNumberValidation();
	}
}
function funOpenBillPrintingSlip(data,pericular)
{
	var receiptNumber = data.value;
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	window.open(getContextPath()+"/rptBillPrinting.html?fromDate="+frmDte1+"&toDate="+toDte1+"&billNo="+receiptNumber+"&strSelectBill="+pericular+ "");
}
function funExportReport()
	{
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	 if(hidReportName=='divSettlementWise')
	 {
		window.location.href = getContextPath()+"/exportSettlementWisePMSSalesFlash.html?frmDte="+frmDte1+"&toDte="+toDte1;
	 }
	else if(hidReportName=='divRevenueHeadWise')
	 {
		window.location.href = getContextPath()+"/exportRevenueHeadWisePMSSalesFlash.html?frmDte="+frmDte1+"&toDte="+toDte1;
	 }
	else if(hidReportName=='divTaxWise')
	 {
		window.location.href = getContextPath()+"/exportTaxWisePMSSalesFlash.html?frmDte="+frmDte1+"&toDte="+toDte1;
	 }
	else if(hidReportName=='divExpectedArrList')
	 {
		window.location.href = getContextPath()+"/exportExpectedArrWisePMSSalesFlash.html?frmDte="+frmDte1+"&toDte="+toDte1;
	 }
	else if(hidReportName=='divExpectedDeptList')
	 {
		window.location.href = getContextPath()+"/exportExpectedDeptWisePMSSalesFlash.html?frmDte="+frmDte1+"&toDte="+toDte1;
	 }
	else if(hidReportName=='divCheckInList')
	 {
		window.location.href = getContextPath()+"/exportCheckInWisePMSSalesFlash.html?frmDte="+frmDte1+"&toDte="+toDte1;
	 }
	else if(hidReportName=='divCheckOutList')
	 {
		window.location.href = getContextPath()+"/exportCheckOutWisePMSSalesFlash.html?frmDte="+frmDte1+"&toDte="+toDte1;
	 }
	else if(hidReportName=='divCancelationList')
	 {
		window.location.href = getContextPath()+"/exportCancelationWisePMSSalesFlash.html?frmDte="+frmDte1+"&toDte="+toDte1;
	 }
	else if(hidReportName=='divNoShowList')
	 {
		window.location.href = getContextPath()+"/exportNoShowWiseWisePMSSalesFlash.html?frmDte="+frmDte1+"&toDte="+toDte1;
	 }
	else if(hidReportName=='divVoidBillList')
	 {
		window.location.href = getContextPath()+"/exportVoidBillWisePMSSalesFlash.html?frmDte="+frmDte1+"&toDte="+toDte1;
	 }
	else if(hidReportName=='divPayment')
	 {
		window.location.href = getContextPath()+"/exportPMSPaymentFlash.html?frmDte="+frmDte1+"&toDte="+toDte1;
	 }
	else if(hidReportName=='divBillPrinting')
	 {
		window.location.href = getContextPath()+"/exportPMSBillPrinting.html?frmDte="+frmDte1+"&toDte="+toDte1;
	 }
	
	
	
  }	

function funGetBillPerticular(billNo)
{
	var searchurl=getContextPath()+"/loadPerticulars.html?billNo="+billNo;
	
	$.ajax({
        type: "GET",
	    url: searchurl,
    	dataType: "json",
    
    	success: function (response) {
    		strPerticulars=response.strBillPerticular;
    	},
        error: function(jqXHR, exception)
   		 {
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

function funOnClickPayment( divId)
{
	funShowTableGUI(divId)
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var searchUrl=getContextPath()+"/loadPaymentForSalesFlash.html?frmDte="+frmDte1+"&toDte="+toDte1;
	$.ajax({
	        type: "GET",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funSetPaymentDetail(response);
		    	funCalculateTotal(response)
		    	
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

function funCalculateTotal (data)
{
	var totalAmt = 0;
	for(var i=0;i<data.length;i++)
	{
		var data1=data[i];
		totalAmt = totalAmt+data1.dblAmount;
		
	}
	
	$("#txtTotValue").val(totalAmt);
	$("#txtTaxTotValue").val("");
	
}

function funOnClckBillPrinting( divId)
{
	funShowTableGUI(divId)
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var searchUrl=getContextPath()+"/loadBillPrintingForSalesFlash.html?frmDte="+frmDte1+"&toDte="+toDte1;
	$.ajax({
	        type: "GET",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funSetBillPrintingDetail(response);
		    	funCalculateBillPrintingTotal(response)
		    	
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

function funCalculateBillPrintingTotal(data)
{
	var totalAmt = 0;
	var totalTaxamt = 0;
	for(var i=0;i<data.length;i++)
	{
		var data1=data[i];
		totalAmt = totalAmt+data1.dblGrndTotal;
		totalTaxamt = totalTaxamt+data1.dblTaxAmount;
		
	}
	totalTaxamt = totalTaxamt.toFixed(2);
	totalAmt = totalAmt.toFixed(2);
	$("#txtTotValue").val(totalAmt);
	$("#txtTaxTotValue").val(totalTaxamt);
	
	
}
function funClick(obj)
{
	var index=obj.parentNode.parentNode.rowIndex;
	var table1=document.getElementById("tblCheckOutDet");
	var indexData=table1.rows[index];
	var billNo=indexData.cells[0].firstElementChild.firstElementChild.defaultValue;
	var frmDte1=$('#dteFromDate').val();
    var toDte1=$('#dteToDate').val();
	var strSelectBill="";
	funGetBillPerticular(billNo);
    window.open(getContextPath()+"/rptBillPrinting.html?fromDate="+frmDte1+"&toDate="+toDte1+"&billNo="+billNo+"&strSelectBill="+strSelectBill+"");
	
}	














</script>
</head>


<body onload="funOnLoad();">
<div id="formHeading">
		<label>Sales Flash </label>
	</div>
	<s:form name="frmPMSSalesFlash" method="GET" action="">
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
		<div id="divSettlementWise" class="dynamicTableContainer"
			style="height: 400px;">
			<table style="width: 100%; border: #0F0; table-layout: fixed;"
				class="transTablex col15-center">
				<tr bgcolor="#72BEFC">
					<td width="8%">Settlement Type </td>
					<td width="6.1%">Settlement Amount</td>
					
			     </tr>
			</table>
			<div
				style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 330px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblSettlementDet"
					style="width: 100%; border: #0F0; table-layout: fixed;"
					class="transTablex col15-center">
					<tbody>
					<col style="width: 10%">
					<col style="width: 6%">
					
					</tbody>

				</table>
			</div>

		</div>
		
		<div id="divRevenueHeadWise" class="dynamicTableContainer"
			style="height: 400px;">
			<table style="width: 100%; border: #0F0; table-layout: fixed;"
				class="transTablex col15-center">
				<tr bgcolor="#72BEFC">
					<td width="7.4%">Revenue Type</td>
					<td width="9.3%">Amount </td>
					<td width="4.5%">Tax Amount</td>

				</tr>
			</table>
			<div
				style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 330px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblRevenueHeadDet"
					style="width: 100%; border: #0F0; table-layout: fixed;"
					class="transTablex col15-center">
					<tbody>
					<col style="width: 7.2%">
					<col style="width: 9.2%">
					<col style="width: 4.5%">
					
				
					</tbody>

				</table>
			</div>

		</div>
		
		<div id="divTaxWise" class="dynamicTableContainer"
			style="height: 400px;">
			<table style="width: 100%; border: #0F0; table-layout: fixed;"
				class="transTablex col15-center">
				<tr bgcolor="#72BEFC">
					<td width="7.4%">Tax Description</td>
					<td width="9.3%">Taxable Amount </td>
					<td width="4.5%">Tax Amount</td>
					
				</tr>
			</table>
			<div
				style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 330px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblTaxDet"
					style="width: 100%; border: #0F0; table-layout: fixed;"
					class="transTablex col15-center">
					<tbody>
					<col style="width: 7.2%">
					<col style="width: 9.2%">
					<col style="width: 4.5%">
					

					</tbody>

				</table>
			</div>

		</div>
		
		<div id="divExpectedArrList" class="dynamicTableContainer"
			style="height: 400px;">
			<table style="width: 100%; border: #0F0; table-layout: fixed;"
				class="transTablex col15-center">
				<tr bgcolor="#72BEFC">
					<td width="7.4%">Reservation No</td>
					<td width="4.5%">Reservation Date </td>
					<td width="9.3%">Guest Name</td>
					<td width="4.8%">Departure Date</td>
					<td width="7.6%">Arrival Date</td>
					<td width="5.1%">Receipt Amount</td>
					
				</tr>
			</table>
			<div
				style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 330px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblExpectedArrDet"
					style="width: 100%; border: #0F0; table-layout: fixed;"
					class="transTablex col15-center">
					<tbody>
					<col style="width: 7.2%">
					<col style="width: 4.5%">
					<col style="width: 9.2%">
					<col style="width: 4.6%">
					<col style="width: 7.6%">
					<col style="width: 5%">
					

					</tbody>

				</table>
			</div>

		</div>
		
		
		<div id="divExpectedDeptList" class="dynamicTableContainer"
			style="height: 400px;">
			<table style="width: 100%; border: #0F0; table-layout: fixed;"
				class="transTablex col15-center">
				<tr bgcolor="#72BEFC">
					<td width="7.4%">Check In No </td>
					<td width="10%">Guest Name</td>
					<td width="6.1%">Departure Date</td>
					<td width="5%">Room Description</td>
					<td width="4.5%">Room Type</td>
					<td width="4.5%">Booking Type</td>
				
					
					
				</tr>
			</table>
			<div
				style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 330px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblExpectedDeptDet"
					style="width: 100%; border: #0F0; table-layout: fixed;"
					class="transTablex col15-center">
					<tbody>
					<col style="width: 7.2%">
					<col style="width: 10%">
					<col style="width: 6%">
					<col style="width: 5%">
					<col style="width: 4.5%">
					<col style="width: 4.5%">
					
					
					</tbody>

				</table>
			</div>

		</div>
		
		
		<div id="divCheckInList" class="dynamicTableContainer"
			style="height: 400px;">
			<table style="width: 100%; border: #0F0; table-layout: fixed;"
				class="transTablex col15-center">
				<tr bgcolor="#72BEFC">
					<td width="7.4%">Check In No </td>
					<td width="10%">Guest Name</td>
					<td width="6.1%">CheckIn Date</td>
					<td width="5%">Room Description</td>
					<td width="4.5%">Room Type</td>
					<td width="4.5%">Booking Type</td>
					<td width="5.1%">Arrival Time</td>
					
					
				</tr>
			</table>
			<div
				style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 330px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblCheckInDet"
					style="width: 100%; border: #0F0; table-layout: fixed;"
					class="transTablex col15-center">
					<tbody>
					<col style="width: 7.2%">
					<col style="width: 10%">
					<col style="width: 6%">
					<col style="width: 5%">
					<col style="width: 4.5%">
					<col style="width: 4.5%">
					<col style="width: 5%">
					
					</tbody>

				</table>
			</div>

		</div>
		
		<div id="divCheckOutList" class="dynamicTableContainer"
			style="height: 400px;">
			<table style="width: 100%; border: #0F0; table-layout: fixed;"
				class="transTablex col15-center">
				<tr bgcolor="#72BEFC">
					<td width="7.4%">Bill No </td>
					<td width="10%">Guest Name</td>
					<td width="6.1%">Departure Date</td>
					<td width="5%">Room Description</td>
					<td width="4.5%">Room Type</td>
					<td width="4.5%">Booking Type</td>
					<td width="5.1%">Grand Total</td>
					
					
				</tr>
			</table>
			<div
				style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 330px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblCheckOutDet"
					style="width: 100%; border: #0F0; table-layout: fixed;"
					class="transTablex col15-center">
					<tbody>
					<col style="width: 7.2%">
					<col style="width: 10%">
					<col style="width: 6%">
					<col style="width: 5%">
					<col style="width: 4.5%">
					<col style="width: 4.5%">
					<col style="width: 5%">
					
					</tbody>

				</table>
			</div>

		</div>
		
		
		
		
		<div id="divCancelationList" class="dynamicTableContainer"
			style="height: 400px;">
			<table style="width: 100%; border: #0F0; table-layout: fixed;"
				class="transTablex col15-center">
				<tr bgcolor="#72BEFC">
					<td width="7.4%">Reservation No </td>
					<td width="10%">Guest Name</td>
					<td width="6.1%">Booking Type</td>
					<td width="5%">Room Type</td>
					<td width="4.5%">Reservation Date</td>
					<td width="4.5%">Cancel Date</td>
					<td width="5.1%">Room Description</td>
					<td width="8.1%">Reason</td>
					<td width="7.1%">Remark</td>
					
				</tr>
			</table>
			<div
				style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 330px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblCancelationDet"
					style="width: 100%; border: #0F0; table-layout: fixed;"
					class="transTablex col15-center">
					<tbody>
					<col style="width: 7.2%">
					<col style="width: 10%">
					<col style="width: 6%">
					<col style="width: 5%">
					<col style="width: 4.5%">
					<col style="width: 4.5%">
					<col style="width: 5%">
					<col style="width: 8%">
					<col style="width: 7%">
					</tbody>

				</table>
			</div>

		</div>
		
		<div id="divNoShowList" class="dynamicTableContainer"
			style="height: 400px;">
			<table style="width: 100%; border: #0F0; table-layout: fixed;"
				class="transTablex col15-center">
				<tr bgcolor="#72BEFC">
					<td width="7.4%">Guest Name</td>
					<td width="6.1%">Reservation No</td>
					<td width="4.5%">No Of Rooms</td>
					<td width="4.8%">Payment</td>
					
				</tr>
			</table>
			<div
				style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 330px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblNoShowDet"
					style="width: 100%; border: #0F0; table-layout: fixed;"
					class="transTablex col15-center">
					<tbody>
					<col style="width: 7.2%">
					<col style="width: 7%">
					<col style="width: 4.5%">
					<col style="width: 4.6%">
					

					</tbody>

				</table>
			</div>

		</div>
		
		<div id="divVoidBillList" class="dynamicTableContainer"
			style="height: 400px;">
			<table style="width: 100%; border: #0F0; table-layout: fixed;"
				class="transTablex col15-center">
				<tr bgcolor="#72BEFC">
					<td width="4%">Bill No</td>
					<td width="3.5%">Bil Date </td>
					<td width="6%">Guest Name</td>
					<td width="3%">Room Desc</td>
					<td width="8.8%">Particular</td>
					<td width="3.6%">Amount</td>
					<td width="5.1%">Reason</td>
					<td width="4.6%">Remark</td>
					<td width="4.7%">Void Type</td>
					<td width="4.7%">Void User</td>
				</tr>
			</table>
			<div
				style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 330px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblVoidBillDet"
					style="width: 100%; border: #0F0; table-layout: fixed;"
					class="transTablex col15-center">
					<tbody>
					<col style="width: 5%">
					<col style="width: 4%">
					<col style="width: 7.8%">
					<col style="width: 4%">
					<col style="width: 10.5%">
					<col style="width: 3.5%">
					<col style="width: 7.1%" >
					<col style="width: 5.1%">
					<col style="width: 5.1%">
					<col style="width: 6%">
					

					</tbody>

				</table>
			</div>

		</div>
		
		
		
		<div id="divPayment" class="dynamicTableContainer"
			style="height: 400px;">
			<table style="width: 100%; border: #0F0; table-layout: fixed;"
				class="transTablex col15-center">
				<tr bgcolor="#72BEFC">
					<td width="2%">Receipt No </td>
					<td width="2%">Receipt Date</td>
					<td width="4%">Name</td>
					<td width="2%">Type</td>
					<td width="2%">Settlement</td>
					<td width="2%">Amount</td>
					
					
					
				</tr>
			</table>
			<div
				style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 330px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblPayment"
					style="width: 100%; border: #0F0; table-layout: fixed;"
					class="transTablex col15-center">
					<tbody>
					<col style="width: 2%">
					<col style="width: 2%">
					<col style="width: 4%">
					<col style="width: 2%">
					<col style="width: 2%">
					<col style="width: 2%">
					
					</tbody>

				</table>
			</div>

		</div>
		
		<div id="divBillPrinting" class="dynamicTableContainer"
			style="height: 400px;">
			<table style="width: 100%; border: #0F0; table-layout: fixed;"
				class="transTablex col15-center">
				<tr bgcolor="#72BEFC">
					<td width="2%">Bill No </td>
					<td width="2%">Bill Date</td>
					<td width="2%">Room No</td>
					<td width="3%">Guest Name</td>
					<td width="2%">Bill Amount</td>
					<td width="2%">Discount</td>
					<td width="2%">Tax Amount</td>
					<td width="2%">Advance Amount</td>
					
					
					
				</tr>
			</table>
			<div
				style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 330px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblBillPrinting"
					style="width: 100%; border: #0F0; table-layout: fixed;"
					class="transTablex col15-center">
					<tbody>
					<col style="width: 2%">
					<col style="width: 2%">
					<col style="width: 2%">
					<col style="width: 3%">
					<col style="width: 2%">
					<col style="width: 2%">
					<col style="width: 2%">
					<col style="width: 2%">
					
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
		

		<div style=" border: 1px solid #ccc; display: block; height: 30px; margin: auto;  width: 94%;">
		<table class="transTable" style="width:100%;height:30px; ">
          <tr>
			<td ><input id="btnSettlmentWise" type="button" 
			class="form_button" value="Settlement Wise" onclick="funOnClckSettlementWiseBtn('divSettlementWise')" style="background-size: 140px 24px; width: 150px;" /></td>
			
			<td colspan="3"><input id="btnRevenueWise" type="button" 
			class="form_button"  value="Revenue Head Wise" onclick="funOnClckRevenueHeadWiseBtn('divRevenueHeadWise')"  style="background-size: 150px 24px; width: 150px;"/></td>
			
			<td colspan="5" ><input id="btnTaxWise" type="button"
			class="form_button" value="Tax Wise"  onclick="funOnClckTaxWiseBtn('divTaxWise')" style="background-size: 140px 24px; width: 150px;"/></td>
			
			<td colspan="7"><input id="btnExpectedArrList" type="button"
			class="form_button" value="Expected Arrival List"  onclick="funOnClckExpectedArrWiseBtn('divExpectedArrList')" style="background-size: 150px 24px; width: 150px;" /></td>
			
			<td colspan="9"><input id="btnExpectedDeptList" type="button"
			class="form_button" value="Expected Departure List"  onclick="funOnClckExpectedDeptWiseBtn('divExpectedDeptList')" style="background-size: 150px 24px; width: 150px;" /></td>
			
			<td colspan="3"><input id="btnBillPrinting" type="button"
			class="form_button" value="Bill Wise"  onclick="funOnClckBillPrinting('divBillPrinting')" style="background-size: 140px 24px; width: 150px;" /></td>
			
		</tr>
		</table>
		</div>
		<div
		style=" border: 1px solid #ccc; display: block; height: 30px; margin: auto;  width: 94%;">
		<table class="transTable" style="width:100%;height:30px; ">
		 <tr>
			<td><input id="btnCheckInList" type="button"
			class="form_button" value="Check In List"  onclick="funOnClckCheckInBtn('divCheckInList')" style="background-size: 140px 24px; width: 150px;" /></td>	
			
			<td colspan="3"><input id="btnCheckOutList" type="button"
			class="form_button" value="Check Out List"  onclick="funOnClckCheckOutBtn('divCheckOutList')" style="background-size: 140px 24px; width: 150px;" /></td>
			
			<td colspan="5"><input id="btnCancelationList" type="button"
			class="form_button" value="Cancellation List"  onclick="funOnClckCancelationWiseBtn('divCancelationList')" style="background-size: 140px 24px; width: 150px;" /></td>
		   
		    <td colspan="7"><input id="btnNoShowList" type="button"
			class="form_button" value="No Show List"  onclick="funOnClckNoShowWiseBtn('divNoShowList')" style="background-size: 140px 24px; width: 150px;" /></td>	
			
			<td colspan="9"><input id="btnVoidBillList" type="button"
			class="form_button" value="Void Bill List"  onclick="funOnClckVoidBillWiseBtn('divVoidBillList')" style="background-size: 140px 24px; width: 150px;" /></td>
			
			<td><input id="btnPayment" type="button"
			class="form_button" value="Payment"  onclick="funOnClickPayment('divPayment')" style="background-size: 140px 24px; width: 150px;" /></td>	
			
		 </tr>
		</table>
		</div>
		
		<br /><br />
<div id="wait" style="display:none;width:60px;height:60px;border:0px solid black;position:absolute;top:60%;left:55%;padding:2px;">
				<img src="../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif" width="60px" height="60px" />
			</div>
	
	
	</s:form>

</body>
</html>