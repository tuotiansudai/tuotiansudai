define(['jquery', 'layerWrapper', 'jquery.ajax.extension'], function($, layer) {

	(function() {
        $safetyFrame=$('#safetySignedFrame');
		//所有弹框协议
        $('body').on('click','a',function(event) {
            var target=event.target,
                $safetyAgreement=$('.safety-agreement-frame',$safetyFrame),
                contentDom;
            var showAgreement=function(title,content) {
                event.preventDefault();
                layer.open({
                    type:1,
                    title:title,
                    area:['800px','520px'],
                    shadeClose: false,
                    content: content
                });
            }
            switch(target.className) {
                case 'link-agree-service':
                    contentDom=$('.service-box',$safetyAgreement);
                    showAgreement('安心平台签服务协议',contentDom);
                    break;
                case 'link-agree-privacy':
                    contentDom=$('.privacy-box',$safetyAgreement);
                    showAgreement('隐私条款',contentDom);
                    break;
                case 'link-agree-number':
                    contentDom=$('.number-box',$safetyAgreement);
                    showAgreement('CFCA数字证书服务协议',contentDom);
                    break;
                case 'link-agree-free-SMS':
                    contentDom=$('.free-SMS-box',$safetyAgreement);
                    showAgreement('安心签免短信授权服务协议',contentDom);
                    break;
            }

        });


	})();

});