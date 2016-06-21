require(['jquery', 'template', 'csrf', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect'], function ($) {
    $(function () {
        var $selectDom = $('.selectpicker');

        //渲染select表单
        $selectDom.selectpicker();

        $('#datetimepickerStartTime').datetimepicker({format: 'YYYY-MM-DD HH:mm'});
        $('#datetimepickerEndTime').datetimepicker({format: 'YYYY-MM-DD HH:mm'});

        $('.search').click(function () {
            var loginName = document.getElementById('loginName').value;
            var startTime = document.getElementById('startTime').value;
            var endTime = document.getElementById('endTime').value;
            var mobile = document.getElementById('mobile').value;
            var type = document.getElementById('type').value;
            var levels = [];
            $('.levelCheckbox .level-box').each(function (index, el) {
                $(this).prop('checked') == true ? levels.push($(this).attr('data-id')) : false;
            });

            var searchUrl = '/membership-manage/membership-list?loginName=' + loginName + '&startTime=' + startTime + '&endTime=' + endTime + '&mobile=' + mobile + '&type=' + type + "&levels=" + levels.join(',');
            window.location.href = searchUrl;
        });
    });
});