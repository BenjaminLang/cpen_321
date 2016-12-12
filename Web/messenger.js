/**************************************************************************/
/* WEBSERVER-MAINSERVER COMMUNICATION */
/**************************************************************************/

var constants = require('./constants.js');
var tls = require('tls');
var fs = require('fs');
var debug = require('debug')('messenger');
var util = require('util');

process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
module.exports = {

  /**
   * Sends a request to the main server.
   * @param  type the kind of request that we're sending
   * @param  req  the HTTP request from the browser
   * @param  res  the HTTP response to the browser
   * @return nothing
   */
  request : function(type, req, res) {

    var json_request = {};
    json_request.message_type = type;

    switch(type) {
      case constants.SEARCH_REQ:
        // Need to extract actual options from user options
        if (req.session.user) json_request.email = req.session.user.email;
        else json_request.email = '';
        var list_of_stores = [];
        var num_items = '-1';

        debug(req.query);

        if (req.query.stores)
          if (req.query.stores != constants.DEFAULT_STORES)
            list_of_stores = req.query.stores.split(/[ ,]+/);
        
        if (req.query.items_per_page)
          if (req.query.items_per_page != constants.DEFAULT_ITEMS_PER_PAGE)
            num_items = req.query.items_per_page;

        debug(list_of_stores + " " + num_items + " ");
        json_request.options = {
          'stores' : list_of_stores,
          'price' : 'min', 
          'num' : num_items,
          'range_min' : formatter(req.query.min_price),
          'range_max' : formatter(req.query.max_price)
        };
        json_request.items = [req.query.item];
        break;
                      
      case constants.CREATE_ACC_REQ:
        json_request.name = req.body.name;
        json_request.email = req.body.email;
        json_request.password = req.body.password;
        json_request.test = req.body.test;
        break;

      case constants.DEL_ACC_REQ:
        json_request.email = req.session.user.email;
        json_request.password = req.body.password;
        break;

      case constants.LOGIN_REQ:
        json_request.email = req.body.email;
        json_request.password = req.body.password;
        break;

      case constants.ACC_UPDATE_REQ:
        json_request.email = req.session.user.email;
        json_request.old_password = req.body.old_password;
        json_request.password = req.body.password;
        break;

      case constants.ADD_LIST_REQ:
        json_request.list = req.body.list;
      case constants.DEL_LIST_REQ:
        json_request.list_name = req.body.list_name;
      case constants.GET_LIST_NAMES_REQ:
        json_request.email = req.session.user.email;
        break;

      case constants.GET_LIST_REQ:
        json_request.list_name = req.query.list_name;
        json_request.email = req.session.user.email;
        break;

      case constants.RECOMMEND_REQ:
        json_request.email = req.session.user.email;
        break;

      case constants.VERIFY_REQ:
        json_request.email = req.body.email;
        json_request.verify_num = req.body.verify_num;
        break;
    }
    socket(json_request, req, res);
  }

};

/**
 * Handles responses from the main server.
 * @param  res_from_server the response from the server
 * @param  req the HTTP request from the browser
 * @param  res the HTTP response to the browser
 * @return nothing
 */
