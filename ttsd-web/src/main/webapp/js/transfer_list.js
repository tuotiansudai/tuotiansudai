require(['jquery', 'layerWrapper','coupon-alert','red-envelope-float'], function ($, layer) {
    $(function() {
        $('.transfer-list-box ul li').on('click', function(event) {
            event.preventDefault();
            var $self = $(this),
                urldata = $self.attr('data-url');
			$.ajax({
				url: '/transfer/purchare?id',
				type: 'POST',
				dataType: 'json',
				data: {param1: 'value1'},
			})
			.done(function(data) {
				if(data==1){
					layer.open({
					  title: '温馨提示',
					  btn: ['确定'],
					  content: '该项目已被承接，请选择其他项目。',
					  btn1: function(index, layero){
					    layer.closeAll();
					  }
					});
				}else if(data==2){
					layer.open({
					  title: '温馨提示',
					  btn: ['确定'],
					  content: '该项目已被取消，请选择其他项目。',
					  btn1: function(index, layero){
					    layer.closeAll();
					  }
					});
				}else{
					location.href = urldata;
				}
			})
			.fail(function() {
				layer.msg('请求失败');
			});
        });
    });
});