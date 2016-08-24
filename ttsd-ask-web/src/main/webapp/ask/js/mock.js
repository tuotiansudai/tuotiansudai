var express = require('express');
var app = express();

// app.use(function(req, res, next) {
// 	res.set({
// 		'Access-Control-Allow-Origin': '*',
// 		"Access-Control-Allow-Methods": "GET,POST,PUT,DELETE,OPTIONS",
// 		"Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept"
// 	});
// 	next();
// });


app.listen(8889, function() {
	console.log('mock server listening on port 8889!');
});