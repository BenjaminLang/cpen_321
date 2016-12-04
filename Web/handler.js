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
    if (req.session.user) {
      name = req.session.user.name;
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
   * Handler for delete account page
   * @param  req the HTTP request from the browser 
   * @param  res the HTTP response to the browser
   * @return  nothing
   */
  delete_acc : function(req, res) {
    res.render('delete_acc', {
      'title' : 'Delete Account',
      'logged_in_name' : req.session.user.name
    });
  },

  /**
   * Handler for login page
   * @param  req the HTTP request from the browser 
   * @param  res the HTTP response to the browser
   * @return  nothing
   */
  login : function(req, res) {
    res.render('login', {'title': 'Login'});
  },

  /**
   * Handler for logging out
   * @param  req the HTTP request from the browser 
   * @param  res the HTTP response to the browser
   * @return  nothing
   */
  logout : function(req, res) {
    // delete the session variable and redirect to homepage
    debug('Logging out...');
    req.session = null;
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
    res.render('update', {
      'title' : 'Change Password',
      'logged_in_name' : req.session.user.name
    });
  },

  /**
   * Handler for displaying the manage lists page
   * @param  req the HTTP request from the browser 
   * @param  res the HTTP response to the browser
   * @return  nothing
   */
  manage_lists : function(req, res) {
    debug('Request to manage lists received.')
    messenger.request(constants.GET_LIST_NAMES_REQ, req, res);
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
   * Handler for deleting account
   * @param  req the HTTP request from the browser 
   * @param  res the HTTP response to the browser
   * @return  nothing
   */
  delete_acc_post : function(req, res) {
    debug('Delete account request for user: ' + req.session.user.email);
    messenger.request(constants.DEL_ACC_REQ, req, res);
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

  /**
   * Handler for verifying codes
   * @param  req the HTTP request from the browser
   * @param  res the HTTP response to the browser
   * @return  nothing
   */ 
  verify_code : function(req, res) {
    debug('Verify code request received.');
    messenger.request(constants.VERIFY_REQ, req, res);
  }

};
