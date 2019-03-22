<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <!-- charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> -->
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>

<script type="text/javascript">
$(function() {

	
		$("#txtFromDate" ).datepicker({ dateFormat: 'dd-mm-yy' });
		$("#txtFromDate" ).datepicker('setDate', 'today');
		$("#txtToDate" ).datepicker({ dateFormat: 'dd-mm-yy' });
		$("#txtToDate" ).datepicker('setDate', 'today');
});


//Execute button 

$(document).ready(function() {
	
	$("#btnExecute").click(function(event)
			{

if($("#cmbType").val()=="Detail")
	{

		funCalculatePettyCashFlashDetail();
	}
else
	{
	funCalculatePettyCashFlasSummary();
	}
	
	});
			

});

				
function funCalculatePettyCashFlashDetail()
{
	var fromDat=$("#txtFromDate").val();
	var toDat=$("#txtToDate").val();
	var searchUrl=getContextPath()+"/rptPettyCashFlashDetail.html?fromDat="+fromDat+"&toDat="+toDat;

}
	
	
function funCalculatePettyCashFlasSummary()
{
	var fromDat=$("#txtFromDate").val();
	var toDat=$("#txtToDate").val();
	var searchUrl=getContextPath()+"/rptPettyCashFlashummary.html?fromDat="+fromDat+"&toDat="+toDat;

	
	
	
	
	}	

		
</script>
<body>

<div id="formHeading">
		<label>Petty Cash Flash</label>
</div>
<br />
<br />

<s:form name="Petty Cash Flash" method="GET" action="" >
		<div>
			<table class="transTable">
			    <tr>
					<td width="10%"><label>From Date </label></td>
					<td width="10%" colspan="1"><s:input id="txtFromDate" path="dteFromDate" required="true" readonly="readonly" cssClass="calenderTextBox"/></td>
					<td width="10%"><label>To Date </label></td>
					<td width="10%"><s:input id="txtToDate" path="dteToDate" required="true" readonly="readonly" cssClass="calenderTextBox"/>
					</td>
					
					<td><label>Type</label> </td>
				<td><s:select id="cmbType" cssClass="combo1" cssStyle="width:125px;height:20px;overflow:scroll" path="">
					<option value="Detail">Detail</option>
					<option value="Summary">Summary</option>
				
					</s:select></td>
				</tr>
				
				

			</table>
			
			<br />
			<br />
			<p align="center">
				<input type="button" id="btnExecute" value="Excecute"  class="form_button" />
				
				
			</p>
			
			
			
			<br/>
			<br/>
			
				
			<dl id="Searchresult" style="width: 95%; margin-left: 26px; overflow:auto;"></dl>
		<div id="Pagination" class="pagination" style="width: 80%;margin-left: 26px;">
		<%-- <s:input type="hidden" id="hidSubCodes" path="strCatCode"></s:input> --%>	
		</div>
		
			<div id="wait" style="display:none;width:60px;height:60px;border:0px solid black;position:absolute;top:60%;left:55%;padding:2px;">
				<img src="../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif" width="60px" height="60px" />
			</div>
	</s:form>




</body>
</html>