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
.ui-tooltip 
{   
   white-space: pre-wrap;      
}

.red
{
    background-color: red;
    width: 50%;
    height: 5px;
}
.gray
{
    background-color: gray;
    width: 50%;
    height: 5px;
}
.ten
{
    color: #ffffff;
}

</style>
<script type="text/javascript">
	
	var fieldName;
	
	/* $(document).ready(function(){
		
		
		$(document).ajaxStart(function() {
				$("#wait").css("display", "block");
			});
			$(document).ajaxComplete(function() {
				$("#wait").css("display", "none");
			});
	}); */
	
	$(function() 
	{
		$( tblRoomInfo ).tooltip();
		
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
			/*  beforeSend : function(){
				 $("#wait").css("display","block");
		    },
		    complete: function(){
		    	 $("#wait").css("display","none");
		    }, */
			
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
	
	
	function funShowRoomStatusDtl()
	{
		var viewDate=$("#txtViewDate").val();
			
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/getRoomStatusDtlList.html?viewDate=" + viewDate,
			dataType : "json",
			
			beforeSend : function(){
				 $("#wait").css("display","block");
		    },
		    complete: function(){
		    	 $("#wait").css("display","none");
		    },
			
			success : function(response){
				funRemoveDetailTableRows();
				
				$.each(response, function(i,item)
				{
					funFillRoomStatusRows(item.strRoomNo,item.strDay1,item.strDay2,item.strDay3,item.strDay4,item.strDay5,item.strDay6,item.strDay7,item.strRoomStatus,item);
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
		funShowRoomStatusDtl();
	}
	
	
	
	function funFillRoomStatusRows(roomNo,day1,day2,day3,day4,day5,day6,day7,roomStatus,response)
	{
		var table=document.getElementById("tblRoomInfo");
		var rowCount=table.rows.length;
		var row=table.insertRow();
		var color='';
		var toolTipText1="",toolTipText2="",toolTipText3="",toolTipText4="",toolTipText5="",toolTipText6="",toolTipText7="";
		if(roomStatus=='Waiting')
		{
			color='Yellow';
		}
		else if(roomStatus=='Confirmed')
		{
			color='Green';
		}
		else if(roomStatus=='Occupied')
		{
			color='Red';
		}
		else if(roomStatus=='Checked Out')
		{
			color='Gray';
		}
		else if(roomStatus=='Blocked')
		{
			color='Olive';
		}
		
		/*if(response.strReservationNo!=null)
		{
			//toolTipText=response.strReservationNo;
			$.each(response.mapGuestListPerDay, function(i,item)
			 {
				$.each(item, function(j,item1)
				 {
					toolTipText=item1.strRoomNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate;
				 });
			 });
		}
		*/
		var count=1;
		if(day1==null)
		{
			day1='';
		}
		else
		{
			if(response.strReservationNo!=null)
			{
				//day1+='               ,'+response.strReservationNo+','+response.strCheckInNo;
				
				$.each(response.mapGuestListPerDay, function(i,item)
				 {
					if(i==0)
					{
						$.each(item, function(j,item1)
						{
						  //day1+=' '+item1.strRoomNo;
					      var temp=day1.trim();
					      var finalTemp=temp.indexOf("/");
					      var nextFinalTemp = temp.substring(finalTemp,temp.length);
					      finalTemp=temp.substring(0,finalTemp);
					      if(roomStatus.includes("Waiting"))
					    	  {
					    	  toolTipText1+=" \n"+item1.strGuestName+"\n"+response.strReservationNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    	  }
					      else
					    	  {
					    	  
					      if(nextFinalTemp == "/")
				    	  {
					    	  roomStatus="Occupied"
							  toolTipText1+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
				    	  }
					      else
				    	  {
					    	  if(count==1)
				    		  {
						    	  roomStatus="Checked Out"
					    		  toolTipText1+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
				    		  }
						      else
						      {
					    	  	roomStatus="Occupied"
						    	toolTipText1+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    	  }
					    	  count++;
				    	  }
					    	  }
						});
					}
				 });
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
				$.each(response.mapGuestListPerDay, function(i,item)
				 {
					if(i==1)
					{
						$.each(item, function(j,item1)
						{
							var temp=day2.trim();
						      var finalTemp=temp.indexOf(" ");
						      var nextFinalTemp = temp.substring(finalTemp,temp.length);
						      finalTemp=temp.substring(0,finalTemp);
						      if(roomStatus.includes("Waiting"))
					    	  {
					    	  toolTipText2+=" \n"+item1.strGuestName+"\n"+response.strReservationNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    	  }
					      else
					    	  {
						      if(nextFinalTemp == "/")
					    	  {
						    	  roomStatus="Occupied"
								  toolTipText2+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    	  }
						      else
					    	  {
						    	  if(count==1)
					    		  {
							    	  roomStatus="Checked Out"
						    		  toolTipText2+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    		  }
							      else
							      {
						    	  	roomStatus="Occupied"
							    	toolTipText2+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
						    	  }
						    	  count++;
					    	  }
					    	  }
						});
					}
					
				 });
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
				$.each(response.mapGuestListPerDay, function(i,item)
				 {
					if(i==2)
					{
						$.each(item, function(j,item1)
						{
							var temp=day3.trim();
						      var finalTemp=temp.indexOf(" ");
						      var nextFinalTemp = temp.substring(finalTemp,temp.length);
						      finalTemp=temp.substring(0,finalTemp);
						      if(roomStatus.includes("Waiting"))
					    	  {
					    	  toolTipText3+=" \n"+item1.strGuestName+"\n"+response.strReservationNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    	  }
					      else
					    	  {
						      if(nextFinalTemp == "/")
					    	  {
						    	  roomStatus="Occupied"
								  toolTipText3+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    	  }
						      else
					    	  {
						    	  if(count==1)
					    		  {
							    	  roomStatus="Checked Out"
						    		  toolTipText3+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    		  }
							      else
							      {
						    	  	roomStatus="Occupied"
							    	toolTipText3+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
						    	  }
						    	  count++;
					    	  }
					    	  }
						});
					}
					
				 });
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
				$.each(response.mapGuestListPerDay, function(i,item)
				 {
					if(i==3)
					{
						$.each(item, function(j,item1)
						{
							var temp=day4.trim();
						      var finalTemp=temp.indexOf(" ");
						      var nextFinalTemp = temp.substring(finalTemp,temp.length);
						      finalTemp=temp.substring(0,finalTemp);
						      if(roomStatus.includes("Waiting"))
					    	  {
					    	  toolTipText4+=" \n"+item1.strGuestName+"\n"+response.strReservationNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    	  }
					      else
					    	  {
						      if(nextFinalTemp == "/")
					    	  {
						    	  roomStatus="Occupied"
								  toolTipText4+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    	  }
						      else
					    	  {
						    	  if(count==1)
					    		  {
							    	  roomStatus="Checked Out"
						    		  toolTipText4+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    		  }
							      else
							      {
						    	  	roomStatus="Occupied"
							    	toolTipText4+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
						    	  }
						    	  count++;
					    	  }  
					    	  }
						});
					}
				 });
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
				$.each(response.mapGuestListPerDay, function(i,item)
				 {
					if(i==4)
					{
						$.each(item, function(j,item1)
						{
							var temp=day5.trim();
						      var finalTemp=temp.indexOf(" ");
						      var nextFinalTemp = temp.substring(finalTemp,temp.length);
						      finalTemp=temp.substring(0,finalTemp);
						      if(roomStatus.includes("Waiting"))
					    	  {
					    	  toolTipText5+=" \n"+item1.strGuestName+"\n"+response.strReservationNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    	  }
					      else
					    	  {
						      if(nextFinalTemp == "/")
					    	  {
						    	  roomStatus="Occupied"
								  toolTipText5+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    	  }
						      else
					    	  {
						    	  if(count==1)
					    		  {
							    	  roomStatus="Checked Out"
						    		  toolTipText5+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    		  }
							      else
							      {
						    	  	roomStatus="Occupied"
							    	toolTipText5+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
						    	  }
						    	  count++;
					    	  }
					    	  }
						});	
					}
				 });
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
				$.each(response.mapGuestListPerDay, function(i,item)
				 {
					if(i==5)
					{
						$.each(item, function(j,item1)
						{
							var temp=day6.trim();
						      var finalTemp=temp.indexOf(" ");
						      var nextFinalTemp = temp.substring(finalTemp,temp.length);
						      finalTemp=temp.substring(0,finalTemp);
						      if(roomStatus.includes("Waiting"))
					    	  {
					    	  toolTipText6+=" \n"+item1.strGuestName+"\n"+response.strReservationNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    	  }
					      else
					    	  {
						      if(nextFinalTemp == "/")
					    	  {
						    	  roomStatus="Occupied"
								  toolTipText6+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    	  }
						      else
					    	  {
						    	  if(count==1)
					    		  {
							    	  roomStatus="Checked Out"
						    		  toolTipText6+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    		  }
							      else
							      {
						    	  	roomStatus="Occupied"
							    	toolTipText6+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
						    	  }
						    	  count++;
					    	  }
					    	  }
						});
					}
					
				 });
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
				$.each(response.mapGuestListPerDay, function(i,item)
				 {
					if(i==6)
					{
						$.each(item, function(j,item1)
						{
							var temp=day7.trim();
						      var finalTemp=temp.indexOf(" ");
						      var nextFinalTemp = temp.substring(finalTemp,temp.length);
						      finalTemp=temp.substring(0,finalTemp);
						      if(roomStatus.includes("Waiting"))
					    	  {
					    	  toolTipText7+=" \n"+item1.strGuestName+"\n"+response.strReservationNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    	  }
					      else
					    	  {
						      if(nextFinalTemp == "/")
					    	  {
						    	  roomStatus="Occupied"
								  toolTipText7+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    	  }
						      else
					    	  {
						    	  if(count==1)
					    		  {
							    	  roomStatus="Checked Out"
						    		  toolTipText7+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
					    		  }
							      else
							      {
						    	  	roomStatus="Occupied"
							    	toolTipText7+=" \n"+item1.strGuestName+"\n"+response.strCheckInNo+"\n"+item1.dteArrivalDate+"\n"+item1.dteDepartureDate+"\n"+roomStatus;
						    	  }
						    	  count++;
					    	  }
					    	  }
						});
					}
				 });
			}
		}
				
		row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \" style=\"padding-left: 5px;width:100%;\" value='"+roomNo+"' >";
		
		

		
		/*if(response.dteArrivalDate!=null)
		{
			toolTipText+="\n"+response.dteArrivalDate;
		}	
		if(response.dteDepartureDate!=null)
		{
			toolTipText+="\n"+response.dteDepartureDate;
		}	
		*/
		
		if(roomStatus!='Blocked')
		{
			
	
		var x1=row.insertCell(1);
		var dayTrim1=day1.trim();
		if(dayTrim1.includes("/"))
			{
			 var dayValue1 = dayTrim1.substring(0,dayTrim1.length-1);
			}
		else
			{
			var dayValue1 = dayTrim1;
			}
	    
	    var firstName = dayValue1.substring(0,dayValue1.indexOf("/"));
	    var secondName = dayValue1.substring(dayValue1.indexOf("/")+1,dayValue1.length);
	   	if(dayValue1.includes("/"))
	    	{
		//x1.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 2px;\" value='"+dayValue1+"' onClick='funOnClick(this)' >";
	    }
	    else
	    	{
	    	x1.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"width:90%; \" value='"+dayValue1+"' onClick='funOnClick(this)' >";
	    	x1.width="100px";
	    	} 
		if(day1!='')
		{
			
			if(dayValue1.includes("/"))
				{
				
				var rowdata= '<span class=\'gray\'>'+"<input readonly=\"readonly\" class=\"Box \"  style=\"margin-left: -9px;width:110%;\" value='"+firstName+"' onClick='funOnClick(this)' >";
				
				rowdata=rowdata+'<span class=\'red\'>'+"<input readonly=\"readonly\" class=\"Box \"  style=\"margin-left: -9px;width: 110%;\" value='"+secondName+"' onClick='funOnClick(this)' >";
				x1.innerHTML=rowdata;
				x1.width="100px";
				/* var size = x1.innerHTML.length; */
				//x1.bgColor = 'Red';
				//x1.bgColor='Red';
				
				x1.title=toolTipText1;
				
				
				
				}
			else
				{
				x1.bgColor=color;
				x1.title=toolTipText1;	
				x1.width="100px";
				}
			
		}
		
		
		var x2=row.insertCell(2);
		var dayTrim2=day2.trim();
		if(dayTrim2.includes("/"))
		{
		 var dayValue2 = dayTrim2.substring(0,dayTrim2.length-1);
		}
	else
		{
		var dayValue2 = dayTrim2;
		}
    
    var firstName = dayValue2.substring(0,dayValue2.indexOf("/"));
    var secondName = dayValue2.substring(dayValue2.indexOf("/")+1,dayValue1.length);
		x2.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"width: 90%;\" value='"+dayValue2+"' onClick='funOnClick(this)' >";
		if(day2!='')
		{
			x2.bgColor=color;
			x2.title=toolTipText2;
		}
		
		var x3=row.insertCell(3);
		var dayTrim3=day3.trim();
		if(dayTrim3.includes("/"))
		{
		 var dayValue3 = dayTrim1.substring(0,dayTrim3.length-1);
		}
	else
		{
		var dayValue3 = dayTrim3;
		}
    
    var firstName = dayValue3.substring(0,dayValue3.indexOf("/"));
    var secondName = dayValue3.substring(dayValue3.indexOf("/")+1,dayValue3.length);
		x3.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"width: 90%;\" value='"+dayValue3+"' onClick='funOnClick(this)' >";
		if(day3!='')
		{
			x3.bgColor=color;
			x3.title=toolTipText3;
		}
		
		var x4=row.insertCell(4);
		var dayTrim4=day4.trim();
		if(dayTrim4.includes("/"))
		{
		 var dayValue4 = dayTrim4.substring(0,dayTrim4.length-1);
		}
	else
		{
		var dayValue4 = dayTrim4;
		}
    
    var firstName = dayValue4.substring(0,dayValue4.indexOf("/"));
    var secondName = dayValue4.substring(dayValue4.indexOf("/")+1,dayValue4.length);
		x4.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"width: 100%;\" value='"+dayValue4+"' onClick='funOnClick(this)' >";
		if(day4!='')
		{
			x4.bgColor=color;
			x4.title=toolTipText4;
		}
		
		var x5=row.insertCell(5);
		var dayTrim5=day5.trim();
		if(dayTrim5.includes("/"))
		{
		 var dayValue5 = dayTrim5.substring(0,dayTrim5.length-1);
		}
	else
		{
		var dayValue5 = dayTrim5;
		}
    
    var firstName = dayValue5.substring(0,dayValue5.indexOf("/"));
    var secondName = dayValue5.substring(dayValue5.indexOf("/")+1,dayValue5.length);
		x5.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"width: 100%;\" value='"+dayValue5+"' onClick='funOnClick(this)' >";
		if(day5!='')
		{
			x5.bgColor=color;
			x5.title=toolTipText5;
		}
		var x6=row.insertCell(6);
		var dayTrim6=day6.trim();
		if(dayTrim6.includes("/"))
		{
		 var dayValue6 = dayTrim6.substring(0,dayTrim6.length-1);
		}
	else
		{
		var dayValue6 = dayTrim6;
		}
    
    var firstName = dayValue6.substring(0,dayValue6.indexOf("/"));
    var secondName = dayValue6.substring(dayValue6.indexOf("/")+1,dayValue6.length);
		x6.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"width: 100%;\" value='"+dayValue6+"' onClick='funOnClick(this)' >";

		if(day6!='')
		{
			x6.bgColor=color;
			x6.title=toolTipText6;
		}
		var x7=row.insertCell(7);
		var dayTrim7=day7.trim();
		if(dayTrim7.includes("/"))
		{
		 var dayValue7 = dayTrim7.substring(0,dayTrim7.length-1);
		}
	else
		{
		var dayValue7 = dayTrim7;
		}
    
    var firstName = dayValue7.substring(0,dayValue7.indexOf("/"));
    var secondName = dayValue7.substring(dayValue7.indexOf("/")+1,dayValue7.length);
		x7.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"width: 100%;\" value='"+dayValue7+"' onClick='funOnClick(this)' >";
		if(day7!='')
		{
			x7.bgColor=color;
			x7.title=toolTipText7;
		}
		}
		else
			{
			var x1=row.insertCell(1);
			x1.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 2px;width: 100%;\" value='"+day1+"' onClick='funOnClick(this)' >";
			x1.bgColor=color;
			toolTipText1+=" \n"+response.strGuestName+"\n"+response.strCheckInNo+"\n";
			x1.title=toolTipText1;
			
			
			var x2=row.insertCell(1);
			x2.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 2px;width: 100%;\" value='"+day1+"' onClick='funOnClick(this)' >";
			x2.bgColor=color;
			toolTipText2+=" \n"+response.strGuestName+"\n"+response.strCheckInNo+"\n";
			x2.title=toolTipText2;
			
			var x3=row.insertCell(1);
			x3.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 2px;width: 100%;\" value='"+day1+"' onClick='funOnClick(this)' >";
			x3.bgColor=color;
			toolTipText3+=" \n"+response.strGuestName+"\n"+response.strCheckInNo+"\n";
			x3.title=toolTipText3;
			
			var x4=row.insertCell(1);
			x4.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 2px;width: 100%;\" value='"+day1+"' onClick='funOnClick(this)' >";
			x4.bgColor=color;
			toolTipText4+=" \n"+response.strGuestName+"\n"+response.strCheckInNo+"\n";
			x4.title=toolTipText4;
			
			var x5=row.insertCell(1);
			x5.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 2px;width: 100%;\" value='"+day1+"' onClick='funOnClick(this)' >";
			x5.bgColor=color;
			toolTipText5+=" \n"+response.strGuestName+"\n"+response.strCheckInNo+"\n";
			x5.title=toolTipText5;
			
			var x6=row.insertCell(1);
			x6.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 2px;width: 100%;\" value='"+day1+"' onClick='funOnClick(this)' >";
			x6.bgColor=color;
			toolTipText6+=" \n"+response.strGuestName+"\n"+response.strCheckInNo+"\n";
			x6.title=toolTipText6;
			
			var x7=row.insertCell(1);
			x7.innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 2px;width: 100%;\" value='"+day1+"' onClick='funOnClick(this)' >";
			x7.bgColor=color;
			toolTipText7+=" \n"+response.strGuestName+"\n"+response.strCheckInNo+"\n";
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
			case 'rgb(255, 0, 0)'://RED-->CHECKED-IN-->OCCUPIED
									  code=obj.value;
									  //code=code.split(',')[2].trim();
									  url=getContextPath()+"/frmCheckOut1.html?docCode="+code
									  window.open(url);
			
		   						  break;
			case 'rgb(128, 128, 128)'://GREY-->CHECKED-OUT
									  
				 code=obj.value;
				  //code=code.split(',')[2].trim();
				  url=getContextPath()+"/frmBillPrinting.html?docCode="+code
				  window.open(url);
									  
				  break;
									  
									  
			case 'rgb(255, 255, 0)'://YELLOW-->WAITING
				  code=obj.value;
				  //code=code.split(',')[1].trim();
				  url=getContextPath()+"/frmCheckIn1.html?docCode="+code
				  window.open(url);

				  break;
				  
				  
			case 'rgb(0, 128, 0)'://GREEN-->CONFIRM
				  code=obj.value;
				  code=code.split(',')[1].trim();
				  url=getContextPath()+"/frmReservation1.html?docCode="+code
				  window.open(url);

				  break;
			
			case 'rgba(0, 0, 0, 0)'://GREEN-->CONFIRM
				  code=obj.value;
				  //code=code.split(',')[1].trim();
				  // For Room Number
				  var index=obj.parentNode.parentNode.rowIndex;
				  var table1=document.getElementById("tblRoomInfo");
				  var indexData=table1.rows[index];
				  var roomNo=indexData.cells[0].childNodes[0].defaultValue;
				  var count=indexData.cells[1].cellIndex;
				  
				  if(obj.parentNode.cellIndex>1)
					  {
						  alert("Proceed to Reservation \n\n"+message);
						  url=getContextPath()+"/frmReservation1.html?docCode="+code+"&roomNo="+roomNo;
						  window.open(url);
					  }
				  else
					  {
						  alert("Proceed to Walkin \n\n"+message);
						  url=getContextPath()+"/frmWalkin1.html?docCode="+code+"&roomNo="+roomNo;
						  window.open(url);
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
</script>


</head>
<body>

	<div id="formHeading">
		<label>Room Status Diary</label>
	</div>

	<br />
	<br />

	<s:form name="RoomStatusDiary" method="POST" action="saveRoomMaster.html">
	
		<div>
			<table class="transTable">
				<tr>
					<td><s:input colspan="1" type="text" id="txtViewDate" disabled="true" path="dteViewDate" cssClass="calenderTextBox" /></td>
					
					
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
			
			<br>
			
			<table id="tblRoomInfo" class="transTable">
			
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
		
		<br><br>
		
		
		
		<div>
			<table >
				<tr>
					<td bgcolor="Red">Occupied</td>
					<td></td>
					<td bgcolor="Yellow">Waiting</td>
					<td></td>
					<td bgcolor="Green">Confirmed</td>
					<!-- <td></td>
					<td bgcolor="Gray">Checked Out</td> -->
					<td></td>
					<td bgcolor=Olive>Blocked</td>
					<td></td>
					<td bgcolor=Gray>Checked Out</td>
					<td></td>
				</tr>
			</table>
		</div>
		
		
		<br><br><br><br><br><br>
		
		
		
		<p align="center">
			<input type="button" value="View" tabindex="3" class="form_button" onclick="funShowRoomStatusFlash();"/>
			<input type="reset" value="Reset" class="form_button" onclick="funResetFields()"/>
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
