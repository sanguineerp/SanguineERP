<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>

<style type="text/css">
body {
  color: black;
  background-color: #f1f3f6;
  font-family: trebuchet ms, Helvetica, sans-serif;
 
  
}


.table>tbody>tr.active>td,
.table>tbody>tr.active>th,
.table>tbody>tr>td.active,
.table>tbody>tr>th.active,
.table>tfoot>tr.active>td,
.table>tfoot>tr.active>th,
.table>tfoot>tr>td.active,
.table>tfoot>tr>th.active,
.table>thead>tr.active>td,
.table>thead>tr.active>th,
.table>thead>tr>td.active,
.table>thead>tr>th.active {
  background-color: #a3d0f7;

}
.table>thead>tr.active {
	background-color: #a3d0f7;
	
},
.table>thead{
	background-color: #a3d0f7;
}


.table-bordered > tbody > tr > td,
.table-bordered > tbody > tr > th,
.table-bordered > tfoot > tr > td,
.table-bordered > tfoot > tr > th,
.table-bordered > thead > tr > td,
.table-bordered > thead > tr > td{
  border-color: #e4e5e7;
  border-bottom: 1px solid #dbd9d9;
},
.table-bordered > thead {
  background-color: #a3d0f7;
}


.table tr.header {
  font-weight: bold;
  /* height:20px;
  width:100px; */
  background-color: #A3D0F7;
  cursor: pointer;
  -webkit-user-select: none;
  /* Chrome all / Safari all */
  -moz-user-select: none;
  /* Firefox all */
  -ms-user-select: none;
  /* IE 10+ */
  user-select: none;
  /* Likely future */

}

.table tr:not(.header) {
  display: none;
    
}

/* .Box { background: inherit; border: 0px solid #060006; outline:0; padding-left: 00px;  font-size:11px;
	font-weight: bold; font-family: trebuchet ms,Helvetica,sans-serif; } */
.table .header td:after {
  /* content: "\002b"; 
  position: absolute;
  font-family: trebuchet ms,Helvetica,sans-serif;
  background: inherit;
  top: 0px;
  width:0px;
  height:0px;
  display: inline-block;
  border: 0px;
  font-style: Bold;
  outline:0;
  font-size:0px;
  font-weight: Bold;
  line-height: 20px;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  float: right;
  color: #999;
  text-align: center;
  padding: 2px;
  padding-left: 00px;
  transition: opacity 0.15s linear 0s;
  -webkit-transition: -webkit-transform .25s linear; */
  content: "\002b";
  position: relative;
  top: 1px;
  display: inline-block;
  font-family: 'Glyphicons Halflings';
  font-style: normal;
  font-weight: 400;
  line-height: 1;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  float: right;
  color: #999;
  text-align: center;
  padding: 3px;
  transition: transform .25s linear;
  -webkit-transition: -webkit-transform .25s linear;

}

.table .header.active td:after {
  content: "\2212";
  
}

</style>
<script type="text/javascript">
	
	var fieldName;
	var occupiedCnt=0;
	var emptyCnt=0;
	var blockCnt=0;
	var dirtyCnt=0;
	var reservedCnt=0;
	var mapRoomType={};
	var lightRed = '#ff4f53';
	
	/* $(document).ready(function(){
		
		
		$(document).ajaxStart(function() {
				$("#wait").css("display", "block");
			});
			$(document).ajaxComplete(function() {
				$("#wait").css("display", "none");
			});
	}); */
	
	
	$(document).ready(function() {
		  //Fixing jQuery Click Events for the iPad
		  var ua = navigator.userAgent,
		    event = (ua.match(/iPad/i)) ? "touchstart" : "click";
		  if ($('.table').length > 0) {
		    $('.table .header').on(event, function() {
		      $(this).toggleClass("active", "").nextUntil('.header').css('display', function(i, v) {
		        return this.style.display === 'table-row' ? 'none' : 'table-row';
		      });
		    });
		  }
		});
	
	$(function() 
	{
		$( tblRoomType ).tooltip();
		
		var pmsDate='<%=session.getAttribute("PMSDate").toString()%>';
		
		$("#txtViewDate").datepicker({ dateFormat: 'dd-mm-yy' });
		$("#txtViewDate").datepicker('setDate', pmsDate);
		
		//funFillHeaderRows();
	});
	
	
