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

exports.item_searched = function(req, res) {
  // if input is not empty or a bunch of spaces, then send a request
  if (req.query.item.replace(/\s/g, '').length > 0) {
    debug('Search request for ' + req.query.item + ' received.');
    handlers.request(constants.SEARCH_REQ, req, res);
  }
  // otherwise, just go back to the home page
  else res.redirect('/');
};

exports.login_post = function(req, res) {
  debug('Login request received');
  handlers.request(constants.LOGIN_REQ, req, res);
};

/**
 * Handler for post requests on the register page.
 */
exports.register_post = function(req, res) {
  // if successful, redirect to home page. otherwise, redirect to register page with 
  // password, email, 
  debug('Account create request received.');
  handlers.request(constants.CREATE_ACC_REQ, req, res);
};