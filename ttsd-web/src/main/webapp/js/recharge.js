require(['jquery'], function ($) {
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
            var  text = $(this).val();
            var  num = text.replace(rep_point,"$1");
            if(rep.test(text)){
                $('.recharge-cz').val(text+'.00');
                $('.recharge-qr').removeClass('grey').removeAttr('disabled');
            }else if(rep_point.test(text)){
                $('.recharge-cz').val(num);
                $('.recharge-qr').removeClass('grey').removeAttr('disabled');
            }else if(rep_point1.test(text)){
                $('.recharge-cz').val(text+'0');
                $('.recharge-qr').removeClass('grey').removeAttr('disabled');
            }else{
                $('.recharge-cz').val('');
                $('.recharge-qr').addClass('grey').attr('disabled','disabled');
            }
        });

        //tab切换
        var _li = $('.banking ul li');
        _li.click(function(){
            var _this = $(this);
            var index = _this.index();
            var boxBanking = $('.box-banking ol');
            if(index == 1){
                _this.addClass('active').siblings().removeClass('active');
                boxBanking.eq(index).show().siblings().hide();
            }else{
                _this.addClass('active').siblings().removeClass('active');
                boxBanking.eq(index).show().siblings().hide();
            }

        });
    });
})