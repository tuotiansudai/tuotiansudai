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

		//英雄榜排名,今日投资排行
		function heroRank(date) {
			$.ajax({
				url: '/activity/hero-ranking/invest/' + date,
				type: 'GET',
				dataType: 'json'
			})
			.done(function(data) {
					var data={"status":true,"message":null,"records":[{"loginName":"hey******","sumAmount":2289444,"userName":"赫媛媛","mobile":"18600749459","centSumAmount":"22894.44"},{"loginName":"yan******","sumAmount":2000000,"userName":"冯华容","mobile":"13932711604","centSumAmount":"20000.00"},{"loginName":"lin******","sumAmount":1628327,"userName":"王美娟","mobile":"15998504411","centSumAmount":"16283.27"},{"loginName":"che******","sumAmount":230000,"userName":"陈放","mobile":"18903240495","centSumAmount":"2300.00"},{"loginName":"lia******","sumAmount":200000,"userName":"梁晔","mobile":"13651133128","centSumAmount":"2000.00"},{"loginName":"lia******","sumAmount":100000,"userName":"梁金花","mobile":"13522923770","centSumAmount":"1000.00"},{"loginName":"wad******","sumAmount":60000,"userName":"吴冬","mobile":"13911552934","centSumAmount":"600.00"},{"loginName":"gai******","sumAmount":52663,"userName":"盖薇","mobile":"13731441374","centSumAmount":"526.63"},{"loginName":"wux******","sumAmount":50000,"userName":"王英霄","mobile":"15890187610","centSumAmount":"500.00"},{"loginName":"dou******","sumAmount":50000,"userName":"马豆","mobile":"15137180297","centSumAmount":"500.00"}]};
				if(data.status) {
					if(_.isNull(data.records) || data.records.length==0) {
						$('.nodata-invest').show();
						return;
					}
					$('#investRanking-tbody').parents('table').show();
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
				var data={"status":true,"message":null,"records":[{"loginName":"tts******","sumAmount":2289444,"userName":"刘守艳","mobile":"13901255493","centSumAmount":"22894.44"},{"loginName":"278******","sumAmount":2000000,"userName":"冯华梅","mobile":"13623271373","centSumAmount":"20000.00"},{"loginName":"aul******","sumAmount":300000,"userName":"梁海燕","mobile":"15910636139","centSumAmount":"3000.00"}]};
				if(data.status) {
					if(_.isNull(data.records) || data.records.length==0) {
						$('.nodata-refer').show();
						return;
					}
					$('#referRanking-tbody').parents('table').show();
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