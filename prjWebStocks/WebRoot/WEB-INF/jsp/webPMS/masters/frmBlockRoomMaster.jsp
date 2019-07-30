<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
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
		
	var pmsDate='<%=session.getAttribute("PMSDate").toString()%>';
		
		$("#dteValidFrom").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#dteValidFrom").datepicker('setDate', pmsDate);

		$("#dteValidTo").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#dteValidTo").datepicker('setDate', pmsDate);
	});
	
	function funSetRoomMasterData(roomCode)
	{
		var searchUrl=getContextPath()+"/loadRoomMasterData.html?roomCode="+roomCode;
		$.ajax({
			
			url:searchUrl,
			type :"GET",
			dataType: "json",
	        success: function(response)
	        {
	        	if(response.strRoomCode=='Invalid Code')
	        	{
	        		alert("Invalid Room Code");
	        		$("#txtRoomCode").val('');
	        	}
	        	else
	        	{
	        		$("#txtRoomCode").val(response.strRoomCode);
	        		$("#txtRoomDesc").val(response.strRoomDesc);
	        		$("#cmbRoomType").val(response.strRoomTypeCode);
	        		funSetRoomType(response.strRoomTypeCode);
	        		$("#txtFloorCode").val(response.strFloorCode);
	        		$("#txtBedType").val(response.strBedType);
	        		$("#txtFurniture").val(response.strFurniture);
	        		$("#txtExtraBed").val(response.strExtraBed);
	        		$("#txtUpholstery").val(response.strUpholstery);
	        		$("#txtLocation").val(response.strLocation);
	        		$("#txtColourScheme").val(response.strColourScheme);
	        		$("#txtPolishType").val(response.strPolishType);
	        		$("#txtGuestAmenities").val(response.strGuestAmenities);
	        		$("#txtInterConnectRooms").val(response.strInterConnectRooms);
	        		$("#txtBathTypeCode").val(response.strBathTypeCode);
	        		
	        		if(response.strAccountCode!="" && response.strAccountCode!="NA" )
	        			{
	        				funSetAccountCode(response.strAccountCode);
	        			}
	        		
	        		
	        		if(response.strProvisionForSmokingYN=="Y")
	        		{
	        			$("#rdbProvisionForSmokingYN").prop('checked',true);
	        		}
	        		else
	        		{
	        			$("#rdbProvisionForSmokingYN").prop('checked',false);
	        		}
	        		if(response.strDeactiveYN=="Y")
	        		{
	        			$("#rdbDeactiveYN").prop('checked',true);
	        		}
	        		else
	        		{
	        			$("#rdbDeactiveYN").prop('checked',false);
	        		}
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
	
	
	
	function funSetRoomType(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/loadRoomTypeMasterData.html?roomCode=" + code,
			dataType : "json",
			success : function(response){ 
				if(response.strRoomTypeCode=='Invalid Code')
	        	{
	        		alert("Invalid Room Type");
	        		$("#txtRoomType").val('');
	        	}
	        	else
	        	{
	        		$("#txtRoomType").val(response.strRoomTypeCode);
	        		$("#lblRoomType").text(response.strRoomTypeDesc);
	        	}
			},
			error : function(e){
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
	linked account code
	**/
	function funSetAccountCode(code){

		$.ajax({
			type : "GET",
			url : getContextPath()+ "/getAccountMasterDtl.html?accountCode=" + code,
			dataType : "json",			
			success : function(response)
			{
				if(response.strAccountCode=='Invalid Code')
	        	{
	        		alert("Invalid Account Code");
	        		$("#txtAccountCode").val('');
	        	}
	        	else
	        	{
	        		$("#txtAccountCode").val(response.strAccountCode);
	        		$("#txtAccountName").val(response.strAccountName);
	        	}
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
	
	
	
	/**
	* Get and Set data from help file and load data Based on Selection Passing Value(Extra Bed Code)
	**/
	
	function funSetExtraBed(code)
	{
		$("#txtExtraBed").val(code);
		var searchurl=getContextPath()+"/loadExtraBedMasterData.html?extraBedCode="+code;
		$.ajax({
			type: "GET",
			url: searchurl,
			dataType: "json",
			success: function(response)
			{
				if(response.strExtraBedTypeCode=='Invalid Code')
			    {
			    	alert("Invalid ExtraBed Code");
			    	$("#txtExtraBed").val('');
			    }
			    else
			    {
			    	$("#lblExtraBed").text(response.strExtraBedTypeDesc);					
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

	function funSetData(code) 
	{

		switch (fieldName) 
		{
			case "roomByRoomType":
				 funSetRoomMasterData(code);
				 break;
			
			case "roomType":
				funSetRoomType(code);
				break;
				
			case "extraBed":
				funSetExtraBed(code);
				break;	
				
			case "accountCode":
				funSetAccountCode(code);
				break;
				
			case "floormaster":
				funSetFloorMaster(code);
				break;
				
			case "bathType":
				funSetBathType(code);
				break;
				
			case 'reasonPMS' : 
				funSetReasonData(code);
			break;
		}
	}

	function funHelp(transactionName)
	{
		fieldName = transactionName;
		window.open("searchform.html?formname="+transactionName+"&searchText=","mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
		//window.showModalDialog("searchform.html?formname=" + transactionName + "&searchText=", "","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
	}
	
	function funHelp1(transactionName,row)
	{
		gridHelpRow=row;
		var condition = $("#txtRoomType").val();
		fieldName = transactionName;
		if(transactionName=="roomByRoomType" && condition!="")
			{
				window.open("searchform.html?formname="+fieldName+"&strRoomTypeCode="+condition+"&searchText=","mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
		
			}
		else
		{
			if(condition=="")
			{
				alert("Please Select Room Type !!!");
			}
			//window.open("searchform.html?formname="+transactionName+"&searchText=","mywindow","directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600,left=400px");
		}
	}
	
	function funSetFloorMaster(code)
	{
		$("#txtFloorCode").val(code);
		var searchurl=getContextPath()+"/loadFloorMasterData.html?floorCode="+code;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(response.strFloorCode=='Invalid Code')
			        	{
			        		alert("Invalid Business Code");
			        		$("#txtFloorCode").val('');
			        	}
			        	else
			        	{	
			        		$("#txtFloorCode").val(response.strFloorCode);
				        	$("#lblFloorName").text(response.strFloorName);
				        	
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
	
	function funSetBathType(code)
	{
		$("#txtBathTypeCode").val(code);
		var searchurl=getContextPath()+"/loadBathTypeMasterData.html?bathTypeCode="+code;
		 $.ajax({
			        type: "GET",
			        url: searchurl,
			        dataType: "json",
			        success: function(response)
			        {
			        	if(response.strBathTypeCode=='Invalid Code')
			        	{
			        		alert("Invalid Bath Code");
			        		$("#txtBathTypeCode").val('');
			        	}
			        	else
			        	{
			        		$("#txtBathTypeCode").val(response.strBathTypeCode);
				        	$("#lblBathTypeDesc").text(response.strBathTypeDesc);
				     
				        	
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
	
		function funBtnSubmit(){
		
			if($("#txtReason").val()==''){
				alert("Please Select Reason");
				return false;
			}
			
			if($("#txtRemarks").val()==''){
				alert("Please Select Remark");
				return false;
			}
		}
		
		function funSetReasonData(code)
		{
			$("#txtReason").val(code);
			var searchurl=getContextPath()+"/loadPMSReasonMasterData.html?reasonCode="+code;
			 $.ajax({
				        type: "GET",
				        url: searchurl,
				        dataType: "json",
				        success: function(response)
				        {
				        	if(response.strReasonCode=='Invalid Code')
				        	{
				        		alert("Invalid Reason Code");
				        		$("#txtReasonCode").val('');
				        	}
				        	else
				        	{	
				        		$("#txtReason").val(response.strReasonCode);
				        		$("#lblReasonDesc").text(response.strReasonDesc);
					        	
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
<body>

	<div id="formHeading">
		<label>Block Room </label>
	</div>

	<br />
	<br />

	<s:form name="BlockRoomMaster" method="POST"
		action="saveBlockRoom.html">


		<div id="tab_container" style="height: 405px">
			

			<!-- General Tab Start -->
			<div id="tab1" class="tab_content" style="height: 400px">
				<br> <br>
				<table class="masterTable">
				
				<tr>
						<td><label>Room Type</label></td>
						<td><s:input id="txtRoomType" path="strRoomType"
								ondblclick="funHelp('roomType')" cssClass="searchTextBox" /></td>
						<td><label id="lblRoomType"></label></td>
						<td colspan="1"></td>
					</tr>
					<tr>
						<td><label>Room Code</label></td>
						<td><s:input id="txtRoomCode" path="strRoomCode"
								ondblclick="funHelp1('roomByRoomType')" cssClass="searchTextBox" /></td>
						<td colspan="2"><s:input id="txtRoomDesc" path=""
								required="true" cssClass="longTextBox" style="width: 316px" /></td>
					</tr>

			<tr>
			    <td><label>Valid From</label></td>	
			    <td><s:input type="text" id="dteValidFrom" path="dteValidFrom" required="true" class="calenderTextBox" /></td>
			    <td><label>Valid To</label></td>	
			    <td><s:input type="text" id="dteValidTo" path="dteValidTo" class="calenderTextBox" /></td>		    		  
			</tr>

					
					<tr>
						<td><label>Reason</label></td>
						<%-- <td><s:input id="txtReason" path="strReason"
								cssClass="longTextBox" style="width: 190px" /></td> --%>
								
								
								<td>
					<s:input colspan="1" type="text" id="txtReason" path="strReason" cssClass="searchTextBox" ondblclick="funHelp('reasonPMS');"/>
				</td>
				<td><label id="lblReasonDesc" ></label></td>
						</td>
						<td></td>
						<td></td>
					</tr>
					
					<tr>
						<td><label>Remarks</label></td>
						<td><s:textarea id="txtRemarks" path="strRemarks"
								cssClass="longTextBox"  /></td>
						</td>
						<td></td>
						<td></td>
					</tr>

					
					<%-- 	<tr>
						
						 <td><label>Room Status</label></td>
						<td><s:input id="txtRoomStatus" path="strStatus"  cssClass="longTextBox" style="width: 190px"/></td>
						<td><s:select id="txtRoomStatus" path="strStatus" cssClass="BoxW124px" >
					    <option selected="selected" value="Free">Free</option>
				        <option value="Blocked">Blocked</option>
				        <option value="Occupied">Occupied</option>
				        </td>
			         </s:select>
						</tr> --%>
				</table>
			</div>
			<!--General Tab End  -->


			

		</div>
		<br />
		<br />
		<p align="center">
			<input type="submit" value="Submit" tabindex="3" class="form_button" onclick="return funBtnSubmit()"/>
			<input type="reset" value="Reset" class="form_button"
				onclick="funResetFields()" />
		</p>

	</s:form>
</body>
</html>
