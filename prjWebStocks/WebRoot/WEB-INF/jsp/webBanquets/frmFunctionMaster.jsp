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
	//Initialize tab Index or which tab is Active
	$(document).ready(function() 
	{		
		$(".tab_content").hide();
		$(".tab_content:first").show();

		$("ul.tabs li").click(function() {
			$("ul.tabs li").removeClass("active");
			$(this).addClass("active");
			$(".tab_content").hide();
			var activeTab = $(this).attr("data-state");
			$("#" + activeTab).fadeIn();
		});
			
		$(document).ajaxStart(function(){
		    $("#wait").css("display","block");
		});
		$(document).ajaxComplete(function(){
		   	$("#wait").css("display","none");
		});
		
		funRemAllRows("tblService");
		funLoadService();
	});
	

	function funResetFields()
	{
		$("#txtFunctionName").focus();
    }
	
	$(function() 
	{
		
		
	          /**
				* On Blur Event on TextField
				**/
				$('#txtFunctionCode').blur(function() 
				{
						var code = $('#txtFunctionCode').val();
						if (code.trim().length > 0 && code !="?" && code !="/")
						{				
							funSetData(code);							
						}
				});
				
				$('#txtFunctionName').blur(function () {
					 var strFunctionName=$('#txtFunctionName').val();
				      var st = strFunctionName.replace(/\s{2,}/g, ' ');
				      $('#txtFunctionName').val(st);
					});
				
	});
 


	function funSetData(code)
	{
		$("#txtFunctionCode").val(code);
		var searchurl=getContextPath()+ "/loadFunctionMasterData.html?functionCode=" + code;
		$.ajax({
			type : "GET",
			url : searchurl,
			dataType : "json",
			success : function(response)
			{ 
				if(response.strFunctionCode=='Invalid Code')
	        	{
	        		alert("Invalid Function Code");
	        		$("#txtFunctionCode").val('');
	        	
	        	}
				else
				{
					$("#txtFunctionCode").val(response.strFunctionCode);
	        		$("#txtFunctionName").val(response.strFunctionName);
	        		$("#txtFunctionName").focus();
	        		if (response.strOperationalYN == 'Y') {
						$("#txtOperational").prop('checked',
								true);
					} else
						$("#txtOperational").prop('checked',
								false);
	        		funLoadFunctionService(code);
	        		//funUpdateServiceData(response.listService);
	        		
				}

			},
			error : function(jqXHR, exception){
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

	});

	
	
   function funHelp(transactionName)
	{
		fieldName=transactionName;
		//window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
		 window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
	}
   
   /**
	 * Remove all product from grid
	 */
	
	function funRemAllRows(tableName)
	{
		var table = document.getElementById(tableName);
		var rowCount = table.rows.length;
		while(rowCount>0)
		{
			table.deleteRow(0);
			rowCount--;
		}
	}
   
   function funLoadService()
	{
	   
		$.ajax({
			type: "GET",
	        url: getContextPath()+"/loadServiceData.html",
	        dataType: "json",
	        success: function(response)
	        {
	        
	        		$.each(response, function(i,item)
	                {
	        			funfillServiceRow(response[i][0],response[i][1],'Y');
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
   
   function funfillServiceRow(ServiceCode,ServiceName,applicable)
	{
		
		var checked=true;
		var table = document.getElementById("tblService");
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);
	    var cnt=0;
	    var insertRowflg='Y';
	    row.insertCell(0).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"8%\" name=\"listService["+(rowCount)+"].strServiceCode\"  id=\"txtServiceCode."+(rowCount)+"\" value='"+ServiceCode+"' />";
	    row.insertCell(1).innerHTML= "<input  readonly=\"readonly\" class=\"Box\" size=\"27%\" name=\"listService["+(rowCount)+"].strServiceName\"  id=\"txtServiceName."+(rowCount)+"\" value='"+ServiceName+"'/>";
	    if(applicable=='Y'){
	    	row.insertCell(2).innerHTML= "<input id=\"chkApplicable."+(rowCount)+"\" style=\"align:right\" type=\"checkbox\" class=\"GCheckBoxClass\"  name=\"listService["+(rowCount)+"].strApplicable\" checked=\"checked\"  value='"+checked+"' />"; /*  checked=\"checked\"  */
	    }else{
	    	row.insertCell(2).innerHTML= "<input id=\"chkApplicable."+(rowCount)+"\" style=\"align:right\" type=\"checkbox\" class=\"GCheckBoxClass\"  name=\"listService["+(rowCount)+"].strApplicable\"  value=\"false\" />"; /*  checked=\"checked\"  */	
	    }
	     
	    
	}
   
   /* function funUpdateServiceData(listService){
		
		if(listService.length>0){
			funRemAllRows("tblService");
   		$.each(listService, function(i,item)
           {
   			funfillServiceRow(listService[i].strServiceCode,listService[i].strServiceName,listService[i].strApplicable);
			});
   	
		}
	}
   
    */
   
   function funLoadFunctionService(funCode)
	{
	  
		$.ajax({
			type: "GET",
	        url: getContextPath()+"/loadFunctionServiceData.html?functionCode="+funCode,
	        dataType: "json",
	        success: function(response)
	        {
	        	    $('#tblService tbody').empty();
	        		$.each(response, function(i,item)
	                {   
	        			funfillServiceRow(response[i][0],response[i][1],response[i][2]);
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
	
   
</script>

</head>
<body>

	<div id="formHeading">
	<label>Function Master</label>
	</div>

<br/>
<br/>
	<s:form name="FunctionMaster" method="POST" action="saveFunctionMaster.html">
     <div id="tab_container" style="height: 405px">
       <ul class="tabs">
					<li data-state="tab1" style="width: 6%; padding-left: 2%;margin-left: 10%; " class="active" >Function</li>
					<li data-state="tab2" style="width: 8%; padding-left: 1%">Service</li>
	    </ul>
		            <!-- Function Tab Start -->
	      <div id="tab1" class="tab_content" style="height: 400px">
					<br> 
					<br>
			   <table class="masterTable">
					<tr>
						<td><label>Function Code</label></td>
						<td><s:input type="text" id="txtFunctionCode" path="strFunctionCode" value="" cssClass="searchTextBox" ondblclick="funHelp('functionMaster')" /></td>
					</tr>
					
					<tr>
						<td><label>Function Name</label></td>
						<td><s:input type="text" id="txtFunctionName" path="strFunctionName" value="" cssClass="longTextBox" /></td>
					</tr>
					
					<tr>
					<td><label>Operational Y/N</label></td>
					<td ><s:checkbox path="strOperationalYN" id="txtOperational" value="Y" />
								<%-- <s:option value="Y">YES</s:option>
								<s:option value="N">NO</s:option></s:select></td> --%>
					</tr>
			    </table>
		        </div>
		        
	        <div id="tab2" class="tab_content" style="height: 400px">
				
				<table class="masterTable">
						<tr>
							<th style="border: 1px white solid;width: 10%"><label>Service Code</label></th>
							<th style="border: 1px white solid;width: 50%"><label>Service Name</label></th>
							<th style="border: 1px white solid;width: 10%"><label>Select</label></th>
						</tr>
					</table>
					
					<div style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 250px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 80%;">
						<table id="tblService" style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll" class="transTablex col5-center">
							<tbody>
								<col style="width: 5%"><!-- col1   -->
							    <col style="width: 30%"><!-- col2   -->
								<col style="width: 5%"><!-- col3   -->
							</tbody>
						</table>
					</div>
				
				</div>
 
       </div>
		

		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button" />
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
		</p>

	</s:form>
</body>
</html>
