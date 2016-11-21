/**************************************************************************/
/* WEBSERVER-MAINSERVER COMMUNICATION */
/**************************************************************************/

var constants = require('./constants.js');
var net = require('net');
var debug = require('debug')('communicate');

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
        json_request.options = {'price' : 'none', 'num' : '-1'};
        json_request.items = [req.query.item];
        // json_request.name 
        break;
                      
      case constants.CREATE_ACC_REQ:
        json_request.name = req.body.name;
        json_request.email = req.body.email;
        json_request.password = req.body.pwd1;
        break;

      case constants.LOGIN_REQ:
        json_request.email = req.body.email;
        json_request.password = req.body.password;
        break;

      // more requests to add?
      // request for saving lists (assuming user is logged in)
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
  
  var message = JSON.parse(res_from_server.toString());
  var type = message.message_type;
  switch(type) {
    case constants.READ_RSP:
      for (var i = 0; i < message.items.length; i++) {
        // replace all instances of '&amp;' in item names with '&'
        message.items[i].data.name = message.items[i].data.name.replace('&amp;', '&');
      }
      
      res.render('item_searched', {'title': 'Search Results', 'list_items': message.items});
      break;
    
    // check if acc_created is true or false
    case constants.CREATE_ACC_RSP:
      // if account creation is successful; then redirect to home page
      // 
      if (message.status == constants.SUCCESS) {
        req.session.name = req.body.name;
        res.redirect('/');
      }
      else {

        // res.render('register', )
      }
      // otherwise,
      break;

    case constants.LOGIN_RSP:
      // check if either acc_exists is false or correct_password is false;
      if (message.status == constants.SUCCESS) {
        // login successful
        // need to get name from message
        // req.session.name = message.name;
        res.redirect('/');
      } 
      else if (message.status == constants.FAILURE) {
        // password is incorrect
        
      }
      else if (message.status == constants.DOES_NOT_EXIST) {
        // email does not exist
        
      }
      break;

    // more responses to add
  }
};

/**
 * Initiates a connection to the main server via TCP socket and sends the given request.
 * The function then waits for a response from the server. Upon receiving it, appropiate actions
 * are taken based on the response and then the connection is closed.
 * @param  req_to_server the request to be sent to the server
 * @param  req the HTTP request from the browser
 * @param  res the HTTP response to the browser
 * @return nothing
 */
socket = function(req_to_server, req, res) {
  // connect to the main server
  var connection = net.createConnection({port: constants.MAINSERVER_PORT, host : constants.HOST});
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

  // signal that all data has been received, so take action
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
  });

  connection.on('close', function(had_error) {
    if (had_error) debug('Socket closed due to a transmission error');
    else debug('Socket closed');
  });

};