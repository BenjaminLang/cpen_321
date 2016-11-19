var net = require('net');
var debug = require('debug')('handlers');

var constants = require('./constants.js');


/**
 * Sends a request to the main server.
 */
exports.request = function(type, req, res) {

  var json_request = {};
  json_request.message_type = type;

  switch(type) {
    case constants.SEARCH_REQ:  
      json_request.options = {'price' : 'none', 'num' : '-1'};
      json_request.items = [req.query.item];
      // json_request.name 
      // json_request.options
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
  }

  socket(json_request, req, res);
};

/**
 * Handles responses from the main server.
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
        // 
        req.session.name = req.body.name;
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

socket = function(req_to_server, req, res) {
  // connect to the main server
  var connection = net.createConnection({port: constants.MAINSERVER_PORT, host : constants.HOST});
  var data = '';

  connection.on('connect', function() {
    debug('Connected to main server!');
    debug('Sending request now...');
    connection.write(JSON.stringify(req_to_server));
  });

  connection.on('data', function(packet) {
    data += packet;
  });

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

////////////////




