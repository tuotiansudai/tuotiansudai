require(['jquery', 'layerWrapper', 'moment', 'pagination', 'mustache', 'text!/tpl/my-treasure-table.mustache','csrf'],
function($, layer, moment, pagination, Mustache, treasureListTemplate) {
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

        function loadLoanData() {
            $('.pagination').loadPagination({}, function(data) {
                var html = Mustache.render(treasureListTemplate, data);
                $('.invest-list').html(html);
            });
        };

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
        loadLoanData();
    });
});