/**
 * The web server for Checkedout
 */

/**************************************************************************/
/* REQUIRED MODULES */
/**************************************************************************/

var express = require('express'),
    app = express();

var http = require('http').Server(app);
var net = require('net');

var body_parser = require('body-parser');
var cookie_session = require('cookie-session');
var logger = require('morgan');
var debug = require('debug')('webserver');
var favicon = require('serve-favicon');

var utility = require('./utility.js');
var routes = require('./routes.js');
var handlers = require('./handlers.js');
var constants = require('./constants.js');

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
app.use(cookie_session({          // for storing cookies
  name: 'session',
  keys: ['key1', 'key2']
}));

app.use(logger('dev'));            // for logging http requests from browser

// Serve static files (HTML, CSS, JS) from the public directory.
// The express object now looks in the public directory for website files.
app.use(express.static('./public'));
// icon that displays in the browser tab (Doesn't work for some reason)
// app.use(favicon('./public/favicon.ico'));

// GET requests
app.get('/', routes.home);
app.get('/register', routes.register);
app.get('/login', routes.login);
app.get('/logout', routes.logout);
/*
app.get('/item_searched', function(req, res) {
  
  var data = '';
  // routes.item_searched.bind({}, main_server, req, res)();
  routes.item_searched(main_server, req, res);

  main_server.on('data', function(chunk) {
    data += chunk;
  });

  main_server.on('end', function() {
    debug('handling response...');
    handlers.response(data, req, res);
    debug('response handled; destroying socket ' + main_server.id);
    main_server.destroy();
    main_server.unref();

    // Re-open socket
    setTimeout(open_socket, 1000);
  });  

});
*/
app.get('/item_searched', routes.item_searched);


// POST requests
/*
app.post('/register', function(req, res) {

  var data = '';
  routes.register_post(main_server, req, res);

  main_server.on('data', function(chunk) {
    data += chunk;
  });

  main_server.on('end', function() {
    handlers.response(data, req, res);

    main_server.destroy();
    main_server.unref();

    // Re-open socket
    setTimeout(open_socket, 1000);
  });
});
*/
app.post('/register', routes.register_post);
/*
app.post('/login', function(req, res) {

  var data = '';
  routes.login_post(main_server, req, res);

  main_server.on('data', function(chunk) {
    data += chunk;
  });

  main_server.on('end', function() {
    handlers.response(data, req, res);

    main_server.destroy();
    main_server.unref();

    // Re-open socket
    setTimeout(open_socket, 1000);
  });
});
*/
app.post('/login', routes.login_post);

/**************************************************************************/
/* LISTENERS */
/**************************************************************************/

/*
var main_server = net.createConnection({port: constants.MAINSERVER_PORT, host : constants.HOST}); 

main_server.on('connect', function() {
  main_server.id = Math.floor(Math.random() * 1000);
  debug('Connected to main server!');
  debug('This socket\'s id is: ' + main_server.id);
});
*/
/**
 * Listener for error events between web server and main server
 */
/*
main_server.on('error', function(error) {
  debug('Socket error: ' + error.code);
  // Kill socket
  main_server.destroy();
  main_server.unref();

    // Re-open socket
  if (error.code != constants.CONNECTION_REFUSED)
    setTimeout(open_socket, 1000);
});
*/
/**
 * Terminate web server when connection between web server and main server closes
 * (can change this later)
 */
 /*
main_server.on('close', function(had_error) {
  if (had_error) debug('Transmission error.');
  debug('Connection closed. Terminating webserver...');
  http.close();
});
*/
/**
 * Binds and listens for connections on the webserver port.
 */
http.listen(constants.WEBSERVER_PORT , function() {
  debug('Web server listening on port ' + constants.WEBSERVER_PORT + '...');
});
/*
function open_socket() {
  main_server = net.createConnection({port: constants.MAINSERVER_PORT, host : constants.HOST}); 
}
*/
/**************************************************************************/
/* EXPERIMENTAL STUFF */
/**************************************************************************/

//var main_server = open_socket();

/*
function open_socket() {
  var socket = net.connect({port: constants.MAINSERVER_PORT, host : constants.HOST});
  socket.setKeepAlive(true);
  socket.on('connect', on_connect.bind({}, socket));
  //socket.on('data', on_data.bind({}, socket));
  //socket.on('end', on_end.bind({}, socket));
  socket.on('error', on_error.bind({}, socket));
  socket.on('close', on_close.bind({}, socket));
  var socket_getter = get_socket.bind({}, socket);
  return socket_getter;
}
*/
/*
function on_connect(socket) {
  // assign a random id
  socket.id = Math.floor(Math.random() * 1000);
  //debug('Socket is open!');
  debug('This socket\'s id is: ' + socket.id);
  debug('Local IP address: ' + socket.localAddress);
}
*/
/*
function on_data(socket, chunk) {
  // console.log('got data!');
  data += chunk;
}

function on_end(socket) {
  handlers.response(data, queue);

  // Kill socket
    socket.destroy();
    socket.unref();

    // Re-open socket
    setTimeout(open_socket, 500);
}
*/
/*
function on_error(socket, error) {
    debug('Socket error!');
    debug(error.code);
    // Kill socket
    socket.destroy();
    socket.unref();

    // Re-open socket
    setTimeout(open_socket, 500);
}
*/
/**
 * Terminate web server when connection between web server and main server closes
 * (can change this later)
 */
/*
function on_close(socket) {
  http.close();
}
*/
/*
function get_socket(socket) {
  return socket;
}
*/
