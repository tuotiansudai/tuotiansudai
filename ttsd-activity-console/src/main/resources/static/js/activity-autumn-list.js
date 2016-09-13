require(['jquery', 'Validform', 'bootstrap','jquery-ui', 'csrf'], function ($, _) {
    $('.down-load').click(function () {
        location.href = "/activity-console/activity-manage/export-autumn-list";
    });
});
