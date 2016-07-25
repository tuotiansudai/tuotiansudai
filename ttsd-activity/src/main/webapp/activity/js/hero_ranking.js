require(['jquery', 'underscore','layerWrapper', 'template', 'jquery.ajax.extension'], function($,_, layer, tpl) {
	$(function() {
		var $getVip=$('#getVip'),
			$getRank=$('.get-rank'),
			$TodayAwards=$('#TodayAwards'),
			$investRankingButton=$('#investRanking-button'),
			$referRankingButton=$('#referRanking-button');

		var todayDate= $.trim($TodayAwards.text());
		var ListTpl=$('#tplTable').html();
		var ListRender = _.template(ListTpl);

		$investRankingButton.find('.button-small').on('click',function(event) {
			var $HistoryAwards=$('#HistoryAwards');
			var dateSpilt=$HistoryAwards.find('em').show().text().split('-'),
				year=dateSpilt[0],
				month=dateSpilt[1],
				day=dateSpilt[2],
				currDate;
			if(/heroPre/.test(event.target.id)) {
				day=day-0-1;
			}
			else if(/heroNext/.test(event.target.id)){
				day=day-0+1;
			}
			if(day>0 && day<=31) {
				currDate=year+'-'+month+'-'+day;
				$HistoryAwards.find('em').text(currDate);
				if(currDate==todayDate) {
					$HistoryAwards.find('i').show();
					$HistoryAwards.find('em').hide();
				}
				else {
					$HistoryAwards.find('i').hide();
					$HistoryAwards.find('em').show();
				}
				heroRank(currDate);
			}
		});

		$referRankingButton.find('.button-small').on('click',function(event) {
			var $ReferRankingDate=$('#ReferRankingDate');
			var dateSpilt=$ReferRankingDate.find('em').text().split('-'),
				year=dateSpilt[0],
				month=dateSpilt[1],
				day=dateSpilt[2],
				currDate;
			if(/referPre/.test(event.target.id)) {
				day=day-0-1;
			}
			else if(/referNext/.test(event.target.id)){
				day=day-0+1;
			}
			if(day>0 && day<=31) {
				currDate=year+'-'+month+'-'+day;
				$ReferRankingDate.find('em').text(currDate);

				if(currDate==todayDate) {
					$ReferRankingDate.find('i').show();
					$ReferRankingDate.find('em').hide();
				}
				else {
					$ReferRankingDate.find('i').hide();
					$ReferRankingDate.find('em').show();
				}

				refeInvest(currDate);
			}
		});



		$getVip.on('click', function(event) {
			event.preventDefault();

			$.ajax({
				url: '/membership/receive',
				type: 'GET',
				dataType: 'json'
			})
			.done(function(data) {
					var tipTpl=$('#vipTipModelTpl').html();
					var TipRender = _.template(tipTpl);
					$('#vipTipModel').html(TipRender(data));

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

		//英雄榜排名,今日投资排行
		function heroRank(date) {
			$.ajax({
				url: '/activity/hero-ranking/invest/' + date,
				type: 'GET',
				dataType: 'json'
			})
			.done(function(data) {
				if(data.status) {
					var $nodataInvest=$('.nodata-invest'),
						$contentRanking=$('#investRanking-tbody').parents('table');

					if(_.isNull(data.records) || data.records.length==0) {
						$nodataInvest.show();
						$contentRanking.hide();
						return;
					}
					$contentRanking.show();
					$nodataInvest.hide();
					data.type='invest';
					$('#investRanking-tbody').html(ListRender(data));
				}

			})
			.fail(function() {
				layer.msg('请求失败，请重试！');
			});
		}
		
		//推荐榜排名
		function refeInvest(date) {
			$.ajax({
				url: '/activity/hero-ranking/referrer-invest/' + date,
				type: 'GET',
				dataType: 'json'
			})
			.done(function(data) {
				if(data.status) {
					var $nodataInvest=$('.nodata-refer'),
						$contentRefer=$('#referRanking-tbody').parents('table');
					if(_.isNull(data.records) || data.records.length==0) {
						$nodataInvest.show();
						$contentRefer.hide();
						return;
					}
					$contentRefer.show();
					$nodataInvest.hide();
					data.type='referrer';
					$('#referRanking-tbody').html(ListRender(data));
				}
			})
			.fail(function() {
				layer.msg('请求失败，请重试！');
			});
		}
		heroRank(todayDate);
		refeInvest(todayDate);

		$getRank.on('click', function() {
			cnzzPush.trackClick('153周年庆', '我要上榜', '英雄榜');
		});

		$getVip.on('click', function() {
			cnzzPush.trackClick('152周年庆', '领取会员', '英雄榜');
		});
		
	});
});