(function ($) {

    $('#startTime').datetimepicker({
        format: 'yyyy-mm-dd',
        showMeridian: 0,
        startView: 2,
        minView:2,
        autoclose: true
    });
    $('#endTime').datetimepicker({
        format: 'yyyy-mm-dd',
        showMeridian: 0,
        startView: 2,
        minView:2,
        autoclose: true
    });

    $('.redeem-pass').on('click', function (event) {
        var self = $(this);
        $('#pass').modal({});
            $('.pass-no').on('click',function (event) {
                $('#pass').modal('hide');
            });
            $('.pass-true').on('click', function (event) {
                 $.ajax({
                     url : '/audit-redeem/pass/' + self.data('redeem-id'),
                     type : 'PUT'
                 })
                     .done(function (data) {
                         if(data.message == 'success'){
                             layer.msg('审核成功')
                             location.reload();
                         }else{
                             layer.msg('审核失败')
                         }

                     })
                     .fail(function (data) {
                         layer.msg('审核失败')
                     });
            });
    });

    $('.redeem-reject').on('click', function (event) {
        var self = $(this);
        $('#reject').modal({});
            $('.reject-no').on('click',function (event) {
                $('#reject').modal('hide');
            });
            $('.reject-true').on('click', function (event) {
                 $.ajax({
                     url : '/audit-redeem/reject/' + self.data('redeem-id'),
                     type : 'PUT'
                 })
                     .done(function (data) {
                         if(data.message == 'success'){
                             layer.msg('驳回成功')
                             location.reload();
                         }else{
                             layer.msg('驳回失败')
                         }

                     })
                     .fail(function (data) {
                         layer.msg('驳回失败')
                     });
            });
    });
})(jQuery);