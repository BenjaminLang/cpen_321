var socket = io();
const ENTER_KEY = 13;

/**
 * Ways to enter a search result
 */
$('#search-btn').click(() => {
	//
	socket.emit('search_request', $('#search-input').val());
	window.location.href = '/item_searched';
	// Refresh the page if we're already viewing a search result
	/*if (window.location.href === '/item_searched') {
		window.location.reload();
	} 
	// Otherwise view the search results in a different page
	else {
		window.location.href = '/item_searched';
	}*/
	/*
	if ( $('#search-input').val() === 'apple') {
		window.location.href = '/index';
	}
	else {
		window.location.href = '/item_searched';
	}*/

});

$('#search-input').keyup((event) => {
	if (event.keyCode == ENTER_KEY) {
		$('#search-btn').click();
	}
});

/*
$('#register-btn').click(() => {
	var acc_info = {
		'userID' : 
	};
	socket.emit('create_account_request', acc_info);
});
*/
/*
$('#login-btn').click(() => {
	var acc_info = {
	
	};
	socket.emit('login_request', );
});
*/
/*
socket.on('search response', function(response) {
	console.log(response);
});
*/

/*$("button").click( function() {
    $.getJSON("json_website_data.json", function(data) { 
	    $.each(data, function(key, value){ 
		    $("ul").append("<li>"+value.name+"</li>");
		    $("ul").append("<li>"+value.age+"</li>");
			$("ul").append("<li>"+value.company+"</li>");
		});
	});
});
*/


/*
 * The Script to handle the Registration Page Verification of passwords, names, and emails. 
 */
function checkForm(form) {
  if(form.name.value == "") {
    alert("Error: You must enter your name!");
    form.name.focus();
    return false;
  }

  re = /^[a-zA-Z\s]*$/;
  if(!re.test(form.name.value)) {
    alert("Error: Your name must contain only letters!");
    form.name.focus();
    return false;
  }

  re = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
  if(!re.test(form.email.value)) {
    alert("Error: Please enter a valid email!");
    form.email.focus();
    return false;
  }

  if(form.email.value != "" && form.email.value != form.email_verify.value) {
    alert("Error: Please check that your emails match!");
    form.email.focus();
    return false;
  }

  if(form.pwd1.value != "" && form.pwd1.value == form.pwd2.value) {
    if(form.pwd1.value.length < 6) {
      alert("Error: Password must contain at least six characters!");
      form.pwd1.focus();
      return false;
    }

    if(form.pwd1.value == form.name.value) {
      alert("Error: Password must be different from your name!");
      form.pwd1.focus();
      return false;
    }

	re = /[0-9]/;
    if(!re.test(form.pwd1.value)) {
      alert("Error: password must contain at least one number (0-9)!");
      form.pwd1.focus();
      return false;
    }

    re = /[a-z]/;
    if(!re.test(form.pwd1.value)) {
      alert("Error: password must contain at least one lowercase letter (a-z)!");
      form.pwd1.focus();
      return false;
    }

    re = /[A-Z]/;
    if(!re.test(form.pwd1.value)) {
      alert("Error: password must contain at least one uppercase letter (A-Z)!");
      form.pwd1.focus();
      return false;
    }
    
  } else {
      alert("Error: Please check that you've entered and confirmed your password!");
      form.pwd1.focus();
      return false;
    }

    return true;
}
	