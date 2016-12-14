$(function(){
  $('.saveChanges').click(function(){
   
   var parameters = { 
      list_name: $("input[name='list_name']").val(),
      list : localStorage.cart
    };
    // Send a request to the server to save list information
     $.post('/save_list', parameters, function(data) {
        // Display status message
        $('#status_message').html(data);
        if (data == 'List saved.') {
            // display success alert
            $('#save_list_status').addClass('alert-success');
        }
        else {
            // display warning alert
            $('#save_list_status').addClass('alert-warning');
        }
        $('#save_list_status').show();

        $('#save_list_status').fadeTo(2000, 500).fadeOut(500, function(){
          $('#save_list_status').fadeOut(500);
        });
    });
  });
});


/*
function save_list() {

  jQuery(function($) {
      var parameters = { 
      list_name: $("input[name='list_name']").text(),
      list : localStorage.cart;
    };
    // Send a request to the server to save list information
     $.post('/save_list', parameters, function(data) {
        // Display status message
        $('#status_message').html(data);
        if (data == 'List saved.') {
            // display success alert
            $('#save_list_status').addClass('alert-success');
        }
        else {
            // display warning alert
            $('#save_list_status').addClass('alert-warning');
        }
        
    });
  });
  
}*/