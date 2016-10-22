require(['jquery','imageShowSlide-v1', 'layerWrapper', 'coupon-alert', 'red-envelope-float', 'count_down', 'jquery.validate', 'autoNumeric', 'logintip'],
    function ($,imageShowSlide,layer) {
        var $homePageContainer = $('#homePageContainer'),
            $imgScroll = $('.banner-img-list', $homePageContainer),
            $registerBox = $('.register-ad-box', $homePageContainer),
            $productFrame = $('#productFrame'),
            $bannerImg = $imgScroll.find('li');

        //首页大图轮播
        (function(){
            var imgCount=$imgScroll.find('li').length;
            if(imgCount>0) {
                var runimg=new imageShowSlide('bannerBox','30',imgCount);
                runimg.info();
            }
        })();

        //最新公告
        //(function(){
        //    var box=document.getElementById("noticeList"),
        //        boxLI=box.getElementsByTagName('li'),
        //        LiHeight=40,
        //        scrollTop=box.offsetHeight,timer;
        //     box.innerHTML += box.innerHTML;
        //    var textMove=function() {
        //        var moveStep=2,top=0;
        //        timer=setInterval(function() {
        //            //把ul里的第一个li移到ul的最后一个
        //            if(top==LiHeight-2) {
        //                var first=box.getElementsByTagName('li')[0];
        //                box.appendChild(first);
        //            }
        //            top +=moveStep;
        //            box.style.top= -top + 'px';
        //
        //            if((top==LiHeight)) {
        //                clearInterval(timer);
        //            }
        //        },30);
        //    }
        //
        //    var timer2 = setInterval(textMove, 2000);
        //
        //    box.onmouseover = function() {
        //        clearInterval(timer2);
        //    };
        //    box.onmouseout = function() {
        //        timer2 = setInterval(textMove, 2000);
        //    };
        //
        //})();

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