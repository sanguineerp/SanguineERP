<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<script type="text/javascript">
		var fieldName,gurl,listRow=0,mastercode;
	 $(document).ready(function()
					{
		 
		 $("#txtChkDte").datepicker({ dateFormat: 'yy-mm-dd' });
			$("#txtChkDte" ).datepicker('setDate', 'today');
			$("#txtChkDte").datepicker();
			
		 	$("#txtMemCode").val('');
			$("#txtChequeNo").val('');
			$("#txtAmt").val('');
			$("#txtDrawnOn").val('');
			
			$("#txtMemCodee").val('');
			$("#txtChequeNoo").val('');
			$("#txtChkDtee").val('');
			$("#txtAmtt").val('');
			$("#txtDrawnOnn").val('');

			$("#txtChkDte").datepicker({ dateFormat: 'yy-mm-dd' });
			$("#txtChkDte").datepicker('setDate', 'today');
			$("#txtChkDtee").datepicker({ dateFormat: 'yy-mm-dd' });
			$("#txtChkDtee").datepicker('setDate', 'today');
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
				alert("Data Save successfully\n");
			<%
			}}%>
		});
	
	 
	 function btnAdd_onclickRecieved() 
		{			
			if(($("#txtMemCode").val().trim().length == 0))
			{
					 alert("Please Enter Member Code Or Search");
		             $("#txtMemCode").focus() ; 
		             return false;
			}
			else if(($("#txtDrawnOn").val().trim().length == 0))
			{
				 alert("Please Enter Drawn On");
	             $("#txtDrawnOn").focus() ; 
	             return false;
			}
			else if(($("#txtChequeNo").val().trim().length == 0))
			{
				 alert("Please Enter Cheque No");
	             $("#txtChequeNo").focus() ; 
	             return false;
			}
			else if(($("#txtAmt").val().trim().length == 0))
			{
				 alert("Please Enter Amount");
	             $("#txtAmt").focus() ; 
	             return false;
			}
			
			else if(($("#txtChkDte").val().trim().length == 0))
			{
				 alert("Please Cheque Date");
	             $("#txtChkDte").focus() ; 
	             return false;
			}
			else
		    {
				 /* if(funDuplicateProduct(txtMemCode,txtBankCode,txtChequeNo,txtChkDte,txtAmt,txtDrawnOn))
	            	{ */ 
						var memCode = $("#txtMemCode").val();
					    var chequeNo = $("#txtChequeNo").val();
					    var chequeDate = $("#txtChkDte").val();
					    var chequeAmt = $("#txtAmt").val();
					    var drawnOn = $("#txtDrawnOn").val();
					    funAddRowReceived(memCode,drawnOn,chequeNo,chequeDate,chequeAmt);
	            	//}
			}		 
		}	 
	  function btnAdd_onclickIssued() 
		{			
			if(($("#txtMemCodee").val().trim().length == 0))
			{
					 alert("Please Enter Member Code Or Search");
		             $("#txtMemCodee").focus() ; 
		             return false;
			}
			else if(($("#txtDrawnOnn").val().trim().length == 0))
			{
				 alert("Please Enter Drawn On");
	             $("#txtDrawnOnn").focus() ; 
	             return false;
			}
			else if(($("#txtChequeNoo").val().trim().length == 0))
			{
				 alert("Please Enter Cheque No");
	             $("#txtChequeNoo").focus() ; 
	             return false;
			}
			else if(($("#txtAmtt").val().trim().length == 0))
			{
				 alert("Please Enter Amount");
	             $("#txtAmtt").focus() ; 
	             return false;
			}
			
			else if(($("#txtChkDtee").val().trim().length == 0))
			{
				 alert("Please Cheque Date");
	             $("#txtChkDtee").focus() ; 
	             return false;
			}
			else
		    {
				var strFacilityCode=$("#txtFacilityCodee").val();
				var memCode = $("#txtMemCodee").val();
				var chequeNo = $("#txtChequeNoo").val();
				var chequeDate = $("#txtChkDtee").val();
				var chequeAmt = $("#txtAmtt").val();
				var drawnOn = $("#txtDrawnOnn").val();
				funAddRowIssued(memCode,drawnOn,chequeNo,chequeDate,chequeAmt);
	            	
			}		 
		} 
	 
		 /*
		 * Check duplicate record in grid
		 */
		function funDuplicateProduct(strFacilityCode)
		{
		    var table = document.getElementById("tblDetails");
		    var rowCount = table.rows.length;		   
		    var flag=true;
		    if(rowCount > 0)
		    	{
				    $('#tblDetails tr').each(function()
				    {
					    if(strFacilityCode==$(this).find('input').val())// `this` is TR DOM element
	    				{
					    	alert("Already added "+ strFacilityCode);
					    	 funResetProductFields();
		    				flag=false;
	    				}
					});
				    
		    	}
		    return flag;
		  
		}
		
		/**
		 * Adding Product Data in grid 
		 */
		function funAddRowReceived(memCode,drawnOn,chequeNo,chequeDate,chequeAmt) 
		{   	    	    
		    var table = document.getElementById("tblDetails");
		    var rowCount = table.rows.length;
		    var row = table.insertRow(rowCount);   
		    
		    rowCount=listRow;
		    row.insertCell(0).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlRecieved["+(rowCount)+"].strMemCode\" value='"+memCode+"' id=\"txtMemCode."+(rowCount)+"\" >";
			row.insertCell(1).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlRecieved["+(rowCount)+"].strDrawnOn\" value='"+drawnOn+"' id=\"txtBankCode."+(rowCount)+"\" >";
		    row.insertCell(2).innerHTML= "<input class=\"Box\" type=\"text\" name=\"listPDCDtlRecieved["+(rowCount)+"].strChequeNo\" size=\"15%\" style=\"text-align: right;\" id=\"txtChequeNo."+(rowCount)+"\" value='"+chequeNo+"'/>";	
		    row.insertCell(3).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlRecieved["+(rowCount)+"].dteChequeDate\" id=\"txtChkDte."+(rowCount)+"\" value="+chequeDate+">";
		    row.insertCell(4).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlRecieved["+(rowCount)+"].dblChequeAmt\" value='"+chequeAmt+"' id=\"txtAmt."+(rowCount)+"\" >";	
		    row.insertCell(5).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlRecieved["+(rowCount)+"].strType\" value='Received' id=\"txtRecieved."+(rowCount)+"\" >";	
			row.insertCell(6).innerHTML= "<input type=\"button\" class=\"deletebutton\" size=\"1%\" value = \"Delete\" onClick=\"Javacsript:funDeleteRowRecieved(this)\"/>";
			   
		    listRow++;		    
		    funResetProductFieldsRecieved();		   		    
		}
		
		function funAddRowIssued(memCode,drawnOn,chequeNo,chequeDate,chequeAmt) 
		{   	    	    
		    var table = document.getElementById("tblDetailss");
		    var rowCount = table.rows.length;
		    var row = table.insertRow(rowCount);   
		    
		    rowCount=listRow;
		    row.insertCell(0).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlIssued["+(rowCount)+"].strMemCode\" value='"+memCode+"' id=\"txtMemCode."+(rowCount)+"\" >";
			row.insertCell(1).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlIssued["+(rowCount)+"].strDrawnOn\" value='"+drawnOn+"' id=\"txtBankCode."+(rowCount)+"\" >";
		    row.insertCell(2).innerHTML= "<input class=\"Box\" type=\"text\" name=\"listPDCDtlIssued["+(rowCount)+"].strChequeNo\" size=\"15%\" style=\"text-align: right;\" id=\"txtChequeNo."+(rowCount)+"\" value='"+chequeNo+"'/>";	
		    row.insertCell(3).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlIssued["+(rowCount)+"].dteChequeDate\" id=\"txtChkDte."+(rowCount)+"\" value="+chequeDate+">";
		    row.insertCell(4).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlIssued["+(rowCount)+"].dblChequeAmt\" value='"+chequeAmt+"' id=\"txtAmt."+(rowCount)+"\" >";	
		    row.insertCell(5).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlIssued["+(rowCount)+"].strType\" value='Issued' id=\"txtRecieved."+(rowCount)+"\" >";	
		    row.insertCell(6).innerHTML= "<input type=\"button\" class=\"deletebutton\" size=\"1%\" value = \"Delete\" onClick=\"Javacsript:funDeleteRowIssued(this)\"/>";
		
		    listRow++;		    
		    funResetProductFieldsIssued();		   		    
		} 
		
		/**
		 * Delete a particular record from a grid
		 */
		function funDeleteRowRecieved(obj)
		{
		    var index = obj.parentNode.parentNode.rowIndex;
		    var table = document.getElementById("tblDetails");
		    table.deleteRow(index);
		}
		function funDeleteRowIssued(obj)
		{
		    var index = obj.parentNode.parentNode.rowIndex;
		    var table = document.getElementById("tblDetailss");
		    table.deleteRow(index);
		}
		
		/**
		 * Remove all product from grid
		 */
		/* function funRemProdRows()
	    {
			var table = document.getElementById("tblDetails");
			var rowCount = table.rows.length;
			for(var i=rowCount;i>=0;i--)
			{
				table.deleteRow(i);
			}
	    } */
		

		/**
		 * Clear textfiled after adding data in textfield
		 */
		function funResetProductFieldsRecieved()
		{
			$("#txtChequeNo").val('');
			$("#txtAmt").val('');
			$("#txtDrawnOn").val('');
		}
		function funResetProductFieldsIssued()
		{
			$("#txtChequeNoo").val('');
			$("#txtAmtt").val('');
			$("#txtDrawnOnn").val('');
		}
		
		/* function funRemoveProductRows()
		{
			var table = document.getElementById("tblDetails");
			var rowCount = table.rows.length;
			while(rowCount>0)
			{
				table.deleteRow(0);
				rowCount--;
			}
		}
		 */
		
		
	
		
	 
	 function funHelp(transactionName)
		{	       
			fieldName=transactionName;
	    //    window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
	        window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
	        
	    }
	 
	 function funResetFields()
		{
			location.reload(true); 
		}	 
	 function funValidate()
		{
		 	flag=true;
		 	var table = document.getElementById("tblDetails");
		    var rowRecieved = table.rows.length;
		    var table = document.getElementById("tblDetailss");
		    var rowIssued = table.rows.length;				    	
		    	if($('#txtMemCode').val()==''&&$('#txtMemCodee').val()=='')
		    		{
		    			flag=false;
		    			alert("Please Enter Data");
		    		}
		 	return flag;
			
		}	
	 
	 
	 function funSetData(code)
		{
		 switch(fieldName)
		 	{

			case 'WCmemProfileCustomer' :
				funSetMemberDataReceived(code);				
				break;
				
			case 'WCmemProfileCustomerIssued' :
				funSetMemberDataIssued(code);				
				break;
				
			case 'WCBankCode' :
				funSetBankCodeRecieved(code);				
				break;
							
			case 'WCBankCodee' :
				funSetBankCodeIssued(code);				
				break;
			}
		}
	 
	function funSetBankCodeRecieved(code){		 
			var searchurl=getContextPath()+"/loadWebBookBankCode.html?bankCode="+code;
			$.ajax({
				        type: "GET",
				        url: searchurl,
				        dataType: "json",
				        success: function(response)
				        {
				        	if(response.strFacilityCode=='Invalid Code')
				        	{
				        		alert("Invalid Bank Code");
				        		$("#txtDrawnOn").val('');
				        	}
				        	else
				        	{				        		
				        		$.each(response, function(cnt,item)
					 			{ 
					        				$("#txtDrawnOn").val(code);
				        					$("#lbldrawnOn").text(response[0]);				        					        					
					 			});	
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
	function funSetBankCodeIssued(code){		 
		var searchurl=getContextPath()+"/loadWebBookBankCode.html?bankCode="+code;
		$.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(response.strFacilityCode=='Invalid Code')
			        	{
			        		alert("Invalid Bank Code");
			        		$("#txtDrawnOn").val('');
			        	}
			        	else
			        	{				        		
			        		$.each(response, function(cnt,item)
				 			{ 
				        				$("#txtDrawnOnn").val(code);
			        					$("#lbldrawnOnn").text(response[0]);			        					        					
				 			});	
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
	 
	 function funSetMemberDataReceived(code){
		 
		 $("#txtFacilityCode").val(code);
			var searchurl=getContextPath()+"/loadWebClubMemberProfileData.html?primaryCode="+code;
			//alert(searchurl);
			 $.ajax({
				        type: "GET",
				        url: searchurl,
				        dataType: "json",
				        success: function(response)
				        {
				        	if(response.strFacilityCode=='Invalid Code')
				        	{
				        		alert("Invalid Category Code");
				        		$("#txtMemCode").val('');
				        	}
				        	else
				        	{
				        		$("#txtMemCode").val(code);	 
					        	$("#lblMemCode").text(response[0].strFirstName);
				        	}
					        	funSetMemberTableReceived(response[0].strMemberCode);
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
	 
	 function funSetMemberTableReceived(code)
	 {
		 var searchurl=getContextPath()+"/loadPDCMemberWiseData.html?memCode="+code;
			 $.ajax({
				        type: "GET",
				        url: searchurl,
				        dataType: "json",
				        success: function(response)
				        {
				        	if(response.strFacilityCode=='Invalid Code')
				        	{
				        		alert("Invalid Member Code");
				        		$("#txtMemCode").val('');
				        	}
				        	else
				        	{				        		
				        		var table=document.getElementById("tblDetails");
				    			var rowCount=table.rows.length;
				    			while(rowCount>0)
				    			{table.deleteRow(0);
				    			   rowCount--;
				    			}
				        		$.each(response, function(cnt,item)
					 					{
				        					$("#txtMemCode").val(item[0]);
				        					if(item[3]=="Received")
				        						{
				        						funAddRowReceived(item[0],item[1],item[2],item[5],item[4])
				        						}		
							      		});		
				        		$("#txtMemCode").val(code);	 					        						        	
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
	 
	 
	 function funSetMemberDataIssued(code){
		 
		 $("#txtFacilityCode").val(code);
			var searchurl=getContextPath()+"/loadWebClubMemberProfileData.html?primaryCode="+code;
			//alert(searchurl);
			 $.ajax({
				        type: "GET",
				        url: searchurl,
				        dataType: "json",
				        success: function(response)
				        {
				        	if(response.strFacilityCode=='Invalid Code')
				        	{
				        		alert("Invalid Category Code");
				        		$("#txtMemCodee").val('');
				        	}
				        	else
				        	{
				        		$("#txtMemCodee").val(code);	 
					        	$("#lblMemCodee").text(response[0].strFirstName);
					        	funSetMemberTableIssued(response[0].strMemberCode);
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
	 
	 function funSetMemberTableIssued(code){
		 var searchurl=getContextPath()+"/loadPDCMemberWiseData.html?memCode="+code;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(response.strFacilityCode=='Invalid Code')
			        	{
			        		alert("Invalid Member Code");
			        		$("#txtMemCodee").val('');
			        	}
			        	else
			        	{
			        		$("#txtMemCodee").val(code);				        						        			
					    	var table=document.getElementById("tblDetailss");
					    	var rowCount=table.rows.length;
					    	while(rowCount>0)
					    	{table.deleteRow(0);
					    	   rowCount--;
					    	}			   
			        		
			        		$.each(response, function(cnt,item)
				 					{
			        					
			        					if(item[3]=="Issued")
			        						{
			        						funAddRowIssued(item[0],item[1],item[2],item[5],item[4])
			        						}
							        	
						      		});		
			        		 
				        						        	
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
	 
</script>

</head>
<body>

	<div id="formHeading">
	<label>WebClubPDC</label>
	</div>

<br/>
<br/>

	<s:form name="WebClubPDC" method="POST" action="saveWebClubPDC.html">

		<table class="masterTable">
			<table style="border: 0px solid black; width: 100%; height: 70%; margin-left: auto; margin-right: auto; background-color: #C0E4FF;">
				<tr>
					<td>
						<div id="tab_container" style="height: 470px">
							<ul class="tabs">
								<li class="active" data-state="tab1">Received</li>
								<li data-state="tab2">Issued</li>
							</ul>
							
							<div id="tab1" class="tab_content" style="height: 290px"> <br><br>	
							<table class="transTable">
									<tr>					
										<td style="width: 220px;">
											<label>Member Code</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;				
											<s:input type="text" id="txtMemCode" path="strMemCodeRecieved"  class="searchTextBox" ondblclick="funHelp('WCmemProfileCustomer');" readonly="true"/>
											<td style="width: 120px;"><label id="lblMemCode"></label></td>					
										</td>									
										<td ><label>Cheque No</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<s:input  type="text" id="txtChequeNo" path="strChequeNo" class="longTextBox" style="width: 180px;"/>
										</td>														
									</tr>
									
									<tr>
									<td style="width: 320px;">
										<label>Drawn On </label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<s:input  type="text" id="txtDrawnOn" path="strDrawnOn" class="searchTextBox" ondblclick="funHelp('WCBankCode');" readonly="true" />
										<td><label id="lbldrawnOn"></label></td>
									</td>
									<td>
										<label>Cheque Date</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<!-- <input  type="text" id="txtChkDte" path="dteChequeDate" cssClass="calenderTextBox hasDatepicker"/> -->
										<s:input id="txtChkDte" path="dteChequeDate"  cssClass="calenderTextBox" /></td>
										
										</td>
									
									</tr>
									
									<tr>	
										<td style="width: 220px;">
										<label>Amount</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="text" id="txtAmt" path="dblChequeAmt" class="decimal-places numberField" />
										<td><label id="lblBankCode"></label></td>						
											<td></td>
										</td>						
									</tr>
									
									<td>
										</td>
										<td>
										</td>
									<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="button" id="btnExcecute" value="Add"  class="form_button" onclick="btnAdd_onclickRecieved()" />
									</td>					
								</tr>		
							</table>
							<div class="dynamicTableContainer" style="height: 300px;width: 99.80%;">
							<table
								style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">				
									<td style="width:6.2%;">Member Code</td>
									<td style="width:6.2%;">Drawn On</td>
									<td style="width:6.2%;">Cheque No</td>
									<td style="width:6.2%;">Cheque Date</td>
									<td style="width:6.2%;">Amount</td>
									<td style="width:6.2%;">Type</td>
								</tr>
							</table>
							
							<div style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
								<table id="tblDetails"
									style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
									class="transTablex col8-center">
									<tbody>			
										<col style="width:21.4%;">	
										<col style="width:21.5%;">
										<col style="width:21.5%;">
										<col style="width:21.7%;">
										<col style="width:21.5%;">
										<col style="width:17%;">
										<col style="width:2.4%;">
									</tbody>
								</table>
							</div>
						</div>						
					</div>
					
					<div id="tab2" class="tab_content" style="height: 290px"> <br><br>	
							<table class="transTable">
									<tr>					
										<td style="width: 220px;">
											<label>Member Code</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;				
											<s:input type="text" id="txtMemCodee" path="strMemCodeIssued"  class="searchTextBox" ondblclick="funHelp('WCmemProfileCustomerIssued');" readonly="true"/>
											<td style="width: 120px;"><label id="lblMemCodee"></label></td>					
										</td>									
										<td ><label>Cheque No</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<s:input  type="text" id="txtChequeNoo" path="strChequeNo" class="longTextBox" style="width: 180px;"/>
																						
									</tr>
									
									<tr>
									<td style="width: 320px;">
										<label>Drawn On </label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<s:input  type="text" id="txtDrawnOnn" path="strDrawnOn" class="searchTextBox" ondblclick="funHelp('WCBankCodee');" readonly="true"/>
										<td><label id="lbldrawnOnn"></label></td>
									</td>
									<td>
										<label>Cheque Date</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<s:input id="txtChkDtee" path="dteChequeDate"  cssClass="calenderTextBox" /></td>
									
										</td>
									
									</tr>
									
									<tr>	
										<td style="width: 220px;">
										<label>Amount</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="text" id="txtAmtt" path="dblChequeAmt" class="decimal-places numberField" />
										<td><label id="lblBankCode"></label></td>						
											<td></td>
										</td>						
									</tr>
									
									<td>
										</td>
										<td>
										</td>
									<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="button" id="btnExcecute" value="Add"  class="form_button" onclick="btnAdd_onclickIssued()" />
									</td>					
								</tr>		
							</table>
							<div class="dynamicTableContainer" style="height: 300px;width: 99.80%;">
							<table
								style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
								<tr bgcolor="#72BEFC">				
									<td style="width:6.2%;">Member Code</td>
									<td style="width:6.2%;">Drawn On</td>
									<td style="width:6.2%;">Cheque No</td>
									<td style="width:6.2%;">Cheque Date</td>
									<td style="width:6.2%;">Amount</td>
									<td style="width:6.2%;">Type</td>
								</tr>
							</table>
							
							<div style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
								<table id="tblDetailss"
									style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
									class="transTablex col8-center">
									<tbody>			
										<col style="width:21.4%;">	
										<col style="width:21.5%;">
										<col style="width:21.5%;">
										<col style="width:21.7%;">
										<col style="width:21.5%;">
										<col style="width:17%;">
										<col style="width:2.4%;">
									</tbody>
								</table>
							</div>
						</div>						
					</div>
					
					
						</div>
					</td>
				</tr>
			</table>					
							
			</table>

		<p align="center">
				<input type="submit" value="Submit" onclick="return funValidate();" class="form_button" />
				&nbsp; &nbsp; &nbsp; 
				<input type="reset" value="Reset"
					class="form_button" onclick="funResetField()" />
			</p>
			<br>
			<br>
			
			
	</s:form>
</body>
</html>
