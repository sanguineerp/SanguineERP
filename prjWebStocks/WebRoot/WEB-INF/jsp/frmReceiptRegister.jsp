<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
var fieldName="";
	/**
	 * Ready Function for Ajax Waiting and reset form
	 */
	$(document).ready(function() 
		{
			$(document).ajaxStart(function()
		 	{
			    $("#wait").css("display","block");
		  	});
		 	
			$(document).ajaxComplete(function()
			{
			    $("#wait").css("display","none");
			});	
			
			funSetAllLocationAllPrpoerty();
			funGetGroupData();
		});
		
		/**
		 * Ready Function for Initialization Text Field with default value 
		 * Set Date in date picker
		 * Getting session value
		 */
		$(function() 
		{
	    	var startDate="${startDate}";
			var arr = startDate.split("/");
			Date1=arr[0]+"-"+arr[1]+"-"+arr[2];	
	    	$( "#txtFromDate" ).datepicker({ dateFormat: 'dd-mm-yy' });		
			$("#txtFromDate" ).datepicker('setDate', Date1);
			$( "#txtToDate" ).datepicker({ dateFormat: 'dd-mm-yy' });		
			$("#txtToDate" ).datepicker('setDate', 'today');
			funRemRows("tblloc");
			funRemRows("tblSupp");
			
			  var formName ="";
			    formName='<%=session.getAttribute("formName").toString()%>';
			    if(formName == "frmReceiptRegister")
			    	{
			    	 	 $('#lblFormHeader').text("Receipt Register Report");
			    	 	document.getElementById('trGrpSubGroup').style.display = "none" ;
			    	 	document.getElementById('trGrpSubGroupData').style.display = "none" ;
			    	 	
			    	}
			    else if(formName == "frmProductPurchaseReciept")
			    	{
			    	   	 $('#lblFormHeader').text("Product Purchase Receipt Report");
			    	}else
			    		{
			    		  $('#lblFormHeader').text("Receipt Register Report");
			    		}
			
		});

		/**
		 * On blur event on Textfield 
		 */
		$(function() {

			$('#txtSuppCode').blur(function() {
				var code = $('#txtSuppCode').val();
				if(code.trim().length > 0 && code !="?" && code !="/"){
					funSetSupplier(code);
					$('#txtSuppCode').val('');
				}
			});

			$('#txtLocCode').blur(function() {
				var code = $('#txtLocCode').val();
				if(code.trim().length > 0 && code !="?" && code !="/"){
					funSetLocation(code);
					$('#txtLocCode').val('');
				}
			});
		});
		
		/**
		 * Open help windows
		 */
		function funHelp(transactionName)
		{
		  	fieldName=transactionName;
		//	window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:1000px;dialogLeft:200px;")
			window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:1000px;dialogLeft:200px;")
		}
		
		 /**
		 * Set Data after selecting form Help windows
		 */
		function funSetData(code)
		{
			switch (fieldName) 
			{
			    case 'locationmaster':
			    	funSetLocation(code);
			        break;
			        
			    case 'suppcode':
			    	funSetSupplier(code);
			        break;
			}
		}
		 
		/**
		  * Set location data
		 **/
		function funSetLocation(code) {
			var searchUrl = "";
			searchUrl = getContextPath()
					+ "/loadLocationMasterData.html?locCode=" + code;
		
			$.ajax({
				type : "GET",
				url : searchUrl,
				dataType : "json",
				success : function(response) {
					if (response.strLocCode == 'Invalid Code') {
						alert("Invalid Location Code");
						$("#txtLocCode").val('');
						$("#lblLocName").text("");
						$("#txtLocCode").focus();
					} else
					{
						funfillLocationGrid(response.strLocCode,response.strLocName);
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
		
		/**
		  * Set Supplier data
		 **/
		function funSetSupplier(code) {
			var searchUrl = "";
			searchUrl = getContextPath()
					+ "/loadSupplierMasterData.html?partyCode=" + code;

			$.ajax({
				type : "GET",
				url : searchUrl,
				dataType : "json",
				success : function(response) {
					if ('Invalid Code' == response.strPCode) {
						alert('Invalid Code');
						$("#txtSuppCode").val('');
						$("#txtSuppName").text('');
						$("#txtSuppCode").focus();
					} else {
						funfillSupplier(response.strPCode,response.strPName);
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
		
		
		//Get and Set All Location on the basis of all Property
	      function funSetAllLocationAllPrpoerty() {
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
		
		
		 /**
		  * Fill location data in grid
		 **/
		    function funfillLocationGrid(strLocCode,strLocationName)
			{
				 	var table = document.getElementById("tblloc");
				    var rowCount = table.rows.length;
				    var row = table.insertRow(rowCount);
				    
				    row.insertCell(0).innerHTML= "<input id=\"cbLocSel."+(rowCount)+"\" name=\"Locthemes\" type=\"checkbox\" class=\"LocCheckBoxClass\"  checked=\"checked\" value='"+strLocCode+"'  class=\"locCheckBoxClass\"/>";
				    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"strLocCode."+(rowCount)+"\" value='"+strLocCode+"' >";
				    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \" size=\"50%\" id=\"strLocName."+(rowCount)+"\" value='"+strLocationName+"' >";
			}
		
		    /**
			  * Fill Supplier data in grid
			 **/
		    function funfillSupplier(strSuppCode,strSuppName) 
			{
				var table = document.getElementById("tblSupp");
			    var rowCount = table.rows.length;
			    var row = table.insertRow(rowCount);
			    
			    row.insertCell(0).innerHTML= "<input id=\"cbSuppSel."+(rowCount)+"\" type=\"checkbox\" checked=\"checked\" name=\"suppthemes\" value='"+strSuppCode+"' class=\"suppCheckBoxClass\" />";
			    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\" size=\"15%\" id=\"strSuppCode."+(rowCount)+"\" value='"+strSuppCode+"' >";
			    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box\" size=\"50%\" id=\"strSuppName."+(rowCount)+"\" value='"+strSuppName+"' >";
			}
		    
		  //Get All Group data 
		    function funGetGroupData()
			{
				var searchUrl = getContextPath() + "/loadAllGroupData.html";
				$.ajax({
					type : "GET",
					url : searchUrl,
					dataType : "json",
					success : function(response) {
						funRemRows("tblGroup");
						$.each(response, function(i,item)
				 		{
							funfillGroupGrid(response[i].strGCode,response[i].strGName);
						});
						funGroupChkOnClick();
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
		    
		    //Fill Group Data
			function funfillGroupGrid(strGroupCode,strGroupName)
			{
				
				 	var table = document.getElementById("tblGroup");
				    var rowCount = table.rows.length;
				    var row = table.insertRow(rowCount);
				    
				    row.insertCell(0).innerHTML= "<input id=\"cbGSel."+(rowCount)+"\" type=\"checkbox\"  name=\"Groupthemes\" class=\"GCheckBoxClass selected \" checked=\"checked\" value='"+strGroupCode+"' onclick=\"funGroupChkOnClick()\"/>";
				    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box selected\" size=\"15%\" id=\"strGCode."+(rowCount)+"\" value='"+strGroupCode+"' >";
				    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box selected\" size=\"50%\" id=\"strGName."+(rowCount)+"\" value='"+strGroupName+"' >";
			}
				
			//Select All Group
			function funGroupChkOnClick()
			{
				var table = document.getElementById("tblGroup");
			    var rowCount = table.rows.length;  
			    var strGCodes="";
			    for(no=0;no<rowCount;no++)
			    {
			        if(document.all("cbGSel."+no).checked==true)
			        	{
			        		if(strGCodes.length>0)
			        			{
			        				strGCodes=strGCodes+","+document.all("strGCode."+no).value;
			        			}
			        		else
			        			{
			        				strGCodes=document.all("strGCode."+no).value;
			        			}
			        	}
			    }
			    funGetSubGroupData(strGCodes);
			}
		    
			
			//Geting SubGroup Data On the basis of Selection Group
			function funGetSubGroupData(strGCodes)
			{
				strCodes = strGCodes.split(",");
				var count=0;
				funRemRows("tblSubGroup");
				for (ci = 0; ci < strCodes.length; ci++) 
				 {
					var searchUrl = getContextPath() + "/loadSubGroupCombo.html?code="+ strCodes[ci];
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
						success : function(response)
						{
							$.each(response, function(key, value) {
								funfillSubGroup(key,value);
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
				
			}
			
			//Fill SubGroup Data
			function funfillSubGroup(strSGCode,strSGName) 
			{
				var table = document.getElementById("tblSubGroup");
			    var rowCount = table.rows.length;
			    var row = table.insertRow(rowCount);
			    
			    row.insertCell(0).innerHTML= "<input id=\"cbSGSel."+(rowCount)+"\" type=\"checkbox\" checked=\"checked\" name=\"SubGroupthemes\" value='"+strSGCode+"' class=\"SGCheckBoxClass\" />";
			    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\" size=\"15%\" id=\"strSGCode."+(rowCount)+"\" value='"+strSGCode+"' >";
			    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box\" size=\"50%\" id=\"strSGName."+(rowCount)+"\" value='"+strSGName+"' >";
			}
			
			//Select All Group,SubGroup,From Location, To Location When Clicking Select All Check Box
			 $(document).ready(function () 
						{
							$("#chkSGALL").click(function ()
							{
							    $(".SGCheckBoxClass").prop('checked', $(this).prop('checked'));
							});
							
							$("#chkGrpALL").click(function () 
							{
							    $(".GCheckBoxClass").prop('checked', $(this).prop('checked'));
							    funGroupChkOnClick();
							  
							});
							
							$("#chkLocALL").click(function () 
							{
								$(".LocCheckBoxClass").prop('checked', $(this).prop('checked'));
							});
							
							$("#chkSuppALL").click(function ()
							{
							    $(".suppCheckBoxClass").prop('checked', $(this).prop('checked'));
							});
							
						});
		
		 /**
		  * Remove All row form Grid
		 **/
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
		

		 /**
		   * Checking Validation before submiting the data
		  **/
		 function btnSubmit_Onclick() 
			{	
			 
				var spFromDate=$("#txtFromDate").val().split('-');
				var spToDate=$("#txtToDate").val().split('-');
				var FromDate= new Date(spFromDate[2],spFromDate[1]-1,spFromDate[0]);
				var ToDate = new Date(spToDate[2],spToDate[1]-1,spToDate[0]);
				if(!fun_isDate($("#txtFromDate").val())) 
			    {
					 alert('Invalid From Date');
					 $("#txtFromDate").focus();
					 return false;  
			    }
			    if(!fun_isDate($("#txtToDate").val())) 
			    {
					 alert('Invalid To Date');
					 $("#txtToDate").focus();
					 return false;  
			    }
				if(ToDate<FromDate)
				{
						alert("To Date Should Not Be Less Than Form Date");
				    	$("#txtToDate").focus();
						return false;		    	
				}
		    	if($("#txtFromDate").val()=='')
			    {
			    	alert("Please enter From Date");
			    	return false;
			    }
			    if($("#txtToDate").val()=='')
			    {
			    	alert("Please enter To Date");
			    	return false;
			    }
			    else
				{
			    	
			    	var strLocCode="";
					 
					 $('input[name="Locthemes"]:checked').each(function() {
						 if(strLocCode.length>0)
							 {
							 strLocCode=strLocCode+","+this.value;
							 }
							 else
							 {
								 strLocCode=this.value;
							 }
						 
						});
					 $("#hidLocCodes").val(strLocCode);
					 
					 var strSuppCode="";
					 
					 $('input[name="suppthemes"]:checked').each(function() {
						 if(strSuppCode.length>0)
							 {
							 strSuppCode=strSuppCode+","+this.value;
							 }
							 else
							 {
								 strSuppCode=this.value;
							 }
						 
						});
					 $("#hidSuppCode").val(strSuppCode);
					 
					 var strGroupCode="";
					 $('input[name="Groupthemes"]:checked').each(function() {
						 if(strGroupCode.length>0)
							 {
							 strGroupCode=strGroupCode+","+this.value;
							 }
							 else
							 {
								 strGroupCode=this.value;
							 }
						 
						});
					 $("#hidGroupCode").val(strGroupCode);
						
					 var strSubGroupCode="";
					 $('input[name="SubGroupthemes"]:checked').each(function() {
						 if(strSubGroupCode.length>0)
							 {
							 strSubGroupCode=strSubGroupCode+","+this.value;
							 }
							 else
							 {
								 strSubGroupCode=this.value;
							 }
						 
						});
					 
					 $("#hidSubGroupCode").val(strSubGroupCode);
					 
					 document.forms[0].action = "rptReceiptRegister.html";
				}
			    
			    
			  // Change ACtion PerForm  
			    
			    var formName ="";
			    formName='<%=session.getAttribute("formName").toString()%>';
			    if(formName == "frmReceiptRegister")
			    	{
			    	 $("#formTag").attr("action", "rptReceiptRegister.html");
			    	
			    	}
			    else if(formName == "frmProductPurchaseReciept")
			    	{
			    	 $("#formTag").attr("action", "rptProductPurchaseReciept.html");
			    	
			    	}else
			    		{
			    		  $("#formTag").attr("action", "rptReceiptRegister.html");
			    		 
			    		}
			    
			    
			    
			    
			    
			}
			
		
	   /**
		 * Reset form
		**/
		function funResetFields()
		{
			location.reload(true); 
		}
		</script>
	</head>
<body>
	<div id="formHeading">
		<label id=lblFormHeader>Receipt Register Report</label>
	</div>
	<br />
	<br />
	<s:form id="formTag" name="frmReceiptRegister" method="POST"
		action="rptReceiptRegister.html" target="_blank">
		<table class="transTable">
			<tr>
				<th colspan="4"></th>
			</tr>

			<tr>
				<td width="10%"><label>From Date</label></td>
				<td colspan="1" width="10%"><s:input id="txtFromDate" path="dtFromDate"
						value="" readonly="readonly" cssClass="calenderTextBox" required="true"/></td>
				<td width="10%"><label>To Date</label></td>
				<td colspan="1"><s:input id="txtToDate" path="dtToDate"
						value="" readonly="readonly" cssClass="calenderTextBox " required="true"/></td>
			</tr>
		</table>

		<br>
		<table class="transTable">
			<tr>
				<td width="49%">Location&nbsp;&nbsp;&nbsp;
					<input type="text" id="txtLocCode" 
					ondblclick="funHelp('locationmaster')" Class="searchTextBox"></input>
					<label id="lblToLocName"></label>
			    </td>
				<td width="49%">Supplier&nbsp;&nbsp;&nbsp;
				 <input id="txtSuppCode"  ondblclick="funHelp('suppcode')" Class="searchTextBox" />
				<label id="txtSuppName"></label>
				
				</td>
			</tr>
			<tr>
				<td style="padding: 0 !important;">
					<div class="transTablex">
						<div
							style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 150px; overflow-x: hidden; overflow-y: scroll;">

							<table id="" class="masterTable"
								style="width: 100%; border-collapse: separate;">
								<tbody>
									<tr bgcolor="#72BEFC">
										<td width="15%"><input type="checkbox" id="chkLocALL"/>Select</td>
										<td width="25%">Location Code</td>
										<td width="65%">Location Name</td>

									</tr>
								</tbody>
							</table>
							<table id="tblloc" class="masterTable"
								style="width: 100%; border-collapse: separate;">

								<tr bgcolor="#72BEFC">
									<td width="15%"></td>
									<td width="25%"></td>
									<td width="65%"></td>

								</tr>
							</table>
						</div>
					</div>
				</td>
				<td style="padding: 0 !important;">
					<div
						style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 150px; overflow-x: hidden; overflow-y: scroll;">

						<table id="" class="masterTable"
							style="width: 100%; border-collapse: separate;">
							<tbody>
								<tr bgcolor="#72BEFC">
									<td width="15%"><input type="checkbox" id="chkSuppALL"
										onclick="funCheckUnchecksupp()" />Select</td>
									<td width="25%">Supplier Code</td>
									<td width="65%">Supplier Name</td>

								</tr>
							</tbody>
						</table>
						<table id="tblSupp" class="masterTable"
							style="width: 100%; border-collapse: separate;">
							<tbody>
								<tr bgcolor="#72BEFC">
									<td width="15%"></td>
									<td width="25%"></td>
									<td width="65%"></td>

								</tr>
							</tbody>
						</table>
					</div>
				</td>
			</tr>
			
			<tr><td></td><br><td></td></tr>
			<tr id="trGrpSubGroup">
				<td width="49%">Group&nbsp;&nbsp;&nbsp;
					<input type="text" id="txtGroupCode" 
					 Class="searchTextBox"></input>
					<label id="lblToGroupName"></label>
			    </td>
				<td width="49%">SubGroup&nbsp;&nbsp;&nbsp;
				 <input id="txtSubGroupCode"  Class="searchTextBox" />
				<label id="txtSubGroupName"></label>
				
				</td>
			</tr>
			<tr id="trGrpSubGroupData">
				<td style="padding: 0 !important;">
					<div class="transTablex">
						<div
							style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 150px; overflow-x: hidden; overflow-y: scroll;">

							<table id="" class="masterTable"
								style="width: 100%; border-collapse: separate;">
								<tbody>
									<tr bgcolor="#72BEFC">
										<td width="15%"><input type="checkbox" id="chkGrpALL"
										 onclick="funCheckUncheckGroup()"/>Select</td>
										<td width="25%">Group Code</td>
										<td width="65%">Group Name</td>

									</tr>
								</tbody>
							</table>
							<table id="tblGroup" class="masterTable"
								style="width: 100%; border-collapse: separate;">

								<tr bgcolor="#72BEFC">
									<td width="15%"></td>
									<td width="25%"></td>
									<td width="65%"></td>

								</tr>
							</table>
						</div>
					</div>
				</td>
				<td style="padding: 0 !important;">
					<div
						style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 150px; overflow-x: hidden; overflow-y: scroll;">

						<table id="" class="masterTable"
							style="width: 100%; border-collapse: separate;">
							<tbody>
								<tr bgcolor="#72BEFC">
									<td width="15%"><input type="checkbox" id="chkSGALL"
										onclick="funCheckUncheckSubgroup()" />Select</td>
									<td width="25%">SubGroup Code</td>
									<td width="65%">SubGroup Name</td>

								</tr>
							</tbody>
						</table>
						<table id="tblSubGroup" class="masterTable"
							style="width: 100%; border-collapse: separate;">
							<tbody>
								<tr bgcolor="#72BEFC">
									<td width="15%"></td>
									<td width="25%"></td>
									<td width="65%"></td>

								</tr>
							</tbody>
						</table>
					</div>
				</td>
			</tr>
		</table>
		<br>
		<table class="transTable">
			<tr>
				<td width="10%"><label>Report Type</label></td>
				<td colspan="3"><s:select id="cmbDocType" path="strDocType"
						cssClass="BoxW124px">
						<s:option value="PDF">PDF</s:option>
						<s:option value="XLS">EXCEL</s:option>
						<s:option value="HTML">HTML</s:option>
						<s:option value="CSV">CSV</s:option>
					</s:select></td>
			</tr>
			<tr>
				<td colspan="5"></td>

			</tr>
		</table>
		<br>
		<p align="center">
			<input type="submit" value="Submit" class="form_button"
				onclick="return btnSubmit_Onclick()" /> <input type="button"
				value="Reset" class="form_button" onclick="funResetFields()" />
		</p>
		<s:input type="hidden" id="hidLocCodes" path="strDocCode"></s:input>
		<s:input type="hidden" id="hidSuppCode" path="strSuppCode"></s:input>
		<s:input type="hidden" id="hidGroupCode" path="strGCode"></s:input>
		<s:input type="hidden" id="hidSubGroupCode" path="strSGCode"></s:input>
		<div id="wait"
			style="display: none; width: 60px; height: 60px; border: 0px solid black; position: absolute; top: 60%; left: 55%; padding: 2px;">
			<img
				src="../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif"
				width="60px" height="60px" />
		</div>
	</s:form>
</body>
</html>