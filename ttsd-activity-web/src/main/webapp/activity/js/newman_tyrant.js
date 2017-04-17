require(['jquery','layerWrapper', 'template', 'logintip', 'jquery.ajax.extension'], function($, layer, tpl) {
	$(function() {
		var $newmanTyrant=$('#newmanTyrant'),
			$boardTab=$('.board-tab span',$newmanTyrant),
			$infoDate=$('#infoDate'),
			todayDate=$.trim($infoDate.attr('data-date')).replace(/-/gi,''),
			startTime=$.trim($infoDate.attr('data-startTime')).replace(/-/gi,''),
			endTime=$.trim($infoDate.attr('data-endTime')).replace(/-/gi,''),
			$boardBtn=$('.board-btn span',$newmanTyrant),
			$heroNext=$('#heroNext'),
			$heroPre=$('#heroPre'),
			$getHistory=$('#getHistory');
			
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
			$.ajax({
				url: '/activity/newman-tyrant/'+type+'/'+date,
				type: 'GET',
				dataType: 'json'
			})
			.done(function(data) {
				data={"status":true,"message":null,"index":0,"pageSize":0,"count":0,"maxPage":0,"hasPreviousPage":false,"hasNextPage":false,"records":[{"loginName":"130****9958","sumAmount":18200000,"userName":"刘丽娜","mobile":"13051419958","centSumAmount":"182000.00"},{"loginName":"139****1689","sumAmount":14606000,"userName":"姚勉","mobile":"13942031689","centSumAmount":"146060.00"},{"loginName":"185****1897","sumAmount":11500000,"userName":"管淑清","mobile":"18510171897","centSumAmount":"115000.00"},{"loginName":"134****3549","sumAmount":5000000,"userName":"高立斌","mobile":"13426473549","centSumAmount":"50000.00"},{"loginName":"188****0295","sumAmount":2002800,"userName":"尤洪君","mobile":"18831480295","centSumAmount":"20028.00"},{"loginName":"186****8659","sumAmount":2000000,"userName":"张聪","mobile":"18643918659","centSumAmount":"20000.00"},{"loginName":"139****5001","sumAmount":2000000,"userName":"刘惠杰","mobile":"13932705001","centSumAmount":"20000.00"},{"loginName":"132****6796","sumAmount":1582700,"userName":"杜云英","mobile":"13283226796","centSumAmount":"15827.00"},{"loginName":"186****9482","sumAmount":1400000,"userName":"苗瑞","mobile":"18600809482","centSumAmount":"14000.00"},{"loginName":"139****7336","sumAmount":700000,"userName":"周真真","mobile":"13916017336","centSumAmount":"7000.00"}]};
				if(data.status) {
					$('#rankTable').html(tpl('rankTableTpl',data));
				}
			})
			.fail(function() {
				layer.msg('请求失败，请重试！');
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
				$.ajax({
					url: '/activity/newman-tyrant/history',
					type: 'GET',
					dataType: 'json'
				})
				.done(function(data) {
					console.log(data.records);
					$('#historyContent').html(tpl('historyContentTpl',data));
					layer.open({
			          type: 1,
			          closeBtn:0,
			          move:false,
			          area:$(window).width()>700?['600px','auto']:['300px','auto'],
			          title:false,
			          content: $('#tipContainer')
			        });
				})
				.fail(function() {
					layer.msg('请求失败，请重试！');
				});
			}
			
		});
		
	});
});