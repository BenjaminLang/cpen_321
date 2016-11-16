/**************************************************************************/
/* UTILITY FUNCTIONS */
/**************************************************************************/

/**
 * Converts the given string to title case.
 * E.g. 'hello world' becomes 'Hello World'
 */
exports.to_title_case = function(str) {
    return str.replace(/\w\S*/g, function(txt) {
    	return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
    });
};