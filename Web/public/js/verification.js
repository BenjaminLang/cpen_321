$('form').submit(function(event) {
	var form_data = { 
		'verify_num' : $('input[name=verify_num]').val(),
		'email' : $('input[name=email]').val()
	};

	$.ajax({
		type : 'POST',
		url : '/verify_code',
		data : form_data,
		dataType : 'json',
		encode : true
	})

		.done(function(data) {
			console.log(data);
		});
	event.preventDefault();
});