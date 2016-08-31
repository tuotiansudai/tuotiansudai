require(['jquery', 'layerWrapper','template', 'jquery.validate', 'jquery.ajax.extension'], function($, layer,tpl) {
	$(function() {
		var $countList = $('.order-number'),
			$numText = $countList.find('.num-text'),
			$bigText = $countList.find('.total-num i'),
			$orderBtn = $('#orderBtn'),
			$addPlace = $('#addPlace'),
			$updatePlace = $('#updatePlace'),
			$addressForm = $('#addressForm');

		$countList.on('click', '.low-btn', function(event) { //减号
			event.preventDefault();
			if ($bigText.text() > 0) {
				$numText.val() > 1 ? $numText.val(function(index, num) {
					return parseInt(num) - 1
				}) && changeCount() : $numText.val('1');
			}
		}).on('click', '.add-btn', function(event) { //加号
			event.preventDefault();
			if ($bigText.text() > 0) {
				$numText.val() < parseInt($bigText.text()) ? $numText.val(function(index, num) {
					return parseInt(num) + 1
				}) && changeCount() : $numText.val($bigText.text());
			}
		});
		changeCount();

		function changeCount() {
			$('.count-num').each(function(index, el) {
				$(this).text(parseInt($(this).attr('data-num')) * parseInt($numText.val()));
			});
		}

		$orderBtn.on('click', function(event) { //立即兑换
			event.preventDefault();
			var $self = $(this),
				idString = $self.attr('data-id'),
				typeString = $self.attr('data-type');
			$.ajax({
					url: '/point-shop/order',
					type: 'POST',
					dataType: 'json',
					data: {
						id: idString,
						itemType: typeString,
						number: $numText.val(),
						userAddressId: $('#updatePlace').attr('data-id')
					}
				})
				.done(function(data) {
					console.log(data);
					if (data.data.status) {
						layer.msg('兑换成功！3秒后自动跳转到兑换记录页面。');
						setTimeout(function(){
							location.href = '/point-shop/record';
						},3000)

					} else {
						errorTip(data.data);
					}
				})
				.fail(function(data) {
					layer.msg('请求失败，请重试！');
				});
		});
		//add adress
		$addPlace.on('click', function(event) {
			event.preventDefault();
			$addressForm.attr('data-type', 'add');
			layer.open({
				type: 1,
				title: '添加地址',
				area: ['700px', 'auto'],
				content: $('#fixAdress')
			});
		});

		//update place
		$updatePlace.on('click', function(event) {
			event.preventDefault();
			var $self = $(this),
				user = $self.attr('data-user'),
				phone = $self.attr('data-phone'),
				address = $self.attr('data-address');
			$('#Recipient').val(user),
			$('#Phone').val(phone),
			$('#AddRess').val(address)
			$addressForm.attr('data-type', 'update');
			layer.open({
				type: 1,
				title: '添加地址',
				area: ['700px', 'auto'],
				content: $('#fixAdress')
			});
		});

		// isphone validate
		jQuery.validator.addMethod("isPhone", function(value, element) {
			var tel = /0?(13|14|15|18)[0-9]{9}/;
			return this.optional(element) || (tel.test(value));
		}, "请正确填写您的手机号码");

		//android validate && submit data
		$addressForm.validate({
			debug: true,
			focusInvalid: false,
			rules: {
				Recipient: {
					required: true
				},
				Phone: {
					required: true,
					isPhone: true,
					minlength: 11,
					maxlength: 11
				},
				AddRess: {
					required: true,
					maxlength: 100
				}
			},
			messages: {
				Recipient: {
					required: "请填写收件人"
				},
				Phone: {
					required: '请输入手机号',
					isPhone: '请输入正确的手机号码',
					minlength: '手机格式不正确',
					maxlength: '手机格式不正确'
				},
				AddRess: {
					required: '请填写收件地址',
					maxlength: '收件地址字数限制在100字以内'
				}
			},
			errorPlacement: function(error, element) {
				error.appendTo(element.parent());
			},
			submitHandler: function(form) {
				submitPlace($(form).attr('data-type'));
			}
		});


		$('body').on('click', '.close-layer',function(event) {
			event.preventDefault();
			layer.closeAll();
		});
		//submit place data
		function submitPlace(type) {
			var dataList = {};
			if (type == 'add') {
				dataList = {
					url: '/point-shop/add-address',
					data: {
						realName: $('#Recipient').val(),
						mobile: $('#Phone').val(),
						address: $('#AddRess').val()
					}
				};
			} else if (type == 'update') {
				dataList = {
					url: '/point-shop/update-address',
					data: {
						id: $('#updatePlace').attr('data-id'),
						realName: $('#Recipient').val(),
						mobile: $('#Phone').val(),
						address: $('#AddRess').val()
					}
				};
			}
			$.ajax({
				url: dataList.url,
				type: 'POST',
				dataType: 'json',
				data: dataList.data
			})
			.done(function(data) {
					if (data.data.status) {
						location.reload();
					} else {
						errorTip(data.data);
					}
			})
			.fail(function() {
				layer.msg('请求失败，请重试！');
			});
		}
		//error tip
		function errorTip(msg){
			$('#errorTip').html(tpl('errorTipTpl', msg));
			layer.open({
				type: 1,
				title: false,
				area: ['300px', '180px'],
				content: $('#errorTip')
			});
		}
	});
})