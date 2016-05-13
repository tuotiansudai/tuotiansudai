require(['jquery', 'jquery-ui', 'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect', 'moment'], function ($) {
    $(function () {
        $('.selectpicker').selectpicker();

        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});

        //自动完成提示
        var autoValue = '';
        $(".jq-loginName").autocomplete({
            source: function (query, process) {
                $.get('/user-manage/user/' + query.term + '/search', function (respData) {
                    autoValue = respData;
                    return process(respData);
                });
            }
        }).blur(function () {
            for (var i = 0; i < autoValue.length; i++) {
                if ($(this).val() == autoValue[i]) {
                    $(this).removeClass('Validform_error');
                    return false;
                } else {
                    $(this).addClass('Validform_error');
                }
            }
        });
    });

    $('.feedback-status').on('click',function(e){
        e.preventDefault();
        var $self=$(this),
            id=$self.attr('data-id'),
            status=$self.prop('checked');

        $.ajax({
            url: "/announce-manage/updateStatus",
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8',
            data: {
                feedbackId:id,
                status:status
            }
        }).done(function (data) {
            console.log("done");
            $self.prop('checked',status);
        }).fail(function () {
            console.log("error");
            alert("提交失败");
        });
    })
});