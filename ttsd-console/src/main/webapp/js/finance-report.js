require(['jquery', 'template', 'csrf', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect'], function ($) {
    $(function () {
        var $selectDom = $('.selectpicker');

        //渲染select表单
        $selectDom.selectpicker();

        $('#datetimepickerStartTime').datetimepicker({format: 'YYYY-MM-DD HH:mm'});
        $('#datetimepickerEndTime').datetimepicker({format: 'YYYY-MM-DD HH:mm'});

        $('.search').click(function () {
            $('.query-form').submit();
        });

        $('.down-load').click(function () {
            location.href = "/finance-manage/financeReport/export?" + $('form').serialize();
        });
    });
});