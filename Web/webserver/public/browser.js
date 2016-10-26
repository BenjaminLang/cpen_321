var socket = io();

// Ways to enter the search result			  			  			  
$('#search-btn').click( function() {
	socket.emit('search item', $('#search-input').val());
});

$('#search-input').keyup( function(event) {
	if (event.keyCode == 13) {
		$('#search-btn').click();
	}
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


