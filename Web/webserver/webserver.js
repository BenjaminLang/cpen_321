/**
 * The web server for smart_shopper
 */

/**************************************************************************/
/* PORTS */
/**************************************************************************/
const WEBSERVER_PORT = 8080;
const MAINSERVER_PORT = 6969;

/**************************************************************************/
/* REQUIRED MODULES */
/**************************************************************************/

var path = require('path');
var express = require('express'),
    app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var net = require('net');
var ip = require('ip');

/**************************************************************************/
/* ROUTING AND MIDDLEWARE */
/**************************************************************************/

var list_items_response = [[]];
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
  res.render('item_searched', {'list_items': list_items_response});
});

/*
app.post('/register', function(req, res) {
  // when user submits their account info on this page, need to send it to the 
  // main server, and then transfer user to a new page
});
*/


/**************************************************************************/
/* LISTENERS */
/**************************************************************************/

var client = net.connect({port: MAINSERVER_PORT, host : ip.address()}, () => {
  console.log('Connected to main server!');
});

/**
 * Listener for socket between browser client and web server
 */
io.on('connection', (socket) => {
  console.log('Client connected.');
  
  socket.on('disconnect', () => {
    console.log('Client disconnected.');
  });
 
  // When browser client submits a search request...
  socket.on('search_request', (item) => {
    // ...convert request to a JSON object...
    var json_request = {
      'message_type' : 'read',
      'collection' : item
    };
    
    // ... and send it to the main server
    client.write(JSON.stringify(json_request));
  });

  // When browser client submits a new account request...
  socket.on('create_account_request', (acc_info) => {

    var json_request = {
      'message_type' : 'acc_create',
      'userID' : acc_info.userID,
      'password' : acc_info.password,
    };

    // ... and send it to the main server
    client.write(JSON.stringify(json_request));
  });

  // When browser client submits a login request...
  socket.on('login_request', (acc_info) => {

    var json_request = {
      'message_type' : 'acc_login',
      'userID' : acc_info.userID,
      'password' : acc_info.password,
    };

    // ... and send it to the main server
    client.write(json_request);
  });

});

/**
 * Listener for responses from the main server
 */
client.on('data',(data) => {
  // Need to check what kind of response I'm getting from the main server
  // var json_data = JSON.parse(data);
  // Is it a response to an item search request?
  if (data.message_type === 'read_response') {
    // need to extract array of items from data and pass it to the render call
    // only feasible way is to store this in a global variable
    list_items_response = data.items.slice();
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

/**************************************************************************/
/* EXPERIMENTAL CODE */
/**************************************************************************/

// var other_server = require("socket.io-client")('http://localhost:6969');
// other_server.emit('read request', json_request);
/*
other_server.on('connect',() => {
  console.log("Connected to main server.");
  other_server.on('disconnect',()=> {
    console.log("Disconnected from main server.");
  });
});
*/



