var port = 6969;
var net = require('net');

var server = net.createServer(); 

server.on('connection', (socket) => {

   console.log('Webserver connected');
   
   socket.on('end', () => {
      console.log('Webserver disconnected');
   });

   socket.on('data', (request) => {

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
      socket.write(JSON.stringify(json_response)); 
   });


   socket.on('error',(error) => {
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
