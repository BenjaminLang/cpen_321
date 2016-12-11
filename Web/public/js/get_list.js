$(function(){
	$('a.viewList').click(function(){
	 
	 var parameters = { list_name: $(this).text() };
	 	// Send a request to the server to get list information
	   $.get('/get_list', parameters, function(data) {
	   		
	   		resetLocalStorage();
	   		// update localStorage with list
	   		localStorage.cart = data;

	   		var list = JSON.parse(localStorage.cart);
	   		// Go to item_searched page with the first item in the selected list displayed as search results
	   		window.location = 'https://checkedout.ca/item_searched?' + $.param({ item: list[0].name });
	   		
	 	});
	});
});