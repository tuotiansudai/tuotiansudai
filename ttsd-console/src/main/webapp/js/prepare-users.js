require(['jquery', 'bootstrap', 'bootstrapDatetimepicker', 'csrf'], function ($) {

    $('#datetimepicker1,#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});
    var tooltip = $('.add-tooltip');
    if (tooltip.length) {
        tooltip.tooltip();
    }

    $(".search").on("click", function () {
        var formData = $("#formPrepareUsers").serialize(),
            allData = formData + '&index=1&pageSize=10';
        location.href = "/activity-manage/prepare-users?" + allData;
    });
});

