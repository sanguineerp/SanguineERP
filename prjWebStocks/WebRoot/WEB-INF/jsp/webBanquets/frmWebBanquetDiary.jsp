<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
  border: 0.5px solid #dbd9d9; 
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
  display: block;
    
}

/* .Box { background: inherit; border: 0px solid #060006; outline:0; padding-left: 00px;  font-size:11px;
	font-weight: bold; font-family: trebuchet ms,Helvetica,sans-serif; } */
/* .table .header td:after {

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

} */

.table .header.active td:after {
  content: "\2212";
  
}

.button {
  background-color: #73cae4;
  border: none;
  color: white;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  margin: 4px 2px;
  cursor: pointer;
  width: 100px;height: 28px; white-space: normal;
}


.tooltip {
  position: relative;
  display: inline-block;
}

.tooltiptext {
  visibility: hidden;
  width: 120px;
  background-color: #555;
  color: #fff;
  text-align: center;
  border-radius: 6px;
  padding: 5px 0;
  position: absolute;
  z-index: 1;
  bottom: -125%;
  left: 50%;
  margin-left: -20px;
  opacity: 0;
  transition: opacity 0.3s;
}

.tooltip:hover .tooltiptext {
  visibility: visible;
  opacity: 1;
}


.tooltiptextleft {
  visibility: hidden;
  width: 120px;
  background-color: #555;
  color: #fff;
  text-align: center;
  border-radius: 6px;
  padding: 5px 0;
  position: absolute;
  z-index: 1;
  bottom: 110%;
  left: 0%;
  margin-left: -50px;
  opacity: 0;
  transition: opacity 0.3s;
}

.tooltip:hover .tooltiptextleft {
  visibility: visible;
  opacity: 1;
}

.btn {
  border: none;
  outline: none;  
  cursor: pointer;
  font-size: 18px;
}

/* Style the active class, and buttons on mouse-over */
.active, .btn:hover {
  background-color: gainsboro;
  color: white;
}
</style>
<script type="text/javascript">
	
