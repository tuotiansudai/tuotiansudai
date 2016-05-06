require(['jquery', 'pagination', 'layerWrapper', 'coupon-alert','red-envelope-float','jquery.ajax.extension'], function ($, pagination,layer) {
    $(function() {
        $('#transferSubmit').on('click', function(event) {
            event.preventDefault();
            var transferApplicationId=parseInt($("#transferInvestId").val()),
                transferAmount=$("#amount").val(),
                userBalance=$("#userBalance").val(),
                $transferDetail = $('.transfer-detail-content');
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
                        var $transferForm = $('#transferForm');
                        if ($transferForm.attr('action') === '/transfer/purchase') {

                            var isInvestor = 'INVESTOR' === $transferDetail.data('user-role');
                            if (!isInvestor) {
                                location.href = '/login?redirect=' + encodeURIComponent(location.href);
                                return false;
                            }

                            var accountAmount = parseInt((userBalance*100).toFixed(0)) || 0;
                            if (parseInt((transferAmount*100).toFixed(0)) > accountAmount) {
                                location.href = '/recharge';
                                return false;
                            }
                        }
                        $transferForm.submit();
                    }
                })
                .fail(function() {
                    layer.msg('请求失败');
                });
        });
    });
});