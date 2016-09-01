/**
 * name: [活动中心]
 * time: 2016-09-01
 * Author: xuqiang
 */
require(['jquery'], function($) {
	$(function() {
		var $filterBtn=$('.ac-item span');
		$filterBtn.on('click', function(event) {
			event.preventDefault();
			var $self=$(this),
				index=$self.index(),
				$actorList=$('.activity-container');
			$self.addClass('active').siblings().removeClass('active');
			$actorList.find('.actor-list:eq('+index+')').addClass('active').siblings().removeClass('active');
		});
	});
});