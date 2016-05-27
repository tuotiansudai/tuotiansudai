import React from 'react';
import ajax from 'utils/ajax';

class Praise extends React.Component {
	static defaultProps = {
		likeCount: 0,
		className: ''
	};
	state = {
		likeCount: this.props.likeCount,
		isLike: sessionStorage.getItem('praise')
	};
	componentWillMount() {
		this.setState({
			likeCount: this.props.likeCount
		});
	}
	componentWillReceiveProps(nextProps) {
		if (nextProps.likeCount !== this.state.likeCount) {
			this.setState({
				likeCount: nextProps.likeCount
			});
		}
	}
	tapHandler() {
		if (this.state.isLike) {
			return false;
		}
		this.setState((previousState) => {
			return {
				likeCount: previousState.likeCount + 1,
				isLike: true
			};
		});
		sessionStorage.setItem('praise', 1);
	}
	render() {
		return <div className={this.props.className} onTouchTap={this.tapHandler.bind(this)}>
					<i style={{marginRight: 10, color: this.state.isLike ? 'red' : ''}} className="fa fa-thumbs-o-up active" aria-hidden="true"></i>
					{this.state.likeCount}
				</div>;
	}
}

export default Praise;
