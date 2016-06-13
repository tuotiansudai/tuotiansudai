require(['jquery', 'layerWrapper', 'template', 'jquery.ajax.extension'], function($, layer, tpl) {
	$(function() {
		var $getHistory=$('.show-btn'),
			$backBtn=$('.back-btn'),
			$heroPre=$('#heroPre'),
			$heroNext=$('#heroNext'),
			$refePre=$('#refePre'),
			$refeNext=$('#refeNext');

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

		//英雄榜排名
		function heroRank(type, date) {
			$.ajax({
				url: '/hero-ranking/invest/' + date,
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
				url: '/hero-ranking/referrer-invest/' + date,
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
		// heroRank('list', '2016-07-01');
		// heroRank('history', '2016-07-01');
		// refeInvest('list', '2016-07-01');
		// refeInvest('history', '2016-07-01');
		var data={
          "status":true,
          "message":"",
          "records":[
            {
              "sumAmount":"100000",
              "loginName":"zl***"
            },
			{
              "sumAmount":"100000",
              "loginName":"zl***"
            },
			{
              "sumAmount":"100000",
              "loginName":"zl***"
            },
			{
              "sumAmount":"100000",
              "loginName":"zl***"
            },
			{
              "sumAmount":"100000",
              "loginName":"zl***"
            },
			{
              "sumAmount":"100000",
              "loginName":"zl***"
            },
			{
              "sumAmount":"100000",
              "loginName":"zl***"
            },
			{
              "sumAmount":"100000",
              "loginName":"zl***"
            },
			{
              "sumAmount":"100000",
              "loginName":"zl***"
            },
			{
              "sumAmount":"100000",
              "loginName":"zl***"
            }
          ]
        };
		$('#heroList').html(tpl('heroListTpl', data));
		$('#heroRecord').html(tpl('heroRecordTpl', data));
		$('#refeInvest').html(tpl('refeInvestTpl', data));
		$('#refeRecord').html(tpl('refeRecordTpl', data));
		
	});
});