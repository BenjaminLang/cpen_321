const constants = {
	// HOSTS
	HOST : 'ryangroup.westus.cloudapp.azure.com',

	// PORTS
	WEBSERVER_PORT : 3000,
	MAINSERVER_PORT : 6969,

	// REQUESTS
	SEARCH_REQ : 'read',
	CREATE_ACC_REQ : 'acc_create',
	LOGIN_REQ : 'acc_login',
	ACC_UPDATE_REQ: 'acc_update',
	ADD_LIST_REQ : 'add_list',
	DEL_LIST_REQ : 'delete_list',
	GET_LIST_REQ : 'retrieve_list',

	// RESPONSES
	READ_RSP : 'read_response',
	CREATE_ACC_RSP : 'acc_create_response',
	LOGIN_RSP : 'login_response',
	ACC_UPDATE_RSP : 'update_response',
	SAVE_LIST_RSP : 'save_list_response',

	// STATUSES
	SUCCESS : 'success',
	FAILURE : 'failed',
	DOES_NOT_EXIST : 'DNE',

	// ERRORS
	ADDRESS_IN_USE : 'EADDRINUSE',
	CONNECTION_REFUSED : 'ECONNREFUSED',
	CONNECTION_RESET : 'ECONNRESET'
};

module.exports = constants;
