<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>  
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
		<meta charset="utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">	<!-- Force Latest IE rendering engine -->
		
		<title>Login Form</title>
		<meta name="description" content="">
		<meta name="author" content="">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" /> 
		
		<jsp:include page="javascriptData.jsp" />
	    <jsp:include page="cssData.jsp" />
        
        <script type="text/javascript">
			jQuery(document).ready(function($) {
	
				var $ = jQuery;
				
				var usernameValue = '<c:out value="${usernameValue}"/>';
				var passwordValue = '<c:out value="${passwordValue}"/>';
				var cBoxValue = '<c:out value="${cBoxValue}"/>';
				var logOutFlag = '<c:out value="${logOutFlag}"/>';
				var loginError = $('#loginError').val();
				
				if(!isNullEmpty(loginError))
					toastr.error(loginError, 'ERROR!');
				
				if(!isNullEmpty(logOutFlag))
				{
					if(logOutFlag == "true")
					{
						if(!isNullEmpty(usernameValue))
						{
							$('#usernameID').val(usernameValue);
						}
						if(!isNullEmpty(passwordValue))
						{
							$('#passwordId').val(passwordValue);
						}
					}
				}
			});
			
		</script>
		
		<style type="text/css">
			.js-hide { display: none; }
		</style>
    </head>
    
    <body id="homeId">
		<div class="container" >
			<div class="form-bg">
				<form:form method="POST" action="loginScreen" modelAttribute="homeModelAttribute">
					<input id="loginError" name="loginError" type="hidden" value="${loginError}" />
					
					<h2>Login</h2>
					
					<p><input type="text" name="usernameID" id="usernameID" placeholder="Username" ></p>
					<p><input type="password" name="passwordId" id="passwordId" placeholder="Password" ></p>
					
					<label for="remember">
						<input type="checkbox" name="rememberId" id="rememberId" value="true" />
						<span>Remember me on this computer</span>
		       		</label>
					
					<button type="submit" id="loginSubmitId" value="Login">Login</button>
				</form:form>
			</div>
		</div>
    </body>
</html>