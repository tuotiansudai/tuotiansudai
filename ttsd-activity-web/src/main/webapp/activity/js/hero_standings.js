require(['jquery', 'underscore','layerWrapper', 'template', 'logintip','jquery.ajax.extension','register_common'], function($,_, layer, tpl) {
	$(function() {
		var browser = globalFun.browserRedirect(),
			$TodayAwards=$('#TodayAwards'),
			$historyTime=$('#historyTime'),
			$investRankingButton=$('#investRanking-button'),
			$heroNext=$('#heroNext'),
			$heroPre=$('#heroPre'),
			todayDate= $.trim($TodayAwards.val()),
			ListTpl=$('#tplTable').html(),
			ListRender = _.template(ListTpl),
			bgListTpl=$('#bgtplTable').html(),
			bgListRender = _.template(bgListTpl);

		if (browser == 'mobile') {

            var urlObj=globalFun.parseURL(location.href);
            if(urlObj.params.tag=='yes') {
                $('.reg-tag-current').show();
            }
        }


        $historyTime.text()>=$TodayAwards.val()?$heroNext.hide():$heroNext.show();
        $historyTime.text().replace(/-/gi,'')==$('#startTime').val()?$heroPre.hide():$heroPre.show();
		//获取前一天或者后一天的日期
		function GetDateStr(date,AddDayCount) {
			var dd = new Date(date);
			dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
			var y = dd.getFullYear();
			var m = dd.getMonth()+1;//获取当前月份的日期
			var d = dd.getDate();

			return y + "-" + (m < 10 ? ('0' + m) : m) + "-" + (d < 10 ? ('0' + d) : d);
		}


		$investRankingButton.find('.change-btn').on('click',function(event) {
			var dateSpilt=$historyTime.text(),
				currDate;
			
			if(/heroPre/.test(event.target.id)) {
				if($historyTime.text().replace(/-/gi,'')>$('#startTime').val()){
					currDate=GetDateStr(dateSpilt,-1); //前一天
					heroRank(currDate);
					$historyTime.text(currDate);
				}
			}
			else if(/heroNext/.test(event.target.id)){
				if($historyTime.text().replace(/-/gi,'')<=$('#endTime').val()){
					currDate=GetDateStr(dateSpilt,1); //后一天
					heroRank(currDate);
					$historyTime.text(currDate);
				}
			}
				
			$historyTime.text().replace(/-/gi,'')>=$TodayAwards.val().replace(/-/gi,'')?$heroNext.hide():$heroNext.show();
			$historyTime.text().replace(/-/gi,'')==$('#startTime').val()?$heroPre.hide():$heroPre.show();
			
			
		});


		//英豪榜排名,今日投资排行
		function heroRank(date) {
			$.ajax({
				url: '/activity/hero-ranking/invest/' + date + '?activityCategory=NEW_HERO_RANKING',
				type: 'GET',
				dataType: 'json'
			})
			.done(function(data) {
				if(data.status) {
					data.type='invest';
					$('#investRanking-tbody').html(ListRender(data));
					$('#bgItem').html(bgListRender(data));
				}

			})
			.fail(function() {
				layer.msg('请求失败，请重试！');
			});
		}
		heroRank(todayDate);
        


	});
});