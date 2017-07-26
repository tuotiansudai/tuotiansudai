(function ($) {
    $('.to-apply').on('click', function (event) {
        var self = $(this);
        layer.confirm('<p style="text-align:center">请提交审核结果</p>', {
            title: false,
            btn: ['审核驳回', '审核通过'],
            btnAlign: 'c'
        }, function (index, layero) {
            //按钮【审核驳回】的回调
            $.ajax({
                url: '/console/audit-reject-loan/reject/' + self.data('loan-id'),
                type: 'PUT'
            })
                .done(function () {
                    layer.msg("成功驳回");
                    location.reload();
                })
                .fail(function () {
                    layer.msg("驳回失败");
                });
        }, function (index) {
            //按钮【审核通过】的回调
            $.ajax({
                url: '/console/audit-reject-loan/audit/' + self.data('loan-id'),
                type: 'PUT'
            })
                .done(function () {
                    layer.msg("审核通过");
                    location.reload();
                })
                .fail(function () {
                    layer.msg("error");
                });
        });
    })
})(jQuery);