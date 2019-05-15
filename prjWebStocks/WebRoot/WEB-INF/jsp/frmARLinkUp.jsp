<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<script type="text/javascript">
	var fieldName;
	var rowNo;
	var acBrandrow;
	var selectedRowIndex;

	$(function() 
	{
		funSetSubGroupLinkUpData('SubGroup');
		funTaxLinkUpData('Tax');
		funSupplierLinkUpData('Supplier');
		funDiscountLinkUpData('Discount');
		funRoundOffLinkUpData('RoundOff');
		funExtraChargesLinkUpData('ExtraCharge');
		funOtherChargesLinkUpData('OtherCharge');
		funSettlementLinkUpData('Settlement');
		funLocationLinkUpData('Location');
		
		$(".tab_content").hide();
		$(".tab_content:first").show();

		$("ul.tabs li").click(function() {
			$("ul.tabs li").removeClass("active");
			$(this).addClass("active");
			$(".tab_content").hide();
			var activeTab = $(this).attr("data-state");
			$("#" + activeTab).fadeIn();
		});
	});
	
	function funSetData(code){

		switch(fieldName){
				
			case 'WSItemCode' : 
				funSetWSItemCode(code);
				break;
		        
			case 'BrandMasterWeb-Service':
		    	funSetBrand(acBrandrow,code);
		        break; 
		        
			case 'SupplierAccCodeWeb-Service':
		    	funSetExciseSupplier(acBrandrow,code);
		        break;      
		        
			case 'SundryCreditorWeb-Service':
		    	funSetSundryCreditor(acBrandrow,code);
		        break;    
		        
			case 'SubGroup':
				funSetSubGroupAccountFields(acBrandrow,code);
		        break;
		        
			case 'SundryDebtorWeb-Service':
		    	funSetSundryDetor(acBrandrow,code);
		        break;  
		        
			case 'TaxWeb-Service':
		    	funSetTaxAccount(acBrandrow,code);
		        break; 
		        
			case 'DiscountWeb-Service':
				funSetDiscountAccount(acBrandrow,code);
				break; 
		        
			case 'RoundOffWeb-Service':
				funSetRoundOffAccount(acBrandrow,code);
		        break; 
		         
			case 'ExtraChargeWeb-Service':
				funSetExtraChargesAccount(acBrandrow,code);
		        break;
		        
			case 'OtherChargeWeb-Service':
				funSetOtherChargesAccount(acBrandrow,code);
		        break;
		        
			case 'SettlementWeb-Service' :
				funSetSettlementAccount(acBrandrow,code);
				break;
				
			case 'Excess' : //Excess Shortage
				funSetLocationAccount(acBrandrow,code);
				break;
				
			case 'Shortage' : // 
				funSetLocationAccount(acBrandrow,code);
				break;
		}
	}
	
	function funSetBrand(acBrandrow,code)
	{	
	 
	 $.ajax({
			type : "GET",
			url : getContextPath()+ "/loadBrandDataFormWebService.html?strBrandCode=" + code,
			dataType : "json",
			success : function(response){ 

				document.getElementById("txtBrandCode."+acBrandrow).value=response.strBrandCode;						
    			document.getElementById("txtBrandName."+acBrandrow).value=response.strBrandName; 
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
	 
	 function funSetExciseSupplier(acBrandrow,code)
		{	
		 
		 $.ajax({
				type : "GET",
				url : getContextPath()+ "/loadLinkupDataFormWebService.html?strAccountCode=" + code,
				dataType : "json",
				success : function(response){ 

					document.getElementById("txtSuppAcCode."+acBrandrow).value=response.strDebtorCode;						
	    			document.getElementById("txtSuppAcName."+acBrandrow).value=response.strFirstName; 
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
	 
	function funSetSundryCreditor(acBrandrow,code)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadSundryCreditorOrDebtorLinkupDataFormWebService.html?strDocCode=" + code,
			dataType : "json",
			success : function(response){ 
				document.getElementById("txtCreditorCode."+acBrandrow).value=response.strDebtorCode;
				document.getElementById("txtCreditorName."+acBrandrow).value=response.strFirstName;
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
	 
	
	function funSetTaxAccount(acBrandrow,code)
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
	 
	
	
	function funSetDiscountAccount(acBrandrow,code)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadTaxLinkupDataFormWebService.html?strDocCode=" + code,
			dataType : "json",
			success : function(response){
				document.getElementById("txtDiscountAmt."+acBrandrow).value=response.strAccountCode;
				document.getElementById("txtDiscountName."+acBrandrow).value=response.strAccountName;
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

	 
	function funSetRoundOffAccount(acBrandrow,code)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadTaxLinkupDataFormWebService.html?strDocCode=" + code,
			dataType : "json",
			success : function(response){ 
				document.getElementById("txtRoundOff."+acBrandrow).value=response.strAccountCode;
				document.getElementById("txtRoundOffName."+acBrandrow).value=response.strAccountName;
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
	 
	
	function funSetExtraChargesAccount(acBrandrow,code)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadTaxLinkupDataFormWebService.html?strDocCode=" + code,
			dataType : "json",
			success : function(response){
				document.getElementById("txtExtraCharges."+acBrandrow).value=response.strAccountCode;
				document.getElementById("txtExtraChargesName."+acBrandrow).value=response.strAccountName;
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
	 
	function funSetSundryDetor(acBrandrow,code)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadSundryDebtorLinkupDataFormWebService.html?strDocCode=" + code,
			dataType : "json",
			success : function(response){
				document.getElementById("txtCustAcCode."+acBrandrow).value=response.strDebtorCode;
				document.getElementById("txtCustAcName."+acBrandrow).value=response.strFirstName;
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
	 
	function funSetSubGroupAccountFields(acBrandrow,code)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadLinkupDataFormWebService.html?strAccountCode=" + code,
			dataType : "json",
			success : function(response){
				document.getElementById("txtSGAcCode."+acBrandrow).value=response.strDebtorCode;
				document.getElementById("txtSGAcName."+acBrandrow).value=response.strFirstName;
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


	function funSetWSItemCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadSundryDataFormWebService.html?strAccountCode=" + code,
			dataType : "json",
			success : function(response){ 

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
	
	function funSetOtherChargesAccount(acBrandrow,code)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadTaxLinkupDataFormWebService.html?strDocCode=" + code,
			dataType : "json",
			success : function(response){
				document.getElementById("txtOtherCharges."+acBrandrow).value=response.strAccountCode;
				document.getElementById("txtOtherChargesName."+acBrandrow).value=response.strAccountName;
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
	
	function funHelp(transactionName)
	{
		fieldName=transactionName;
		window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
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
	

	
	function funAddRowSubgroupLinkUpData(rowData)  
	{
		$('#hidLinkup').val("");
		$('#hidLinkup').val("subGroupLinkup");
		var table = document.getElementById("tblSubGroup");
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);
	    var strSubgroupCode = rowData.strMasterCode;
    	var strSubgroupName = rowData.strMasterName;
    	var strAcCode = rowData.strAccountCode;
    	var strAcName = rowData.strMasterDesc;
		
	    row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listSubGroupLinkUp["+(rowCount)+"].strMasterCode\"   id=\"txtSubgroupCode."+(rowCount)+"\" value='"+strSubgroupCode+"' />";
	    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listSubGroupLinkUp["+(rowCount)+"].strMasterName\"   id=\"txtSubgroupName."+(rowCount)+"\" value='"+strSubgroupName+"' />";
	    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"searchTextBox\" name=\"listSubGroupLinkUp["+(rowCount)+"].strAccountCode\"    id=\"txtSGAcCode."+(rowCount)+"\" value='"+strAcCode+"' ondblclick=\"funHelp1("+(rowCount)+",'SubGroup')\"/>";
	    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listSubGroupLinkUp["+(rowCount)+"].strMasterDesc\"   id=\"txtSGAcName."+(rowCount)+"\" value='"+strAcName+"' />";
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
		
	    row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listTaxLinkUp["+(rowCount)+"].strMasterCode\"  id=\"txtTaxCode."+(rowCount)+"\" value='"+strTaxCode+"' />";
	    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listTaxLinkUp["+(rowCount)+"].strMasterName\"  id=\"txtTaxDesc."+(rowCount)+"\" value='"+strDesc+"' />";
	    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"searchTextBox\"  name=\"listTaxLinkUp["+(rowCount)+"].strAccountCode\"   id=\"txtTaxAcCode."+(rowCount)+"\" value='"+strAcCode+"' ondblclick=\"funHelp1("+(rowCount)+",'TaxWeb-Service')\" />";
	    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listTaxLinkUp["+(rowCount)+"].strMasterDesc\"   id=\"txtTaxAcName."+(rowCount)+"\" value='"+strAcName+"' />";
	}
	
	function funAddRowSupplierLinkUpData(rowData)
	{
		$('#hidLinkup').val("");
		$('#hidLinkup').val("suppLinkup");
		var table = document.getElementById("tblSupplier");
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);
	    var strSuppCode = rowData.strMasterCode;
    	var strSuppName = rowData.strMasterName;
    	var strAcCode = rowData.strAccountCode;
    	var strAcName = rowData.strMasterDesc;
    	var strWebBookAccCode = rowData.strWebBookAccCode;
    	if(strWebBookAccCode==null)
    	{
    		strWebBookAccCode="";
    	}
    	var strWebBookAccName = rowData.strWebBookAccName;
    	if(strWebBookAccName==null)
    	{
    		strWebBookAccName="";
    	}
	    row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listSupplierLinkUp["+(rowCount)+"].strMasterCode\"   id=\"txtSuppcode."+(rowCount)+"\" value='"+strSuppCode+"' />";
	    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listSupplierLinkUp["+(rowCount)+"].strMasterName\"   id=\"txtSuppName."+(rowCount)+"\" value='"+strSuppName+"' />";
	    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"searchTextBox\"   name=\"listSupplierLinkUp["+(rowCount)+"].strWebBookAccCode\" id=\"txtSuppAcCode."+(rowCount)+"\" value='"+strWebBookAccCode+"' ondblclick=\"funHelp1("+(rowCount)+",'SupplierAccCodeWeb-Service')\" />";
	    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listSupplierLinkUp["+(rowCount)+"].strWebBookAccName\"  id=\"txtSuppAcName."+(rowCount)+"\" value='"+strWebBookAccName+"' />";
	    row.insertCell(4).innerHTML= "<input readonly=\"readonly\" class=\"searchTextBox\"  name=\"listSupplierLinkUp["+(rowCount)+"].strAccountCode\"    id=\"txtCreditorCode."+(rowCount)+"\" value='"+strAcCode+"' ondblclick=\"funHelp1("+(rowCount)+",'SundryCreditorWeb-Service')\"/>";
	    row.insertCell(5).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listSupplierLinkUp["+(rowCount)+"].strMasterDesc\"   id=\"txtCreditorName."+(rowCount)+"\" value='"+strAcName+"' />";

	}
		
	
	function funAddRowDiscountLinkUpData(rowData)
	{	
		$('#hidLinkup').val("");
		$('#hidLinkup').val("discountLinkup");
		var table = document.getElementById("tblDiscount");
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);
	    var strProdCode = rowData.strMasterCode;
    	var strProdName = rowData.strMasterName;
    	var strAcCode = rowData.strAccountCode;
    	var strAcName = rowData.strMasterDesc;
    	
	    row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listDiscountLinkUp["+(rowCount)+"].strMasterCode\"  id=\"txtProdcode."+(rowCount)+"\" value='"+strProdCode+"'  />";
	    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listDiscountLinkUp["+(rowCount)+"].strMasterName\"    id=\"txtProdName."+(rowCount)+"\" value='"+strProdName+"' />";
	    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"searchTextBox\" name=\"listDiscountLinkUp["+(rowCount)+"].strAccountCode\"    id=\"txtDiscountAmt."+(rowCount)+"\" value='"+strAcCode+"' ondblclick=\" funHelp1("+(rowCount)+",'DiscountWeb-Service') \" />";
	    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listDiscountLinkUp["+(rowCount)+"].strMasterDesc\"  id=\"txtDiscountName."+(rowCount)+"\" value='"+strAcName+"' />";
	}
	
	function funAddRowRoundOffLinkUpData(rowData)
	{	
		$('#hidLinkup').val("");
		$('#hidLinkup').val("roundOffLinkup");
		var table = document.getElementById("tblRoundOff");
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);
	    var strProdCode = rowData.strMasterCode;
    	var strProdName = rowData.strMasterName;
    	var strAcCode = rowData.strAccountCode;
    	var strAcName = rowData.strMasterDesc;
	      
	    row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listRoundOffLinkUp["+(rowCount)+"].strMasterCode\"  id=\"txtProdcode."+(rowCount)+"\" value='"+strProdCode+"'  />";
	    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listRoundOffLinkUp["+(rowCount)+"].strMasterName\"  id=\"txtProdName."+(rowCount)+"\" value='"+strProdName+"' />";
	    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"searchTextBox\" name=\"listRoundOffLinkUp["+(rowCount)+"].strAccountCode\"    id=\"txtRoundOff."+(rowCount)+"\" value='"+strAcCode+"' ondblclick=\" funHelp1("+(rowCount)+",'RoundOffWeb-Service') \" />";
	    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listRoundOffLinkUp["+(rowCount)+"].strMasterDesc\"   id=\"txtRoundOffName."+(rowCount)+"\" value='"+strAcName+"' />";
	}
	
	
	
	function funAddRowExtraChargesLinkUpData(rowData)
	{	
		$('#hidLinkup').val("");
		$('#hidLinkup').val("extraChargesLinkup");
		var table = document.getElementById("tblExtraCharges");
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);
	    var strProdCode = rowData.strMasterCode;
    	var strProdName = rowData.strMasterName;
    	var strAcCode = rowData.strAccountCode;
    	var strAcName = rowData.strMasterDesc;
	    row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listExtraCharLinkUp["+(rowCount)+"].strMasterCode\"    id=\"txtProdcode."+(rowCount)+"\" value='"+strProdCode+"'  />";
	    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\"   name=\"listExtraCharLinkUp["+(rowCount)+"].strMasterName\"  id=\"txtProdName."+(rowCount)+"\" value='"+strProdName+"' />";
	    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"searchTextBox\" name=\"listExtraCharLinkUp["+(rowCount)+"].strAccountCode\"   id=\"txtExtraCharges."+(rowCount)+"\" value='"+strAcCode+"' ondblclick=\" funHelp1("+(rowCount)+",'ExtraChargeWeb-Service') \" />";
	    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listExtraCharLinkUp["+(rowCount)+"].strMasterDesc\"    id=\"txtExtraChargesName."+(rowCount)+"\" value='"+strAcName+"' />";
	}
	

	function funAddRowOtherChargesLinkUpData(rowData)
	{	
		$('#hidLinkup').val("");
		$('#hidLinkup').val("OtherChargesLinkup");
		var table = document.getElementById("tblOtherCharges");
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);
	    var strProdCode = rowData.strMasterCode;
    	var strProdName = rowData.strMasterName;
    	var strAcCode = rowData.strAccountCode;
    	var strAcName = rowData.strMasterDesc;
	    row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listOtherCharLinkUp["+(rowCount)+"].strMasterCode\"    id=\"txtProdcode."+(rowCount)+"\" value='"+strProdCode+"'  />";
	    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\"   name=\"listOtherCharLinkUp["+(rowCount)+"].strMasterName\"  id=\"txtProdName."+(rowCount)+"\" value='"+strProdName+"' />";
	    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"searchTextBox\" name=\"listOtherCharLinkUp["+(rowCount)+"].strAccountCode\"   id=\"txtOtherCharges."+(rowCount)+"\" value='"+strAcCode+"' ondblclick=\" funHelp1("+(rowCount)+",'OtherChargeWeb-Service') \" />";
	    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listOtherCharLinkUp["+(rowCount)+"].strMasterDesc\"    id=\"txtOtherChargesName."+(rowCount)+"\" value='"+strAcName+"' />";
	}
	
	function funSupplierLinkUpData(code)
	{
		var searchUrl="";
		var property=$('#cmbProperty').val();
		searchUrl=getContextPath()+"/loadARLinkUpData.html?strDoc="+code;
		$.ajax({
	        type: "POST",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funDeleteTableAllRowsOfParticulorTable(code);
		    	$.each(response, function(i,item)
				{
					funAddRowSupplierLinkUpData(item);
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
	
	function funSetSubGroupLinkUpData(code)
	{
		var searchUrl="";
		var property=$('#cmbProperty').val();
		searchUrl=getContextPath()+"/loadARLinkUpData.html?strDoc="+code;
		$.ajax({
	        type: "POST",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funDeleteTableAllRowsOfParticulorTable(code);
		    	$.each(response, function(i,item)
				{
					funAddRowSubgroupLinkUpData(item);
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
	
	function funSaleSubGroupLinkUpData(code)
	{
		var searchUrl="";
		var property=$('#cmbProperty').val();
		searchUrl=getContextPath()+"/loadARLinkUpData.html?strDoc="+code;
		$.ajax({
	        type: "POST",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funDeleteTableAllRowsOfParticulorTable(code);
		    	$.each(response, function(i,item)
				{
		    		funAddRowSaleSubgroupLinkUpData(item);
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
		var property=$('#cmbProperty').val();
		searchUrl=getContextPath()+"/loadARLinkUpData.html?strDoc="+code;
		$.ajax
		({
	        type: "POST",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funDeleteTableAllRowsOfParticulorTable(code);
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
	
	
	
	
	function funDiscountLinkUpData(code)
	{
		var searchUrl="";
		var property=$('#cmbProperty').val();
		searchUrl=getContextPath()+"/loadARLinkUpData.html?strDoc="+code;
		$.ajax
		({
	        type: "POST",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funDeleteTableAllRowsOfParticulorTable(code);
		    	$.each(response, function(i,item)
						{
// 				    		var arr = jQuery.makeArray( response[i] );
				    		funAddRowDiscountLinkUpData(item);
				    		
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
	
	   
	function funRoundOffLinkUpData(code)
	{
		var searchUrl="";
		var property=$('#cmbProperty').val();
		searchUrl=getContextPath()+"/loadARLinkUpData.html?strDoc="+code;
		$.ajax({
	        type: "POST",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funDeleteTableAllRowsOfParticulorTable(code);
		    	$.each(response, function(i,item)
				{
		    		funAddRowRoundOffLinkUpData(item);
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
	
	
	function funExtraChargesLinkUpData(code)
	{
		var searchUrl="";
		var property=$('#cmbProperty').val();
		searchUrl=getContextPath()+"/loadARLinkUpData.html?strDoc="+code;
		$.ajax({
	        type: "POST",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funDeleteTableAllRowsOfParticulorTable(code);
		    	$.each(response, function(i,item)
				{
					funAddRowExtraChargesLinkUpData(item);
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

	function funOtherChargesLinkUpData(code)
	{
		var searchUrl="";
		var property=$('#cmbProperty').val();
		searchUrl=getContextPath()+"/loadARLinkUpData.html?strDoc="+code;
		$.ajax({
	        type: "POST",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funDeleteTableAllRowsOfParticulorTable(code);
		    	$.each(response, function(i,item)
				{
		    		funAddRowOtherChargesLinkUpData(item);
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
		 var property=$('#cmbProperty').val();
		 searchUrl=getContextPath()+"/loadCRMWebBooksLinkUpData.html?strDoc="+code;
		$.ajax({
	        type: "POST",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	funDeleteTableAllRowsOfParticulorTable(code);
		    	$.each(response, function(i,item)
				{
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
	
	function funAddRowSettlementLinkUpData(rowData)
	{	
		$('#hidLinkup').val("");
		$('#hidLinkup').val("settlementLinkup");
		var table = document.getElementById("tblSettlement");
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);
	    var strProdCode = rowData.strMasterCode;
    	var strProdName = rowData.strMasterName;
    	var strAcCode = rowData.strAccountCode;
    	var strAcName = rowData.strMasterDesc;
	    row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listSettlementLinkUp["+(rowCount)+"].strMasterCode\"    id=\"txtProdcode."+(rowCount)+"\" value='"+strProdCode+"'  />";
	    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\"   name=\"listSettlementLinkUp["+(rowCount)+"].strMasterName\"  id=\"txtProdName."+(rowCount)+"\" value='"+strProdName+"' />";
	    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"searchTextBox\" name=\"listSettlementLinkUp["+(rowCount)+"].strAccountCode\"   id=\"txtSettlement."+(rowCount)+"\" value='"+strAcCode+"' ondblclick=\" funHelp1("+(rowCount)+",'SettlementWeb-Service') \" />";
	    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listSettlementLinkUp["+(rowCount)+"].strMasterDesc\"    id=\"txtSettlementName."+(rowCount)+"\" value='"+strAcName+"' />";
	}
	
	function funLocationLinkUpData(code)
	{
		var searchUrl="";
		var property=$('#cmbProperty').val();
		searchUrl=getContextPath()+"/loadARLinkUpData.html?strDoc="+code;
		$.ajax({
	        type: "POST",
	        url: searchUrl,
		    dataType: "json",
		    success: function(response)
		    {
		    	//funDeleteTableAllRowsOfParticulorTable(code);
		    	$.each(response, function(i,item)
				{
		    		funAddRowLocationLinkUpData(item);
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
	
	function funAddRowLocationLinkUpData(rowData)
	{	
		$('#hidLinkup').val("");
		$('#hidLinkup').val("locationLinkup");
		var table = document.getElementById("tblLocation");
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);
	    var strLocCode = rowData.strMasterCode;
    	var strLocName = rowData.strMasterName;
    	var strAcCode = rowData.strAccountCode;
    	var strAcName = rowData.strMasterDesc;
    	var strWebAcCode = rowData.strWebBookAccCode;
    	var strWebAcName = rowData.strWebBookAccName;
	    row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box\" name=\"listLocationLinkUp["+(rowCount)+"].strMasterCode\"    id=\"txtProdcode."+(rowCount)+"\" value='"+strLocCode+"'  />";
	    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\"   name=\"listLocationLinkUp["+(rowCount)+"].strMasterName\"  id=\"txtProdName."+(rowCount)+"\" value='"+strLocName+"' />";
	    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"searchTextBox\"  name=\"listLocationLinkUp["+(rowCount)+"].strWebBookAccCode\" id=\"txtLocAcCode."+(rowCount)+"\" value='"+strWebAcCode+"' ondblclick=\" funHelp1("+(rowCount)+",'Excess') \" />";
	    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listLocationLinkUp["+(rowCount)+"].strWebBookAccName\"    id=\"txtLocAcName."+(rowCount)+"\" value='"+strWebAcName+"' />";
	    row.insertCell(4).innerHTML= "<input readonly=\"readonly\" class=\"searchTextBox\" name=\"listLocationLinkUp["+(rowCount)+"].strAccountCode\"   id=\"txtSecLocAcCode."+(rowCount)+"\" value='"+strAcCode+"'  ondblclick=\" funHelp1("+(rowCount)+",'Shortage') \" />";
	    row.insertCell(5).innerHTML= "<input readonly=\"readonly\" class=\"Box\"  name=\"listLocationLinkUp["+(rowCount)+"].strMasterDesc\"    id=\"txtSecLocAcName."+(rowCount)+"\" value='"+strAcName+"' />";
	    /* SundryCreditorWeb-Service */
	}
	
	function funSetLocationAccount(acBrandrow,code)
	{
	
	 $.ajax({
			type : "GET",
			url : getContextPath()+ "/loadLinkupDataFormWebService.html?strAccountCode=" + code,
			dataType : "json",
			success : function(response){ 
				if(fieldName=="Excess")  //Excess Shortage
				{
					document.getElementById("txtLocAcCode."+acBrandrow).value=response.strDebtorCode;						
	    			document.getElementById("txtLocAcName."+acBrandrow).value=response.strFirstName; 
					
				}else{
					document.getElementById("txtSecLocAcCode."+acBrandrow).value=response.strDebtorCode;						
	    			document.getElementById("txtSecLocAcName."+acBrandrow).value=response.strFirstName; 
					
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
	
	function funSetSettlementAccount(acBrandrow,code)
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
	
	function funDeleteTableAllRowsOfParticulorTable(tableName)
	{
		switch(tableName)
		{
			case "SubGroup" :
			{
				$("#tbl"+tableName+ " tr").remove();
				break;
			}
			case "Tax" :
			{
				$("#tbl"+tableName+ " tr").remove();
				break;
			}
			
			case "Supplier" :
			{
				$("#tbl"+tableName+ " tr").remove();
				break;
			}
			case "Product" :
			{
				$("#tbl"+tableName+ " tr").remove();
				break;
			}
						
			case "Discount" :
			{
				$("#tbl"+tableName+ " tr").remove();
				break;
			}
			
			case "RoundOff" :
			{
				$("#tbl"+tableName+ " tr").remove();
				break;
			}
			
			case "ExtraCharge" :
			{
				$("#tbl"+tableName+ " tr").remove();
				break;
			}
			case "saleSubGroup" :
			{
				$("#tblSaleSubGroup  tr").remove();
				break;
			}
			case "Location" :
			{
				$("#tbl"+tableName+ " tr").remove();
				break;
			}
		}
	}	
	
</script>

</head>
<body>

	<div id="formHeading">
	<label>WebBooks Link Up</label>
	</div>

<br/>
<br/>

	<s:form name="ARLinkUp" method="POST" action="saveARLinkUp.html">

		<table style="border: 0px solid black; width: 100%; height: 100%; margin-left: auto; margin-right: auto; background-color: #C0E4FF;">
			
			<tr>
				<td><s:select path="strProperty" id="cmbProperty" items="${listProperty}" onchange="funOnChange();" cssClass="BoxW124px"> 						
					</s:select>
				</td>
			</tr>
			<tr style="height: 10px;"></tr>
			
			<tr>
				<td>
					<div id="tab_container" class="masterTable"  style="height: 535px;">
						<ul class="tabs">
							<li class="active" data-state="divSubGroup" style="width: 10%;padding-left: 55px;">Sub Group</li>
							<li data-state="divTax" style="width: 10%; padding-left: 55px">Tax</li>
							<li data-state="divSupplier" style="width: 10%; padding-left: 55px">Supplier</li>
							<li data-state="divDiscount" style="width: 10%; padding-left: 55px">Discount</li>
							<li data-state="divRoundOff" style="width: 10%; padding-left: 55px">Round OFF</li>
							<li data-state="divExtraCharge" style="width: 10%; padding-left: 55px">Extra Charges</li>
							<li data-state="divOtherCharge" style="width: 10%; padding-left: 55px">Other Charges</li>
							<li data-state="divSettlement" style="width: 10%; padding-left: 55px">Settlement</li>
							<li data-state="divLocation" style="width: 10%; padding-left: 55px">Location</li>
						</ul>
						
					&nbsp;&nbsp;
		
						<div id="divSubGroup" class="tab_content" style="height: 500px;margin-top: 20px;">
							<table
								style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">
									<td style="width:10%;">Subgroup Code</td>
									<td style="width:20%;">Subgroup Name</td>
									<td style="width:20%;">Account Code</td>
									<td style="width:20%;">Account Name</td>
								</tr>
							</table>
							
							<div style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
									<table id="tblSubGroup"
									style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
									class="transTablex col8-center">
									<tbody>
									<col style="width:10%">
									<col style="width:20%">					
									<col style="width:20%">
									<col style="width:20%">
									</tbody>
								</table>
							</div>
						</div>
						
						<div id="divTax" class="tab_content" style="height: 500px;margin-top: 20px;">
							<table style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">
									<td style="width:10%;">Tax Code</td>
									<td style="width:20%;">Tax Desc</td>
									<td style="width:20%;">Account Code</td>
									<td style="width:20%;">Account Name</td>
								</tr>
							</table>
						
							<div style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
								<table id="tblTax" style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll" class="transTablex col8-center">
								<tbody>
									<col style="width:10%">
									<col style="width:22%">					
									<col style="width:20%">
									<col style="width:20%">
								</tbody>
								</table>
							</div>
						</div>
				
						<div id="divSupplier" class="tab_content" style="height: 500px;margin-top: 20px;">
							<table
								style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">
								<td style="width:16%;">Supplier Code</td>
								<td style="width:20%;">Supplier Name</td>
								<td style="width:15%;">Account Code</td>
								<td style="width:15%;">Account Name</td>
								<td style="width:15%;">Creditor Code</td>
								<td style="width:20%;">Creditor Name</td>
							</tr>
						</table>
							<div
								style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
									<table id="tblSupplier"
									style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
									class="transTablex col8-center">
									<tbody>
									<col style="width:16%">
									<col style="width:20%">					
									<col style="width:15%">
									<col style="width:15%">
									<col style="width:15%">
									<col style="width:20%">	
									</tbody>
								</table>
							</div>
						</div>
										
						<div id="divDiscount" class="tab_content" style="height: 500px;margin-top: 20px;">
							<table
								style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">
									<td style="width:10%;">Discount Code</td>
									<td style="width:15%;">Discount Name</td>
									<td style="width:10%;">Account Code</td>
									<td style="width:15%;">Account Name</td>
								</tr>
							</table>
							
							<div
								style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
									<table id="tblDiscount"
									style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
									class="transTablex col8-center">
									<tbody>
									<col style="width:10%">
									<col style="width:16%">					
									<col style="width:10%">
									<col style="width:15%">
									</tbody>
								</table>
							</div>
						</div>
						
						<div id="divRoundOff" class="tab_content" style="height: 500px;margin-top: 20px;">
							<table style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">
									<td style="width:10%;">RoundOff</td>
									<td style="width:15%;">RoundOff Name</td>
									<td style="width:10%;">Account Code</td>
									<td style="width:15%;">Account Name</td>
								</tr>
							</table>
							<div style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
									<table id="tblRoundOff"
									style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
									class="transTablex col8-center">
									<tbody>
									<col style="width:10%" >
									<col style="width:16%">					
									<col style="width:10%">
									<col style="width:15%">
									</tbody>
								</table>
							</div>
						</div>
						
						<div id="divExtraCharge" class="tab_content" style="height: 500px;margin-top: 20px;">
							<table
								style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">
									<td style="width:10%;">Extra Charges</td>
									<td style="width:15%;">ExtraCharges Name</td>
									<td style="width:10%;">Account Code</td>
									<td style="width:15%;">Account Name</td>
								</tr>
							</table>
							<div
								style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
									<table id="tblExtraCharges"
									style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
									class="transTablex col8-center">
									<tbody>
									<col style="width:10%"/>
									<col style="width:16%">					
									<col style="width:10%">
									<col style="width:15%">
									</tbody>
								</table>
							</div>
						</div>
						
						<div id="divOtherCharge" class="tab_content" style="height: 500px;margin-top: 20px;">
							<table
								style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">
									<td style="width:10%;">Other Charges</td>
									<td style="width:15%;">OtherCharges Name</td>
									<td style="width:10%;">Account Code</td>
									<td style="width:15%;">Account Name</td>
								</tr>
							</table>
							<div
								style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
									<table id="tblOtherCharges"
									style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
									class="transTablex col8-center">
									<tbody>
									<col style="width:10%"/>
									<col style="width:16%">					
									<col style="width:10%">
									<col style="width:15%">
									</tbody>
								</table>
							</div>
						</div>
						
						<div id="divSettlement" class="tab_content" style="height: 550px">
							<table
								style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">
									<td style="width:10%;">Settlement Code</td>
									<td style="width:15%;">Settlement Name</td>
									<td style="width:10%;">Account Code</td>
									<td style="width:15%;">Account Name</td>
								</tr>
							</table>
							<div
								style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
									<table id="tblSettlement"
									style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
									class="transTablex col8-center">
									<tbody>
									<col style="width:10%"/>
									<col style="width:16%">					
									<col style="width:10%">
									<col style="width:15%">
									</tbody>
								</table>
							</div>
						</div>
						
						<div id="divLocation" class="tab_content" style="height: 550px">
							<table id="tblHeadLocation"
								style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">
									<td style="width:10%;">Location Code</td>
									<td style="width:15%;">Location Name</td>
									<td style="width:10%;">Excess Code</td>
									<td style="width:15%;">Excess Inventory</td>
									<td style="width:10%;">Shortage Code</td>
									<td style="width:15%;">Shortage Inventory</td>
								</tr>
							</table>
							<div
								style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
									<table id="tblLocation"
									style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
									class="transTablex col8-center">
									<tbody>
									<col style="width:10%"/>
									<col style="width:15.5%">
									<col style="width:10%"/>					
									<col style="width:15%">
									<col style="width:10%"/>
									<col style="width:15%">
									</tbody>
								</table>
							</div>
						</div>
						
					</div>
				</td>
			</tr>
		</table>

		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button" />
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>
		
	
	
		<br />
		<br />
		<br />
		<br />
	</s:form>
</body>
</html>
