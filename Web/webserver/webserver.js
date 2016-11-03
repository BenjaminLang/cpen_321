/**
 * The web server for smart_shopper
 */

/*------------------------------------------------------------------------------*/
/**
 * Ports
 */
const WEBSERVER_PORT = 8080;
const MAINSERVER_PORT = 6969;

/*------------------------------------------------------------------------------*/

var path = require('path');
var express = require('express'),
    app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);
// var other_server = require("socket.io-client")('http://localhost:6969');
var net = require('net');
var ip = require("ip");
var client = net.connect({port: MAINSERVER_PORT, host : ip.address()}, () => {
  console.log('Connected to main server!');
});

/*------------------------------------------------------------------------------*/

var response_rdy = false;
var price_1 = 0;
var price_2 = 0;
app.set('views', './views');
app.set('view engine', 'pug');

/**
 * Serve static files (HTML, CSS, JS) from the public directory.
 * The express object now looks in the public directory for website files.
 */
app.use(express.static(path.join(__dirname, 'public')));

app.get('/', function(req, res) {
  res.sendFile('index.html');
});

app.get('/register', function(req, res) {
  res.sendFile('register.html');
});

app.get('/login', function(req, res) {
  res.sendFile('login.html');
});

app.get('/logged_in_dashboard', function(req, res) {
  res.render('logged_in_dashboard');
});

/**
 * 
 */
app.get('/item_searched', function(req, res) {
  // Need to update the item_searched page based on what I get
  // from the main server here
  res.render('item_searched', { 'price_1' : price_1, 'price_2' : price_2 });
});

/*
app.post('/register', function(req, res) {
  // when user submits their account info on this page, need to send it to the 
  // main server, and then transfer user to a new page
});
*/
var update_prices = function(item) {
  price_1 = item;
  price_2 = item;
}

/**
 * Listener for socket between browser client and web server
 */
io.on('connection', (socket) => {
  console.log( 'Client connected.' );
  
  socket.on('disconnect', () => {
    console.log( 'Client disconnected.' );
  });
 
  // When browser client submits a search request...
  socket.on('search_request', (item) => {

    update_prices(item);
    /*
    // ...convert request to a JSON object...
    var json_request = {"message_type" : "read", "collection" : item};
    
    // ... and send it to the main server
    // other_server.emit('read request', json_request);
    client.write(JSON.stringify(json_request));
    */
  });

  // When browser client submits a new account request...
  socket.on('create_account_request', () => {

  });

  // When browser client submits a login request...
  socket.on('login_request', () => {

  });

});

/**
 * Listener for responses from the main server
 */
client.on('data',(data) => {
  // Need to check what kind of response I'm getting from the main server

  // Is it a response to an item search request?
  if (data.message_type === 'response') {
    // need to update the appropriate variables in the item_searched template
  }
  else if (data.message_type === 'acc_create_response') {
    // check if acc_created is true or false
  }
  else {
    //
  }

  // ---------------- not doing this most likely
  // Upon receiving search response data from main server, inform the browser client and
  // send it to the browser
  // io.emit('search response', data.toString());
  // 

  // 
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
  // Should we attempt to reconnect?
  /*
  client.connect({port: 6969},() => {
    console.log("Attempting to reconnect");
  });
  */
});

/**
 * Binds and listens for connections on the webserver port.
 */
http.listen(WEBSERVER_PORT , () => {
  console.log('Listening on port ' + WEBSERVER_PORT + '...');
});

/*---------------------------------TEST STUFF---------------------------------*/

/*
other_server.on('connect',() => {
  console.log("Connected to main server.");
  other_server.on('disconnect',()=> {
    console.log("Disconnected from main server.");
  });
});
*/



