<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
  	<link rel="stylesheet" type="text/css" href="default.css" />
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Web Stocks</title>
    <script type="text/javascript">
    	var fieldName;
    	/**
		 * Reset Textfield
		 */
    	function funResetFields()
    	{
    		$("#txtProdCode").val('');
    	}
    	/**
		 * Open help windows
		 */
    	function funHelp(transactionName)
		{
			fieldName=transactionName;
		//	 window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
			 window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
	    }
    	/**
		 * Set Data after selecting form Help windows
		 */
		function funSetData(code)
		{
			$("#txtProdCode").val(code);
		}
    </script>
  </head>
  
	<body >
	<div id="formHeading">
		<label>Recipes List</label>
	</div>
	<br />
	<br />
		<s:form name="frmRecipesList" method="GET" action="rptRecipesList.html" target="_blank">
			<table class="masterTable">
	<tr><th colspan="2"></th></tr>
				<tr>
					<td width="150px"><label>Product Code</label></td>
					<td><s:input  id="txtProdCode" path="strDocCode" ondblclick="funHelp('productProducedslip')" cssClass="searchTextBox" cssStyle="width:150px;background-position: 136px 4px;"/></td>
				</tr>
				
				<tr>
					<td><label>Report Type</label></td>
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
						<td width="10%">Rate PickUp From</td>
						<td>
							<s:select  id="cmbRatePickUpFrom" path="strShowBOM" class="BoxW48px" style="width:130px" >
							<option selected="selected" value="Product Master">Product Master</option>
							<option value="Last Purchase Rate">Last Purchase Rate</option>
								
							</s:select>
						</td>
						</tr>
				<tr>
				<td colspan="2"></td>
					<!-- <td><input type="submit" value="Submit" /></td>
					<td><input type="reset" value="Reset" onclick="funResetFields()"/></td>	 -->				
				</tr>
			</table>
			<br>
			<p align="center">
				<input type="submit" value="Submit"  class="form_button"/>
				 <input type="button" value="Reset" class="form_button"  onclick="funResetFields()"/>
			</p>
			
		</s:form>
	</body>
</html>