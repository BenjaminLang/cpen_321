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
var port = 6969;
var net = require('net');
var server = net.createServer((connection) => { 
   console.log('Webserver connected');
   
   connection.on('end', () => {
      console.log('Webserver disconnected');
   });

   connection.on('data', (request) => {
      // for now, just send back the request concatenated with a word
      connection.write(JSON.parse(request).collection + ' kappa');
   });
});

server.listen(port, () => { 
   console.log('server is listening');
});