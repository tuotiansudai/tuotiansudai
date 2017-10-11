require(['jquery', 'underscore', 'jquery-ui', 'bootstrap', 'bootstrapSelect', 'bootstrapDatetimepicker', 'csrf'], function ($, layer, _) {
    $('#startTime,#endTime').datetimepicker({
        format: 'YYYY-MM-DD'
    });

    $("#startTime").on("dp.change", function (e) {
        $('#endTime').data("DateTimePicker").minDate(e.date);
    });

    $("#endTime").on("dp.change", function (e) {
        $('#startTime').data("DateTimePicker").maxDate(e.date);
    });

    $('.count-update').click(function () {
        var self = $(this);
        $('#id').prop('value', self.data('prize-id'));
        $('#prizeTotal').prop('value', self.data('prize-total'));
        $('#prizeSurplus').prop('value', self.data('prize-surplus'));
        $('#updatePrizeCount').modal({});
    });

    $('#submit').on('click', function (e) {
        e.preventDefault();
        var $prizeTotal = $('#prizeTotal').val();
        var $prizeSurplus = $('#prizeSurplus').val();

        if (isNaN($prizeTotal) || isNaN($prizeSurplus) || $prizeTotal=='' || $prizeSurplus==''){
            alert("数量必须为整数");
            return;
        }

        if ($prizeTotal<0 || $prizeSurplus<0){
            alert("数量必须大于0");
            return;
        }

        $.ajax({
            url: '/activity-console/activity-manage/zero-shopping/update-prize-count',
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify({
                'id': $('#id').val(),
                'prizeTotal': $prizeTotal,
                'prizeSurplus': $prizeSurplus
            })
        }).done(function (data) {
            $('#updatePrizeCount').modal('hide');
            location.reload();
        });
    });
});
