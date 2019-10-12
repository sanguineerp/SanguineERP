<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
				}
			}%>

	   });

	$(function() 
	{
	
		
		$('#txtFromTime').timepicker({
	        'timeFormat':'H:i:s'
		});
		$('#txtToTime').timepicker({
	        'timeFormat':'H:i:s'
		});
		
		$('#txtFromTime').timepicker('setTime', new Date());
	    $("#txtBookingDate").datepicker({ dateFormat: 'dd-mm-yy' });
		$("#txtFromDate").datepicker({ dateFormat: 'dd-mm-yy' });
		$("#txtToDate").datepicker({ dateFormat: 'dd-mm-yy' });
		$("#txtFromDate").datepicker('setDate','todate');
		$("#txtToDate").datepicker('setDate', 'todate');
		$("#txtBookingDate").datepicker('setDate', 'todate');
		
		$('a#baseUrl').click(function() 
		{
			if($("#txtBookingNo").val().trim()=="")
			{
				alert("Please Select Reservation No ");
				return false;
			}
			window.open('attachDoc.html?transName=frmReservation.jsp&formName=Reservation &code='+$('#txtReservationNo').val(),"mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
		});	
		
		
		
		
		
		
	});
function funCreateNewCustomer(){
		
		window.open("frmCustomerMaster.html", "myhelp", "scrollbars=1,width=500,height=350");
	}
	
	function funHelp(transactionName)
	{
		fieldName=transactionName;
	
			window.open("searchform.html?formname="+transactionName+"&searchText=","mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
			
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
		   
		    case 'BookingNo' : 
				funSetBookingData(code);
				 break;
				 
		    case 'banquetCode' : 
				funSetBanquetName(code);
				break;	 
		    	
			
		
			
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
						$("#txtEmailId").val(response.strEmail);
						
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
				    	$("#lblEventCoordinatorCode").text(response.strStaffName);
				    
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
				    	$("#lblEventCoordinatorCode").text(response.strStaffName);
				    	
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
	        		
	        		funSetServiceData(response.strFunctionCode)
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
	
    function funSetServiceData(Code)
	{
    	funRemAllRows('tblServiceDtl');
		var funcode=$("#txtFunctionCode").val();
		var searchurl=getContextPath()+ "/loadBookingServiceData.html?functionCode=" + funcode ;
		$.ajax({
			type : "GET",
			url : searchurl,
			dataType : "json",
			success : function(response)
			{ 
				$.each(response, function(i,item)
	            {
				       funfillServiceRow(response[i][0],response[i][1],response[i][2],'N');
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
	function funSetBookingFunServiceData()
	{
		funRemAllRows('tblServiceDtl');
		var funcode=$("#txtFunctionCode").val();
		var bookCode=$("#txtBookingNo").val();
		var searchurl=getContextPath()+ "/loadBookingFunServiceData.html?functionCode=" + funcode+"&bookingCode="+ bookCode;
		$.ajax({
			type : "GET",
			url : searchurl,
			dataType : "json",
			success : function(response)
			{ 
				$.each(response, function(i,item)
	            {
				       funfillServiceRow(response[i][0],response[i][1],response[i][2],response[i][3]);
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
	
	function funSetEquipmentName(code){
	
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadEquipmentName.html?docCode=" + code,
			dataType : "json",
			success : function(response){ 
				
				$("#txtEquipmentCode").val(response.strEquipmentCode);
				$("#lblEquipmentCode").text(response.strEquipmentName);
				funfillEquipmentDtlRow(response.strEquipmentCode,response.strEquipmentName,response.dblEquipmentRate,'1');
	        		
	        	
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
				    	$("#txtStaffCatCode").val(response.strStaffCategeoryCode);
						$("#lblStaffCatCode").text(response.strStaffCategeoryName);
				    	funfillStaffCatRow(response.strStaffCategeoryCode,response.strStaffCategeoryName,'0');
				    	
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
				$("#txtItemCode").val(response.strItemCode);
				$("#lblItemCode").text(response.strItemName);
				funfillMenuItemDtlRow(response.strItemCode,response.strItemName,response.dblAmount,'1');
	        		
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
	function funSetBanquetName(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadBanquetName.html?docCode=" + code,
			dataType : "json",
			success : function(response){ 

				if(response.strEquipmentCode=='Invalid Code')
	        	{
	        		alert("Invalid Equipment No");
	        		$("#txtBanquetCode").val('');
	        	}
	        	else
	        	{
	        		
	        		$("#txtBanquetCode").val(response.strBanquetCode);
	        		$("#lblBanquetName").text(response.strBanquetName);
	        		
	        	}
			},
			error : function(e){

			}
		});
	}
    var ServiceTotal=0;
	function funfillServiceRow(ServiceCode,ServiceName,Rate,Applicable)
	{
		
		var table = document.getElementById("tblServiceDtl");
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);
	    row.insertCell(0).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"15%\" name=\"listSeriveDtl["+(rowCount)+"].strDocNo\"  id=\"txtServiceCode."+(rowCount)+"\" value='"+ServiceCode+"' />";
	    row.insertCell(1).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"40%\" name=\"listSeriveDtl["+(rowCount)+"].strDocName\"  id=\"txtServiceName."+(rowCount)+"\" value='"+ServiceName+"'/>";
	    row.insertCell(2).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"25%\" style=\"padding-right: 5px;text-align: right;\"  name=\"listSeriveDtl["+(rowCount)+"].dblDocRate\"  id=\"txtServiceRate."+(rowCount)+"\" value='"+Rate+"'/>";
	    if(Applicable=='Y')
	    {
		    row.insertCell(3).innerHTML= "<input id=\"chkServiceSel."+(rowCount)+"\" type=\"checkbox\"  checked=\"checked\" class=\"GCheckBoxClass\" name=\"listSeriveDtl["+(rowCount)+"].strType\" size=\"3%\" value=\"Y\"   />";
		    ServiceTotal=ServiceTotal + Rate;
	  	    $("#txtTotalServiceAmt").val(ServiceTotal);
	    }
	    else
	    {
		    row.insertCell(3).innerHTML= "<input id=\"chkServiceSel."+(rowCount)+"\" type=\"checkbox\"  class=\"GCheckBoxClass\" name=\"listSeriveDtl["+(rowCount)+"].strType\" size=\"3%\" value=\"Y\"   />";

	    }	
	    
	}

	var EuipTotal=0;
	function funfillEquipmentDtlRow(EquipCode,EquipName,EquipRate,EquipQty)
	{
		if(funDuplicateEuipForUpdate(EquipCode))
	    {
			var table = document.getElementById("tblEquipDtl");
		    var rowCount = table.rows.length;
		    rowCount=listEquipRow;
		    var row = table.insertRow(rowCount);
		    row.insertCell(0).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"8%\" name=\"listEquipDtl["+(rowCount)+"].strDocNo\"  id=\"txtEquipCode."+(rowCount)+"\" value='"+EquipCode+"' />";
		    row.insertCell(1).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"27%\" name=\"listEquipDtl["+(rowCount)+"].strDocName\"  id=\"txtEquipName."+(rowCount)+"\" value='"+EquipName+"'/>"; 
		    row.insertCell(2).innerHTML= "<input   class=\" decimal-places-amt\" size=\"5%\" name=\"listEquipDtl["+(rowCount)+"].dblDocQty\"  id=\"txtEquipQty."+(rowCount)+"\" value='"+EquipQty+"' onblur=\"funUpdateEuipPrice(this);\"/>";    
		    row.insertCell(3).innerHTML= "<input  readonly=\"readonly\" class=\"Box \" style=\"padding-right: 5px;text-align: right;\" size=\"25%\" name=\"listEquipDtl["+(rowCount)+"].dblDocRate\"  id=\"txtEquipRate."+(rowCount)+"\" value='"+EquipRate+"'/>";
		    row.insertCell(4).innerHTML= '<input type="button" class="deletebutton" value = "Delete" onClick="Javacsript:funDeleteRowEquip(this)">';		    
		    listEquipRow++;
		    funCalculateEuipTotal();
			
	    }		
		
	     
	    
	}
	
	function funfillStaffCatRow(StaffCatCode,StaffCatName,StaffQty)
	{
		
		var table = document.getElementById("tblStaffCatDtl");
	    var rowCount = table.rows.length;
	    rowCount=listStaffRow;
	    var row = table.insertRow(rowCount);
	    row.insertCell(0).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"8%\" name=\"listStaffCatDtl["+(rowCount)+"].strDocNo\"  id=\"txtStaffCatCode."+(rowCount)+"\" value='"+StaffCatCode+"' />";
	    row.insertCell(1).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"27%\" name=\"listStaffCatDtl["+(rowCount)+"].strDocName\"  id=\"txtStaffCatName."+(rowCount)+"\" value='"+StaffCatName+"'/>";
	    row.insertCell(2).innerHTML= "<input   class=\" decimal-places-amt\" size=\"5%\" name=\"listStaffCatDtl["+(rowCount)+"].dblDocQty\"  id=\"txtStaffCatNumber."+(rowCount)+"\" value='"+StaffQty+"'/>";
	    row.insertCell(3).innerHTML= '<input type="button" class="deletebutton" value = "Delete" onClick="Javacsript:funDeleteRowStaff(this)">';		    
	    listStaffRow++;
	  
	     
	    
	}
	var MenuTotal=0;
	function funfillMenuItemDtlRow(ItemCode,ItemName,ItemRate,ItemQty)
	{
		if(funDuplicateItemForUpdate(ItemCode))
		{
			var table = document.getElementById("tblMenuDtl");
		    var rowCount = table.rows.length;
		    rowCount=listItemRow;
		    var row = table.insertRow(rowCount);
		    row.insertCell(0).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"8%\" name=\"listMenuItemDtl["+(rowCount)+"].strDocNo\"  id=\"txtItemCode."+(rowCount)+"\" value='"+ItemCode+"'/>";
		    row.insertCell(1).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"27%\" name=\"listMenuItemDtl["+(rowCount)+"].strDocName\"  id=\"txtItemName."+(rowCount)+"\" value='"+ItemName+"'/>";
		    row.insertCell(2).innerHTML= "<input  class=\"decimal-places-amt\" size=\"5%\" name=\"listMenuItemDtl["+(rowCount)+"].dblDocQty\"  id=\"txtItemQty."+(rowCount)+"\" value='"+ItemQty+"' onblur=\"funUpdateItemPrice(this);\"/>";
		    row.insertCell(3).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"15%\" style=\"padding-right: 5px;text-align: right;\"  name=\"listMenuItemDtl["+(rowCount)+"].dblDocRate\"  id=\"txtItemRate."+(rowCount)+"\" value='"+ItemRate+"'/>";
		    row.insertCell(4).innerHTML= '<input type="button" class="deletebutton" value = "Delete" onClick="Javacsript:funDeleteRowMenu(this)">';	
		    
		    listItemRow++;
		    funCalculateItemTotal();
		
		}
		
	     
	    
	}
	function funDeleteRowStaff(obj)
	{
		 var index = obj.parentNode.parentNode.rowIndex;
		 var table = document.getElementById("tblStaffCatDtl");
		 table.deleteRow(index);
	}
	function funDeleteRowEquip(obj)
	{
		 var index = obj.parentNode.parentNode.rowIndex;
		 var table = document.getElementById("tblEquipDtl");
		 table.deleteRow(index);
		 funCalculateEuipTotal();
	}
	function funDeleteRowMenu(obj)
	{
		 var index = obj.parentNode.parentNode.rowIndex;
		 var table = document.getElementById("tblMenuDtl");
		 table.deleteRow(index);
		 funCalculateItemTotal();
	}
	
	function funRemAllRows(tableName)
	{
		var table = document.getElementById(tableName);
		var rowCount = table.rows.length;
		while(rowCount>0)
		{
			table.deleteRow(0);
			rowCount--;
		}
	}
	
	function funSetBookingData(Code) {
		
		var searchurl=getContextPath()+ "/loadBanquetBookingHd.html?bookingCode=" + Code;
		$.ajax({
			type : "GET",
			url : searchurl,
			dataType : "json",
			success : function(response)
			{ 
				if(response.strBookingNo=='Invalid Code')
	        	{
	        		alert("Invalid Booking Code");
	        		$("#txtBookingNo").val('');
	        	
	        	}
				else
				{
					$("#txtBookingNo").val(response.strBookingNo);
	        		$("#txtBookingDate").val(funGetDate(response.dteBookingDate));
	        		$("#txtFromDate").val(funGetDate(response.dteFromDate));
	        		$("#txtToDate").val(funGetDate(response.dteToDate));
	        		$("#txtMaxPaxNo").val(response.intMaxPaxNo);
	        		$("#txtMinPaxNo").val(response.intMinPaxNo);
	        		$("#txtAreaCode").val(response.strAreaCode);
	        		$("#txtBillingInstructionCode").val(response.strBillingInstructionCode);
	        		$("#cmbBookingStatus").val(response.strBookingStatus);
	        		$("#txtEmailId").val(response.strEmailID);
	        		$("#txtEventCoordinatorCode").val(response.strEventCoordinatorCode);
	        		$("#txtFunctionCode").val(response.strFunctionCode);
	        		funSetPropertyCode(response.strPropertyCode);
	        		
	        		$("#txtFromTime").val(response.tmeFromTime);
	        		$("#txtToTime").val(response.tmeToTime);
	        		
	        		$("#txtTotalBookingAmt").val(response.dblSubTotal);
	        		        
	        		
	        		funSetCustomer(response.strCustomerCode);
	        		funSetBookingFunServiceData();
	        		funUpdateBookingDtl(response.listBanquetBookingDtlModels);
	        		
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
	 function funUpdateBookingDtl(listBookingDtl){
		 funRemAllRows('tblEquipDtl');	
		 funRemAllRows('tblStaffCatDtl');
		 funRemAllRows('tblMenuDtl');
   		$.each(listBookingDtl, function(i,item)
           {
   			funfillBookingDtl(listBookingDtl[i].strType,listBookingDtl[i].strDocNo,listBookingDtl[i].strDocName,listBookingDtl[i].dblDocRate,listBookingDtl[i].dblDocQty);
			});
   	
		}
	
	function funfillBookingDtl(strType,strDocNo,strDocName,dblDocRate,dblDocQty) {
		if(strType =='Equipment')
		{
			funfillEquipmentDtlRow(strDocNo,strDocName,dblDocRate,dblDocQty);	
		}
		else if(strType =='Staff')
		{
			funfillStaffCatRow(strDocNo,strDocName,dblDocQty);	
		}
		else if(strType =='Menu')
		{
			funfillMenuItemDtlRow(strDocNo,strDocName,dblDocRate,dblDocQty);	
		}
		
	}
	 function  funGetDate(date) {
		 var dateSplit = date.split(" ")[0].split("-");    
		 date=dateSplit[2]+"-"+dateSplit[1]+"-"+dateSplit[0];
		 return date;
		
	}
	 function funSetPropertyCode(code){

			$.ajax({
				type : "GET",
				url : getContextPath()+ "/loadPropertyCode.html?docCode=" + code,
				dataType : "json",
				success : function(response){ 
					$("#txtPropertyCode").text(response.strPropertyName);
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
	 
	   		   $(document).on('change', '[type=checkbox]', function() {
			     var checkbox = $(this).is(':checked');
			     var index = this.parentNode.parentNode.rowIndex;
			         if(checkbox)
			    	 {
			        	 var  Rate1=document.getElementById("txtServiceRate."+index).value;
					     var ServiceRate1 =$("#txtTotalServiceAmt").val();
					     var Total1=0;
					     if(ServiceRate1=='')
				    	 {
					    	 Total1=  parseFloat(Rate1);
					    	
				    	 }
					     else
					     {
					    	 Total1= parseFloat(ServiceRate1) + parseFloat(Rate1);
					     } 	 
					     $("#txtTotalServiceAmt").val(Total1);
			    	 
			    	 }else{
			    		 var  Rate2=document.getElementById("txtServiceRate."+index).value;
					     var ServiceRate2 =$("#txtTotalServiceAmt").val();
					     var Total2=0;
					     if(ServiceRate2=='')
					     {
					    	 Total2= parseFloat(Rate2);
					    	 
					     }
					     else
					     {
					    	 Total2= parseFloat(ServiceRate2) - parseFloat(Rate2);
					     } 	 
					   
					     $("#txtTotalServiceAmt").val(Total2);
			    		 
			    	 }
			    
			});
	   		   
	   	function funOnClickBookingTotal()
	   	{
	   	    var rateService =0;
	   	    if($("#txtTotalServiceAmt").val()!='')
		   	{
	   	    	
	   	    	rateService= $("#txtTotalServiceAmt").val(); 
		   	}
	   	    var rateItem =0;
	   	    if($("#txtTotalItemAmt").val()!='')
		   	{
	   	    	rateItem  =$("#txtTotalItemAmt").val();
		   	}
	   	    var rateEquip =0;
	   	    if($("#txtTotalEquipAmt").val()!='')
		   	{
	   	    	rateEquip =$("#txtTotalEquipAmt").val(); 
		   	}
	   	   var bookingTotal = parseFloat(rateEquip) + parseFloat(rateItem) + parseFloat(rateService);
	       $("#txtTotalBookingAmt").val(bookingTotal);
	   		
	   	}
	   	function funUpdateEuipPrice(object)
		{
	   		 
	   		funCalculateEuipTotal();
			/* var index=object.parentNode.parentNode.rowIndex;
			if(document.getElementById("txtEquipQty."+index).value !='')
			{
				var Qty=document.getElementById("txtEquipQty."+index).value;
				var AmtEquip=document.getElementById("txtEquipRate."+index).value;
				var TotalEquip=parseFloat($("#txtTotalEquipAmt").val()) +(parseFloat(Qty)*parseFloat(AmtEquip) );
				$("#txtTotalEquipAmt").val(TotalEquip);
				
			}
			 */
		
		}
		function funUpdateItemPrice(object)
		{
			funCalculateItemTotal();
			
			/* var index=object.parentNode.parentNode.rowIndex;
			if(document.getElementById("txtItemQty."+index).value !='')
			{
				var Qty=document.getElementById("txtItemQty."+index).value;
				var AmtItem=document.getElementById("txtItemRate."+index).value;
				var TotalItem=parseFloat($("#txtTotalEquipAmt").val()) + (parseFloat(Qty) * parseFloat(AmtItem));
				$("#txtTotalEquipAmt").val(TotalItem);
			
			} */
			
		
		}
		function funCalculateEuipTotal()
		{
			 
		    var table = document.getElementById("tblEquipDtl");
		    var rowCount = table.rows.length;
		    var TotalEquip=0;
		    if(rowCount > 0)
		    {
			    $('#tblEquipDtl tr').each(function()
			    {
			    	var qty=$(this).find('input')[2].value;
			    	var amt=$(this).find('input')[3].value;
			    	
			    	TotalEquip=TotalEquip + (parseFloat(qty) * parseFloat(amt));
			    	
				});
			    $("#txtTotalEquipAmt").val(TotalEquip);
			    
				    
		   }
		  
			
		}
			
	
		function funCalculateItemTotal()
		{
			 var table = document.getElementById("tblMenuDtl");
			 var rowCount = table.rows.length;
			 var TotalItem=0;
		    if(rowCount > 0)
		    {
			    $('#tblMenuDtl tr').each(function()
			    {
			    	var qty=$(this).find('input')[2].value;
			    	var amt=$(this).find('input')[3].value;
			    	
			    	TotalItem=TotalItem + (parseFloat(qty) * parseFloat(amt));
			
				});
			    $("#txtTotalItemAmt").val(TotalItem);
				    
		   }
			
		}
		function funDuplicateEuipForUpdate(strEuipCode)
		{
		 var table = document.getElementById("tblEquipDtl");
		    var rowCount = table.rows.length;		   
		    var flag=true;
		    if(rowCount > 0)
		    {
				    $('#tblEquipDtl tr').each(function()
				    {
					    if(strEuipCode==$(this).find('input').val())// `this` is TR DOM element
	    				{
					    	alert("Already added Equipment "+ strEuipCode );
					    	flag=false; 
					    	
		    			
	    				}
					});
				    
		   }
		    return flag;
		} 
		function funDuplicateItemForUpdate(strItemCode)
		{
		 var table = document.getElementById("tblMenuDtl");
		    var rowCount = table.rows.length;		   
		    var flag=true;
		    if(rowCount > 0)
		    {
				    $('#tblMenuDtl tr').each(function()
				    {
					    if(strItemCode==$(this).find('input').val())// `this` is TR DOM element
	    				{
					    	alert("Already added Item "+ strItemCode );
					    	flag=false; 
					    	
		    			
	    				}
					});
				    
		   }
		    return flag;
		} 

	 
</script>

</head>
<body>

	<div id="formHeading">
		<label>Banquet Booking</label>
	</div>

	<br />
	<br />

	<s:form name="Banquet Booking" method="POST"
		action="saveBanquetBooking.html">

		<div id="tab_container" style="height: 550px">
			<ul class="tabs">
				<li data-state="tab1" style="width: 6%; padding-left: 2%;"active" onclick="funOnClickBookingTotal()" >Booking</li>
				<li data-state="tab2" style="width: 8%; padding-left: 1%">Service</li>
				<li data-state="tab3" style="width: 8%; padding-left: 1%">Equipment</li>
				<li data-state="tab4" style="width: 8%; padding-left: 1%">Staff
					Category</li>
				<li data-state="tab5" style="width: 8%; padding-left: 1%">Menu</li>


			</ul>

		

		<div id="tab1" class="tab_content" style="height: 800px">
            <table class="transTable">
				<tr>
					<th colspan="6">
				</tr>

				<tr>
					<td><label>Booking No</label></td>
					<td><s:input type="text" id="txtBookingNo"
							path="strBookingNo" cssClass="searchTextBox"
							ondblclick="funHelp('BookingNo');" /></td>

					<td><label>Property</label></td>
					<td><s:select id="txtPropertyCode" path="strPropertyCode"
							items="${listOfProperty}" required="true" cssClass="BoxW200px"></s:select></td>

					<td><label id="lblPropName"></label></td>
				</tr>

				<tr>
					<td><label>Area</label></td>
					<td><s:input type="text" id="txtAreaCode" path="strAreaCode"
							cssClass="searchTextBox"
							ondblclick="funHelp('PropertyWiseLocation');" /> <label
						id="lblAreaCode"></label></td>

					<td><label>Booking Date</label></td>
					<td><s:input type="text" id="txtBookingDate"
							path="dteBookingDate" cssClass="calenderTextBox" /> <!-- onchange="funChangeArrivalDate();" -->
						<label id="lblBookingDate"></label></td>


				</tr>
			

				<tr>
					<td><label>From Date</label></td>
					<td><s:input type="text" id="txtFromDate" path="dteFromDate"
							cssClass="calenderTextBox" /></td>

					<!-- onchange="funChangeArrivalDate();"  -->
					<td><label>To Date</label></td>
					<td><s:input type="text" id="txtToDate" path="dteToDate"
							cssClass="calenderTextBox" /></td>
					<!-- onchange="CalculateDateDiff();" -->
					<td colspan="2"></td>
				</tr>

				<tr>
					<td><label>In Time</label></td>
					<td><s:input type="text" id="txtFromTime" path="tmeFromTime"
							cssClass="calenderTextBox" /></td>

					<td><label>Out Time</label></td>
					<td><s:input type="text" id="txtToTime" path="tmeToTime"
							cssClass="calenderTextBox" /></td>
					<td colspan="2"></td>
				</tr>

				<tr>
					<td><label>Min Pax</label></td>
					<td><s:input id="txtMinPaxNo" name="txtMinPaxNo"
							path="intMinPaxNo" style="width: 20%;text-align: right;"
							type="number" min="1" step="1" class="longTextBox" /></td>
					<td><label>Max Pax</label></td>
					<td><s:input id="txtMaxPaxNo" path="intMaxPaxNo"
							style="text-align: right; width: 20%;" type="number" min="0"
							step="1" name="txtMaxPaxNo" class="longTextBox" /></td>
					<td colspan="2"></td>
				</tr>

				<tr>
				<td><label>Billing Instructions</label></td>
					<td><s:input type="text" id="txtBillingInstructionCode"
							path="strBillingInstructionCode" cssClass="searchTextBox"
							ondblclick="funHelp('BillingInstCode');" /> <label
						id="lblBillingInstDesc"></label></td>
				<td><label>Event Co-Ordinator</label></td>
					<td><s:input type="text" id="txtEventCoordinatorCode"
							path="strEventCoordinatorCode" cssClass="searchTextBox"
							ondblclick="funHelp('StaffCode');" /> &nbsp;&nbsp;&nbsp;<label
						id="lblEventCoordinatorCode"></label></td>
					
					<td colspan="2"></td>
				</tr>

				<tr>
					<td width="100px"><label>Function Code</label></td>
					<!--  <td><label>Function Code</label></td> -->
					<td><s:input id="txtFunctionCode" path="strFunctionCode"
							readonly="true" ondblclick="funHelp('functionMaster')"
							cssClass="searchTextBox" /> <label id="lblFunctionName"></label>
					</td>

					<td><label>Booking Status</label></td>
					<td colspan="2"><s:select id="cmbBookingStatus"
							path="strBookingStatus" cssClass="BoxW124px">
							<s:option value="Waiting">Waiting</s:option>
							<s:option value="Provisional">Provisional</s:option>

						</s:select></td>

				</tr>
				<tr>
				<td><label>Banquet</label></td>
					<td><s:input type="text" id="txtBanquetCode" 
							path="strBanquetCode" cssClass="searchTextBox"
							ondblclick="funHelp('banquetCode');" /> <label
						id="lblBanquetName"></label></td>
				
					
				</tr>
				
				

			</table>

			<br> <br>

			<div>
				<table class="transTable">

					<tr>
						<td><label>Customer Code</label></td>
						<td><s:input type="text" id="txtCustomerCode"
								path="strCustomerCode" ondblclick="funHelp('custMaster');"
								class="searchTextBox" /></td>


						<td><label id="lblGFirstName"> Name</label></td>
						<td><input type="text" id="txtCustomerName"
							class="longTextBox" /></td>

						<td><input type="Button" value="New Costomer"
							onclick="return funCreateNewCustomer()" class="form_button" />
						</td>
						<td colspan="2"></td>
					</tr>

					<tr>
						<td><label>Mobile No</label></td>
						<td><input type="text" id="txtMobileNo" class="longTextBox" /></td>
                        	<td><label>Email Id</label></td>
					<td><s:input type="text" id="txtEmailId" path="strEmailID"
							cssClass="longTextBox" /></td>
						<td colspan="1"></td>
					</tr>
				

				</table>
			</div>

</br>
			<div style="margin:auto;width: 50%; float:left; margin-left:25px; font-size: 15px;">
	              <label>Total Booking Amount</label>
	             <td><s:input id="txtTotalBookingAmt" path="dblSubTotal"  readonly="true" cssClass="shortTextBox"  style="text-align: right;"/></td>
	           </div>		
			</div>
			<div id="tab3" class="tab_content" style="height: 400px">
				<br />
				<br />
				<table class="transTable">
					<tr>
						<td width="100px"><label>Equipment Code</label></td>
						<!--  <td><label>Function Code</label></td> -->
						<td><s:input id="txtEquipmentCode" path="" readonly="true"
								ondblclick="funHelp('equipmentCode')" cssClass="searchTextBox" />
							<label id="lblEquipmentCode"></label>
						</td>

					</tr>

				</table>
				<br />
				<div class="dynamicTableContainer" style="height: 400px; width: 80%">
					<table
						style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
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
					<div
						style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 400px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
						<table id="tblEquipDtl"
							style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
							class="transTablex col3-center">
							<tbody>
								<!-- col1   -->
							<col width="16%">
							<!-- col1   -->

							<!-- col2   -->
							<col width="42.5%">
							<!-- col2   -->

							<!-- col3   -->
							<col width="10.5%">
							<!-- col3   -->

							<!-- col4   -->
							<col width="21.5%">
							<!-- col4   -->

							<!-- col5   -->
							<col width="14%">
							<!-- col5   -->

							</tbody>
						</table>
					</div>


				</div>
				<div style="margin:auto;width: 25%; float:right; margin-right:100px; ">
	              <label>Total</label>
	             <td><s:input id="txtTotalEquipAmt" path=""  readonly="true" cssClass="shortTextBox" style="text-align: right;"/></td>
	           </div>
				
			</div>

			

			<div id="tab2" class="tab_content" style="height: 400px">
				<br>
				<br>

				<table class="transTable">

					
				</table>

				<br />
				<!-- Generate Dynamic Table   -->
				<div class="dynamicTableContainer" style="height: 400px; width: 80%">
					<table
						style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
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
							<td align="center"">Select</td>
							<!-- col3   -->


						</tr>
					</table>
					<div
						style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 400px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
						<table id="tblServiceDtl"
							style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
							class="transTablex col3-center">
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
				
				<div style="margin:auto;width: 25%; float:right; margin-right:100px; ">
	              <label>Total</label>
	             <td><s:input id="txtTotalServiceAmt" path=""  readonly="true" cssClass="shortTextBox" style="text-align: right;"/></td>
	           </div>
			</div>


			<div id="tab4" class="tab_content" style="height: 400px">
				<!-- Generate Dynamic Table   -->
				<br />
				<br />
				<table class="transTable">
					<tr>
						<td width="130px"><label>Staff Category Code</label></td> 
						<!--  <td><label>Function Code</label></td> -->
						<td><s:input id="txtStaffCatCode" path="" readonly="true"
								ondblclick="funHelp('StaffCatCode')" cssClass="searchTextBox" />
							<label id="lblStaffCatCode"></label></td>

					</tr>

				</table>
				<br />
				<div class="dynamicTableContainer" style="height: 400px; width: 80%">
					<table
						style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
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
					<div
						style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 400px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
						<table id="tblStaffCatDtl"
							style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
							class="transTablex col3-center">
							<tbody>
								<!-- col1   -->
							<col width="19%">
							<!-- col1   -->

							<!-- col2   -->
							<col width="43%">
							<!-- col2   -->

							<!-- col2   -->
							<col width="20%">
							<!-- col2   -->

							<!-- col2   -->
							<col width="13%">
							<!-- col2   -->

							</tbody>
						</table>
					</div>


				</div>
				
			</div>

			<div id="tab5" class="tab_content" style="height: 400px">
				<!-- Generate Dynamic Table   -->
				<br />
				<br />
				<table class="transTable">
					<tr>
						<td width="130px"><label>Item Code</label></td>
						<!--  <td><label>Function Code</label></td> -->
						<td><s:input id="txtItemCode" path="" readonly="true"
								ondblclick="funHelp('ItemCode')" cssClass="searchTextBox" /> 
								<label id="lblItemCode"></label></td>

					</tr>

				</table>
				<br />
				<div class="dynamicTableContainer" style="height: 400px; width: 80%">
					<table
						style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
						<tr bgcolor="#72BEFC" style="height: 24px;">
							
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
					<div
						style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 400px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
						<table id="tblMenuDtl"
							style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
							class="transTablex col3-center">
							<tbody>
						

							<!-- col2   -->
							<col width="20%">
							<!-- col2   -->

							<!-- col2   -->
							<col width="40%">
							<!-- col2   -->

							<!-- col2   -->
							<col width="10%">
							<!-- col2   -->


							<!-- col2   -->
							<col width="20%">
							<!-- col2   -->

							<!-- col2   -->
							<col width="15%">
							<!-- col2   -->

							</tbody>
						</table>
					</div>


				</div>
				
				<div style="margin:auto;width: 25%; float:right; margin-right:100px; ">
	              <label>Total</label>
	             <td><s:input id="txtTotalItemAmt" path=""  readonly="true" cssClass="shortTextBox" style="text-align: right;"/></td>
	           </div>
				
					</div>
			

		

		</div>

		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button"
				onclick="return funValidateForm();" /> <input type="reset"
				value="Reset" class="form_button" onclick="funResetDetailFields()" />
		</p>
		

		<br />
		<br />

	</s:form>
</body>
</html>
