require(['jquery', 'pagination', 'layerWrapper'], function ($, pagination, layer) {
	$(function() {
		var $filtersList=$('.filters-list li');
		$filtersList.on('click', function(event) {
			event.preventDefault();
			var $self=$(this),
				$recordList=$('.list-container'),
				index=$self.index();
			$self.addClass('active').siblings().removeClass('active');
			$recordList.find('.record-list:eq('+index+')').addClass('active').siblings().removeClass('active');
		});
	});
});
