require('mobileJsModule/lib-flexible');
require('mobileStyle/base.scss');
let MediaList = require('mobileJs/page/MediaList');
let Article = require('mobileJs/page/Article');
let messageCenter =require('mobileJs/page/messageCenter');
let taskCenter =require('mobileJs/page/taskCenter');

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





