require(['jquery', 'csrf'], function ($) {
    $(function () {
        // 提现金额保留小数点后2位
        var rep = /^\d+$/;
        var rep_point = /^([0-9]+\.[0-9]{2})[0-9]*$/;
        var rep_point1 = /^[0-9]+\.[0-9]$/;
        $('.recharge-cz').blur(function () {
            var _this = $(this)
            var _btn = _this.closest('form').find('.recharge-qr');
            var text = _this.val();
            var num = text.replace(rep_point, "$1");
            if (rep.test(text)) {
                _this.val(text + '.00');
                _btn.removeClass('grey').removeAttr('disabled');
            } else if (rep_point.test(text)) {
                _this.val(num);
                _btn.removeClass('grey').removeAttr('disabled');
            } else if (rep_point1.test(text)) {
                _this.val(text + '0');
                _btn.removeClass('grey').removeAttr('disabled');
            } else {
                _this.val('');
                _btn.addClass('grey').attr('disabled', 'disabled');
            }
            if ($('.jq-total').text() - _this.val() >= 0 && $('.jq-total').text() > 3) {
                var money = _this.val() - 3.00;
                var s_money = money.toString();
                console.log(s_money + ':' + s_money.indexOf('.'))
                if (s_money.indexOf('.') == -1) {
                    money += ".00";
                }
                $('.jq-sj em').text(money);
                $('.recharge-bank .error').hide();
                $('.recharge-qr').removeClass('grey').removeAttr('disabled');
            } else {
                $('.jq-sj em').text('0.00');
                $('.recharge-bank .error i').text($('.jq-total').text());
                $('.recharge-bank .error').css('display', 'inline-block');
                $('.recharge-qr').addClass('grey').attr('disabled', 'disabled');
            }
        });

    });
});