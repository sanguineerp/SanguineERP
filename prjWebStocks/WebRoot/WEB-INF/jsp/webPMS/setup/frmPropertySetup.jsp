<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<style type="text/css">
.ui-timepicker-wrapper
{
	width: 95px;
}
</style>

<script type="text/javascript">
	var fieldName;
	var acBrandrow;
	var reservationMessage;
	var smsAPI;
	var smsContentForReservation;
	var emailContentForCheckIn;
	var emailContentForReservation;
	
	$(document).ready(function() {

		funTaxLinkUpData('Tax');
		funDepartmentLinkUpData('Department');
		funSettlementLinkUpData('Settlement');
		funRoomTypeLinkUpData('Room Type');
		funPackageLinkUpData('Package');
		
		
		$(".tab_content").hide();
		$(".tab_content:first").show();

		$("ul.tabs li").click(function() {
			$("ul.tabs li").removeClass("active");
			$(this).addClass("active");
			$(".tab_content").hide();

			var activeTab = $(this).attr("data-state");
			$("#" + activeTab).fadeIn();
		});
		
		
		$(".tab_content1").hide();
		$(".tab_content1:first").show();
		
		$("ul.tabs1 li").click(function() {
			$("ul.tabs1 li").removeClass("active");
			$(this).addClass("active");
			$(".tab_content1").hide();

			var activeTab = $(this).attr("data-state");
			$("#" + activeTab).fadeIn();
		});
		
		
		
	});
	
	
// 	function funValidateFields()
// 	{
// 		var flag=false;
// 		if($('#tmeCheckInTime').val().trim().length==0)
// 		{
// 			 alert("Please Select Check In Time");		 	 
// 		}
// 		else if($('#tmeCheckOutTime').val().trim().length==0)
// 		{
// 			 alert("Please Select Check Out Time");
// 		}
// 		else
// 		{
// 			//checkins
// 			var checkin=$('#tmeCheckInTime').val();
// 			var inHH="00";var inMM="00";var inSS="00";
// 			if(checkin.contains("am"))
// 			{
// 				var checkinvalue=checkin.split("am")[0];
// 				var inHH=checkinvalue.split(":")[0];
// 				var inMM=checkinvalue.split(":")[1];				
// 			}
// 			else
// 			{
// 				var checkinvalue=checkin.split("pm")[0];
// 				var inHH=checkinvalue.split(":")[0];
// 				var inMM=checkinvalue.split(":")[1];				
// 			}
			
// 			//checkouts
// 			var checkout=$('#tmeCheckOutTime').val();
// 			var outHH="00";var outMM="00";var outSS="00";
// 			if(checkout.contains("am"))
// 			{
// 				var checkoutvalue=checkout.split("am")[0];
// 				var outHH=checkoutvalue.split(":")[0];
// 				var outMM=checkoutvalue.split(":")[1];				
// 			}
// 			else
// 			{
// 				var checkoutvalue=checkout.split("pm")[0];
// 				var outHH=checkoutvalue.split(":")[0];
// 				var outMM=checkoutvalue.split(":")[1];				
// 			}
			
// 			$('#tmeCheckInTime').val(inHH+":"+inMM+":"+inSS);
// 			$('#tmeCheckOutTime').val(outHH+":"+outMM+":"+outSS);
			
// 			flag=true;
// 		}
		
