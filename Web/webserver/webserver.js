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
var logger = require('morgan');

var utility = require(path.join(__dirname, 'functions/utility.js'));
var routes = require(path.join(__dirname, 'functions/routes.js'));
var handlers = require(path.join(__dirname, 'functions/handlers.js'));

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
var queue = {
  'list_items_response' : []
};

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
// for debugging purposes
app.use(logger('dev'));

/**
 * Serve static files (HTML, CSS, JS) from the public directory.
 * The express object now looks in the public directory for website files.
 */
app.use(express.static(path.join(__dirname, 'public')));

app.get('/', routes.home);
app.get('/register', routes.register);
app.get('/login', routes.login);


app.get('/item_searched', (req, res) => {
  var data = '';
  client.on('data', function(chunk) {
    data += chunk;
  });

  client.on('end', function() {
    handlers.response(data, queue);
    res.render('item_searched', {'title': 'Search Results', 'list_items': queue.list_items_response.shift()});
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
  	handlers.request(client, item, SEARCH_REQ);
  });
  
  // browser client submits a new account request
  socket.on(CREATE_ACC_REQ, (acc_info) => {
  	handlers.request(client, acc_info, CREATE_ACC_REQ);
  });

  // browser client submits a login request
  socket.on(LOGIN_REQ, (acc_info) => {
  	handlers.request(client, acc_info, LOGIN_REQ);
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
  handlers.error(error);
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


