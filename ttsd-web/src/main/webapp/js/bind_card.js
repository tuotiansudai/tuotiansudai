require(['jquery', 'csrf'], function ($) {
    $(function () {
        //select bank
        var _bank = $('.select-bank li');
        _bank.click(function(){
            var text = $(this).find('input').attr('data-name');
            $('.jq-bank').val(text);
        });

        //校验银行卡号
        var reg_bank = /^\d{16,19}$/;
        $('.input-bankcard').blur(function(){
           var _val = $(this).val();
            if(reg_bank.test(_val)){
                $('.btn-ok').removeClass('grey').removeAttr('disabled');
            }else{
                $('.btn-ok').addClass('grey').attr('disabled','disabled');
            }
        });



        // 绑卡弹出页面
        $('.ecope-dialog .close').click(function(){
            $('.ecope-overlay,.ecope-dialog').hide();
        });
        //绑卡提交
        $('.btn-ok').click(function(){
            $('.ecope-overlay,.ecope-dialog').show();
        });
    });
});