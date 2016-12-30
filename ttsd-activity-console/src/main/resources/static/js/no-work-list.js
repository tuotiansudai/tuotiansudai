require(['jquery', 'underscore', 'jquery-ui', 'bootstrap', 'bootstrapSelect', 'bootstrapDatetimepicker'], function ($, _) {
    $(function () {
        $('.selectpicker').selectpicker();

        $('#activityPrizeExport').click(function () {
            location.href = "/activity-console/activity-manage/export-not-work?" + $('#rewardForm').serialize();
        });
    });
});