var strViewType="normal";
var selectedCell;	
var bookingNo;
var customerNo
var gstrLocCode;

	$(function() 
	{
		$( tblBanquetInfo ).tooltip();
		$("#txtViewDate").datepicker({ dateFormat: 'dd-mm-yy' });
		$("#txtViewDate").datepicker('setDate', 'today');
		$('#tdDay').text($('#txtViewDate').val());
		funGetHeaderData();
	});
	
	
	window.onclick = function(event) {
	  if (event.target.value == 'Close') {
		  var modal = document.getElementById("dialog");
		  modal.style.display = "none";
	  }
	}
	
	
	
	 
	 function openDialog(){
		document.getElementById("dialog").style.display = "block"
		$("#dialog").dialog({
	            width: 600,
	            height: 200,
	             open: function(){

	             },
		 		
	        });
	 }
	
	function funRemoveTableRows(table)
	{
		var table = document.getElementById(table);
		var rowCount = table.rows.length;
		while(rowCount>1)
		{
			table.deleteRow(1);
			rowCount--;
		}
	}
	function funRemoveAllRows(table)
	{
		var table = document.getElementById(table);
		var rowCount = table.rows.length;
		while(rowCount>0)
		{
			table.deleteRow(0);
			rowCount--;
		}
	}
	
	function funGetHeaderData()
	{
		var viewDate=$("#txtViewDate").val();
			
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/getBanquetDiaryHeader.html?viewDate=" + viewDate+"&viewType="+strViewType,
			dataType : "json",
			 beforeSend : function(){
				 $("#wait").css("display","block");
		    },
		    complete: function(){
		    	 $("#wait").css("display","none");
		    },
			
			success : function(response){ 
				funRemoveAllRows('tblBanquetInfo');
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
	
	function funGetDayViewHeaderData(bookingNo)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/getBanquetDiaryDayViewHeader.html?",
			dataType : "json",
			 beforeSend : function(){
				 $("#wait").css("display","block");
		    },
		    complete: function(){
		    	 $("#wait").css("display","none");
		    },
			
			success : function(response){ 
				funRemoveTableRows("tblBanquetInfo");					
				if(response.length>0){						
					funFillDayViewHeaderRows(response);				
					
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
	
	function funGetWeekendView(gstrLocCode)
	{
		var viewDate=$("#txtViewDate").val();
			
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/getBanquetWeekendViewBookingDetails.html?viewDate=" + viewDate+"&viewType="+strViewType+"&areaCode="+gstrLocCode,
			dataType : "json",
			 beforeSend : function(){
				 $("#wait").css("display","block");
		    },
		    complete: function(){
		    	 $("#wait").css("display","none");
		    },
			
			success : function(response){ 
				funRemoveTableRows("tblBanquetInfo");
				$.each(response, function(i,item)
				{
					funFillBanquetDairy(response[i].strDay,response[i].strDay1,response[i].strDay2,response[i].strDay3,response[i].strDay4,response[i].strDay5,response[i].strDay6,response[i].strDay7);
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
		
	
	function funShowDiaryNormalView(strAreaCode)
	{
		var viewDate=$("#txtViewDate").val();
			
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/getBanquetBookingDetails.html?viewDate=" + viewDate+"&viewType="+strViewType+"&areaCode="+strAreaCode,
			dataType : "json",
			 beforeSend : function(){
				 $("#wait").css("display","block");
		    },
		    complete: function(){
		    	 $("#wait").css("display","none");
		    },
			
			success : function(response){ 
				funRemoveTableRows("tblBanquetInfo");
				$.each(response, function(i,item)
				{
					funFillBanquetDairy(response[i].strDay,response[i].strDay1,response[i].strDay2,response[i].strDay3,response[i].strDay4,response[i].strDay5,response[i].strDay6,response[i].strDay7);
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
	
	function funShowDailyView(strAreaCode)
	{
		var viewDate=$("#txtViewDate").val();
			
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/getBanquetDailyViewBookingDetails.html?viewDate=" + viewDate+"&viewType="+strViewType+"&areaCode="+strAreaCode,
			dataType : "json",
			 beforeSend : function(){
				 $("#wait").css("display","block");
		    },
		    complete: function(){
		    	 $("#wait").css("display","none");
		    },
			
			success : function(response){ 
				funRemoveTableRows("tblBanquetInfo");
				$.each(response, function(i,item)
				{
					funFillDailyViewBanquetDairy(response[i].strDay,response[i].strDay1,response[i].strDay2,response[i].strDay3,response[i].strDay4,response[i].strDay5,response[i].strDay6,response[i].strDay7);
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
	
	function funShowDiaryCancelView(strAreaCode)
	{
		var viewDate=$("#txtViewDate").val();
			
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/getBanquetCancelBookingDetails.html?viewDate=" + viewDate+"&viewType="+strViewType+"&areaCode="+strAreaCode,
			dataType : "json",
			 beforeSend : function(){
				 $("#wait").css("display","block");
		    },
		    complete: function(){
		    	 $("#wait").css("display","none");
		    },
			
			success : function(response){ 
				funRemoveTableRows("tblBanquetInfo");
				$.each(response, function(i,item)
				{
					funFillBanquetDairy(response[i].strDay,response[i].strDay1,response[i].strDay2,response[i].strDay3,response[i].strDay4,response[i].strDay5,response[i].strDay6,response[i].strDay7);
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
	
	
	function funFillBanquetDairy(time,strDay1,strDay2,strDay3,strDay4,strDay5,strDay6,strDay7){
		
		var table=document.getElementById("tblBanquetInfo");
		var rowCount=table.rows.length;
		var row=table.insertRow();
		strDay1=funCheckNull(strDay1);
		strDay2=funCheckNull(strDay2);
		strDay3=funCheckNull(strDay3);
		strDay4=funCheckNull(strDay4);
		strDay5=funCheckNull(strDay5);
		strDay6=funCheckNull(strDay6);
		strDay7=funCheckNull(strDay7);
		
		row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 95%; height: 20px;background: #cfe8e8;\" value='"+time+"' onClick='funCellOnClick(this)' >";
		var style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay1!=''){
			var data=strDay1.split("#");
			 style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
			if(data[2]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[2]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[2]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			else if(data[2]=='Cancel'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: orange;\"';
			}
			row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[1]+"'  name='"+strDay1+"' title='"+data+"'  onclick='funCellOnClick(this)'>";
		}else{
			row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value=''  onClick='funCellOnClick(this)'>";
		}
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay2!=''){
			var data=strDay2.split("#");
			
			if(data[2]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[2]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[2]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			else if(data[2]=='Cancel'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: orange;\"';
			}
			row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[1]+"' name='"+strDay2+"' title='"+data+"' onclick='funCellOnClick(this)' >";
		}else{
			row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value=''  onClick='funCellOnClick(this)'>";
		}
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay3!=''){
			var data=strDay3.split("#");
			 
			if(data[2]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[2]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[2]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			else if(data[2]=='Cancel'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: orange;\"';
			}
			row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[1]+"'  name='"+strDay3+"' title='"+data+"' onclick='funCellOnClick(this)' >";
		}else{
			row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='' onClick='funCellOnClick(this)' >";
		}
		
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay4!=''){
			var data=strDay4.split("#");
			 
			if(data[2]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[2]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[2]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			else if(data[2]=='Cancel'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: orange;\"';
			}
			row.insertCell(4).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[1]+"'  name='"+strDay4+"' title='"+data+"' onclick='funCellOnClick(this)' >";
		}else{
			row.insertCell(4).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='' onClick='funCellOnClick(this)' >";
		}
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay5!=''){
			var data=strDay5.split("#");
			
			if(data[2]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[2]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[2]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			else if(data[2]=='Cancel'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: orange;\"';
			}
			row.insertCell(5).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[1]+"' name='"+strDay5+"' title='"+data+"' onclick='funCellOnClick(this)' >";
		}else{
			row.insertCell(5).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value=''  onClick='funCellOnClick(this)'>";
		}
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay6!=''){
			var data=strDay6.split("#");
			
			if(data[2]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[2]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[2]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			else if(data[2]=='Cancel'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: orange;\"';
			}
			row.insertCell(6).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[1]+"'  name='"+strDay6+"' title='"+data+"' onclick='funCellOnClick(this)' >";
		}else{
			row.insertCell(6).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='' onClick='funCellOnClick(this)' >";
		}
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay7!=''){
			var data=strDay7.split("#");
			
			
			if(data[2]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[2]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[2]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			else if(data[2]=='Cancel'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: orange;\"';
			}
			row.insertCell(7).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[1]+"'  name='"+strDay7+"' title='"+data+"' onclick='funCellOnClick(this)'>";
		}else{
			row.insertCell(7).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='' onClick='funCellOnClick(this)' >";
		}
		
		
	}
	
function funFillDailyViewBanquetDairy(time,strDay1,strDay2,strDay3,strDay4,strDay5,strDay6,strDay7){
		
		var table=document.getElementById("tblBanquetInfo");
		var rowCount=table.rows.length;
		var row=table.insertRow();
		strDay1=funCheckNull(strDay1);
		strDay2=funCheckNull(strDay2);
		strDay3=funCheckNull(strDay3);
		strDay4=funCheckNull(strDay4);
		strDay5=funCheckNull(strDay5);
		strDay6=funCheckNull(strDay6);
		strDay7=funCheckNull(strDay7);
		
		row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 95%; height: 20px;background: #cfe8e8;\" value='"+time+"' onClick='funCellOnClick(this)' >";
		var style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay1!=''){
			var data=strDay1.split("#");
			 style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
			if(data[2]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[2]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[2]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			else if(data[2]=='Cancel'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: orange;\"';
			}
			row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[1]+"'  name='"+strDay1+"' title='"+data+"'  onclick='funCellOnClick(this)'>";
		}else{
			row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value=''  onClick='funCellOnClick(this)'>";
		}
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay2!=''){
			var data=strDay2.split("#");
			
			if(data[2]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[2]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[2]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			else if(data[2]=='Cancel'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: orange;\"';
			}
			row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[1]+"' name='"+strDay2+"' title='"+data+"' onclick='funCellOnClick(this)' >";
		}else{
			row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value=''  onClick='funCellOnClick(this)'>";
		}
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay3!=''){
			var data=strDay3.split("#");
			 
			if(data[2]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[2]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[2]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			else if(data[2]=='Cancel'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: orange;\"';
			}
			row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[1]+"'  name='"+strDay3+"' title='"+data+"' onclick='funCellOnClick(this)' >";
		}else{
			row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='' onClick='funCellOnClick(this)' >";
		}
		
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay4!=''){
			var data=strDay4.split("#");
			 
			if(data[2]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[2]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[2]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			else if(data[2]=='Cancel'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: orange;\"';
			}
			row.insertCell(4).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[1]+"'  name='"+strDay4+"' title='"+data+"' onclick='funCellOnClick(this)' >";
		}else{
			row.insertCell(4).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='' onClick='funCellOnClick(this)' >";
		}
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay5!=''){
			var data=strDay5.split("#");
			
			if(data[2]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[2]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[2]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			else if(data[2]=='Cancel'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: orange;\"';
			}
			row.insertCell(5).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[1]+"' name='"+strDay5+"' title='"+data+"' onclick='funCellOnClick(this)' >";
		}else{
			row.insertCell(5).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value=''  onClick='funCellOnClick(this)'>";
		}
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay6!=''){
			var data=strDay6.split("#");
			
			if(data[2]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[2]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[2]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			else if(data[2]=='Cancel'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: orange;\"';
			}
			row.insertCell(6).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[1]+"'  name='"+strDay6+"' title='"+data+"' onclick='funCellOnClick(this)' >";
		}else{
			row.insertCell(6).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='' onClick='funCellOnClick(this)' >";
		}
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay7!=''){
			var data=strDay7.split("#");
			
			
			if(data[2]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[2]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[2]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			else if(data[2]=='Cancel'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: orange;\"';
			}
			row.insertCell(7).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[1]+"'  name='"+strDay7+"' title='"+data+"' onclick='funCellOnClick(this)'>";
		}else{
			row.insertCell(7).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='' onClick='funCellOnClick(this)' >";
		}
		
		
	}
	
	function funCellOnClick(objCell){			
		selectedCell=objCell;
		var bookingColor=objCell.style.backgroundColor;
		var customer =objCell.value;
		
		bookingNo=objCell.name.split("#")[0];
		funGetCustomerBookingDtl(bookingNo);
					
			if(bookingColor=='red') //confirm
			{
				openDialog();
				/* var isCheckOk=confirm("Do You Want Genarate FP"); 
				if(isCheckOk)
				{
					var bookingNo=objCell.title.split("#")[2];
					window.open(getContextPath()+"/rptOpenFunctionProspectus.html?bookingNo="+bookingNo);
				} */
				//alert('confirm');
			}
			else if(bookingColor=='green') //provisional
			{
				openDialog();
			
				/* var isCheckOk=confirm("Do You Want to Payment ?"); 
				if(isCheckOk)
				{
			 	 url=getContextPath()+"/frmPMSPayment.html";
			 	 window.open(url);
				} */
			}
			else if(bookingColor=='yellow') //waitlisted
			{			
				openDialog();
				
				/* var isCheckOk=confirm("Do You Want to Payment ?"); 
				if(isCheckOk)
				{
			 	 url=getContextPath()+"/frmPMSPayment.html";
			 	 window.open(url);
				} */
				
			}else{
				$('#dialog').dialog('close');
				var isCheckOk=confirm("Do You Want to Book ?"); 
				if(isCheckOk)
				{
			 	 url=getContextPath()+"/frmBanquetBooking.html";
			 	 window.open(url);
				}
			}
		
	}	
	
	function funCheckNull(strData){
		if(strData==null){
			strData='';
		}
		return strData;
	}
	function funFillHeaderRows(obj)
	{
		var table=document.getElementById("tblBanquetInfo");
		table.setAttribute("class", "table table-bordered");
		var rowCount=table.rows.length;
		var row=table.insertRow();
		
		row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;height: 30Px;\" value='Time' >";
		row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;height: 30Px;\" value='"+obj[0]+"' >";
	    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;height: 30Px;\" value='"+obj[1]+"' >";
		row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;height: 30Px;\" value='"+obj[2]+"' >";
		row.insertCell(4).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;height: 30Px;\" value='"+obj[3]+"' >";
		row.insertCell(5).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;height: 30Px;\" value='"+obj[4]+"' >";
		row.insertCell(6).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;height: 30Px;\" value='"+obj[5]+"' >";
		row.insertCell(7).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;height: 30Px;\" value='"+obj[6]+"' >";
		
	}	
	
	function funFillDayViewHeaderRows(obj)
	{
		document.getElementById("tblBanquetInfo").deleteRow(0);
		var table=document.getElementById("tblBanquetInfo");
		table.setAttribute("class", "table table-bordered");
		var rowCount=table.rows.length;		
		var row=table.insertRow();		
		row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 119.3px;height: 30Px;\" value='Time' >";
		for(var i=0;i<obj.length;i++)
			{
			row.insertCell(i+1).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 119.3px;height: 30Px; background-color: honeydew;\" value='"+obj[i]+"' >";
			}	
	}
	function funFillDialogTableHeaderRows()
	{
		var table=document.getElementById("tblCustomerInfo");
		table.setAttribute("class", "table table-bordered");
		var rowCount=table.rows.length;
		var row=table.insertRow();
		
		row.insertCell(0).innerHTML=  "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 80px;height: 30Px;background:#73cae4;\" value='Booking No' >";
		row.insertCell(1).innerHTML=  "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100px;height: 30Px;background:#73cae4;\" value='Contact Name' >";
		row.insertCell(2).innerHTML=  "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 70px;height: 30Px;background:#73cae4;\" value='Mobile NO' >";
		row.insertCell(3).innerHTML=  "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100px;height: 30Px;background:#73cae4;\" value='Email ID' >";
		row.insertCell(4).innerHTML=  "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 70px;height: 30Px;background:#73cae4;\" value='From Date' >";
		row.insertCell(5).innerHTML=  "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 70px;height: 30Px;background:#73cae4;\" value='To Date' >";
		row.insertCell(6).innerHTML=  "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 150px;height: 30Px;background:#73cae4;\" value='Time' >";
		row.insertCell(7).innerHTML=  "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100px;height: 30Px;background:#73cae4;\" value='Area Name'>";
		row.insertCell(8).innerHTML=  "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 70px;height: 30Px;background:#73cae4;\" value='PAX' >";
		row.insertCell(9).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 80px;height: 30Px;background:#73cae4;\" value='Status' >";	
		row.insertCell(10).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100px;height: 30Px;background:#73cae4;\" value='Function Name' >";
		row.insertCell(11).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 80px;height: 30Px;background:#73cae4;\" value='Booked Date' >";
					

	}
	
	function funFillDialogTableRows(obj)
	{
		var table=document.getElementById("tblCustomerInfo");
		table.setAttribute("class", "table table-bordered");
		var rowCount=table.rows.length;
		var row=table.insertRow();		
		
		row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 80px;height: 30Px;\" value='"+obj[0]+"' >";
		row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100px;height: 30Px;\" value='"+obj[1]+"' >";
		row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 70px;height: 30Px;\" value='"+obj[2]+"' >";
		row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100px;height: 30Px;\" value='"+obj[3]+"' >";
		row.insertCell(4).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 70px;height: 30Px;\" value='"+obj[4]+"' >";
		row.insertCell(5).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 70px;height: 30Px;\" value='"+obj[5]+"' >";
		row.insertCell(6).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 150px;height: 30Px;\" value=' "+obj[6]+"  To  "+obj[7]+"'>";
		row.insertCell(7).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100px;height: 30Px;\" value='"+obj[8]+"' >";
		row.insertCell(8).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 70px;height: 30Px;\" value='"+obj[9]+" To "+obj[10]+"'>";
		row.insertCell(9).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 80px;height: 30Px;\" value='"+obj[11]+"' >";
		row.insertCell(10).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100px;height: 30Px;\" value='"+obj[12]+"' >";		
		row.insertCell(11).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 80px;height: 30Px;\" value='"+obj[13]+"' >";		
		
	}
	
	function funAreaOptionSelected(strLocCode,strLocName){		
		gstrLocCode=strLocCode;
		funDiaryView(strViewType);
		$('#dialog').dialog('close');
		//funGetHeaderData();
		//funShowDiaryNormalView(strLocCode);
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
	
	function funDiaryView(viewName){
		if(gstrLocCode===undefined)
			{
				alert("Please select location");			
			}
		else
			{
			strViewType=viewName;
			document.getElementById("normal").className = "btn";
			document.getElementById("day").className = "btn";
			document.getElementById("cancel").className = "btn";
			document.getElementById("weekend").className = "btn";
			
			switch(strViewType){				
			case 'normal' :			
				funGetHeaderData();
				$('#tdconfirm').show();
				$('#tdwait').show();
				$('#tdprov').show();
				$('#tdcancel').hide();
				$('#tdAreaButtons').show();	
				$('#tdDay').hide();					
				$('#tdwidth').css('width', '100px');
				document.getElementById("normal").className = "btn active";
				funShowDiaryNormalView(gstrLocCode);
				 break;
			
			case 'day' :	
				$('#tdconfirm').show();
				$('#tdwait').show();
				$('#tdprov').show();
				$('#tdcancel').hide();
				$('#tdAreaButtons').hide();
				$('#tdDay').show();
				$('#tdwidth').css('width', '100px');
				document.getElementById("day").className = "btn active";
				funGetDayViewHeaderData();
				funShowDailyView(gstrLocCode);			
				 break;
			
			case 'cancel' :			
				funGetHeaderData();			
				$('#tdconfirm').hide();
				$('#tdwait').hide();
				$('#tdprov').hide();
				$('#tdcancel').show();
				$('#tdAreaButtons').show();
				$('#tdDay').hide();
				$('#tdwidth').css('width', '228px');
				document.getElementById("cancel").className = "btn active";
				funShowDiaryCancelView(gstrLocCode);
				 break;
			
			case 'weekend' :		
				$('#tdconfirm').show();
				$('#tdwait').show();
				$('#tdprov').show();
				$('#tdcancel').hide();
				$('#tdAreaButtons').show();	
				$('#tdDay').hide();
				$('#tdwidth').css('width', '100px');
				document.getElementById("weekend").className = "btn active";
				funGetHeaderData();
				funGetWeekendView(gstrLocCode);
				 break;
			
				}
			$('#dialog').dialog('close');
			}
		
		
		
	}
	
	function funDialogButtonclick(value){
		//var buttonVal=button.value;
		switch(value){
			
		case 'Cancel' :			
			 url=getContextPath()+"/frmRoomCancellation.html?strBookingNo="+bookingNo;
		 	 window.open(url);
			 break;
		
		case 'Payment' :
			 funGetInvoiceForPaymentCode(bookingNo);
			
			 break;
			
		case 'FP' :			
		     window.open(getContextPath()+"/rptOpenFunctionProspectus.html?bookingNo="+bookingNo);			
			 break;
		
		case 'Pro-Invoice' :
			funGetInvoiceCode(bookingNo);
			 
			 break;
			
		case 'Close' :			
			 $('#dialog').dialog('close');			 
			 break;
			
		default :			
			break;				
		}
		
	}
	
	function funGetCustomerBookingDtl(bookingNo)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/getCustomerBookingDetails.html?bookingNo="+bookingNo,
			dataType : "json",
			 beforeSend : function(){
				 $("#wait").css("display","block");
		    },
		    complete: function(){
		    	 $("#wait").css("display","none");
		    },
			
			success : function(response){ 
				funRemoveTableRows("tblCustomerInfo");				
				if(response.length>0){
					
					var table=document.getElementById("tblCustomerInfo");
					var rowCount=table.rows.length;
					var row=table.insertRow();
					funFillDialogTableHeaderRows();					
					 $.each(response, function(cnt,item)
								{
									funFillDialogTableRows(item);	
									customerNo=item[14];
									
								}); 
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
		
	function funGetInvoiceCode(bookingNo)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/getInvoiceCodeExist.html?bookingNo="+bookingNo,
			dataType : "json",
			 beforeSend : function(){
				 $("#wait").css("display","block");
		    },
		    complete: function(){
		    	 $("#wait").css("display","none");
		    },
			
			success : function(response){ 								
				if(response.length>0){
					 $.each(response, function(cnt,item)
								{
									url=getContextPath()+"/rptProFormaInvoiceSlipFormat5Report.html?rptInvCode="+item[0]+"&rptInvDate="+item[1];
								 	window.open(url);									
								}); 
				}
				else
					{
						url=getContextPath()+"/frmProFormaInvoice.html?bookingNo="+bookingNo+"&CustomerCode="+customerNo;
				 	 	window.open(url);
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
	function funGetInvoiceForPaymentCode(bookingNo)
	{
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/getInvoiceCodeExist.html?bookingNo="+bookingNo,
			dataType : "json",
			 beforeSend : function(){
				 $("#wait").css("display","block");
		    },
		    complete: function(){
		    	 $("#wait").css("display","none");
		    },
			
			success : function(response){ 								
				if(response.length>0){
					 $.each(response, function(cnt,item)
					{
						url=getContextPath()+"/frmPMSPayment.html?invoiceCode="+item[0]+"&date="+item[1]+"&strBookingNo="+item[2];
						window.open(url);									
					}); 
				}
				else
					{
						 url=getContextPath()+"/frmPMSPayment.html?sstrBookingNo="+bookingNo;
					 	 window.open(url);
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
	 
	function funDateChange()
	{
		  $( "#tdDay" ).text($("#txtViewDate").val());
	}
	
	
	
	
</script>


</head>
<body>

	<div id="formHeading">
		<label>Banquet Diary</label>
	</div>

	<br />
	<s:form name="banquetDiary" method="POST" action="ShowDiary.html" style="height: 700px;">
		<div style="height: 100%;">
			<table class="transTable">
				<tr>
					<td>
					<table>
						<tr>
							<td><s:input type="text" id="txtViewDate" path="" cssClass="calenderTextBox" height="25px" onchange="funDateChange()"/></td>
							<td style="width :350px;"> <!-- <button id="myButton">click!</button> --></td>
							<td id="tdconfirm" bgcolor="ff0000" style="padding-left: 5px;padding-right: 5px;">Confirm</td>
							<td id="tdwait" bgcolor="Yellow" style="padding-left: 5px;padding-right: 5px;">Waitlisted</td>
							<td id="tdprov" bgcolor="Green" style="padding-left: 5px;padding-right: 5px;">Provisinal</td>
							<td id="tdcancel" bgcolor="Orange" style="padding-left: 5px;padding-right: 5px; display:none;  width: 40px" >Cancel</td>
							<td id="tdwidth" style="width :100px;"> </td>
							<td style="background: white;">
							<span>
							<div id="myDIV">
								<div class="tooltip">									
									<img  src="../${pageContext.request.contextPath}/resources/images/banquet/normalView.png" id="normal" title="HOME" height="18px" width="20px" class="btn active" onclick="funDiaryView('normal');">	&nbsp;&nbsp;
										<span class="tooltiptextleft">Normal View</span>
								</div>
								<div class="tooltip">	
									<img  src="../${pageContext.request.contextPath}/resources/images/banquet/dayView.png" id="day" title="HOME" height="18px" width="20px" class="btn" onclick="funDiaryView('day');">	&nbsp;&nbsp;
									<span class="tooltiptextleft">Day View</span>
								</div>
								<div class="tooltip">								
									<img  src="../${pageContext.request.contextPath}/resources/images/banquet/cancel.png" id="cancel" title="HOME" height="18px" width="20px" class="btn" onclick="funDiaryView('cancel');"> 	&nbsp;&nbsp;
									<span class="tooltiptextleft">Cancel</span>
								</div>
								<div class="tooltip">	
									<img  src="../${pageContext.request.contextPath}/resources/images/banquet/weekend.png" id="weekend" title="HOME" height="18px" width="20px" class="btn" onclick="funDiaryView('weekend');">	&nbsp;&nbsp;
									<span class="tooltiptextleft">Weekend</span>
								</div>
								</div>
							</span>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
				<td id="tdAreaButtons">
				<div id="divAreaButtons" style="text-align: right; height:40px; overflow-x: auto; overflow-y: hidden; width: 100%;">
					 	<table id="tblAreaButtons"  cellpadding="0" cellspacing="2"  >				 																																	
								<tr>							
									<c:forEach var="objAreaButtons" items="${command.jsonArrForLocationButtons}"  varStatus="varAreaButtons">
											<td style="padding-right: 3px;">
												<input  type="button" id="${objAreaButtons.strLocCode}"  value="${objAreaButtons.strLocName}" tabindex="${varAreaButtons.getIndex()}" onclick="funAreaOptionSelected('${objAreaButtons.strLocCode}','${objAreaButtons.strLocName}')" class="button"/>
											</td>
									</c:forEach>																						
							    </tr>																																				 									   				   									   									   						
						</table>		
			 	</div>
				</td>
				</tr>
			</table> 
				<label id="tdDay" style="text-align: center; display: none; margin-left:132px; font-size:19px; margin-top:13px; margin-bottom:-1px; height:24px; overflow-x: auto; overflow-y: hidden; width: 100px" class="button" >Male</label>	
			<br>
			<table id="tblBanquetInfo" class="table table-bordered"   >
			</table>			
		</div>
		<div id="wait"
			style="display: none; width: 60px; height: 60px; border: 0px solid black; position: absolute; top: 60%; left: 55%; padding: 2px;">
			<img
				src="../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif"
				width="60px" height="60px" />
		</div>
		
		<div id="dialog" title="Booking Details" style="display: none;">
			  <table id="tblBookingDetails"  cellpadding="0" cellspacing="2"  >				 																																	
						<tr>
						<td>						
							<div class="tooltip">
							<img onclick="funDialogButtonclick('Cancel')" src="resources/images/banquet/cancelpayment.jpg">
							<span class="tooltiptext">Cancel Booking</span>
							</div>
							<div class="tooltip">
							<img onclick="funDialogButtonclick('Payment')" src="resources/images/banquet/payment.jpg"> 
							<span class="tooltiptext">Payment</span>
							</div>
							<div class="tooltip">
							<img onclick="funDialogButtonclick('FP')" src="resources/images/banquet/FunctionPlan.jpg"> 
							<span class="tooltiptext">Function Prospectus</span>
							</div>
							<div class="tooltip">
							<img onclick="funDialogButtonclick('Pro-Invoice')" src="resources/images/banquet/ProInvoice.jpg"> 
							<span class="tooltiptext">ProForma Invoice</span>
							</div>
							<div class="tooltip">
							<img onclick="funDialogButtonclick('Close')" src="resources/images/banquet/closeIcons.jpg">   
							<span class="tooltiptext">Close</span>
							</div>
	
					 <!-- < <input  type="button" id="btnCancelBooking" value ="Cancle" onclick="funDialogButtonclick(this)" class="button" />
							<input  type="button" id="btnPayment" value ="Payment" onclick="funDialogButtonclick(this)" class="button" />
							<input  type="button" id="btnProspect" value ="FP" onclick="funDialogButtonclick(this)" class="button" />
							<input  type="button" id="btnProInvoice" value ="Pro-Invoice" onclick="funDialogButtonclick(this)" class="button" />
							<input  type="button" id="btnClose" value ="Close" onclick="funDialogButtonclick(this)" class="button" /> -->
						</td>
						</tr>		
						<tr>
						<td>
							<table id="tblCustomerInfo"> </table>
						</td>
						</tr>
																																				 									   				   									   									   						
			</table>	
		</div>

	</s:form>
</body>
</html>



<%-- 
	<table>
			<tr>
		 <td>
				<table id="tblTimes" style="width: 100px;">
					<tr>
					<th>Time</th>
					</tr>
					<%
					String myTime="00:01",tableRow="";
					for(int i=0;i<24;i++){
					
					%>
					<tr><td>
					<% 
					 SimpleDateFormat df = new SimpleDateFormat("HH:mm");
					 Date d = df.parse(myTime); 
					 Calendar cal = Calendar.getInstance();
					 cal.setTime(d);
					 cal.add(Calendar.HOUR, 1);
					 String newTime = df.format(cal.getTime());
					 tableRow=myTime+"-"+newTime;
					 out.println(tableRow);
					 myTime=newTime;
					}
					%></td></tr>
					
				</table>
			</td> 
				<td>
				
				</td>
				
			</tr>
			
			</table> --%>