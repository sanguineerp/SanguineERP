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
 var strGCode="";  
 var strSGCode="";
 
 
 $(document).ready(function() 
 		{
		 var tablename='';
			$('#searchGrp').keyup(function()
			{
				tablename='#tblGroup';
				searchTable($(this).val(),tablename);
			});
			$('#searchSGrp').keyup(function()
	 			{
						tablename='#tblSubGroup';
	 				searchTable($(this).val(),tablename);
	 			});
 			$(document).ajaxStart(function()
 		 	{
 			    $("#wait").css("display","block");
 		  	});
 			$(document).ajaxComplete(function(){
 			    $("#wait").css("display","none");
 			  });
 			funGetGroupData();
 		});
 
	 function searchTable(inputVal,tablename)
		{
			var table = $(tablename);
			table.find('tr').each(function(index, row)
			{
				var allCells = $(row).find('td');
				if(allCells.length > 0)
				{
					var found = false;
					allCells.each(function(index, td)
					{
						var regExp = new RegExp(inputVal, 'i');
						if(regExp.test($(td).find('input').val()))
						{
							found = true;
							return false;
						}
					});
					if(found == true)$(row).show();else $(row).hide();
				}
			});
		}
    //Get Project Path
	function getContextPath() 
	{
		return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
	}

    //Check From to where Link Click to Open
	function funExport()
	{
		funSetGroupSubGroupCode();
		switch(transactionformName)
		{
			case "frmOpeningStock":
				window.location.href=getContextPath()+"/frmOpeningStkExcelExport.html?strLocCode="+LocCode;
				break;
			case "frmPhysicalStkPosting":
				
				var gCode = strGCode  //$("#txtGroupCode").val();
				var sgCode = strSGCode //$("#txtSubGroupCode").val();
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
				var gCode = strGCode  //$("#txtGroupCode").val();
				var sgCode = strSGCode //$("#txtSubGroupCode").val();
				var prodWiseStock=$("#cmbProdStock").val();
				
					window.location.href=getContextPath()+"/MaterialReqExport.html?locCode="+LocCode+"&gCode="+gCode+"&sgCode="+sgCode+"&strTransDate="+dtPhydate+"&strUOM=RecUOM&prodWiseStock="+prodWiseStock;	
				break;
				
			case "frmPurchaseIndent":
				var gCode = strGCode  //$("#txtGroupCode").val();
				var sgCode = strSGCode //$("#txtSubGroupCode").val();
				var prodWiseStock=$("#cmbProdStock").val();
				
					window.location.href=getContextPath()+"/PurchaseIndentExport.html?locCode="+LocCode+"&gCode="+gCode+"&sgCode="+sgCode+"&strTransDate="+dtPhydate+"&strUOM=RecUOM&prodWiseStock="+prodWiseStock;	
				break;
				
			case "frmPurchaseOrder":
				var gCode = strGCode  //$("#txtGroupCode").val();
				var sgCode = strSGCode //$("#txtSubGroupCode").val();
				var prodWiseStock=$("#cmbProdStock").val();
				
					window.location.href=getContextPath()+"/PurchaseOrderExport.html?suppCode="+supplierCode+"&gCode="+gCode+"&sgCode="+sgCode+"&strTransDate="+dtPhydate+"&strUOM=RecUOM&prodWiseStock="+prodWiseStock;	
				break;
				
				
			case "frmMIS":
				var gCode = strGCode  //$("#txtGroupCode").val();
				var sgCode = strSGCode //$("#txtSubGroupCode").val();
				var prodWiseStock=$("#cmbProdStock").val();
				
					window.location.href=getContextPath()+"/MISExport.html?locCode="+LocCode+"&gCode="+gCode+"&sgCode="+sgCode+"&strTransDate="+dtPhydate+"&strUOM=RecUOM&prodWiseStock="+prodWiseStock;	
				break;
				
			case "frmGuestMaster" :
				window.location.href=getContextPath()+"/GuestMasterExport.html";	
				break;
				
			case "frmRoomMaster" :
				window.location.href=getContextPath()+"/RoomMasterImport.html";	
				break;
				
				
			case "frmSupplieMaster" :
				window.location.href=getContextPath()+"/SupplierMasterImport.html";	
				break;
				
			case "frmProductMaster" :
				window.location.href=getContextPath()+"/ProductMasterExport.html";
				break;
				
			/* case "frmCheckInCheckOutList" :
				window.location.href=getContextPath()+"/CheckInCheckOutList.html";	
				
				break;
			 */
		}
	}
    
    
    function funSetGroupSubGroupCode()
    {
	    
    	strGCode="";
	   	 $('input[name="GCodethemes"]:checked').each(function() {
	   		 if(strGCode.length>0)
	   			 {
	   			 strGCode=strGCode+","+this.value;
	   			 }
	   			 else
	   			 {
	   				 strGCode=this.value;
	   			 }
	   		 
	   		});
	   	 if(strGCode=="")
	   	 {
	   	 	alert("Please Select Group Code");
	   	 	return false;
	   	 }
	   	 $("#txtGroupCode").val(strGCode);
	   	
	   	 
	   	
	   	strSGCode="";
	   	 $('input[name="SubGroupthemes"]:checked').each(function() {
	   		 if(strSGCode.length>0)
	   			 {
	   			 strSGCode=strSGCode+","+this.value;
	   			 }
	   			 else
	   			 {
	   				 strSGCode=this.value;
	   			 }
	   		 
	   		});
	   	 if(strSGCode=="")
	   	 {
	   	 	alert("Please Select SubGroup");
	   	 	return false;
	   	 }
	   	 $("#txtSubGroupCode").val(strSGCode);
   		
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
		
		if(transactionformName=="frmProductMaster")
		{
			LocCode='<%=request.getParameter("strProdCode") %>'
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
	

	   
    //Get and Set All Location on the basis of all Property
      function funSetAllLocationAllPrpoerty() {
			var searchUrl = "";
			searchUrl = getContextPath()+ "/loadAllLocationForAllProperty.html";
			$.ajax({
				type : "GET",
				url : searchUrl,
				dataType : "json",
				beforeSend : function(){
					 $("#wait").css("display","block");
			    },
			    complete: function(){
			    	 $("#wait").css("display","none");
			    },
				success : function(response) {
					if (response.strLocCode == 'Invalid Code') {
						alert("Invalid Location Code");
						$("#txtFromLocCode").val('');
						$("#lblFromLocName").text("");
						$("#txtFromLocCode").focus();
					} else
					{
						$.each(response, function(i,item)
						 		{
							funfillLocationGrid(response[i].strLocCode,response[i].strLocName);
								});
						
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
    
      //Fill  Location Data
	    function funfillLocationGrid(strLocCode,strLocationName)
		{
			
			 	var table = document.getElementById("tblloc");
			    var rowCount = table.rows.length;
			    var row = table.insertRow(rowCount);
			    
			    row.insertCell(0).innerHTML= "<input id=\"cbToLocSel."+(rowCount)+"\" name=\"Locthemes\" type=\"checkbox\" class=\"LocCheckBoxClass\"  checked=\"checked\" value='"+strLocCode+"' />";
			    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"strToLocCode."+(rowCount)+"\" value='"+strLocCode+"' >";
			    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \" size=\"50%\" id=\"strToLocName."+(rowCount)+"\" value='"+strLocationName+"' >";
		}
	  
	  
		     
	      //Get and set Supplier  Data 
	      function funSetAllSupplier() {
				var searchUrl = "";
				searchUrl = getContextPath()+ "/loadAllSupplier.html";
				$.ajax({
					type : "GET",
					url : searchUrl,
					dataType : "json",
					beforeSend : function(){
						 $("#wait").css("display","block");
				    },
				    complete: function(){
				    	 $("#wait").css("display","none");
				    },
					success : function(response) {
						if (response.strSuppCode == 'Invalid Code') {
							alert("Invalid Supplier Code");
							
						} else
						{
							$.each(response, function(i,item)
							 		{
								funfillSuppGrid(response[i].strPCode,response[i].strPName);
									});
							
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
		

				
		    //Fill Supplier Data
		    function funfillSuppGrid(strSuppCode,strSuppName)
			{
				
				 	var table = document.getElementById("tblSupp");
				    var rowCount = table.rows.length;
				    var row = table.insertRow(rowCount);
				    
				    row.insertCell(0).innerHTML= "<input id=\"cbSuppSel."+(rowCount)+"\" name=\"Suppthemes\" type=\"checkbox\" class=\"SuppCheckBoxClass\"  checked=\"checked\" value='"+strSuppCode+"' />";
				    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \" size=\"15%\" id=\"strSuppCode."+(rowCount)+"\" value='"+strSuppCode+"' >";
				    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \" size=\"50%\" id=\"strSName."+(rowCount)+"\" value='"+strSuppName+"' >";
			}
		    //Remove All Row from Grid Passing Table Id as a parameter
		    function funRemRows(tablename) 
			{
				var table = document.getElementById(tablename);
				var rowCount = table.rows.length;
				while(rowCount>0)
				{
					table.deleteRow(0);
					rowCount--;
				}
			}
		    
		    //Get All Group data 
		    function funGetGroupData()
			{
				var searchUrl = getContextPath() + "/loadAllGroupData.html";
				$.ajax({
					type : "GET",
					url : searchUrl,
					dataType : "json",
					success : function(response) {
						funRemRows("tblGroup");
						$.each(response, function(i,item)
				 		{
							funfillGroupGrid(response[i].strGCode,response[i].strGName);
						});
						funGroupChkOnClick();
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
		    
		    //Fill Group Data
			function funfillGroupGrid(strGroupCode,strGroupName)
			{
				
				 	var table = document.getElementById("tblGroup");
				    var rowCount = table.rows.length;
				    var row = table.insertRow(rowCount);
				    
				    row.insertCell(0).innerHTML= "<input id=\"cbGSel."+(rowCount)+"\" type=\"checkbox\"  name=\"GCodethemes\" class=\"GCheckBoxClass selected \" checked=\"checked\" value='"+strGroupCode+"' onclick=\"funGroupChkOnClick()\"/>";
				    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box selected\" size=\"15%\" id=\"strGCode."+(rowCount)+"\" value='"+strGroupCode+"' >";
				    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box selected\" size=\"50%\" id=\"strGName."+(rowCount)+"\" value='"+strGroupName+"' >";
			}
				
			//Select All Group
			function funGroupChkOnClick()
			{
				var table = document.getElementById("tblGroup");
			    var rowCount = table.rows.length;  
			    var strGCodes="";
			    for(no=0;no<rowCount;no++)
			    {
			        if(document.all("cbGSel."+no).checked==true)
			        	{
			        		if(strGCodes.length>0)
			        			{
			        				strGCodes=strGCodes+","+document.all("strGCode."+no).value;
			        			}
			        		else
			        			{
			        				strGCodes=document.all("strGCode."+no).value;
			        			}
			        	}
			    }
			    funGetSubGroupData(strGCodes);
			}
			
			//Geting SubGroup Data On the basis of Selection Group
			function funGetSubGroupData(strGCodes)
			{
				strCodes = strGCodes.split(",");
				var count=0;
				funRemRows("tblSubGroup");
				for (ci = 0; ci < strCodes.length; ci++) 
				 {
					var searchUrl = getContextPath() + "/loadSubGroupCombo.html?code="+ strCodes[ci];
					$.ajax({
						type : "GET",
						url : searchUrl,
						dataType : "json",
						beforeSend : function(){
							 $("#wait").css("display","block");
					    },
					    complete: function(){
					    	 $("#wait").css("display","none");
					    },
						success : function(response)
						{
							$.each(response, function(key, value) {
								funfillSubGroup(key,value);
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
				
			}
			
			//Fill SubGroup Data
			function funfillSubGroup(strSGCode,strSGName) 
			{
				var table = document.getElementById("tblSubGroup");
			    var rowCount = table.rows.length;
			    var row = table.insertRow(rowCount);
			    
			    row.insertCell(0).innerHTML= "<input id=\"cbSGSel."+(rowCount)+"\" type=\"checkbox\" checked=\"checked\" name=\"SubGroupthemes\" value='"+strSGCode+"' class=\"SGCheckBoxClass\" />";
			    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box\" size=\"15%\" id=\"strSGCode."+(rowCount)+"\" value='"+strSGCode+"' >";
			    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box\" size=\"50%\" id=\"strSGName."+(rowCount)+"\" value='"+strSGName+"' >";
			}
			
			//Select All Group,SubGroup,From Location, To Location When Clicking Select All Check Box
			 $(document).ready(function () 
						{
							$("#chkSGALL").click(function ()
							{
							    $(".SGCheckBoxClass").prop('checked', $(this).prop('checked'));
							});
							
							$("#chkGALL").click(function () 
							{
							    $(".GCheckBoxClass").prop('checked', $(this).prop('checked'));
							    funGroupChkOnClick();
							  
							});
							
							
						
							
						});
					 
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
	   <td colspan="1">Show Stock Wise Product</td>
				<td colspan="1"><select id="cmbProdStock" Class="BoxW124px" >
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
				<!--  <tr> 
			    <td><label path="strGCode" >Group Code</label></td>
		        <td><input type="text" id="txtGroupCode" name="txtGroupCode" autocomplete="off"    ondblclick="funHelp('group')" required="true" cssClass="searchTextBox" /></td><td><label id="lblgroupname">All</label></td>
				
			</tr>
			
				<tr>
				        <td><label >Sub-Group Code</label></td>
				        <td ><input id="txtSubGroupCode" name="subGroupCode"  ondblclick="funHelp('subgroup')" autocomplete="off" cssClass="searchTextBox"/></td>
				       <td><label id="txtSubGroupName">All</label></td>
				        <td></td>
				       
			   		</tr> -->
			   	
		<tr>
		<td colspan="2">Group&nbsp;&nbsp;&nbsp;
			<input type="text"   style="width: 35%;background-position: 150px 2px;"  Class="searchTextBox" placeholder="Type to search"  
			id="searchGrp" >
		 </td>
		 <td colspan="2">Sub Group&nbsp;&nbsp;&nbsp;&nbsp;
		  		 <input type="text" id="searchSGrp" 
		  		  style="width: 35%;background-position: 150px 2px;"  Class="searchTextBox" placeholder="Type to search" >
		 </td>
		 	
		  </tr>
			
			<tr>
				<td colspan="2">
						<div
							style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 150px; overflow-x: hidden; overflow-y: scroll;">
							<table id="" class="display"
								style="width: 100%; border-collapse: separate;">
								<tbody>
									<tr bgcolor="#72BEFC">
										<td width="15%"><input type="checkbox" id="chkGALL"
											checked="checked" onclick="funCheckUncheck()" />Select</td>
										<td width="20%">Group Code</td>
										<td width="65%">Group Name</td>

									</tr>
								</tbody>
							</table>
							<table id="tblGroup" class="masterTable"
								style="width: 100%; border-collapse: separate;">
								<tbody>
									<tr bgcolor="#72BEFC">
										<td width="15%"></td>
										<td width="20%"></td>
										<td width="65%"></td>

									</tr>
								</tbody>
							</table>
						</div>
						</td>
						<td  colspan="2">
						<div
							style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; height: 150px; overflow-x: hidden; overflow-y: scroll;">

							<table id="" class="masterTable"
								style="width: 100%; border-collapse: separate;">
								<tbody>
									<tr bgcolor="#72BEFC">
										<td width="15%"><input type="checkbox" id="chkSGALL"
											checked="checked" onclick="funCheckUncheckSubGroup()" />Select</td>
										<td width="25%">Sub Group Code</td>
										<td width="65%">Sub Group Name</td>

									</tr>
								</tbody>
							</table>
							<table id="tblSubGroup" class="masterTable"
								style="width: 100%; border-collapse: separate;">
								<tbody>
									<tr bgcolor="#72BEFC">
										<td width="15%"></td>
										<td width="25%"></td>
										<td width="65%"></td>

									</tr>
								</tbody>
							</table>
							
						</div>
				</td></tr><tr>
				
		
		
			   		
			   	  	
		  
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