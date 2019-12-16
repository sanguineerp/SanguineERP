<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<script type="text/javascript" src="/resources/js/jquery-1.11.1.min.js">
 

	
	


$(document).ready(function(){
  
});


$(function() 
		{
			
	 var strGuestName = "";
	   $('#id1').val(strGuestName);
			
		});
		
		
</script>
</head>
<body>
	<!--   name="checkDirtyRoom"-->
		<div>
			<s:form action="saveCheckDirtyRoom.html?saddr=${urlHits}" method="POST">
			<table   class="transTable" style="border: 1px solid black;">
			<br />
			<br />
			<br />
			
			
			<thead>
			<tr>
			<td style="padding-left: 100px;"><label>Guest Name: ${guestName}</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
			<td><label>Room No: ${roomNo}</label></td></tr> 
			</thead>
				<thead>
					<tr>
						
						<th style="padding-left: 100px;">House Keeping Service</th>
						<th style="padding-left: 100px;">Completed</th>
						<th style="padding-left: 100px;">Remarks</th>
					</tr>
				</thead>
				
				<c:forEach items="${houseKeepService}" var="tree"
					varStatus="status">
					
					<div><input type="hidden" id="id1" value="${tree.strGuestName}"></div>
					<tr>
						<td style="padding-left: 100px;"><label id="listBean[${status.index}].strHouseKeepName" value="${tree.strHouseKeepName}" >${tree.strHouseKeepName}</label></td>
						<td align="center"><input type="checkbox"
							name="listBean[${status.index}].strAdd"
							<c:if test="${tree.strAdd == 'true'}">checked="checked"</c:if>
							value="true" /></td>
						<td align="center"><input type="text"
							name="listBean[${status.index}].strRemarks" /></td>
							
							<td align="center"><input type="hidden" name="listBean[${status.index}].strHouseKeepCode" value="${tree.strHouseKeepCode}" /></td>	
							<td align="center"><input type="hidden" name="listBean[${status.index}].strRoomCode" value="${tree.strRoomCode}" /></td>	
							
					</tr>
				</c:forEach>									
				
					<%-- <tr>
						<td><label>SUMEET
							</label></td>
																
						<td align="center"><input type="checkbox"
							name="listBean[1].strAdd"
							<c:if test="${house.strAdd == 'true'}">checked="checked"</c:if>
							value="true" /></td>
							
							<td align="center"><input type="text"
							name="listBean[1].strRemarks" /></td>
							
							<td align="center"><input type="hidden"
							name="listBean[1].strHouseKeepCode" /></td>	
					</tr> --%>
			
		
		<br />
		
			</table>
			
					<p align="center">			
					<input type="submit" value="Submit"  tabindex="5" class="form_button"/>
		
		</s:form>
		</div>
	
				
				
					
	
</body>
</html>