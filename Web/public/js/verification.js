// Email verification is on new page, so this is no longer needed
$('form#verify').submit(function(event) {
	var form_data = { 
		'verify_num' : $('input[name="verify_num"]').val(),
		'email_verify' : $('input[name="email_verify"]').val()
	};

	console.log(form_data);

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
