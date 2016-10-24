require(['jquery','imageShowSlide-v1', 'layerWrapper','commonFun', 'coupon-alert', 'red-envelope-float', 'count_down', 'jquery.validate', 'autoNumeric', 'logintip'],
    function ($,imageShowSlide,layer) {
        var $homePageContainer = $('#homePageContainer'),
            $imgScroll = $('.banner-img-list', $homePageContainer),
            $registerBox = $('.register-ad-box', $homePageContainer),
            $productFrame = $('#productFrame'),
            $bannerImg = $imgScroll.find('li');
        var viewport = commonFun.browserRedirect();
        //首页大图轮播和最新公告滚动
        (function(){
            var imgCount=$imgScroll.find('li').length;
            //如果是手机浏览器，更换手机图片
            if(imgCount>0 && viewport=='mobile') {
                $imgScroll.find('img').each(function(key,option) {
                    var appUrl=option.getAttribute('data-app-img');
                    option.setAttribute('src',appUrl);
                });
            }
            if(imgCount>0) {
                var runimg=new imageShowSlide.runImg('bannerBox','30',imgCount);
                runimg.info();
            }
            var startMarquee=new imageShowSlide.startMarquee();
            startMarquee.init();
        })();

        //点击进入相应的标的详情
        $('[data-url]',$homePageContainer).on('click',function(event) {
            event.preventDefault();
            var $this=$(this),
                url=$this.data('url');
            location.href=url;
        });

        //预约投资
        (function() {
            var $bookInvestForm = $('.book-invest-form',$homePageContainer);
            $('input.autoNumeric',$bookInvestForm).autoNumeric('init');
            //初始化radio
            $bookInvestForm.find('.init-radio-style').on('click', function () {
                var $this = $(this);
                $bookInvestForm.find('.init-radio-style').removeClass('on');
                $bookInvestForm.find('input:radio').prop('checked', false);
                $this.addClass("on");
                $this.find('input:radio').prop('checked', true);
            });

            $('input.autoNumeric',$homePageContainer).autoNumeric();
            $('.book-invest-box',$homePageContainer).on('click',function(event) {
                event.preventDefault();
                $.ajax({
                    url: '/isLogin',
                    type: 'GET',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                })
                    .fail(function (response) {

                        if ("" == response.responseText) {
                            $bookInvestForm.find('.init-radio-style').removeClass('on');
                            $bookInvestForm.find('input[name="bookingAmount"]').val('');
                            layer.open({
                                title: '预约投资',
                                type: 1,
                                skin: 'book-box-layer',
                                area: ['680px'],
                                content: $('.book-invest-frame',$homePageContainer)
                            });
                        } else {
                            $("meta[name='_csrf']").remove();
                            $('head').append($(response.responseText));
                            var token = $("meta[name='_csrf']").attr("content");
                            var header = $("meta[name='_csrf_header']").attr("content");
                            $(document).ajaxSend(function (e, xhr, options) {
                                xhr.setRequestHeader(header, token);
                            });
                            layer.open({
                                type: 1,
                                title: false,
                                closeBtn: 0,
                                area: ['auto', 'auto'],
                                content: $('#loginTip')
                            });
                            $('.image-captcha img').trigger('click');
                        }
                    }
                );
            })
        })();

});