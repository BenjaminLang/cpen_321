/**
 * The web server for Checkedout
 */

/**************************************************************************/
/* REQUIRED MODULES */
/**************************************************************************/
var fs = require('fs');
var express = require('express'),
    app = express();
var https = require('https');
var body_parser = require('body-parser');
var cookie_session = require('cookie-session');
var logger = require('morgan');
var debug = require('debug')('main');
var favicon = require('serve-favicon');

// Our files
var handler = require('./handler.js');
var constants = require('./constants.js');

// SSL
var private_key  = fs.readFileSync(constants.PRIVATE_KEY_LOCATION, 'utf8');
var certificate = fs.readFileSync(constants.CERTIFICATE_LOCATION, 'utf8');
var credentials = {key: private_key, cert: certificate};

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
// Serve static files from the public directory.
app.use(express.static('./public'));
// icon that displays in the browser tab
app.use(favicon('./public/favicon.ico'));

// MIDDLEWARE
function require_login(req, res, next) {
	if (!req.session.user) {
    res.redirect('/login');
  } else {
    next();
  }
};

function require_logout(req, res, next) {
  if (req.session.user) {
    res.redirect('/');
  } else {
    next();
  }
}

// GET requests
app.get('/', handler.home);
app.get('/register', require_logout, handler.register);
app.get('/delete_acc', require_login, handler.delete_acc);
app.get('/login', require_logout, handler.login);
app.get('/logout', handler.logout);
app.get('/item_searched', handler.item_searched);
app.get('/update', require_login, handler.update);
app.get('/manage_lists', require_login, handler.manage_lists);
app.get('/verify', handler.verify);
app.get('/get_list', handler.get_list);

// POST requests
app.post('/register', handler.register_post);
app.post('/delete_acc', handler.delete_acc_post); 
app.post('/login', handler.login_post);
app.post('/update', require_login, handler.update_post);
app.post('/save_list', handler.save_list);
app.post('/verify', handler.verify_post);

app.use(function (req, res, next) {
  res.status(404).send('Sorry, cant find that!')
})

/////////////

var httpsServer = https.createServer(credentials, app);

// Binds and listens for connections on the web server port. 
httpsServer.listen(constants.WEBSERVER_PORT , function() {
  debug('Web server listening on port ' + constants.WEBSERVER_PORT + '...');
});
