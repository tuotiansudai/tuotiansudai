require(['jquery','layerWrapper', 'template', 'logintip', 'jquery.ajax.extension'], function($, layer, tpl) {
	$(function() {
		var $newmanTyrant=$('#newmanTyrant'),
			$boardTab=$('.board-tab span',$newmanTyrant),
			$infoDate=$('#infoDate'),
			todayDate=$.trim($infoDate.attr('data-date')).replace(/-/gi,''),
			$boardBtn=$('.board-btn span',$newmanTyrant),
			$heroNext=$('#heroNext'),
			$heroPre=$('#heroPre');
			
		if(todayDate==20170420) {
	        $heroPre.hide();
	        $heroNext.hide();
	    }else if(todayDate==20170509){
	        $heroNext.hide();
	    }else if(todayDate>20170420 && todayDate<20170509){
	        $heroPre.show();
	        $heroNext.hide();
	    }else if(todayDate>20170509){
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


			if(currDate.replace(/-/gi,'')==20170420) {
	            $heroPre.hide();
	            $heroNext.show();
	        }else if(currDate.replace(/-/gi,'')==todayDate && currDate.replace(/-/gi,'')>20170420){
	            $heroPre.show();
	            $heroNext.hide();
	        }else if(currDate.replace(/-/gi,'')==20170509 && currDate.replace(/-/gi,'')==todayDate){
	            $heroPre.show();
	            $heroNext.hide();
	        }else if(currDate.replace(/-/gi,'')>20170420 && currDate.replace(/-/gi,'')<todayDate){
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
	});
});