<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <!-- charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> -->
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%-- <script type="text/javascript" src="<spring:url value="/resources/js/hindiTextBox.js"/>"></script> --%>
<title>Member Explorer</title>
<style>
.ui-autocomplete {
    max-height: 200px;
    overflow-y: auto;
    /* prevent horizontal scrollbar */
    overflow-x: hidden;
    /* add padding to account for vertical scrollbar */
    padding-right: 20px;
}
/* IE 6 doesn't support max-height
 * we use height instead, but this forces the menu to always be this tall
 */
* html .ui-autocomplete {
    height: 200px;
}
</style>
<%-- <script src="http://www.hinkhoj.com/common/js/keyboard.js"></script> --%>
 <%-- <script src="hindiTyping.js"></script> --%>
         <link rel="stylesheet" type="text/css" href="http://www.hinkhoj.com/common/css/keyboard.css" /> 
  
<script type="text/javascript">

/*On form Load It Reset form :Ritesh 22 Nov 2014*/
 $(document).ready(function () {
    resetForms('Member Explorer');
    $("#txtGroupName").focus();
    $(".tab_content").hide();
	$(".tab_content:first").show();

	$("ul.tabs li").click(function() {
		$("ul.tabs li").removeClass("active");
		$(this).addClass("active");
		$(".tab_content").hide();

		var activeTab = $(this).attr("data-state");
		$("#" + activeTab).fadeIn();
    
    
}); 
	$('#itemImage').attr('src', getContextPath()+"/resources/images/company_Logo.png");
	 funGetImage();
 });
 var clickCount =0.0;
 var fieldName;
 var listRow=0;
