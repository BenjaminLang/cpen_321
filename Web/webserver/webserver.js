/**
 * The web server for smart_shopper
 */

const WEBSERVER_PORT = 80;
const MAINSERVER_PORT = 6969;

/*------------------------------------------------------------------------------*/

var path = require('path');
var express = require('express'),
    app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);
// var other_server = require("socket.io-client")('http://localhost:6969');
var net = require('net');
var client = net.connect({port: MAINSERVER_PORT}, () => {
  console.log('connected to server!');
});

/*------------------------------------------------------------------------------*/

/**
 * Serve static files (HTML, CSS, JS) from the public directory
 */
app.use(express.static(path.join(__dirname, 'public')));

/**
 * Listener for socket between browser client and web server
 */
io.on('connection', (socket) => {
	console.log( 'Client connected.' );
	
  socket.on('disconnect', () => {
  	console.log( 'Client disconnected.' );
  });
 
  socket.on('search item', (item) => {
    var json_request = {"message_type" : "read", "collection" : item};
    
    // send json_request to main server
    // other_server.emit('read request', json_request);
    client.write(JSON.stringify(json_request));
  });

});

/**
 * Send data from the main server to the website.
 */
client.on('data',(data) => {
  io.emit('search response', data.toString());
});

/**
 * Handle error events between web server and main server.
 */
client.on('error',(error) => {
  if (error.code === 'ECONNREFUSED') {
    console.log("Error: main server is not available.");
  }
  else if (error.code === 'ECONNRESET') {
    console.log("Error: connection to main server closed abruptly.");
  }
  else {
    console.log("Error: " + error.code);
  }
});

/**
 * Terminate web server when connection between web server and main server closes
 * (can change this later)
 */
client.on('close', () => {
  http.close();
  /*
  client.connect({port: 6969},() => {
    console.log("Attempting to reconnect");
  });
  */
});

/*
other_server.on('connect',() => {
  console.log("Connected to main server.");
  other_server.on('disconnect',()=> {
    console.log("Disconnected from main server.");
  });
});
*/

/**
 * Binds and listens for connections on the webserver port.
 * Also prints a relevant statement to the console.
 */
http.listen(WEBSERVER_PORT ,() => {
	console.log('Listening on port ' + WEBSERVER_PORT);
});




