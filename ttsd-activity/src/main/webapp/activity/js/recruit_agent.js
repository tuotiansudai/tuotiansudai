require(['jquery','fullPage','commonFun'], function ($) {

    var $assuranceEffect=$('#assuranceEffect'),
        viewport=commonFun.browserRedirect();

    function resize(){
        $('.intro-list .hover-text').height($('.intro-list .picture-text').height()-10);
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
        css3: true
    });

});