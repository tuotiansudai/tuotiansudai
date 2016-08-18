require(['jquery', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'moment', 'layerWrapper'], function ($) {
    $('.selectpicker').selectpicker();

    $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
    $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});
    $('form button[type="reset"]').click(function () {
        location.href = "/activity-manage/user-lottery-list";
    });

    $('#lotteryTimeBtn').click(function(){
        window.location.href = "/activity-manage/user-lottery-list?"+$('#lotteryTimeForm').serialize()+"&export=csv";
    });
});
