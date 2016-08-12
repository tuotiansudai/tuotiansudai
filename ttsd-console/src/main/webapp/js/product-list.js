require(['jquery'], function ($) {
    $(function () {
        var $productListCon = $('#productListContainer'),
            $saveBtn = $('#btnSave');

        $productListCon.on('click', '.delete-record', function (event) {
            event.preventDefault();
            var $self = $(this),
                productId = $this.siblings('input').attr('data-id');

            $.ajax({
                    url: '/product-manage/delete/' + productId,
                    type: 'GET',
                    dataType: 'json'
                })
                .done(function (data) {
                    data.status ? window.location.reload() : false;
                })
                .fail(function () {
                    console.log("请求失败");
                });
        });

        $saveBtn.on('click', function (event) {
            event.preventDefault();
            var checkArr = [];
            $('.confirm-btn').each(function () {
                if ($(this).prop('checked') == true && $('.confirm-btn:checked').length > 0) {
                    checkArr.push($(this).attr('data-id'));
                }
            });
            $.ajax({
                    url: '/product-manage/goods-active',
                    type: 'POST',
                    dataType: 'json',
                    data: {
                        productId: checkArr
                    },
                })
                .done(function (data) {
                    data.status ? window.location.reload() : false;
                });
        })
        ;
    });
});