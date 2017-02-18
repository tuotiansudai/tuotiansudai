require(['jquery', 'bootstrap', 'bootstrapDatetimepicker', 'csrf'], function ($) {
    $(function () {
        //confirm event
        $('body').delegate('.confirm-btn', 'click', function (e) {
            e.preventDefault();
            var $self = $(this),
                $parentTd = $self.parents('td'),
                thisId = $self.attr('data-id'),//orderId
                thisProductId = $self.attr('data-product-id');//productId
            if (!confirm("是否确认执行此操作?")) {
                return;
            } else {
                $.ajax({
                    url: '/point-manage/' + thisId + '/consignment',
                    type: 'POST',
                    dataType: 'json'
                })
                    .done(function (res) {
                        if (res.data.status) {
                            $parentTd.html('<label><i class="check-btn add-check"></i><button class="loan_repay already-btn btn-link inactive-btn" data-id="' + thisId + '">已发货</button></label>');
                            location.href="/point-manage/"+thisProductId+"/detail";
                        } else {
                            $tipCom.show().find('.txt').text('操作失败！');
                        }
                    })
                    .fail(function (res) {
                        $self.addClass('confirm-btn').text('操作失败');
                        $tipCom.show().find('.txt').text('请求发送失败，请刷新重试！');
                    });
            }
        });

        $('body').delegate('.btnSend', 'click', function (e) {
            e.preventDefault();
            var $self = $(this),
                thisId = $self.attr('data-id');//data id
            if (!confirm("是否确认执行全部发货此操作吗?")) {
                return;
            } else {
                $.ajax({
                        url: '/point-manage/batch/' + thisId + '/consignment',
                        type: 'POST',
                        dataType: 'json'
                    })
                    .done(function (res) {
                        if (res.data.status) {
                            location.href="/point-manage/"+thisId+"/detail";
                        } else {
                            $tipCom.show().find('.txt').text('操作失败！');
                        }
                    })
                    .fail(function (res) {
                        $self.addClass('confirm-btn').text('操作失败');
                        $tipCom.show().find('.txt').text('请求发送失败，请刷新重试！');
                    });
            }
        });


        $('body').delegate('.btnShowAll', 'click', function (e) {
            e.preventDefault();
            var $self = $(this),
                thisId = $self.attr('data-id');//data id
            location.href = "/point-manage/coupon/" + thisId + "/detail";
        });

        $('.export-product').click(function (e) {
            e.preventDefault();
            var $self = $(this),
                $productId = $self.attr('data-pid');
            location.href = "/export/product-order-list?productId=" + $productId;
        });
    });
});