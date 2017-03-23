import React, {Component, PropTypes} from 'react';
import { Router, Route, Redirect, IndexRoute, browserHistory, hashHistory } from 'react-router';
// import MediaList from 'mobileJs/page/MediaList';
const history = process.env.NODE_ENV !== 'production' ? browserHistory : hashHistory;


class Roots extends Component {
    render() {
        return (
            <div>{this.props.children}</div>
        );
    }
}

const MediaList = (location, cb) => {
    require.ensure([], require => {
        cb(null, require('mobileJs/page/MediaList').default)
    },'MediaList')
}


const Article = (location, cb) => {
    require.ensure([], require => {
        cb(null, require('mobileJs/page/Article').default)
    },'Article')
}

const messageCenter = (location, cb) => {
    require.ensure([], require => {
        cb(null, require('mobileJs/page/messageCenter').default)
    },'messageCenter')
}

const taskCenter = (location, cb) => {
    require.ensure([], require => {
        cb(null, require('mobileJs/page/taskCenter').default)
    },'taskCenter')
}

const RouteConfig = (
    <Router history={history}>
        <Route path="/" component={Roots}>
            <Route path="media-center" component={MediaList} />
            <Route path="task-center" component={taskCenter} />
            <Route path="media-center/article/:id" component={Article} />
            <Route path="message-center/:id" component={messageCenter}/>
            <Redirect from="/" to="media-center" />
        </Route>
    </Router>
);

export default RouteConfig;