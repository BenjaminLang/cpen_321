/**
 * The web server for smart_shopper
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


/**************************************************************************/
/* HOSTS */
/**************************************************************************/

const HOST = ip.address(); // returns local ip address
//const HOST = 'ec2-35-160-222-208.us-west-2.compute.amazonaws.com';
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

//var list_items_response = [[]];
// Queue for responses from main server
var list_items_response = [];

/**************************************************************************/
/* ROUTING AND MIDDLEWARE */
/**************************************************************************/

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

app.post('/register', (req, res) => {
  console.log(req);
});

app.get('/login', (req, res) => {
  res.render('login');
});

app.get('/logged_in_dashboard', (req, res) => {
  res.render('logged_in_dashboard');
});

app.get('/item_searched', (req, res) => {
  // res.render('item_searched', {'list_items': list_items_response});
  res.render('item_searched', {'list_items': list_items_response.shift()});
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

    // more requests to add
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
      // console.log(message.items);
      // Convert item names and stores to title case
      for (var i = 0; i < message.items.length; i++) {
        for (var j = 0; j < message.items[i].length; j++) {
          message.items[i][j].data.name = to_title_case(message.items[i][j].data.name);
          message.items[i][j].data.store = to_title_case(message.items[i][j].data.store);
        }
      }
      
      //list_items_response = message.items.slice();
      list_items_response.push(message.items);
      //console.log(list_items_response);
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

/**
 * Converts the given string to title case.
 * E.g. 'hello world' becomes 'Hello World'
 */

var to_title_case = (str) => {
    return str.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
};
