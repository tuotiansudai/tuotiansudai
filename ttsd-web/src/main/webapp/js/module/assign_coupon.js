define(['jquery'], function ($) {

    function assignUserCoupon()
    {
        $.ajax({
            url: '/isLogin',
            //data:data,
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8'
        }).fail(function (response) {
            if (response.responseText == "") {
                $.ajax({
                    url: '/assign-coupon',
                    type: 'Post',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                })
            }
        });
    }

    assignUserCoupon();
});

