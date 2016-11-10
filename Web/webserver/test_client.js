var someRandomPort = 8099,
  jot = require('json-over-tcp');

var connection = jot.connect(someRandomPort);

// assume that I have a jot server listening somewhere and I created a connection to it called "connection" 
var someObject = {
  "this property is null": null,
  1928: 3734,
  turtle: {
    neck: "sweater"
  },
  question: "dank"
};
 
connection.write(someObject);
// Whatever is listening to this connection on the server-side will now recieve a "data" event with an object that 
// has the same values as "someObject". 
