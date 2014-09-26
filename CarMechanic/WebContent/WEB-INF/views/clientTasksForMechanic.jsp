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
					$('#clientTasksForMechanic, #clientTaskTableBodyDiv, #clientTasksForMechanicTableContainer').click(function(e) {			
						$('#clientTaskTable tr').removeAttr('customSelector');
						$("#clientTaskTable").find("tr:odd").css("background-color", "#FFFFFF");
					$("#clientTaskTable").find("tr:even").not(':first').css("background-color", "#00B6FF");
					});
					
					if(!($(this).hasClass('heading')))
					{
						$(this).attr('customSelector', 1);
						$(this).css("background-color", "#527BD3");
						$("#rowIdClientTasksHidden").val($(this).attr('rowId'));
					}
				});
				
			});
			
			function checkIfRowIsSelected() {
				var $ = jQuery;
				
				var isSelected = false;
				
				$("#clientTaskTable tr").each(function(e) {
					if(($(this).attr('customSelector')))
						isSelected = true;
				});
				return isSelected;				
			}
			
			function deleteClientTasksForMechanic() {
				var $ = jQuery;
				
				var rowIsSelected = checkIfRowIsSelected();
				
				var isEmpty = checkIfTableIsEmpty();
				
				if(isEmpty == true)
					toastr.info("The list is empty", 'ERROR!');
				else
				{
					if(rowIsSelected == false)
						toastr.info("Please select row!", 'ERROR!');
					else
					{
						openConfirmationDialog("Are you sure you want to delete?", "Warning");
					}
				}
			}
			
			function doDelete() {
				var $ = jQuery;
				
				/*Ajax Akcija - brisi redak i refreshaj tablicu*/
				var json = { "RowId" : $('#rowIdClientTasksHidden').val()};

				$.ajax({
					type: "POST",
					url: "deleteItemByRowId",
					data: json,
					success: function(data) {
						$("#clientTasksForMechanic").attr('action', 'openCLientTaskForMechanicAndAdministrator');
						$("#clientTasksForMechanic").submit();
					}
				});
			}
			
			function saveClientTasksForMechanic() {
				var $ = jQuery;
				
				var isEmpty = checkIfTableIsEmpty();
				
				if(isEmpty == true)
					toastr.info("The list is empty", 'ERROR!');
				else
				{
					/*Za kliknuti ROW_ID uzeti vrijednosti s jquery funkcijom i poslati s JSON u javu i AZURIRAT tabelu s ajax akcijom*/
					fillRowValuesToHidden();
					
					var value = "";
					var tableTrLength = $('#clientTaskTable tr').length;
					var emptyFieldFlag = false;
					
					$('#clientTaskTable tr').each(function(e) {
						if(!($(this).hasClass('heading')))
						{
							if(!isNullEmpty($(this).attr('rowId')))
								value += $(this).attr('rowId') + ":";
							else
								value += ":";
							
							$(this).find('td:not(first) textarea').each(function(e2) {
							
								if(e2 == 2)
								{
									if(!isNullEmpty($(this).val()))
										value += $(this).val();
									else
										emptyFieldFlag = true;
								}
								
								else
								{	
									if(!isNullEmpty($(this).val()))
										value += $(this).val() + ":";
									else
										emptyFieldFlag = true;
								}
							});
							
							if(e < tableTrLength - 1)
								value += ";";
						}
					});
					
					if(emptyFieldFlag == true)
					{
						toastr.info("Some values are missing!", 'ERROR!');
						return false;
					}
					else
					{
						var json = {"parsedDataByDelimiters" : value, "rowIdClientHidden" : $('#rowIdClientHidden').val()};
	
						$.ajax({
							type: "POST",
							url: "saveItemByRowId",
							data: json,
							success: function(data) {
								$("#clientTasksForMechanic").attr('action', 'openCLientTaskForMechanicAndAdministrator');
								$("#clientTasksForMechanic").submit();
							}
						});
					}
				}
			}
			
			function fillRowValuesToHidden() {
				var $ = jQuery;
				
				$("#clientTaskTable tr").each(function(e) {
					if(($(this).attr('customSelector')))
					{
						$($(this).find('td:not(:eq(0))')).each(function(e2) {
							if(e2 == 0)
								$('#taskNameHidden').val($(this).find('textarea').val());
							
							else if(e2 == 1)
								$('#taskDescriptionHidden').val($(this).find('textarea').val());
							
							else if(e2 == 2)
								$('#taskStatusHidden').val($(this).find('textarea').val());
						});
					}
				});
			}
			
			function checkIfTableIsEmpty() {
				var $ = jQuery;
				
				var isEmpty = true;
				
				$("#clientTaskTable tr").each(function(e) {
					if(e > 0)
						isEmpty = false;
				});
				return isEmpty;				
			}
			
			function backToClientList() {
				var $ = jQuery;
				
				$("#clientTasksForMechanic").attr('action', 'loginScreen');
				$("#clientTasksForMechanic").submit();
			}
			
			function newClientTasksForMechanic() {
				var $ = jQuery;
			
				$(function () {
				    newRow = "<tr>" +
				    	"<td align='center'></td>" +
			            "<td><textarea name='taskName' id='taskName'></textarea></td>" +
			            "<td><textarea name='taskDescription' id='taskDescription'></textarea></td>" +
			            "<td><textarea name='taskDescription' id='taskStatus'></textarea></td>" +
				    "</tr>";
				    $('#clientTaskTable > tbody > tr:last').after(newRow);
				});
			}
			
			function openConfirmationDialog(message, title) {
				var $ = jQuery;
				
				$('<div id="Warning"></div>').appendTo('body').html('<div><h4>'+message+'</h4></div>').dialog({
				    modal: true,
				    title: title,
				    autoOpen: false,
				    closeOnEscape: false,
				    width: 345,
				    height: 164,
					zIndex: 10002,
				    resizable: false,
					buttons: {
						Ok: function() {
							doDelete();
							$(this).dialog("close");
						},
						Cancel: function() {
							$(this).dialog("close");
						}
					 }
				 });
				
				$('#Warning').dialog('open');
			}
			
		</script>
		
		<style type="text/css">
			.js-hide { display: none; }
		</style>
	</head>
	
	<body>
		<form:form id="clientTasksForMechanic" method="POST" modelAttribute="clientTaskForMechanicModel">
			<input id="usernameHidden" name="usernameHidden" type="hidden" value="${clientTaskForMechanicModel.username}" />
			<input id="passwordHidden" name="passwordHidden" type="hidden" value="${clientTaskForMechanicModel.password}" />
			<input id="rememberHidden" name="rememberHidden" type="hidden" value="${clientTaskForMechanicModel.rememberCbox}" />
			<input id="rowIdClientTasksHidden" name="rowIdClientTasksHidden" type="hidden" value="" />
			<input id="rowIdClientHidden" name="rowIdClientHidden" type="hidden" value="${clientTaskForMechanicModel.rowIdClient}" />
			<input id="sessionUserName" name="sessionUserName" type="hidden" value="${sessionUsername}" />
			<input id="sessionPassword" name="sessionPassword" type="hidden" value="${sessionPassword}" />
			<input id="idApplicationUser" name="idApplicationUser" type="hidden" value="${idApplicationUser}" />
			
			<!-- Hidden za vrijednosti textarea oznacenog retka -->
			<input id="taskNameHidden" name="taskNameHidden" type="hidden" value="" />
			<input id="taskDescriptionHidden" name="taskDescriptionHidden" type="hidden" value="" />
			<input id="taskStatusHidden" name="taskStatusHidden" type="hidden" value="" />
			
			<div id="clientTasksForMechanicTableContainer" >
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
							<c:when test="${not empty clientTaskForMechanicModel.tasks}">
								<c:forEach items="${clientTaskForMechanicModel.tasks}" var="task" varStatus="status">
							        <tr rowId = ${task.rowId}>
							            <td align="center">${status.count}</td>
							            <td><textarea name="tasks[${status.index}].taskName" id="taskName">${task.taskName}</textarea></td>
							            <td><textarea name="tasks[${status.index}].taskDescription" id="taskDescription">${task.taskDescription}</textarea></td>
							            <td><textarea name="tasks[${status.index}].taskStatus" id="taskStatus">${task.taskStatus}</textarea></td>
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
					<button type="button" name="backClientTaskForMechanicButtonId" id="backClientTaskForMechanicButtonId" value="Back" onclick="backToClientList();">Back</button>
					<button type="button" name="newClientTaskForMechanicButtonId" id="newClientTaskForMechanicButtonId" value="New" onclick="newClientTasksForMechanic();">New</button>
					<button type="button" name="deleteClientTaskForMechanicButtonId" id="deleteClientTaskForMechanicButtonId" value="Delete" onclick="deleteClientTasksForMechanic();">Delete</button>
					<button type="button" name="saveClientTaskForMechanicButtonId" id="saveClientTaskForMechanicButtonId" value="Save" onclick="saveClientTasksForMechanic();">Save</button>
					<button type="button" name="logoutClientTaskForMechanicButtonId" id="logoutClientTaskForMechanicButtonId" value="Logout" onclick="logOut();">Logout</button>
	       		</div>
	        </div>
	        
		</form:form>
		<div id="result"></div>
	</body>
</html>