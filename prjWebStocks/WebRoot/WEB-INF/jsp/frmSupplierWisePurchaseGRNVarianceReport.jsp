 <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<script type="text/javascript">

$(function() 
		{
			var startDate="${startDate}";
			var arr = startDate.split("/");
			Date1=arr[0]+"-"+arr[1]+"-"+arr[2];
			$( "#txtFromDate" ).datepicker({ dateFormat: 'dd-mm-yy' });
			$( "#txtToDate" ).datepicker({ dateFormat: 'dd-mm-yy' });
			
			$("#txtFromDate" ).datepicker('setDate', Date1);
			$("#txtToDate" ).datepicker('setDate', 'today');
			
			
			$("#btnSubmit").click(function( event )
			{
				var fromDate=$("#txtFromDate").val();
				var toDate=$("#txtToDate").val();
				var suppCode=$("#txtPartyCode").val();
				
				if(suppCode=='')
				{
					$("#txtPartyCode").val("All");
					return;
				}
				
				if(fromDate=='')
				{
					alert("Please Enter From Date");
					return;
				}
				if(toDate=='')
				{
					alert("Please Enter To Date");
					return;
				}
				
			});
			 funSetAllSupplier();
			 funGetSubGroupData();
					
		});
		
//Select Location When Clicking Select All Check Box
$(document).ready(function () 
			{
									
				$("#chkSuppALL").click(function () {
				    $(".SuppCheckBoxClass").prop('checked', $(this).prop('checked'));
				});
				
				$("#chkSGALL").click(function () {
				    $(".SGCheckBoxClass").prop('checked', $(this).prop('checked'));
				});
				
			});

function funHelp(transactionName)
{
	fieldName=transactionName;
//	window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
	window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
}

function funSetData(code)
{
	switch (fieldName) 
	{
	case 'suppcode':
    	funSetSupplier(code);
        break;
	}
	
}

