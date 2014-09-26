<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
	
		<meta http-equiv='cache-control' content='no-cache'>
		<meta http-equiv='expires' content='0'>	
		<meta http-equiv='pragma' content='no-cache'>
		
	    <title>Task List</title>
	    
	   	<jsp:include page="javascriptData.jsp" />
	    <jsp:include page="cssData.jsp" />
		
		<script type="text/javascript">
			jQuery(document).ready(function() {
	
				var $ = jQuery;
				
				$('#clientTaskTable tr').live('click', function(e) {
				
					$("#clientTaskTable").find("tr:odd").css("background-color", "#FFFFFF");
					$("#clientTaskTable").find("tr:even").not(':first').css("background-color", "#00B6FF");
				
					$('#clientTaskTable tr').each(function(e) {
						if(!($(this).hasClass('heading')))
						{
							$(this).removeAttr('customSelector');
						}
					});
					
					/*Klik sa strane, deselektira oznaceni redak*/
					$('#clientTask').click(function(e) {			
						$('#clientTaskTable tr').removeAttr('customSelector');
						$('#clientTaskTable tr:odd:gt(0)').css("background-color", "#FFFFFF");
						$('#clientTaskTable tr:even:gt(0)').css("background-color", "#00B6FF");
					});
					
					if(!($(this).hasClass('heading')))
					{
						$(this).attr('customSelector', 1);
						$(this).css("background-color", "#527BD3");
					}
				});
			});
		</script>
		
		<style type="text/css">
			.js-hide { display: none; }
		</style>
	</head>
	
	<body>
		<form:form id="clientTasks" method="POST" modelAttribute="clientTaskModel">
			<input id="usernameHidden" name="usernameHidden" type="hidden" value="${clientTaskModel.username}"/>
			<input id="passwordHidden" name="passwordHidden" type="hidden" value="${clientTaskModel.password}"/>
			<input id="rememberHidden" name="rememberHidden" type="hidden" value="${clientTaskModel.rememberCbox}"/>
			<input id="idApplicationUser" name="idApplicationUser" type="hidden" value="${idApplicationUser}" />
			
			<div id="clientTaskTableContainer" >
				<h2 style="color: black; margin-left: 18px;">Task List</h2>
				<div id="clientTaskTableBodyDiv">
					<table id="clientTaskTable" class="carMechanicTable" >
					    <tr class="heading">
					        <th>No.</th>
					        <th>Task Name</th>
					        <th>Description</th>
					        <th>Status</th>
					    </tr>
					    
					    <c:choose>
							<c:when test="${not empty clientTaskModel.tasks}">
								<c:forEach items="${clientTaskModel.tasks}" var="task" varStatus="status">
							        <tr rowId = ${task.rowId}>
							            <td align="center">${status.count}</td>
							            <td><label name="tasks[${status.index}].taskName">${task.taskName}</label></td>
							            <td><label name="tasks[${status.index}].taskDescription">${task.taskDescription}</label></td>
							            <td><label name="tasks[${status.index}].taskStatus">${task.taskStatus}</label></td>
							        </tr>
						  		</c:forEach>
							</c:when>
						 
							<c:otherwise>
							    <h3 style="color: black;">The list is empty</h3>
							</c:otherwise>
						</c:choose>
					</table>
				</div>
				
				<div id="clientTaskButtonContainer">
					<button type="button" name="logoutClientTaskButtonId" id="logoutClientTaskButtonId" value="Logout" onclick="logOut();">Logout</button>
	       		</div>
	        </div>
	        
		</form:form>
		<div id="result"></div>
	</body>
</html>