require(['jquery', 'underscore', 'layerWrapper','superslide','jquery.ajax.extension', 'commonFun', 'coupon-alert', 'red-envelope-float', 'count_down','jquery.validate','autoNumeric'], function ($, _,layer) {
    $(function () {
        var $bannerBox = $('.banner-box'),
            $imgScroll = $('.banner-img-list', $bannerBox),
            $registerBox = $('.register-ad-box', $bannerBox),
            $scrollNum = $('.scroll-num', $bannerBox),
            $productFrame = $('#productFrame'),
            $dlAmount = $('.dl-amount', $productFrame),
            $imgNum = $('li', $scrollNum),
            $bannerImg = $imgScroll.find('li'),
            screenWid, picWid, leftWid, adTimer = null,
            n = 0;

        var $bookInvestForm=$('.book-invest-form'),
            $projectTransfer=$('.project-transfer-frame');

        $dlAmount.find('i').filter(function (index) {
            var value = $(this).text(),
                valueAmount = value.replace(/[^\d|.]*/g, '');
            return valueAmount.length > 5;
        }).css({
            'font-size': '16px'
        });
        screenWid = $(window).width(); //screen width
        picWid = $bannerImg.first().find('img').width();
        leftWid = (picWid - screenWid) / 2;
        $imgScroll.find('img').css({
            'margin-left': '-' + leftWid + 'px'
        });
        $(".product-box .pad-m").click(function () {
            window.location.href = $(this).data("url");
        });

        var viewport = commonFun.browserRedirect();

        if (viewport == 'pc') {
            $imgScroll.find('img.iphone-img').remove();
            $("#bannerBox").slide({mainCell:".bd ul",effect:"leftLoop",autoPlay:true});
        } else if (viewport == 'mobile') {
            $imgScroll.find('img.pc-img').remove();
            $scrollNum.css({'left': (screenWid - $scrollNum.find('li').length * 25) / 2, 'visibility': 'visible'});
            $imgNum.click(function () {
                var num_nav = $imgNum.index(this);
                $(this).addClass("on").siblings().removeClass("on");
                $bannerImg.eq(num_nav).fadeIn(500).siblings().fadeOut(500);
            });
            $bannerBox.hover(function () {
                clearInterval(adTimer);
            }, function () {
                adTimer = setInterval(function () {
                    var index = ++n % $bannerImg.length;
                    $imgNum.eq(index).trigger('click');
                }, 6000);
            }).trigger('mouseleave');
            // 移动端滑动切换
            (function() {
                var startX, startY;
                $bannerBox.on('touchstart', function(event) {
                    startX = event.originalEvent.changedTouches[0].pageX;
                    startY = event.originalEvent.changedTouches[0].pageY;
                });
                $bannerBox.on('touchmove', function(event) {
                    event.preventDefault();
                })
                $bannerBox.on('touchend', function(event) {
                    var endX, endY;
                    endX = event.originalEvent.changedTouches[0].pageX;
                    endY = event.originalEvent.changedTouches[0].pageY;
                    var direction = GetSlideDirection(startX, startY, endX, endY);
                    switch(direction) {
                        case 3:
                            var index = ++n % $bannerImg.length;
                            $imgNum.eq(index).trigger('click');
                            break;
                        case 4:
                            var index = --n % $bannerImg.length;
                            $imgNum.eq(index).trigger('click');
                            break;
                    }
                });
            })();
        }

        var scrollTimer;
        $(".scroll-top").hover(function () {
            clearInterval(scrollTimer);
        }, function () {
            scrollTimer = setInterval(function () {
                scrollNews($(".scroll-top"));
            }, 2000);
        }).trigger("mouseout");

        function scrollNews(obj) {
            var $self = obj.find("ul:first");
            var lineHeight = $self.find("li:first").height();
            $self.animate({
                "margin-top": -lineHeight + "px"
            }, 600, function () {
                $self.css({
                    "margin-top": "0px"
                }).find("li:first").appendTo($self);
            })
        }

        $('.web-book-box,.book-text-tip').on('click',function(event) {
            event.preventDefault();

            layer.open({
                title: '预约投资',
                type: 1,
                skin: 'book-box-layer',
                area: ['500px'],
                content: $('.book-invest-box')
            });

        })
        $('.loan-btn').on('click', 'li',function(event) {
            event.preventDefault();
            window.location.href=$(this).data('url');
        });

        $('.project-transfer-mobile').on('click','.product-box-tran',function() {
            window.location.href=$(this).data('url');
        })

        $('.new-user-free').on('click', function (event) {
            event.preventDefault();
            window.location.href=$(this).data('url');
            //window.location.href = $(this).attr('data-url');
        });
        $('.mask-btn').on('click', function (event) {
            event.preventDefault();
            $('.new-user-free').removeClass('active');
        });
        $('.guide-btn').on('click', function (event) {
            event.preventDefault();
            $('.product-box-inner').removeClass('active');
        });
        $('input.autoNumeric').autoNumeric('init');
        $bookInvestForm.validate({
            focusInvalid: false,
            errorPlacement: function(error, element) {
                layer.tips(error.text(), element, {
                    tips: [1, '#efbf5c'],
                    time: 3000,
                    tipsMore: true,
                    area: 'auto',
                    maxWidth: '500'
                });
            },
            rules: {
                productType:{
                    required: true
                },
                bookingAmount:{
                    required: true,
                    number:true
                }
            },
            messages: {
                productType: {
                    required: "请选择您希望投资的项目"
                },
                bookingAmount:{
                    required:"请输入预计投资金额",
                    number:"请输入正确的投资金额"
                }

            },
            submitHandler: function (form) {
                var data=$(form).serialize();
                //form.submit();
                $.ajax({
                    url:'/booking-loan/invest?'+data,
                    //data:data,
                    type: 'GET',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                })
                    .done(function (response) {
                        if (response.data.status) {
                            layer.closeAll();
                            layer.msg('<h2>恭喜您预约成功！</h2> 当有可投项目时客服人员会在第一时间与您联系，请您耐心等候并保持电话畅通。', {
                                time: 6000,
                                icon:1,
                                tips: [1, '#efbf5c']
                            });

                        }
                    })
                    .fail(function(response) {
                        layer.alert('接口错误');
                    });
                return false;
            }
        });

        //转让项目
        $.ajax({
            url:'/transfer-list/homePage',
            data:{"index":1,"pageSize":2},
            type:'GET',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8'
        })
            .done(function (response) {
                var transerTpl=$('#transerTpl').html(),
                    transerTplMobile=$('#transerTplMobile').html();

                var render = _.template(transerTpl),
                    renderMobile=_.template(transerTplMobile);
                var data={};
                data.list=response;
                var html = render(data),
                    htmlMobile=renderMobile(data);

                var $element=$projectTransfer.find('.loan-box-inner'),
                    $elementMobile=$('.project-transfer-mobile');
                $element.html(html);
                $elementMobile.html(htmlMobile);

            });

        function cnzzCount() {
            var url = $(this).data('name');
            switch (url) {
                case '/activity/landing-page':
                    cnzzPush.trackClick('83首页', 'Banner模块', 'landingpage');
                    break;
                case '/activity/rank-list':
                    cnzzPush.trackClick('27首页', 'Banner模块', '排行榜');
                    break;
                case '/activity/birth-month':
                    cnzzPush.trackClick('23首页', 'Banner模块', '生日月');
                    break;
                case '/activity/share-reward':
                    cnzzPush.trackClick('74首页', 'Banner模块', '推荐奖励');
                    break;
                case 'http://www.iqiyi.com/w_19rt7ygfmh.html#vfrm=8-8-0-1':
                    cnzzPush.trackClick('25首页', 'Banner模块', '上市');
                    break;
                case '/activity/recruit':
                    cnzzPush.trackClick('26首页', 'Banner模块', '代理');
                    break;
                case '/activity/app-download':
                    cnzzPush.trackClick('83首页', 'Banner模块', 'app');
                    break;

            }
        }

        $('.banner-img-list a').on('click', cnzzCount);
    });


    function GetSlideAngle(dx, dy) {
        return Math.atan2(dy, dx) * 180 / Math.PI;
    }
    //根据起点和终点返回方向 1：向上，2：向下，3：向左，4：向右,0：未滑动
    function GetSlideDirection(startX, startY, endX, endY) {
        var dy = startY - endY;
        var dx = endX - startX;
        var result = 0;
        if(Math.abs(dx) < 2 && Math.abs(dy) < 2) {
            return result;
        }
        var angle = GetSlideAngle(dx, dy);
        if(angle >= -45 && angle < 45) {
            result = 4;
        }else if (angle >= 45 && angle < 135) {
            result = 1;
        }else if (angle >= -135 && angle < -45) {
            result = 2;
        }
        else if ((angle >= 135 && angle <= 180) || (angle >= -180 && angle < -135)) {
            result = 3;
        }
        return result;
    }
});