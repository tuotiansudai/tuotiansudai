/**
 * [point store index js]
 * @xuqiang  
 * @2016-07-11
 */
require(['jquery','layerWrapper','template', 'jquery.ajax.extension'], function ($,layer,tpl) {
	$(function() {
		var $signBtn = $('#signBtn'),
			$signTip = $('#signLayer'),
			$closeSign = $('#closeSign'),
			$materialList=$('.material-list li'),
			$pointRuleTip=$('.point-rule-tip');
		//show sign tip
		$signBtn.on('click', function(event) {
			event.preventDefault();
			var _this = $(this),
				$signText = $(".sign-text"),
				$tomorrowText = $(".tomorrow-text"),
				$addDou = $(".add-dou"),
				$signBtn = $("#signBtn");

			$.ajax({
				url: _this.attr('data-url'),
				type: 'POST',
				dataType: 'json',
				contentType: 'application/json; charset=UTF-8'
			}).done(function(response) {
				if (response.data.status) {
					$signText.html("签到成功，领取" + response.data.signInPoint + "积分！");
					$tomorrowText.html("明日可领" + response.data.nextSignInPoint + "积分");
					$signBtn.addClass("no-click").html("已签到");
					$addDou.html("+" + response.data.signInPoint);
					$signTip.fadeIn('fast', function() {
						$(this).find('.add-dou').animate({
							'bottom': '50px',
							'opacity': '0'
						}, 800);
					});
				}else{
					$('#errorTip').html(tpl('errorTipTpl', response.data));
					layer.open({
						type: 1,
						title: false,
						area: ['300px', '180px'],
						content: $('#errorTip')
					});
				}
			})
		});
		//hide sign tip
		$closeSign.on('click', function(event) {
			event.preventDefault();
			location.href = "/point-shop";
		});
		//go to detail
		$materialList.on('click', function(event) {//click all model
			event.preventDefault();
			location.href = $(this).attr('data-href');
		}).on('click','.get-btn', function(event) {//click btn
			event.preventDefault();
			$(this).hasClass('active')?location.href = $(this).parent('a').attr('href'):false;
			event.stopPropagation();
		});
		//show rule tip
		$pointRuleTip.on('click', function(event) {
			event.preventDefault();
			layer.open({
				type: 1,
				title: false,
				closeBtn:0,
				area: ['auto', '520px'],
                scrollbar: true,
				content: $('#ruleInfoTip')
			});
		});
		//close rule tip
		$('#ruleInfoTip').on('click', '.close-btn', function(event) {
			event.preventDefault();
			layer.closeAll();
		});
	});
})