$(function(){
	$('a.viewList').click(function(){
	 
	 var parameters = { list_name: $(this).val() };

	 	// Send an AJAX request to the server to get list information
	   $.get('/get_list', parameters, function(data) {
	   	$('#results').html(data);
	 });
	});
});