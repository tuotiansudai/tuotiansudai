require(['jquery', 'csrf'], function ($) {
    $(function () {
        // 提现金额保留小数点后2位
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
        //提现提交
        $('.recharge-qr').click(function(){
            $('.ecope-overlay,.ecope-dialog').show();
        });
    });
});