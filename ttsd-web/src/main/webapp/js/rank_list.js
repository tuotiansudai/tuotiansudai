require(['jquery','rotate'], function($,rotate) {
	var bRotateTd = false,
		bRotateCd = false,
		$beanBtn=$('#beanBtn li'),
		$awardBtn=$('#awardBtn li'),
		$pointerTd=$('#pointerTd'),
		$pointerCd=$('#pointerCd'),
		$rotateTd=$('#rotateTd'),
		$rotateCd=$('#rotateCd');
	//change rank list
	$beanBtn.on('click', function(event) {
		var $self=$(this),
			index=$self.index();
		$self.addClass('active').siblings('li').removeClass('active');
		$('#beanCom').find('.leader-list:eq('+index+')').addClass('active')
			.siblings('.leader-list').removeClass('active');
	});

	//change rank list
	$awardBtn.on('click', function(event) {
		var $self=$(this),
			index=$self.index();
		$self.addClass('active').siblings('li').removeClass('active');
		$('#awardCom').find('.leader-list:eq('+index+')').addClass('active')
			.siblings('.leader-list').removeClass('active');
	});

	$pointerTd.on('click', function(event) {
		event.preventDefault();
		if(bRotateTd)return;
        var item = rnd(0,7);

        switch (item) {
            case 0:
                //var angle = [26, 88, 137, 185, 235, 287, 337];
                rotateFnTd(0, 337, '未中奖');
                break;
            case 1:
                //var angle = [88, 137, 185, 235, 287];
                rotateFnTd(1, 26, '免单4999元');
                break;
            case 2:
                //var angle = [137, 185, 235, 287];
                rotateFnTd(2, 88, '免单50元');
                break;
            case 3:
                //var angle = [137, 185, 235, 287];
                rotateFnTd(3, 137, '免单10元');
                break;
            case 4:
                //var angle = [185, 235, 287];
                rotateFnTd(4, 185, '免单5元');
                break;
            case 5:
                //var angle = [185, 235, 287];
                rotateFnTd(5, 185, '免单5元');
                break;
            case 6:
                //var angle = [235, 287];
                rotateFnTd(6, 235, '免分期服务费');
                break;
            case 7:
                //var angle = [287];
                rotateFnTd(7, 287, '提高白条额度');
                break;
        }
	});
	$pointerCd.on('click', function(event) {
		event.preventDefault();
		if(bRotateCd)return;
        var item = rnd(0,7);

        switch (item) {
            case 0:
                //var angle = [26, 88, 137, 185, 235, 287, 337];
                rotateFnCd(0, 337, '未中奖');
                break;
            case 1:
                //var angle = [88, 137, 185, 235, 287];
                rotateFnCd(1, 26, '免单4999元');
                break;
            case 2:
                //var angle = [137, 185, 235, 287];
                rotateFnCd(2, 88, '免单50元');
                break;
            case 3:
                //var angle = [137, 185, 235, 287];
                rotateFnCd(3, 137, '免单10元');
                break;
            case 4:
                //var angle = [185, 235, 287];
                rotateFnCd(4, 185, '免单5元');
                break;
            case 5:
                //var angle = [185, 235, 287];
                rotateFnCd(5, 185, '免单5元');
                break;
            case 6:
                //var angle = [235, 287];
                rotateFnCd(6, 235, '免分期服务费');
                break;
            case 7:
                //var angle = [287];
                rotateFnCd(7, 287, '提高白条额度');
                break;
            case 8:
                //var angle = [287];
                rotateFnCd(8, 287, '提高白条额度');
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
                alert(txt);
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
                alert(txt);
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