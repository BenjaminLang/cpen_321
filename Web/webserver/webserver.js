/**
 * The web server for Checkedout
 */

/**************************************************************************/
/* REQUIRED MODULES */
/**************************************************************************/

var express = require('express'),
    app = express();
var http = require('http').Server(app);
var body_parser = require('body-parser');
var cookie_session = require('cookie-session');
var logger = require('morgan');
var debug = require('debug')('webserver');
var favicon = require('serve-favicon');

var routes = require('./routes.js');
var constants = require('./constants.js');

/**************************************************************************/
/* ROUTING AND MIDDLEWARE */
/**************************************************************************/

// Want to look in './views' for the application's views
app.set('views', './views');
// Want to use 'pug' for our view engine
app.set('view engine', 'pug');

app.use(body_parser.json());      // for parsing application/json
app.use(body_parser.urlencoded({  // for parsing application/x-www-form-urlencoded
  extended: true 
})); 
app.use(cookie_session({          // for storing cookies
  name: 'session',
  keys: ['key1', 'key2']
}));

app.use(logger('dev'));            // for logging http requests from browser

// Serve static files (HTML, CSS, JS) from the public directory.
// The express object now looks in the public directory for website files.
app.use(express.static('./public'));
// icon that displays in the browser tab (Doesn't work for some reason)
// app.use(favicon('./public/favicon.ico'));

// GET requests
app.get('/', routes.home);
app.get('/register', routes.register);
app.get('/login', routes.login);
app.get('/logout', routes.logout);
app.get('/item_searched', routes.item_searched);

// POST requests
app.post('/register', routes.register_post);
app.post('/login', routes.login_post);

//Binds and listens for connections on the webserver port. 
http.listen(constants.WEBSERVER_PORT , function() {
  debug('Web server listening on port ' + constants.WEBSERVER_PORT + '...');
});
