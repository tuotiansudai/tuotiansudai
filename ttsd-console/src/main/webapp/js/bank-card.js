require(['jquery', 'bootstrap','Validform','Validform_Datatype','jquery-ui','csrf'], function ($) {
    $(function () {

        $('.feedback-remark').on('click', function (e) {
            e.preventDefault();
            var $self = $(this),
                feedbackId = $self.attr('data-feedback-id');
            //向模态框中传值
            $('#feedbackId').val(feedbackId);
            $('#update').modal('show');
        });


    });
})


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
        url: "/user-manage/update-remark",
        data: {
            bankCardId: feedbackId,
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