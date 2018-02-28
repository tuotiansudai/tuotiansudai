require("activityStyle/newman_tyrant.scss");
require('publicJs/login_tip');
let commonFun= require('publicJs/commonFun');
let tpl = require('art-template/dist/template');


var $newmanTyrant=$('#newmanTyrant'),
	$boardTab=$('.board-tab span',$newmanTyrant),
	$infoDate=$('#infoDate'),
	todayDate=$.trim($infoDate.attr('data-date')).replace(/-/gi,''),
	startTime=$.trim($infoDate.attr('data-startTime').substr(0,10)).replace(/-/gi,''),
	endTime=$.trim($infoDate.attr('data-endTime').substr(0,10)).replace(/-/gi,''),
	$boardBtn=$('.board-btn span',$newmanTyrant),
	$heroNext=$('#heroNext'),
	$heroPre=$('#heroPre'),
	$getHistory=$('#getHistory'),
	$showLogin=$('.show-login',$newmanTyrant);
	
if(todayDate==startTime) {
    $heroPre.hide();
    $heroNext.hide();
}else if(todayDate==endTime){
    $heroNext.hide();
}else if(todayDate>startTime && todayDate<endTime){
    $heroPre.show();
    $heroNext.hide();
}else if(todayDate>endTime){
	$heroPre.show();
    $heroNext.hide();
}else{
    $heroPre.hide();
    $heroNext.hide();
}

//切换排行榜
$boardTab.on('click', function(event) {
	event.preventDefault();
	var $self=$(this),
		type=$self.attr('data-type'),
		date=$.trim($infoDate.text());
	$self.addClass('active').siblings().removeClass('active');
	getBoard(type,date);
});

//获取排行榜数据
function getBoard(type,date){
	commonFun.useAjax({
        type:'GET',
        dataType: 'json',
        url:'/activity/newman-tyrant/'+type+'/'+date
    },function(data) {
        if(data.status) {
			$('#rankTable').append(tpl('rankTableTpl',data));
		}
    });
}
//获取前一天或者后一天的日期
function GetDateStr(date,AddDayCount) {
	var dd = new Date(date);
	dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
	var y = dd.getFullYear();
	var m = dd.getMonth()+1;//获取当前月份的日期
	var d = dd.getDate();

	return y + "-" + (m < 10 ? ('0' + m) : m) + "-" + (d < 10 ? ('0' + d) : d);
}
//查看前后日期排行榜
$boardBtn.on('click',function(event) {
	var dateSpilt=$.trim($infoDate.text()),
		currDate;
	if(/heroPre/.test(event.target.id)) {
		currDate=GetDateStr(dateSpilt,-1); //前一天
	}
	else if(/heroNext/.test(event.target.id)){
		currDate=GetDateStr(dateSpilt,1); //后一天
	}


	if(currDate.replace(/-/gi,'')==startTime) {
        $heroPre.hide();
        $heroNext.show();
    }else if(currDate.replace(/-/gi,'')==todayDate && currDate.replace(/-/gi,'')>startTime){
        $heroPre.show();
        $heroNext.hide();
    }else if(currDate.replace(/-/gi,'')==endTime && currDate.replace(/-/gi,'')==todayDate){
        $heroPre.show();
        $heroNext.hide();
    }else if(currDate.replace(/-/gi,'')>startTime && currDate.replace(/-/gi,'')<todayDate){
        $heroPre.show();
        $heroNext.show();
    }
	$infoDate.text(currDate);
	getBoard($('.board-tab span.active').attr('data-type'),$.trim($infoDate.text()));

});

$showLogin.on('click', function(event) {
	event.preventDefault();
	layer.open({
		type: 1,
		title: false,
		closeBtn: 0,
		area: ['auto', 'auto'],
		content: $('#loginTip')
	});
});

//关闭弹框
$('body').on('click', '.close-tip', function(event) {
	event.preventDefault();
	layer.closeAll();
});

getBoard('tyrant',$.trim($infoDate.text()));

//查看历史记录
$getHistory.on('click', function(event) {
	event.preventDefault();
	if(todayDate<startTime){
		layer.msg('活动暂未开始！');
	}else if(todayDate>endTime){
		layer.msg('活动已结束！');
	}else{
		commonFun.useAjax({
	        type:'GET',
	        dataType: 'json',
	        url:'/activity/newman-tyrant/history'
	    },function(data) {
	        $('#historyContent').html(tpl('historyContentTpl',data));
			layer.open({
	          type: 1,
	          closeBtn:0,
	          move:false,
	          area:$(window).width()>700?['600px','auto']:['300px','auto'],
	          title:false,
	          content: $('#tipContainer')
	        });
	    });
	}
	
});
