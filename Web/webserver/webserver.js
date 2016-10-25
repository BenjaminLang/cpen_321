var path = require('path');
var express = require('express'),
	app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);

// Serve static files (HTML, CSS, JS) from the public directory
app.use(express.static(path.join(__dirname, 'public')));

io.on('connection', function (socket) {
	console.log( 'Hello World' );
	/*
    socket.on( "echo", function ( msg, callback ) {
        callback( msg );
    } );
    */
	
  socket.on('disconnect', function () {
  	console.log( 'Goodbye' );
  });

  socket.emit('news', { hello: 'world' });
  socket.on('my other event', function (data) {
    console.log(data);
  });

});

// This doesn't work; need to use "socket.on" within io.on{} above
/* 
io.on('disconnect', function (socket) {
	console.log( 'Goodbye' );
}); 
*/
/* Binds and listens for connections on port 80.
Also prints a relevant statement to the console */
http.listen(80, function() {
	console.log('Listening on port 80');
});

//var ioc = require('socket.io-client');
//var client = ioc.connect( "http://localhost:80");