response = function (res_from_server, req, res) {
  
  var message;
  try {
    message = JSON.parse(res_from_server.toString());
  } catch(err) {
    debug('Error in parsing this message from the main server: ' + res_from_server.toString());
    res.sendStatus(500);
    return;
  }

  var type = message.message_type;
  debug('message is of type: ' + type);
  if (message.status) {
    debug('... with status: ' + message.status);
    //if (message.status == 'exception')
      //res.sendStatus(500);
  } 

  switch(type) {
    //////////////////////////////
    case constants.READ_RSP:
        for (var i = 0; i < message.items.length; i++) {
          // replace all instances of '&amp;' in item names with '&'
          message.items[i].data.name = message.items[i].data.name.replace('&amp;', '&');
        }
        var name;
        if (req.session.user) name = req.session.user.name;
        res.render('item_searched', {'title' : req.query.item + ' - Search Results',
                                    'logged_in_name' : name,
                                    'list_items' : message.items});
        break;
    //////////////////////////////
    case constants.CREATE_ACC_RSP:
        if (message.status == constants.SUCCESS) {
          // save user info, then redirect to home page
          // req.session.user = {name : req.body.name, email : req.body.email};
          res.redirect('/login');
        }
        else {
          // failure, which means email is already in use
          res.render('register', {
            'title' : 'Registration Form',
            'email_taken' : 'That email is already in use.'
          });
        }
        break;
    //////////////////////////////
    case constants.DEL_ACC_RSP:
        if (message.status == constants.SUCCESS) {
          res.redirect('/logout');
        }
        else if (message.status == constants.FAILURE) {
          res.render('/delete_acc', {
            'title' : 'Delete Account',
            'status' : 'Incorrect Password'
          });
        }
        break;
    //////////////////////////////
    case constants.LOGIN_RSP:
        if (message.status == constants.SUCCESS) {
          // login successful
          // need to get name from message
          debug('login: successful');
          req.session.user = {name : message.name, email : req.body.email};
          res.redirect('/');
        } 
        else if (message.status == constants.FAILURE) {
          debug('login: failure');
          res.render('login', {
            'title' : 'Login',
            'login_failed' : 'Password is incorrect.'
          });
          
        }
        else if (message.status == constants.DOES_NOT_EXIST) {
          debug('login: email does not exist');
          res.render('login', {
            'title' : 'Login',
            'login_failed' : 'Email does not exist.'
          });
        }
        else if (message.status == constants.NOT_VERIFIED) {
          debug('login: email has not been verified');
          res.render('login', {
            'title' : 'Login',
            'login_failed' : 'Email has not been verified.'
          });
        }
        else // shouldn't get here!
          res.end();
        break;
    //////////////////////////////
    case constants.ACC_UPDATE_RSP:
        if (message.status == constants.SUCCESS) {
          res.render('update', {
            'title' : 'Change Password',
            'logged_in_name' : req.session.user.name,
            'status' : 'Password successfully updated.'
          });
        }
        else if (message.status == constants.FAILURE) {
          res.render('update', {
            'title' : 'Change Password',
            'logged_in_name' : req.session.user.name,
            'status' : 'Old password is incorrect.'
          });
        }
        // this should never happen
        else {
          res.render('update', {
            'title' : 'Change Password',
            'logged_in_name' : req.session.user.name,
            'status' : 'Your email does not exist. What happened?'
          });
        }
        break;
    //////////////////////////////
    case constants.ADD_LIST_RSP:
        // exception most likely caused if account is deleted right before sending
        // the request, so just logout
        if (message.status == 'exception') res.redirect('/logout');

        // stay on same page
        res.status(204).end();
        break;
      //////////////////////////////
      case constants.GET_LIST_RSP:
        // do something with the lists
        // res.render('list', {list :});
        res.send(message.list);
        break;
    //////////////////////////////
    case constants.GET_LIST_NAMES_RSP:
        // do something with the list names
        res.render('manage_lists', {
          'title' : 'Manage Lists',
          'logged_in_name' : req.session.user.name,
          'user_lists' : message.list_names 
        });
        break;
    //////////////////////////////
    case constants.DEL_LIST_RSP:
        // refresh the page the user is on, but with the list deleted
        if (message.status == constants.SUCCESS) {
          
        }
        
        break;
    //////////////////////////////
    case constants.RECOMMEND_RSP:
        // do something with the recommended list
        res.render('index', {
          'title': 'Home', 
          'logged_in_name': req.session.user.name,
          'lists' : message.rec_list
        });
        break;
    //////////////////////////////
    case constants.VERIFY_RSP:
        if (message.status == constants.SUCCESS) {
          // tell the user that their email is verified
          res.render('verify', {
            'title': 'Email Verification',
            'status': 'Email successfully verified.'
          })
        }
        else if (message.status == constants.FAILURE) {
          // tell the user that the verify_num was incorrect
          res.render('verify', {
            'title': 'Email Verification',
            'status': 'Incorrect verification code.'
          })
        }
        else {
          res.render('verify', {
            'title': 'Email Verification',
            'status': 'That email does not exist.'
          })
        }
        break;
    default:
        res.sendStatus(404);
        break;
  }
};

/**
 * Initiates a connection to the main server via TCP socket and sends the given request.
 * The function then waits for a response from the server. Upon receiving it, appropiate actions
 * are taken based on the response and then the connection is closed.
 * @param  req_to_server the request to be sent to the main server
 * @param  req the HTTP request from the browser
 * @param  res the HTTP response to the browser
 * @return nothing
 */
socket = function(req_to_server, req, res) {
  
  // for TLS
  var options = { 
    key : fs.readFileSync('../Server/src/client.key'),
    cert : fs.readFileSync('../Server/src/client.crt'),
    ca : [ fs.readFileSync('../Server/src/server.crt') ]
  };

  // connect to the main server
  var connection = tls.connect({
      port : constants.MAINSERVER_PORT,
      host : constants.HOST,
      options : options
    }, function() {debug('Connection protocol: ' + connection.getProtocol());}
  );
  // container for incoming data
  var data = '';

  connection.on('connect', function() {
    debug('Connected to main server!');
    debug('Sending request now...');
    connection.write(JSON.stringify(req_to_server));
  });

  // need to accumulate packets until all data has been received
  connection.on('data', function(packet) {
    data += packet;
  });

  // all data has been received, so take action
  connection.on('end', function() {
    debug('handling response...');
    response(data, req, res);
    debug('response handled; destroying socket');
    connection.destroy();
    connection.unref();
  }); 

  connection.on('error', function(error) {
    debug('Socket error: ' + error.code);
    connection.destroy();
    connection.unref();
    res.sendStatus(500);
  });

  connection.on('close', function(had_error) {
    if (had_error) debug('Socket closed due to a transmission error');
    else debug('Socket closed');
  });

};

formatter = function(value) {
  if (value) return value;
  else return '';
};
