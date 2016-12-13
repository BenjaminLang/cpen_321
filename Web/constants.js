const constants = {
	// HOSTS
	HOST : 'ryangroup.westus.cloudapp.azure.com',
	//HOST : '13.88.11.52',

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
	GET_LIST_REQ : 'get_list',
	GET_LIST_NAMES_REQ : 'get_list_names',
	DEL_LIST_REQ : 'delete_list',
	RECOMMEND_REQ : 'recommend',
	VERIFY_REQ : 'acc_verify',

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
	RECOMMEND_RSP : 'recommend_response',
	VERIFY_RSP : 'verify_response',

	// STATUSES
	SUCCESS : 'success',
	FAILURE : 'failed',
	DOES_NOT_EXIST : 'DNE',
	NOT_VERIFIED : 'Not Verified',
	EXCEPTION : 'exception',

	// ERRORS
	ADDRESS_IN_USE : 'EADDRINUSE',
	CONNECTION_REFUSED : 'ECONNREFUSED',
	CONNECTION_RESET : 'ECONNRESET',

	// FILE PATHS
	PRIVATE_KEY_LOCATION : '/etc/letsencrypt/live/checkedout.ca/privkey.pem',
	CERTIFICATE_LOCATION : '/etc/letsencrypt/live/checkedout.ca/fullchain.pem',

	// DEFAULTS VALUES
	DEFAULT_STORES : 'Shopping Location Options',
	DEFAULT_ITEMS_PER_PAGE : 'Items Per Page',

	// ALERT TYPES
	ALERT_SUCCESS : ['alert', 'alert-success', 'alert-dismissable', 'fade', 'in'],
	ALERT_INFO : 	['alert', 'alert-info', 'alert-dismissable', 'fade', 'in'],
	ALERT_WARNING : ['alert', 'alert-warning', 'alert-dismissable', 'fade', 'in'],
	ALERT_DANGER : 	['alert', 'alert-danger', 'alert-dismissable', 'fade', 'in']
};

module.exports = constants;
