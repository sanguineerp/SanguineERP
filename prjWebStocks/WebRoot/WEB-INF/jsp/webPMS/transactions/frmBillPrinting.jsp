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
    
	
	function funValidateFields()
	{
		var flag=false;
		if($("#strBillNo").val().trim().length==0)
		{
			alert("Please Select Bill No.");
		}
		else
		{
			flag=true;
			var fromDate=$("#dteFromDate").val();
			var toDate=$("#dteToDate").val();
			
			var fd=fromDate.split("-")[0];
			var fm=fromDate.split("-")[1];
			var fy=fromDate.split("-")[2];
			
			var td=toDate.split("-")[0];
			var tm=toDate.split("-")[1];
			var ty=toDate.split("-")[2];
			
// 			$("#dteFromDate").val(fy+"-"+fm+"-"+fd);
// 			$("#dteToDate").val(ty+"-"+tm+"-"+td);
			
			var billNo=$("#strBillNo").val();
			var against=$("#cmbType").val();
			var checkedBill=$("#hidData").val();
			
			/***************** */
			var strSelectBill="";
			$('input[name="billData"]:checked').each(function() {
				 if(strSelectBill.length>0)
				 {
					 strSelectBill=strSelectBill +","+ this.value;
				 }
				 else
				 {
					 strSelectBill=this.value;
					// strSelectBill="'"+this.value+"'";
				 }
				 
			});
			if(strSelectBill=="")
			{
			 	alert("Please Select Folio ");
			 	return false;
			}
			$("#hidData").val(strSelectBill);
			
			if(against=='Bill')
			{
				window.open(getContextPath()+"/rptBillPrinting.html?fromDate="+fromDate+"&toDate="+toDate+"&billNo="+billNo+"&strSelectBill="+strSelectBill+"");
			}else{
			window.open(getContextPath()+"/rptBillPrintingForCheckIn.html?fromDate="+fromDate+"&toDate="+toDate+"&checkInNo="+billNo+"&against="+against+ "");
		    }	
			}
		
		return flag;
	}
	
	function funSetBillNo(billNo)
	{
		$("#strBillNo").val(billNo);
		funLoadBillData(billNo);
	}	
	
	function funSetBillNoForCheckIn(billNo)
	{
		$("#strBillNo").val(billNo);
		funLoadBillDataForCheckIn(billNo);
	}
	/**
	* Success Message After Saving Record
	**/
	 $(document).ready(function()
				{
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
		
		 var pmsDate='<%=session.getAttribute("PMSDate").toString()%>';
		  var dte=pmsDate.split("-");
		  $("#txtPMSDate").val(dte[2]+"-"+dte[1]+"-"+dte[0]);
		  
			var billNo='<%=session.getAttribute("BillNo").toString()%>';
			if(billNo!='')
			 {
				 $("#strBillNo").val(billNo);
				
				 funSetBillNo(billNo);
				 <%session.removeAttribute("BillNo");
				 %>
			 }
			 else
			 {
				
				 $("#strBillNo").val("");
				 <%session.removeAttribute("BillNo");
				 %>
				 
			 }
			 
		  

	});
	/**
		* Success Message After Saving Record
	**/
	
	/* set date values */
	
	/*
	function funSetDate(id,responseValue)
	{
		var id=id;
		var value=responseValue;
		var date=responseValue.split(" ")[0];
		
		var y=date.split("-")[0];
		var m=date.split("-")[1];
		var d=date.split("-")[2];
		
		$(id).val(d+"-"+m+"-"+y);		
	}*/
	
	//set date
	$(document).ready(function(){
		
		var pmsDate='<%=session.getAttribute("PMSDate").toString()%>';
		
		$("#dteFromDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#dteFromDate").datepicker('setDate', pmsDate);
		
		
		$("#dteToDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#dteToDate").datepicker('setDate', pmsDate);
		
		$("#chkBill").click(function ()
		{
		    $(".suppCheckBoxClass").prop('checked', $(this).prop('checked'));
		});
	
	});
	
	function funSetData(code)
	{
		switch(fieldName)
		{
			case "billNo":
				funSetBillNo(code);
				break;
				
			case "checkInForBill":
				funSetBillNoForCheckIn(code);
				break;
		}
	}

	function funHelp(transactionName)
	{
		fieldName=transactionName;
		var fromDate=$("#dteFromDate").val();
		var toDate=$("#dteToDate").val();
		var type=$("#cmbType").val();
		if(type=='Bill')
		{
		window.open("searchform.html?formname="+transactionName+"&searchText=","mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
		//window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
		}else{
			transactionName="checkInForBill";
		    fieldName=transactionName;
		    
		    var fDate=fromDate.split("-");
		    var tDate=toDate.split("-");
		    fromDate=fDate[2]+"-"+fDate[1]+"-"+fDate[0];
		    toDate=tDate[2]+"-"+tDate[1]+"-"+tDate[0];
		    
		    window.open("searchform.html?formname="+transactionName+"&fromDate="+fromDate+"&toDate="+toDate+"&searchText=","mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
		}
	}
	
	function funLoadBillData(code){
		var searchUrl=getContextPath()+ "/loadBillDetails.html?strBillNo=" + code;;
		$.ajax({
			type :"GET",
			url : searchUrl,
			dataType : "json",
			async: false,
			success: function(response){
				funRemoveRows();
				$.each(response, function(i,item)
				{
					funFillBillTable(response[i].strFolioNo,response[i].strDocNo,response[i].strMenuHead,response[i].dblIncomeHeadPrice,response[i].strRevenueCode);
				});
				
			},
			error : function(jqXHR, exception)
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

	function funLoadBillDataForCheckIn(code){
		var searchUrl=getContextPath()+ "/loadBillDetailsForCheckin.html?strBillNo=" + code;;
		$.ajax({
			type :"GET",
			url : searchUrl,
			dataType : "json",
			async: false,
			success: function(response){
				funRemoveRows();
				$.each(response, function(i,item)
				{
					funFillBillTable(response[i].strFolioNo,response[i].strDocNo,response[i].strMenuHead,response[i].dblIncomeHeadPrice,response[i].strRevenueCode);
				});
				
			},
			error : function(jqXHR, exception)
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
	
	function funFillBillTable(strFolioNo,docNo,incomeHead,incomeHeadPrice,strRevenueCode){
	 	var table = document.getElementById("tblBillDetails");
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);

	    row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box\" size=\"10%\" style=\"text-align: left;width:100%\" name=\"listVoidBilldtl["+(rowCount)+"].strFolioNo\" id=\"strFolioNo."+(rowCount)+"\" value='"+strFolioNo+"' />";
	    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\" size=\"10%\" style=\"text-align: left;width:100%\" name=\"listVoidBilldtl["+(rowCount)+"].strRevenueCode\" id=\"strRevenueCode."+(rowCount)+"\" value='"+strRevenueCode+"' />";
	   	row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box\" size=\"10%\" style=\"text-align: left;width:100%\" name=\"listVoidBilldtl["+(rowCount)+"].strMenuHead\" id=\"strMenuHead."+(rowCount)+"\" value='"+incomeHead+"' />";
	    row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box\" size=\"10%\" style=\"text-align: right;width:100%\" name=\"listVoidBilldtl["+(rowCount)+"].dblIncomeHeadPrice\" id=\"dblIncomeHeadPrice."+(rowCount)+"\" value='"+incomeHeadPrice+"' />";
	    row.insertCell(4).innerHTML= "<input id=\"cbToLocSel."+(rowCount)+"\" type=\"checkbox\" name=\"billData\" checked=\"checked\" size=\"2%\" style=\"text-align: center;width:100%\" class=\"suppCheckBoxClass\"  value='"+incomeHead+"' />";
	   // row.insertCell(0).innerHTML= "<input id=\"cbSuppSel."+(rowCount)+"\" name=\"Suppthemes\" type=\"checkbox\" class=\"SuppCheckBoxClass\"  checked=\"checked\" value='"+strSuppCode+"' />";
	}
	
	function funRemoveRows()
	{
		var table = document.getElementById("tblBillDetails");
		var rowCount = table.rows.length;
		while(rowCount>0)
		{
			table.deleteRow(0);
			rowCount--;
		}
	}
	
	
</script>

</head>
<body>

	<div id="formHeading">
	<label>Bill Printing</label>
	</div>

<br/>
<br/>

	<s:form name="BillPrinting" method="GET" action="">

		<table class="masterTable" style="width:90%;">
			<tr>
				<td><label>From Date</label></td>
				<td><s:input type="text" id="dteFromDate" path="dteFromDate" required="true" class="calenderTextBox" /></td>
				<td><label>To Date</label></td>
				<td><s:input type="text" id="dteToDate" path="dteToDate" required="true" class="calenderTextBox" /></td>				
			</tr>
			
			<tr>
				<td><label>Bill No.</label></td>
				<td ><s:input id="strBillNo" path="strBillNo"  cssClass="searchTextBox" ondblclick="funHelp('billNo')"/></td>													
				<td> Against</td>
				<td><select id="cmbType" class="BoxW124px">
				   		<option value="Bill">Bill</option>
				   		<option value="CheckIn">CheckIn</option></select>
				</td>
			</tr>
		</table>
		
		<div class="dynamicTableContainer" style="width:90%;height: 300px;margin-top:10px;">
				<table style="height: 28px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">
					<tr bgcolor="#72BEFC">
					
						<td style="width:8%;">Folio</td>
						<td style="width:7.5%;">Revenue Code</td>
						<td style="width:14%;">Income Head</td>
						<td style="width:9%; text-align: center;">Total</td>
						<td style="width:3%;">Select <input type="checkbox" id="chkBill" /></td>
					</tr>
				</table>
		
			<div style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 250px; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
				<table id="tblBillDetails"
					style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
					class="transTablex col8-center">
					<tbody>
						<col style="width: 8.5%;">
						<col style="width: 8%;">
						<col style="width: 15%;">
						<col style="width: 9%;">
						<col style="width: 3%;">
					</tbody>
				</table>
			</div>
		</div>
					
		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button" onclick="return funValidateFields()" />
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>
		<s:input type="hidden" id="hidData" path="strSelectBill" ></s:input>				
	</s:form>
</body>
</html>
