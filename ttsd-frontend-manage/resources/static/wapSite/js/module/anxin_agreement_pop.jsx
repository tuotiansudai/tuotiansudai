//所有弹框协议
$('body').on('click','a',function(event) {
    event.preventDefault();
    var target=event.target,
        $safetyAgreement=$('.safety-agreement-frame'),
        contentDom;
    var showAgreement=function(title,content) {
        event.preventDefault();
        layer.open({
            type:1,
            title:title,
            area:['100%','100%'],

            shadeClose: false,
            content: content
        });
    };

    let agreementInfo = {
        'link-agree-service':{
            title:'安心平台签服务协议',
            content:$('.service-box',$safetyAgreement)
        },
        'link-agree-privacy':{
            title:'隐私条款',
            content:$('.privacy-box',$safetyAgreement)
        },

        'link-agree-number':{
            title:'CFCA数字证书服务协议',
            content:$('.number-box',$safetyAgreement)
        },
        'link-agree-number-authorize':{
            title:'CFCA数字证书授权协议',
            content:$('.number-authorize-box',$safetyAgreement)
        },
        'link-agree-free-SMS':{
            title:'短信免责申明',
            content:$('.free-SMS-box',$safetyAgreement)
        }
    };

    let agreeDom = agreementInfo[target.className];

    agreeDom && showAgreement(agreeDom.title,agreeDom.content);



    // switch(target.className) {
    //     case 'link-agree-service':
    //         contentDom=$('.service-box',$safetyAgreement);
    //         showAgreement('安心平台签服务协议',contentDom);
    //         break;
    //     case 'link-agree-privacy':
    //         contentDom=$('.privacy-box',$safetyAgreement);
    //         showAgreement('隐私条款',contentDom);
    //         break;
    //     case 'link-agree-number':
    //         contentDom=$('.number-box',$safetyAgreement);
    //         showAgreement('CFCA数字证书服务协议',contentDom);
    //         break;
    //     case 'link-agree-number-authorize':
    //         contentDom=$('.number-authorize-box',$safetyAgreement);
    //         showAgreement('CFCA数字证书服务协议',contentDom);
    //         break;
    //     case 'link-agree-free-SMS':
    //         contentDom=$('.free-SMS-box',$safetyAgreement);
    //         showAgreement('短信免责申明',contentDom);
    //         break;
    // }

});