/**************************************************************************/
/* ROUTE HANDLERS */
/**************************************************************************/

// handler for homepage
exports.home = function(req, res) {
	res.render('index', {'title': 'Home'});
};

//exports.home

exports.register = function(req, res) {
	res.render('register', {'title': 'Registration Form'});
};

// this currently does not work
exports.register_post = function(req, res) {
  // if the username is not submitted, give it a default of "Anonymous"
  var userID = req.body.userID;
  // store the username as a session variable
  req.session.userID = userID;
  // redirect the user to homepage
  res.redirect('/');
};

exports.login = function(req, res) {
  res.render('login', {'title': 'Login'});
};