require(['jquery'], function ($) {
	$(function() {
		$('.table .btn-danger').on('click',function(event) {
			event.preventDefault();
			var $self=$(this),
					$parent=$self.parents('tr');
			$.ajax({
				url: '/path/to/file',
				type: 'POST',
				dataType: 'json',
				data: {param1: 'value1'},
			})
			.done(function() {
				$parent.fadeOut('fast').remove();
			})
			.fail(function() {
			});
		});
	});
});
