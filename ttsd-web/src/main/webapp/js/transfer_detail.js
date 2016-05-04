require(['jquery', 'pagination', 'layerWrapper', 'coupon-alert','red-envelope-float','jquery.ajax.extension'], function ($, pagination,layer) {
    $(function() {
        $('#transferBtn').on('click', function(event) {
            event.preventDefault();
            var $self=$(this),
                transferApplicationId=$self.attr('data-url-id'),
                transferStatus=$self.attr('data-url-status'),
                $transferDetail = $('.transfer-detail-content'),
                urldata=$self.attr('data-url');
            $.ajax({
                    url: '/transfer/'+transferApplicationId+'/purchase-check',
                    type: 'GET',
                    dataType: 'json'
                })
                .done(function(data) {
                    if(data.message == "SUCCESS"){
                        layer.open({
                            title: '温馨提示',
                            btn: ['确定'],
                            content: '该项目已被承接，请选择其他项目。',
                            btn1: function(index, layero){
                                layer.closeAll();
                                location.href = "/transfer-list";
                            }
                        });
                    } else if(data.message == "CANCEL"){
                        layer.open({
                            title: '温馨提示',
                            btn: ['确定'],
                            content: '该项目已被取消，请选择其他项目。',
                            btn1: function(index, layero){
                                layer.closeAll();
                                location.href = "/transfer-list";
                            }
                        });
                    }else{
                        var isInvestor = 'INVESTOR' === $transferDetail.data('user-role');
                        if (!isInvestor) {
                            location.href = '/login?redirect=' + encodeURIComponent(location.href);
                            return false;
                        }
                        location.href = urldata;
                    }
                })
                .fail(function() {
                    layer.msg('请求失败');
                });
        });
    });
});