<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@	taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
	<script type="text/javascript">

$(document).ready(function() 
		{
			var startDate="${startDate}";
			var arr = startDate.split("/");
			Dat=arr[0]+"-"+arr[1]+"-"+arr[2];
			$("#txtFromDate").datepicker({ dateFormat: 'dd-mm-yy' });
			$("#txtFromDate").datepicker('setDate',Dat);
			$("#txtToDate").datepicker({ dateFormat: 'dd-mm-yy' });
			$("#txtToDate").datepicker('setDate', 'today');

			
				
			$(document).ajaxStart(function()
			{
			    $("#wait").css("display","block");
			});
			$(document).ajaxComplete(function()
			{
				$("#wait").css("display","none");
			});
			
			
			$("#btnExecute").click(function( event )
			{
				funLoadAMCList();					
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
				}}%>
						
							<%if(null!=session.getAttribute("rptInvCode")){%>
							
							code='<%=session.getAttribute("rptInvCode").toString()%>';
<%-- 							dccode='<%=session.getAttribute("rptDcCode").toString()%>'; --%>
                           
                     
							dccode='';
							<%if(null!=session.getAttribute("rptInvDate")){%>
							invDate='<%=session.getAttribute("rptInvDate").toString()%>';
							invoiceformat='<%=session.getAttribute("invoieFormat").toString()%>';
<%-- 							invoiceformat='<%=session.getAttribute("invoieFormat").toString()%>'; --%>
							<%session.removeAttribute("rptInvCode");%>
							<%session.removeAttribute("rptInvDate");%>
							<%session.removeAttribute("rptDcCode");%>
							var isOk=confirm("Do You Want to Generate Slip?");
							
							if(isOk){
								var mulltiInvCode= code.split(",");
							    for(var i=0;i<mulltiInvCode.length;i++)
							    { 
							    	code='';
		                            code=mulltiInvCode[i];
 							if(invoiceformat=="Format 1")
 								{
		 						window.open(getContextPath()+"/openRptProFormaInvoiceSlip.html?rptInvCode="+code,'_blank');
		 						window.open(getContextPath()+"/openRptProFormaInvoiceProductSlip.html?rptInvCode="+code+"&rptInvDate="+invDate,'_blank');
		 						window.open(getContextPath()+"/rptProFormaTradingInvoiceSlip.html?rptInvCode="+code+"&rptInvDate="+invDate,'_blank');
								}
							else{
								if(invoiceformat=="Format 2")
								{
								window.open(getContextPath()+"/rptProFormaInvoiceSlipFromat2.html?rptInvCode="+code+"&rptInvDate="+invDate,'_blank');
								window.open(getContextPath()+"/rptProFormaInvoiceSlipNonExcisableFromat2.html?rptInvCode="+code+"&rptInvDate="+invDate,'_blank');
								//window.open(getContextPath()+"/rptDeliveryChallanInvoiceSlip.html?strDocCode="+dccode,'_blank');
								}else if(invoiceformat=="Format 5")
								{
									
									window.open(getContextPath()+"/rptProFormaInvoiceSlipFormat5Report.html?rptInvCode="+code+"&rptInvDate="+invDate,'_blank');
								}
								else if(invoiceformat=="RetailNonGSTA4"){
								window.open(getContextPath()+"/openRptProFormaInvoiceRetailNonGSTReport.html?rptInvCode="+code,'_blank');
							    }else
							    	{
							    	window.open(getContextPath()+"/openRptProFormaInvoiceRetailReport.html?rptInvCode="+code,'_blank');
							    	}
							}
							}//End of for loop
							}//End of IF
// 							var isOk=confirm("Do You Want to Generate Product Detail Slip?");
// 							if(isOk){
// 								window.open(getContextPath()+"/openRptInvoiceProductSlip.html?rptInvCode="+code+"&rptInvDate="+invDate,'_blank');
// 									}
							<%}%><%}%>
							
							
							
							$('a#baseUrl').click(function() 
									{
										if($("#txtDCCode").val().trim() == "")
										{
											alert("Please Select Invoice Code");
											return false;
										}
										window.open('attachDoc.html?transName=frmInovice.jsp&formName=Invoice&code='+$("#txtDCCode").val(),"mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
									});				
		});
		
		
		
		
		
		
		function funLoadAMCList()
		{
			var fromDate=$("#txtFromDate").val();
			var toDate=$("#txtToDate").val();
			$("#tblTranList").empty();
			
			var searchUrl="";
	    	var searchUrl=getContextPath()+"/loadAMCReport.html?fromDate="+fromDate+"&toDate="+toDate;
			$.ajax({
			        type: "GET",
			        url: searchUrl,
				    dataType: "json",
				    async: true,

				    success: function(response)
				    {
				    	funFillTable(response);		       	

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
		
		function funFillTable(resp)
		{
			for(var i=0;i<resp.length;i++)
			{
			 	var data=resp[i];
				var table = document.getElementById("tblTranList");
			    var rowCount = table.rows.length;
			    var row = table.insertRow(rowCount);
			
			    
			    row.insertCell(0).innerHTML= "<input name=\"listAMCDtl["+(rowCount)+"].strselect\" id=\"cbSel."+(rowCount)+"\" size=\"3%\" type=\"checkbox\"  />";
			    row.insertCell(1).innerHTML= "<input  name=\"listAMCDtl["+(rowCount)+"].strCustomerName\"  readonly=\"readonly\" class=\"Box\" size=\"33%\" id=\"StrInvCode."+(rowCount)+"\" value='"+data.strCustomerName+"'>";
			    row.insertCell(2).innerHTML= "<input name=\"listAMCDtl["+(rowCount)+"].dblLicenceAmt\" readonly=\"readonly\" class=\"Box\"  style=\"text-align: center;\" size=\"34%\" id=\"DteInvDate."+(rowCount)+"\" value='"+data.dblLicenceAmt+"'>";
			    row.insertCell(3).innerHTML= "<input name=\"listAMCDtl["+(rowCount)+"].dteInstallation\" readonly=\"readonly\" style=\"text-align: center;\"  class=\"Box\" size=\"30%\" id=\"DteInvDate1."+(rowCount)+"\" value='"+data.dteInstallation+"'>";
			    row.insertCell(4).innerHTML= "<input name=\"listAMCDtl["+(rowCount)+"].dteExpiry\" id=\"dteExpiry."+(rowCount)+"\" readonly=\"readonly\" style=\"text-align: right;\" size=\"33%\" class=\"Box\" value="+data.dteExpiry+">"; 
			    row.insertCell(5).innerHTML= "<input name=\"listAMCDtl["+(rowCount)+"].dblAMCAmt\" readonly=\"readonly\" class=\"Box\"  style=\"text-align: right;\" size=\"16%\" id=\"dblAMCAmt."+(rowCount)+"\" value='"+data.dblAMCAmt+"'>";
			    row.insertCell(6).innerHTML= "<input type=\"hidden\" name=\"listAMCDtl["+(rowCount)+"].strCustCode\" readonly=\"readonly\"   size=\"0%\" id=\"strCustomerCode."+(rowCount)+"\" value='"+data.strCustomerCode+"'>";
			    
// 			    row.insertCell(0).innerHTML= "<input id=\"cbSel."+(rowCount)+"\" size=\"3%\" type=\"checkbox\"  />";
// 			    row.insertCell(1).innerHTML= "<input name=\"listAMCDtl["+(rowCount)+"].StrInvCode\" readonly=\"readonly\" class=\"Box\" size=\"13%\" id=\"StrInvCode."+(rowCount)+"\" value='"+data.strCustomerName+"'>";
// 			    row.insertCell(2).innerHTML= "<input name=\"listAMCDtl["+(rowCount)+"].DteInvDate\" readonly=\"readonly\" class=\"Box\"  style=\"text-align: right;\" size=\"34%\" id=\"DteInvDate."+(rowCount)+"\" value='"+data.dblLicenceAmt+"'>";
// 			    row.insertCell(3).innerHTML= "<input name=\"listAMCDtl["+(rowCount)+"].DteInvDate1\" readonly=\"readonly\" class=\"Box\" size=\"13%\" id=\"DteInvDate1."+(rowCount)+"\" value='"+data.dteInstallation+"'>";
// 			    row.insertCell(4).innerHTML= "<input name=\"listAMCDtl["+(rowCount)+"].DblSubTotalAmt\" id=\"DblSubTotalAmt."+(rowCount)+"\" readonly=\"readonly\" style=\"text-align: right;\" size=\"13%\" class=\"Box\" value="+data.dteExpiry+">"; 
// 			    row.insertCell(5).innerHTML= "<input name=\"listAMCDtl["+(rowCount)+"].strSerialNo\" readonly=\"readonly\" class=\"Box\"  style=\"text-align: right;\" size=\"6%\" id=\"strSerialNo."+(rowCount)+"\" value='"+data.dblAMCAmt+"'>";
		   }
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
		
		
		//Check Validation before Submit the data
		function funValidateFields() 
		{

			if(  $("#cmbSettlement").val() == null )
			{
		 		alert("Please Select Against");
				return false;
		 	}
			
			
			
		
		}
		
</script>
<body>
	<s:form name="AMCFlash" method="POST" action="saveAMCInvoice.html?saddr=${urlHits}" >
	<table class="transTable">
			<tr><th colspan="6"></th></tr>
				<tr>
				    <td><label id="lblFromDate">AMC From Date</label></td>
			        <td>
			            <s:input id="txtFromDate" name="fromDate" path="dteFromDate" cssClass="calenderTextBox"/>
			        	<s:errors path="dteFromDate"></s:errors>
			        </td>
			        <td><label id="lblToDate">AMC To Date</label></td>
			        <td>
			            <s:input id="txtToDate" name="toDate" path="dteToDate" cssClass="calenderTextBox"/>
			        	<s:errors path="dteToDate"></s:errors>
			        </td>
			        
					<td width="100px"><label>Settlement</label>
					<td><s:select id="cmbSettlement" path="strSettlementCode"
								items="${settlementList}" cssClass="BoxW124px" 
								onkeypress="funGetKeyCode(event,'Settlement')" onclick="funChangeCombo()" /></td>
				</tr>
				<tr>
					<td colspan="4"><input id="btnExecute" type="button" class="form_button1"   value="EXECUTE"/></td>
				</tr>
			</table>
			
		<div id="divDocList" class="dynamicTableContainer"
			style="height: 400px;">
			<table style="width: 100%; border: #0F0;   overflow-x: scroll; overflow-y: scroll;"
				class="transTablex col15-center">
				<tr bgcolor="#72BEFC">
					<td width="1%">Select<input type="checkbox" id="chkALL" onclick="funCheckUncheck()" /></td>
					<td width="8%">Customer Name</td>
					<td width="9%"> Licence Amount</td>
					<td width="9%">Insatlation Date</td>
					<td width="8%">Expiry Date</td>
					<td width="6%">AMC Amount</td>
				</tr>
			</table>
			<div
				style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 330px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
					<table id="tblTranList"
					style="width: 100%; border: #0F0;  overflow-x: scroll; overflow-y: scroll;"
					class="transTablex col15-center">
					<tbody>
					<col style="width: 3%">
					<col style="width: 8%">
					<!--  COl1   -->
					<col style="width: 9%">
					<!--  COl2   -->
					<col style="width: 9%">
					<!--  COl3   -->
					<col style="width: 8%">
					<!--  COl4   -->
					<col style="width: 6%">
					
					<col style="width: 0%">
				 
					
										
					</tbody>

				</table>
			</div>

		</div>
		<div align="center">
			<input type="submit" value="Submit" onclick="return funValidateFields();" class="form_button" /> &nbsp; &nbsp; &nbsp; 
			<input type="button" id="reset" name="reset" value="Reset" class="form_button" />
		</div>
		<div id="wait" style="display:none;width:60px;height:60px;border:0px solid black;position:absolute;top:60%;left:55%;padding:2px;">
				<img src="../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif" width="60px" height="60px" />
			</div>
	</s:form>
</body>
</html>