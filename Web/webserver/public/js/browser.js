var socket = io();

/**
 * Ways to enter a search result
 */
$('#search-btn').click(() => {
	socket.emit('search_request', $('#search-input').val());
	// View the search results in a different page
	window.location.href = '/item_searched';
});

$('#search-input').keyup((event) => {
	// Enter key
	if (event.keyCode == 13) {
		$('#search-btn').click();
	}
});

socket.on('search response', function(response) {
	console.log(response);
});

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


