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


        $('.stop-bank-card').on('click', function (e) {
            e.preventDefault();
            var $self = $(this),
                feedbackId = $self.attr('data-replace-id'),
                loginName = $self.attr('data-replace-name'),
                replaceStatus = $self.attr('data-replace-status'),
                activeStatus = $self.attr('data-active-status');

            if (!confirm("确认提交审核?")) {
                return;
            }

            if (replaceStatus == "PASSED" || replaceStatus == "REJECT" || replaceStatus == "FAILED" || replaceStatus == "REMOVED") {
                alert("该状态不可以终止订单!");
                return;
            }

            if (activeStatus == "inRecheck") {
                alert("正在审核中!");
                return;
            }
            $.ajax({
                type: "GET",
                url: "/user-manage/audit-bank-card",
                data: {
                    bankCardId: feedbackId,
                    loginName: loginName
                },
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function (data) {
                alert(data.data.message);
                location.href = "/user-manage/bind-card";
            }).fail(function () {
                alert("终止订单失败");
            });
        });


        $('.audit-bank-card').on('click', function (e) {
            e.preventDefault();
            var $self = $(this),
                feedbackId = $self.attr('data-replace-id'),
                loginName = $self.attr('data-replace-name'),
                replaceStatus = $self.attr('data-replace-status'),
                activeStatus = $self.attr('data-active-status');

            if (!confirm("确认审核?")) {
                return;
            }

            if (replaceStatus == "PASSED" || replaceStatus == "STOP" || replaceStatus == "FAILED" || replaceStatus == "REMOVED") {
                alert("该状态不可以终止订单!");
                return;
            }

            if (activeStatus == "noRecheck") {
                alert("正在审核中!");
                return;
            }

            $.ajax({
                type: "GET",
                url: "/user-manage/audit-bank-card",
                data: {
                    bankCardId: feedbackId,
                    loginName: loginName
                },
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            }).done(function (data) {
                alert(data.data.message);
                location.href = "/user-manage/bind-card";
            }).fail(function () {
                alert("审核失败");
            });

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
