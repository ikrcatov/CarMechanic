jQuery(document).ready(function($) {

	var $ = jQuery;
	
	$.ajaxSetup({
		cache : false
	});

//	$.Shortcuts.add({
//		enableInInput : true,
//		type : 'hold',
//		mask : 'Ctrl+F5',
//		handler : function(e) {
//			e.preventDefault();
//		}
//	}).start();
	
	$.Shortcuts.add({
		enableInInput : true,
		type : 'hold',
		mask : 'F5',
		handler : function(e) {
			e.preventDefault();
		}
	}).start();
});

function logIn() {
	var $ = jQuery;
	
	var json = { "Username" : $('#usernameID').val(), "Password" : $('#passwordId').val()};
	
	$.ajax({
		type: "POST",
		url: "loginScreen",
		data: json,
		success: function(data) {
			
			$("#homeId").html(data);
		}
	});
}

function logOut() {
	var $ = jQuery;
	
	openConfirmationDialogForLogout("Are you sure you want to logout", "Warning");
}

function logOutConfirmation() {
	var $ = jQuery;
	
	var formName = $('form').attr('id');
	var rememberUserCredentials = $('#rememberHidden').val();
	
	if(!isNullEmpty(formName))
	{
		if(formName == "loginScreenId")
		{
			$("#loginScreenId").attr('action', 'logout');
			$("#loginScreenId").submit();
		}
		
		else if(formName == "clientTasks")
		{
			$("#clientTasks").attr('action', 'logout');
			$("#clientTasks").submit();
		}
		
		else if(formName == "clientTasksForMechanic")
		{
			$("#clientTasksForMechanic").attr('action', 'logout');
			$("#clientTasksForMechanic").submit();
		}
	}
}

function isNullEmpty(variable) {
	var $ = jQuery;
	
	if(variable != null && variable != "null" && variable != "undefined" && variable != "")
		return false;
	else
		return true;
}

function openNonDestructiveDialog(message, title) {
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
           OK: function () {
			 $('#Warning').dialog('close');  
	         $('#Warning').remove();
           }
		}
	});

	$("#Warning").dialog('open');
}

function inputControlValidationByKrcy(input, format)
{ 
	var $ = jQuery;
	
    var value=input.val();
    var values=value.split("");
    var update = "";
    var transition = "";
    
    var expression = "";
    var finalExpression = "";
    
    if (format == 'int'){
        expression = /^([0-9])$/;
        finalExpression = /^([0-9][0-9]*)$/;
    }
    else if (format == 'float')
    {
        expression = /(^\d+$)|(^\d+\.\d+$)|[,\.]/;
        finalExpression = /^([0-9][0-9]*[,\.]?\d{0,3})$/;
    } 
    else if (format == 'percent')
    {
    	expression = /(^\d+$)|(^\d+\.\d+$)|[,\.]/;
        finalExpression = /^100([,\.]0{0,1})?$|^([1-9]([0-9])?|0)([,\.][0-9]{0,1})?$|^([1-9]([0-9])?|0)([,\.])$/;
    } 
    else if (format == 'alfaNum')
    {
        expression = /^[A-Za-z0-9]+$/;
        finalExpression = /^[A-Za-z0-9]+$/;
    } 
    else if(format == 'alfa'){
        expression = /^[a-zA-Z]+$/;
        finalExpression = /^[a-zA-Z]+$/;
    }
    else if(format == 'alfaWithSpace') {
        expression = /^[a-zA-Z\s]+$/;
        finalExpression = /^[a-zA-Z\s]+$/;
    }
    else if (format == 'alfaNumWithSpace')
    {
        expression = /^[A-Za-z0-9\s]+$/;
        finalExpression = /^[A-Za-z0-9\s]+$/;
    }
    else if (format == 'mtow')
    {
    	expression = /(^\d+$)|(^\d+\.\d+$)|[,\.]/;
        finalExpression = /^\d{1,3}$|^\d{1,3}[.\,]\d$|^\d{1,3}[.\,]$/;		//Zlatko: 999 99 9|9,9 99,9 999,9|9, 99, 999,
    }
    else if (format == 'double')
    {
    	expression = /(^\d+$)|(^\d+\.\d+$)|[,\.]/;
        finalExpression = /^\d{1,6}$|^\d{1,6}[.\,]\d{1,6}$|^\d{1,6}[.\,]$/;		
    }
    for(id in values)
    {           
        if (expression.test(values[id]) == true && values[id] != '')
        {
            transition += '' + values[id].replace(',','.');
            if(finalExpression.test(transition) == true)
                update += '' + values[id].replace(',','.');
        }
    }
    
    input.val(update);
}

function openConfirmationDialogForLogout(message, title) {
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
				logOutConfirmation();
				$(this).dialog("close");
			},
			Cancel: function() {
				$(this).dialog("close");
			}
		 }
	 });
	
	$('#Warning').dialog('open');
}