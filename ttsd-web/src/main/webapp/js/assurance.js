require(['jquery','fullPage'], function ($) {

    var $assuranceEffect=$('#assuranceEffect'),
        locationSearch=location.search,
        locationId,
        $boxProfit=$assuranceEffect.find('.box-benift'),
        $boxWealth=$assuranceEffect.find('.box-wealth'),
        $boxInsurance=$assuranceEffect.find('.box-insurance'),
        boolHead=$('.header-container').is(':visible');
    if(!!location.search) {
        locationId=Number(/\d/.exec(location.search)[0]);
    }
        $assuranceEffect.find('.section').eq(locationId-1).addClass('active').siblings('.section').removeClass('active');

    $assuranceEffect.fullpage({
        sectionsColor: ['#d9ac52', '#50b281', '#9676d6'],
        navigation: true,
        resize:true,
        scrollingSpeed:600,
        css3: true,
        afterLoad: function(anchorLink, index){
            var $fpNav=$('#fp-nav');

            if(boolHead) {
                if(index == 1){
                    $boxProfit.find('img.fl').animate({
                        'marginLeft': '50px'
                    }, 600);
                }
                if(index == 2){
                    $boxWealth.find('img.page-img').animate({
                        left: '360px'
                    }, 600);
                }
                if(index == 3){
                    $boxInsurance.find('img.page-img').animate({
                        right: '0'
                    }, 600);

                }
            }


                $fpNav.find('li').each(function(key,option) {
                    var $this=$(this);
                   switch(key){
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

        },
        onLeave: function(index, direction){
            if(boolHead) {
                if(index == 1){
                    $boxProfit.find('img.fl').animate({
                        'marginLeft': '-850px'
                    }, 600);
                }
                if(index == 2){
                    $boxWealth.find('img.page-img').animate({
                        left: '-360px'
                    }, 600);
                }
                if(index == 3){
                    $boxInsurance.find('img.page-img').animate({
                        right: '1400px'
                    }, 600);

                }
            }
        }
    });

});