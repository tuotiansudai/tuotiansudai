import React from 'react';
import ReactDOM from 'react-dom';
import App from './app';
// const React = require('react');
// const ReactDOM = require('react-dom');


var style = require('../style/sass/main.scss'); 
$('#app').text('lanyan');

var img1 = document.createElement("img");
img1.src = require("./dou.png");
document.body.appendChild(img1);


ReactDOM.render(
  <App/>,
  document.querySelector('#wrapper')
);

