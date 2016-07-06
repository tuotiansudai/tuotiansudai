require(['jquery', 'layerWrapper', 'template', 'jquery.ajax.extension'], function($, layer, tpl) {
	$(function() {
		var $getHistory=$('.show-btn'),
			$backBtn=$('.back-btn'),
			$heroPre=$('#heroPre'),
			$heroNext=$('#heroNext'),
			$refePre=$('#refePre'),
			$refeNext=$('#refeNext'),
			$getVip=$('#getVip'),
			$getRank=$('.get-rank');

		$getHistory.on('click', function(event) {
			event.preventDefault();
			var $self=$(this),
				$rankList=$self.closest('.rank-model');
			$rankList.addClass('active');
		});
		$backBtn.on('click', function(event) {
			event.preventDefault();
			var $self=$(this),
				$rankList=$self.closest('.rank-model');
			$rankList.removeClass('active');
		});

		$heroPre.on('click', function(event) {
			event.preventDefault();
			var $self=$(this),
				$dateTime=$self.siblings('.date-info'),
				getTime=parseInt($dateTime.text().split('-')[2])>1?parseInt($dateTime.text().split('-')[2])-1:1,
				timeNum='2016-07-'+(getTime>9?getTime:'0'+getTime);
			heroRank('history', timeNum);
			$dateTime.text(timeNum);
		});
		$heroNext.on('click', function(event) {
			event.preventDefault();
			var $self=$(this),
				$dateTime=$self.siblings('.date-info'),
				getTime=parseInt($dateTime.text().split('-')[2])<31?parseInt($dateTime.text().split('-')[2])+1:31,
				timeNum='2016-07-'+(getTime>9?getTime:'0'+getTime);
			heroRank('history', timeNum);
			$dateTime.text(timeNum);
		});

		$refePre.on('click', function(event) {
			event.preventDefault();
			var $self=$(this),
				$dateTime=$self.siblings('.date-info'),
				getTime=parseInt($dateTime.text().split('-')[2])>1?parseInt($dateTime.text().split('-')[2])-1:1,
				timeNum='2016-07-'+(getTime>9?getTime:'0'+getTime);
			refeInvest('history', timeNum);
			$dateTime.text(timeNum);
		});
		$refeNext.on('click', function(event) {
			event.preventDefault();
			var $self=$(this),
				$dateTime=$self.siblings('.date-info'),
				getTime=parseInt($dateTime.text().split('-')[2])<31?parseInt($dateTime.text().split('-')[2])+1:31,
				timeNum='2016-07-'+(getTime>9?getTime:'0'+getTime);
			refeInvest('history', timeNum);
			$dateTime.text(timeNum);
		});

		$getVip.on('click', function(event) {
			event.preventDefault();
			$.ajax({
				url: '/membership/receive',
				type: 'GET',
				dataType: 'json'
			})
			.done(function(data) {
				console.log(data);
				$('#vipTipModel').html(tpl('vipTipModelTpl', data));
				layer.open({
				  type: 1,
				  move:false,
				  area:['320px','auto'],
				  title:'领取须知',
				  content: $('#vipTipModel')
				});
			})
			.fail(function() {
				layer.msg('请求失败，请重试！');
			});
		});

		$('body').on('click', '#closeTip', function(event) {
			event.preventDefault();
			layer.closeAll();
		});

		//英雄榜排名
		function heroRank(type, date) {
			$.ajax({
				url: '/activity/hero-ranking/invest/' + date,
				type: 'GET',
				dataType: 'json'
			})
			.done(function(data) {
				if (type == 'list') {
					$('#heroList').html(tpl('heroListTpl', data));
				} else if (type == 'history') {
					$('#heroRecord').html(tpl('heroRecordTpl', data));
				}
			})
			.fail(function() {
				layer.msg('请求失败，请重试！');
			});
		}
		
		//推荐榜排名
		function refeInvest(type, date) {
			$.ajax({
				url: '/activity/hero-ranking/referrer-invest/' + date,
				type: 'GET',
				dataType: 'json'
			})
			.done(function(data) {
				if (type == 'list') {
					$('#refeInvest').html(tpl('refeInvestTpl', data));
				} else if (type == 'history') {
					$('#refeRecord').html(tpl('refeRecordTpl', data));
				}
			})
			.fail(function() {
				layer.msg('请求失败，请重试！');
			});
		}
		heroRank('list', $('.date-text:eq(0)').text());
		heroRank('history', $('.date-info:eq(0)').text());
		refeInvest('list', $('.date-text:eq(0)').text());
		refeInvest('history', $('.date-info:eq(0)').text());

		$getRank.on('click', function() {
			cnzzPush.trackClick('153周年庆', '我要上榜', '英雄榜');
		});

		$getVip.on('click', function() {
			cnzzPush.trackClick('152周年庆', '领取会员', '英雄榜');
		});
		
	});
});