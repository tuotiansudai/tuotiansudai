require(['jquery', 'underscore','layerWrapper', 'template', 'jquery.ajax.extension'], function($,_, layer, tpl) {
	$(function() {
		var $getVip=$('#getVip'),
			$getRank=$('.get-rank'),
			$TodayAwards=$('#TodayAwards'),
			$ReferRankingDate=$('#ReferRankingDate'),
			$investRankingButton=$('#investRanking-button'),
			$referRankingButton=$('#referRanking-button');

		var todayDate= $.trim($TodayAwards.text());
		var ListTpl=$('#tplTable').html();
		var ListRender = _.template(ListTpl);

		$investRankingButton.find('.button-small').on('click',function(event) {
			var dateSpilt=$TodayAwards.text().split('-'),
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
				$TodayAwards.text(currDate);
				heroRank(currDate);
			}
		});

		$referRankingButton.find('.button-small').on('click',function(event) {
			var dateSpilt=$ReferRankingDate.text().split('-'),
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
				$ReferRankingDate.text(currDate);
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
					var data={"status":true,"message":null,"records":[{"loginName":"158**32","sumAmount":6010023,"userName":"王林","mobile":"15810802032","centSumAmount":"60100.23"},{"loginName":"157**97","sumAmount":3000000,"userName":"齐银河","mobile":"15720311797","centSumAmount":"30000.00"},{"loginName":"139**10","sumAmount":2093113,"userName":"刘桂清","mobile":"13932754010","centSumAmount":"20931.13"},{"loginName":"137**81","sumAmount":2005000,"userName":"杨文萍","mobile":"13731738181","centSumAmount":"20050.00"},{"loginName":"135**82","sumAmount":2000000,"userName":"王淑颖","mobile":"13585010482","centSumAmount":"20000.00"},{"loginName":"158**01","sumAmount":500492,"userName":"李艳菊","mobile":"15810919401","centSumAmount":"5004.92"},{"loginName":"189**89","sumAmount":429155,"userName":"安桥芬","mobile":"18931772989","centSumAmount":"4291.55"},{"loginName":"189**96","sumAmount":215000,"userName":"王会青","mobile":"18911343396","centSumAmount":"2150.00"},{"loginName":"151**48","sumAmount":200000,"userName":"仲国苓","mobile":"15133898548","centSumAmount":"2000.00"},{"loginName":"138**19","sumAmount":155000,"userName":"张晨","mobile":"13810668619","centSumAmount":"1550.00"}]};
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