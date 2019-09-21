<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<script type="text/javascript">
	var fieldName,settlementType="";
	
	$(function() 
	{
		var pmsDate='<%=session.getAttribute("PMSDate").toString()%>';
		
		$("#txtExpiryDate").datepicker({ dateFormat: 'dd-mm-yy' });
		$("#txtExpiryDate").datepicker('setDate', pmsDate);		
		
		 var pmsDate='<%=session.getAttribute("PMSDate").toString()%>';
		  var dte=pmsDate.split("-");
		  $("#txtPMSDate").val(dte[2]+"-"+dte[1]+"-"+dte[0]);
		  
		  var code='${code}';
		  var dblAmt='${dblBalanceAmt}';
		  
		  if(code!=''||code!="")
		  {
		    $("#txtDocCode").val(code);
		    $("#txtSettlementCode").focus();
		    $("#flagForAdvAmt").val("Y");
		    $("#lblBalnceAmount").text(dblAmt);
		   
		    
		  }
		  else{
			  
		  }
	});


	function funSetData(code){

		switch(fieldName){

			case 'RegistrationNo':
				//funSetPaymentData(code);
				break;
				
			case 'ReservationNo':
				funSetPaymentGuestData(code,"Reservation");
				break;
				
			case 'folioPayee':
				funSetPaymentGuestData(code,"Folio-No");
				break;
				
			case 'checkInRooms':
				funSetPaymentGuestData(code,"CheckIn");
				break;
			
			case 'BillForPayment':
				funSetPaymentGuestData(code,"Bill");
				break;
				
			case 'settlementCode':
				funSetSettlementType(code);
				break;
				
			case 'receiptNo':
				funSetReceiptData(code);
				break;
				
			case 'guestCode' : 
				funSetGuestCode(code);
				break;
		}
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
	
	function funSetGuestInfo(obj)
	{
		$("#txtCreditName").val(obj.strGuestCode);
		$("#txtCredit").val(obj.strFirstName);
	
	}
	
	
	function funSetReceiptData(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadPMSReceiptData.html?receiptNo=" + code,
			dataType : "json",
			success : function(response){ 
				funSetReceiptInfo(response);
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
	
	
	function funSetFolioNoData(code){

		$("#txtDocCode").val(code);
	}
	
	

	function funSetRegistrationNo(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadRegistrationNo.html?docCode=" + code,
			dataType : "json",
			success : function(response){ 
								
			},
			error : function(e){

			}
		});
	}

	function funSetReservationNo(code)
	{
		$("#txtDocCode").val(code);
	}
	
	
//set PaymentGuestDetails Data
	function funSetPaymentGuestData(code,type)
	{
		var searchUrl=getContextPath()+"/loadPaymentGuestDetails.html?docCode="+code+ "&docName=" + type;
		$.ajax({
			
			url:searchUrl,
			type :"GET",
			dataType: "json",
	        success: function(response)
	        {
	        	if(response.strRoomCode=='Invalid Code')
	        	{
	        		alert("Invalid Doc Code");
	        		$("#txtDocCode").val('');
	        	}
	        	else
	        	{
	        		$("#txtDocCode").val(code);
	        		$("#lblGuestFullName").text(response[0].strFirstName+" "+response[0].strMiddleName+" "+response[0].strLastName);
	        		$( "#txtReceiptAmt" ).focus();
	        		$( "#lblBalnceAmount").text(response[0].dblBalanceAmount);
	        		 $("#txtReceiptAmt").val(response[0].dblBalanceAmount);
	        	}
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

	
	function funSetBillNo(code)
	{
	  	$("#txtDocCode").val(code);
	}
	
	
	function funSetCheckinNo(code)
	{
		$("#txtDocCode").val(code);
	}
	
	
	function funSetSettlementType(code)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadSettlementCode.html?settlementCode=" + code,
			dataType : "json",
			success : function(response)
			{
				if(response.strService=='Invalid Code')
	        	{
	        		alert("Invalid Service Code");
	        		$("#txtSettlement").val('');
	        	}
	        	else
	        	{
		        	$("#txtSettlementCode").val(response.strSettlementCode);
		        	$("#txtSettlementDesc").val(response.strSettlementDesc);
		        	settlementType=response.strSettlementType;
	        	}

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
	}
	
	
	//Open Against Form
	function funOpenAgainst() {
		if ($("#cmbAgainst").val() == "Reservation")
		{
			funHelp("ReservationNo");
			fieldName = "ReservationNo";
		}

		else if ($("#cmbAgainst").val() == "Folio-No") 
		{
			funHelp("folioPayee");
			fieldName = "folioPayee";
		}
		/* else if ($("#cmbAgainst").val() == "Check-In") 
		{
			funHelp("checkIn");
			fieldName = "checkInRooms";
		} */
		else if ($("#cmbAgainst").val() == "Bill")
		{
			funHelp("BillForPayment");
			fieldName = "BillForPayment";
		}
	}
	

	function funSetReceiptInfo(response)
	{
		$("#txtReceiptNo").val(response.strReceiptNo);
		$("#lblAgainst").text(response.strAgainst);
		$("#txtDocCode").val(response.strDocNo);
		$("#txtReceiptAmt").val(response.dblReceiptAmt);
		
		if(response.strAgainst=='Reservation')
		{
			funSetPaymentGuestData(response.strDocNo,"Reservation");
		}
		else if(response.strAgainst=='Check-In')
		{
			funSetPaymentGuestData(response.strDocNo,"CheckIn");
		}
		else if(response.strAgainst=='Bill')
		{
			funSetPaymentGuestData(response.strDocNo,"Bill");
		}
		
		$("#txtSettlementCode").val(response.strSettlementCode);
		funSetSettlementType(response.strSettlementCode);
		$("#txtCardNo").val(response.strCardNo);
		$("#txtExpiryDate").val(response.dteExpiryDate);
		$("#txtRemarks").val(response.strRemarks);
		$("#flagForAdvAmt").val(response.strFlagOfAdvAmt);
		
	}
	
	

	function funHelp(transactionName)
	{
		fieldName=transactionName;
		window.open("searchform.html?formname="+transactionName+"&searchText=","mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=900,height=600,left=400px");
		//window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
	}
	


/**
* Success Message After Saving Record
**/
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
			{%>
				alert("Data Save successfully\n\n"+message);
				var reciptNo='';
				var isOk=confirm("Do You Want to Generate Slip?");
				if(isOk)
 				{
	 				reciptNo='<%=session.getAttribute("GenerateSlip").toString()%>';
	 				checkAgainst='<%=session.getAttribute("Against").toString()%>';
	    			window.open(getContextPath()+"/rptReservationPaymentRecipt.html?reciptNo="+reciptNo+"&checkAgainst="+checkAgainst,'_blank');
					session.removeAttribute("GenerateSlip");
					session.removeAttribute("Against");
				}<%
			}
		}%>
	
	
 	/**
	*   Attached document Link
	**/
	$(function()
	{
	
		$('a#baseUrl').click(function() 
		{
			if($("#txtReceiptNo").val().trim()=="")
			{
				alert("Please Select Receipt Number");
				return false;
			}
		   	window.open('attachDoc.html?transName=frmPMSReprintReceipt.jsp&formName=Payment&code='+$('#txtReceiptNo').val(),"mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
		});
		
		/**
		* On Blur Event on Reservation Code Textfield
		**/
		$('#txtReceiptNo').blur(function() 
		{
			var code = $('#txtReceiptNo').val();
			if (code.trim().length > 0 && code !="?" && code !="/")
			{
				funSetData(code);
			}
		});
		
	});
 	
 	
 	
 	
	function funValidateFields(actionName,object)
	{
		var flg=true;
		
		if($("#txtReceiptNo").val().trim().length==0)
		{
			alert("Please Select Receipt Number mode!!");
			 flg=false;
		}
		
	
		var checkAgainst= $("#lblAgainst").text();
		var reciptNo='';
		reciptNo = $("#txtReceiptNo").val();
		if(flg==true)
			{
				window.open(getContextPath()+"/rptReprintReservationPaymentRecipt.html?reciptNo="+reciptNo+"&checkAgainst="+checkAgainst,'_blank');	
			}
		
		return flg;
	}
	
function funCreateNewGuest(){
		
		window.open("frmGuestMaster.html", "myhelp", "scrollbars=1,width=500,height=350");
<%-- 		var GuestDetails='<%=session.getAttribute("GuestDetails").toString()%>'; --%>
// 		var guest=GuestDetails.split("#");
		
	}
	
	
	
	

</script>

</head>
<body>

	<div id="formHeading">
	<label>Reprint Receipt</label>
	</div>

<br/>
<br/>

<s:form name="Reprint Receipt" method="GET" action="">
	
	<table class="masterTable">
	
		<tr>
			<td>Payment Receipt No</td>
		
			<td>
				<s:input id="txtReceiptNo" path="strReceiptNo" readonly="readonly" ondblclick="funHelp('receiptNo');" class="searchTextBox"></s:input>
			</td>
			<td colspan="3"></td>
		</tr>
		
		<tr>
			<td>Against</td>
		
			<td>
				<label id="lblAgainst"></label>
			</td>
			<td colspan="3"></td>
		</tr>
		
	  
		  
	  	<tr>
	      	<td colspan="1"><label>Guest Name</label></td>
		   	<td colspan="2" ><label id="lblGuestFullName"></label></td>	
		   	<td colspan="2"></td>
	  	</tr>

		
		
		
		
	</table>

		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button" onclick="return funValidateFields('submit',this)" />
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>
       <s:input type="hidden"  id="flagForAdvAmt" path="strFlagOfAdvAmt" value="N" name="saddr" />
	</s:form>
</body>
</html>
