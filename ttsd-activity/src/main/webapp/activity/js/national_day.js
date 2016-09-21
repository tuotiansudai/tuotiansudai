require(['jquery', 'underscore', 'layerWrapper','commonFun','register_common','circle'], function ($, _, layer) {

    var $nationalDayFrame=$('#nationalDayFrame'),
        $tourSlide=$('#tourSlide');
    var browser = commonFun.browserRedirect();
    if (browser == 'mobile') {
        var urlObj=commonFun.parseURL(location.href);
        if(urlObj.params.tag=='yes') {
            $nationalDayFrame.find('.reg-tag-current').show();
        }
    }
    else {
        fixMenuJump();
        $(window).on('scroll',function() {
            fixMenuJump();
        });
    }

    function fixMenuJump() {
        var scrollTop=$(window).scrollTop();
        if(scrollTop>=580) {
            $tourSlide.addClass('fixed-menu');
            $('.seizeSeat',$nationalDayFrame).show();
        }
        else {
            $tourSlide.removeClass('fixed-menu');
            $('.seizeSeat',$nationalDayFrame).hide();
        }
    }

    $('.button-area',$tourSlide).find('li').on('click',function(event) {
        var $this=$(this),
            num=$this.index();
        $tourSlide.addClass('fixed-menu');
        $('.seizeSeat',$nationalDayFrame).show();
        var contentOffset = $('.section-outer',$nationalDayFrame).eq(num).offset().top;
        if(num==0) {
            $(window).scrollTop(600);
        }
        else {
            $(window).scrollTop(contentOffset-137);
        }

    });


});

