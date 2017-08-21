define([], function() {
    require("webStyle/module/anxin_agreement.scss");
    //所有弹框协议
    $('.anxin_layer').on('click',function(event) {
        var target=event.target,
            $safetyAgreement=$('.safety-agreement-frame'),
            contentDom;
        var showAgreement=function(title,content) {
            event.preventDefault();
            layer.open({
                type:1,
                title:title,
                area:$(window).width()>700?['800px','520px']:['320px','100%'],
                shadeClose: false,
                scrollbar: true,
                skin:'register-skin',
                content: content
            });
        }
        if($(this).hasClass('link-agree-service')){
            contentDom=$('.service-box',$safetyAgreement);
            showAgreement('安心签平台服务协议',contentDom);
        }else if($(this).hasClass('link-agree-privacy')){
            contentDom=$('.privacy-box',$safetyAgreement);
            showAgreement('隐私条款',contentDom);
        }else if($(this).hasClass('link-agree-number')){
            contentDom=$('.number-box',$safetyAgreement);
            showAgreement('CFCA数字证书服务协议',contentDom);
        }else if($(this).hasClass('link-agree-free-SMS')){
            contentDom=$('.free-SMS-box',$safetyAgreement);
            showAgreement('短信免责申明',contentDom);
        }else if($(this).hasClass('link-agree-number-authorize')){
            contentDom=$('.number-authorize-box',$safetyAgreement);
            showAgreement('CFCA数字证书授权协议',contentDom);
        }

    });

});