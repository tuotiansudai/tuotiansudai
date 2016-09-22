require(['jquery', 'underscore', 'template','layerWrapper','drawCircle','commonFun','register_common','circle'], function ($, _,tpl,layer,drawCircle) {

    var $nationalDayFrame=$('#nationalDayFrame'),
        $tourSlide=$('#tourSlide'),
        $nationalDayCircle=$('#nationalDayCircle'),
        $allInvestAmount=$nationalDayFrame.find('.invest-percent-box em.total-invest');
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

    var allInvestAmount=$.trim($allInvestAmount.text());

    function addSeparator(str){
        var re = /(?=(?!\b)(\d{3})+$)/g;
        return str.replace(re, ',');
    }
    //给数字加上逗号分隔
    var allAmountInteger=allInvestAmount.split('.')[0];
    if(allInvestAmount.split('.')[1]) {
        var allAmountDecimal='.'+allInvestAmount.split('.')[1];
    }
    else {
        allAmountDecimal='';
    }

    var realInvestAmount=addSeparator(allAmountInteger);
    $allInvestAmount.text(realInvestAmount+allAmountDecimal);

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

    //以下为抽奖转盘
    var $pointer = $('#pointer');
    var $MobileNumber=$('#MobileNumber'),
        travelAllList='/activity/national/all-list',  //中奖记录接口地址
        travelUserList='/activity/national/user-list',   //我的奖品接口地址
        drawURL='/activity/national/draw',    //抽奖的接口链接
        myMobileNumber=$MobileNumber.length ? $MobileNumber.data('mobile') : '';  //当前登录用户的手机号

    var drawCircle=new drawCircle(travelAllList,travelUserList,drawURL,myMobileNumber);

    //渲染中奖记录
    drawCircle.GiftRecord();

    //渲染我的奖品
    drawCircle.MyGift(travelUserList,myMobileNumber);

    //开始抽奖
    $pointer.on('click', function(event) {
        drawCircle.beginLotteryDraw(function(data) {
            //抽奖接口成功后奖品指向位置
            if (data.returnCode == 0) {
                switch (data.prize) {
                    case 'MANGO_CARD_100':
                        drawCircle.rotateFn(347, '100元芒果卡',data.prizeType);
                        break;
                    case 'RED_INVEST_15':
                        drawCircle.rotateFn(30, '15元投资红包',data.prizeType);
                        break;
                    case 'RED_INVEST_50':
                        drawCircle.rotateFn(120, '50元投资红包',data.prizeType);
                        break;
                    case 'TELEPHONE_FARE_10':
                        drawCircle.rotateFn(265, '10元话费',data.prizeType);
                        break;
                    case 'IQIYI_MEMBERSHIP':
                        drawCircle.rotateFn(80, '1个月爱奇艺会员',data.prizeType);
                        break;
                    case 'CINEMA_TICKET':
                        drawCircle.rotateFn(310, '电影票一张',data.prizeType);
                        break;
                    case 'FLOWER_CUP':
                        drawCircle.rotateFn(170, '青花瓷杯子',data.prizeType);
                        break;
                    case 'MEMBERSHIP_V5':
                        drawCircle.rotateFn(347, '1个月V5会员',data.prizeType);
                        break;
                }
            } else if (data.returnCode == 2) {
                $('#tipList').html(tpl('tipListTpl', {tiptext:data.message,istype:'nologin'})).show().find('.tip-dom').show();
            } else if(data.returnCode == 3){
                $('#tipList').html(tpl('tipListTpl', {tiptext:data.message,istype:'timeout'})).show().find('.tip-dom').show();
            } else {
                $('#tipList').html(tpl('tipListTpl', {tiptext:data.message,istype:'notimes'})).show().find('.tip-dom').show();
            }
        });
    });



});

