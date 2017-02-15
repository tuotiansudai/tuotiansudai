/**
 * [point store index js]
 * @xuqiang  
 * @2017-02-13
 */
require(['jquery', 'layerWrapper', 'template', 'drawCircle', 'jquery.ajax.extension'], function($,layer, tpl, drawCircle) {
	$(function() {
		var $signBtn = $('#signBtn'),
			$signTip = $('#signLayer'),
			$closeSign = $('#closeSign'),
			$materialList = $('.material-list li'),
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

			$.ajax({
				url: _this.attr('data-url'),
				type: 'POST',
				dataType: 'json',
				contentType: 'application/json; charset=UTF-8'
			}).done(function(response) {
				if (response.data.status) {
					response.data.signIn == true ? $signText.html("您今天已签到") : $signText.html("签到成功");
					$tomorrowText.html("明日签到可获得" + response.data.nextSignInPoint + "积分");
					$introText.html(response.data.currentRewardDesc);
					$nextText.html(response.data.nextRewardDesc);
					$signBtn.addClass("no-click").html("已签到");
					$signPoint.find('span').html(response.data.signInPoint);
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
			})
		});
		//hide sign tip
		$closeSign.on('click', function(event) {
			event.preventDefault();
			location.href = "/point-shop";
		});
		//go to detail
		$materialList.on('click', function(event) { //click all model
			event.preventDefault();
			location.href = $(this).attr('data-href');
		}).on('click', '.get-btn', function(event) { //click btn
			event.preventDefault();
			$(this).hasClass('active') ? location.href = $(this).parent('a').attr('href') : false;
			event.stopPropagation();
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
	});
    (function(drawCircle) {
        var $pointContainerFrame = $('#pointContainer');
        var tipGroupObj = {};
        //抽奖模块
        var $rewardGiftBox = $('.nine-lottery-group', $pointContainerFrame);

        var pointAllList = '/activity/point-draw/all-list', //中奖记录接口地址
            pointUserList = '/activity/point-draw/user-list', //我的奖品接口地址
            drawURL = '/activity/point-draw/draw', //抽奖的接口链接
            $pointerImg = $('.lottery-btn', $rewardGiftBox);

        $pointContainerFrame.find('.tip-list-frame .tip-list').each(function(key, option) {
            var kind = $(option).data('return');
            tipGroupObj[kind] = option;
        });

        var paramData = {
            "activityCategory": "POINT_SHOP_DRAW_1000"
        };
        var drawCircle = new drawCircle(pointAllList, pointUserList, drawURL, paramData, $rewardGiftBox);

        //渲染中奖记录
        drawCircle.GiftRecord();

        //渲染我的奖品
        drawCircle.MyGift();

        //**********************开始抽奖**********************//
        $pointerImg.on('click', function() {
            drawCircle.beginLuckDraw(function(data) {
            	var prizeKind;

            	if (data.returnCode == 0) {

            		switch (data.prize) {
            			case 'POINT_SHOP_INTEREST_COUPON_2': //0.2加息券
            				prizeKind = 7;
            				break;
            			case 'POINT_SHOP_RED_ENVELOPE_10': //10元投资红包
            				prizeKind = 0;
            				break;
            			case 'POINT_SHOP_POINT_500': //500积分
            				prizeKind = 1;
            				break;
            			case 'POINT_SHOP_PHONE_CHARGE_10': //10元话费
            				prizeKind = 5;
            				break;
            			case 'OINT_SHOP_JD_100': //100元京东卡
            				prizeKind = 2;
            				break;
            			case 'POINT_SHOP_POINT_3000': //3000积分
            				prizeKind = 6;
            				break;
            			case 'POINT_SHOP_INTEREST_COUPON_5': //0.5加息券
            				prizeKind = 3;
            				break;
            			case 'RED_ENVELOPE_50_POINT_DRAW_REF_CARNIVAL': //50元投资红包
            				prizeKind = 4;
            				break;
            		}

            		var prizeType = data.prizeType.toLowerCase();
            		$(tipGroupObj[prizeType]).find('.prizeValue').text(data.prizeValue);

            		drawCircle.lotteryRoll({
            			elementId: 'lotteryBox',
            			speed: 100,
            			prize: prizeKind
            		}, tipGroupObj[prizeType]);

            	} else if (data.returnCode == 1) {
            		//没有抽奖机会
            		drawCircle.tipWindowPop(tipGroupObj['nochance']);
            	} else if (data.returnCode == 2) {
            		//未登录
            		location.href='/login';

            	} else if (data.returnCode == 3) {
            		//不在活动时间范围内！
            		drawCircle.tipWindowPop(tipGroupObj['expired']);

            	} else if (data.returnCode == 4) {
            		//实名认证
            		drawCircle.tipWindowPop(tipGroupObj['authentication']);
            	}
            });
        });

        //点击切换按钮
        var menuCls = $rewardGiftBox.find('.lottery-right-group').find('h3 span');
        menuCls.on('click', function() {
            var $this = $(this),
                index = $this.index(),
                contentCls = $rewardGiftBox.find('.record-item');
            $this.addClass('active').siblings().removeClass('active');
            contentCls.eq(index).show().siblings().hide();
        });

    })(drawCircle);
})