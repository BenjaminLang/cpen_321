
var utility = require('./utility.js');
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
      json_request.options = {'price' : 'none', 'num' : '10'};
			json_request.items = [data];
      // json_request.userID 
      // json_request.options
			break;
										
		case constants.CREATE_ACC_REQ:
			json_request.userID = data.userID;
			json_request.password = data.password;
			break;

		case constants.LOGIN_REQ:
			json_request.userID = data.userID;
			json_request.password = data.password;
			break;

    // more requests to add
	}
  
  debug('Request formed. Is the socket destroyed?');
  debug(socket.destroyed);
  debug('Writing to the socket now...');
  socket.write(JSON.stringify(json_request));
};

/**
 * Handles responses from the main server.
 */
exports.response = function (web_req, web_res, server_res) {
  
  var message = JSON.parse(server_res.toString());
  var type = message.message_type;
  switch(type) {
    case constants.READ_RSP:
      // Convert item names and stores to title case
      for (var i = 0; i < message.items.length; i++) {
        for (var j = 0; j < message.items[i].length; j++) {
          message.items[i][j].data.name = utility.to_title_case(message.items[i][j].data.name);
          message.items[i][j].data.store = utility.to_title_case(message.items[i][j].data.store);
        }
      }
      
      //object.list_items_response.push(message.items);
      web_res.render('item_searched', {'title': 'Search Results', 'list_items': message.items});
      break;
    
    // check if acc_created is true or false
    case constants.CREATE_ACC_RSP:
      break;

    case constants.LOGIN_RSP:
      break;

    // more responses to add
  }
};

/**
 * Handles errors between the web server and main server. 
 */
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

////////////////




