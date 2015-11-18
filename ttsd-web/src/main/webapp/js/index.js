require(['jquery', 'csrf'], function ($) {
    $(".product-box .pad-m").click(function() {
        window.location.href = $(this).data("url");
    });
});