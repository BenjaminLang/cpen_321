/**************************************************************************/
/* ROUTE HANDLERS */
/**************************************************************************/

var constants = require('./constants.js');
var handlers = require('./handlers.js');
var debug = require('debug')('routes');

// handler for homepage
exports.home = function(req, res) {
  var userID;
  if (req.session.userID) {
    userID = req.session.userID;
  }
  else {
    userID = 'Anonymous';
  }
	res.render('index', {'title': 'Home', 'userID': userID});
};

exports.register = function(req, res) {
	res.render('register', {'title': 'Registration Form'});
};

/**
 * Handler for post requests on the register page.
 */
exports.register_post = function(req, res) {
  // but for now, just save it in a variable
  // store the username as a session variable
  req.session.registered_userID = req.body.userID;
  req.session.registered_password = req.body.password;
  // redirect the user to homepage
  res.redirect('/login');
};

exports.login = function(req, res) {
  res.render('login', {'title': 'Login'});
};

exports.login_post = function(req, res) {
  // check if userID and password match up with something already registered
  if (req.body.userID == req.session.registered_userID && req.body.password == req.session.registered_password) {
    req.session.userID = req.body.userID;
    req.session.password = req.body.password;
    res.render('index', {'title':'Home', 'userID': req.session.userID});
  }
  else {
    res.redirect('/');
  }
};

exports.item_searched = function(socket, req, res) {
  // extract submission data from req object, then call request handler with the socket, data, and request type
  debug("Search request for " + req.query.item + " received.");
  handlers.request(socket, req.query.item, constants.SEARCH_REQ);
};
