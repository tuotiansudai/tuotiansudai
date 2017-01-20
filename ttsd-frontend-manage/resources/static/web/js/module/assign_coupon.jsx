define([], function () {
    if($('#logout-link').length > 0){
        $.ajax({
            url: '/assign-coupon',
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8'
        })
    }
});

