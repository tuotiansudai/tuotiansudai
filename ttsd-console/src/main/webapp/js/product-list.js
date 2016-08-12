require(['jquery'], function ($) {

    var $productListCon = $('#productListContainer');

    $('.delete-record', $productListCon).on('click', function () {

        var $this = $(this);
        var productId = $this.parent().find(':hidden').data('id');

        $.ajax({
                url: '/product-manage/delete/' + productId,
                type: 'GET',
            })
            .done(function (data) {
                if (data.status) {
                    window.location.reload();
                }
            });
    });
});