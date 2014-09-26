<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<head>
		<meta http-equiv='cache-control' content='no-cache'>
		<meta http-equiv='expires' content='0'>	
		<meta http-equiv='pragma' content='no-cache'>
		
	    <title>Client List</title>
	    
	    <jsp:include page="javascriptData.jsp" />
	    <jsp:include page="cssData.jsp" />
	    
		<script type="text/javascript">
			jQuery(document).ready(function() {
	
				var $ = jQuery;
				
				$('#carMechanicTableClients tr').live('click', function(e) {
					
					$("#carMechanicTableClients").find("tr:odd").css("background-color", "#FFFFFF");
					$("#carMechanicTableClients").find("tr:even").not(':first').css("background-color", "#00B6FF");
					
					$("#carMechanicTableClients tr").each(function(e) {
						if(!($(this).hasClass('heading')))
						{
							$(this).removeAttr('customSelector');
						}
					});
					
					/*Klik sa strane, deselektira oznaceni redak*/
					$('#loginScreenId, #carMechanicTableBodyDiv, #carMechanicTableContainer').click(function(e) {			
						$('#carMechanicTableClients tr').removeAttr('customSelector');
						$("#carMechanicTableClients").find("tr:odd").css("background-color", "#FFFFFF");
						$("#carMechanicTableClients").find("tr:even").not(':first').css("background-color", "#00B6FF");
					});
					
					if(!($(this).hasClass('heading')))
					{
						$(this).attr('customSelector', 1);
						$(this).css("background-color", "#527BD3");
						$("#rowIdClientHidden").val($(this).attr('rowId'));
					}
				});
				
				$('#loginScreenId').on('keyup', '#firstname, #lastname', function(e) {
					
					var keyCode = (window.event) ? e.which : e.keyCode;
					
					if(!((keyCode == 37) || (keyCode == 39) || (keyCode == 8) || (keyCode == 32)))
					{
						inputControlValidationByKrcy($(this), 'alfa');
					}
				});
			});
			
			function checkIfRowIsSelected() {
				var $ = jQuery;
				
				var isSelected = false;
				
				$("#carMechanicTableClients tr").each(function(e) {
					if(($(this).attr('customSelector')))
						isSelected = true;
				});
				return isSelected;				
			}
			
			function checkIfTableIsEmpty() {
				var $ = jQuery;
				
				var isEmpty = true;
				
				$("#carMechanicTableClients tr").each(function(e) {
					if(e > 0)
						isEmpty = false;
				});
				return isEmpty;				
			}
			
			function deleteItem() {
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
				var json = { "RowId" : $('#rowIdClientHidden').val()};

				$.ajax({
					type: "POST",
					url: "deleteClientByRowId",
					data: json,
					success: function(data) {
						$("#loginScreenId").attr('action', 'loginScreen');
						$("#loginScreenId").submit();
					}
				});
			}
			
			function openItem() {
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
						/*Ajax Akcija - Otvori novu stranicu*/
						$("#loginScreenId").attr('action', 'openCLientTaskForMechanicAndAdministrator');
						$("#loginScreenId").submit();
					}
				}
			}
			
			function checkIfTableIsEmpty() {
				var $ = jQuery;
				
				var isEmpty = true;
				
				$("#carMechanicTableClients tr").each(function(e) {
					if(e > 0)
						isEmpty = false;
				});
				return isEmpty;				
			}
			
			function addNewItem() {
				var $ = jQuery;
			
				$(function () {
				    newRow = "<tr>" +
				    	"<td align='center'></td>" +
			            "<td><input type='text' name='firstname' id='firstname' maxLength='20'></td>" +
			            "<td><input type='text' name='lastname' id='lastname' maxLength='20'></td>" +
			            "<td><input type='text' name='phone' id='phone' maxLength='20'></td>" +
				    "</tr>";
				    $('#carMechanicTableClients > tbody > tr:last').after(newRow);
				});
			}
			
			function editSelectedItem() {
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
						$("#carMechanicTableClients tr").each(function(e) {
							if(($(this).attr('customSelector')))
							{
								$(this).find('td:not(first) label').each(function(e2) {
									
									if(e2 == 0)
									{
										$(this).replaceWith( function() {
										    return "<input type=\"text\" id=\"firstname\" value=\"" + $(this).html() + "\" maxlength=\"20\" />";
										});
									}
									
									else if(e2 == 1)
									{
										$(this).replaceWith( function() {
										    return "<input type=\"text\" id=\"lastname\" value=\"" + $(this).html() + "\" maxlength=\"20\" />";
										});
									}
									
									else
									{
										$(this).replaceWith( function() {
										    return "<input type=\"text\" id=\"phone\" value=\"" + $(this).html() + "\" maxlength=\"20\" />";
										});
									}
								});
							}
						});
					}
				}
			}
			
			function saveClients() {
				var $ = jQuery;
				
				var isEmpty = checkIfTableIsEmpty();
				
				if(isEmpty == true)
					toastr.info("The list is empty", 'ERROR!');
				else
				{
					/*Za kliknuti ROW_ID uzeti vrijednosti s jquery funkcijom i poslati s JSON u javu i AZURIRAT tabelu s ajax akcijom*/
					fillRowValuesToHidden();
					
					var value = "";
					var tableTrLength = $('#carMechanicTableClients tr').length;
					var emptyFieldFlag = false;
					
					$('#carMechanicTableClients tr').each(function(e) {
						if(!($(this).hasClass('heading')))
						{
							if(!isNullEmpty($(this).attr('rowId')))
								value += $(this).attr('rowId') + ":";
							else
								value += ":";
							
							$(this).find('td:not(first) input').each(function(e2) {
							
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
						toastr.error("Some values are missing!", 'ERROR!');
						return false;
					}
					else
					{
						var json = {"parsedDataByDelimiters" : value};
	
						$.ajax({
							type: "POST",
							url: "saveClientsByRowId",
							data: json,
							success: function(data) {
								$("#loginScreenId").attr('action', 'loginScreen');
								$("#loginScreenId").submit();
							}
						});
					}
				}
			}
			
			function fillRowValuesToHidden() {
				var $ = jQuery;
				
				$("#carMechanicTableClients tr").each(function(e) {
					$($(this).find('td:not(:eq(0))')).each(function(e2) {
						if(e2 == 0)
							$('#clientFirstNameHidden').val($(this).find('type[text]').val());
						
						else if(e2 == 1)
							$('#clientLastNameHidden').val($(this).find('type[text]').val());
						
						else if(e2 == 2)
							$('#clientPhoneHidden').val($(this).find('type[text]').val());
					});
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
			
			function openCredentialsDialog() {
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
						var json = { "RowId" : $('#rowIdClientHidden').val()};
	
						$.ajax({
							type: "POST",
							url: "getClientCredentialsByRowId",
							data: json,
							error: function() {alert('Error!');},
							success: function(data) {
							
								$('#usernameInput').val('');
								$('#passwordInput').val('');
							
								if(!isNullEmpty(data))
								{
									if(!isNullEmpty(data.clientCredentials[0]))
									{
										if(!isNullEmpty(data.clientCredentials[0].username))
										{
											$('#usernameInput').val(data.clientCredentials[0].username);
										}
										
										if(!isNullEmpty(data.clientCredentials[0].password))
										{
											$('#passwordInput').val(data.clientCredentials[0].password);
										}
									}
								}

								$('#userCredentials').dialog({
								    modal: true,
								    title: "User Credentials",
								    autoOpen: false,
								    closeOnEscape: false,
								    width: 410,
								    height: 317,
									zIndex: 10002,
								    resizable: false,
									buttons: {
										Save: function() {
											$('#usernameInputHidden').val($('#usernameInput').val());
											$('#passwordInputHidden').val($('#passwordInput').val());
											
											var jsonUserCredentialsSave = { "RowId" : $('#rowIdClientHidden').val(), "usernameInputHidden" : $('#usernameInputHidden').val(), "passwordInputHidden" : $('#passwordInputHidden').val()};
											
											$.ajax({
												type: "POST",
												url: "saveUserCredentials",
												data: jsonUserCredentialsSave,
												error: function() {alert('Error!');},
												success: function(data) {
												}
												});
											$(this).dialog("close");
										},
										Cancel: function() {
											$(this).dialog("close");
										}
									 }
								 });
								
								$('#userCredentials').dialog('open');
							}
						});
					}
				}
			}
		</script>
		
		<style type="text/css">
			.js-hide { display: none; }
		</style>
	</head>
	
	<body>
		<form:form id="loginScreenId" method="POST" modelAttribute="clientForm">
			<input id="usernameHidden" name="usernameHidden" type="hidden" value="${clientForm.username}"/>
			<input id="passwordHidden" name="passwordHidden" type="hidden" value="${clientForm.password}"/>
			<input id="rememberHidden" name="rememberHidden" type="hidden" value="${clientForm.rememberCbox}"/>
			<input id="rowIdClientHidden" name="rowIdClientHidden" type="hidden" value="" />
			<input id="sessionUserName" name="sessionUserName" type="hidden" value="${sessionUsername}" />
			<input id="sessionPassword" name="sessionPassword" type="hidden" value="${sessionPassword}" />
			<input id="idApplicationUser" name="idApplicationUser" type="hidden" value="${idApplicationUser}" />
			
			<!-- Hidden za vrijednosti textarea oznacenog retka -->
			<input id="clientFirstNameHidden" name="clientFirstNameHidden" type="hidden" value="" />
			<input id="clientLastNameHidden" name="clientLastNameHidden" type="hidden" value="" />
			<input id="clientPhoneHidden" name="clientPhoneHidden" type="hidden" value="" />
			
			<!-- Hidden za UserCredentials dialog -->
			<input id="usernameInputHidden" name="usernameInputHidden" type="hidden" value="" />
			<input id="passwordInputHidden" name="passwordInputHidden" type="hidden" value="" />
			
			<div id="carMechanicTableContainer" >
				<h2 style="color: black; margin-left: 18px;">Client List</h2>
				<div id="carMechanicTableBodyDiv">
					<table id="carMechanicTableClients" class="carMechanicTable" >
					    <tr class="heading">
					        <th>No.</th>
					        <th>First Name</th>
					        <th>Last Name</th>
					        <th>Phone</th>
					    </tr>
					    
					    <c:choose>
							<c:when test="${not empty clientForm.clients}">
								<c:forEach items="${clientForm.clients}" var="client" varStatus="status">
							        <tr rowId = ${client.rowId}>
							            <td align="center">${status.count}</td>
							            <td><label name="clients[${status.index}].firstname" id="firstname">${client.firstname}</label></td>
							            <td><label name="clients[${status.index}].lastname" id="lastname">${client.lastname}</label></td>
							            <td><label name="clients[${status.index}].phone" id="phone">${client.phone}</label></td>
							        </tr>
						  		</c:forEach>
							</c:when>
						 
							<c:otherwise>
							    <h3 style="color: black;">The list is empty</h3>
							</c:otherwise>
						</c:choose>
					</table>
				</div>
				
				<div id="clientListButtonContainer">
	        		<button type="button" name="newClientButtonId" id="newClientButtonId" value="New" onclick="addNewItem();">New</button>
	        		<button type="button" name="editClientButtonId" id="editClientButtonId" value="Edit" onclick="editSelectedItem();">Edit</button>
	        		<button type="button" name="saveClientButtonId" id="saveClientButtonId" value="New" onclick="saveClients();">Save</button>
					<button type="button" name="deleteClientButtonId" id="deleteClientButtonId" value="Delete" onclick="deleteItem();">Delete</button>
					<button type="button" name="openClientButtonId" id="openClientButtonId" value="Open" onclick="openItem();">Open</button>
					<button type="button" name="quitClientButtonId" id="quitClientButtonId" value="Logout" onclick="logOut();">Logout</button>
					<button type="button" name="clientCredentialButtonId" id="clientCredentialButtonId" value="Edit/Add Credentials" onclick="openCredentialsDialog();">Edit/Add Credentials</button>
	       		</div>
	        </div>
	        
		</form:form>
		
		<div id="result"></div>
		<div id="userCredentials" style="display: none; text-align: center;">
			<span id="userNameSpan">Username</span>
			<div id="usernameContainer" style="width: calc(100% - 3px); height: calc(20% - 3px);">
				<input id="usernameInput" type="text" value="" style="width: calc(50% - 3px);">
			</div>
			
			<span id="passwordSpan">Password</span>
			<div id="passwordContainer" style="width: calc(100% - 3px); height: calc(20% - 3px);">
				<input id="passwordInput" type="text" value="" style="width: calc(50% - 3px);">
			</div>
		</div>
		
	</body>
</html>