/**
 * The web server for smart_shopper
 */

const HOST = 'ec2-35-160-222-208.us-west-2.compute.amazonaws.com';

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

//const HOST = ip.address();

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
/* ROUTING AND MIDDLEWARE */
/**************************************************************************/

var list_items_response = [[]];
app.set('views', './views');
app.set('view engine', 'pug');

/**
 * Serve static files (HTML, CSS, JS) from the public directory.
 * The express object now looks in the public directory for website files.
 */
app.use(express.static(path.join(__dirname, 'public')));

app.get('/', (req, res) => {
  res.render('index');
});

app.get('/register', (req, res) => {
  res.render('register');
});

app.get('/login', (req, res) => {
  res.render('login');
});

app.get('/logged_in_dashboard', (req, res) => {
  res.render('logged_in_dashboard');
});

app.get('/item_searched', (req, res) => {
  res.render('item_searched', {'list_items': list_items_response});
});

/*
app.post('/register', function(req, res) {
  // when user submits their account info on this page, need to send it to the 
  // main server, and then transfer user to a new page
});
*/

/**************************************************************************/
/* LISTENERS */
/**************************************************************************/

var client = net.connect({port: MAINSERVER_PORT, host : HOST}, () => {
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
client.on('data', (response) => {
  handle_response(response);
  // ---------------- not doing this most likely
  // Upon receiving search response data from main server, inform the browser client and
  // send it to the browser
  // io.emit('search response', response.toString());
  // 
});

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
client.on('close', () => {
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
http.listen(WEBSERVER_PORT , () => {
  console.log('Web server listening on port ' + WEBSERVER_PORT + '...');
});

/**************************************************************************/
/* HELPER FUNCTIONS */
/**************************************************************************/

/**
 * Sends a request to the main server.
 */
var send_request = (socket, data, type) => {

	var json_request = {};

	switch(type) {
		case SEARCH_REQ:  
			json_request.message_type = 'read';
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
	}
  
  socket.write(JSON.stringify(json_request));
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
      list_items_response = message.items.slice();
      break;
    
    // check if acc_created is true or false
    case CREATE_ACC_RSP:
      break;

    case LOGIN_RSP:
      break;
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

var to_title_case = (str) => {
    return str.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
};