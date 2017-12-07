require('mWebStyle/account/anxin_electro_sign.scss');
require('mWebStyle/account/anxin_advantage.scss');
require('mWebJsModule/anxin_agreement_pop');
let commonFun= require('publicJs/commonFun');
let $anxinElectroSign = $('#anxinElectroSign');

$('.init-checkbox-style',$anxinElectroSign).initCheckbox(function(element) {
    var $parentBox=$(element).parents('.safety-status-box');
    //点击我已阅读并同意是否disable按钮
    $(element).hasClass('on');
});

// 开启安心签服务
$('#openSafetySigned').on('click',function() {
    var $this=$(this);
    $this.prop('disabled',true);
    commonFun.useAjax({
        type:'POST',
        url:'/anxinSign/createAccount'
    },function(response) {
        $this.prop('disabled',false);
        if(!response.success){
            layer.msg('<span class="layer-msg-tip"><i></i>开启失败：'+response.data.message+'</span>',{
                skin:'msg-tip-box',
                time: 1500,
                area:['370px','90px']
            });
        } else {
            layer.msg('<span class="layer-msg-tip"><i></i>开启成功!</span>', {
                skin: 'msg-tip-box',
                time: 1500,
                area: ['290px', '90px']
            }, function () {
                //done
                location.href='anxin-electro-success.ftl';

            });
        }
    });
});


