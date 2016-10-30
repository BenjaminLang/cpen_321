var path = require('path');
var express = require('express'),
    app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);
// var other_server = require("socket.io-client")('http://localhost:6969');
var net = require('net');
var client = net.connect({port: 6969}, function() {
  console.log('connected to server!');
});

// Serve static files (HTML, CSS, JS) from the public directory
app.use(express.static(path.join(__dirname, 'public')));


io.on('connection', function (socket) {
	console.log( 'Client connected.' );
	
  socket.on('disconnect', function () {
  	console.log( 'Client disconnected.' );
  });
 
  socket.on('search item', function (item) {
    var json_request = {"message_type" : "read", "collection" : item};
    
    // send json_request to main server
    // other_server.emit('read request', json_request);
    client.write(JSON.stringify(json_request));
  });

});

client.on('data', function(data) {
  // send the response to the website
  io.emit('search response', data.toString());
});

/* Handle error events between web server and main server */
client.on('error', function(error){
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

client.on('close', function() {
  http.close();
  //console.log("CLOSED");
  /*
  client.connect({port: 6969}, function() {
    console.log("Attempting to reconnect");
  });
  */
});

/*
other_server.on('connect', function(){
  console.log("Connected to main server.");
  other_server.on('disconnect', function(){
    console.log("Disconnected from main server.");
  });
});
*/

/* Binds and listens for connections on port 80.
Also prints a relevant statement to the console */
http.listen(80, function() {
	console.log('Listening on port 80');
});




