define(['jquery'], function ($) {

    function assignUserCoupon()
    {
        $.ajax({
            url: '/user-coupon/assign-user-coupon',
            type: 'Post',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8'
        })
    }

    assignUserCoupon();
});

