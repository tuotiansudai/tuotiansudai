import React from 'react';
import ajax from 'utils/ajax';

class Praise extends React.Component {
	static defaultProps = {
		id: '',
		likeCount: 0,
		className: ''
	};
	state = {
		likeCount: this.props.likeCount,
		isLike: sessionStorage.getItem(`praise${this.props.id}`)
	};
	componentWillReceiveProps(nextProps) {
		if (nextProps.likeCount !== this.state.likeCount) {
			this.setState({
				likeCount: nextProps.likeCount
			});
		}
		if (nextProps.id !== this.props.id) {
			this.setState({
				isLike: sessionStorage.getItem(`praise${nextProps.id}`)
			});
		}
	}
	tapHandler() {
		if (this.state.isLike) {
			return false;
		}
		ajax({
			url: `/media-center/${this.props.id}/like`,
			done: (response) => {
				this.setState((previousState) => {
					return {
						likeCount: response.data.likeCount,
						isLike: true
					};
				});
				sessionStorage.setItem(`praise${this.props.id}`, 1);
			}
		});
	}
	render() {
		return (
			<div className={this.props.className} onTouchTap={this.tapHandler.bind(this)}>
				<i style={{marginRight: 10, color: this.state.isLike ? 'red' : ''}} className="fa fa-thumbs-o-up active" aria-hidden="true"></i>
				{this.state.likeCount}
			</div>
		)
	}
}

export default Praise;
