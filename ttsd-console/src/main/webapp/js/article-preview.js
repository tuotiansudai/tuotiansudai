require(['jquery'], function ($) {
    $(function () {
        if ($('#content').attr('data-id') == '-1') {
            alert('文章不存在!');
        }
    });
});
