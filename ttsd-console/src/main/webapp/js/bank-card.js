require(['jquery', 'jquery-ui', 'layer', 'layer-extend', 'layerWrapper', 'bootstrapDatetimepicker', 'bootstrapSelect', 'moment'], function ($) {
    $(function () {

        $('.tooltip-list').on('mouseover', function () {
            var that = this,
                tiptext = $(this).attr('data-original-title');
            layer.tips(tiptext, that, {
                tips: [4, '#000000']
            });
        });


        $('.replace-remark').on('click', function (e) {
            e.preventDefault();
            var $self = $(this),
                feedbackId = $self.attr('data-replace-id');
            //向模态框中传值
            $('#replaceId').val(feedbackId);
            $('#update').modal('show');
        });
    });
})


//提交备注
function update() {
    //获取模态框数据
    var replaceId = $('#replaceId').val();
    var remark = $('#remark').val();

    if (remark == '' || remark.trim() == '') {
        alert('备注不能为空');
        return false;
    }

    $.ajax({
        type: "GET",
        url: "/user-manage/update-remark",
        data: {
            bankCardId: replaceId,
            remark: remark
        },
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8'
    }).done(function (data) {
        location.href = "/user-manage/bind-card";
    }).fail(function () {
        alert("提交备注失败");
    });

    $('#update').modal('hide');
}