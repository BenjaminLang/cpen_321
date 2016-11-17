var ip = require('ip');

/**************************************************************************/
/* HOSTS */
/**************************************************************************/

//exports.HOST = ip.address(); // returns local ip address
exports.HOST = 'ryangroup.westus.cloudapp.azure.com'; // main server host

/**************************************************************************/
/* PORTS */
/**************************************************************************/

 exports.WEBSERVER_PORT = 8080;
 exports.MAINSERVER_PORT = 6969;

/**************************************************************************/
/* REQUESTS */
/**************************************************************************/

exports.SEARCH_REQ = 'read';
exports.CREATE_ACC_REQ = 'acc_create';
exports.LOGIN_REQ = 'acc_login';
// more to be added

/**************************************************************************/
/* RESPONSES */
/**************************************************************************/

exports.READ_RSP = 'read_response';
exports.CREATE_ACC_RSP = 'acc_create_response';
exports.LOGIN_RSP = 'acc_login_response';
// more to be added

/**************************************************************************/
/* ERRORS */
/**************************************************************************/

exports.ADDRESS_IN_USE = 'EADDRINUSE';
exports.CONNECTION_REFUSED = 'ECONNREFUSED';
exports.CONNECTION_RESET = 'ECONNRESET';