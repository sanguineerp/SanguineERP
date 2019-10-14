<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" />

<script type="text/javascript">
		var fieldName,gurl,listRow=0,mastercode;
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
				alert("\n"+message);
			<%
			}}%>
			
			

		});
	
	 
	//satyajit code start
	 function btnAdd_onclick() 
		{			
			if(($("#txtFacilityCode").val().trim().length == 0) )
	        {
				 alert("Please Enter Product Code Or Search");
	             $("#txtFacilityCode").focus() ; 
	             return false;
	        }						 		     	 
			else
		    {
				var strFacilityCode=$("#txtFacilityCode").val();
				if(funDuplicateProduct(strFacilityCode))
	            	{
						var facilityCode = $("#txtFacilityCode").val();
					    var facilityName = $("#txtFacilityName").val();
					    var OperationalNY = $("#chkOperationalNY").val();
						funAddRow(facilityCode,facilityName,OperationalNY);
	            	}
			}		 
		}	 
	 
		 /*
		 * Check duplicate record in grid
		 */
		function funDuplicateProduct(strFacilityCode)
		{
		    var table = document.getElementById("tblProduct");
		    var rowCount = table.rows.length;		   
		    var flag=true;
		    if(rowCount > 0)
		    	{
				    $('#tblProduct tr').each(function()
				    {
					    if(strFacilityCode==$(this).find('input').val())// `this` is TR DOM element
	    				{
					    	alert("Already added "+ strFacilityCode);
					    	 funResetProductFields();
		    				flag=false;
	    				}
					});
				    
		    	}
		    return flag;
		  
		}
		
		/**
		 * Adding Product Data in grid 
		 */
		function funAddRow(facilityCode,facilityName,OperationalNY) 
		{   	    	    
		    var table = document.getElementById("tblProduct");
		    var rowCount = table.rows.length;
		    var row = table.insertRow(rowCount);   
		    
		    rowCount=listRow;
		    row.insertCell(0).innerHTML= "<input class=\"Box\" size=\"8%\" name=\"listFacilityDtl["+(rowCount)+"].strFacilityCode\" id=\"txtFacilityCode."+(rowCount)+"\" value="+facilityCode+">";
		    row.insertCell(1).innerHTML= "<input class=\"Box\" size=\"55%\" name=\"listFacilityDtl["+(rowCount)+"].strFacilityName\" value='"+facilityName+"' id=\"txtFacilityName."+(rowCount)+"\" >";
		    row.insertCell(2).innerHTML= "<input class=\"Box\" type=\"text\" name=\"listFacilityDtl["+(rowCount)+"].strOperationalNY\" size=\"9%\" style=\"text-align: right;\" id=\"chkOperationalNY."+(rowCount)+"\" value='"+OperationalNY+"'/>";	
		    row.insertCell(3).innerHTML= "<input type=\"button\" class=\"deletebutton\" size=\"5%\" value = \"Delete\" onClick=\"Javacsript:funDeleteRow(this)\"/>";
			   
		    listRow++;		    
		    funResetProductFields();		   		    
		}
		
		
		/**
		 * Delete a particular record from a grid
		 */
		function funDeleteRow(obj)
		{
		    var index = obj.parentNode.parentNode.rowIndex;
		    var table = document.getElementById("tblProduct");
		    table.deleteRow(index);
		}
		
		/**
		 * Remove all product from grid
		 */
		function funRemProdRows()
	    {
			var table = document.getElementById("tblProduct");
			var rowCount = table.rows.length;
			for(var i=rowCount;i>=0;i--)
			{
				table.deleteRow(i);
			}
	    }
		

		/**
		 * Clear textfiled after adding data in textfield
		 */
		function funResetProductFields()
		{
			$("#txtFacilityCode").val('');
			$("#txtFacilityName").val('');
			$("#chkOperationalNY").val('');
		}
		
		function funRemoveProductRows()
		{
			var table = document.getElementById("tblProduct");
			var rowCount = table.rows.length;
			while(rowCount>0)
			{
				table.deleteRow(0);
				rowCount--;
			}
		}
		
		
		
	
		
	 //satyajit code end
	 
	 function funHelp(transactionName)
		{	       
			fieldName=transactionName;
	    //    window.showModalDialog("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
	        window.open("searchform.html?formname="+transactionName+"&searchText=","","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;")
	        
	    }
	 
	 function funResetFields()
		{
			location.reload(true); 
		}	 
	 
	 function funSetData(code)
		{
		 switch(fieldName)
		 	{

			case 'WCFacilityMaster' :
				funSetFacilityData(code);
				
				break;
				
			case 'WCCatMaster' :				
				funSetMemberCategoryData(code);					
				//funSetFacilityMasterListData(code);
				break;
			}
		}
	 function funSetMemberCategoryData(code){
		 
			$("#txtCatCode").val(code);
			var searchurl=getContextPath()+"/loadWebClubMemberCategoryMaster.html?catCode="+code;
			//alert(searchurl);
			 $.ajax({
				        type: "GET",
				        url: searchurl,
				        dataType: "json",
				        success: function(response)
				        {
				        	if(response.strGCode=='Invalid Code')
				        	{
				        		alert("Invalid Category Code");
				        		$("#txtCatCode").val('');
				        	}
				        	else
				        	{
					        	$("#txtCatCode").val(code);
					        	mastercode=$("#txtCatCode").val(code);
					        	$("#txtMCName").val(response.strCatName);
					        	$("#txtGroupCategoryCode").val(response.strGroupCategoryCode);
					        	$("#txtTenure").val(response.strTenure);
					        	$("#cmbVotingRight").val(response.strVotingRights);
					        	$("#txtRCode").val(response.strRuleCode);  	
					        	$("#txtCreditLimit").val(response.intCreditLimit);
					        	$("#txtRemarks").val(response.strRemarks);
					        	$("#txtCreditAmt").val(response.dblCreditAmt);
					        	$("#txtDiscountAmt").val(response.dblDisAmt);
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
	 
	 
	 function funSetFacilityData(code){
		 
			$("#txtFacilityCode").val(code);
			var searchurl=getContextPath()+"/loadWebClubFacilityMaster.html?catCode="+code;
			//alert(searchurl);
			 $.ajax({
				        type: "GET",
				        url: searchurl,
				        dataType: "json",
				        success: function(response)
				        {
				        	if(response.strFacilityCode=='Invalid Code')
				        	{
				        		alert("Invalid Category Code");
				        		$("#txtFacilityCode").val('');
				        	}
				        	else
				        	{
					        	$("#txtFacilityCode").val(code);
					        	mastercode=$("#txtFacilityCode").val(code);
					        	$("#txtFacilityName").val(response.strFacilityName);
					        	$("#chkOperationalNY").val(response.strOperationalNY);
					        	
				        	}
				        	
				        	
				        	if(response.strOperationalNY=='Y')
				        	{
				        		$("#chkOperationalNY").attr('checked', true);
				        	}
				        	else
				        	{
				        		$("#chkOperationalNY").attr('checked', false);
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
	 
	 
	 function funSetFacilityMasterListData(){
		 	funRemoveProductRows();
			var catCode=$("#txtCatCode").val();
			var searchurl=getContextPath()+"/loadWebClubFacilityMasterListDtl.html?catCode="+catCode;
			//alert(searchurl);
			 $.ajax({
				        type: "GET",
				        url: searchurl,
				        dataType: "json",
				        success: function(response)
				        {
				        	
				        	$.each(response, function(cnt,item)
		 					{
					        	funAddRow(item[1],item[2],item[3])
					        	//alert(response);
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
		<label>Banquet Setup</label>
	</div>
	<div>
		<s:form name="frmBanquetSetupMaster" action="saveBanquetSetupMaster.html?saddr=${urlHits}"
			method="POST">
			<br>
			<table
				style="border: 0px solid black; width: 100%; height: 70%; margin-left: auto; margin-right: auto; background-color: #C0E4FF;">
				<tr>
					<td>
						<div id="tab_container" style="height: 380px">
							<ul class="tabs">
								<li class="active" data-state="tab1">General</li>
								<li data-state="tab2" onclick="funSetFacilityMasterListData()">Weekend Setup</li>
							</ul>

			<div id="tab1" class="tab_content" style="height: 290px">
				<br>
				<br>
				<table class="transTable">
			 
			 
			 
			 						
					</table>
				</div>	
				
				
				<div id="tab2" class="tab_content" style="height: 290px;">
				<br>
				<br>
	<table class="transTable" style="width: 50%; margin-left: 0px;">
			 
		<tr>
				<td>
					<label>Sunday</label>					
					<td><s:checkbox id="chkSunday" name="chkSunday" path="strSunday" value="Sunday" /></td> 
				<td>
					<label>Monday</label>
					<td><s:checkbox id="chkMonday" name="chkMonday" path="strMonday" value="Monday" /></td> 
			
			
				<td>
					<label>Tuesday</label>
					<td><s:checkbox id="chkTuesday" name="chkTuesday" path="strTuesday" value="Tuesday" /></td> 
				</tr>
				<tr>
				<td>
					<label>Wednesday</label>
					<td><s:checkbox id="chkWednesday" name="chkWednesday" path="strWednesday" value="Wednesday" /></td> 
				<td>
					<label>Thursday</label>
					<td><s:checkbox id="chkThursday" name="chkSunday" path="strThursday" value="Thursday" /></td> 
				<td>
					<label>Friday</label>
					<td><s:checkbox id="chkFriday" name="chkFriday" path="strFriday" value="Friday" /></td> 
				</tr>
				<tr>
				<td>
					<label>Saturday</label>
					<td><s:checkbox id="chkSaturday" name="chkSaturday" path="strSaturday" value="Saturday" /></td> 
			</tr>	 
			 
			 						
					</table>
				</div>			
			</div>
		</td>
	</tr>
</table>
			<p align="center">
				<input type="submit" value="Submit" onclick="" class="form_button" />
				&nbsp; &nbsp; &nbsp; 
				<input type="reset" value="Reset" class="form_button" onclick="funResetField()" />
			</p>
			<br>
			<br>
		</s:form>
	</div>

</body>
</html>