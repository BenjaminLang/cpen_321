/**************************************************************************/
/* ROUTE HANDLERS */
/**************************************************************************/

var constants = require('./constants.js');
var communicate = require('./communicate.js');
var debug = require('debug')('handlers');

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
    else {
      name = 'Anonymous';
    }
    res.render('index', {'title': 'Home', 'name': name});
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
    delete req.session.name;
    delete req.session.email;
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
      communicate.request(constants.SEARCH_REQ, req, res);
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
      debug('Account update request received.');
      communicate.request(constants.ACC_UPDATE_REQ, req, res);
    }
    // otherwise, just go back to the home page
    else res.redirect('/');
  },

  /**
   * Handler for submitting login information
   * @param  req the HTTP request from the browser 
   * @param  res the HTTP response to the browser
   * @return  nothing
   */
  login_post : function(req, res) {
    debug('Login request received');
    communicate.request(constants.LOGIN_REQ, req, res);
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
    communicate.request(constants.CREATE_ACC_REQ, req, res);
  }

};
