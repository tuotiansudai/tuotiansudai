require(['jquery','imageShowSlide-v1', 'layerWrapper', 'commonFun','jquery.ajax.extension', 'coupon-alert', 'red-envelope-float',  'jquery.validate', 'autoNumeric', 'logintip','assign_coupon'],

    function ($,imageShowSlide,layer) {
        var $homePageContainer = $('#homePageContainer'),
            $imgScroll = $('.banner-img-list', $homePageContainer);
        var viewport = globalFun.browserRedirect();

        // $.when(commonFun.isUserLogin())
        //     .done(function(){
        //         console.log('已经登陆');
        //     })
        //     .fail(function(){
        //         console.log('未登陆');
        //     });

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
                $imgScroll.find('li').eq(0).css({"z-index":2});
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

        //开标倒计时
        (function() {
            var $preheat=$('.preheat',$homePageContainer);

            function countDownLoan(domElement) {
                return $(domElement).each(function () {
                    var $this = $(this);
                    var countdown=$this.data('time');
                    if(countdown > 0) {
                       var timer= setInterval(function () {
                            var $minuteShow=$this.find('.minute_show'),
                                $secondShow=$this.find('.second_show'),
                                minute=Math.floor(countdown/60),
                                second=countdown%60;
                           if (countdown == 0) {
                               //结束倒计时
                               clearInterval(timer);
                               $this.parents('a').removeClass('preheat-btn').text('立即购买');
                               $this.remove();

                           }
                            minute=(minute <= 9)?('0' + minute):minute;
                            second=(second <= 9)?('0' + second):second;
                            $minuteShow.text(minute);
                            $secondShow.text(second);
                            countdown--;
                        },1000);
                    }

                });
            };
            countDownLoan($preheat);

        })();

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

            //点击我要预约按钮
            $('.book-invest-box',$homePageContainer).on('click',function(event) {
                var $this=$(this);
                // if($this.hasClass('show-login')) {
                //     return;
                // }

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
            });
            // 预约校验
            $bookInvestForm.validate({
                focusInvalid: false,
                errorPlacement: function (error, element) {
                    layer.tips(error.text(), element, {
                        tips: [1, '#efbf5c'],
                        time: 3000,
                        tipsMore: true,
                        area: 'auto',
                        maxWidth: '500'
                    });
                },
                rules: {
                    productType: {
                        required: true
                    },
                    bookingAmount: {
                        required: true,
                        number: true
                    }
                },
                messages: {
                    productType: {
                        required: "请选择您希望投资的项目"
                    },
                    bookingAmount: {
                        required: "请输入预计投资金额",
                        number: "请输入正确的投资金额"
                    }

                },
                submitHandler: function (form) {
                    var amount = $(form).find('input[name="bookingAmount"]').val().replace(/,/gi, '');

                    $(form).find('input[name="bookingAmount"]').val(amount);
                    var data = $(form).serialize();
                    $.ajax({
                        url: '/isLogin',
                        type: 'GET',
                        dataType: 'json',
                        contentType: 'application/json; charset=UTF-8'
                    })
                        .fail(function (response) {
                                if ("" == response.responseText) {
                                    $.ajax({
                                        url: '/booking-loan/invest?' + data,
                                        type: 'GET',
                                        dataType: 'json',
                                        contentType: 'application/json; charset=UTF-8'
                                    })
                                        .done(function (response) {
                                            if (response.data.status) {
                                                layer.closeAll();

                                                layer.open({
                                                    type: 1,
                                                    title: '&nbsp',
                                                    area: ['400px', '185px'],
                                                    content: '<div class="success-info-tip"> <i class="icon-tip"></i> <div class="detail-word"><h2>恭喜您预约成功！</h2> 当有可投项目时，客服人员会在第一时间与您联系，请您耐心等候并保持电话畅通。</div> </div>'
                                                });
                                            }
                                        })
                                        .fail(function (response) {
                                            layer.alert('接口错误');
                                        });
                                    return false;
                                } else {
                                    location.href='/login';
                                }
                            }
                        );
                }
            });

        })();

});