/*
 * script to handle the verification of passwords, names, and emails. 
 */
function checkForm(form) {
  if(form.name.value == "") {
    add_alert('alert-warning', 'You must enter your name!');
    form.name.focus();
    return false;
  }

  re = /^[a-zA-Z\s]*$/;
  if(!re.test(form.name.value)) {
    add_alert('alert-warning', 'Your name must contain only letters!');
    form.name.focus();
    return false;
  }

  re = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
  if(!re.test(form.email.value)) {
    add_alert('alert-warning', 'Please enter a valid email!');
    form.email.focus();
    return false;
  }

  if(form.email.value != "" && form.email.value != form.email_verify.value) {
    add_alert('alert-warning', 'Please check that your emails match!');
    form.email.focus();
    return false;
  }

  if(form.password.value == form.name.value) {
      add_alert('alert-warning', 'Password must be different from your name!');
      form.password.focus();
      return false;
  }

  return check_password(form);
}

function checkUpdateForm(form) {
  return check_password(form);
}

function check_password(form) {
  if(form.password.value != "" && form.password.value == form.password_verify.value) {
    if(form.password.value.length < 6) {
      add_alert('alert-warning', 'Password must contain at least six characters!');
      form.password.focus();
      return false;
    }    

  re = /[0-9]/;
    if(!re.test(form.password.value)) {
      add_alert('alert-warning', 'Password must contain at least one number (0-9)!');
      form.password.focus();
      return false;
    }

    re = /[a-z]/;
    if(!re.test(form.password.value)) {
      add_alert('alert-warning', 'Password must contain at least one lowercase letter (a-z)!');
      form.password.focus();
      return false;
    }

    re = /[A-Z]/;
    if(!re.test(form.password.value)) {
      add_alert('alert-warning', 'Password must contain at least one uppercase letter (A-Z)!');
      form.password.focus();
      return false;
    }
    
  } else {
      add_alert('alert-warning', 'Please check that you\'ve entered your password and confirmed it correctly!');
      form.password.focus();
      return false;
    }

  return true;
}


function add_alert(alert_type, alert_text) {
  //'alert', 'alert-warning', 'alert-dismissable', 'fade', 'in'
  $('.alert_class').removeClass('alert_class').addClass('alert ' + alert_type + ' alert-dismissable fade in');
  $('#status_message').text(alert_text);

  $('.alert').show();
  $('a.close').show();
}

function enter_options(form) {
  form.stores.value = $(".selectStore").find(":button").attr("title");
  form.items_per_page.value = $(".selectNumberItems").find(":button").attr("title");
  form.min_price.value = $(".selectMinPrice").find("input").val();
  form.max_price.value = $(".selectMaxPrice").find("input").val();
}
