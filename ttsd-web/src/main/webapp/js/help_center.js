require(['jquery'], function($) {
	$(function() {
		var $changeBtn = $('.problem-title-item span'),
			$contentList = $('.problem-content-item'),
			$problem = $('.problem-single-item');


		//切换类型
		$changeBtn.on('click', function(event) {
			event.preventDefault();
			var $self=$(this),
				index=$self.index();
			$self.addClass('active').siblings().removeClass('active');
			$contentList.find('.list-group:eq('+index+')').addClass('active')
				.siblings('.list-group').removeClass('active');
		});

		$problem.on('click', '.single-title', function(event) {
			event.preventDefault();
			var $self=$(this);
			$self.siblings('.single-answer').slideToggle('fast',function(){
				$self.parent().hasClass('active')?$self.parent().removeClass('active').find('.fa').removeClass('fa-angle-up').addClass('fa-angle-down'):$self.parent().addClass('active').find('.fa').removeClass('fa-angle-down').addClass('fa-angle-up');
			});
		}).on('click', '.fa', function(event) {
			event.preventDefault();
			$(this).siblings('.single-title').trigger('click')
		});
	});
});