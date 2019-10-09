<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>

<script type="text/javascript">

	var fieldName,listServiceRow=0,listEquipRow=0,listStaffRow=0,listItemRow=0;
	var totalTerrAmt = 0.0;
	  $(document).ready(function(){
		    
		  $(".tab_content").hide();
			$(".tab_content:first").show();

			$("ul.tabs li").click(function() {
				$("ul.tabs li").removeClass("active");
				$(this).addClass("active");
				$(".tab_content").hide();
				var activeTab = $(this).attr("data-state");
				$("#" + activeTab).fadeIn();
			});
			
	/* 	  $("#txtNoOfAdults").val(1);
		  $("#txtNoOfBookingRoom").val(1); */
		  
<%-- 		  var pmsDate='<%=session.getAttribute("PMSDate").toString()%>'; --%>
// 		  var dte=pmsDate.split("-");
// 		  $("#txtPMSDate").val(dte[2]+"-"+dte[1]+"-"+dte[0]);
	   });

	$(function() 
	{
		//var arrivalTime=session.getAttribute("PMSCheckInTime");
		//var departureTime=session.getAttribute("PMSCheckOutTime");
		
	<%-- 	 var pmsDate='<%=session.getAttribute("PMSDate").toString()%>';
		 --%>
		$('#txtFromTime').timepicker();
		$('#txtToTime').timepicker();
		
		$('#txtFromTime').timepicker({
		        'timeFormat':'H:i:s'
		});
		$('#txtFromTime').timepicker({
		        'timeFormat':'H:i:s'
		});
		
		
		$("#txtFromTime").timepicker();
		$("#txtToTime").timepicker();
		  
		
		$('#txtFromTime').timepicker({
	        'timeFormat':'H:i:s'
		});
		$('#txtToTime').timepicker({
			        'timeFormat':'H:i:s'
		});
			
		$('#txtFromTime').timepicker('setTime', new Date());
		//$('#txtDepartureTime').timepicker('setTime', new Date());
		//$('#txtDepartureTime').val("${tmeCheckOutPropertySetupTime}");
		
		$('#txtFromTime').timepicker();
		$('#txtToTime').timepicker();
		
		
		/* 
		$('#txtPickUpTime').timepicker();
		$('#txtDropTime').timepicker();
		
		$('#txtPickUpTime').timepicker({
		        'timeFormat':'H:i:s'
		});
		$('#txtDropTime').timepicker({
		        'timeFormat':'H:i:s'
		});
		
		$('#txtPickUpTime').timepicker('setTime', new Date());
		$('#txtDropTime').timepicker('setTime', new Date());
		
		
		$("#txtPickUpTime").timepicker();
		$("#txtDropTime").timepicker();
		 */  
		
// 		$( "#txtArrivalDate" ).datepicker({
// 			minDate: 0,
// 			 dateFormat: 'dd-mm-yy' 
// 		});
		
		 
	    $("#txtBookingDate").datepicker({ dateFormat: 'dd-mm-yy' });
		$("#txtFromDate").datepicker({ dateFormat: 'dd-mm-yy' });
		//$("#txtFromDate").datepicker('setDate', pmsDate);
		
// 		$( "#txtDepartureDate" ).datepicker({
// 			minDate: 0,
// 			 dateFormat: 'dd-mm-yy' 
// 		});
		
		$("#txtToDate").datepicker({ dateFormat: 'dd-mm-yy' });
		//$("#txtDepartureDate").datepicker('setDate', pmsDate);
		
		$("#txtFromDate").datepicker('setDate', '02-10-2019');
		$("#txtToDate").datepicker('setDate', '02-10-2019');
		
// 		$("#txtArrivalDate").datepicker({ dateFormat: 'dd-mm-yy' });
// 		$("#txtArrivalDate").datepicker('setDate', pmsDate);
		
// 		$("#txtDepartureDate").datepicker({ dateFormat: 'dd-mm-yy' });
// 		$("#txtDepartureDate").datepicker('setDate', pmsDate);
		
		/* $("#txtCancelDate").datepicker({ dateFormat: 'dd-mm-yy' });
		$("#txtCancelDate").datepicker('setDate', pmsDate);
		
		$("#txtConfirmDate").datepicker({ dateFormat: 'dd-mm-yy' });
		$("#txtConfirmDate").datepicker('setDate', pmsDate);
		 */
		
		$('a#baseUrl').click(function() 
		{
			if($("#txtBookingNo").val().trim()=="")
			{
				alert("Please Select Reservation No ");
				return false;
			}
			window.open('attachDoc.html?transName=frmReservation.jsp&formName=Reservation &code='+$('#txtReservationNo').val(),"mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
		});	
		
		
		
		<%-- var message='';
		var retval="";
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
				%> alert("Data Save successfully\n\n"+message);
				var AdvAmount='';
				var isOk=confirm("Do You Want to pay Advance Amount ?");
				if(isOk)
 				{
					var checkAgainst="Reservation";
					AdvAmount='<%=session.getAttribute("AdvanceAmount").toString()%>';
	    			window.location.href=getContextPath()+"/frmPMSPaymentAdvanceAmount.html?AdvAmount="+AdvAmount ;
	    			session.removeAttribute("AdvanceAmount");
	    			
 				}<%
			}
		}%>
		
		var resNo='';
		<%if (session.getAttribute("ResNo") != null) 
		{
			%>
			resNo='<%=session.getAttribute("ResNo").toString()%>';
			funSetReservationNo(resNo);
		    <%
		    session.removeAttribute("ResNo");
		}%>
		
		
		var roomNo='';
		<%if (session.getAttribute("RoomCode") != null) 
		{
			%>
			roomNo='<%=session.getAttribute("RoomCode").toString()%>';
			funSetRoomNo(roomNo);
		    <%
		    session.removeAttribute("RoomCode");
		}%> --%>
		
		
	});
	 var functionCode=""
	function funHelp(transactionName)
	{
		fieldName=transactionName;
		functionCode=$("#txtFunctionCode").val();
		if(transactionName =="FunctionService")
		{
			if(functionCode.length >0)
			{
				window.open("searchform.html?formname="+transactionName+"&functionCode="+functionCode+"&searchText=","mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
			}
			else
			{
				alert("Please Select Function code");
			}		
			
		}
		else
		{
			window.open("searchform.html?formname="+transactionName+"&functionCode="+functionCode+"&searchText=","mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
			
		}	
		
		//window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
	}

	
	function funSetData(code){

		switch(fieldName){
		
		    case 'custMaster' : 
		    	funSetCustomer(code);
				break;
			case 'PropertyWiseLocation' : 
				funSetAreaCode(code);
				break;
			case 'functionMaster' : 	
				funSetFunctionCode(code)
				break;
			case 'BillingInstCode' : 
				funSetBillingInstCode(code);
				break;
			case 'FunctionService' : 
				 funSetServiceData(code);
				 break;
			case 'equipmentCode' : 
				 funSetEquipmentName(code);
			     break;
			case 'ItemCode' : 
				 funSetItemData(code);
				 break;
			
			case 'StaffCatCode' : 
				funSetCatData(code);
				 break;
		    case 'StaffCode' : 
				funSetStaffData(code);
				 break;
				 
			
				
				
			/* case 'PropertyCode' : 
				funSetPropertyCode(code);
				break;
				
			case 'guestCode' : 
				funSetGuestCode(code);
				break;
				
			case 'CorporateCode' : 
				funSetCorporateCode(code);
				break;
				
			case 'BookingTypeCode' : 
				funSetBookingTypeCode(code);
				break;
				
			case 'BillingInstCode' : 
				funSetBillingInstCode(code);
				break;
				
			case 'BookerCode' : 
				funSetBookerCode(code);
				break;
				
			case 'business' : 
				funSetBusinessSourceCode(code);
				break;
				
			case 'AgentCode' : 
				funSetAgentCode(code);
				break;
				
			case 'roomCode' : 
				funSetRoomNo(code);
				break;
			
			case 'extraBed' : 
				funSetExtraBed(code);
				break;
				
			case 'marketsource' : 
				funSetMarketSourceCode(code);
			break;
			
			case "incomeHead":
				funSetIncomeHead(code);
			break;
			
			case "roomType":
				funSetRoomType(code);
			break;
			
			case "package":
				funSetPackageNo(code);
			break;
			
			case "roomByRoomType":
				funSetRoomNo(code);
			break;
			
			case 'roomByRoomTypeForReservation': 
				funSetRoomNo(code);
				break; */
			
		} 
	}
	function funSetCustomer(code)
	{
		gurl=getContextPath()+"/loadPartyMasterData.html?partyCode=";
		$.ajax({
	        type: "GET",
	        url: gurl+code,
	        dataType: "json",
	    
	        success: function(response)
	        {		        	
	        		if('Invalid Code' == response.strPCode){
	        			alert("Invalid Customer Code");
	        			$("#txtCustomerCode").val('');
	        			$("#txtCustomerCode").focus();
	        			  
	        		}else{
	        			$("#txtCustomerCode").val(response.strPCode);
						$("#txtCustomerName").val(response.strPName);
						$("#txtMobileNo").val(response.strMobile);
						
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
	
	function funSetAreaCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadLocationMasterData.html?locCode=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strLocCode=='Invalid Code')
	        	{
	        		alert("Invalid Location Code");
	        		$("#txtCorporateCode").val('');
	        	}
	        	else
	        	{
	        		$("#txtAreaCode").val(response.strLocCode);
	        		$("#lblAreaCode").text(response.strLocName);
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
	function funSetBillingInstCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadBillingInstCode.html?billingInstructions=" + code,
			dataType : "json",
			success : function(response){
				$("#txtBillingInstructionCode").val(response.strBillingInstCode);
        	    $("#lblBillingInstDesc").text(response.strBillingInstDesc);
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

	// 
	function funSetStaffCode(code)
	{
			var searchUrl="";
			searchUrl=getContextPath()+"/loadStaffMasterData.html?staffCode="+code;			
			$.ajax({
			        type: "GET",
			        url: searchUrl,
				    dataType: "json",
				    success: function(response)
				    {
				    	$("#txtEventCoordinatorCode").val(code);
				    	$("#lblEventCoordinatorCode").val(response.strStaffName);
				    
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
	
	
	function funSetStaffData(code)
	{
			var searchUrl="";
			searchUrl=getContextPath()+"/loadStaffMasterData.html?staffCode="+code;			
			$.ajax({
			        type: "GET",
			        url: searchUrl,
				    dataType: "json",
				    success: function(response)
				    { 
				    	$("#txtEventCoordinatorCode").val(code);
				    	$("#lblEventCoordinatorCode").val(response.strStaffName);
				    	
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
	
	
	
	
	function funSetFunctionCode(code)
	{
		$("#txtFunctionCode").val(code);
		var searchurl=getContextPath()+ "/loadFunctionMasterData.html?functionCode=" + code;
		$.ajax({
			type : "GET",
			url : searchurl,
			dataType : "json",
			success : function(response)
			{ 
				if(response.strFunctionCode=='Invalid Code')
	        	{
	        		alert("Invalid Function Code");
	        		$("#txtFunctionCode").val('');
	        	}
				else
				{
					$("#txtFunctionCode").val(response.strFunctionCode);
	        		$("#lblFunctionName").text(response.strFunctionName);
	        		
	        		
				}

			},
			error : function(jqXHR, exception){
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
	
	function funSetServiceData(ServiceCode)
	{
		var funcode=$("#txtFunctionCode").val();
		var searchurl=getContextPath()+ "/loadFunctionServiceData.html?functionCode=" + funcode +"&ServiceCode="+ServiceCode;
		$.ajax({
			type : "GET",
			url : searchurl,
			dataType : "json",
			success : function(response)
			{ 
				$.each(response, function(i,item)
	                {
				funfillServiceRow(response[i][0],response[i][1],response[i][2]);
			    });
	        	
			},
			error : function(jqXHR, exception){
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
	//  
	
	function funSetEquipmentName(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadEquipmentName.html?docCode=" + code,
			dataType : "json",
			success : function(response){ 

				
				funfillEquipmentDtlRow(response.strEquipmentCode,response.strEquipmentName,response.dblEquipmentRate);
	        		
	        	
			},
			error : function(jqXHR, exception){
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
	//
	function funSetCatData(code)
	{
			var searchUrl="";
			searchUrl=getContextPath()+"/loadStaffCategeoryMasterData.html?staffCatCode="+code;			
			$.ajax({
			        type: "GET",
			        url: searchUrl,
				    dataType: "json",
				    success: function(response)
				    {
				    	funfillStaffCatRow(response.strStaffCategeoryCode,response.strStaffCategeoryName);
				    	
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


	function funSetItemData(code)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadItemCode.html?docCode=" + code,
			dataType : "json",
			success : function(response){ 
				
					funfillMenuItemDtlRow(response.strMenuHeadCode,response.strItemCode,response.strItemName,response.dblAmount);
	        		
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

	function funfillServiceRow(ServiceCode,ServiceName,Rate)
	{
		
		var checked=true;
		var table = document.getElementById("tblServiceDtl");
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);
	    rowCount=listServiceRow;
	    row.insertCell(0).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"15%\" name=\"listSeriveDtl["+(rowCount)+"].strDocNo\"  id=\"txtServiceCode."+(rowCount)+"\" value='"+ServiceCode+"' />";
	    row.insertCell(1).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"40%\" name=\"listSeriveDtl["+(rowCount)+"].strDocName\"  id=\"txtServiceName."+(rowCount)+"\" value='"+ServiceName+"'/>";
	    row.insertCell(2).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"25%\" style=\"padding-right: 5px;text-align: right;\"  name=\"listSeriveDtl["+(rowCount)+"].dblDocRate\"  id=\"txtServiceRate."+(rowCount)+"\" value='"+Rate+"'/>";
	    row.insertCell(3).innerHTML= '<input type="button" class="deletebutton" value = "Delete"  onClick="Javacsript:funDeleteRow(this)">';	   
	    listServiceRow++;
	     
	    
	}

	function funfillEquipmentDtlRow(EquipCode,EquipName,EquipRate)
	{
		
		var checked=true;
		var table = document.getElementById("tblEquipDtl");
	    var rowCount = table.rows.length;
	    rowCount=listEquipRow;
	    var row = table.insertRow(rowCount);
	    row.insertCell(0).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"8%\" name=\"listEquipDtl["+(rowCount)+"].strDocNo\"  id=\"txtEquipCode."+(rowCount)+"\" value='"+EquipCode+"' />";
	    row.insertCell(1).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"27%\" name=\"listEquipDtl["+(rowCount)+"].strDocName\"  id=\"txtEquipName."+(rowCount)+"\" value='"+EquipName+"'/>";
	    row.insertCell(2).innerHTML= "<input   class=\" decimal-places-amt\" size=\"5%\" name=\"listEquipDtl["+(rowCount)+"].dblDocQty\"  id=\"txtEquipQty."+(rowCount)+"\" value='1'/>";
	    row.insertCell(3).innerHTML= "<input  readonly=\"readonly\" class=\"Box \" style=\"padding-right: 5px;text-align: right;\" size=\"25%\" name=\"listEquipDtl["+(rowCount)+"].dblDocRate\"  id=\"txtEquipRate."+(rowCount)+"\" value='"+EquipRate+"'/>";
	    row.insertCell(4).innerHTML= '<input type="button" class="deletebutton" value = "Delete" onClick="Javacsript:funDeleteRow(this)">';		    
	    listEquipRow++;
	     
	    
	}
	
	function funfillStaffCatRow(StaffCatCode,StaffCatName)
	{
		var checked=true;
		var table = document.getElementById("tblStaffCatDtl");
	    var rowCount = table.rows.length;
	    rowCount=listStaffRow;
	    var row = table.insertRow(rowCount);
	    row.insertCell(0).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"8%\" name=\"listStaffCatDtl["+(rowCount)+"].strDocNo\"  id=\"txtStaffCatCode."+(rowCount)+"\" value='"+StaffCatCode+"' />";
	    row.insertCell(1).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"27%\" name=\"listStaffCatDtl["+(rowCount)+"].strDocName\"  id=\"txtStaffCatName."+(rowCount)+"\" value='"+StaffCatName+"'/>";
	    row.insertCell(2).innerHTML= "<input   class=\" decimal-places-amt\" size=\"5%\" name=\"listStaffCatDtl["+(rowCount)+"].dblDocQty\"  id=\"txtStaffCatNumber."+(rowCount)+"\" value='0'/>";
	    row.insertCell(3).innerHTML= '<input type="button" class="deletebutton" value = "Delete" onClick="Javacsript:funDeleteRow(this)">';		    
	    listStaffRow++;
	    
	}
	function funfillMenuItemDtlRow(MenuCode,ItemCode,ItemName,ItemRate)
	{
		var table = document.getElementById("tblMenuDtl");
	    var rowCount = table.rows.length;
	    rowCount=listItemRow;
	    var row = table.insertRow(rowCount);
	    row.insertCell(0).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"8%\"   id=\"txtMenuCode."+(rowCount)+"\" value='"+MenuCode+"' />";
	    row.insertCell(1).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"8%\" name=\"listMenuItemDtl["+(rowCount)+"].strDocNo\"  id=\"txtItemCode."+(rowCount)+"\" value='"+ItemCode+"'/>";
	    row.insertCell(2).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"27%\" name=\"listMenuItemDtl["+(rowCount)+"].strDocName\"  id=\"txtItemName."+(rowCount)+"\" value='"+ItemName+"'/>";
	    row.insertCell(3).innerHTML= "<input  class=\"decimal-places-amt\" size=\"5%\" name=\"listMenuItemDtl["+(rowCount)+"].dblDocQty\"  id=\"txtItemQty."+(rowCount)+"\" value='1'/>";
	    row.insertCell(4).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"15%\" style=\"padding-right: 5px;text-align: right;\"  name=\"listMenuItemDtl["+(rowCount)+"].dblDocRate\"  id=\"txtServiceName."+(rowCount)+"\" value='"+ItemRate+"'/>";
	    row.insertCell(5).innerHTML= '<input type="button" class="deletebutton" value = "Delete" onClick="Javacsript:funDeleteRow(this)">';	

	    listItemRow++;
	     
	    
	}
	function funDeleteRow(obj)
	{
		
		switch('tblServiceDtl'){
		
	    case 'tblServiceDtl' : 
	    	 var index = obj.parentNode.parentNode.rowIndex;
	 	     var table = document.getElementById("tblServiceDtl");
	 	     table.deleteRow(index);
			 break;
		case 'tblEquipDtl' : 
			 var index = obj.parentNode.parentNode.rowIndex;
			 var table = document.getElementById("tblEquipDtl");
			 table.deleteRow(index);
			 break;
		case 'tblStaffCatDtl' : 	
			 var index = obj.parentNode.parentNode.rowIndex;
			 var table = document.getElementById("tblStaffCatDtl");
			 table.deleteRow(index);
			 break;
		case 'tblMenuDtl' : 
			 var index = obj.parentNode.parentNode.rowIndex;
			 var table = document.getElementById("tblMenuDtl");
			 table.deleteRow(index);
			 break;
	   
	}
	}
<%-- 	
	function funSetReservationNo(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadReservation.html?docCode=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strReservationNo!="Invalid")
		    	{
		    		funFillHdData(response);
		    	}
		    	else
			    {
			    	alert("Invalid Reservation No");
			    	$("#txtReservationNo").val("");
			    	$("#txtReservationNo").focus();
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

	
	function funSetPropertyCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadPropertyCode.html?docCode=" + code,
			dataType : "json",
			success : function(response){ 
				
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

	
	function funSetGuestCode(code){
			
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadGuestCode.html?guestCode=" + code,
			dataType : "json",
			success : function(response){ 
				funSetGuestInfo(response);
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

	
	function funSetCorporateCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadCorporateCode.html?corpcode=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strCorporateCode=='Invalid Code')
	        	{
	        		alert("Invalid Agent Code");
	        		$("#txtCorporateCode").val('');
	        	}
	        	else
	        	{
	        		$("#txtCorporateCode").val(response.strCorporateCode);
	        		$("#lblCorporateDesc").text(response.strCorporateDesc);
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

	
	function funSetBookingTypeCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadBookingTypeCode.html?bookingType=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strBookingTypeCode=='Invalid Code')
	        	{
	        		alert("Invalid Agent Code");
	        		$("#txtBookingTypeCode").val('');
	        	}
	        	else
	        	{
	        		$("#txtBookingTypeCode").val(response.strBookingTypeCode);
	        	    $("#lblBookingTypeDesc").text(response.strBookingTypeDesc);
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


	function funSetBillingInstCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadBillingInstCode.html?billingInstructions=" + code,
			dataType : "json",
			success : function(response){
				$("#txtBillingInstCode").val(response.strBillingInstCode);
        	    $("#lblBillingInstDesc").text(response.strBillingInstDesc);
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

	
	function funSetBookerCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadBookerCode.html?bookerCode=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strBookerCode=='Invalid Code')
	        	{
	        		alert("Invalid Agent Code");
	        		$("#txtBookerCode").val('');
	        	}
	        	else
	        	{
	        		$("#txtBookerCode").val(response.strBookerCode);
	        		$("#lblBookerName").text(response.strBookerName);
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

	
	function funSetBusinessSourceCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadBusinessMasterData.html?businessCode=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strBusinessSourceCode=='Invalid Code')
	        	{
	        		alert("Invalid Business Code");
	        		$("#txtBusinessSourceCode").val('');
	        	}
	        	else
	        	{
	        		$("#txtBusinessSourceCode").val(response.strBusinessSourceCode);
		        	$("#lblBusinessSourceName").text(response.strDescription);
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

	
	function funSetAgentCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadAgentCode.html?agentCode=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strAgentCode=='Invalid Code')
	        	{
	        		alert("Invalid Agent Code");
	        		$("#txtAgentCode").val('');
	        	}
	        	else
	        	{
	        		$("#txtAgentCode").val(response.strAgentCode);
	        		$("#lblAgentName").text(response.strDescription);
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

	
	
	function funSetRoomType(code){
		$("#txtRoomTypeCode").val(code);
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadRoomTypeMasterData.html?roomCode=" + code,
			dataType : "json",
		    async:false,
			success : function(response){ 
				if(response.strAgentCode=='Invalid Code')
	        	{
	        		alert("Invalid Room Type");
	        		$("#lblRoomType").text('');
	        	}
	        	else
	        	{					        	    	        		
	        		$("#lblRoomType").text(response.strRoomTypeDesc);
	        		alert("Room Rate is "+response.dblRoomTerrif);
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
	
	
	
	function funSetRoomNo(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadRoomMasterData.html?roomCode=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strAgentCode=='Invalid Code')
	        	{
	        		alert("Invalid Room No");
	        		$("#txtRoomNo").val('');
	        	}
	        	else
	        	{
	        		$("#txtRoomNo").val(response.strRoomCode);
	        		$("#lblRoomNo").text(response.strRoomDesc);
	        		alert("Room Rate is "+response.strRoomDesc);
	        		funSetRoomType(response.strRoomTypeCode);
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
	
	
	
	function funSetExtraBed(code)
	{
		$("#txtExtraBed").val(code);
		var searchurl=getContextPath()+"/loadExtraBedMasterData.html?extraBedCode="+code;
		 $.ajax({
			    type: "GET",
			    url: searchurl,
			    dataType: "json",
			    success: function(response)
			    {
			        if(response.strExtraBedTypeCode=='Invalid Code')
			        {
			        	alert("Invalid ExtraBed Code");
			        	$("#txtExtraBed").val('');
			        }
			        else
			        {
				        $("#lblExtraBed").text(response.strExtraBedTypeDesc);
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
	
	function funSetRoomNo(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadRoomMasterData.html?roomCode=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strRoomCode=='Invalid Code')
	        	{
	        		alert("Invalid Room No");
	        		$("#txtRoomNo").val('');
	        	}
	        	else
	        	{
	        		if(response.strStatus=='Blocked')
	        			{
	        			alert('This room is Blocked Please Select Different Room');
	        			}
	        		else
	        		{
	        			funSetRoom(response.strRoomCode)
	        		/* $("#txtRoomNo").val(response.strRoomCode);
	        		$("#lblRoomNo").text(response.strRoomDesc);
	        		funSetRoomType(response.strRoomTypeCode); */
	        		}
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
	
	function funSetRoom(roomCode)
	{
		var arrivalDate = $("#txtArrivalDate").val();
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/setRoomCode.html?roomCode=" + roomCode+"&dteArrDate="+arrivalDate,
			dataType : "json",
			success : function(response){ 
				if(response.strRoomCode=='Invalid')
	        	{
	        		alert("This Room is booked for "+arrivalDate+" ");
	        		$("#txtRoomNo").val('');
	        	}
	        	
	        		else
	        		{
	        			
	        		$("#txtRoomNo").val(response.strRoomCode);
	        		$("#lblRoomNo").text(response.strRoomDesc);
	        		funSetRoomType(response.strRoomTypeCode);
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
	
	function funSetPackageNo(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadPackageMaster.html?docCode=" + code,
			dataType : "json",
			success : function(response)
			{ 
				if(response.strPackageCode!="Invalid")
		    	{
					$("#txtPackageCode").val(response.strPackageCode);
					var table = document.getElementById("tblResDetails");
				    var rowCount = table.rows.length;
					if(rowCount==0)
					{
						alert("Please Enter Guest For Reservation");
					}
					else
					{
						$("#txtPackageName").val(response.strPackageName);
						$.each(response.listPackageDtl, function(i,item)
						{
							funAddIncomeHeadRow(item.strIncomeHeadCode.split('#')[0],item.strIncomeHeadCode.split('#')[1],item.dblAmt);		
						});
					}
		    	}
		    	else
			    {
			    	alert("Invalid Package No");
			    	$("#txtPackageCode").val(code);
			    	$("#txtPackageCode").focus();
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
	
	
	
	function funSetGuestInfo(obj)
	{
		$("#txtGuestCode").val(obj.strGuestCode);
		$("#txtMobileNo").val(obj.lngMobileNo);
		$("#txtGFirstName").val(obj.strFirstName);
		$("#txtGMiddleName").val(obj.strMiddleName);
		$("#txtGLastName").val(obj.strLastName);
		$("#txtAddress").val(obj.strAddress);
		
	}
	
	
	
	function funFillHdData(response)
	{
		$("#txtReservationNo").val(response.strReservationNo);
		$("#txtPropertyCode").val(response.strPropertyCode);
		
		$("#txtGuestCode").val(response.strGuestCode);
		$("#txtGuestPrefix").val(response.strGuestPrefix);
		$("#txtGFirstName").val(response.strFirstName);
		$("#txtGMiddleName").val(response.strMiddleName);
		$("#txtGLastName").val(response.strLastName);
		$("#txtAddress").val(response.strAddress);
		
		
		if(response.strCorporateCode!='')
		{
			$("#txtCorporateCode").val(response.strCorporateCode);
			funSetCorporateCode(response.strCorporateCode);
		}
		
		if(response.strBookingTypeCode!='')
		{
			$("#txtBookingTypeCode").val(response.strBookingTypeCode);
			funSetBookingTypeCode(response.strBookingTypeCode);
		}
		
		if(response.strBillingInstCode!='')
		{
			$("#txtBillingInstCode").val(response.strBillingInstCode);
			funSetBillingInstCode(response.strBillingInstCode);
		}
		
		if(response.strBookerCode!='')
		{
			$("#txtBookerCode").val(response.strBookerCode);
			funSetBookerCode(response.strBookerCode);
		}
		
		if(response.strBusinessSourceCode!='')
		{
			$("#txtBusinessSourceCode").val(response.strBusinessSourceCode);
			funSetBusinessSourceCode(response.strBusinessSourceCode);
		}
		
		if(response.strAgentCode!='')
		{
			$("#txtAgentCode").val(response.strAgentCode);
			funSetAgentCode(response.strAgentCode);
		}
		
		$("#txtRoomNo").val(response.strRoomNo);
		$("#lblRoomNo").text(response.strRoomDesc);
	    $("#txtExtraBed").val(response.strExtraBedCode);
	    $("#lblExtraBed").text(response.strExtraBedDesc);	    
	    $("#txtNoOfAdults").val(response.intNoOfAdults);
	    $("#txtNoOfChild").val(response.intNoOfChild);
				
		$("#txtArrivalDate").val(response.dteArrivalDate);
		$("#txtDepartureDate").val(response.dteDepartureDate);

		$("#txtArrivalTime").val(response.tmeArrivalTime);
		$("#txtDepartureTime").val(response.tmeDepartureTime);
		$("#txtNoOfNights").val(response.intNoOfNights);
		$("#txtNoOfBookingRoom").val(response.intNoRoomsBooked);
		$("#txtContactPerson").val(response.strContactPerson);
		$("#txtEmailId").val(response.strEmailId);
		$("#txtMobileNo").val(response.lngMobileNo);
		
		$("#txtRemarks").val(response.strRemarks);
		$("#txtCancelDate").val(response.dteCancelDate);
		$("#txtConfirmDate").val(response.dteConfirmDate);
		$("#txtOTANo").text(response.strOTANo);
		
		$("#txtPickUpTime").val(response.tmePickUpTime);
		$("#txtDropTime").text(response.tmeDropTime);
		
		$("#txtPackageCode").val(response.strPackageCode);
		$("#txtPackageName").val(response.strPackageName);
		      
		$("#hidIncomeHead").val("");
		funRemoveProductRowsForIncomeHead();
		funRemoveTariffRows();
		funFillDtlGrid(response.listReservationDetailsBean);
		funAddRommRateDtlOnReservationSelect(response.listReservationRoomRateDtl);
		funGetPreviouslyLoadedPkgList(response.listRoomPackageDtl);
		/*var incomeHeadCode =response.strIncomeHeadCode;
		if(incomeHeadCode!='')
		{
		var incomeHead = incomeHeadCode.split(',');
		for(var i=0; i<incomeHead.length; i++)
		{
		funSetIncomeHead(incomeHead[i]);
		}
		}
		*/
	}
	
	function funAddRommRateDtlOnReservationSelect(dataList)
	{
		 var table=document.getElementById("tblRommRate");
		 
		 for(var i=0;i<dataList.length;i++ )
	     {
			 var rowCount=table.rows.length;
			 var row=table.insertRow();
			 var list=dataList[i];
			 var date=list.dtDate;
	// 		 var roomDesc = funGetRoomDescOnLoad(list.strRoomNo);
		     var roomtypeDesc=funSetRoomType(list.strRoomType);
			 var roomtypeDesc=$("#lblRoomType").text();
			 $("#lblRoomType").text("");
			 var dateSplit = date.split("-");
			 date=dateSplit[2]+"-"+dateSplit[1]+"-"+dateSplit[0];
			 row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-center: 5px;width:50%;\" name=\"listReservationRoomRateDtl["+(rowCount)+"].dtDate\"  id=\"dtDate."+(rowCount)+"\" value='"+date+"' >";
	 	     row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\" size=\"15%\" id=\"strRoomTypeDesc."+(rowCount)+"\" value='"+roomtypeDesc+"' />";
	 	     row.insertCell(2).innerHTML= "<input type=\"text\" style=\"text-align:right;\"  name=\"listReservationRoomRateDtl["+(rowCount)+"].dblRoomRate\" id=\"dblRoomRate."+(rowCount)+"\" onblur =\"Javacsript:funCalculateTotals()\" value='"+list.dblRoomRate+"' >";
	 	     row.insertCell(3).innerHTML= "<input type=\"hidden\" class=\"Box \" name=\"listReservationRoomRateDtl["+(rowCount)+"].strRoomType\" id=\"strRoomType."+(rowCount)+"\" value='"+list.strRoomType+"' >";
	 	    
	     }
	}

	function funSetIncomeHead(code)
	{
		$("#txtIncomeHeadCode").val(code);
		var searchurl=getContextPath()+"/loadIncomeHeadMasterData.html?incomeCode="+code;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(response.strIncomeHeadCode=='Invalid Code')
			        	{
			        		alert("Invalid Income Head Code");
			        		$("#txtIncomeHeadCode").val('');
			        	}
			        	else
			        	{
			        		funfillIncomHeadGrid(response);
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
	
	function funfillIncomHeadGrid(data){
		
		$("#txtIncomeHead").val(data.strIncomeHeadCode);
		$("#txtIncomeHeadName").val(data.strIncomeHeadDesc);
	 
		//funAddIncomeHeadRow();
		
		//$("#txtIncomeHead").val('');
		//$("#txtIncomeHeadName").text('');
	
	}
	
	

	function funFillDtlGrid(resListResDtlBean)
		{
			funRemoveProductRows();
			var count=0;
			$.each(resListResDtlBean, function(i,item)
			{
				count=i;
				var roomDesc="";
				var roomtypeDesc=funSetRoomType(resListResDtlBean[i].strRoomType);
				var roomtypeDesc=$("#lblRoomType").text();
				$("#lblRoomType").text("");
				funAddDetailsRow(resListResDtlBean[i].strGuestName,resListResDtlBean[i].strGuestCode,resListResDtlBean[i].lngMobileNo
					,resListResDtlBean[i].strRoomType,"",resListResDtlBean[i].strRoomNo,roomDesc
					,resListResDtlBean[i].strExtraBedCode,resListResDtlBean[i].strExtraBedDesc,resListResDtlBean[i].strPayee,"",roomtypeDesc);
			});
			listRow=count+1;
		}


	function funGetRoomDescOnLoad(roomNo) 
		{
			var returnVal =0;
			var searchurl=getContextPath()+"/loadRoomDesc.html?roomNo="+roomNo;
			$.ajax({
		        type: "GET",
		        url: searchurl,
		        dataType: "json",
		        async:false,
				success: function(response)
		        {
		        	returnVal = response;
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
			
			return returnVal;
		}
	
	
	
//Delete a All record from a grid
	function funRemoveProductRows()
	{
		var table = document.getElementById("tblResDetails");
		var rowCount = table.rows.length;
		while(rowCount>0)
		{
			table.deleteRow(0);
			rowCount--;
		}
	}
	
	
//Function to Delete Selected Row From Grid
	function funDeleteRow(obj)
	{
	    var index = obj.parentNode.parentNode.rowIndex;
	    var table = document.getElementById("tblResDetails");
	    table.deleteRow(index);
	}
	
	
	
// Reset Detail Fields
	function funResetDetailFields()
	{
		//$("#txtGuestCode").val('');
		//$("#txtMobileNo").val('');
		//$("#txtGFirstName").val('');
		//$("#txtGMiddleName").val('');
		//$("#txtGLastName").val('');
		//$("#txtAddress").val('');
		
		//$("#lblRoomType").text('');
	    $("#txtRoomNo").val('');
	    //$("#lblRoomNo").text('');
	    $("#txtExtraBed").val('');
	    //$("#lblExtraBed").text('');	
	    $("#txtRemark").text('');	
	    $("#txtRoomTypeCode").val('');
	    
	    
	}
	
	//Delete a All record from a grid
	function funRemoveTariffRows()
	{
		var table = document.getElementById("tblRommRate");
		var rowCount = table.rows.length;
		while(rowCount>0)
		{
			table.deleteRow(0);
			rowCount--;
		}
	}
	
	
	

// Get Detail Info From detail fields and pass them to function to add into detail grid
	function funGetDetailsRow() 
	{
	/*
		var table = document.getElementById("tblResDetails");
		var rowCount = table.rows.length;
		if(rowCount>0)
		{
			alert('Only One Guest is Allowed at a time.');
			return;
		}*/
		
		
		if($("#txtRoomTypeCode").val()=='')
		{
			alert('Select RoomType!!');
		}
		else
		{
			var gCodeval=$("#txtGuestCode").val().trim();
			
			if(gCodeval==''){
				
				var gCode = funGetGuestCode(gCodeval);
				$("#txtGuestCode").val(gCode.strGuestCode);
				
			}
		
			var guestCode=$("#txtGuestCode").val().trim();
			var mobileNo=$("#txtMobileNo").val().trim();
			var guestName=$("#txtGFirstName").val().trim()+" "+$("#txtGMiddleName").val().trim()+" "+$("#txtGLastName").val().trim();
			var roomType =$("#txtRoomTypeCode").val();
			var roomNo =$("#txtRoomNo").val();
			var roomDesc =$("#lblRoomNo").text().trim();
			var extraBedCode=$("#txtExtraBed").val();
			var extraBedDesc=$("#lblExtraBed").text();
			var remark=$("#txtRemark").val();
			var address=$("#txtAddress").val();
			var roomTypeDesc=$("#lblRoomType").text();
			if(roomTypeDesc=='')
			{
				funSetRoomType($("#txtRoomTypeCode").val().trim());
				roomTypeDesc=$("#lblRoomType").text();
			}
			
			if(mobileNo=='')
			{
				alert('Enter Mobile No!!!');
				$("#txtMobileNo").focus();
				return;
			}
			else
			{
				/*var phoneno = /^\d{10}$/;
				if((mobileNo.match(phoneno)))
				{
				}
				else
				{
					alert("Invalid Mobile No");
				    return;
				}
				*/
				
				var pattern = /^[\s()+-]*([0-9][\s()+-]*){6,20}$/;
				if (pattern.test(mobileNo)) 
				{
				}
				else
				{
					alert("Invalid Mobile No");
				    return;
				}	
			}
			
			
			
			
			if($("#txtGFirstName").val().trim()=='')
			{
				alert('Enter Guest First Name!!!');
				$("#txtGFirstName").focus();
				return;
			}
			
			funAddDetailsRow(guestName,guestCode,mobileNo,roomType,remark,roomNo,roomDesc,extraBedCode,extraBedDesc,"N",address,roomTypeDesc);

			funFillRoomRate(roomType,roomDesc);
		}	
	}
	
	
	function funGetDetailsRowinGrid(ArrivalDate, DepartureDate) 
	{
		var returnVal =0;
		var searchurl=getContextPath()+"/loadRoomLimit.html?ArrivalDate="+ArrivalDate+"&DepartureDate="+DepartureDate;
		$.ajax({
	        type: "GET",
	        url: searchurl,
	        dataType: "json",
	        async:false,
			success: function(response)
	        {
	        	returnVal = response;
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
		
		return returnVal;
	}
	
	
// Function to add detail grid rows	
	function funAddDetailsRow(guestName,guestCode,mobileNo,roomType,remark,roomNo,roomDesc,extraBedCode,extraBedDesc,payee,address,roomTypeDesc)
	{
	    var table = document.getElementById("tblResDetails");
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);
	    rowCount=listRow;

	    row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box\" size=\"45%\" name=\"listReservationDetailsBean["+(rowCount)+"].strGuestName\" id=\"strGuestName."+(rowCount)+"\" value='"+guestName+"' />";
	    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\" size=\"10%\" name=\"listReservationDetailsBean["+(rowCount)+"].lngMobileNo\" id=\"lngMobileNo."+(rowCount)+"\" value='"+mobileNo+"' />";
	    
	    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box\" size=\"10%\"  id=\"strRoomType."+(rowCount)+"\" value='"+roomTypeDesc+"' />";
	    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box\" size=\"20%\" name=\"listReservationDetailsBean["+(rowCount)+"].strRemark\" id=\"strRemark."+(rowCount)+"\" value='"+remark+"' />";
	    
	    if(payee=='Y')
	    	{
	    		row.insertCell(4).innerHTML= "<input id=\"cbItemCodeSel."+(rowCount)+"\" type=\"radio\" checked=\"checked\" class=\"Box payeeSel\" name=\"listReservationDetailsBean.strPayee\" size=\"2%\"   value=\"Y\" onClick=\"Javacsript:funRadioRow(this)\"  />";
	    	}else
	    	{
	    		row.insertCell(4).innerHTML= "<input id=\"cbItemCodeSel."+(rowCount)+"\" type=\"radio\" class=\"Box payeeSel\" name=\"listReservationDetailsBean.strPayee\" size=\"2%\" value=\"N\" onClick=\"Javacsript:funRadioRow(this)\"  />";
	    	}
	    
	    row.insertCell(5).innerHTML= "<input type=\"button\" class=\"deletebutton\" size=\"2%\" value = \"Delete\" onClick=\"Javacsript:funDeleteRow(this)\"/>";
	    
	    row.insertCell(6).innerHTML= "<input type=\"hidden\"  name=\"listReservationDetailsBean["+(rowCount)+"].strGuestCode\" id=\"strGuestCode."+(rowCount)+"\" value='"+guestCode+"' />";
	    row.insertCell(7).innerHTML= "<input type=\"hidden\" size=\"0%\" name=\"listReservationDetailsBean["+(rowCount)+"].strRoomNo\" id=\"strRoomNo."+(rowCount)+"\" value='"+roomNo+"' />";
	    row.insertCell(8).innerHTML= "<input type=\"hidden\" size=\"0%\" name=\"listReservationDetailsBean["+(rowCount)+"].strExtraBedCode\" id=\"strExtraBedCode."+(rowCount)+"\" value='"+extraBedCode+"' />";
	    row.insertCell(9).innerHTML= "<input type=\"hidden\" size=\"0%\" name=\"listReservationDetailsBean["+(rowCount)+"].strAddress\" id=\"strAddress."+(rowCount)+"\" value='"+address+"' />";
	    row.insertCell(10).innerHTML= "<input type=\"hidden\" size=\"0%\" id=\"strRoomDesc."+(rowCount)+"\" value='' />";
	    row.insertCell(11).innerHTML= "<input type=\"hidden\"  size=\"0%\" id=\"strExtraBedDesc."+(rowCount)+"\" value='"+extraBedDesc+"' />";
	    row.insertCell(12).innerHTML= "<input type=\"hidden\" size=\"0%\" name=\"listReservationDetailsBean["+(rowCount)+"].strRoomType\" id=\"strRoomType."+(rowCount)+"\" value='"+roomType+"' />";
	    funResetDetailFields();
	    
	    if(payee=='Y')
	    	{
	    	 	$("#hidPayee").val(guestCode);
	    	}
	    
	    listRow++;
	    
	   
	}

	function funRadioRow(rowObj)
	{
		/* $( "input[type='radio']" ).prop({
			checked: false
			}); */
			var no=0;
			$('#tblResDetails tr').each(function() {
				
				if(document.getElementById("cbItemCodeSel."+no).checked == true)
					{
					  var gustcode = document.getElementById("strGuestCode."+no).value;
					  $("#hidPayee").val(gustcode);
					}
				no++;
			});
	}

	function funHelp(transactionName)
	{
		fieldName=transactionName;
		window.open("searchform.html?formname="+transactionName+"&searchText=","mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
		//window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
	}
	
	function funHelp1(transactionName,row)
	{
		gridHelpRow=row;
		fieldName=transactionName;
		var condition = $("#txtRoomTypeCode").val();
		if(condition=='')
			{
			alert("Please Select Room Type")
			}
		else
			{
			if(transactionName=="roomByRoomTypeForReservation" && condition!=" ")
			{
				window.open("searchform.html?formname="+fieldName+"&strRoomTypeCode="+condition+"&searchText=","mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
			
			}
			else
			{
				if(condition==" ")
				{
					alert("Please Select Room Type !!!");
				}
				//window.open("searchform.html?formname="+transactionName+"&searchText=","mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
			}
			}
		
	}
		
	
	function funValidateForm()
	{
		var flg=false;
		if($("#txtBookingTypeCode").val()=='')
		{
			alert("Please Select Booking Type");
			flg=false;
		}
		else
		{
			if($('#txtEmailId').val()=='')
			{
				flg=true;
			}
			else
			{
				var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;

		        if (reg.test($('#txtEmailId').val()) == false) 
		        {
		            alert('Invalid Email Address');
		            flg=false;
		        }	
			}
			
			
			if($("#txtArrivalTime").val()=='')
			{
				alert("Please Enter Arrival Time");
				flg=false;
			}
			
			if($("#txtDepartureTime").val()=='')
			{
				alert("Please Enter Departure Time");
				flg=false;
			}
			
//	 		if($("#hidPayee").val()=='')
//	 		{
//	 			alert("Please Select One Payee");
//	 			return false;
//	 		}
		/* 	if($("#txtRoomNo").val()=='')
			{
				alert('Select Room No!!!');
				$("#txtRoomNo").focus();
				return false;
			}
			 */
			if($("#txtNoOfAdults").val()=='')
			{
				alert('Enter No of Adults!!!');
				$("#txtNoOfAdults").focus();
				flg=false;
			}
			
			if($("#txtNoOfChild").val()=='')
			{
				alert('Enter No of Child!!!');
				$("#txtNoOfChild").focus();
				flg=false;
			}
			
			var table = document.getElementById("tblResDetails");
		    var rowCount = table.rows.length;
			if(rowCount==0)
			{
				alert("Please Enter Guest For Reservation");
				flg=false;
			}
			
			var ArrivalDate = $("#txtArrivalDate").val();
			var DepartureDate = $("#txtDepartureDate").val();
			var roomLimitCount = funGetDetailsRowinGrid(ArrivalDate, DepartureDate)

			if(roomLimitCount == "0"){
				alert("Room Limit exceed for today");
				flg=false;
			}

			var ArrivalDate = new Date($("#txtArrivalDate").val()); //Year, Month, Date
	        var DepartureDate = new Date($("#txtDepartureDate").val()); //Year, Month, Date
	        if (ArrivalDate > DepartureDate) {
			    	alert("Departure Date Should not be come before Arrival Date");
			    	flg=false;
	        }
	        
	        if(document.getElementById("tblIncomeHeadDtl").rows.length>0)
	        {
	        	var table = document.getElementById("tblTotalPackageDtl");
			    var rowCount = table.rows.length;
				if(rowCount>0)
				{
					if($("#txtPackageName").val()=='')
					 {
						alert("Please Enter Package Name");
						flg=false;
					 }
				}
	        }
	        
	        
			
		}	
		
		
				
		return flg;
	}
	

	function funSetMarketSourceCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadMarketMasterData.html?marketCode=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strMarketSourceCode=='Invalid Code')
	        	{
	        		alert("Invalid Business Code");
	        		$("#txtMarketSourceCode").val('');
	        	}
	        	else
	        	{
	        		$("#txtMarketSourceCode").val(response.strMarketSourceCode);
		        	$("#lblMarketSourceName").text(response.strDescription);
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

// 	//set Reservation Data
// 	function funSetIncomeHeadData(incomeCode)
// 	{
// 		$("#txtIncomeHead").val(incomeCode);
// 		var searchUrl=getContextPath()+"/loadIncomeHeadMasterData.html?incomeCode="+incomeCode;
// 		$.ajax({
			
// 			url:searchUrl,
// 			type :"GET",
// 			dataType: "json",
// 	        success: function(response)
// 	        {
// 	        	if(response.strIncomeHeadCode=='Invalid Code')
// 	        	{
// 	        		alert("Invalid Reservation No.");
// 	        		$("#txtIncomeHead").val('');
// 	        	}
// 	        	else
// 	        	{
// 	        		$("#txtIncomeHead").val(response.strIncomeHeadCode);
// 	        		$("#lblIncomeHeadName").text(response.strIncomeHeadDesc);
// 	        	}
// 			},
// 			error: function(jqXHR, exception) 
// 			{
// 	            if (jqXHR.status === 0) {
// 	                alert('Not connect.n Verify Network.');
// 	            } else if (jqXHR.status == 404) {
// 	                alert('Requested page not found. [404]');
// 	            } else if (jqXHR.status == 500) {
// 	                alert('Internal Server Error [500].');
// 	            } else if (exception === 'parsererror') {
// 	                alert('Requested JSON parse failed.');
// 	            } else if (exception === 'timeout') {
// 	                alert('Time out error.');
// 	            } else if (exception === 'abort') {
// 	                alert('Ajax request aborted.');
// 	            } else {
// 	                alert('Uncaught Error.n' + jqXHR.responseText);
// 	            }
// 	        }
// 		});
// 	}
	
	function funAddRow()
	{
		var flag=false;
		
		
		if($("#txtIncomeHead").val().trim().length==0)
		{
			alert("Please Select Income Head.");
		}
		
		if($("#txtIncomeHead").val().trim().length==0)
		{
			alert("Please Select Income Head.");
		}
		else
		{
			if($("#txtIncomeHeadAmt").val().trim().length==0)
			{
				alert("Please Enter Amount For Income Head.");
			}
			else
			{
				flag=true;
				var table = document.getElementById("tblResDetails");
			    var rowCount = table.rows.length;
				if(rowCount==0)
				{
					alert("Please Enter Guest For Reservation");
					
				}
				else
				{
					flag=true;
					funAddIncomeHeadRow($("#txtIncomeHead").val(),$("#txtIncomeHeadName").val(),$("#txtIncomeHeadAmt").val());
					$("#txtIncomeHead").val("");
					$("#txtIncomeHeadName").val("");
					$("#txtIncomeHeadAmt").val("");
				}
				
			}
		}		
		return flag;
	}
	
	function funGetPreviouslyLoadedPkgList(resPackageIncomeHeadList)
	{
		$.each(resPackageIncomeHeadList, function(i,item)
		 {
	 		funAddIncomeHeadRow(item.strIncomeHeadCode,item.strIncomeHeadName,item.dblIncomeHeadAmt);
	 	 });
		
	}
	
	
	
	
	//add income head
	function funAddIncomeHeadRow(incomeHeadCode,incomeHeadName,incomeHeadAmt)
	{
		/*var incomeHeadCode=$("#txtIncomeHead").val();
		var incomeHeadName=$("#txtIncomeHeadName").val();
		var incomeHeadAmt=$("#txtIncomeHeadAmt").val(); 
		*/var amount="0.0";
		
		var flag=false;
		flag=funChechDuplicate(incomeHeadCode);
		if(flag)
		{
			alert("Already Added.");
		}
		else
		{
			var table=document.getElementById("tblIncomeHeadDtl");
			var rowCount=table.rows.length;
			var row=table.insertRow();
			
			var incomehead = $("#hidIncomeHead").val();
			
			if(!incomehead==''){
				incomehead=incomehead+","+incomeHeadCode;
				 $("#hidIncomeHead").val(incomehead);
			}
			else{
				$("#hidIncomeHead").val(incomeHeadCode);
				
			}
			
			
			 
	 	    row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width:50%;\"  name=\"listRoomPackageDtl["+(rowCount)+"].strIncomeHeadCode\"    id=\"strIncomeHeadCode."+(rowCount)+"\" value='"+incomeHeadCode+"' >";
	 	    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width:50%;\" name=\"listRoomPackageDtl["+(rowCount)+"].strIncomeHeadName\"   id=\"strIncomeHeadDesc."+(rowCount)+"\" value='"+incomeHeadName+"' >";
	 	    row.insertCell(2).innerHTML= "<input type=\"readonly\"   class=\"Box \"  style=\"text-align:right;\" name=\"listRoomPackageDtl["+(rowCount)+"].dblIncomeHeadAmt\"   id=\"dblIncomeRate."+(rowCount)+"\" value='"+incomeHeadAmt+"' >";
	 	    row.insertCell(3).innerHTML= "<input type=\"button\" value=\"Delete\" style=\"padding-right: 5px;width:80%;text-align: right;\" class=\"deletebutton\" onclick=\"funRemoveRow(this)\" />";
					
		//calculate totals
			funCalculateTotals();
		
			//$("#txtIncomeHead").val('');
			//$("#dblIncomeHeadAmt").val('');
		}
	}
	var totalTarriff=0;
	//check duplicate value
	function funChechDuplicate(incomeHeadCode)
	{
		var flag=false;
		var table=document.getElementById("tblIncomeHeadDtl");
		var rowCount=table.rows.length;
		if(rowCount>0)
		{
		    for(var i=0;i<rowCount;i++)
		    {
		       var containsAccountCode=table.rows[i].cells[0].innerHTML;
		       var addedIHCode=$(containsAccountCode).val();
		       if(addedIHCode==incomeHeadCode)
		       {
		    	   flag=true;
		    	   break;
		       }
		    }
		}
		return flag;
	}
	
	//calculate totals
	function funCalculateTotals()
	{
		var totalAmt=0.00;
		var table=document.getElementById("tblIncomeHeadDtl");
		var rowCount=table.rows.length;
		if(rowCount>0)
		{
		    for(var i=0;i<rowCount;i++)
		    {
		    	var containsAccountCode=table.rows[i].cells[2].innerHTML;
		       	totalAmt=totalAmt+parseFloat($(containsAccountCode).val());
		    }
		   	totalAmt=parseFloat(totalAmt).toFixed(maxAmountDecimalPlaceLimit);
		}
		//$("#dblTotalAmt").text(totalAmt);
		
		//For tarrif
		
		if(document.getElementById("tblRommRate").rows.length>0)
		{
			for(var i=0;i<document.getElementById("tblRommRate").rows.length;i++)
		    {
				var objName =document.getElementById("dblRoomRate."+i);
		        totalTarriff=totalTarriff+parseFloat(objName.value);
		    }
			totalTarriff=parseFloat(totalTarriff).toFixed(maxAmountDecimalPlaceLimit);
		}
		
		
		var table=document.getElementById("tblTotalPackageDtl");
		var rowCount=table.rows.length;
		while(rowCount>0)
		{table.deleteRow(0);
		   rowCount--;
		}
		var row=table.insertRow();
		row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 56%;width:100%; font-size:13px; font-weight: bold;text-align:right; \"  id=\"strPackageDesc."+(rowCount)+"\" value='Total' >";
 	    row.insertCell(1).innerHTML= "<input type=\"text\" class=\"Box \"  style=\"text-align:right; font-size:13px; font-weight: bold;width:95%;\"  id=\"dblIncomeRate."+(rowCount)+"\" value='"+totalAmt+"' >";
 	    
 	    row=table.insertRow();
		row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 67%;width:100%; font-size:13px; font-weight: bold;text-align:right; \"  id=\"strPackageDesc."+(rowCount)+"\" value='Room Tarrif' >";
	    row.insertCell(1).innerHTML= "<input type=\"readonly\"   class=\"Box \" style=\"text-align:right; font-size:13px; font-weight: bold;width:95%;\"  id=\"dblIncomeRate."+(rowCount)+"\" value='"+totalTarriff+"' >";
	    var totalPkgAmt=0;
	    var rowCount=table.rows.length;
	    if(rowCount>0)
		{
		    for(var i=0;i<rowCount;i++)
		    {
		    	var containsAccountCode=table.rows[i].cells[1].innerHTML;
		    	totalPkgAmt=totalPkgAmt+parseFloat($(containsAccountCode).val());
		    }
		    totalPkgAmt=parseFloat(totalPkgAmt).toFixed(maxAmountDecimalPlaceLimit);
		}
	    row=table.insertRow();
		row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 70%;width:100%; font-size:13px; font-weight: bold;text-align:right; \"  id=\"strPackageDesc."+(rowCount)+"\" value='Total Package' >";
	    row.insertCell(1).innerHTML= "<input type=\"text\" class=\"Box \"  style=\"text-align:right; font-size:13px; font-weight: bold;width:95%;\"  id=\"dblIncomeRate."+(rowCount)+"\" value='"+totalPkgAmt+"' >";
	    $("#txtTotalPackageAmt").val(totalPkgAmt);
	}
	
	function funFillRoomRate(roomNo,roomDesc)
	{
		
		 var arrivalDate= $("#txtArrivalDate").val();
		 var departureDate=$("#txtDepartureDate").val();
		 var roomDescList = new Array();
		 var table = document.getElementById("tblResDetails");
		 var rowCount = table.rows.length;
		 for (i = 0; i < rowCount; i++){
			 
			 var oCells = table.rows.item(i).cells;
			 
			 if(roomDescList!='')
			 {
					 roomDescList = roomDescList + ","+table.rows[i].cells[12].children[0].value;
				
			 }
			
			 else
			 {
				 roomDescList = table.rows[i].cells[12].children[0].value;	 
			 }	 

		}
		 $.ajax({  
				type : "GET",
				url : getContextPath()+ "/loadRoomRate.html?arrivalDate="+arrivalDate+"&departureDate="+departureDate+"&roomDescList="+roomDescList+"&noOfNights="+$("#txtNoOfNights").val(),
				dataType : "json",
				success : function(response){ 
				funAddRommRateDtl(response,roomNo,roomDesc);
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

	

	function funAddRommRateDtl(dataList,roomNo,roomDesc)
	{
		$('#tblRommRate tbody').empty()
		 var table=document.getElementById("tblRommRate");
		 
		 for(var i=0;i<dataList.length;i++ )
	     {
			 var rowCount=table.rows.length;
			 var row=table.insertRow();
		 var list=dataList[i];
		 var date=list[0];
		 var dateSplit= date.split("-");
		 var month=dateSplit[1];
		 var day = dateSplit[2];
// 		 if(day<10) 
// 		 {
// 			 day='0'+day;
// 		 } 

// 		 if(month<10) 	
// 		 {
// 			 month='0'+month;
// 		 } 
		 date=day+"-"+month+"-"+dateSplit[0];
		 row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-center: 5px;width:50%;\" name=\"listReservationRoomRateDtl["+(rowCount)+"].dtDate\"  id=\"dtDate."+(rowCount)+"\" value='"+date+"' >";
 	     row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\" size=\"10%\" id=\"strTypeRoomDesc."+(rowCount)+"\" value='"+list[2]+"' />";
 	     row.insertCell(2).innerHTML= "<input type=\"text\"    style=\"text-align:right;\"  name=\"listReservationRoomRateDtl["+(rowCount)+"].dblRoomRate\" id=\"dblRoomRate."+(rowCount)+"\" onchange =\"Javacsript:funCalculateTotals()\" value='"+list[1]+"' >";
 	     row.insertCell(3).innerHTML= "<input type=\"hidden\" class=\"Box \"  name=\"listReservationRoomRateDtl["+(rowCount)+"].strRoomType\" id=\"strRoomType."+(rowCount)+"\" value='"+list[3]+"' >";
 	   
 	     totalTerrAmt =totalTerrAmt + list[1];
 	    $("#txtTotalAmt").val(totalTerrAmt);
		}
		 
	}
	
	function CalculateDateDiff() 
	{

		var fromDate=$("#txtArrivalDate").val();
		var toDate=$("#txtDepartureDate").val()
		var frmDate= fromDate.split('-');
	    var fDate = new Date(frmDate[2],frmDate[1],frmDate[0]);
	    
	    var tDate= toDate.split('-');
	    var t1Date = new Date(tDate[2],tDate[1],tDate[0]);

    	var dateDiff=t1Date-fDate;
  		 if (dateDiff >= 0 ) 
  		 {
  			var total_days = (dateDiff) / (1000 * 60 * 60 * 24);
  			$("#txtNoOfNights").val(total_days);
         	
         }else{
        	 alert("Please Check Departure Date");
        	 $("#txtDepartureDate").datepicker({ dateFormat: 'dd-mm-yy' });
			 $("#txtDepartureDate").datepicker('setDate', pmsDate);
        	 return false
         }
  		funFillRoomRate('','');
  		
		
	}
	
	function funChangeArrivalDate()
	{
		var arrivalDate=$("#txtArrivalDate").val();
	    var pmsDate='<%=session.getAttribute("PMSDate").toString()%>';

    	if (arrivalDate < pmsDate) 
  		 {
		    	alert("Arrival Date Should not be come before PMS Date");
		    	$("#txtArrivalDate").datepicker({ dateFormat: 'dd-mm-yy' });
				$("#txtArrivalDate").datepicker('setDate', pmsDate);
				return false
         }
    	funFillRoomRate('','');
    	
	}

	
	function validateEmail(emailField){
        var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;

        if (reg.test(emailField.value) == false) 
        {
            alert('Invalid Email Address');
            return false;
        }

        return true;

	}
	
	function funRemoveRow(obj)
	{
	    var index = obj.parentNode.parentNode.rowIndex;
	    var table = document.getElementById("tblIncomeHeadDtl");
	    table.deleteRow(index);
	    funCalculateTotals();
	}
	
	function funRemoveProductRowsForIncomeHead()
	{
		var table = document.getElementById("tblIncomeHeadDtl");
		var rowCount = table.rows.length;
		while(rowCount>0)
		{
			table.deleteRow(0);
			rowCount--;
		}
	}
	
	function funGetGuestCode(guestCode) 
	{	
		var returnVal =0;
		var searchurl=getContextPath()+"/getGuestCode.html?guestCode="+guestCode;
		 $.ajax({
		        type: "GET",
		        url: searchurl,
		        dataType: "json",
		        async:false,
		        success: function(response)
		        {
		        	returnVal = response;
				},
			error : function(e)
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
		
		 return returnVal;
	}
	function funCreateNewGuest(){
		
		window.open("frmGuestMaster.html", "myhelp", "scrollbars=1,width=500,height=350");
		var GuestDetails='<%=session.getAttribute("GuestDetails").toString()%>';
// 		var guest=GuestDetails.split("#");
		
	}
	
 --%></script>

</head>
<body>

	<div id="formHeading">
	<label>Banquet Booking</label>
	</div>

	<br/>
	<br/>

	<s:form name="Banquet Booking" method="POST" action="saveBanquetBooking.html">
	
		<div id="tab_container" style="height: 500px">
				<ul class="tabs">
					<li data-state="tab1" style="width: 6%; padding-left: 2%; class="active" >Booking</li>
					<li data-state="tab2" style="width: 8%; padding-left: 1%">Service</li>
			   	    <li data-state="tab3" style="width: 8%; padding-left: 1%">Equipment</li>
			   	    <li data-state="tab4" style="width: 8%; padding-left: 1%">Staff Category</li>
			   	    <li data-state="tab5" style="width: 8%; padding-left: 1%">Menu</li>
					
				
				</ul>
							
<!-- Reservation Tab Start -->

				<div id="tab1" class="tab_content" style="height: 800px">

		<table class="transTable">
		

			<tr>
			<th colspan="6">
			</tr>
		
			<tr>
				<td><label>Booking No</label></td>
				<td>
					<s:input type="text" id="txtBookingNo" path="strBookingNo" cssClass="searchTextBox" ondblclick="funHelp('BookingNo');"/>
				</td>
				
				<td><label>Property</label></td>
				<td><s:select id="txtPropertyCode" path="strPropertyCode" items="${listOfProperty}" required="true" cssClass="BoxW200px"></s:select></td>
				
				<td><label id="lblPropName"></label></td>
			</tr>
			
			<tr>
				<td><label>Area</label></td>
				<td>
					<s:input type="text" id="txtAreaCode" path="strAreaCode" cssClass="searchTextBox" ondblclick="funHelp('PropertyWiseLocation');"/>
					<label id="lblAreaCode"></label>
				</td>
			
				<td ><label>Booking Date</label></td>
				<td>
					<s:input type="text" id="txtBookingDate" path="dteBookingDate" cssClass="calenderTextBox" />
					<!-- onchange="funChangeArrivalDate();" -->
					<label id="lblBookingDate"></label>
				</td>
				
				
			</tr>
	<!-- 		private String tmeFromTime;
	
	private String tmeToTime;

	private String dteToDate;dteFromDate -->
			<tr>
				<td><label>From Date</label></td>
				<td><s:input type="text" id="txtFromDate" path="dteFromDate" cssClass="calenderTextBox" /></td>
				
			<!-- onchange="funChangeArrivalDate();"  -->
				<td><label>To Date</label></td>
				<td><s:input type="text" id="txtToDate" path="dteToDate" cssClass="calenderTextBox"  /></td>
			  <!-- onchange="CalculateDateDiff();" -->
			    <td colspan="2"></td>
			</tr>
			
			<tr>
				<td><label>In Time</label></td>
				<td><s:input type="text" id="txtFromTime" path="tmeFromTime" cssClass="calenderTextBox" /></td>
			
				<td><label>Out Time</label></td>
				<td><s:input type="text" id="txtToTime" path="tmeToTime"  cssClass="calenderTextBox"  /></td>
				<td colspan="2"></td>
			</tr>
			
			<%-- <tr>
				<td><label>Pick Up Time</label></td>
				<td><s:input type="text" id="txtPickUpTime" path="tmePickUpTime" cssClass="calenderTextBox" /></td>
			
				<td><label>Drop Time</label></td>
				<td><s:input type="text" id="txtDropTime" path="tmeDropTime" cssClass="calenderTextBox"  /></td>
				<td colspan="2"></td>
			</tr> --%>
	
						
			<tr>
				<td><label>Min Pax</label></td>
				<td><s:input id="txtMinPaxNo" name="txtMinPaxNo" path="intMinPaxNo" style="width: 20%;text-align: right;" type="number" min="1" step="1"   class="longTextBox" /></td>
				<td><label>Max Pax</label></td>
				<td><s:input id="txtMaxPaxNo" path="intMaxPaxNo" style="text-align: right; width: 20%;" type="number" min="0" step="1" name="txtMaxPaxNo" class="longTextBox" /></td>				
				<td colspan="2"></td>
			</tr>
			
			<%-- <tr>
				<td><label>No Of Nights</label></td>
				<td>
					<s:input type="text" class="numeric" id="txtNoOfNights" path="intNoOfNights" cssClass="longTextBox" style="width: 20%;text-align: right;"/>
				</td>
				
				<td><label>Booking Rooms</label></td>
				<td>
					<s:input type="number" min="1" step="1" class="longTextBox" id="txtNoOfBookingRoom" path="intNoRoomsBooked" cssClass="longTextBox" style="text-align: right;width: 20%;" />
				</td>
				<td colspan="2"></td>
			
			</tr> --%>
			
			<tr>
				<td><label>Email Id</label></td>
				<td><s:input type="text" id="txtEmailId" path="strEmailID" cssClass="longTextBox" /></td>
				<td><label>Billing Instructions</label></td>
				<td><s:input type="text" id="txtBillingInstructionCode" path="strBillingInstructionCode" cssClass="searchTextBox" ondblclick="funHelp('BillingInstCode');"/>
				<label id="lblBillingInstDesc"></label></td>
				<td colspan="2"></td>
			</tr>
			
			<%-- <tr>
				<td><label>Billing Instructions</label></td>
				<td>
					<s:input type="text" id="txtBillingInstructionCode" path="strBillingInstructionCode" cssClass="searchTextBox" ondblclick="funHelp('BillingInstCode');"/>
					<label id="lblBillingInstDesc"></label></td>
			
				<td><label>Booker</label></td>
				<td colspan="3">
					<s:input type="text" id="txtBookerCode" path="strBookerCode" cssClass="searchTextBox" ondblclick="funHelp('BookerCode');"/>
					<label id="lblBookerName"></label>
				</td>
			</tr> --%>
			
			<%-- <tr>
				<td><label>Business Source</label></td>
				<td>
					<s:input type="text" id="txtBusinessSourceCode" path="strBusinessSourceCode" cssClass="searchTextBox" ondblclick="funHelp('business');"/>
					<label id="lblBusinessSourceName"></label></td>
			
				<td><label>Agent</label></td>
				<td colspan="3">
					<s:input type="text" id="txtAgentCode" path="strAgentCode" cssClass="searchTextBox" ondblclick="funHelp('AgentCode');"/>
					<label id="lblAgentName"></label></td>
			</tr> --%>
			
			<%-- <tr>
				<td><label>Remarks</label></td>
				<td><s:input colspan="1" type="text" id="txtRemarks" path="strRemarks" cssClass="longTextBox" /></td>
				<td><label>OTA No</label></td>
				<td><s:input type="text" id="txtOTANo" path="strOTANo" class="longTextBox" style="width: 34%"/></td>
				<td colspan="2"></td>
			</tr>
			 --%>
			<%-- <tr>
				<td><label>Cancel Date</label></td>
				<td><s:input type="text" id="txtCancelDate" path="dteCancelDate" cssClass="calenderTextBox" /></td>
			
				<td><label>Confirm Date</label></td>
				<td><s:input type="text" id="txtConfirmDate" path="dteConfirmDate" cssClass="calenderTextBox" /></td>
				<td colspan="2"></td>
			</tr> --%>
			
			<tr>
				<td><label>Event Co-Ordinator</label></td>
				<td>
					<s:input  type="text" id="txtEventCoordinatorCode" path="strEventCoordinatorCode" cssClass="searchTextBox" ondblclick="funHelp('StaffCode');"/>
						&nbsp;&nbsp;&nbsp;<label id="lblEventCoordinatorCode"></label>
				</td>
			
			<td><label>Booking Status</label></td>
					<td colspan="2">
						<s:select id="cmbBookingStatus" path="strBookingStatus" cssClass="BoxW124px">
				    		<s:option value="Confirmed">Confirmed</s:option>
				    		<s:option value="Waiting">Waiting</s:option>
				    		<s:option value="Provisional">Provisional</s:option>
				    	
				    	</s:select>
			</td>
				
			</tr>
			
		</table>

			<br>
			<br>
		
			<div >
			<table class="transTable">
				
				<tr>
				<td><label>Customer Code</label></td>
					<td><s:input  type="text" id="txtCustomerCode"  path="strCustomerCode" ondblclick="funHelp('custMaster');" class="searchTextBox" /></td>
					
					
					<td><label id="lblGFirstName"> Name</label></td>
					<td><input type="text" id="txtCustomerName" class="longTextBox" /></td>
					
					<td>
					<input type="Button" value="New Costomer" onclick="return funCreateNewGuest()" class="form_button" />
					</td>
					<td colspan="2"></td>
				</tr>
				
				<tr>
					<td><label>Mobile No</label></td>
					<td><input type="text" id="txtMobileNo" class="longTextBox" /></td>
			
					<td colspan="1"></td>
				</tr>
				<!-- <tr>
				<td><label id="lblRoomType">Room Type</label></td>
				<td><input type="text" id="txtRoomTypeCode" name="txtRoomTypeCode" Class="searchTextBox" ondblclick="funHelp('roomType')" /></td>
				
			    <td><label id="lblRoomNo">Room</label></td>
			    <td><input type="text" id="txtRoomNo" name="txtRoomNo" path="strRoomNo" ondblclick="funHelp1('roomByRoomTypeForReservation')" Class="searchTextBox"/></td> 
				 
				<td><label id="lblExtraBed">Extra Bed</label></td>
				<td><input type="text" id="txtExtraBed" name="txtExtraBed" Class="searchTextBox" ondblclick="funHelp('extraBed')" /></td>
				<td colspan="1" ></td>
				
				</tr> -->
				
			<!-- 	<tr>
					<td><label>Address</label></td>
					<td><input type="text" id="txtAddress" Class="longTextBox" /></td>
					<td><label>Remarks</label></td>
					<td><input type="text" id="txtRemark" path="strRemark" Class="longTextBox"  /></td>
					<td colspan="3"></td>
     			</tr> -->
				
				<!-- <tr>
					<td></td>
					<td>
						<input type="Button" value="Add" onclick="return funGetDetailsRow()" class="smallButton" />
					</td>
					<td colspan="5"></td>
				</tr> -->
			
			</table>
		</div>

	
		<%-- <div class="dynamicTableContainer" style="height: 300px;">
			<table style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
				<tr bgcolor="#72BEFC">				
					<td style="width:22%;">Name</td>
					<td style="width:10%;">Mobile No</td>
<!-- 					<td style="width:7%;">Room No</td> -->
<!-- 					<td style="width:9%;">Extra Bed</td> -->
					<td style="width:6%;">Room Type</td>
					<td style="width:04%;">Remarks</td>
					<td style="width:5%;">Payee</td>
					<td style="width:25%;">Delete</td>
				</tr>
			</table>
		
			<div style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblResDetails"
					style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
					class="transTablex col8-center">
					<tbody>
						<col style="width: 37%;">
						<col style="width: 16% ;">
						<col style="width: 80px;">
						<col style="width: 100px;">
						<col style="width: 10%;">
						<col style="width: 7%;">
						<col style="width: 8%;">
						<col style="width: 38%;">
						<col style="width: 0px;">
						<col style="width: 0px;">
						<col style="width: 0px;">
						<col style="width: 0px;">
						<col style="width: 0px;">
						<col style="width: 0px;">
						<col style="width: 0px;">
							
					</tbody>
				</table>
			</div>
		</div> --%>
</div>

<!-- End of Reservation Tab -->

<!-- Start of Tarif Tab -->

	<div id="tab3" class="tab_content" style="height: 400px">
	<!-- Generate Dynamic Table   -->		
	<br/><br/>
	<table class="transTable">
	 	   <tr>
	 	   <td width="100px"><label>Equipment Code</label></td>
			   <!--  <td><label>Function Code</label></td> -->
			    <td><s:input id="txtEquipmentCode" path=""  readonly="true"  ondblclick="funHelp('equipmentCode')" cssClass="searchTextBox"/>
			     <label id="lblEquipmentCode"></label>
			    </td>
			      
		</tr>
			  
		</table>
		<br/>
		<div class="dynamicTableContainer" style="height: 400px; width: 80%">
			<table style="height: 28px; border: #0F0; width: 100%;font-size:11px; font-weight: bold;">
				<tr bgcolor="#72BEFC" style="height: 24px;">
					<!-- col1   -->
					<td align="center" style="width: 15%">Equipment Code</td>
					<!-- col1   -->
					
					<!-- col2   -->
					<td align="center" style="width: 40%">Equipment Name</td>
					<!-- col2   -->
					
					<!-- col3   -->
					<td align="center" style="width: 10%">Qty</td>
					<!-- col3   -->
					
					<!-- col4   -->
					<td align="center" style="width: 20%">Rate</td>
					<!-- col4   -->
					
					<!-- col5   -->
					<td align="center"">Delete</td>
					<!-- col5   -->
					
													
				</tr>
			</table>
			<div style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 400px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblEquipDtl" style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll" class="transTablex col3-center">
					<tbody>
						<!-- col1   -->
						<col width="16%">
						<!-- col1   -->
						
						<!-- col2   -->
						<col width="42.5%" >
						<!-- col2   -->
						
						<!-- col3   -->
						<col width="10.5%" >
						<!-- col3   -->
						
						<!-- col4   -->
						<col width="21.5%" >
						<!-- col4   -->
						
						<!-- col5   -->
						<col width="14%" >
						<!-- col5   -->
						
					</tbody>
				</table>
			</div>			
	
	
	</div>
	<%-- <div style="margin:auto;width: 25%; float:right; margin-right:100px; ">
	<label>Total</label>
	<td><s:input id="txtTotalAmt" path=""  readonly="true" cssClass="shortTextBox"/></td>
	</div> --%>
	</div>
	
<!-- End of Tarif Tab -->

<!-- Start of Package Tab -->

	 <div id="tab2" class="tab_content" style="height: 400px">
	 <br><br>
	 
	 	<table class="transTable">
	 	   <tr>
	 	   <td width="100px"><label>Function Code</label></td>
			   <!--  <td><label>Function Code</label></td> -->
			    <td><s:input id="txtFunctionCode" path="strFunctionCode"  readonly="true"  ondblclick="funHelp('functionMaster')" cssClass="searchTextBox"/>
			     <label id="lblFunctionName"></label>
			    </td>
			    <td><label>Service Code</label></td>
				<td><s:input type="text" id="txtSeriveCode" path="" cssClass="searchTextBox" ondblclick="funHelp('FunctionService');"/>
				<label id="lblSeriveName"></label></td>
				<td colspan="2"></td>		    
		</tr>
			  <%--   <td><label>Package Name</label></td>
			    <td><s:input id="txtPackageName" path="strPackageName" class="longTextBox" /></td> --%>
		
		 <%-- 	<tr>
			    <td><label>Income Head</label></td>
			    <td><s:input id="txtIncomeHead" path=""  readonly="true"  ondblclick="funHelp('incomeHead')" cssClass="searchTextBox"/></td>
			    <td><label>Income Head Name</label></td>
				<td><input type="text" id="txtIncomeHeadName" path="" Class="longTextBox"  /></td> 
			</tr>
			<tr>
			    <td><label>Amount</label></td>
			    <td><input type="text" id="txtIncomeHeadAmt" path="" Class="BoxW124px"  /></td>
			    <td><input type="button" value="Add"  class="smallButton" onclick='return funAddRow()'/></td>
			    <td></td>
	
			</tr> --%>
		</table>
		
		<br/>
		<!-- Generate Dynamic Table   -->	
		<div class="dynamicTableContainer" style="height: 400px; width: 80%">
			<table style="height: 28px; border: #0F0; width: 100%;font-size:11px; font-weight: bold;">
				<tr bgcolor="#72BEFC" style="height: 24px;">
					<!-- col1   -->
					<td align="center" style="width: 20%">Service Code</td>
					<!-- col1   -->
					
					<!-- col2   -->
					<td align="center" style="width: 50%">Service Name</td>
					<!-- col2   -->
					
						<!-- col2   -->
					<td align="center" style="width: 20%">Rate</td>
					<!-- col2   -->
					
					<!-- col3   -->
					<td align="center"">Delete</td>
					<!-- col3   -->
					
													
				</tr>
			</table>
			<div style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 400px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblServiceDtl" style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll" class="transTablex col3-center">
					<tbody>
						<col width="35%">
						<!-- col1   -->
						
						<!-- col2   -->
						<col width="90%">
						<!-- col2   -->
							<!-- col2   -->
						<col width="35%">
						<!-- col2   -->
						
						<!-- col2   -->
						<col width="15%">
						<!-- col2   -->
					</tbody>
				</table>
			</div>			
	
	
	</div>	
	 </div>
	 
	 
	 <div id="tab4" class="tab_content" style="height: 400px">
	<!-- Generate Dynamic Table   -->		
	<br/><br/>
	<table class="transTable">
	 	   <tr>
	 	   <td width="130px"><label>Staff Category Code</label></td>
			   <!--  <td><label>Function Code</label></td> -->
			    <td><s:input id="txtStaffCatCode" path=""  readonly="true"  ondblclick="funHelp('StaffCatCode')" cssClass="searchTextBox"/>
			     <label id="lblStaffCatCode"></label>
			    </td>
			      
		</tr>
			  
		</table>
		<br/>
		<div class="dynamicTableContainer" style="height: 400px; width: 80%">
			<table style="height: 28px; border: #0F0; width: 100%;font-size:11px; font-weight: bold;">
				<tr bgcolor="#72BEFC" style="height: 24px;">
					<!-- col1   -->
					<td align="center" style="width: 20%">Staff Category Code</td>
					<!-- col1   -->
					
					<!-- col2   -->
					<td align="center" style="width: 45%">Staff Category Name</td>
					<!-- col2   -->
					
					<!-- col2   -->
					<td align="center" style="width: 20%">Number</td>
					<!-- col2   -->
					
					<!-- col3   -->
					<td align="center"">Delete</td>
					<!-- col3   -->
					
													
				</tr>
			</table>
			<div style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 400px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblStaffCatDtl" style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll" class="transTablex col3-center">
					<tbody>
						<!-- col1   -->
						<col width="19%">
						<!-- col1   -->
						
						<!-- col2   -->
						<col width="43%" >
						<!-- col2   -->
						
						<!-- col2   -->
						<col width="20%" >
						<!-- col2   -->
						
						<!-- col2   -->
						<col width="13%" >
						<!-- col2   -->
						
					</tbody>
				</table>
			</div>			
	
	
	</div>
	<%-- <div style="margin:auto;width: 25%; float:right; margin-right:100px; ">
	<label>Total</label>
	<td><s:input id="txtTotalAmt" path=""  readonly="true" cssClass="shortTextBox"/></td>
	</div> --%>
	</div>
	
	 <div id="tab5" class="tab_content" style="height: 400px">
	<!-- Generate Dynamic Table   -->		
	<br/><br/>
	<table class="transTable">
	 	   <tr>
	 	   <td width="130px"><label>Item Code</label></td>
			   <!--  <td><label>Function Code</label></td> -->
			    <td><s:input id="txtItemCode" path=""  readonly="true"  ondblclick="funHelp('ItemCode')" cssClass="searchTextBox"/>
			     <label id="lblItemCode"></label>
			    </td>
			      
		</tr>
			  
		</table>
		<br/>
		<div class="dynamicTableContainer" style="height: 400px; width: 80%">
			<table style="height: 28px; border: #0F0; width: 100%;font-size:11px; font-weight: bold;">
				<tr bgcolor="#72BEFC" style="height: 24px;">
					<!-- col1   -->
					<td align="center" style="width: 15%">Menu Code</td>
					<!-- col1   -->
					
					<!-- col1   -->
					<td align="center" style="width: 15%">Item Code</td>
					<!-- col1   -->
					
					
					<!-- col2   -->
					<td align="center" style="width: 30%">Item Name</td>
					<!-- col2   -->
					
					<!-- col2   -->
					<td align="center" style="width: 12%">Qty</td>
					<!-- col2   -->
					
					<!-- col2   -->
					<td align="center" style="width: 15%">Rate</td>
					<!-- col2   -->
					
					<!-- col3   -->
					<td align="center"">Delete</td>
					
					<!-- col3   -->
					
													
				</tr>
			</table>
			<div style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 400px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblMenuDtl" style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll" class="transTablex col3-center">
					<tbody>
						<!-- col1   -->
						<col width="20%">
						<!-- col1   -->
						
						<!-- col2   -->
						<col width="20%" >
						<!-- col2   -->
						
						<!-- col2   -->
						<col width="40%" >
						<!-- col2   -->
						
						<!-- col2   -->
						<col width="10%" >
						<!-- col2   -->
						
						
						<!-- col2   -->
						<col width="20%" >
						<!-- col2   -->
						
						<!-- col2   -->
						<col width="15%" >
						<!-- col2   -->
						
					</tbody>
				</table>
			</div>			
	
	
	</div>
	<%-- <div style="margin:auto;width: 25%; float:right; margin-right:100px; ">
	<label>Total</label>
	<td><s:input id="txtTotalAmt" path=""  readonly="true" cssClass="shortTextBox"/></td>
	</div> --%>
	</div>
		<%-- <div class="dynamicTableContainer" style="height: 320px; width: 80%">
			<table style="height: 28px; border: #0F0; width: 100%;font-size:11px; font-weight: bold;">
				<tr bgcolor="#72BEFC" style="height: 24px;">
					<!-- col1   -->
					<td align="left" style="width: 20%">Service Code</td>
					<!-- col1   -->
					
					<!-- col2   -->
					<td align="left" style="width: 60%">Service Name</td>
					<!-- col2   -->
					
					<!-- col3   -->
<!-- 					<td align="right" style="width: 30.6%">Amount</td>
 -->					<!-- col3   -->
					
					<!-- col4   -->
					<td align="center">Select</td>
					<!-- col4  -->									
				</tr>
			</table>
			<div style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 200px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblIncomeHeadDtl" style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll" class="transTablex col3-center">
					<tbody>
						<!-- col1   -->
						<col width="100%">
						<!-- col1   -->
						
						<!-- col2   -->
						<col width="100%" >
						<!-- col2   -->
						
						<!-- col3   -->
						<col width="100%" >
						<!-- col3   -->
						
						<!-- col4   -->
						<col  width="10%">
						<!-- col4   -->
					</tbody>
				</table>
			</div>	
			
			<div style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 120px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
				<table id="tblTotalPackageDtl" style="width: 100%; border: #0F0; font-size:15px; font-weight: bold; table-layout: fixed; overflow: scroll" class="display dataTable no-footer">
					<tbody>
						<!-- col1   -->
						<col width="100%" >
						<!-- col1   -->
						
						<!-- col2   -->
						<col width="100%" >
						<!-- col2   -->
						
						<!-- col3   -->
						<col  width="10%">
						<!-- col3   -->
					</tbody>
				</table>
			</div>		
		</div>		
	  --%>

	
	 
<!-- End of Package Tab -->	 
				
</div>

		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button" onclick="return funValidateForm();" />
			<input type="reset" value="Reset" class="form_button" onclick="funResetDetailFields()"/>
		</p>
		<%-- <s:input type="hidden" id="hidPayee" path="strPayeeGuestCode"></s:input>
		<s:input type="hidden" id="hidIncomeHead" path="strIncomeHeadCode"></s:input>
		<s:input type="hidden" id="txtTotalPackageAmt" path="strTotalPackageAmt"></s:input>
		 --%>
		
			
		<br />
		<br />

	</s:form>
</body>
</html>
							