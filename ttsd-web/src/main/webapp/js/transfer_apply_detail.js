require(['jquery', 'layerWrapper','jquery.validate','coupon-alert','red-envelope-float','jquery.ajax.extension'], function ($, layer) {
	$(function() {
		$("#createForm").validate({
			debug:true,
			rules: {
		      price: {
		        required: true,
		        max:parseFloat($('#tipText').attr('data-max')),
		        min:parseFloat($('#tipText').attr('data-min'))
		      }
		    },
		    onkeyup:function(){
		    	$('#tipText').removeClass('active');
		    },
	        errorPlacement: function(error, element) {  
			    $('#tipText').addClass('active'); 
			},
		    submitHandler:function(form){
		    	$.ajax({
		    		url: '/transfer/apply',
		    		type: 'POST',
		    		dataType: 'json',
					contentType:'application/json',
					data: JSON.stringify({
						'transferAmount': parseFloat($('#transferAmount').val())*100,
						'transferInvestId': $('#transferInvestId').val()
					})
		    	})
		    	.done(function(data) {
		    		if(data==true){
			    		layer.open({
						  title: '温馨提示',
						  btn:0,
						  area: ['400px', '150px'],
						  content: $('#successTip').html(),
						  success: function(layero, index){
							    setInterval(function(){
							    	if($('.layui-layer-content .count-time').text()<2){
							    		window.location.href='/transferrer/transfer-application-list/TRANSFERRING';
							    	}else{
							    		$('.layui-layer-content .count-time').text(function(index,num){return parseInt(num)-1});
							    	}
							    }, 1000);
							}
						});
		    		}else{
		    			layer.msg('申请失败，请重试！');
		    		}
		    	})
		    	.fail(function() {
		    		layer.msg('请求失败，请重试！');
		    	});
		    	
	              
	        }
		});

		$('#cancleBtn').on('click', function(event) {
			event.preventDefault();
			history.go(-1);
		});
	});
});