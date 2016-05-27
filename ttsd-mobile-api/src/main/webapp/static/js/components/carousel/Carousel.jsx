import React from 'react';
import swiper from 'swiper';
import imagesLoaded from 'imagesLoaded';
import { main } from './Carousel.scss';
import { hashHistory } from 'react-router';

class Carousel extends React.Component {
	static defaultProps = {
		data: []
	};
	destroySwiper() {
		if (this.swiper) {
			this.swiper.destroy();
			this.swiper = null;
		}
	}
	goTo(event) {
		let href = event.target.dataset.href;
		hashHistory.push(href);
	}
	componentDidMount() {
		imagesLoaded(this.refs.scrollWrap).on('always', () => {
			this.destroySwiper.call(this);
			this.swiper = new Swiper(this.refs.scrollWrap, {
		       pagination: '.swiper-pagination',
		       paginationClickable: true
		   	});
		});
	}
	componentWillUnmount() {
		this.destroySwiper.call(this);
	}
	render() {
		return (
		    <div className={main}>
				<section ref="scrollWrap" className="swiper-container">
					<ul className="swiper-wrapper">
						{this.props.data.map((value, index) => {
							return (
								<li key={index} className="swiper-slide">
									<a href="javascript:" onTouchTap={this.goTo.bind(this)} data-href={value.href}>
										<img src={value.img} data-href={value.href} />
									</a>
								</li>
							)
						})}
					</ul>
					<div className="swiper-pagination"></div>
				</section>
			</div>
		);
	}
}

export default Carousel;
