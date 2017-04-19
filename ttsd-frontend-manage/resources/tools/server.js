var path = require('path');
var express = require('express');
var fs = require('fs');
var _ = require('underscore');

var app = express();

var Freemarker = require('freemarker.js');

// web template
var webTemplatePath='../ttsd-web/src/main/webapp/templates';
var fm = new Freemarker({
    viewRoot: webTemplatePath,
    options: {
        /** for fmpp */
    }
});

var getData = function (fileName) {
    return _.compose(_.partial(_.extend, {
        staticServer: 'http://localhost:3088'
    }), JSON.parse, fs.readFileSync, path.join)(__dirname, 'data/' + fileName + '.json');
};

var config = [
    {
        route: '/wechart/start',
        template: 'weChat/start',
        data: 'start'
    },
    {
        route: '/wechart/login',
        template: 'weChat/login',
        data: 'login'
    },
    {
        route: '/wechart/register',
        template: 'weChat/register',
        data: 'register'
    }];


config.forEach(function (val) {

    app.get(val.route, function (req, res) {
        console.log('test1:##########'+val.route);
        fm.render(val.template + '.ftl', getData(val.data), function (err, html, output) {
            console.log(html);
            if (err) {
                console.error(err);
                res.send(err);
                return false;
            }
            res.send(html);
        });
    });

});

app.listen(8087, function () {
    console.log('ttsd-web frontend develop server listening on port 8087!');
});