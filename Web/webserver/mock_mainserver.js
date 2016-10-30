/*var io = require("socket.io").listen(6969);

io.on("connection", function(socket){
    // Display a connected message
    console.log("Webserver connected.");

    socket.on("disconnect", function() {
    	console.log("Webserver disconnected.");
    });

    // When we receive a request...
    socket.on("read request", function(data){
        // We got a message... I dunno what we should do with this...
        console.log("Request received by main server");
        console.log("User searched for: " + data.collection);
    });
});*/

var net = require('net');
var server = net.createServer(function(connection) { 
   console.log('Webserver connected');
   
   connection.on('end', function() {
      console.log('Webserver disconnected');
   });

   connection.on('data', function(request) {
      console.log(JSON.parse(request).collection + ' kappa');
      //console.log(JSON.stringify(JSON.parse(request).collection));
      connection.write(JSON.parse(request).collection + ' kappa');
      //connection.pipe(connection);
   });

});

server.listen(6969, function() { 
   console.log('server is listening');
});