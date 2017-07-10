require('wapSiteStyle/account/anxin_electro_sign.scss');
require('wapSiteJsModule/anxin_agreement_pop');
let $anxinElectroSign = $('#anxinElectroSign');

$('.init-checkbox-style',$anxinElectroSign).initCheckbox(function(element) {
    var $parentBox=$(element).parents('.safety-status-box');
    //点击我已阅读并同意是否disable按钮
    var isCheck=$(element).hasClass('on');
});