function funSetSupplier(code)
{
	
	gurl=getContextPath()+"/loadSupplierMasterData.html?partyCode=";
	$.ajax({
        type: "GET",
        url: gurl+code,
        dataType: "json",
        success: function(response)
        {
        	
        		if('Invalid Code' == response.strPCode){
        			alert("Invalid Supplier Code");
        			$("#txtPartyCode").val('');
        			$("#lblSuppName").focus();
        			
        		}else{
        			$("#txtPartyCode").val(response.strPCode);
					$("#lblSuppName").text(response.strPName);
	
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


//Get and set Supplier  Data 
function funSetAllSupplier() {
		var searchUrl = "";
		searchUrl = getContextPath()+ "/loadAllSupplier.html";
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
				if (response.strSuppCode == 'Invalid Code') {
					alert("Invalid Supplier Code");
					
				} else
				{
					$.each(response, function(i,item)
					 		{
						funfillSuppGrid(response[i].strPCode,response[i].strPName);
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
function funfillSuppGrid(strSuppCode,strSuppName)
{
	
	 	var table = document.getElementById("tblSupp");
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);
	    
	    row.insertCell(0).innerHTML= "<input id=\"cbSuppSel."+(rowCount)+"\" name=\"Suppthemes\" type=\"checkbox\" class=\"SuppCheckBoxClass\"  checked=\"checked\" value='"+strSuppCode+"' />";
	    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"strSuppCode."+(rowCount)+"\" value='"+strSuppCode+"' >";
	    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \" size=\"50%\" id=\"strSName."+(rowCount)+"\" value='"+strSuppName+"' >";
}





//Submit Data after clicking Submit Button with validation 
function btnSubmit_Onclick()
 {
	 
	 
	
	 var strSuppCode="";
	 
	 $('input[name="Suppthemes"]:checked').each(function() {
		 if(strSuppCode.length>0)
			 {
			 strSuppCode=strSuppCode+","+this.value;
			 }
			 else
			 {
				 strSuppCode=this.value;
			 }
		 
		});
	 if(strSuppCode=="")
	 {
	 	alert("Please Select To Supplier");
	 	return false;
	 }
	 $("#hidSuppCodes").val(strSuppCode);

	    	document.forms["SupplierwiseGRNReport"].submit();
	
 }
</script>
<body>
	<div id="formHeading">
		<label> Supplier wise Category wise GRN Report</label>
	</div>
		<s:form name="SupplierwiseGRNReport" method="GET" action="rptSupplierProdWisePurchAndGRNReport.html" target="_blank">
            <br />
	   		<table class="transTable">
			    <tr>
					<td width="10%"><label>From Date :</label></td>
					<td width="10%" colspan="1"><s:input id="txtFromDate" path="dteFromDate" required="true" readonly="readonly" cssClass="calenderTextBox"/></td>
					<td width="10%"><label>To Date :</label></td>
					<td><s:input id="txtToDate" path="dteToDate" required="true" readonly="readonly" cssClass="calenderTextBox"/>
					</td>	
					<td></td>
				</tr>
				
				<br>
				
				<tr>
<!-- 				<td width="140px"> -->
<!-- 				        		<label>Supplier Code </label> -->
<!-- 				        	</td> -->
<!-- 				        	<td  width="15%"> -->
<%-- 				        		<s:input id="txtPartyCode" name="txtPartyCode"  path="strPCode" ondblclick="funHelp('suppcode')"   cssClass="searchTextBox"/> --%>
<!-- 				       	</td><td><label id=lblSuppName>ALL Supplier</label> </td> -->
				
		
			   
					<td width="10%"><label>Report Type :</label></td>
					<td>
						<s:select id="cmbDocType" path="strDocType" cssClass="BoxW124px">
				    		<s:option value="PDF">PDF</s:option>
				    		<s:option value="XLS">EXCEL</s:option>
				    		<s:option value="HTML">HTML</s:option>
				    		<s:option value="CSV">CSV</s:option>
				    	</s:select>
					</td>
				</tr>
					<tr>
		
			
			<td colspan="2">Supplier&nbsp;&nbsp;&nbsp;<input style="width: 35%; background-position: 150px 2px;" type="text" id="txtSuppCode" 
			 Class="searchTextBox" placeholder="Type to search"></input>
			<label id="lblSuppName"></label></td>
			
			<td colspan="2">Category&nbsp;&nbsp;&nbsp;<input style="width: 35%; background-position: 150px 2px;" type="text" id="txtSuppCode" 
			 Class="searchTextBox" placeholder="Type to search"></input>
			<label id="lblCategory"></label></td>
			
			</tr>
			
			
			<tr>
			
			
					<td colspan="2">
						<div
							style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 150px; overflow-x: hidden; overflow-y: scroll;">

							<table id="" class="masterTable"
								style="width: 100%; border-collapse: separate;">
								<tbody>
									<tr bgcolor="#72BEFC">
										<td width="15%"><input type="checkbox" checked="checked" 
										id="chkSuppALL"/>Select</td>
										<td width="25%">Supplier Code</td>
										<td width="65%"> Supplier Name</td>

									</tr>
								</tbody>
							</table>
							<table id="tblSupp" class="masterTable"
								style="width: 100%; border-collapse: separate;">

								<tr bgcolor="#72BEFC">
									

								</tr>
							</table>
						</div>
				</td>
				
					<td  colspan="2">
<!-- 						<div -->
<!-- 							style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 150px; overflow-x: hidden; overflow-y: scroll;"> -->

<!-- 							<table id="" class="masterTable" -->
<!-- 								style="width: 100%; border-collapse: separate;"> -->
<!-- 								<tbody> -->
<!-- 									<tr bgcolor="#72BEFC"> -->
<!-- 										<td width="15%"><input type="checkbox" id="chkSGALL" -->
<!-- 											checked="checked" onclick="funCheckUncheckSubGroup()" />Select</td> -->
<!-- 										<td width="25%">Sub Group Code</td> -->
<!-- 										<td width="65%">Sub Group Name</td> -->

<!-- 									</tr> -->
<!-- 								</tbody> -->
<!-- 							</table> -->
<!-- 							<table id="tblSubGroup" class="masterTable" -->
<!-- 								style="width: 100%; border-collapse: separate;"> -->
<!-- 								<tbody> -->
<!-- 									<tr bgcolor="#72BEFC"> -->
<!-- 										<td width="15%"></td> -->
<!-- 										<td width="25%"></td> -->
<!-- 										<td width="65%"></td> -->

<!-- 									</tr> -->
<!-- 								</tbody> -->
<!-- 							</table> -->
							
<!-- 						</div> -->
				</td>
			</tr>
			</table>
			<br>
			<p align="center">
				 <input type="submit" value="Submit"  class="form_button"  onclick="btnSubmit_Onclick()"  id="btnSubmit" />
				 <input type="button" value="Reset" class="form_button" onclick="funResetFields()"/>			     
			</p>
		
			<div id="wait"
			style="display: none; width: 60px; height: 60px; border: 0px solid black; position: absolute; top: 60%; left: 55%; padding: 2px;">
			<img
				src="../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif"
				width="60px" height="60px" />
				
			<s:input type="hidden" id="hidSuppCodes" path="strPCode"></s:input>		
			<s:input type="hidden" id="hidSubCodes" path="strCatCode"></s:input>	
		</div>
		</s:form>
	</body>
</html>