 <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
  <head>
    <link rel="stylesheet" type="text/css" href="default.css" />
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Bulk Invoice</title>
    <style>
  #tblGroup tr:hover , #tblSubGroup tr:hover, #tblloc tr:hover{
	background-color: #72BEFC;
	
}
</style>
    <script type="text/javascript">
    
 		 //Serching on Table when user type in text field
          $(document).ready(function()
    		{
    			var tablename='';
    			
    			$('#txtCustCode').keyup(function()
    	    			{
    						tablename='#tblCust';
    	    				searchTable($(this).val(),tablename);
    	    			});
    			$('#txtLocCode').keyup(function()
    	    			{
    						tablename='#tblloc';
    	    				searchTable($(this).val(),tablename);
    	    			});	
    			
    			var message='';
    			var code='',invDate='';
    			
    			<%if (session.getAttribute("success") != null) {
    				if(session.getAttribute("successMessage") != null){%>
    					message='<%=session.getAttribute("successMessage").toString()%>';
    					<%
    					session.removeAttribute("successMessage");
    				}
    				boolean test = ((Boolean) session.getAttribute("success")).booleanValue();
    				session.removeAttribute("success");
    				if (test) {%>
    					 code='<%=session.getAttribute("rptInvCode").toString()%>';
    					 invDate='<%=session.getAttribute("rptInvDate").toString()%>';
                         
    					
    					alert("Data Save successfully\n\n"+message);
    					window.open(getContextPath()+"/rptBulkInvoice.html?rptInvCode="+code+"&rptInvDate="+invDate);
    					 $("#wait").css("display","none");
    				<%}
    			}%>
    			
    		});

           //Searching on table on the basis of input value and table name
    		function searchTable(inputVal,tablename)
    		{
    			var table = $(tablename);
    			table.find('tr').each(function(index, row)
    			{
    				var allCells = $(row).find('td');
    				if(allCells.length > 0)
    				{
    					var found = false;
    					allCells.each(function(index, td)
    					{
    						var regExp = new RegExp(inputVal, 'i');
    						if(regExp.test($(td).find('input').val()))
    						{
    							found = true;
    							return false;
    						}
    					});
    					if(found == true)$(row).show();else $(row).hide();
    				}
    			});
    		}
    		
	    var fieldName="";
	    //Ajax Wait Image display
	    $(document).ready(function() 
    		{
    			$(document).ajaxStart(function()
    		 	{
    			    $("#wait").css("display","block");
    		  	});
    			$(document).ajaxComplete(function(){
    			    $("#wait").css("display","none");
    			  });
    		});
    
	    //Set Start Date in date picker
        $(function() 
    		{
	    	      	  
    			/*$( "#txtFromDate" ).datepicker({ dateFormat: 'dd-mm-yy' });		
    			$("#txtFromDate" ).datepicker('setDate', 'today'); */
    			var startDate="${startDate}";
    			var arr = startDate.split("/");
    			Dat=arr[0]+"-"+arr[1]+"-"+arr[2];
    			$( "#txtFromDate" ).datepicker({ dateFormat: 'dd-mm-yy' });
    			$("#txtFromDate" ).datepicker('setDate', 'today');
    			
    			$( "#txtToDate" ).datepicker({ dateFormat: 'dd-mm-yy' });		
    			$("#txtToDate" ).datepicker('setDate', 'today'); 
    			
    			$( "#txtInvoiceDate" ).datepicker({ dateFormat: 'dd-mm-yy' });
    			$("#txtInvoiceDate" ).datepicker('setDate', 'today');
    			
    		   var strPropCode='<%=session.getAttribute("propertyCode").toString()%>';
    			 
    		   var locationCode ='<%=session.getAttribute("locationCode").toString()%>';

     			// funSetAllLocationAllPrpoerty();
    		   funSetAllCust();
    		 
    			 
    			 
    		});	
      

    
	  //Open Help
      function funHelp(transactionName)
		{
    	  	fieldName=transactionName;
		//	window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:1000px;dialogLeft:200px;")
			window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:1000px;top=500,left=500")
		}
	  
	  //Set data After Seletion of Help
      function funSetData(code)
		{
			switch (fieldName) 
			{

			    case 'custcode':
			    	funSetCustomer(code);
			        break;  
			}
		}
      
   
    //Get and Set All Location on the basis of all Property
      /* function funSetAllLocationAllPrpoerty() {
			var searchUrl = "";
			searchUrl = getContextPath()+ "/loadAllLocationForAllProperty.html";
			$.ajax({
				type : "GET",
				url : searchUrl,
				dataType : "json",
				beforeSend : function(){
					 $("#wait").css("display","block");
			    },
			    complete: function(){
			    	 $("#wait").css("display","none");
			    },
				success : function(response) {
					if (response.strLocCode == 'Invalid Code') {
						alert("Invalid Location Code");
						$("#txtFromLocCode").val('');
						$("#lblFromLocName").text("");
						$("#txtFromLocCode").focus();
					} else
					{
						$.each(response, function(i,item)
						 		{
							funfillLocationGrid(response[i].strLocCode,response[i].strLocName);
								});
						
					}
				},
				error : function(jqXHR, exception) {
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
 */    
      //Fill  Location Data
	   /*  function funfillLocationGrid(strLocCode,strLocationName)
		{
			
			 	var table = document.getElementById("tblloc");
			    var rowCount = table.rows.length;
			    var row = table.insertRow(rowCount);
			    
			    row.insertCell(0).innerHTML= "<input id=\"cbToLocSel."+(rowCount)+"\" name=\"Locthemes\" type=\"checkbox\" class=\"LocCheckBoxClass\"  checked=\"checked\" value='"+strLocCode+"' />";
			    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"strToLocCode."+(rowCount)+"\" value='"+strLocCode+"' >";
			    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \" size=\"50%\" id=\"strToLocName."+(rowCount)+"\" value='"+strLocationName+"' >";
		} */
	  
	  
		     
	      //Get and set Customer  Data 
	      function funSetAllCust() {
				var searchUrl = "";
				searchUrl = getContextPath()+ "/loadAllCustomer.html";
				$.ajax({
					type : "GET",
					url : searchUrl,
					dataType : "json",
					beforeSend : function(){
						 $("#wait").css("display","block");
				    },
				    complete: function(){
				    	 $("#wait").css("display","none");
				    },
					success : function(response) {
						if (response.strPCode == 'Invalid Code') {
							alert("Invalid Customer Code");
							
						} else
						{
							$.each(response, function(i,item)
							 		{
								funfillCustGrid(response[i].strPCode,response[i].strPName);
									});
							
						}
					},
					error : function(jqXHR, exception) {
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
		
		 function funLoadAllCustomerSO() {
				var searchUrl = "";
				var frmDate=$("#txtFromDate").val()
				var toDate=	$("#txtToDate").val()
				searchUrl = getContextPath()+ "/loadAllCustomerSO.html?CustCode="+strCustCode+"&frmDate="+frmDate+"&toDate="+toDate;
				$.ajax({
					type : "GET",
					url : searchUrl,
					dataType : "json",
					beforeSend : function(){
						 $("#wait").css("display","block");
				    },
				    complete: function(){
				    	 $("#wait").css("display","none");
				    },
					success : function(response) {
					    funRemRows("tblSOData");
					   
						if (response.strPCode == 'Invalid Code') {
							alert("Invalid Customer Code");
							
						} else
						{
							$.each(response, function(i,item)
							 		{
								      funfillSOGrid(response[i].strSOCode,response[i].dteSODate,response[i].strCustName,response[i].dblSubTotal,response[i].strCustCode);
								     
									});
						}
					},
					error : function(jqXHR, exception) {
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
		    
				
		    //Fill Supplier Data
		    function funfillCustGrid(strCustCode,strCustName)
			{
				
				 	var table = document.getElementById("tblCust");
				    var rowCount = table.rows.length;
				    var row = table.insertRow(rowCount);
				    
				    row.insertCell(0).innerHTML= "<input id=\"cbSuppSel."+(rowCount)+"\" name=\"Custthemes\" type=\"checkbox\" class=\"CustCheckBoxClass\"  checked=\"checked\" value='"+strCustCode+"' />";
				    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"strCustCode."+(rowCount)+"\" value='"+strCustCode+"' >";
				    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \" size=\"50%\" id=\"strCName."+(rowCount)+"\" value='"+strCustName+"' >";
			}
		    
		    function funfillSOGrid(strSOCode,dteSODate,strCustName,dblSubTotal,strCustCode)
			{
		    	
				 	var table = document.getElementById("tblSOData");
				    var rowCount = table.rows.length;
				    var row = table.insertRow(rowCount);
				   // name=\"listInvoiceTaxDtl["+(rowCount)+"].strTaxCode\"
				   
				    row.insertCell(0).innerHTML= "<input id=\"SOCode."+(rowCount)+"\"  name=\"listMultipleSOCodes["+(rowCount)+"].SOCodethemes\" type=\"checkbox\" class=\"CustSOCheckBoxClass\"  checked=\"checked\" value='"+strSOCode+"' />";
				    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \" name=\"listMultipleSOCodes["+(rowCount)+"].strSOCode\" size=\"19%\" id=\"strSOCode."+(rowCount)+"\" value='"+strSOCode+"' >";
				    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \" name=\"listMultipleSOCodes["+(rowCount)+"].dteSODate\" size=\"15%\" id=\"dteSODate."+(rowCount)+"\" value='"+dteSODate+"' >";
				    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box \" name=\"listMultipleSOCodes["+(rowCount)+"].strCustName\" size=\"32%\" id=\"strCustName."+(rowCount)+"\" value='"+strCustName+"' >";
				    row.insertCell(4).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  name=\"listMultipleSOCodes["+(rowCount)+"].dblSubTotalAmt\"  style=\"text-align: right;\" size=\"11%\" id=\"dblSubTotalAmt."+(rowCount)+"\" value='"+dblSubTotal+"' >";
				    row.insertCell(5).innerHTML= "<input type=\"hidden\"   name=\"listMultipleSOCodes["+(rowCount)+"].strCustCode\"   size=\"0%\" id=\"strCustCode."+(rowCount)+"\" value='"+strCustCode+"' >";

				
			}
		    
		    
		    //Remove All Row from Grid Passing Table Id as a parameter
		    function funRemRows(tablename) 
			{
				var table = document.getElementById(tablename);
				var rowCount = table.rows.length;
				while(rowCount>0)
				{
					table.deleteRow(0);
					rowCount--;
				}
			}
		 
			//Select All Group,SubGroup,From Location, To Location When Clicking Select All Check Box
			 $(document).ready(function () 
						{
							
							$("#chkCustALL").click(function () {
							    $(".CustCheckBoxClass").prop('checked', $(this).prop('checked'));
							});
							
							
// 							$("#chkLocALL").click(function () {
// 							    $(".LocCheckBoxClass").prop('checked', $(this).prop('checked'));
// 							});
							
						
							
						});
			 $(document).ready(function () 
						{
							
							$("#chkCustALLSO").click(function () {
							    $(".CustSOCheckBoxClass").prop('checked', $(this).prop('checked'));
							});
							
							
//							$("#chkLocALL").click(function () {
//							    $(".LocCheckBoxClass").prop('checked', $(this).prop('checked'));
//							});
							
						
							
						});
			// chkCustALLSO		 
			 
	    //Submit Data after clicking Submit Button with validation 
	    var strCustCode=" ";
	    function btnExecute_Onclick()
	    {
		            strCustCode=" ";
					 $('input[name="Custthemes"]:checked').each(function() {
						if(strCustCode.length>0)
						{
							 strCustCode=strCustCode+","+this.value;
						}
						else
						{
							strCustCode=this.value;
					    }
						 
						});

				//	 $("#hidCustCodes").val(strCustCode);
					 
					 funLoadAllCustomerSO();
		    }
	    var strSoCode="";
	   /*  function btnSubmit_Onclick()
	    {
		             strSoCode=" ";
					 $('input[name="SOCodethemes"]:checked').each(function() {
						 if(strSoCode.length>0)
							 {
							 strSoCode=strSoCode+","+this.value;
							 }
							 else
							 {
								 strSoCode=this.value;
							 }
						 
						});
					 $("#hidSOCodes").val(strSOCode);
	    } */
	  
	  
	   //Reset All Filed After Clicking Reset Button
	    function funResetFields()
		{
			location.reload(true); 
		}
		
		function funOnChangeCurrency()
		{
			
			var currencyCode=$("#cmbCurrency").val();
			   var currValue=funGetCurrencyCode(currencyCode);
				if(currValue==null)
				{
					currValue=1.0;
				}
				$("#txtDblCurrencyConv").val(currValue);
		
		}

		function funGetCurrencyCode(code){

			var amt=1;
			$.ajax({
				type : "POST",
				url : getContextPath()+ "/loadCurrencyCode.html?docCode=" + code,
				dataType : "json",
				async:false,
				success : function(response){ 
					if(response.strCurrencyCode=='Invalid Code')
		        	{
		        		
		        	}
		        	else
		        	{        
		        		amt=response.dblConvToBaseCurr;
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
			return amt;
		}
		
		function funChangeCombo() {
			
			// Ajax call loadSettlementMasterData  --> pass settlement code
			var code=$("#cmbSettlement").val();
			var searchurl=getContextPath()+"/loadSettlementMasterData.html?code="+code;
			 $.ajax({
				        type: "GET",
				        url: searchurl,
				        dataType: "json",
				        success: function(response)
				        {
				        	if(response.strSettlementCode=='Invalid Code')
				        	{
				        		alert("Settlement type not found");
				        	}
				        	else
				        	{
					        	$("#txtSettlementType").val(response.strSettlementType);
					        	//alert($("#txtSettlementType").val());
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
    function funSubmit()
	{
    
    	$("#wait").css("display","block");
    	return true;
    	
	}
		
	</script>    
  </head>
  	
	<body id="frmBulkInvoice">
	<div id="formHeading">
		<label>Bulk Invoice</label>
	</div>
	
		<s:form name="frmBulkInvoice" method="POST"
		action="saveBulkInvoice.html?saddr=${urlHits}">
	   		<br />
	   		<table class="transTable">
			    <tr>
					<td width="10%"><label>From SO Date :</label></td>
					<td colspan="1" width="10%"><s:input id="txtFromDate" path="dteFromDate" required="true" readonly="readonly" cssClass="calenderTextBox"/></td>
					<td width="10%"><label>To SO Date :</label></td>
					<td colspan="1"><s:input id="txtToDate" path="dteToDate" required="true" readonly="readonly" cssClass="calenderTextBox"/>
					<td width="10%"><label>Invoice Date :</label></td>
				    <td colspan="1"><s:input id="txtInvoiceDate" path="dteInvDate" required="true" readonly="readonly" cssClass="calenderTextBox"/>
				     
		            <td width="10%"><input type="button" value="Execute" class="form_button" onclick="btnExecute_Onclick();"/></td>
					
				</tr>
				<tr>
				<!-- <td colspan="4"></td> -->
				<td width="100px"><label>Settlement</label>
					<td><s:select id="cmbSettlement" path="strSettlementCode"
											items="${settlementList}" cssClass="BoxW124px" 
											onkeypress="funGetKeyCode(event,'Settlement')" onclick="funChangeCombo()" /></td>
								
					<td ><label>Currency </label></td>
									
					<td width="10%"><s:select id="cmbCurrency" items="${currencyList}" path="strCurrencyCode" cssClass="BoxW124px" onclick="funOnChangeCurrency()">
				    </s:select></td>
					<td >
					<s:input  type="text"   id="txtDblCurrencyConv" style="text-align: right;" name="txtDblCurrencyConv" path="dblCurrencyConv" class="decimal-places numberField" cssClass="BoxW48px" />
					</td >
					<td><input type="hidden" id="txtSettlementType"> 
					</td>	
				</tr>
			<%-- 	 <tr>
					<td width="10%"><label>From Fulfillment Date :</label></td>
					<td colspan="1" width="10%"><s:input id="txtFromFulfillment" path="dteFromFulfillment" required="true" readonly="readonly" cssClass="calenderTextBox"/></td>
					<td width="10%"><label>To Fulfillment Date :</label></td>
					<td colspan="1"><s:input id="txtToFulfillment" path="dteToFulfillment" required="true" readonly="readonly" cssClass="calenderTextBox"/>
					</td>
				</tr>
 --%>				
			    </table>
				<br>
			<table class="transTable">
			
			<tr>
			
					
			</tr>
		</table>
		<br>
		<table class="transTable">
				
		<tr>
				<td colspan="2">Customer&nbsp;&nbsp;&nbsp;<input style="width: 35%; background-position: 150px 2px;" type="text" id="txtCustCode" 
			 Class="searchTextBox" placeholder="Type to search"></input>
			<label id="lblCustName"></label></td>
		
			</tr>
			<tr>
						<td colspan="2">
						<div
							style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 350px;width: 400px; overflow-x: hidden; overflow-y: scroll;">

							<table id="" class="masterTable"
								style="width: 100%; border-collapse: separate;">
								<tbody>
									<tr bgcolor="#72BEFC">
										<td width="15%"><input type="checkbox" checked="checked" 
										id="chkCustALL"/>Select</td>
										<td width="25%">To Customer Code</td>
										<td width="65%">To Customer Name</td>

									</tr>
								</tbody>
							</table>
							<table id="tblCust" class="masterTable"
								style="width: 100%; border-collapse: separate;">

								<tr bgcolor="#72BEFC">
									

								</tr>
							</table>
						</div>
				</td>
				<td colspan="5">
						<div
							style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 350px;width: 600px; overflow-x: hidden; overflow-y: scroll;">

							<table id="" class="masterTable"
								style="width: 100%; border-collapse: separate;">
								<tbody>
									<tr bgcolor="#72BEFC">
										<td width="5%"><input type="checkbox" checked="checked" 
										id="chkCustALLSO"/>Select</td>
										<td width="22.5%">SO Code</td>
										<td width="20%">SO Date</td>
										<td width="38%">Customer</td>
										<td width="40%">Amount</td>
										

									</tr>
								</tbody>
							</table>
							<table id="tblSOData" class="masterTable"
								style="width: 100%; border-collapse: separate;">

								<tr bgcolor="#72BEFC">
									

								</tr>
							</table>
						</div>
				</td>
				
				
				
				
				
				
			
		</table>
		
		<br>
	<%-- 	<table class="transTable">
			<tr>
				<td width="10%"><label>Report Type :</label></td>
				<td colspan="3"><s:select id="cmbDocType" path="strDocType"
						cssClass="BoxW124px">
 						<s:option value="PDF">PDF</s:option> 
						<s:option value="XLS">EXCEL</s:option>
					</s:select></td>
			</tr>
<tr>


	</table> --%>
		

		<br>
			<p align="center">
				 <input type="submit" value="Submit"  class="form_button" onclick ="return funSubmit()" />
				 <input type="button" value="Reset" class="form_button" onclick="funResetFields()"/>	
				 		     
			</p>  
			
			
			<s:input type="hidden" id="hidCustCodes" path="strSuppCode"></s:input>
			<s:input type="hidden" id="hidSOCodes" path="strSOCode"></s:input>

			
			
			<div id="wait"
			style="display: none; width: 60px; height: 60px; border: 0px solid black; position: absolute; top: 60%; left: 55%; padding: 2px;">
			<img
				src="../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif"
				width="60px" height="60px" />
		</div>
		</s:form>
	</body>
</html>