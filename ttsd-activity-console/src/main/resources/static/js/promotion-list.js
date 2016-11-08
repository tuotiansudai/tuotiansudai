require(['jquery', 'Validform', 'bootstrap','jquery-ui', 'csrf'], function ($) {
    $(function () {
        var $body = $('body'),
            $tipCom = $('.tip-container');

        $('.promotion-delete,.promotion-rejection,.promotion-approved').on('click', function () {
            var $self = $(this),
                thisLink = $self.attr('data-link'),
                thisOperator = $self.attr('data-operator');
            if (!confirm("是否确认执行此操作吗?")) {
                return;
            } else {
                $.ajax({
                        url: thisLink + "/" + thisOperator,
                        type: 'post',
                        dataType: 'json',
                        contentType: 'application/json; charset=UTF-8'
                    })
                    .done(function (res) {
                        if (res.data.status) {
                            $self.closest('tr').remove();
                            window.location.reload();
                        } else {
                            $tipCom.show().find('.txt').text('操作失败！');
                        }
                    })
                    .fail(function (res) {
                        $tipCom.show().find('.txt').text('请求发送失败，请刷新重试！');
                    });
            }
        });

    });
})