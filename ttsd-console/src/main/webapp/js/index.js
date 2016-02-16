require(['jquery'], function ($) {
	$(function() {
		$('.table .btn-danger').on('click',function(event) {
			if (!confirm("确认要拒绝吗?")) {
				return;
			}
			event.preventDefault();
			var $self=$(this);
			$.ajax({
				url: '/refuse?taskId='+$(this).attr('data-taskId'),
				type: 'GET',
				dataType: 'json',
				data: {}
			})
			.done(function() {
				window.location="/";
			})
			.fail(function() {
				alert("出错了！");
			});
		});

		$('.table .btn-info').on('click',function(event) {
			event.preventDefault();
			var $self=$(this);
			$.ajax({
				url: '/deleteNotify?taskId='+$(this).attr('data-taskId'),
				type: 'GET',
				dataType: 'json',
				data: {}
			})
            .done(function() {
                window.location="/";
            })
            .fail(function() {
                alert("出错了！");
            });
		});

	});
});
