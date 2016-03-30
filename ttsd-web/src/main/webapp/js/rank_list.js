require(['jquery','rotate','template'], function($,rotate,tpl) {
	var bRotateTd = false,
		bRotateCd = false,
		$beanBtn=$('#beanBtn li'),
		$awardBtn=$('#awardBtn li'),
		$pointerTd=$('#pointerTd'),
		$pointerCd=$('#pointerCd'),
		$rotateTd=$('#rotateTd'),
		$rotateCd=$('#rotateCd'),
		$giftRecord=$('.gift-record li');
	//change rank list
	$beanBtn.on('click', function(event) {
		var $self=$(this),
			index=$self.index();
		$self.addClass('active').siblings('li').removeClass('active');
		$('#beanCom').find('.leader-list:eq('+index+')').addClass('active')
			.siblings('.leader-list').removeClass('active');
	});

	//change award list
	$awardBtn.on('click', function(event) {
		var $self=$(this),
			index=$self.index();
		$self.addClass('active').siblings('li').removeClass('active');
		$('#awardCom').find('.leader-list:eq('+index+')').addClass('active')
			.siblings('.leader-list').removeClass('active');
	});

	//change award record btn
	$giftRecord.on('click', function(event) {
		var $self=$(this),
			index=$self.index();
		$self.addClass('active').siblings('li').removeClass('active');
		$('#recordList').find('.record-model:eq('+index+')').addClass('active')
			.siblings('.record-model').removeClass('active');
	});
	//td click
	$pointerTd.on('click', function(event) {
		event.preventDefault();
		if(bRotateTd)return;
        var item = rnd(0,4);

        switch (item) {
            case 0:
                rotateFnTd(0, 56, '20元现金');
                break;
            case 1:
                rotateFnTd(1, 120, 'iPhone 6s Plus');
                break;
            case 2:
                rotateFnTd(2, 200, '300元京东购物卡');
                break;
            case 3:
                rotateFnTd(3, 260, '0.5%加息券');
                break;
            case 4:
                rotateFnTd(4, 337, 'MacBook Air');
                break;
        }
	});
	//cd click
	$pointerCd.on('click', function(event) {
		event.preventDefault();
		if(bRotateCd)return;
        var item = rnd(0,7);

        switch (item) {
            case 0:
                rotateFnCd(0, 26, '港澳游');
                break;
            case 1:
                rotateFnCd(1, 80, '现金5元');
                break;
            case 2:
                rotateFnCd(2, 120, 'iPhone 6sp');
                break;
            case 3:
                rotateFnCd(3, 173, '谢谢参与');
                break;
            case 4:
                rotateFnCd(4, 210, '现金2元');
                break;
            case 5:
                rotateFnCd(5, 255, '0.2%加息券');
                break;
            case 6:
                rotateFnCd(6, 325, '日韩游');
                break;
            case 7:
                rotateFnCd(7, 355, '3000元体验金');
                break;
        }
	});

	function rnd(n, m){
        return Math.floor(Math.random()*(m-n+1)+n)
    }
    function rotateFnTd(awards, angles, txt){
        bRotateTd = !bRotateTd;
        $('#rotateTd').stopRotate();
        $('#rotateTd').rotate({
            angle:0,
            animateTo:angles+1800,
            duration:8000,
            callback:function (){
                console.log(txt);
                bRotateTd = !bRotateTd;
            }
        })
    };
    function rotateFnCd(awards, angles, txt){
        bRotateCd = !bRotateCd;
        $('#rotateCd').stopRotate();
        $('#rotateCd').rotate({
            angle:0,
            animateTo:angles+1800,
            duration:8000,
            callback:function (){
                console.log(txt);
                bRotateCd = !bRotateCd;
            }
        })
    };



	//share event
	window._bd_share_config = {
		"common": {
			"bdSnsKey": {},
			"bdText": "",
			"bdMini": "2",
			"bdPic": "",
			"bdStyle": "0",
			"bdSize": "16",
			onAfterClick:function(cmd){
				console.log("我被分享了！");
			}
		},
		"share": {}
	};
	with(document) 0[(getElementsByTagName('head')[0] || body).appendChild(createElement('script')).src = 'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion=' + ~(-new Date() / 36e5)];


});