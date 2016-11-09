var socket = io();
const ENTER_KEY = 13;
/**
 * Ways to enter a search result
 */
$('#search-btn').click(() => {
	socket.emit('search_request', $('#search-input').val());
	// View the search results in a different page
	if (window.location.href === '/item_searched') {
		window.location.reload();
	} 
	else {
		window.location.href = '/item_searched';
	}
});

$('#search-input').keyup((event) => {
	if (event.keyCode == ENTER_KEY) {
		$('#search-btn').click();
	}
});

/*
$('#login-btn').click(() => {
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



