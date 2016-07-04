import React from 'react';
import { render } from 'react-dom';
import { Router, Route, Link, IndexRoute, browserHistory } from 'react-router';
import App from './app';

var style = require('../style/sass/main.scss'); 


// var img1 = document.createElement("img");
// img1.src = require("./dou.png");
// document.body.appendChild(img1);

var Dashboard = React.createClass({
  render: function () {
    return (
      <div>
        <p>hello Dashboard</p>
      </div>
    );
  }
});

var Inbox = React.createClass({
  render: function () {
    return (
      <div>
        <p>hello Inbox</p>
      </div>
    );
  }
});

var Calendar = React.createClass({
  render: function () {
    return (
      <div>
        <p>hello Calendar</p>
      </div>
    );
  }
});


render((
  <Router history={browserHistory}>
    <Route path="/" component={App}>
      <IndexRoute component={Dashboard}/>
      <Route path="app" component={Dashboard}/>
      <Route path="inbox" component={Inbox}/>
      <Route path="calendar" component={Calendar}/>
      <Route path="*" component={Dashboard}/>
    </Route>
  </Router>
), document.querySelector('#wrapper'));



