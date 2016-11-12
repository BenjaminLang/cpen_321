/**
 * The web server for Checkedout
 */

/**************************************************************************/
/* REQUIRED MODULES */
/**************************************************************************/

var path = require('path');
var express = require('express'),
    app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var net = require('net');
var ip = require('ip');
var body_parser = require('body-parser');
var cookie_session = require('cookie-session');

var utility = require(path.join(__dirname, 'functions/utility.js'));
var routes = require(path.join(__dirname, 'functions/routes.js'));

/**************************************************************************/
/* HOSTS */
/**************************************************************************/

const HOST = ip.address(); // returns local ip address
//const HOST = 'ryangroup.westus.cloudapp.azure.com';

/**************************************************************************/
/* PORTS */
/**************************************************************************/

const WEBSERVER_PORT = 8080;
const MAINSERVER_PORT = 6969;

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

/**************************************************************************/
/* GLOBAL VARIABLES FOR COMMUNICATION WITH BROWSER */
/**************************************************************************/

// Queue for responses from main server
var list_items_response = [];

/**************************************************************************/
/* ROUTING AND MIDDLEWARE */
/**************************************************************************/

// Want to look in './views' for the application's views
app.set('views', './views');

// Want to use 'pug' for our view engine
app.set('view engine', 'pug');

app.use(body_parser.json());      // for parsing application/json
app.use(body_parser.urlencoded({  // for parsing application/x-www-form-urlencoded
 extended: true 
})); 
app.use(cookie_session({
  name: 'session',
  keys: ['key1', 'key2']
}));

/**
 * Serve static files (HTML, CSS, JS) from the public directory.
 * The express object now looks in the public directory for website files.
 */
app.use(express.static(path.join(__dirname, 'public')));

app.get('/', routes.home);
app.get('/register', routes.register);
app.get('/login', routes.login);

app.get('/logged_in_dashboard', function(req,res) {
  res.render('logged_in_dashboard');
});

app.get('/item_searched', (req, res) => {
  var data = '';
  client.on('data', function(chunk) {
    data += chunk;
  });

  client.on('end', function() {
    handle_response(data);
    res.render('item_searched', {'title': 'Search Results', 'list_items': list_items_response.shift()});
  });
  
});

app.post('/register', routes.register_post);
app.post('/login', routes.login_post);


/**************************************************************************/
/* LISTENERS */
/**************************************************************************/

var client = net.connect({port: MAINSERVER_PORT, host : HOST}, function() {
  console.log('Connected to main server!');
});

/**
 * Listener for socket between browser client and web server
 */
io.on('connection', (socket) => {
  console.log('Client connected.');
  
  socket.on('disconnect', () => {
    console.log('Client disconnected.');
  });
 
  // browser client submits a search request
  socket.on(SEARCH_REQ, (item) => {
    console.log("Search request for " + item + " received.");
  	send_request(client, item, SEARCH_REQ);
  });
  
  // browser client submits a new account request
  socket.on(CREATE_ACC_REQ, (acc_info) => {
  	send_request(client, acc_info, CREATE_ACC_REQ);
  });

  // browser client submits a login request
  socket.on(LOGIN_REQ, (acc_info) => {
  	send_request(client, acc_info, LOGIN_REQ);
  })
});

/**
 * Listener for responses from the main server
 */
/*
client.on('data', (response) => {
  handle_response(response);
  // ---------------- not doing this most likely
  // Upon receiving search response data from main server, inform the browser client and
  // send it to the browser
  // io.emit('search response', response.toString());
  //
});
*/

/**
 * Listener for error events between web server and main server
 */
client.on('error',(error) => {
  handle_error(error);
});

/**
 * Terminate web server when connection between web server and main server closes
 * (can change this later)
 */
client.on('close', function() {
  http.close();
  // Should we attempt to reconnect?
  /*
  client.connect({port: 6969},() => {
    console.log("Attempting to reconnect");
  });
  */
});
	
/**
 * Binds and listens for connections on the webserver port.
 */
http.listen(WEBSERVER_PORT , function() {
  console.log('Web server listening on port ' + WEBSERVER_PORT + '...');
});



/**
 * Sends a request to the main server.
 */
var send_request = (socket, data, type) => {

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
  socket.end();
};

/**
 * Handles responses from the main server.
 */
var handle_response = (response) => {
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
      list_items_response.push(message.items);
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
var handle_error = (error) => {
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
