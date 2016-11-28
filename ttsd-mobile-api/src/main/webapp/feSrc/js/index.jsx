import React from 'react';
import ReactDom from 'react-dom';
import {
	Route,
	Router,
	hashHistory,
	Redirect,
	IndexRoute
} from 'react-router';
import 'components/lib-flexible/lib-flexible';
import changeTitle from 'utils/changeTitle';
import './index.scss';
import injectTapEventPlugin from 'react-tap-event-plugin';
injectTapEventPlugin({
shouldRejectClick: function (lastTouchEventTimestamp, clickEventTimestamp) {
    return true;
 	}
});

import MediaList from 'components/licaiCircle/MediaList';
import Article from 'components/licaiCircle/Article';
import messageCenter from 'components/messageCenter/messageCenter';
import taskCenter from 'components/taskCenter/taskCenter';

class App extends React.Component {
	render() {
		return (
			<Router history={hashHistory}>
        		<Route path="media-center" component={MediaList} />
        		<Route path="task-center" component={taskCenter} />
        		<Route path="media-center/article/:id" component={Article} />
        		<Route path="message-center" component={messageCenter} />
        		<Redirect from="/" to="media-center" />
	        </Router>
		)
	}
}

document.addEventListener('touchmove', function(event) {
	event.preventDefault();
});

ReactDom.render(<App />, document.getElementById('app'));