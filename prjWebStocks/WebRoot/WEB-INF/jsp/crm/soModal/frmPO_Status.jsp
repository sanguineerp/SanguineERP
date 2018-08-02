<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<script>
     
       

</script>
</head>





<body onload=""  >
	<div id="formHeading">
		<label>Purchase Order status</label> <br />
	</div>
	<br />
	<br />
	<br />
	<br />
	<s:form name="Purchase Order status" method="POST" action="">
	<c:choose>
    <c:when test="${tableRows != null}">
		<div class="transTable"
			style="background-color: #a4d7ff; border: 1px solid #ccc; display: block; margin: auto;">
          	
			<table style="width: 100%; border: 1px solid black !important; table-layout: fixed;"
				class="transTable  col7-right col10-right col11-right">

				<tr bgcolor="#72BEFC">


					<th width="10%">PI_Code</th>

					<th width="10%">Reference Code</th>
					<th width="10%">PO Date</th>
					<th width="9%">Supplier Code</th>
					<th width="12%">Supplier Name</th>
					<th width="8%">Product Code</th>
					<th width="5%">Part No.</th>
					<th width="15%">Product Name</th>
					
					<th width="5%">UOM</th>
					<th width="7%">Ordered Qty</th>
					<th width="5%">Received Qty</th>
					<th width="7%">Price</th>
					<th width="5%">Pending Qty</th>
					<th width="8%">Status</th>

				</tr>


				<c:forEach items="${tableRows}" var="modelvar" varStatus="status1">
                          			
<%-- 				//		<c:forEach items="${modelvar}" var="modelvar1" varStatus="status2"> --%>
						<tr>
							<c:forEach items="${modelvar}" var="modelvar2"
								varStatus="status3">
							
								<td>${modelvar2}</td>
							</c:forEach>
						</tr>
						</c:forEach>
					
<%-- 				</c:forEach> --%>
			</table>
		
		</div>
		 </c:when>    
    <c:otherwise>
       No Intend Item Found For this Sales Order 
        <br />
    </c:otherwise>
</c:choose>
	</s:form>



</body>
</html>