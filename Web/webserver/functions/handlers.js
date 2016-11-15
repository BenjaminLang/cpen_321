/**************************************************************************/
/* REQUESTS */
/**************************************************************************/

const SEARCH_REQ = 'search_request';
const CREATE_ACC_REQ = 'create_account_request';
const LOGIN_REQ = 'login_request';
// more to be added

/**************************************************************************/
/* RESPONSES */
/**************************************************************************/

const READ_RSP = 'read_response';
const CREATE_ACC_RSP = 'acc_create_response';
const LOGIN_RSP = 'acc_login_response';
// more to be added


//var net = require('net');
var utility = require('./utility.js');

/**
 * Sends a request to the main server.
 */
exports.request = function(socket, data, type) {

	var json_request = {};

	switch(type) {
		case SEARCH_REQ:  
			json_request.message_type = 'read';
      json_request.options = {'price' : 'none', 'num' : '10'};
			json_request.items = [data];
      // json_request.userID 
      // json_request.options
			break;
										
		case CREATE_ACC_REQ:
			json_request.message_type = 'acc_create';
			json_request.userID = data.userID;
			json_request.password = data.password;
			break;

		case LOGIN_REQ:
			json_request.message_type = 'acc_login';
			json_request.userID = data.userID;
			json_request.password = data.password;
			break;

    // more requests to add
	}
  
  socket.write(JSON.stringify(json_request));
  //socket.end();

};

/**
 * Handles responses from the main server.
 */
exports.response = function (response, object) {
  var message = JSON.parse(response.toString());
  var type = message.message_type;
  switch(type) {
    // need to extract array of items from response and pass it to the render call
    // only feasible way is to store this in a global variable
    case READ_RSP:
      // Convert item names and stores to title case
      for (var i = 0; i < message.items.length; i++) {
        for (var j = 0; j < message.items[i].length; j++) {
          message.items[i][j].data.name = utility.to_title_case(message.items[i][j].data.name);
          message.items[i][j].data.store = utility.to_title_case(message.items[i][j].data.store);
        }
      }
      
      //list_items_response = message.items.slice();
      object.list_items_response.push(message.items);
      break;
    
    // check if acc_created is true or false
    case CREATE_ACC_RSP:
      break;

    case LOGIN_RSP:
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




