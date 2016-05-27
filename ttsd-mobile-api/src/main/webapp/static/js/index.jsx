import React from 'react';
import ReactDom from 'react-dom';
import {
	Route,
	Router,
	hashHistory,
	Redirect,
	IndexRoute
} from 'react-router';
import changeTitle from 'utils/changeTitle';
import './index.scss';
import injectTapEventPlugin from 'react-tap-event-plugin';
injectTapEventPlugin();

import MediaList from 'components/licaiCircle/MediaList';
import Article from 'components/licaiCircle/Article';

class App extends React.Component {
	render() {
		return (
			<Router history={hashHistory}>
        		<Route path="media-center" component={MediaList} />
        		<Route path="media-center/article/:id" component={Article} />
	        </Router>
		)
	}
}

document.addEventListener('touchmove', function(event) {
	event.preventDefault();
});

ReactDom.render(<App />, document.getElementById('app'));