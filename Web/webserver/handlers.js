
//var utility = require('./utility.js');
var constants = require('./constants.js');
var debug = require('debug')('handlers');

/**
 * Sends a request to the main server.
 */
exports.request = function(socket, data, type) {

	var json_request = {};
  json_request.message_type = type;

	switch(type) {
		case constants.SEARCH_REQ:  
      json_request.options = {'price' : 'none', 'num' : '-1'};
			json_request.items = [data];
      // json_request.name 
      // json_request.options
			break;
										
		case constants.CREATE_ACC_REQ:
			json_request = data;
      json_request.message_type = type;
			break;

		case constants.LOGIN_REQ:
      json_request = data;
      json_request.message_type = type;
			break;

    // more requests to add
	}
  
  debug('Request formed. Is socket destroyed? ' + socket.destroyed);
  debug('Writing to socket now...');
  socket.write(JSON.stringify(json_request));
};

/**
 * Handles responses from the main server.
 */
exports.response = function (server_res, web_req, web_res) {
  
  var message = JSON.parse(server_res.toString());
  var type = message.message_type;
  // debug(message.items);
  switch(type) {
    case constants.READ_RSP:
      // Convert item names and stores to title case
      for (var i = 0; i < message.items.length; i++) {
          //debug(message.items[i].data.image);
          message.items[i].data.name = message.items[i].data.name.replace('&amp;', '&');
          //message.items[i].data.name = utility.to_title_case(message.items[i].data.name);
          //message.items[i].data.store = utility.to_title_case(message.items[i].data.store);
      }
      
      web_res.render('item_searched', {'title': 'Search Results', 'list_items': message.items});
      break;
    
    // check if acc_created is true or false
    case constants.CREATE_ACC_RSP:
      // if account creation is successful; then redirect to home page
      // 
      if (message.status == constants.SUCCESS) {
        web_req.session.name = web_req.body.name;
        web_res.redirect('/');
      }
      
      // otherwise,
      break;

    case constants.LOGIN_RSP:
      // check if either acc_exists is false or correct_password is false;
      if (message.status == constants.SUCCESS) {
        web_req.session.name = web_req.body.name;
        web_res.redirect('/');
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
 * Handles errors between the web server and main server. 
 */
/*
exports.error = function(error) {
  switch(error.code){
    case 'ECONNREFUSED':
      console.log("Error: main server is not available.");
      break;

    case 'ECONNRESET':
      console.log("Error: connection to main server closed abruptly.");
      break;

    default:
      console.log("Error: " + error.code); 
  }
};
*/
////////////////




