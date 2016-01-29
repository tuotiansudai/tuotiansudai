require(['jquery', 'layerWrapper', 'moment', 'pagination', 'mustache', 'text!/tpl/my-treasure-table.mustache',
        'text!/tpl/my-treasure-table-interest.mustache', 'text!/tpl/my-treasure-table-red.mustache', 'csrf', 'coupon-alert'],
function($, layer, moment, pagination, Mustache, treasureListTemplate, treasureListTemplateInterest, treasureListTemplateRedEnvelope) {

    $(function() {
        var $navLi = $('.column-title .title-navli'),
            $listTab = $('.list-tab');

        $navLi.on('click', function(event) {
            event.preventDefault();
            var $self = $(this),
                index = $self.index();
            $navLi.removeClass('active');
            $self.addClass('active');
            $listTab.removeClass('tab-show');
            $('.list-tab:eq(' + index + ')').addClass('tab-show');
        });


        $('.invest-list').on('mouseenter', '.project-name', function() {
            layer.closeAll('tips');
            if ($(this).text().length > 15) {
                layer.tips($(this).text(), $(this), {
                    tips: [1, '#efbf5c'],
                    time: 2000,
                    tipsMore: true,
                    area: 'auto',
                    maxWidth: '500'
                });
            }
        });

        function loadRedEnvelopeData() {
            $('#use-record-red').loadPagination({couponTypeList:['RED_ENVELOPE']}, function(data) {
                var html = Mustache.render(treasureListTemplateRedEnvelope, data);
                $('.invest-list-red').html(html);
            });
        };

        function loadTasteData() {
            $('#use-record-taste').loadPagination({couponTypeList:['NEWBIE_COUPON','INVEST_COUPON']}, function(data) {
                var html = Mustache.render(treasureListTemplate, data);
                $('.invest-list-taste').html(html);
            });
        };

        function loadInterestData() {
            $('#use-record-interest').loadPagination({couponTypeList:['INTEREST_COUPON']}, function(response) {
                if (response.status) {
                    _.each(response.records, function (item) {
                        item.rate = item.rate * 100;
                    });
                }
                var html = Mustache.render(treasureListTemplateInterest, response);
                $('.invest-list-interest').html(html);
            });
        };

        var $selItems = $('.status-filter .select-item'),
            $recordTab = $('.record-tab');

        $selItems.on('click', function(event) {
            event.preventDefault();
            var $self = $(this),
                index = $self.index()-1;
            $selItems.removeClass('current');
            $self.addClass('current');
            $recordTab.removeClass('tab-show');
            $('.record-tab:eq(' + index + ')').addClass('tab-show');
        });

        loadRedEnvelopeData();
        loadTasteData();
        loadInterestData();
    });
});