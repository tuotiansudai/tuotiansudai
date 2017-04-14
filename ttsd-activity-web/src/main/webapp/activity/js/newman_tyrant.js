require(['jquery', 'underscore','layerWrapper', 'template', 'jquery.ajax.extension'], function($,_, layer, tpl) {
	// $(function() {
	// 	var $getVip=$('#getVip'),
	// 		$getRank=$('.get-rank'),
	// 		$TodayAwards=$('#TodayAwards'),
	// 		$investRankingButton=$('#investRanking-button'),
	// 		$referRankingButton=$('#referRanking-button'),
	// 		$heroNext=$('#heroNext'),
	// 		$referPre=$('#referPre'),
	// 		$referNext=$('#referNext'),
	// 		$heroPre=$('#heroPre');

	// 	var todayDate= $.trim($TodayAwards.text());
	// 	var ListTpl=$('#tplTable').html();
	// 	var ListRender = _.template(ListTpl);

	// 	if(todayDate.replace(/-/gi,'')>=20160731) {
	// 		$heroNext.hide();
	// 		$referNext.hide();
	// 	}

	// 	//获取前一天或者后一天的日期
	// 	function GetDateStr(date,AddDayCount) {
	// 		var dd = new Date(date);
	// 		dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
	// 		var y = dd.getFullYear();
	// 		var m = dd.getMonth()+1;//获取当前月份的日期
	// 		var d = dd.getDate();

	// 		return y + "-" + (m < 10 ? ('0' + m) : m) + "-" + (d < 10 ? ('0' + d) : d);
	// 	}

	// 	$investRankingButton.find('.button-small').on('click',function(event) {
	// 		var $HistoryAwards=$('#HistoryAwards');
	// 		var dateSpilt=$HistoryAwards.find('em').show().text(),
	// 			currDate;
	// 		if(/heroPre/.test(event.target.id)) {
	// 			currDate=GetDateStr(dateSpilt,-1); //前一天
	// 		}
	// 		else if(/heroNext/.test(event.target.id)){
	// 			currDate=GetDateStr(dateSpilt,1); //后一天
	// 		}
	// 		if(currDate.replace(/-/gi,'')>=20160731) {
	// 			$heroNext.hide();
	// 		}
	// 		else {
	// 			$heroNext.show();
	// 		}
	// 		if(currDate.replace(/-/gi,'')<=20160701) {
	// 			$heroPre.hide();
	// 		}
	// 		else {
	// 			$heroPre.show();
	// 		}
	// 			$HistoryAwards.find('em').text(currDate);
	// 			heroRank(currDate);

	// 	});

	// 	$referRankingButton.find('.button-small').on('click',function(event) {
	// 		var $ReferRankingDate=$('#ReferRankingDate');
	// 		var dateSpilt=$ReferRankingDate.find('em').text(),
	// 			currDate;
	// 		if(/referPre/.test(event.target.id)) {
	// 			currDate=GetDateStr(dateSpilt,-1); //前一天
	// 		}
	// 		else if(/referNext/.test(event.target.id)){
	// 			currDate=GetDateStr(dateSpilt,1); //后一天
	// 		}
	// 		if(currDate.replace(/-/gi,'')>=20160731) {
	// 			$referNext.hide();
	// 		}
	// 		else {
	// 			$referNext.show();
	// 		}
	// 		if(currDate.replace(/-/gi,'')<=20160701) {
	// 			$referPre.hide();
	// 		}
	// 		else {
	// 			$referPre.show();
	// 		}

	// 			$ReferRankingDate.find('em').text(currDate);
	// 			refeInvest(currDate);
	// 	});

	// 	$getVip.on('click', function(event) {
	// 		event.preventDefault();

	// 		$.ajax({
	// 			url: '/membership/receive',
	// 			type: 'GET',
	// 			dataType: 'json'
	// 		})
	// 		.done(function(data) {
	// 				var tipTpl=$('#vipTipModelTpl').html();
	// 				var TipRender = _.template(tipTpl);
	// 				$('#vipTipModel').html(TipRender(data));

	// 			layer.open({
	// 			  type: 1,
	// 			  move:false,
	// 			  area:['320px','auto'],
	// 			  title:'领取须知',
	// 			  content: $('#vipTipModel')
	// 			});
	// 		})
	// 		.fail(function() {
	// 			layer.msg('请求失败，请重试！');
	// 		});
	// 	});

	// 	$('body').on('click', '#closeTip', function(event) {
	// 		event.preventDefault();
	// 		layer.closeAll();
	// 	});

	// 	//英雄榜排名,今日投资排行
	// 	function heroRank(date) {
	// 		$.ajax({
	// 			url: '/activity/hero-ranking/invest/' + date,
	// 			type: 'GET',
	// 			dataType: 'json'
	// 		})
	// 		.done(function(data) {
	// 			if(data.status) {
	// 				var $nodataInvest=$('.nodata-invest'),
	// 					$contentRanking=$('#investRanking-tbody').parents('table');

	// 				if(_.isNull(data.records) || data.records.length==0) {
	// 					$nodataInvest.show();
	// 					$contentRanking.hide();
	// 					return;
	// 				}
	// 				$contentRanking.show();
	// 				$nodataInvest.hide();
	// 				data.type='invest';
	// 				$('#investRanking-tbody').html(ListRender(data));
	// 			}

	// 		})
	// 		.fail(function() {
	// 			layer.msg('请求失败，请重试！');
	// 		});
	// 	}
		
	// 	//推荐榜排名
	// 	function refeInvest(date) {
	// 		$.ajax({
	// 			url: '/activity/hero-ranking/referrer-invest/' + date,
	// 			type: 'GET',
	// 			dataType: 'json'
	// 		})
	// 		.done(function(data) {
	// 			if(data.status) {
	// 				var $nodataInvest=$('.nodata-refer'),
	// 					$contentRefer=$('#referRanking-tbody').parents('table');
	// 				if(_.isNull(data.records) || data.records.length==0) {
	// 					$nodataInvest.show();
	// 					$contentRefer.hide();
	// 					return;
	// 				}
	// 				$contentRefer.show();
	// 				$nodataInvest.hide();
	// 				data.type='referrer';
	// 				$('#referRanking-tbody').html(ListRender(data));
	// 			}
	// 		})
	// 		.fail(function() {
	// 			layer.msg('请求失败，请重试！');
	// 		});
	// 	}
	// 	heroRank('2016-07-31');
	// 	refeInvest('2016-07-31');

	// 	$getRank.on('click', function() {
	// 		cnzzPush.trackClick('153周年庆', '我要上榜', '英雄榜');
	// 	});

	// 	$getVip.on('click', function() {
	// 		cnzzPush.trackClick('152周年庆', '领取会员', '英雄榜');
	// 	});
		
	// });
});