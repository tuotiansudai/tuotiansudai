define(['jquery', 'rotate', 'layerWrapper','template'], function($, rotate, layer,tpl) {
    var $RecordBtn = $('.gift-record li');

    //allListURL： 中奖纪录的接口链接
    //userListURL：我的奖品的接口链接
    //drawURL：抽奖的接口链接
    //mobileNumber：当前登录用户的手机号

    function giftCircleDraw(allListURL,userListURL,drawURL,mobileNumber) {
        this.bRotate=false;
        this.allListURL=allListURL;
        this.userListURL=userListURL;
        this.drawURL=drawURL;
        this.mobileNumber=mobileNumber;
        //开始抽奖
        //url:抽奖接口路径
        //category:活动类型
        //mobileNumber:活动页面获取用户手机号
        this.beginLotteryDraw=function(callback) {
            if (this.bRotate) return;
            $.ajax({
                url: this.drawURL+'?mobile='+this.mobileNumber,
                type: 'POST',
                dataType: 'json'
            })
                .done(function(data) {
                    callback(data);
                })
                .fail(function() {
                    layer.msg('请求失败');
                });
        }
        //中奖记录
        //url:抽奖接口路径
        //category:活动类型

        this.GiftRecord=function() {
            $.ajax({
                url: this.allListURL,
                type: 'GET',
                dataType: 'json'
            })
                .done(function(data) {
                    $('#GiftRecord').html(tpl('GiftRecordTpl', {record:data}));
                });
        }

        //我的奖品
        //url:抽奖接口路径
        //category:活动类型
        //mobileNumber:活动页面获取用户手机号

        this.MyGift=function() {
            $.ajax({
                url: this.userListURL+'?mobile='+mobileNumber,
                type: 'GET',
                dataType: 'json'
            })
                .done(function(data) {
                    $('#MyGift').html(tpl('MyGiftTpl', {gift:data}));
                });
        }

    }

    //转盘旋转
    //angles:奖项对应的角度，
    //text:提示文字
    //urls:中奖记录和我的奖品的接口对象
    giftCircleDraw.prototype.rotateFn=function(angles, txt,type) {
        this.bRotate = !this.bRotate;
        $('#rotate').stopRotate();
        $('#rotate').rotate({
            angle: 0,
            animateTo: angles + 1800,
            duration: 8000,
            callback: function() {
                $('#tipList').html(tpl('tipListTpl', {tiptext:'抽中了'+txt,istype:type})).show().find('.tip-dom').show();
                this.bRotate = !this.bRotate;
                this.GiftRecord();
                this.MyGift();
                $('.lottery-time').each(function(index,el){
                    $(this).text()>1?$(this).text(function(index,num){return parseInt(num)-1}):$(this).text('0');
                });
            }
        })
    }

    //change award record btn
    $RecordBtn.on('click', function(event) {
        var $self = $(this),
            index = $self.index();
        $self.addClass('active').siblings('li').removeClass('active');
        $('#recordList').find('.record-model:eq(' + index + ')').addClass('active')
            .siblings('.record-model').removeClass('active');
    });

    //close btn
    $('body').on('click', '.go-close', function(event) {
        event.preventDefault();
        var $self = $(this),
            $parent = $self.parents('.tip-list'),
            $tipDom = $parent.find('.tip-dom');
        $parent.hide();
        $tipDom.hide();
    });

    //scroll award record list
    var scrollTimer;
    $(".user-record").hover(function() {
        clearInterval(scrollTimer);
    }, function() {
        scrollTimer = setInterval(function() {
            scrollNews($("#recordList"));
        }, 2000);
    }).trigger("mouseout");

    function scrollNews(obj) {
        var $self = obj.find("ul.user-record");
        var lineHeight = $self.find("li:first").height();
        if ($self.find('li').length > 10) {
            $self.animate({
                "margin-top": -lineHeight + "px"
            }, 600, function() {
                $self.css({
                    "margin-top": "0px"
                }).find("li:first").appendTo($self);
            })
        }
    }

    return giftCircleDraw;
});