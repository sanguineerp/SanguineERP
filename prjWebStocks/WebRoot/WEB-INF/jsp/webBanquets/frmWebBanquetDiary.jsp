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
		
		$("#txtViewDate").datepicker({ dateFormat: 'dd-mm-yy' });
		$("#txtViewDate").datepicker('setDate', 'today');
		
		funGetHeaderData();
	});
	

	
	
	function funRemoveTableRows(table)
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
			url : getContextPath()+ "/getRoomStatusList.html?viewDate=" + viewDate,
			dataType : "json",
			 beforeSend : function(){
				 $("#wait").css("display","block");
		    },
		    complete: function(){
		    	 $("#wait").css("display","none");
		    },
			
			success : function(response){ 
				funRemoveTableRows("tblHeaders");
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
	
	function funShowDiary()
	{
		var viewDate=$("#txtViewDate").val();
			
		$.ajax({
			type : "GET",
			url : getContextPath()+ "/getBanquetBookingDetails.html?viewDate=" + viewDate,
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
		
		row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+time+"' >";
		row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+strDay1+"' >";
	    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+strDay2+"' >";
		row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+strDay3+"' >";
		row.insertCell(4).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+strDay4+"' >";
		row.insertCell(5).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+strDay5+"' >";
		row.insertCell(6).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+strDay6+"' >";
		row.insertCell(7).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+strDay7+"' >";
		
		
	}
		
	function funFillHeaderRows(obj)
	{
		var table=document.getElementById("tblHeaders");
		var rowCount=table.rows.length;
		var row=table.insertRow();
		
		row.insertCell(0).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='Time' >";
		row.insertCell(1).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj[0]+"' >";
	    row.insertCell(2).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj[1]+"' >";
		row.insertCell(3).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj[2]+"' >";
		row.insertCell(4).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj[3]+"' >";
		row.insertCell(5).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj[4]+"' >";
		row.insertCell(6).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj[5]+"' >";
		row.insertCell(7).innerHTML= "<input readonly=\"readonly\" class=\"Box \"  style=\"padding-left: 5px;width: 100%;\" value='"+obj[6]+"' >";
		
		//funGetRoomTypeAndStatus();
		
		//funShowRoomStatusDtl();
	}
	
	function funAreaOptionSelected(strLocCode,strLocName){
		funShowDiary();
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
					<td><s:input colspan="1" type="text" id="txtViewDate" path="" cssClass="calenderTextBox" /></td>
				</tr>
				<tr>
				<td>
				<div id="divAreaButtons" style="text-align: right; height:50px; overflow-x: auto; overflow-y: auto; width: 100%;">
					 	<table id="tblAreaButtons"  cellpadding="0" cellspacing="2"  >				 																																	
								<tr>							
									<c:forEach var="objAreaButtons" items="${command.jsonArrForLocationButtons}"  varStatus="varAreaButtons">
											<td style="padding-right: 3px;">
												<input  type="button" id="${objAreaButtons.strLocCode}"  value="${objAreaButtons.strLocName}" tabindex="${varAreaButtons.getIndex()}"  style="width: 100px;height: 30px; white-space: normal;"   onclick="funAreaOptionSelected('${objAreaButtons.strLocCode}','${objAreaButtons.strLocName}')" class="btn btn-outline-secondary"/>
											</td>
									</c:forEach>																						
							    </tr>																																				 									   				   									   									   						
						</table>			
			 	</div>
				</td>
				</tr>
			</table> 
		
			
		
			<table id="tblHeaders"  style="padding-left: 30px;" class="transTable">
					
			</table>
			<br>
			<table id="tblBanquetInfo" class="transTable" class="collapse show" >
			</table>
			
		</div>
		
		<div style="position: fixed;">
		 <p align="center" >
			<input type="button" value="View" id="btnView" tabindex="3" class="form_button" onclick="funShowDiary();"/>
			<input type="reset" value="Reset" id="btnReset" class="form_button" onclick="funResetFields()"/>
		</p>
		</div>
	
		<div id="wait"
			style="display: none; width: 60px; height: 60px; border: 0px solid black; position: absolute; top: 60%; left: 55%; padding: 2px;">
			<img
				src="../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif"
				width="60px" height="60px" />
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