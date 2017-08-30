(function ($) {
    var $updatedTime0 = $('#updated_time_0');
    var $updatedTime1 = $('#updated_time_1');

    $updatedTime0.datetimepicker({
        format: 'yyyy-mm-dd',
        showMeridian: 0,
        startView: 2,
        minView:2,
        autoclose: true
    });

    $updatedTime1.datetimepicker({
        format: 'yyyy-mm-dd',
        showMeridian: 0,
        startView: 2,
        minView:2,
        endDate: $updatedTime0.val(),
        autoclose: true
    });

    $updatedTime0.datetimepicker().on('changeDate', function() {
        $updatedTime1.datetimepicker('setStartDate', $updatedTime0.val());
    });

    $updatedTime1.datetimepicker().on('changeDate', function() {
        $updatedTime0.datetimepicker('setEndDate', $updatedTime1.val());
    });

})(jQuery);