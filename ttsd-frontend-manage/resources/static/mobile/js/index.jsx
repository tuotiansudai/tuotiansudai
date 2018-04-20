import 'mobileStyle/index.scss';
import React from "react";
import ReactDom from "react-dom";
import {Route, Router, hashHistory, Redirect, IndexRoute} from "react-router";
import injectTapEventPlugin from "react-tap-event-plugin";
import MediaList from 'mobileJs/page/MediaList';
import KnowledgeList from 'mobileJs/page/KnowledgeList';
import Article from 'mobileJs/page/Article';
import messageCenter from 'mobileJs/page/messageCenter';
import taskCenter from 'mobileJs/page/taskCenter';

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
                <Route path="knowledge-center" component={KnowledgeList} />
                <Route path="media-center/article/:id" component={Article} />
                <Route path="task-center" component={taskCenter} />
                <Route path="message-center/:id" component={messageCenter}/>
                <Redirect from="/" to="media-center" />
            </Router>
        )
    }
}

// document.addEventListener('touchmove', function(event) {
//     event.preventDefault();
// });

ReactDom.render(<App />, document.getElementById('app'));





