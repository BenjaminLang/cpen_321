var port = 6969;
var net = require('net');
var debug = require('debug')('mock_mainserver');

var server = net.createServer(); 

server.on('connection', (socket) => {
   

   debug('Webserver connected');

   // assign a random id
   //socket.id = Math.floor(Math.random() * 1000);
   //debug('This socket\'s id is: ' + socket.id);

   /*
   socket.on('end', () => {
      debug('Webserver disconnected');
   });
  */
   socket.on('data', (request) => {
      debug('request received');

      var sample_item_data_1 = {
        'name' : 'red apples',
        'price' : '1.50',
        'store' : 'save on foods',
        //'image' : 'green-apple.png'
        // 'image' : '//www.brandsoftheworld.com/sites/default/files/styles/logo-thumbnail/public/082016/untitled-1_5.png?itok=8JWuhnSo'
        'image' : '//static.pexels.com/photos/39803/pexels-photo-39803.jpeg'
      };

      var sample_item_data_2 = {
        'name' : 'meme',
        'price' : '4.20',
        'store' : 'costco',
        //'image' : 'green-apple.png'
         'image' : '//www.brandsoftheworld.com/sites/default/files/styles/logo-thumbnail/public/082016/untitled-1_5.png?itok=8JWuhnSo'
        //'image' : '//static.pexels.com/photos/39803/pexels-photo-39803.jpeg'
      };

      var sample_item_data;
      if (JSON.parse(request).items[0] === 'apple') {
        sample_item_data = sample_item_data_1;
      }
      else sample_item_data = sample_item_data_2;

      var sample_item = {
        'id' : '123',
        'data' : sample_item_data
      };

      var json_response = {
        'message_type' : 'read_response',
        'items' : [[]]
      };

      for (var i = 0; i < 500; i++) {
        json_response.items[0].push(sample_item);
      }
      
      debug("sending a response...");
      socket.write(JSON.stringify(json_response));
      socket.end(); 
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
   debug('Main server is listening...');
});
