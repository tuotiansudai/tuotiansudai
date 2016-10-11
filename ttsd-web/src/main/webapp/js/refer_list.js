require(['jquery', 'mustache', 'text!/tpl/refer-table.mustache', 'text!/tpl/refer-invest-table.mustache', 'moment', 'pagination', 'layerWrapper', 'daterangepicker', 'jquery.ajax.extension','copyclip'],
    function ($, Mustache, referRelationTemplate, referInvestTemplate, moment, pagination, layer) {

        var $investListContent=$('#investListContent'),
            $copyButton=$('.copy-button',$investListContent);
        var $searchBox=$('#search-box'),
            dataPickerElement = $('#date-picker'),
            loginName = $("#loginName"),
            paginationElementRelation = $('#referRelationPagination'),
            paginationElementInvest = $('#referInvestPagination'),
            $btnSearch=$('.btn-search',$searchBox),
            $btnReset=$('.btn-reset',$searchBox),
            $searchContent=$('.search-content-tab');

        /*复制链接*/
        var client = new ZeroClipboard($copyButton);
        client.on( "ready", function( readyEvent ) {
            client.on( "aftercopy", function( event ) {
                layer.msg('复制成功');
            } );
        } );


        var paginationElement = paginationElementRelation;
        var template = referRelationTemplate;

        var today = moment().format('YYYY-MM-DD');
        dataPickerElement.dateRangePicker({separator: ' ~ '});

        $(".search-type .select-item").click(function () {
            $(this).addClass("current").siblings(".select-item").removeClass("current");

            if ($(this).data('type') == 'referRelation') {
                paginationElementRelation.show();
                paginationElementInvest.hide();
                paginationElement = paginationElementRelation;
                template = referRelationTemplate;
            } else if ($(this).data('type') == 'referInvest') {
                paginationElementRelation.hide();
                paginationElementInvest.show();
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
                    url: 'total-reward?' + queryParams,
                    type: 'get',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).success(function (response) {
                    data.totalReward = response;
                    var html = Mustache.render(template, data);
                    $searchContent.empty().append(html);

                });
            });
            $('.search-content-tab').on('mouseenter','span.loan-name-col',function() {
                layer.closeAll('tips');
                if($(this).text().length>13){
                    layer.tips($(this).text(), $(this), {
                        tips: [1, '#efbf5c'],
                        time: 2000,
                        tipsMore: true,
                        area: 'auto',
                        maxWidth: '500'
                    });
                }
            });
        };

        loadReferData();

        $btnSearch.click(function () {
            loadReferData();
        });

        $btnReset.click(function () {
            dataPickerElement.val('');
            loginName.val('');
        });
    });
