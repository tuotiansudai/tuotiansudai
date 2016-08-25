require(['jquery', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'moment', 'layerWrapper'], function ($) {
    $('.selectpicker').selectpicker();

    $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
    $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});
    $('#timeRestBtn').click(function () {
        location.href = "/activity-manage/user-lottery-list";
    });
    $('#prizeRestBtn').click(function () {
        location.href = "/activity-manage/user-prize-list";
    });

    $('#lotteryTimeBtn').click(function(){
        window.location.href = "/activity-manage/user-lottery-list?"+$('#lotteryTimeForm').serialize()+"&export=csv";
    });

    $('#prizeBtn').click(function(){
        window.location.href = "/activity-manage/user-prize-list?"+$('#prizeFrom').serialize()+"&export=csv";
    });

    $('.btnPrizeRecord').click(function(){
        $("#prizeTimeDiv").show();
        $("#prizeDive").hide();
    });

    $('.btnPrizeTime').click(function(){
        $("#prizeDive").show();
        $("#prizeTimeDiv").hide();
    });
});
