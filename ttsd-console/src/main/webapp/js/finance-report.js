require(['jquery', 'template', 'csrf', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect'], function ($) {
    $(function () {
        var $selectDom = $('.selectpicker');

        //渲染select表单
        $selectDom.selectpicker();
        $('#datetimepickerStartTime').datetimepicker({format: 'YYYY-MM-DD HH:mm:ss'});
        $('#datetimepickerEndTime').datetimepicker({
            format: 'YYYY-MM-DD HH:mm:ss'
        }).on('dp.change', function(ev){
            if($(this).find('.form-control').val().split(' ')[1]=='00:00:00') {
                $(this).find('.form-control').val($(this).find('.form-control').val().split(' ')[0] + ' 23:59:59')
            }
        });

        $('#repayDatetimepickerStartTime').datetimepicker({format: 'YYYY-MM-DD HH:mm:ss'});
        $('#repayDatetimepickerEndTime').datetimepicker({
            format: 'YYYY-MM-DD HH:mm:ss'
        }).on('dp.change', function(ev){
            if($(this).find('.form-control').val().split(' ')[1]=='00:00:00') {
                $(this).find('.form-control').val($(this).find('.form-control').val().split(' ')[0] + ' 23:59:59')
            }
        });

        $('.search').click(function () {
            $('.query-form').submit();
        });

        $('.down-load').click(function () {
            location.href = "/finance-manage/financeReport/export?" + $('form').serialize();
        });
    });
});