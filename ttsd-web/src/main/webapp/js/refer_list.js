require(['jquery', 'mustache', 'text!/tpl/refer-table.mustache', 'text!/tpl/refer-invest-table.mustache', 'moment', 'pagination', 'layer', 'daterangepicker'],
    function ($, Mustache, referRelationTemplate, referInvestTemplate, moment, pagination, layer) {

        var dataPickerElement = $('#date-picker'),
            loginName = $("#loginName"),
            paginationElementRelation = $('#referRelationPagination'),
            paginationElementInvest = $('#referInvestPagination');

        var paginationElement = paginationElementRelation;
        var template = referRelationTemplate;

        var today = moment().format('YYYY-MM-DD'); // 今天
        dataPickerElement.dateRangePicker({separator: ' ~ '}).val(today + '~' + today);

        $(".search-type .select-item").click(function () {
            $(this).addClass("current").siblings(".select-item").removeClass("current");

            if ($(this).data('type') == 'referRelation') {
                paginationElementInvest.display='none';
                paginationElement = paginationElementRelation;
                template = referRelationTemplate;
            } else if ($(this).data('type') == 'referInvest') {
                paginationElementRelation.display='none';
                paginationElement = paginationElementInvest;
                template = referInvestTemplate;
            }
            loadReferData();
        });

        var loadReferData = function (currentPage) {
            var dates = dataPickerElement.val().split('~');
            var startTime = $.trim(dates[0]) || '';
            var endTime = $.trim(dates[1]) || '';
            var loginName = $('#loginName').val();

            var requestData = {startTime: startTime, endTime: endTime, loginName: loginName, index: currentPage || 1};
            var queryParams = '';
            _.each(requestData, function (value, key) {
                queryParams += key + "=" + value + '&';
            });
            paginationElement.loadPagination(requestData, function (data) {
                $.ajax({
                    url: 'total-reward?'+queryParams,
                    type: 'get',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).success(function (response) {
                    data.totalReward=response;
                    var html = Mustache.render(template, data);
                    $('.refer-relation').html(html);
                });
            });
        };

        loadReferData();

        $('.btn-normal').click(function () {
            loadReferData();
        });

        $(".btn").click(function () {
            dataPickerElement.val('');
            loginName.val('');
        });
    });
