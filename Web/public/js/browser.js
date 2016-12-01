/*
 * The Script to handle the Registration Page Verification of passwords, names, and emails. 
 */
function checkForm(form) {
  if(form.name.value == "") {
    alert("Error: You must enter your name!");
    form.name.focus();
    return false;
  }

  re = /^[a-zA-Z\s]*$/;
  if(!re.test(form.name.value)) {
    alert("Error: Your name must contain only letters!");
    form.name.focus();
    return false;
  }

  re = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
  if(!re.test(form.email.value)) {
    alert("Error: Please enter a valid email!");
    form.email.focus();
    return false;
  }

  if(form.email.value != "" && form.email.value != form.email_verify.value) {
    alert("Error: Please check that your emails match!");
    form.email.focus();
    return false;
  }

  if(form.password.value != "" && form.password.value == form.pwd2.value) {
    if(form.password.value.length < 6) {
      alert("Error: Password must contain at least six characters!");
      form.password.focus();
      return false;
    }

    if(form.password.value == form.name.value) {
      alert("Error: Password must be different from your name!");
      form.password.focus();
      return false;
    }

	re = /[0-9]/;
    if(!re.test(form.password.value)) {
      alert("Error: password must contain at least one number (0-9)!");
      form.password.focus();
      return false;
    }

    re = /[a-z]/;
    if(!re.test(form.password.value)) {
      alert("Error: password must contain at least one lowercase letter (a-z)!");
      form.password.focus();
      return false;
    }

    re = /[A-Z]/;
    if(!re.test(form.password.value)) {
      alert("Error: password must contain at least one uppercase letter (A-Z)!");
      form.password.focus();
      return false;
    }
    
  } else {
      alert("Error: Please check that you've entered and confirmed your password!");
      form.password.focus();
      return false;
    }

    return true;
}

function save_list() {
  $("input[name='list']").val(localStorage.cart);
}