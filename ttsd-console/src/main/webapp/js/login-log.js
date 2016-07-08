require(['jquery', 'csrf', 'jquery-ui', 'bootstrapSelect', 'bootstrap'], function ($) {

    var $mobile = $('#login-mobile');
    var $selectedYear = $('#selected-year');
    var $selectedMonth = $('#selected-month');
    var $status = $('#status');

    $status.selectpicker();
    $selectedYear.selectpicker();
    $selectedMonth.selectpicker();

    $mobile.autocomplete({
        minLength: 3,
        source: function (query, process) {
            //var matchCount = this.options.items;//返回结果集最大数量
            $.get('/user-manage/mobile/' + query.term + '/search', function (respData) {
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
        alert($mobile.val());
        if ($mobile.val()) {
            params.push("mobile=" + $mobile.val());
        }
        params.push("selectedYear=" + $selectedYear.val());
        params.push("selectedMonth=" + $selectedMonth.val());
        if ($status.val()) {
            params.push("success=" + $status.val());
        }
        window.location.href = "?" + params.join("&");
        return false;
    });



    $('.pagination .previous').click(function () {
        if ($(this).hasClass("disabled")) {
            return false;
        }

        var params = [];
        if ($mobile.val()) {
            params.push("mobile=" + $mobile.val());
        }
        params.push("index=" + (parseInt($('.current-page').data('index')) - 1));
        params.push("selectedYear=" + $selectedYear.val());
        params.push("selectedMonth=" + $selectedMonth.val());
        if ($status.val()) {
            params.push("success=" + $status.val());
        }
        window.location.href = "?" + params.join("&");
        return false;
    });

    $('.pagination .next').click(function () {
        if ($(this).hasClass("disabled")) {
            return false;
        }

        var params = [];
        if ($mobile.val()) {
            params.push("mobile=" + $mobile.val());
        }
        params.push("index=" + (parseInt($('.current-page').data('index')) + 1));
        params.push("selectedYear=" + $selectedYear.val());
        params.push("selectedMonth=" + $selectedMonth.val());
        if ($status.val()) {
            params.push("success=" + $status.val());
        }
        window.location.href = "?" + params.join("&");
        return false;
    });

});