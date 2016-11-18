/**************************************************************************/
/* ROUTE HANDLERS */
/**************************************************************************/

var constants = require('./constants.js');
var handlers = require('./handlers.js');
var debug = require('debug')('routes');

/**
 * Handler for homepage
 */
exports.home = function(req, res) {
  var name;
  if (req.session.name) {
    name = req.session.name;
  }
  else {
    name = 'Anonymous';
  }
	res.render('index', {'title': 'Home', 'name': name});
};

exports.register = function(req, res) {
	res.render('register', {'title': 'Registration Form'});
};

exports.login = function(req, res) {
  res.render('login', {'title': 'Login'});
};

exports.logout = function(req, res) {
  // delete the session variable
  delete req.session.name;
  // redirect user to homepage
  res.redirect('/');
}

/*
exports.item_searched = function(socket, req, res) {
  // first, check if submission data exists
  // need to also check for search options
  // extract submission data from req object, then call request handler with the socket, data, and request type
  debug('Search request for ' + req.query.item + ' received.');
  handlers.request(socket, req.query.item, constants.SEARCH_REQ);
};
*/
exports.item_searched = function(req, res) {
  debug('Search request for ' + req.query.item + ' received.');
  // handlers.request(req.query.item, constants.SEARCH_REQ, req, res);
  handlers.request(constants.SEARCH_REQ, req, res);
};

exports.login_post = function(req, res) {
  // check if name and password match up with something already registered
  /*var acc_info = {};

  acc_info.password = req.body.password;
  acc_info.email = req.body.email;
  debug('Login request for ' + acc_info + ' received.'); */
  debug('Login request received');
  // handlers.request(acc_info, constants.LOGIN_REQ);
  handlers.request(constants.LOGIN_REQ, req, res);
};

/**
 * Handler for post requests on the register page.
 */
exports.register_post = function(req, res) {
  // if successful, redirect to home page. otherwise, redirect to register page with 
  // password, email, 
  // store the username as a session variable
  /*
  var acc_info = {};

  acc_info.name = req.body.name;
  acc_info.password = req.body.pwd1;
  acc_info.email = req.body.email;
  */
  //req.session.registered_name = req.body.name;
  //req.session.registered_password = req.body.password;
  // redirect the user to homepage
  

  debug('Account create request received.');
  // handlers.request(socket, acc_info, constants.CREATE_ACC_REQ);
  handlers.request(constants.CREATE_ACC_REQ, req, res);
};