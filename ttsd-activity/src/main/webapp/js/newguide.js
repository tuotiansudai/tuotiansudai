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
        sectionsColor: ['#ffffff', '#f7f7f7', '#fffcf1','#fdfdfd', '#fffcf1', '#fdfdfd'],
        navigation: true,
        resize:true,
        scrollingSpeed:600,
        css3: true,
        afterLoad: function(anchorLink, index){
            if(boolHead) {
                if(index == 1){
                    $boxProfit.find('img.fl').animate({
                        'marginLeft': '90px'
                    }, 600);
                }
                if(index == 2){
                    $boxWealth.find('img.page-img').animate({
                        left: '360px'
                    }, 600);
                }
                if(index == 3){
                    $boxInsurance.find('img.page-img').animate({
                        right: '20px'
                    }, 600);

                }
            }
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
                        left: '1260px'
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