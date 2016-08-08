require(['jquery', 'pagination', 'layerWrapper', 'coupon-alert','red-envelope-float','jquery.ajax.extension'], function ($, pagination,layer) {
    var $transferDetailCon=$('.transfer-detail-content'),
        $errorTip = $('.errorTip',$transferDetailCon),
        $questionList=$('.question-list',$transferDetailCon),
        $detailRecord=$('.detail-record',$transferDetailCon);

    $detailRecord.find('li').on('click',function() {
        var $this=$(this),
            num=$this.index();
        $this.addClass('active').siblings('li').removeClass('active');
        $('.detail-record-info',$transferDetailCon).eq(num).show().siblings('.detail-record-info').hide();

    })
    function showInputErrorTips(message) {
            layer.msg(message);
        }

        if ($errorTip.length > 0 && $errorTip.text() != '') {
            showInputErrorTips($errorTip.text());
        }

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
                    }else if(data.message == "MULTITERM"){
                        layer.open({
                            title: '温馨提示',
                            btn: ['确定'],
                            content: '该项目已被承接或已取消，请选择其他项目。',
                            btn1: function(index, layero){
                                layer.closeAll();
                                location.href = "/transfer-list";
                            }
                        });
                    }else{
                        layer.open({
                            type: 1,
                            closeBtn: 0,
                            skin: 'demo-class',
                            title: '投资提示',
                            shadeClose:false,
                            btn:[ '取消','确认'],
                            area: ['300px', '160px'],
                            content: '<p class="pad-m-tb tc">确认投资？</p>',
                            btn1: function(){
                                layer.closeAll();
                            },
                            btn2:function(){
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
                        });
                    }
                })
                .fail(function() {
                    layer.msg('请求失败');
                });
        });

    $questionList.find('dl dd').hide();
    $questionList.find('dl dd').eq(0).show();
    $questionList.find('dl dt').eq(0).find('i').addClass('fa-chevron-circle-up').removeClass('fa-chevron-circle-down')
    $questionList.find('dt').on('click',function(index) {
        var $this=$(this);
        $this.next('dd').toggle();
        if($this.next('dd').is(':hidden')) {
            $this.find('i').removeClass('fa-chevron-circle-up').addClass('fa-chevron-circle-down');
        }
        else {
            $this.find('i').addClass('fa-chevron-circle-up').removeClass('fa-chevron-circle-down');
        }
    })
});