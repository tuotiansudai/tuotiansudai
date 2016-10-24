define(['jquery'], function ($) {

    function assignUserCoupon()
    {
        if($('#logout-link').length > 0){
            $.ajax({
                url: '/assign-coupon',
                type: 'Post',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8'
            })
        }
    }

    assignUserCoupon();
});