//Delete a All record from a grid
	function funRemoveHeaderTableRows()
	{
		var table = document.getElementById("tblDays");
		var rowCount = table.rows.length;
		while(rowCount>0)
		{
			table.deleteRow(0);
			rowCount--;
		}
		var table1 = document.getElementById("tblRoomType");
		var rowCount = table1.rows.length;
		while(rowCount>0)
		{
			table1.deleteRow(0);
			rowCount--;
		}
		occupiedCnt=0;
		emptyCnt=0;
		blockCnt=0;
		dirtyCnt=0;
		reservedCnt=0;
	}
	
	
	
	function funRemoveDetailTableRows()
	{
		var table = document.getElementById("tblRoomInfo");
		var rowCount = table.rows.length;
		while(rowCount>0)
		{
			table.deleteRow(0);
			rowCount--;
		}
	}
	
	
	
	function funShowRoomStatusFlash()
	{
		var viewDate=$("#txtViewDate").val();
				
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/getRoomStatusList.html?viewDate=" + viewDate,
			dataType : "json",
			 beforeSend : function(){
				 $("#wait").css("display","block");
		    },
		    complete: function(){
		    	 $("#wait").css("display","none");
		    },
			
			success : function(response){ 
				funRemoveHeaderTableRows();
				funFillHeaderRows(response);
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
	
	function funShowRoomStatusDtl1(row)
	{
		$(document).ready(function() {
			  //Fixing jQuery Click Events for the iPad
			  var ua = navigator.userAgent,
			    event = (ua.match(/iPad/i)) ? "touchstart" : "click";
			  if ($('.table').length > 0) {
			    $('.table .header').on(event, function() {
			      $(this).toggleClass("active", "").nextUntil('.header').css('display', function(i, v) {
			        return this.style.display === 'table-row' ? 'none' : 'table-row';
			      });
			    });
			  }
			})
		 
	
	}
	
	function funShowRoomStatusDtl()
	{
		 /* var code=key.value;
		  //code=code.split(',')[1].trim();
		  // For Room Number
		  var index=key.parentNode.parentNode.rowIndex;
		  var table1=document.getElementById("tblRoomType");
		  var indexData=table1.rows[index];
		  var roomType=indexData.cells[0].childNodes[0].defaultValue; */
		
		var viewDate=$("#txtViewDate").val();
			
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/getRoomStatusDtlList.html?viewDate=" + viewDate,
			dataType : "json",
			async:false,
			beforeSend : function(){
				 $("#wait").css("display","block");
		    },
		    complete: function(){
		    	 $("#wait").css("display","none");
		    },
			
			success : function(response){
				funRemoveDetailTableRows();
				var itemroomType='';
				$.each(response, function(i,item)
				{
					/* occupiedCnt=0;
					var roomType = $("#cmbGuestPrefix").val(); */
					/* if(roomType.includes(item.strRoomType)){ */
						//if(key.includes(item.strRoomType))
							
					if(itemroomType!=item.strRoomType){
						
						var key=item.strRoomType;
						var value=mapRoomType[key];
						funFillROomTypeHeaderRowsHeaderRows(key,value);
					}
					itemroomType=item.strRoomType;
					funFillRoomStatusRows(item.strRoomNo,item.strDay1,item.strDay2,item.strDay3,item.strDay4,item.strDay5,item.strDay6,item.strDay7,item.strRoomStatus,item);
					
					/* if(item.strRoomStatus.includes('Occupied'))
						{
						occupiedCnt=occupiedCnt+1;
						$("#occupiedRoomCnt").val(occupiedCnt);
						}
					if(item.strRoomStatus.includes('Free'))
					{
					emptyCnt=emptyCnt+1;
					$("#freeRoomCnt").val(emptyCnt);
					}
					
					if(item.strRoomStatus.includes('RESERVATION'))
					{
					reservedCnt=reservedCnt+1;
					$("#reservedRoomCnt").val(reservedCnt);
					}
					if(item.strRoomStatus.includes('Blocked'))
					{
					blockCnt=blockCnt+1;
					$("#blockedRoomCnt").val(blockCnt);
					}
					if(item.strRoomStatus.includes('Dirty'))
					{
					dirtyCnt=dirtyCnt+1;
					$("#dirtyRoomCnt").val(dirtyCnt);
					}  */
				
				});
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
	
		function funFillHeader(){
		
			
		}
		
	function funGetRoomTypeAndStatus()
	{
		
	
		var viewDate=$("#txtViewDate").val();
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/getRoomTypeWiseList.html?viewDate=" + viewDate,
			dataType : "json",
			async:false,
			success : function(response){ 
				//funRemoveHeaderTableRows();
				mapRoomType=response.RoomTypeCount;
				   /*  $.each(response.RoomTypeCount, function(key, value) {
				    	funFillROomTypeHeaderRowsHeaderRows(key,value);
		 					}); */
				
				
				//funShowRoomStatusDtl();
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
		
	function funFillHeaderRows(obj)
	{
		var table=document.getElementById("tblDays");
		var rowCount=table.rows.length;
		var row=table.insertRow();
		
		
		row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='Room No' >";
		row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj[0]+"' >";
	    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj[1]+"' >";
		row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj[2]+"' >";
		row.insertCell(4).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj[3]+"' >";
		row.insertCell(5).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj[4]+"' >";
		row.insertCell(6).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj[5]+"' >";
		row.insertCell(7).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj[6]+"' >";
		
		/*
		row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj.strRoomNo+"' >";
	    row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj.strRegistrationNo+"' >";
		row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj.strFolioNo+"' >";
		row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj.strGuestName+"' >";
		row.insertCell(4).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj.dblAmount+"' >";
		row.insertCell(5).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj.dteCheckInDate+"' >";
		row.insertCell(6).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj.dteCheckOutDate+"' >";
		row.insertCell(7).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj.strCorporate+"' >";
		*/
		funGetRoomTypeAndStatus();
		
		funShowRoomStatusDtl();
	}
	
	
	
	function funFillRoomStatusRows(roomNo,day1,day2,day3,day4,day5,day6,day7,roomStatus,response)
	{
		var table=document.getElementById("tblRoomType");
		var rowCount=table.rows.length;
		var row=table.insertRow();
		row.style.display='none';
		response.dblRemainingAmt='Balance: '+response.dblRemainingAmt;
		var color='';
		var toolTipText1="",toolTipText2="",toolTipText3="",toolTipText4="",toolTipText5="",toolTipText6="",toolTipText7="";
		if(roomStatus=='Waiting')
		{
			color='Yellow';
		}
		else if(roomStatus=='RESERVATION')
		{
			color='Green';
		}
		else if(roomStatus=='Occupied')
		{
			color=lightRed;
		}
		else if(roomStatus=='Checked Out')
		{
			color='Gray';
		}
		else if(roomStatus=='Blocked')
		{
			color='Olive';
		}
		else if(roomStatus=='Dirty')
		{
			color='Orange';
		}
	
		if(day1==null)
		{
			day1='';
		}
		else
		{
			if(response.strReservationNo!=null)
			{
				
				
				
					      day1+='                  ,'+response.strReservationNo;
					  
					      toolTipText1+="\n"+response.strGuestName+"\n"+response.strReservationNo+"\n"+response.strRoomNo+"\n"+response.dteArrivalDate+"\n"+response.dteDepartureDate+"\n"+roomStatus+"\n"+response.dblRemainingAmt;
					      
			}
		}
		
		if(day2==null)
		{
			day2='';
		}
		else
		{
			if(response.strReservationNo!=null)
			{
				//day2+='               ,'+response.strReservationNo+','+response.strCheckInNo;
				
					      day2+='                    ,'+response.strReservationNo;
					      //alert(item[i].strRoomNo);
					      toolTipText2+="\n"+response.strGuestName+"\n"+response.strReservationNo+"\n"+response.strRoomNo+"\n"+response.dteArrivalDate+"\n"+response.dteDepartureDate+"\n"+roomStatus+"\n"+response.dblRemainingAmt;
				
	        }
		}
		
		if(day3==null)
		{
			day3='';
		}
		else
		{
			if(response.strReservationNo!=null)
			{
				//day3+='               ,'+response.strReservationNo+','+response.strCheckInNo;
				
					      day3+='                     ,'+response.strReservationNo;
					      //alert(item[i].strRoomNo);
					      toolTipText3+="\n"+response.strGuestName+"\n"+response.strReservationNo+"\n"+response.strRoomNo+"\n"+response.dteArrivalDate+"\n"+response.dteDepartureDate+"\n"+roomStatus+"\n"+response.dblRemainingAmt;
				
			}
		}
		
		if(day4==null)
		{
			day4='';
		}
		else
		{
			if(response.strReservationNo!=null)
			{
				//day4+='               ,'+response.strReservationNo+','+response.strCheckInNo;
				
					      day4+='                    ,'+response.strReservationNo;
					      //alert(item[i].strRoomNo);
					      toolTipText4+="\n"+response.strGuestName+"\n"+response.strReservationNo+"\n"+response.strRoomNo+"\n"+response.dteArrivalDate+"\n"+response.dteDepartureDate+"\n"+roomStatus+"\n"+response.dblRemainingAmt;
					
			}
		}
		
		if(day5==null)
		{
			day5='';
		}
		else
		{
			if(response.strReservationNo!=null)
			{
				//day5+='               ,'+response.strReservationNo+','+response.strCheckInNo;
			
					      day5+='                    ,'+response.strReservationNo;
					      //alert(item[i].strRoomNo);
					      toolTipText5+="\n"+response.strGuestName+"\n"+response.strReservationNo+"\n"+response.strRoomNo+"\n"+response.dteArrivalDate+"\n"+response.dteDepartureDate+"\n"+roomStatus+"\n"+response.dblRemainingAmt;
				
			}
		}
		
		if(day6==null)
		{
			day6='';
		}
		else
		{
			if(response.strReservationNo!=null)
			{
				//day6+='               ,'+response.strReservationNo+','+response.strCheckInNo;
			
					      day6+='                    ,'+response.strReservationNo;
					      //alert(item[i].strRoomNo);
					      toolTipText6+="\n"+response.strGuestName+"\n"+response.strReservationNo+"\n"+response.strRoomNo+"\n"+response.dteArrivalDate+"\n"+response.dteDepartureDate+"\n"+roomStatus+"\n"+response.dblRemainingAmt;
					
			}
		}
		
		if(day7==null)
		{
			day7='';
		}
		else
		{
			if(response.strReservationNo!=null)
			{
				//day7+='               ,'+response.strReservationNo+','+response.strCheckInNo;
				
					      day7+='                     ,'+response.strReservationNo;
					      //alert(item[i].strRoomNo);
					      toolTipText7+="\n"+response.strGuestName+"\n"+response.strReservationNo+"\n"+response.strRoomNo+"\n"+response.dteArrivalDate+"\n"+response.dteDepartureDate+"\n"+roomStatus+"\n"+response.dblRemainingAmt;
				
			}
		}
				
		row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"text-align: center;width: 100%;\" value='"+roomNo+"' >";

		
		
		var x1=row.insertCell(1);
		if(roomStatus.includes('Dirty'))
			{
			day1='DIRTY                            ,'+roomNo;
			x1.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"width: 100%;height: 20px;color: white;\" value='"+day1+"' onClick='funOnClick(this)' >";			
			
			}
		else
			{
				x1.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"width: 100%;height: 20px;color: white;\" value='"+day1+"' onClick='funOnClick(this)' >";
			}
		if(day1!='')
		{
			x1.bgColor=color;
			x1.title=toolTipText1;
		}
		
		var x2=row.insertCell(2);
		x2.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"width: 100%;color: white;\" value='"+day2+"' onClick='funOnClick(this)' >";
		if(day2!='')
		{
			x2.bgColor=color;
			x2.title=toolTipText2;
		}
		
		var x3=row.insertCell(3);
		x3.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"width: 100%;color: white;\" value='"+day3+"' onClick='funOnClick(this)' >";
		if(day3!='')
		{
			x3.bgColor=color;
			x3.title=toolTipText3;
		}
		
		var x4=row.insertCell(4);
		x4.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"width: 100%;color: white;\" value='"+day4+"' onClick='funOnClick(this)' >";
		if(day4!='')
		{
			x4.bgColor=color;
			x4.title=toolTipText4;
		}
		
		var x5=row.insertCell(5);
		x5.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"width: 100%;color: white;\" value='"+day5+"' onClick='funOnClick(this)' >";
		if(day5!='')
		{
			x5.bgColor=color;
			x5.title=toolTipText5;
		}
		
		var x6=row.insertCell(6);
		x6.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"width: 100%;color: white;\" value='"+day6+"' onClick='funOnClick(this)' >";
		if(day6!='')
		{
			x6.bgColor=color;
			x6.title=toolTipText6;
		}
		
		var x7=row.insertCell(7);
		x7.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"width: 100%;color: white;\" value='"+day7+"' onClick='funOnClick(this)' >";
		if(day7!='')
		{
			x7.bgColor=color;
			x7.title=toolTipText7;
		}
		
	}
	
	var message = "";
	function funOnClick(obj)
	{
		/* var resNo=obj.value;
		resNo=resNo.split(',')[1].trim();
		*/
			
		var color=$(obj).parent().css("backgroundColor");	
		var url='';
		switch(color)
		{
			case 'rgb(255, 79, 83)'://RED-->CHECKED-IN-->OCCUPIED
									  //code=obj.value;
									  code=obj.defaultValue.split(",");
									  var subStr = code[1];
									  var isCheckOk=confirm("Do You Want to Checkout ?"); 
										if(isCheckOk)
										{
									 	 url=getContextPath()+"/frmCheckOut1.html?docCode="+subStr
									 	 window.open(url);
										}
		   						  break;
			case 'rgb(128, 128, 128)'://GREY-->CHECKED-OUT
									  
			      code=obj.defaultValue.split(",");
				  var subStr = code[1];
				  url=getContextPath()+"/frmBillPrinting.html?docCode="+subStr
				  window.open(url);
									  
				  break;
									  
									  
			case 'rgb(255, 255, 0)'://YELLOW-->WAITING
				  code=obj.defaultValue.split(",");
				  var subStr = code[1];
				  url=getContextPath()+"/frmCheckIn1.html?docCode="+subStr
				  window.open(url);

				  break;
				  
				  
			case 'rgb(0, 128, 0)'://GREEN-->CONFIRM
				 code=obj.defaultValue.split(",");
				  var subStr = code[1];
				  url=getContextPath()+"/frmCheckIn1.html?docCode="+subStr
				  window.open(url);

				  break;
			
			case 'rgba(0, 0, 0, 0)'://GREEN-->CONFIRM
				  code=obj.value;
				  //code=code.split(',')[1].trim();
				  // For Room Number
				  var index=obj.parentNode.parentNode.rowIndex;
				  var table1=document.getElementById("tblRoomType");
				  var indexData=table1.rows[index];
				  var roomNo=indexData.cells[0].childNodes[0].defaultValue;
				  var count=indexData.cells[1].cellIndex;
				  
				  var table2=document.getElementById("tblDays");
				  var indexDate=table2.rows[0];
				 // var roomDate=table2.rows[0].cells[index-1].childNodes[0].defaultValue;
				  
				  
				  if(obj.parentNode.cellIndex>1)
					  {
					  var isCheckOk=confirm("Do You Want to go on Reservation ?"); 
						if(isCheckOk)
						{
						  url=getContextPath()+"/frmReservation1.html?docCode="+code+"&roomNo="+roomNo;
						  window.open(url);
						}
					  }
				  else
					  {
					  var isCheckOk=confirm("Do You Want to go on Walkin ?"); 
						if(isCheckOk)
						{
						  url=getContextPath()+"/frmWalkin1.html?docCode="+code+"&roomNo="+roomNo;
						  window.open(url);
						}
					  }
				  

				  break;
				  
			case 'rgb(255, 165, 0)'://Orange-->Dirty
				  code=obj.defaultValue.split(",");
				  var subStr = code[1];
				  var isDirtyOk=confirm("Do You want to mark this room as Clean ?");
				  if(isDirtyOk)
					{		
					  funCallRoomClean(subStr,obj);
					  
						//window.open(getContextPath() + "/cleanRoomStatus.html?checkInNo=" +nextFinalTemp);
					}

				  break;
				  
			/* case 'rgba(0, 0, 0, 0)'://GREEN-->CONFIRM
				  code=obj.value;
				  //code=code.split(',')[1].trim();
				  // For Room Number
				  var index=obj.parentNode.parentNode.rowIndex;
				  var table1=document.getElementById("tblRoomInfo");
				  var indexData=table1.rows[index];
				  var roomNo=indexData.cells[0].childNodes[0].defaultValue;
				  // For Particular Date
				  var table2=document.getElementById("tblDays");
				  var indexDate=table2.rows[0];
				  var roomDate=table2.rows[0].cells[index-1].childNodes[0].defaultValue
				  alert("Proceed to \n\n"+message);
				  url=getContextPath()+"/frmWalkin1.html?docCode="+code+"&roomNo="+roomNo+"&roomDate="+roomDate;
				  window.open(url);

				  break; */
									  
		}					
	}
		
	function funSetData(code)
	{
		switch (fieldName)
		{
			
		}
	}
	
	
	
	function funHelp(transactionName)
	{
		fieldName = transactionName;
		window.showModalDialog("searchform.html?formname=" + transactionName + "&searchText=", "","dialogHeight:600px;dialogWidth:600px;dialogLeft:400px;");
	}
	
	function funCallRoomClean(subStr,obj)
	{/* 
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/getRoomStatusList.html?viewDate=" + viewDate,
			dataType : "json", */
			 		  
		$.ajax({
			type : "GET",
			  url: getContextPath()+ "/cleanRoomStatus.html?checkInNo=" +subStr,
			  dataType : "text",
			  success: function(response) {
			    
				  var index=obj.parentNode.parentNode.rowIndex;
				  var table1=document.getElementById("tblRoomType");
				  var indexData=table1.rows[index];
				  indexData.cells[1].bgColor='';
				  obj.defaultValue='';
				  alert('Room cleaned Successfully');
			    
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
		
		function funFillROomTypeHeaderRowsHeaderRows(key,value)
		{
		
			var table=document.getElementById("tblRoomType");
			table.setAttribute("class", "table table-bordered");
			var rowCount=table.rows.length;
			var row=table.insertRow();
			row.setAttribute("class", "header");
			
			//table.setAttribute("class","table table-bordered");

			var valueArr = value.split('/');
			var inpKey = key;
			row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 0px;width: 85%;height: 20px;\" value='"+key+"' onClick=\"funShowRoomStatusDtl1(this)\">";
			row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 0px;width: 85%;text-align:center;margin-top: 4px;\" value='"+valueArr[1].replace("-", "/")+"' onClick=\"funShowRoomStatusDtl1(this)\">";
		    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 0px;width: 85%;text-align:center;margin-top: 4px;\" value='"+valueArr[2].replace("-", "/")+"' onClick=\"funShowRoomStatusDtl1(this)\">";
			row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 0px;width: 85%;text-align:center;margin-top: 4px;\" value='"+valueArr[3].replace("-", "/")+"' onClick=\"funShowRoomStatusDtl1(this)\">";
			row.insertCell(4).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 0px;width: 85%;text-align:center;margin-top: 4px;\" value='"+valueArr[4].replace("-", "/")+"' onClick=\"funShowRoomStatusDtl1(this)\">";
			row.insertCell(5).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 0px;width: 85%;text-align:center;margin-top: 4px;\" value='"+valueArr[5].replace("-", "/")+"' onClick=\"funShowRoomStatusDtl1(this)\">";
			row.insertCell(6).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 0px;width: 85%;text-align:center;margin-top: 4px;\" value='"+valueArr[6].replace("-", "/")+"' onClick=\"funShowRoomStatusDtl1(this)\">";
			row.insertCell(7).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 0px;width: 85%;text-align:center;margin-top: 4px;\" value='"+valueArr[7].replace("-", "/")+"' onClick=\"funShowRoomStatusDtl1(this)\">";

		/* 	document.getElementById('btnView').style.visibility = 'hidden';
			document.getElementById('btnReset').style.visibility = 'hidden'; */
			
		//	funShowRoomStatusDtl(key);
			//funShowRoomStatusDtl();
		}
</script>


</head>
<body>

	<div id="formHeading">
		<label>Room Status Diary</label>
	</div>

	<br />
	<br />
<div style="float: right; margin-top: -40px;margin-bottom: 10px; margin-right: 40px;">
			<table >
				<tr>
					<td bgcolor="#ff4f53" style="padding-left: 5px;padding-right: 5px;">Occupied</td>
					<td></td>
					<td></td>
					<td bgcolor="Yellow" style="padding-left: 5px;padding-right: 5px;">Waiting</td>
					<td></td>
					<td></td>
					<td bgcolor="Green" style="padding-left: 5px;padding-right: 5px;">Reservation</td>
					<!-- <td></td>
					<td bgcolor="Gray">Checked Out</td> -->
					<td></td>
					<td></td>
					<td bgcolor=Olive style="padding-left: 5px;padding-right: 5px;">Blocked</td>
					<td></td>
					<td></td>
					 <td bgcolor=Gray style="padding-left: 5px;padding-right: 5px;">Checked Out</td>
					<td></td>
					<td></td> 
					<td bgcolor=Orange style="padding-left: 5px;padding-right: 5px;">Dirty</td>
					<td></td>
					<td></td>
				</tr>
			</table>
		</div>
	<s:form name="RoomStatusDiary" method="POST" action="saveRoomMaster.html">
	
		
	
		<div>
			<table class="transTable">
				<tr>
					<td><s:input colspan="1" type="text" id="txtViewDate"  path="dteViewDate" cssClass="calenderTextBox" /></td>
				
			
					<%-- <td> <s:select id="cmbGuestPrefix" path="" cssClass="BoxW124px" onchange="funShowRoomStatusFlash();">
					disabled="true"
							<s:options items="${prefix}"/>
				    	    </s:select></td>--%>
					
					
				</tr>
			</table> 

		
	
		
		
			<table id="tblDays" class="transTable" >
				<!-- 
				<tr>
					<td width="70px">Room No</td>
					<td width="100px">Mon  4 Apr 2016</td>
					<td width="100px">Tue  5 Apr 2016</td>
					<td width="100px">Wed  6 Apr 2016</td>
					<td width="100px">Thur 7 Apr 2016</td>
					<td width="100px">Fri  8 Apr 2016</td>
					<td width="100px">Sat  9 Apr 2016</td>
					<td width="100px">Sun  10 Apr 2016</td>
				</tr>
				 -->
			</table>
			<br/>
	
			<table id="tblRoomType" style="margin-left: 25px;margin-right: 25px;" class="transTable" >
		
		</table>
		
			<br>
			
			<table id="tblRoomInfo" class="transTable" class="collapse show" >
			
			<!-- 
				<tr>
					<td width="70px">1</td>
					<td width="100px" bgcolor="red" bordercolor="black"><label id="lblCol2">Prashant R Ingale</label></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
				</tr>
				
				<tr>
					<td width="70px">2</td>
					<td width="100px"></td>
					<td width="100px" bgcolor="green"><label id="lblCol3">DDDD WWW RRRR</label></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
				</tr>
				
				<tr>
					<td width="70px">3</td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
				</tr>
				
				<tr>
					<td width="50px">4</td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
				</tr>
				
				<tr>
					<td width="50px">5</td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
					<td width="100px"></td>
				</tr>
				
				 -->
				
			</table>
			
		</div>
		
	
		<!-- <div style="margin-left: 20px;">
		<b><label style="font-size: 12px; font-style: Bold;">Free Rooms:</label></b>
		<input  value="0" type="text" id="freeRoomCnt" readonly="readonly" style="width: 40px;" />
		<b><label style="font-size: 12px; font-style: Bold;">Occupied Rooms:</label></b>
		<input value="0" type="text" id="occupiedRoomCnt" readonly="readonly" style="width: 40px;"/>
		<b><label style="font-size: 12px; font-style: Bold;">Dirty Rooms:</label></b>
		<input value="0" type="text" id="dirtyRoomCnt" readonly="readonly" style="width: 40px;"/>
		<b><label style="font-size: 12px; font-style: Bold;">Reservation Rooms:</label></b>
		<input value="0" type="text" id="reservedRoomCnt" readonly="readonly" style="width: 40px;"/>
		<b><label style="font-size: 12px; font-style: Bold;">Blocked Rooms:</label></b>
		<input value="0" type="text" id="blockedRoomCnt" readonly="readonly" style="width: 40px;"/>
		<b><label style="font-size: 12px; font-style: Bold;">Waiting Rooms:</label></b>
		<input value="0" type="text" id="waitingRoomCnt" readonly="readonly" style="width: 40px;"/> 
		
		
	
		
		</div> -->
		<br />
		
		 <p align="center">
			<input type="button" value="View" id="btnView" tabindex="3" class="form_button" onclick="funShowRoomStatusFlash();"/>
			<input type="reset" value="Reset" id="btnReset" class="form_button" onclick="funResetFields()"/>
		</p>
		<br><br>

		<div id="wait"
			style="display: none; width: 60px; height: 60px; border: 0px solid black; position: absolute; top: 60%; left: 55%; padding: 2px;">
			<img
				src="../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif"
				width="60px" height="60px" />
		</div>

	</s:form>
</body>
</html>
