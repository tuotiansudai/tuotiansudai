require(['jquery','fullPage','commonFun'], function ($) {

    var $assuranceEffect=$('#assuranceEffect'),
        locationSearch=location.search,
        locationId,
        $boxProfit=$assuranceEffect.find('.box-benift'),
        $boxWealth=$assuranceEffect.find('.box-wealth'),
        $boxInsurance=$assuranceEffect.find('.box-insurance'),
        boolHead=$('.header-container').is(':visible'),
        viewport=commonFun.browserRedirect();
    if(!!location.search) {
        locationId=Number(/\d/.exec(location.search)[0]);
    }
        $assuranceEffect.find('.section').eq(locationId-1).addClass('active').siblings('.section').removeClass('active');
    
    function resize(){
        $('.intro-list .hover-text').height($('.intro-list .picture-text').height());
    }
    $(window).resize(resize);
    resize();

    if(viewport=='pc') {
        $('.intro-list .hover-text').height('110px');
    } else if(viewport=='mobile') {
        $('.intro-list .hover-text').height($('.intro-list .picture-text').height());
    }
    $assuranceEffect.fullpage({
        sectionsColor: ['#eaf2ff', '#c5dbff', '#eaf2ff'],
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