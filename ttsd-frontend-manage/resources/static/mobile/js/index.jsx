// import React from "react";
// import ReactDom from "react-dom";
// import {Route, Router, hashHistory, Redirect, IndexRoute} from "react-router";
import "./components/lib-flexible";
import "mobileStyle/base.scss";
// import injectTapEventPlugin from "react-tap-event-plugin";
import MediaList from "./MediaList";
import Article from "./Article";
import messageCenter from "./messageCenter";
import taskCenter from "./taskCenter";
injectTapEventPlugin({
shouldRejectClick: function (lastTouchEventTimestamp, clickEventTimestamp) {
    return true;
 	}
});

class App extends React.Component {
	render() {
		return (
			<Router history={hashHistory}>
        		<Route path="media-center" component={MediaList} />
        		<Route path="task-center" component={taskCenter} />
        		<Route path="media-center/article/:id" component={Article} />
                <Route path="message-center/:id" component={messageCenter}/>
        		<Redirect from="/" to="media-center" />
	        </Router>
		)
	}
}

document.addEventListener('touchmove', function(event) {
	event.preventDefault();
});

ReactDom.render(<App />, document.getElementById('app'));