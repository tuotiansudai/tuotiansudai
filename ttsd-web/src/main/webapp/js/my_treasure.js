require(['jquery','layerWrapper', 'moment', 'pagination', 'mustache', 'text!/tpl/investor-invest-table.mustache'],
 function ($, layer, Mustache, moment, pagination, treasureListTemplate) {
    $(function() {
        var $body=$('body'),
            $navLi=$('.column-title .title-navli'),
            $listTab=$('.list-tab');

        $navLi.on('click',function(event) {
            event.preventDefault();
            var $self=$(this),
                index=$self.index();
            $navLi.removeClass('active');
            $self.addClass('active');
            $listTab.removeClass('tab-show');
            $('.list-tab:eq('+index+')').addClass('tab-show');
        });
        $body.on('click','.select-item',function(event) {
            event.preventDefault();
            var $self=$(this),
                dataStatus=$self.attr('data-status');
            $self.addClass('current').siblings('.select-item').removeClass('current');
            templateData(dataStatus);
        });

        $('.invest-list').on('mouseenter','.project-name',function() {
            layer.closeAll('tips');
            if($(this).text().length>15){
                layer.tips($(this).text(), $(this), {
                    tips: [1, '#efbf5c'],
                    time: 2000,
                    tipsMore: true,
                    area: 'auto',
                    maxWidth: '500'
                });
            }
        });

        function templateData(data){
            $.ajax({
                url: '/path/to/file',
                type: 'POST',
                dataType: 'json',
                data: {param1: data}
            })
            .done(function(json) {
                var html = Mustache.render(treasureListTemplate, json);
                $('.invest-list').html(html);
            })
            .fail(function() {
                console.log("error");
            });
        }
        templateData(data);
    });
});
