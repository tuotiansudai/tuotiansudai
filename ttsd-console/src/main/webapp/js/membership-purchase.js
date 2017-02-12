require(['jquery', 'underscore', 'jquery-ui', 'bootstrap', 'bootstrapSelect', 'bootstrapDatetimepicker'], function ($, _) {
    $(function () {
        var mobileElement = $('#mobile');

        $('#datetimepickerStartTime').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepickerEndTime').datetimepicker({format: 'YYYY-MM-DD'});

        $('form select.selectpicker').selectpicker();

        $('form button[type="reset"]').click(function () {
            location.href = "/membership-manage/membership-privilege-purchase";
        });

        //自动完成提示
        mobileElement.autocomplete({
            source: function (query, process) {
                //var matchCount = this.options.items;//返回结果集最大数量
                $.get('/user-manage/mobile/' + query.term + '/search', function (respData) {
                    return process(respData);
                });
            }
        });
    });
});