// 		return flag;
// 	}
	
	$(function() 
	{	
		$('#tmeCheckInTime').timepicker();
		$('#tmeCheckOutTime').timepicker();	
		
		
		$('#tmeCheckInTime').timepicker({
		       
			datepicker:false,
		    formatTime:"h:i A",
		    step:60,
		    format:"h:i A"
		});
		 
		$('#tmeCheckOutTime').timepicker({
			datepicker:false,
		    formatTime:"h:i A",
		    step:60,
		    format:"h:i A"
		}); 			
		/* 
		$('#tmeCheckInTime').timepicker('setTime', new Date());
		$('#tmeCheckOutTime').timepicker('setTime', new Date());*/

	}); 
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

		
		reservationMessage=value="${ReservationEmail}"
		$('#txtReservationEmailContent').val(reservationMessage);
		
		smsAPI=value="${SmsApi}";
		$('#txtSMSAPI').val(smsAPI);
		
		
		smsContentForReservation=value="${smsContentForReservatiojn}"
		$('#txtReservationSMSContent').val(smsContentForReservation);
		
		emailContentForCheckIn=value="${emailContentForCheckIn}"
		$('#txtCheckINEmailContent').val(emailContentForCheckIn);
		
		emailContentForReservation=value="${emailContentForReservation}"
		$('#txtReservationEmailContent').val(emailContentForReservation);
			
		
	});
	/**
		* Success Message After Saving Record
	**/
	
	function funHelp(transactionName)
	{
		fieldName=transactionName;
		window.open("searchform.html?formname="+transactionName+"&searchText=","mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
		//window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
	}
	
	
	function funCreateSMS1()
	{
	 	 	
		var field =$("#cmbReservationSMSField").val();
		var content='';
		var mainSMS =$("#txtReservationSMSContent").val();
		
		if(field=='CompanyName')
		{
			content='%%CompanyName';
		}
		if(field=='PropertyName')
		{
			content='%%PropertyName';
		}
		if(field=='RNo')
		{
			content='%%RNo';
		}
		if(field=='RDate')
		{
			content='%%RDate';
		}
	
		if(field=='GuestName')
		{
			content='%%GuestName';
		}
		
		if(field=='RoomNo')
		{
			content='%%RoomNo';
		}
		
		if(field=='NoNights')
		{
			content='%%NoNights';
		}
		
		mainSMS+=content;
		$("#txtReservationSMSContent").val(mainSMS);
	 }
		
	function funCreateSMS2()
		{
		
		   
		var field =$("#cmbCheckINSMSField").val();
		var content='';
		var mainSMS =$("#txtCheckINSMSContent").val();
		
		if(field=='CompanyName')
		{
			content='%%CompanyName';
		}
		if(field=='PropertyName')
		{
			content='%%PropertyName';
		}
		if(field=='CheckIn')
		{
			content='%%CheckIn';
		}
		if(field=='GuestName')
		{
			content='%%GuestName';
		}
		if(field=='CheckInDate')
		{
			content='%%CheckInDate';
		}
		
		if(field=='RoomNo')
		{
			content='%%RoomNo';
		}
		
		if(field=='NoNights')
		{
			content='%%NoNights';
		}
		mainSMS+=content;
		$("#txtCheckINSMSContent").val(mainSMS);
		
		}	
		
	
	function funCreateSMS3()
		{	
		
		var field =$("#cmbAdvAmtSMSField").val();
		var content='';
		var mainSMS =$("#txtAdvAmtSMSContent").val();
		
		if(field=='CompanyName')
		{
			content='%%CompanyName';
		}
		if(field=='PropertyName')
		{
			content='%%PropertyName';
		}
		if(field=='PaymentNo')
		{
			content='%%PaymentNo';
		}
		if(field=='SettlementDesc')
		{
			content='%%SettlementDesc';
		}
	
		if(field=='Amount')
		{
			content='%%Amount';
		}
		mainSMS+=content;
		$("#txtAdvAmtSMSContent").val(mainSMS);
		}	
	
	
		function funCreateSMS4()
		{
			
		var field =$("#cmbCheckOutSMSField").val();
		var content='';
		var mainSMS =$("#txtCheckOutSMSContent").val();
		
		if(field=='CompanyName')
		{
			content='%%CompanyName';
		}
		if(field=='PropertyName')
		{
			content='%%PropertyName';
		}
		if(field=='CheckOut')
		{
			content='%%CheckOut';
		}
		if(field=='GuestName')
		{
			content='%%GuestName';
		}
	
		if(field=='RoomNo')
		{
			content='%%RoomNo';
		}
		if(field=='checkOutDate')
		{
			content='%%checkOutDate';
		}
		mainSMS+=content;
		$("#txtCheckOutSMSContent").val(mainSMS);
		}		
		
		function funCreateEmail1()
		{
		 	 	
			var field =$("#cmbReservationEmailField").val();
			var content='';
			var mainSMS =$("#txtReservationEmailContent").val();
			
			if(field=='CompanyName')
			{
				content='%%CompanyName';
			}
			if(field=='PropertyName')
			{
				content='%%PropertyName';
			}
			if(field=='RNo')
			{
				content='%%RNo';
			}
			if(field=='RDate')
			{
				content='%%RDate';
			}
		
			if(field=='GuestName')
			{
				content='%%GuestName';
			}
			
			if(field=='RoomNo')
			{
				content='%%RoomNo';
			}
			
			if(field=='NoNights')
			{
				content='%%NoNights';
			}
			
			mainSMS+=content;
			$("#txtReservationEmailContent").val(mainSMS);
		 }
		function funCreateEmail2()
		{
		
		   
		var field =$("#cmbCheckINEmailField").val();
		var content='';
		var mainSMS =$("#txtCheckINEmailContent").val();
		
		if(field=='CompanyName')
		{
			content='%%CompanyName';
		}
		if(field=='PropertyName')
		{
			content='%%PropertyName';
		}
		if(field=='CheckIn')
		{
			content='%%CheckIn';
		}
		if(field=='GuestName')
		{
			content='%%GuestName';
		}
		if(field=='CheckInDate')
		{
			content='%%CheckInDate';
		}
		
		if(field=='RoomNo')
		{
			content='%%RoomNo';
		}
		
		if(field=='NoNights')
		{
			content='%%NoNights';
		}
		mainSMS+=content;
		$("#txtCheckINEmailContent").val(mainSMS);
		
		}
		
		
		function funValidateFields()
		{
			var roomLimit =  parseFloat($("#txtRoomLimit").val());
			var noOfRoom =  parseFloat($("#txtNoOfRooms").val());
			
			/* if(roomLimit =="0" || roomLimit == "")
			{
				 alert("Please Enter Room Limit");
				 return false;
			} 
			else if(roomLimit!="0")
			{
				
				if(roomLimit > noOfRoom)
				   {
					alert("Room Limit Cannot be greater than Number of Room.");
				   	return false;
				   }*/
			
				
		}
		
		function funRoomTypeLinkUpData(code)
		{
			var searchUrl="";
			var property=$('#strPropertyCode').val();
			searchUrl=getContextPath()+"/loadPMSLinkUpData.html?strDoc="+code;
			$.ajax
			({
		        type: "POST",
		        url: searchUrl,
			    dataType: "json",
			    success: function(response)
			    {
			    	//funDeleteTableAllRowsOfParticulorTable(code);
			    	$.each(response, function(i,item)
							{
					    		//var arr = jQuery.makeArray( response[i] );
					    		funAddRowRoomTypeLinkUpData(item);
					    		
							}); 
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
		
		function funPackageLinkUpData(code)
		{
			var searchUrl="";
			var property=$('#strPropertyCode').val();
			searchUrl=getContextPath()+"/loadPMSLinkUpData.html?strDoc="+code;
			$.ajax
			({
		        type: "POST",
		        url: searchUrl,
			    dataType: "json",
			    success: function(response)
			    {
			    	//funDeleteTableAllRowsOfParticulorTable(code);
			    	$.each(response, function(i,item)
							{
					    		//var arr = jQuery.makeArray( response[i] );
					    		funAddRowPackageLinkUpData(item);
					    		
							}); 
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
		
		function funSettlementLinkUpData(code)
		{
			var searchUrl="";
			var property=$('#strPropertyCode').val();
			searchUrl=getContextPath()+"/loadPMSLinkUpData.html?strDoc="+code;
			$.ajax
			({
		        type: "POST",
		        url: searchUrl,
			    dataType: "json",
			    success: function(response)
			    {
			    	//funDeleteTableAllRowsOfParticulorTable(code);
			    	$.each(response, function(i,item)
							{
					    		//var arr = jQuery.makeArray( response[i] );
					    		funAddRowSettlementLinkUpData(item);
					    		
							}); 
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
		
		function funTaxLinkUpData(code)
		{
			var searchUrl="";
			var property=$('#strPropertyCode').val();
			searchUrl=getContextPath()+"/loadPMSLinkUpData.html?strDoc="+code;
			$.ajax
			({
		        type: "POST",
		        url: searchUrl,
			    dataType: "json",
			    success: function(response)
			    {
			    	//funDeleteTableAllRowsOfParticulorTable(code);
			    	$.each(response, function(i,item)
							{
					    		//var arr = jQuery.makeArray( response[i] );
					    		funAddRowTaxLinkUpData(item);
					    		
							}); 
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
		
		function funDepartmentLinkUpData(code)
		{
			var searchUrl="";
			var property=$('#strPropertyCode').val();
			searchUrl=getContextPath()+"/loadPMSLinkUpData.html?strDoc="+code;
			$.ajax
			({
		        type: "POST",
		        url: searchUrl,
			    dataType: "json",
			    success: function(response)
			    {
			    	//funDeleteTableAllRowsOfParticulorTable(code);
			    	$.each(response, function(i,item)
							{
					    		//var arr = jQuery.makeArray( response[i] );
					    		funAddRowDeptLinkUpData(item);
					    		
							}); 
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
		
		
		
		function funDeleteTableAllRowsOfParticulorTable(tableName)
		{
			switch(tableName)
			{
				case "Department" :
				{
					$("#tbl"+tableName+ " tr").remove();
					break;
				}
				case "Tax" :
				{
					$("#tbl"+tableName+ " tr").remove();
					break;
				}
				
				case "Room Type" :
				{
					$("#tbl"+tableName+ " tr").remove();
					break;
				}
				case "Package" :
				{
					$("#tbl"+tableName+ " tr").remove();
					break;
				}
							
				case "Settlement" :
				{
					$("#tbl"+tableName+ " tr").remove();
					break;
				}
				
				
			}
		}
		
		function funAddRowDeptLinkUpData(rowData)
		{
			$('#hidLinkup').val("");
			$('#hidLinkup').val("DeptLinkup");
			var table = document.getElementById("tblDepartment");
		    var rowCount = table.rows.length;
		    var row = table.insertRow(rowCount);
		    var strMasterCode = rowData.strMasterCode;
	    	var strMasterName = rowData.strMasterName;
	    	var strAcCode = rowData.strAccountCode;
	    	var strAcName = rowData.strMasterDesc;
    		if(strAcCode==null && strAcName == null)
   			{
   				strAcCode = "";
   				strAcName = "";
			}
    		else
   			{
    			strAcCode =	rowData.strAccountCode;
				strAcName = rowData.strMasterDesc;
   			}
			row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listDeptLinkUp["+(rowCount)+"].strMasterCode\"  id=\"txtMasterCode."+(rowCount)+"\" value='"+strMasterCode+"' />";
		    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listDeptLinkUp["+(rowCount)+"].strMasterName\"  id=\"txtMasterName."+(rowCount)+"\" value='"+strMasterName+"' />";
		    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"searchTextBox\"  name=\"listDeptLinkUp["+(rowCount)+"].strAccountCode\"   id=\"txtSettlement."+(rowCount)+"\" value='"+strAcCode+"' ondblclick=\"funHelp1("+(rowCount)+",'Dept-Service')\" />";
		    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listDeptLinkUp["+(rowCount)+"].strMasterDesc\"   id=\"txtSettlementName."+(rowCount)+"\" value='"+strAcName+"' />";
	 	}
		
		function funAddRowPackageLinkUpData(rowData)
		{
			$('#hidLinkup').val("");
			$('#hidLinkup').val("Package");
			var table = document.getElementById("tblPackage");
		    var rowCount = table.rows.length;
		    var row = table.insertRow(rowCount);
		    var strTaxCode = rowData.strMasterCode;
	    	var strDesc = rowData.strMasterName;
	    	var strAcCode = rowData.strAccountCode;
	    	var strAcName = rowData.strMasterDesc;
    		if(strAcCode==null && strAcName == null)
   			{
   				strAcCode = "";
   				strAcName = "";
			}
    		else
   			{
    			strAcCode =	rowData.strAccountCode;
				strAcName = rowData.strMasterDesc;
   			}
		    row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listTaxLinkUp["+(rowCount)+"].strMasterCode\"  id=\"txtTaxCode."+(rowCount)+"\" value='"+strTaxCode+"' />";
		    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listTaxLinkUp["+(rowCount)+"].strMasterName\"  id=\"txtTaxDesc."+(rowCount)+"\" value='"+strDesc+"' />";
		    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"searchTextBox\"  name=\"listTaxLinkUp["+(rowCount)+"].strAccountCode\"   id=\"txtTaxAcCode."+(rowCount)+"\" value='"+strAcCode+"' ondblclick=\"funHelp1("+(rowCount)+",'Package-Service')\" />";
		    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listTaxLinkUp["+(rowCount)+"].strMasterDesc\"   id=\"txtTaxAcName."+(rowCount)+"\" value='"+strAcName+"' />";
	 	}
		
		
		function funAddRowRoomTypeLinkUpData(rowData)
		{
			$('#hidLinkup').val("");
			$('#hidLinkup').val("RoomType");
			var table = document.getElementById("tblrmType");
		    var rowCount = table.rows.length;
		    var row = table.insertRow(rowCount);
		    var strTaxCode = rowData.strMasterCode;
	    	var strDesc = rowData.strMasterName;
	    	var strAcCode = rowData.strAccountCode;
	    	var strAcName = rowData.strMasterDesc;
    		if(strAcCode==null && strAcName == null)
   			{
   				strAcCode = "";
   				strAcName = "";
			}
    		else
   			{
    			strAcCode =	rowData.strAccountCode;
				strAcName = rowData.strMasterDesc;
   			}
			
		    row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listTaxLinkUp["+(rowCount)+"].strMasterCode\"  id=\"txtTaxCode."+(rowCount)+"\" value='"+strTaxCode+"' />";
		    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listTaxLinkUp["+(rowCount)+"].strMasterName\"  id=\"txtTaxDesc."+(rowCount)+"\" value='"+strDesc+"' />";
		    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"searchTextBox\"  name=\"listTaxLinkUp["+(rowCount)+"].strAccountCode\"   id=\"txtTaxAcCode."+(rowCount)+"\" value='"+strAcCode+"' ondblclick=\"funHelp1("+(rowCount)+",'RoomType-Service')\" />";
		    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listTaxLinkUp["+(rowCount)+"].strMasterDesc\"   id=\"txtTaxAcName."+(rowCount)+"\" value='"+strAcName+"' />";
	 	}
		
		
		function funAddRowSettlementLinkUpData(rowData)
		{
			$('#hidLinkup').val("");
			$('#hidLinkup').val("SettlementLinkup");
			var table = document.getElementById("tblSettlement");
		    var rowCount = table.rows.length;
		    var row = table.insertRow(rowCount);
		    var strTaxCode = rowData.strMasterCode;
	    	var strDesc = rowData.strMasterName;
	    	var strAcCode = rowData.strAccountCode;
	    	var strAcName = rowData.strMasterDesc;
    		if(strAcCode==null && strAcName == null)
   			{
   				strAcCode = "";
   				strAcName = "";
			}
    		else
   			{
    			strAcCode =	rowData.strAccountCode;
				strAcName = rowData.strMasterDesc;
   			}
			
		    row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listTaxLinkUp["+(rowCount)+"].strMasterCode\"  id=\"txtTaxCode."+(rowCount)+"\" value='"+strTaxCode+"' />";
		    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listTaxLinkUp["+(rowCount)+"].strMasterName\"  id=\"txtTaxDesc."+(rowCount)+"\" value='"+strDesc+"' />";
		    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"searchTextBox\"  name=\"listTaxLinkUp["+(rowCount)+"].strAccountCode\"   id=\"txtTaxAcCode."+(rowCount)+"\" value='"+strAcCode+"' ondblclick=\"funHelp1("+(rowCount)+",'Settlement-Service')\" />";
		    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listTaxLinkUp["+(rowCount)+"].strMasterDesc\"   id=\"txtTaxAcName."+(rowCount)+"\" value='"+strAcName+"' />";
	 	}
		
		function funAddRowTaxLinkUpData(rowData)
		{
			$('#hidLinkup').val("");
			$('#hidLinkup').val("taxLinkup");
			var table = document.getElementById("tblTax");
		    var rowCount = table.rows.length;
		    var row = table.insertRow(rowCount);
		    var strTaxCode = rowData.strMasterCode;
	    	var strDesc = rowData.strMasterName;
	    	var strAcCode = rowData.strAccountCode;
	    	var strAcName = rowData.strMasterDesc;
    		if(strAcCode==null && strAcName == null)
   			{
   				strAcCode = "";
   				strAcName = "";
			}
    		else
   			{
    			strAcCode =	rowData.strAccountCode;
				strAcName = rowData.strMasterDesc;
   			}
		    row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listTaxLinkUp["+(rowCount)+"].strMasterCode\"  id=\"txtTaxCode."+(rowCount)+"\" value='"+strTaxCode+"' />";
		    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listTaxLinkUp["+(rowCount)+"].strMasterName\"  id=\"txtTaxDesc."+(rowCount)+"\" value='"+strDesc+"' />";
		    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"searchTextBox\"  name=\"listTaxLinkUp["+(rowCount)+"].strAccountCode\"   id=\"txtTaxAcCode."+(rowCount)+"\" value='"+strAcCode+"' ondblclick=\"funHelp1("+(rowCount)+",'Tax-Service')\" />";
		    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listTaxLinkUp["+(rowCount)+"].strMasterDesc\"   id=\"txtTaxAcName."+(rowCount)+"\" value='"+strAcName+"' />";
	 	}
		
		function funHelp1(row,transactionName)
		{
			acBrandrow=row;
			fieldName = transactionName;
			
			if(transactionName=='SubGroup' || transactionName=='Supplier')
			{
				transactionName='AccountMasterGLOnlyWeb-Service';
			}
			 if(transactionName=='Excess' || transactionName=='Shortage')
			{ //
				transactionName='LocationWeb-Service';
			} 
			
			window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:1000px;top=500,left=500")
		}
		
		function funSetData(code){

		switch(fieldName){
				
			case 'Dept-Service' : 
				funSetDept(code);
				break;
		        
			case 'Tax-Service':
		    	funSetTax(acBrandrow,code);
		        break; 
		        
			case 'Settlement-Service':
		    	funSetSettlement(acBrandrow,code);
		        break;      
		        
			case 'Package-Service':
		    	funSetPackage(acBrandrow,code);
		        break;    
		        
			case 'RoomType-Service':
		    	funSetRoomType(acBrandrow,code);
		        break;  
		        
			
		}
	}
		
		function funSetDept(acBrandrow,code)
		{
			$.ajax({
				type : "GET",
				url : getContextPath()+ "/loadCRMTaxLinkupDataFormWebService.html?strDocCode=" + code,
				dataType : "json",
				success : function(response){
					document.getElementById("txtSettlement."+acBrandrow).value=response.strAccountCode;
					document.getElementById("txtSettlementName."+acBrandrow).value=response.strAccountName;
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
		
		
		function funSetTax(acBrandrow,code)
		{
			$.ajax({
				type : "GET",
				url : getContextPath()+ "/loadTaxLinkupDataFormWebService.html?strDocCode=" + code,
				dataType : "json",
				success : function(response){
					document.getElementById("txtTaxAcCode."+acBrandrow).value=response.strAccountCode;
					document.getElementById("txtTaxAcName."+acBrandrow).value=response.strAccountName;
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

</head>
<body>

	<div id="formHeading">
	<label>Property Setup</label>
	</div>

<br/>
<br/>

	<s:form name="PropertySetup" method="POST" action="savePropertySetup.html">
	<table>
		<tr>
			<td  style="width: 100px;"><label>Property</label></td>
			<td colspan="5"><s:select id="strPropertyCode" path="strPropertyCode" items="${listOfProperty}" required="true" cssClass="BoxW200px"></s:select></td>				    						    		        			 
		</tr>
	</table>
	
	<br/>
	
	<table
				style="border: 0px solid black; width: 100%;height:100%; background-color: #C0E4FF;">
				
				
				<tr>
					<td>
					
					<div id="tab_container" style="height: 600px">
								<ul class="tabs">
								<li data-state="tab1">General</li>
								
								<li data-state="tab2">SMS Setup</li>
								
								<li data-state="tab3">Linkup</li>
								
								<li data-state="tab4">E-mail Setup</li>

							</ul>
							<div id="tab1" class="tab_content" style="height: 800px">
							<br><br>
									<table class="masterTable">
									
<!-- 							<tr> -->
<!-- 							    <td  style="width: 100px;"><label>Property</label></td> -->
<%-- 								<td colspan="5"><s:select id="strPropertyCode" path="strPropertyCode" items="${listOfProperty}" required="true" cssClass="BoxW200px"></s:select></td>				    						    		        			  --%>
<!-- 							</tr> -->
							<tr>
							    <td><label>Check In Time</label></td>
							    <td><s:input path="tmeCheckInTime"  id="tmeCheckInTime" value="${checkInTime}"  class="timePickerTextBox" /></td>	
							    <td><label>Check Out Time</label></td>
							    <td><s:input path="tmeCheckOutTime"  id="tmeCheckOutTime" value="${checkOutTime}" class="timePickerTextBox" /></td>	
							    <td><label>GST No</label></td>
							    <td><s:input path="strGSTNo"  id="txtGSTNo"  value="${GSTNo}" cssClass="longTextBox" style="width: 190px" /></td>								    						    		        			
							</tr>
							
							<tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
							<br /><br />
							
									
									<tr>
										 <td style="width: 10%;"><label>Total Numbers of Room</label></td>
									     <td style="width: 5%;"><input type="text" class="numeric" id="txtNoOfRooms" Class="longTextBox" value="${listOfRoom}"/>
									     </td>	
									     <td style="width: 10%;"><label>Room Limit</label></td>
									     <td style="width: 5%;"><s:input colspan="3" type="text" class="numeric" id="txtRoomLimit" path="strRoomLimit" value="${RoomLimit}"  cssClass="longTextBox"/></td>	
									</tr>
									
									<tr>
									<td style="width: 10%;"><label>Bank Ac Name</label></td>
									<td style="width: 5%;"><s:input colspan="3" type="text" class="numeric" id="txtbankAcName" value="${bankAcName}" path="strBankAcName" cssClass="longTextBox"/></td>
									
									<td style="width: 10%;"><label>Bank Ac Number</label></td>
									<td style="width: 5%;"><s:input colspan="3" type="text" class="numeric" id="txtbankAcNum" value="${BankACNumber}" path="strBankAcNumber" cssClass="longTextBox"/></td>
									
									<td style="width: 10%;"><label>Bank IFS Code</label></td>
									<td style="width: 5%;"><s:input colspan="3" type="text" class="numeric" id="txtbankIFSC" value="${BankIFSC}" path="strBankIFSC" cssClass="longTextBox"/></td>
									
									</tr>
									
									<tr>
									<td style="width: 10%;"><label>Branch Name</label></td>
									<td style="width: 5%;"><s:input colspan="3" type="text" class="numeric" id="txtBranchName" value="${BranchName}" path="stBranchName" cssClass="longTextBox"/></td>
									
									<td style="width: 10%;"><label>Pan Number</label></td>
									<td style="width: 5%;"><s:input colspan="3" type="text" class="numeric" id="txtPANNo" value="${panNo}" path="strPanNo" cssClass="longTextBox"/></td>
									
									
									<td style="width: 10%;"><label>HSN Code/SAC</label></td>
									<td style="width: 5%;"><s:input colspan="3" type="text" class="numeric" id="txtHSCCode" value="${HSCCode}" path="strHscCode" cssClass="longTextBox"/></td>
									
									</tr>
									
									<%-- <tr>
									
									<td ><label>Amount Decimal Places</label></td>
										<td><s:select path="intdec" id="intdec"
												cssClass="BoxW48px">
												<s:option value="0">0</s:option>
												<s:option value="1">1</s:option>
												<s:option value="2">2</s:option>
												<s:option value="3">3</s:option>
												<s:option value="4">4</s:option>
												<s:option value="5">5</s:option>
												<s:option value="6">6</s:option>
												<s:option value="7">7</s:option>
												<s:option value="8">8</s:option>
												<s:option value="9">9</s:option>
												<s:option value="10">10</s:option>
												
											</s:select></td>
									
									</tr> --%>
									
									
								
							
							
						</table>
							</div>
						
							
								<div id="tab2" class="tab_content">
							<br><br><br>
							<table id="tblAudit" class="transTable">
							<tr>
							<td><label >SMS Provider</label></td>
									<td colspan="3"><s:select  id="cmbSMSProvider" path="strSMSProvider" class="BoxW48px" style="width:130px">
											<option value="SANGUINE">SANGUINE</option>
										</s:select>
									</td>
							</tr>
							
							<tr>
							<td><label >SMS API</label></td>
								<td colspan="3"><s:textarea  id="txtSMSAPI" text="${SmsApi}" path="strSMSAPI"  cssStyle="width: 669px;" /></td>
							</tr>
						 	<tr>
							<td style="width: 130px;"><label >SMS Content For Reservation </label></td>
							<td>	
									<select  id="cmbReservationSMSField" class="BoxW48px" style="width:130px" >
										<option value="CompanyName">Company Name</option>
										<option value="PropertyName">Property Name</option>
										<option value="RNo">Reservation No</option>
										<option value="RDate">Reservation Date</option>
										<option value="GuestName">GuestName</option>
										<option value="RoomNo">Room No</option>
									<option value="NoNights">No of Nights</option>
									</select>
							 </td>
							 
							<td><input type="button" value="Add" class="smallButton" onclick="funCreateSMS1();" id=btnAddSMS1 /></td>
									<td><s:textarea cssStyle="width: 373px; height: 101px;" id="txtReservationSMSContent" path="strReservationSMSContent"  /></td>
							</tr> 
							
							
							<tr>
							<td style="width: 130px;"><label >Email Content For Check IN </label></td>
							<td>	
									<select  id="cmbCheckINSMSField" class="BoxW48px" style="width:130px" >
										<option value="CompanyName">Company Name</option>
										<option value="PropertyName">Property Name</option>
										<option value="CheckIn">Check IN</option>
										<option value="GuestName">GuestName</option>
										<option value="RoomNo">RoomNo</option>
										<option value="NoNights">No of Nights</option>
										<option value="RDate">CheckInDate</option>
										
									</select>
							 </td>
							 
									<td><input type="button" value="Add" class="smallButton" onclick="funCreateSMS2();" id=btnAddSMS2 /></td>
									<td><s:textarea cssStyle="width: 373px; height: 101px;" id="txtCheckINSMSContent" path="strCheckInSMSContent"  /></td>
							</tr>
							
	
										<tr>
							<td style="width: 130px;"><label >SMS Content For Advance Amount </label></td>
							<td>	
									<select  id="cmbAdvAmtSMSField" class="BoxW48px" style="width:130px" >
										<option value="CompanyName">Company Name</option>
										<option value="PropertyName">Property Name</option>
										<option value="PaymentNo">Payment Recipt No</option>
										<option value="Amount">Amount</option>
										<option value="SettlementDesc">Settlement Description</option>
									</select>
							 </td>
							 
									<td><input type="button" value="Add" class="smallButton" onclick="funCreateSMS3();" id=btnAddSMS3 /></td>
									<td><s:textarea cssStyle="width: 373px; height: 101px;" id="txtAdvAmtSMSContent" path="strAdvAmtSMSContent"  /></td>
							</tr>
							
							
											<tr>
							<td style="width: 130px;"><label >SMS Content For check Out </label></td>
							<td>	
									<select  id="cmbCheckOutSMSField" class="BoxW48px" style="width:130px" >
									<option value="CompanyName">Company Name</option>
										<option value="PropertyName">Property Name</option>
										<option value="CheckOut">Check Out</option>
										<option value="GuestName">GuestName</option>
										<option value="RoomNo">RoomNo</option>
										<option value="checkOutDate">CheckOutDate</option>
									</select>
							 </td>
							 
									<td><input type="button" value="Add" class="smallButton" onclick="funCreateSMS4();" id=btnAddSMS4 /></td>
									<td><s:textarea cssStyle="width: 373px; height: 101px;" id="txtCheckOutSMSContent" path="strCheckOutSMSContent"  /></td>
							</tr>
							
							</table>							
							</div>
							
							<div id="tab3" class="tab_content" style="height: 890px">
					<br/><br/>
					
					<div id="tab_container1" class="masterTable"  style="height: 535px">
							<ul class="tabs1">
							<li  class="active" data-state="divSubGroup"  style="width: 10%;padding-left: 55px;">Department</li>
							
							<li data-state="divTax" style="width: 10%; padding-left: 55px">Tax</li>
							
							<li data-state="divSupplier" style="width: 10%; padding-left: 55px">Room Type</li>
							
							<li data-state="divDiscount" style="width: 10%; padding-left: 55px">Package</li>
				
							<li data-state="divRoundOff" style="width: 10%; padding-left: 55px">Settlement</li>
						 </ul>
						
					&nbsp;&nbsp;
							
							<div id="divSubGroup" class="tab_content1" style="height: 500px;margin-top: 20px;">
							<table
								style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">
									<td style="width:10%;">Department Code</td>
									<td style="width:10%;">Department Code</td>
									<td style="width:20%;">Account Code</td>
									<td style="width:20%;">Account Name</td>
								</tr>
							</table>
							
							<div style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
									<table id="tblDepartment"
									style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
									class="transTablex col8-center">
									<tbody>
									<col style="width:10%">
									<col style="width:10%">					
									<col style="width:20%">
									<col style="width:20%">
									</tbody>
								</table>
							</div>
							</div>
							<div id="divTax" class="tab_content1" style="height: 500px;margin-top: 20px;">
							<table
								style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">
									<td style="width:10%;">Tax Code</td>
									<td style="width:10%;">Tax Name</td>
									<td style="width:20%;">Account Code</td>
									<td style="width:20%;">Account Name</td>
								</tr>
							</table>
							
							<div style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
									<table id="tblTax"
									style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
									class="transTablex col8-center">
									<tbody>
									<col style="width:10%">
									<col style="width:10%">					
									<col style="width:20%">
									<col style="width:20%">
									</tbody>
								</table>
							</div>
							</div>
							
							<div id="divSupplier" class="tab_content1" style="height: 500px;margin-top: 20px;">
							<table
								style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">
									<td style="width:10%;">Room Code</td>
									<td style="width:10%;">Room Desc</td>
									<td style="width:20%;">Account Code</td>
									<td style="width:20%;">Account Name</td>
								</tr>
							</table>
							
							<div style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
									<table id="tblrmType"
									style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
									class="transTablex col8-center">
									<tbody>
									<col style="width:10%">
									<col style="width:10%">					
									<col style="width:20%">
									<col style="width:20%">
									</tbody>
								</table>
							</div>
							</div>
							

							<div id="divDiscount" class="tab_content1" style="height: 500px;margin-top: 20px;">
							<table
								style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">
									<td style="width:10%;">Package Code</td>
									<td style="width:10%;">Package Desc</td>
									<td style="width:20%;">Account Code</td>
									<td style="width:20%;">Account Name</td>
								</tr>
							</table>
							
							<div style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
									<table id="tblPackage"
									style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
									class="transTablex col8-center">
									<tbody>
									<col style="width:10%">
									<col style="width:10%">					
									<col style="width:20%">
									<col style="width:20%">
									</tbody>
								</table>
							</div>
							</div>
							
							<div id="divRoundOff" class="tab_content1" style="height: 500px;margin-top: 20px;">
							<table
								style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">
									<td style="width:10%;">Settlement Code</td>
									<td style="width:10%;">Settlement Desc</td>
									<td style="width:20%;">Account Code</td>
									<td style="width:20%;">Account Name</td>
								</tr>
							</table>
							
							<div style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
									<table id="tblSettlement"
									style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
									class="transTablex col8-center">
									<tbody>
									<col style="width:10%">
									<col style="width:10%">					
									<col style="width:20%">
									<col style="width:20%">
									</tbody>
								</table>
							</div>
							</div>
													
						</div>
							
							
							</div>
							
							<div id="tab4" class="tab_content">
							<br><br><br>
							<table id="tblAudit" class="transTable">
							
						 	<tr>
							<td style="width: 130px;"><label >Email Content For Reservation </label></td>
							<td>	
									<select  id="cmbReservationEmailField" class="BoxW48px" style="width:130px" >
										<option value="CompanyName">Company Name</option>
										<option value="PropertyName">Property Name</option>
										<option value="RNo">Reservation No</option>
										<option value="RDate">Reservation Date</option>
										<option value="GuestName">GuestName</option>
										<option value="RoomNo">Room No</option>
									<option value="NoNights">No of Nights</option>
									</select>
							 </td>
							 
							<td><input type="button" value="Add" class="smallButton" onclick="funCreateEmail1();" id=btnAddEmail1 /></td>
									<td><s:textarea cssStyle="width: 373px; height: 101px;" id="txtReservationEmailContent" value="${ReservationEmail}" path="strReservationEmailContent"  /></td>
							</tr> 
							
							
							<tr>
							<td style="width: 130px;"><label >Email Content For Check IN </label></td>
							<td>	
									<select  id="cmbCheckINEmailField" class="BoxW48px" style="width:130px" >
										<option value="CompanyName">Company Name</option>
										<option value="PropertyName">Property Name</option>
										<option value="CheckIn">Check IN</option>
										<option value="GuestName">GuestName</option>
										<option value="RoomNo">RoomNo</option>
										<option value="NoNights">No of Nights</option>
										<option value="RDate">CheckInDate</option>
										
									</select>
							 </td>
							 
									<td><input type="button" value="Add" class="smallButton" onclick="funCreateEmail2();" id=btnAddEmail2 /></td>
									<td><s:textarea cssStyle="width: 373px; height: 101px;" id="txtCheckINEmailContent" path="strCheckInEmailContent"  /></td>
									<%-- <td><s:textarea cssStyle="width: 200px; height: 50px;" id="txtSMSContent" path="strInvNote"  /></td> --%>
									
							</tr>
							
	
										
							
							
									
							
							</table>							
							</div>
							
						</div>
					</td>
				</tr>
		</table>
	
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button" onclick="return funValidateFields()"/><!-- onclick="return funValidateFields()" -->
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>

	</s:form>
</body>
</html>
