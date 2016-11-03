var port = 6969;
var net = require('net');
var server = net.createServer((connection) => { 
   console.log('Webserver connected');
   
   connection.on('end', () => {
      console.log('Webserver disconnected');
   });

   connection.on('data', (request) => {
      // for now, just send back the request concatenated with a word
      var sample_item_data = {
        'name' : 'Green Apples',
        'price' : '1.50',
        'store' : 'save on foods',
        'image link' : 'green-apple.png'
        //'image link' : 'http://www.brandsoftheworld.com/sites/default/files/styles/logo-thumbnail/public/082016/untitled-1_5.png?itok=8JWuhnSo'
      };

      var sample_item = {
        'id' : '123',
        'data' : sample_item_data
      }

      var json_response = {
        'message_type' : 'read_response',
        'items' : [[]]
      };

      for (var i = 0; i < 100; i++) {
        json_response.items[0].push(sample_item);
      }

      connection.write(JSON.stringify(json_response)); 
      //connection.write(JSON.parse(request).collection + ' kappa');
   });

   connection.on('error',(error) => {
      if (error.code === 'ECONNRESET') {
        console.log("Error: webserver disconnected.");
      }
      else {
        console.log("Error: " + error.code);
      }
   });
});

server.listen(port, () => { 
   console.log('Main server is listening...');
});

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