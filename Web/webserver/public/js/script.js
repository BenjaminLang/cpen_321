$("button").click( function() {
    $.getJSON("json_website_data.json", function(data) { 
	    $.each(data, function(key, value){ 
		    $("ul").append("<li>"+value.name+"</li>");
		    $("ul").append("<li>"+value.age+"</li>");
			$("ul").append("<li>"+value.company+"</li>");
		});
	});
});