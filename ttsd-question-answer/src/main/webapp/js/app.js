import React, { Component } from 'react';
import { Link } from 'react-router';
export default class App extends Component {
  render() {
    return (
      <div>
        <header>
          <ul>
            <li><Link to="/app">Dashboard</Link></li>
            <li><Link to="/inbox">Inbox</Link></li>
            <li><Link to="/calendar">Calendar</Link>  Logged in as Jane</li>
          </ul>
          
        </header>
        {this.props.children}
      </div>
    );
  }
}


