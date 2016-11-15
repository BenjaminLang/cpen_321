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
var debug = require('debug')('webserver');

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
// should be very wary about these variables; how will the webserver behave when multiple clients connect?
var data_ready = false;
var data = '';
// Queue for responses from main server
var queue = {
  'list_items_response' : []
};
var interval;
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


// Serve static files (HTML, CSS, JS) from the public directory.
// The express object now looks in the public directory for website files.
app.use(express.static(path.join(__dirname, 'public')));

app.get('/', routes.home);
app.get('/register', routes.register);
app.get('/login', routes.login);


app.get('/item_searched', function(req, res) {
  main_server.on('data', function(chunk) {
    data += chunk;
  });

  main_server.on('end', function() {
    handlers.response(data, queue);
    res.render('item_searched', {'title': 'Search Results', 'list_items': queue.list_items_response.shift()});
    //data_ready = true;
  });  

});

/*
app.get('/item_searched', function(req, res, next) {
  debug(!data_ready);
  interval = setInterval(function() {
    debug("Waiting for response from main server...");
  }, 1000);
  data_ready = false;
  debug("Response received!");
  next();
}, function(req, res) {
  res.render('item_searched', {'title': 'Search Results', 'list_items': queue.list_items_response.shift()});
});
*/
/*
app.get('/item_searched', function(req, res) {
  var cb = foo.bind({}, req, res); // cb is now the function foo with two extra arguments prepended
  // call some function with cb as an argument
  cb(2);
});
*/
app.post('/register', routes.register_post);
app.post('/login', routes.login_post);

/*
function foo(req, res, arg) {
  console.log(arg);
  res.render('register', {'title': 'Registration Form'});
}
*/

/**************************************************************************/
/* LISTENERS */
/**************************************************************************/

var main_server = net.connect({port: MAINSERVER_PORT, host : HOST}, function() {
  debug('Connected to main server!');
});

//var main_server = open_socket();
/**
 * Listener for socket between browser client and web server
 */
io.on('connection', (socket) => {
  debug('Client connected.');
  
  socket.on('disconnect', () => {
    debug('Client disconnected.');
  });
 
  // browser client submits a search request
  socket.on(SEARCH_REQ, (item) => {
    debug("Search request for " + item + " received.");
  	handlers.request(main_server, item, SEARCH_REQ);
  });
  
  // browser client submits a new account request
  socket.on(CREATE_ACC_REQ, (acc_info) => {
  	handlers.request(main_server, acc_info, CREATE_ACC_REQ);
  });

  // browser client submits a login request
  socket.on(LOGIN_REQ, (acc_info) => {
  	handlers.request(main_server, acc_info, LOGIN_REQ);
  })
});

/**
 * Listener for responses from the main server
 */

main_server.on('data', function(chunk) {
  data += chunk;
});

main_server.on('end', function() {
  handlers.response(data, queue);
  data_ready = true;
});

/**
 * Listener for error events between web server and main server
 */
main_server.on('error', function(error) {
  handlers.error(error);
});

/**
 * Terminate web server when connection between web server and main server closes
 * (can change this later)
 */
main_server.on('close', function() {
  http.close();
});
	
/**
 * Binds and listens for connections on the webserver port.
 */
http.listen(WEBSERVER_PORT , function() {
  debug('Web server listening on port ' + WEBSERVER_PORT + '...');
});

/*
function open_socket() {
  var socket = net.connect({port: MAINSERVER_PORT, host : HOST});
  socket.setKeepAlive(true);
  socket.on('connect', on_connect.bind({}, socket));
  socket.on('data', on_data.bind({}, socket));
  socket.on('end', on_end.bind({}, socket));
  socket.on('error', on_error.bind({}, socket));
  return socket;
}

var interval;
function on_connect(socket) {
  console.log('Socket is open!');

   interval = setInterval(function() {
        console.log("SPAM");
    }, 1000);
}

function on_data(socket, chunk) {
  // console.log('got data!');
  data += chunk;
}

function on_end(socket) {
  handlers.response(data, queue);
  data_ready = true;
}

function on_error(socket, error) {

    console.log('Socket error!');
    console.log(error.code);
    // Kill socket
    clearInterval(interval);
    socket.destroy();
    socket.unref();

    // Re-open socket
    setTimeout(open_socket, 1000);
}

*/
