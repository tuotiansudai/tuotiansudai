require(['jquery', 'moment', 'pagination', 'mustache'],
    function($, moment, pagination, Mustache) {
        $(function() {
            var $signBtn=$('#signBtn'),
                $signTip=$('#signLayer'),
                $closeSign=$('#closeSign');


            $signBtn.on('click', function(event) {
                event.preventDefault();
                $signTip.fadeIn('fast', function() {
                    $(this).find('.add-dou').animate({
                        'bottom': '50px',
                        'opacity': '0'
                    }, 'slow');
                });
            });
            $closeSign.on('click', function(event) {
                event.preventDefault();
                $signTip.fadeOut('fast', function() {
                    $(this).find('.add-dou').css({
                        'bottom': '0',
                        'opacity': '1'
                    });
                });
            });
        });
    });