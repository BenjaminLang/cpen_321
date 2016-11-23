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

var handlers = require('./handlers.js');
var constants = require('./constants.js');

/**************************************************************************/
/* ROUTING AND MIDDLEWARE */
/**************************************************************************/

// Want to look in './views' for the application's views
app.set('views', './views');
// Want to use 'pug' for our view engine
app.set('view engine', 'pug');

// for parsing application/json
app.use(body_parser.json()); 

// for parsing application/x-www-form-urlencoded     
app.use(body_parser.urlencoded({ extended: true }));

// for storing cookies
app.use(cookie_session({ name: 'session', keys: ['key1', 'key2']}));

// for logging http requests from browser
app.use(logger('dev'));

// Serve static files (HTML, CSS, JS) from the public directory.
app.use(express.static('./public'));
// icon that displays in the browser tab (Doesn't work for some reason)
// app.use(favicon('./public/favicon.ico'));

// GET requests
app.get('/', handlers.home);
app.get('/register', handlers.register);
app.get('/login', handlers.login);
app.get('/logout', handlers.logout);
app.get('/item_searched', handlers.item_searched);
app.get('/update', handlers.update);

// POST requests
app.post('/register', handlers.register_post);
app.post('/login', handlers.login_post);
app.post('/save_list', handlers.save_list);

/////////////

// Binds and listens for connections on the webserver port. 
http.listen(constants.WEBSERVER_PORT , function() {
  debug('Web server listening on port ' + constants.WEBSERVER_PORT + '...');
});
