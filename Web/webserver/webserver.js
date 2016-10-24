var path = require('path');
var express = require('express'),
	app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);

// Serve static files (HTML, CSS, JS) from the public directory
app.use(express.static(path.join(__dirname, 'public')));

/* */
io.on('connection', function(socket){
  socket.on('chat message', function(msg){
    io.emit('chat message', msg);
  });
});

/* Binds and listens for connections on port 80.
Also prints a relevant statement to the console */
app.listen(80, function() {
	console.log('Listening on port 80');
});

