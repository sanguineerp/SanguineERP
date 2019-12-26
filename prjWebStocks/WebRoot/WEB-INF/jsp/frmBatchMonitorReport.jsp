<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Batch Monitor</title>
</head>
<body>
<div id="formHeading">
		<label>Supplier Tax Wise GRN Report</label>
	</div>
		<s:form name="frmBatchMonitorReport" method="POST" action="" >
			<input type="hidden" value="${urlHits}" name="saddr">
            <br />
	   		<table class="transTable">
			  <!--  <tr><th colspan="10"></th></tr> -->
				
				<br>
				
				<tr>
				<td width="10%"><label >Batch Code</label></td>
				   <td width="60%"><s:input id="txtBatchCode"  path="strBatchCode"  readonly="true" ondblclick="funHelp('Batch')" cssClass="searchTextBox jQKeyboard form-control"/></td>
				 
				 <td colspan="7">
				 <input id="btnExecute" type="button" class="form_button1" value="EXECUTE"/></td>  
				 
				 <td colspan="9">						
						<input id="btnExport" type="button" value="EXPORT"  class="form_button1"/>
					</td>
				</tr>
				
			</table>
			<br>
			
			
			
		</s:form>
</body>
</html>