require('pointStyle/point_index.scss');
require('pointJsModule/nine_lottery');
let commonFun= require('publicJs/commonFun');
let tpl = require('art-template/dist/template');


		var $pointContainer = $('#pointContainer');
		var $signBtn = $('#signBtn'),
			$signTip = $('#signLayer'),
			$closeSign = $('#closeSign'),
			$materialList = $('.material-list li',$pointContainer),
			$pointRuleTip = $('.point-rule-tip');

		//show sign tip
		$signBtn.on('click', function(event) {
			event.preventDefault();
			var _this = $(this),
				$signText = $(".sign-text"),
				$tomorrowText = $(".tomorrow-text"),
				$signPoint = $(".sign-point"),
				$introText = $('.intro-text'),
				$nextText = $('.next-text'),
				$signBtn = $("#signBtn");

            commonFun.useAjax({
                url: _this.attr('data-url'),
                type: 'POST'
            },function(response) {
                if (response.data.status) {
                    var signInStatus = response.data.signIn ? '您今天已签到' : '签到成功';
                    $signText.html(signInStatus);

                    $tomorrowText.html("明日签到可获得" + response.data.nextSignInPoint + "积分");

                    if(response.data.full){
                        $introText.html('已连续签到365天，获得全勤奖！');
                        $nextText.html('365元投资红包');
                    }
                    else{
                        $introText.html(response.data.currentRewardDesc);
                        $nextText.html(response.data.nextRewardDesc);
                    }
                    $signBtn.addClass("no-click").html("已签到");
                    $signPoint.find('span').html('+'+response.data.signInPoint);
                    $signTip.fadeIn('fast');
                } else {
                    $('#errorTip').html(tpl('errorTipTpl', response.data));
                    layer.open({
                        type: 1,
                        title: false,
                        area: ['300px', '180px'],
                        content: $('#errorTip')
                    });
                }
            });
		});
		//hide sign tip
		$closeSign.on('click', function(event) {
			event.preventDefault();
			location.href = "/point-shop";
		});

		//查看详情 与立即兑换按钮
		$materialList.on('click', function(event) {
			event.stopPropagation();
			var eventCls = event.target.className;
			if(/active/.test(eventCls)) {
				return;
			}
			location.href = $(this).data('href');
		});

		//show rule tip
		$pointRuleTip.on('click', function(event) {
			event.preventDefault();
			layer.open({
				type: 1,
				title: false,
				closeBtn: 0,
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
