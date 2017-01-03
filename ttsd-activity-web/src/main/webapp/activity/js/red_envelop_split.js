require(['jquery', 'layerWrapper','commonFun','validator'], function($,layer,commonFun,validator) {

    $(function() {
        var $redEnvelopSplit=$('#redEnvelopSplit');
        var  $redEnvelopReferrer=$('#redEnvelopReferrer');
        var phoneForm = document.getElementById('phoneForm');

        //会员发红包
        (function () {
            if(!$redEnvelopSplit.length) {
                return;
            }
            $('.envelop-button span',$redEnvelopSplit).on('click',function() {
                var $this=$(this),
                    thisNum=$this.index();

                $this.addClass('active').siblings('span').removeClass('active');
                $('.envelop-box',$redEnvelopSplit).eq(thisNum).show().siblings('.envelop-box').hide();
            });

        })();

        //被邀请人注册
        //第一步，输入手机号
        (function() {
            if(!$redEnvelopReferrer.length) {
                return;
            }
            var validatorMobile = new validator();
            var phoneForm=globalFun.$('#phoneForm');
            var locationUrl=location.href;
            var parseURL=globalFun.parseURL(locationUrl),
                channel=parseURL.params.channel,
                loginName=parseURL.params.loginName;

            phoneForm.loginName.value=loginName;
            phoneForm.channel.value=channel;

            validatorMobile.add(phoneForm.mobile, [{
                strategy: 'isNonEmpty',
                errorMsg: '手机号不能为空'
            }, {
                strategy: 'isMobile',
                errorMsg: '手机号格式不正确'
            },{
                strategy: 'isMobileExist',
                errorMsg: '手机号不存在'
            }]);

            phoneForm.onsubmit = function(event) {
                event.preventDefault();
                var errorMsg = validatorMobile.start(phoneForm.mobile);
                if(errorMsg) {
                    layer.msg(errorMsg);
                    return;
                }
                else {
                    phoneForm.submit();
                }
            }


        })();
    })
});