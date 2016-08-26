require(['jquery', 'underscore', 'jquery-ui', 'bootstrap', 'bootstrapSelect', 'bootstrapDatetimepicker'], function ($, _) {
    $('.selectpicker').selectpicker();

    $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
    $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});

    $('#lotteryTimeBtn').click(function(){
        window.location.href = "/activity-console/activity-manage/user-lottery-list?"+$('#lotteryTimeForm').serialize()+"&export=csv";
    });

    $('#prizeBtn').click(function(){
        window.location.href = "/activity-console/activity-manage/user-prize-list?"+$('#prizeFrom').serialize()+"&export=csv";
    });

});
