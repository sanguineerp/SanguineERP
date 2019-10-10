<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
 <script type="text/javascript" src="<spring:url value="/resources/js/jQuery.js"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/jquery-ui.min.js"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/validations.js"/>"></script>
<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/design.css"/>" /> 
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Excel Import Export</title>
<script type="text/javascript">
//Press ESC button to Close Form
	window.onkeyup = function (event) {
		if (event.keyCode == 27) {
			window.close ();
		}
	}
</script>
<script type="text/javascript">
 var transactionformName="";
 var LocCode="";
 var dtPhydate='';
 var supplierCode='';
 
    //Get Project Path
	function getContextPath() 
	{
		return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
	}

    //Check From to where Link Click to Open
	function funExport()
	{
		switch(transactionformName)
		{
			case "frmOpeningStock":
				window.location.href=getContextPath()+"/frmOpeningStkExcelExport.html?strLocCode="+LocCode;
				break;
			case "frmPhysicalStkPosting":
				
				var gCode=$("#txtGroupCode").val();
				var sgCode=$("#txtSubGroupCode").val();
				var prodWiseStock=$("#cmbProdStock").val();
				
					window.location.href=getContextPath()+"/PhyStkPstExcelExport.html?locCode="+LocCode+"&gCode="+gCode+"&sgCode="+sgCode+"&strTransDate="+dtPhydate+"&strUOM=RecUOM&prodWiseStock="+prodWiseStock;	
			
//  			window.location.href=getContextPath()+"/PhyStkPstExcelExport.html?locCode="+LocCode+"&gCode="+gCode+"&sgCode="+sgCode;
				
				break;
			case "frmLocationMaster":
				window.location.href=getContextPath()+"/LocationMasterReorderLevelExcelExport.html?locCode="+LocCode;
				break;
			case "frmPOSSalesSheet":
				window.location.href=getContextPath()+"/POSSalesExcelExport.html?locCode="+LocCode;
				break;
				
			case "frmMaterialReq":
				var gCode=$("#txtGroupCode").val();
				var sgCode=$("#txtSubGroupCode").val();
				var prodWiseStock=$("#cmbProdStock").val();
				
					window.location.href=getContextPath()+"/MaterialReqExport.html?locCode="+LocCode+"&gCode="+gCode+"&sgCode="+sgCode+"&strTransDate="+dtPhydate+"&strUOM=RecUOM&prodWiseStock="+prodWiseStock;	
				break;
				
			case "frmPurchaseIndent":
				var gCode=$("#txtGroupCode").val();
				var sgCode=$("#txtSubGroupCode").val();
				var prodWiseStock=$("#cmbProdStock").val();
				
					window.location.href=getContextPath()+"/PurchaseIndentExport.html?locCode="+LocCode+"&gCode="+gCode+"&sgCode="+sgCode+"&strTransDate="+dtPhydate+"&strUOM=RecUOM&prodWiseStock="+prodWiseStock;	
				break;
				
			case "frmPurchaseOrder":
				var gCode=$("#txtGroupCode").val();
				var sgCode=$("#txtSubGroupCode").val();
				var prodWiseStock=$("#cmbProdStock").val();
				
					window.location.href=getContextPath()+"/PurchaseOrderExport.html?suppCode="+supplierCode+"&gCode="+gCode+"&sgCode="+sgCode+"&strTransDate="+dtPhydate+"&strUOM=RecUOM&prodWiseStock="+prodWiseStock;	
				break;
				
				
			case "frmMIS":
				var gCode=$("#txtGroupCode").val();
				var sgCode=$("#txtSubGroupCode").val();
				var prodWiseStock=$("#cmbProdStock").val();
				
					window.location.href=getContextPath()+"/MISExport.html?locCode="+LocCode+"&gCode="+gCode+"&sgCode="+sgCode+"&strTransDate="+dtPhydate+"&strUOM=RecUOM&prodWiseStock="+prodWiseStock;	
				break;
				
			case "frmGuestMaster" :
				window.location.href=getContextPath()+"/GuestMasterExport.html";	
				break;
				
			case "frmRoomMaster" :
				window.location.href=getContextPath()+"/RoomMasterImport.html";	
				break;
				
			/* case "frmCheckInCheckOutList" :
				window.location.href=getContextPath()+"/CheckInCheckOutList.html";	
				
				break;
			 */
		}
	}

    //Check File is Excel or another format
	function funValidateFile() 
	{	
		var value=$("#File").val();
		var Extension=value.split(".");
		var ext=Extension[1];
		if(ext=="xls" || ext =="xlsx" )
			{
			 return true;
			}
		else
		{
			alert("Invalid File");
			return false;
			
		}
	}

    //After Submit Button
	function funSubmit()
	{
		if(funValidateFile())
			{
				var jForm = new FormData();    
			    jForm.append("file", $('#File').get(0).files[0]);
			    
			  
			   if(transactionformName=='frmPhysicalStkPosting')
				{
				   var prodStock= $("#cmbProdStock").val();
				   searchUrl=getContextPath()+"/ExcelExportImport.html?formname="+transactionformName+"&prodStock="+prodStock;
				}else{
					searchUrl=getContextPath()+"/ExcelExportImport.html?formname="+transactionformName;
				}
			    	
		        $.ajax({
		           // url : $("#uploadExcel").attr('action'),
		            url : searchUrl,
		            type: "POST",
	                data: jForm,
	                mimeType: "multipart/form-data",
	                contentType: false,
	                cache: false,
	                processData: false,
	                dataType: "json",
		            success : function(response) 
		            {
		            	if(response[0]=="Invalid Excel File")
		            		{
		            			alert(response[1]);
		            		}
		            	else
		            		{
								window.returnValue = response;
								window.close();
		            		}
		            },
		            error: function(jqXHR, exception)
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
	}
    //Get Transaction from to which Link is Click and Location Code
	function funOnLoad()
	{
		
		transactionformName='<%=request.getParameter("formname") %>'
		
		
		if(transactionformName=="frmPhysicalStkPosting")
			{
			dtPhydate='<%=request.getParameter("dtPhydate") %>'
// 			document.all["divFilter"].style.display = 'block';
			document.getElementById("divFilter").style.display = 'block';
				LocCode='<%=request.getParameter("strLocCode") %>'
			}
		if(transactionformName=="frmOpeningStock")
		{
			LocCode='<%=request.getParameter("strLocCode") %>'
		}
		if(transactionformName=="frmLocationMaster")
			{
				LocCode='<%=request.getParameter("locCode") %>'
			}
		if(transactionformName=="frmPurchaseIndent")
		{
			LocCode='<%=request.getParameter("strLocCode") %>'
		}
		
		if(transactionformName=="frmMaterialReq")
		{
			LocCode='<%=request.getParameter("strLocCode") %>'
		}
		if(transactionformName=="frmPurchaseOrder")
		{
			supplierCode='<%=request.getParameter("strSuppCode") %>'
		}
		
		if(transactionformName=="frmMIS")
		{
			LocCode='<%=request.getParameter("strLocCode") %>'
		}
		
		
	}
	
	function funHelp(transactionName)
	{
		fieldName=transactionName;
	//	 window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:1000px;dialogLeft:200px;")
		 window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:1000px;dialogLeft:200px;")
    }
	
	function funSetData(code)
	{
		switch (fieldName) 
		{
		    case 'locationmaster':
		    	funSetLocation(code);
		        break;
		    case 'subgroup':
		    	funSetSubGroup(code);
		        break;
		   
		   case 'group':
		    	funSetGroup(code);
		        break;
		}
	}
	
	/**
	* Get and set Location Data Passing value(Location Code)
    **/
	function funSetLocation(code)
	{
		var searchUrl="";
		searchUrl=getContextPath()+"/loadLocationMasterData.html?locCode="+code;			
		$.ajax({
		        type: "GET",
		        url: searchUrl,
			    dataType: "json",
			    success: function(response)
			    {
			    	if(response.strLocCode=='Invalid Code')
			       	{
			       		alert("Invalid Location Code");
			       		$("#txtLocCode").val('');
			       		$("#lblLocName").text("");
			       		$("#txtLocCode").focus();
			       	}
			       	else
			       	{
			    	$("#txtLocCode").val(response.strLocCode);
	        		$("#lblLocName").text(response.strLocName);	
	        		
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
	
	function funSetSubGroup(code)
	{
		var searchUrl="";
		
		searchUrl=getContextPath()+"/loadSubGroupMasterData.html?subGroupCode="+code;
		
		$.ajax({
		        type: "GET",
		        url: searchUrl,
			    dataType: "json",
			    success: function(response)
			    {
			    	if('Invalid Code' == response.strSGCode){
			    		alert('Invalid Code');
			    		$("#txtSubGroupCode").val('');
			    		$("#txtSubGroupCode").focus();
			    	}else{
			    	$("#txtSubGroupCode").val(code);
			    	$("#txtSubGroupName").text(response.strSGName);
			    	
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
	
	
	function funSetGroup(code)
	{
			 $.ajax({
				        type: "GET",
				        url: getContextPath()+"/loadGroupMasterData.html?groupCode="+code,
				        dataType: "json",
				        success: function(response)
				        {
				        	if(response.strGCode=='Invalid Code')
				        	{
				        		alert("Invalid Group Code");
				        		$("#txtGroupCode").val('');
				        		$("#lblgroupname").text('');
				        		$("#txtGroupCode").focus();
				        	}
				        	else
				        	{
				        		$("#txtGroupCode").val(response.strGCode);
					        	$("#lblgroupname").text(response.strGName);						    
					        	$("#txtGroupName").focus();
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
</script>
</head>
<body onload="funOnLoad();">

<s:form name="uploadExcel" id="uploadExcel" method="POST" action="ExcelExportImport.html" enctype="multipart/form-data" >
<br>
<br>
	<table>
	   <tbody>
		    <tr>
			    <td class="content" bgcolor="#a6d1f6">Export Excel File</td>
			    <td><input type="button" id="btnExport" value="Export" class="form_button1" onclick="funExport();"/></td>
		    </tr>
		    <tr>
		    	<td><input type="file" id="File"  Width="50%" accept="application/vnd.ms-excel"  ></input></td>    
		    </tr>
		       </tbody>
	</table>
  <div id="divFilter" style="display:none;">
  <table>
	   <tbody>
	   
	   <tr>
	   <td>Show Stock Wise Product</td>
				<td colspan="3"><select id="cmbProdStock" Class="BoxW124px" >
						<option value="Yes">Yes</option>
						<option selected="selected" value="No">No</option>
						
				</select></td>
<!-- 	   </tr> -->
<!-- 		    <tr> -->
<!-- 			   <td width="120px"><label>Location Code </label></td> -->
<!-- 			   <td><input id="txtLocCode" name="txtLocCode"  ondblclick="funHelp('locationmaster')"  cssClass="searchTextBox"/></td> -->
<!-- 				<td colspan="2"> -->
<!-- 					<label id="lblLocName">All</label> -->
<!-- 			  </td> -->
<!-- 			</tr> -->
				 <tr> 
			    <td><label path="strGCode" >Group Code</label></td>
		        <td><input type="text" id="txtGroupCode" name="txtGroupCode" autocomplete="off"    ondblclick="funHelp('group')" required="true" cssClass="searchTextBox" /></td><td><label id="lblgroupname">All</label></td>
				
			</tr>
			
				<tr>
				        <td><label >Sub-Group Code</label></td>
				        <td ><input id="txtSubGroupCode" name="subGroupCode"  ondblclick="funHelp('subgroup')" autocomplete="off" cssClass="searchTextBox"/></td>
				       <td><label id="txtSubGroupName">All</label></td>
				        <td></td>
				       
			   		</tr>
			   		
			   	  	
		  
	    </tbody>
	</table>
	</div>
	<br>
    <p align="center">
			<input id="btnSubmit" type="button" class="form_button" value="Submit" onclick="return funSubmit();"/>
			&nbsp; &nbsp; &nbsp;
			 <input id="btnReset" type="reset" value="Reset" class="form_button" onclick="funResetField()" />
	</p>
</s:form>
</body>
</html>