require(['jquery', 'csrf'], function ($) {
    $(function () {
        //select bank
        var _bank = $('.banking ol li');
        _bank.click(function(){
            var text = $(this).find('input').attr('data-name');
            $('.jq-bank').val(text);
        });

        // 充值金额保留小数点后2位
        var rep = /^\d+$/;
        var rep_point = /^([0-9]+\.[0-9]{2})[0-9]*$/;
        var rep_point1 = /^[0-9]+\.[0-9]$/;
        $('.recharge-cz').blur(function () {
            var  _this = $(this)
            var _btn = _this.closest('form').find('.recharge-qr');
            var  text = _this.val();
            var  num = text.replace(rep_point,"$1");
            if(rep.test(text)){
                _this.val(text+'.00');
                _btn.removeClass('grey').removeAttr('disabled');
            }else if(rep_point.test(text)){
                _this.val(num);
                _btn.removeClass('grey').removeAttr('disabled');
            }else if(rep_point1.test(text)){
                _this.val(text+'0');
                _btn.removeClass('grey').removeAttr('disabled');
            }else{
                _this.val('');
                _btn.addClass('grey').attr('disabled','disabled');
            }
        });

        //tab切换
        var _li = $('.banking ul li');
        _li.click(function(){
            var _this = $(this);
            var index = _this.index();
            var boxBanking = $('.box-banking .tab-box');
            if(_this.hasClass('tab-fast-pay')){
                $.get(API_FAST_PAY, function(data) {
                    /*optional stuff to do after success */
                    if(data.bindCardStatus == "unbindCard"){
                        //未绑卡
                        $('.bind-card-layer').find('p').text("您还未绑定银行卡，请您绑定银行卡！");
                        $('.btn-box-layer').find('.now').attr('href',"/bind-card");
                        $('.bind-card-layer').show();
                        //location.href = 'http://localhost:8080/bind-card';
                    }else if(data.bindCardStatus == "commonBindCard"){
                        $('.bind-card-layer').find('p').text("您绑定的银行卡不支持快捷支付，是否换卡？");
                        $('.btn-box-layer').find('.now').attr('href',"/bind-card");
                        $('.bind-card-layer').show();
                        //  普卡绑
                        //location.href = 'http://localhost:8080/bind-card';

                    }else if(data.bindCardStatus == "specialBindCard"){
                        //  7卡未开
                        _this.addClass('active').siblings().removeClass('active');
                        boxBanking.eq(index).find('.recharge-bank').hide();
                        boxBanking.eq(index).find('.jq-open-fast-pay').show();
                        boxBanking.eq(index).show().siblings().hide();
                    }else if(data.bindCardStatus == "openFastPay"){
                        //  开通快捷支付
                        _this.addClass('active').siblings().removeClass('active');
                        boxBanking.eq(index).find('.recharge-bank').show()
                        boxBanking.eq(index).find('.jq-open-fast-pay').hide()
                        boxBanking.eq(index).show().siblings().hide();

                    }
                });

            }else{
                _this.addClass('active').siblings().removeClass('active');
                boxBanking.eq(index).show().siblings().hide();
            }

        });
        $('.close2,.cancel').click(function(){
            $('.bind-card-layer').hide();
        });

        // 充值弹出页面
        $('.ecope-dialog .close').click(function(){
            $('.ecope-overlay,.ecope-dialog').hide();
        });
        //充值提交
        $('.recharge-qr').click(function(){
            $('.ecope-overlay,.ecope-dialog').show();
        });
    });
});