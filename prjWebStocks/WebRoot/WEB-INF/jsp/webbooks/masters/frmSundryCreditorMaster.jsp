<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<style type="text/css">  
   
	#tblOpeningBalance th:nth-child(1),#tblOpeningBalance th:nth-child(2),
	#tblOpeningBalance td:nth-child(1),#tblOpeningBalance td:nth-child(2)
	{
		text-align: left;
	}
	#tblOpeningBalance td:nth-child(3),#tblOpeningBalance td:nth-child(5),#tblOpeningBalance td:nth-child(6)
	{
		text-align: right;
	}
	#tblOpeningBalance th:nth-child(4),#tblOpeningBalance td:nth-child(4)
	{
		text-align: center;
	}
	#txtRemarks
	{
    	resize: none;
	}
</style>
<script type="text/javascript">
	var fieldName;
	
	function funCheckDataValidation(tableId)
	{
	    switch(tableId)
	    {
	    	case 'tblOpeningBalance':
		        var accountCode = $("#txtAccountCode").val();
			    var accountName = $("#txtAccountName").val();
			    var openingBalance = $("#txtOpeningBalance").val();
			    if(parseFloat(openingBalance)<0){
			    	alert("Opening Balance is not less than Zero.")
			    	return false;
			    }
			    var DrCr= $("#cmbDrCr").val();
			    var currentBalance = $("#txtCurrentBalance").val();
			    
			    if(accountCode.trim()==''||accountName.trim()==''||openingBalance.trim()==''||DrCr.trim()==''||currentBalance.trim()=='')
			    {	       
			       return false;
			    }
			    else
			    {
			       return true;
			    }
			    break;
			    
	    	case 'tblItemDetails':
		        var productCode = $("#txtProductCode").val();
			    var productName = $("#txtProductName").val();
			    var licenseAmount = $("#txtLicenseAmount").val();
			    var amcAmount = $("#txtAMCAmount").val();
			    var amcType= $("#cmbAMCType").val();
			    var installationDate = $("#txtInstallationDate").val();
			    var warrantyInDays = $("#txtWarrInDays").val();
			    
			    if(productCode.trim()==''||productName.trim()==''||licenseAmount.trim()==''||amcAmount.trim()==''||amcType.trim()==''||installationDate.trim()==''||warrantyInDays.trim()=='')
			    {	       
			       return false;
			    }
			    else
			    {
			       return true;
			    }
			    break;
	    }
	}
	
	function onClickedConsolidatedBilling()
	{
	    var flagConsolidatedBilling="No";
	    if($("#chkConsolidated").prop("checked") == true)
	    {
	       flagConsolidatedBilling="Yes";
	    }
	    $("#chkConsolidated").val(flagConsolidatedBilling);	  		    
	}
	/* function to add rows to  Opening Balance dynamic table */
	function funAddRowToTblOpeningBalance(tableId)
	{
	    var table=document.getElementById(tableId);	    
	    var rowCount=table.rows.length;
	    var flag=true;
	    var row = table.insertRow(rowCount);
	    if(rowCount>1)
	    {
	        var accountCode= $("#txtAccountCode").val();
	        for(var i=1;i<rowCount;i++)
	        {
	            var containsAccountCode=table.rows[i].cells[0].innerHTML;
	            if(accountCode.trim()==containsAccountCode)
	            {	               
	                flag=false;	                
	            }
	        }	        
	    }
	    if(flag)
	    {
	       /*  var row=table.insertRow();
		    var col0=row.insertCell(0);
		    var col1=row.insertCell(1);
		    var col2=row.insertCell(2);
		    var col3=row.insertCell(3);
		    var col4=row.insertCell(4);
		    var col5=row.insertCell(5); */
		    
		    row.insertCell(0).innerHTML = "<input readonly=\"readonly\" size=\"10%\" style=\"width:98%;\" class=\"Box\"  name=\"listSundryCreditorOpenongBalModel["+(rowCount)+"].strAccountCode\" id=\"strAccountCode."+(rowCount)+"\" style=\"text-align: left;  height:20px;\"  value='"+$("#txtAccountCode").val()+"' />";
		    row.insertCell(1).innerHTML = "<input readonly=\"readonly\" size=\"10%\" style=\"width:98%;\"  class=\"Box\"  name=\"listSundryCreditorOpenongBalModel["+(rowCount)+"].strAccountName\" id=\"strAccountName."+(rowCount)+"\" style=\"text-align: left;  height:20px;\"  value='"+$("#txtAccountName").val()+"' />";
		    row.insertCell(2).innerHTML = "<input readonly=\"readonly\" size=\"10%\" style=\"width:98%; text-align: right; padding-right: 4px;\"  class=\"Box\"  name=\"listSundryCreditorOpenongBalModel["+(rowCount)+"].dblOpeningbal\" id=\"dblOpeningbal."+(rowCount)+"\" style=\"text-align: left;  height:20px;\"  value='"+$("#txtOpeningBalance").val()+"' />";
		    row.insertCell(3).innerHTML = "<input readonly=\"readonly\" size=\"3%\" style=\"width:98%;\"  class=\"Box\"  name=\"listSundryCreditorOpenongBalModel["+(rowCount)+"].strCrDr\" id=\"strCrDr."+(rowCount)+"\" style=\"text-align: left;  height:20px;\"  value='"+$("#cmbDrCr").val()+"' />";
		    row.insertCell(3).innerHTML = "<select  id=\"strCrDr."+(rowCount)+"\" name=\"listSundryCreditorOpenongBalModel["+(rowCount)+"].strCrDr\" class=\"BoxW124px\"><option selected value='"+$("#cmbDrCr").val()+"'>"+$("#cmbDrCr").val()+"</option><option value=\"Dr\">Dr</option><option value=\"Cr\">Cr</option></select>"
		    
		    row.insertCell(4).innerHTML = "<input readonly=\"readonly\" size=\"10%\" class=\"Box\"  name=\"currentBal\" id=\"currentBal."+(rowCount)+"\" style=\" padding-right: 4px; text-align: right; width:98%;  height:20px;\"  value='"+$("#txtCurrentBalance").val()+"' />";
		    row.insertCell(5).innerHTML = "<input type=\"button\" value=\"Delete\"  class=\"deletebutton\" onclick=\"funRemoveRow(this,'tblOpeningBalance')\" />";	
// 		    col0.innerHTML = $("#txtAccountCode").val();
// 		    col1.innerHTML = $("#txtAccountName").val();
// 		    col2.innerHTML = $("#txtOpeningBalance").val();
// 		    col3.innerHTML = $("#cmbDrCr").val();
// 		    col4.innerHTML = $("#txtCurrentBalance").val();
// 		    col5.innerHTML = "<input type=\"button\" value=\"Delete\"  class=\"deletebutton\" onclick=\"funRemoveRow(this,'tblOpeningBalance')\" />";		    		   		    
		    
		    $("#txtAccountCode").val('');
		    $("#txtAccountName").val('');
		    $("#txtOpeningBalance").val('');
		    $("#cmbDrCr").prop('selectedIndex',0);
		    $("#txtCurrentBalance").val('0');
	    }
	    else
	    {
	        alert("Record Already Exists!");
	    }  
	  /*   $("#tblOpeningBalance td:nth-child(6)").each(function (index) {
	        $(this).attr('align', 'right');
	     }); */
	}
	
	/* function to add rows to  items detail dynamic table */
	function funAddRowToTblItemsDetail(tableId)
	{
	    var table=document.getElementById(tableId);	    
	    var rowCount=table.rows.length;
	    var flag=true;
	    if(rowCount>1)
	    {
	        var productCode= $("#txtProductCode").val();
	        for(var i=1;i<rowCount;i++)
	        {
	            var containsProductCode=table.rows[i].cells[0].innerHTML;
	            if(productCode.trim()==containsProductCode)
	            {	               
	                flag=false;	                
	            }
	        }	        
	    }
	    if(flag)
	    {
	        var row=table.insertRow();
		    var col0=row.insertCell(0);
		    var col1=row.insertCell(1);
		    var col2=row.insertCell(2);
		    var col3=row.insertCell(3);
		    var col4=row.insertCell(4);
		    var col5=row.insertCell(5);
		    var col6=row.insertCell(6);
		    var col7=row.insertCell(7);
		    
		    col0.innerHTML = $("#txtProductCode").val();
		    col1.innerHTML = $("#txtProductName").val();
		    col2.innerHTML = $("#txtLicenseAmount").val();
		    col3.innerHTML = $("#txtAMCAmount").val();
		    col4.innerHTML = $("#cmbAMCType").val();
		    col5.innerHTML = $("#txtInstallationDate").val();
		    col6.innerHTML = $("#txtWarrInDays").val();
		    col7.innerHTML = "<input type=\"button\" value=\"Delete\"  class=\"deletebutton\" onclick=\"funRemoveRow(this,'tblItemDetails')\" />";		    		   		    
		    
		    $("#txtProductCode").val('');
		    $("#txtProductName").val('');
		    $("#txtLicenseAmount").val('');
		    $("#txtAMCAmount").val('');
		    $("#cmbAMCType").prop('selectedIndex',0);
		    $("#txtInstallationDate").datepicker('setDate', 'today');
		    $("#txtWarrInDays").val('');
	    }
	    else
	    {
	        alert("Record Already Exists!");
	    }  
	  /*   $("#tblOpeningBalance td:nth-child(6)").each(function (index) {
	        $(this).attr('align', 'right');
	     }); */
	}
	
	function funAddRow(tableId)
	{	    	 
	  var isValidData=funCheckDataValidation(tableId);
	  
	  if(isValidData)
	  {
	      switch(tableId)
		   {
		  	 case "tblOpeningBalance":
		      	 	funAddRowToTblOpeningBalance(tableId);
		       		break;
		  	case "tblItemDetails":
	      	 	funAddRowToTblItemsDetail(tableId);
	       		break;
		   }
	  } 
	  else
	  {
	      alert("Please Enter Valid Data!!!");
	  }    
	    
	   
	}
	function funRemoveRow(selectedRow,tableId)
	{
	    
	    var rowIndex = selectedRow.parentNode.parentNode.rowIndex;	    
	    
	    switch(tableId)
	    {
	    	case 'tblOpeningBalance':
	    			document.getElementById("tblOpeningBalance").deleteRow(rowIndex);
	    			break;
	    	case 'tblItemDetails':
    			document.getElementById("tblItemDetails").deleteRow(rowIndex);
    			break;
	    }
	    
	    /* var table=document.getElementById(tableId);
	    var size= $("#"+tableId+" tr").length;
	    
	    if(size>1)
		{
	        var lastIndex=size-1;
	        table.deleteRow(lastIndex);
	    }
	    else
	    {
	        alert("No Record Found.");
	    }     */
	   
	}
	
	function funSetAccountDetails(accountCode)
	{
	    $("#txtAccountCode").val(accountCode);
		var searchurl=getContextPath()+"/loadAccountMasterData.html?accountCode="+accountCode;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(response.strAccountCode=='Invalid Code')
			        	{
			        		alert("Invalid Account Code");
			        		$("#txtAccountCode").val('');
			        	}
			        	else 
			        	{
				        	$("#txtAccountName").val(response.strAccountName);
				        	$("#txtAccountName").focus();				        					        	
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
	
	function funSetCreditorMasterData(CreditorCode)
	{
	   
		var searchurl=getContextPath()+"/loadSundryCreditorMasterData.html?creditorCode="+CreditorCode;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(response.strCreditorCode=='Invalid Code')
			        	{
			        		alert("Invalid Creditor Code");
			        		$("#txtCreditorCode").val('');
			        		$("#txtFirstName").val('');
			        		$("#txtMiddleName").val('');
			        		$("#txtLastName").val('');
			        	}
			        	else
			        	{					        	    			        	    
			        	    /* Creditor Details */
			        	    $("#txtCreditorCode").val(CreditorCode);
			        	    $("#cmbPrefix").val(response.strPrefix);
				        	$("#txtFirstName").val(response.strFirstName);
				        	$("#txtFirstName").focus();
				        	$("#txtMiddleName").val(response.strMiddleName);
				        	$("#txtLastName").val(response.strLastName);
				        	$("#txtGLCode").val(response.strAccountCode);
							$("#lblGLCode").text(response.strAccountName);
				        	//set category data
				        //	funSetCategoryData(response.strCategoryCode);				        					        	
				        	$("#txtAddressLine1").val(response.strAddressLine1);
				        	$("#txtAddressLine2").val(response.strAddressLine2);
				        	$("#txtAddressLine3").val(response.strAddressLine3);
				        	$("#cmbBlocked").val(response.strBlocked);
				        	//fun set reason master data
				        	if(response.strExpiryReasonCode.toString.length!=0)
				        	{
				        	funSetReasonData(response.strExpiryReasonCode);
				        	}
				        	$("#txtFax").val(response.strFax);
				        	$("#txtLandmark").val(response.strLandmark);
				        	$("#txtEmail").val(response.strEmail);
				        	//set area data
				        //	funSetAreaData(response.strArea);
				        	//set city data
				        //	funSetCityData(response.strCity);
				        	//set state data
				       // 	funSetStateData(response.strState);
				        	//set region data
				       // 	funSetRegionData(response.strRegion);
				        	//set country data
				       // 	funSetCountryData(response.strCountry);				        	
				        	$("#cmbCurrencyType").val(response.strCurrencyType);
				        	$("#txtLicenseFee").val(response.dblLicenseFee);
				        	$("#txtAnnualFee").val(response.dblAnnualFee);
				        	$("#txtRemarks").val(response.strRemarks);				        	
				        	$("#txtZipCode").val(response.longZipCode);
				        	$("#txtCreditDays").val(response.intCreditDays);				        	
				        	$("#txtTelNo1").val(response.strTelNo1);
				        	$("#txtTelNo2").val(response.strTelNo2);
				        	$("#txtMobileNo").val(response.longMobileNo);
				        	$("#cmbOperational").val(response.strOperational);
				        	/* Creditor Details */
				        	
				        	/* Billing Details */
				        	$("#txtContactPerson1").val(response.strContactPerson1);
				        	$("#txtContactPerson2").val(response.strContactPerson2);
				        	$("#txtContactDesignation1").val(response.strContactDesignation1);
				        	$("#txtContactDesignation2").val(response.strContactDesignation2);
				        	$("#txtContactEmail1").val(response.strContactEmail1);
				        	$("#txtContactEmail2").val(response.strContactEmail2);
				        	$("#txtContactTelNo1").val(response.strContactTelNo1);
				        	$("#txtContactTelNo2").val(response.strContactTelNo2);
				        	if(response.strConsolidated=="Yes")
				            {
				        	    $("#chkConsolidated").prop('checked',true);					        	    
				        	}
				        	else
				        	{
				        	    $("#chkConsolidated").prop('checked',false);	
				        	}
				        	$("#txtAccountHolderCode").val(response.strAccountHolderCode);
				        	$("#txtAccountHolderName").val(response.strAccountHolderName);
				        	$("#cmbAMCCycle").val(response.strAMCCycle);
				        	$("#txtAMCRemarks").val(response.strAMCRemarks);
				        	/* Billing Details */
				        	
				        	/* ECS Details */
				        	$("#cmbECSYN").val(response.strECSYN);
				        	$("#txtECSLimit").val(response.dblECS);
				        	$("#txtAccountNo").val(response.strAccountNo);
				        	$("#cmbSaveCurAccount").val(response.strSaveCurAccount);
				        	$("#txtHolderName").val(response.strHolderName);
				        	$("#txtAlternateCode").val(response.strAlternateCode);				        	
				        	$("#txtMICRNo").val(response.strMICRNo);
				        	$("#cmbECSActivate").val(response.strECSActivate);
				        	/* ECS Details */
				        	
				        	/* Opening Balanace Details */
				        	
				        	$.each(response.listSundryCreditorOpenongBalModel, function(i, item) {
						
								funLoadOpeningBalTable(item[0],item[1],item[2],item[3]);
							});	
				        	
				        	/* Opening Balanace  Details */
				        	
				        	/* Item Details */
				        	/* Item Details */
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
	function funLoadOpeningBalTable(strAccountCode,strAccountName,dblOpeningbal,strCrDr)
	{
		
			var table = document.getElementById("tblOpeningBalance");
		    var rowCount = table.rows.length;
		    var row = table.insertRow(rowCount);
		    
		    row.insertCell(0).innerHTML= "<input class=\"Box\" size=\"10%\" style=\"width: 98%;\" name=\"listSundryCreditorOpenongBalModel["+(rowCount)+"].strAccountCode\" id=\"txtAccountCode."+(rowCount)+"\" value='"+strAccountCode+"' >";
		    row.insertCell(1).innerHTML= "<input class=\"Box\" size=\"10%\" style=\"width: 98%;\" name=\"listSundryCreditorOpenongBalModel["+(rowCount)+"].strAccountName\"  id=\"txtAccountName."+(rowCount)+"\" value='"+strAccountName+"'>";		    	    
		    row.insertCell(2).innerHTML= "<input class=\"Box\" size=\"10%\" style=\"text-align: right; width: 98%; padding-right: 4px;\" size=\"8%\" name=\"listSundryCreditorOpenongBalModel["+(rowCount)+"].dblOpeningbal\" id=\"txtOpeningBalance."+(rowCount)+"\" value='"+dblOpeningbal+"'>";
		   // row.insertCell(3).innerHTML= "<input class=\"Box\"  size=\"3%\" style=\"width: 98%;\" name=\"listSundryCreditorOpenongBalModel["+(rowCount)+"].strCrDr\" id=\"cmbDrCr."+(rowCount)+"\" value="+strCrDr+">";
		   row.insertCell(3).innerHTML = "<select  id=\"strCrDr."+(rowCount)+"\" name=\"listSundryCreditorOpenongBalModel["+(rowCount)+"].strCrDr\" class=\"BoxW124px\"><option selected value='"+strCrDr+"'>"+strCrDr+"</option><option value=\"Dr\">Dr</option><option value=\"Cr\">Cr</option></select>"
		    row.insertCell(4).innerHTML= "<input class=\"Box\"  size=\"10%\" style=\"text-align: right; width: 98%;  padding-right: 4px;\" id=\"currentBal."+(rowCount)+"\" value="+0+">";
			row.insertCell(5).innerHTML="<input type=\"button\" value=\"Delete\"  class=\"deletebutton\" onclick=\"funRemoveRow(this,'tblOpeningBalance')\" />";   
		
	}
	
	function funSetReasonData(reasonCode)
	{
	   
		var searchurl=getContextPath()+"/loadWebBooksReasonMasterData.html?reasonCode="+reasonCode;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(response.strReasonCode=='Invalid Code')
			        	{
			        		alert("Invalid Reason Code");
			        		$("#txtExpiryReasonCode").val('');
			        		$("#txtExpiryReasonName").val('');
			        	}
			        	else
			        	{		
			        	    $("#txtExpiryReasonCode").val(reasonCode);
				        	$("#txtExpiryReasonName").val(response.strReasonDesc);
				        	$("#txtExpiryReasonName").focus();				        						        						      
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
	
	function funSetCategoryData(categoryCode)
	{
	   
		var searchurl=getContextPath()+"/loadCategoryData.html?categoryCode="+categoryCode;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(response.strCatCode=='Invalid Code')
			        	{
			        		alert("Invalid Category Code");
			        		$("#strCategoryCode").val('');
			        		$("#txtCategoryName").val('');
			        	}
			        	else
			        	{		
			        	    $("#txtCategoryCode").val(categoryCode);
				        	$("#txtCategoryName").val(response.strCatName);
				        	$("#txtCategoryName").focus();				        						        						      
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
	
	function  funSetAreaData(areaCode)
	{
	    $("#txtArea").val(areaCode);
	    var searchurl=getContextPath()+"/loadAreaMasterData.html?areaCode="+areaCode;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(response.strAreaCode=='Invalid Code')
			        	{
			        		alert("Invalid Area Code");
			        		$("#txtArea").val('');
			        		$("#txtAreaName").val('');
			        	}
			        	else 
			        	{		
			        	    $("#txtAreaName").val(response.strAreaName);				        							        						       
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
	
	function funSetCityData(cityCode)
	{
	    $("#txtCity").val(cityCode);
	    var searchurl=getContextPath()+"/loadCityMasterData.html?cityCode="+cityCode;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(response.strCityCode=='Invalid Code')
			        	{
			        		alert("Invalid City Code");
			        		$("#txtCity").val('');
			        		$("#txtCityName").val('');
			        	}
			        	else 
			        	{		
			        	    $("#txtCityName").val(response.strCityName);				        							        						       
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
	
	function funSetStateData(stateCode)
	{
	    $("#txtState").val(stateCode);
	    var searchurl=getContextPath()+"/loadStateMasterData.html?stateCode="+stateCode;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(response.strStateCode=='Invalid Code')
			        	{
			        		alert("Invalid State Code");
			        		$("#txtState").val('');
			        		$("#txtStateName").val('');
			        	}
			        	else 
			        	{		
			        	    $("#txtStateName").val(response.strStateName);				        							        						       
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
	
	function funSetRegionData(regionCode)
	{
	    $("#txtRegion").val(regionCode);
	    var searchurl=getContextPath()+"/loadRegionMasterData.html?regionCode="+regionCode;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(response.strRegionCode=='Invalid Code')
			        	{
			        		alert("Invalid Region Code");
			        		$("#txtRegion").val('');
			        		$("#txtRegionName").val('');
			        	}
			        	else 
			        	{		
			        	    $("#txtRegionName").val(response.strRegionName);				        							        						       
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
	
	
	function funSetCountryData(countryCode)
	{
	    $("#txtCountry").val(countryCode);
	    var searchurl=getContextPath()+"/loadCountryMasterData.html?countryCode="+countryCode;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(response.strCountryCode=='Invalid Code')
			        	{
			        		alert("Invalid Country Code");
			        		$("#txtCountry").val('');
			        		$("#txtCountryName").val('');
			        	}
			        	else 
			        	{		
			        	    $("#txtCountryName").val(response.strCountryName);				        							        						       
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
	
	function funSetAccountHolderData(accountHolderCode)
	{
	    $("#txtAccountHolderCode").val(accountHolderCode);
		var searchurl=getContextPath()+"/loadACHolderMasterData.html?acHolderCode="+accountHolderCode;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(response.strACHolderCode=='Invalid Code')
			        	{
			        		alert("Invalid Account Holder Code");
			        		$("#txtAccountHolderCode").val('');
			        		$("#txtAccountHolderName").val('');
			        	}
			        	else
			        	{
				        	$("#txtAccountHolderName").val(response.strACHolderName);				        	
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
						{%>	
							alert("Data Save successfully\n\n"+message);
						<%}
		}%>

	});
	
	$(function() {
		
		$('#txtCreditorCode').blur(function() {
			var code = $('#txtCreditorCode').val();
			if(code.trim().length > 0 && code !="?" && code !="/")
			{
				funSetCreditorMasterData(code);
			}
		});
		
		$('#strCategoryCode').blur(function() {
			var code = $('#strCategoryCode').val();
			if(code.trim().length > 0 && code !="?" && code !="/")
			{
				funSetCategoryData(code);
			}
		});
		
		$('#txtArea').blur(function() {
			var code = $('#txtArea').val();
			if(code.trim().length > 0 && code !="?" && code !="/")
			{
				funSetAreaData(code);
			}
		});
		
		$('#txtCity').blur(function() {
			var code = $('#txtCity').val();
			if(code.trim().length > 0 && code !="?" && code !="/")
			{
				funSetCityData(code);
			}
		});
		
		$('#txtState').blur(function() {
			var code = $('#txtState').val();
			if(code.trim().length > 0 && code !="?" && code !="/")
			{
				funSetStateData(code);
			}
		});
		

		$('#txtRegion').blur(function() {
			var code = $('#txtRegion').val();
			if(code.trim().length > 0 && code !="?" && code !="/")
			{
				funSetRegionData(code);
			}
		});
		
		$('#txtCountry').blur(function() {
			var code = $('#txtCountry').val();
			if(code.trim().length > 0 && code !="?" && code !="/")
			{
				funSetCountryData(code);
			}
		});
		
		$('#txtAccountHolderCode').blur(function() {
			var code = $('#txtAccountHolderCode').val();
			if(code.trim().length > 0 && code !="?" && code !="/")
			{
				funSetAccountHolderData(code);
			}
		});
		
		$('#txtExpiryReasonCode').blur(function() {
			var code = $('#txtExpiryReasonCode').val();
			if(code.trim().length > 0 && code !="?" && code !="/")
			{
				funSetReasonData(code);
			}
		});
		
		$('#txtAccountCode').blur(function() {
			var code = $('#txtAccountCode').val();
			if(code.trim().length > 0 && code !="?" && code !="/")
			{
				funSetAccountDetails(code);
			}
		});
		
		$('#txtGLCode').blur(function() {
			var code = $('#txtGLCode').val();
			if(code.trim().length > 0 && code !="?" && code !="/")
			{
				funSetGLCode(code);
			}
		});
	
	});
		

	function funSetData(code){

		switch(fieldName)
		{		
			case "creditorCode":
				funRemRows("tblOpeningBalance");
			     funSetCreditorMasterData(code);			    
				 break;		
			case "categoryCode":
			     funSetCategoryData(code);			    
				 break;
			case "areaCode":
			     funSetAreaData(code);			    
				 break;
			case "cityCode":
			     funSetCityData(code);			    
				 break;
			case "stateCode":
			     funSetStateData(code);			    
				 break;
			case "regionCode":
			     funSetRegionData(code);
			     break;
			case "countryCode":
			     funSetCountryData(code);
			     break;
			case "acHolderCode":
			     funSetAccountHolderData(code);
				 break;
			case "reasonCode":
			     funSetReasonData(code);
			     break;
			case "accountCode": 
			     funSetAccountDetails(code);
				 break;
			case 'creditorAccountCode' : 
				funSetGLCode(code);
				break;
		}
	}
	
	function funSetGLCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadAccontCodeAndName.html?accountCode=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strAccountCode!="Invalid Code")
		    	{
					$("#txtGLCode").val(response.strAccountCode);
					$("#lblGLCode").text(response.strAccountName);
					$("#txtFromDebtorCode").focus();					
		    	}
		    	else
			    {
			    	alert("Invalid Account Code");
			    	$("#txtVouchNo").val("");
			    	$("#lblGLCode").text("");
			    	$("#txtVouchNo").focus();
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

	function funHelp(transactionName)
	{
		fieldName=transactionName;
	//	window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
		window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
	}
	
	$(document).ready(function() 
    {
		$(".tab_content").hide();
		$(".tab_content:first").show();
		$("ul.tabs li").click(function() 
		{
			$("ul.tabs li").removeClass("active");
			$(this).addClass("active");
			$(".tab_content").hide();
			var activeTab = $(this).attr("data-state");
			
			$("#" + activeTab).fadeIn();					
			
		});
		
		
		 $("#txtInstallationDate").datepicker(
		 {
			dateFormat : 'dd-mm-yy'
		 });
		$("#txtInstallationDate").datepicker('setDate', 'today');
		var cur="";
		<%if(session.getAttribute("successMessage") != null)
		{%>
		  cur='<%=session.getAttribute("currencyCode").toString()%>';
		<%}%>
		$("#cmbCurrencyType").val(cur);
	});
	
	function funRemRows(tablename) 
	{
		var table = document.getElementById(tablename);
		var rowCount = table.rows.length;
		while(rowCount>1)
		{
			table.deleteRow(1);
			rowCount--;
		}
	}
	function funValidateForm() 
	{
		var flg=false;
		var creditorName=$("#txtFirstName").val().trim();
		if(creditorName=='') 
		{
			alert('Enter Creditor Name!!!');
			$("#txtFirstName").focus();
		}
		else
		{
			var pattern =/^[a-zA-Z\s]+$/;
			if (pattern.test(creditorName)) 
			{
				flg=true;
				if($('#txtMiddleName').val().trim().length!=0)
				{
					var creditorMiddleName=$("#txtMiddleName").val().trim();
					var pattern =/^[a-zA-Z\s]+$/;
					if (pattern.test(creditorMiddleName)) 
					{
						flg=true;
					}
					else
					{
						alert("Enter valid Creditor's MiddleName!!");
						flg=false;
					}
				}
				
				if($('#txtLastName').val().trim().length!=0)
				{
					var creditorLastName=$("#txtLastName").val().trim();
					var pattern =/^[a-zA-Z\s]+$/;
					if (pattern.test(creditorLastName)) 
					{
						flg=true;
					}
					else
					{
						alert("Enter valid Creditor's LastName!!");
						flg=false;
					}
				}
			}
			else
			{
				alert("Enter valid Creditor name!!");
			}
			
			if($('#cmbOperational').val()=='No')
			{
		
			var isOk=confirm("Do you want to delete this enrty??");
			if(isOk)
			{
				flg=funCheckRecordsInTransactions();
			}
			}
			
			if(!flg)
			{
				location.reload();
			}
		}
		
		
		return flg
	}
	function funResetFields() 
	{
		$('#tblOpeningBalance tbody').empty()
		$('#tblItemDetails tbody').empty()
		
	}
	
	function funCheckRecordsInTransactions()
	{
		var accountCode=$('#txtCreditorCode').val()
		var result;
		var searchurl=getContextPath()+"/loadRecordsInTransaction.html?type=Creditor&docCode="+accountCode;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        async:false,
			        success: function(response)
			        {
			        	if(!response)
			        	{
			        		alert("Account Delete Successfully");
			        		result= false;
			        	}else{
			        		alert("There are records present in transaction for this entry, please delete those records First")
			        			$('#cmbOperational').val("Yes");
			        		result= true;
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
	return result;	
	}
	
</script>

</head>
<body>

	<div id="formHeading">
	<label>Sundry Creditor Master</label>
	</div>

<br/>
<br/>

	<s:form name="SundryCreditorMaster" method="POST" action="saveSundryCreditorMaster.html">

	    <!-- Main Creditor Master Data -->
		<table class="masterTable" style="width: 99%">
			<tr>
			    <td width="85px"><label style="width:65px">Creditor Code</label></td>
			    <td width="125px"><s:input id="txtCreditorCode" path="strCreditorCode"  ondblclick="funHelp('creditorCode')" cssClass="searchTextBox" cssStyle="width:125px" /></td>
			    <td width="50px"><s:select id="cmbPrefix" path="strPrefix" items="${listMrMrs}" cssClass="BoxW124px" cssStyle="width:50px"/></td>			        			        
			    <td width="120px"><s:input id="txtFirstName" path="strFirstName" required="true" cssClass="longTextBox" cssStyle="width:120px" /></td>
			    <td width="85px"><s:input id="txtMiddleName" path="strMiddleName"  cssClass="longTextBox" cssStyle="width:85px" /></td>
			    <td><s:input id="txtLastName" path="strLastName"  cssClass="longTextBox" cssStyle="width:65%"/></td>			    	    		        			  
			</tr>
			<tr>
				<td width="65px"><label style="width:65px">Category Code</label></td>
				<td width="125px"><s:input id="txtCategoryCode" path="strCategoryCode" ondblclick="funHelp('categoryCode')" cssClass="searchTextBox" cssStyle="width:125px" /></td>
				<td colspan="4"><s:input id="txtCategoryName" path="" required="true" readonly="true"  cssClass="longTextBox" cssStyle="width:37%" /></td>				
			</tr>
			<tr>
			    <td><label >Account Code</label></td>
			    <td><s:input id="txtGLCode" path="strAccountCode"  ondblclick="funHelp('creditorAccountCode')" cssClass="searchTextBox"/></td>			        			        
			    <td  colspan="4"><label id="lblGLCode"></label></td>			    		        			   
			</tr>
		</table>			
		<br />
		
		<!-- <table style="border: 0px solid black; width: 100%; height: 100%; margin-left: auto; margin-right: auto; background-color: #C0E4FF; padding-left: 100px;"> -->
		<table class="masterTable" style="width: 99%">
			<tr>
				<th style="padding-left: 0px">				
					 <div id="tab_container" style="height: 100%" >
						<ul class="tabs">
							<li class="active" data-state="tab1" style="width: 75px; padding-left: 10px">Creditor Detail</li>
							<li data-state="tab2" style="width: 75px; padding-left: 10px">Billing Detail</li>
							<li data-state="tab3" style="width: 75px; padding-left: 10px">ECS Detail</li>
							<li data-state="tab4" style="width: 100px; padding-left: 10px">Opening Balance</li>
							<li data-state="tab5" style="width: 75px; padding-left: 10px">Item Detail</li>													
						</ul>
					</div>
				</th>
			</tr>
	    </table>			    	    	 
	    
	    <!--Creditor Detail-->
		<div id="tab1" class="tab_content" style="height: auto;">
		<br> 
			<table class="masterTable" style="width: 99%">								
			    <tr>
					<td><label>Address</label></td>
					<td colspan="2"><s:input id="txtAddressLine1" path="strAddressLine1" cssClass="longTextBox" cssStyle="width:325px" /></td>
					<td><label>Blocked</label></td>
					<td colspan="3"><s:select id="cmbBlocked" path="strBlocked" items="${listBlocked}" cssClass="BoxW124px" /></td>
					<!-- <td></td>
					<td></td> -->
			    </tr>		    				   
			    <tr>
			    	<td></td>			    	
			    	<td colspan="2"><s:input id="txtAddressLine2" path="strAddressLine2"  cssClass="longTextBox" cssStyle="width:325px" /></td>
			    	<td><label >Reason</label></td>
				    <td style="width:125px"><s:input id="txtExpiryReasonCode" path="strExpiryReasonCode" ondblclick="funHelp('reasonCode')" cssClass="searchTextBox"/></td>			        			        
				    <td colspan="2"><s:input id="txtExpiryReasonName" path="" readonly="true"  cssClass="longTextBox" cssStyle="width:200px" /></td>
				     
			    </tr>
			    <tr>
			    	<td></td>			    	
			    	<td colspan="2"><s:input id="txtAddressLine3" path="strAddressLine3"  cssClass="longTextBox" cssStyle="width:325px" /></td>
			    	<td><label >FAX</label></td>				    			        			       
				    <td colspan="3"><s:input id="txtFax" path="strFax"  cssClass="longTextBox" cssStyle="width:117px" /></td>				    
			    </tr>
			    <tr>
			    	<td><label>Landmark</label></td>			    	
			    	<td colspan="2"><s:input id="txtLandmark" path="strLandmark"  cssClass="longTextBox" cssStyle="width:325px" /></td>
			    	<td><label>Email</label></td>				    			        			       
				    <td colspan="3"><s:input id="txtEmail" path="strEmail"   cssClass="longTextBox" placeholder="example@email.com"/></td>  <!-- type="email" -->				  
			    </tr>
			    <tr>
			    	<td><label>Area</label></td>
				    <td style="width: 130px;"><s:input id="txtArea" path="strArea" ondblclick="funHelp('areaCode')" cssClass="searchTextBox"/></td>			
			    	<td><s:input id="txtAreaName" path="" readonly="true"   cssClass="longTextBox" cssStyle="width:190px" /></td>			    			        			       
				    <td><label>Currency Type</label></td>
					<td colspan="3"><s:select id="cmbCurrencyType" path="strCurrencyType" items="${listCurrencyType}" cssClass="BoxW124px" /></td>
									
			    </tr>
			    <tr>
			    	<td><label>City</label></td>
				    <td><s:input id="txtCity" path="strCity"  ondblclick="funHelp('cityCode')" cssClass="searchTextBox"/></td>			
			    	<td><s:input id="txtCityName" path=""  readonly="true" cssClass="longTextBox" cssStyle="width:190px" /></td>			    			        			       
				    <td><label>License Fee in Rs.</label></td>
					<td colspan="3"><s:input id="txtLicenseFee" path="dblLicenseFee"  cssClass="longTextBox" cssStyle="width:115px" /></td>							
			    </tr>
			    <tr>
			    	<td><label>Sate</label></td>
				    <td><s:input id="txtState" path="strState"  ondblclick="funHelp('stateCode')" cssClass="searchTextBox"/></td>			
			    	<td><s:input id="txtStateName" path=""  readonly="true" cssClass="longTextBox" cssStyle="width:190px" /></td>			    			        			       
				    <td><label>Annual Fee in Rs.</label></td>
					<td colspan="3"><s:input id="txtAnnualFee" path="dblAnnualFee"  cssClass="longTextBox" cssStyle="width:115px" /></td>								
			    </tr>
			    <tr>
			    	<td><label>Region</label></td>
				    <td><s:input id="txtRegion" path="strRegion" ondblclick="funHelp('regionCode')" cssClass="searchTextBox"/></td>			
			    	<td><s:input id="txtRegionName" path=""  readonly="true" cssClass="longTextBox" cssStyle="width:190px" /></td>			    			        			       
				    <td><label>Remarks</label></td>
					<td colspan="3"><s:textarea id="txtRemarks" path="strRemarks"   cssClass="longTextBox" cssStyle="height:50px" /></td>					
			    </tr>
			    <tr>
			    	<td><label>Counrty</label></td>
				    <td><s:input id="txtCountry" path="strCountry" ondblclick="funHelp('countryCode')" cssClass="searchTextBox"/></td>			
			    	<td ><s:input id="txtCountryName" path=""  readonly="true" cssClass="longTextBox" cssStyle="width:190px" /></td>	
			    	<td><label>Operational</label> </td>
					<td colspan="3"><s:select id="cmbOperational" path="strOperational" items="${listOperational}" cssClass="BoxW124px"/></td>		    			        			       				   				
			    </tr>
			    <tr>
			    	<td><label>Zip</label></td>				    		
			    	<td colspan="2"><s:input id="txtZipCode" path="longZipCode"   cssClass="longTextBox"  cssStyle="width:117px" /></td>	<!-- pattern="[0-9]{7}"		  -->   			        			       				    
				    <td><label>Telephone Nos.</label></td>		
					<td><s:input id="txtTelNo1" path="strTelNo1"  cssClass="longTextBox" style="width: 125px;" /></td>
					<td colspan="2"><s:input id="txtTelNo2" path="strTelNo2"  cssClass="longTextBox" style="width: 125px;" /></td>									
			    </tr>
			    <tr>
			    	<td><label>Credit Days</label></td>				    		
			    	<td colspan="2"><s:input id="txtCreditDays" path="intCreditDays"  cssClass="longTextBox" cssStyle="width:117px" pattern="\d*" /></td>			    			        			       				   
				    <td><label>Mobile No.</label></td>		
					<td colspan="3"><s:input id="txtMobileNo" path="longMobileNo"  cssClass="longTextBox"  placeholder="10 digit mobile number" /></td> <%-- pattern="[0-9]{10}" --%>											
			    </tr>
			</table>						
		</div>						
		<!--Creditor Detail-->
		
		<!--Billing Detail-->
		<div id="tab2" class="tab_content" style="height: auto;">
		 <br> 
			<table class="masterTable" style="width: 99%">				
				<tr>
				        <th><label style="font-style: italic;">Contact Person 1</label></th>
				        <th></th>
				        <th></th>
				        <th><label style="font-style: italic;">Contact Person 2</label></th>				        
			   	</tr>	
			   	<tr>
					<td><label>Name</label></td>
					<td><s:input id="txtContactPerson1" path="strContactPerson1"  cssClass="longTextBox"/></td>
					<td><label>Name</label></td>
					<td><s:input id="txtContactPerson2" path="strContactPerson2"  cssClass="longTextBox"/></td>
			    </tr>	
			    <tr>
					<td><label>Designation</label></td>
					<td><s:input id="txtContactDesignation1" path="strContactDesignation1"  cssClass="longTextBox"/></td>
					<td><label>Designation</label></td>
					<td><s:input id="txtContactDesignation2" path="strContactDesignation2"  cssClass="longTextBox"/></td>
			    </tr>	
			    <tr>
					<td><label>Email</label></td>
					<td><s:input id="txtContactEmail1" path="strContactEmail1"  cssClass="longTextBox"/></td>
					<td><label>Email</label></td>
					<td><s:input id="txtContactEmail2" path="strContactEmail2"  cssClass="longTextBox"/></td>
			    </tr>	
			    <tr>
					<td><label>Telephone No.</label></td>
					<td><s:input id="txtContactTelNo1" path="strContactTelNo1"  cssClass="longTextBox"/></td>
					<td><label>Telephone No.</label></td>
					<td><s:input id="txtContactTelNo2" path="strContactTelNo2"  cssClass="longTextBox"/></td>
			    </tr>			    
			    <tr>
				        <th><label style="font-style: italic;">Additional Information</label></th>
				        <th></th>
				        <th></th>
				        <th></th>				        
			   	</tr>	
			   	<tr>
			    	<td><label>Billing To</label></td>
				    <td><s:input id="txtBillingToCode" path="strBillingToCode" readonly="true"  ondblclick="funHelp('billingTo')" cssClass="searchTextBox"/></td>			
			    	<td colspan="2"><s:input id="" path=""  readonly="true" cssClass="longTextBox" /></td>			    			        			       					
			    </tr>
			    <tr>
			    	<td><label>Consolidated Billing</label></td>
				    <td><s:checkbox id="chkConsolidated" path="strConsolidated" value="" onclick='onClickedConsolidatedBilling()' /></td>			
			    	<td></td>			    			        			       
				    <td></td>					
			    </tr>
			    <tr>
			    	<td><label>Account Holder Code</label></td>
				    <td><s:input id="txtAccountHolderCode" path="strAccountHolderCode"  ondblclick="funHelp('acHolderCode')" cssClass="searchTextBox"/></td>			
			    	<td colspan="2"><s:input id="txtAccountHolderName" path="strAccountHolderName"  readonly="true" cssClass="longTextBox" /></td>			    			        			       				
			    </tr>
			    <tr>
			    	<td><label>AMC Cycle</label> </td>
			    	<td><s:select id="cmbAMCCycle" path="strAMCCycle" items="${listAMCCycle}" cssClass="BoxW124px" /></td>
			    	<td><label>AMC Remark</label></td>
				    <td><s:textarea id="txtAMCRemarks" path="strAMCRemarks"  cssClass="longTextBox" cssStyle="height:50px" /></td>						    				
			    </tr>
			    <tr>
			    	<td></td>
			    	<td></td>
			    	<td></td>
				    <td></td>						    				
			    </tr>
			    			    				   
			</table>						
		</div>					
		<!--Billing Detail-->
		
		<!--ECS Detail-->
		<div id="tab3" class="tab_content" style="height: auto;">
		<br> 
			<table class="masterTable" style="width: 99%">				
				<tr>
				        <th><label>ECS Information</label></th>
				        <th></th>
				        <th></th>
				        <th></th>				        
			   	</tr>	
			   	<tr>
			    	<td><label>ECS Y/N</label> </td>
			    	<td><s:select id="cmbECSYN" path="strECSYN" items="${listECSYN}" cssClass="BoxW124px" /></td>
			    	<td><label>ECS Limit</label></td>
				    <td><s:input id="txtECSLimit" path="dblECS"   cssClass="longTextBox"  /></td>						    				
			    </tr>	
			    <tr>
			    	<td><label>Account No.</label></td>
				    <td><s:input id="txtAccountNo" path="strAccountNo"  cssClass="longTextBox"  /></td>	
			    	<td><label>Saving/Current Account</label> </td>
			    	<td><s:select id="cmbSaveCurAccount" path="strSaveCurAccount" items="${listAccountType}" cssClass="BoxW124px" /></td>					    				
			    </tr>	
			    <tr>
			    	<td><label>Holder Name</label></td>
				    <td><s:input id="txtHolderName" path="strHolderName"  cssClass="longTextBox"  /></td>	
			    	<td><label>Alternate Code</label></td>
				    <td><s:input id="txtAlternateCode" path="strAlternateCode"  cssClass="longTextBox"  /></td>					    				
			    </tr>	
			    <tr>
			    	<td><label>MICR No.</label></td>
				    <td><s:input id="txtMICRNo" path="strMICRNo"  cssClass="longTextBox"  /></td>	
			    	<td><label>ECS Activated</label> </td>
			    	<td><s:select id="cmbECSActivate" path="strECSActivate" items="${listECSActivated}" cssClass="BoxW124px" /></td>					    				
			    </tr>			    	    				  
			</table>						
		</div>					
		<!--ECS Detail-->
		
		<!--Opening Balance-->
		<div id="tab4" class="tab_content" style="height: auto;">
		<br> 
			<table class="masterTable" style="width: 99%">				
				<tr>
				        <th><label>Account Code</label></th>
				        <th><label>Account Name</label></th>
				        <th><label>Opening Balance</label></th>
				        <th><label>Dr/Cr</label></th>
				        <th><label>Current Balance</label></th>		
				        <th></th>		        
			   	</tr>			   			
			   	<tr>
			    	<td style="padding-left: 0px;"><s:input id="txtAccountCode" path=""  cssClass="searchTextBox" ondblclick='funHelp("accountCode")' /></td>	
			    	<td><s:input id="txtAccountName" path=""  cssClass="longTextBox" style="width: 235px"  /></td>	
			    	<td><s:input   type="number" id="txtOpeningBalance" path="" class="decimal-places numberField"  cssStyle="text-align: right;" /></td>			    	
				    <td><s:select id="cmbDrCr" path="" items="${listDrCr}" cssClass="BoxW124px" /></td>	
				    <td><s:input id="txtCurrentBalance" path=""  cssClass="longTextBox" value="0" readonly="true"/></td>
				    <td><input type="button" value="Add"  class="smallButton" onclick='funAddRow("tblOpeningBalance")'/></td>					    				
			    </tr>		    				   
			</table>			
			<br>
			<br>
			<div style="background-color: #a4d7ff;border: 1px solid #ccc;display: block; height: 150px;
				    				margin: auto;overflow-x: hidden; overflow-y: scroll;width: 99%;">
				<!-- Dynamic Table Generation for tab4 (Opening Balance) -->
				<table id="tblOpeningBalance" class="transTablex" style="width: 100%">				
					<tr>
					        <td>Account Code</td>
					        <td>Account Name</td>
					        <td>Opening Balance</td>
					        <td>Dr/Cr</td>
					        <td>Current Balance</td>		
					        <td>Delete</td>				       
				   	</tr>			   						   	    				  
				</table>	
			</div>						
		</div>					
		<!--Opening Balance-->
		
		<!--Item Detail-->
		<div id="tab5" class="tab_content" style="height: auto;">
		<br> 
			<table class="masterTable" style="width: 99%">				
				<tr>
				        <th><label>Product Code</label></th>
				        <th><label>Product Name</label></th>
				        <th><label>License Amount</label></th>
				        <th><label>AMC Amount</label></th>
				        <th><label>AMC Type</label></th>
				        <th><label>Installation Date</label></th>
				        <th><label>Warranty In Day's</label></th>		
				        <th></th>		        
			   	</tr>			   			
			   	<tr>
			    	<td style="padding-left: 0px"><s:input  id="txtProductCode"  path=""  cssClass="searchTextBox"  /></td>	
			    	<td><s:input  id="txtProductName"  path=""  cssClass="longTextBox" style="width: 200px;" /></td>	
			    	<td><s:input  id="txtLicenseAmount"  path=""  cssClass="longTextBox" style="width: 100px;" /></td>
			    	<td><s:input  id="txtAMCAmount"  path=""  cssClass="longTextBox" style="width: 100px;" /></td>	
				    <td><s:select id="cmbAMCType"  path="" items="${listDrCr}" cssClass="BoxW124px" style="width: 100px;" /></td>					    
				    <td><s:input  id="txtInstallationDate"  path=""  cssClass="calenderTextBox" /></td>
				    <td><s:input  id="txtWarrInDays"  path=""  cssClass="longTextBox" style="width: 100px;" /></td>
				    <td><input type="button" value="Add"  class="smallButton" onclick='funAddRow("tblItemDetails")'/></td>					    				
			    </tr>		    				   
			</table>		
			<br>
			<br>
				<div style="background-color: #a4d7ff;border: 1px solid #ccc;display: block; height: 150px;
				    				margin: auto;overflow-x: hidden; overflow-y: scroll;width: 99%;">	
				<!-- Dynamic Table Generation for tab5 (Item Details) -->
					<table id="tblItemDetails" class="transTablex" style="width: 100%">				
						<tr>
						        <th><label>Product Code</label></th>
						        <th><label>Product Name</label></th>
						        <th><label>License Amount</label></th>
						        <th><label>AMC Amount</label></th>
						        <th><label>AMC Type</label></th>
						        <th><label>Installation Date</label></th>
						        <th><label>Warranty In Day's</label></th>	
						        <th><label>Delete</label></th>					       
					   	</tr>			   						   	    				  
					</table>	
				</div>					
		</div>					
		<!--Item Detail-->
	    				
		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button"  onclick="return funValidateForm();" />
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()";" />

		</p>

	</s:form>
</body>
</html>
