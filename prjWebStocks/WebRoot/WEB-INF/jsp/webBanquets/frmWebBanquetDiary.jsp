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

</style>
<script type="text/javascript">
	
var strViewType="normal";
	
	$(function() 
	{
		
		$("#txtViewDate").datepicker({ dateFormat: 'dd-mm-yy' });
		$("#txtViewDate").datepicker('setDate', 'today');
		
		funGetHeaderData();
	});
	
	$(function() {

		  $("#dialog").dialog({
		     autoOpen: false,
		     modal: true
		   });

		  $("#myButton").on("click", function(e) {
		      e.preventDefault();
		      $("#dialog").dialog("open");
		  });

		});
	
	
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
	
	function funShowDiary(strAreaCode)
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
		
		row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 95%; height: 20px;background: #cfe8e8;\" value='"+time+"' onClick='funCellOnClick(this)'>";
		var style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay1!=''){
			var data=strDay1.split("#");
			 style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
			if(data[1]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[1]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[1]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[0]+"' title ='"+strDay1+"'  onClick='funCellOnClick(this)'>";
		}else{
			row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value=''  onClick='funCellOnClick(this)'>";
		}
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay2!=''){
			var data=strDay2.split("#");
			
			if(data[1]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[1]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[1]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[0]+"'  title ='"+strDay2+"' onClick='funCellOnClick(this)'>";
		}else{
			row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value=''  onClick='funCellOnClick(this)'>";
		}
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay3!=''){
			var data=strDay3.split("#");
			 
			if(data[1]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[1]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[1]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[0]+"'  title ='"+strDay3+"' onClick='funCellOnClick(this)'>";
		}else{
			row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='' onClick='funCellOnClick(this)' >";
		}
		
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay4!=''){
			var data=strDay4.split("#");
			 
			if(data[1]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[1]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[1]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			row.insertCell(4).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[0]+"'  title ='"+strDay4+"' onClick='funCellOnClick(this)'>";
		}else{
			row.insertCell(4).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='' onClick='funCellOnClick(this)' >";
		}
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay5!=''){
			var data=strDay5.split("#");
			
			if(data[1]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[1]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[1]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			row.insertCell(5).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[0]+"' onClick='funCellOnClick(this)' >";
		}else{
			row.insertCell(5).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value=''  onClick='funCellOnClick(this)'>";
		}
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay6!=''){
			var data=strDay6.split("#");
			
			if(data[1]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[1]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[1]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			row.insertCell(6).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[0]+"'  title ='"+strDay5+"' onClick='funCellOnClick(this)'>";
		}else{
			row.insertCell(6).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='' onClick='funCellOnClick(this)' >";
		}
		style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer;\"';
		if(strDay7!=''){
			var data=strDay7.split("#");
			
			
			if(data[1]=='Confirm'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: red;\"';
			}else if(data[1]=='Provisional'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: green;\"';
			}
			else if(data[1]=='Waiting'){
				style='\"padding-left: 5px;width: 95%; height: 20px;cursor: pointer; background: yellow;\"';
			}
			row.insertCell(7).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='"+data[0]+"'  title ='"+strDay6+"' onClick='funCellOnClick(this)' >";
		}else{
			row.insertCell(7).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style="+style+" value='' onClick='funCellOnClick(this)' >";
		}
	}
	
	function funCellOnClick(objCell){
		var bookingColor=objCell.style.backgroundColor;
		var customer =objCell.value;
		if(bookingColor=='red') //confirm
		{
			var isCheckOk=confirm("Do You Want Genarate FP"); 
			if(isCheckOk)
			{
				var bookingNo=objCell.title.split("#")[2];
				window.open(getContextPath()+"/rptOpenFunctionProspectus.html?bookingNo="+bookingNo);
			}
			//alert('confirm');
		}
		else if(bookingColor=='green') //provisional
		{
			var isCheckOk=confirm("Do You Want to Payment ?"); 
			if(isCheckOk)
			{
		 	 url=getContextPath()+"/frmPMSPayment.html";
		 	 window.open(url);
			}
		}
		else if(bookingColor=='yellow') //waitlisted
		{
			var isCheckOk=confirm("Do You Want to Payment ?"); 
			if(isCheckOk)
			{
		 	 url=getContextPath()+"/frmPMSPayment.html";
		 	 window.open(url);
			}
		}else{
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
	
	function funAreaOptionSelected(strLocCode,strLocName){
		funGetHeaderData();
		funShowDiary(strLocCode);
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
		strViewType=viewName;
	}
	
	function funDialogButtonclick(){
		
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
							<td><s:input type="text" id="txtViewDate" path="" cssClass="calenderTextBox" /></td>
							<td style="width :400px;"> <button id="myButton">click!</button></td>
							<td bgcolor="ff0000" style="padding-left: 5px;padding-right: 5px;">Confirm</td>
							<td bgcolor="Yellow" style="padding-left: 5px;padding-right: 5px;">Waitlisted</td>
							<td bgcolor="Green" style="padding-left: 5px;padding-right: 5px;">Provisinal</td>
							<td style="width :100px;"> </td>
							<td style="background: white;">
							<span>
								<img  src="../${pageContext.request.contextPath}/resources/images/banquet/normalView.png" title="HOME" height="20px" width="20px" onclick="funDiaryView('normal');">	&nbsp;&nbsp;
								<img  src="../${pageContext.request.contextPath}/resources/images/banquet/dayView.png" title="HOME" height="20px" width="20px" onclick="funDiaryView('day');">	&nbsp;&nbsp;
								<img  src="../${pageContext.request.contextPath}/resources/images/banquet/cancel.png" title="HOME" height="20px" width="20px" onclick="funDiaryView('cancel');"> 	&nbsp;&nbsp;
								<img  src="../${pageContext.request.contextPath}/resources/images/banquet/weekend.png" title="HOME" height="20px" width="20px" onclick="funDiaryView('weekend');">	&nbsp;&nbsp;
							</span>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
				<td>
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
		
			<br>
			<table id="tblBanquetInfo" class="table table-bordered" >
			</table>
			
		</div>
		
		<!-- <div style="position: fixed;">
		 <p align="center" >
			<input type="button" value="View" id="btnView" tabindex="3" class="form_button" onclick="funShowDiary();"/>
			<input type="reset" value="Reset" id="btnReset" class="form_button" onclick="funResetFields()"/>
		</p>
		</div>
	 -->
		<div id="wait"
			style="display: none; width: 60px; height: 60px; border: 0px solid black; position: absolute; top: 60%; left: 55%; padding: 2px;">
			<img
				src="../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif"
				width="60px" height="60px" />
		</div>
		
		<div id="dialog" title="Booking Details">
			  <table id="tblBookingDetails"  cellpadding="0" cellspacing="2"  >				 																																	
						<tr>
						<td>
							<input  type="button" id="btnCancelBooking" value ="Cancle" onclick="funDialogButtonclick(this)" class="button" />
							<input  type="button" id="btnPayment" value ="payment" onclick="funDialogButtonclick(this)" class="button" />
							<input  type="button" id="btnProspect" value ="FP" onclick="funDialogButtonclick(this)" class="button" />
							<input  type="button" id="btnConfirm" value ="Cancle" onclick="funDialogButtonclick(this)" class="button" />
							<input  type="button" id="btnConfirm" value ="Cancle" onclick="funDialogButtonclick(this)" class="button" />
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