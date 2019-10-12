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
				alert("Data Save successfully\n\n"+message);
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
					    var OperationalNY = $("#txtOperationalNY").val();
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
		    row.insertCell(2).innerHTML= "<input class=\"Box\" type=\"text\" name=\"listFacilityDtl["+(rowCount)+"].strOperationalNY\" size=\"9%\" style=\"text-align: right;\" id=\"txtOperationalNY."+(rowCount)+"\" value='"+OperationalNY+"'/>";	
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
			$("#txtOperationalNY").val('');
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
					        	$("#txtOperationalNY").val(response.strOperationalNY);
					        	
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
		<label>Member Category Master</label>
	</div>
	<div>
		<s:form name="frmMemberCategoryMaster"
			action="saveWebClubMemberCategoryMaster.html?saddr=${urlHits}"
			method="POST">
			<br>
			<table
				style="border: 0px solid black; width: 100%; height: 70%; margin-left: auto; margin-right: auto; background-color: #C0E4FF;">
				<tr>
					<td>
						<div id="tab_container" style="height: 380px">
							<ul class="tabs">
								<li class="active" data-state="tab1">Member Category</li>
								<li data-state="tab2" onclick="funSetFacilityMasterListData()">Facility
									Master</li>
							</ul>

							<div id="tab1" class="tab_content" style="height: 290px">
								<br>
								<br>
								<table class="transTable">
									<tr>
										<td width="18%">Member Category Code</td>
										<td width="10%"><s:input id="txtCatCode"
												path="strCatCode" cssClass="searchTextBox"
												ondblclick="funHelp('WCCatMaster')" readonly="true" /> <s:input
												type="text" id="txtMCName" name="txtMCName"
												path="strCatName" required="true"
												cssStyle="text-transform: uppercase;" cssClass="longTextBox" />
											<s:errors path=""></s:errors></td>
									</tr>
									<tr>
										<td width="18%"">Member Group Category Code</td>
										<td><s:input id="txtGroupCategoryCode"
												path="strGroupCategoryCode" cssClass="searchTextBox"
												ondblclick="" readonly="true" /> <%-- <s:input type="text" id="txtMSCName" 
						name="txtMSCName" path="" required="true"
						cssStyle="text-transform: uppercase;" cssClass="longTextBox"  /> <s:errors path=""></s:errors> --%></td>
									</tr>

									<tr>
										<td width="18%"">Tenure</td>
										<td><s:input type="text" id="txtTenure" name="txtTenure"
												path="strTenure" required="true" cssClass="longTextBox"
												style="width: 118px;" />&nbsp;&nbsp;&nbsp;(Years)</td>
									</tr>

									<tr>
										<td width="18%"">Rule Code</td>
										<td><s:input id="txtRCode" path="strRuleCode"
												cssClass="searchTextBox" ondblclick="" readonly="true" /> <%-- <s:input type="text" id="txtRName" 
						name="txtRName" path="" required="true"
						cssStyle="text-transform: uppercase;" cssClass="longTextBox"  /> <s:errors path=""></s:errors> --%></td>
									</tr>
									<tr>
										<td width="18%"><label>Credit Limit</label></td>
										<td width="100%"><s:input id="txtCreditLimit"
												path="intCreditLimit" required="required"
												class="decimal-places numberField" type="text"></s:input></td>

									</tr>

									<tr>
										<td><label>Voting Rights</label></td>
										<td><s:select id="cmbVotingRight" name="cmbVotingRight"
												path="strVotingRights" cssClass="BoxW124px">
												<option value="N">No</option>
												<option value="Y">Yes</option>
											</s:select></td>
									</tr>

									<tr>
										<td><label>Remark</label></td>
										<td><s:textarea path="strRemarks" id="txtRemarks"
												style="width: 400px; height: 37px" /></td>
									</tr>
									<tr>
										<td><label>Credit Amount</label></td>
										<td><s:input id="txtCreditAmt" path="dblCreditAmt"
												required="required" class="decimal-places numberField"
												type="text"></s:input></td>

									</tr>
									<tr>
										<td><label>Discount Amount</label></td>
										<td><s:input id="txtDiscountAmt" path="dblDisAmt"
												required="required" class="decimal-places numberField"
												type="text"></s:input></td>

									</tr>




								</table>
							</div>
							<div id="tab2" class="tab_content" style="height: 290px">
								<br>
								<br>
								<table class="transTable">

									<tr>
										<td width="20px"><label>Facility Code</label></td>
										<td width="20px"><s:input id="txtFacilityCode"
												path="strFacilityCode" required="" cssClass="searchTextBox"
												type="text" ondblclick="funHelp('WCFacilityMaster')"
												readonly="true"></s:input></td>

									</tr>
									<tr>
										<td width="30px"><label>Facility Name</label>
										<td colspan="2"><s:input id="txtFacilityName"
												path="strFacilityName" required="" cssStyle="width:250% ;"
												cssClass="longTextBox" type="text"></s:input></td>
									</tr>
									<tr>
										<td width="20px"><label>Operational</label>
										<td colspan="2"><s:input id="txtOperationalNY"
												path="strOperationalNY" required="" cssStyle="width:80% ;"
												cssClass="longTextBox" /></td>
										<td colspan="4"><input id="btnAdd" type="button"
											class="smallButton" value="Add"
											onclick="return btnAdd_onclick();"></input></td>
									</tr>
								</table>


								<div class="dynamicTableContainer" style="height: 260px">
									<table
										style="height: 25px; border: #0F0; width: 100%; font-size: 11px; font-weight: bold;">

										<tr bgcolor="#72BEFC">
											<td width="5%">Facility Code</td>
											<!--  COl1   -->
											<td width="27%">Facility Name</td>
											<!--  COl2   -->
											<td width="7%">Operational</td>
											<!--  COl3   -->
										</tr>
									</table>
									<div
										style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 275px; margin: auto; overflow-x: hidden; overflow-y: scroll; width: 100%;">
										<table id="tblProduct" path="strTblProduct"
											style="width: 100%; border: #0F0; table-layout: fixed; overflow: scroll"
											class="transTablex col9-center">
											<tbody>
											<col style="width: 12.85%">
											<!--  COl1   -->
											<col style="width: 70.8%">
											<!--  COl2   -->
											<col style="width: 10%">
											<!--  COl3   -->
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</td>
				</tr>
			</table>


			<p align="center">
				<input type="submit" value="Submit" onclick="" class="form_button" />
				&nbsp; &nbsp; &nbsp; <input type="reset" value="Reset"
					class="form_button" onclick="funResetField()" />
			</p>
			<br>
			<br>

		</s:form>
	</div>

</body>
</html>