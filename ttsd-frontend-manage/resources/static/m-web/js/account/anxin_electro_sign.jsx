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
            layer.msg('开启失败');
        } else {
            layer.msg('开启成功！');
            location.href='/m/anxinSign/createAccountSuccess';
        }
    });
});

(function (doc, win) {
    let docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            let clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            let fSize = 20 * (clientWidth /375);
            fSize > 40 && (fSize = 39.36);
            docEl.style.fontSize = fSize + 'px';
        };

    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);

// 点击返回btn
$('.go-back-container').on('click',() => {
    history.go(-1);
});



$('.init-checkbox-style').initCheckbox(function(element) {
    //点击我已阅读并同意是否disable按钮
    var isCheck=$(element).hasClass('on'),
        $btnNormal=$('#openSafetySigned');

    if(isCheck) {
        $btnNormal.prop('disabled',false);
    }
    else {
        $btnNormal.prop('disabled',true);
    }
});