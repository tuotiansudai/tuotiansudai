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
					//var data={"status":true,"message":null,"records":[{"loginName":"152**20","sumAmount":20600257,"userName":"张成","mobile":"15201110820","centSumAmount":"206002.57"},{"loginName":"187**00","sumAmount":5000000,"userName":"张林峰","mobile":"18703348400","centSumAmount":"50000.00"},{"loginName":"139**01","sumAmount":3000000,"userName":"刘惠杰","mobile":"13932705001","centSumAmount":"30000.00"},{"loginName":"151**55","sumAmount":2000000,"userName":"王淑丹","mobile":"15110026455","centSumAmount":"20000.00"},{"loginName":"132**96","sumAmount":1544619,"userName":"杜云英","mobile":"13283226796","centSumAmount":"15446.19"},{"loginName":"139**36","sumAmount":1021904,"userName":"周真真","mobile":"13916017336","centSumAmount":"10219.04"},{"loginName":"139**26","sumAmount":255873,"userName":"刘继红","mobile":"13966809826","centSumAmount":"2558.73"},{"loginName":"137**23","sumAmount":100000,"userName":"于洪梅","mobile":"13722473823","centSumAmount":"1000.00"},{"loginName":"139**71","sumAmount":100000,"userName":"王悦双","mobile":"13932461171","centSumAmount":"1000.00"},{"loginName":"137**36","sumAmount":22615,"userName":"韩巧丽","mobile":"13780579036","centSumAmount":"226.15"}]};
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