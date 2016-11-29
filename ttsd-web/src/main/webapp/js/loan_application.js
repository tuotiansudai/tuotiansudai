require(['jquery', 'layerWrapper', 'jquery.ajax.extension', 'jquery.validate', 'logintip'], function($, layer) {
	$(function() {
		var $loanTip = $('.loan-tip');

		//show tip
		$loanTip.on('click', function(event) {
			event.preventDefault();
			var _title = $(this).attr('data-title'),
				_holder = $(this).attr('data-holder'),
				_type = $(this).attr('data-type');
			$.ajax({
				url: '/isLogin',
				type: 'GET',
				dataType: 'json',
				contentType: 'application/json; charset=UTF-8'
			})
			.fail(function(response) {
				if ("" == response.responseText) {
					if ($('#userName').val() != '') {
						layerTip(_title, _holder, _type);
					} else {
						layer.open({
							type: 1,
							btn: 0,
                            area: ['auto', 'auto'],
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
					required: true,
					digits: true,
					min: 1,
					max: 100000000
				},
				monthText: {
					required: true,
					digits: true,
					min: 1,
					max: 1000
				},
				infoText: {
					required: true,
					maxlength: 200
				}
			},
			messages: {
				moneyText: {
					required: '请填写借款金额',
					digits: '请输入不小于1的整数',
					min: '请输入不小于1的整数',
					max: '请输入不大于1亿的整数'
				},
				monthText: {
					required: '请填写借款周期',
					digits: '请输入不小于1的整数',
					min: '请输入不小于1的整数',
					max: '请输入不大于1千的整数'
				},
				infoText: {
					required: '请填写信息',
					maxlength: '字数限制200字以内'
				}
			},
			submitHandler: function(form) {
				var _data = {
					loginName: null,
					region: $('#placeText').attr("data-value"),
					amount: $('#moneyText').val(),
					period: $('#monthText').val(),
					pledgeInfo: $('#infoText').val(),
					pledgeType: $('#pledgeType').val()
				};
				$.ajax({
						url: '/loan-application/create',
						type: 'POST',
						dataType: 'json',
					data: JSON.stringify(_data),
					contentType: 'application/json; charset=UTF-8'
					})
					.done(function(data) {
						if (data.data.status) {
							layer.closeAll();
							layer.open({
								type: 1,
								btn: 0,
                                area: ['auto', 'auto'],
								title: '温馨提示',
								content: $('#successTip'),
								cancel: function (index) {
									$('#loanForm').find('.input-box').val('');
								}
							});
						}

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
				value = $self.attr('data-value'),
				$parent = $self.parent(),
				$areaInt = $('#placeText');
			$('#placeText').val(text).attr('data-value', value);
			$parent.slideUp('fast');
		}).on('click', '.close-btn', function(event) {
			event.preventDefault();
			layer.closeAll();
			$('#loanForm').find('.input-box').val('');
		});

		//tip code
		function layerTip(title, holder, type) {
			$('#pledgeType').val(type);
			$('#infoText').attr('placeholder', holder);
			layer.open({
				type: 1,
				btn: 0,
                area: ['auto', 'auto'],
				title: [title, 'padding:0;text-align:center'],
				content: $('#homeTip'),
				cancel: function () {
					$('#loanForm').find('label.error').hide();
					$('#placeText').val('北京');
				}
			});
		}
	});
});