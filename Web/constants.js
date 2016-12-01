const constants = {
	// HOSTS
	HOST : 'ryangroup.westus.cloudapp.azure.com',

	// PORTS
	WEBSERVER_PORT : 3000,
	MAINSERVER_PORT : 6969,

	// REQUESTS
	SEARCH_REQ : 'read',
	CREATE_ACC_REQ : 'acc_create',
	DEL_ACC_REQ : 'acc_delete',
	LOGIN_REQ : 'acc_login',
	ACC_UPDATE_REQ: 'acc_update',
	ADD_LIST_REQ : 'add_list',
	GET_LIST_REQ : 'retrieve_list',
	GET_LIST_NAMES_REQ : 'get_list_names',
	DEL_LIST_REQ : 'delete_list',

	// RESPONSES
	READ_RSP : 'read_response',
	CREATE_ACC_RSP : 'acc_create_response',
	DEL_ACC_RSP : 'acc_delete_response',
	LOGIN_RSP : 'acc_login_response',
	ACC_UPDATE_RSP : 'acc_update_response',
	ADD_LIST_RSP : 'add_list_response',
	GET_LIST_RSP : 'get_list_response',
	GET_LIST_NAMES_RSP : 'get_list_names_response',
	DEL_LIST_RSP : 'delete_list_response',

	// STATUSES
	SUCCESS : 'success',
	FAILURE : 'failed',
	DOES_NOT_EXIST : 'DNE',

	// ERRORS
	ADDRESS_IN_USE : 'EADDRINUSE',
	CONNECTION_REFUSED : 'ECONNREFUSED',
	CONNECTION_RESET : 'ECONNRESET',

	// FILE PATHS
	PRIVATE_KEY_LOCATION : '/etc/letsencrypt/live/checkedout.ca/privkey.pem',
	CERTIFICATE_LOCATION : '/etc/letsencrypt/live/checkedout.ca/fullchain.pem'
};

module.exports = constants;
