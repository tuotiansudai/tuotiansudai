require(['jquery', 'layerWrapper', 'jquery.ajax.extension', 'jquery.validate', 'logintip'], function($, layer) {
	$(function() {
		var $loanTip = $('.loan-tip');

		//show tip
		$loanTip.on('click', function(event) {
			event.preventDefault();
			var _title = $(this).attr('data-title'),
				_holder = $(this).attr('data-holder');
			$.ajax({
				url: '/isLogin',
				type: 'GET',
				dataType: 'json',
				contentType: 'application/json; charset=UTF-8'
			})
			.fail(function(response) {
				if ("" == response.responseText) {
					if ($('#userName').val() != '') {
						layerTip(_title, _holder);
					} else {
						layer.open({
							type: 1,
							btn: 0,
							area: ['400px', 'auto'],
							title: '温馨提示',
							content: $('#isUser')
						});
					}
				} else {
					$("meta[name='_csrf']").remove();
					$('head').append($(response.responseText));
					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");
					$(document).ajaxSend(function(e, xhr, options) {
						xhr.setRequestHeader(header, token);
					});
					layer.open({
						type: 1,
						title: false,
						closeBtn: 0,
						area: ['auto', 'auto'],
						content: $('#loginTip')
					});
					$('.image-captcha img').trigger('click');
				}
			});
		});

		//validate form
		$("#loanForm").validate({
			debug: true,
			rules: {
				moneyText: {
					required: true
				},
				monthText: {
					required: true
				},
				infoText: {
					required: true
				}
			},
			messages: {
				moneyText: {
					required: '请输入借款金额'
				},
				monthText: {
					required: '请输入借款周期'
				},
				infoText: {
					required: '请填写信息'
				}
			},
			submitHandler: function(form) {
				$.ajax({
						url: '/loan-application/create',
						type: 'POST',
						dataType: 'json',
						data: {
							param1: $('#userName').val(),
							param1: $('#userPhone').val(),
							param1: $('#placeText').val(),
							param1: $('#moneyText').val(),
							param1: $('#monthText').val(),
							param1: $('#infoText').val()
						}
					})
					.done(function(data) {
						layer.closeAll();
						layer.open({
							type: 1,
							btn: 0,
							area: ['400px', 'auto'],
							title: '温馨提示',
							content: $('#successTip')
						});
					})
					.fail(function() {
						layer.msg('请求失败，请稍后重试！');
					});

			}
		});

		$('body').on('click', '.area-bg', function(event) {
			event.preventDefault();
			var $self = $(this),
				$areaList = $self.siblings('.area-list-group');
			$areaList.slideToggle('fast');
		}).on('click', '.area-list-group li', function(event) {
			event.preventDefault();
			var $self = $(this),
				text = $self.text(),
				$parent = $self.parent(),
				$areaInt = $('#placeText');
			$('#placeText').val(text);
			$parent.slideUp('fast');
		}).on('click', '.close-btn', function(event) {
			event.preventDefault();
			layer.closeAll();
		});

		//tip code
		function layerTip(title, holder) {
			$('#infoText').attr('placeholder', holder);
			layer.open({
				type: 1,
				btn: 0,
				area: ['500px', '510px'],
				title: [title, 'padding:0;text-align:center'],
				content: $('#homeTip')
			});
		}
	});
});