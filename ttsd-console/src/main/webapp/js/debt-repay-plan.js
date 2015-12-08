require(['jquery', 'bootstrap','bootstrapSelect','bootstrapDatetimepicker','jquery-ui'], function ($) {
    $(function () {
        $('.selectpicker').selectpicker();
    });

    $('.jq-search').click(function() {
        var status = $('.status').val();
        window.location.href = '/finance-manage/debt-repayment-plan?repayStatus='+status;
    });

    $('.jq-search-detail').click(function() {
        var status = $('.status').val();
        var date = $('.date').val();
        window.location.href = '/finance-manage/debt-repayment-detail?date='+date+'&repayStatus='+status;
    });
})