/**
 * AutoComplte when user Type the Name then Display Exists Group Name
 */
 $(document).ready(function()
			{
				$(function() {
					
					$("#txtGroupName").autocomplete({
					source: function(request, response)
					{
						var searchUrl=getContextPath()+"/AutoCompletGetGroupName.html";
						$.ajax({
						url: searchUrl,
						type: "POST",
						data: { term: request.term },
						dataType: "json",
							success: function(data) 
							{
								response($.map(data, function(v,i)
								{
									return {
										label: v,
										value: v
										};
								}));
							}
						});
					}
				});
				});
			});


	/**
	* Reset The Group Name TextField
	**/
	function funResetFields()
	{
		$("#txtGroupName").focus();
    }
	
	
		/**
		* Open Help
		**/
		function funHelp(transactionName)
		{	       
	       // window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
	       fieldName = transactionName;
	       window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
	       
	    }
		
		/**
		* Get and Set data from help file and load data Based on Selection Passing Value(Group Code)
		**/
		function funSetData(code)
		{
			switch(fieldName)
			{
			
			case "WCMemberCode":
				funSetMemberDataReceived(code);
				break;
			}
		}
		
		
		function funCallFormAction(actionName,object) 
		{
			var flg=true;
			if($('#txtCustCode').val()!='')
			{
				
				funGetImage();
					var code = $('#txtCustCode').val();
					 $.ajax({
					        type: "GET",
					        url: getContextPath()+"/memberExplore.html?code="+code,
					        async: false,
					        dataType: "json",
					        success: function(response)
					        {
					        	if(response[0].strMemberCode!=null)
					        		{
					        		
					        		funSetMemberData(response[0]);
					        		}
					        	else
					        		{
					        		alert("Invalid member code");
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
			else
				{
				alert("Pleae select member")
				}
			
			return false;
		}
		 function funShowImagePreview(input)
		 {
			 if (input.files && input.files[0])
			 {
				 var filerdr = new FileReader();
				 filerdr.onloadend = function(event) 
				 {
				 $('#memImage').attr('src', event.target.result);
				 }
				 filerdr.readAsDataURL(input.files[0]);
			 }
		 }

		 function funGetImage()
			{
					var code = $("#txtCustCode").val();
					searchUrl=getContextPath()+"/loadImage.html?custCode="+code;
					$("#itemImage").attr('src', searchUrl);
					
				
			}
		 
		 function funSetMemberData(response)
		 {
			 var address = response.strResidentAddressLine1+response.strResidentAddressLine2+
			 response.strResidentAddressLine3+response.strResidentAreaCode+response.strResidentCountryCode;
			 $("#txtMCName").text(response.strFullName);
			 $("#txtEmailId").text(response.strResidentEmailID);
			 $("#txtMobNo").text(response.strResidentMobileNo);
			 $("#txtAddress").text(address);
			 $("#txtDob").text(response.dteDateofBirth);
			 $("#txtMemStartDate").text(response.dteMembershipStartDate);
			 $("#txtMemExpiryDate").text(response.dteMembershipExpiryDate);
			 
			 $("#tblExplorer").css("display","block");
		 }
		 
		 function funSetMemberDataReceived(code){
			 resetTables(); 
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
					        		
					        	}
					        	else
					        	{
					        		$("#txtCustCode").val(response[0].strMemberCode);	 
						        	//$("#lblMemCode").text(response[0].strFirstName);
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
					        		
					        		/* var table=document.getElementById("tblDetails");
					    			var rowCount=table.rows.length;
					    			while(rowCount>0)
					    			{
					    			   table.deleteRow(0);
					    			   rowCount--;
					    			} */
					        		$.each(response, function(cnt,item)
						 					{
					        					
					        					$("#txtMemCode").val(item[0]);
					        					if(item[3]=="Received")
					        						{
					        						funAddRowReceived(item[0],item[1],item[2],item[5],item[4]);
					        						}
					        					else
					        						{
					        						funAddRowIssued(item[0],item[1],item[2],item[5],item[4]);
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
		 /**
			 * Adding Product Data in grid 
			 */
			function funAddRowReceived(memCode,drawnOn,chequeNo,chequeDate,chequeAmt) 
			{   	
				
			    var table = document.getElementById("tblDetails");
			    var rowCount = table.rows.length;
			    var row = table.insertRow(rowCount);   
			    
			    rowCount=listRow;
			    row.insertCell(0).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlRecieved["+(rowCount)+"].strMemCode\" value='"+memCode+"' id=\"txtMemCodeRec."+(rowCount)+"\" >";
				row.insertCell(1).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlRecieved["+(rowCount)+"].strDrawnOn\" value='"+drawnOn+"' id=\"txtBankCodeRec."+(rowCount)+"\" >";
			    row.insertCell(2).innerHTML= "<input class=\"Box\" type=\"text\" name=\"listPDCDtlRecieved["+(rowCount)+"].strChequeNo\" size=\"15%\" style=\"text-align: right;\" id=\"txtChequeNoRec."+(rowCount)+"\" value='"+chequeNo+"'/>";	
			    row.insertCell(3).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlRecieved["+(rowCount)+"].dteChequeDate\" id=\"txtChkDte."+(rowCount)+"\" value="+chequeDate+">";
			    row.insertCell(4).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlRecieved["+(rowCount)+"].dblChequeAmt\" value='"+chequeAmt+"' id=\"txtAmtRec."+(rowCount)+"\" >";	
			    row.insertCell(5).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlRecieved["+(rowCount)+"].strType\" value='Received' id=\"txtRecieved."+(rowCount)+"\" >";	
				row.insertCell(6).innerHTML= "<input type=\"button\" class=\"deletebutton\" size=\"1%\" value = \"Delete\" onClick=\"Javacsript:funDeleteRowRecieved(this)\"/>";
				   
			    listRow++;		    
			       
			}
		 
			function funAddRowIssued(memCode,drawnOn,chequeNo,chequeDate,chequeAmt) 
			{   	    	    
			    var table = document.getElementById("tblDetailss");
			    var rowCount = table.rows.length;
			    var row = table.insertRow(rowCount);   
			    
			    rowCount=listRow;
			    row.insertCell(0).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlIssued["+(rowCount)+"].strMemCode\" value='"+memCode+"' id=\"txtMemCodeIssued."+(rowCount)+"\" >";
				row.insertCell(1).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlIssued["+(rowCount)+"].strDrawnOn\" value='"+drawnOn+"' id=\"txtBankCodeIssued."+(rowCount)+"\" >";
			    row.insertCell(2).innerHTML= "<input class=\"Box\" type=\"text\" name=\"listPDCDtlIssued["+(rowCount)+"].strChequeNo\" size=\"15%\" style=\"text-align: right;\" id=\"txtChequeNoIssued."+(rowCount)+"\" value='"+chequeNo+"'/>";	
			    row.insertCell(3).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlIssued["+(rowCount)+"].dteChequeDate\" id=\"txtChkDteIssued."+(rowCount)+"\" value="+chequeDate+">";
			    row.insertCell(4).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlIssued["+(rowCount)+"].dblChequeAmt\" value='"+chequeAmt+"' id=\"txtAmtIssued."+(rowCount)+"\" >";	
			    row.insertCell(5).innerHTML= "<input class=\"Box\" size=\"15%\" name=\"listPDCDtlIssued["+(rowCount)+"].strType\" value='Issued' id=\"txtIssued."+(rowCount)+"\" >";	
			    row.insertCell(6).innerHTML= "<input type=\"button\" class=\"deletebutton\" size=\"1%\" value = \"Delete\" onClick=\"Javacsript:funDeleteRowIssued(this)\"/>";
			
			    listRow++;		    
			   		   		    
			} 
		 
			
			
		function resetTables()
		{
			$('#tblDetails').empty();
			$('#tblDetailss').empty();
		}
		 
		
</script>


</head>

<body onload="funOnLoad();">
	<div id="formHeading">
		<label>Member Explorer</label>
	</div>
	<div>
	<s:form name="Member Explorer" method="POST" action="showMemberExplorer.html?saddr=${urlHits}">

		<br />
		<br />
		<table class="masterTable">

			
			<tr>
				<td width="140px">Member Code</td>
				<td ><s:input id="txtCustCode" path="strCustomerCode"
						cssClass="searchTextBox" ondblclick="funHelp('WCMemberCode')" /></td>
			</tr>
				
				
			
		</table>
		<br />
		<p align="center">
			<input type="submit" value="Show" tabindex="3" class="form_button"
				onclick="return funCallFormAction('submit',this);" /> 
				<input type="reset"
				value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>
		 <!-- <table class="masterTable">
		<tr>
				<td style="width:150px;background-color: #C0E4FF;">
				
				 <div style="width:150px" > <img id="memImage" src="" width="150px" height="155px" alt="" onchange="funShowImagePreview(this);" ></div>&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;
				

				</td>
				</tr>
				
				<tr>
				
				
				</tr>
				</table> --> 
				
				<table id="tblExplorer" hidden="hidden"
				style="border: 0px solid black; width: 100%; height: 70%; margin-left: auto; margin-right: auto; background-color: #C0E4FF;">
				<tr>
					<td> 
						<div id="tab_container" style="height: 380px">
							<ul class="tabs">
								<li class="active" data-state="tab1">General</li>
								<li data-state="tab2">Received cheque</li>
								<li data-state="tab3">Issued cheque</li>
							</ul>

							<div id="tab1" class="tab_content" style="width: 1000px;height: 290px; display: block;">
								<br>
								<br>
								<table class="transTable">
									<tr>
									
									<td rowspan="9"  width="20%" style="background-color: #C0E4FF;">
				       					<img id="itemImage" src=""  width="200px" height="200px" alt="Item Image" /></td>
										<td width="18%">Member Name</td>
										
												<td><label id="txtMCName"></label></td>
												
											<s:errors path=""></s:errors></td>
									</tr>
									

									<tr>
										<td width="18%"">Email Id</td>
										<td><label id="txtEmailId"></label> </td>
									</tr>

									
									<tr>
										<td width="18%"><label>Mobile Number</label></td>
										<td width="100%"><label id="txtMobNo"></label>
												</td>

									</tr>
									
									<tr>
										<td width="18%"><label>Date of birth</label></td>
										<td width="100%"><label id="txtDob"></label>
												</td>

									</tr>
									
									<tr>
										<td width="18%"><label>Membership start Date</label></td>
										<td width="100%"><label id="txtMemStartDate"></label>
										</tr>
										<tr>
										<td width="18%"><label>Membership expiry Date</label></td>
										<td width="100%"><label id="txtMemExpiryDate"></label>
												</td>

									</tr>
									
									<tr>
										<td>Address</td>
										<td><label id="txtAddress"></label> </td>
									</tr>

								</table>
							</div>
							<div id="tab2" class="tab_content" style="width: 1000px;height: 290px; display: none;">
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
							
							<div id="tab3" class="tab_content" style="width: 1000px;height: 290px; display: none;">
								
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
				
				
	</s:form>
</div>
</body>
</html>