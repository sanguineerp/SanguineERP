<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Budget Master</title>

<script type="text/javascript">
	/*On form Load It Reset form :Priyanka 25 april 2017*/
	$(function() {

		/* var start = 1980;
		var endYear = new Date().getFullYear();
		var options = "";
		for (var year = endYear; year >= start; year--) {
			options += "<option>" + year + "</option>";
		}
		document.getElementById("cmbYear").innerHTML = options;

		var monthNames = [ "January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November",
				"December" ];

		var d = new Date();
		var endMonth = monthNames[d.getMonth()];
		var monthOption="";
		for (var i = 0; i <= monthNames.length; i++) {
			monthOption += "<option>" +monthNames[i]+ "</option>";
		}
		document.getElementById("cmbMonth").innerHTML = monthOption;
 */
 
		funFillTableData('');

	});

	function funOnClickProceed() {
		 	
		var t = document.getElementById("cmbYear");
		var year = t.options[t.selectedIndex].text;
		funFillTableData(year);
	}
	function funFillTableData(year) {

		funRemoveRows();
		var searchUrl = "";
		searchUrl = getContextPath() + "/fillBudgetTableData.html?year=" + year;

		$.ajax({
			type : "GET",
			url : searchUrl,
			dataType : "json",
			async : false,
			success : function(response) {
				if(response.length>0){
					funFillAddRow(response,year)	
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

	function funRemoveRows() {
		var table = document.getElementById("tblBudget");
		var rowCount = table.rows.length;
		while (rowCount > 0) {
			table.deleteRow(0);
			rowCount--;
		}
	}
	
	function funFillAddRow(data,year) {

		
		//$("#cmbMonth").val(month);
		$("#cmbYear").val(year);
		
	
		var table = document.getElementById("tblBudget");
		var rowCount = table.rows.length;

		var rowData;
		var row
		for (var cnt = 0; cnt < data.length; cnt++) {
			row = table.insertRow(rowCount);
			rowData = data[cnt];
// 			"<input type=\"text\"  readonly=\"readonly\" style=\"text-align: right;width:100%\" name=\"listclsProductionOrderDtlModel[" + (rowCount) + "].dblUnitPrice\" id=\"dblUnitPrice."	+ (rowCount)+ "\" value="+ dblUnitPrice+ " class=\"decimal-places-amt inputText-Auto\">";

			row.insertCell(0).innerHTML = "<input readonly=\"readonly\" class=\"Box \" size=\"35%\" name=\"listBudgetDtlModel["+ (rowCount)+ "].strAccCode\" id=\"strAccCode."+ (rowCount)+ "\" value='" + rowData[0] + "'>";
			row.insertCell(1).innerHTML = "<input readonly=\"readonly\" class=\"Box \" size=\"50%\" name=\"listBudgetDtlModel["+ (rowCount)+ "].strAccName\" id=\"strAccName."+ (rowCount)+ "\" value='" + rowData[1] + "'>";
			
			row.insertCell(2).innerHTML = "<input type=\"text\" style=\"text-align: right;width:25%\"size=\"15%\"name=\"listBudgetDtlModel["+ (rowCount)+ "].dblBudgetAmt\" id=\"dblBudgetAmt."+ (rowCount)+ "\" value='" + rowData[2] + "'>";
		
			row.insertCell(3).innerHTML = "<input type=\"hidden\" size=\"0%\"name=\"listBudgetDtlModel["+ (rowCount)+ "].intId\" id=\"intId."+ (rowCount)+ "\" value='" + rowData[3] + "'>";
			rowCount++;
		}
       
	}
	/**
	 * Reset function  
	 */
	function funResetFields() {
		location.reload(true);
	}
	
	function funValidate(){
		if($("#cmbYear").val()==null ||$("#cmbYear").val()=='' ){
			alert("Please Select Year");
			return false;	
		}
	}
</script>
</head>
<body>
	<div id="formHeading">
		<label>Budget</label>
	</div>

	<br />
	<br />
	<br />
	<br />
	<br />

	<s:form name="APGLBudget" method="POST"
		action="saveAPGLBudget.html?saddr=${urlHits}">

		<div>
			<table class="transTable">
				<tr>

					<td>Select Year</td>

					<td>
						<%--<s:input type="text" id="txtYear" class="calenderTextBox" path="Year" required="required" /> --%>

						<s:select id="cmbYear" path="strYear" items="${listFinancialYear}" class="BoxW124px"></s:select>
					</td>
					<td><input id="btnAdd" type="button" class="smallButton"
						value="Proceed"; onclick="funOnClickProceed()"></input></td>
				</tr>
			</table>
		</div>
		<br />
		<br />



		<div class="dynamicTableContainer"
			style="height: 30%; border: #0F0; text-align: center">
			<table
				style="width: 100%; border: #0F0; table-layout: fixed; text-align: center"
				class="transTablex col7-center">
				<tr>
					<td style="width: 12%;">Account Code</td>
					<td style="width: 28%;">Account Name</td>
					<td style="width: 20%;">Yearly Amount</td>
				 	<td style="width: 0%;"></td>

				</tr>
			</table>
			<div
				style="background-color: #C0E2FE; border: 1px solid #ccc; display: block; height: 350px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 99.80%;">
				<table id="tblBudget"
					style="width: 100%; height: 550px; border: #0F0; table-layout: fixed; overflow: scroll"
					class="transTablex col8-center">
					<tbody>
					<col style="width: 12%">
					<col style="width: 28%">
					<col style="width: 18%">
					<col style="width: 0%">

					</tbody>
				</table>
			</div>
		</div>
		<br />
		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button" onclick="return funValidate();" />
			<input type="reset" value="Reset" class="form_button"
				onclick="funResetFields()" />
		</p>
		
	</s:form>



</body>
</html>