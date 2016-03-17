require(['jquery', 'fullPage'], function($) {

    var $assuranceEffect = $('#assuranceEffect'),
        locationId;
    if (!!location.search) {
        locationId = Number(/\d/.exec(location.search)[0]);
    }
    $assuranceEffect.find('.section').eq(locationId - 1).addClass('active').siblings('.section').removeClass('active');

    $assuranceEffect.fullpage({
        sectionsColor: ['#d9ac52', '#50b281', '#9676d6'],
        navigation: true,
        resize: true,
        scrollingSpeed: 800,
        css3: true,
        afterLoad: function(anchorLink, index) {
            var $fpNav = $('#fp-nav');
            $fpNav.find('li').each(function(key, option) {
                var $this = $(this);
                switch (key) {
                    case 0:
                        $this.find('span').text('益');
                        break;
                    case 1:
                        $this.find('span').text('财');
                        break;
                    case 2:
                        $this.find('span').text('保');
                        break;
                }
            });
        }
    });
});