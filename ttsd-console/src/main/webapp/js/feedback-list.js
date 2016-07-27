require(['jquery', 'jquery-ui', 'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect', 'moment'], function ($) {
    $(function () {

        var tooltip = $('.add-tooltip');
        if (tooltip.length){
            tooltip.tooltip();
        }

        $('.selectpicker').selectpicker();

        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});

        //自动完成提示
        var autoValue = '';
        $(".jq-loginName").autocomplete({
            source: function (query, process) {
                $.get('/user-manage/mobile/' + query.term + '/search', function (respData) {
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
    });

    $('.feedback-remark').on('click',function(e){
        e.preventDefault();
        var $self=$(this),
            feedbackId=$self.attr('data-feedback-id');
        //向模态框中传值
        $('#feedbackId').val(feedbackId);
        $('#update').modal('show');
    });

    $('.down-load').click(function () {
        location.href = "/announce-manage/feedback?"+$('form').serialize()+"&export=csv";
    });

    $('.pagination .previous').click(function () {
        if ($(this).hasClass("disabled")) {
            return false;
        }
    });

    $('.pagination .next').click(function () {
        if ($(this).hasClass("disabled")) {
            return false;
        }
    });

});

//提交备注
function update() {
    //获取模态框数据
    var feedbackId = $('#feedbackId').val();
    var remark = $('#remark').val();

    if (remark == '' || remark.trim() == '') {
        alert('备注不能为空');
        return false;
    }

    $.ajax({
        type: "GET",
        url: "/announce-manage/updateRemark",
        data: {
            feedbackId:feedbackId,
            remark:remark
        },
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8'
    }).done(function (data) {
        console.log("done");
        location.href = "/announce-manage/feedback";
    }).fail(function () {
        console.log("error");
        alert("提交备注失败");
    });
    $('#update').modal('hide');
}