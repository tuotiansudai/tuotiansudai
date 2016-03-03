require(['jquery', 'moment', 'pagination', 'mustache'],
    function($, moment, pagination, Mustache) {
        $(function() {
            var $navBtn=$('.column-title .title-navli'),
                $signBtn=$('#signBtn'),
                $signTip=$('#signLayer'),
                $closeSign=$('#closeSign'),
                $taskBtn=$('#taskBtn'),
                $taskTip=$('#taskLayer'),
                $closeTask=$('#closeTask');
            //change model
            $navBtn.on('click', function(event) {
                event.preventDefault();
                var $self=$(this),
                    index=$self.index();
                $self.addClass('active').siblings().removeClass('active');
                $('.content-list .choi-beans-list:eq('+index+')').show().siblings().hide();
            });
            //show sign tip
            $signBtn.on('click', function(event) {
                event.preventDefault();
                $signTip.fadeIn('fast', function() {
                    $(this).find('.add-dou').animate({
                        'bottom': '50px',
                        'opacity': '0'
                    }, 800);
                });
            });
            //hide sign tip
            $closeSign.on('click', function(event) {
                event.preventDefault();
                $signTip.fadeOut('fast', function() {
                    $(this).find('.add-dou').css({
                        'bottom': '0',
                        'opacity': '1'
                    });
                });
            });
            //show task tip
            $taskBtn.on('click', function(event) {
                event.preventDefault();
                $taskTip.fadeIn('fast');
            });
            //hide task tip
            $closeTask.on('click', function(event) {
                event.preventDefault();
                $taskTip.fadeOut('fast');
            });
        });
    });