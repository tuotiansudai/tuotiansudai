var honeySwitch = {};
var commonFun= require('publicJs/commonFun');
honeySwitch.themeColor = "rgb(100, 189, 99)";
honeySwitch.init = function() {
	var s = "<span class='slider'></span>";
	$("[class^=switch]").append(s);
	$("[class^=switch]").click(function() {
		if ($(this).hasClass("switch-disabled")) {
			return;
		}
		if ($(this).hasClass("switch-on")) {
            $('.shade_mine').show();
            let isOpen = false;
            commonFun.CommonLayerTip({
                btn: ['确认','取消'],
                area:['280px', '160px'],
                content: $('#freeSuccess'),
                shade: false  // hack ios shade
            },() => {
                commonFun.useAjax({
                    type:'POST',
                    url:'/anxinSign/switchSkipAuth',
                    data:{
                        "open":isOpen
                    }
                },function(data) {
                    if(data.success) {
                        layer.closeAll();
                        $('.shade_mine').hide();
                        $('#switch').removeClass("switch-on").addClass("switch-off");
                        $('#switch').css({
                            'border-color' : '#dfdfdf',
                            'box-shadow' : 'rgb(223, 223, 223) 0px 0px 0px 0px inset',
                            'background-color' : 'rgb(255, 255, 255)'
                        });
                    }
                    else {
                        layer.closeAll();
                        $('.shade_mine').hide();
                        layer.msg('请求失败，请重试或联系客服！');
                    }

                });

            },() => {
                $('.shade_mine').hide(); // hack ios shade
            });
		} else {
			let isOpen = true;
            commonFun.useAjax({
                type:'POST',
                url:'/anxinSign/switchSkipAuth',
                data:{
                    "open":isOpen
                }
            },function(data) {
                if(data.success) {
                    layer.msg('已开启安心签免短信服务');
                    $(this).removeClass("switch-off").addClass("switch-on");
                    if (honeySwitch.themeColor) {
                        var c = honeySwitch.themeColor;
                        $('#switch').css({
                            'border-color' : c,
                            'box-shadow' : c + ' 0px 0px 0px 16px inset',
                            'background-color' : c
                        });
                    }
                    if ($('#switch').attr('themeColor')) {
                        var c2 = $('#switch').attr('themeColor');
                        $('#switch').css({
                            'border-color' : c2,
                            'box-shadow' : c2 + ' 0px 0px 0px 16px inset',
                            'background-color' : c2
                        });
                    }
                }
                else {
                    layer.msg('请求失败，请重试或联系客服！');
                }

            });
		}
	});
	window.switchEvent = function(ele, on, off) {
		$(ele).click(function() {
			if ($(this).hasClass("switch-disabled")) {
				return;
			}
			if ($(this).hasClass('switch-on')) {
				if ( typeof on == 'function') {
					on();
				}
			} else {
				if ( typeof off == 'function') {
					off();
				}
			}
		});
	}
	if (this.themeColor) {
		var c = this.themeColor;
		$(".switch-on").css({
			'border-color' : c,
			'box-shadow' : c + ' 0px 0px 0px 16px inset',
			'background-color' : c
		});
		$(".switch-off").css({
			'border-color' : '#dfdfdf',
			'box-shadow' : 'rgb(223, 223, 223) 0px 0px 0px 0px inset',
			'background-color' : 'rgb(255, 255, 255)'
		});
	}
	if ($('[themeColor]').length > 0) {
		$('[themeColor]').each(function() {
			var c = $(this).attr('themeColor') || honeySwitch.themeColor;
			if ($(this).hasClass("switch-on")) {
				$(this).css({
					'border-color' : c,
					'box-shadow' : c + ' 0px 0px 0px 16px inset',
					'background-color' : c
				});
			} else {
				$(".switch-off").css({
					'border-color' : '#dfdfdf',
					'box-shadow' : 'rgb(223, 223, 223) 0px 0px 0px 0px inset',
					'background-color' : 'rgb(255, 255, 255)'
				});
			}
		});
	}
};
honeySwitch.showOn = function(ele) {
	$(ele).removeClass("switch-off").addClass("switch-on");
	if(honeySwitch.themeColor){
		var c = honeySwitch.themeColor;
		$(ele).css({
			'border-color' : c,
			'box-shadow' : c + ' 0px 0px 0px 16px inset',
			'background-color' : c
		});
	}
	if ($(ele).attr('themeColor')) {
		var c2 = $(ele).attr('themeColor');
		$(ele).css({
			'border-color' : c2,
			'box-shadow' : c2 + ' 0px 0px 0px 16px inset',
			'background-color' : c2
		});
	}
}
honeySwitch.showOff = function(ele) {
	$(ele).removeClass("switch-on").addClass("switch-off");
	$(".switch-off").css({
		'border-color' : '#dfdfdf',
		'box-shadow' : 'rgb(223, 223, 223) 0px 0px 0px 0px inset',
		'background-color' : 'rgb(255, 255, 255)'
	});
}
$(function() {
	honeySwitch.init();
}); 