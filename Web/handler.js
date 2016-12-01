/**************************************************************************/
/* ROUTE HANDLERS */
/**************************************************************************/

var constants = require('./constants.js');
var messenger = require('./messenger.js');
var debug = require('debug')('handler');

module.exports = {

  /**
   * Handler for home page
   * @param  req  the HTTP request from the browser 
   * @param  res  the HTTP response to the browser
   * @return  nothing
   */
  home : function(req, res) {
    var name;
    if (req.session.name) {
      name = req.session.name;
    }
    res.render('index', {'title': 'Home', 'logged_in_name': name});
  },

  /**
   * Handler for register page
   * @param  req the HTTP request from the browser 
   * @param  res the HTTP response to the browser
   * @return  nothing
   */
  register : function(req, res) {
    res.render('register', {'title': 'Registration Form'});
  },

  /**
   * Handler for deleting account
   * @param  req the HTTP request from the browser 
   * @param  res the HTTP response to the browser
   * @return  nothing
   */
  delete_acc : function(req, res) {
    debug('Delete account request for user: ' + req.session.email);
    messenger.request(constants.DEL_ACC_REQ, req, res);
  },

  /**
   * Handler for login page
   * @param  req the HTTP request from the browser 
   * @param  res the HTTP response to the browser
   * @return  nothing
   */
  login : function(req, res) {
    // user is already logged in; redirect them back to home page
    if (req.session.name) res.redirect('/');

    res.render('login', {'title': 'Login'});
  },

  /**
   * Handler for logging out
   * @param  req the HTTP request from the browser 
   * @param  res the HTTP response to the browser
   * @return  nothing
   */
  logout : function(req, res) {
    // delete the session variable
    req.session = null;
    // redirect user to homepage
    res.redirect('/');
  },

  /**
   * Handler for displaying items
   * @param  req the HTTP request from the browser 
   * @param  res the HTTP response to the browser
   * @return  nothing
   */
  item_searched : function(req, res) {
    // if input is not empty or a bunch of spaces, then send a request
    if (req.query.item.replace(/\s/g, '').length > 0) {
      debug('Search request for ' + req.query.item + ' received.');
      messenger.request(constants.SEARCH_REQ, req, res);
    }
    // otherwise, just go back to the home page
    else res.redirect('/');
  },

  /**
   * Handler for displaying update (account) page
   * @param  req the HTTP request from the browser 
   * @param  res the HTTP response to the browser
   * @return  nothing
   */
  update : function(req, res) {
    // is user logged in?
    if (req.session.name) {
      res.render('update', {'title' : 'Change Password'});
    }
    // otherwise, go to the login page
    else res.redirect('/login');
  },

  /**
   * Handler for submitting login information
   * @param  req the HTTP request from the browser 
   * @param  res the HTTP response to the browser
   * @return  nothing
   */
  login_post : function(req, res) {
    debug('Login request received');
    messenger.request(constants.LOGIN_REQ, req, res);
  },

  /**
   * Handler for submitting new account information
   * @param  req the HTTP request from the browser 
   * @param  res the HTTP response to the browser
   * @return  nothing
   */
  register_post : function(req, res) {
    // if successful, redirect to home page. otherwise, redirect to register page with 
    // password, email, 
    debug('Account create request received.');
    messenger.request(constants.CREATE_ACC_REQ, req, res);
  },

  /**
   * Handler for updating password
   * @param  req the HTTP request from the browser 
   * @param  res the HTTP response to the browser
   * @return  nothing
   */
  update_post : function(req, res) {
    // is user logged in?
    debug('Account update request received.');
    messenger.request(constants.ACC_UPDATE_REQ, req, res);
  },


  /**
   * Handler for saving lists
   * @param  req the HTTP request from the browser
   * @param  res the HTTP response to the browser
   * @return  nothing
   */ 
  save_list : function(req, res) {
    debug('Save list request received.');
    messenger.request(constants.ADD_LIST_REQ, req, res);
  }

};
