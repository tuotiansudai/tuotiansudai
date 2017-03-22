// require(['jquery', 'layerWrapper','template', 'jquery.validate', 'jquery.ajax.extension'], function($, layer,tpl) {
// 	$(function() {
		var $pointOrder = $('#pointOrder');
		var $exchangeTip = $('#exchangeTip');
		var $countList = $('.order-number',$pointOrder),
			$numText = $countList.find('.num-text'),
			currentNum=parseInt($numText.val()), //目前已选商品的个数
			$orderBtn = $('#orderBtn'),
			$addPlace = $('#addPlace'),
			$updatePlace = $('#updatePlace'),
			$addressForm = $('#addressForm');

		//该用户总共可以兑换商品的数量
		var myLimit = parseInt($countList.data('mylimit'));
		//该用户本月已兑换的数量
		var buyCount = parseInt($countList.data('buycount'));
		//本月该用户剩下能兑换的数量
		var myRest = myLimit-buyCount;
		$exchangeTip.find('i').text(myRest);
		if(myRest==0) {
			$orderBtn.prop('disabled',true);
		}

		$countList.on('click',function(event) {
			var target = event.target;
			var overplus = parseInt($countList.data('overplus'));  //剩余商品的数量

			var compareNum = (myRest==0) ? overplus :Math.min(overplus,myRest);
			currentNum=parseInt($numText.val());
			if(overplus<1) {
				return;
			}
			//点击减少－
			if(/low-btn/.test(target.className) && currentNum>1) {
				$numText.val(--currentNum);

			} else if(/add-btn/.test(target.className) && currentNum < compareNum) {
				$numText.val(++currentNum);
			}
			changeCount();
		});

		changeCount();

		function changeCount() {
			var totalPoint = parseInt($('.count-num',$pointOrder).data('num')) * currentNum;
			$('.count-num',$pointOrder).text(totalPoint);
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
						goodsType: typeString,
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
// 	});
// })