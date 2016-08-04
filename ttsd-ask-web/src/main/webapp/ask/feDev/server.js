var path = require('path');
var express = require('express');
var fs = require('fs');
var _ = require('underscore');

var app = express();

var Freemarker = require('freemarker.js');
var fm = new Freemarker({
	viewRoot: path.join(__dirname, '../templates'),
	options: {
		/** for fmpp */
	}
});
debugger
var getData = function(fileName) {
	return _.compose(JSON.parse, fs.readFileSync, path.join)(__dirname, 'data/' + fileName + '.json');
};


var config = [
	{
		route: '/',
		template: 'footer',
		data: 'question'
	},
	{
		route: '/question',
		template: 'question',
		data: 'questions'
	},
	{
		route: '/answers',
		template: 'to-answer',
		data: 'questions'
	},
	{
		route: '/askQuestion',
		template: 'ask-question',
		data: 'questions'
	},
	{
		route: '/category',
		template: 'category',
		data: 'questions'
	},
	{
		route: '/qaAnswer',
		template: 'my-qa-answer',
		data: 'questions'
	},
	{
		route: '/questionDetail',
		template: 'question-detail',
		data: 'questions'
	}];


config.forEach(function(val) {
	app.get(val.route, function(req, res) {
		fm.render(val.template + '.ftl', getData(val.data), function(err, html, output) {
			if (err) {
				console.error(err);
				res.send(err);
				return false;
			}
			res.send(html);
		});
	});
});

app.listen(8087, function() {
	console.log('ttsd-ask frontend develop server listening on port 8087!');
});