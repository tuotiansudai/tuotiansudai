require(['jquery', 'csrf', 'jquery-ui', 'bootstrapDatetimepicker', 'bootstrap', 'moment'], function ($) {

    var $loginName = $('#login-name');
    var $operatorLoginName = $('#operator-login-name');

    var $startTime = $('#startTime');
    var $endTime = $('#endTime');
    var $startTimeInput = $('input[name="startTime"]');
    var $endTimeInput = $('input[name="endTime"]');

    $startTime.datetimepicker({format: 'YYYY-MM-DD', locale: 'zh-cn'});
    $endTime.datetimepicker({format: 'YYYY-MM-DD', locale: 'zh-cn'});

    //$startTime.on("dp.change", function (e) {
    //    $endTime.data("DateTimePicker").minDate(e.date);
    //});
    //$endTime.on("dp.change", function (e) {
    //    $startTime.data("DateTimePicker").maxDate(e.date);
    //});

    $loginName.autocomplete({
        minLength: 3,
        source: function (query, process) {
            //var matchCount = this.options.items;//返回结果集最大数量
            $.get('/user-manage/user/' + query.term + '/search', function (respData) {
                return process(respData);
            });
        },
        change: function (event, ui) {
            if (!ui.item) {
                this.value = '';
            }
        }
    });

    $operatorLoginName.autocomplete({
        minLength: 3,
        source: function (query, process) {
            //var matchCount = this.options.items;//返回结果集最大数量
            $.get('/user/' + query.term + '/search', function (respData) {
                return process(respData);
            });
        },
        change: function (event, ui) {
            if (!ui.item) {
                this.value = '';
            }
        }
    });

    $('form .query').click(function () {
        var params = [];
        if ($loginName.val()) {
            params.push("loginName=" + $loginName.val());
        }

        if ($operatorLoginName.val()) {
            params.push("operatorLoginName=" + $operatorLoginName.val());
        }

        if ($startTimeInput.val()) {
            params.push("startTime=" + $startTimeInput.val());
        }

        if ($endTimeInput.val()) {
            params.push("endTime=" + $endTimeInput.val());
        }
        window.location.href = "?" + params.join("&");
        return false;
    });

    $('.pagination .previous').click(function () {
        if ($(this).hasClass("disabled")) {
            return false;
        }

        var params = [];
        params.push("index=" + (parseInt($('.current-page').data('index')) - 1));
        if ($loginName.val()) {
            params.push("loginName=" + $loginName.val());
        }

        if ($operatorLoginName.val()) {
            params.push("operatorLoginName=" + $operatorLoginName.val());
        }

        if ($startTimeInput.val()) {
            params.push("startTime=" + $startTimeInput.val());
        }

        if ($endTimeInput.val()) {
            params.push("endTime=" + $endTimeInput.val());
        }

        window.location.href = "?" + params.join("&");
        return false;
    });

    $('.pagination .next').click(function () {
        if ($(this).hasClass("disabled")) {
            return false;
        }

        var params = [];
        params.push("index=" + (parseInt($('.current-page').data('index')) + 1));
        if ($loginName.val()) {
            params.push("loginName=" + $loginName.val());
        }

        if ($operatorLoginName.val()) {
            params.push("operatorLoginName=" + $operatorLoginName.val());
        }

        if ($startTimeInput.val()) {
            params.push("startTime=" + $startTimeInput.val());
        }

        if ($endTimeInput.val()) {
            params.push("endTime=" + $endTimeInput.val());
        }

        window.location.href = "?" + params.join("&");
        return false;
    });

});